import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;


public class TextFileParser 
{
	private String filepath;
	private String destination;
	private List<String> output;
	private List<String> failedOutput;
	private List<String> input;

	public TextFileParser(String filepath, String destination) throws IOException
	{
		this.filepath = filepath;
		this.destination = destination;
		output = new ArrayList<String>();
		failedOutput = new ArrayList<String>();
		input = new ArrayList<String>();
		setupFile();
	}

	private void setupFile() throws IOException 
	{
		FileInputStream in = null;
		try {
			in = new FileInputStream(filepath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		input.add("Author,Year,Title,Publication,Location,Date,Volume,Issue,Pages");
		while ((strLine = br.readLine()) != null) 
		{
			input.add(strLine);
		}
		
		in.close();
	}
	
	public void addToOutput() throws IOException
	{
		RegExTraversal traversal = new RegExTraversal();
		
		for (String string : input)
		{
			SimpleEntry<Boolean, String> entry = traversal.TransformInput(string);
			if (entry.getKey())
			{
				output.add(entry.getValue());
			}
			else
			{
				failedOutput.add(string);
			}
		}
		
		for(int i = 0; i < output.size(); i++) 
		{
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(destination + "\\testOut.txt", true))) 
            {
                String s;
	            s = output.get(i);
	            bw.write(s);
	            bw.newLine();
	            bw.flush();
            }
		}
		
		for(int i = 0; i < failedOutput.size(); i++) 
		{
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(destination + "\\FailedOutput.txt", true))) 
            {
                String s;
	            s = failedOutput.get(i);
	            bw.write(s);
	            bw.newLine();
	            bw.flush();
            }
		}
		
		return;
	}
	
	public List<String> getInput()
	{
		return input;
	}
	
	public List<String> getOutput()
	{
		return output;
	}
}
