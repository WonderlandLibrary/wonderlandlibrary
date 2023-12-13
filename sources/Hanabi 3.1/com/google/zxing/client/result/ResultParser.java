package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class ResultParser {
   private static final ResultParser[] PARSERS = new ResultParser[]{new BookmarkDoCoMoResultParser(), new AddressBookDoCoMoResultParser(), new EmailDoCoMoResultParser(), new AddressBookAUResultParser(), new VCardResultParser(), new BizcardResultParser(), new VEventResultParser(), new EmailAddressResultParser(), new SMTPResultParser(), new TelResultParser(), new SMSMMSResultParser(), new SMSTOMMSTOResultParser(), new GeoResultParser(), new WifiResultParser(), new URLTOResultParser(), new URIResultParser(), new ISBNResultParser(), new ProductResultParser(), new ExpandedProductResultParser()};
   private static final Pattern DIGITS = Pattern.compile("\\d*");
   private static final Pattern ALPHANUM = Pattern.compile("[a-zA-Z0-9]*");
   private static final Pattern AMPERSAND = Pattern.compile("&");
   private static final Pattern EQUALS = Pattern.compile("=");
   private static final String BYTE_ORDER_MARK = "\ufeff";

   public abstract ParsedResult parse(Result var1);

   protected static String getMassagedText(Result result) {
      String text = result.getText();
      if (text.startsWith("\ufeff")) {
         text = text.substring(1);
      }

      return text;
   }

   public static ParsedResult parseResult(Result theResult) {
      ResultParser[] arr$ = PARSERS;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ResultParser parser = arr$[i$];
         ParsedResult result = parser.parse(theResult);
         if (result != null) {
            return result;
         }
      }

      return new TextParsedResult(theResult.getText(), (String)null);
   }

   protected static void maybeAppend(String value, StringBuilder result) {
      if (value != null) {
         result.append('\n');
         result.append(value);
      }

   }

   protected static void maybeAppend(String[] value, StringBuilder result) {
      if (value != null) {
         String[] arr$ = value;
         int len$ = value.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String s = arr$[i$];
            result.append('\n');
            result.append(s);
         }
      }

   }

   protected static String[] maybeWrap(String value) {
      return value == null ? null : new String[]{value};
   }

   protected static String unescapeBackslash(String escaped) {
      int backslash = escaped.indexOf(92);
      if (backslash < 0) {
         return escaped;
      } else {
         int max = escaped.length();
         StringBuilder unescaped = new StringBuilder(max - 1);
         unescaped.append(escaped.toCharArray(), 0, backslash);
         boolean nextIsEscaped = false;

         for(int i = backslash; i < max; ++i) {
            char c = escaped.charAt(i);
            if (!nextIsEscaped && c == '\\') {
               nextIsEscaped = true;
            } else {
               unescaped.append(c);
               nextIsEscaped = false;
            }
         }

         return unescaped.toString();
      }
   }

   protected static int parseHexDigit(char c) {
      if (c >= '0' && c <= '9') {
         return c - 48;
      } else if (c >= 'a' && c <= 'f') {
         return 10 + (c - 97);
      } else {
         return c >= 'A' && c <= 'F' ? 10 + (c - 65) : -1;
      }
   }

   protected static boolean isStringOfDigits(CharSequence value, int length) {
      return value != null && length == value.length() && DIGITS.matcher(value).matches();
   }

   protected static boolean isSubstringOfDigits(CharSequence value, int offset, int length) {
      if (value == null) {
         return false;
      } else {
         int max = offset + length;
         return value.length() >= max && DIGITS.matcher(value.subSequence(offset, max)).matches();
      }
   }

   protected static boolean isSubstringOfAlphaNumeric(CharSequence value, int offset, int length) {
      if (value == null) {
         return false;
      } else {
         int max = offset + length;
         return value.length() >= max && ALPHANUM.matcher(value.subSequence(offset, max)).matches();
      }
   }

   static Map parseNameValuePairs(String uri) {
      int paramStart = uri.indexOf(63);
      if (paramStart < 0) {
         return null;
      } else {
         Map result = new HashMap(3);
         String[] arr$ = AMPERSAND.split(uri.substring(paramStart + 1));
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String keyValue = arr$[i$];
            appendKeyValue(keyValue, result);
         }

         return result;
      }
   }

   private static void appendKeyValue(CharSequence keyValue, Map result) {
      String[] keyValueTokens = EQUALS.split(keyValue, 2);
      if (keyValueTokens.length == 2) {
         String key = keyValueTokens[0];
         String value = keyValueTokens[1];

         try {
            value = URLDecoder.decode(value, "UTF-8");
            result.put(key, value);
         } catch (UnsupportedEncodingException var6) {
            throw new IllegalStateException(var6);
         } catch (IllegalArgumentException var7) {
         }
      }

   }

   static String[] matchPrefixedField(String prefix, String rawText, char endChar, boolean trim) {
      List matches = null;
      int i = 0;
      int max = rawText.length();

      while(i < max) {
         i = rawText.indexOf(prefix, i);
         if (i < 0) {
            break;
         }

         i += prefix.length();
         int start = i;
         boolean more = true;

         while(more) {
            i = rawText.indexOf(endChar, i);
            if (i < 0) {
               i = rawText.length();
               more = false;
            } else if (rawText.charAt(i - 1) == '\\') {
               ++i;
            } else {
               if (matches == null) {
                  matches = new ArrayList(3);
               }

               String element = unescapeBackslash(rawText.substring(start, i));
               if (trim) {
                  element = element.trim();
               }

               matches.add(element);
               ++i;
               more = false;
            }
         }
      }

      return matches != null && !matches.isEmpty() ? (String[])matches.toArray(new String[matches.size()]) : null;
   }

   static String matchSinglePrefixedField(String prefix, String rawText, char endChar, boolean trim) {
      String[] matches = matchPrefixedField(prefix, rawText, endChar, trim);
      return matches == null ? null : matches[0];
   }
}
