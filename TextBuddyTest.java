import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class TextBuddyTest {
	
	private static Class c;
	private static Class[] cArgs = new Class[1];
	private static Method add;
	private static Method delete;
	private static Method sort;
	private static Method clear;
	private static Method search;
	private static Method display;
	private static Field taskList;
	
	private static final String FILE_ADD = "toAdd.txt";
	private static final String FILE_DELETE = "toDelete.txt";
	private static final String FILE_SORT = "toSort.txt";
	private static final String FILE_CLEAR = "toClear.txt";
	private static final String FILE_SEARCH = "toSearch.txt";
	private static final String FILE_DISPLAY = "toDisplay.txt";
	
	private static final String EXPECTED_ADD = "expectedAdd.txt";
	private static final String EXPECTED_DELETE = "expectedDelete.txt";
	private static final String EXPECTED_SORT = "expectedSort.txt";
	
	@BeforeClass
	public static void setUp() throws Exception {
		
		c = Class.forName("TextBuddy");
		cArgs[0] = String.class;
		add = c.getDeclaredMethod("addTask", cArgs);
		add.setAccessible(true);
		delete = c.getDeclaredMethod("deleteTask", cArgs);
		delete.setAccessible(true);
		sort = c.getDeclaredMethod("sortTasks", null);
		sort.setAccessible(true);		
		clear = c.getDeclaredMethod("clearTasks", null);
		clear.setAccessible(true);
		search = c.getDeclaredMethod("findMatches", cArgs);
		search.setAccessible(true);
		cArgs = new Class[2];
		cArgs[0] = int.class;
		cArgs[1] = String.class;
		display = c.getDeclaredMethod("formatTaskToDisplay", cArgs);
		display.setAccessible(true);
		taskList = c.getDeclaredField("_taskList");
		taskList.setAccessible(true);
		
		File tempFile;
		FileWriter fw;
		PrintWriter pw;
		
		// set up toAdd.txt
		File toAdd = new File(FILE_ADD);
		tempFile = new File("tempFile.txt");
		fw = new FileWriter(tempFile);
		fw.close();
		tempFile.renameTo(toAdd);
		
		// set up expectedAdd.txt
		File expectedAdd = new File(EXPECTED_ADD);
		tempFile = new File("tempFile.txt");
		fw = new FileWriter(tempFile);
		pw = new PrintWriter(fw);
		pw.println("abd");
		pw.println("abc");
		pw.println("zzz");
		pw.println("man");
		pw.println("pot");
		fw.close();
		tempFile.renameTo(expectedAdd);
		
		// set up toClear.txt
		File toClear = new File(FILE_CLEAR);
		tempFile = new File("tempFile.txt");
		fw = new FileWriter(tempFile);
		pw = new PrintWriter(fw);
		pw.println("aaa");
		pw.println("bbb");
		pw.println("ccc");
		pw.close();
		tempFile.renameTo(toClear);
		
		// set up toDelete.txt
		File toDelete = new File(FILE_DELETE);
		tempFile = new File("tempFile.txt");
		fw = new FileWriter(tempFile);
		pw = new PrintWriter(fw);
		pw.println("abc");
		pw.println("abd");
		pw.println("man");
		pw.println("pot");
		pw.println("zzz");
		pw.close();
		tempFile.renameTo(toDelete);
		
		// set up expectedDelete.txt
		File expectedDelete = new File(EXPECTED_DELETE);
		tempFile = new File("tempFile.txt");
		fw = new FileWriter(tempFile);
		pw = new PrintWriter(fw);
		pw.println("abc");
		pw.println("pot");
		pw.println("zzz");
		fw.close();
		tempFile.renameTo(expectedDelete);
		
		// set up toSort.txt
		File toSort = new File(FILE_SORT);
		tempFile = new File("tempFile.txt");
		fw = new FileWriter(tempFile);
		pw = new PrintWriter(fw);
		pw.println("abd");
		pw.println("abc");
		pw.println("zzz");
		pw.println("man");
		pw.println("pot");
		pw.close();
		tempFile.renameTo(toSort);
		
		// set up expectedSort.txt
		File expectedSort = new File(EXPECTED_SORT);
		tempFile = new File("tempFile.txt");
		fw = new FileWriter(tempFile);
		pw = new PrintWriter(fw);
		pw.println("abc");
		pw.println("abd");
		pw.println("man");
		pw.println("pot");
		pw.println("zzz");
		pw.close();
		tempFile.renameTo(expectedSort);
		
		// set up toSearch.txt
		File toSearch = new File(FILE_SEARCH);
		tempFile = new File("tempFile.txt");
		fw = new FileWriter(tempFile);
		pw = new PrintWriter(fw);
		pw.println("boom");
		pw.println("aaa");
		pw.println("good");
		pw.println("mushroom");
		pw.println("zzz");
		pw.println("bbb");
		pw.println("food");
		pw.println("ccc");
		pw.println("ddd");
		pw.close();
		tempFile.renameTo(toSearch);
		
		// set up toDisplay.txt
		File toDisplay = new File(FILE_DISPLAY);
		tempFile = new File("tempFile.txt");
		fw = new FileWriter(tempFile);
		pw = new PrintWriter(fw);
		pw.println("aaa");
		pw.println("bbb");
		pw.println("ccc");
		pw.println("ddd");
		pw.close();
		tempFile.renameTo(toDisplay);
		
	}
	
	@AfterClass
	public static void cleanUp() {
		File file = new File(FILE_ADD);
		file.delete();
		file = new File(FILE_DELETE);
		file.delete();
		file = new File(FILE_SORT);
		file.delete();
		file = new File(FILE_CLEAR);
		file.delete();
		file = new File(FILE_SEARCH);
		file.delete();
		file = new File(FILE_DISPLAY);
		file.delete();
		file = new File(EXPECTED_ADD);
		file.delete();
		file = new File(EXPECTED_DELETE);
		file.delete();
		file = new File(EXPECTED_SORT);
		file.delete();
	}
		
	public void assertEqualsArrayLists(TextBuddy tbTest, ArrayList<String> expected) throws Exception {
		ArrayList<String> testList = (ArrayList<String>) taskList.get(tbTest);
		assertEquals(expected, testList);
	}
	
	public void assertEqualsFileContents(String expectedFileName, String actualFileName) throws Exception {
		File testFile = new File(actualFileName);
		File expectedFile = new File(expectedFileName); // file prepared externally
		Scanner scannerTest = new Scanner(testFile);
		Scanner scannerExpected = new Scanner(expectedFile);
		scannerTest.useDelimiter("\\Z");
		scannerExpected.useDelimiter("\\Z");
		String actualContent = scannerTest.next();
		String expectedContent = scannerExpected.next();
		scannerTest.close();
		scannerExpected.close();
		assertEquals(expectedContent, actualContent);
	}
	
	@Test
	public void addTest() throws Exception {
		
		// set up expected ArrayList<String>
		ArrayList<String> expectedList = new ArrayList<String>();
		expectedList.add("abd");
		expectedList.add("abc");
		expectedList.add("zzz");
		expectedList.add("man");
		expectedList.add("pot");
		
		// invoke private add method
		TextBuddy tbTest = new TextBuddy(FILE_ADD);
		add.invoke(tbTest, "abd");
		add.invoke(tbTest, "abc");
		add.invoke(tbTest, "zzz");
		add.invoke(tbTest, "man");
		add.invoke(tbTest, "pot");
		
		assertEqualsArrayLists(tbTest, expectedList);
		
		assertEqualsFileContents(EXPECTED_ADD, FILE_ADD);
		
	}
	
	@Test
	public void sortTest() throws Exception {
		
		// set up expected ArrayList<String>
		ArrayList<String> expectedList = new ArrayList<String>();
		expectedList.add("abc");
		expectedList.add("abd");
		expectedList.add("man");
		expectedList.add("pot");
		expectedList.add("zzz");
		
		
		// invoke private sort method
		TextBuddy tbTest = new TextBuddy(FILE_SORT);
		sort.invoke(tbTest, null);
		
		assertEqualsArrayLists(tbTest, expectedList);
	
		assertEqualsFileContents(EXPECTED_SORT, FILE_SORT);
		
	}
	
	@Test
	public void deleteTest() throws Exception {
		
		// set up expected ArrayList<String>
		ArrayList<String> expectedList = new ArrayList<String>();
		expectedList.add("abc");
		expectedList.add("pot");
		expectedList.add("zzz");

		// invoke private delete method
		TextBuddy tbTest = new TextBuddy(FILE_DELETE);
		delete.invoke(tbTest, "2");
		delete.invoke(tbTest, "2");
		
		assertEqualsArrayLists(tbTest, expectedList);
		
		assertEqualsFileContents(EXPECTED_DELETE, FILE_DELETE);
		
	}
	
	@Test
	public void clearTest() throws Exception {
		
		// set up expected ArrayList<String>
		ArrayList<String> expectedList = new ArrayList<String>();
		expectedList.clear();
		
		// invoke private clear method
		TextBuddy tbTest = new TextBuddy(FILE_CLEAR);
		clear.invoke(tbTest, null);
		
		assertEqualsArrayLists(tbTest, expectedList);
		
		// check that toClear.txt is empty
		File testFile = new File(FILE_CLEAR);
		Scanner scannerTest = new Scanner(testFile);
		assertFalse(scannerTest.hasNext());
		scannerTest.close();
		
	}
	
	@Test
	public void searchTest() throws Exception {
		
		// set up expected ArrayList<String>
		ArrayList<String> expectedList = new ArrayList<String>();
		expectedList.add("1. boom");
		expectedList.add("3. good");
		expectedList.add("4. mushroom");
		expectedList.add("7. food");
		
		// invoke private findMatches method
		TextBuddy tbTest = new TextBuddy(FILE_SEARCH);
		ArrayList<String> matches = (ArrayList<String>) search.invoke(tbTest, "oo");
		
		assertEquals(expectedList, matches);
		
	}
	
	@Test
	public void displayTest() throws Exception {
		
		// set up expected ArrayList<String>
		ArrayList<String> expectedList = new ArrayList<String>();
		expectedList.add("1. aaa");
		expectedList.add("2. bbb");
		expectedList.add("3. ccc");
		expectedList.add("4. ddd");
		
		// invoke private formatTaskToDisplay method
		// compare line by line
		TextBuddy tbTest = new TextBuddy(FILE_DISPLAY);
		String line;
		ArrayList<String> testList = (ArrayList<String>) taskList.get(tbTest);
		for (int i=0; i<testList.size(); i++) {
			line = (String) display.invoke(tbTest, i+1, testList.get(i));
			assertEquals(expectedList.get(i), line);
		}
		
	}
	
}