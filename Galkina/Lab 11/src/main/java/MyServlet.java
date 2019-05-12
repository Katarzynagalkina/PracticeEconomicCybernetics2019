import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class MyServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        for(int i=1;i<4;i++){
            if(req.getParameter("button" + Integer.toString(i)) != null) {
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/cat"+ Integer.toString(i)+".jsp");
                requestDispatcher.forward(req, resp);
            }
        }
    }
}
