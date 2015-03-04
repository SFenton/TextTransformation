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
        int count = -1;
        // go through each line
        while ((line = bufferReader.readLine()) != null)   
        {
        	// increment the count
        	count++;
        	
        	// create a new citation object to the list
        	Citation currentCitation = new Citation();
        	
        	// create a separate line string to manipulate
        	String processLine = line;
        	
        	// add the new citation to the list
        	citations.add(currentCitation);
        	
        	// ~~~~~~~~~~~~~~~~~ Name Parsing Done Here ~~~~~~~~~~~~~~~~~
        	// if null then there was an error with the parsing
        	if((processLine = parseName(currentCitation, processLine)) == null)
        	{
        		printErrorToFile(logWriter, "Date", count, line);
        		continue;
        	}
        	
        	
        	// ~~~~~~~~~~~~~~~~~ Date Parsing Done Here ~~~~~~~~~~~~~~~~~
        	// if null then there was an error with the parsing
        	if((processLine = parseDate(currentCitation, processLine)) == null)
        	{
        		printErrorToFile(logWriter, "Date", count, line);
        		continue;
        	}
        	
        	// ~~~~~~~~~~ Title or Publication Parsing Done Here ~~~~~~~~~~~
        	// if null then there was an error with the parsing
        	/*if((processLine = parseTitleAndPublication(currentCitation, processLine)) == null)
        	{
        		printErrorToFile(logWriter, "Title or Publication", count, line);
        		continue;
        	}*/
        }
        
		input.close();
		
		printCitationsToFile(csvWriter, citations);
		csvWriter.flush();
		
		csvWriter.close();
		logWriter.close();
		input.close();
	}
	
	private static String parseName(Citation currentCitation, String processLine)
	{
		// looks for ". (" and ", (" and ", "" and ". "" which indicates, most of the time, the end of the authors
    	int[] endOfAuthors = new int[4];
    	endOfAuthors[0] = processLine.indexOf(". (");
    	endOfAuthors[1] = processLine.indexOf(", (");
    	endOfAuthors[2] = processLine.indexOf(", “");
    	endOfAuthors[3] = processLine.indexOf(". “");
    	
    	// find the min of the valid indexes found
    	int authorEnd = endOfAuthors[0];
    	for(int x = 1; x < 4; x++)
    	{
    		// if min is -1 then replace it with the next or if that
    		// endOfAuthors value is less than the min
    		if(authorEnd == -1 || (endOfAuthors[x] < authorEnd && endOfAuthors[x] != -1))
    		{
    			authorEnd = endOfAuthors[x];
    		}
    	}
    	
    	// check that one was found
    	if(authorEnd > -1)
    	{
    		// add 1 to include the "," or "."
    		String authors = processLine.substring(0, authorEnd + 1);
    		
    		// check if there is a "," at the end and truncate it 
			if(authors.endsWith(","))
			{
				authors = processLine.substring(0, authorEnd);
			}
			
    		currentCitation.Authors = authors;
    		
    		// set the processLine to a substring of itself sinse names have been processed
    		processLine = processLine.substring(authorEnd);
    	}
    	else
    	{
    		return null;
    	}
    	return processLine;
	}
	
	private static String parseDate(Citation currentCitation, String processLine)
	{
		// the first of ". (" which denotes the beginning of the date
    	int begOfDatePeriod = processLine.indexOf(". (");
    	// the first of ", (" which denotes the beginning of the date
    	int begOfDateComma = processLine.indexOf(", (");
    	// the first of " (" which denotes the beginning of the date
    	int begOfDateSpace = processLine.indexOf(" (");
    	
    	// the beginning of the date
    	int dateBeg = begOfDatePeriod;
    	
    	// change dateBeg if it is -1 or begOfDateComma occurs before begOfDatePeriod
    	if(dateBeg == -1 || (dateBeg > begOfDateComma && begOfDateComma != -1))
    	{
    		dateBeg = begOfDateComma;
    	}
    	// if still -1 then set dateBeg to the first " ("
    	if(dateBeg == -1)
    	{
    		dateBeg = begOfDateSpace;
    	}
    	
    	// check that one of them were found and it was relatively close to the start
    	// since we truncated the current line being processed
		if(dateBeg > -1 && dateBeg < 5)
		{
			// add 3 to truncate ". (" or ", ("
			dateBeg = dateBeg + 3;
			
			// get the closure of the "( date in here  )"
			int dateEnd = processLine.substring(dateBeg).indexOf(")") + dateBeg;
			
			// grab the date with the specified beginning and end
    		String date = processLine.substring(dateBeg, dateEnd);
    		
    		currentCitation.Date1 = date;
    		
    		// set the processLine to a substring of itself since the date has been processed
    		processLine.substring(dateEnd);
		}
		else
    	{
			return null;
    	}
		return processLine;
	}
	
	private static void printCitationsToFile(FileWriter csvWriter, ArrayList<Citation> citations) throws IOException
	{
		int numCitations = citations.size();
		for(int x = 0; x < numCitations; x++)
		{
			csvWriter.append(citations.get(x).toCSVString());
		}
	}
	
	private static void printErrorToFile(FileWriter logWriter, String errorType, int count, String line) throws IOException
	{
		logWriter.append(errorType + " error on line # " + (count+1) + ": " + line + "\n");
		logWriter.flush();
	}
}
