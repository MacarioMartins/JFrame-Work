/*
 * 
 * In this class you may find methods to excute you application sucessfully.
 * If you don't find a method that you want, sorry xD. We'll trying adjust
 * this, or if you've a good idea and guess that is no problem to share with us,
 * please, send you code to us, send a commit in GitHub ;)
 * 
 */

package core.dataManipulation;

import app.config.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Macário Martins <macariomartinsjunior@gmail.com>
 *
 */
public class Database {	
	
	private static Database db = null;
	private boolean dbConfigured = false;
	private Connection connection = null;
	private String user;
	private String password;
	private String database;
	private String driver;
	private String url;	
	private String primaryKey = "id";
	
	private Database() {
		if ( ! dbConfigured)
			configureDatabase();
		
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url + database, user, password);
		}
	
		catch (ClassNotFoundException exception) {
			System.out.println("Exception! Class \"" + driver + "\" not found!");
			System.out.println(exception.getMessage());
			connection = null;
		}
	
		catch (SQLException exception) {
			System.out.println("SQL Exception. Failed to execute SQL query! Connection fail!");
			System.out.println(exception.getMessage());
			connection = null;
		}
	}
	
	private Database(String user, String password, String database, String driver, String url) {
		this.user		= user;
		this.password	= password;
		this.database	= database;
		this.driver		= driver;
		this.url		= url;
		
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url + database, user, password);
		}
	
		catch (ClassNotFoundException exception) {
			System.out.println("Exception! Class \"" + driver + "\" not found!");
			System.out.println(exception.getMessage());
			connection = null;
		}
	
		catch (SQLException exception) {
			System.out.println("SQL Exception. Failed to execute SQL query! Connection fail!");
			System.out.println(exception.getMessage());
			connection = null;
		}
	}
	
	private void configureDatabase() {
		LinkedArray config = DatabaseConfig.getConfig();
		user		= (String) config.get("user");
		password	= (String) config.get("password");
		database	= (String) config.get("database");
		driver		= (String) config.get("driver");
		url			= (String) config.get("url");
		
		dbConfigured = true;
	}
	
	public static Database getConnection() {
		if (db == null) {
			db = new Database();
			if (db.connection == null) {
				db = null;
				return null;
			}
		}
		return db;
	}
	
	public void refresh() {
		db = new Database(user, password, database, driver, url);
	}
	
	public void reloadConfiguration() {
		configureDatabase();
	}
		
	public void setUser(String user) {
		this.user = user;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setDatabase(String database) {
		this.database = database;
	}
	
	public void setDriver(String driver) {
		this.driver	= driver;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	public LinkedArray select(String table, ArrayList<String> fields, String complement) {
		String sql;
		String fieldsList = "";
		
		for (int i = 0; i < fields.size(); i++) {
			fieldsList += fields.get(i);
			fieldsList += (i+1 != fields.size())? ", " : "";
		}
		
		sql = "SELECT " + fieldsList + " FROM " + table + " " + complement;
		
		try {
			Statement statement = connection.createStatement();
			ResultSet results	= statement.executeQuery(sql);
			LinkedArray values	= new LinkedArray();
			int i, j, fieldsNum;
			
			if (fields.size() == 1 && fields.get(0).equals("*")) {
				try {
					ResultSetMetaData meta = results.getMetaData();
					fieldsNum	= meta.getColumnCount();
					fields		= new ArrayList<String>();
					
					for (i = 1; i <= fieldsNum; i++)
						fields.add(meta.getColumnName(i));
				}
				catch (SQLException exception) {
					System.out.println("SQL Exception. Failed to execute SQL query! When using " + table + " table.");
					System.out.println(exception.getMessage());
				}
			}
			
			for (i = 0; results.next(); i++) {
				LinkedArray tmp = new LinkedArray();
				for (j = 0; j < fields.size(); j++) {
					String key = fields.get(j);
					if (key.equals(primaryKey))
						tmp.add(key, Integer.parseInt(results.getString(key)));
					else
						tmp.add(key, results.getString(key));
				}
				values.add(i, tmp);
			}
			
			results.close();
			statement.close();
			return values;
		}
		
		catch (SQLException exception) {
			System.out.println("SQL Exception. Failed to execute SQL query! When using " + table + " table.");
			System.out.println("SQL: " + sql);
			System.out.println(exception.getMessage());
		}
				
		return null;
	}
	
	public LinkedArray select(String table, ArrayList<String> fields) {
		return select(table, fields, "");
	}
	
	public boolean insert(String table, LinkedArray data) {
		String sql =  "INSERT INTO " +
				table + "(" + getKeys(data)	  + ")" +
				" VALUES (" + getValues(data) + ")" ;
		
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(sql);
			statement.close();
			return true;
		}
		
		catch (SQLException exception) {
			System.out.println("SQL Exception. Failed to execute SQL query! When using " + table + " table.");
			System.out.println(exception.getMessage());
		}
				
		return false;
	}
	
	public boolean update(String table, LinkedArray data, String complement) {
		String sql = "UPDATE " + table + " SET ";
		for (int i = 0; i < data.size(); i++) {
			sql += data.getKeyByIndex(i) + " = '" + data.getValueByIndex(i) + "'";
			sql += (i+1 != data.size())? ", " : "";
		}
		sql += " " + complement;
		
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(sql);
			statement.close();
			return true;
		}
		
		catch (SQLException exception) {
			System.out.println("SQL Exception. Failed to execute SQL query! When using " + table + " table.");
			System.out.println(exception.getMessage());
		}
				
		return false;
	}
	
	public boolean delete(String table, String condition) {
		String sql = "DELETE FROM " + table + " " + condition;
		
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(sql);
			statement.close();
			return true;
		}
		catch (SQLException exception) {
			System.out.println("SQL Exception. Failed to execute SQL query! When using " + table + " table.");
			System.out.println(exception.getMessage());
		}
		
		return false;
	}
	
	
//	public boolean createTable(String sql) { return false; }
//	public boolean deleteTable(String tableName) { return false; }
//	public boolean truncateTable(String tableName) { return false; }
//	
//	public boolean createDatabase(String dbName) { return false; }
//	public boolean deleteDatabase(String dbName) { return false; }
//	public boolean truncateDatabase(String dbName) { return false; }
	
	private String getKeys(LinkedArray sql) {
		String keys = "";
		
		for (int i = 0; i < sql.size(); i++) {
			keys += sql.getKeyByIndex(i);
			keys += (i+1 != sql.size())? ", " : "";
		}
		
		return keys;
	}
	
	private String getValues(LinkedArray sql) {
		String values = "";
		
		for (int i = 0; i < sql.size(); i++) {
			values += "'" + sql.getValueByIndex(i) + "'";
			values += (i+1 != sql.size())? ", " : "";
		}
		
		return values;
	}
	
}
