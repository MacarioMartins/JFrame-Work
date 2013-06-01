/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core.modelTypes;

import core.dataManipulation.LinkedArray;

/**
 *
 * @author Macário Martins <macariomartinsjunior@gmail.com>
 *
 */
public abstract class HasNothing extends ModelType {
	
	@Override
	public boolean saveComplements(Integer owner_id, LinkedArray complements) { return true; }

	@Override
	public LinkedArray getComplements(Integer owner_id) { return null; }

	@Override
	public boolean deleteComplements(Integer owner_id) { return true; }
	
	@Override
	protected void setModels() {
		models = new String[]{};
	}

}
