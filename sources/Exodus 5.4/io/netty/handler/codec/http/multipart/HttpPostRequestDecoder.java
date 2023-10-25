/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.CaseIgnoringComparator;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpData;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostBodyUtil;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HttpPostRequestDecoder {
    private String multipartDataBoundary;
    private final HttpDataFactory factory;
    private Map<String, Attribute> currentFieldAttributes;
    private static final int DEFAULT_DISCARD_THRESHOLD = 0xA00000;
    private String multipartMixedBoundary;
    private int discardThreshold = 0xA00000;
    private final Charset charset;
    private final List<InterfaceHttpData> bodyListHttpData = new ArrayList<InterfaceHttpData>();
    private FileUpload currentFileUpload;
    private boolean bodyToDecode;
    private boolean isLastChunk;
    private boolean destroyed;
    private int bodyListHttpDataRank;
    private final Map<String, List<InterfaceHttpData>> bodyMapHttpData = new TreeMap<CharSequence, List<InterfaceHttpData>>(CaseIgnoringComparator.INSTANCE);
    private Attribute currentAttribute;
    private ByteBuf undecodedChunk;
    private final HttpRequest request;
    private boolean isMultipart;
    private MultiPartStatus currentStatus = MultiPartStatus.NOTSTARTED;

    public void cleanFiles() {
        this.checkDestroyed();
        this.factory.cleanRequestHttpDatas(this.request);
    }

    public InterfaceHttpData next() throws EndOfDataDecoderException {
        this.checkDestroyed();
        if (this.hasNext()) {
            return this.bodyListHttpData.get(this.bodyListHttpDataRank++);
        }
        return null;
    }

    private void readFileUploadByteMultipartStandard(String string) throws ErrorDataDecoderException, NotEnoughDataDecoderException {
        int n = this.undecodedChunk.readerIndex();
        boolean bl = true;
        int n2 = 0;
        int n3 = this.undecodedChunk.readerIndex();
        boolean bl2 = false;
        while (this.undecodedChunk.isReadable()) {
            byte by = this.undecodedChunk.readByte();
            if (bl) {
                if (by == string.codePointAt(n2)) {
                    if (string.length() != ++n2) continue;
                    bl2 = true;
                    break;
                }
                bl = false;
                n2 = 0;
                if (by == 13) {
                    if (!this.undecodedChunk.isReadable()) continue;
                    by = this.undecodedChunk.readByte();
                    if (by == 10) {
                        bl = true;
                        n2 = 0;
                        n3 = this.undecodedChunk.readerIndex() - 2;
                        continue;
                    }
                    n3 = this.undecodedChunk.readerIndex() - 1;
                    this.undecodedChunk.readerIndex(n3);
                    continue;
                }
                if (by == 10) {
                    bl = true;
                    n2 = 0;
                    n3 = this.undecodedChunk.readerIndex() - 1;
                    continue;
                }
                n3 = this.undecodedChunk.readerIndex();
                continue;
            }
            if (by == 13) {
                if (!this.undecodedChunk.isReadable()) continue;
                by = this.undecodedChunk.readByte();
                if (by == 10) {
                    bl = true;
                    n2 = 0;
                    n3 = this.undecodedChunk.readerIndex() - 2;
                    continue;
                }
                n3 = this.undecodedChunk.readerIndex() - 1;
                this.undecodedChunk.readerIndex(n3);
                continue;
            }
            if (by == 10) {
                bl = true;
                n2 = 0;
                n3 = this.undecodedChunk.readerIndex() - 1;
                continue;
            }
            n3 = this.undecodedChunk.readerIndex();
        }
        ByteBuf byteBuf = this.undecodedChunk.copy(n, n3 - n);
        if (bl2) {
            try {
                this.currentFileUpload.addContent(byteBuf, true);
                this.undecodedChunk.readerIndex(n3);
            }
            catch (IOException iOException) {
                throw new ErrorDataDecoderException(iOException);
            }
        } else {
            this.currentFileUpload.addContent(byteBuf, false);
            this.undecodedChunk.readerIndex(n3);
            throw new NotEnoughDataDecoderException();
        }
    }

    private InterfaceHttpData findMultipartDisposition() throws ErrorDataDecoderException {
        Object object;
        int n = this.undecodedChunk.readerIndex();
        if (this.currentStatus == MultiPartStatus.DISPOSITION) {
            this.currentFieldAttributes = new TreeMap<CharSequence, Attribute>(CaseIgnoringComparator.INSTANCE);
        }
        while (!this.skipOneLine()) {
            Object object2;
            try {
                this.skipControlCharacters();
                object = this.readLine();
            }
            catch (NotEnoughDataDecoderException notEnoughDataDecoderException) {
                this.undecodedChunk.readerIndex(n);
                return null;
            }
            String[] stringArray = HttpPostRequestDecoder.splitMultipartHeader((String)object);
            if (stringArray[0].equalsIgnoreCase("Content-Disposition")) {
                boolean bl;
                if (this.currentStatus == MultiPartStatus.DISPOSITION) {
                    bl = stringArray[1].equalsIgnoreCase("form-data");
                } else {
                    boolean bl2 = bl = stringArray[1].equalsIgnoreCase("attachment") || stringArray[1].equalsIgnoreCase("file");
                }
                if (!bl) continue;
                for (int i = 2; i < stringArray.length; ++i) {
                    Attribute attribute;
                    object2 = StringUtil.split(stringArray[i], '=');
                    try {
                        String string = HttpPostRequestDecoder.cleanString(object2[0]);
                        String string2 = object2[1];
                        string2 = "filename".equals(string) ? string2.substring(1, string2.length() - 1) : HttpPostRequestDecoder.cleanString(string2);
                        attribute = this.factory.createAttribute(this.request, string, string2);
                    }
                    catch (NullPointerException nullPointerException) {
                        throw new ErrorDataDecoderException(nullPointerException);
                    }
                    catch (IllegalArgumentException illegalArgumentException) {
                        throw new ErrorDataDecoderException(illegalArgumentException);
                    }
                    this.currentFieldAttributes.put(attribute.getName(), attribute);
                }
                continue;
            }
            if (stringArray[0].equalsIgnoreCase("Content-Transfer-Encoding")) {
                Attribute attribute;
                try {
                    attribute = this.factory.createAttribute(this.request, "Content-Transfer-Encoding", HttpPostRequestDecoder.cleanString(stringArray[1]));
                }
                catch (NullPointerException nullPointerException) {
                    throw new ErrorDataDecoderException(nullPointerException);
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    throw new ErrorDataDecoderException(illegalArgumentException);
                }
                this.currentFieldAttributes.put("Content-Transfer-Encoding", attribute);
                continue;
            }
            if (stringArray[0].equalsIgnoreCase("Content-Length")) {
                Attribute attribute;
                try {
                    attribute = this.factory.createAttribute(this.request, "Content-Length", HttpPostRequestDecoder.cleanString(stringArray[1]));
                }
                catch (NullPointerException nullPointerException) {
                    throw new ErrorDataDecoderException(nullPointerException);
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    throw new ErrorDataDecoderException(illegalArgumentException);
                }
                this.currentFieldAttributes.put("Content-Length", attribute);
                continue;
            }
            if (stringArray[0].equalsIgnoreCase("Content-Type")) {
                if (stringArray[1].equalsIgnoreCase("multipart/mixed")) {
                    if (this.currentStatus == MultiPartStatus.DISPOSITION) {
                        String[] stringArray2 = StringUtil.split(stringArray[2], '=');
                        this.multipartMixedBoundary = "--" + stringArray2[1];
                        this.currentStatus = MultiPartStatus.MIXEDDELIMITER;
                        return this.decodeMultipart(MultiPartStatus.MIXEDDELIMITER);
                    }
                    throw new ErrorDataDecoderException("Mixed Multipart found in a previous Mixed Multipart");
                }
                for (int i = 1; i < stringArray.length; ++i) {
                    Object object3;
                    if (stringArray[i].toLowerCase().startsWith("charset")) {
                        object3 = StringUtil.split(stringArray[i], '=');
                        try {
                            object2 = this.factory.createAttribute(this.request, "charset", HttpPostRequestDecoder.cleanString(object3[1]));
                        }
                        catch (NullPointerException nullPointerException) {
                            throw new ErrorDataDecoderException(nullPointerException);
                        }
                        catch (IllegalArgumentException illegalArgumentException) {
                            throw new ErrorDataDecoderException(illegalArgumentException);
                        }
                        this.currentFieldAttributes.put("charset", (Attribute)object2);
                        continue;
                    }
                    try {
                        object3 = this.factory.createAttribute(this.request, HttpPostRequestDecoder.cleanString(stringArray[0]), stringArray[i]);
                    }
                    catch (NullPointerException nullPointerException) {
                        throw new ErrorDataDecoderException(nullPointerException);
                    }
                    catch (IllegalArgumentException illegalArgumentException) {
                        throw new ErrorDataDecoderException(illegalArgumentException);
                    }
                    this.currentFieldAttributes.put(object3.getName(), (Attribute)object3);
                }
                continue;
            }
            throw new ErrorDataDecoderException("Unknown Params: " + (String)object);
        }
        object = this.currentFieldAttributes.get("filename");
        if (this.currentStatus == MultiPartStatus.DISPOSITION) {
            if (object != null) {
                this.currentStatus = MultiPartStatus.FILEUPLOAD;
                return this.decodeMultipart(MultiPartStatus.FILEUPLOAD);
            }
            this.currentStatus = MultiPartStatus.FIELD;
            return this.decodeMultipart(MultiPartStatus.FIELD);
        }
        if (object != null) {
            this.currentStatus = MultiPartStatus.MIXEDFILEUPLOAD;
            return this.decodeMultipart(MultiPartStatus.MIXEDFILEUPLOAD);
        }
        throw new ErrorDataDecoderException("Filename not found");
    }

    private void cleanMixedAttributes() {
        this.currentFieldAttributes.remove("charset");
        this.currentFieldAttributes.remove("Content-Length");
        this.currentFieldAttributes.remove("Content-Transfer-Encoding");
        this.currentFieldAttributes.remove("Content-Type");
        this.currentFieldAttributes.remove("filename");
    }

    private void readFileUploadByteMultipart(String string) throws ErrorDataDecoderException, NotEnoughDataDecoderException {
        HttpPostBodyUtil.SeekAheadOptimize seekAheadOptimize;
        try {
            seekAheadOptimize = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
        }
        catch (HttpPostBodyUtil.SeekAheadNoBackArrayException seekAheadNoBackArrayException) {
            this.readFileUploadByteMultipartStandard(string);
            return;
        }
        int n = this.undecodedChunk.readerIndex();
        boolean bl = true;
        int n2 = 0;
        int n3 = seekAheadOptimize.pos;
        boolean bl2 = false;
        while (seekAheadOptimize.pos < seekAheadOptimize.limit) {
            byte by = seekAheadOptimize.bytes[seekAheadOptimize.pos++];
            if (bl) {
                if (by == string.codePointAt(n2)) {
                    if (string.length() != ++n2) continue;
                    bl2 = true;
                    break;
                }
                bl = false;
                n2 = 0;
                if (by == 13) {
                    if (seekAheadOptimize.pos >= seekAheadOptimize.limit) continue;
                    if ((by = seekAheadOptimize.bytes[seekAheadOptimize.pos++]) == 10) {
                        bl = true;
                        n2 = 0;
                        n3 = seekAheadOptimize.pos - 2;
                        continue;
                    }
                    n3 = --seekAheadOptimize.pos;
                    continue;
                }
                if (by == 10) {
                    bl = true;
                    n2 = 0;
                    n3 = seekAheadOptimize.pos - 1;
                    continue;
                }
                n3 = seekAheadOptimize.pos;
                continue;
            }
            if (by == 13) {
                if (seekAheadOptimize.pos >= seekAheadOptimize.limit) continue;
                if ((by = seekAheadOptimize.bytes[seekAheadOptimize.pos++]) == 10) {
                    bl = true;
                    n2 = 0;
                    n3 = seekAheadOptimize.pos - 2;
                    continue;
                }
                n3 = --seekAheadOptimize.pos;
                continue;
            }
            if (by == 10) {
                bl = true;
                n2 = 0;
                n3 = seekAheadOptimize.pos - 1;
                continue;
            }
            n3 = seekAheadOptimize.pos;
        }
        int n4 = seekAheadOptimize.getReadPosition(n3);
        ByteBuf byteBuf = this.undecodedChunk.copy(n, n4 - n);
        if (bl2) {
            try {
                this.currentFileUpload.addContent(byteBuf, true);
                this.undecodedChunk.readerIndex(n4);
            }
            catch (IOException iOException) {
                throw new ErrorDataDecoderException(iOException);
            }
        } else {
            this.currentFileUpload.addContent(byteBuf, false);
            this.undecodedChunk.readerIndex(n4);
            throw new NotEnoughDataDecoderException();
        }
    }

    public boolean isMultipart() {
        this.checkDestroyed();
        return this.isMultipart;
    }

    public int getDiscardThreshold() {
        return this.discardThreshold;
    }

    private InterfaceHttpData findMultipartDelimiter(String string, MultiPartStatus multiPartStatus, MultiPartStatus multiPartStatus2) throws ErrorDataDecoderException {
        String string2;
        int n = this.undecodedChunk.readerIndex();
        try {
            this.skipControlCharacters();
        }
        catch (NotEnoughDataDecoderException notEnoughDataDecoderException) {
            this.undecodedChunk.readerIndex(n);
            return null;
        }
        this.skipOneLine();
        try {
            string2 = this.readDelimiter(string);
        }
        catch (NotEnoughDataDecoderException notEnoughDataDecoderException) {
            this.undecodedChunk.readerIndex(n);
            return null;
        }
        if (string2.equals(string)) {
            this.currentStatus = multiPartStatus;
            return this.decodeMultipart(multiPartStatus);
        }
        if (string2.equals(string + "--")) {
            this.currentStatus = multiPartStatus2;
            if (this.currentStatus == MultiPartStatus.HEADERDELIMITER) {
                this.currentFieldAttributes = null;
                return this.decodeMultipart(MultiPartStatus.HEADERDELIMITER);
            }
            return null;
        }
        this.undecodedChunk.readerIndex(n);
        throw new ErrorDataDecoderException("No Multipart delimiter found");
    }

    private static String decodeAttribute(String string, Charset charset) throws ErrorDataDecoderException {
        try {
            return QueryStringDecoder.decodeComponent(string, charset);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new ErrorDataDecoderException("Bad string: '" + string + '\'', illegalArgumentException);
        }
    }

    public HttpPostRequestDecoder(HttpDataFactory httpDataFactory, HttpRequest httpRequest) throws IncompatibleDataDecoderException, ErrorDataDecoderException {
        this(httpDataFactory, httpRequest, HttpConstants.DEFAULT_CHARSET);
    }

    private static String[] splitHeaderContentType(String string) {
        int n = HttpPostBodyUtil.findNonWhitespace(string, 0);
        int n2 = string.indexOf(59);
        if (n2 == -1) {
            return new String[]{string, ""};
        }
        if (string.charAt(n2 - 1) == ' ') {
            --n2;
        }
        int n3 = HttpPostBodyUtil.findNonWhitespace(string, n2 + 1);
        int n4 = HttpPostBodyUtil.findEndOfString(string);
        return new String[]{string.substring(n, n2), string.substring(n3, n4)};
    }

    public HttpPostRequestDecoder(HttpDataFactory httpDataFactory, HttpRequest httpRequest, Charset charset) throws IncompatibleDataDecoderException, ErrorDataDecoderException {
        if (httpDataFactory == null) {
            throw new NullPointerException("factory");
        }
        if (httpRequest == null) {
            throw new NullPointerException("request");
        }
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        this.request = httpRequest;
        HttpMethod httpMethod = httpRequest.getMethod();
        if (httpMethod.equals(HttpMethod.POST) || httpMethod.equals(HttpMethod.PUT) || httpMethod.equals(HttpMethod.PATCH)) {
            this.bodyToDecode = true;
        }
        this.charset = charset;
        this.factory = httpDataFactory;
        String string = this.request.headers().get("Content-Type");
        if (string != null) {
            this.checkMultipart(string);
        } else {
            this.isMultipart = false;
        }
        if (!this.bodyToDecode) {
            throw new IncompatibleDataDecoderException("No Body to decode");
        }
        if (httpRequest instanceof HttpContent) {
            this.offer((HttpContent)((Object)httpRequest));
        } else {
            this.undecodedChunk = Unpooled.buffer();
            this.parseBody();
        }
    }

    private String readLineStandard() throws NotEnoughDataDecoderException {
        int n = this.undecodedChunk.readerIndex();
        try {
            ByteBuf byteBuf = Unpooled.buffer(64);
            while (this.undecodedChunk.isReadable()) {
                byte by = this.undecodedChunk.readByte();
                if (by == 13) {
                    by = this.undecodedChunk.getByte(this.undecodedChunk.readerIndex());
                    if (by == 10) {
                        this.undecodedChunk.skipBytes(1);
                        return byteBuf.toString(this.charset);
                    }
                    byteBuf.writeByte(13);
                    continue;
                }
                if (by == 10) {
                    return byteBuf.toString(this.charset);
                }
                byteBuf.writeByte(by);
            }
        }
        catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            this.undecodedChunk.readerIndex(n);
            throw new NotEnoughDataDecoderException(indexOutOfBoundsException);
        }
        this.undecodedChunk.readerIndex(n);
        throw new NotEnoughDataDecoderException();
    }

    void skipControlCharacters() throws NotEnoughDataDecoderException {
        HttpPostBodyUtil.SeekAheadOptimize seekAheadOptimize;
        try {
            seekAheadOptimize = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
        }
        catch (HttpPostBodyUtil.SeekAheadNoBackArrayException seekAheadNoBackArrayException) {
            try {
                this.skipControlCharactersStandard();
            }
            catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                throw new NotEnoughDataDecoderException(indexOutOfBoundsException);
            }
            return;
        }
        while (seekAheadOptimize.pos < seekAheadOptimize.limit) {
            char c;
            if (Character.isISOControl(c = (char)(seekAheadOptimize.bytes[seekAheadOptimize.pos++] & 0xFF)) || Character.isWhitespace(c)) continue;
            seekAheadOptimize.setReadPosition(1);
            return;
        }
        throw new NotEnoughDataDecoderException("Access out of bounds");
    }

    public void destroy() {
        this.checkDestroyed();
        this.cleanFiles();
        this.destroyed = true;
        if (this.undecodedChunk != null && this.undecodedChunk.refCnt() > 0) {
            this.undecodedChunk.release();
            this.undecodedChunk = null;
        }
        for (int i = this.bodyListHttpDataRank; i < this.bodyListHttpData.size(); ++i) {
            this.bodyListHttpData.get(i).release();
        }
    }

    private String readDelimiter(String string) throws NotEnoughDataDecoderException {
        HttpPostBodyUtil.SeekAheadOptimize seekAheadOptimize;
        try {
            seekAheadOptimize = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
        }
        catch (HttpPostBodyUtil.SeekAheadNoBackArrayException seekAheadNoBackArrayException) {
            return this.readDelimiterStandard(string);
        }
        int n = this.undecodedChunk.readerIndex();
        int n2 = string.length();
        try {
            byte by;
            StringBuilder stringBuilder = new StringBuilder(64);
            for (int i = 0; seekAheadOptimize.pos < seekAheadOptimize.limit && i < n2; ++i) {
                if ((by = seekAheadOptimize.bytes[seekAheadOptimize.pos++]) == string.charAt(i)) {
                    stringBuilder.append((char)by);
                    continue;
                }
                this.undecodedChunk.readerIndex(n);
                throw new NotEnoughDataDecoderException();
            }
            if (seekAheadOptimize.pos < seekAheadOptimize.limit) {
                if ((by = seekAheadOptimize.bytes[seekAheadOptimize.pos++]) == 13) {
                    if (seekAheadOptimize.pos < seekAheadOptimize.limit) {
                        if ((by = seekAheadOptimize.bytes[seekAheadOptimize.pos++]) == 10) {
                            seekAheadOptimize.setReadPosition(0);
                            return stringBuilder.toString();
                        }
                        this.undecodedChunk.readerIndex(n);
                        throw new NotEnoughDataDecoderException();
                    }
                    this.undecodedChunk.readerIndex(n);
                    throw new NotEnoughDataDecoderException();
                }
                if (by == 10) {
                    seekAheadOptimize.setReadPosition(0);
                    return stringBuilder.toString();
                }
                if (by == 45) {
                    stringBuilder.append('-');
                    if (seekAheadOptimize.pos < seekAheadOptimize.limit && (by = seekAheadOptimize.bytes[seekAheadOptimize.pos++]) == 45) {
                        stringBuilder.append('-');
                        if (seekAheadOptimize.pos < seekAheadOptimize.limit) {
                            if ((by = seekAheadOptimize.bytes[seekAheadOptimize.pos++]) == 13) {
                                if (seekAheadOptimize.pos < seekAheadOptimize.limit) {
                                    if ((by = seekAheadOptimize.bytes[seekAheadOptimize.pos++]) == 10) {
                                        seekAheadOptimize.setReadPosition(0);
                                        return stringBuilder.toString();
                                    }
                                    this.undecodedChunk.readerIndex(n);
                                    throw new NotEnoughDataDecoderException();
                                }
                                this.undecodedChunk.readerIndex(n);
                                throw new NotEnoughDataDecoderException();
                            }
                            if (by == 10) {
                                seekAheadOptimize.setReadPosition(0);
                                return stringBuilder.toString();
                            }
                            seekAheadOptimize.setReadPosition(1);
                            return stringBuilder.toString();
                        }
                        seekAheadOptimize.setReadPosition(0);
                        return stringBuilder.toString();
                    }
                }
            }
        }
        catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            this.undecodedChunk.readerIndex(n);
            throw new NotEnoughDataDecoderException(indexOutOfBoundsException);
        }
        this.undecodedChunk.readerIndex(n);
        throw new NotEnoughDataDecoderException();
    }

    public void setDiscardThreshold(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("discardThreshold must be >= 0");
        }
        this.discardThreshold = n;
    }

    private static String cleanString(String string) {
        StringBuilder stringBuilder = new StringBuilder(string.length());
        for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            if (c == ':') {
                stringBuilder.append(32);
                continue;
            }
            if (c == ',') {
                stringBuilder.append(32);
                continue;
            }
            if (c == '=') {
                stringBuilder.append(32);
                continue;
            }
            if (c == ';') {
                stringBuilder.append(32);
                continue;
            }
            if (c == '\t') {
                stringBuilder.append(32);
                continue;
            }
            if (c == '\"') continue;
            stringBuilder.append(c);
        }
        return stringBuilder.toString().trim();
    }

    public HttpPostRequestDecoder(HttpRequest httpRequest) throws IncompatibleDataDecoderException, ErrorDataDecoderException {
        this(new DefaultHttpDataFactory(16384L), httpRequest, HttpConstants.DEFAULT_CHARSET);
    }

    private void parseBody() throws ErrorDataDecoderException {
        if (this.currentStatus == MultiPartStatus.PREEPILOGUE || this.currentStatus == MultiPartStatus.EPILOGUE) {
            if (this.isLastChunk) {
                this.currentStatus = MultiPartStatus.EPILOGUE;
            }
            return;
        }
        if (this.isMultipart) {
            this.parseBodyMultipart();
        } else {
            this.parseBodyAttributes();
        }
    }

    public boolean hasNext() throws EndOfDataDecoderException {
        this.checkDestroyed();
        if (this.currentStatus == MultiPartStatus.EPILOGUE && this.bodyListHttpDataRank >= this.bodyListHttpData.size()) {
            throw new EndOfDataDecoderException();
        }
        return !this.bodyListHttpData.isEmpty() && this.bodyListHttpDataRank < this.bodyListHttpData.size();
    }

    private InterfaceHttpData decodeMultipart(MultiPartStatus multiPartStatus) throws ErrorDataDecoderException {
        switch (multiPartStatus) {
            case NOTSTARTED: {
                throw new ErrorDataDecoderException("Should not be called with the current getStatus");
            }
            case PREAMBLE: {
                throw new ErrorDataDecoderException("Should not be called with the current getStatus");
            }
            case HEADERDELIMITER: {
                return this.findMultipartDelimiter(this.multipartDataBoundary, MultiPartStatus.DISPOSITION, MultiPartStatus.PREEPILOGUE);
            }
            case DISPOSITION: {
                return this.findMultipartDisposition();
            }
            case FIELD: {
                Charset charset = null;
                Attribute attribute = this.currentFieldAttributes.get("charset");
                if (attribute != null) {
                    try {
                        charset = Charset.forName(attribute.getValue());
                    }
                    catch (IOException iOException) {
                        throw new ErrorDataDecoderException(iOException);
                    }
                }
                Attribute attribute2 = this.currentFieldAttributes.get("name");
                if (this.currentAttribute == null) {
                    try {
                        this.currentAttribute = this.factory.createAttribute(this.request, HttpPostRequestDecoder.cleanString(attribute2.getValue()));
                    }
                    catch (NullPointerException nullPointerException) {
                        throw new ErrorDataDecoderException(nullPointerException);
                    }
                    catch (IllegalArgumentException illegalArgumentException) {
                        throw new ErrorDataDecoderException(illegalArgumentException);
                    }
                    catch (IOException iOException) {
                        throw new ErrorDataDecoderException(iOException);
                    }
                    if (charset != null) {
                        this.currentAttribute.setCharset(charset);
                    }
                }
                try {
                    this.loadFieldMultipart(this.multipartDataBoundary);
                }
                catch (NotEnoughDataDecoderException notEnoughDataDecoderException) {
                    return null;
                }
                Attribute attribute3 = this.currentAttribute;
                this.currentAttribute = null;
                this.currentFieldAttributes = null;
                this.currentStatus = MultiPartStatus.HEADERDELIMITER;
                return attribute3;
            }
            case FILEUPLOAD: {
                return this.getFileUpload(this.multipartDataBoundary);
            }
            case MIXEDDELIMITER: {
                return this.findMultipartDelimiter(this.multipartMixedBoundary, MultiPartStatus.MIXEDDISPOSITION, MultiPartStatus.HEADERDELIMITER);
            }
            case MIXEDDISPOSITION: {
                return this.findMultipartDisposition();
            }
            case MIXEDFILEUPLOAD: {
                return this.getFileUpload(this.multipartMixedBoundary);
            }
            case PREEPILOGUE: {
                return null;
            }
            case EPILOGUE: {
                return null;
            }
        }
        throw new ErrorDataDecoderException("Shouldn't reach here.");
    }

    protected InterfaceHttpData getFileUpload(String string) throws ErrorDataDecoderException {
        HttpData httpData;
        Object object;
        Attribute attribute = this.currentFieldAttributes.get("Content-Transfer-Encoding");
        Charset charset = this.charset;
        HttpPostBodyUtil.TransferEncodingMechanism transferEncodingMechanism = HttpPostBodyUtil.TransferEncodingMechanism.BIT7;
        if (attribute != null) {
            try {
                object = attribute.getValue().toLowerCase();
            }
            catch (IOException iOException) {
                throw new ErrorDataDecoderException(iOException);
            }
            if (((String)object).equals(HttpPostBodyUtil.TransferEncodingMechanism.BIT7.value())) {
                charset = HttpPostBodyUtil.US_ASCII;
            } else if (((String)object).equals(HttpPostBodyUtil.TransferEncodingMechanism.BIT8.value())) {
                charset = HttpPostBodyUtil.ISO_8859_1;
                transferEncodingMechanism = HttpPostBodyUtil.TransferEncodingMechanism.BIT8;
            } else if (((String)object).equals(HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value())) {
                transferEncodingMechanism = HttpPostBodyUtil.TransferEncodingMechanism.BINARY;
            } else {
                throw new ErrorDataDecoderException("TransferEncoding Unknown: " + (String)object);
            }
        }
        if ((object = this.currentFieldAttributes.get("charset")) != null) {
            try {
                charset = Charset.forName(object.getValue());
            }
            catch (IOException iOException) {
                throw new ErrorDataDecoderException(iOException);
            }
        }
        if (this.currentFileUpload == null) {
            long l;
            httpData = this.currentFieldAttributes.get("filename");
            Attribute attribute2 = this.currentFieldAttributes.get("name");
            Attribute attribute3 = this.currentFieldAttributes.get("Content-Type");
            if (attribute3 == null) {
                throw new ErrorDataDecoderException("Content-Type is absent but required");
            }
            Attribute attribute4 = this.currentFieldAttributes.get("Content-Length");
            try {
                l = attribute4 != null ? Long.parseLong(attribute4.getValue()) : 0L;
            }
            catch (IOException iOException) {
                throw new ErrorDataDecoderException(iOException);
            }
            catch (NumberFormatException numberFormatException) {
                l = 0L;
            }
            try {
                this.currentFileUpload = this.factory.createFileUpload(this.request, HttpPostRequestDecoder.cleanString(attribute2.getValue()), HttpPostRequestDecoder.cleanString(httpData.getValue()), attribute3.getValue(), transferEncodingMechanism.value(), charset, l);
            }
            catch (NullPointerException nullPointerException) {
                throw new ErrorDataDecoderException(nullPointerException);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                throw new ErrorDataDecoderException(illegalArgumentException);
            }
            catch (IOException iOException) {
                throw new ErrorDataDecoderException(iOException);
            }
        }
        try {
            this.readFileUploadByteMultipart(string);
        }
        catch (NotEnoughDataDecoderException notEnoughDataDecoderException) {
            return null;
        }
        if (this.currentFileUpload.isCompleted()) {
            if (this.currentStatus == MultiPartStatus.FILEUPLOAD) {
                this.currentStatus = MultiPartStatus.HEADERDELIMITER;
                this.currentFieldAttributes = null;
            } else {
                this.currentStatus = MultiPartStatus.MIXEDDELIMITER;
                this.cleanMixedAttributes();
            }
            httpData = this.currentFileUpload;
            this.currentFileUpload = null;
            return httpData;
        }
        return null;
    }

    private void setFinalBuffer(ByteBuf byteBuf) throws IOException, ErrorDataDecoderException {
        this.currentAttribute.addContent(byteBuf, true);
        String string = HttpPostRequestDecoder.decodeAttribute(this.currentAttribute.getByteBuf().toString(this.charset), this.charset);
        this.currentAttribute.setValue(string);
        this.addHttpData(this.currentAttribute);
        this.currentAttribute = null;
    }

    public List<InterfaceHttpData> getBodyHttpDatas() throws NotEnoughDataDecoderException {
        this.checkDestroyed();
        if (!this.isLastChunk) {
            throw new NotEnoughDataDecoderException();
        }
        return this.bodyListHttpData;
    }

    private String readLine() throws NotEnoughDataDecoderException {
        HttpPostBodyUtil.SeekAheadOptimize seekAheadOptimize;
        try {
            seekAheadOptimize = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
        }
        catch (HttpPostBodyUtil.SeekAheadNoBackArrayException seekAheadNoBackArrayException) {
            return this.readLineStandard();
        }
        int n = this.undecodedChunk.readerIndex();
        try {
            ByteBuf byteBuf = Unpooled.buffer(64);
            while (seekAheadOptimize.pos < seekAheadOptimize.limit) {
                byte by;
                if ((by = seekAheadOptimize.bytes[seekAheadOptimize.pos++]) == 13) {
                    if (seekAheadOptimize.pos < seekAheadOptimize.limit) {
                        if ((by = seekAheadOptimize.bytes[seekAheadOptimize.pos++]) == 10) {
                            seekAheadOptimize.setReadPosition(0);
                            return byteBuf.toString(this.charset);
                        }
                        --seekAheadOptimize.pos;
                        byteBuf.writeByte(13);
                        continue;
                    }
                    byteBuf.writeByte(by);
                    continue;
                }
                if (by == 10) {
                    seekAheadOptimize.setReadPosition(0);
                    return byteBuf.toString(this.charset);
                }
                byteBuf.writeByte(by);
            }
        }
        catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            this.undecodedChunk.readerIndex(n);
            throw new NotEnoughDataDecoderException(indexOutOfBoundsException);
        }
        this.undecodedChunk.readerIndex(n);
        throw new NotEnoughDataDecoderException();
    }

    public HttpPostRequestDecoder offer(HttpContent httpContent) throws ErrorDataDecoderException {
        this.checkDestroyed();
        ByteBuf byteBuf = httpContent.content();
        if (this.undecodedChunk == null) {
            this.undecodedChunk = byteBuf.copy();
        } else {
            this.undecodedChunk.writeBytes(byteBuf);
        }
        if (httpContent instanceof LastHttpContent) {
            this.isLastChunk = true;
        }
        this.parseBody();
        if (this.undecodedChunk != null && this.undecodedChunk.writerIndex() > this.discardThreshold) {
            this.undecodedChunk.discardReadBytes();
        }
        return this;
    }

    private void loadFieldMultipart(String string) throws NotEnoughDataDecoderException, ErrorDataDecoderException {
        HttpPostBodyUtil.SeekAheadOptimize seekAheadOptimize;
        try {
            seekAheadOptimize = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
        }
        catch (HttpPostBodyUtil.SeekAheadNoBackArrayException seekAheadNoBackArrayException) {
            this.loadFieldMultipartStandard(string);
            return;
        }
        int n = this.undecodedChunk.readerIndex();
        try {
            boolean bl = true;
            int n2 = 0;
            int n3 = seekAheadOptimize.pos;
            boolean bl2 = false;
            while (seekAheadOptimize.pos < seekAheadOptimize.limit) {
                byte by = seekAheadOptimize.bytes[seekAheadOptimize.pos++];
                if (bl) {
                    if (by == string.codePointAt(n2)) {
                        if (string.length() != ++n2) continue;
                        bl2 = true;
                        break;
                    }
                    bl = false;
                    n2 = 0;
                    if (by == 13) {
                        if (seekAheadOptimize.pos >= seekAheadOptimize.limit) continue;
                        if ((by = seekAheadOptimize.bytes[seekAheadOptimize.pos++]) == 10) {
                            bl = true;
                            n2 = 0;
                            n3 = seekAheadOptimize.pos - 2;
                            continue;
                        }
                        n3 = --seekAheadOptimize.pos;
                        continue;
                    }
                    if (by == 10) {
                        bl = true;
                        n2 = 0;
                        n3 = seekAheadOptimize.pos - 1;
                        continue;
                    }
                    n3 = seekAheadOptimize.pos;
                    continue;
                }
                if (by == 13) {
                    if (seekAheadOptimize.pos >= seekAheadOptimize.limit) continue;
                    if ((by = seekAheadOptimize.bytes[seekAheadOptimize.pos++]) == 10) {
                        bl = true;
                        n2 = 0;
                        n3 = seekAheadOptimize.pos - 2;
                        continue;
                    }
                    n3 = --seekAheadOptimize.pos;
                    continue;
                }
                if (by == 10) {
                    bl = true;
                    n2 = 0;
                    n3 = seekAheadOptimize.pos - 1;
                    continue;
                }
                n3 = seekAheadOptimize.pos;
            }
            int n4 = seekAheadOptimize.getReadPosition(n3);
            if (bl2) {
                try {
                    this.currentAttribute.addContent(this.undecodedChunk.copy(n, n4 - n), true);
                }
                catch (IOException iOException) {
                    throw new ErrorDataDecoderException(iOException);
                }
            }
            try {
                this.currentAttribute.addContent(this.undecodedChunk.copy(n, n4 - n), false);
            }
            catch (IOException iOException) {
                throw new ErrorDataDecoderException(iOException);
            }
            this.undecodedChunk.readerIndex(n4);
            throw new NotEnoughDataDecoderException();
            this.undecodedChunk.readerIndex(n4);
        }
        catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            this.undecodedChunk.readerIndex(n);
            throw new NotEnoughDataDecoderException(indexOutOfBoundsException);
        }
    }

    private void parseBodyMultipart() throws ErrorDataDecoderException {
        if (this.undecodedChunk == null || this.undecodedChunk.readableBytes() == 0) {
            return;
        }
        InterfaceHttpData interfaceHttpData = this.decodeMultipart(this.currentStatus);
        while (interfaceHttpData != null) {
            this.addHttpData(interfaceHttpData);
            if (this.currentStatus == MultiPartStatus.PREEPILOGUE || this.currentStatus == MultiPartStatus.EPILOGUE) break;
            interfaceHttpData = this.decodeMultipart(this.currentStatus);
        }
    }

    private static String[] splitMultipartHeader(String string) {
        int n;
        int n2;
        char c;
        int n3;
        ArrayList<String> arrayList = new ArrayList<String>(1);
        for (n3 = n2 = HttpPostBodyUtil.findNonWhitespace(string, 0); n3 < string.length() && (c = string.charAt(n3)) != ':' && !Character.isWhitespace(c); ++n3) {
        }
        for (n = n3; n < string.length(); ++n) {
            if (string.charAt(n) != ':') continue;
            ++n;
            break;
        }
        int n4 = HttpPostBodyUtil.findNonWhitespace(string, n);
        int n5 = HttpPostBodyUtil.findEndOfString(string);
        arrayList.add(string.substring(n2, n3));
        String string2 = string.substring(n4, n5);
        String[] stringArray = string2.indexOf(59) >= 0 ? StringUtil.split(string2, ';') : StringUtil.split(string2, ',');
        for (String string3 : stringArray) {
            arrayList.add(string3.trim());
        }
        String[] stringArray2 = new String[arrayList.size()];
        for (int i = 0; i < arrayList.size(); ++i) {
            stringArray2[i] = (String)arrayList.get(i);
        }
        return stringArray2;
    }

    private void checkDestroyed() {
        if (this.destroyed) {
            throw new IllegalStateException(HttpPostRequestDecoder.class.getSimpleName() + " was destroyed already");
        }
    }

    public InterfaceHttpData getBodyHttpData(String string) throws NotEnoughDataDecoderException {
        this.checkDestroyed();
        if (!this.isLastChunk) {
            throw new NotEnoughDataDecoderException();
        }
        List<InterfaceHttpData> list = this.bodyMapHttpData.get(string);
        if (list != null) {
            return list.get(0);
        }
        return null;
    }

    public List<InterfaceHttpData> getBodyHttpDatas(String string) throws NotEnoughDataDecoderException {
        this.checkDestroyed();
        if (!this.isLastChunk) {
            throw new NotEnoughDataDecoderException();
        }
        return this.bodyMapHttpData.get(string);
    }

    protected void addHttpData(InterfaceHttpData interfaceHttpData) {
        if (interfaceHttpData == null) {
            return;
        }
        List<InterfaceHttpData> list = this.bodyMapHttpData.get(interfaceHttpData.getName());
        if (list == null) {
            list = new ArrayList<InterfaceHttpData>(1);
            this.bodyMapHttpData.put(interfaceHttpData.getName(), list);
        }
        list.add(interfaceHttpData);
        this.bodyListHttpData.add(interfaceHttpData);
    }

    private boolean skipOneLine() {
        if (!this.undecodedChunk.isReadable()) {
            return false;
        }
        byte by = this.undecodedChunk.readByte();
        if (by == 13) {
            if (!this.undecodedChunk.isReadable()) {
                this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
                return false;
            }
            by = this.undecodedChunk.readByte();
            if (by == 10) {
                return true;
            }
            this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 2);
            return false;
        }
        if (by == 10) {
            return true;
        }
        this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void parseBodyAttributes() throws ErrorDataDecoderException {
        int n;
        HttpPostBodyUtil.SeekAheadOptimize seekAheadOptimize;
        try {
            seekAheadOptimize = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
        }
        catch (HttpPostBodyUtil.SeekAheadNoBackArrayException seekAheadNoBackArrayException) {
            this.parseBodyAttributesStandard();
            return;
        }
        int n2 = n = this.undecodedChunk.readerIndex();
        if (this.currentStatus == MultiPartStatus.NOTSTARTED) {
            this.currentStatus = MultiPartStatus.DISPOSITION;
        }
        boolean bl = true;
        try {
            int n3;
            block9: while (seekAheadOptimize.pos < seekAheadOptimize.limit) {
                char c = (char)(seekAheadOptimize.bytes[seekAheadOptimize.pos++] & 0xFF);
                ++n2;
                switch (this.currentStatus) {
                    case DISPOSITION: {
                        String string;
                        if (c == '=') {
                            this.currentStatus = MultiPartStatus.FIELD;
                            int n4 = n2 - 1;
                            string = HttpPostRequestDecoder.decodeAttribute(this.undecodedChunk.toString(n, n4 - n, this.charset), this.charset);
                            this.currentAttribute = this.factory.createAttribute(this.request, string);
                            n = n2;
                            continue block9;
                        }
                        if (c != '&') continue block9;
                        this.currentStatus = MultiPartStatus.DISPOSITION;
                        n3 = n2 - 1;
                        string = HttpPostRequestDecoder.decodeAttribute(this.undecodedChunk.toString(n, n3 - n, this.charset), this.charset);
                        this.currentAttribute = this.factory.createAttribute(this.request, string);
                        this.currentAttribute.setValue("");
                        this.addHttpData(this.currentAttribute);
                        this.currentAttribute = null;
                        n = n2;
                        bl = true;
                        continue block9;
                    }
                    case FIELD: {
                        if (c == '&') {
                            this.currentStatus = MultiPartStatus.DISPOSITION;
                            n3 = n2 - 1;
                            this.setFinalBuffer(this.undecodedChunk.copy(n, n3 - n));
                            n = n2;
                            bl = true;
                            continue block9;
                        }
                        if (c == '\r') {
                            if (seekAheadOptimize.pos < seekAheadOptimize.limit) {
                                c = (char)(seekAheadOptimize.bytes[seekAheadOptimize.pos++] & 0xFF);
                                ++n2;
                                if (c != '\n') {
                                    seekAheadOptimize.setReadPosition(0);
                                    throw new ErrorDataDecoderException("Bad end of line");
                                }
                                this.currentStatus = MultiPartStatus.PREEPILOGUE;
                                n3 = n2 - 2;
                                seekAheadOptimize.setReadPosition(0);
                                this.setFinalBuffer(this.undecodedChunk.copy(n, n3 - n));
                                n = n2;
                                bl = false;
                                break block9;
                            }
                            if (seekAheadOptimize.limit <= 0) continue block9;
                            --n2;
                            continue block9;
                        }
                        if (c != '\n') continue block9;
                        this.currentStatus = MultiPartStatus.PREEPILOGUE;
                        n3 = n2 - 1;
                        seekAheadOptimize.setReadPosition(0);
                        this.setFinalBuffer(this.undecodedChunk.copy(n, n3 - n));
                        n = n2;
                        bl = false;
                        break block9;
                    }
                }
                seekAheadOptimize.setReadPosition(0);
                bl = false;
                break;
            }
            if (this.isLastChunk && this.currentAttribute != null) {
                n3 = n2;
                if (n3 > n) {
                    this.setFinalBuffer(this.undecodedChunk.copy(n, n3 - n));
                } else if (!this.currentAttribute.isCompleted()) {
                    this.setFinalBuffer(Unpooled.EMPTY_BUFFER);
                }
                n = n2;
                this.currentStatus = MultiPartStatus.EPILOGUE;
                this.undecodedChunk.readerIndex(n);
                return;
            }
            if (bl && this.currentAttribute != null) {
                if (this.currentStatus == MultiPartStatus.FIELD) {
                    this.currentAttribute.addContent(this.undecodedChunk.copy(n, n2 - n), false);
                    n = n2;
                }
                this.undecodedChunk.readerIndex(n);
                return;
            }
            this.undecodedChunk.readerIndex(n);
            return;
        }
        catch (ErrorDataDecoderException errorDataDecoderException) {
            this.undecodedChunk.readerIndex(n);
            throw errorDataDecoderException;
        }
        catch (IOException iOException) {
            this.undecodedChunk.readerIndex(n);
            throw new ErrorDataDecoderException(iOException);
        }
    }

    private void loadFieldMultipartStandard(String string) throws ErrorDataDecoderException, NotEnoughDataDecoderException {
        int n = this.undecodedChunk.readerIndex();
        try {
            boolean bl = true;
            int n2 = 0;
            int n3 = this.undecodedChunk.readerIndex();
            boolean bl2 = false;
            while (this.undecodedChunk.isReadable()) {
                byte by = this.undecodedChunk.readByte();
                if (bl) {
                    if (by == string.codePointAt(n2)) {
                        if (string.length() != ++n2) continue;
                        bl2 = true;
                        break;
                    }
                    bl = false;
                    n2 = 0;
                    if (by == 13) {
                        if (this.undecodedChunk.isReadable()) {
                            by = this.undecodedChunk.readByte();
                            if (by == 10) {
                                bl = true;
                                n2 = 0;
                                n3 = this.undecodedChunk.readerIndex() - 2;
                                continue;
                            }
                            n3 = this.undecodedChunk.readerIndex() - 1;
                            this.undecodedChunk.readerIndex(n3);
                            continue;
                        }
                        n3 = this.undecodedChunk.readerIndex() - 1;
                        continue;
                    }
                    if (by == 10) {
                        bl = true;
                        n2 = 0;
                        n3 = this.undecodedChunk.readerIndex() - 1;
                        continue;
                    }
                    n3 = this.undecodedChunk.readerIndex();
                    continue;
                }
                if (by == 13) {
                    if (this.undecodedChunk.isReadable()) {
                        by = this.undecodedChunk.readByte();
                        if (by == 10) {
                            bl = true;
                            n2 = 0;
                            n3 = this.undecodedChunk.readerIndex() - 2;
                            continue;
                        }
                        n3 = this.undecodedChunk.readerIndex() - 1;
                        this.undecodedChunk.readerIndex(n3);
                        continue;
                    }
                    n3 = this.undecodedChunk.readerIndex() - 1;
                    continue;
                }
                if (by == 10) {
                    bl = true;
                    n2 = 0;
                    n3 = this.undecodedChunk.readerIndex() - 1;
                    continue;
                }
                n3 = this.undecodedChunk.readerIndex();
            }
            if (bl2) {
                try {
                    this.currentAttribute.addContent(this.undecodedChunk.copy(n, n3 - n), true);
                }
                catch (IOException iOException) {
                    throw new ErrorDataDecoderException(iOException);
                }
            }
            try {
                this.currentAttribute.addContent(this.undecodedChunk.copy(n, n3 - n), false);
            }
            catch (IOException iOException) {
                throw new ErrorDataDecoderException(iOException);
            }
            this.undecodedChunk.readerIndex(n3);
            throw new NotEnoughDataDecoderException();
            this.undecodedChunk.readerIndex(n3);
        }
        catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            this.undecodedChunk.readerIndex(n);
            throw new NotEnoughDataDecoderException(indexOutOfBoundsException);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void parseBodyAttributesStandard() throws ErrorDataDecoderException {
        int n;
        int n2 = n = this.undecodedChunk.readerIndex();
        if (this.currentStatus == MultiPartStatus.NOTSTARTED) {
            this.currentStatus = MultiPartStatus.DISPOSITION;
        }
        boolean bl = true;
        try {
            int n3;
            block7: while (this.undecodedChunk.isReadable() && bl) {
                char c = (char)this.undecodedChunk.readUnsignedByte();
                ++n2;
                switch (this.currentStatus) {
                    case DISPOSITION: {
                        String string;
                        if (c == '=') {
                            this.currentStatus = MultiPartStatus.FIELD;
                            int n4 = n2 - 1;
                            string = HttpPostRequestDecoder.decodeAttribute(this.undecodedChunk.toString(n, n4 - n, this.charset), this.charset);
                            this.currentAttribute = this.factory.createAttribute(this.request, string);
                            n = n2;
                            continue block7;
                        }
                        if (c != '&') continue block7;
                        this.currentStatus = MultiPartStatus.DISPOSITION;
                        n3 = n2 - 1;
                        string = HttpPostRequestDecoder.decodeAttribute(this.undecodedChunk.toString(n, n3 - n, this.charset), this.charset);
                        this.currentAttribute = this.factory.createAttribute(this.request, string);
                        this.currentAttribute.setValue("");
                        this.addHttpData(this.currentAttribute);
                        this.currentAttribute = null;
                        n = n2;
                        bl = true;
                        continue block7;
                    }
                    case FIELD: {
                        if (c == '&') {
                            this.currentStatus = MultiPartStatus.DISPOSITION;
                            n3 = n2 - 1;
                            this.setFinalBuffer(this.undecodedChunk.copy(n, n3 - n));
                            n = n2;
                            bl = true;
                            continue block7;
                        }
                        if (c == '\r') {
                            if (this.undecodedChunk.isReadable()) {
                                c = (char)this.undecodedChunk.readUnsignedByte();
                                if (c != '\n') throw new ErrorDataDecoderException("Bad end of line");
                                this.currentStatus = MultiPartStatus.PREEPILOGUE;
                                n3 = ++n2 - 2;
                                this.setFinalBuffer(this.undecodedChunk.copy(n, n3 - n));
                                n = n2;
                                bl = false;
                                continue block7;
                            }
                            --n2;
                            continue block7;
                        }
                        if (c != '\n') continue block7;
                        this.currentStatus = MultiPartStatus.PREEPILOGUE;
                        n3 = n2 - 1;
                        this.setFinalBuffer(this.undecodedChunk.copy(n, n3 - n));
                        n = n2;
                        bl = false;
                        continue block7;
                    }
                }
                bl = false;
            }
            if (this.isLastChunk && this.currentAttribute != null) {
                n3 = n2;
                if (n3 > n) {
                    this.setFinalBuffer(this.undecodedChunk.copy(n, n3 - n));
                } else if (!this.currentAttribute.isCompleted()) {
                    this.setFinalBuffer(Unpooled.EMPTY_BUFFER);
                }
                n = n2;
                this.currentStatus = MultiPartStatus.EPILOGUE;
                this.undecodedChunk.readerIndex(n);
                return;
            }
            if (bl && this.currentAttribute != null) {
                if (this.currentStatus == MultiPartStatus.FIELD) {
                    this.currentAttribute.addContent(this.undecodedChunk.copy(n, n2 - n), false);
                    n = n2;
                }
                this.undecodedChunk.readerIndex(n);
                return;
            }
            this.undecodedChunk.readerIndex(n);
            return;
        }
        catch (ErrorDataDecoderException errorDataDecoderException) {
            this.undecodedChunk.readerIndex(n);
            throw errorDataDecoderException;
        }
        catch (IOException iOException) {
            this.undecodedChunk.readerIndex(n);
            throw new ErrorDataDecoderException(iOException);
        }
    }

    private String readDelimiterStandard(String string) throws NotEnoughDataDecoderException {
        int n = this.undecodedChunk.readerIndex();
        try {
            byte by;
            StringBuilder stringBuilder = new StringBuilder(64);
            int n2 = string.length();
            for (int i = 0; this.undecodedChunk.isReadable() && i < n2; ++i) {
                by = this.undecodedChunk.readByte();
                if (by == string.charAt(i)) {
                    stringBuilder.append((char)by);
                    continue;
                }
                this.undecodedChunk.readerIndex(n);
                throw new NotEnoughDataDecoderException();
            }
            if (this.undecodedChunk.isReadable()) {
                by = this.undecodedChunk.readByte();
                if (by == 13) {
                    by = this.undecodedChunk.readByte();
                    if (by == 10) {
                        return stringBuilder.toString();
                    }
                    this.undecodedChunk.readerIndex(n);
                    throw new NotEnoughDataDecoderException();
                }
                if (by == 10) {
                    return stringBuilder.toString();
                }
                if (by == 45) {
                    stringBuilder.append('-');
                    by = this.undecodedChunk.readByte();
                    if (by == 45) {
                        stringBuilder.append('-');
                        if (this.undecodedChunk.isReadable()) {
                            by = this.undecodedChunk.readByte();
                            if (by == 13) {
                                by = this.undecodedChunk.readByte();
                                if (by == 10) {
                                    return stringBuilder.toString();
                                }
                                this.undecodedChunk.readerIndex(n);
                                throw new NotEnoughDataDecoderException();
                            }
                            if (by == 10) {
                                return stringBuilder.toString();
                            }
                            this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
                            return stringBuilder.toString();
                        }
                        return stringBuilder.toString();
                    }
                }
            }
        }
        catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            this.undecodedChunk.readerIndex(n);
            throw new NotEnoughDataDecoderException(indexOutOfBoundsException);
        }
        this.undecodedChunk.readerIndex(n);
        throw new NotEnoughDataDecoderException();
    }

    void skipControlCharactersStandard() {
        char c;
        while (Character.isISOControl(c = (char)this.undecodedChunk.readUnsignedByte()) || Character.isWhitespace(c)) {
        }
        this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
    }

    public void removeHttpDataFromClean(InterfaceHttpData interfaceHttpData) {
        this.checkDestroyed();
        this.factory.removeHttpDataFromClean(this.request, interfaceHttpData);
    }

    private void checkMultipart(String string) throws ErrorDataDecoderException {
        String[] stringArray = HttpPostRequestDecoder.splitHeaderContentType(string);
        if (stringArray[0].toLowerCase().startsWith("multipart/form-data") && stringArray[1].toLowerCase().startsWith("boundary")) {
            int n;
            String string2;
            String[] stringArray2 = StringUtil.split(stringArray[1], '=');
            if (stringArray2.length != 2) {
                throw new ErrorDataDecoderException("Needs a boundary value");
            }
            if (stringArray2[1].charAt(0) == '\"' && (string2 = stringArray2[1].trim()).charAt(n = string2.length() - 1) == '\"') {
                stringArray2[1] = string2.substring(1, n);
            }
            this.multipartDataBoundary = "--" + stringArray2[1];
            this.isMultipart = true;
            this.currentStatus = MultiPartStatus.HEADERDELIMITER;
        } else {
            this.isMultipart = false;
        }
    }

    public static class IncompatibleDataDecoderException
    extends DecoderException {
        private static final long serialVersionUID = -953268047926250267L;

        public IncompatibleDataDecoderException(String string, Throwable throwable) {
            super(string, throwable);
        }

        public IncompatibleDataDecoderException(String string) {
            super(string);
        }

        public IncompatibleDataDecoderException(Throwable throwable) {
            super(throwable);
        }

        public IncompatibleDataDecoderException() {
        }
    }

    public static class ErrorDataDecoderException
    extends DecoderException {
        private static final long serialVersionUID = 5020247425493164465L;

        public ErrorDataDecoderException(Throwable throwable) {
            super(throwable);
        }

        public ErrorDataDecoderException(String string, Throwable throwable) {
            super(string, throwable);
        }

        public ErrorDataDecoderException(String string) {
            super(string);
        }

        public ErrorDataDecoderException() {
        }
    }

    public static class NotEnoughDataDecoderException
    extends DecoderException {
        private static final long serialVersionUID = -7846841864603865638L;

        public NotEnoughDataDecoderException() {
        }

        public NotEnoughDataDecoderException(String string) {
            super(string);
        }

        public NotEnoughDataDecoderException(Throwable throwable) {
            super(throwable);
        }

        public NotEnoughDataDecoderException(String string, Throwable throwable) {
            super(string, throwable);
        }
    }

    public static class EndOfDataDecoderException
    extends DecoderException {
        private static final long serialVersionUID = 1336267941020800769L;
    }

    private static enum MultiPartStatus {
        NOTSTARTED,
        PREAMBLE,
        HEADERDELIMITER,
        DISPOSITION,
        FIELD,
        FILEUPLOAD,
        MIXEDPREAMBLE,
        MIXEDDELIMITER,
        MIXEDDISPOSITION,
        MIXEDFILEUPLOAD,
        MIXEDCLOSEDELIMITER,
        CLOSEDELIMITER,
        PREEPILOGUE,
        EPILOGUE;

    }
}

