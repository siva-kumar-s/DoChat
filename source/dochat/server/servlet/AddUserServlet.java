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

public class AddUserServlet extends HttpServlet {

	private static final String CLASSNAME = AddUserServlet.class.getName();

	protected void doPost(HttpServletRequest req, HttpServletResponse resp){
		docProcess(req,resp);
	}

	private void docProcess(HttpServletRequest req, HttpServletResponse resp) {
		String  methodName = "docProcess";
		try {
			String mailId = req.getParameter(ServletConstant.MAILID);
			String userName = req.getParameter(ServletConstant.USER_NAME);
			String password = req.getParameter(ServletConstant.PASSWORD);

			JSONObject responseJson = new JSONObject();
			if (userName != null && mailId != null && password != null) {
				String status = ServiceDao.addUser(userName, mailId, password);
				if (Objects.equals(status, ServletConstant.MAILID_ALREADY_EXISTS)) {
					resp.setStatus(400);
				}
				responseJson.put(ServletConstant.MESSAGE, status);
			} else {
				resp.setStatus(400);
				responseJson.put(ServletConstant.MESSAGE, "User userName or MailId is Invalid");
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
