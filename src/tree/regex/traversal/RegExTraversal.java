package tree.regex.traversal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.regex.*;

import parse.binary.tree.TreeParser;
import tree.regex.RegExTree;
import tree.regex.components.Node;
import tree.regex.components.Order;


/**
 * Notes for improvement:
 * Javadoc for class, class variables
 * Deal with warnings, try not to suppress them and try to deal with 
 * 		already suppressed things (unless you have good reason not to)
 * Eliminate magic strings
 * stop being a hacky 1114 student (check the TODO you made)
 */



public class RegExTraversal {
	
	private String PARSE_ERROR = "The string was unable to be parsed.";
	private RegExTree tree;
	
	/**
	 * Constructor for the class.  No variables should need to be initialized.
	 * @throws IOException 
	 */
	public RegExTraversal(RegExTree regexTree) throws IOException
	{
		this.tree = regexTree;
	}
	
	/**
	 * Transforms input into either CSV output format, or errors gracefully.
	 * @param input The input to transform.
	 * @return The error string, or the successfully parsed string.
	 */
	@SuppressWarnings("unchecked")
	public SimpleEntry<Boolean, String> TransformInput(String input)
	{
		if (!validateString(input))
		{
			return new SimpleEntry<Boolean, String>(false, PARSE_ERROR);
		}
		
		// Temporary hacky code
		input = input.replace("vs.", "vs@");
		input = input.replace("U.S.", "U@S@");
		
		return TreeTransformation(input, tree.getRoot());
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
		Object data = node.data;
		Pattern p = (Pattern) data;
		
		// Match the input with the pattern.  If we find a match, do something with it.
		Matcher match = p.matcher(input);
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
		
		for (int i = 0; i < match.groupCount(); i++)
		{
			System.out.println(match.group(i));
		}
		
		// Iterate through the enum in the proper order.
		for (Order enumOrder : Order.values())
		{
			for (int i = 0; i < order.size(); i++)
			{
				if (enumOrder.equals(order.get(i)))
				{
					String matchResult = "";
					for (int j = i + 1; j < match.groupCount(); j++ )
					{
						if (validateString(match.group(j)))
						{
							matchResult = match.group(j);
							j = match.groupCount();
						}
					}
					// 0th match is the whole string; capturing groups start
					// at index 1
					if (enumOrder.equals(Order.Author))
					{
						output += "\"" + formatString(matchResult, enumOrder) + "\"";
					}
					else
					{
						// Remove leading and trailing whitespace
						matchResult= matchResult.trim();
						if (matchResult.startsWith("\"") && matchResult.endsWith("\""))
						{
							if (matchResult.endsWith(",\""))
							{
								matchResult = matchResult.replace(",\"", "\"");
							}
							
							matchResult = matchResult.replace("\"", "'");
							
							output += matchResult;
						}
						else
						{
							// Temporary junky fix
							if (matchResult.startsWith(",") || matchResult.startsWith("."))
							{
								int spaceCount = 1;
								while (matchResult.charAt(spaceCount) == ' ')
								{
									spaceCount++;
								}
								matchResult = matchResult.substring(spaceCount, matchResult.length());
							}
							
							// Temporary junky fix for quotation mark issues
							matchResult = matchResult.replace("\"", "'");
							
							matchResult = matchResult.trim();
							
							if (matchResult.endsWith(",'") || matchResult.endsWith(".'"))
							{
								matchResult = matchResult.replace(",'", "'");
								matchResult = matchResult.replace(".'", "'");
							}
							
							// Remove leading space
							
							output += "\"" + matchResult + "\"";
						}
					}
				}
			}
			
			output += ",";
		}
		
		// Temporary junky code.  We don't want a last comma in there.
		if (output.length() > 0 && output.charAt(output.length()-1)==',') {
		      output = output.substring(0, output.length()-1);
		    }
		
		// Fix select output items.
		output = output.replace("vs@", "vs.");
		output = output.replace("U@S@", "U.S.");
		
		
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
		String[] subGroup_split = subGroup.split("\\.,| and | & ");
		
		List<String> subGroup_list = new ArrayList<String>();

	    for(String s : subGroup_split) 
	    {
	       if(s != null && s.length() > 0) 
	       {
	    	   subGroup_list.add(s);
	       }
	    }

	    subGroup_split = subGroup_list.toArray(new String[subGroup_list.size()]);
		
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
	private boolean validateString(String input) 
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
