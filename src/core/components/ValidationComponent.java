/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core.components;

import core.dataManipulation.LinkedArray;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Macário Martins <macariomartinsjunior@gmail.com>
 *
 */
public class ValidationComponent extends Component {

	private Matcher matcher;
	private Pattern pattern;
	private LinkedArray errors	 = new LinkedArray();
	private LinkedArray patterns = new LinkedArray();
	
	public ValidationComponent() {
		/**
		 * 
		 * Here you may add the patterns like bellow.
		 * 
		 */
		patterns.add("phrase", "[a-zA-Z][a-zA-Z ]*");
		patterns.add("numeric", "[0-9]*");
		patterns.add("alfanumeric", "[a-zA-Z0-9]*");		
	}
	
	public Boolean validate(LinkedArray data) {
		Boolean valid = Boolean.TRUE;
		
		errors.reset();
		
		for (int i = 0; i < data.size(); i++) {
			String value = (String) data.getKeyByIndex(i);
			String rule	 = (String) ((LinkedArray) data.get(value)).get("rule");
			String msg	 = (String) ((LinkedArray) data.get(value)).get("message");
			
			if (patterns.containsKey(rule))
				if (verify(rule, value))
					valid = valid && Boolean.TRUE;
					
				else {
					valid = Boolean.FALSE ;
					setError(msg);
				}
			
			else {
				valid = Boolean.FALSE;
				System.out.println("Attribute 'patterns' don't have the rule '" + rule + "'");
			}
		}
		
		return valid;
	}
	
	public String normalize(String str) {
		str = str.replace("á", "a");	str = str.replace("Á", "A");
		str = str.replace("â", "a");	str = str.replace("Â", "A");
		str = str.replace("ã", "a");	str = str.replace("Ã", "A");
		str = str.replace("à", "a");	str = str.replace("À", "A");
		str = str.replace("ä", "a");	str = str.replace("Ä", "A");
		
		str = str.replace("é", "e");	str = str.replace("É", "E");
		str = str.replace("ê", "e");	str = str.replace("Ê", "E");
		str = str.replace("ẽ", "e");	str = str.replace("Ẽ", "E");
		str = str.replace("è", "e");	str = str.replace("È", "E");
		str = str.replace("ë", "e");	str = str.replace("Ë", "E");

		str = str.replace("í", "i");	str = str.replace("Í", "I");
		str = str.replace("î", "i");	str = str.replace("Î", "I");
		str = str.replace("ĩ", "i");	str = str.replace("Ĩ", "I");
		str = str.replace("ì", "i");	str = str.replace("Ì", "I");
		str = str.replace("ï", "i");	str = str.replace("Ï", "I");

		str = str.replace("ó", "o");	str = str.replace("Ó", "O");
		str = str.replace("ô", "o");	str = str.replace("Ô", "O");
		str = str.replace("õ", "o");	str = str.replace("Õ", "O");
		str = str.replace("ò", "o");	str = str.replace("Ò", "O");
		str = str.replace("ö", "o");	str = str.replace("Ö", "O");
		
		str = str.replace("ú", "u");	str = str.replace("Ú", "U");
		str = str.replace("û", "u");	str = str.replace("Û", "U");
		str = str.replace("ũ", "u");	str = str.replace("Ũ", "U");
		str = str.replace("ù", "u");	str = str.replace("Ù", "U");
		str = str.replace("ü", "u");	str = str.replace("Ü", "U");

		return str;
	}
	
	private Boolean verify(String rule, String value) {
		pattern = Pattern.compile((String) patterns.get(rule));
		matcher = pattern.matcher(normalize(value));
		
		return (Boolean) matcher.matches();
	}
	
	private void setError(String message) {
		errors.put(message);
	}
	
	public LinkedArray getErrors() {
		LinkedArray tmp = new LinkedArray();
		tmp.takeTransference(errors);
		return tmp;
	}
	
}
