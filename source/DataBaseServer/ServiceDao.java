package DataBaseServer;

import Server.constants.ServiceDaoConstants;
import Server.constants.ServletConstant;

import java.sql.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ServiceDao {
	private static String CLASSNAME = ServiceDao.class.getName();
	private static String username = "postgres";
	private static String password = "1234";
	private static String dbDriver = "org.postgresql.Driver";

	public static Connection getConnection() {
		String methodName = "getConnection";
		Connection con = null;
		try {
			Class.forName(dbDriver);
			con = DriverManager.getConnection(ServiceDaoConstants.DATABASE_URL, username, password);
		} catch (Exception e) {
			LoggerService.Logger.severe(CLASSNAME,methodName,"Can NOT connect to Database",e);
		}
		return con;
	}



	public static String addUser(String name, String mailId) {
		String methodName = "addUser";
		String insertUserQuery = "insert into USERPROFILE.USER (USER_NAME,MAIL_ID,PASSWORD) values (?,?);";
		Connection con = getConnection();
		try {
			PreparedStatement pst = con.prepareStatement(insertUserQuery);
			String checkQuery = "SELECT mailId FROM USERPROFILE.USER WHERE MAIL_ID = '" + mailId + "';";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(checkQuery);
			if (rs.next()) {
				st.close();
				return ServletConstant.MAILID_ALREADY_EXISTS;
			} else {
				pst.setString(1, name);
				pst.setString(2, mailId);
				pst.executeUpdate();
				con.close();
				return ServletConstant.NEW_USER_ADDED_SUCCESSFULLY;
			}
		} catch (Exception e) {
			LoggerService.Logger.severe(CLASSNAME,methodName,"Can not add New User "+username+" to the Chat Table",e);
		}
		return null;
	}

	public  static String LoginUser(String UserMailID, String Password){
		if(isUserMailIdExists(UserMailID)){
			if(isValidUser(UserMailID,Password)){
				return "Secure String cookie of user id";
			}
			else {
				return "password is InValid";
			}
		} else {
			return "USER MAIL IS NOT AVAILABLE";
		}
	}

	public static boolean isValidUser(String userMailID,String Password){
		Connection con = getConnection();
		String checkPasswordQuery = "SELECT COUNT(USER_ID) FROM USERPROFILE.USER WHERE MAIL_ID = '"+userMailID+"' and PASSWORD = '"+Password+"';";
		try {

			Statement statement = con.createStatement();
			statement.execute(checkPasswordQuery);

		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}


	public static boolean isUserMailIdExists(String mailId) {
		Connection con = getConnection();
		String query = "Select USER_ID from USERPROFILE.USER where MAIL_ID ='" + mailId + "';";
		try { 
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	public static int getUserId(String mailId) {
		Connection con = getConnection();
		String query = "Select USER_ID from USERPROFILE.User where MAIL_ID ='"+mailId+"';";
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				int i = rs.getInt(1);
				con.close();
				st.close();
				return i;
			} else {
				return -1;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return 1;
	}

	public static int SendMessage(int senderId, int receiverId, String msg) {
		String query = "insert into MESSAGESCHEMA.MESSAGE (SENDER_ID,RECIVER_ID,MESSAGE_CONTENT) values (?,?,?);";
		Connection con = getConnection();
		try {
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, senderId);
			pst.setInt(2, receiverId);
			pst.setString(3, msg);
			pst.executeUpdate();
			return 1;
		} catch (Exception e) {
			System.out.println(e);
		}
		return 2;
	}

	public static String getUsername(int userid) {

		String query = "Select USER_NAME from USERPROFILE.USER Where USER_NAME = " + userid + ";";
		Connection con = getConnection();

		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				String name  = rs.getString(1);
				con.close();
				st.close();
				return name;
			} else {
				return null;
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		return null;
	}


	public static int getGroupId() {
		String query = "Select GROUP_ID from GROUPSCHEMA.GROUP WHERE ;";
		Connection con = getConnection();

		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				int i = rs.getInt(1);
				con.close();
				st.close();
				return i + 1;
			} else {
				return 1;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return 1;
	}

	public static int createGroup(int gid, String groupName, String adminId) {
		// TODO Auto-generated method stub
		String query = "insert into Grouptable values (?,?,?);";
		String addMemberQuery = "insert into groupmember values(?,?)";
		Connection con = getConnection();
		try {
			PreparedStatement pst = con.prepareStatement(query);
			String checkQuery = "SELECT * FROM Grouptable WHERE name='" + groupName + "';";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(checkQuery);
			PreparedStatement prest = con.prepareStatement(addMemberQuery);
			if (rs.next()) {
				st.close();
				return 2;
			} else {
				pst.setInt(1, gid);
				pst.setString(2, groupName);
				pst.setString(3, adminId);
				prest.setInt(1, gid);
				prest.setString(2, adminId);
				pst.executeUpdate();
				prest.executeUpdate();
				con.close();
				st.close();
				return 1;
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		return 3;
	}

	public static int checkAdmin(int adminId, int groupId) {
		// TODO Auto-generated method stub
		Connection con = getConnection();
		String query = "Select * from grouptable where gid =" + groupId + " And admin_id="+adminId+";";
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				return 1;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return 0;
	}

	public static int addGroupMember(int groupId, int memberId) {
		// TODO Auto-generated method stub
		String query = "insert into	groupmember values (?,?);";
		Connection con = getConnection();
		try {
			PreparedStatement pst = con.prepareStatement(query);
			String checkQuery = "SELECT * FROM groupmember WHERE gid="+groupId+" And memberid=" + memberId + ";";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(checkQuery);
			if (rs.next()) {
				st.close();
				return 2;
			} else {
				pst.setInt(1, groupId);
				pst.setInt(2, memberId);
				pst.executeUpdate();
				con.close();
				return 1;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return 0;
	}

	public static int checkGroup(int groupid) {
		// TODO Auto-generated method stub
		Connection con = getConnection();
		String query = "Select * from grouptable where gid =" + groupid + ";";
		try { 
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				return 1;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return 0;
	}

	public static int SendGroupMsg(int senderid, int groupid, String msg) {
		// TODO Auto-generated method stub
		String query = "insert into groupmessage values (?,?,?,?);";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		Connection con = getConnection();
		try {
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, senderid);
			pst.setInt(2, groupid);
			pst.setString(3, msg);
			pst.setString(4, dtf.format(now));
			pst.executeUpdate();
			return 1;
		} catch (Exception e) {
			System.out.println(e);
		}
		return 0;
	}

	public static int checkGroupMember(int userid,int groupid) {
		// TODO Auto-generated method stub
		Connection con = getConnection();
		String query = "Select * from groupmember where gid =" + groupid + " And memberid="+userid+";";
		try { 
			Statement st = con.createStatement();
			Statement sat = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				return 1;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return 0;
	}

	public static int removeGroupMember(int groupId, int memberId) {
		// TODO Auto-generated method stub
		String query = "delete from	groupmember Where gid="+groupId+" And memberId=+"+memberId+";";
		Connection con = getConnection();
		try {
			PreparedStatement pst = con.prepareStatement(query);
			String checkQuery = "SELECT * FROM groupmember WHERE gid="+groupId+" And memberid=" + memberId + ";";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(checkQuery);
			if (!rs.next()) {
				st.close();
				return 2;
			} else {
				pst.executeUpdate();
				con.close();
				return 1;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return 0;
	}

	public static int deleteGroup(int groupId) {
		// TODO Auto-generated method stub
		String deleteGroupQuery = "Delete from Grouptable Where gid="+groupId+";";
		String deleteGroupMemberQuery = "Delete from groupmember Where gid="+groupId+";";
		String deleteGroupMessageQuery = "Delete from groupmessage Where gid="+groupId+";";
		Connection con = getConnection();
		try {
			String checkQuery = "SELECT * FROM Grouptable WHERE gid=" + groupId + ";";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(checkQuery);
			PreparedStatement pst1 = con.prepareStatement(deleteGroupQuery);
			PreparedStatement pst2 = con.prepareStatement(deleteGroupMemberQuery);
			PreparedStatement pst3 = con.prepareStatement(deleteGroupMessageQuery);
			
			if (!rs.next()) {
				st.close();
				return 2;
			} else {
				pst1.executeUpdate();
				pst2.executeUpdate();
				pst3.executeUpdate();
				con.close();
				st.close();
				return 1;
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		return 0;
	}

	public static int blockUser(int userId, int blockId) {
		// TODO Auto-generated method stub
		String query = "insert into block_list values (?,?);";
		Connection con = getConnection();
		try {
			PreparedStatement pst = con.prepareStatement(query);
			String checkQuery = "SELECT * FROM block_list WHERE userid="+userId+" And blockid="+blockId+";";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(checkQuery);
			if (rs.next()) {
				st.close();
				return 2;
			} else {
				pst.setInt(1, userId);
				pst.setInt(2, blockId);
				pst.executeUpdate();
				con.close();
				return 1;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return 0;
	}

	public static int checkBlockUser(int senderid, int receiverid) {
		// TODO Auto-generated method stub
		Connection con = getConnection();
		String query = "Select * from block_list where userid ="+ senderid +" And blockid="+receiverid+";";
		try { 
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			if (!rs.next()) {
				return 1;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return 0;
	}

	public static String[] autoAssignAdmin(int adminId) {
		// TODO Auto-generated method stub
		Connection con = getConnection();
		String query = "Select * from grouptable where admin_id ="+adminId+";";
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				int groupId = rs.getInt(1);
				int memberId = getMemberId(groupId,adminId);
				String autoAssignAdminQuery ="Update grouptable SET admin_id="+memberId+" where gid="+groupId+";";
				PreparedStatement pst = con.prepareStatement(autoAssignAdminQuery);
				pst.executeUpdate();
				String[] arr = new String[2];
				arr[0] = "1";
				arr[1] = ServiceDao.getGroupName(groupId);
				arr[2] = ServiceDao.getUsername(memberId);
				return arr;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	private static String getGroupName(int groupId) {
		String query = "Select * from grouptable Where gid="+groupId+";";
		Connection con = getConnection();

		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				String name  = rs.getString(2);
				con.close();
				st.close();
				return name;
			} else {
				return null;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	public static int getMemberId(int groupId,int adminId) {
		String query = "Select * from groupmember where gid="+groupId+" And memberid !="+adminId+";";
		Connection con = getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				int i = rs.getInt(2);
				con.close();
				st.close();
				return i;
			} else {
				return 0;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return 0;
		
	}

	public static String removeUser(String mailId) {
		int userId = getUserId(mailId);
		if(userId == -1){
			return ServletConstant.INVALID_USER;
		}
		String deleteUserQuery = "delete from User where mailid="+userId+";";
		String deleteGroupMemberQuery = "delete from groupmember where mailid="+userId+";";
		String deleteBlockedListQuery = "delete from blockedlists where userid="+userId+" OR blockid="+userId+";";
		Connection con = getConnection();
		try {
			PreparedStatement pst = con.prepareStatement(deleteUserQuery);
			String checkQuery = "SELECT * FROM User WHERE id=" + userId + ";";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(checkQuery);
			PreparedStatement pst1 = con.prepareStatement(deleteGroupMemberQuery);
			PreparedStatement pst2 = con.prepareStatement(deleteBlockedListQuery);
			if (!rs.next()) {
				st.close();
				return "user account " + mailId + " is Not Available";
			} else {
				pst.executeUpdate();
				pst1.executeUpdate();
				pst2.executeUpdate();
				con.close();
				return "user account " + mailId + " is removed from DoChat";
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public static int unBlockUser(int userId, int blockId) {
		// TODO Auto-generated method stub
		String query = "delete from block_list where userid="+userId+" And blockid="+blockId+";";
		Connection con = getConnection();
		try {
			PreparedStatement pst = con.prepareStatement(query);
			String checkQuery = "SELECT * FROM block_list WHERE userid="+userId+" And blockid="+blockId+";";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(checkQuery);
			if (!rs.next()) {
				st.close();
				return 2;
			} else {
				pst.executeUpdate();
				con.close();
				return 1;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return 0;
	}

	public static int getId(String mailIdId) {
		String query = "Select id from user where mailId='"+mailIdId+"';";
		Connection con = getConnection();

		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				int i = rs.getInt(1);
				con.close();
				st.close();
				return i;
			} else {
				return 1;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return 1;
	}
}
