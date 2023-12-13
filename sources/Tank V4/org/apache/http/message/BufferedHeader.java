package org.apache.http.message;

import java.io.Serializable;
import org.apache.http.FormattedHeader;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class BufferedHeader implements FormattedHeader, Cloneable, Serializable {
   private static final long serialVersionUID = -2768352615787625448L;
   private final String name;
   private final CharArrayBuffer buffer;
   private final int valuePos;

   public BufferedHeader(CharArrayBuffer var1) throws ParseException {
      Args.notNull(var1, "Char array buffer");
      int var2 = var1.indexOf(58);
      if (var2 == -1) {
         throw new ParseException("Invalid header: " + var1.toString());
      } else {
         String var3 = var1.substringTrimmed(0, var2);
         if (var3.length() == 0) {
            throw new ParseException("Invalid header: " + var1.toString());
         } else {
            this.buffer = var1;
            this.name = var3;
            this.valuePos = var2 + 1;
         }
      }
   }

   public String getName() {
      return this.name;
   }

   public String getValue() {
      return this.buffer.substringTrimmed(this.valuePos, this.buffer.length());
   }

   public HeaderElement[] getElements() throws ParseException {
      ParserCursor var1 = new ParserCursor(0, this.buffer.length());
      var1.updatePos(this.valuePos);
      return BasicHeaderValueParser.INSTANCE.parseElements(this.buffer, var1);
   }

   public int getValuePos() {
      return this.valuePos;
   }

   public CharArrayBuffer getBuffer() {
      return this.buffer;
   }

   public String toString() {
      return this.buffer.toString();
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }
}
