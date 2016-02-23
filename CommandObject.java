class CommandObject {
	
	public static final String COMMAND_ADD = "add";
	public static final String COMMAND_DELETE = "delete";
	public static final String COMMAND_DISPLAY = "display";
	public static final String COMMAND_CLEAR = "clear";
	public static final String COMMAND_SORT = "sort";
	public static final String COMMAND_SEARCH = "search";
	public static final String COMMAND_EXIT = "exit";
	
	public static final String ERROR_EMPTY_TASK = "cannot add/search for empty task";
	public static final String ERROR_INVALID = "invalid command";
	public static final String ERROR_CLEAR = "invalid command format; to clear all tasks, simply type \"clear\"";
	public static final String ERROR_INTEGER = "invalid command format; please enter an integer (\"delete int\")";
	
	private static final int PARSED_COMMAND_INDEX = 0;
	private static final int PARSED_ARGS_INDEX = 1;
	
	private static final String EMPTY_STRING = "";
	
	// attributes
	private String _commandType = EMPTY_STRING;
	private String _commandArgs = EMPTY_STRING;
	private boolean _isValid = true;
	private String _errorMessage = EMPTY_STRING;
	
	// constructor
	CommandObject(String userInput) {
		String[] parsedInput = parse(userInput);
		_commandType = parsedInput[PARSED_COMMAND_INDEX];
		if (parsedInput.length > 1) {
			_commandArgs = parsedInput[PARSED_ARGS_INDEX];
		}
		checkValid(parsedInput);
	}
	
	private String[] parse(String userInput) {
		String[] parsedInput = userInput.split(" ", 2);
		parsedInput[PARSED_COMMAND_INDEX] = parsedInput[PARSED_COMMAND_INDEX].trim();
		if (parsedInput.length > 1) {
			parsedInput[PARSED_ARGS_INDEX] = parsedInput[PARSED_ARGS_INDEX].trim();
		}
		return parsedInput;
	}
	
	// sets String _errorMessage if command is invalid
	private void checkValid(String[] parsedInput) {
		if (_commandType.equals(COMMAND_ADD) || _commandType.equals(COMMAND_SEARCH)) {
			if (_commandArgs.equals(EMPTY_STRING)) {
				_isValid = false;
				_errorMessage = ERROR_EMPTY_TASK;
			}
			return;
		}
		else if (_commandType.equals(COMMAND_DELETE)) {
			try {
				Integer.parseInt(_commandArgs);
			}
			catch (Exception e) {
				_isValid = false;
				_errorMessage = ERROR_INTEGER;	
			}
			return;
		}
		else if (_commandType.equals(COMMAND_CLEAR)) {
			if (!_commandArgs.equals(EMPTY_STRING)) {
				_isValid = false;
				_errorMessage = ERROR_CLEAR;
			}
			return;
		}
		else if (_commandType.equals(COMMAND_DISPLAY)) {
			return;
		}
		else if (_commandType.equals(COMMAND_SORT)) {
			return;
		}
		else if (_commandType.equals(COMMAND_EXIT)) {
			return;
		}
		else {
			_isValid = false;
			_errorMessage = ERROR_INVALID;
		}
	}
	
	boolean isValid() {
		return _isValid;
	}
	
	String getErrorMessage() {
		return _errorMessage;
	}
	
	String getCommandType() {
		return _commandType;
	}
	
	String getCommandArgs() {
		return _commandArgs;
	}

}