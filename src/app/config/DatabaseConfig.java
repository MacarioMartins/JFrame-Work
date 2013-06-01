/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.config;

import core.dataManipulation.LinkedArray;

/**
 *
 * @author Macário Martins <macariomartinsjunior@gmail.com>
 *
 */
public abstract class DatabaseConfig {
	
	private static LinkedArray config = new LinkedArray();

	public static LinkedArray getConfig() {
		config.add("user",	"root");
		config.add("password",	"");
		config.add("database",	"cashControl");
		config.add("driver",	"com.mysql.jdbc.Driver");
		config.add("url",	"jdbc:mysql://localhost/"); // Without DB name!
		
		return config;
	}
	
}
