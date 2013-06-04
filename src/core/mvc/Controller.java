/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core.mvc;

import core.components.ComponentsManager;
import core.dataManipulation.LinkedArray;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import javax.swing.JOptionPane;

/**
 *
 * @author Macário Martins <macariomartinsjunior@gmail.com>
 *
 */
public abstract class Controller {
	
	protected Model model;
	protected LinkedArray views	= new LinkedArray();
	protected LinkedArray data	= new LinkedArray();
	public ComponentsManager components = new ComponentsManager();
	public Controller controllerAux;
	
	public Controller() {
		setModel("");
	}
	
	public void set(LinkedArray values) {
		String action = new Throwable().getStackTrace()[1].getMethodName();
		set(values, action);
	}
	
	public void set(LinkedArray values, String action) {
		View view = getView(action);
		view.setData(values);
		views.add(action, view);
	}
	
	public void display(String action) {
		View view = getView(action);
		view.run();
		view.setVisible(true);
		views.add(action, view);
		resetData();
	}
	
	public void updateView(String action, View view, Object param) {
		data = view.getData();
		view.setController(this);
		views.add(action, view);
		callAction(action, param);
	}
	
	public void updateView(String action, View view, Integer param) {
		data = view.getData();
		view.setController(this);
		views.add(action, view);
		callAction(action, param);
	}
	
	public void updateView(String action, View view) {
		updateView(action, view, null);
	}
	
	public Object callAction(String actionName) {
		return callAction(actionName, null);
	}
	
	public Object callAction(String actionName, Object param) {
		try {
			Method method = param == null?
				this.getClass().getMethod(actionName) : 
				this.getClass().getMethod(actionName, Object.class);
			
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
	
	public Object callAction(String actionName, Integer param) {
		try {
			Method method = param == null?
				this.getClass().getMethod(actionName) : 
				this.getClass().getMethod(actionName, Integer.class);
			
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
	
	public void changeModel(String model) {
		setModel(model);
	}
	
	public void resetModel() {
		setModel("");
	}
	
	public void setData(LinkedArray data) {
		this.data = data;
	}
	
	public void mergeData(LinkedArray data) {
		this.data.merge(data);
	}
	
	public LinkedArray getData() {
		return data;
	}
	
	public void resetData() {
		data.reset();
	}
	
	private void setModel(String modelName) {
		if (modelName.isEmpty())
			modelName = this.getClass().getSimpleName().replace("Controller", "");
		
		modelName += modelName.equals("App")? "Model" : "";
		
		try {
			model = (Model) Class.forName("app.models." + modelName).newInstance();
			model.setController(this);
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
	
	private View getView(String action) {
		if (views.containsKey(action))
			return (View) views.get(action);
		
		else {
			String category		= this.getClass().getSimpleName().replace("Controller", "").toLowerCase();
			String viewsPackage = "app.views.";
	
			category = category.equals("app")? "" : (category + ".");
			viewsPackage += category;
	
			action = action.substring(0, 1).toUpperCase() + action.substring(1);
			action = action + "View";
		
			try {
				View view = (View) Class.forName(viewsPackage + action).newInstance();
				view.setController(this);
				view.setModel(model);
				return view;
			}
			catch (ClassNotFoundException exception) {
				System.out.println("View '"+ action +"' not found!");
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
			
			return null;
		}
	}
	
	public void setControllerAux(Controller aux) {
		controllerAux = aux;
	}
	
	public Controller getControllerAux() {
		return controllerAux;
	}
	
	public Model getModel() {
		return model;
	}
	
	public void message(String text) {
		JOptionPane.showMessageDialog(null, text);
	}
	
	public String input(String message) {
		return JOptionPane.showInputDialog(message);
	}
    
    public String convertAddress(String address) {
        String separator = (String) System.getProperties().get("file.separator");
        return separator + address.replace("->", separator);
    }
    
    public URL getResource(String url) {
        return getClass().getResource(convertAddress(url));
    }
    
}
