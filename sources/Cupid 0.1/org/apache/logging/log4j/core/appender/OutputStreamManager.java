package org.apache.logging.log4j.core.appender;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.layout.ByteBufferDestination;
import org.apache.logging.log4j.core.layout.ByteBufferDestinationHelper;
import org.apache.logging.log4j.core.util.Constants;

public class OutputStreamManager extends AbstractManager implements ByteBufferDestination {
  protected final Layout<?> layout;
  
  protected ByteBuffer byteBuffer;
  
  private volatile OutputStream outputStream;
  
  private boolean skipFooter;
  
  protected OutputStreamManager(OutputStream os, String streamName, Layout<?> layout, boolean writeHeader) {
    this(os, streamName, layout, writeHeader, Constants.ENCODER_BYTE_BUFFER_SIZE);
  }
  
  protected OutputStreamManager(OutputStream os, String streamName, Layout<?> layout, boolean writeHeader, int bufferSize) {
    this(os, streamName, layout, writeHeader, ByteBuffer.wrap(new byte[bufferSize]));
  }
  
  @Deprecated
  protected OutputStreamManager(OutputStream os, String streamName, Layout<?> layout, boolean writeHeader, ByteBuffer byteBuffer) {
    super(null, streamName);
    this.outputStream = os;
    this.layout = layout;
    if (writeHeader)
      writeHeader(os); 
    this.byteBuffer = Objects.<ByteBuffer>requireNonNull(byteBuffer, "byteBuffer");
  }
  
  protected OutputStreamManager(LoggerContext loggerContext, OutputStream os, String streamName, boolean createOnDemand, Layout<? extends Serializable> layout, boolean writeHeader, ByteBuffer byteBuffer) {
    super(loggerContext, streamName);
    if (createOnDemand && os != null)
      LOGGER.error("Invalid OutputStreamManager configuration for '{}': You cannot both set the OutputStream and request on-demand.", streamName); 
    this.layout = layout;
    this.byteBuffer = Objects.<ByteBuffer>requireNonNull(byteBuffer, "byteBuffer");
    this.outputStream = os;
    if (writeHeader)
      writeHeader(os); 
  }
  
  public static <T> OutputStreamManager getManager(String name, T data, ManagerFactory<? extends OutputStreamManager, T> factory) {
    return AbstractManager.<OutputStreamManager, T>getManager(name, (ManagerFactory)factory, data);
  }
  
  protected OutputStream createOutputStream() throws IOException {
    throw new IllegalStateException(getClass().getCanonicalName() + " must implement createOutputStream()");
  }
  
  public void skipFooter(boolean skipFooter) {
    this.skipFooter = skipFooter;
  }
  
  public boolean releaseSub(long timeout, TimeUnit timeUnit) {
    writeFooter();
    return closeOutputStream();
  }
  
  protected void writeHeader(OutputStream os) {
    if (this.layout != null && os != null) {
      byte[] header = this.layout.getHeader();
      if (header != null)
        try {
          os.write(header, 0, header.length);
        } catch (IOException e) {
          logError("Unable to write header", e);
        }  
    } 
  }
  
  protected void writeFooter() {
    if (this.layout == null || this.skipFooter)
      return; 
    byte[] footer = this.layout.getFooter();
    if (footer != null)
      write(footer); 
  }
  
  public boolean isOpen() {
    return (getCount() > 0);
  }
  
  public boolean hasOutputStream() {
    return (this.outputStream != null);
  }
  
  protected OutputStream getOutputStream() throws IOException {
    if (this.outputStream == null)
      this.outputStream = createOutputStream(); 
    return this.outputStream;
  }
  
  protected void setOutputStream(OutputStream os) {
    this.outputStream = os;
  }
  
  protected void write(byte[] bytes) {
    write(bytes, 0, bytes.length, false);
  }
  
  protected void write(byte[] bytes, boolean immediateFlush) {
    write(bytes, 0, bytes.length, immediateFlush);
  }
  
  public void writeBytes(byte[] data, int offset, int length) {
    write(data, offset, length, false);
  }
  
  protected void write(byte[] bytes, int offset, int length) {
    writeBytes(bytes, offset, length);
  }
  
  protected synchronized void write(byte[] bytes, int offset, int length, boolean immediateFlush) {
    if (immediateFlush && this.byteBuffer.position() == 0) {
      writeToDestination(bytes, offset, length);
      flushDestination();
      return;
    } 
    if (length >= this.byteBuffer.capacity()) {
      flush();
      writeToDestination(bytes, offset, length);
    } else {
      if (length > this.byteBuffer.remaining())
        flush(); 
      this.byteBuffer.put(bytes, offset, length);
    } 
    if (immediateFlush)
      flush(); 
  }
  
  protected synchronized void writeToDestination(byte[] bytes, int offset, int length) {
    try {
      getOutputStream().write(bytes, offset, length);
    } catch (IOException ex) {
      throw new AppenderLoggingException("Error writing to stream " + getName(), ex);
    } 
  }
  
  protected synchronized void flushDestination() {
    OutputStream stream = this.outputStream;
    if (stream != null)
      try {
        stream.flush();
      } catch (IOException ex) {
        throw new AppenderLoggingException("Error flushing stream " + getName(), ex);
      }  
  }
  
  protected synchronized void flushBuffer(ByteBuffer buf) {
    buf.flip();
    try {
      if (buf.remaining() > 0)
        writeToDestination(buf.array(), buf.arrayOffset() + buf.position(), buf.remaining()); 
    } finally {
      buf.clear();
    } 
  }
  
  public synchronized void flush() {
    flushBuffer(this.byteBuffer);
    flushDestination();
  }
  
  protected synchronized boolean closeOutputStream() {
    flush();
    OutputStream stream = this.outputStream;
    if (stream == null || stream == System.out || stream == System.err)
      return true; 
    try {
      stream.close();
      LOGGER.debug("OutputStream closed");
    } catch (IOException ex) {
      logError("Unable to close stream", ex);
      return false;
    } 
    return true;
  }
  
  public ByteBuffer getByteBuffer() {
    return this.byteBuffer;
  }
  
  public ByteBuffer drain(ByteBuffer buf) {
    flushBuffer(buf);
    return buf;
  }
  
  public void writeBytes(ByteBuffer data) {
    if (data.remaining() == 0)
      return; 
    synchronized (this) {
      ByteBufferDestinationHelper.writeToUnsynchronized(data, this);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\OutputStreamManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */