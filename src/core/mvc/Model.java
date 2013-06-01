/*
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core.mvc;

import core.components.ComponentsManager;
import core.components.ValidationComponent;
import core.dataManipulation.Database;
import core.dataManipulation.LinkedArray;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 *
 * @author Macário Martins <macariomartinsjunior@gmail.com>
 *
 */
public abstract class Model {
	
	protected Controller controller;
	protected Database db = Database.getConnection();
	protected String table = getTable();
	protected String primaryKey = "id";
	protected String prefix = null;
	protected String sufix  = null;
	protected String foreignKey;
	protected String[] models;
	protected LinkedArray data = new LinkedArray();
	protected LinkedArray validations = new LinkedArray();
	protected LinkedArray errors = new LinkedArray();
	protected Model	modelAux;
	public ComponentsManager components = new ComponentsManager();
	
	public Model() {
		components.install("Validation", new ValidationComponent());
		setComponents();
		setValidations();
	}
	
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
		return (LinkedArray) db.select(table, params, "WHERE " + options);
	}
	
	public LinkedArray firstBy(String condition) {
		LinkedArray tmp = all(condition);
		return tmp.isEmpty()? tmp : (LinkedArray) tmp.get(0);
	}
	
	public LinkedArray firstById(Integer id) {
		return firstBy(primaryKey + " = " + id);
	}
	
	public boolean delete(Integer id) {
		return db.delete(table, "WHERE " + primaryKey + " = " + id);
	}
	
	public boolean delete(String condition) {
		return db.delete(table, "WHERE " + condition);
	}
	
	public boolean redundantRegister(LinkedArray param, String[] fields) {
		LinkedArray filter = new LinkedArray();
		
		for (int i = 0; i < param.size(); i++)
			filter.add(fields[i], param.get(fields[i]));
		
		return redundantRegister(filter);
	}
	
	public boolean redundantRegister(LinkedArray param) {
		data.copy(param);
		checkoutForComplements();
		String condition  = "";
		
		for (int i = 0; i < data.size(); i++) {
			String key	 = data.getKeyByIndex(i).toString();
			String value = data.getValueByIndex(i).toString();
			
			condition += key + " = \"" + value + "\"";
			condition += i < data.size()-1? " and " : "";
		}
		
		return isValid(firstBy(condition));
	}
	
	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	public void setTable(String table) {
		this.table = table;
	}
	
	public String getTable() {
		return getTable(this.getClass().getSimpleName());
	}
	
	public String getTable(String modelName) {
		return modelName.substring(0, 1).toLowerCase() + modelName.substring(1);
	}
	
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	public void sendData(LinkedArray data) {
		controller.setData(data);
	}
	
	public void resetTable() {
		table = getTable();
	}
	
	public Object callMethod(String methodName) {
		return callMethod(methodName, null);
	}
	
	public Object callMethod(String methodName, Object param) {
		try {
			Method method = param == null?
				this.getClass().getMethod(methodName) : 
				this.getClass().getMethod(methodName, Object.class);
			
			return param == null? method.invoke(this) : method.invoke(this, param);
		}
		catch (NoSuchMethodException exception) {
			System.out.println(exception.getMessage());
		}
		catch (SecurityException exception) {
			System.out.println(exception.getMessage());
		}
		catch (IllegalAccessException exception) {
			System.out.println(exception.getMessage());
		}
		catch (IllegalArgumentException exception) {
			System.out.println(exception.getMessage());
		}
		catch (InvocationTargetException exception) {
			System.out.println(exception.getMessage());
		}
		
		return null;
	}
	
	public Object callMethod(String methodName, Integer param) {
		try {
			Method method = param == null?
				this.getClass().getMethod(methodName) : 
				this.getClass().getMethod(methodName, Integer.class);
			
			return param == null? method.invoke(this) : method.invoke(this, param);
		}
		catch (NoSuchMethodException exception) {
			System.out.println(exception.getMessage());
		}
		catch (SecurityException exception) {
			System.out.println(exception.getMessage());
		}
		catch (IllegalAccessException exception) {
			System.out.println(exception.getMessage());
		}
		catch (IllegalArgumentException exception) {
			System.out.println(exception.getMessage());
		}
		catch (InvocationTargetException exception) {
			System.out.println(exception.getMessage());
		}
		
		return null;
	}
	
	protected boolean isValid(Object param) {
		return param != null;
	}
	
	protected boolean isValid(LinkedArray param) {
		return ! (param == null || param.isEmpty());
	}
	
	public String getPrimaryKey() {
		return primaryKey;
	}
		
	protected String modelName(String table) {
		return table.substring(0, 1).toUpperCase() + table.substring(1);
	}
	
	protected Integer recoverPrimaryKey(LinkedArray params) {
		Integer id;
		
		if (params.containsKey(primaryKey))
			id = (Integer) params.get(primaryKey);
		
		else {
			String conditions = "";

			for (int i = 0; i < params.size(); i++) {
				conditions += params.getKeyByIndex(i).toString();
				conditions += " = \"" + params.getValueByIndex(i).toString() + "\"";
				conditions += i < params.size() - 1? " and " : " ";
			}
			
			id = (Integer) firstBy(conditions).get(primaryKey);
		}
		
		return id;
	}
	
	protected void setPrefix(String prefix) {
		this.prefix = prefix;
		sufix = null;
		setForeignKey();
	}
	
	protected void setSufix(String sufix) {
		this.sufix = sufix;
		prefix = null;
		setForeignKey();
	}
	
	protected void setForeignKey() {
		if (prefix != null)
			foreignKey = prefix + "_" + primaryKey;
		else
			foreignKey = primaryKey + "_" + sufix;
	}
	
	protected void useModelAux(String modelName) {
		modelName += modelName.equals("App")? "Model" : "";
		
		try {
			modelAux = (Model) Class.forName("app.models." + modelName).newInstance();
		}
		catch (ClassNotFoundException exception) {
			System.out.println("Model '"+ modelName +"' not found!");
			System.out.println(exception.getMessage());
		}
		catch (InstantiationException exception) {
			System.out.println("Failed to create an instance!");
			System.out.println(exception.getMessage());
		}
		catch (IllegalAccessException exception) {
			System.out.println("Illegal access!");
			System.out.println(exception.getMessage());
		}
	}
	
	protected LinkedArray checkoutForComplements() {
		LinkedArray complements = new LinkedArray();
		
		for (int i = 0; i < models.length; i++)
			if (data.containsKey(models[i]))
				complements.add(models[i], (LinkedArray) data.extract(models[i]));
		
		return complements;
	}
	
	public LinkedArray extractComplements(LinkedArray param) {
		LinkedArray complements = new LinkedArray();
		
		for (int i = 0; i < models.length; i++)
			if (param.containsKey(models[i]))
				complements.add(models[i], (LinkedArray) param.extract(models[i]));
		
		return complements.isEmpty()? null : complements;
	}
	
	protected void setComponents() {
		// If the model needs to install any component, this will be implemented by you.
	}
	
	protected void setValidations() {
		// If the model really needs to validate anything, this will be implemented by you.
	}
	
	protected void addValidation(String value, String rule, String message) {
		LinkedArray conf = new LinkedArray();
		
		conf.add("rule", rule);
		conf.add("message", message);
		
		validations.add(value, conf);
	}
	
	public boolean validate(LinkedArray param) {
		boolean valid;
		LinkedArray tmp = new LinkedArray();
		
		for (int i = 0; i < validations.size(); i++) {
			
			String key = (String) validations.getKeyByIndex(i);
			
			if (param.containsKey(key))
				tmp.add(param.get(key), validations.get(key));
		}
		
		valid = (Boolean) components.use("Validation", "validate", tmp);
		errors.reset();
		errors = (LinkedArray) components.use("Validation", "getErrors");
		
		return valid;
	}
	
	public boolean hasErrors() {
		return isValid(errors);
	}
	
	public void displayErrors(String headerMessage) {
		String output = headerMessage + "\n";
		
		for (int i = 0; i < errors.size(); i++)
			output += "\n- " + errors.get(i);
		
		errors.reset();
		controller.message(output);
	}
	
	public abstract boolean saveComplements(Integer owner_id, LinkedArray complements);
	public abstract LinkedArray getComplements(Integer owner_id);
	public abstract boolean deleteComplements(Integer owner_id);
	
}
