/**
 * TextBuddy is a program that allows users to edit text files of the format *.txt.
 * 
 * Only the following commands are recognised:
 * 		"add String"	: adds the String to the text file and creates a new line,
 * 		"delete Int"	: deletes the the line in the Int-th position,
 * 		"display"		: displays all lines in the text file in a numbered fashion,
 * 		"clear"			: deletes all lines in the text file,
 * 		"sort"			: sorts all lines lexicographically and displays them,
 * 		"search String"	: displays all the lines containing the String with their corresponding indices,
 * 		"exit"			: exits TextBuddy.
 * 
 * Any white spaces entered before a command will render the command invalid, and the command will be ignored.
 * Excess white spaces at the end of any line will be ignored.
 * Any command entered with the first word not matching any of the above commands will be ignored.
 * Command keywords are only recognised in lower case.
 * 
 * If the user enters the command "add" without specifying a line, the command will be ignored.
 * Excess white spaces between the keyword "add" and the line to be added will be ignored.
 * Hence a command like "add    " is invalid and will be ignored.
 * 
 * If the user enters the command "delete" without specifying any other input, the command will be ignored.
 * If the user enters character(s) that are not integers, the command will be ignored.
 * If the user enters the command "delete Int" when the text file is empty, the command will be ignored.
 * If "Int" is greater than the number of lines in the text file, the command will be ignored. 
 * 
 * If the user enters the commands "display" or "sort" with any other words in the same line,
 * the program will execute the command "display"/"sort" and ignore the following words.
 * 
 * If the user enters the commands "clear" with any other words in the same line, the command will be ignored.
 * This is to prevent the user mistaking the command "clear" for "delete" and erasing all content by accident.
 * 
 * If the user enters the command "search" without specifying a line, the command will be ignored.
 * Excess white spaces between the keyword "search" and the String to be searched will be ignored.
 * 
 * If the user enters the command "exit" followed by any other words in the same line,
 * the program will exit and the following words will be ignored.
 * 
 * The text file is saved after each command.
 * 
 * The user should not directly modify the text file while TextBuddy is running.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class TextBuddy {
	
	public static final String COMMAND_PROMPT = "command:";
	public static final String COMMAND_ADD = "add";
	public static final String COMMAND_DELETE = "delete";
	public static final String COMMAND_DISPLAY = "display";
	public static final String COMMAND_CLEAR = "clear";
	public static final String COMMAND_SORT = "sort";
	public static final String COMMAND_SEARCH = "search";
	public static final String COMMAND_EXIT = "exit";
	
	public static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %s is ready for use";
	public static final String MESSAGE_ADDED = "added to %s: \"%s\"";
	public static final String MESSAGE_DELETED = "deleted from %s: \"%s\"";
	public static final String MESSAGE_CLEARED = "all content deleted from %s";
	public static final String MESSAGE_EMPTY = "%s is empty";
	public static final String MESSAGE_NO_MATCH = "no matches found";
	public static final String MESSAGE_SORTED = "%s has been sorted lexicographically";
	public static final String MESSAGE_INVALID_INDEX = "index out of range; %s currently only has %d task(s)";
	public static final String MESSAGE_GOODBYE = "thank you for using TextBuddy, goodbye";
	
	public static final String ERROR_OPEN = "error creating/opening file";
	public static final String ERROR_FATAL = "TextBuddy failed to run";
	
	private static Scanner scanner = new Scanner(System.in);
	
	public static final int PARSED_COMMAND_INDEX = 0;
	public static final int PARSED_ARGS_INDEX = 1;
	
	public static final String EMPTY_STRING = "";

	// attributes
	private File _file;
	private String _fileName;
	private FileWriter fWriter;
	private BufferedWriter bWriter;
	private FileReader fReader;
	private BufferedReader bReader;
	private ArrayList<String> _taskList = new ArrayList<String>();
	
	// constructor
	public TextBuddy(String fileName) {
		try {
		_file = new File(fileName);
		_fileName = fileName;
		openFile();
		readFromFile();
		}
		catch (IOException e) {
			showToUser(ERROR_FATAL);
			exit();
		}
	}
	
	public static void main(String[] args) throws IOException {
		TextBuddy textBuddy = new TextBuddy(args[0]);
		textBuddy.runTillExit();
	}
	
	private void readFromFile() throws IOException {
		String line = EMPTY_STRING;
		fReader = new FileReader(_fileName);
		bReader = new BufferedReader(fReader);
		while ((line = bReader.readLine()) != null) {
			_taskList.add(line);
		}
		bReader.close();
	}
	
	private void showToUser(String message) {
		System.out.println(message);
	}
	
	private void printEmptyLine() {
		System.out.println();
	}
	
	private void displayWelcomeMessage() {
		showToUser(String.format(MESSAGE_WELCOME, _fileName));
	}
	
	private void openFile() throws IOException {
			fWriter = new FileWriter(_fileName, true);
			fWriter.close();
	}
	
	private String formatTaskToDisplay(int n, String task) {
		String output = Integer.toString(n) + ". " + task;
		return output;
	}
	
	private void promptUser() {
		showToUser(COMMAND_PROMPT);
	}
	
	private void copyToTempFile(File tempFile) throws IOException {
		fWriter = new FileWriter(tempFile);
		bWriter = new BufferedWriter(fWriter);
		String line = EMPTY_STRING;
		for (int i=0; i<_taskList.size(); i++) {
			line = _taskList.get(i);
			bWriter.write(line);
			bWriter.newLine();	
			}
		bWriter.close();
	}
		
	private void replaceFile(File tempFile) {
			tempFile.renameTo(_file);
	}
	
	// store matching lines with their corresponding indices
	private ArrayList<String> findMatches(String keywords) {
		String line = EMPTY_STRING;
		int index = 1;
		ArrayList<String> listOfMatches = new ArrayList<String>(); 
		for (int i=0; i<_taskList.size(); i++) {
			line = _taskList.get(i);
			if (line.contains(keywords)) {
				line = formatTaskToDisplay(index, line);
				listOfMatches.add(line);
			}
			index++;
		}
		return listOfMatches;
	}
	
	private void addTask(String text) throws IOException {
		fWriter = new FileWriter(_fileName, true);
		bWriter = new BufferedWriter(fWriter);
		bWriter.write(text);
		bWriter.newLine();
		bWriter.close();
		_taskList.add(text);
		showToUser(String.format(MESSAGE_ADDED, _fileName, text));
	}
	
	private void displayTasks() throws IOException {
		if (_taskList.isEmpty()) {
			showToUser(String.format(MESSAGE_EMPTY, _fileName));
			return;
		}
		String line = EMPTY_STRING;
		int index = 1;
		for (int i=0; i<_taskList.size(); i++) {
			line = formatTaskToDisplay(index, _taskList.get(i));
			showToUser(line);
			index++;
		}
	}
	
	private void deleteTask(String commandArgs) throws IOException {
		if (_taskList.isEmpty()) {
			showToUser(String.format(MESSAGE_EMPTY, _fileName));
			return;
		}
		int taskNum = Integer.parseInt(commandArgs);
		if (_taskList.size() < taskNum) {
			showToUser(String.format(MESSAGE_INVALID_INDEX, _fileName, _taskList.size()));
			return;
		}
		int index = taskNum - 1;
		String deletedLine = _taskList.get(index);
		_taskList.remove(index);
		File tempFile = new File("tempFile.txt");
		copyToTempFile(tempFile);
		replaceFile(tempFile);
		showToUser(String.format(MESSAGE_DELETED, _fileName, deletedLine));
	}
	
	private void clearTasks() throws IOException {
		_file.delete();
		openFile();
		_taskList.clear();
		showToUser(String.format(MESSAGE_CLEARED, _fileName));
	}
	
	// sort and display sorted tasks
	private void sortTasks() throws IOException {
		if (_taskList.isEmpty()) {
			showToUser(String.format(MESSAGE_EMPTY, _fileName));
			return;
		}
		Collections.sort(_taskList);
		File tempFile = new File("tempFile.txt");
		copyToTempFile(tempFile);
		replaceFile(tempFile);
		displayTasks();
	}
	
	// matching lines shown with indices for ease of operation
	private void searchTasks(String keywords) {
		if (_taskList.isEmpty()) {
			showToUser(String.format(MESSAGE_EMPTY, _fileName));
			return;
		}
		ArrayList<String> listOfMatches = findMatches(keywords);
		if (listOfMatches.isEmpty()) {
			showToUser(MESSAGE_NO_MATCH);
		}
		else {
			for (int i=0; i<listOfMatches.size(); i++) {
				showToUser(listOfMatches.get(i));
			}
		}
	}
	
	private void exit() {
		scanner.close();
		showToUser(MESSAGE_GOODBYE);
		printEmptyLine();
		System.exit(0);
	}
	
	private String getUserInput() {
		promptUser();
		String userInput = scanner.nextLine();
		userInput.trim();
		return userInput;
	}
	
	private void executeCommand(String userInput) throws IOException {
		CommandObject cmd = new CommandObject(userInput);
		String commandType = cmd.getCommandType();
		String commandArgs = cmd.getCommandArgs();
		if (cmd.isValid()) {
			switch(commandType) {
			case COMMAND_ADD:
				addTask(commandArgs);
				break;
			case COMMAND_DELETE:
				deleteTask(commandArgs);
				break;
			case COMMAND_CLEAR:
				clearTasks();
				break;
			case COMMAND_DISPLAY:
				displayTasks();
				break;
			case COMMAND_SORT:
				sortTasks();
				break;
			case COMMAND_SEARCH:
				searchTasks(commandArgs);
				break;
			case COMMAND_EXIT:
				exit();
				break;
			default:
				break;
			}
		}
		else {
			String errorType = cmd.getErrorMessage();
			showToUser(errorType);
		}
	}
	
	private void runTillExit() throws IOException {
		displayWelcomeMessage();
		while (true) {
			String userInput = getUserInput();
			printEmptyLine();
			executeCommand(userInput);
			printEmptyLine();
		}
	}

}
