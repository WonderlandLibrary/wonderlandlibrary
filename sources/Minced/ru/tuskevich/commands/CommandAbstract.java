// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.commands;

import ru.tuskevich.util.chat.ChatUtility;
import ru.tuskevich.util.Utility;

public abstract class CommandAbstract implements Utility
{
    public String name;
    public String description;
    
    public CommandAbstract() {
        this.name = this.getClass().getAnnotation(Command.class).name();
        this.description = this.getClass().getAnnotation(Command.class).description();
    }
    
    public abstract void error();
    
    public abstract void execute(final String[] p0) throws Exception;
    
    public void sendMessage(final String message) {
        ChatUtility.addChatMessage(message);
    }
}
