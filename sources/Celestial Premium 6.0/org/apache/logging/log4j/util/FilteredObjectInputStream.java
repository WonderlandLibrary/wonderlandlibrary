/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FilteredObjectInputStream
extends ObjectInputStream {
    private static final Set<String> REQUIRED_JAVA_CLASSES = new HashSet<String>(Arrays.asList("java.math.BigDecimal", "java.math.BigInteger", "java.rmi.MarshalledObject", "[B"));
    private static final Set<String> REQUIRED_JAVA_PACKAGES = new HashSet<String>(Arrays.asList("java.lang.", "java.time.", "java.util.", "org.apache.logging.log4j.", "[Lorg.apache.logging.log4j."));
    private final Collection<String> allowedExtraClasses;

    public FilteredObjectInputStream() throws IOException, SecurityException {
        this.allowedExtraClasses = Collections.emptySet();
    }

    public FilteredObjectInputStream(InputStream inputStream) throws IOException {
        super(inputStream);
        this.allowedExtraClasses = Collections.emptySet();
    }

    public FilteredObjectInputStream(Collection<String> allowedExtraClasses) throws IOException, SecurityException {
        this.allowedExtraClasses = allowedExtraClasses;
    }

    public FilteredObjectInputStream(InputStream inputStream, Collection<String> allowedExtraClasses) throws IOException {
        super(inputStream);
        this.allowedExtraClasses = allowedExtraClasses;
    }

    public Collection<String> getAllowedClasses() {
        return this.allowedExtraClasses;
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String name = desc.getName();
        if (!FilteredObjectInputStream.isAllowedByDefault(name) && !this.allowedExtraClasses.contains(name)) {
            throw new InvalidObjectException("Class is not allowed for deserialization: " + name);
        }
        return super.resolveClass(desc);
    }

    private static boolean isAllowedByDefault(String name) {
        return FilteredObjectInputStream.isRequiredPackage(name) || REQUIRED_JAVA_CLASSES.contains(name);
    }

    private static boolean isRequiredPackage(String name) {
        for (String packageName : REQUIRED_JAVA_PACKAGES) {
            if (!name.startsWith(packageName)) continue;
            return true;
        }
        return false;
    }
}

