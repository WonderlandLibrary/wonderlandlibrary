package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Ascii;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.RoundingMode;
import java.util.Arrays;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

@Beta
@GwtCompatible(emulated = true)
public abstract class BaseEncoding {
  public static final class DecodingException extends IOException {
    DecodingException(String message) {
      super(message);
    }
    
    DecodingException(Throwable cause) {
      super(cause);
    }
  }
  
  public String encode(byte[] bytes) {
    return encode((byte[])Preconditions.checkNotNull(bytes), 0, bytes.length);
  }
  
  public final String encode(byte[] bytes, int off, int len) {
    Preconditions.checkNotNull(bytes);
    Preconditions.checkPositionIndexes(off, off + len, bytes.length);
    GwtWorkarounds.CharOutput result = GwtWorkarounds.stringBuilderOutput(maxEncodedSize(len));
    GwtWorkarounds.ByteOutput byteOutput = encodingStream(result);
    try {
      for (int i = 0; i < len; i++)
        byteOutput.write(bytes[off + i]); 
      byteOutput.close();
    } catch (IOException impossible) {
      throw new AssertionError("impossible");
    } 
    return result.toString();
  }
  
  @GwtIncompatible("Writer,OutputStream")
  public final OutputStream encodingStream(Writer writer) {
    return GwtWorkarounds.asOutputStream(encodingStream(GwtWorkarounds.asCharOutput(writer)));
  }
  
  @GwtIncompatible("ByteSink,CharSink")
  public final ByteSink encodingSink(final CharSink encodedSink) {
    Preconditions.checkNotNull(encodedSink);
    return new ByteSink() {
        public OutputStream openStream() throws IOException {
          return BaseEncoding.this.encodingStream(encodedSink.openStream());
        }
      };
  }
  
  private static byte[] extract(byte[] result, int length) {
    if (length == result.length)
      return result; 
    byte[] trunc = new byte[length];
    System.arraycopy(result, 0, trunc, 0, length);
    return trunc;
  }
  
  public final byte[] decode(CharSequence chars) {
    try {
      return decodeChecked(chars);
    } catch (DecodingException badInput) {
      throw new IllegalArgumentException(badInput);
    } 
  }
  
  final byte[] decodeChecked(CharSequence chars) throws DecodingException {
    chars = padding().trimTrailingFrom(chars);
    GwtWorkarounds.ByteInput decodedInput = decodingStream(GwtWorkarounds.asCharInput(chars));
    byte[] tmp = new byte[maxDecodedSize(chars.length())];
    int index = 0;
    try {
      int i;
      for (i = decodedInput.read(); i != -1; i = decodedInput.read())
        tmp[index++] = (byte)i; 
    } catch (DecodingException badInput) {
      throw badInput;
    } catch (IOException impossible) {
      throw new AssertionError(impossible);
    } 
    return extract(tmp, index);
  }
  
  @GwtIncompatible("Reader,InputStream")
  public final InputStream decodingStream(Reader reader) {
    return GwtWorkarounds.asInputStream(decodingStream(GwtWorkarounds.asCharInput(reader)));
  }
  
  @GwtIncompatible("ByteSource,CharSource")
  public final ByteSource decodingSource(final CharSource encodedSource) {
    Preconditions.checkNotNull(encodedSource);
    return new ByteSource() {
        public InputStream openStream() throws IOException {
          return BaseEncoding.this.decodingStream(encodedSource.openStream());
        }
      };
  }
  
  private static final BaseEncoding BASE64 = new StandardBaseEncoding("base64()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", Character.valueOf('='));
  
  abstract int maxEncodedSize(int paramInt);
  
  abstract GwtWorkarounds.ByteOutput encodingStream(GwtWorkarounds.CharOutput paramCharOutput);
  
  abstract int maxDecodedSize(int paramInt);
  
  abstract GwtWorkarounds.ByteInput decodingStream(GwtWorkarounds.CharInput paramCharInput);
  
  abstract CharMatcher padding();
  
  @CheckReturnValue
  public abstract BaseEncoding omitPadding();
  
  @CheckReturnValue
  public abstract BaseEncoding withPadChar(char paramChar);
  
  @CheckReturnValue
  public abstract BaseEncoding withSeparator(String paramString, int paramInt);
  
  @CheckReturnValue
  public abstract BaseEncoding upperCase();
  
  @CheckReturnValue
  public abstract BaseEncoding lowerCase();
  
  public static BaseEncoding base64() {
    return BASE64;
  }
  
  private static final BaseEncoding BASE64_URL = new StandardBaseEncoding("base64Url()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_", Character.valueOf('='));
  
  public static BaseEncoding base64Url() {
    return BASE64_URL;
  }
  
  private static final BaseEncoding BASE32 = new StandardBaseEncoding("base32()", "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567", Character.valueOf('='));
  
  public static BaseEncoding base32() {
    return BASE32;
  }
  
  private static final BaseEncoding BASE32_HEX = new StandardBaseEncoding("base32Hex()", "0123456789ABCDEFGHIJKLMNOPQRSTUV", Character.valueOf('='));
  
  public static BaseEncoding base32Hex() {
    return BASE32_HEX;
  }
  
  private static final BaseEncoding BASE16 = new StandardBaseEncoding("base16()", "0123456789ABCDEF", null);
  
  public static BaseEncoding base16() {
    return BASE16;
  }
  
  private static final class Alphabet extends CharMatcher {
    private final String name;
    
    private final char[] chars;
    
    final int mask;
    
    final int bitsPerChar;
    
    final int charsPerChunk;
    
    final int bytesPerChunk;
    
    private final byte[] decodabet;
    
    private final boolean[] validPadding;
    
    Alphabet(String name, char[] chars) {
      this.name = (String)Preconditions.checkNotNull(name);
      this.chars = (char[])Preconditions.checkNotNull(chars);
      try {
        this.bitsPerChar = IntMath.log2(chars.length, RoundingMode.UNNECESSARY);
      } catch (ArithmeticException e) {
        throw new IllegalArgumentException("Illegal alphabet length " + chars.length, e);
      } 
      int gcd = Math.min(8, Integer.lowestOneBit(this.bitsPerChar));
      this.charsPerChunk = 8 / gcd;
      this.bytesPerChunk = this.bitsPerChar / gcd;
      this.mask = chars.length - 1;
      byte[] decodabet = new byte[128];
      Arrays.fill(decodabet, (byte)-1);
      for (int i = 0; i < chars.length; i++) {
        char c = chars[i];
        Preconditions.checkArgument(CharMatcher.ASCII.matches(c), "Non-ASCII character: %s", new Object[] { Character.valueOf(c) });
        Preconditions.checkArgument((decodabet[c] == -1), "Duplicate character: %s", new Object[] { Character.valueOf(c) });
        decodabet[c] = (byte)i;
      } 
      this.decodabet = decodabet;
      boolean[] validPadding = new boolean[this.charsPerChunk];
      for (int j = 0; j < this.bytesPerChunk; j++)
        validPadding[IntMath.divide(j * 8, this.bitsPerChar, RoundingMode.CEILING)] = true; 
      this.validPadding = validPadding;
    }
    
    char encode(int bits) {
      return this.chars[bits];
    }
    
    boolean isValidPaddingStartPosition(int index) {
      return this.validPadding[index % this.charsPerChunk];
    }
    
    int decode(char ch) throws IOException {
      if (ch > '' || this.decodabet[ch] == -1)
        throw new BaseEncoding.DecodingException("Unrecognized character: " + ch); 
      return this.decodabet[ch];
    }
    
    private boolean hasLowerCase() {
      for (char c : this.chars) {
        if (Ascii.isLowerCase(c))
          return true; 
      } 
      return false;
    }
    
    private boolean hasUpperCase() {
      for (char c : this.chars) {
        if (Ascii.isUpperCase(c))
          return true; 
      } 
      return false;
    }
    
    Alphabet upperCase() {
      if (!hasLowerCase())
        return this; 
      Preconditions.checkState(!hasUpperCase(), "Cannot call upperCase() on a mixed-case alphabet");
      char[] upperCased = new char[this.chars.length];
      for (int i = 0; i < this.chars.length; i++)
        upperCased[i] = Ascii.toUpperCase(this.chars[i]); 
      return new Alphabet(this.name + ".upperCase()", upperCased);
    }
    
    Alphabet lowerCase() {
      if (!hasUpperCase())
        return this; 
      Preconditions.checkState(!hasLowerCase(), "Cannot call lowerCase() on a mixed-case alphabet");
      char[] lowerCased = new char[this.chars.length];
      for (int i = 0; i < this.chars.length; i++)
        lowerCased[i] = Ascii.toLowerCase(this.chars[i]); 
      return new Alphabet(this.name + ".lowerCase()", lowerCased);
    }
    
    public boolean matches(char c) {
      return (CharMatcher.ASCII.matches(c) && this.decodabet[c] != -1);
    }
    
    public String toString() {
      return this.name;
    }
  }
  
  static final class StandardBaseEncoding extends BaseEncoding {
    private final BaseEncoding.Alphabet alphabet;
    
    @Nullable
    private final Character paddingChar;
    
    private transient BaseEncoding upperCase;
    
    private transient BaseEncoding lowerCase;
    
    StandardBaseEncoding(String name, String alphabetChars, @Nullable Character paddingChar) {
      this(new BaseEncoding.Alphabet(name, alphabetChars.toCharArray()), paddingChar);
    }
    
    StandardBaseEncoding(BaseEncoding.Alphabet alphabet, @Nullable Character paddingChar) {
      this.alphabet = (BaseEncoding.Alphabet)Preconditions.checkNotNull(alphabet);
      Preconditions.checkArgument((paddingChar == null || !alphabet.matches(paddingChar.charValue())), "Padding character %s was already in alphabet", new Object[] { paddingChar });
      this.paddingChar = paddingChar;
    }
    
    CharMatcher padding() {
      return (this.paddingChar == null) ? CharMatcher.NONE : CharMatcher.is(this.paddingChar.charValue());
    }
    
    int maxEncodedSize(int bytes) {
      return this.alphabet.charsPerChunk * IntMath.divide(bytes, this.alphabet.bytesPerChunk, RoundingMode.CEILING);
    }
    
    GwtWorkarounds.ByteOutput encodingStream(final GwtWorkarounds.CharOutput out) {
      Preconditions.checkNotNull(out);
      return new GwtWorkarounds.ByteOutput() {
          int bitBuffer = 0;
          
          int bitBufferLength = 0;
          
          int writtenChars = 0;
          
          public void write(byte b) throws IOException {
            this.bitBuffer <<= 8;
            this.bitBuffer |= b & 0xFF;
            this.bitBufferLength += 8;
            while (this.bitBufferLength >= BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar) {
              int charIndex = this.bitBuffer >> this.bitBufferLength - BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar & BaseEncoding.StandardBaseEncoding.this.alphabet.mask;
              out.write(BaseEncoding.StandardBaseEncoding.this.alphabet.encode(charIndex));
              this.writtenChars++;
              this.bitBufferLength -= BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar;
            } 
          }
          
          public void flush() throws IOException {
            out.flush();
          }
          
          public void close() throws IOException {
            if (this.bitBufferLength > 0) {
              int charIndex = this.bitBuffer << BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar - this.bitBufferLength & BaseEncoding.StandardBaseEncoding.this.alphabet.mask;
              out.write(BaseEncoding.StandardBaseEncoding.this.alphabet.encode(charIndex));
              this.writtenChars++;
              if (BaseEncoding.StandardBaseEncoding.this.paddingChar != null)
                while (this.writtenChars % BaseEncoding.StandardBaseEncoding.this.alphabet.charsPerChunk != 0) {
                  out.write(BaseEncoding.StandardBaseEncoding.this.paddingChar.charValue());
                  this.writtenChars++;
                }  
            } 
            out.close();
          }
        };
    }
    
    int maxDecodedSize(int chars) {
      return (int)((this.alphabet.bitsPerChar * chars + 7L) / 8L);
    }
    
    GwtWorkarounds.ByteInput decodingStream(final GwtWorkarounds.CharInput reader) {
      Preconditions.checkNotNull(reader);
      return new GwtWorkarounds.ByteInput() {
          int bitBuffer = 0;
          
          int bitBufferLength = 0;
          
          int readChars = 0;
          
          boolean hitPadding = false;
          
          final CharMatcher paddingMatcher = BaseEncoding.StandardBaseEncoding.this.padding();
          
          public int read() throws IOException {
            while (true) {
              int readChar = reader.read();
              if (readChar == -1) {
                if (!this.hitPadding && !BaseEncoding.StandardBaseEncoding.this.alphabet.isValidPaddingStartPosition(this.readChars))
                  throw new BaseEncoding.DecodingException("Invalid input length " + this.readChars); 
                return -1;
              } 
              this.readChars++;
              char ch = (char)readChar;
              if (this.paddingMatcher.matches(ch)) {
                if (!this.hitPadding && (this.readChars == 1 || !BaseEncoding.StandardBaseEncoding.this.alphabet.isValidPaddingStartPosition(this.readChars - 1)))
                  throw new BaseEncoding.DecodingException("Padding cannot start at index " + this.readChars); 
                this.hitPadding = true;
                continue;
              } 
              if (this.hitPadding)
                throw new BaseEncoding.DecodingException("Expected padding character but found '" + ch + "' at index " + this.readChars); 
              this.bitBuffer <<= BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar;
              this.bitBuffer |= BaseEncoding.StandardBaseEncoding.this.alphabet.decode(ch);
              this.bitBufferLength += BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar;
              if (this.bitBufferLength >= 8) {
                this.bitBufferLength -= 8;
                return this.bitBuffer >> this.bitBufferLength & 0xFF;
              } 
            } 
          }
          
          public void close() throws IOException {
            reader.close();
          }
        };
    }
    
    public BaseEncoding omitPadding() {
      return (this.paddingChar == null) ? this : new StandardBaseEncoding(this.alphabet, null);
    }
    
    public BaseEncoding withPadChar(char padChar) {
      if (8 % this.alphabet.bitsPerChar == 0 || (this.paddingChar != null && this.paddingChar.charValue() == padChar))
        return this; 
      return new StandardBaseEncoding(this.alphabet, Character.valueOf(padChar));
    }
    
    public BaseEncoding withSeparator(String separator, int afterEveryChars) {
      Preconditions.checkNotNull(separator);
      Preconditions.checkArgument(padding().or(this.alphabet).matchesNoneOf(separator), "Separator cannot contain alphabet or padding characters");
      return new BaseEncoding.SeparatedBaseEncoding(this, separator, afterEveryChars);
    }
    
    public BaseEncoding upperCase() {
      BaseEncoding result = this.upperCase;
      if (result == null) {
        BaseEncoding.Alphabet upper = this.alphabet.upperCase();
        result = this.upperCase = (upper == this.alphabet) ? this : new StandardBaseEncoding(upper, this.paddingChar);
      } 
      return result;
    }
    
    public BaseEncoding lowerCase() {
      BaseEncoding result = this.lowerCase;
      if (result == null) {
        BaseEncoding.Alphabet lower = this.alphabet.lowerCase();
        result = this.lowerCase = (lower == this.alphabet) ? this : new StandardBaseEncoding(lower, this.paddingChar);
      } 
      return result;
    }
    
    public String toString() {
      StringBuilder builder = new StringBuilder("BaseEncoding.");
      builder.append(this.alphabet.toString());
      if (8 % this.alphabet.bitsPerChar != 0)
        if (this.paddingChar == null) {
          builder.append(".omitPadding()");
        } else {
          builder.append(".withPadChar(").append(this.paddingChar).append(')');
        }  
      return builder.toString();
    }
  }
  
  static GwtWorkarounds.CharInput ignoringInput(final GwtWorkarounds.CharInput delegate, final CharMatcher toIgnore) {
    Preconditions.checkNotNull(delegate);
    Preconditions.checkNotNull(toIgnore);
    return new GwtWorkarounds.CharInput() {
        public int read() throws IOException {
          int readChar;
          do {
            readChar = delegate.read();
          } while (readChar != -1 && toIgnore.matches((char)readChar));
          return readChar;
        }
        
        public void close() throws IOException {
          delegate.close();
        }
      };
  }
  
  static GwtWorkarounds.CharOutput separatingOutput(final GwtWorkarounds.CharOutput delegate, final String separator, final int afterEveryChars) {
    Preconditions.checkNotNull(delegate);
    Preconditions.checkNotNull(separator);
    Preconditions.checkArgument((afterEveryChars > 0));
    return new GwtWorkarounds.CharOutput() {
        int charsUntilSeparator = afterEveryChars;
        
        public void write(char c) throws IOException {
          if (this.charsUntilSeparator == 0) {
            for (int i = 0; i < separator.length(); i++)
              delegate.write(separator.charAt(i)); 
            this.charsUntilSeparator = afterEveryChars;
          } 
          delegate.write(c);
          this.charsUntilSeparator--;
        }
        
        public void flush() throws IOException {
          delegate.flush();
        }
        
        public void close() throws IOException {
          delegate.close();
        }
      };
  }
  
  static final class SeparatedBaseEncoding extends BaseEncoding {
    private final BaseEncoding delegate;
    
    private final String separator;
    
    private final int afterEveryChars;
    
    private final CharMatcher separatorChars;
    
    SeparatedBaseEncoding(BaseEncoding delegate, String separator, int afterEveryChars) {
      this.delegate = (BaseEncoding)Preconditions.checkNotNull(delegate);
      this.separator = (String)Preconditions.checkNotNull(separator);
      this.afterEveryChars = afterEveryChars;
      Preconditions.checkArgument((afterEveryChars > 0), "Cannot add a separator after every %s chars", new Object[] { Integer.valueOf(afterEveryChars) });
      this.separatorChars = CharMatcher.anyOf(separator).precomputed();
    }
    
    CharMatcher padding() {
      return this.delegate.padding();
    }
    
    int maxEncodedSize(int bytes) {
      int unseparatedSize = this.delegate.maxEncodedSize(bytes);
      return unseparatedSize + this.separator.length() * IntMath.divide(Math.max(0, unseparatedSize - 1), this.afterEveryChars, RoundingMode.FLOOR);
    }
    
    GwtWorkarounds.ByteOutput encodingStream(GwtWorkarounds.CharOutput output) {
      return this.delegate.encodingStream(separatingOutput(output, this.separator, this.afterEveryChars));
    }
    
    int maxDecodedSize(int chars) {
      return this.delegate.maxDecodedSize(chars);
    }
    
    GwtWorkarounds.ByteInput decodingStream(GwtWorkarounds.CharInput input) {
      return this.delegate.decodingStream(ignoringInput(input, this.separatorChars));
    }
    
    public BaseEncoding omitPadding() {
      return this.delegate.omitPadding().withSeparator(this.separator, this.afterEveryChars);
    }
    
    public BaseEncoding withPadChar(char padChar) {
      return this.delegate.withPadChar(padChar).withSeparator(this.separator, this.afterEveryChars);
    }
    
    public BaseEncoding withSeparator(String separator, int afterEveryChars) {
      throw new UnsupportedOperationException("Already have a separator");
    }
    
    public BaseEncoding upperCase() {
      return this.delegate.upperCase().withSeparator(this.separator, this.afterEveryChars);
    }
    
    public BaseEncoding lowerCase() {
      return this.delegate.lowerCase().withSeparator(this.separator, this.afterEveryChars);
    }
    
    public String toString() {
      return this.delegate.toString() + ".withSeparator(\"" + this.separator + "\", " + this.afterEveryChars + ")";
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\io\BaseEncoding.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */