import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Converter {
	
	public static void main (String[] args) throws IOException
	{
		FileReader input = new FileReader("VTTI_pubs.txt");

        BufferedReader bufferReader = new BufferedReader(input);
        
        FileWriter writer = new FileWriter("VTTI_pubs.csv");

        // setup the csv columns (Authors, Date, Title, Publication, Location, Date, Volume, Issue, Pages)
        writer.append("Authors,Date,Title,Publication,Location,Date,Volume,Issue,Pages\n");
        writer.flush();
        
        String line;
        // go through each line
        while ((line = bufferReader.readLine()) != null)   
        {
        	// looks for ". (" and ", (" which indicates, most of the time, the end of the authors
        	int endofauthors = line.indexOf(". (") + 1;
        	if(endofauthors == -1)
        	{
        		endofauthors = line.indexOf(", (") + 1;
        	}
        	if(endofauthors != -1)
        	{
        		String authors = line.substring(0, endofauthors);
        		writer.append("\"" + authors + "\"\n");
        	}
        	
        	
        	// TO DO: make sure that citation is good
        	
        	// flush it to the file
        	writer.flush();
        }
		input.close();
		
		
		writer.close();
		input.close();
	}

}
