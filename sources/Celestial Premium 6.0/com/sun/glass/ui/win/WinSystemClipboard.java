/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.win;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.SystemClipboard;
import com.sun.glass.ui.win.WinHTMLCodec;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

class WinSystemClipboard
extends SystemClipboard {
    private long ptr = 0L;
    static final byte[] terminator;
    static final String defaultCharset = "UTF-16LE";
    static final String RTFCharset = "US-ASCII";

    private static native void initIDs();

    protected WinSystemClipboard(String string) {
        super(string);
        this.create();
    }

    protected final long getPtr() {
        return this.ptr;
    }

    @Override
    protected native boolean isOwner();

    protected native void create();

    protected native void dispose();

    protected native void push(Object[] var1, int var2);

    protected native boolean pop();

    private byte[] fosSerialize(String string, long l) {
        Pixels pixels;
        Object object = this.getLocalData(string);
        if (object instanceof ByteBuffer) {
            byte[] arrby = ((ByteBuffer)object).array();
            if ("text/html".equals(string)) {
                arrby = WinHTMLCodec.encode(arrby);
            }
            return arrby;
        }
        if (object instanceof String) {
            String string2 = ((String)object).replaceAll("(\r\n|\r|\n)", "\r\n");
            if ("text/html".equals(string)) {
                try {
                    byte[] arrby = string2.getBytes("UTF-8");
                    ByteBuffer byteBuffer = ByteBuffer.allocate(arrby.length + 1);
                    byteBuffer.put(arrby);
                    byteBuffer.put((byte)0);
                    return WinHTMLCodec.encode(byteBuffer.array());
                }
                catch (UnsupportedEncodingException unsupportedEncodingException) {
                    return null;
                }
            }
            if ("text/rtf".equals(string)) {
                try {
                    byte[] arrby = string2.getBytes("US-ASCII");
                    ByteBuffer byteBuffer = ByteBuffer.allocate(arrby.length + 1);
                    byteBuffer.put(arrby);
                    byteBuffer.put((byte)0);
                    return byteBuffer.array();
                }
                catch (UnsupportedEncodingException unsupportedEncodingException) {
                    return null;
                }
            }
            ByteBuffer byteBuffer = ByteBuffer.allocate((string2.length() + 1) * 2);
            try {
                byteBuffer.put(string2.getBytes("UTF-16LE"));
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                // empty catch block
            }
            byteBuffer.put(terminator);
            return byteBuffer.array();
        }
        if ("application/x-java-file-list".equals(string)) {
            String[] arrstring = (String[])object;
            if (arrstring != null && arrstring.length > 0) {
                int n = 0;
                for (String string3 : arrstring) {
                    n += (string3.length() + 1) * 2;
                }
                n += 2;
                try {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(n);
                    for (String string4 : arrstring) {
                        byteBuffer.put(string4.getBytes("UTF-16LE"));
                        byteBuffer.put(terminator);
                    }
                    byteBuffer.put(terminator);
                    return byteBuffer.array();
                }
                catch (UnsupportedEncodingException unsupportedEncodingException) {}
            }
        } else if ("application/x-java-rawimage".equals(string) && (pixels = (Pixels)object) != null) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(pixels.getWidth() * pixels.getHeight() * 4 + 8);
            byteBuffer.putInt(pixels.getWidth());
            byteBuffer.putInt(pixels.getHeight());
            byteBuffer.put(pixels.asByteBuffer());
            return byteBuffer.array();
        }
        return null;
    }

    @Override
    protected final void pushToSystem(HashMap<String, Object> hashMap, int n) {
        Set<String> set = hashMap.keySet();
        HashSet<String> hashSet = new HashSet<String>();
        MimeTypeParser mimeTypeParser = new MimeTypeParser();
        for (String string : set) {
            mimeTypeParser.parse(string);
            if (mimeTypeParser.isInMemoryFile()) continue;
            hashSet.add(string);
        }
        this.push(hashSet.toArray(), n);
    }

    private native byte[] popBytes(String var1, long var2);

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected final Object popFromSystem(String string) {
        if (!this.pop()) {
            return null;
        }
        MimeTypeParser mimeTypeParser = new MimeTypeParser(string);
        String string2 = mimeTypeParser.getMime();
        byte[] arrby = this.popBytes(string2, mimeTypeParser.getIndex());
        if (arrby != null) {
            if ("text/plain".equals(string2) || "text/uri-list".equals(string2)) {
                try {
                    return new String(arrby, 0, arrby.length - 2, "UTF-16LE");
                }
                catch (UnsupportedEncodingException unsupportedEncodingException) {
                    return null;
                }
            }
            if ("text/html".equals(string2)) {
                try {
                    arrby = WinHTMLCodec.decode(arrby);
                    return new String(arrby, 0, arrby.length, "UTF-8");
                }
                catch (UnsupportedEncodingException unsupportedEncodingException) {
                    return null;
                }
            }
            if ("text/rtf".equals(string2)) {
                try {
                    return new String(arrby, 0, arrby.length, "US-ASCII");
                }
                catch (UnsupportedEncodingException unsupportedEncodingException) {
                    return null;
                }
            }
            if (!"application/x-java-file-list".equals(string2)) {
                if (!"application/x-java-rawimage".equals(string2)) return ByteBuffer.wrap(arrby);
                ByteBuffer byteBuffer = ByteBuffer.wrap(arrby, 0, 8);
                return Application.GetApplication().createPixels(byteBuffer.getInt(), byteBuffer.getInt(), ByteBuffer.wrap(arrby, 8, arrby.length - 8));
            }
            try {
                String string3 = new String(arrby, 0, arrby.length, "UTF-16LE");
                return string3.split("\u0000");
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                return null;
            }
        }
        if (("text/uri-list".equals(string2) || "text/plain".equals(string2)) && (arrby = this.popBytes(string2 + ";locale", mimeTypeParser.getIndex())) != null) {
            try {
                return new String(arrby, 0, arrby.length - 1, "UTF-8");
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                // empty catch block
            }
        }
        if (!"text/uri-list".equals(string2)) return null;
        String[] arrstring = (String[])this.popFromSystem("application/x-java-file-list");
        if (arrstring == null) return null;
        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        while (n < arrstring.length) {
            String string4 = arrstring[n];
            string4 = string4.replace("\\", "/");
            if (stringBuilder.length() > 0) {
                stringBuilder.append("\r\n");
            }
            stringBuilder.append("file:/").append(string4);
            ++n;
        }
        return stringBuilder.toString();
    }

    private native String[] popMimesFromSystem();

    @Override
    protected final String[] mimesFromSystem() {
        if (!this.pop()) {
            return null;
        }
        return this.popMimesFromSystem();
    }

    @Override
    public String toString() {
        return "Windows System Clipboard";
    }

    @Override
    protected final void close() {
        this.dispose();
        this.ptr = 0L;
    }

    @Override
    protected native void pushTargetActionToSystem(int var1);

    private native int popSupportedSourceActions();

    @Override
    protected int supportedSourceActionsFromSystem() {
        if (!this.pop()) {
            return 0;
        }
        return this.popSupportedSourceActions();
    }

    static {
        WinSystemClipboard.initIDs();
        terminator = new byte[]{0, 0};
    }

    private static final class MimeTypeParser {
        protected static final String externalBodyMime = "message/external-body";
        protected String mime;
        protected boolean bInMemoryFile;
        protected int index;

        public MimeTypeParser() {
            this.parse("");
        }

        public MimeTypeParser(String string) {
            this.parse(string);
        }

        public void parse(String string) {
            this.mime = string;
            this.bInMemoryFile = false;
            this.index = -1;
            if (string.startsWith(externalBodyMime)) {
                String[] arrstring = string.split(";");
                String string2 = "";
                int n = -1;
                for (int i = 1; i < arrstring.length; ++i) {
                    String[] arrstring2 = arrstring[i].split("=");
                    if (arrstring2.length == 2) {
                        if (arrstring2[0].trim().equalsIgnoreCase("index")) {
                            n = Integer.parseInt(arrstring2[1].trim());
                        } else if (arrstring2[0].trim().equalsIgnoreCase("access-type")) {
                            string2 = arrstring2[1].trim();
                        }
                    }
                    if (n != -1 && !string2.isEmpty()) break;
                }
                if (string2.equalsIgnoreCase("clipboard")) {
                    this.bInMemoryFile = true;
                    this.mime = arrstring[0];
                    this.index = n;
                }
            }
        }

        public String getMime() {
            return this.mime;
        }

        public int getIndex() {
            return this.index;
        }

        public boolean isInMemoryFile() {
            return this.bInMemoryFile;
        }
    }
}

