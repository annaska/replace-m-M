
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Random;


/**
 * mMTask zur Übung mit Java, JUnit, Maven, Git und Travis.
 * m wird mit M ersetzt, der Rest des InputFiles soll nicht verändert werden.
 * 
 * TestFile: https://staff.fnwi.uva.nl/t.h.koornwinder/art/informal/KLSadd.tex
 * 
 */
public class MmReplace {

	static private String INPUT_DIR = System.getProperty("user.dir")+"/input";
	
	/**
	 * Liest Dateien aus dem Input-Ordner.
	 * Ersetzt im File alle ms durch Ms und alle Ms durch ms.
	 * 
	 */
	public static void main(String[] args) {
		
		File[] input = new File(INPUT_DIR).listFiles();
		
		if (input != null) {
			for (int i = 0; i < input.length; i++) {
				if (input[i].isFile() && input[i].canRead() && input[i].canWrite()) {
					replace(input[i]);
				}
			}
		} else {
			System.err.println("Input-Ordner ist leer/nicht vorhanden.");
		}
     }
	
	public static void replace (File input) {
		if(input == null){return;}
		
		RandomAccessFile file;
		
		try{
			file = new RandomAccessFile(input, "rw");
			String line;
			long pointerStart;
			
			file.seek(0);
			
			while((pointerStart=file.getFilePointer()) < file.length()
					& ((line=file.readLine()) != null)){
				
				//--------------------------------------------------------
				//Ersetzten im String
				
				int r = new Random().hashCode();
				while(line.contains(String.valueOf(r))){r++;}
				
				line=line.replace("m","m"+String.valueOf(r));
				line=line.replace("M","m");
				line=line.replace("m"+String.valueOf(r),"M");
				
				//--------------------------------------------------------
				//Ersetzen im File
				
				byte[] byteLine = line.getBytes(Charset.forName("UTF-8"));
				file.seek(pointerStart);
				for(int i=0; i < byteLine.length; i++){
					if(file.readByte()!=byteLine[i]){
						file.seek(file.getFilePointer()-1);
						file.write(byteLine[i]);
					}
				}
				file.seek(file.getFilePointer()+1);
			}
			file.close();
			
		} catch (IOException e) {
			System.err.println("Exception beim Ändern des Files.");
			e.printStackTrace();
		}
	}
}
