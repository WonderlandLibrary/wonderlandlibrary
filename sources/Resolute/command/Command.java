// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.command;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public abstract class Command
{
    public String name;
    public String description;
    public String syntax;
    public List<String> aliases;
    
    public Command(final String name, final String description, final String syntax, final String... aliases) {
        this.aliases = new ArrayList<String>();
        this.name = name;
        this.description = description;
        this.syntax = syntax;
        this.aliases = Arrays.asList(aliases);
    }
    
    public abstract void onCommand(final String[] p0, final String p1);
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public String getSyntax() {
        return this.syntax;
    }
    
    public void setSyntax(final String syntax) {
        this.syntax = syntax;
    }
    
    public List<String> getAliases() {
        return this.aliases;
    }
    
    public void setAliases(final List<String> aliases) {
        this.aliases = aliases;
    }
}
