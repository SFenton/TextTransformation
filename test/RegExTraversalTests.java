import static org.junit.Assert.*;

import java.util.AbstractMap.SimpleEntry;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import tree.regex.traversal.RegExTraversal;


public class RegExTraversalTests {
	
	private RegExTraversal traversal;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		traversal = new RegExTraversal();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBlankString() {
		assertFalse(traversal.TransformInput(" ").getKey());
		assertFalse(traversal.TransformInput("             ").getKey());
		assertFalse(traversal.TransformInput("").getKey());
	}
	
	@Test
	public void testNullString()
	{
		assertFalse(traversal.TransformInput(null).getKey());
	}
	
	@Test
	public void testValidInput()
	{
		SimpleEntry<Boolean, String> entry = traversal.TransformInput("Aatique, M., Mizusawa, G., and Woerner, B. (1997)."
				+ "Performance of hyperbolic position location techniques for code division multiple access, " +
				"Proceedings of Wireless ‘97, Calgary, Canada, July 9-11, 1997.");
		assertTrue(entry.getKey());
		assertEquals(entry.getValue(), "\"Aatique, M.||Mizusawa, G.||Woerner, B.\",\"1997\"," +
				"\"Performance of hyperbolic position location techniques for code division multiple access\"," +
				"\"Proceedings of Wireless ‘97\",\"Calgary, Canada\",\"July 9-11, 1997\",,,");
		
		entry = traversal.TransformInput("Ahmadian, M. (1997). " +
				"A hybrid semiactive control for secondary suspension applications, " +
				"Proceedings of the Sixth ASME Symposium on Advanced Automotive Technologies, " +
				"1997 ASME International Congress and Exposition, Dallas, TX, November 1997.");
		assertTrue(entry.getKey());
		assertEquals(entry.getValue(), "\"Ahmadian, M.\",\"1997\"," +
				"\"A hybrid semiactive control for secondary suspension applications\"," +
				"\"Proceedings of the Sixth ASME Symposium on Advanced Automotive Technologies\"," +
				"\"1997 ASME International Congress and Exposition, Dallas, TX\",\"November 1997\",,,");
	}
	
	@Test
	public void testInvalidInput()
	{
		SimpleEntry<Boolean, String> entry = traversal.TransformInput("This is a test string that shouldn't work."
				+ "If this works, blame Dustin.  It's probably his fault anyways");
		assertFalse(entry.getKey());
	}

}

