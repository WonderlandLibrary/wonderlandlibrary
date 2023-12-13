package org.json;

public class HTTPTokener extends JSONTokener {
   public HTTPTokener(String string) {
      super(string);
   }

   public String nextToken() throws JSONException {
      StringBuilder sb = new StringBuilder();

      char c;
      do {
         c = this.next();
      } while(Character.isWhitespace(c));

      if (c != '"' && c != '\'') {
         while(c != 0 && !Character.isWhitespace(c)) {
            sb.append(c);
            c = this.next();
         }

         return sb.toString();
      } else {
         char q = c;

         while(true) {
            c = this.next();
            if (c < ' ') {
               throw this.syntaxError("Unterminated string.");
            }

            if (c == q) {
               return sb.toString();
            }

            sb.append(c);
         }
      }
   }
}
