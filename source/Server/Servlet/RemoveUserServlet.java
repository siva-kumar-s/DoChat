package Server.Servlet;

import DataBaseServer.ServiceDao;
import Server.constants.ServletConstant;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class RemoveUserServlet extends HttpServlet {
    private static final String CLASSNAME = RemoveUserServlet.class.getName();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        docProcess(req,resp);
    }

    private void docProcess(HttpServletRequest req, HttpServletResponse resp) {
        String  methodName = "docProcess";
        try {
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            JSONObject inputJson = null;
            if (buffer.toString().trim().length() > 0) {
                inputJson = new JSONObject(buffer.toString().trim());
            }
            String mailId = null;
            String userName = null;
            if (inputJson != null) {
                if(inputJson.has("userName")){
                    userName = inputJson.getString("userName");
                }
                if(inputJson.has("mailId")) {
                    mailId = inputJson.getString("mailId");
                }
            } else {
                userName = req.getParameter("userName");
                mailId = req.getParameter("mailId");
            }
            JSONObject responseJson = new JSONObject();
            if (userName != null && mailId != null) {
                String status = ServiceDao.addUser(userName, mailId);
                if (Objects.equals(status, "MailId is Already Exists")) {
                    resp.setStatus(400);
                }
                responseJson.put(ServletConstant.MESSAGE, status);
            } else {
                resp.setStatus(400);
                responseJson.put(ServletConstant.MESSAGE, "User userName or MailId is empty");
            }
            resp.getWriter().write(responseJson.toString());
        } catch (Exception e) {
            System.out.println("ClassuserName :: "+CLASSNAME +" MethoduserName :: " + methodName + " Exception ::" + e + Arrays.toString(e.getStackTrace()));
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
