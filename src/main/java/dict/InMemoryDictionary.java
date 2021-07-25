package dict;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;


/**
 * Implements a persistent dictionary that can be held entirely in memory.
 * When flushed, it writes the entire dictionary back to a file.
 * 
 * The file format has one keyword per line:
 * <pre>word:def1:def2:def3,...</pre>
 * 
 * Note that an empty definition list is allowed (in which case the entry would have the form: <pre>word:</pre> 
 * 
 * @author talm
 *
 */
public class InMemoryDictionary extends TreeMap<String,String> implements PersistentDictionary  {
	private static final long serialVersionUID = 1L; // (because we're extending a serializable class)
	private File dictFile;

	public InMemoryDictionary(File dictFile) {
		this.dictFile = dictFile;
	}
	
	@Override
	public void open() throws IOException {
		String word = "";
		String def = "";
		String lineFile;
		boolean found = false;
		this.clear();
		if (!dictFile.exists()){ //if the file dont exists we cant open him.
			return;
		}
		try {
			BufferedReader readMyFile = new BufferedReader(new FileReader(this.dictFile));
			lineFile = readMyFile.readLine();
			while (lineFile != null) { //go over all the lins in the file
				for ( int i = 0; i < lineFile.length(); i++){ //in every line spares to word and def
					if( lineFile.charAt(i) == ':'){
						found = true;
					}
					else {
						if (!found) {
							word += lineFile.charAt(i);
						}
						if (found) {
							def += lineFile.charAt(i);
						}
					}
				}
				super.put(word , def);
				word = "";
				def = "";
				found = false;
				lineFile = readMyFile.readLine();
			}
			readMyFile.close();
		}
		catch (IOException e){
			System.err.println("Error:" + e);
		}
	}

	@Override
	public void close() throws IOException {
		try {
			this.dictFile.createNewFile(); // check if the file exist, if not, create one.
			BufferedWriter writeMyFile = new BufferedWriter(new FileWriter(this.dictFile));
			for (Map.Entry<String,String> lineRead : this.entrySet()){ //go over all the key&val in the line and write them to the file
				if ( lineRead.getValue() != null){
					writeMyFile.write(lineRead.getKey() + ":" + lineRead.getValue());
					writeMyFile.newLine();
				}
			}
			writeMyFile.close();
		}
		catch (IOException e){
			System.err.println("Error:" + e);
		}
	}
}
