// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.util;

import com.google.common.base.Joiner;
import java.util.Iterator;
import com.google.common.io.Files;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import com.google.common.io.CharStreams;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.Arrays;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;

public class CommentStore
{
    private final Map<String, List<String>> headers;
    private final char pathSeperator;
    private final int indents;
    private List<String> mainHeader;
    
    public CommentStore(final char pathSeperator, final int indents) {
        this.headers = (Map<String, List<String>>)Maps.newConcurrentMap();
        this.mainHeader = (List<String>)Lists.newArrayList();
        this.pathSeperator = pathSeperator;
        this.indents = indents;
    }
    
    public void mainHeader(final String... header) {
        this.mainHeader = Arrays.asList(header);
    }
    
    public List<String> mainHeader() {
        return this.mainHeader;
    }
    
    public void header(final String key, final String... header) {
        this.headers.put(key, Arrays.asList(header));
    }
    
    public List<String> header(final String key) {
        return this.headers.get(key);
    }
    
    public void storeComments(final InputStream inputStream) throws IOException {
        final InputStreamReader reader = new InputStreamReader(inputStream);
        String contents;
        try {
            contents = CharStreams.toString(reader);
        }
        finally {
            reader.close();
        }
        final StringBuilder memoryData = new StringBuilder();
        final String pathSeparator = Character.toString(this.pathSeperator);
        int currentIndents = 0;
        String key = "";
        List<String> headers = (List<String>)Lists.newArrayList();
        for (final String line : contents.split("\n")) {
            if (!line.isEmpty()) {
                final int indent = this.getSuccessiveCharCount(line, ' ');
                final String subline = (indent > 0) ? line.substring(indent) : line;
                if (subline.startsWith("#")) {
                    if (subline.startsWith("#>")) {
                        final String txt = subline.startsWith("#> ") ? subline.substring(3) : subline.substring(2);
                        this.mainHeader.add(txt);
                    }
                    else {
                        final String txt = subline.startsWith("# ") ? subline.substring(2) : subline.substring(1);
                        headers.add(txt);
                    }
                }
                else {
                    final int indents = indent / this.indents;
                    if (indents <= currentIndents) {
                        final String[] array = key.split(Pattern.quote(pathSeparator));
                        final int backspace = currentIndents - indents + 1;
                        key = this.join(array, this.pathSeperator, 0, array.length - backspace);
                    }
                    final String separator = (key.length() > 0) ? pathSeparator : "";
                    final String lineKey = line.contains(":") ? line.split(Pattern.quote(":"))[0] : line;
                    key = key + separator + lineKey.substring(indent);
                    currentIndents = indents;
                    memoryData.append(line).append('\n');
                    if (!headers.isEmpty()) {
                        this.headers.put(key, headers);
                        headers = (List<String>)Lists.newArrayList();
                    }
                }
            }
        }
    }
    
    public void writeComments(final String yaml, final File output) throws IOException {
        final int indentLength = this.indents;
        final String pathSeparator = Character.toString(this.pathSeperator);
        final StringBuilder fileData = new StringBuilder();
        int currentIndents = 0;
        String key = "";
        for (final String h : this.mainHeader) {
            fileData.append("#> ").append(h).append('\n');
        }
        for (final String line : yaml.split("\n")) {
            if (line.isEmpty() || line.trim().charAt(0) == '-') {
                fileData.append(line).append('\n');
            }
            else {
                final int indent = this.getSuccessiveCharCount(line, ' ');
                final int indents = indent / indentLength;
                final String indentText = (indent > 0) ? line.substring(0, indent) : "";
                if (indents <= currentIndents) {
                    final String[] array = key.split(Pattern.quote(pathSeparator));
                    final int backspace = currentIndents - indents + 1;
                    key = this.join(array, this.pathSeperator, 0, array.length - backspace);
                }
                final String separator = key.isEmpty() ? "" : pathSeparator;
                final String lineKey = line.contains(":") ? line.split(Pattern.quote(":"))[0] : line;
                key = key + separator + lineKey.substring(indent);
                currentIndents = indents;
                final List<String> header = this.headers.get(key);
                final String headerText = (header != null) ? this.addHeaderTags(header, indentText) : "";
                fileData.append(headerText).append(line).append('\n');
            }
        }
        Files.write(fileData.toString(), output, StandardCharsets.UTF_8);
    }
    
    private String addHeaderTags(final List<String> header, final String indent) {
        final StringBuilder builder = new StringBuilder();
        for (final String line : header) {
            builder.append(indent).append("# ").append(line).append('\n');
        }
        return builder.toString();
    }
    
    private String join(final String[] array, final char joinChar, final int start, final int length) {
        final String[] copy = new String[length - start];
        System.arraycopy(array, start, copy, 0, length - start);
        return Joiner.on(joinChar).join(copy);
    }
    
    private int getSuccessiveCharCount(final String text, final char key) {
        int count = 0;
        for (int i = 0; i < text.length() && text.charAt(i) == key; ++i) {
            ++count;
        }
        return count;
    }
}
