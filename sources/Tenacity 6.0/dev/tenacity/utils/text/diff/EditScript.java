// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.text.diff;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class EditScript<T>
{
    private final List<EditCommand<T>> commands;
    private int lcsLength;
    private int modifications;
    
    public EditScript() {
        this.commands = new ArrayList<EditCommand<T>>();
        this.lcsLength = 0;
        this.modifications = 0;
    }
    
    public void append(final KeepCommand<T> command) {
        this.commands.add(command);
        ++this.lcsLength;
    }
    
    public void append(final InsertCommand<T> command) {
        this.commands.add(command);
        ++this.modifications;
    }
    
    public void append(final DeleteCommand<T> command) {
        this.commands.add(command);
        ++this.modifications;
    }
    
    public void visit(final CommandVisitor<T> visitor) {
        for (final EditCommand<T> command : this.commands) {
            command.accept(visitor);
        }
    }
    
    public int getLCSLength() {
        return this.lcsLength;
    }
    
    public int getModifications() {
        return this.modifications;
    }
}
