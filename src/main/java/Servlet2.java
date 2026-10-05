import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Servlet2")
public class Servlet2 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public Servlet2() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
           
            Cookie[] cookies = request.getCookies();

            String nombre = null;
            String ciudad = null;
            String edad = null;
            String estatura = null;
            String novedades = null;
            String hobby = null;

          
            if (cookies != null) {

                for (Cookie cookie : cookies) {

                    switch (cookie.getName()) {

                        case "uname":
                            nombre = URLDecoder.decode(
                                    cookie.getValue(),
                                    StandardCharsets.UTF_8);
                            break;

                        case "ciudad":
                            ciudad = URLDecoder.decode(
                                    cookie.getValue(),
                                    StandardCharsets.UTF_8);
                            break;

                        case "edad":
                            edad = cookie.getValue();
                            break;

                        case "estatura":
                            estatura = cookie.getValue();
                            break;

                        case "novedades":
                            novedades = cookie.getValue();
                            break;

                        case "hobby":
                            hobby = cookie.getValue();
                            break;
                    }
                }
            }

           
            if (nombre == null || ciudad == null || edad == null
                    || estatura == null || novedades == null
                    || hobby == null) {

                out.print("<h2>Faltan cookies o han vencido.</h2>");
                out.print("<a href='index.html'>Completar el formulario</a>");
                return;
            }

        
            out.print("<h1>Datos recuperados de las cookies</h1>");

            out.print("<p><strong>Nombre:</strong> "
                    + escaparHtml(nombre) + "</p>");

            out.print("<p><strong>Ciudad:</strong> "
                    + escaparHtml(ciudad) + "</p>");

            out.print("<p><strong>Edad:</strong> "
                    + escaparHtml(edad) + " años</p>");

            out.print("<p><strong>Estatura:</strong> "
                    + escaparHtml(estatura) + " metros</p>");

            String respuestaNovedades =
                    "true".equals(novedades) ? "Sí" : "No";

            out.print("<p><strong>Recibir novedades:</strong> "
                    + respuestaNovedades + "</p>");

            out.print("<p><strong>Hobby favorito:</strong> "
                    + escaparHtml(hobby) + "</p>");

            out.print("<a href='index.html'>Volver al formulario</a>");

        } catch (IllegalArgumentException e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("<h2>Una cookie tiene una codificación incorrecta.</h2>");
            out.print("<a href='index.html'>Volver al formulario</a>");

        } finally {
            out.close();
        }
    }

    
    private String escaparHtml(String texto) {
        return texto.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
}