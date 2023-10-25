/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.impl.cookie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import org.apache.http.annotation.Immutable;
import org.apache.http.impl.cookie.PublicSuffixFilter;

@Immutable
public class PublicSuffixListParser {
    private static final int MAX_LINE_LEN = 256;
    private final PublicSuffixFilter filter;

    PublicSuffixListParser(PublicSuffixFilter filter) {
        this.filter = filter;
    }

    public void parse(Reader list) throws IOException {
        ArrayList<String> rules = new ArrayList<String>();
        ArrayList<String> exceptions = new ArrayList<String>();
        BufferedReader r = new BufferedReader(list);
        StringBuilder sb = new StringBuilder(256);
        boolean more = true;
        while (more) {
            boolean isException;
            more = this.readLine(r, sb);
            String line = sb.toString();
            if (line.length() == 0 || line.startsWith("//")) continue;
            if (line.startsWith(".")) {
                line = line.substring(1);
            }
            if (isException = line.startsWith("!")) {
                line = line.substring(1);
            }
            if (isException) {
                exceptions.add(line);
                continue;
            }
            rules.add(line);
        }
        this.filter.setPublicSuffixes(rules);
        this.filter.setExceptions(exceptions);
    }

    private boolean readLine(Reader r, StringBuilder sb) throws IOException {
        char c;
        int b;
        sb.setLength(0);
        boolean hitWhitespace = false;
        while ((b = r.read()) != -1 && (c = (char)b) != '\n') {
            if (Character.isWhitespace(c)) {
                hitWhitespace = true;
            }
            if (!hitWhitespace) {
                sb.append(c);
            }
            if (sb.length() <= 256) continue;
            throw new IOException("Line too long");
        }
        return b != -1;
    }
}

