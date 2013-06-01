/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core.components;

import core.dataManipulation.LinkedArray;
import core.mvc.Model;
import java.util.Enumeration;
import java.util.Properties;

/**
 *
 * @author Macário Martins <macariomartinsjunior@gmail.com>
 *
 */
public class AuthComponent extends Component {
	
	private String loginKeyWord = "login";
	private String passwordKeyWord = "password";
	private String securityString = "#$WH$&)@e73nc104nq0<Ae#%(#954!ax";
	private boolean useSecurityString = true;
	private static LinkedArray users = new LinkedArray();
//	private LinkedArray blackList = new LinkedArray();
	
	
	public AuthComponent(Model model) {
		setModel(model);
	}
	
	public void setLoginKeyWord(String word) {
		loginKeyWord = word;
	}
	
	public void setPasswordKeyWord(String word) {
		passwordKeyWord = word;
	}
	
	public void useSecurityString(boolean choice) {
		useSecurityString = choice;
	}
	
	public Boolean isLogged(LinkedArray params) {
		if ( ! (params.isEmpty() || params == null)) {
			String login = (String) params.get(loginKeyWord);
			Integer password = params.get(passwordKeyWord).hashCode();

			for (Integer i = 0; i < users.size(); i++) {
				LinkedArray tmp = (LinkedArray) users.getValueByIndex(i);

				boolean flag1 = tmp.get(loginKeyWord).equals(login);
				boolean flag2 = password.toString().equals(tmp.get(passwordKeyWord));

				if (flag1 && flag2)
					return Boolean.TRUE;
			}
		}
		
		return Boolean.FALSE;
	}
	
	public Boolean login(LinkedArray params) {
		if ( ! isLogged(params)) {
			String condition1, condition2;
			
			condition1 = loginKeyWord + " = \"" + params.get(loginKeyWord) + "\"";
			condition2 = passwordKeyWord + " = \"" + params.get(passwordKeyWord).hashCode() + "\"";
			
			LinkedArray user = model.firstBy(condition1 + " and " + condition2);
			
			users.add(userCode(), user);
		}
		
		return isLogged(params);
	}
	
	public Boolean logout() {
		LinkedArray user = getUserData();
		users.remove(userCode());
		return ! isLogged(user);
	}
	
	public LinkedArray getUserData() {
		return (LinkedArray) users.get(userCode());
	}
	
	public String userCode() {
		String userCode = useSecurityString? securityString : "";
		Properties p  = System.getProperties();
		Enumeration e = p.propertyNames();
		
		
		while (e.hasMoreElements()) {
			String pName = (String) e.nextElement();
			String pValue = (String) p.get(pName);
			if ( ! (pName.contains("version") || pName.startsWith("file") || pName.startsWith("path")  || pName.startsWith("line")))
				userCode += pValue;
		}
		
		return ((Integer) userCode.hashCode()).toString();
	}
	
//	public void deny(LinkedArray blackList) {
//		if ( ! this.blackList.isEmpty())
//			for (int i = 0; ! blackList.isEmpty(); i++) {
//				Object key = blackList.getKeyByIndex(i);
//				Object value = blackList.getValueByIndex(i);
//				this.blackList.add(key, value);
//				blackList.removeByIndex(i);
//			}
//		else
//			this.blackList = blackList;
//	}
	
}
