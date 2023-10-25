/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.win;

import com.sun.glass.ui.win.EHTMLReadMode;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

class HTMLCodec
extends InputStream {
    public static final String ENCODING = "UTF-8";
    public static final String VERSION = "Version:";
    public static final String START_HTML = "StartHTML:";
    public static final String END_HTML = "EndHTML:";
    public static final String START_FRAGMENT = "StartFragment:";
    public static final String END_FRAGMENT = "EndFragment:";
    public static final String START_SELECTION = "StartSelection:";
    public static final String END_SELECTION = "EndSelection:";
    public static final String START_FRAGMENT_CMT = "<!--StartFragment-->";
    public static final String END_FRAGMENT_CMT = "<!--EndFragment-->";
    public static final String SOURCE_URL = "SourceURL:";
    public static final String DEF_SOURCE_URL = "about:blank";
    public static final String EOLN = "\r\n";
    private static final String VERSION_NUM = "1.0";
    private static final int PADDED_WIDTH = 10;
    private final BufferedInputStream bufferedStream;
    private boolean descriptionParsed = false;
    private boolean closed = false;
    public static final int BYTE_BUFFER_LEN = 8192;
    public static final int CHAR_BUFFER_LEN = 2730;
    private static final String FAILURE_MSG = "Unable to parse HTML description: ";
    private static final String INVALID_MSG = " invalid";
    private long iHTMLStart;
    private long iHTMLEnd;
    private long iFragStart;
    private long iFragEnd;
    private long iSelStart;
    private long iSelEnd;
    private String stBaseURL;
    private String stVersion;
    private long iStartOffset;
    private long iEndOffset;
    private long iReadCount;
    private EHTMLReadMode readMode;

    private static String toPaddedString(int n, int n2) {
        Object object = "" + n;
        int n3 = ((String)object).length();
        if (n >= 0 && n3 < n2) {
            char[] arrc = new char[n2 - n3];
            Arrays.fill(arrc, '0');
            StringBuffer stringBuffer = new StringBuffer(n2);
            stringBuffer.append(arrc);
            stringBuffer.append((String)object);
            object = stringBuffer.toString();
        }
        return object;
    }

    public static byte[] convertToHTMLFormat(byte[] arrby) {
        Object object = "";
        Object object2 = "";
        String string = new String(arrby);
        String string2 = string.toUpperCase();
        if (-1 == string2.indexOf("<HTML")) {
            object = "<HTML>";
            object2 = "</HTML>";
            if (-1 == string2.indexOf("<BODY")) {
                object = (String)object + "<BODY>";
                object2 = "</BODY>" + (String)object2;
            }
        }
        object = (String)object + START_FRAGMENT_CMT;
        object2 = END_FRAGMENT_CMT + (String)object2;
        string = DEF_SOURCE_URL;
        int n = VERSION.length() + VERSION_NUM.length() + EOLN.length() + START_HTML.length() + 10 + EOLN.length() + END_HTML.length() + 10 + EOLN.length() + START_FRAGMENT.length() + 10 + EOLN.length() + END_FRAGMENT.length() + 10 + EOLN.length() + SOURCE_URL.length() + string.length() + EOLN.length();
        int n2 = n + ((String)object).length();
        int n3 = n2 + arrby.length - 1;
        int n4 = n3 + ((String)object2).length();
        StringBuilder stringBuilder = new StringBuilder(n2 + START_FRAGMENT_CMT.length());
        stringBuilder.append(VERSION);
        stringBuilder.append(VERSION_NUM);
        stringBuilder.append(EOLN);
        stringBuilder.append(START_HTML);
        stringBuilder.append(HTMLCodec.toPaddedString(n, 10));
        stringBuilder.append(EOLN);
        stringBuilder.append(END_HTML);
        stringBuilder.append(HTMLCodec.toPaddedString(n4, 10));
        stringBuilder.append(EOLN);
        stringBuilder.append(START_FRAGMENT);
        stringBuilder.append(HTMLCodec.toPaddedString(n2, 10));
        stringBuilder.append(EOLN);
        stringBuilder.append(END_FRAGMENT);
        stringBuilder.append(HTMLCodec.toPaddedString(n3, 10));
        stringBuilder.append(EOLN);
        stringBuilder.append(SOURCE_URL);
        stringBuilder.append(string);
        stringBuilder.append(EOLN);
        stringBuilder.append((String)object);
        byte[] arrby2 = null;
        byte[] arrby3 = null;
        try {
            arrby2 = stringBuilder.toString().getBytes(ENCODING);
            arrby3 = ((String)object2).getBytes(ENCODING);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            return null;
        }
        byte[] arrby4 = new byte[arrby2.length + arrby.length + arrby3.length];
        System.arraycopy(arrby2, 0, arrby4, 0, arrby2.length);
        System.arraycopy(arrby, 0, arrby4, arrby2.length, arrby.length - 1);
        System.arraycopy(arrby3, 0, arrby4, arrby2.length + arrby.length - 1, arrby3.length);
        arrby4[arrby4.length - 1] = 0;
        return arrby4;
    }

    public HTMLCodec(InputStream inputStream, EHTMLReadMode eHTMLReadMode) throws IOException {
        this.bufferedStream = new BufferedInputStream(inputStream, 8192);
        this.readMode = eHTMLReadMode;
    }

    public synchronized String getBaseURL() throws IOException {
        if (!this.descriptionParsed) {
            this.parseDescription();
        }
        return this.stBaseURL;
    }

    public synchronized String getVersion() throws IOException {
        if (!this.descriptionParsed) {
            this.parseDescription();
        }
        return this.stVersion;
    }

    private void parseDescription() throws IOException {
        String string;
        int n;
        this.stBaseURL = null;
        this.stVersion = null;
        this.iSelStart = -1L;
        this.iSelEnd = -1L;
        this.iFragStart = -1L;
        this.iFragEnd = -1L;
        this.iHTMLStart = -1L;
        this.iHTMLEnd = -1L;
        this.bufferedStream.mark(8192);
        String[] arrstring = new String[]{VERSION, START_HTML, END_HTML, START_FRAGMENT, END_FRAGMENT, START_SELECTION, END_SELECTION, SOURCE_URL};
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((InputStream)this.bufferedStream, ENCODING), 2730);
        long l = 0L;
        long l2 = EOLN.length();
        int n2 = arrstring.length;
        boolean bl = true;
        block16: for (n = 0; n < n2 && null != (string = bufferedReader.readLine()); ++n) {
            while (n < n2) {
                if (string.startsWith(arrstring[n])) {
                    l += (long)string.length() + l2;
                    String string2 = string.substring(arrstring[n].length()).trim();
                    if (null == string2) continue block16;
                    try {
                        switch (n) {
                            case 0: {
                                this.stVersion = string2;
                                break;
                            }
                            case 1: {
                                this.iHTMLStart = Integer.parseInt(string2);
                                break;
                            }
                            case 2: {
                                this.iHTMLEnd = Integer.parseInt(string2);
                                break;
                            }
                            case 3: {
                                this.iFragStart = Integer.parseInt(string2);
                                break;
                            }
                            case 4: {
                                this.iFragEnd = Integer.parseInt(string2);
                                break;
                            }
                            case 5: {
                                this.iSelStart = Integer.parseInt(string2);
                                break;
                            }
                            case 6: {
                                this.iSelEnd = Integer.parseInt(string2);
                                break;
                            }
                            case 7: {
                                this.stBaseURL = string2;
                            }
                        }
                        continue block16;
                    }
                    catch (NumberFormatException numberFormatException) {
                        throw new IOException(FAILURE_MSG + arrstring[n] + " value " + numberFormatException + INVALID_MSG);
                    }
                }
                ++n;
            }
        }
        if (-1L == this.iHTMLStart) {
            this.iHTMLStart = l;
        }
        if (-1L == this.iFragStart) {
            this.iFragStart = this.iHTMLStart;
        }
        if (-1L == this.iFragEnd) {
            this.iFragEnd = this.iHTMLEnd;
        }
        if (-1L == this.iSelStart) {
            this.iSelStart = this.iFragStart;
        }
        if (-1L == this.iSelEnd) {
            this.iSelEnd = this.iFragEnd;
        }
        switch (this.readMode) {
            case HTML_READ_ALL: {
                this.iStartOffset = this.iHTMLStart;
                this.iEndOffset = this.iHTMLEnd;
                break;
            }
            case HTML_READ_FRAGMENT: {
                this.iStartOffset = this.iFragStart;
                this.iEndOffset = this.iFragEnd;
                break;
            }
            default: {
                this.iStartOffset = this.iSelStart;
                this.iEndOffset = this.iSelEnd;
            }
        }
        this.bufferedStream.reset();
        if (-1L == this.iStartOffset) {
            throw new IOException("Unable to parse HTML description: invalid HTML format.");
        }
        n = 0;
        while ((long)n < this.iStartOffset) {
            n = (int)((long)n + this.bufferedStream.skip(this.iStartOffset - (long)n));
        }
        this.iReadCount = n;
        if (this.iStartOffset != this.iReadCount) {
            throw new IOException("Unable to parse HTML description: Byte stream ends in description.");
        }
        this.descriptionParsed = true;
    }

    @Override
    public synchronized int read() throws IOException {
        if (this.closed) {
            throw new IOException("Stream closed");
        }
        if (!this.descriptionParsed) {
            this.parseDescription();
        }
        if (-1L != this.iEndOffset && this.iReadCount >= this.iEndOffset) {
            return -1;
        }
        int n = this.bufferedStream.read();
        if (n == -1) {
            return -1;
        }
        ++this.iReadCount;
        return n;
    }

    @Override
    public synchronized void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            this.bufferedStream.close();
        }
    }
}

