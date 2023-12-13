/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpVersion
implements Comparable<HttpVersion> {
    private static final Pattern VERSION_PATTERN = Pattern.compile("(\\S+)/(\\d+)\\.(\\d+)");
    private static final String HTTP_1_0_STRING = "HTTP/1.0";
    private static final String HTTP_1_1_STRING = "HTTP/1.1";
    public static final HttpVersion HTTP_1_0 = new HttpVersion("HTTP", 1, 0, false, true);
    public static final HttpVersion HTTP_1_1 = new HttpVersion("HTTP", 1, 1, true, true);
    private final String protocolName;
    private final int majorVersion;
    private final int minorVersion;
    private final String text;
    private final boolean keepAliveDefault;
    private final byte[] bytes;

    public static HttpVersion valueOf(String text) {
        if (text == null) {
            throw new NullPointerException("text");
        }
        if ((text = text.trim()).isEmpty()) {
            throw new IllegalArgumentException("text is empty");
        }
        HttpVersion version = HttpVersion.version0(text);
        if (version == null && (version = HttpVersion.version0(text = text.toUpperCase())) == null) {
            version = new HttpVersion(text, true);
        }
        return version;
    }

    private static HttpVersion version0(String text) {
        if (HTTP_1_1_STRING.equals(text)) {
            return HTTP_1_1;
        }
        if (HTTP_1_0_STRING.equals(text)) {
            return HTTP_1_0;
        }
        return null;
    }

    public HttpVersion(String text, boolean keepAliveDefault) {
        if (text == null) {
            throw new NullPointerException("text");
        }
        if ((text = text.trim().toUpperCase()).isEmpty()) {
            throw new IllegalArgumentException("empty text");
        }
        Matcher m2 = VERSION_PATTERN.matcher(text);
        if (!m2.matches()) {
            throw new IllegalArgumentException("invalid version format: " + text);
        }
        this.protocolName = m2.group(1);
        this.majorVersion = Integer.parseInt(m2.group(2));
        this.minorVersion = Integer.parseInt(m2.group(3));
        this.text = this.protocolName + '/' + this.majorVersion + '.' + this.minorVersion;
        this.keepAliveDefault = keepAliveDefault;
        this.bytes = null;
    }

    public HttpVersion(String protocolName, int majorVersion, int minorVersion, boolean keepAliveDefault) {
        this(protocolName, majorVersion, minorVersion, keepAliveDefault, false);
    }

    private HttpVersion(String protocolName, int majorVersion, int minorVersion, boolean keepAliveDefault, boolean bytes) {
        if (protocolName == null) {
            throw new NullPointerException("protocolName");
        }
        if ((protocolName = protocolName.trim().toUpperCase()).isEmpty()) {
            throw new IllegalArgumentException("empty protocolName");
        }
        for (int i2 = 0; i2 < protocolName.length(); ++i2) {
            if (!Character.isISOControl(protocolName.charAt(i2)) && !Character.isWhitespace(protocolName.charAt(i2))) continue;
            throw new IllegalArgumentException("invalid character in protocolName");
        }
        if (majorVersion < 0) {
            throw new IllegalArgumentException("negative majorVersion");
        }
        if (minorVersion < 0) {
            throw new IllegalArgumentException("negative minorVersion");
        }
        this.protocolName = protocolName;
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.text = protocolName + '/' + majorVersion + '.' + minorVersion;
        this.keepAliveDefault = keepAliveDefault;
        this.bytes = (byte[])(bytes ? this.text.getBytes(CharsetUtil.US_ASCII) : null);
    }

    public String protocolName() {
        return this.protocolName;
    }

    public int majorVersion() {
        return this.majorVersion;
    }

    public int minorVersion() {
        return this.minorVersion;
    }

    public String text() {
        return this.text;
    }

    public boolean isKeepAliveDefault() {
        return this.keepAliveDefault;
    }

    public String toString() {
        return this.text();
    }

    public int hashCode() {
        return (this.protocolName().hashCode() * 31 + this.majorVersion()) * 31 + this.minorVersion();
    }

    public boolean equals(Object o2) {
        if (!(o2 instanceof HttpVersion)) {
            return false;
        }
        HttpVersion that = (HttpVersion)o2;
        return this.minorVersion() == that.minorVersion() && this.majorVersion() == that.majorVersion() && this.protocolName().equals(that.protocolName());
    }

    @Override
    public int compareTo(HttpVersion o2) {
        int v2 = this.protocolName().compareTo(o2.protocolName());
        if (v2 != 0) {
            return v2;
        }
        v2 = this.majorVersion() - o2.majorVersion();
        if (v2 != 0) {
            return v2;
        }
        return this.minorVersion() - o2.minorVersion();
    }

    void encode(ByteBuf buf) {
        if (this.bytes == null) {
            HttpHeaders.encodeAscii0(this.text, buf);
        } else {
            buf.writeBytes(this.bytes);
        }
    }
}

