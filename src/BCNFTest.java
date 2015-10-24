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
	 * Performs a basic test on a simple table. gives input attributes (a,c) and
	 * functional dependency a->c and expects output (a,c) since no
	 * decomposition is needed
	 **/
	@Test
	public void test2AttributeSimpleBCNF() {
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

	// R=abcde FDs: ab->c d->e
	@Test
	public void test5AttributeSimpleBCNF() {
		// construct table
		AttributeSet attrs = new AttributeSet();
		attrs.addAttribute(new Attribute("a"));
		attrs.addAttribute(new Attribute("b"));
		attrs.addAttribute(new Attribute("c"));
		attrs.addAttribute(new Attribute("d"));
		attrs.addAttribute(new Attribute("e"));

		// create functional dependencies
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		AttributeSet ind = new AttributeSet();
		AttributeSet dep = new AttributeSet();
		ind.addAttribute(new Attribute("a"));
		ind.addAttribute(new Attribute("b"));
		dep.addAttribute(new Attribute("c"));
		FunctionalDependency fd = new FunctionalDependency(ind, dep);
		fds.add(fd);

		AttributeSet ind1 = new AttributeSet();
		AttributeSet dep1 = new AttributeSet();
		ind1.addAttribute(new Attribute("d"));
		dep1.addAttribute(new Attribute("e"));
		FunctionalDependency fd1 = new FunctionalDependency(ind1, dep1);
		fds.add(fd1);

		// run client code
		Set<AttributeSet> bcnf = BCNF.decompose(attrs, fds);
		
		// verify output
		assertEquals("Incorrect number of tables", 3, bcnf.size());

		for (AttributeSet as : bcnf) {
			if (as.contains(new Attribute("c"))) {
				assertEquals("Incorrect number of attributes", 3, as.size());
				assertTrue("Incorrect table", as.contains(new Attribute("a")));
			} else if (as.contains(new Attribute("e"))) {
				assertEquals("Incorrect number of attributes", 2, as.size());
				assertTrue("Incorrect table", as.contains(new Attribute("d")));
			} else {
				assertEquals("Incorrect number of attributes", 3, as.size());
				assertTrue("Incorrect table", as.contains(new Attribute("a")));
			}
		}
	}

	// Base case where no decomposition is needed
	@Test
	public void testNoBCNFViolations() {
		// construct table
		AttributeSet attrs = new AttributeSet();
		attrs.addAttribute(new Attribute("a"));
		attrs.addAttribute(new Attribute("b"));
		attrs.addAttribute(new Attribute("c"));
		attrs.addAttribute(new Attribute("d"));
		attrs.addAttribute(new Attribute("e"));

		// create functional dependencies
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		AttributeSet ind = new AttributeSet();
		AttributeSet dep = new AttributeSet();
		ind.addAttribute(new Attribute("a"));
		dep.addAttribute(new Attribute("b"));
		dep.addAttribute(new Attribute("c"));
		dep.addAttribute(new Attribute("d"));
		dep.addAttribute(new Attribute("e"));
		FunctionalDependency fd = new FunctionalDependency(ind, dep);
		fds.add(fd);

		// run client code
		Set<AttributeSet> bcnf = BCNF.decompose(attrs, fds);

		// verify output
		assertEquals("Incorrect number of tables", 1, bcnf.size());

		for (AttributeSet as : bcnf) {
			assertEquals("Incorrect number of attributes", 5, as.size());
			assertTrue("Incorrect table", as.contains(new Attribute("a")));
		}
	}

	
	/**
	 * 
	 * Expected - bac, afe, dfg, db
	 * My output - afdg afe dbc
	 * 
	 */

	@Test
	public void testBCNFQuestion3() {

		// construct table
		AttributeSet attrs = new AttributeSet();
		attrs.addAttribute(new Attribute("a"));
		attrs.addAttribute(new Attribute("b"));
		attrs.addAttribute(new Attribute("c"));
		attrs.addAttribute(new Attribute("d"));
		attrs.addAttribute(new Attribute("e"));
		attrs.addAttribute(new Attribute("f"));
		attrs.addAttribute(new Attribute("g"));

		// create functional dependencies
		// D -> BC
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		AttributeSet ind = new AttributeSet();
		AttributeSet dep = new AttributeSet();
		ind.addAttribute(new Attribute("d"));
		dep.addAttribute(new Attribute("b"));
		dep.addAttribute(new Attribute("c"));
		FunctionalDependency fd = new FunctionalDependency(ind, dep);
		fds.add(fd);
		
		// AF -> E
		ind = new AttributeSet();
		dep = new AttributeSet();
		ind.addAttribute(new Attribute("a"));
		ind.addAttribute(new Attribute("f"));
		dep.addAttribute(new Attribute("e"));
		fd = new FunctionalDependency(ind, dep);
		fds.add(fd);

		// B -> AC
		ind = new AttributeSet();
		dep = new AttributeSet();
		ind.addAttribute(new Attribute("b"));
		dep.addAttribute(new Attribute("a"));
		dep.addAttribute(new Attribute("c"));
		fd = new FunctionalDependency(ind, dep);
		fds.add(fd);

		// run client code
		Set<AttributeSet> bcnf = BCNF.decompose(attrs, fds);
		
		// verify output
		assertEquals("Incorrect number of tables", 4, bcnf.size());

		for(AttributeSet as : bcnf) {
			if (as.contains(new Attribute("c"))) {
				assertEquals("Incorrect number of attributes", 3, as.size());
				assertTrue("Incorrect table", as.contains(new Attribute("a")));
				assertTrue("Incorrect table", as.contains(new Attribute("b")));
			} else if (as.contains(new Attribute("e"))) {
				assertEquals("Incorrect number of attributes", 3, as.size());
				assertTrue("Incorrect table", as.contains(new Attribute("d")));
				assertTrue("Incorrect table", as.contains(new Attribute("f")));
			} 
			else if (as.contains(new Attribute("g"))) {
				assertEquals("Incorrect number of attributes", 3, as.size());
				assertTrue("Incorrect table", as.contains(new Attribute("d")));
				assertTrue("Incorrect table", as.contains(new Attribute("f")));
			}else {
				assertEquals("Incorrect number of attributes", 2, as.size());
				assertTrue("Incorrect table", as.contains(new Attribute("b")));
				assertTrue("Incorrect table", as.contains(new Attribute("d")));
			}
		}
	}

	/**
	 * 
	 * Expected - ab, cd, ace
	 * 
	 */

	@Test
	public void testBCNFQuestion19_5_1() {

		// construct table

		AttributeSet attrs = new AttributeSet();
		attrs.addAttribute(new Attribute("a"));
		attrs.addAttribute(new Attribute("b"));
		attrs.addAttribute(new Attribute("c"));
		attrs.addAttribute(new Attribute("d"));
		attrs.addAttribute(new Attribute("e"));

		// create functional dependencies
		// A -> B
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		AttributeSet ind = new AttributeSet();
		AttributeSet dep = new AttributeSet();
		ind.addAttribute(new Attribute("a"));
		dep.addAttribute(new Attribute("b"));
		FunctionalDependency fd = new FunctionalDependency(ind, dep);
		fds.add(fd);

		// C -> D
		ind = new AttributeSet();
		dep = new AttributeSet();
		ind.addAttribute(new Attribute("c"));
		dep.addAttribute(new Attribute("d"));
		fd = new FunctionalDependency(ind, dep);
		fds.add(fd);

		// run client code
		Set<AttributeSet> bcnf = BCNF.decompose(attrs, fds);

		// verify output
		assertEquals("Incorrect number of tables", 3, bcnf.size());
	}

	/**
	 * 
	 * Expected - ab, bf
	 * 
	 */

	@Test
	public void testBCNFQuestion19_5_2() {

		// construct table
		AttributeSet attrs = new AttributeSet();
		attrs.addAttribute(new Attribute("a"));
		attrs.addAttribute(new Attribute("b"));
		attrs.addAttribute(new Attribute("f"));

		// create functional dependencies
		// ac -> e
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		AttributeSet ind = new AttributeSet();
		AttributeSet dep = new AttributeSet();
		ind.addAttribute(new Attribute("a"));
		ind.addAttribute(new Attribute("c"));
		dep.addAttribute(new Attribute("e"));
		FunctionalDependency fd = new FunctionalDependency(ind, dep);
		fds.add(fd);

		// b -> f
		ind = new AttributeSet();
		dep = new AttributeSet();
		ind.addAttribute(new Attribute("b"));
		dep.addAttribute(new Attribute("f"));
		fd = new FunctionalDependency(ind, dep);
		fds.add(fd);

		// run client code
		Set<AttributeSet> bcnf = BCNF.decompose(attrs, fds);

		// verify output
		assertEquals("Incorrect number of tables", 2, bcnf.size());

		for (AttributeSet as : bcnf) {
			assertEquals("Incorrect number of attributes", 2, as.size());
			assertTrue("Incorrect table", as.contains(new Attribute("b")));

		}
	}
	
	@Test
	public void difficultTextBookExample() {
		// construct table
		AttributeSet attrs = new AttributeSet();
		attrs.addAttribute(new Attribute("c"));
		attrs.addAttribute(new Attribute("s"));
		attrs.addAttribute(new Attribute("j"));
		attrs.addAttribute(new Attribute("d"));
		attrs.addAttribute(new Attribute("p"));
		attrs.addAttribute(new Attribute("q"));
		attrs.addAttribute(new Attribute("v"));

		// create functional dependencies ***KEY*** Set<FunctionalDependency>
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		AttributeSet ind = new AttributeSet();
		AttributeSet dep = new AttributeSet();
		
		//c -> sjdpqv
		ind.addAttribute(new Attribute("c"));
		dep.addAttribute(new Attribute("s"));
		dep.addAttribute(new Attribute("j"));
		dep.addAttribute(new Attribute("d"));
		dep.addAttribute(new Attribute("p"));
		dep.addAttribute(new Attribute("q"));
		dep.addAttribute(new Attribute("v"));
		FunctionalDependency fd = new FunctionalDependency(ind, dep);
		fds.add(fd);

		// create functional dependencies jp->c
		AttributeSet ind1 = new AttributeSet();
		AttributeSet dep1 = new AttributeSet();
		ind1.addAttribute(new Attribute("j"));
		ind1.addAttribute(new Attribute("p"));
		dep1.addAttribute(new Attribute("c"));
		FunctionalDependency fd1 = new FunctionalDependency(ind1, dep1);
		fds.add(fd1);

		// create functional dependencies SD->P
		AttributeSet ind2 = new AttributeSet();
		AttributeSet dep2 = new AttributeSet();
		ind2.addAttribute(new Attribute("s"));
		ind2.addAttribute(new Attribute("d"));
		dep2.addAttribute(new Attribute("p"));
		FunctionalDependency fd2 = new FunctionalDependency(ind2, dep2);
		fds.add(fd2);

		// create functional dependencies J->S
		AttributeSet ind3 = new AttributeSet();
		AttributeSet dep3 = new AttributeSet();
		ind3.addAttribute(new Attribute("j"));
		dep3.addAttribute(new Attribute("s"));
		FunctionalDependency fd3 = new FunctionalDependency(ind3, dep3);
		fds.add(fd3);

		// run client code
		Set<AttributeSet> bcnf = BCNF.decompose(attrs, fds);
		
		// verify output
		assertEquals("Incorrect number of tables", 2, bcnf.size());
		//#TODO: Check solution
	}

	/**
	 * Performs a basic test on a simple table. gives input attributes
	 * (a,b,c,d,e,f) and functional dependency d->bc, af->e, b->ac and expects
	 * output (a,c) since no decomposition is needed
	 * 
	 * Expected output is dbc, afe, daf
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

		// Adding dependency d -> bc
		ind.addAttribute(new Attribute("d"));
		dep.addAttribute(new Attribute("b"));
		dep.addAttribute(new Attribute("c"));
		FunctionalDependency fd = new FunctionalDependency(ind, dep);
		fds.add(fd);

		// Adding dependency af -> e
		AttributeSet ind2 = new AttributeSet();
		AttributeSet dep2 = new AttributeSet();
		ind2.addAttribute(new Attribute("a"));
		ind2.addAttribute(new Attribute("f"));
		dep2.addAttribute(new Attribute("e"));
		fd = new FunctionalDependency(ind2, dep2);
		fds.add(fd);

		// Adding dependency b > ac
		AttributeSet ind3 = new AttributeSet();
		AttributeSet dep3 = new AttributeSet();
		ind3.addAttribute(new Attribute("b"));
		dep3.addAttribute(new Attribute("a"));
		dep3.addAttribute(new Attribute("c"));
		fd = new FunctionalDependency(ind3, dep3);
		fds.add(fd);

		// run client code
		Set<AttributeSet> bcnf = BCNF.decompose(attrs, fds);
		
		// verify output
		assertEquals("Incorrect number of tables", 3, bcnf.size());

		//dbc, afe, bac
		for (AttributeSet as : bcnf) {
			if (as.contains(new Attribute("c"))) {
				assertEquals("Incorrect number of attributes", 3, as.size());
				assertTrue("Incorrect table", as.contains(new Attribute("b")));
				assertTrue("Incorrect table", as.contains(new Attribute("a")));
			} else if (as.contains(new Attribute("e"))) {
				assertEquals("Incorrect number of attributes", 3, as.size());
				assertTrue("Incorrect table", as.contains(new Attribute("d")));
				assertTrue("Incorrect table", as.contains(new Attribute("f")));
			} else {
				assertEquals("Incorrect number of attributes", 2, as.size());
				assertTrue("Incorrect table", as.contains(new Attribute("d")));
				assertTrue("Incorrect table", as.contains(new Attribute("b")));
			}
		}
	}
	
	@Test
	public void testExtraAttributeFD() {
		// construct table
		AttributeSet attrs = new AttributeSet();
		attrs.addAttribute(new Attribute("a"));
		attrs.addAttribute(new Attribute("b"));
		attrs.addAttribute(new Attribute("c"));

		// create functional dependencies
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		AttributeSet ind = new AttributeSet();
		AttributeSet dep = new AttributeSet();

		// Adding dependency a -> d
		ind.addAttribute(new Attribute("a"));
		dep.addAttribute(new Attribute("d"));
		FunctionalDependency fd = new FunctionalDependency(ind, dep);
		fds.add(fd);

		// Adding dependency af -> e
		AttributeSet ind2 = new AttributeSet();
		AttributeSet dep2 = new AttributeSet();
		ind2.addAttribute(new Attribute("d"));
		dep2.addAttribute(new Attribute("b"));
		fd = new FunctionalDependency(ind2, dep2);
		fds.add(fd);
		
		// run client code
		Set<AttributeSet> bcnf = BCNF.decompose(attrs, fds);
		
		// verify output
		assertEquals("Incorrect number of tables", 2, bcnf.size());
	}
	
	@Test
	  public void testCase1() {
	    //construct table
	    AttributeSet attrs = new AttributeSet();
	    attrs.addAttribute(new Attribute("a"));
	    attrs.addAttribute(new Attribute("b"));
	    attrs.addAttribute(new Attribute("c"));
	    attrs.addAttribute(new Attribute("d"));
	    attrs.addAttribute(new Attribute("e"));
	    attrs.addAttribute(new Attribute("f"));

	    //create functional dependencies
	    Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
	    AttributeSet ind = new AttributeSet();
	    AttributeSet dep = new AttributeSet();
	    
	    ind.addAttribute(new Attribute("a"));
	    dep.addAttribute(new Attribute("c"));
	    dep.addAttribute(new Attribute("b"));
	    
	    AttributeSet ind1 = new AttributeSet();
	    AttributeSet dep1 = new AttributeSet();
	    ind1.addAttribute(new Attribute("a"));
	    dep1.addAttribute(new Attribute("d"));
	    
	    FunctionalDependency fd = new FunctionalDependency(ind, dep);
	    FunctionalDependency fd1 = new FunctionalDependency(ind1, dep1);
	    fds.add(fd);
	    fds.add(fd1);

	    //run client code
	    Set<AttributeSet> bcnf = BCNF.decompose(attrs, fds);
	    System.out.println(bcnf);

	    //verify output
	    assertEquals("Incorrect number of tables", 3, bcnf.size());

	    for(AttributeSet as : bcnf) {
	     if(as.contains(new Attribute("d")))
	    		assertEquals("Incorrect number of attributes", 2, as.size());
	     else
	    	 assertEquals("Incorrect number of attributes", 3, as.size());
	      assertTrue("Incorrect table", as.contains(new Attribute("a")));
	    }

	  }
	
	//R-abcde bcd->A 
	  @Test
	  public void testCase2() {
	    //construct table
	    AttributeSet attrs = new AttributeSet();
	    attrs.addAttribute(new Attribute("a"));
	    attrs.addAttribute(new Attribute("b"));
	    attrs.addAttribute(new Attribute("c"));
	    attrs.addAttribute(new Attribute("d"));
	    attrs.addAttribute(new Attribute("e"));
	    

	    //create functional dependencies
	    Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
	    AttributeSet ind = new AttributeSet();
	    AttributeSet dep = new AttributeSet();
	    
	    ind.addAttribute(new Attribute("b"));
	    ind.addAttribute(new Attribute("c"));
	    ind.addAttribute(new Attribute("d"));
	    dep.addAttribute(new Attribute("a"));
	    
	        
	    FunctionalDependency fd = new FunctionalDependency(ind, dep);
	    
	    fds.add(fd);
	        //run client code
	    Set<AttributeSet> bcnf = BCNF.decompose(attrs, fds);

	    //verify output
	    assertEquals("Incorrect number of tables", 2, bcnf.size());

	    for(AttributeSet as : bcnf) {
	     
	      assertEquals("Incorrect number of attributes", 4, as.size());
	      assertTrue("Incorrect table", as.contains(new Attribute("b")));
	    }

	  }
}