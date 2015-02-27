import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Converter {
	
	public static void main (String[] args) throws IOException
	{
		FileReader input = new FileReader("VTTI_pubs.txt");

        BufferedReader bufferReader = new BufferedReader(input);
        
        FileWriter csvWriter = new FileWriter("VTTI_pubs.csv");
        FileWriter logWriter = new FileWriter("log.txt");

        // setup the csv columns (Authors, Date, Title, Publication, Location, Date, Volume, Issue, Pages)
        csvWriter.append("Authors,Date,Title,Publication,Location,Date,Volume,Issue,Pages\n");
        csvWriter.flush();
        
        String line;
        
        // count for citations that are placed in log file that correlates to the line number in
        // the input file
        int count = 1;
        // go through each line
        while ((line = bufferReader.readLine()) != null)   
        {
        	// looks for ". (" and ", (" which indicates, most of the time, the end of the authors
        	int endofauthors = line.indexOf(". (") + 1;
        	if(endofauthors == 0)
        	{
        		endofauthors = line.indexOf(", (") + 1;
        	}
        	if(endofauthors != 0)
        	{
        		String authors = line.substring(0, endofauthors);
        		//System.out.println(endofauthors);
        		csvWriter.append("\"" + authors + "\"\n");
        	}
        	else
        	{
        		logWriter.append("Line number " + count + ": " + line + "\n");

        		// flush citation to the log file since there was an error while parsing
            	logWriter.flush();
        	}
        	
        	
        	// TO DO: make sure that citation is good
        	
        	// flush name to the csv file
        	csvWriter.flush();
        	
        	count++;
        }
		input.close();
		
		
		csvWriter.close();
		logWriter.close();
		input.close();
	}

}
