/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.message;

import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.message.HeaderValueFormatter;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class BasicHeaderValueFormatter
implements HeaderValueFormatter {
    @Deprecated
    public static final BasicHeaderValueFormatter DEFAULT = new BasicHeaderValueFormatter();
    public static final BasicHeaderValueFormatter INSTANCE = new BasicHeaderValueFormatter();
    public static final String SEPARATORS = " ;,:@()<>\\\"/[]?={}\t";
    public static final String UNSAFE_CHARS = "\"\\";

    public static String formatElements(HeaderElement[] elems, boolean quote, HeaderValueFormatter formatter) {
        return (formatter != null ? formatter : INSTANCE).formatElements(null, elems, quote).toString();
    }

    @Override
    public CharArrayBuffer formatElements(CharArrayBuffer charBuffer, HeaderElement[] elems, boolean quote) {
        Args.notNull(elems, "Header element array");
        int len = this.estimateElementsLen(elems);
        CharArrayBuffer buffer = charBuffer;
        if (buffer == null) {
            buffer = new CharArrayBuffer(len);
        } else {
            buffer.ensureCapacity(len);
        }
        for (int i2 = 0; i2 < elems.length; ++i2) {
            if (i2 > 0) {
                buffer.append(", ");
            }
            this.formatHeaderElement(buffer, elems[i2], quote);
        }
        return buffer;
    }

    protected int estimateElementsLen(HeaderElement[] elems) {
        if (elems == null || elems.length < 1) {
            return 0;
        }
        int result = (elems.length - 1) * 2;
        for (HeaderElement elem : elems) {
            result += this.estimateHeaderElementLen(elem);
        }
        return result;
    }

    public static String formatHeaderElement(HeaderElement elem, boolean quote, HeaderValueFormatter formatter) {
        return (formatter != null ? formatter : INSTANCE).formatHeaderElement(null, elem, quote).toString();
    }

    @Override
    public CharArrayBuffer formatHeaderElement(CharArrayBuffer charBuffer, HeaderElement elem, boolean quote) {
        int parcnt;
        Args.notNull(elem, "Header element");
        int len = this.estimateHeaderElementLen(elem);
        CharArrayBuffer buffer = charBuffer;
        if (buffer == null) {
            buffer = new CharArrayBuffer(len);
        } else {
            buffer.ensureCapacity(len);
        }
        buffer.append(elem.getName());
        String value = elem.getValue();
        if (value != null) {
            buffer.append('=');
            this.doFormatValue(buffer, value, quote);
        }
        if ((parcnt = elem.getParameterCount()) > 0) {
            for (int i2 = 0; i2 < parcnt; ++i2) {
                buffer.append("; ");
                this.formatNameValuePair(buffer, elem.getParameter(i2), quote);
            }
        }
        return buffer;
    }

    protected int estimateHeaderElementLen(HeaderElement elem) {
        int parcnt;
        if (elem == null) {
            return 0;
        }
        int result = elem.getName().length();
        String value = elem.getValue();
        if (value != null) {
            result += 3 + value.length();
        }
        if ((parcnt = elem.getParameterCount()) > 0) {
            for (int i2 = 0; i2 < parcnt; ++i2) {
                result += 2 + this.estimateNameValuePairLen(elem.getParameter(i2));
            }
        }
        return result;
    }

    public static String formatParameters(NameValuePair[] nvps, boolean quote, HeaderValueFormatter formatter) {
        return (formatter != null ? formatter : INSTANCE).formatParameters(null, nvps, quote).toString();
    }

    @Override
    public CharArrayBuffer formatParameters(CharArrayBuffer charBuffer, NameValuePair[] nvps, boolean quote) {
        Args.notNull(nvps, "Header parameter array");
        int len = this.estimateParametersLen(nvps);
        CharArrayBuffer buffer = charBuffer;
        if (buffer == null) {
            buffer = new CharArrayBuffer(len);
        } else {
            buffer.ensureCapacity(len);
        }
        for (int i2 = 0; i2 < nvps.length; ++i2) {
            if (i2 > 0) {
                buffer.append("; ");
            }
            this.formatNameValuePair(buffer, nvps[i2], quote);
        }
        return buffer;
    }

    protected int estimateParametersLen(NameValuePair[] nvps) {
        if (nvps == null || nvps.length < 1) {
            return 0;
        }
        int result = (nvps.length - 1) * 2;
        for (NameValuePair nvp : nvps) {
            result += this.estimateNameValuePairLen(nvp);
        }
        return result;
    }

    public static String formatNameValuePair(NameValuePair nvp, boolean quote, HeaderValueFormatter formatter) {
        return (formatter != null ? formatter : INSTANCE).formatNameValuePair(null, nvp, quote).toString();
    }

    @Override
    public CharArrayBuffer formatNameValuePair(CharArrayBuffer charBuffer, NameValuePair nvp, boolean quote) {
        Args.notNull(nvp, "Name / value pair");
        int len = this.estimateNameValuePairLen(nvp);
        CharArrayBuffer buffer = charBuffer;
        if (buffer == null) {
            buffer = new CharArrayBuffer(len);
        } else {
            buffer.ensureCapacity(len);
        }
        buffer.append(nvp.getName());
        String value = nvp.getValue();
        if (value != null) {
            buffer.append('=');
            this.doFormatValue(buffer, value, quote);
        }
        return buffer;
    }

    protected int estimateNameValuePairLen(NameValuePair nvp) {
        if (nvp == null) {
            return 0;
        }
        int result = nvp.getName().length();
        String value = nvp.getValue();
        if (value != null) {
            result += 3 + value.length();
        }
        return result;
    }

    protected void doFormatValue(CharArrayBuffer buffer, String value, boolean quote) {
        int i2;
        boolean quoteFlag = quote;
        if (!quoteFlag) {
            for (i2 = 0; i2 < value.length() && !quoteFlag; ++i2) {
                quoteFlag = this.isSeparator(value.charAt(i2));
            }
        }
        if (quoteFlag) {
            buffer.append('\"');
        }
        for (i2 = 0; i2 < value.length(); ++i2) {
            char ch = value.charAt(i2);
            if (this.isUnsafe(ch)) {
                buffer.append('\\');
            }
            buffer.append(ch);
        }
        if (quoteFlag) {
            buffer.append('\"');
        }
    }

    protected boolean isSeparator(char ch) {
        return SEPARATORS.indexOf(ch) >= 0;
    }

    protected boolean isUnsafe(char ch) {
        return UNSAFE_CHARS.indexOf(ch) >= 0;
    }
}

