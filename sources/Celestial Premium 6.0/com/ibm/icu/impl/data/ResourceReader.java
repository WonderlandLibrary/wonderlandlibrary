/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl.data;

import com.ibm.icu.impl.ICUData;
import com.ibm.icu.impl.PatternProps;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class ResourceReader {
    private BufferedReader reader;
    private String resourceName;
    private String encoding;
    private Class<?> root;
    private int lineNo;

    public ResourceReader(String resourceName, String encoding) throws UnsupportedEncodingException {
        this(ICUData.class, "data/" + resourceName, encoding);
    }

    public ResourceReader(String resourceName) {
        this(ICUData.class, "data/" + resourceName);
    }

    public ResourceReader(Class<?> rootClass, String resourceName, String encoding) throws UnsupportedEncodingException {
        this.root = rootClass;
        this.resourceName = resourceName;
        this.encoding = encoding;
        this.lineNo = -1;
        this._reset();
    }

    public ResourceReader(InputStream is, String resourceName, String encoding) {
        this.root = null;
        this.resourceName = resourceName;
        this.encoding = encoding;
        this.lineNo = -1;
        try {
            InputStreamReader isr = encoding == null ? new InputStreamReader(is) : new InputStreamReader(is, encoding);
            this.reader = new BufferedReader(isr);
            this.lineNo = 0;
        }
        catch (UnsupportedEncodingException e) {
            // empty catch block
        }
    }

    public ResourceReader(InputStream is, String resourceName) {
        this(is, resourceName, null);
    }

    public ResourceReader(Class<?> rootClass, String resourceName) {
        this.root = rootClass;
        this.resourceName = resourceName;
        this.encoding = null;
        this.lineNo = -1;
        try {
            this._reset();
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            // empty catch block
        }
    }

    public String readLine() throws IOException {
        if (this.lineNo == 0) {
            ++this.lineNo;
            String line = this.reader.readLine();
            if (line.charAt(0) == '\uffef' || line.charAt(0) == '\ufeff') {
                line = line.substring(1);
            }
            return line;
        }
        ++this.lineNo;
        return this.reader.readLine();
    }

    public String readLineSkippingComments(boolean trim) throws IOException {
        String line;
        int pos;
        do {
            if ((line = this.readLine()) != null) continue;
            return line;
        } while ((pos = PatternProps.skipWhiteSpace(line, 0)) == line.length() || line.charAt(pos) == '#');
        if (trim) {
            line = line.substring(pos);
        }
        return line;
    }

    public String readLineSkippingComments() throws IOException {
        return this.readLineSkippingComments(false);
    }

    public int getLineNumber() {
        return this.lineNo;
    }

    public String describePosition() {
        return this.resourceName + ':' + this.lineNo;
    }

    public void reset() {
        try {
            this._reset();
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            // empty catch block
        }
    }

    private void _reset() throws UnsupportedEncodingException {
        if (this.lineNo == 0) {
            return;
        }
        InputStream is = ICUData.getStream(this.root, this.resourceName);
        if (is == null) {
            throw new IllegalArgumentException("Can't open " + this.resourceName);
        }
        InputStreamReader isr = this.encoding == null ? new InputStreamReader(is) : new InputStreamReader(is, this.encoding);
        this.reader = new BufferedReader(isr);
        this.lineNo = 0;
    }
}

