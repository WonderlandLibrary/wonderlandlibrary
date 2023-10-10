// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling.action;

import java.util.Objects;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.Path;

public class PathWithAttributes
{
    private final Path path;
    private final BasicFileAttributes attributes;
    
    public PathWithAttributes(final Path path, final BasicFileAttributes attributes) {
        this.path = Objects.requireNonNull(path, "path");
        this.attributes = Objects.requireNonNull(attributes, "attributes");
    }
    
    @Override
    public String toString() {
        return this.path + " (modified: " + this.attributes.lastModifiedTime() + ")";
    }
    
    public Path getPath() {
        return this.path;
    }
    
    public BasicFileAttributes getAttributes() {
        return this.attributes;
    }
}
