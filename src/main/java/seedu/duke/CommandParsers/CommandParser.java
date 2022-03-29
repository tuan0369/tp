package seedu.duke.CommandParsers;

import seedu.duke.Warehouse;
import util.exceptions.NullException;
import util.exceptions.WrongCommandException;

import java.util.HashMap;

public abstract class CommandParser {
    HashMap<String, String> matches;
    String userInput;
    Warehouse warehouse;

    public CommandParser(Warehouse warehouse){
        this.warehouse = warehouse;
    }

    protected abstract void extract_params();
    protected abstract void execute() throws WrongCommandException, NullException;

    public void parse(String userInput) throws WrongCommandException  {
        this.userInput = userInput;
        this.extract_params();
        this.execute();
    }
}
