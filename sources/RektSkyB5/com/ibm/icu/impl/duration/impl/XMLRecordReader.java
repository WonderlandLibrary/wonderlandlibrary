/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.duration.impl;

import com.ibm.icu.impl.duration.impl.RecordReader;
import com.ibm.icu.lang.UCharacter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class XMLRecordReader
implements RecordReader {
    private Reader r;
    private List<String> nameStack;
    private boolean atTag;
    private String tag;

    public XMLRecordReader(Reader r2) {
        this.r = r2;
        this.nameStack = new ArrayList<String>();
        if (this.getTag().startsWith("?xml")) {
            this.advance();
        }
        if (this.getTag().startsWith("!--")) {
            this.advance();
        }
    }

    @Override
    public boolean open(String title) {
        if (this.getTag().equals(title)) {
            this.nameStack.add(title);
            this.advance();
            return true;
        }
        return false;
    }

    @Override
    public boolean close() {
        int ix = this.nameStack.size() - 1;
        String name = this.nameStack.get(ix);
        if (this.getTag().equals("/" + name)) {
            this.nameStack.remove(ix);
            this.advance();
            return true;
        }
        return false;
    }

    @Override
    public boolean bool(String name) {
        String s2 = this.string(name);
        if (s2 != null) {
            return "true".equals(s2);
        }
        return false;
    }

    @Override
    public boolean[] boolArray(String name) {
        String[] sa = this.stringArray(name);
        if (sa != null) {
            boolean[] result = new boolean[sa.length];
            for (int i2 = 0; i2 < sa.length; ++i2) {
                result[i2] = "true".equals(sa[i2]);
            }
            return result;
        }
        return null;
    }

    @Override
    public char character(String name) {
        String s2 = this.string(name);
        if (s2 != null) {
            return s2.charAt(0);
        }
        return '\uffff';
    }

    @Override
    public char[] characterArray(String name) {
        String[] sa = this.stringArray(name);
        if (sa != null) {
            char[] result = new char[sa.length];
            for (int i2 = 0; i2 < sa.length; ++i2) {
                result[i2] = sa[i2].charAt(0);
            }
            return result;
        }
        return null;
    }

    @Override
    public byte namedIndex(String name, String[] names) {
        String sa = this.string(name);
        if (sa != null) {
            for (int i2 = 0; i2 < names.length; ++i2) {
                if (!sa.equals(names[i2])) continue;
                return (byte)i2;
            }
        }
        return -1;
    }

    @Override
    public byte[] namedIndexArray(String name, String[] names) {
        String[] sa = this.stringArray(name);
        if (sa != null) {
            byte[] result = new byte[sa.length];
            block0: for (int i2 = 0; i2 < sa.length; ++i2) {
                String s2 = sa[i2];
                for (int j2 = 0; j2 < names.length; ++j2) {
                    if (!names[j2].equals(s2)) continue;
                    result[i2] = (byte)j2;
                    continue block0;
                }
                result[i2] = -1;
            }
            return result;
        }
        return null;
    }

    @Override
    public String string(String name) {
        if (this.match(name)) {
            String result = this.readData();
            if (this.match("/" + name)) {
                return result;
            }
        }
        return null;
    }

    @Override
    public String[] stringArray(String name) {
        if (this.match(name + "List")) {
            String s2;
            ArrayList<String> list = new ArrayList<String>();
            while (null != (s2 = this.string(name))) {
                if ("Null".equals(s2)) {
                    s2 = null;
                }
                list.add(s2);
            }
            if (this.match("/" + name + "List")) {
                return list.toArray(new String[list.size()]);
            }
        }
        return null;
    }

    @Override
    public String[][] stringTable(String name) {
        if (this.match(name + "Table")) {
            String[] sa;
            ArrayList<String[]> list = new ArrayList<String[]>();
            while (null != (sa = this.stringArray(name))) {
                list.add(sa);
            }
            if (this.match("/" + name + "Table")) {
                return (String[][])list.toArray((T[])new String[list.size()][]);
            }
        }
        return null;
    }

    private boolean match(String target) {
        if (this.getTag().equals(target)) {
            this.advance();
            return true;
        }
        return false;
    }

    private String getTag() {
        if (this.tag == null) {
            this.tag = this.readNextTag();
        }
        return this.tag;
    }

    private void advance() {
        this.tag = null;
    }

    private String readData() {
        int c2;
        StringBuilder sb = new StringBuilder();
        boolean inWhitespace = false;
        while (true) {
            if ((c2 = this.readChar()) == -1 || c2 == 60) break;
            if (c2 == 38) {
                c2 = this.readChar();
                if (c2 == 35) {
                    StringBuilder numBuf = new StringBuilder();
                    int radix = 10;
                    c2 = this.readChar();
                    if (c2 == 120) {
                        radix = 16;
                        c2 = this.readChar();
                    }
                    while (c2 != 59 && c2 != -1) {
                        numBuf.append((char)c2);
                        c2 = this.readChar();
                    }
                    try {
                        int num = Integer.parseInt(numBuf.toString(), radix);
                        c2 = (char)num;
                    }
                    catch (NumberFormatException ex) {
                        System.err.println("numbuf: " + numBuf.toString() + " radix: " + radix);
                        throw ex;
                    }
                } else {
                    StringBuilder charBuf = new StringBuilder();
                    while (c2 != 59 && c2 != -1) {
                        charBuf.append((char)c2);
                        c2 = this.readChar();
                    }
                    String charName = charBuf.toString();
                    if (charName.equals("lt")) {
                        c2 = 60;
                    } else if (charName.equals("gt")) {
                        c2 = 62;
                    } else if (charName.equals("quot")) {
                        c2 = 34;
                    } else if (charName.equals("apos")) {
                        c2 = 39;
                    } else if (charName.equals("amp")) {
                        c2 = 38;
                    } else {
                        System.err.println("unrecognized character entity: '" + charName + "'");
                        continue;
                    }
                }
            }
            if (UCharacter.isWhitespace(c2)) {
                if (inWhitespace) continue;
                c2 = 32;
                inWhitespace = true;
            } else {
                inWhitespace = false;
            }
            sb.append((char)c2);
        }
        this.atTag = c2 == 60;
        return sb.toString();
    }

    private String readNextTag() {
        int c2 = 0;
        while (!this.atTag) {
            c2 = this.readChar();
            if (c2 == 60 || c2 == -1) {
                if (c2 != 60) break;
                this.atTag = true;
                break;
            }
            if (UCharacter.isWhitespace(c2)) continue;
            System.err.println("Unexpected non-whitespace character " + Integer.toHexString(c2));
            break;
        }
        if (this.atTag) {
            this.atTag = false;
            StringBuilder sb = new StringBuilder();
            while ((c2 = this.readChar()) != 62 && c2 != -1) {
                sb.append((char)c2);
            }
            return sb.toString();
        }
        return null;
    }

    int readChar() {
        try {
            return this.r.read();
        }
        catch (IOException iOException) {
            return -1;
        }
    }
}

