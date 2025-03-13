package pt.pa.command;

import javax.management.InvalidAttributeValueException;
import java.util.Stack;

public class CommandManager {
    private Stack<Command> undoCommands;
    private Stack<Command> redoCommands;

    public CommandManager() {
        this.undoCommands = new Stack<>();
        this.redoCommands = new Stack<>();
    }

    public void executeCommand(Command c) throws InvalidAttributeValueException {
        undoCommands.push(c);
        c.execute();
        redoCommands.clear();
    }

    public void undo() {
        try{
            if(!undoCommands.isEmpty()){
                Command c = undoCommands.pop();
                c.unExecute();
                redoCommands.push(c);
            }
        } catch (InvalidAttributeValueException err){

        }

    }

    public void redo() {
        try{
            if(!redoCommands.isEmpty()){
                Command c = redoCommands.pop();
                c.execute();
                undoCommands.push(c);
            }
    } catch (InvalidAttributeValueException err){

    }
    }
}
