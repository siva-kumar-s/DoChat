package dochat.server.servlet;

import dochat.logger.Logger;
import dochat.database_server.ServiceDao;
import dochat.server.constants.ServletConstant;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;


public class SendMessageServlet extends HttpServlet {

  	private static final String CLASSNAME = SendMessageServlet.class.getName();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        docProcess(req, resp);
    }

    private void docProcess(HttpServletRequest req, HttpServletResponse resp) {
        String  methodName = "docProcess";
        try {

            String senderID = req.getParameter(ServletConstant.SENDER_ID);
            String receiverID = req.getParameter(ServletConstant.RECEIVER_ID);
            String content = req.getParameter(ServletConstant.CONTENT);

            JSONObject responseJson = new JSONObject();

            if (senderID != null && receiverID != null && content != null) {
                String status = ServiceDao.sendMessage(Integer.parseInt(senderID), Integer.parseInt(receiverID), content);
                if (!Objects.equals(status, ServletConstant.MESSAGE_SENT_SUCCESSFULLY)) {
                    resp.setStatus(400);
                }
                responseJson.put(ServletConstant.MESSAGE, status);
            } else {
                resp.setStatus(400);
                responseJson.put(ServletConstant.MESSAGE, "senderId or receiverId or content is empty");
            }

            resp.getWriter().write(responseJson.toString());

        } catch (Exception e) {
            Logger.info(CLASSNAME,methodName,null,e);
        } finally {
            resp.setContentType("application/json"); //No I18N
            try {
                if (resp.getWriter() != null) {
                    resp.getWriter().close();
                }
            } catch (IOException e) {
                Logger.severe(CLASSNAME,methodName,"Cannot close response output stream",e);
            }
        }
    }
}
