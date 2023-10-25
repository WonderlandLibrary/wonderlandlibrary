package org.apache.http.impl.cookie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.http.annotation.Immutable;

@Immutable
public class PublicSuffixListParser {
  private static final int MAX_LINE_LEN = 256;
  
  private final PublicSuffixFilter filter;
  
  PublicSuffixListParser(PublicSuffixFilter filter) {
    this.filter = filter;
  }
  
  public void parse(Reader list) throws IOException {
    Collection<String> rules = new ArrayList<String>();
    Collection<String> exceptions = new ArrayList<String>();
    BufferedReader r = new BufferedReader(list);
    StringBuilder sb = new StringBuilder(256);
    boolean more = true;
    while (more) {
      more = readLine(r, sb);
      String line = sb.toString();
      if (line.length() == 0)
        continue; 
      if (line.startsWith("//"))
        continue; 
      if (line.startsWith("."))
        line = line.substring(1); 
      boolean isException = line.startsWith("!");
      if (isException)
        line = line.substring(1); 
      if (isException) {
        exceptions.add(line);
        continue;
      } 
      rules.add(line);
    } 
    this.filter.setPublicSuffixes(rules);
    this.filter.setExceptions(exceptions);
  }
  
  private boolean readLine(Reader r, StringBuilder sb) throws IOException {
    sb.setLength(0);
    boolean hitWhitespace = false;
    int b;
    while ((b = r.read()) != -1) {
      char c = (char)b;
      if (c == '\n')
        break; 
      if (Character.isWhitespace(c))
        hitWhitespace = true; 
      if (!hitWhitespace)
        sb.append(c); 
      if (sb.length() > 256)
        throw new IOException("Line too long"); 
    } 
    return (b != -1);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\cookie\PublicSuffixListParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */