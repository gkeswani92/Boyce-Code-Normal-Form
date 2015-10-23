import java.util.*;

public class BCNF {

	/**
	 * Implement your algorithm here
	 **/
	public static Set<AttributeSet> decompose(AttributeSet attributeSet, Set<FunctionalDependency> functionalDependencies) {
		Set<AttributeSet> normalisedRelations = new HashSet<AttributeSet>();
		return decomposeAttributeSet(attributeSet, functionalDependencies, normalisedRelations);
	}

	/**
	 * Recursive method that actually decomposes a relations into two relations if it violates BCNF
	 * @param attributeSet
	 * @param functionalDependencies
	 * @param normalisedRelations
	 * @return
	 */
	public static Set<AttributeSet> decomposeAttributeSet(AttributeSet attributeSet,
						Set<FunctionalDependency> functionalDependencies, Set<AttributeSet> normalisedRelations){
		
		//Creating a list of attributes and finding all combinations of these attributes to determine the candidate keys
		ArrayList<Attribute> attributes = attributeSet.getAttributeList();
		ArrayList<AttributeSet> attributeCombination = new ArrayList<AttributeSet>();
		AttributeSet combinationGenerator = new AttributeSet();
		attributeCombination = getAttributeCombinations(attributes, 0, attributeCombination, combinationGenerator);
		
		//Determine all the keys of the given relation and then determining the candidate keys
		ArrayList<AttributeSet> keys = getAllKeys(attributeSet, functionalDependencies, attributeCombination);
		
		//Check if an FD violates BCNF. If it does, normalise the relation into two and recursively normalise on the new two
		boolean needToDecompose = true;
		for(FunctionalDependency fd: functionalDependencies){	
			needToDecompose = needToDecompose(fd, keys, attributeSet);
			if(needToDecompose == true){
				ArrayList<AttributeSet> normalised = normalizeRelations(fd, attributeSet);
				normalisedRelations = decomposeAttributeSet(normalised.get(1), functionalDependencies, normalisedRelations);
				normalisedRelations = decomposeAttributeSet(normalised.get(0), functionalDependencies, normalisedRelations);
				break; //We break because this originally normalised relation is no more. So we should not be looping over it
			}
		}
			
		if (needToDecompose == false)
			normalisedRelations.add(attributeSet);
			
		return normalisedRelations;
	}
	
	/**
	 * Checks the current FD, candidate key and the attributes of the current relation to determine 
	 * if we need to decompose further or not
	 * @param fd
	 * @param candidateKey
	 * @param currentRelation
	 * @return
	 */
	public static boolean needToDecompose(FunctionalDependency fd, ArrayList<AttributeSet> candKeys, AttributeSet currentRelation ){
		
		//Check if the FD contains valid attributes i.e. attributes that are present in the current relation
		AttributeSet fd_attr = new AttributeSet(fd.independent());
		fd_attr.appendAttributeSet(fd.dependent());
		
		//If the relation contains attributes the fd does not contain, then false
		if(!currentRelation.isSupersetOf(fd_attr))
			return false;
		
		//If the current relations only contrains those attributes that are there in the FD, then it is in BCNF
		if(currentRelation.equals(fd_attr))
			return false;
		
		//If the FD matches even one of the candidate keys, we dont decompose
		for(AttributeSet candKey: candKeys)
			if(fd.independent().equals(candKey))
				return false;
		
		return true;
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
		
		//Generate all possible combinations of attribute subsets
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
		
		//Compute closure of each attribute set and check if its closure is U. 
		//If it is, add it to the list of keys
		for(AttributeSet as: attributeCombination){			
			AttributeSet closureOfCurrentAttributeSet = closure(as, functionalDependencies);
			if(closureOfCurrentAttributeSet.isSupersetOf(attributeSetRelation)){
				keys.add(as);
			}
		}
		return keys;
	}

	/**
	 * Normalises the relation into two halves depending on the functional dependency that
	 * violated BCNF
	 * @param fd
	 * @param as
	 * @return
	 */
	public static ArrayList<AttributeSet> normalizeRelations(FunctionalDependency fd, AttributeSet as){
		
		//The right relations is the contents of the FD
		AttributeSet right = new AttributeSet();
		right.appendAttributeSet(fd.independent());
		right.appendAttributeSet(fd.dependent());
		
		//The left relations is the remaining contents and the independent side of the fd
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
	
	
	/**
	 * Finds the attribute closure of the current set of attributes
	 * @param attributeSet
	 * @param functionalDependencies
	 * @return
	 */
	public static AttributeSet closure(AttributeSet attributeSet, Set<FunctionalDependency> functionalDependencies) {

		AttributeSet closureOfAttributeSet = new AttributeSet(attributeSet);
		ArrayList<AttributeSet> attributeCombination = new ArrayList<AttributeSet>();
		AttributeSet combinationGenerator = new AttributeSet();
		ArrayList<AttributeSet> currentAttrCombination = getAttributeCombinations(attributeSet.getAttributeList(), 0, attributeCombination, combinationGenerator);
		
		//Determining the closure from the attribute set passed to the method
		for(AttributeSet currentAS: currentAttrCombination){
			for(FunctionalDependency fd: functionalDependencies){
				
				if(fd.independent().equals(currentAS)){
					if(!closureOfAttributeSet.isSupersetOf(fd.dependent())){
						closureOfAttributeSet.appendAttributeSet(fd.dependent());
					}
				}
			}
		}
		
		//The closure generated from the original attributes can lead to more fd's 
		//becoming applicable to it. This method generates those and adds it to 
		//the closure set
		reCalculateNewClosure(closureOfAttributeSet, functionalDependencies);
			
		return closureOfAttributeSet;
	}
	
	/**
	 * Recursive method to compute the closure of a set of attributes
	 * @param currentClosure
	 * @param functionalDependencies
	 * @return
	 */
	public static AttributeSet reCalculateNewClosure(AttributeSet currentClosure, Set<FunctionalDependency> functionalDependencies){
		
		ArrayList<AttributeSet> attributeCombination = new ArrayList<AttributeSet>();
		AttributeSet combinationGenerator = new AttributeSet();
		ArrayList<AttributeSet> currentClosureCombination = getAttributeCombinations(currentClosure.getAttributeList(), 0, attributeCombination, combinationGenerator);
		
		for(AttributeSet currentAS: currentClosureCombination){
			for(FunctionalDependency fd: functionalDependencies){
				if(fd.independent().equals(currentAS)){
					if(!currentClosure.isSupersetOf(fd.dependent())){
						currentClosure.appendAttributeSet(fd.dependent());
						reCalculateNewClosure(currentClosure, functionalDependencies);
					}
				}
			}
		}
		return currentClosure;
	}
	
}
