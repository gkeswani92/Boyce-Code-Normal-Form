import java.util.*;

public class BCNF {

	
	/**
	 * Implement your algorithm here
	 **/
	public static Set<AttributeSet> decompose(AttributeSet attributeSet, Set<FunctionalDependency> functionalDependencies) {
		Set<AttributeSet> normalisedRelations = new HashSet<AttributeSet>();
		return decomposeAttributeSet(attributeSet, functionalDependencies, normalisedRelations);
	}

	public static Set<AttributeSet> decomposeAttributeSet(AttributeSet attributeSet,
						Set<FunctionalDependency> functionalDependencies, Set<AttributeSet> normalisedRelations){
		
		//Creating a list of attributes and finding all combinations of these attributes to determine the candidate keys
		ArrayList<Attribute> attributes = attributeSet.getAttributeList();
		ArrayList<AttributeSet> attributeCombination = new ArrayList<AttributeSet>();
		AttributeSet combinationGenerator = new AttributeSet();
		attributeCombination = getAttributeCombinations(attributes, 0, attributeCombination, combinationGenerator);
		
		//Determine all the keys of the given relation and then determining the candidate keys
		ArrayList<AttributeSet> keys = getAllKeys(attributeSet, functionalDependencies, attributeCombination);
		ArrayList<AttributeSet> candidateKeys = getCandidateKeys(keys);
		
		boolean needToDecompose = true;
		
		//TODO: Check for crappy values
		for(FunctionalDependency fd: functionalDependencies){
			needToDecompose = true;
			
			for(AttributeSet cdKey: candidateKeys){
				if(cdKey.equals(fd.independent())){
					needToDecompose = false;
				}
			}
			if(needToDecompose == true){
				ArrayList<AttributeSet> normalised = normalizeRelations(fd, attributeSet);
				//normalisedRelations = decomposeAttributeSet(normalised.get(0), functionalDependencies, normalisedRelations);
				normalisedRelations = decomposeAttributeSet(normalised.get(1), functionalDependencies, normalisedRelations);
				break;
			}
		}
		
		if (needToDecompose == false)
			normalisedRelations.add(attributeSet);
		
		return normalisedRelations;
	}
	
	/**
	 * Method to determine all the possible combinations of attributes
	 * @param attributes
	 * @param startIndex
	 * @param attributeCombination
	 * @param combinationGenerator
	 * @return
	 */
	public static ArrayList<AttributeSet> getAttributeCombinations(ArrayList<Attribute> attributes, int startIndex,
					ArrayList<AttributeSet> attributeCombination, AttributeSet combinationGenerator) {
		
		for (int i = startIndex; i < attributes.size(); i++) {
			combinationGenerator.addAttribute(attributes.get(i));
			attributeCombination.add(new AttributeSet(combinationGenerator));
			attributeCombination = getAttributeCombinations(attributes, i+1, attributeCombination, combinationGenerator);
			combinationGenerator.removeLastAttribute();
		}
		return attributeCombination;
	}
	
	/**
	 * Finds all the keys for the relation given the functional dependencies
	 * @param attributeSetRelation
	 * @param functionalDependencies
	 * @param attributeCombination
	 * @return
	 */
	public static ArrayList<AttributeSet> getAllKeys(AttributeSet attributeSetRelation,
			Set<FunctionalDependency> functionalDependencies, ArrayList<AttributeSet> attributeCombination){
		
		ArrayList<AttributeSet> keys = new ArrayList<AttributeSet>();
		
		for(AttributeSet as: attributeCombination){
			AttributeSet closureOfCurrentAttributeSet = closure(as, functionalDependencies);
			if(closureOfCurrentAttributeSet.size() == attributeSetRelation.size()){
				keys.add(as);
			}
		}
		return keys;
	}
	
	public static ArrayList<AttributeSet> getCandidateKeys(ArrayList<AttributeSet> keys){
		
		ArrayList<AttributeSet> candidateKeys = new ArrayList<AttributeSet>();
		
		for(AttributeSet as: keys){
			boolean isSubset = false;
			
			for(AttributeSet temp: keys)
				if(temp != as && as.isSupersetOf(temp))
					isSubset = true;
			
			if(isSubset == false)
				candidateKeys.add(as);
		}
		return candidateKeys;
	}


	public static ArrayList<AttributeSet> normalizeRelations(FunctionalDependency fd, AttributeSet as){
		
		AttributeSet right = new AttributeSet();
		right.appendAttributeSet(fd.independent());
		right.appendAttributeSet(fd.dependent());
		
		AttributeSet left = new AttributeSet();
		ArrayList<Attribute> dependentAttributes = fd.dependent().getAttributeList();
		for(Attribute current: as.getAttributeList()){
			if(!dependentAttributes.contains(current)){
				left.addAttribute(current);
			}
		}
		
		ArrayList<AttributeSet> normalised = new ArrayList<AttributeSet>();
		normalised.add(left);
		normalised.add(right);
		return normalised;
	}
	
	
	public static AttributeSet closure(AttributeSet attributeSet, Set<FunctionalDependency> functionalDependencies) {

		AttributeSet closureOfAttributeSet = new AttributeSet(attributeSet);
		ArrayList<AttributeSet> attributeCombination = new ArrayList<AttributeSet>();
		AttributeSet combinationGenerator = new AttributeSet();
		ArrayList<AttributeSet> currentAttrCombination = getAttributeCombinations(attributeSet.getAttributeList(), 0, attributeCombination, combinationGenerator);
		
		//Determining the closure from the attribute set passed to the method
		for(AttributeSet currentAS: currentAttrCombination){
			for(FunctionalDependency fd: functionalDependencies){
				if(fd.independent().equals(currentAS)){
					closureOfAttributeSet.appendAttributeSet(fd.dependent());
				}
			}
		}
		
		//TODO: Not working for 1.3 example. Talk to Tanvi
		//Determining other attributes of the closure depending on the computer closure uptil now
		currentAttrCombination = getAttributeCombinations(closureOfAttributeSet.getAttributeList(), 0, attributeCombination, combinationGenerator);
		for(AttributeSet currentAS: currentAttrCombination){
			for(FunctionalDependency fd: functionalDependencies){
				if(fd.independent().equals(currentAS)){
					closureOfAttributeSet.appendAttributeSet(fd.dependent());
				}
			}
		}
		
		return closureOfAttributeSet;
	}
}
