import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class TextFileTests {
	
	private TextFileParser parser;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception 
	{
		parser = new TextFileParser("C:\\Users\\Stephen Fenton\\Desktop\\test.txt", 
				"C:\\Users\\Stephen Fenton\\Desktop\\testout.csv");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws IOException {
		List<String> test = parser.getInput();
		assertNotNull(test);
		parser.addToOutput();
	}

}
