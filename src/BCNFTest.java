import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class BCNFTest {
	/**
	 * Performs a basic test on a simple table. gives input attributes (a,b,c)
	 * and functional dependency a->c and expects output (a,c),(b,c) or any
	 * reordering
	 **/
	@Test
	public void testSimpleBCNF() {
		// construct table
		AttributeSet attrs = new AttributeSet();
		attrs.addAttribute(new Attribute("a"));
		attrs.addAttribute(new Attribute("b"));
		attrs.addAttribute(new Attribute("c"));

		// create functional dependencies
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		AttributeSet ind = new AttributeSet();
		AttributeSet dep = new AttributeSet();
		ind.addAttribute(new Attribute("a"));
		dep.addAttribute(new Attribute("c"));
		FunctionalDependency fd = new FunctionalDependency(ind, dep);
		fds.add(fd);

		// run client code
		Set<AttributeSet> bcnf = BCNF.decompose(attrs, fds);

		// verify output
		assertEquals("Incorrect number of tables", 2, bcnf.size());

		for (AttributeSet as : bcnf) {
			assertEquals("Incorrect number of attributes", 2, as.size());
			assertTrue("Incorrect table", as.contains(new Attribute("a")));
		}
	}

	/**
	 * Performs a basic test on a simple table. gives input attributes (a,c)
	 * and functional dependency a->c and expects output (a,c) since no 
	 * decomposition is needed
	 **/
	@Test
	public void testSimplestBCNF() {
		// construct table
		AttributeSet attrs = new AttributeSet();
		attrs.addAttribute(new Attribute("a"));
		attrs.addAttribute(new Attribute("c"));

		// create functional dependencies
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		AttributeSet ind = new AttributeSet();
		AttributeSet dep = new AttributeSet();
		ind.addAttribute(new Attribute("a"));
		dep.addAttribute(new Attribute("c"));
		FunctionalDependency fd = new FunctionalDependency(ind, dep);
		fds.add(fd);

		// run client code
		Set<AttributeSet> bcnf = BCNF.decompose(attrs, fds);

		// verify output
		assertEquals("Incorrect number of tables", 1, bcnf.size());

		for (AttributeSet as : bcnf) {
			assertEquals("Incorrect number of attributes", 2, as.size());
			assertTrue("Incorrect table", as.contains(new Attribute("a")));
		}
	}
	
	/**
	 * Performs a basic test on a simple table. gives input attributes (a,b,c,d,e,f)
	 * and functional dependency d->bc, af->e, b->ac and expects output (a,c) since no 
	 * decomposition is needed
	 **/
	@Test
	public void testMediumBCNF() {
		// construct table
		AttributeSet attrs = new AttributeSet();
		attrs.addAttribute(new Attribute("a"));
		attrs.addAttribute(new Attribute("b"));
		attrs.addAttribute(new Attribute("c"));
		attrs.addAttribute(new Attribute("d"));
		attrs.addAttribute(new Attribute("e"));
		attrs.addAttribute(new Attribute("f"));

		// create functional dependencies
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		AttributeSet ind = new AttributeSet();
		AttributeSet dep = new AttributeSet();
		
		//Adding dependency d 0> bc
		ind.addAttribute(new Attribute("d"));
		dep.addAttribute(new Attribute("b"));
		dep.addAttribute(new Attribute("c"));
		FunctionalDependency fd = new FunctionalDependency(ind, dep);
		fds.add(fd);
		
		//Adding dependency d 0> bc
		ind.addAttribute(new Attribute("a"));
		ind.addAttribute(new Attribute("f"));
		dep.addAttribute(new Attribute("e"));
		fd = new FunctionalDependency(ind, dep);
		fds.add(fd);
		
		//Adding dependency d 0> bc
		ind.addAttribute(new Attribute("b"));
		dep.addAttribute(new Attribute("a"));
		dep.addAttribute(new Attribute("c"));
		fd = new FunctionalDependency(ind, dep);
		fds.add(fd);

		// run client code
		Set<AttributeSet> bcnf = BCNF.decompose(attrs, fds);

		// verify output
		assertEquals("Incorrect number of tables", 3, bcnf.size());

		for (AttributeSet as : bcnf) {
			assertEquals("Incorrect number of attributes", 2, as.size());
			assertTrue("Incorrect table", as.contains(new Attribute("a")));
		}
	}
	
}