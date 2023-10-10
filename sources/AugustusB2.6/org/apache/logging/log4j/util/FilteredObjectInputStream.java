// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.util;

import java.util.HashSet;
import java.util.Arrays;
import java.util.Iterator;
import java.io.InvalidObjectException;
import java.io.ObjectStreamClass;
import java.io.InputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Collection;
import java.util.Set;
import java.io.ObjectInputStream;

public class FilteredObjectInputStream extends ObjectInputStream
{
    private static final Set<String> REQUIRED_JAVA_CLASSES;
    private static final Set<String> REQUIRED_JAVA_PACKAGES;
    private final Collection<String> allowedExtraClasses;
    
    public FilteredObjectInputStream() throws IOException, SecurityException {
        this.allowedExtraClasses = (Collection<String>)Collections.emptySet();
    }
    
    public FilteredObjectInputStream(final InputStream inputStream) throws IOException {
        super(inputStream);
        this.allowedExtraClasses = (Collection<String>)Collections.emptySet();
    }
    
    public FilteredObjectInputStream(final Collection<String> allowedExtraClasses) throws IOException, SecurityException {
        this.allowedExtraClasses = allowedExtraClasses;
    }
    
    public FilteredObjectInputStream(final InputStream inputStream, final Collection<String> allowedExtraClasses) throws IOException {
        super(inputStream);
        this.allowedExtraClasses = allowedExtraClasses;
    }
    
    public Collection<String> getAllowedClasses() {
        return this.allowedExtraClasses;
    }
    
    @Override
    protected Class<?> resolveClass(final ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        final String name = desc.getName();
        if (!isAllowedByDefault(name) && !this.allowedExtraClasses.contains(name)) {
            throw new InvalidObjectException("Class is not allowed for deserialization: " + name);
        }
        return super.resolveClass(desc);
    }
    
    private static boolean isAllowedByDefault(final String name) {
        return isRequiredPackage(name) || FilteredObjectInputStream.REQUIRED_JAVA_CLASSES.contains(name);
    }
    
    private static boolean isRequiredPackage(final String name) {
        for (final String packageName : FilteredObjectInputStream.REQUIRED_JAVA_PACKAGES) {
            if (name.startsWith(packageName)) {
                return true;
            }
        }
        return false;
    }
    
    static {
        REQUIRED_JAVA_CLASSES = new HashSet<String>(Arrays.asList("java.math.BigDecimal", "java.math.BigInteger", "java.rmi.MarshalledObject", "[B"));
        REQUIRED_JAVA_PACKAGES = new HashSet<String>(Arrays.asList("java.lang.", "java.time.", "java.util.", "org.apache.logging.log4j.", "[Lorg.apache.logging.log4j."));
    }
}
