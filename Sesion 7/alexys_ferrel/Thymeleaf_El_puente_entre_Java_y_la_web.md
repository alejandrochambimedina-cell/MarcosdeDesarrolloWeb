# THYMELEAF: EL PUENTE ENTRE JAVA Y LA WEB

**Duración aproximada:** 5-7 minutos | **Personajes:** 5

---

### ESCENA 1 - EL PROBLEMA
**Narrador:** En un proyecto web, un desarrollador necesita mostrar información que viene desde Java dentro de una página HTML. Pero aparece un problema: ¿cómo hacemos que nuestros datos del servidor lleguen a la página web?
**Desarrollador Java:** ¡Tengo mis datos en Java! Tengo usuarios, productos y cursos. Pero necesito mostrarlos en una página web.
**HTML:** ¿Y qué quieres que haga yo? Yo puedo mostrar textos, botones, imágenes y formularios, pero no puedo trabajar directamente con tus datos de Java.
**Desarrollador Java:** ¡Exactamente! Necesito alguna herramienta que nos conecte.
**Thymeleaf:** ¡No busquen más! ¡Aquí estoy yo!
**Usuario:** ¿Y tú quién eres?
**Thymeleaf:** Soy Thymeleaf, un motor de plantillas para aplicaciones web desarrolladas principalmente con Java. Mi trabajo es ayudar a generar páginas HTML dinámicas utilizando información que viene desde el servidor.

---

### ESCENA 2 - ¿QUÉ ES THYMELEAF?
**Narrador:** Thymeleaf es un motor de plantillas del lado del servidor. Es muy utilizado junto con frameworks como Spring y Spring Boot.
**Desarrollador Java:** Entonces, ¿puedes tomar los datos que tengo en Java y enviarlos al HTML?
**Thymeleaf:** ¡Exactamente! El servidor procesa la plantilla y coloca los datos correspondientes antes de enviarla al navegador.
**HTML:** ¿Eso significa que seguimos utilizando HTML?
**Thymeleaf:** ¡Claro! Esa es una de mis principales características. Trabajo directamente sobre documentos HTML utilizando atributos especiales como "th:text", "th:if", "th:each" y "th:href".
**Usuario:** Entonces, ¿yo puedo ver el resultado final como una página web normal?
**Thymeleaf:** Sí. Tú recibes una página HTML lista para visualizar en tu navegador.

---

### ESCENA 3 - ¿CÓMO FUNCIONA?
**Desarrollador Java:** A ver si entendí. Yo tengo una aplicación desarrollada con Spring Boot.
**Thymeleaf:** Correcto.
**Desarrollador Java:** Desde un controlador puedo enviar información a una plantilla.
**Thymeleaf:** Correcto nuevamente.
**HTML:** ¿Y yo recibo esa información?
**Thymeleaf:** Sí. Por ejemplo, imagina que Java tiene una variable llamada "nombre" con el valor "Jorge". En el HTML podemos utilizar algo como: `th:text="${nombre}"`
**Usuario:** ¿Y qué vería yo?
**Thymeleaf:** En el navegador simplemente verías: "Jorge"
**Narrador:** De esta manera, Thymeleaf permite combinar la estructura HTML con información dinámica proporcionada por el servidor.

---

### ESCENA 4 - SUS USOS
**Usuario:** ¿Pero solamente sirve para mostrar nombres?
**Thymeleaf:** ¡No! Puedo hacer muchas cosas.
**HTML:** ¡Cuéntanos!
**Thymeleaf:** Puedo utilizarse para:
- Mostrar información dinámica.
- Crear listas y tablas.
- Mostrar u ocultar elementos.
- Crear formularios.
- Generar enlaces dinámicos.
- Mostrar mensajes.
- Trabajar con objetos enviados desde Java.
- Crear páginas web conectadas con una aplicación Spring Boot.
**Desarrollador Java:** Por ejemplo, si tengo una tienda online, puedo recibir una lista de productos desde Java y mostrar cada producto automáticamente.
**Thymeleaf:** ¡Exactamente!
**Usuario:** Entonces podría ver algo como: producto, precio, descripción e imagen, todo generado a partir de los datos del servidor.
**Thymeleaf:** ¡Así es!

---

### ESCENA 5 - VENTAJAS
**Narrador:** Pero Thymeleaf también tiene varias ventajas.
**Desarrollador Java:** Primera ventaja: es fácil de integrar con Spring y Spring Boot.
**HTML:** Segunda: mantiene la estructura basada en HTML, por lo que resulta familiar para quienes conocen desarrollo web.
**Thymeleaf:** Tercera: permite crear contenido dinámico utilizando expresiones y atributos especiales.
**Usuario:** ¿Y tiene alguna ventaja para trabajar en equipo?
**Thymeleaf:** Sí. Al utilizar HTML como base, facilita la colaboración entre desarrolladores y diseñadores.
**Desarrollador Java:** Además, permite trabajar con formularios y datos enviados al servidor.
**Narrador:** Por estas características, Thymeleaf es una alternativa muy utilizada para construir aplicaciones web del lado del servidor.

---

### ESCENA 6 - APLICACIÓN PRÁCTICA
**Usuario:** ¡Quiero ver un ejemplo!
**Desarrollador Java:** Imaginemos una plataforma educativa. Desde Java obtengo una lista de cursos: "HTML y CSS", "Bootstrap" y "Spring Boot".
**Thymeleaf:** Yo puedo tomar esa lista y recorrerla para mostrar cada curso en una página HTML.
**HTML:** Entonces podría aparecer:
- HTML y CSS
- Bootstrap
- Spring Boot
**Usuario:** ¡Y todo eso puede venir desde Java!
**Thymeleaf:** Exactamente.
**Desarrollador Java:** Por eso Thymeleaf funciona como un puente entre la información que maneja el servidor y la interfaz que ve el usuario.

---

### ESCENA 7 - CONCLUSIÓN
**Narrador:** Después de conocer sus características, nuestros personajes han descubierto que Thymeleaf no reemplaza a Java ni a HTML.
**HTML:** Yo me encargo de la estructura de la página.
**Desarrollador Java:** Yo manejo la lógica y los datos de la aplicación.
**Thymeleaf:** Y yo ayudo a combinar esos datos con las plantillas HTML para generar contenido dinámico.
**Usuario:** Entonces, cuando utilizamos Spring Boot con Thymeleaf, podemos construir páginas web dinámicas conectadas con nuestro backend.
**Thymeleaf:** ¡Exactamente!
**Narrador:** Y así, Thymeleaf se convierte en un puente entre el backend desarrollado con Java y las páginas HTML que finalmente utiliza el usuario.
**TODOS:** ¡THYMELEAF: conectando Java con la web!
