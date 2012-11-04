/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core;

import java.util.ArrayList;

/**
 *
 * @author Macário Martins <macariomartinsjunior@gmail.com>
 *
 */
public abstract class Model {
	
	protected Controller controller;
	protected Database db = Database.getConnection();
	protected String table = this.getClass().getSimpleName().toLowerCase();
	protected String primaryKey = "id";
	
	public boolean save(LinkedArray data) {
		if (data.containsKey(primaryKey))
			return db.update(table, data, "WHERE " + primaryKey + " = " + (Integer) data.get(primaryKey));
		else
			return db.insert(table, data);
	}
	
	public LinkedArray all() {
		ArrayList<String> params = new ArrayList<String>();
		params.add("*");
		return (LinkedArray) db.select(table, params);
	}
	
	public LinkedArray all(String options) {
		ArrayList<String> params = new ArrayList<String>();
		params.add("*");
		return (LinkedArray) db.select(table, params, options);
	}
	
	public LinkedArray firstBy(String condition) {
		ArrayList<String> params = new ArrayList<String>();
		params.add("*");
		return (LinkedArray) db.select(table, params, condition).getValueByIndex(0);
	}
	
	public LinkedArray firstById(Integer id) {
		return firstBy("WHERE " + primaryKey + " = " + id);
	}
	
	public boolean delete(Integer id) {
		return db.delete(table, "WHERE " + primaryKey + " = " + id);
	}
	
	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	public void setTable(String table) {
		this.table = table;
	}
	
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	public void sendData(LinkedArray data) {
		controller.setData(data);
	}
	
}
