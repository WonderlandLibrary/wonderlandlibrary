// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.commands.macro;

public class Macro
{
    private String message;
    private int key;
    
    public Macro(final String message, final int key) {
        this.message = message;
        this.key = key;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
    
    public int getKey() {
        return this.key;
    }
    
    public void setKey(final int key) {
        this.key = key;
    }
}
