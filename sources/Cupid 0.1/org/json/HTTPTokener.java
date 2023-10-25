package org.json;

public class HTTPTokener extends JSONTokener {
  public HTTPTokener(String string) {
    super(string);
  }
  
  public String nextToken() throws JSONException {
    StringBuilder sb = new StringBuilder();
    while (true) {
      char c = next();
      if (!Character.isWhitespace(c)) {
        if (c == '"' || c == '\'') {
          char q = c;
          while (true) {
            c = next();
            if (c < ' ')
              throw syntaxError("Unterminated string."); 
            if (c == q)
              return sb.toString(); 
            sb.append(c);
          } 
          break;
        } 
        while (true) {
          if (c == '\000' || Character.isWhitespace(c))
            return sb.toString(); 
          sb.append(c);
          c = next();
        } 
        break;
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\json\HTTPTokener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */