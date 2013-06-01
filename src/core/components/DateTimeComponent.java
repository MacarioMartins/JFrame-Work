/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core.components;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Macário Martins <macariomartinsjunior@gmail.com>
 *
 */
public class DateTimeComponent extends Component {

	private String pattern = "yyyy-MM-dd HH:mm:ss";
	private SimpleDateFormat dateFormat;
	
	public DateTimeComponent() {
		dateFormat = new SimpleDateFormat(pattern);
	}
	
	public DateTimeComponent(String pattern) {
		dateFormat = new SimpleDateFormat(pattern);
		this.pattern = pattern;
	}
	
	public String rightNow() {
		return dateFormat.format(new Date());
	}
	
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	public String getPattern() {
		return pattern;
	}
	
}
