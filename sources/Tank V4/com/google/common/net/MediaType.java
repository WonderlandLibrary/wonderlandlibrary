package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Ascii;
import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Beta
@GwtCompatible
@Immutable
public final class MediaType {
   private static final String CHARSET_ATTRIBUTE = "charset";
   private static final ImmutableListMultimap UTF_8_CONSTANT_PARAMETERS;
   private static final CharMatcher TOKEN_MATCHER;
   private static final CharMatcher QUOTED_TEXT_MATCHER;
   private static final CharMatcher LINEAR_WHITE_SPACE;
   private static final String APPLICATION_TYPE = "application";
   private static final String AUDIO_TYPE = "audio";
   private static final String IMAGE_TYPE = "image";
   private static final String TEXT_TYPE = "text";
   private static final String VIDEO_TYPE = "video";
   private static final String WILDCARD = "*";
   private static final Map KNOWN_TYPES;
   public static final MediaType ANY_TYPE;
   public static final MediaType ANY_TEXT_TYPE;
   public static final MediaType ANY_IMAGE_TYPE;
   public static final MediaType ANY_AUDIO_TYPE;
   public static final MediaType ANY_VIDEO_TYPE;
   public static final MediaType ANY_APPLICATION_TYPE;
   public static final MediaType CACHE_MANIFEST_UTF_8;
   public static final MediaType CSS_UTF_8;
   public static final MediaType CSV_UTF_8;
   public static final MediaType HTML_UTF_8;
   public static final MediaType I_CALENDAR_UTF_8;
   public static final MediaType PLAIN_TEXT_UTF_8;
   public static final MediaType TEXT_JAVASCRIPT_UTF_8;
   public static final MediaType TSV_UTF_8;
   public static final MediaType VCARD_UTF_8;
   public static final MediaType WML_UTF_8;
   public static final MediaType XML_UTF_8;
   public static final MediaType BMP;
   public static final MediaType CRW;
   public static final MediaType GIF;
   public static final MediaType ICO;
   public static final MediaType JPEG;
   public static final MediaType PNG;
   public static final MediaType PSD;
   public static final MediaType SVG_UTF_8;
   public static final MediaType TIFF;
   public static final MediaType WEBP;
   public static final MediaType MP4_AUDIO;
   public static final MediaType MPEG_AUDIO;
   public static final MediaType OGG_AUDIO;
   public static final MediaType WEBM_AUDIO;
   public static final MediaType MP4_VIDEO;
   public static final MediaType MPEG_VIDEO;
   public static final MediaType OGG_VIDEO;
   public static final MediaType QUICKTIME;
   public static final MediaType WEBM_VIDEO;
   public static final MediaType WMV;
   public static final MediaType APPLICATION_XML_UTF_8;
   public static final MediaType ATOM_UTF_8;
   public static final MediaType BZIP2;
   public static final MediaType EOT;
   public static final MediaType EPUB;
   public static final MediaType FORM_DATA;
   public static final MediaType KEY_ARCHIVE;
   public static final MediaType APPLICATION_BINARY;
   public static final MediaType GZIP;
   public static final MediaType JAVASCRIPT_UTF_8;
   public static final MediaType JSON_UTF_8;
   public static final MediaType KML;
   public static final MediaType KMZ;
   public static final MediaType MBOX;
   public static final MediaType MICROSOFT_EXCEL;
   public static final MediaType MICROSOFT_POWERPOINT;
   public static final MediaType MICROSOFT_WORD;
   public static final MediaType OCTET_STREAM;
   public static final MediaType OGG_CONTAINER;
   public static final MediaType OOXML_DOCUMENT;
   public static final MediaType OOXML_PRESENTATION;
   public static final MediaType OOXML_SHEET;
   public static final MediaType OPENDOCUMENT_GRAPHICS;
   public static final MediaType OPENDOCUMENT_PRESENTATION;
   public static final MediaType OPENDOCUMENT_SPREADSHEET;
   public static final MediaType OPENDOCUMENT_TEXT;
   public static final MediaType PDF;
   public static final MediaType POSTSCRIPT;
   public static final MediaType PROTOBUF;
   public static final MediaType RDF_XML_UTF_8;
   public static final MediaType RTF_UTF_8;
   public static final MediaType SFNT;
   public static final MediaType SHOCKWAVE_FLASH;
   public static final MediaType SKETCHUP;
   public static final MediaType TAR;
   public static final MediaType WOFF;
   public static final MediaType XHTML_UTF_8;
   public static final MediaType XRD_UTF_8;
   public static final MediaType ZIP;
   private final String type;
   private final String subtype;
   private final ImmutableListMultimap parameters;
   private static final Joiner.MapJoiner PARAMETER_JOINER;

   private static MediaType createConstant(String var0, String var1) {
      return addKnownType(new MediaType(var0, var1, ImmutableListMultimap.of()));
   }

   private static MediaType createConstantUtf8(String var0, String var1) {
      return addKnownType(new MediaType(var0, var1, UTF_8_CONSTANT_PARAMETERS));
   }

   private static MediaType addKnownType(MediaType var0) {
      KNOWN_TYPES.put(var0, var0);
      return var0;
   }

   private MediaType(String var1, String var2, ImmutableListMultimap var3) {
      this.type = var1;
      this.subtype = var2;
      this.parameters = var3;
   }

   public String type() {
      return this.type;
   }

   public String subtype() {
      return this.subtype;
   }

   public ImmutableListMultimap parameters() {
      return this.parameters;
   }

   private Map parametersAsMap() {
      return Maps.transformValues((Map)this.parameters.asMap(), new Function(this) {
         final MediaType this$0;

         {
            this.this$0 = var1;
         }

         public ImmutableMultiset apply(Collection var1) {
            return ImmutableMultiset.copyOf((Iterable)var1);
         }

         public Object apply(Object var1) {
            return this.apply((Collection)var1);
         }
      });
   }

   public Optional charset() {
      ImmutableSet var1 = ImmutableSet.copyOf((Collection)this.parameters.get("charset"));
      switch(var1.size()) {
      case 0:
         return Optional.absent();
      case 1:
         return Optional.of(Charset.forName((String)Iterables.getOnlyElement(var1)));
      default:
         throw new IllegalStateException("Multiple charset values defined: " + var1);
      }
   }

   public MediaType withoutParameters() {
      return this.parameters.isEmpty() ? this : create(this.type, this.subtype);
   }

   public MediaType withParameters(Multimap var1) {
      return create(this.type, this.subtype, var1);
   }

   public MediaType withParameter(String var1, String var2) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      String var3 = normalizeToken(var1);
      ImmutableListMultimap.Builder var4 = ImmutableListMultimap.builder();
      Iterator var5 = this.parameters.entries().iterator();

      while(var5.hasNext()) {
         Entry var6 = (Entry)var5.next();
         String var7 = (String)var6.getKey();
         if (!var3.equals(var7)) {
            var4.put(var7, var6.getValue());
         }
      }

      var4.put(var3, normalizeParameterValue(var3, var2));
      MediaType var8 = new MediaType(this.type, this.subtype, var4.build());
      return (MediaType)Objects.firstNonNull(KNOWN_TYPES.get(var8), var8);
   }

   public MediaType withCharset(Charset var1) {
      Preconditions.checkNotNull(var1);
      return this.withParameter("charset", var1.name());
   }

   public boolean hasWildcard() {
      return "*".equals(this.type) || "*".equals(this.subtype);
   }

   public boolean is(MediaType var1) {
      return (var1.type.equals("*") || var1.type.equals(this.type)) && (var1.subtype.equals("*") || var1.subtype.equals(this.subtype)) && this.parameters.entries().containsAll(var1.parameters.entries());
   }

   public static MediaType create(String var0, String var1) {
      return create(var0, var1, ImmutableListMultimap.of());
   }

   static MediaType createApplicationType(String var0) {
      return create("application", var0);
   }

   static MediaType createAudioType(String var0) {
      return create("audio", var0);
   }

   static MediaType createImageType(String var0) {
      return create("image", var0);
   }

   static MediaType createTextType(String var0) {
      return create("text", var0);
   }

   static MediaType createVideoType(String var0) {
      return create("video", var0);
   }

   private static MediaType create(String var0, String var1, Multimap var2) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      String var3 = normalizeToken(var0);
      String var4 = normalizeToken(var1);
      Preconditions.checkArgument(!"*".equals(var3) || "*".equals(var4), "A wildcard type cannot be used with a non-wildcard subtype");
      ImmutableListMultimap.Builder var5 = ImmutableListMultimap.builder();
      Iterator var6 = var2.entries().iterator();

      while(var6.hasNext()) {
         Entry var7 = (Entry)var6.next();
         String var8 = normalizeToken((String)var7.getKey());
         var5.put(var8, normalizeParameterValue(var8, (String)var7.getValue()));
      }

      MediaType var9 = new MediaType(var3, var4, var5.build());
      return (MediaType)Objects.firstNonNull(KNOWN_TYPES.get(var9), var9);
   }

   private static String normalizeToken(String var0) {
      Preconditions.checkArgument(TOKEN_MATCHER.matchesAllOf(var0));
      return Ascii.toLowerCase(var0);
   }

   private static String normalizeParameterValue(String var0, String var1) {
      return "charset".equals(var0) ? Ascii.toLowerCase(var1) : var1;
   }

   public static MediaType parse(String var0) {
      Preconditions.checkNotNull(var0);
      MediaType.Tokenizer var1 = new MediaType.Tokenizer(var0);

      try {
         String var2 = var1.consumeToken(TOKEN_MATCHER);
         var1.consumeCharacter('/');
         String var3 = var1.consumeToken(TOKEN_MATCHER);

         ImmutableListMultimap.Builder var4;
         String var5;
         String var6;
         for(var4 = ImmutableListMultimap.builder(); var1.hasMore(); var4.put(var5, var6)) {
            var1.consumeCharacter(';');
            var1.consumeTokenIfPresent(LINEAR_WHITE_SPACE);
            var5 = var1.consumeToken(TOKEN_MATCHER);
            var1.consumeCharacter('=');
            if ('"' != var1.previewChar()) {
               var6 = var1.consumeToken(TOKEN_MATCHER);
            } else {
               var1.consumeCharacter('"');
               StringBuilder var7 = new StringBuilder();

               while('"' != var1.previewChar()) {
                  if ('\\' == var1.previewChar()) {
                     var1.consumeCharacter('\\');
                     var7.append(var1.consumeCharacter(CharMatcher.ASCII));
                  } else {
                     var7.append(var1.consumeToken(QUOTED_TEXT_MATCHER));
                  }
               }

               var6 = var7.toString();
               var1.consumeCharacter('"');
            }
         }

         return create(var2, var3, var4.build());
      } catch (IllegalStateException var8) {
         throw new IllegalArgumentException("Could not parse '" + var0 + "'", var8);
      }
   }

   public boolean equals(@Nullable Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof MediaType)) {
         return false;
      } else {
         MediaType var2 = (MediaType)var1;
         return this.type.equals(var2.type) && this.subtype.equals(var2.subtype) && this.parametersAsMap().equals(var2.parametersAsMap());
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.type, this.subtype, this.parametersAsMap());
   }

   public String toString() {
      StringBuilder var1 = (new StringBuilder()).append(this.type).append('/').append(this.subtype);
      if (!this.parameters.isEmpty()) {
         var1.append("; ");
         ListMultimap var2 = Multimaps.transformValues((ListMultimap)this.parameters, new Function(this) {
            final MediaType this$0;

            {
               this.this$0 = var1;
            }

            public String apply(String var1) {
               return MediaType.access$000().matchesAllOf(var1) ? var1 : MediaType.access$100(var1);
            }

            public Object apply(Object var1) {
               return this.apply((String)var1);
            }
         });
         PARAMETER_JOINER.appendTo((StringBuilder)var1, (Iterable)var2.entries());
      }

      return var1.toString();
   }

   private static String escapeAndQuote(String var0) {
      StringBuilder var1 = (new StringBuilder(var0.length() + 16)).append('"');
      char[] var2 = var0.toCharArray();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         char var5 = var2[var4];
         if (var5 == '\r' || var5 == '\\' || var5 == '"') {
            var1.append('\\');
         }

         var1.append(var5);
      }

      return var1.append('"').toString();
   }

   static CharMatcher access$000() {
      return TOKEN_MATCHER;
   }

   static String access$100(String var0) {
      return escapeAndQuote(var0);
   }

   static {
      UTF_8_CONSTANT_PARAMETERS = ImmutableListMultimap.of("charset", Ascii.toLowerCase(Charsets.UTF_8.name()));
      TOKEN_MATCHER = CharMatcher.ASCII.and(CharMatcher.JAVA_ISO_CONTROL.negate()).and(CharMatcher.isNot(' ')).and(CharMatcher.noneOf("()<>@,;:\\\"/[]?="));
      QUOTED_TEXT_MATCHER = CharMatcher.ASCII.and(CharMatcher.noneOf("\"\\\r"));
      LINEAR_WHITE_SPACE = CharMatcher.anyOf(" \t\r\n");
      KNOWN_TYPES = Maps.newHashMap();
      ANY_TYPE = createConstant("*", "*");
      ANY_TEXT_TYPE = createConstant("text", "*");
      ANY_IMAGE_TYPE = createConstant("image", "*");
      ANY_AUDIO_TYPE = createConstant("audio", "*");
      ANY_VIDEO_TYPE = createConstant("video", "*");
      ANY_APPLICATION_TYPE = createConstant("application", "*");
      CACHE_MANIFEST_UTF_8 = createConstantUtf8("text", "cache-manifest");
      CSS_UTF_8 = createConstantUtf8("text", "css");
      CSV_UTF_8 = createConstantUtf8("text", "csv");
      HTML_UTF_8 = createConstantUtf8("text", "html");
      I_CALENDAR_UTF_8 = createConstantUtf8("text", "calendar");
      PLAIN_TEXT_UTF_8 = createConstantUtf8("text", "plain");
      TEXT_JAVASCRIPT_UTF_8 = createConstantUtf8("text", "javascript");
      TSV_UTF_8 = createConstantUtf8("text", "tab-separated-values");
      VCARD_UTF_8 = createConstantUtf8("text", "vcard");
      WML_UTF_8 = createConstantUtf8("text", "vnd.wap.wml");
      XML_UTF_8 = createConstantUtf8("text", "xml");
      BMP = createConstant("image", "bmp");
      CRW = createConstant("image", "x-canon-crw");
      GIF = createConstant("image", "gif");
      ICO = createConstant("image", "vnd.microsoft.icon");
      JPEG = createConstant("image", "jpeg");
      PNG = createConstant("image", "png");
      PSD = createConstant("image", "vnd.adobe.photoshop");
      SVG_UTF_8 = createConstantUtf8("image", "svg+xml");
      TIFF = createConstant("image", "tiff");
      WEBP = createConstant("image", "webp");
      MP4_AUDIO = createConstant("audio", "mp4");
      MPEG_AUDIO = createConstant("audio", "mpeg");
      OGG_AUDIO = createConstant("audio", "ogg");
      WEBM_AUDIO = createConstant("audio", "webm");
      MP4_VIDEO = createConstant("video", "mp4");
      MPEG_VIDEO = createConstant("video", "mpeg");
      OGG_VIDEO = createConstant("video", "ogg");
      QUICKTIME = createConstant("video", "quicktime");
      WEBM_VIDEO = createConstant("video", "webm");
      WMV = createConstant("video", "x-ms-wmv");
      APPLICATION_XML_UTF_8 = createConstantUtf8("application", "xml");
      ATOM_UTF_8 = createConstantUtf8("application", "atom+xml");
      BZIP2 = createConstant("application", "x-bzip2");
      EOT = createConstant("application", "vnd.ms-fontobject");
      EPUB = createConstant("application", "epub+zip");
      FORM_DATA = createConstant("application", "x-www-form-urlencoded");
      KEY_ARCHIVE = createConstant("application", "pkcs12");
      APPLICATION_BINARY = createConstant("application", "binary");
      GZIP = createConstant("application", "x-gzip");
      JAVASCRIPT_UTF_8 = createConstantUtf8("application", "javascript");
      JSON_UTF_8 = createConstantUtf8("application", "json");
      KML = createConstant("application", "vnd.google-earth.kml+xml");
      KMZ = createConstant("application", "vnd.google-earth.kmz");
      MBOX = createConstant("application", "mbox");
      MICROSOFT_EXCEL = createConstant("application", "vnd.ms-excel");
      MICROSOFT_POWERPOINT = createConstant("application", "vnd.ms-powerpoint");
      MICROSOFT_WORD = createConstant("application", "msword");
      OCTET_STREAM = createConstant("application", "octet-stream");
      OGG_CONTAINER = createConstant("application", "ogg");
      OOXML_DOCUMENT = createConstant("application", "vnd.openxmlformats-officedocument.wordprocessingml.document");
      OOXML_PRESENTATION = createConstant("application", "vnd.openxmlformats-officedocument.presentationml.presentation");
      OOXML_SHEET = createConstant("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet");
      OPENDOCUMENT_GRAPHICS = createConstant("application", "vnd.oasis.opendocument.graphics");
      OPENDOCUMENT_PRESENTATION = createConstant("application", "vnd.oasis.opendocument.presentation");
      OPENDOCUMENT_SPREADSHEET = createConstant("application", "vnd.oasis.opendocument.spreadsheet");
      OPENDOCUMENT_TEXT = createConstant("application", "vnd.oasis.opendocument.text");
      PDF = createConstant("application", "pdf");
      POSTSCRIPT = createConstant("application", "postscript");
      PROTOBUF = createConstant("application", "protobuf");
      RDF_XML_UTF_8 = createConstantUtf8("application", "rdf+xml");
      RTF_UTF_8 = createConstantUtf8("application", "rtf");
      SFNT = createConstant("application", "font-sfnt");
      SHOCKWAVE_FLASH = createConstant("application", "x-shockwave-flash");
      SKETCHUP = createConstant("application", "vnd.sketchup.skp");
      TAR = createConstant("application", "x-tar");
      WOFF = createConstant("application", "font-woff");
      XHTML_UTF_8 = createConstantUtf8("application", "xhtml+xml");
      XRD_UTF_8 = createConstantUtf8("application", "xrd+xml");
      ZIP = createConstant("application", "zip");
      PARAMETER_JOINER = Joiner.on("; ").withKeyValueSeparator("=");
   }

   private static final class Tokenizer {
      final String input;
      int position = 0;

      Tokenizer(String var1) {
         this.input = var1;
      }

      String consumeTokenIfPresent(CharMatcher var1) {
         Preconditions.checkState(this.hasMore());
         int var2 = this.position;
         this.position = var1.negate().indexIn(this.input, var2);
         return this >= 0 ? this.input.substring(var2, this.position) : this.input.substring(var2);
      }

      String consumeToken(CharMatcher var1) {
         int var2 = this.position;
         String var3 = this.consumeTokenIfPresent(var1);
         Preconditions.checkState(this.position != var2);
         return var3;
      }

      char consumeCharacter(CharMatcher var1) {
         Preconditions.checkState(this.hasMore());
         char var2 = this.previewChar();
         Preconditions.checkState(var1.matches(var2));
         ++this.position;
         return var2;
      }

      char consumeCharacter(char var1) {
         Preconditions.checkState(this.hasMore());
         Preconditions.checkState(this.previewChar() == var1);
         ++this.position;
         return var1;
      }

      char previewChar() {
         Preconditions.checkState(this.hasMore());
         return this.input.charAt(this.position);
      }
   }
}
