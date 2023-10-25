/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

import com.ibm.icu.text.StringTransform;
import com.ibm.icu.text.SymbolTable;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.Freezable;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class UnicodeRegex
implements Cloneable,
Freezable<UnicodeRegex>,
StringTransform {
    private SymbolTable symbolTable;
    private static UnicodeRegex STANDARD = new UnicodeRegex();
    private String bnfCommentString = "#";
    private String bnfVariableInfix = "=";
    private String bnfLineSeparator = "\n";
    private Appendable log = null;
    private Comparator<Object> LongestFirst = new Comparator<Object>(){

        @Override
        public int compare(Object obj0, Object obj1) {
            int len1;
            String arg0 = obj0.toString();
            String arg1 = obj1.toString();
            int len0 = arg0.length();
            if (len0 != (len1 = arg1.length())) {
                return len1 - len0;
            }
            return arg0.compareTo(arg1);
        }
    };

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    public UnicodeRegex setSymbolTable(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
        return this;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public String transform(String regex) {
        result = new StringBuilder();
        temp = new UnicodeSet();
        pos = new ParsePosition(0);
        state = 0;
        i = 0;
        while (i < regex.length()) {
            block11: {
                ch = regex.charAt(i);
                switch (state) {
                    case 0: {
                        if (ch != '\\') ** GOTO lbl16
                        if (UnicodeSet.resemblesPattern(regex, i)) {
                            i = this.processSet(regex, i, result, temp, pos);
                        } else {
                            state = 1;
                            break;
lbl16:
                            // 1 sources

                            if (ch != '[' || !UnicodeSet.resemblesPattern(regex, i)) break;
                            i = this.processSet(regex, i, result, temp, pos);
                        }
                        break block11;
                    }
                    case 1: {
                        if (ch == 'Q') {
                            state = 1;
                            break;
                        }
                        state = 0;
                        break;
                    }
                    case 2: {
                        if (ch != '\\') break;
                        state = 3;
                        break;
                    }
                    case 3: {
                        if (ch == 'E') {
                            state = 0;
                        }
                        state = 2;
                    }
                }
                result.append(ch);
            }
            ++i;
        }
        return result.toString();
    }

    public static String fix(String regex) {
        return STANDARD.transform(regex);
    }

    public static Pattern compile(String regex) {
        return Pattern.compile(STANDARD.transform(regex));
    }

    public static Pattern compile(String regex, int options) {
        return Pattern.compile(STANDARD.transform(regex), options);
    }

    public String compileBnf(String bnfLines) {
        return this.compileBnf(Arrays.asList(bnfLines.split("\\r\\n?|\\n")));
    }

    public String compileBnf(List<String> lines) {
        Map<String, String> variables = this.getVariables(lines);
        LinkedHashSet<String> unused = new LinkedHashSet<String>(variables.keySet());
        for (int i = 0; i < 2; ++i) {
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                String variable = entry.getKey();
                String definition = entry.getValue();
                for (Map.Entry<String, String> entry2 : variables.entrySet()) {
                    String altered2;
                    String variable2 = entry2.getKey();
                    String definition2 = entry2.getValue();
                    if (variable.equals(variable2) || (altered2 = definition2.replace(variable, definition)).equals(definition2)) continue;
                    unused.remove(variable);
                    variables.put(variable2, altered2);
                    if (this.log == null) continue;
                    try {
                        this.log.append(variable2 + "=" + altered2 + ";");
                    }
                    catch (IOException e) {
                        throw (IllegalArgumentException)new IllegalArgumentException().initCause(e);
                    }
                }
            }
        }
        if (unused.size() != 1) {
            throw new IllegalArgumentException("Not a single root: " + unused);
        }
        return variables.get(unused.iterator().next());
    }

    public String getBnfCommentString() {
        return this.bnfCommentString;
    }

    public void setBnfCommentString(String bnfCommentString) {
        this.bnfCommentString = bnfCommentString;
    }

    public String getBnfVariableInfix() {
        return this.bnfVariableInfix;
    }

    public void setBnfVariableInfix(String bnfVariableInfix) {
        this.bnfVariableInfix = bnfVariableInfix;
    }

    public String getBnfLineSeparator() {
        return this.bnfLineSeparator;
    }

    public void setBnfLineSeparator(String bnfLineSeparator) {
        this.bnfLineSeparator = bnfLineSeparator;
    }

    public static List<String> appendLines(List<String> result, String file, String encoding) throws IOException {
        return UnicodeRegex.appendLines(result, new FileInputStream(file), encoding);
    }

    public static List<String> appendLines(List<String> result, InputStream inputStream, String encoding) throws UnsupportedEncodingException, IOException {
        String line;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, encoding == null ? "UTF-8" : encoding));
        while ((line = in.readLine()) != null) {
            result.add(line);
        }
        return result;
    }

    @Override
    public UnicodeRegex cloneAsThawed() {
        try {
            return (UnicodeRegex)this.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public UnicodeRegex freeze() {
        return this;
    }

    @Override
    public boolean isFrozen() {
        return true;
    }

    private int processSet(String regex, int i, StringBuilder result, UnicodeSet temp, ParsePosition pos) {
        try {
            pos.setIndex(i);
            UnicodeSet x = temp.clear().applyPattern(regex, pos, this.symbolTable, 0);
            x.complement().complement();
            result.append(x.toPattern(false));
            i = pos.getIndex() - 1;
            return i;
        }
        catch (Exception e) {
            throw (IllegalArgumentException)new IllegalArgumentException("Error in " + regex).initCause(e);
        }
    }

    private Map<String, String> getVariables(List<String> lines) {
        TreeMap<Object, String> variables = new TreeMap<Object, String>(this.LongestFirst);
        String variable = null;
        StringBuffer definition = new StringBuffer();
        int count = 0;
        for (String line : lines) {
            int equalsPos;
            String linePart;
            String trimline;
            int hashPos;
            ++count;
            if (line.length() == 0) continue;
            if (line.charAt(0) == '\ufeff') {
                line = line.substring(1);
            }
            if (this.bnfCommentString != null && (hashPos = line.indexOf(this.bnfCommentString)) >= 0) {
                line = line.substring(0, hashPos);
            }
            if ((trimline = line.trim()).length() == 0 || (linePart = line).trim().length() == 0) continue;
            boolean terminated = trimline.endsWith(";");
            if (terminated) {
                linePart = linePart.substring(0, linePart.lastIndexOf(59));
            }
            if ((equalsPos = linePart.indexOf(this.bnfVariableInfix)) >= 0) {
                if (variable != null) {
                    throw new IllegalArgumentException("Missing ';' before " + count + ") " + line);
                }
                variable = linePart.substring(0, equalsPos).trim();
                if (variables.containsKey(variable)) {
                    throw new IllegalArgumentException("Duplicate variable definition in " + line);
                }
                definition.append(linePart.substring(equalsPos + 1).trim());
            } else {
                if (variable == null) {
                    throw new IllegalArgumentException("Missing '=' at " + count + ") " + line);
                }
                definition.append(this.bnfLineSeparator).append(linePart);
            }
            if (!terminated) continue;
            variables.put(variable, definition.toString());
            variable = null;
            definition.setLength(0);
        }
        if (variable != null) {
            throw new IllegalArgumentException("Missing ';' at end");
        }
        return variables;
    }
}

