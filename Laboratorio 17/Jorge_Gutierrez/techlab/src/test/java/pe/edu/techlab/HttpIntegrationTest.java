package pe.edu.techlab;
import java.net.URI;
import java.net.http.*;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT, properties={
    "spring.datasource.url=jdbc:h2:mem:techlab;MODE=MySQL;DB_CLOSE_DELAY=-1",
    "spring.datasource.username=sa", "spring.datasource.password=",
    "techlab.user-password=LabUser_test_2026", "techlab.admin-password=LabAdmin_test_2026",
    "techlab.jwt-secret=QUJDREVGR0hJSktMTU5PUFFSU1RVVldYWVoxMjM0NTY=",
    "server.address=127.0.0.1"})
class HttpIntegrationTest {
    @LocalServerPort int port;
    HttpClient client=HttpClient.newHttpClient();
    HttpResponse<String> call(String method,String path,String token,String json) throws Exception {
        var b=HttpRequest.newBuilder(URI.create("http://127.0.0.1:"+port+path));
        if(token!=null) b.header("Authorization","Bearer "+token);
        if(json!=null) b.header("Content-Type","application/json");
        return client.send(b.method(method,json==null?HttpRequest.BodyPublishers.noBody():
            HttpRequest.BodyPublishers.ofString(json)).build(),HttpResponse.BodyHandlers.ofString());
    }
    String token(String name,String pass) throws Exception {
        var r=call("POST","/api/auth/token",null,"{\"username\":\""+name+"\",\"password\":\""+pass+"\"}");
        assertEquals(200,r.statusCode(),r.body());
        var m=Pattern.compile("\"accessToken\"\\s*:\\s*\"([^\"]+)\"").matcher(r.body());
        assertTrue(m.find(),r.body()); return m.group(1);
    }
    @Test void fullCrudAndSecurity() throws Exception {
        assertEquals(401,call("GET","/api/courses",null,null).statusCode());
        assertEquals(401,call("GET","/api/courses","bad.token.value",null).statusCode());
        String user=token("user","LabUser_test_2026"), admin=token("admin","LabAdmin_test_2026");
        String data="{\"name\":\"Java test\",\"price\":12.50,\"categoryId\":1}";
        assertEquals(200,call("GET","/api/courses",user,null).statusCode());
        assertEquals(403,call("POST","/api/courses",user,data).statusCode());
        assertEquals(400,call("POST","/api/courses",admin,"{}").statusCode());
        var created=call("POST","/api/courses",admin,data);
        assertEquals(201,created.statusCode(),created.body());
        String path=created.headers().firstValue("Location").orElseThrow();
        assertEquals(200,call("GET",path,user,null).statusCode());
        assertEquals(200,call("PUT",path,admin,data.replace("Java test","Java edit")).statusCode());
        assertTrue(call("GET",path,user,null).body().contains("Java edit"));
        assertEquals(204,call("DELETE",path,admin,null).statusCode());
        assertEquals(404,call("GET",path,user,null).statusCode());
        assertEquals(302,call("GET","/courses",null,null).statusCode());
    }
}
