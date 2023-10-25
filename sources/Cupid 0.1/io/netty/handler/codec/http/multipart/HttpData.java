package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public interface HttpData extends InterfaceHttpData, ByteBufHolder {
  void setContent(ByteBuf paramByteBuf) throws IOException;
  
  void addContent(ByteBuf paramByteBuf, boolean paramBoolean) throws IOException;
  
  void setContent(File paramFile) throws IOException;
  
  void setContent(InputStream paramInputStream) throws IOException;
  
  boolean isCompleted();
  
  long length();
  
  void delete();
  
  byte[] get() throws IOException;
  
  ByteBuf getByteBuf() throws IOException;
  
  ByteBuf getChunk(int paramInt) throws IOException;
  
  String getString() throws IOException;
  
  String getString(Charset paramCharset) throws IOException;
  
  void setCharset(Charset paramCharset);
  
  Charset getCharset();
  
  boolean renameTo(File paramFile) throws IOException;
  
  boolean isInMemory();
  
  File getFile() throws IOException;
  
  HttpData copy();
  
  HttpData duplicate();
  
  HttpData retain();
  
  HttpData retain(int paramInt);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\multipart\HttpData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */