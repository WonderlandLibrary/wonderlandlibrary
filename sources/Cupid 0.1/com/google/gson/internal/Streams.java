package com.google.gson.internal;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import java.io.Writer;

public final class Streams {
  public static JsonElement parse(JsonReader reader) throws JsonParseException {
    boolean isEmpty = true;
    try {
      reader.peek();
      isEmpty = false;
      return (JsonElement)TypeAdapters.JSON_ELEMENT.read(reader);
    } catch (EOFException e) {
      if (isEmpty)
        return (JsonElement)JsonNull.INSTANCE; 
      throw new JsonSyntaxException(e);
    } catch (MalformedJsonException e) {
      throw new JsonSyntaxException(e);
    } catch (IOException e) {
      throw new JsonIOException(e);
    } catch (NumberFormatException e) {
      throw new JsonSyntaxException(e);
    } 
  }
  
  public static void write(JsonElement element, JsonWriter writer) throws IOException {
    TypeAdapters.JSON_ELEMENT.write(writer, element);
  }
  
  public static Writer writerForAppendable(Appendable appendable) {
    return (appendable instanceof Writer) ? (Writer)appendable : new AppendableWriter(appendable);
  }
  
  private static final class AppendableWriter extends Writer {
    private final Appendable appendable;
    
    private final CurrentWrite currentWrite = new CurrentWrite();
    
    private AppendableWriter(Appendable appendable) {
      this.appendable = appendable;
    }
    
    public void write(char[] chars, int offset, int length) throws IOException {
      this.currentWrite.chars = chars;
      this.appendable.append(this.currentWrite, offset, offset + length);
    }
    
    public void write(int i) throws IOException {
      this.appendable.append((char)i);
    }
    
    public void flush() {}
    
    public void close() {}
    
    static class CurrentWrite implements CharSequence {
      char[] chars;
      
      public int length() {
        return this.chars.length;
      }
      
      public char charAt(int i) {
        return this.chars[i];
      }
      
      public CharSequence subSequence(int start, int end) {
        return new String(this.chars, start, end - start);
      }
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\gson\internal\Streams.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */