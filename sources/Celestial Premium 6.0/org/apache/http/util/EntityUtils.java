/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.CharArrayBuffer;

public final class EntityUtils {
    private EntityUtils() {
    }

    public static void consumeQuietly(HttpEntity entity) {
        try {
            EntityUtils.consume(entity);
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    public static void consume(HttpEntity entity) throws IOException {
        InputStream instream;
        if (entity == null) {
            return;
        }
        if (entity.isStreaming() && (instream = entity.getContent()) != null) {
            instream.close();
        }
    }

    public static void updateEntity(HttpResponse response, HttpEntity entity) throws IOException {
        Args.notNull(response, "Response");
        EntityUtils.consume(response.getEntity());
        response.setEntity(entity);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static byte[] toByteArray(HttpEntity entity) throws IOException {
        Args.notNull(entity, "Entity");
        InputStream instream = entity.getContent();
        if (instream == null) {
            return null;
        }
        try {
            int l;
            Args.check(entity.getContentLength() <= Integer.MAX_VALUE, "HTTP entity too large to be buffered in memory");
            int i = (int)entity.getContentLength();
            if (i < 0) {
                i = 4096;
            }
            ByteArrayBuffer buffer = new ByteArrayBuffer(i);
            byte[] tmp = new byte[4096];
            while ((l = instream.read(tmp)) != -1) {
                buffer.append(tmp, 0, l);
            }
            byte[] arrby = buffer.toByteArray();
            return arrby;
        }
        finally {
            instream.close();
        }
    }

    @Deprecated
    public static String getContentCharSet(HttpEntity entity) throws ParseException {
        NameValuePair param;
        HeaderElement[] values;
        Args.notNull(entity, "Entity");
        String charset = null;
        if (entity.getContentType() != null && (values = entity.getContentType().getElements()).length > 0 && (param = values[0].getParameterByName("charset")) != null) {
            charset = param.getValue();
        }
        return charset;
    }

    @Deprecated
    public static String getContentMimeType(HttpEntity entity) throws ParseException {
        HeaderElement[] values;
        Args.notNull(entity, "Entity");
        String mimeType = null;
        if (entity.getContentType() != null && (values = entity.getContentType().getElements()).length > 0) {
            mimeType = values[0].getName();
        }
        return mimeType;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String toString(HttpEntity entity, Charset defaultCharset) throws IOException, ParseException {
        Args.notNull(entity, "Entity");
        InputStream instream = entity.getContent();
        if (instream == null) {
            return null;
        }
        try {
            int l;
            Args.check(entity.getContentLength() <= Integer.MAX_VALUE, "HTTP entity too large to be buffered in memory");
            int i = (int)entity.getContentLength();
            if (i < 0) {
                i = 4096;
            }
            Charset charset = null;
            try {
                ContentType contentType = ContentType.get(entity);
                if (contentType != null) {
                    charset = contentType.getCharset();
                }
            }
            catch (UnsupportedCharsetException ex) {
                throw new UnsupportedEncodingException(ex.getMessage());
            }
            if (charset == null) {
                charset = defaultCharset;
            }
            if (charset == null) {
                charset = HTTP.DEF_CONTENT_CHARSET;
            }
            InputStreamReader reader = new InputStreamReader(instream, charset);
            CharArrayBuffer buffer = new CharArrayBuffer(i);
            char[] tmp = new char[1024];
            while ((l = reader.read(tmp)) != -1) {
                buffer.append(tmp, 0, l);
            }
            String string = buffer.toString();
            return string;
        }
        finally {
            instream.close();
        }
    }

    public static String toString(HttpEntity entity, String defaultCharset) throws IOException, ParseException {
        return EntityUtils.toString(entity, defaultCharset != null ? Charset.forName(defaultCharset) : null);
    }

    public static String toString(HttpEntity entity) throws IOException, ParseException {
        return EntityUtils.toString(entity, (Charset)null);
    }
}

