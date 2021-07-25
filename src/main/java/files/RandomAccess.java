package files;

import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccess {
	
	/**
	 * Treat the file as an array of (unsigned) 8-bit values and sort them
	 * in-place using a bubble-sort algorithm.
	 * You may not read the whole file into memory! 
	 * @param file
	 */
	public static void sortBytes(RandomAccessFile file) throws IOException {
		try{
			long len = file.length();
			boolean swap;
			int temp;
			int next;
			int curr;
			for (int i = 0 ; i < len-1 ; i++){ //go over the all arry
				swap = false;
				for ( int j = 0; j < len-i-1; j++){ //check if the num is bigger than the others
					file.seek(j);
					curr = file.read();
					next = file.read();
					if ( curr > next){ //swap
						temp = curr;
						file.seek(j);
						file.write(next);
						file.write(temp);
						swap = true;
					}
				}
				if (!swap){
					return;
				}
			}
		}
		catch (IOException e){
			System.err.println("Error:" + e);
		}
	}
	
	/**
	 * Treat the file as an array of unsigned 24-bit values (stored MSB first) and sort
	 * them in-place using a bubble-sort algorithm. 
	 * You may not read the whole file into memory! 
	 * @param file
	 * @throws IOException
	 */
	public static void sortTriBytes(RandomAccessFile file) throws IOException {
		try {
			byte[] indexcurr = new byte[3]; //in order to take care of the 24-bit
			byte[] indexnext = new byte[3]; //in order to take return the 24-bit
			long len = file.length();
			boolean swap;
			int next = 0;
			int curr = 0;
			for (int i = 0 ; i < len-3-1 ; i+=3) { //go over the all arry
				swap = false;
				for (int j = 0; j < len - i - 3 - 1; j+=3) {  //check if the num is bigger than the others
					file.seek(j);
					file.read(indexcurr);
					for (byte b : indexcurr) { //for the curr number , move the bits byte left and union with the new byte ( keep the unsign)
						curr <<= 8;
						curr |= (b & 0xff);
					}
					file.read(indexnext);
					for (byte b : indexnext) { //for the next number, move the bits byte left and union with the new byte ( keep the unsign)
						next <<= 8;
						next |= (b & 0xff);
					}
					if (curr > next) { // check how bigger and swap if needed
						file.seek(j);
						file.write(indexnext);
						file.write(indexcurr);
						swap = true;
					}
					curr = 0;
					next = 0;
				}
				if (!swap) {
					return;
				}
			}
		}
		catch (IOException e){
			System.err.println("Error:" + e);
		}
	}
}
