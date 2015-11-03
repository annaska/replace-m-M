import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class MmReplaceTest {
	
	static private String RES_DIR = System.getProperty("user.dir")+"/src/test/resources/KLSadd.tex";
	static private String KOPIE1_DIR = System.getProperty("user.dir")+"/src/test/resources/KLSadd-Kopie1.tex";
	static private String KOPIE2_DIR = System.getProperty("user.dir")+"/src/test/resources/KLSadd-Kopie2.tex";
	static private File file0, file1, file2;
	private BufferedReader bfr0, bfr1, bfr2;
	
	@BeforeClass
	static public void set() {
		try {
			Files.copy(Paths.get(RES_DIR), Paths.get(KOPIE1_DIR),StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Paths.get(RES_DIR), Paths.get(KOPIE2_DIR),StandardCopyOption.REPLACE_EXISTING);
			file0 = new File(RES_DIR);
			file1 = new File(KOPIE1_DIR);
			file2 = new File(KOPIE2_DIR);
			
			MmReplace.replace(file1);
			
			MmReplace.replace(file2);
			MmReplace.replace(file2);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Before
	public void setReader() {
		try {
			bfr0 = new BufferedReader(new FileReader(file0));
			bfr1 = new BufferedReader(new FileReader(file1));
			bfr2 = new BufferedReader(new FileReader(file2));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void close() {
		try {
			bfr0.close();
			bfr1.close();
			bfr2.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@AfterClass
	static public void delete() {
		file1.delete();
		file2.delete();
	}
	
	@Test
	public void testRichtigGeaendert() {
		String line0, line1;
		try {
			while((line0 = bfr0.readLine())!= null){
				line1 = bfr1.readLine();
				
				assertEquals(	line0.replace("M", "").replace("m", "M"),
								line1.replace("m", ""));
				
				assertEquals(	line0.replace("m", "").replace("M", "m"),
								line1.replace("M", ""));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	   
	@Test
	public void testZweimalAendern() {		
		String line0, line2;
		try {
			while((line0 = bfr0.readLine())!= null){
				line2 = bfr2.readLine();
				
				assertEquals(line0,line2);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}