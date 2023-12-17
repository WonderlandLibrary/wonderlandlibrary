// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.util;

import org.apache.commons.lang3.StringUtils;
import java.io.FileNotFoundException;
import com.google.common.collect.Lists;
import java.util.List;
import java.io.IOException;

public class JsonException extends IOException
{
    private final List<Entry> entries;
    private final String message;
    
    public JsonException(final String messageIn) {
        (this.entries = (List<Entry>)Lists.newArrayList()).add(new Entry());
        this.message = messageIn;
    }
    
    public JsonException(final String messageIn, final Throwable cause) {
        super(cause);
        (this.entries = (List<Entry>)Lists.newArrayList()).add(new Entry());
        this.message = messageIn;
    }
    
    public void prependJsonKey(final String key) {
        this.entries.get(0).addJsonKey(key);
    }
    
    public void setFilenameAndFlush(final String filenameIn) {
        this.entries.get(0).filename = filenameIn;
        this.entries.add(0, new Entry());
    }
    
    @Override
    public String getMessage() {
        return "Invalid " + this.entries.get(this.entries.size() - 1) + ": " + this.message;
    }
    
    public static JsonException forException(final Exception exception) {
        if (exception instanceof JsonException) {
            return (JsonException)exception;
        }
        String s = exception.getMessage();
        if (exception instanceof FileNotFoundException) {
            s = "File not found";
        }
        return new JsonException(s, exception);
    }
    
    public static class Entry
    {
        private String filename;
        private final List<String> jsonKeys;
        
        private Entry() {
            this.jsonKeys = (List<String>)Lists.newArrayList();
        }
        
        private void addJsonKey(final String key) {
            this.jsonKeys.add(0, key);
        }
        
        public String getJsonKeys() {
            return StringUtils.join((Iterable)this.jsonKeys, "->");
        }
        
        @Override
        public String toString() {
            if (this.filename != null) {
                return this.jsonKeys.isEmpty() ? this.filename : (this.filename + " " + this.getJsonKeys());
            }
            return this.jsonKeys.isEmpty() ? "(Unknown file)" : ("(Unknown file) " + this.getJsonKeys());
        }
    }
}
