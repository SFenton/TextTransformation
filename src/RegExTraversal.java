import java.util.AbstractMap.SimpleEntry;
import java.util.regex.*;


public class RegExTraversal {
	
	private String TRAVERSAL_RESULT;
	private String INPUT;
	public boolean RESULT = false;
	private String PARSE_ERROR = "The string was unable to be parsed.";
	private RegExTree tree;
	
	public RegExTraversal()
	{

	}
	
	/**
	 * Transforms input into either CSV output format, or errors gracefully.
	 * @param input The input to transform.
	 * @return The error string, or the successfully parsed string.
	 */
	@SuppressWarnings("unchecked")
	public SimpleEntry<Boolean, String> TransformInput(String input)
	{
		if (!ValidateString(input))
		{
			return new SimpleEntry<Boolean, String>(false, PARSE_ERROR);
		}
		
		InitializeRegExTree();
		
		return TreeTransformation(input, tree.getRoot());
	}

	/**
	 * Initializes the regular expression tree.
	 */
	private void InitializeRegExTree() 
	{
		tree = new RegExTree(Pattern.compile(".*"));
		
	}

	private SimpleEntry<Boolean, String> TreeTransformation(String input, Node node) 
	{
		Matcher match = ((Pattern)node.data).matcher(input);
		if (match.matches())
		{
			if (node.children.isEmpty())
			{
				return new SimpleEntry<Boolean, String>(true, "Yay!");
			}
			for (int i = 0; i < node.children.size(); i++)
			{
				SimpleEntry<Boolean, String> entry = TreeTransformation(input, (Node) node.children.get(i));
				if (entry.getKey())
				{
					return entry;
				}
			}
		}
		
		return new SimpleEntry<Boolean, String>(false, PARSE_ERROR);
	}

	/**
	 * Validates if a string can be parsed.
	 * @param input The input string to validate.
	 * @return Whether or not the string can be parsed.
	 */
	private boolean ValidateString(String input) 
	{
		if (input == null)
		{
			return false;
		}
		
		if (input.isEmpty())
		{
			return false;
		}
		
		return true;
	}

}
