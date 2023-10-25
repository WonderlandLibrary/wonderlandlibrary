package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

public abstract class CharSink implements OutputSupplier<Writer> {
  @Deprecated
  public final Writer getOutput() throws IOException {
    return openStream();
  }
  
  public Writer openBufferedStream() throws IOException {
    Writer writer = openStream();
    return (writer instanceof BufferedWriter) ? writer : new BufferedWriter(writer);
  }
  
  public void write(CharSequence charSequence) throws IOException {
    Preconditions.checkNotNull(charSequence);
    Closer closer = Closer.create();
    try {
      Writer out = closer.<Writer>register(openStream());
      out.append(charSequence);
      out.flush();
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    } 
  }
  
  public void writeLines(Iterable<? extends CharSequence> lines) throws IOException {
    writeLines(lines, System.getProperty("line.separator"));
  }
  
  public void writeLines(Iterable<? extends CharSequence> lines, String lineSeparator) throws IOException {
    Preconditions.checkNotNull(lines);
    Preconditions.checkNotNull(lineSeparator);
    Closer closer = Closer.create();
    try {
      Writer out = closer.<Writer>register(openBufferedStream());
      for (CharSequence line : lines)
        out.append(line).append(lineSeparator); 
      out.flush();
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    } 
  }
  
  public long writeFrom(Readable readable) throws IOException {
    Preconditions.checkNotNull(readable);
    Closer closer = Closer.create();
    try {
      Writer out = closer.<Writer>register(openStream());
      long written = CharStreams.copy(readable, out);
      out.flush();
      return written;
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    } 
  }
  
  public abstract Writer openStream() throws IOException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\io\CharSink.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */