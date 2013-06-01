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
public abstract class HasMany extends ModelType {
	
	@Override
	public boolean saveComplements(Integer owner_id, LinkedArray complements) {
		boolean sucess = true;
		
		for (int i = 0; i < models.length; i++)
			if (complements.containsKey(models[i])) {
				LinkedArray complement = (LinkedArray) complements.get(models[i]);
				
				useModelAux(models[i]);
				for (int j = 0; j < complement.size(); j++) {
					LinkedArray part = (LinkedArray) complement.get(j);
					
					if ( ! part.containsKey(foreignKey))
						part.add(foreignKey, owner_id);
					
					if ( ! modelAux.save(part)) {
						System.out.println("Failed to save data in " + models[i]);
						sucess = false;
					}
				}
			}
		
		return sucess;
	}
	
	@Override
	public LinkedArray getComplements(Integer owner_id) {
		if (owner_id != null) {
			LinkedArray complements = new LinkedArray();
			
			for (int i = 0; i < models.length; i++) {
				useModelAux(models[i]);
				
				LinkedArray tmp = modelAux.all(foreignKey + " = \"" + owner_id + "\"");
				complements.add(models[i], tmp);
			}
			
			return complements;
		}
		System.out.println("Warning: The parameter 'owner_id', sent to getComplements is null!");
		return null;
	}
	
	@Override
	public boolean deleteComplements(Integer owner_id) {
		if (owner_id != null) {
			LinkedArray complements = getComplements(owner_id);
			boolean sucess = true;

			for (int i = 0; i < models.length; i++)
				if (complements.containsKey(models[i])) {
					LinkedArray complement = (LinkedArray) complements.get(models[i]);

					useModelAux(models[i]);
					for (int j = 0; j < complement.size(); j++) {
						LinkedArray part = (LinkedArray) complement.get(i);
						Integer part_id = (Integer) part.get(modelAux.getPrimaryKey());

						if ( ! modelAux.delete(part_id)) {
							System.out.println("Failed to delete data in " + models[i]);
							sucess = false;
						}
					}
				}

			return sucess;
		}
		System.out.println("Warning: The parameter 'owner_id', sent to deleteComplements is null!");
		return true;
	}
	
}
