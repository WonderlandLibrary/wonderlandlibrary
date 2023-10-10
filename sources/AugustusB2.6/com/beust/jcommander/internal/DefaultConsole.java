// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.internal;

import java.io.IOException;
import com.beust.jcommander.ParameterException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DefaultConsole implements Console
{
    @Override
    public void print(final String s) {
        System.out.print(s);
    }
    
    @Override
    public void println(final String x) {
        System.out.println(x);
    }
    
    @Override
    public char[] readPassword(final boolean b) {
        try {
            return new BufferedReader(new InputStreamReader(System.in)).readLine().toCharArray();
        }
        catch (IOException ex) {
            throw new ParameterException(ex);
        }
    }
}
