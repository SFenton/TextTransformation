/*
 * This class represents a citation in the document being read from.
 * Contains a default set of fields that are represented by strings.
 */
public class Citation {
	public String Authors, Date1, Title, Publication, Location, Date2, Volume,
			Issue, Pages;

	public Citation() {
		Authors = "";
		Date1 = "";
		Title = "";
		Publication = "";
		Location = "";
		Date2 = "";
		Volume = "";
		Issue = "";
		Pages = "";
	}

	public String toCSVString() {
		String csvstring = "\"" + Authors + "\",\"" + Date1 + "\",\"" + Title
				+ "\",\"" + Publication + "\",\"" + Location + "\",\"" + Date2
				+ "\",\"" + Volume + "\",\"" + Issue + "\",\"" + Pages + "\"\n";

		return csvstring;
	}
}
