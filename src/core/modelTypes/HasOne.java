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
public abstract class HasOne extends ModelType {
	
	@Override
	public boolean saveComplements(Integer owner_id, LinkedArray complements) {
		LinkedArray saveSucess = new LinkedArray();

		for (int i = 0; i < models.length; i++)
			if (complements.containsKey(models[i])) {
				LinkedArray complement = (LinkedArray) complements.get(models[i]);

				if ( ! complement.containsKey(foreignKey))
					complement.add(foreignKey, owner_id);

				useModelAux(models[i]);
				saveSucess.add(models[i], Boolean.valueOf(modelAux.save(complement)));
			}

		int sucess = 0;
		for (int i = 0; i < saveSucess.size(); i++)
			if ((Boolean) saveSucess.getValueByIndex(i))
				sucess++;
			else
				System.out.println("Failed to save data in " + saveSucess.getKeyByIndex(i));

		return sucess == saveSucess.size();
	}
	
	@Override
	public LinkedArray getComplements(Integer owner_id) {
		LinkedArray complements = new LinkedArray();
		
		for (int i = 0; i < models.length; i++) {
			useModelAux(models[i]);
			LinkedArray tmp = modelAux.firstBy(foreignKey + " = '" + owner_id + "'");
			data.add(models[i], tmp);
		}
		
		return complements;
	}
	
	@Override
	public boolean deleteComplements(Integer owner_id) {
		LinkedArray deleteSucess = new LinkedArray();
		LinkedArray complements  = getComplements(owner_id);

		for (int i = 0; i < models.length; i++)
			if (complements.containsKey(models[i])) {
				LinkedArray complement = (LinkedArray) complements.get(models[i]);

				if ( ! complement.containsKey(foreignKey))
					complement.add(foreignKey, owner_id);

				useModelAux(models[i]);
				Integer complement_id = (Integer) complement.get(modelAux.getPrimaryKey());
				deleteSucess.add(models[i], Boolean.valueOf(modelAux.delete(complement_id)));
			}

		int sucess = 0;
		for (int i = 0; i < deleteSucess.size(); i++)
			if ((Boolean) deleteSucess.getValueByIndex(i))
				sucess++;
			else
				System.out.println("Failed to delete data in " + deleteSucess.getKeyByIndex(i));

		return sucess == deleteSucess.size();
	}
	
}
