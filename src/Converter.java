import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Converter {
	
	public static void main (String[] args) throws IOException
	{
		// array list of citations
		ArrayList<Citation> citations = new ArrayList<Citation>();
		
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
        int count = 0;
        // go through each line
        while ((line = bufferReader.readLine()) != null)   
        {
        	// create a new citation object to the list
        	Citation currentcitation = new Citation();
        	
        	// add the new citation to the list
        	citations.add(currentcitation);
        	
        	// boolean to hold whether or not there was a parsing error
        	boolean error = false;
        	
        	// ~~~~~~~~~~~~~~~~~ Name Parsing Done Here ~~~~~~~~~~~~~~~~~
        	
        	// looks for ". (" and ", (" and ", "" and ". "" which indicates, most of the time, the end of the authors
        	int[] endofauthors = new int[4];
        	endofauthors[0] = line.indexOf(". (");
        	endofauthors[1] = line.indexOf(", (");
        	endofauthors[2] = line.indexOf(", “");
        	endofauthors[3] = line.indexOf(". “");
        	
        	// find the min of the valid indexes found
        	int authorend = endofauthors[0];
        	for(int x = 1; x < 4; x++)
        	{
        		// if min is -1 then replace it with the next or if that
        		// endofauthors value is less than the min
        		if(authorend == -1 || (endofauthors[x] < authorend && endofauthors[x] != -1))
        		{
        			authorend = endofauthors[x];
        		}
        	}
        	
        	// check that one was found
        	if(authorend > -1)
        	{
        		// add 1 to include the "," or "."
        		String authors = line.substring(0, authorend + 1);
        		
        		// check if there is a "," at the end and truncate it 
    			if(authors.endsWith(","))
    			{
    				authors = line.substring(0, authorend);
    			}
    			
        		currentcitation.Authors = authors;
        	}
        	else
        	{
        		logWriter.append("Naming");
        		error = true;
        	}
        	
        	
        	// ~~~~~~~~~~~~~~~~~ Date Parsing Done Here ~~~~~~~~~~~~~~~~~
        	
        	int begofdateperiod = line.indexOf(". (");
        	int begofdatecomma = line.indexOf(", (");
        	
        	int datebeg = begofdateperiod;
        	
        	// change datemin if it is greater than begofdatecomma or is -1
        	if(datebeg == -1 || (datebeg > begofdatecomma && begofdatecomma != -1))
        	{
        		datebeg = begofdatecomma;
        	}
        	
        	// check that one was found
    		if(datebeg > -1)
    		{
    			// add 3 to truncate ". (" or ", ("
    			datebeg = datebeg + 3;
    			
    			// get the closure of the "( date in here  )"
    			int dateend = line.substring(datebeg).indexOf(")") + datebeg;
    			
    			// grab the date with the specified beginning and end
        		String date = line.substring(datebeg, dateend);
        		
        		currentcitation.Date1 = date;
    		}
    		else
        	{
    			if(error)
    			{
    				logWriter.append(" & ");
    			}
        		logWriter.append("Date");
        		error = true;
        	}
        	
    		
    		csvWriter.append(currentcitation.toCSVString());
        	csvWriter.flush();
        	if(error)
        	{
        		logWriter.append(" error on line # " + (count+1) + ": " + line + "\n");
        		logWriter.flush();
        	}
        	count++;
        }
		input.close();
		
		
		csvWriter.close();
		logWriter.close();
		input.close();
	}

}
