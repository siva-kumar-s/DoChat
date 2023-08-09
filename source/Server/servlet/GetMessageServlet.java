package Server.servlet;

import DataBaseServer.ServiceDao;
import LoggerService.Logger;
import Server.constants.ServletConstant;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class GetMessageServlet extends HttpServlet {
    private static final String CLASSNAME = GetMessageServlet.class.getName();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        docProcess(req, resp);
    }

    private void docProcess(HttpServletRequest req, HttpServletResponse resp) {
        String  methodName = "docProcess";
        try {

            String senderID = req.getParameter(ServletConstant.SENDER_ID);
            String receiverID = req.getParameter(ServletConstant.RECEIVER_ID);
            String lastMessageId = req.getParameter(ServletConstant.LASE_MESSAGE_ID);

            JSONObject responseJson = new JSONObject();

            if (senderID != null && receiverID != null && lastMessageId != null) {
                JSONArray messageJsonArray = ServiceDao.getMessages(Long.parseLong(senderID), Long.parseLong(receiverID), lastMessageId);
                if (messageJsonArray == null) {
                    resp.setStatus(400);
                    responseJson.put(ServletConstant.MESSAGE, "unable to get the message");
                } else {
                    responseJson.put(ServletConstant.DATA,messageJsonArray);
                }
            } else {
                resp.setStatus(400);
                responseJson.put(ServletConstant.MESSAGE, "senderId or receiverId or lastMessageId is empty");
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
