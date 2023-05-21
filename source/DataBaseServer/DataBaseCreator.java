package DataBaseServer;


import java.sql.Connection;
import java.sql.Statement;

public class DataBaseCreator {
    public static final String CLASSNAME = DataBaseCreator.class.getName();
    public static void main(String[] args) {
        createUserTable();
        createMessageTable();
    }

    private static boolean createUserTable()  {
        String methodName = "createUserTable";
        Connection con = ServiceDao.getConnection();
        try {
            String createChatTableQuery = "CREATE TABLE IF NOT EXISTS USERPROFILE.USER"
                    + "  (USER_ID SERIAL PRIMARY KEY,"
                    + "  USER_NAME VARCHAR(25),"
                    + "  MAIL_ID   VARCHAR(45),"
                    + "  STATUS VARCHAR(50))";

            Statement createUserTableStmt = con.createStatement();
            return createUserTableStmt.execute(createChatTableQuery);

        } catch (Exception e) {
            LoggerService.Logger.severe(CLASSNAME,methodName,"Can not Create CHAT Table",e);
        }
        return  false;
    }

    private static boolean createMessageTable(){
        String methodName = "createMessageTable";
        Connection con = ServiceDao.getConnection();
        try {
            String createChatTableQuery = "CREATE TABLE IF NOT EXISTS MESSAGE_SCHEMA.MESSAGE"
                    + "(M_ID SERIAL PRIMARY KEY," +
                    "  SENDER_ID INTEGER," +
                    "  RECEIVER_ID INTEGER," +
                    "  CONTENT VARCHAR(500)," +
                    "  IS_READ BOOLEAN)";

            Statement createUserTableStmt = con.createStatement();
            return createUserTableStmt.execute(createChatTableQuery);

        } catch (Exception e) {
            LoggerService.Logger.severe(CLASSNAME,methodName,"Can not Create MESSAGE Table",e);
        }
        return  false;
    }
}
