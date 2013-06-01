/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core.mvc;

import core.dataManipulation.LinkedArray;
import javax.swing.JOptionPane;

/**
 *
 * @author Macário Martins <macariomartinsjunior@gmail.com>
 *
 */
public abstract class View extends javax.swing.JFrame {

	protected Model model;
	protected Controller controller;
	protected LinkedArray data = new LinkedArray();
	
	public abstract void run();
	
	public void setData(LinkedArray data) {
		this.data = data;
	}
	
	public LinkedArray getData() {
		return data;
	}
	
	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	public void setModel(Model model) {
		this.model = model;
	}
	
	public void updateController(Object param) {
		String actionName;
		
		actionName = this.getClass().getSimpleName();
		actionName = actionName.replace("View", "");
		actionName = actionName.substring(0, 1).toLowerCase() + actionName.substring(1);
		
		controller.updateView(actionName, this, param);
	}
	
	public void updateController(Integer param) {
		String actionName;
		
		actionName = this.getClass().getSimpleName();
		actionName = actionName.replace("View", "");
		actionName = actionName.substring(0, 1).toLowerCase() + actionName.substring(1);
		
		controller.updateView(actionName, this, param);
	}
	
	public void updateController() {
		updateController(null);
	}
	
	protected void setLookAndFeel(String lookAndFeel) {
		String view = new Throwable().getStackTrace()[1].getClassName();
		
		try {
			if (lookAndFeel.equals(""))
				lookAndFeel = "Metal";
			
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if (lookAndFeel.equalsIgnoreCase(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}
		catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(view).log(java.util.logging.Level.SEVERE, null, ex);
		}
		catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(view).log(java.util.logging.Level.SEVERE, null, ex);
		}
		catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(view).log(java.util.logging.Level.SEVERE, null, ex);
		}
		catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(view).log(java.util.logging.Level.SEVERE, null, ex);
		}
	}
	
	public void message(String text) {
		JOptionPane.showMessageDialog(null, text);
	}
	
	public String input(String message) {
		return JOptionPane.showInputDialog(null, message);
	}
	
}
