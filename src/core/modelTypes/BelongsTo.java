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
public abstract class BelongsTo extends ModelType {

	protected boolean foreignKeyPrefixed = false;
	protected boolean foreignKeySufixed  = false;
	
	public BelongsTo() {
		if ( ! (foreignKeyPrefixed || foreignKeySufixed))
			foreignKeyPrefixed = true;
	}
	
	@Override
	public boolean saveComplements(Integer owner_id, LinkedArray complement) {
		String complementName = modelName(table);
		LinkedArray owner_data = controller.controllerAux.getData();
		LinkedArray complements = ( ! owner_data.containsKey(complementName))?
			new LinkedArray() :
			(LinkedArray) owner_data.get(complementName);
		
		complements.put(complement);
		owner_data.add(complementName, complements);
		
		controller.controllerAux.setData(owner_data);
		
		return controller.controllerAux.getData().containsKey(modelName(table));
	}

	@Override
	public LinkedArray getComplements(Integer owner_id) {
		for (int i = 0; i < models.length; i++) {
			LinkedArray complements = all(getForeignKey(models[i] + " = '" + owner_id + "'"));
			
			if (isValid(complements))
				data.add(modelName(table), complements);
		}
		
		return data;
	}

	@Override
	public boolean deleteComplements(Integer owner_id) {
		LinkedArray complements = getComplements(owner_id);
		
		for (int i = 0; i < complements.size(); i++) {
			LinkedArray complement = (LinkedArray) complements.get(i);
			
			if ( ! delete((Integer) complement.get(primaryKey)))
				return false;
		}
		
		return true;
	}
	
	public String getForeignKey(String modelName) {
		useModelAux(modelName);
		
		if (foreignKeyPrefixed)
			return getTable(modelName) + "_" + modelAux.getPrimaryKey();
		else
			return modelAux.getPrimaryKey() + "_" + getTable(modelName);
	}

}
