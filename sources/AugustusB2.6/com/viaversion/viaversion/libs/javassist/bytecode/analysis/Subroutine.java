// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.bytecode.analysis;

import java.util.Collection;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;

public class Subroutine
{
    private List<Integer> callers;
    private Set<Integer> access;
    private int start;
    
    public Subroutine(final int start, final int caller) {
        this.callers = new ArrayList<Integer>();
        this.access = new HashSet<Integer>();
        this.start = start;
        this.callers.add(caller);
    }
    
    public void addCaller(final int caller) {
        this.callers.add(caller);
    }
    
    public int start() {
        return this.start;
    }
    
    public void access(final int index) {
        this.access.add(index);
    }
    
    public boolean isAccessed(final int index) {
        return this.access.contains(index);
    }
    
    public Collection<Integer> accessed() {
        return this.access;
    }
    
    public Collection<Integer> callers() {
        return this.callers;
    }
    
    @Override
    public String toString() {
        return "start = " + this.start + " callers = " + this.callers.toString();
    }
}
