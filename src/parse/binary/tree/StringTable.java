package parse.binary.tree;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;


/**
 * Notes for improvement:
 * Add javadoc for class, class variables
 * Consider following java standards by using functions to deal with variables
 */



public class StringTable 
{
    public int numEntries;
    public int sizeTable;
    public int sizeLeadingUpToTable;
    public List<SimpleEntry<Long, String>> stringTable;
    
}
