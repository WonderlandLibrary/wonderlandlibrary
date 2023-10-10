package org.apache.http.impl.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.MessageConstraintException;
import org.apache.http.ParseException;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.config.MessageConstraints;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;
import org.apache.http.params.HttpParamConfig;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public abstract class AbstractMessageParser implements HttpMessageParser {
   private static final int HEAD_LINE = 0;
   private static final int HEADERS = 1;
   private final SessionInputBuffer sessionBuffer;
   private final MessageConstraints messageConstraints;
   private final List headerLines;
   protected final LineParser lineParser;
   private int state;
   private HttpMessage message;

   /** @deprecated */
   @Deprecated
   public AbstractMessageParser(SessionInputBuffer var1, LineParser var2, HttpParams var3) {
      Args.notNull(var1, "Session input buffer");
      Args.notNull(var3, "HTTP parameters");
      this.sessionBuffer = var1;
      this.messageConstraints = HttpParamConfig.getMessageConstraints(var3);
      this.lineParser = (LineParser)(var2 != null ? var2 : BasicLineParser.INSTANCE);
      this.headerLines = new ArrayList();
      this.state = 0;
   }

   public AbstractMessageParser(SessionInputBuffer var1, LineParser var2, MessageConstraints var3) {
      this.sessionBuffer = (SessionInputBuffer)Args.notNull(var1, "Session input buffer");
      this.lineParser = (LineParser)(var2 != null ? var2 : BasicLineParser.INSTANCE);
      this.messageConstraints = var3 != null ? var3 : MessageConstraints.DEFAULT;
      this.headerLines = new ArrayList();
      this.state = 0;
   }

   public static Header[] parseHeaders(SessionInputBuffer var0, int var1, int var2, LineParser var3) throws HttpException, IOException {
      ArrayList var4 = new ArrayList();
      return parseHeaders(var0, var1, var2, (LineParser)(var3 != null ? var3 : BasicLineParser.INSTANCE), var4);
   }

   public static Header[] parseHeaders(SessionInputBuffer var0, int var1, int var2, LineParser var3, List var4) throws HttpException, IOException {
      Args.notNull(var0, "Session input buffer");
      Args.notNull(var3, "Line parser");
      Args.notNull(var4, "Header line list");
      CharArrayBuffer var5 = null;
      CharArrayBuffer var6 = null;

      do {
         if (var5 == null) {
            var5 = new CharArrayBuffer(64);
         } else {
            var5.clear();
         }

         int var7 = var0.readLine(var5);
         int var8;
         if (var7 == -1 || var5.length() < 1) {
            Header[] var12 = new Header[var4.size()];

            for(var8 = 0; var8 < var4.size(); ++var8) {
               CharArrayBuffer var13 = (CharArrayBuffer)var4.get(var8);

               try {
                  var12[var8] = var3.parseHeader(var13);
               } catch (ParseException var11) {
                  throw new ProtocolException(var11.getMessage());
               }
            }

            return var12;
         }

         if ((var5.charAt(0) == ' ' || var5.charAt(0) == '\t') && var6 != null) {
            for(var8 = 0; var8 < var5.length(); ++var8) {
               char var9 = var5.charAt(var8);
               if (var9 != ' ' && var9 != '\t') {
                  break;
               }
            }

            if (var2 > 0 && var6.length() + 1 + var5.length() - var8 > var2) {
               throw new MessageConstraintException("Maximum line length limit exceeded");
            }

            var6.append(' ');
            var6.append(var5, var8, var5.length() - var8);
         } else {
            var4.add(var5);
            var6 = var5;
            var5 = null;
         }
      } while(var1 <= 0 || var4.size() < var1);

      throw new MessageConstraintException("Maximum header count exceeded");
   }

   protected abstract HttpMessage parseHead(SessionInputBuffer var1) throws IOException, HttpException, ParseException;

   public HttpMessage parse() throws IOException, HttpException {
      int var1 = this.state;
      switch(var1) {
      case 0:
         try {
            this.message = this.parseHead(this.sessionBuffer);
         } catch (ParseException var4) {
            throw new ProtocolException(var4.getMessage(), var4);
         }

         this.state = 1;
      case 1:
         Header[] var2 = parseHeaders(this.sessionBuffer, this.messageConstraints.getMaxHeaderCount(), this.messageConstraints.getMaxLineLength(), this.lineParser, this.headerLines);
         this.message.setHeaders(var2);
         HttpMessage var3 = this.message;
         this.message = null;
         this.headerLines.clear();
         this.state = 0;
         return var3;
      default:
         throw new IllegalStateException("Inconsistent parser state");
      }
   }
}
