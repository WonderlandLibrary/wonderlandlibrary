// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.util;

import java.util.Iterator;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import com.google.common.io.CharStreams;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentStore
{
    private final Map<String, List<String>> headers;
    private final String pathSeparator;
    private final String pathSeparatorQuoted;
    private final int indents;
    private List<String> mainHeader;
    
    public CommentStore(final char pathSeparator, final int indents) {
        this.headers = new HashMap<String, List<String>>();
        this.mainHeader = new ArrayList<String>();
        this.pathSeparator = Character.toString(pathSeparator);
        this.pathSeparatorQuoted = Pattern.quote(this.pathSeparator);
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
        this.mainHeader.clear();
        this.headers.clear();
        String data;
        try (final InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            data = CharStreams.toString(reader);
        }
        final List<String> currentComments = new ArrayList<String>();
        boolean header = true;
        boolean multiLineValue = false;
        int currentIndents = 0;
        String key = "";
        for (final String line : data.split("\n")) {
            final String s = line.trim();
            Label_0488: {
                if (s.startsWith("#")) {
                    currentComments.add(s);
                }
                else {
                    if (header) {
                        if (!currentComments.isEmpty()) {
                            currentComments.add("");
                            this.mainHeader.addAll(currentComments);
                            currentComments.clear();
                        }
                        header = false;
                    }
                    if (s.isEmpty()) {
                        currentComments.add(s);
                    }
                    else if (s.startsWith("- |")) {
                        multiLineValue = true;
                    }
                    else {
                        final int indent = this.getIndents(line);
                        final int indents = indent / this.indents;
                        if (multiLineValue) {
                            if (indents > currentIndents) {
                                break Label_0488;
                            }
                            multiLineValue = false;
                        }
                        if (indents <= currentIndents) {
                            final String[] array = key.split(this.pathSeparatorQuoted);
                            final int backspace = currentIndents - indents + 1;
                            final int delta = array.length - backspace;
                            key = ((delta >= 0) ? this.join(array, delta) : key);
                        }
                        final String separator = key.isEmpty() ? "" : this.pathSeparator;
                        final String lineKey = (line.indexOf(58) != -1) ? line.split(Pattern.quote(":"))[0] : line;
                        key = key + separator + lineKey.substring(indent);
                        currentIndents = indents;
                        if (!currentComments.isEmpty()) {
                            this.headers.put(key, new ArrayList<String>(currentComments));
                            currentComments.clear();
                        }
                    }
                }
            }
        }
    }
    
    public void writeComments(final String rawYaml, final File output) throws IOException {
        final StringBuilder fileData = new StringBuilder();
        for (final String mainHeaderLine : this.mainHeader) {
            fileData.append(mainHeaderLine).append('\n');
        }
        fileData.deleteCharAt(fileData.length() - 1);
        int currentKeyIndents = 0;
        String key = "";
        for (final String line : rawYaml.split("\n")) {
            if (!line.isEmpty()) {
                final int indent = this.getIndents(line);
                final int indents = indent / this.indents;
                final String substring = line.substring(indent);
                boolean keyLine;
                if (substring.trim().isEmpty() || substring.charAt(0) == '-') {
                    keyLine = false;
                }
                else if (indents <= currentKeyIndents) {
                    final String[] array = key.split(this.pathSeparatorQuoted);
                    final int backspace = currentKeyIndents - indents + 1;
                    key = this.join(array, array.length - backspace);
                    keyLine = true;
                }
                else {
                    keyLine = (line.indexOf(58) != -1);
                }
                if (!keyLine) {
                    fileData.append(line).append('\n');
                }
                else {
                    final String newKey = substring.split(Pattern.quote(":"))[0];
                    if (!key.isEmpty()) {
                        key += this.pathSeparator;
                    }
                    key += newKey;
                    final List<String> strings = this.headers.get(key);
                    if (strings != null && !strings.isEmpty()) {
                        final String indentText = (indent > 0) ? line.substring(0, indent) : "";
                        for (final String comment : strings) {
                            if (comment.isEmpty()) {
                                fileData.append('\n');
                            }
                            else {
                                fileData.append(indentText).append(comment).append('\n');
                            }
                        }
                    }
                    currentKeyIndents = indents;
                    fileData.append(line).append('\n');
                }
            }
        }
        Files.write(fileData.toString(), output, StandardCharsets.UTF_8);
    }
    
    private int getIndents(final String line) {
        int count = 0;
        for (int i = 0; i < line.length() && line.charAt(i) == ' '; ++i) {
            ++count;
        }
        return count;
    }
    
    private String join(final String[] array, final int length) {
        final String[] copy = new String[length];
        System.arraycopy(array, 0, copy, 0, length);
        return String.join(this.pathSeparator, (CharSequence[])copy);
    }
}
