// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import java.util.Objects;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import java.net.URI;
import java.io.File;

public class Source
{
    private final File file;
    private final URI uri;
    private final String location;
    
    public Source(final ConfigurationSource source) {
        this.file = source.getFile();
        this.uri = source.getURI();
        this.location = source.getLocation();
    }
    
    public Source(final File file) {
        this.file = Objects.requireNonNull(file, "file is null");
        this.location = file.getAbsolutePath();
        this.uri = null;
    }
    
    public Source(final URI uri, final long lastModified) {
        this.uri = Objects.requireNonNull(uri, "URI is null");
        this.location = uri.toString();
        this.file = null;
    }
    
    public File getFile() {
        return this.file;
    }
    
    public URI getURI() {
        return this.uri;
    }
    
    public String getLocation() {
        return this.location;
    }
    
    @Override
    public String toString() {
        return this.location;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Source)) {
            return false;
        }
        final Source source = (Source)o;
        return Objects.equals(this.location, source.location);
    }
    
    @Override
    public int hashCode() {
        return 31 + Objects.hashCode(this.location);
    }
}
