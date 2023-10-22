package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class VCardResultParser extends ResultParser {
   private static final Pattern BEGIN_VCARD = Pattern.compile("BEGIN:VCARD", 2);
   private static final Pattern VCARD_LIKE_DATE = Pattern.compile("\\d{4}-?\\d{2}-?\\d{2}");
   private static final Pattern CR_LF_SPACE_TAB = Pattern.compile("\r\n[ \t]");
   private static final Pattern NEWLINE_ESCAPE = Pattern.compile("\\\\[nN]");
   private static final Pattern VCARD_ESCAPES = Pattern.compile("\\\\([,;\\\\])");
   private static final Pattern EQUALS = Pattern.compile("=");
   private static final Pattern SEMICOLON = Pattern.compile(";");
   private static final Pattern UNESCAPED_SEMICOLONS = Pattern.compile("(?<!\\\\);+");

   public AddressBookParsedResult parse(Result result) {
      String rawText = getMassagedText(result);
      Matcher m = BEGIN_VCARD.matcher(rawText);
      if (m.find() && m.start() == 0) {
         List names = matchVCardPrefixedField("FN", rawText, true, false);
         if (names == null) {
            names = matchVCardPrefixedField("N", rawText, true, false);
            formatNames(names);
         }

         List phoneNumbers = matchVCardPrefixedField("TEL", rawText, true, false);
         List emails = matchVCardPrefixedField("EMAIL", rawText, true, false);
         List note = matchSingleVCardPrefixedField("NOTE", rawText, false, false);
         List addresses = matchVCardPrefixedField("ADR", rawText, true, true);
         List org = matchSingleVCardPrefixedField("ORG", rawText, true, true);
         List birthday = matchSingleVCardPrefixedField("BDAY", rawText, true, false);
         if (birthday != null && !isLikeVCardDate((CharSequence)birthday.get(0))) {
            birthday = null;
         }

         List title = matchSingleVCardPrefixedField("TITLE", rawText, true, false);
         List url = matchSingleVCardPrefixedField("URL", rawText, true, false);
         List instantMessenger = matchSingleVCardPrefixedField("IMPP", rawText, true, false);
         return new AddressBookParsedResult(toPrimaryValues(names), (String)null, toPrimaryValues(phoneNumbers), toTypes(phoneNumbers), toPrimaryValues(emails), toTypes(emails), toPrimaryValue(instantMessenger), toPrimaryValue(note), toPrimaryValues(addresses), toTypes(addresses), toPrimaryValue(org), toPrimaryValue(birthday), toPrimaryValue(title), toPrimaryValue(url));
      } else {
         return null;
      }
   }

   static List matchVCardPrefixedField(CharSequence prefix, String rawText, boolean trim, boolean parseFieldDivider) {
      List matches = null;
      int i = 0;
      int max = rawText.length();

      while(i < max) {
         Matcher matcher = Pattern.compile("(?:^|\n)" + prefix + "(?:;([^:]*))?:", 2).matcher(rawText);
         if (i > 0) {
            --i;
         }

         if (!matcher.find(i)) {
            break;
         }

         i = matcher.end(0);
         String metadataString = matcher.group(1);
         List metadata = null;
         boolean quotedPrintable = false;
         String quotedPrintableCharset = null;
         if (metadataString != null) {
            String[] arr$ = SEMICOLON.split(metadataString);
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               String metadatum = arr$[i$];
               if (metadata == null) {
                  metadata = new ArrayList(1);
               }

               metadata.add(metadatum);
               String[] metadatumTokens = EQUALS.split(metadatum, 2);
               if (metadatumTokens.length > 1) {
                  String key = metadatumTokens[0];
                  String value = metadatumTokens[1];
                  if ("ENCODING".equalsIgnoreCase(key) && "QUOTED-PRINTABLE".equalsIgnoreCase(value)) {
                     quotedPrintable = true;
                  } else if ("CHARSET".equalsIgnoreCase(key)) {
                     quotedPrintableCharset = value;
                  }
               }
            }
         }

         int matchStart = i;

         while((i = rawText.indexOf(10, i)) >= 0) {
            if (i >= rawText.length() - 1 || rawText.charAt(i + 1) != ' ' && rawText.charAt(i + 1) != '\t') {
               if (!quotedPrintable || (i < 1 || rawText.charAt(i - 1) != '=') && (i < 2 || rawText.charAt(i - 2) != '=')) {
                  break;
               }

               ++i;
            } else {
               i += 2;
            }
         }

         if (i < 0) {
            i = max;
         } else if (i > matchStart) {
            if (matches == null) {
               matches = new ArrayList(1);
            }

            if (i >= 1 && rawText.charAt(i - 1) == '\r') {
               --i;
            }

            String element = rawText.substring(matchStart, i);
            if (trim) {
               element = element.trim();
            }

            if (quotedPrintable) {
               element = decodeQuotedPrintable(element, quotedPrintableCharset);
               if (parseFieldDivider) {
                  element = UNESCAPED_SEMICOLONS.matcher(element).replaceAll("\n").trim();
               }
            } else {
               if (parseFieldDivider) {
                  element = UNESCAPED_SEMICOLONS.matcher(element).replaceAll("\n").trim();
               }

               element = CR_LF_SPACE_TAB.matcher(element).replaceAll("");
               element = NEWLINE_ESCAPE.matcher(element).replaceAll("\n");
               element = VCARD_ESCAPES.matcher(element).replaceAll("$1");
            }

            if (metadata == null) {
               List match = new ArrayList(1);
               match.add(element);
               matches.add(match);
            } else {
               metadata.add(0, element);
               matches.add(metadata);
            }

            ++i;
         } else {
            ++i;
         }
      }

      return matches;
   }

   private static String decodeQuotedPrintable(CharSequence value, String charset) {
      int length = value.length();
      StringBuilder result = new StringBuilder(length);
      ByteArrayOutputStream fragmentBuffer = new ByteArrayOutputStream();

      for(int i = 0; i < length; ++i) {
         char c = value.charAt(i);
         switch (c) {
            case '\n':
            case '\r':
               break;
            case '=':
               if (i < length - 2) {
                  char nextChar = value.charAt(i + 1);
                  if (nextChar != '\r' && nextChar != '\n') {
                     char nextNextChar = value.charAt(i + 2);
                     int firstDigit = parseHexDigit(nextChar);
                     int secondDigit = parseHexDigit(nextNextChar);
                     if (firstDigit >= 0 && secondDigit >= 0) {
                        fragmentBuffer.write((firstDigit << 4) + secondDigit);
                     }

                     i += 2;
                  }
               }
               break;
            default:
               maybeAppendFragment(fragmentBuffer, charset, result);
               result.append(c);
         }
      }

      maybeAppendFragment(fragmentBuffer, charset, result);
      return result.toString();
   }

   private static void maybeAppendFragment(ByteArrayOutputStream fragmentBuffer, String charset, StringBuilder result) {
      if (fragmentBuffer.size() > 0) {
         byte[] fragmentBytes = fragmentBuffer.toByteArray();
         String fragment;
         if (charset == null) {
            fragment = new String(fragmentBytes);
         } else {
            try {
               fragment = new String(fragmentBytes, charset);
            } catch (UnsupportedEncodingException var6) {
               fragment = new String(fragmentBytes);
            }
         }

         fragmentBuffer.reset();
         result.append(fragment);
      }

   }

   static List matchSingleVCardPrefixedField(CharSequence prefix, String rawText, boolean trim, boolean parseFieldDivider) {
      List values = matchVCardPrefixedField(prefix, rawText, trim, parseFieldDivider);
      return values != null && !values.isEmpty() ? (List)values.get(0) : null;
   }

   private static String toPrimaryValue(List list) {
      return list != null && !list.isEmpty() ? (String)list.get(0) : null;
   }

   private static String[] toPrimaryValues(Collection lists) {
      if (lists != null && !lists.isEmpty()) {
         List result = new ArrayList(lists.size());
         Iterator i$ = lists.iterator();

         while(i$.hasNext()) {
            List list = (List)i$.next();
            result.add(list.get(0));
         }

         return (String[])result.toArray(new String[lists.size()]);
      } else {
         return null;
      }
   }

   private static String[] toTypes(Collection lists) {
      if (lists != null && !lists.isEmpty()) {
         List result = new ArrayList(lists.size());

         String type;
         for(Iterator i$ = lists.iterator(); i$.hasNext(); result.add(type)) {
            List list = (List)i$.next();
            type = null;

            for(int i = 1; i < list.size(); ++i) {
               String metadatum = (String)list.get(i);
               int equals = metadatum.indexOf(61);
               if (equals < 0) {
                  type = metadatum;
                  break;
               }

               if ("TYPE".equalsIgnoreCase(metadatum.substring(0, equals))) {
                  type = metadatum.substring(equals + 1);
                  break;
               }
            }
         }

         return (String[])result.toArray(new String[lists.size()]);
      } else {
         return null;
      }
   }

   private static boolean isLikeVCardDate(CharSequence value) {
      return value == null || VCARD_LIKE_DATE.matcher(value).matches();
   }

   private static void formatNames(Iterable names) {
      if (names != null) {
         Iterator i$ = names.iterator();

         while(i$.hasNext()) {
            List list = (List)i$.next();
            String name = (String)list.get(0);
            String[] components = new String[5];
            int start = 0;

            int end;
            int componentIndex;
            for(componentIndex = 0; componentIndex < components.length - 1 && (end = name.indexOf(59, start)) > 0; start = end + 1) {
               components[componentIndex] = name.substring(start, end);
               ++componentIndex;
            }

            components[componentIndex] = name.substring(start);
            StringBuilder newName = new StringBuilder(100);
            maybeAppendComponent(components, 3, newName);
            maybeAppendComponent(components, 1, newName);
            maybeAppendComponent(components, 2, newName);
            maybeAppendComponent(components, 0, newName);
            maybeAppendComponent(components, 4, newName);
            list.set(0, newName.toString().trim());
         }
      }

   }

   private static void maybeAppendComponent(String[] components, int i, StringBuilder newName) {
      if (components[i] != null) {
         newName.append(' ');
         newName.append(components[i]);
      }

   }
}
