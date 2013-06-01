/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core.components;

import core.dataManipulation.LinkedArray;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author Macário Martins <macariomartinsjunior@gmail.com>
 *
 */
public class ComponentsManager {
	
	protected LinkedArray components = new LinkedArray();
	
	public void install(String componentsName, Component newComponent) {
		if ( ! components.containsKey(componentsName))
			components.add(componentsName, newComponent);
	}

	public void remove(String componentsName) {
		if (components.containsKey(componentsName))
			components.remove(this);
	}

	public Object use(String componentsName, String methodName) {
		return use(componentsName, methodName, null);
	}

	public Object use(String componentsName, String methodName, LinkedArray params) {
		try {
			Method method = params == null?
				components.get(componentsName).getClass().getMethod(methodName):
				components.get(componentsName).getClass().getMethod(methodName, LinkedArray.class);

			return params == null?
				method.invoke(components.get(componentsName)):
				method.invoke(components.get(componentsName), params);
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
	
}
