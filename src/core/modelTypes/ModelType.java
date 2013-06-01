/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core.modelTypes;

import core.dataManipulation.LinkedArray;
import core.mvc.Model;

/**
 *
 * @author Macário Martins <macariomartinsjunior@gmail.com>
 *
 */
public abstract class ModelType extends Model {

	public ModelType() {
		setModels();
		
		if (prefix == null && sufix == null)
			setPrefix(getTable() + "_");
	}
	
	@Override
	public boolean save(LinkedArray params) {
		LinkedArray complements;
		
		data = params;
		complements = checkoutForComplements();
		
		if (super.save(data))
			if (isValid(complements))
				return saveComplements(recoverPrimaryKey(data), complements);
			else
				return true;
		
		return false;
	}
	
	@Override
	public LinkedArray all() {
		LinkedArray members = super.all();
		
		for (int i = 0; i < members.size(); i++) {
			LinkedArray member = (LinkedArray) members.get(i);
			member.merge(getComplements((Integer) member.get(primaryKey)));
			members.add(i, member);
		}
		
		return members;
	}
	
	@Override
	public LinkedArray all(String options) {
		return super.all(options);
	}
	
	@Override
	public LinkedArray firstBy(String conditions) {
		LinkedArray first = super.firstBy(conditions);
		LinkedArray complements = getComplements((Integer) first.get(primaryKey));
		
		if (isValid(complements))
			first.merge(complements);
		
		return first;
	}
	
	@Override
	public LinkedArray firstById(Integer id) {
		LinkedArray first = super.firstById(id);
		
		first.merge(getComplements(id));
		
		return first;
	}
	
	@Override
	public boolean delete(Integer id) {
		if (super.delete(id))
			return deleteComplements(id);
			
		return false;
	}
	
	@Override
	public boolean delete(String conditions) {
		data = all(conditions);
		
		int sucess = 0;
		for (int i = 0; i < data.size(); i++) {
			LinkedArray member = (LinkedArray) data.get(i);
			if (delete((Integer) member.get(primaryKey)))
				sucess++;
		}
		
		return sucess == data.size();
	}
	
	protected abstract void setModels();
	
}
