package files;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Streams {
	/**
	 * Read from an InputStream until a quote character (") is found, then read
	 * until another quote character is found and return the bytes in between the two quotes. 
	 * If no quote character was found return null, if only one, return the bytes from the quote to the end of the stream.
	 * @param in
	 * @return A list containing the bytes between the first occurrence of a quote character and the second.
	 */
	public static List<Byte> getQuoted(InputStream in) throws IOException {
        int i = in.read(); //the counter for the input
        List<Byte> listByte = new ArrayList<>(); // the return output
        try { // if the byte we read is illegal and equal to the quote char send exceptoin
            while ((i != -1) && ((char) i != '"')) {
                i = in.read();
            }
        } catch (IOException e) {
            System.err.println("Error:" + e);
        } finally { // adding the bytes to the list
            in.close();
            if (i == -1) {
                return null;
            }
            i = in.read();
            while ((i != -1) && ((char) i != '"')) {
                listByte.add((byte) i);
                i = in.read();
            }
        }
        return listByte;
    }

	
	
	/**
	 * Read from the input until a specific string is read, return the string read up to (not including) the endMark.
	 * @param in the Reader to read from
	 * @param endMark the string indicating to stop reading. 
	 * @return The string read up to (not including) the endMark (if the endMark is not found, return up to the end of the stream).
	 */
	public static String readUntil(Reader in, String endMark) throws IOException {
        StringBuilder ans = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        int i = in.read();
        int j = 0;
		try{
		    while (( i != -1) && (i != (int)endMark.charAt(0))){ //append till the endmark
		        ans.append((char)i);
		        i = in.read();
            }
        } catch (IOException e) {
            System.err.println("Error:" + e);
        }
		finally {
		    if ( i == -1){  //if we finish the loop because the ens of the file, return the ans.
		        return ans.toString();
            }
		    while (i != -1){ //keep going till stop reading from file
		        j = 0;
                while ((j < endMark.length()) && ((int)endMark.charAt(j) == i)){ // keep going til the next endmark
                    temp.append((char)i);
                    j++;
                    i = in.read();
                }
                if (j >= endMark.length()){ // if finishing because we get to the endmark, return ans.
                    return ans.toString();
                }
                else{ // append the word
                    if (j != 0) {
                        ans.append(temp);
                        temp.delete(0, j);
                    }
                    ans.append((char)i);
                    i = in.read();
                }
            }
		    return ans.toString();
        }
	}
	
	/**
	 * Copy bytes from input to output, ignoring all occurrences of badByte.
	 * @param in
	 * @param out
	 * @param badByte
	 */
	public static void filterOut(InputStream in, OutputStream out, byte badByte) throws IOException {
		// TODO: Implement
        int i = in.read();
        try{
            while ( i != -1){ //keep goint till stop reading.
                if ((byte)i == badByte){ //if its the badbyte, read the next byte
                    i = in.read();
                }
                while ((i != -1)&&((byte)i != badByte)){ //whil the byte not the bad byte, write it to the out stream.
                    out.write((byte)i);
                    i = in.read();
                }
            }
        }
        catch (IOException e){
            System.err.println("Error:" + e);
        }
        finally {
            in.close();
        }
	}
	
	/**
	 * Read a 40-bit (unsigned) integer from the stream and return it.
     * The number is represented as five bytes,
	 * with the most-significant byte first. 
	 * If the stream ends before 5 bytes are read, return -1.
	 * @param in
	 * @return the number read from the stream
	 */
	public static long readNumber(InputStream in) throws IOException {
        int i = in.read();
        long ans = 0;
        int count = 0;
	    try{
	        while (( i != -1 ) && (count < 5)){ // shift the ans byte left and add the new byte.
                ans = ans<<8;
                ans = i | ans;
                i = in.read();
                count++;
            }
            if (count < 5 ){ //if the word leas than 5 return -1.
                return -1;
            }
            return ans;
        }
        catch (IOException e){
            System.err.println("Error:" + e);
        }
        finally {
            in.close();
            if (count < 5 ){
                return -1;
            }
            return ans;
        }
	}
}
