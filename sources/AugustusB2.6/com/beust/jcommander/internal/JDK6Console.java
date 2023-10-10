// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.internal;

import com.beust.jcommander.ParameterException;
import java.io.PrintWriter;

public class JDK6Console implements Console
{
    private Object console;
    private PrintWriter writer;
    
    public JDK6Console(final Object o) throws Exception {
        this.console = o;
        this.writer = (PrintWriter)o.getClass().getDeclaredMethod("writer", (Class<?>[])new Class[0]).invoke(o, new Object[0]);
    }
    
    @Override
    public void print(final String s) {
        this.writer.print(s);
    }
    
    @Override
    public void println(final String x) {
        this.writer.println(x);
    }
    
    @Override
    public char[] readPassword(final boolean b) {
        try {
            this.writer.flush();
            if (b) {
                return ((String)this.console.getClass().getDeclaredMethod("readLine", (Class<?>[])new Class[0]).invoke(this.console, new Object[0])).toCharArray();
            }
            return (char[])this.console.getClass().getDeclaredMethod("readPassword", (Class<?>[])new Class[0]).invoke(this.console, new Object[0]);
        }
        catch (Exception ex) {
            throw new ParameterException(ex);
        }
    }
}
