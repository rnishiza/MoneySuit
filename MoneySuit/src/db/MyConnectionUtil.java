package db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entity.User;

public class MyConnectionUtil{
	
	private static Connection conn;
	private final static String DBFILENAME = "db";
	private final static String PATH = System.getProperty("user.dir") + "/h2/";
	private final static String DRIVER = "org.h2.Driver";
	private final static String CONNECTION = "jdbc:h2:"+PATH+DBFILENAME;
	
	private MyConnectionUtil(){}
	
	public static Connection getConnection() throws SQLException{
		if(conn == null || conn.isClosed()) openConnection();
		DBConnection.setConnection(conn);
		return conn;
	}

	private static void openConnection() {
		
		try {
			
			if(conn != null && !conn.isClosed()){
				closeConnection();
			}
			
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(CONNECTION, "sa", "");
			
			if (!exists()) {
				initDB();
	        }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	private static boolean exists(){
		try {
			ResultSet result = executeQuery(conn, "show tables");
			result.last();
			int i = result.getRow();
			if(i > 0) return true;
			return false;
		} catch (SQLException e) {
			return false;
		}
	}
	
	private static void initDB() throws IOException, SQLException{
		File file = new File("resources/db-script.txt");
		FileInputStream fis = new FileInputStream(file);
	    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	    Connection connection = null;
	    try {
	        connection = getConnection();
	        String line = br.readLine();
	        StringBuilder statement = new StringBuilder();
	        while (line != null) {
	            line = line.trim();
	            if (!line.startsWith("--") && !line.startsWith("#") && !line.startsWith("//")) {
	                statement.append(line);
	                if (line.endsWith(";")) {
	                    executeLine(connection, statement.toString());
	                    statement = new StringBuilder();
	                }
	            }
	            line = br.readLine();
	        }
	        if (statement.length() > 0) {
	            executeLine(connection, statement.toString());
	        }
	    } finally {
	        try {
	            br.close();
	        } catch (Exception e) {;}
	        try {
	            if (connection != null) connection.close();
	        } catch (Exception e) {;}
	    }
	}
	
	private static ResultSet executeQuery(Connection connection, String statement) throws SQLException{
		PreparedStatement query = connection.prepareStatement(statement);
		ResultSet result = query.executeQuery();
		return result;
	}

	private static void executeLine(Connection connection, String statement) throws SQLException {
	    PreparedStatement pstmt = connection.prepareStatement(statement);
	    pstmt.execute();
	    pstmt.close();
	    System.out.println("Ejecutando "+statement);
	}

	public static void closeConnection() {
		try{
			if(conn != null && !conn.isClosed()){
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			return;
		}
	}
}
