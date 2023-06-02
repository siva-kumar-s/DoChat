package Server.Servlet;

import DataBaseServer.ServiceDao;
import Server.constants.ServletConstant;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;

public class RemoveUserServlet extends HttpServlet {
    private static final String CLASSNAME = RemoveUserServlet.class.getName();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        docProcess(req,resp);
    }

    private void docProcess(HttpServletRequest req, HttpServletResponse resp) {
        String  methodName = "docProcess";
        try {
            String mailId = null;
            mailId = req.getParameter("mailId");
            JSONObject responseJson = new JSONObject();
            if (mailId != null) {
                String status = ServiceDao.removeUser(mailId);
                if(status != null){
                    if(status.equals(ServletConstant.INVALID_USER)){
                        resp.setStatus(400);
                        responseJson.put(ServletConstant.MESSAGE, ServletConstant.INVALID_USER);
                    }
                    else {
                        responseJson.put(ServletConstant.MESSAGE, status);
                    }
                }
            } else {
                resp.setStatus(400);
                responseJson.put(ServletConstant.MESSAGE, "User MailId is empty");
            }
            resp.getWriter().write(responseJson.toString());
        } catch (Exception e) {
            System.out.println("Class :: "+CLASSNAME +" Method :: " + methodName);
            e.printStackTrace();
        } finally {
            resp.setContentType("application/json"); //No I18N
            try {
                if (resp.getWriter() != null) {
                    resp.getWriter().close();
                }
            } catch (IOException e) {
                LoggerService.Logger.severe(CLASSNAME,methodName,"Cannot close response output stream",e);
            }
        }
    }
}
