/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.entity;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.message.BasicHeaderValueFormatter;
import org.apache.http.message.BasicHeaderValueParser;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.TextUtils;

@Contract(threading=ThreadingBehavior.IMMUTABLE)
public final class ContentType
implements Serializable {
    private static final long serialVersionUID = -7768694718232371896L;
    public static final ContentType APPLICATION_ATOM_XML = ContentType.create("application/atom+xml", Consts.ISO_8859_1);
    public static final ContentType APPLICATION_FORM_URLENCODED = ContentType.create("application/x-www-form-urlencoded", Consts.ISO_8859_1);
    public static final ContentType APPLICATION_JSON = ContentType.create("application/json", Consts.UTF_8);
    public static final ContentType APPLICATION_OCTET_STREAM = ContentType.create("application/octet-stream", (Charset)null);
    public static final ContentType APPLICATION_SVG_XML = ContentType.create("application/svg+xml", Consts.ISO_8859_1);
    public static final ContentType APPLICATION_XHTML_XML = ContentType.create("application/xhtml+xml", Consts.ISO_8859_1);
    public static final ContentType APPLICATION_XML = ContentType.create("application/xml", Consts.ISO_8859_1);
    public static final ContentType IMAGE_BMP = ContentType.create("image/bmp");
    public static final ContentType IMAGE_GIF = ContentType.create("image/gif");
    public static final ContentType IMAGE_JPEG = ContentType.create("image/jpeg");
    public static final ContentType IMAGE_PNG = ContentType.create("image/png");
    public static final ContentType IMAGE_SVG = ContentType.create("image/svg+xml");
    public static final ContentType IMAGE_TIFF = ContentType.create("image/tiff");
    public static final ContentType IMAGE_WEBP = ContentType.create("image/webp");
    public static final ContentType MULTIPART_FORM_DATA = ContentType.create("multipart/form-data", Consts.ISO_8859_1);
    public static final ContentType TEXT_HTML = ContentType.create("text/html", Consts.ISO_8859_1);
    public static final ContentType TEXT_PLAIN = ContentType.create("text/plain", Consts.ISO_8859_1);
    public static final ContentType TEXT_XML = ContentType.create("text/xml", Consts.ISO_8859_1);
    public static final ContentType WILDCARD = ContentType.create("*/*", (Charset)null);
    private static final Map<String, ContentType> CONTENT_TYPE_MAP;
    public static final ContentType DEFAULT_TEXT;
    public static final ContentType DEFAULT_BINARY;
    private final String mimeType;
    private final Charset charset;
    private final NameValuePair[] params;

    ContentType(String mimeType, Charset charset) {
        this.mimeType = mimeType;
        this.charset = charset;
        this.params = null;
    }

    ContentType(String mimeType, Charset charset, NameValuePair[] params) {
        this.mimeType = mimeType;
        this.charset = charset;
        this.params = params;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public String getParameter(String name) {
        Args.notEmpty(name, "Parameter name");
        if (this.params == null) {
            return null;
        }
        for (NameValuePair param : this.params) {
            if (!param.getName().equalsIgnoreCase(name)) continue;
            return param.getValue();
        }
        return null;
    }

    public String toString() {
        CharArrayBuffer buf = new CharArrayBuffer(64);
        buf.append(this.mimeType);
        if (this.params != null) {
            buf.append("; ");
            BasicHeaderValueFormatter.INSTANCE.formatParameters(buf, this.params, false);
        } else if (this.charset != null) {
            buf.append("; charset=");
            buf.append(this.charset.name());
        }
        return buf.toString();
    }

    private static boolean valid(String s2) {
        for (int i2 = 0; i2 < s2.length(); ++i2) {
            char ch = s2.charAt(i2);
            if (ch != '\"' && ch != ',' && ch != ';') continue;
            return false;
        }
        return true;
    }

    public static ContentType create(String mimeType, Charset charset) {
        String normalizedMimeType = Args.notBlank(mimeType, "MIME type").toLowerCase(Locale.ROOT);
        Args.check(ContentType.valid(normalizedMimeType), "MIME type may not contain reserved characters");
        return new ContentType(normalizedMimeType, charset);
    }

    public static ContentType create(String mimeType) {
        return ContentType.create(mimeType, (Charset)null);
    }

    public static ContentType create(String mimeType, String charset) throws UnsupportedCharsetException {
        return ContentType.create(mimeType, !TextUtils.isBlank(charset) ? Charset.forName(charset) : null);
    }

    private static ContentType create(HeaderElement helem, boolean strict) {
        return ContentType.create(helem.getName(), helem.getParameters(), strict);
    }

    private static ContentType create(String mimeType, NameValuePair[] params, boolean strict) {
        Charset charset = null;
        for (NameValuePair param : params) {
            if (!param.getName().equalsIgnoreCase("charset")) continue;
            String s2 = param.getValue();
            if (TextUtils.isBlank(s2)) break;
            try {
                charset = Charset.forName(s2);
                break;
            }
            catch (UnsupportedCharsetException ex) {
                if (!strict) break;
                throw ex;
            }
        }
        return new ContentType(mimeType, charset, params != null && params.length > 0 ? params : null);
    }

    public static ContentType create(String mimeType, NameValuePair ... params) throws UnsupportedCharsetException {
        String type = Args.notBlank(mimeType, "MIME type").toLowerCase(Locale.ROOT);
        Args.check(ContentType.valid(type), "MIME type may not contain reserved characters");
        return ContentType.create(mimeType, params, true);
    }

    public static ContentType parse(String s2) throws ParseException, UnsupportedCharsetException {
        Args.notNull(s2, "Content type");
        CharArrayBuffer buf = new CharArrayBuffer(s2.length());
        buf.append(s2);
        ParserCursor cursor = new ParserCursor(0, s2.length());
        HeaderElement[] elements = BasicHeaderValueParser.INSTANCE.parseElements(buf, cursor);
        if (elements.length > 0) {
            return ContentType.create(elements[0], true);
        }
        throw new ParseException("Invalid content type: " + s2);
    }

    public static ContentType get(HttpEntity entity) throws ParseException, UnsupportedCharsetException {
        HeaderElement[] elements;
        if (entity == null) {
            return null;
        }
        Header header = entity.getContentType();
        if (header != null && (elements = header.getElements()).length > 0) {
            return ContentType.create(elements[0], true);
        }
        return null;
    }

    public static ContentType getLenient(HttpEntity entity) {
        if (entity == null) {
            return null;
        }
        Header header = entity.getContentType();
        if (header != null) {
            try {
                HeaderElement[] elements = header.getElements();
                if (elements.length > 0) {
                    return ContentType.create(elements[0], false);
                }
            }
            catch (ParseException ex) {
                return null;
            }
        }
        return null;
    }

    public static ContentType getOrDefault(HttpEntity entity) throws ParseException, UnsupportedCharsetException {
        ContentType contentType = ContentType.get(entity);
        return contentType != null ? contentType : DEFAULT_TEXT;
    }

    public static ContentType getLenientOrDefault(HttpEntity entity) throws ParseException, UnsupportedCharsetException {
        ContentType contentType = ContentType.get(entity);
        return contentType != null ? contentType : DEFAULT_TEXT;
    }

    public static ContentType getByMimeType(String mimeType) {
        if (mimeType == null) {
            return null;
        }
        return CONTENT_TYPE_MAP.get(mimeType);
    }

    public ContentType withCharset(Charset charset) {
        return ContentType.create(this.getMimeType(), charset);
    }

    public ContentType withCharset(String charset) {
        return ContentType.create(this.getMimeType(), charset);
    }

    public ContentType withParameters(NameValuePair ... params) throws UnsupportedCharsetException {
        if (params.length == 0) {
            return this;
        }
        LinkedHashMap<String, String> paramMap = new LinkedHashMap<String, String>();
        if (this.params != null) {
            for (NameValuePair param : this.params) {
                paramMap.put(param.getName(), param.getValue());
            }
        }
        for (NameValuePair param : params) {
            paramMap.put(param.getName(), param.getValue());
        }
        ArrayList<BasicNameValuePair> newParams = new ArrayList<BasicNameValuePair>(paramMap.size() + 1);
        if (this.charset != null && !paramMap.containsKey("charset")) {
            newParams.add(new BasicNameValuePair("charset", this.charset.name()));
        }
        for (Map.Entry entry : paramMap.entrySet()) {
            newParams.add(new BasicNameValuePair((String)entry.getKey(), (String)entry.getValue()));
        }
        return ContentType.create(this.getMimeType(), newParams.toArray(new NameValuePair[newParams.size()]), true);
    }

    static {
        ContentType[] contentTypes = new ContentType[]{APPLICATION_ATOM_XML, APPLICATION_FORM_URLENCODED, APPLICATION_JSON, APPLICATION_SVG_XML, APPLICATION_XHTML_XML, APPLICATION_XML, IMAGE_BMP, IMAGE_GIF, IMAGE_JPEG, IMAGE_PNG, IMAGE_SVG, IMAGE_TIFF, IMAGE_WEBP, MULTIPART_FORM_DATA, TEXT_HTML, TEXT_PLAIN, TEXT_XML};
        HashMap<String, ContentType> map = new HashMap<String, ContentType>();
        for (ContentType contentType : contentTypes) {
            map.put(contentType.getMimeType(), contentType);
        }
        CONTENT_TYPE_MAP = Collections.unmodifiableMap(map);
        DEFAULT_TEXT = TEXT_PLAIN;
        DEFAULT_BINARY = APPLICATION_OCTET_STREAM;
    }
}

