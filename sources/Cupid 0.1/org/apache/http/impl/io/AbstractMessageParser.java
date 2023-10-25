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
public abstract class AbstractMessageParser<T extends HttpMessage> implements HttpMessageParser<T> {
  private static final int HEAD_LINE = 0;
  
  private static final int HEADERS = 1;
  
  private final SessionInputBuffer sessionBuffer;
  
  private final MessageConstraints messageConstraints;
  
  private final List<CharArrayBuffer> headerLines;
  
  protected final LineParser lineParser;
  
  private int state;
  
  private T message;
  
  @Deprecated
  public AbstractMessageParser(SessionInputBuffer buffer, LineParser parser, HttpParams params) {
    Args.notNull(buffer, "Session input buffer");
    Args.notNull(params, "HTTP parameters");
    this.sessionBuffer = buffer;
    this.messageConstraints = HttpParamConfig.getMessageConstraints(params);
    this.lineParser = (parser != null) ? parser : (LineParser)BasicLineParser.INSTANCE;
    this.headerLines = new ArrayList<CharArrayBuffer>();
    this.state = 0;
  }
  
  public AbstractMessageParser(SessionInputBuffer buffer, LineParser lineParser, MessageConstraints constraints) {
    this.sessionBuffer = (SessionInputBuffer)Args.notNull(buffer, "Session input buffer");
    this.lineParser = (lineParser != null) ? lineParser : (LineParser)BasicLineParser.INSTANCE;
    this.messageConstraints = (constraints != null) ? constraints : MessageConstraints.DEFAULT;
    this.headerLines = new ArrayList<CharArrayBuffer>();
    this.state = 0;
  }
  
  public static Header[] parseHeaders(SessionInputBuffer inbuffer, int maxHeaderCount, int maxLineLen, LineParser parser) throws HttpException, IOException {
    List<CharArrayBuffer> headerLines = new ArrayList<CharArrayBuffer>();
    return parseHeaders(inbuffer, maxHeaderCount, maxLineLen, (parser != null) ? parser : (LineParser)BasicLineParser.INSTANCE, headerLines);
  }
  
  public static Header[] parseHeaders(SessionInputBuffer inbuffer, int maxHeaderCount, int maxLineLen, LineParser parser, List<CharArrayBuffer> headerLines) throws HttpException, IOException {
    Args.notNull(inbuffer, "Session input buffer");
    Args.notNull(parser, "Line parser");
    Args.notNull(headerLines, "Header line list");
    CharArrayBuffer current = null;
    CharArrayBuffer previous = null;
    while (true) {
      if (current == null) {
        current = new CharArrayBuffer(64);
      } else {
        current.clear();
      } 
      int l = inbuffer.readLine(current);
      if (l == -1 || current.length() < 1)
        break; 
      if ((current.charAt(0) == ' ' || current.charAt(0) == '\t') && previous != null) {
        int j = 0;
        while (j < current.length()) {
          char ch = current.charAt(j);
          if (ch != ' ' && ch != '\t')
            break; 
          j++;
        } 
        if (maxLineLen > 0 && previous.length() + 1 + current.length() - j > maxLineLen)
          throw new MessageConstraintException("Maximum line length limit exceeded"); 
        previous.append(' ');
        previous.append(current, j, current.length() - j);
      } else {
        headerLines.add(current);
        previous = current;
        current = null;
      } 
      if (maxHeaderCount > 0 && headerLines.size() >= maxHeaderCount)
        throw new MessageConstraintException("Maximum header count exceeded"); 
    } 
    Header[] headers = new Header[headerLines.size()];
    for (int i = 0; i < headerLines.size(); i++) {
      CharArrayBuffer buffer = headerLines.get(i);
      try {
        headers[i] = parser.parseHeader(buffer);
      } catch (ParseException ex) {
        throw new ProtocolException(ex.getMessage());
      } 
    } 
    return headers;
  }
  
  protected abstract T parseHead(SessionInputBuffer paramSessionInputBuffer) throws IOException, HttpException, ParseException;
  
  public T parse() throws IOException, HttpException {
    Header[] headers;
    T result;
    int st = this.state;
    switch (st) {
      case 0:
        try {
          this.message = parseHead(this.sessionBuffer);
        } catch (ParseException px) {
          throw new ProtocolException(px.getMessage(), px);
        } 
        this.state = 1;
      case 1:
        headers = parseHeaders(this.sessionBuffer, this.messageConstraints.getMaxHeaderCount(), this.messageConstraints.getMaxLineLength(), this.lineParser, this.headerLines);
        this.message.setHeaders(headers);
        result = this.message;
        this.message = null;
        this.headerLines.clear();
        this.state = 0;
        return result;
    } 
    throw new IllegalStateException("Inconsistent parser state");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\io\AbstractMessageParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */