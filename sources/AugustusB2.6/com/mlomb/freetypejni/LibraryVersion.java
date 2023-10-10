// 
// Decompiled by Procyon v0.5.36
// 

package com.mlomb.freetypejni;

public class LibraryVersion
{
    private int major;
    private int minor;
    private int patch;
    
    public LibraryVersion(final int major, final int minor, final int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }
    
    public int getMajor() {
        return this.major;
    }
    
    public int getMinor() {
        return this.minor;
    }
    
    public int getPatch() {
        return this.patch;
    }
    
    @Override
    public String toString() {
        return this.major + "." + this.minor + "." + this.patch;
    }
}
