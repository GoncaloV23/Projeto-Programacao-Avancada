package pt.pa.command;

import javax.management.InvalidAttributeValueException;

public interface Command {
    void execute() throws InvalidAttributeValueException;
    void unExecute() throws InvalidAttributeValueException;
}
