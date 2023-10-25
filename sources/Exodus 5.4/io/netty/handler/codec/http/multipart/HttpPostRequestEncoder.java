/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpData;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostBodyUtil;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InternalAttribute;
import io.netty.handler.stream.ChunkedInput;
import io.netty.util.internal.ThreadLocalRandom;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Pattern;

public class HttpPostRequestEncoder
implements ChunkedInput<HttpContent> {
    private boolean isChunked;
    private boolean isLastChunkSent;
    private InterfaceHttpData currentData;
    private final HttpRequest request;
    String multipartMixedBoundary;
    private final boolean isMultipart;
    final List<InterfaceHttpData> multipartHttpDatas;
    private FileUpload currentFileUpload;
    private long globalBodySize;
    private final List<InterfaceHttpData> bodyListDatas;
    private final Charset charset;
    private final EncoderMode encoderMode;
    private static final Map<Pattern, String> percentEncodings = new HashMap<Pattern, String>();
    String multipartDataBoundary;
    private boolean isKey = true;
    private ByteBuf currentBuffer;
    private boolean duringMixedMode;
    private final HttpDataFactory factory;
    private ListIterator<InterfaceHttpData> iterator;
    private boolean isLastChunk;
    private boolean headerFinalized;

    public void addBodyFileUpload(String string, File file, String string2, boolean bl) throws ErrorDataEncoderException {
        if (string == null) {
            throw new NullPointerException("name");
        }
        if (file == null) {
            throw new NullPointerException("file");
        }
        String string3 = string2;
        String string4 = null;
        if (string2 == null) {
            string3 = bl ? "text/plain" : "application/octet-stream";
        }
        if (!bl) {
            string4 = HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value();
        }
        FileUpload fileUpload = this.factory.createFileUpload(this.request, string, file.getName(), string3, string4, null, file.length());
        try {
            fileUpload.setContent(file);
        }
        catch (IOException iOException) {
            throw new ErrorDataEncoderException(iOException);
        }
        this.addBodyHttpData(fileUpload);
    }

    public HttpPostRequestEncoder(HttpDataFactory httpDataFactory, HttpRequest httpRequest, boolean bl) throws ErrorDataEncoderException {
        this(httpDataFactory, httpRequest, bl, HttpConstants.DEFAULT_CHARSET, EncoderMode.RFC1738);
    }

    public void addBodyAttribute(String string, String string2) throws ErrorDataEncoderException {
        if (string == null) {
            throw new NullPointerException("name");
        }
        String string3 = string2;
        if (string2 == null) {
            string3 = "";
        }
        Attribute attribute = this.factory.createAttribute(this.request, string, string3);
        this.addBodyHttpData(attribute);
    }

    @Override
    public void close() throws Exception {
    }

    public void cleanFiles() {
        this.factory.cleanRequestHttpDatas(this.request);
    }

    public void setBodyHttpDatas(List<InterfaceHttpData> list) throws ErrorDataEncoderException {
        if (list == null) {
            throw new NullPointerException("datas");
        }
        this.globalBodySize = 0L;
        this.bodyListDatas.clear();
        this.currentFileUpload = null;
        this.duringMixedMode = false;
        this.multipartHttpDatas.clear();
        for (InterfaceHttpData interfaceHttpData : list) {
            this.addBodyHttpData(interfaceHttpData);
        }
    }

    public List<InterfaceHttpData> getBodyListAttributes() {
        return this.bodyListDatas;
    }

    public HttpRequest finalizeRequest() throws ErrorDataEncoderException {
        Object object;
        Object object2;
        if (!this.headerFinalized) {
            if (this.isMultipart) {
                object2 = new InternalAttribute(this.charset);
                if (this.duringMixedMode) {
                    ((InternalAttribute)object2).addValue("\r\n--" + this.multipartMixedBoundary + "--");
                }
                ((InternalAttribute)object2).addValue("\r\n--" + this.multipartDataBoundary + "--\r\n");
                this.multipartHttpDatas.add((InterfaceHttpData)object2);
                this.multipartMixedBoundary = null;
                this.currentFileUpload = null;
                this.duringMixedMode = false;
                this.globalBodySize += (long)((InternalAttribute)object2).size();
            }
        } else {
            throw new ErrorDataEncoderException("Header already encoded");
        }
        this.headerFinalized = true;
        object2 = this.request.headers();
        List<String> list = ((HttpHeaders)object2).getAll("Content-Type");
        List<String> list2 = ((HttpHeaders)object2).getAll("Transfer-Encoding");
        if (list != null) {
            ((HttpHeaders)object2).remove("Content-Type");
            for (String string : list) {
                object = string.toLowerCase();
                if (((String)object).startsWith("multipart/form-data") || ((String)object).startsWith("application/x-www-form-urlencoded")) continue;
                ((HttpHeaders)object2).add("Content-Type", (Object)string);
            }
        }
        if (this.isMultipart) {
            String string = "multipart/form-data; boundary=" + this.multipartDataBoundary;
            ((HttpHeaders)object2).add("Content-Type", (Object)string);
        } else {
            ((HttpHeaders)object2).add("Content-Type", (Object)"application/x-www-form-urlencoded");
        }
        long l = this.globalBodySize;
        if (this.isMultipart) {
            this.iterator = this.multipartHttpDatas.listIterator();
        } else {
            --l;
            this.iterator = this.multipartHttpDatas.listIterator();
        }
        ((HttpHeaders)object2).set("Content-Length", (Object)String.valueOf(l));
        if (l > 8096L || this.isMultipart) {
            this.isChunked = true;
            if (list2 != null) {
                ((HttpHeaders)object2).remove("Transfer-Encoding");
                for (String string : list2) {
                    if (string.equalsIgnoreCase("chunked")) continue;
                    ((HttpHeaders)object2).add("Transfer-Encoding", (Object)string);
                }
            }
            HttpHeaders.setTransferEncodingChunked(this.request);
            return new WrappedHttpRequest(this.request);
        }
        object = this.nextChunk();
        if (this.request instanceof FullHttpRequest) {
            FullHttpRequest fullHttpRequest = (FullHttpRequest)this.request;
            ByteBuf byteBuf = object.content();
            if (fullHttpRequest.content() != byteBuf) {
                fullHttpRequest.content().clear().writeBytes(byteBuf);
                byteBuf.release();
            }
            return fullHttpRequest;
        }
        return new WrappedFullHttpRequest(this.request, (HttpContent)object);
    }

    public void addBodyFileUploads(String string, File[] fileArray, String[] stringArray, boolean[] blArray) throws ErrorDataEncoderException {
        if (fileArray.length != stringArray.length && fileArray.length != blArray.length) {
            throw new NullPointerException("Different array length");
        }
        for (int i = 0; i < fileArray.length; ++i) {
            this.addBodyFileUpload(string, fileArray[i], stringArray[i], blArray[i]);
        }
    }

    private HttpContent nextChunk() throws ErrorDataEncoderException {
        HttpContent httpContent;
        if (this.isLastChunk) {
            this.isLastChunkSent = true;
            return LastHttpContent.EMPTY_LAST_CONTENT;
        }
        int n = 8096;
        if (this.currentBuffer != null) {
            n -= this.currentBuffer.readableBytes();
        }
        if (n <= 0) {
            ByteBuf byteBuf = this.fillByteBuf();
            return new DefaultHttpContent(byteBuf);
        }
        if (this.currentData != null) {
            if (this.isMultipart ? (httpContent = this.encodeNextChunkMultipart(n)) != null : (httpContent = this.encodeNextChunkUrlEncoded(n)) != null) {
                return httpContent;
            }
            n = 8096 - this.currentBuffer.readableBytes();
        }
        if (!this.iterator.hasNext()) {
            this.isLastChunk = true;
            ByteBuf byteBuf = this.currentBuffer;
            this.currentBuffer = null;
            return new DefaultHttpContent(byteBuf);
        }
        while (n > 0 && this.iterator.hasNext()) {
            this.currentData = this.iterator.next();
            httpContent = this.isMultipart ? this.encodeNextChunkMultipart(n) : this.encodeNextChunkUrlEncoded(n);
            if (httpContent == null) {
                n = 8096 - this.currentBuffer.readableBytes();
                continue;
            }
            return httpContent;
        }
        this.isLastChunk = true;
        if (this.currentBuffer == null) {
            this.isLastChunkSent = true;
            return LastHttpContent.EMPTY_LAST_CONTENT;
        }
        ByteBuf byteBuf = this.currentBuffer;
        this.currentBuffer = null;
        return new DefaultHttpContent(byteBuf);
    }

    private HttpContent encodeNextChunkUrlEncoded(int n) throws ErrorDataEncoderException {
        ByteBuf byteBuf;
        String string;
        if (this.currentData == null) {
            return null;
        }
        int n2 = n;
        if (this.isKey) {
            string = this.currentData.getName();
            byteBuf = Unpooled.wrappedBuffer(string.getBytes());
            this.isKey = false;
            if (this.currentBuffer == null) {
                this.currentBuffer = Unpooled.wrappedBuffer(byteBuf, Unpooled.wrappedBuffer("=".getBytes()));
                n2 -= byteBuf.readableBytes() + 1;
            } else {
                this.currentBuffer = Unpooled.wrappedBuffer(this.currentBuffer, byteBuf, Unpooled.wrappedBuffer("=".getBytes()));
                n2 -= byteBuf.readableBytes() + 1;
            }
            if (this.currentBuffer.readableBytes() >= 8096) {
                byteBuf = this.fillByteBuf();
                return new DefaultHttpContent(byteBuf);
            }
        }
        try {
            byteBuf = ((HttpData)this.currentData).getChunk(n2);
        }
        catch (IOException iOException) {
            throw new ErrorDataEncoderException(iOException);
        }
        string = null;
        if (byteBuf.readableBytes() < n2) {
            this.isKey = true;
            String string2 = string = this.iterator.hasNext() ? Unpooled.wrappedBuffer("&".getBytes()) : null;
        }
        if (byteBuf.capacity() == 0) {
            this.currentData = null;
            if (this.currentBuffer == null) {
                this.currentBuffer = string;
            } else if (string != null) {
                this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[]{this.currentBuffer, string});
            }
            if (this.currentBuffer.readableBytes() >= 8096) {
                byteBuf = this.fillByteBuf();
                return new DefaultHttpContent(byteBuf);
            }
            return null;
        }
        this.currentBuffer = this.currentBuffer == null ? (string != null ? Unpooled.wrappedBuffer(new ByteBuf[]{byteBuf, string}) : byteBuf) : (string != null ? Unpooled.wrappedBuffer(new ByteBuf[]{this.currentBuffer, byteBuf, string}) : Unpooled.wrappedBuffer(this.currentBuffer, byteBuf));
        if (this.currentBuffer.readableBytes() < 8096) {
            this.currentData = null;
            this.isKey = true;
            return null;
        }
        byteBuf = this.fillByteBuf();
        return new DefaultHttpContent(byteBuf);
    }

    @Override
    public HttpContent readChunk(ChannelHandlerContext channelHandlerContext) throws Exception {
        if (this.isLastChunkSent) {
            return null;
        }
        return this.nextChunk();
    }

    public HttpPostRequestEncoder(HttpRequest httpRequest, boolean bl) throws ErrorDataEncoderException {
        this(new DefaultHttpDataFactory(16384L), httpRequest, bl, HttpConstants.DEFAULT_CHARSET, EncoderMode.RFC1738);
    }

    public boolean isMultipart() {
        return this.isMultipart;
    }

    private static String getNewMultipartDelimiter() {
        return Long.toHexString(ThreadLocalRandom.current().nextLong()).toLowerCase();
    }

    private ByteBuf fillByteBuf() {
        int n = this.currentBuffer.readableBytes();
        if (n > 8096) {
            ByteBuf byteBuf = this.currentBuffer.slice(this.currentBuffer.readerIndex(), 8096);
            this.currentBuffer.skipBytes(8096);
            return byteBuf;
        }
        ByteBuf byteBuf = this.currentBuffer;
        this.currentBuffer = null;
        return byteBuf;
    }

    public void addBodyHttpData(InterfaceHttpData interfaceHttpData) throws ErrorDataEncoderException {
        if (this.headerFinalized) {
            throw new ErrorDataEncoderException("Cannot add value once finalized");
        }
        if (interfaceHttpData == null) {
            throw new NullPointerException("data");
        }
        this.bodyListDatas.add(interfaceHttpData);
        if (!this.isMultipart) {
            if (interfaceHttpData instanceof Attribute) {
                Attribute attribute = (Attribute)interfaceHttpData;
                try {
                    String string = this.encodeAttribute(attribute.getName(), this.charset);
                    String string2 = this.encodeAttribute(attribute.getValue(), this.charset);
                    Attribute attribute2 = this.factory.createAttribute(this.request, string, string2);
                    this.multipartHttpDatas.add(attribute2);
                    this.globalBodySize += (long)(attribute2.getName().length() + 1) + attribute2.length() + 1L;
                }
                catch (IOException iOException) {
                    throw new ErrorDataEncoderException(iOException);
                }
            } else if (interfaceHttpData instanceof FileUpload) {
                FileUpload fileUpload = (FileUpload)interfaceHttpData;
                String string = this.encodeAttribute(fileUpload.getName(), this.charset);
                String string3 = this.encodeAttribute(fileUpload.getFilename(), this.charset);
                Attribute attribute = this.factory.createAttribute(this.request, string, string3);
                this.multipartHttpDatas.add(attribute);
                this.globalBodySize += (long)(attribute.getName().length() + 1) + attribute.length() + 1L;
            }
            return;
        }
        if (interfaceHttpData instanceof Attribute) {
            InternalAttribute internalAttribute;
            if (this.duringMixedMode) {
                internalAttribute = new InternalAttribute(this.charset);
                internalAttribute.addValue("\r\n--" + this.multipartMixedBoundary + "--");
                this.multipartHttpDatas.add(internalAttribute);
                this.multipartMixedBoundary = null;
                this.currentFileUpload = null;
                this.duringMixedMode = false;
            }
            internalAttribute = new InternalAttribute(this.charset);
            if (!this.multipartHttpDatas.isEmpty()) {
                internalAttribute.addValue("\r\n");
            }
            internalAttribute.addValue("--" + this.multipartDataBoundary + "\r\n");
            Attribute attribute = (Attribute)interfaceHttpData;
            internalAttribute.addValue("Content-Disposition: form-data; name=\"" + attribute.getName() + "\"\r\n");
            Charset charset = attribute.getCharset();
            if (charset != null) {
                internalAttribute.addValue("Content-Type: text/plain; charset=" + charset + "\r\n");
            }
            internalAttribute.addValue("\r\n");
            this.multipartHttpDatas.add(internalAttribute);
            this.multipartHttpDatas.add(interfaceHttpData);
            this.globalBodySize += attribute.length() + (long)internalAttribute.size();
        } else if (interfaceHttpData instanceof FileUpload) {
            Object object;
            boolean bl;
            FileUpload fileUpload = (FileUpload)interfaceHttpData;
            InternalAttribute internalAttribute = new InternalAttribute(this.charset);
            if (!this.multipartHttpDatas.isEmpty()) {
                internalAttribute.addValue("\r\n");
            }
            if (this.duringMixedMode) {
                if (this.currentFileUpload != null && this.currentFileUpload.getName().equals(fileUpload.getName())) {
                    bl = true;
                } else {
                    internalAttribute.addValue("--" + this.multipartMixedBoundary + "--");
                    this.multipartHttpDatas.add(internalAttribute);
                    this.multipartMixedBoundary = null;
                    internalAttribute = new InternalAttribute(this.charset);
                    internalAttribute.addValue("\r\n");
                    bl = false;
                    this.currentFileUpload = fileUpload;
                    this.duringMixedMode = false;
                }
            } else if (this.currentFileUpload != null && this.currentFileUpload.getName().equals(fileUpload.getName())) {
                this.initMixedMultipart();
                object = (InternalAttribute)this.multipartHttpDatas.get(this.multipartHttpDatas.size() - 2);
                this.globalBodySize -= (long)((InternalAttribute)object).size();
                StringBuilder stringBuilder = new StringBuilder(139 + this.multipartDataBoundary.length() + this.multipartMixedBoundary.length() * 2 + fileUpload.getFilename().length() + fileUpload.getName().length());
                stringBuilder.append("--");
                stringBuilder.append(this.multipartDataBoundary);
                stringBuilder.append("\r\n");
                stringBuilder.append("Content-Disposition");
                stringBuilder.append(": ");
                stringBuilder.append("form-data");
                stringBuilder.append("; ");
                stringBuilder.append("name");
                stringBuilder.append("=\"");
                stringBuilder.append(fileUpload.getName());
                stringBuilder.append("\"\r\n");
                stringBuilder.append("Content-Type");
                stringBuilder.append(": ");
                stringBuilder.append("multipart/mixed");
                stringBuilder.append("; ");
                stringBuilder.append("boundary");
                stringBuilder.append('=');
                stringBuilder.append(this.multipartMixedBoundary);
                stringBuilder.append("\r\n\r\n");
                stringBuilder.append("--");
                stringBuilder.append(this.multipartMixedBoundary);
                stringBuilder.append("\r\n");
                stringBuilder.append("Content-Disposition");
                stringBuilder.append(": ");
                stringBuilder.append("attachment");
                stringBuilder.append("; ");
                stringBuilder.append("filename");
                stringBuilder.append("=\"");
                stringBuilder.append(fileUpload.getFilename());
                stringBuilder.append("\"\r\n");
                ((InternalAttribute)object).setValue(stringBuilder.toString(), 1);
                ((InternalAttribute)object).setValue("", 2);
                this.globalBodySize += (long)((InternalAttribute)object).size();
                bl = true;
                this.duringMixedMode = true;
            } else {
                bl = false;
                this.currentFileUpload = fileUpload;
                this.duringMixedMode = false;
            }
            if (bl) {
                internalAttribute.addValue("--" + this.multipartMixedBoundary + "\r\n");
                internalAttribute.addValue("Content-Disposition: attachment; filename=\"" + fileUpload.getFilename() + "\"\r\n");
            } else {
                internalAttribute.addValue("--" + this.multipartDataBoundary + "\r\n");
                internalAttribute.addValue("Content-Disposition: form-data; name=\"" + fileUpload.getName() + "\"; " + "filename" + "=\"" + fileUpload.getFilename() + "\"\r\n");
            }
            internalAttribute.addValue("Content-Type: " + fileUpload.getContentType());
            object = fileUpload.getContentTransferEncoding();
            if (object != null && ((String)object).equals(HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value())) {
                internalAttribute.addValue("\r\nContent-Transfer-Encoding: " + HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value() + "\r\n\r\n");
            } else if (fileUpload.getCharset() != null) {
                internalAttribute.addValue("; charset=" + fileUpload.getCharset() + "\r\n\r\n");
            } else {
                internalAttribute.addValue("\r\n\r\n");
            }
            this.multipartHttpDatas.add(internalAttribute);
            this.multipartHttpDatas.add(interfaceHttpData);
            this.globalBodySize += fileUpload.length() + (long)internalAttribute.size();
        }
    }

    public HttpPostRequestEncoder(HttpDataFactory httpDataFactory, HttpRequest httpRequest, boolean bl, Charset charset, EncoderMode encoderMode) throws ErrorDataEncoderException {
        if (httpDataFactory == null) {
            throw new NullPointerException("factory");
        }
        if (httpRequest == null) {
            throw new NullPointerException("request");
        }
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        if (httpRequest.getMethod() != HttpMethod.POST) {
            throw new ErrorDataEncoderException("Cannot create a Encoder if not a POST");
        }
        this.request = httpRequest;
        this.charset = charset;
        this.factory = httpDataFactory;
        this.bodyListDatas = new ArrayList<InterfaceHttpData>();
        this.isLastChunk = false;
        this.isLastChunkSent = false;
        this.isMultipart = bl;
        this.multipartHttpDatas = new ArrayList<InterfaceHttpData>();
        this.encoderMode = encoderMode;
        if (this.isMultipart) {
            this.initDataMultipart();
        }
    }

    private void initMixedMultipart() {
        this.multipartMixedBoundary = HttpPostRequestEncoder.getNewMultipartDelimiter();
    }

    private HttpContent encodeNextChunkMultipart(int n) throws ErrorDataEncoderException {
        ByteBuf byteBuf;
        if (this.currentData == null) {
            return null;
        }
        if (this.currentData instanceof InternalAttribute) {
            byteBuf = ((InternalAttribute)this.currentData).toByteBuf();
            this.currentData = null;
        } else {
            if (this.currentData instanceof Attribute) {
                try {
                    byteBuf = ((Attribute)this.currentData).getChunk(n);
                }
                catch (IOException iOException) {
                    throw new ErrorDataEncoderException(iOException);
                }
            }
            try {
                byteBuf = ((HttpData)this.currentData).getChunk(n);
            }
            catch (IOException iOException) {
                throw new ErrorDataEncoderException(iOException);
            }
            if (byteBuf.capacity() == 0) {
                this.currentData = null;
                return null;
            }
        }
        this.currentBuffer = this.currentBuffer == null ? byteBuf : Unpooled.wrappedBuffer(this.currentBuffer, byteBuf);
        if (this.currentBuffer.readableBytes() < 8096) {
            this.currentData = null;
            return null;
        }
        byteBuf = this.fillByteBuf();
        return new DefaultHttpContent(byteBuf);
    }

    private void initDataMultipart() {
        this.multipartDataBoundary = HttpPostRequestEncoder.getNewMultipartDelimiter();
    }

    static {
        percentEncodings.put(Pattern.compile("\\*"), "%2A");
        percentEncodings.put(Pattern.compile("\\+"), "%20");
        percentEncodings.put(Pattern.compile("%7E"), "~");
    }

    public boolean isChunked() {
        return this.isChunked;
    }

    @Override
    public boolean isEndOfInput() throws Exception {
        return this.isLastChunkSent;
    }

    private String encodeAttribute(String string, Charset charset) throws ErrorDataEncoderException {
        if (string == null) {
            return "";
        }
        try {
            String string2 = URLEncoder.encode(string, charset.name());
            if (this.encoderMode == EncoderMode.RFC3986) {
                for (Map.Entry<Pattern, String> entry : percentEncodings.entrySet()) {
                    String string3 = entry.getValue();
                    string2 = entry.getKey().matcher(string2).replaceAll(string3);
                }
            }
            return string2;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new ErrorDataEncoderException(charset.name(), unsupportedEncodingException);
        }
    }

    private static class WrappedHttpRequest
    implements HttpRequest {
        private final HttpRequest request;

        @Override
        public HttpRequest setMethod(HttpMethod httpMethod) {
            this.request.setMethod(httpMethod);
            return this;
        }

        @Override
        public HttpMethod getMethod() {
            return this.request.getMethod();
        }

        @Override
        public HttpHeaders headers() {
            return this.request.headers();
        }

        @Override
        public String getUri() {
            return this.request.getUri();
        }

        @Override
        public HttpVersion getProtocolVersion() {
            return this.request.getProtocolVersion();
        }

        @Override
        public HttpRequest setUri(String string) {
            this.request.setUri(string);
            return this;
        }

        @Override
        public void setDecoderResult(DecoderResult decoderResult) {
            this.request.setDecoderResult(decoderResult);
        }

        @Override
        public HttpRequest setProtocolVersion(HttpVersion httpVersion) {
            this.request.setProtocolVersion(httpVersion);
            return this;
        }

        WrappedHttpRequest(HttpRequest httpRequest) {
            this.request = httpRequest;
        }

        @Override
        public DecoderResult getDecoderResult() {
            return this.request.getDecoderResult();
        }
    }

    public static class ErrorDataEncoderException
    extends Exception {
        private static final long serialVersionUID = 5020247425493164465L;

        public ErrorDataEncoderException(Throwable throwable) {
            super(throwable);
        }

        public ErrorDataEncoderException(String string) {
            super(string);
        }

        public ErrorDataEncoderException(String string, Throwable throwable) {
            super(string, throwable);
        }

        public ErrorDataEncoderException() {
        }
    }

    public static enum EncoderMode {
        RFC1738,
        RFC3986;

    }

    private static final class WrappedFullHttpRequest
    extends WrappedHttpRequest
    implements FullHttpRequest {
        private final HttpContent content;

        @Override
        public FullHttpRequest setMethod(HttpMethod httpMethod) {
            super.setMethod(httpMethod);
            return this;
        }

        @Override
        public FullHttpRequest setUri(String string) {
            super.setUri(string);
            return this;
        }

        @Override
        public FullHttpRequest copy() {
            DefaultFullHttpRequest defaultFullHttpRequest = new DefaultFullHttpRequest(this.getProtocolVersion(), this.getMethod(), this.getUri(), this.content().copy());
            defaultFullHttpRequest.headers().set(this.headers());
            defaultFullHttpRequest.trailingHeaders().set(this.trailingHeaders());
            return defaultFullHttpRequest;
        }

        @Override
        public FullHttpRequest retain() {
            this.content.retain();
            return this;
        }

        @Override
        public FullHttpRequest setProtocolVersion(HttpVersion httpVersion) {
            super.setProtocolVersion(httpVersion);
            return this;
        }

        @Override
        public HttpHeaders trailingHeaders() {
            if (this.content instanceof LastHttpContent) {
                return ((LastHttpContent)this.content).trailingHeaders();
            }
            return HttpHeaders.EMPTY_HEADERS;
        }

        @Override
        public ByteBuf content() {
            return this.content.content();
        }

        @Override
        public int refCnt() {
            return this.content.refCnt();
        }

        @Override
        public FullHttpRequest duplicate() {
            DefaultFullHttpRequest defaultFullHttpRequest = new DefaultFullHttpRequest(this.getProtocolVersion(), this.getMethod(), this.getUri(), this.content().duplicate());
            defaultFullHttpRequest.headers().set(this.headers());
            defaultFullHttpRequest.trailingHeaders().set(this.trailingHeaders());
            return defaultFullHttpRequest;
        }

        @Override
        public boolean release(int n) {
            return this.content.release(n);
        }

        private WrappedFullHttpRequest(HttpRequest httpRequest, HttpContent httpContent) {
            super(httpRequest);
            this.content = httpContent;
        }

        @Override
        public FullHttpRequest retain(int n) {
            this.content.retain(n);
            return this;
        }

        @Override
        public boolean release() {
            return this.content.release();
        }
    }
}

