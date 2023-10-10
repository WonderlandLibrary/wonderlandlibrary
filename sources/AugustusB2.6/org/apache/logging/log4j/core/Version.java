// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core;

public class Version
{
    public static void main(final String[] args) {
        System.out.println(getProductString());
    }
    
    public static String getProductString() {
        final Package pkg = Version.class.getPackage();
        if (pkg == null) {
            return "Apache Log4j";
        }
        return String.format("%s %s", pkg.getSpecificationTitle(), pkg.getSpecificationVersion());
    }
}
