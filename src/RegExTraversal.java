import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.regex.*;


public class RegExTraversal {
	
	private String PARSE_ERROR = "The string was unable to be parsed.";
	private RegExTree tree;
	
	/**
	 * Constructor for the class.  No variables should need to be initialized.
	 */
	public RegExTraversal()
	{
		InitializeRegExTree();
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
		
		return TreeTransformation(input, tree.getRoot());
	}

	/**
	 * Initializes the regular expression tree.
	 * THIS WILL NOT STAY IN THE FINAL RELEASE.  THIS IS FOR PROTOTYPING PURPOSES ONLY.
	 */
	@SuppressWarnings("unchecked")
	private void InitializeRegExTree() 
	{
		// Add the root of the tree.  Temporary.
		tree = new RegExTree(Pattern.compile(".*"));
		
		// Temporary simple child node.
		Node child = new Node();
		child.data = Pattern.compile("(.*)\\((\\d{1,4})\\).(.*),(.*),(.*,.*),(.*,.*)(.*)");
		child.parent = tree.getRoot();
		child.children = new ArrayList<Node>();
		child.order = new ArrayList<Order>();
		
		// Temporary specific child nodes.
		Node noSpecificLocation = new Node();
		noSpecificLocation.data = Pattern.compile("(.*)\\((\\d{1,4})\\).(.*),(.*),(.*,.*),(.*, \\d{1,4})");
		noSpecificLocation.parent = child;
		noSpecificLocation.children = new ArrayList<Node>();
		noSpecificLocation.order = new ArrayList<Order>();
		
		noSpecificLocation.order.add(Order.Author);
		noSpecificLocation.order.add(Order.Year);
		noSpecificLocation.order.add(Order.Title);
		noSpecificLocation.order.add(Order.Publication);
		noSpecificLocation.order.add(Order.Location);
		noSpecificLocation.order.add(Order.Date);
		
		child.children.add(noSpecificLocation);
		
		// Specific location nide
		Node specificLocation = new Node();
		specificLocation.data = Pattern.compile("(.*)\\((.*)\\).(.*),(.*),(.*,.*,.*),(.*\\d{1,4})");
		specificLocation.parent = child;
		specificLocation.children = new ArrayList<Node>();
		specificLocation.order = new ArrayList<Order>();
		
		specificLocation.order.add(Order.Author);
		specificLocation.order.add(Order.Year);
		specificLocation.order.add(Order.Title);
		specificLocation.order.add(Order.Publication);
		specificLocation.order.add(Order.Location);
		specificLocation.order.add(Order.Date);
		
		child.children.add(specificLocation);
		
		tree.getRoot().children.add(child);
		
	}

	/**
	 * Simple recursive method to traverse a regular expression tree and find the regex that matches
	 * the input string.  Successful parsing will call a string reorder method that will format the
	 * string appropriately to .CSV standard.  Failed parsing will return an error.
	 * 
	 * @param input The input string that we wish to parse
	 * @param node The node of the tree we are parsing from
	 * @return A key-value pair of a boolean for success and a successful parsed string or
	 * 		   appropriate error message.
	 */
	private SimpleEntry<Boolean, String> TreeTransformation(String input, Node node) 
	{
		// Match the input with the pattern.  If we find a match, do something with it.
		Matcher match = ((Pattern)node.data).matcher(input);
		if (match.find())
		{
			// If we find no children, we've found the regex.  Reorder the input string and return.
			if (node.children.isEmpty())
			{
				return new SimpleEntry<Boolean, String>(true, reorderString(match, node.order));
			}
			
			// Otherwise, recurse with children.
			for (int i = 0; i < node.children.size(); i++)
			{
				SimpleEntry<Boolean, String> entry = TreeTransformation(input, (Node) node.children.get(i));
				if (entry.getKey())
				{
					return entry;
				}
			}
		}
		
		// Last resort failure.
		return new SimpleEntry<Boolean, String>(false, PARSE_ERROR);
	}

	/**
	 * Returns a properly formatted string for input into a .CSV file.
	 * 
	 * @param match The regular expression match containing our match groups.
	 * @param order The order in which our regular expressions are stored.
	 * @return The properly formatted string for output.
	 */
	private String reorderString(Matcher match, List order) 
	{
		String output = "";
		
		// Iterate through the enum in the proper order.
		for (Order enumOrder : Order.values())
		{
			for (int i = 0; i < order.size(); i++)
			{
				if (enumOrder.equals(order.get(i)))
				{
					// 0th match is the whole string; capturing groups start
					// at index 1
					output += "\"" + formatString(match.group(i + 1), enumOrder) + "\"";
				}
			}
			
			output += ",";
		}
		
		// Temporary junky code.  We don't want a last comma in there.
		if (output.length() > 0 && output.charAt(output.length()-1)==',') {
		      output = output.substring(0, output.length()-1);
		    }
		
		return output;
	}

	/**
	 * Formats a string appropriately.  Replaces commas with appropriate substitutions,
	 * removes leading and trailing whitespace, substitutes ampersands and " and " values 
	 * appropriately.  More functionality will be added as necessary.
	 * 
	 * @param subGroup The portion of the string we have taken in.
	 * @param enumOrder The enumeration we are looking at.  Certain enumerations will
	 * 					dictate if we should remove commas or not.
	 * @return The formatted string.
	 */
	private String formatString(String subGroup, Order enumOrder) 
	{
		String output = "";
		// Split the string based on comma
		String[] subGroup_split = subGroup.split("\\.,");
		
		// Enum-specific operations should go here
		if (enumOrder.equals(Order.Author))
		{
			for (int i = 0; i < subGroup_split.length; i++)
			{
				subGroup_split[i] = subGroup_split[i].replace("and", "").replace("&", "");
				subGroup_split[i] = subGroup_split[i].trim();
				if (!subGroup_split[i].endsWith("."))
				{
					subGroup_split[i] = subGroup_split[i] + ".";
				}
			}
		}
		
		// Remove leading and trailing whitespace
		for (int i = 0; i < subGroup_split.length; i++)
		{
			subGroup_split[i] = subGroup_split[i].trim();
		}
		
		// Add the CSV delimiter
		for (int i = 0; i < subGroup_split.length; i++)
		{
			output += subGroup_split[i];
			if (i < subGroup_split.length - 1)
			{
				output += "||";
			}
		}
		
		return output;
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
		
		if (input.isEmpty() || input.replaceAll(" ", "").isEmpty())
		{
			return false;
		}
		
		return true;
	}

}
