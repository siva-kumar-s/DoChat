package DataBaseServer;


import java.sql.Connection;
import java.sql.Statement;


public class DataBaseCreator {
    public static final String CLASSNAME = DataBaseCreator.class.getName();

    public static void main(String[] args) {
        if(createUserTable()){
            System.out.println("user table created successfully");
        }else {
            System.out.println("user table already created.");
        }
        if(createMessageTable()){
            System.out.println("message table created successfully");
        }
        else {
            System.out.println("message table already created.");
        }
        if(createGroupTable()){
            System.out.println("group table created successfully.");
        }
        else {
            System.out.println("group table is already created.");
        }
        if(createBlockedUsersTable()){
            System.out.println("blocked user table is created successfully.");
        }
        else{
            System.out.println("blocked user table is created successfully.");
        }
        if(createGroupMessageTable()){
            System.out.println("group message table is created successfully.");
        }
        else {
            System.out.println("group message table is already created.");
        }
    }
    private static boolean createUserTable()  {
        String methodName = "createUserTable";
        Connection con = ServiceDao.getConnection();
        try {
            String createUserSchemaQuery = "CREATE SCHEMA IF NOT EXISTS USERPROFILE";
            String createChatTableQuery = "CREATE TABLE IF NOT EXISTS USERPROFILE.USER"
                    + "  (USER_ID SERIAL PRIMARY KEY,"
                    + "  USER_NAME VARCHAR(25),"
                    + "  MAIL_ID   VARCHAR(45),"
                    + "  PASSWORD  VARCHAR(15))";
            Statement createUserprofileSchemaStmt = con.createStatement();
            Statement createUserTableStmt = con.createStatement();
            createUserprofileSchemaStmt.execute(createUserSchemaQuery);
            return  createUserTableStmt.execute(createChatTableQuery);

        } catch (Exception e) {
            e.printStackTrace();
            LoggerService.Logger.severe(CLASSNAME,methodName,"Can not Create CHAT USER Table",e);
        }
        return  false;
    }

    private static boolean createMessageTable(){
        String methodName = "createMessageTable";
        Connection con = ServiceDao.getConnection();
        try {
            String createMessageSchema = "CREATE SCHEMA IF NOT EXISTS MESSAGESCHEMA";
            String createMessageTableQuery = "CREATE TABLE IF NOT EXISTS MESSAGESCHEMA.MESSAGE"
                    + "(MESSAGE_ID SERIAL PRIMARY KEY," +
                    "  SENDER_ID INTEGER," +
                    "  RECEIVER_ID INTEGER," +
                    "  MESSAGE_CONTENT VARCHAR(500)," +
                    "  SENT_AT DATE DEFAULT now(),"+
                    "  IS_READ BOOLEAN)";

            Statement createMessageSchemaStmt = con.createStatement();
            Statement createMessageTableStmt = con.createStatement();

            createMessageSchemaStmt.execute(createMessageSchema);
            return createMessageTableStmt.execute(createMessageTableQuery);

        } catch (Exception e) {
             e.printStackTrace();
//            LoggerService.Logger.severe(CLASSNAME,methodName,"Can not Create MESSAGE Table");
        }
        return  false;
    }

    private static boolean createGroupTable(){
        String methodName = "createGroupTable";
        Connection con = ServiceDao.getConnection();
        try {
            String createGroupSchemaQuery = "CREATE SCHEMA IF NOT EXISTS GROUPSCHEMA";
            String createGroupTableQuery = "CREATE TABLE IF NOT EXISTS GROUPSCHEMA.GROUPS"
                    + "(GROUP_ID SERIAL PRIMARY KEY," +
                    "  GROUP_NAME VARCHAR(20) NOT NULL ," +
                    "  GROUP_ADMIN_ID VARCHAR(25) NOT NULL," +
                    "  CREATED_AT DATE DEFAULT now(),"+
                    "  MEMBER_ID INTEGER[] DEFAULT null)";

            Statement createGroupSchemaStmt = con.createStatement();
            Statement createGroupTableQueryStmt = con.createStatement();

            createGroupSchemaStmt.execute(createGroupSchemaQuery);
            return createGroupTableQueryStmt.execute(createGroupTableQuery);

        } catch (Exception e) {
            e.printStackTrace();
//            LoggerService.Logger.severe(CLASSNAME,methodName,"Can not Create MESSAGE Table");
        }
        return  false;
    }

    private static boolean createGroupMessageTable(){
        String methodName = "createGroupMessageTable";
        Connection con = ServiceDao.getConnection();
        try {
            String createGroupSchemaQuery = "CREATE SCHEMA IF NOT EXISTS GROUPSCHEMA";
            String createGroupMessageTable = "CREATE TABLE IF NOT EXISTS GROUPSCHEMA.GROUP_MESSAGE"
                    + "(GROUP_ID SERIAL PRIMARY KEY," +
                    "  SENDER_ID INTEGER NOT NULL ," +
                    "  MESSAGE_CONTENT VARCHAR(500)," +
                    "  SENT_AT DATE DEFAULT now())";

            Statement createMessageSchemaStmt = con.createStatement();
            Statement createGroupMessageTableStmt = con.createStatement();

            createMessageSchemaStmt.execute(createGroupSchemaQuery);
            return createGroupMessageTableStmt.execute(createGroupMessageTable);

        } catch (Exception e) {
            e.printStackTrace();
//            LoggerService.Logger.severe(CLASSNAME,methodName,"Can not Create MESSAGE Table");
        }
        return  false;
    }


    private static boolean createBlockedUsersTable(){
        String methodName = "createBlockedUsersTable";
        Connection con = ServiceDao.getConnection();
        try {
            String createBlockedUserTable = "CREATE TABLE IF NOT EXISTS USERPROFILE.BLOCKED_USERS"
                    + "(BLOCK_ID SERIAL PRIMARY KEY," +
                    "  BLOCKED_USER_ID INTEGER[] NOT NULL," +
                    "  BLOCKED_BY SERIAL NOT NULL," +
                    "  CREATED_AT DATE DEFAULT now())";

            Statement createBlockedUserTableStmt = con.createStatement();
            return createBlockedUserTableStmt.execute(createBlockedUserTable);

        } catch (Exception e) {
            e.printStackTrace();
//            LoggerService.Logger.severe(CLASSNAME,methodName,"Can not Create MESSAGE Table");
        }
        return  false;
    }

}
