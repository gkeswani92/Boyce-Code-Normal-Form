import java.util.*;

public class BCNF {

	/**
	 * Implement your algorithm here
	 **/
	public static Set<AttributeSet> decompose(AttributeSet attributeSet,
						Set<FunctionalDependency> functionalDependencies) {

		//Creating a list of attributes and finding all combinations of these attributes to determine the candidate keys
		ArrayList<Attribute> attributes 			 = attributeSet.getAttributeList();
		ArrayList<AttributeSet> attributeCombination = new ArrayList<AttributeSet>();
		AttributeSet combinationGenerator 			 = new AttributeSet();
		attributeCombination 						 = getAttributeCombinations(attributes, 0, attributeCombination, combinationGenerator);
		
		//Determine all the keys of the given relation and then determining the candidate keys
		ArrayList<AttributeSet> keys = getAllKeys(attributeSet, functionalDependencies, attributeCombination);
		ArrayList<AttributeSet> candidateKeys = getCandidateKeys(keys);
		
		return Collections.emptySet();
	}


	/**
	 * Recommended helper method 
	 * ￼CLOSURE(X, F) 
	 * begin 
	 * OLDDEP = null; 
	 * NEWDEP = X,
	 * while NEWDEP != OLDDEP do 
	 * begin 
	 * 		OLDDEP := NEWDEP; 
	 * 		for every FD W->Z in F do 
	 * 			if NEWDEP I> W then 
	 * 				NEWDEP :=NEWDEP U Z 
	 * 			end; 
	 * retum(NEWDEP) 
	 * end
	 **/
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
