package com.google.zxing.client.result;

import java.util.regex.Pattern;

public final class URIParsedResult extends ParsedResult {
   private static final Pattern USER_IN_HOST = Pattern.compile(":/*([^/@]+)@[^/]+");
   private final String uri;
   private final String title;

   public URIParsedResult(String uri, String title) {
      super(ParsedResultType.URI);
      this.uri = massageURI(uri);
      this.title = title;
   }

   public String getURI() {
      return this.uri;
   }

   public String getTitle() {
      return this.title;
   }

   public boolean isPossiblyMaliciousURI() {
      return USER_IN_HOST.matcher(this.uri).find();
   }

   public String getDisplayResult() {
      StringBuilder result = new StringBuilder(30);
      maybeAppend(this.title, result);
      maybeAppend(this.uri, result);
      return result.toString();
   }

   private static String massageURI(String uri) {
      uri = uri.trim();
      int protocolEnd = uri.indexOf(58);
      if (protocolEnd < 0) {
         uri = "http://" + uri;
      } else if (isColonFollowedByPortNumber(uri, protocolEnd)) {
         uri = "http://" + uri;
      }

      return uri;
   }

   private static boolean isColonFollowedByPortNumber(String uri, int protocolEnd) {
      int nextSlash = uri.indexOf(47, protocolEnd + 1);
      if (nextSlash < 0) {
         nextSlash = uri.length();
      }

      if (nextSlash <= protocolEnd + 1) {
         return false;
      } else {
         for(int x = protocolEnd + 1; x < nextSlash; ++x) {
            if (uri.charAt(x) < '0' || uri.charAt(x) > '9') {
               return false;
            }
         }

         return true;
      }
   }
}
