/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core.components;

import core.mvc.Controller;
import core.mvc.Model;

/**
 *
 * @author Macário Martins <macariomartinsjunior@gmail.com>
 *
 */
public abstract class Component {

	protected Model model;
	protected Controller controller;
		
	public void setModel(Model model) { this.model = model; }
	public void setController(Controller controller) { this.controller = controller; }
	
}
