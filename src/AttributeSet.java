import java.util.*;

/**
 * An unordered set of Attributes. This could very easily be a Java collection,
 * but an important operation (namely examining the powerset) is not easily done
 * with the Java collection.
 **/
public class AttributeSet {

	//a list of the backing attributes
	private final List<Attribute> _attributes;

	//construct an empty AttributeSet
	public AttributeSet() {
		_attributes = new ArrayList<>();
	}

	//copy constructor
	public AttributeSet(AttributeSet other) {
		_attributes = new ArrayList<>(other._attributes);
	}

	public void addAttribute(Attribute a) {
		if(!_attributes.contains(a))
			_attributes.add(a);
	}

	public boolean contains(Attribute a) {
		return _attributes.contains(a);
	}

	public int size() {
		return _attributes.size();
	}

	public boolean equals(Object other) {
		if(other == null || !(other instanceof AttributeSet)){
			return false;
		}
		//TODO: you should probably implement this
		if(this.size() == ((AttributeSet)other).size()){
			Iterator<Attribute> iter = iterator();
			while(iter.hasNext()){
				Attribute attr =  iter.next();
				if(!((AttributeSet)other).contains(attr))
					return false;
			}
			return true;
		}
		return false;
	}

	public Iterator<Attribute> iterator() {
		return _attributes.iterator();
	}

	public String toString() {
		String out = "";
		Iterator<Attribute> iter = iterator();
		while(iter.hasNext())
			out += iter.next() + "\t";

		return out;
	}
	
	/**
	 * Gets the attribute set as an arraylist of attributes
	 */
	public ArrayList<Attribute> getAttributeList(){
		
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		Iterator<Attribute> iter = iterator();
		
		while(iter.hasNext())
			attributes.add(iter.next());
		
		return attributes;
	}
	
	/**
	 * Util method that is used while calculating the combinations of the various
	 * attributes
	 */
	public void removeLastAttribute(){
		Iterator<Attribute> iter = iterator();
		while(iter.hasNext()){
			iter.next();
			if(iter.hasNext() == false)
				iter.remove();
		}
	}
	
	/**
	 * Append attributes to the current attribute set
	 * @param other
	 */
	public void appendAttributeSet(AttributeSet other){
		Iterator<Attribute> iter = other.iterator();
		while(iter.hasNext()){
			this.addAttribute(iter.next());
		}
	}
	
	/**
	 * Checking if current attribute set is a subset of another attribute set
	 * @param other
	 */
	public boolean isSupersetOf(AttributeSet other){
		Iterator<Attribute> iter = other.iterator();
		while(iter.hasNext()){
			if(!this.contains(iter.next()))
				return false;
		}
		return true;
	}
	
	public AttributeSet dropAttributes(AttributeSet other, AttributeSet relationAttributes){
		
		//Removing elements that are on the independent side from the dependent side
		Iterator<Attribute> iter = other.iterator();
		while(iter.hasNext()){
			Attribute attrToRemove = iter.next();
			if(this.contains(attrToRemove)){
				this._attributes.remove(attrToRemove);
			}
		}
		
		//Removing elements from the dependent side that are not in the relation set
		ArrayList<Attribute> attrToBeRemoved = new ArrayList<Attribute>();
		for(int index=0; index<this._attributes.size(); index++){
			if(!relationAttributes.contains(this._attributes.get(index)))
				attrToBeRemoved.add(this._attributes.get(index));
		}
		
		for(Attribute attr: attrToBeRemoved){
			this._attributes.remove(attr);
		}
		
		return this;
	}
}
