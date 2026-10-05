import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Servlet1")
public class Servlet1 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public Servlet1() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
           
            String nombre = request.getParameter("userName");
            String ciudad = request.getParameter("ciudad");
            String textoEdad = request.getParameter("edad");
            String textoEstatura = request.getParameter("estatura");
            String textoNovedades = request.getParameter("novedades");
            String hobby = request.getParameter("hobby");

            
            if (nombre == null || ciudad == null
                    || textoEdad == null || textoEstatura == null
                    || hobby == null) {

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("<h2>Faltan datos obligatorios.</h2>");
                out.print("<a href='index.html'>Volver</a>");
                return;
            }

           
            nombre = nombre.trim();
            ciudad = ciudad.trim();

            
            String patronTexto = "[\\p{L} ]{2,50}";

            if (!nombre.matches(patronTexto)
                    || !ciudad.matches(patronTexto)) {

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("<h2>Nombre y ciudad deben tener entre "
                        + "2 y 50 caracteres, solo letras y espacios.</h2>");
                out.print("<a href='index.html'>Volver</a>");
                return;
            }

          
            int edad = Integer.parseInt(textoEdad);
            double estatura = Double.parseDouble(textoEstatura);
            

            if (edad < 1 || edad > 120) {

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("<h2>La edad debe estar entre 1 y 120.</h2>");
                out.print("<a href='index.html'>Volver</a>");
                return;
            }

           
            if (!Double.isFinite(estatura)
                    || estatura < 0.50 || estatura > 2.50) {

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("<h2>La estatura debe estar entre "
                        + "0.50 y 2.50 metros.</h2>");
                out.print("<a href='index.html'>Volver</a>");
                return;
            }

            
            if (textoNovedades != null
                    && !textoNovedades.equals("true")) {

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("<h2>El valor de novedades no es válido.</h2>");
                out.print("<a href='index.html'>Volver</a>");
                return;
            }

            boolean novedades = "true".equals(textoNovedades);

         
            if (!hobby.equals("LEER")
                    && !hobby.equals("DEPORTE")
                    && !hobby.equals("MUSICA")
                    && !hobby.equals("VIDEOJUEGOS")
                    && !hobby.equals("BAILAR")
                    && !hobby.equals("OTRO")) {

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("<h2>Selecciona un hobby válido.</h2>");
                out.print("<a href='index.html'>Volver</a>");
                return;
            }

          
            Cookie cookieNombre = new Cookie(
                    "uname",
                    URLEncoder.encode(nombre, StandardCharsets.UTF_8));

            Cookie cookieCiudad = new Cookie(
                    "ciudad",
                    URLEncoder.encode(ciudad, StandardCharsets.UTF_8));

            Cookie cookieEdad = new Cookie(
                    "edad", String.valueOf(edad));

            Cookie cookieEstatura = new Cookie(
                    "estatura", String.valueOf(estatura));

            Cookie cookieNovedades = new Cookie(
                    "novedades", String.valueOf(novedades));

            Cookie cookieHobby = new Cookie("hobby", hobby);

       
            Cookie[] cookies = {
                    cookieNombre,
                    cookieCiudad,
                    cookieEdad,
                    cookieEstatura,
                    cookieNovedades,
                    cookieHobby
            };

            String ruta = request.getContextPath();

            if (ruta.isEmpty()) {
                ruta = "/";
            }

            for (Cookie cookie : cookies) {
                cookie.setPath(ruta);
                cookie.setMaxAge(60 * 60); 
                cookie.setHttpOnly(true);

                response.addCookie(cookie);
            }

            
            out.print("<h1>Datos guardados en cookies</h1>");

            out.print("<form action='Servlet2' method='post'>");
            out.print("<input type='submit' value='Ver mis datos'>");
            out.print("</form>");

            out.print("<p><a href='index.html'>Volver al formulario</a></p>");

        } catch (NumberFormatException e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("<h2>La edad debe ser un entero y la estatura "
                    + "un número válido, usando punto decimal.</h2>");
            out.print("<a href='index.html'>Volver</a>");

        } finally {
            out.close();
        }
    }
}



