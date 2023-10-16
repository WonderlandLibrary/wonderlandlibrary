package com.enjoytheban.command;

import com.enjoytheban.utils.Helper;

public abstract class Command
{
	//Holds the string for name
    private String name;
	//Holds the string for alias
    private String[] alias;
	//Holds the string for the syntax
    private String syntax;
	//Holds the string for any help needed
    private String help;
    
    //constructor
    public Command(final String name, String[] alias, final String syntax, final String help) {
        this.name = name.toLowerCase();
        this.syntax = syntax.toLowerCase();
        this.help = help;
        this.alias = alias;
    }
    
    //Execute command
    public abstract String execute(final String[] p0);
    
    //getters
    public String getName() {
        return this.name;
    }
    
    public String[] getAlias() {
    	return this.alias;
    }
    
    public String getSyntax() {
        return this.syntax;
    }
    
    public String getHelp() {
        return this.help;
    }
    
    //invalid syntax?
    public void syntaxError(final String msg) {
        Helper.sendMessage(String.format("§7Invalid command usage", msg));
    }
    
    //cases for a syntax error
    public void syntaxError(final byte errorType) {
        switch (errorType) {
            case 0: {
                this.syntaxError("bad argument");
                break;
            }
            case 1: {
                this.syntaxError("argument gay");
                break;
            }
        }
    }
}

