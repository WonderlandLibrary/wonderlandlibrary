// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.cmd.macro;

public class Macro
{
    public String value;
    public int key;
    
    public Macro(final int key, final String macroValue) {
        this.key = key;
        this.value = macroValue;
    }
    
    public int getKey() {
        return this.key;
    }
    
    public String getValue() {
        return this.value;
    }
}
