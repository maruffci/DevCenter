import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;

public class App extends HttpServlet {

    private String API_KEY = "123456";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String api_key = req.getParameter("api_key");
        if(api_key != null && !api_key.isEmpty()) {
            resp.setHeader("Content-Type", "application/json;charset=utf-8");
            if(api_key.equals(this.API_KEY)) {
                String action = req.getParameter("action");
                if (action != null && !action.isEmpty() && action.equals("create_developer")) {
                    String email = req.getParameter("email");
                    String plang = req.getParameter("plang");
                    String lang = req.getParameter("lang");

                    if ((email != null && !email.isEmpty()) && (plang != null && !plang.isEmpty()) && (lang != null && !lang.isEmpty())) {
                        try {
                            Model m = new Model();
                            String response = m.create_developer(email, plang, lang);
                            resp.getWriter().print(response);
                        } catch (Exception e) {
                            resp.getWriter().print("[{\"status\":\"failed\",\"error\":\"System error While connecting Database\"}]");
                        }
                    } else {
                        resp.getWriter().print("[{\"status\":\"failed\",\"error\":\"Invalid parameter list!\"}]");
                    }
                } else {
                    resp.getWriter().print("[{\"status\":\"failed\",\"error\":\"Invalid URL!\"}]");
                }
            }
            else{
                resp.getWriter().print("[{\"status\":\"failed\",\"error\":\"Invalid API KEY\"}]");
            }
        }
        else{
            resp.getWriter().print("Welcome to Developer Center!");
        }
    }

    public static void main(String[] args) throws Exception{

        int port = (System.getenv("PORT") == null) ? 80 : Integer.valueOf(System.getenv("PORT"));
        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new App()),"/*");
        server.start();
        server.join();
    }
}