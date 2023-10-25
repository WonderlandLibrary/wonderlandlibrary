package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HttpPostRequestDecoder {
  private static final int DEFAULT_DISCARD_THRESHOLD = 10485760;
  
  private final HttpDataFactory factory;
  
  private final HttpRequest request;
  
  private final Charset charset;
  
  private boolean bodyToDecode;
  
  private boolean isLastChunk;
  
  private final List<InterfaceHttpData> bodyListHttpData = new ArrayList<InterfaceHttpData>();
  
  private final Map<String, List<InterfaceHttpData>> bodyMapHttpData = new TreeMap<String, List<InterfaceHttpData>>(CaseIgnoringComparator.INSTANCE);
  
  private ByteBuf undecodedChunk;
  
  private boolean isMultipart;
  
  private int bodyListHttpDataRank;
  
  private String multipartDataBoundary;
  
  private String multipartMixedBoundary;
  
  private MultiPartStatus currentStatus = MultiPartStatus.NOTSTARTED;
  
  private Map<String, Attribute> currentFieldAttributes;
  
  private FileUpload currentFileUpload;
  
  private Attribute currentAttribute;
  
  private boolean destroyed;
  
  private int discardThreshold = 10485760;
  
  public HttpPostRequestDecoder(HttpRequest request) throws ErrorDataDecoderException, IncompatibleDataDecoderException {
    this(new DefaultHttpDataFactory(16384L), request, HttpConstants.DEFAULT_CHARSET);
  }
  
  public HttpPostRequestDecoder(HttpDataFactory factory, HttpRequest request) throws ErrorDataDecoderException, IncompatibleDataDecoderException {
    this(factory, request, HttpConstants.DEFAULT_CHARSET);
  }
  
  public HttpPostRequestDecoder(HttpDataFactory factory, HttpRequest request, Charset charset) throws ErrorDataDecoderException, IncompatibleDataDecoderException {
    if (factory == null)
      throw new NullPointerException("factory"); 
    if (request == null)
      throw new NullPointerException("request"); 
    if (charset == null)
      throw new NullPointerException("charset"); 
    this.request = request;
    HttpMethod method = request.getMethod();
    if (method.equals(HttpMethod.POST) || method.equals(HttpMethod.PUT) || method.equals(HttpMethod.PATCH))
      this.bodyToDecode = true; 
    this.charset = charset;
    this.factory = factory;
    String contentType = this.request.headers().get("Content-Type");
    if (contentType != null) {
      checkMultipart(contentType);
    } else {
      this.isMultipart = false;
    } 
    if (!this.bodyToDecode)
      throw new IncompatibleDataDecoderException("No Body to decode"); 
    if (request instanceof HttpContent) {
      offer((HttpContent)request);
    } else {
      this.undecodedChunk = Unpooled.buffer();
      parseBody();
    } 
  }
  
  private enum MultiPartStatus {
    NOTSTARTED, PREAMBLE, HEADERDELIMITER, DISPOSITION, FIELD, FILEUPLOAD, MIXEDPREAMBLE, MIXEDDELIMITER, MIXEDDISPOSITION, MIXEDFILEUPLOAD, MIXEDCLOSEDELIMITER, CLOSEDELIMITER, PREEPILOGUE, EPILOGUE;
  }
  
  private void checkMultipart(String contentType) throws ErrorDataDecoderException {
    String[] headerContentType = splitHeaderContentType(contentType);
    if (headerContentType[0].toLowerCase().startsWith("multipart/form-data") && headerContentType[1].toLowerCase().startsWith("boundary")) {
      String[] boundary = StringUtil.split(headerContentType[1], '=');
      if (boundary.length != 2)
        throw new ErrorDataDecoderException("Needs a boundary value"); 
      if (boundary[1].charAt(0) == '"') {
        String bound = boundary[1].trim();
        int index = bound.length() - 1;
        if (bound.charAt(index) == '"')
          boundary[1] = bound.substring(1, index); 
      } 
      this.multipartDataBoundary = "--" + boundary[1];
      this.isMultipart = true;
      this.currentStatus = MultiPartStatus.HEADERDELIMITER;
    } else {
      this.isMultipart = false;
    } 
  }
  
  private void checkDestroyed() {
    if (this.destroyed)
      throw new IllegalStateException(HttpPostRequestDecoder.class.getSimpleName() + " was destroyed already"); 
  }
  
  public boolean isMultipart() {
    checkDestroyed();
    return this.isMultipart;
  }
  
  public void setDiscardThreshold(int discardThreshold) {
    if (discardThreshold < 0)
      throw new IllegalArgumentException("discardThreshold must be >= 0"); 
    this.discardThreshold = discardThreshold;
  }
  
  public int getDiscardThreshold() {
    return this.discardThreshold;
  }
  
  public List<InterfaceHttpData> getBodyHttpDatas() throws NotEnoughDataDecoderException {
    checkDestroyed();
    if (!this.isLastChunk)
      throw new NotEnoughDataDecoderException(); 
    return this.bodyListHttpData;
  }
  
  public List<InterfaceHttpData> getBodyHttpDatas(String name) throws NotEnoughDataDecoderException {
    checkDestroyed();
    if (!this.isLastChunk)
      throw new NotEnoughDataDecoderException(); 
    return this.bodyMapHttpData.get(name);
  }
  
  public InterfaceHttpData getBodyHttpData(String name) throws NotEnoughDataDecoderException {
    checkDestroyed();
    if (!this.isLastChunk)
      throw new NotEnoughDataDecoderException(); 
    List<InterfaceHttpData> list = this.bodyMapHttpData.get(name);
    if (list != null)
      return list.get(0); 
    return null;
  }
  
  public HttpPostRequestDecoder offer(HttpContent content) throws ErrorDataDecoderException {
    checkDestroyed();
    ByteBuf buf = content.content();
    if (this.undecodedChunk == null) {
      this.undecodedChunk = buf.copy();
    } else {
      this.undecodedChunk.writeBytes(buf);
    } 
    if (content instanceof io.netty.handler.codec.http.LastHttpContent)
      this.isLastChunk = true; 
    parseBody();
    if (this.undecodedChunk != null && this.undecodedChunk.writerIndex() > this.discardThreshold)
      this.undecodedChunk.discardReadBytes(); 
    return this;
  }
  
  public boolean hasNext() throws EndOfDataDecoderException {
    checkDestroyed();
    if (this.currentStatus == MultiPartStatus.EPILOGUE)
      if (this.bodyListHttpDataRank >= this.bodyListHttpData.size())
        throw new EndOfDataDecoderException();  
    return (!this.bodyListHttpData.isEmpty() && this.bodyListHttpDataRank < this.bodyListHttpData.size());
  }
  
  public InterfaceHttpData next() throws EndOfDataDecoderException {
    checkDestroyed();
    if (hasNext())
      return this.bodyListHttpData.get(this.bodyListHttpDataRank++); 
    return null;
  }
  
  private void parseBody() throws ErrorDataDecoderException {
    if (this.currentStatus == MultiPartStatus.PREEPILOGUE || this.currentStatus == MultiPartStatus.EPILOGUE) {
      if (this.isLastChunk)
        this.currentStatus = MultiPartStatus.EPILOGUE; 
      return;
    } 
    if (this.isMultipart) {
      parseBodyMultipart();
    } else {
      parseBodyAttributes();
    } 
  }
  
  protected void addHttpData(InterfaceHttpData data) {
    if (data == null)
      return; 
    List<InterfaceHttpData> datas = this.bodyMapHttpData.get(data.getName());
    if (datas == null) {
      datas = new ArrayList<InterfaceHttpData>(1);
      this.bodyMapHttpData.put(data.getName(), datas);
    } 
    datas.add(data);
    this.bodyListHttpData.add(data);
  }
  
  private void parseBodyAttributesStandard() throws ErrorDataDecoderException {
    int firstpos = this.undecodedChunk.readerIndex();
    int currentpos = firstpos;
    if (this.currentStatus == MultiPartStatus.NOTSTARTED)
      this.currentStatus = MultiPartStatus.DISPOSITION; 
    boolean contRead = true;
    try {
      while (this.undecodedChunk.isReadable() && contRead) {
        char read = (char)this.undecodedChunk.readUnsignedByte();
        currentpos++;
        switch (this.currentStatus) {
          case DISPOSITION:
            if (read == '=') {
              this.currentStatus = MultiPartStatus.FIELD;
              int equalpos = currentpos - 1;
              String key = decodeAttribute(this.undecodedChunk.toString(firstpos, equalpos - firstpos, this.charset), this.charset);
              this.currentAttribute = this.factory.createAttribute(this.request, key);
              firstpos = currentpos;
              continue;
            } 
            if (read == '&') {
              this.currentStatus = MultiPartStatus.DISPOSITION;
              int ampersandpos = currentpos - 1;
              String key = decodeAttribute(this.undecodedChunk.toString(firstpos, ampersandpos - firstpos, this.charset), this.charset);
              this.currentAttribute = this.factory.createAttribute(this.request, key);
              this.currentAttribute.setValue("");
              addHttpData(this.currentAttribute);
              this.currentAttribute = null;
              firstpos = currentpos;
              contRead = true;
            } 
            continue;
          case FIELD:
            if (read == '&') {
              this.currentStatus = MultiPartStatus.DISPOSITION;
              int i = currentpos - 1;
              setFinalBuffer(this.undecodedChunk.copy(firstpos, i - firstpos));
              firstpos = currentpos;
              contRead = true;
              continue;
            } 
            if (read == '\r') {
              if (this.undecodedChunk.isReadable()) {
                read = (char)this.undecodedChunk.readUnsignedByte();
                currentpos++;
                if (read == '\n') {
                  this.currentStatus = MultiPartStatus.PREEPILOGUE;
                  int ampersandpos = currentpos - 2;
                  setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
                  firstpos = currentpos;
                  contRead = false;
                  continue;
                } 
                throw new ErrorDataDecoderException("Bad end of line");
              } 
              currentpos--;
              continue;
            } 
            if (read == '\n') {
              this.currentStatus = MultiPartStatus.PREEPILOGUE;
              int ampersandpos = currentpos - 1;
              setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
              firstpos = currentpos;
              contRead = false;
            } 
            continue;
        } 
        contRead = false;
      } 
      if (this.isLastChunk && this.currentAttribute != null) {
        int i = currentpos;
        if (i > firstpos) {
          setFinalBuffer(this.undecodedChunk.copy(firstpos, i - firstpos));
        } else if (!this.currentAttribute.isCompleted()) {
          setFinalBuffer(Unpooled.EMPTY_BUFFER);
        } 
        firstpos = currentpos;
        this.currentStatus = MultiPartStatus.EPILOGUE;
        this.undecodedChunk.readerIndex(firstpos);
        return;
      } 
      if (contRead && this.currentAttribute != null) {
        if (this.currentStatus == MultiPartStatus.FIELD) {
          this.currentAttribute.addContent(this.undecodedChunk.copy(firstpos, currentpos - firstpos), false);
          firstpos = currentpos;
        } 
        this.undecodedChunk.readerIndex(firstpos);
      } else {
        this.undecodedChunk.readerIndex(firstpos);
      } 
    } catch (ErrorDataDecoderException e) {
      this.undecodedChunk.readerIndex(firstpos);
      throw e;
    } catch (IOException e) {
      this.undecodedChunk.readerIndex(firstpos);
      throw new ErrorDataDecoderException(e);
    } 
  }
  
  private void parseBodyAttributes() throws ErrorDataDecoderException {
    // Byte code:
    //   0: new io/netty/handler/codec/http/multipart/HttpPostBodyUtil$SeekAheadOptimize
    //   3: dup
    //   4: aload_0
    //   5: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
    //   8: invokespecial <init> : (Lio/netty/buffer/ByteBuf;)V
    //   11: astore_1
    //   12: goto -> 21
    //   15: astore_2
    //   16: aload_0
    //   17: invokespecial parseBodyAttributesStandard : ()V
    //   20: return
    //   21: aload_0
    //   22: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
    //   25: invokevirtual readerIndex : ()I
    //   28: istore_2
    //   29: iload_2
    //   30: istore_3
    //   31: aload_0
    //   32: getfield currentStatus : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
    //   35: getstatic io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.NOTSTARTED : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
    //   38: if_acmpne -> 48
    //   41: aload_0
    //   42: getstatic io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.DISPOSITION : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
    //   45: putfield currentStatus : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
    //   48: iconst_1
    //   49: istore #6
    //   51: aload_1
    //   52: getfield pos : I
    //   55: aload_1
    //   56: getfield limit : I
    //   59: if_icmpge -> 512
    //   62: aload_1
    //   63: getfield bytes : [B
    //   66: aload_1
    //   67: dup
    //   68: getfield pos : I
    //   71: dup_x1
    //   72: iconst_1
    //   73: iadd
    //   74: putfield pos : I
    //   77: baload
    //   78: sipush #255
    //   81: iand
    //   82: i2c
    //   83: istore #7
    //   85: iinc #3, 1
    //   88: getstatic io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$1.$SwitchMap$io$netty$handler$codec$http$multipart$HttpPostRequestDecoder$MultiPartStatus : [I
    //   91: aload_0
    //   92: getfield currentStatus : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
    //   95: invokevirtual ordinal : ()I
    //   98: iaload
    //   99: lookupswitch default -> 498, 1 -> 124, 2 -> 287
    //   124: iload #7
    //   126: bipush #61
    //   128: if_icmpne -> 192
    //   131: aload_0
    //   132: getstatic io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.FIELD : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
    //   135: putfield currentStatus : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
    //   138: iload_3
    //   139: iconst_1
    //   140: isub
    //   141: istore #4
    //   143: aload_0
    //   144: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
    //   147: iload_2
    //   148: iload #4
    //   150: iload_2
    //   151: isub
    //   152: aload_0
    //   153: getfield charset : Ljava/nio/charset/Charset;
    //   156: invokevirtual toString : (IILjava/nio/charset/Charset;)Ljava/lang/String;
    //   159: aload_0
    //   160: getfield charset : Ljava/nio/charset/Charset;
    //   163: invokestatic decodeAttribute : (Ljava/lang/String;Ljava/nio/charset/Charset;)Ljava/lang/String;
    //   166: astore #8
    //   168: aload_0
    //   169: aload_0
    //   170: getfield factory : Lio/netty/handler/codec/http/multipart/HttpDataFactory;
    //   173: aload_0
    //   174: getfield request : Lio/netty/handler/codec/http/HttpRequest;
    //   177: aload #8
    //   179: invokeinterface createAttribute : (Lio/netty/handler/codec/http/HttpRequest;Ljava/lang/String;)Lio/netty/handler/codec/http/multipart/Attribute;
    //   184: putfield currentAttribute : Lio/netty/handler/codec/http/multipart/Attribute;
    //   187: iload_3
    //   188: istore_2
    //   189: goto -> 509
    //   192: iload #7
    //   194: bipush #38
    //   196: if_icmpne -> 509
    //   199: aload_0
    //   200: getstatic io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.DISPOSITION : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
    //   203: putfield currentStatus : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
    //   206: iload_3
    //   207: iconst_1
    //   208: isub
    //   209: istore #5
    //   211: aload_0
    //   212: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
    //   215: iload_2
    //   216: iload #5
    //   218: iload_2
    //   219: isub
    //   220: aload_0
    //   221: getfield charset : Ljava/nio/charset/Charset;
    //   224: invokevirtual toString : (IILjava/nio/charset/Charset;)Ljava/lang/String;
    //   227: aload_0
    //   228: getfield charset : Ljava/nio/charset/Charset;
    //   231: invokestatic decodeAttribute : (Ljava/lang/String;Ljava/nio/charset/Charset;)Ljava/lang/String;
    //   234: astore #8
    //   236: aload_0
    //   237: aload_0
    //   238: getfield factory : Lio/netty/handler/codec/http/multipart/HttpDataFactory;
    //   241: aload_0
    //   242: getfield request : Lio/netty/handler/codec/http/HttpRequest;
    //   245: aload #8
    //   247: invokeinterface createAttribute : (Lio/netty/handler/codec/http/HttpRequest;Ljava/lang/String;)Lio/netty/handler/codec/http/multipart/Attribute;
    //   252: putfield currentAttribute : Lio/netty/handler/codec/http/multipart/Attribute;
    //   255: aload_0
    //   256: getfield currentAttribute : Lio/netty/handler/codec/http/multipart/Attribute;
    //   259: ldc ''
    //   261: invokeinterface setValue : (Ljava/lang/String;)V
    //   266: aload_0
    //   267: aload_0
    //   268: getfield currentAttribute : Lio/netty/handler/codec/http/multipart/Attribute;
    //   271: invokevirtual addHttpData : (Lio/netty/handler/codec/http/multipart/InterfaceHttpData;)V
    //   274: aload_0
    //   275: aconst_null
    //   276: putfield currentAttribute : Lio/netty/handler/codec/http/multipart/Attribute;
    //   279: iload_3
    //   280: istore_2
    //   281: iconst_1
    //   282: istore #6
    //   284: goto -> 509
    //   287: iload #7
    //   289: bipush #38
    //   291: if_icmpne -> 330
    //   294: aload_0
    //   295: getstatic io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.DISPOSITION : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
    //   298: putfield currentStatus : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
    //   301: iload_3
    //   302: iconst_1
    //   303: isub
    //   304: istore #5
    //   306: aload_0
    //   307: aload_0
    //   308: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
    //   311: iload_2
    //   312: iload #5
    //   314: iload_2
    //   315: isub
    //   316: invokevirtual copy : (II)Lio/netty/buffer/ByteBuf;
    //   319: invokespecial setFinalBuffer : (Lio/netty/buffer/ByteBuf;)V
    //   322: iload_3
    //   323: istore_2
    //   324: iconst_1
    //   325: istore #6
    //   327: goto -> 509
    //   330: iload #7
    //   332: bipush #13
    //   334: if_icmpne -> 450
    //   337: aload_1
    //   338: getfield pos : I
    //   341: aload_1
    //   342: getfield limit : I
    //   345: if_icmpge -> 437
    //   348: aload_1
    //   349: getfield bytes : [B
    //   352: aload_1
    //   353: dup
    //   354: getfield pos : I
    //   357: dup_x1
    //   358: iconst_1
    //   359: iadd
    //   360: putfield pos : I
    //   363: baload
    //   364: sipush #255
    //   367: iand
    //   368: i2c
    //   369: istore #7
    //   371: iinc #3, 1
    //   374: iload #7
    //   376: bipush #10
    //   378: if_icmpne -> 422
    //   381: aload_0
    //   382: getstatic io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.PREEPILOGUE : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
    //   385: putfield currentStatus : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
    //   388: iload_3
    //   389: iconst_2
    //   390: isub
    //   391: istore #5
    //   393: aload_1
    //   394: iconst_0
    //   395: invokevirtual setReadPosition : (I)V
    //   398: aload_0
    //   399: aload_0
    //   400: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
    //   403: iload_2
    //   404: iload #5
    //   406: iload_2
    //   407: isub
    //   408: invokevirtual copy : (II)Lio/netty/buffer/ByteBuf;
    //   411: invokespecial setFinalBuffer : (Lio/netty/buffer/ByteBuf;)V
    //   414: iload_3
    //   415: istore_2
    //   416: iconst_0
    //   417: istore #6
    //   419: goto -> 512
    //   422: aload_1
    //   423: iconst_0
    //   424: invokevirtual setReadPosition : (I)V
    //   427: new io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException
    //   430: dup
    //   431: ldc 'Bad end of line'
    //   433: invokespecial <init> : (Ljava/lang/String;)V
    //   436: athrow
    //   437: aload_1
    //   438: getfield limit : I
    //   441: ifle -> 509
    //   444: iinc #3, -1
    //   447: goto -> 509
    //   450: iload #7
    //   452: bipush #10
    //   454: if_icmpne -> 509
    //   457: aload_0
    //   458: getstatic io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.PREEPILOGUE : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
    //   461: putfield currentStatus : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
    //   464: iload_3
    //   465: iconst_1
    //   466: isub
    //   467: istore #5
    //   469: aload_1
    //   470: iconst_0
    //   471: invokevirtual setReadPosition : (I)V
    //   474: aload_0
    //   475: aload_0
    //   476: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
    //   479: iload_2
    //   480: iload #5
    //   482: iload_2
    //   483: isub
    //   484: invokevirtual copy : (II)Lio/netty/buffer/ByteBuf;
    //   487: invokespecial setFinalBuffer : (Lio/netty/buffer/ByteBuf;)V
    //   490: iload_3
    //   491: istore_2
    //   492: iconst_0
    //   493: istore #6
    //   495: goto -> 512
    //   498: aload_1
    //   499: iconst_0
    //   500: invokevirtual setReadPosition : (I)V
    //   503: iconst_0
    //   504: istore #6
    //   506: goto -> 512
    //   509: goto -> 51
    //   512: aload_0
    //   513: getfield isLastChunk : Z
    //   516: ifeq -> 592
    //   519: aload_0
    //   520: getfield currentAttribute : Lio/netty/handler/codec/http/multipart/Attribute;
    //   523: ifnull -> 592
    //   526: iload_3
    //   527: istore #5
    //   529: iload #5
    //   531: iload_2
    //   532: if_icmple -> 554
    //   535: aload_0
    //   536: aload_0
    //   537: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
    //   540: iload_2
    //   541: iload #5
    //   543: iload_2
    //   544: isub
    //   545: invokevirtual copy : (II)Lio/netty/buffer/ByteBuf;
    //   548: invokespecial setFinalBuffer : (Lio/netty/buffer/ByteBuf;)V
    //   551: goto -> 573
    //   554: aload_0
    //   555: getfield currentAttribute : Lio/netty/handler/codec/http/multipart/Attribute;
    //   558: invokeinterface isCompleted : ()Z
    //   563: ifne -> 573
    //   566: aload_0
    //   567: getstatic io/netty/buffer/Unpooled.EMPTY_BUFFER : Lio/netty/buffer/ByteBuf;
    //   570: invokespecial setFinalBuffer : (Lio/netty/buffer/ByteBuf;)V
    //   573: iload_3
    //   574: istore_2
    //   575: aload_0
    //   576: getstatic io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.EPILOGUE : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
    //   579: putfield currentStatus : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
    //   582: aload_0
    //   583: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
    //   586: iload_2
    //   587: invokevirtual readerIndex : (I)Lio/netty/buffer/ByteBuf;
    //   590: pop
    //   591: return
    //   592: iload #6
    //   594: ifeq -> 649
    //   597: aload_0
    //   598: getfield currentAttribute : Lio/netty/handler/codec/http/multipart/Attribute;
    //   601: ifnull -> 649
    //   604: aload_0
    //   605: getfield currentStatus : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
    //   608: getstatic io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.FIELD : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
    //   611: if_acmpne -> 637
    //   614: aload_0
    //   615: getfield currentAttribute : Lio/netty/handler/codec/http/multipart/Attribute;
    //   618: aload_0
    //   619: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
    //   622: iload_2
    //   623: iload_3
    //   624: iload_2
    //   625: isub
    //   626: invokevirtual copy : (II)Lio/netty/buffer/ByteBuf;
    //   629: iconst_0
    //   630: invokeinterface addContent : (Lio/netty/buffer/ByteBuf;Z)V
    //   635: iload_3
    //   636: istore_2
    //   637: aload_0
    //   638: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
    //   641: iload_2
    //   642: invokevirtual readerIndex : (I)Lio/netty/buffer/ByteBuf;
    //   645: pop
    //   646: goto -> 658
    //   649: aload_0
    //   650: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
    //   653: iload_2
    //   654: invokevirtual readerIndex : (I)Lio/netty/buffer/ByteBuf;
    //   657: pop
    //   658: goto -> 696
    //   661: astore #7
    //   663: aload_0
    //   664: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
    //   667: iload_2
    //   668: invokevirtual readerIndex : (I)Lio/netty/buffer/ByteBuf;
    //   671: pop
    //   672: aload #7
    //   674: athrow
    //   675: astore #7
    //   677: aload_0
    //   678: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
    //   681: iload_2
    //   682: invokevirtual readerIndex : (I)Lio/netty/buffer/ByteBuf;
    //   685: pop
    //   686: new io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException
    //   689: dup
    //   690: aload #7
    //   692: invokespecial <init> : (Ljava/lang/Throwable;)V
    //   695: athrow
    //   696: return
    // Line number table:
    //   Java source line number -> byte code offset
    //   #627	-> 0
    //   #631	-> 12
    //   #628	-> 15
    //   #629	-> 16
    //   #630	-> 20
    //   #632	-> 21
    //   #633	-> 29
    //   #636	-> 31
    //   #637	-> 41
    //   #639	-> 48
    //   #641	-> 51
    //   #642	-> 62
    //   #643	-> 85
    //   #644	-> 88
    //   #646	-> 124
    //   #647	-> 131
    //   #648	-> 138
    //   #649	-> 143
    //   #651	-> 168
    //   #652	-> 187
    //   #653	-> 189
    //   #654	-> 199
    //   #655	-> 206
    //   #656	-> 211
    //   #658	-> 236
    //   #659	-> 255
    //   #660	-> 266
    //   #661	-> 274
    //   #662	-> 279
    //   #663	-> 281
    //   #664	-> 284
    //   #667	-> 287
    //   #668	-> 294
    //   #669	-> 301
    //   #670	-> 306
    //   #671	-> 322
    //   #672	-> 324
    //   #673	-> 330
    //   #674	-> 337
    //   #675	-> 348
    //   #676	-> 371
    //   #677	-> 374
    //   #678	-> 381
    //   #679	-> 388
    //   #680	-> 393
    //   #681	-> 398
    //   #682	-> 414
    //   #683	-> 416
    //   #684	-> 419
    //   #687	-> 422
    //   #688	-> 427
    //   #691	-> 437
    //   #692	-> 444
    //   #695	-> 450
    //   #696	-> 457
    //   #697	-> 464
    //   #698	-> 469
    //   #699	-> 474
    //   #700	-> 490
    //   #701	-> 492
    //   #702	-> 495
    //   #707	-> 498
    //   #708	-> 503
    //   #709	-> 506
    //   #711	-> 509
    //   #712	-> 512
    //   #714	-> 526
    //   #715	-> 529
    //   #716	-> 535
    //   #717	-> 554
    //   #718	-> 566
    //   #720	-> 573
    //   #721	-> 575
    //   #722	-> 582
    //   #723	-> 591
    //   #725	-> 592
    //   #727	-> 604
    //   #728	-> 614
    //   #730	-> 635
    //   #732	-> 637
    //   #735	-> 649
    //   #745	-> 658
    //   #737	-> 661
    //   #739	-> 663
    //   #740	-> 672
    //   #741	-> 675
    //   #743	-> 677
    //   #744	-> 686
    //   #746	-> 696
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   16	5	2	e1	Lio/netty/handler/codec/http/multipart/HttpPostBodyUtil$SeekAheadNoBackArrayException;
    //   168	21	8	key	Ljava/lang/String;
    //   143	49	4	equalpos	I
    //   236	48	8	key	Ljava/lang/String;
    //   211	119	5	ampersandpos	I
    //   393	29	5	ampersandpos	I
    //   85	424	7	read	C
    //   469	123	5	ampersandpos	I
    //   663	12	7	e	Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException;
    //   677	19	7	e	Ljava/io/IOException;
    //   0	697	0	this	Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder;
    //   12	685	1	sao	Lio/netty/handler/codec/http/multipart/HttpPostBodyUtil$SeekAheadOptimize;
    //   29	668	2	firstpos	I
    //   31	666	3	currentpos	I
    //   51	646	6	contRead	Z
    // Exception table:
    //   from	to	target	type
    //   0	12	15	io/netty/handler/codec/http/multipart/HttpPostBodyUtil$SeekAheadNoBackArrayException
    //   51	591	661	io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException
    //   51	591	675	java/io/IOException
    //   592	658	661	io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException
    //   592	658	675	java/io/IOException
  }
  
  private void setFinalBuffer(ByteBuf buffer) throws ErrorDataDecoderException, IOException {
    this.currentAttribute.addContent(buffer, true);
    String value = decodeAttribute(this.currentAttribute.getByteBuf().toString(this.charset), this.charset);
    this.currentAttribute.setValue(value);
    addHttpData(this.currentAttribute);
    this.currentAttribute = null;
  }
  
  private static String decodeAttribute(String s, Charset charset) throws ErrorDataDecoderException {
    try {
      return QueryStringDecoder.decodeComponent(s, charset);
    } catch (IllegalArgumentException e) {
      throw new ErrorDataDecoderException("Bad string: '" + s + '\'', e);
    } 
  }
  
  private void parseBodyMultipart() throws ErrorDataDecoderException {
    if (this.undecodedChunk == null || this.undecodedChunk.readableBytes() == 0)
      return; 
    InterfaceHttpData data = decodeMultipart(this.currentStatus);
    while (data != null) {
      addHttpData(data);
      if (this.currentStatus == MultiPartStatus.PREEPILOGUE || this.currentStatus == MultiPartStatus.EPILOGUE)
        break; 
      data = decodeMultipart(this.currentStatus);
    } 
  }
  
  private InterfaceHttpData decodeMultipart(MultiPartStatus state) throws ErrorDataDecoderException {
    Charset localCharset;
    Attribute charsetAttribute;
    Attribute nameAttribute;
    Attribute finalAttribute;
    switch (state) {
      case NOTSTARTED:
        throw new ErrorDataDecoderException("Should not be called with the current getStatus");
      case PREAMBLE:
        throw new ErrorDataDecoderException("Should not be called with the current getStatus");
      case HEADERDELIMITER:
        return findMultipartDelimiter(this.multipartDataBoundary, MultiPartStatus.DISPOSITION, MultiPartStatus.PREEPILOGUE);
      case DISPOSITION:
        return findMultipartDisposition();
      case FIELD:
        localCharset = null;
        charsetAttribute = this.currentFieldAttributes.get("charset");
        if (charsetAttribute != null)
          try {
            localCharset = Charset.forName(charsetAttribute.getValue());
          } catch (IOException e) {
            throw new ErrorDataDecoderException(e);
          }  
        nameAttribute = this.currentFieldAttributes.get("name");
        if (this.currentAttribute == null) {
          try {
            this.currentAttribute = this.factory.createAttribute(this.request, cleanString(nameAttribute.getValue()));
          } catch (NullPointerException e) {
            throw new ErrorDataDecoderException(e);
          } catch (IllegalArgumentException e) {
            throw new ErrorDataDecoderException(e);
          } catch (IOException e) {
            throw new ErrorDataDecoderException(e);
          } 
          if (localCharset != null)
            this.currentAttribute.setCharset(localCharset); 
        } 
        try {
          loadFieldMultipart(this.multipartDataBoundary);
        } catch (NotEnoughDataDecoderException e) {
          return null;
        } 
        finalAttribute = this.currentAttribute;
        this.currentAttribute = null;
        this.currentFieldAttributes = null;
        this.currentStatus = MultiPartStatus.HEADERDELIMITER;
        return finalAttribute;
      case FILEUPLOAD:
        return getFileUpload(this.multipartDataBoundary);
      case MIXEDDELIMITER:
        return findMultipartDelimiter(this.multipartMixedBoundary, MultiPartStatus.MIXEDDISPOSITION, MultiPartStatus.HEADERDELIMITER);
      case MIXEDDISPOSITION:
        return findMultipartDisposition();
      case MIXEDFILEUPLOAD:
        return getFileUpload(this.multipartMixedBoundary);
      case PREEPILOGUE:
        return null;
      case EPILOGUE:
        return null;
    } 
    throw new ErrorDataDecoderException("Shouldn't reach here.");
  }
  
  void skipControlCharacters() throws NotEnoughDataDecoderException {
    HttpPostBodyUtil.SeekAheadOptimize sao;
    try {
      sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
    } catch (SeekAheadNoBackArrayException e) {
      try {
        skipControlCharactersStandard();
      } catch (IndexOutOfBoundsException e1) {
        throw new NotEnoughDataDecoderException(e1);
      } 
      return;
    } 
    while (sao.pos < sao.limit) {
      char c = (char)(sao.bytes[sao.pos++] & 0xFF);
      if (!Character.isISOControl(c) && !Character.isWhitespace(c)) {
        sao.setReadPosition(1);
        return;
      } 
    } 
    throw new NotEnoughDataDecoderException("Access out of bounds");
  }
  
  void skipControlCharactersStandard() {
    while (true) {
      char c = (char)this.undecodedChunk.readUnsignedByte();
      if (!Character.isISOControl(c) && !Character.isWhitespace(c)) {
        this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
        return;
      } 
    } 
  }
  
  private InterfaceHttpData findMultipartDelimiter(String delimiter, MultiPartStatus dispositionStatus, MultiPartStatus closeDelimiterStatus) throws ErrorDataDecoderException {
    String newline;
    int readerIndex = this.undecodedChunk.readerIndex();
    try {
      skipControlCharacters();
    } catch (NotEnoughDataDecoderException e1) {
      this.undecodedChunk.readerIndex(readerIndex);
      return null;
    } 
    skipOneLine();
    try {
      newline = readDelimiter(delimiter);
    } catch (NotEnoughDataDecoderException e) {
      this.undecodedChunk.readerIndex(readerIndex);
      return null;
    } 
    if (newline.equals(delimiter)) {
      this.currentStatus = dispositionStatus;
      return decodeMultipart(dispositionStatus);
    } 
    if (newline.equals(delimiter + "--")) {
      this.currentStatus = closeDelimiterStatus;
      if (this.currentStatus == MultiPartStatus.HEADERDELIMITER) {
        this.currentFieldAttributes = null;
        return decodeMultipart(MultiPartStatus.HEADERDELIMITER);
      } 
      return null;
    } 
    this.undecodedChunk.readerIndex(readerIndex);
    throw new ErrorDataDecoderException("No Multipart delimiter found");
  }
  
  private InterfaceHttpData findMultipartDisposition() throws ErrorDataDecoderException {
    int readerIndex = this.undecodedChunk.readerIndex();
    if (this.currentStatus == MultiPartStatus.DISPOSITION)
      this.currentFieldAttributes = new TreeMap<String, Attribute>(CaseIgnoringComparator.INSTANCE); 
    while (!skipOneLine()) {
      String newline;
      try {
        skipControlCharacters();
        newline = readLine();
      } catch (NotEnoughDataDecoderException e) {
        this.undecodedChunk.readerIndex(readerIndex);
        return null;
      } 
      String[] contents = splitMultipartHeader(newline);
      if (contents[0].equalsIgnoreCase("Content-Disposition")) {
        boolean checkSecondArg;
        if (this.currentStatus == MultiPartStatus.DISPOSITION) {
          checkSecondArg = contents[1].equalsIgnoreCase("form-data");
        } else {
          checkSecondArg = (contents[1].equalsIgnoreCase("attachment") || contents[1].equalsIgnoreCase("file"));
        } 
        if (checkSecondArg)
          for (int i = 2; i < contents.length; i++) {
            Attribute attribute;
            String[] values = StringUtil.split(contents[i], '=');
            try {
              String name = cleanString(values[0]);
              String value = values[1];
              if ("filename".equals(name)) {
                value = value.substring(1, value.length() - 1);
              } else {
                value = cleanString(value);
              } 
              attribute = this.factory.createAttribute(this.request, name, value);
            } catch (NullPointerException e) {
              throw new ErrorDataDecoderException(e);
            } catch (IllegalArgumentException e) {
              throw new ErrorDataDecoderException(e);
            } 
            this.currentFieldAttributes.put(attribute.getName(), attribute);
          }  
        continue;
      } 
      if (contents[0].equalsIgnoreCase("Content-Transfer-Encoding")) {
        Attribute attribute;
        try {
          attribute = this.factory.createAttribute(this.request, "Content-Transfer-Encoding", cleanString(contents[1]));
        } catch (NullPointerException e) {
          throw new ErrorDataDecoderException(e);
        } catch (IllegalArgumentException e) {
          throw new ErrorDataDecoderException(e);
        } 
        this.currentFieldAttributes.put("Content-Transfer-Encoding", attribute);
        continue;
      } 
      if (contents[0].equalsIgnoreCase("Content-Length")) {
        Attribute attribute;
        try {
          attribute = this.factory.createAttribute(this.request, "Content-Length", cleanString(contents[1]));
        } catch (NullPointerException e) {
          throw new ErrorDataDecoderException(e);
        } catch (IllegalArgumentException e) {
          throw new ErrorDataDecoderException(e);
        } 
        this.currentFieldAttributes.put("Content-Length", attribute);
        continue;
      } 
      if (contents[0].equalsIgnoreCase("Content-Type")) {
        if (contents[1].equalsIgnoreCase("multipart/mixed")) {
          if (this.currentStatus == MultiPartStatus.DISPOSITION) {
            String[] values = StringUtil.split(contents[2], '=');
            this.multipartMixedBoundary = "--" + values[1];
            this.currentStatus = MultiPartStatus.MIXEDDELIMITER;
            return decodeMultipart(MultiPartStatus.MIXEDDELIMITER);
          } 
          throw new ErrorDataDecoderException("Mixed Multipart found in a previous Mixed Multipart");
        } 
        for (int i = 1; i < contents.length; i++) {
          if (contents[i].toLowerCase().startsWith("charset")) {
            Attribute attribute;
            String[] values = StringUtil.split(contents[i], '=');
            try {
              attribute = this.factory.createAttribute(this.request, "charset", cleanString(values[1]));
            } catch (NullPointerException e) {
              throw new ErrorDataDecoderException(e);
            } catch (IllegalArgumentException e) {
              throw new ErrorDataDecoderException(e);
            } 
            this.currentFieldAttributes.put("charset", attribute);
          } else {
            Attribute attribute;
            try {
              attribute = this.factory.createAttribute(this.request, cleanString(contents[0]), contents[i]);
            } catch (NullPointerException e) {
              throw new ErrorDataDecoderException(e);
            } catch (IllegalArgumentException e) {
              throw new ErrorDataDecoderException(e);
            } 
            this.currentFieldAttributes.put(attribute.getName(), attribute);
          } 
        } 
        continue;
      } 
      throw new ErrorDataDecoderException("Unknown Params: " + newline);
    } 
    Attribute filenameAttribute = this.currentFieldAttributes.get("filename");
    if (this.currentStatus == MultiPartStatus.DISPOSITION) {
      if (filenameAttribute != null) {
        this.currentStatus = MultiPartStatus.FILEUPLOAD;
        return decodeMultipart(MultiPartStatus.FILEUPLOAD);
      } 
      this.currentStatus = MultiPartStatus.FIELD;
      return decodeMultipart(MultiPartStatus.FIELD);
    } 
    if (filenameAttribute != null) {
      this.currentStatus = MultiPartStatus.MIXEDFILEUPLOAD;
      return decodeMultipart(MultiPartStatus.MIXEDFILEUPLOAD);
    } 
    throw new ErrorDataDecoderException("Filename not found");
  }
  
  protected InterfaceHttpData getFileUpload(String delimiter) throws ErrorDataDecoderException {
    Attribute encoding = this.currentFieldAttributes.get("Content-Transfer-Encoding");
    Charset localCharset = this.charset;
    HttpPostBodyUtil.TransferEncodingMechanism mechanism = HttpPostBodyUtil.TransferEncodingMechanism.BIT7;
    if (encoding != null) {
      String code;
      try {
        code = encoding.getValue().toLowerCase();
      } catch (IOException e) {
        throw new ErrorDataDecoderException(e);
      } 
      if (code.equals(HttpPostBodyUtil.TransferEncodingMechanism.BIT7.value())) {
        localCharset = HttpPostBodyUtil.US_ASCII;
      } else if (code.equals(HttpPostBodyUtil.TransferEncodingMechanism.BIT8.value())) {
        localCharset = HttpPostBodyUtil.ISO_8859_1;
        mechanism = HttpPostBodyUtil.TransferEncodingMechanism.BIT8;
      } else if (code.equals(HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value())) {
        mechanism = HttpPostBodyUtil.TransferEncodingMechanism.BINARY;
      } else {
        throw new ErrorDataDecoderException("TransferEncoding Unknown: " + code);
      } 
    } 
    Attribute charsetAttribute = this.currentFieldAttributes.get("charset");
    if (charsetAttribute != null)
      try {
        localCharset = Charset.forName(charsetAttribute.getValue());
      } catch (IOException e) {
        throw new ErrorDataDecoderException(e);
      }  
    if (this.currentFileUpload == null) {
      long l;
      Attribute filenameAttribute = this.currentFieldAttributes.get("filename");
      Attribute nameAttribute = this.currentFieldAttributes.get("name");
      Attribute contentTypeAttribute = this.currentFieldAttributes.get("Content-Type");
      if (contentTypeAttribute == null)
        throw new ErrorDataDecoderException("Content-Type is absent but required"); 
      Attribute lengthAttribute = this.currentFieldAttributes.get("Content-Length");
      try {
        l = (lengthAttribute != null) ? Long.parseLong(lengthAttribute.getValue()) : 0L;
      } catch (IOException e) {
        throw new ErrorDataDecoderException(e);
      } catch (NumberFormatException e) {
        l = 0L;
      } 
      try {
        this.currentFileUpload = this.factory.createFileUpload(this.request, cleanString(nameAttribute.getValue()), cleanString(filenameAttribute.getValue()), contentTypeAttribute.getValue(), mechanism.value(), localCharset, l);
      } catch (NullPointerException e) {
        throw new ErrorDataDecoderException(e);
      } catch (IllegalArgumentException e) {
        throw new ErrorDataDecoderException(e);
      } catch (IOException e) {
        throw new ErrorDataDecoderException(e);
      } 
    } 
    try {
      readFileUploadByteMultipart(delimiter);
    } catch (NotEnoughDataDecoderException e) {
      return null;
    } 
    if (this.currentFileUpload.isCompleted()) {
      if (this.currentStatus == MultiPartStatus.FILEUPLOAD) {
        this.currentStatus = MultiPartStatus.HEADERDELIMITER;
        this.currentFieldAttributes = null;
      } else {
        this.currentStatus = MultiPartStatus.MIXEDDELIMITER;
        cleanMixedAttributes();
      } 
      FileUpload fileUpload = this.currentFileUpload;
      this.currentFileUpload = null;
      return fileUpload;
    } 
    return null;
  }
  
  public void destroy() {
    checkDestroyed();
    cleanFiles();
    this.destroyed = true;
    if (this.undecodedChunk != null && this.undecodedChunk.refCnt() > 0) {
      this.undecodedChunk.release();
      this.undecodedChunk = null;
    } 
    for (int i = this.bodyListHttpDataRank; i < this.bodyListHttpData.size(); i++)
      ((InterfaceHttpData)this.bodyListHttpData.get(i)).release(); 
  }
  
  public void cleanFiles() {
    checkDestroyed();
    this.factory.cleanRequestHttpDatas(this.request);
  }
  
  public void removeHttpDataFromClean(InterfaceHttpData data) {
    checkDestroyed();
    this.factory.removeHttpDataFromClean(this.request, data);
  }
  
  private void cleanMixedAttributes() {
    this.currentFieldAttributes.remove("charset");
    this.currentFieldAttributes.remove("Content-Length");
    this.currentFieldAttributes.remove("Content-Transfer-Encoding");
    this.currentFieldAttributes.remove("Content-Type");
    this.currentFieldAttributes.remove("filename");
  }
  
  private String readLineStandard() throws NotEnoughDataDecoderException {
    int readerIndex = this.undecodedChunk.readerIndex();
    try {
      ByteBuf line = Unpooled.buffer(64);
      while (this.undecodedChunk.isReadable()) {
        byte nextByte = this.undecodedChunk.readByte();
        if (nextByte == 13) {
          nextByte = this.undecodedChunk.getByte(this.undecodedChunk.readerIndex());
          if (nextByte == 10) {
            this.undecodedChunk.skipBytes(1);
            return line.toString(this.charset);
          } 
          line.writeByte(13);
          continue;
        } 
        if (nextByte == 10)
          return line.toString(this.charset); 
        line.writeByte(nextByte);
      } 
    } catch (IndexOutOfBoundsException e) {
      this.undecodedChunk.readerIndex(readerIndex);
      throw new NotEnoughDataDecoderException(e);
    } 
    this.undecodedChunk.readerIndex(readerIndex);
    throw new NotEnoughDataDecoderException();
  }
  
  private String readLine() throws NotEnoughDataDecoderException {
    HttpPostBodyUtil.SeekAheadOptimize sao;
    try {
      sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
    } catch (SeekAheadNoBackArrayException e1) {
      return readLineStandard();
    } 
    int readerIndex = this.undecodedChunk.readerIndex();
    try {
      ByteBuf line = Unpooled.buffer(64);
      while (sao.pos < sao.limit) {
        byte nextByte = sao.bytes[sao.pos++];
        if (nextByte == 13) {
          if (sao.pos < sao.limit) {
            nextByte = sao.bytes[sao.pos++];
            if (nextByte == 10) {
              sao.setReadPosition(0);
              return line.toString(this.charset);
            } 
            sao.pos--;
            line.writeByte(13);
            continue;
          } 
          line.writeByte(nextByte);
          continue;
        } 
        if (nextByte == 10) {
          sao.setReadPosition(0);
          return line.toString(this.charset);
        } 
        line.writeByte(nextByte);
      } 
    } catch (IndexOutOfBoundsException e) {
      this.undecodedChunk.readerIndex(readerIndex);
      throw new NotEnoughDataDecoderException(e);
    } 
    this.undecodedChunk.readerIndex(readerIndex);
    throw new NotEnoughDataDecoderException();
  }
  
  private String readDelimiterStandard(String delimiter) throws NotEnoughDataDecoderException {
    int readerIndex = this.undecodedChunk.readerIndex();
    try {
      StringBuilder sb = new StringBuilder(64);
      int delimiterPos = 0;
      int len = delimiter.length();
      while (this.undecodedChunk.isReadable() && delimiterPos < len) {
        byte nextByte = this.undecodedChunk.readByte();
        if (nextByte == delimiter.charAt(delimiterPos)) {
          delimiterPos++;
          sb.append((char)nextByte);
          continue;
        } 
        this.undecodedChunk.readerIndex(readerIndex);
        throw new NotEnoughDataDecoderException();
      } 
      if (this.undecodedChunk.isReadable()) {
        byte nextByte = this.undecodedChunk.readByte();
        if (nextByte == 13) {
          nextByte = this.undecodedChunk.readByte();
          if (nextByte == 10)
            return sb.toString(); 
          this.undecodedChunk.readerIndex(readerIndex);
          throw new NotEnoughDataDecoderException();
        } 
        if (nextByte == 10)
          return sb.toString(); 
        if (nextByte == 45) {
          sb.append('-');
          nextByte = this.undecodedChunk.readByte();
          if (nextByte == 45) {
            sb.append('-');
            if (this.undecodedChunk.isReadable()) {
              nextByte = this.undecodedChunk.readByte();
              if (nextByte == 13) {
                nextByte = this.undecodedChunk.readByte();
                if (nextByte == 10)
                  return sb.toString(); 
                this.undecodedChunk.readerIndex(readerIndex);
                throw new NotEnoughDataDecoderException();
              } 
              if (nextByte == 10)
                return sb.toString(); 
              this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
              return sb.toString();
            } 
            return sb.toString();
          } 
        } 
      } 
    } catch (IndexOutOfBoundsException e) {
      this.undecodedChunk.readerIndex(readerIndex);
      throw new NotEnoughDataDecoderException(e);
    } 
    this.undecodedChunk.readerIndex(readerIndex);
    throw new NotEnoughDataDecoderException();
  }
  
  private String readDelimiter(String delimiter) throws NotEnoughDataDecoderException {
    HttpPostBodyUtil.SeekAheadOptimize sao;
    try {
      sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
    } catch (SeekAheadNoBackArrayException e1) {
      return readDelimiterStandard(delimiter);
    } 
    int readerIndex = this.undecodedChunk.readerIndex();
    int delimiterPos = 0;
    int len = delimiter.length();
    try {
      StringBuilder sb = new StringBuilder(64);
      while (sao.pos < sao.limit && delimiterPos < len) {
        byte nextByte = sao.bytes[sao.pos++];
        if (nextByte == delimiter.charAt(delimiterPos)) {
          delimiterPos++;
          sb.append((char)nextByte);
          continue;
        } 
        this.undecodedChunk.readerIndex(readerIndex);
        throw new NotEnoughDataDecoderException();
      } 
      if (sao.pos < sao.limit) {
        byte nextByte = sao.bytes[sao.pos++];
        if (nextByte == 13) {
          if (sao.pos < sao.limit) {
            nextByte = sao.bytes[sao.pos++];
            if (nextByte == 10) {
              sao.setReadPosition(0);
              return sb.toString();
            } 
            this.undecodedChunk.readerIndex(readerIndex);
            throw new NotEnoughDataDecoderException();
          } 
          this.undecodedChunk.readerIndex(readerIndex);
          throw new NotEnoughDataDecoderException();
        } 
        if (nextByte == 10) {
          sao.setReadPosition(0);
          return sb.toString();
        } 
        if (nextByte == 45) {
          sb.append('-');
          if (sao.pos < sao.limit) {
            nextByte = sao.bytes[sao.pos++];
            if (nextByte == 45) {
              sb.append('-');
              if (sao.pos < sao.limit) {
                nextByte = sao.bytes[sao.pos++];
                if (nextByte == 13) {
                  if (sao.pos < sao.limit) {
                    nextByte = sao.bytes[sao.pos++];
                    if (nextByte == 10) {
                      sao.setReadPosition(0);
                      return sb.toString();
                    } 
                    this.undecodedChunk.readerIndex(readerIndex);
                    throw new NotEnoughDataDecoderException();
                  } 
                  this.undecodedChunk.readerIndex(readerIndex);
                  throw new NotEnoughDataDecoderException();
                } 
                if (nextByte == 10) {
                  sao.setReadPosition(0);
                  return sb.toString();
                } 
                sao.setReadPosition(1);
                return sb.toString();
              } 
              sao.setReadPosition(0);
              return sb.toString();
            } 
          } 
        } 
      } 
    } catch (IndexOutOfBoundsException e) {
      this.undecodedChunk.readerIndex(readerIndex);
      throw new NotEnoughDataDecoderException(e);
    } 
    this.undecodedChunk.readerIndex(readerIndex);
    throw new NotEnoughDataDecoderException();
  }
  
  private void readFileUploadByteMultipartStandard(String delimiter) throws NotEnoughDataDecoderException, ErrorDataDecoderException {
    int readerIndex = this.undecodedChunk.readerIndex();
    boolean newLine = true;
    int index = 0;
    int lastPosition = this.undecodedChunk.readerIndex();
    boolean found = false;
    while (this.undecodedChunk.isReadable()) {
      byte nextByte = this.undecodedChunk.readByte();
      if (newLine) {
        if (nextByte == delimiter.codePointAt(index)) {
          index++;
          if (delimiter.length() == index) {
            found = true;
            break;
          } 
          continue;
        } 
        newLine = false;
        index = 0;
        if (nextByte == 13) {
          if (this.undecodedChunk.isReadable()) {
            nextByte = this.undecodedChunk.readByte();
            if (nextByte == 10) {
              newLine = true;
              index = 0;
              lastPosition = this.undecodedChunk.readerIndex() - 2;
              continue;
            } 
            lastPosition = this.undecodedChunk.readerIndex() - 1;
            this.undecodedChunk.readerIndex(lastPosition);
          } 
          continue;
        } 
        if (nextByte == 10) {
          newLine = true;
          index = 0;
          lastPosition = this.undecodedChunk.readerIndex() - 1;
          continue;
        } 
        lastPosition = this.undecodedChunk.readerIndex();
        continue;
      } 
      if (nextByte == 13) {
        if (this.undecodedChunk.isReadable()) {
          nextByte = this.undecodedChunk.readByte();
          if (nextByte == 10) {
            newLine = true;
            index = 0;
            lastPosition = this.undecodedChunk.readerIndex() - 2;
            continue;
          } 
          lastPosition = this.undecodedChunk.readerIndex() - 1;
          this.undecodedChunk.readerIndex(lastPosition);
        } 
        continue;
      } 
      if (nextByte == 10) {
        newLine = true;
        index = 0;
        lastPosition = this.undecodedChunk.readerIndex() - 1;
        continue;
      } 
      lastPosition = this.undecodedChunk.readerIndex();
    } 
    ByteBuf buffer = this.undecodedChunk.copy(readerIndex, lastPosition - readerIndex);
    if (found) {
      try {
        this.currentFileUpload.addContent(buffer, true);
        this.undecodedChunk.readerIndex(lastPosition);
      } catch (IOException e) {
        throw new ErrorDataDecoderException(e);
      } 
    } else {
      try {
        this.currentFileUpload.addContent(buffer, false);
        this.undecodedChunk.readerIndex(lastPosition);
        throw new NotEnoughDataDecoderException();
      } catch (IOException e) {
        throw new ErrorDataDecoderException(e);
      } 
    } 
  }
  
  private void readFileUploadByteMultipart(String delimiter) throws NotEnoughDataDecoderException, ErrorDataDecoderException {
    HttpPostBodyUtil.SeekAheadOptimize sao;
    try {
      sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
    } catch (SeekAheadNoBackArrayException e1) {
      readFileUploadByteMultipartStandard(delimiter);
      return;
    } 
    int readerIndex = this.undecodedChunk.readerIndex();
    boolean newLine = true;
    int index = 0;
    int lastrealpos = sao.pos;
    boolean found = false;
    while (sao.pos < sao.limit) {
      byte nextByte = sao.bytes[sao.pos++];
      if (newLine) {
        if (nextByte == delimiter.codePointAt(index)) {
          index++;
          if (delimiter.length() == index) {
            found = true;
            break;
          } 
          continue;
        } 
        newLine = false;
        index = 0;
        if (nextByte == 13) {
          if (sao.pos < sao.limit) {
            nextByte = sao.bytes[sao.pos++];
            if (nextByte == 10) {
              newLine = true;
              index = 0;
              lastrealpos = sao.pos - 2;
              continue;
            } 
            lastrealpos = --sao.pos;
          } 
          continue;
        } 
        if (nextByte == 10) {
          newLine = true;
          index = 0;
          lastrealpos = sao.pos - 1;
          continue;
        } 
        lastrealpos = sao.pos;
        continue;
      } 
      if (nextByte == 13) {
        if (sao.pos < sao.limit) {
          nextByte = sao.bytes[sao.pos++];
          if (nextByte == 10) {
            newLine = true;
            index = 0;
            lastrealpos = sao.pos - 2;
            continue;
          } 
          lastrealpos = --sao.pos;
        } 
        continue;
      } 
      if (nextByte == 10) {
        newLine = true;
        index = 0;
        lastrealpos = sao.pos - 1;
        continue;
      } 
      lastrealpos = sao.pos;
    } 
    int lastPosition = sao.getReadPosition(lastrealpos);
    ByteBuf buffer = this.undecodedChunk.copy(readerIndex, lastPosition - readerIndex);
    if (found) {
      try {
        this.currentFileUpload.addContent(buffer, true);
        this.undecodedChunk.readerIndex(lastPosition);
      } catch (IOException e) {
        throw new ErrorDataDecoderException(e);
      } 
    } else {
      try {
        this.currentFileUpload.addContent(buffer, false);
        this.undecodedChunk.readerIndex(lastPosition);
        throw new NotEnoughDataDecoderException();
      } catch (IOException e) {
        throw new ErrorDataDecoderException(e);
      } 
    } 
  }
  
  private void loadFieldMultipartStandard(String delimiter) throws NotEnoughDataDecoderException, ErrorDataDecoderException {
    int readerIndex = this.undecodedChunk.readerIndex();
    try {
      boolean newLine = true;
      int index = 0;
      int lastPosition = this.undecodedChunk.readerIndex();
      boolean found = false;
      while (this.undecodedChunk.isReadable()) {
        byte nextByte = this.undecodedChunk.readByte();
        if (newLine) {
          if (nextByte == delimiter.codePointAt(index)) {
            index++;
            if (delimiter.length() == index) {
              found = true;
              break;
            } 
            continue;
          } 
          newLine = false;
          index = 0;
          if (nextByte == 13) {
            if (this.undecodedChunk.isReadable()) {
              nextByte = this.undecodedChunk.readByte();
              if (nextByte == 10) {
                newLine = true;
                index = 0;
                lastPosition = this.undecodedChunk.readerIndex() - 2;
                continue;
              } 
              lastPosition = this.undecodedChunk.readerIndex() - 1;
              this.undecodedChunk.readerIndex(lastPosition);
              continue;
            } 
            lastPosition = this.undecodedChunk.readerIndex() - 1;
            continue;
          } 
          if (nextByte == 10) {
            newLine = true;
            index = 0;
            lastPosition = this.undecodedChunk.readerIndex() - 1;
            continue;
          } 
          lastPosition = this.undecodedChunk.readerIndex();
          continue;
        } 
        if (nextByte == 13) {
          if (this.undecodedChunk.isReadable()) {
            nextByte = this.undecodedChunk.readByte();
            if (nextByte == 10) {
              newLine = true;
              index = 0;
              lastPosition = this.undecodedChunk.readerIndex() - 2;
              continue;
            } 
            lastPosition = this.undecodedChunk.readerIndex() - 1;
            this.undecodedChunk.readerIndex(lastPosition);
            continue;
          } 
          lastPosition = this.undecodedChunk.readerIndex() - 1;
          continue;
        } 
        if (nextByte == 10) {
          newLine = true;
          index = 0;
          lastPosition = this.undecodedChunk.readerIndex() - 1;
          continue;
        } 
        lastPosition = this.undecodedChunk.readerIndex();
      } 
      if (found) {
        try {
          this.currentAttribute.addContent(this.undecodedChunk.copy(readerIndex, lastPosition - readerIndex), true);
        } catch (IOException e) {
          throw new ErrorDataDecoderException(e);
        } 
        this.undecodedChunk.readerIndex(lastPosition);
      } else {
        try {
          this.currentAttribute.addContent(this.undecodedChunk.copy(readerIndex, lastPosition - readerIndex), false);
        } catch (IOException e) {
          throw new ErrorDataDecoderException(e);
        } 
        this.undecodedChunk.readerIndex(lastPosition);
        throw new NotEnoughDataDecoderException();
      } 
    } catch (IndexOutOfBoundsException e) {
      this.undecodedChunk.readerIndex(readerIndex);
      throw new NotEnoughDataDecoderException(e);
    } 
  }
  
  private void loadFieldMultipart(String delimiter) throws NotEnoughDataDecoderException, ErrorDataDecoderException {
    HttpPostBodyUtil.SeekAheadOptimize sao;
    try {
      sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
    } catch (SeekAheadNoBackArrayException e1) {
      loadFieldMultipartStandard(delimiter);
      return;
    } 
    int readerIndex = this.undecodedChunk.readerIndex();
    try {
      boolean newLine = true;
      int index = 0;
      int lastrealpos = sao.pos;
      boolean found = false;
      while (sao.pos < sao.limit) {
        byte nextByte = sao.bytes[sao.pos++];
        if (newLine) {
          if (nextByte == delimiter.codePointAt(index)) {
            index++;
            if (delimiter.length() == index) {
              found = true;
              break;
            } 
            continue;
          } 
          newLine = false;
          index = 0;
          if (nextByte == 13) {
            if (sao.pos < sao.limit) {
              nextByte = sao.bytes[sao.pos++];
              if (nextByte == 10) {
                newLine = true;
                index = 0;
                lastrealpos = sao.pos - 2;
                continue;
              } 
              lastrealpos = --sao.pos;
            } 
            continue;
          } 
          if (nextByte == 10) {
            newLine = true;
            index = 0;
            lastrealpos = sao.pos - 1;
            continue;
          } 
          lastrealpos = sao.pos;
          continue;
        } 
        if (nextByte == 13) {
          if (sao.pos < sao.limit) {
            nextByte = sao.bytes[sao.pos++];
            if (nextByte == 10) {
              newLine = true;
              index = 0;
              lastrealpos = sao.pos - 2;
              continue;
            } 
            lastrealpos = --sao.pos;
          } 
          continue;
        } 
        if (nextByte == 10) {
          newLine = true;
          index = 0;
          lastrealpos = sao.pos - 1;
          continue;
        } 
        lastrealpos = sao.pos;
      } 
      int lastPosition = sao.getReadPosition(lastrealpos);
      if (found) {
        try {
          this.currentAttribute.addContent(this.undecodedChunk.copy(readerIndex, lastPosition - readerIndex), true);
        } catch (IOException e) {
          throw new ErrorDataDecoderException(e);
        } 
        this.undecodedChunk.readerIndex(lastPosition);
      } else {
        try {
          this.currentAttribute.addContent(this.undecodedChunk.copy(readerIndex, lastPosition - readerIndex), false);
        } catch (IOException e) {
          throw new ErrorDataDecoderException(e);
        } 
        this.undecodedChunk.readerIndex(lastPosition);
        throw new NotEnoughDataDecoderException();
      } 
    } catch (IndexOutOfBoundsException e) {
      this.undecodedChunk.readerIndex(readerIndex);
      throw new NotEnoughDataDecoderException(e);
    } 
  }
  
  private static String cleanString(String field) {
    StringBuilder sb = new StringBuilder(field.length());
    for (int i = 0; i < field.length(); i++) {
      char nextChar = field.charAt(i);
      if (nextChar == ':') {
        sb.append(32);
      } else if (nextChar == ',') {
        sb.append(32);
      } else if (nextChar == '=') {
        sb.append(32);
      } else if (nextChar == ';') {
        sb.append(32);
      } else if (nextChar == '\t') {
        sb.append(32);
      } else if (nextChar != '"') {
        sb.append(nextChar);
      } 
    } 
    return sb.toString().trim();
  }
  
  private boolean skipOneLine() {
    if (!this.undecodedChunk.isReadable())
      return false; 
    byte nextByte = this.undecodedChunk.readByte();
    if (nextByte == 13) {
      if (!this.undecodedChunk.isReadable()) {
        this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
        return false;
      } 
      nextByte = this.undecodedChunk.readByte();
      if (nextByte == 10)
        return true; 
      this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 2);
      return false;
    } 
    if (nextByte == 10)
      return true; 
    this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
    return false;
  }
  
  private static String[] splitHeaderContentType(String sb) {
    int aStart = HttpPostBodyUtil.findNonWhitespace(sb, 0);
    int aEnd = sb.indexOf(';');
    if (aEnd == -1)
      return new String[] { sb, "" }; 
    if (sb.charAt(aEnd - 1) == ' ')
      aEnd--; 
    int bStart = HttpPostBodyUtil.findNonWhitespace(sb, aEnd + 1);
    int bEnd = HttpPostBodyUtil.findEndOfString(sb);
    return new String[] { sb.substring(aStart, aEnd), sb.substring(bStart, bEnd) };
  }
  
  private static String[] splitMultipartHeader(String sb) {
    String[] values;
    ArrayList<String> headers = new ArrayList<String>(1);
    int nameStart = HttpPostBodyUtil.findNonWhitespace(sb, 0);
    int nameEnd;
    for (nameEnd = nameStart; nameEnd < sb.length(); nameEnd++) {
      char ch = sb.charAt(nameEnd);
      if (ch == ':' || Character.isWhitespace(ch))
        break; 
    } 
    int colonEnd;
    for (colonEnd = nameEnd; colonEnd < sb.length(); colonEnd++) {
      if (sb.charAt(colonEnd) == ':') {
        colonEnd++;
        break;
      } 
    } 
    int valueStart = HttpPostBodyUtil.findNonWhitespace(sb, colonEnd);
    int valueEnd = HttpPostBodyUtil.findEndOfString(sb);
    headers.add(sb.substring(nameStart, nameEnd));
    String svalue = sb.substring(valueStart, valueEnd);
    if (svalue.indexOf(';') >= 0) {
      values = StringUtil.split(svalue, ';');
    } else {
      values = StringUtil.split(svalue, ',');
    } 
    for (String value : values)
      headers.add(value.trim()); 
    String[] array = new String[headers.size()];
    for (int i = 0; i < headers.size(); i++)
      array[i] = headers.get(i); 
    return array;
  }
  
  public static class NotEnoughDataDecoderException extends DecoderException {
    private static final long serialVersionUID = -7846841864603865638L;
    
    public NotEnoughDataDecoderException() {}
    
    public NotEnoughDataDecoderException(String msg) {
      super(msg);
    }
    
    public NotEnoughDataDecoderException(Throwable cause) {
      super(cause);
    }
    
    public NotEnoughDataDecoderException(String msg, Throwable cause) {
      super(msg, cause);
    }
  }
  
  public static class EndOfDataDecoderException extends DecoderException {
    private static final long serialVersionUID = 1336267941020800769L;
  }
  
  public static class ErrorDataDecoderException extends DecoderException {
    private static final long serialVersionUID = 5020247425493164465L;
    
    public ErrorDataDecoderException() {}
    
    public ErrorDataDecoderException(String msg) {
      super(msg);
    }
    
    public ErrorDataDecoderException(Throwable cause) {
      super(cause);
    }
    
    public ErrorDataDecoderException(String msg, Throwable cause) {
      super(msg, cause);
    }
  }
  
  public static class IncompatibleDataDecoderException extends DecoderException {
    private static final long serialVersionUID = -953268047926250267L;
    
    public IncompatibleDataDecoderException() {}
    
    public IncompatibleDataDecoderException(String msg) {
      super(msg);
    }
    
    public IncompatibleDataDecoderException(Throwable cause) {
      super(cause);
    }
    
    public IncompatibleDataDecoderException(String msg, Throwable cause) {
      super(msg, cause);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\multipart\HttpPostRequestDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */