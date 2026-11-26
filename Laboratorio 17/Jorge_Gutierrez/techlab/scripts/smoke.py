"""Prueba HTTP contra una instancia real. Crea y elimina un curso temporal."""
import os, json, urllib.request, urllib.error, uuid, http.cookiejar
import base64, hmac, hashlib, time
from html.parser import HTMLParser
base=os.getenv('BASE_URL','http://127.0.0.1:8080').rstrip('/')
def call(method,path,data=None,token=None):
    headers={}
    if data is not None: headers['Content-Type']='application/json'
    if token: headers['Authorization']='Bearer '+token
    req=urllib.request.Request(base+path,data=None if data is None else json.dumps(data).encode(),headers=headers,method=method)
    try:
        with urllib.request.urlopen(req,timeout=15) as r: return r.status,r.read(),r.headers
    except urllib.error.HTTPError as e: return e.code,e.read(),e.headers
def expect(code,result):
    assert result[0]==code,(code,result[0],result[1][:500]); return result

def login(name, env):
    r=expect(200,call('POST','/api/auth/token',{'username':name,'password':os.environ[env]}))
    return json.loads(r[1])['accessToken']
expect(401,call('GET','/api/courses'))
expect(401,call('GET','/api/courses',token='bad.token.value'))
expect(401,call('POST','/api/auth/token',{'username':'user','password':'wrong'}))
user=login('user','TECHLAB_USER_PASSWORD'); admin=login('admin','TECHLAB_ADMIN_PASSWORD')
expect(200,call('GET','/api/courses',token=user))
# Generate invalid tokens using only the LOCAL test key. Never log the key or tokens.
def sign_test_token(**changes):
    def b64(raw): return base64.urlsafe_b64encode(raw).rstrip(b'=').decode()
    now=int(time.time())
    claims=dict(iss='techlab',aud=['techlab-api'],sub='user',iat=now,exp=now+900,roles=['USER'])
    claims.update(changes)
    payload='.'.join(b64(json.dumps(part,separators=(',',':')).encode())
                     for part in [{'alg':'HS256','typ':'JWT'},claims])
    key=base64.b64decode(os.environ['TECHLAB_JWT_SECRET'])
    return payload+'.'+b64(hmac.new(key,payload.encode(),hashlib.sha256).digest())
for token in [sign_test_token(exp=int(time.time())-120,iat=int(time.time())-1000),
              sign_test_token(iss='other'),sign_test_token(aud=['other'])]:
    expect(401,call('GET','/api/courses',token=token))
data={'name':'Smoke '+uuid.uuid4().hex,'price':10.50,'categoryId':1}
expect(403,call('POST','/api/courses',data,user))
expect(400,call('POST','/api/courses',{'name':'','price':-1,'categoryId':1},admin))
expect(400,call('POST','/api/courses',dict(data,categoryId=999999),admin))
r=expect(201,call('POST','/api/courses',data,admin)); path=r[2]['Location']
try:
    expect(200,call('GET',path,token=user))
    expect(200,call('PUT',path,dict(data,name='Smoke actualizado'),admin))
    assert json.loads(expect(200,call('GET',path,token=user))[1])['name']=='Smoke actualizado'
finally:
    expect(204,call('DELETE',path,token=admin))
expect(404,call('GET',path,token=user))
# Flujo web con cookies y CSRF, sin reutilizar tokens JWT.
from urllib.parse import urlencode
class Csrf(HTMLParser):
    value=None
    def handle_starttag(self,tag,attrs):
        a=dict(attrs)
        if tag=='input' and a.get('name')=='_csrf': self.value=a.get('value')
opener=urllib.request.build_opener(urllib.request.HTTPCookieProcessor(http.cookiejar.CookieJar()))
def page(path,form=None):
    payload=None if form is None else urlencode(form).encode()
    try:
        with opener.open(base+path,payload,timeout=15) as r: return r.status,r.read().decode()
    except urllib.error.HTTPError as e: return e.code,e.read().decode()
code,html=page('/login'); assert code==200
parser=Csrf(); parser.feed(html); assert parser.value
code,html=page('/login',{'username':'admin','password':os.environ['TECHLAB_ADMIN_PASSWORD'],'_csrf':parser.value})
assert code==200 and 'Catálogo TechLab' in html
assert page('/courses',{'name':'Sin CSRF','price':'1','categoryId':'1'})[0]==403
parser=Csrf(); parser.feed(page('/courses')[1]); assert parser.value
assert page('/logout',{'_csrf':parser.value})[0]==200
assert 'Catálogo TechLab' not in page('/courses')[1]
print('PASS: API CRUD, validación, autenticación, autorización, sesión y CSRF')
