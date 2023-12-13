package com.sun.javafx.css.parser;

import com.sun.javafx.css.Combinator;
import com.sun.javafx.css.CompoundSelector;
import com.sun.javafx.css.CssError;
import com.sun.javafx.css.Declaration;
import com.sun.javafx.css.FontFace;
import com.sun.javafx.css.ParsedValueImpl;
import com.sun.javafx.css.Rule;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.SimpleSelector;
import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.Stylesheet;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.DurationConverter;
import com.sun.javafx.css.converters.EffectConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.FontConverter;
import com.sun.javafx.css.converters.InsetsConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.css.converters.URLConverter;
import com.sun.javafx.scene.layout.region.BackgroundPositionConverter;
import com.sun.javafx.scene.layout.region.BackgroundSizeConverter;
import com.sun.javafx.scene.layout.region.BorderImageSliceConverter;
import com.sun.javafx.scene.layout.region.BorderImageWidthConverter;
import com.sun.javafx.scene.layout.region.BorderImageWidthsSequenceConverter;
import com.sun.javafx.scene.layout.region.BorderStrokeStyleSequenceConverter;
import com.sun.javafx.scene.layout.region.BorderStyleConverter;
import com.sun.javafx.scene.layout.region.CornerRadiiConverter;
import com.sun.javafx.scene.layout.region.LayeredBackgroundPositionConverter;
import com.sun.javafx.scene.layout.region.LayeredBackgroundSizeConverter;
import com.sun.javafx.scene.layout.region.LayeredBorderPaintConverter;
import com.sun.javafx.scene.layout.region.LayeredBorderStyleConverter;
import com.sun.javafx.scene.layout.region.Margins;
import com.sun.javafx.scene.layout.region.RepeatStructConverter;
import com.sun.javafx.scene.layout.region.SliceSequenceConverter;
import com.sun.javafx.scene.layout.region.StrokeBorderPaintConverter;
import com.sun.javafx.util.Logging;
import com.sun.javafx.util.Utils;
import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import javafx.collections.ObservableList;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.scene.effect.BlurType;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import sun.util.logging.PlatformLogger;
import sun.util.logging.PlatformLogger.Level;

public final class CSSParser {
   private String stylesheetAsText;
   private String sourceOfStylesheet;
   private Styleable sourceOfInlineStyle;
   private static final PlatformLogger LOGGER = Logging.getCSSLogger();
   private final Map properties = new HashMap();
   private static final ParsedValueImpl ZERO_PERCENT;
   private static final ParsedValueImpl FIFTY_PERCENT;
   private static final ParsedValueImpl ONE_HUNDRED_PERCENT;
   public static final String SPECIAL_REGION_URL_PREFIX = "SPECIAL-REGION-URL:";
   Token currentToken = null;
   private static Stack imports;

   /** @deprecated */
   @Deprecated
   public static CSSParser getInstance() {
      return new CSSParser();
   }

   private void setInputSource(String var1, String var2) {
      this.stylesheetAsText = var2;
      this.sourceOfStylesheet = var1;
      this.sourceOfInlineStyle = null;
   }

   private void setInputSource(String var1) {
      this.stylesheetAsText = var1;
      this.sourceOfStylesheet = null;
      this.sourceOfInlineStyle = null;
   }

   private void setInputSource(Styleable var1) {
      this.stylesheetAsText = var1 != null ? var1.getStyle() : null;
      this.sourceOfStylesheet = null;
      this.sourceOfInlineStyle = var1;
   }

   public Stylesheet parse(String var1) {
      Stylesheet var2 = new Stylesheet();
      if (var1 != null && !var1.trim().isEmpty()) {
         this.setInputSource(var1);

         try {
            CharArrayReader var3 = new CharArrayReader(var1.toCharArray());
            Throwable var4 = null;

            try {
               this.parse((Stylesheet)var2, (Reader)var3);
            } catch (Throwable var14) {
               var4 = var14;
               throw var14;
            } finally {
               if (var3 != null) {
                  if (var4 != null) {
                     try {
                        var3.close();
                     } catch (Throwable var13) {
                        var4.addSuppressed(var13);
                     }
                  } else {
                     var3.close();
                  }
               }

            }
         } catch (IOException var16) {
         }
      }

      return var2;
   }

   public Stylesheet parse(String var1, String var2) throws IOException {
      Stylesheet var3 = new Stylesheet(var1);
      if (var2 != null && !var2.trim().isEmpty()) {
         this.setInputSource(var1, var2);
         CharArrayReader var4 = new CharArrayReader(var2.toCharArray());
         Throwable var5 = null;

         try {
            this.parse((Stylesheet)var3, (Reader)var4);
         } catch (Throwable var14) {
            var5 = var14;
            throw var14;
         } finally {
            if (var4 != null) {
               if (var5 != null) {
                  try {
                     var4.close();
                  } catch (Throwable var13) {
                     var5.addSuppressed(var13);
                  }
               } else {
                  var4.close();
               }
            }

         }
      }

      return var3;
   }

   public Stylesheet parse(URL var1) throws IOException {
      String var2 = var1 != null ? var1.toExternalForm() : null;
      Stylesheet var3 = new Stylesheet(var2);
      if (var1 != null) {
         this.setInputSource(var2, (String)null);
         BufferedReader var4 = new BufferedReader(new InputStreamReader(var1.openStream()));
         Throwable var5 = null;

         try {
            this.parse((Stylesheet)var3, (Reader)var4);
         } catch (Throwable var14) {
            var5 = var14;
            throw var14;
         } finally {
            if (var4 != null) {
               if (var5 != null) {
                  try {
                     var4.close();
                  } catch (Throwable var13) {
                     var5.addSuppressed(var13);
                  }
               } else {
                  var4.close();
               }
            }

         }
      }

      return var3;
   }

   private void parse(Stylesheet var1, Reader var2) {
      CSSLexer var3 = new CSSLexer();
      var3.setReader(var2);

      try {
         this.parse(var1, var3);
      } catch (Exception var5) {
         this.reportException(var5);
      }

   }

   public Stylesheet parseInlineStyle(Styleable var1) {
      Stylesheet var2 = new Stylesheet();
      String var3 = var1 != null ? var1.getStyle() : null;
      if (var3 != null && !var3.trim().isEmpty()) {
         this.setInputSource(var1);
         ArrayList var4 = new ArrayList();

         try {
            CharArrayReader var5 = new CharArrayReader(var3.toCharArray());
            Throwable var6 = null;

            try {
               CSSLexer var7 = CSSLexer.getInstance();
               var7.setReader(var5);
               this.currentToken = this.nextToken(var7);
               List var8 = this.declarations(var7);
               if (var8 != null && !var8.isEmpty()) {
                  Selector var9 = Selector.getUniversalSelector();
                  Rule var10 = new Rule(Collections.singletonList(var9), var8);
                  var4.add(var10);
               }
            } catch (Throwable var20) {
               var6 = var20;
               throw var20;
            } finally {
               if (var5 != null) {
                  if (var6 != null) {
                     try {
                        var5.close();
                     } catch (Throwable var19) {
                        var6.addSuppressed(var19);
                     }
                  } else {
                     var5.close();
                  }
               }

            }
         } catch (IOException var22) {
         } catch (Exception var23) {
            this.reportException(var23);
         }

         var2.getRules().addAll(var4);
      }

      this.setInputSource((Styleable)null);
      return var2;
   }

   public ParsedValueImpl parseExpr(String var1, String var2) {
      if (var1 != null && var2 != null) {
         ParsedValueImpl var3 = null;
         this.setInputSource((String)null, var1 + ": " + var2);
         char[] var4 = new char[var2.length() + 1];
         System.arraycopy(var2.toCharArray(), 0, var4, 0, var2.length());
         var4[var4.length - 1] = ';';

         try {
            CharArrayReader var5 = new CharArrayReader(var4);
            Throwable var6 = null;

            try {
               CSSLexer var7 = CSSLexer.getInstance();
               var7.setReader(var5);
               this.currentToken = this.nextToken(var7);
               Term var8 = this.expr(var7);
               var3 = this.valueFor(var1, var8, var7);
            } catch (Throwable var19) {
               var6 = var19;
               throw var19;
            } finally {
               if (var5 != null) {
                  if (var6 != null) {
                     try {
                        var5.close();
                     } catch (Throwable var18) {
                        var6.addSuppressed(var18);
                     }
                  } else {
                     var5.close();
                  }
               }

            }
         } catch (IOException var21) {
         } catch (ParseException var22) {
            if (LOGGER.isLoggable(Level.WARNING)) {
               LOGGER.warning("\"" + var1 + ": " + var2 + "\" " + var22.toString());
            }
         } catch (Exception var23) {
            this.reportException(var23);
         }

         return var3;
      } else {
         return null;
      }
   }

   private CssError createError(String var1) {
      Object var2 = null;
      if (this.sourceOfStylesheet != null) {
         var2 = new CssError.StylesheetParsingError(this.sourceOfStylesheet, var1);
      } else if (this.sourceOfInlineStyle != null) {
         var2 = new CssError.InlineStyleParsingError(this.sourceOfInlineStyle, var1);
      } else {
         var2 = new CssError.StringParsingError(this.stylesheetAsText, var1);
      }

      return (CssError)var2;
   }

   private void reportError(CssError var1) {
      ObservableList var2 = null;
      if ((var2 = StyleManager.getErrors()) != null) {
         var2.add(var1);
      }

   }

   private void error(Term var1, String var2) throws ParseException {
      Token var3 = var1 != null ? var1.token : null;
      ParseException var4 = new ParseException(var2, var3, this);
      this.reportError(this.createError(var4.toString()));
      throw var4;
   }

   private void reportException(Exception var1) {
      if (LOGGER.isLoggable(Level.WARNING)) {
         StackTraceElement[] var2 = var1.getStackTrace();
         if (var2.length > 0) {
            StringBuilder var3 = new StringBuilder("Please report ");
            var3.append(var1.getClass().getName()).append(" at:");
            int var4 = 0;

            while(var4 < var2.length && this.getClass().getName().equals(var2[var4].getClassName())) {
               var3.append("\n\t").append(var2[var4++].toString());
            }

            LOGGER.warning(var3.toString());
         }
      }

   }

   private String formatDeprecatedMessage(Term var1, String var2) {
      StringBuilder var3 = new StringBuilder("Using deprecated syntax for ");
      var3.append(var2);
      if (this.sourceOfStylesheet != null) {
         var3.append(" at ").append(this.sourceOfStylesheet).append("[").append(var1.token.getLine()).append(',').append(var1.token.getOffset()).append("]");
      }

      var3.append(". Refer to the CSS Reference Guide.");
      return var3.toString();
   }

   private ParsedValueImpl colorValueOfString(String var1) {
      if (!var1.startsWith("#") && !var1.startsWith("0x")) {
         try {
            return new ParsedValueImpl(Color.web(var1), (StyleConverter)null);
         } catch (IllegalArgumentException var7) {
         } catch (NullPointerException var8) {
         }

         return null;
      } else {
         double var2 = 1.0;
         String var4 = var1;
         int var5 = var1.startsWith("#") ? 1 : 2;
         int var6 = var1.length();
         if (var6 - var5 == 4) {
            var2 = (double)((float)Integer.parseInt(var1.substring(var6 - 1), 16) / 15.0F);
            var4 = var1.substring(0, var6 - 1);
         } else if (var6 - var5 == 8) {
            var2 = (double)((float)Integer.parseInt(var1.substring(var6 - 2), 16) / 255.0F);
            var4 = var1.substring(0, var6 - 2);
         }

         return new ParsedValueImpl(Color.web(var4, var2), (StyleConverter)null);
      }
   }

   private String stripQuotes(String var1) {
      return Utils.stripQuotes(var1);
   }

   private double clamp(double var1, double var3, double var5) {
      if (var3 < var1) {
         return var1;
      } else {
         return var5 < var3 ? var5 : var3;
      }
   }

   private boolean isSize(Token var1) {
      int var2 = var1.getType();
      switch (var2) {
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 26:
            return true;
         default:
            return var1.getType() == 11;
      }
   }

   private Size size(Token var1) throws ParseException {
      SizeUnits var2 = SizeUnits.PX;
      byte var3 = 2;
      String var4 = var1.getText().trim();
      int var5 = var4.length();
      int var6 = var1.getType();
      switch (var6) {
         case 13:
            var2 = SizeUnits.PX;
            var3 = 0;
            break;
         case 14:
            var2 = SizeUnits.CM;
            break;
         case 15:
            var2 = SizeUnits.EM;
            break;
         case 16:
            var2 = SizeUnits.EX;
            break;
         case 17:
            var2 = SizeUnits.IN;
            break;
         case 18:
            var2 = SizeUnits.MM;
            break;
         case 19:
            var2 = SizeUnits.PC;
            break;
         case 20:
            var2 = SizeUnits.PT;
            break;
         case 21:
            var2 = SizeUnits.PX;
            break;
         case 22:
            var2 = SizeUnits.PERCENT;
            var3 = 1;
            break;
         case 23:
            var2 = SizeUnits.DEG;
            var3 = 3;
            break;
         case 24:
            var2 = SizeUnits.GRAD;
            var3 = 4;
            break;
         case 25:
            var2 = SizeUnits.RAD;
            var3 = 3;
            break;
         case 26:
            var2 = SizeUnits.TURN;
            var3 = 4;
            break;
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 43:
         case 44:
         default:
            if (LOGGER.isLoggable(Level.FINEST)) {
               LOGGER.finest("Expected '<number>'");
            }

            ParseException var7 = new ParseException("Expected '<number>'", var1, this);
            this.reportError(this.createError(var7.toString()));
            throw var7;
         case 45:
            var2 = SizeUnits.S;
            var3 = 1;
            break;
         case 46:
            var2 = SizeUnits.MS;
      }

      return new Size(Double.parseDouble(var4.substring(0, var5 - var3)), var2);
   }

   private int numberOfTerms(Term var1) {
      if (var1 == null) {
         return 0;
      } else {
         int var2 = 0;
         Term var3 = var1;

         do {
            ++var2;
            var3 = var3.nextInSeries;
         } while(var3 != null);

         return var2;
      }
   }

   private int numberOfLayers(Term var1) {
      if (var1 == null) {
         return 0;
      } else {
         int var2 = 0;
         Term var3 = var1;

         do {
            ++var2;

            while(var3.nextInSeries != null) {
               var3 = var3.nextInSeries;
            }

            var3 = var3.nextLayer;
         } while(var3 != null);

         return var2;
      }
   }

   private int numberOfArgs(Term var1) {
      if (var1 == null) {
         return 0;
      } else {
         int var2 = 0;

         for(Term var3 = var1.firstArg; var3 != null; var3 = var3.nextArg) {
            ++var2;
         }

         return var2;
      }
   }

   private Term nextLayer(Term var1) {
      if (var1 == null) {
         return null;
      } else {
         Term var2;
         for(var2 = var1; var2.nextInSeries != null; var2 = var2.nextInSeries) {
         }

         return var2.nextLayer;
      }
   }

   ParsedValueImpl valueFor(String var1, Term var2, CSSLexer var3) throws ParseException {
      String var4 = var1.toLowerCase(Locale.ROOT);
      this.properties.put(var4, var4);
      if (var2 == null || var2.token == null) {
         this.error(var2, "Expected value for property '" + var4 + "'");
      }

      String var5;
      if (var2.token.getType() == 11) {
         var5 = var2.token.getText();
         if ("inherit".equalsIgnoreCase(var5)) {
            return new ParsedValueImpl("inherit", (StyleConverter)null);
         }

         if ("null".equalsIgnoreCase(var5) || "none".equalsIgnoreCase(var5)) {
            return new ParsedValueImpl("null", (StyleConverter)null);
         }
      }

      ParsedValueImpl var9;
      if ("-fx-fill".equals(var4)) {
         var9 = this.parse(var2);
         if (var9.getConverter() == StyleConverter.getUrlConverter()) {
            var9 = new ParsedValueImpl(new ParsedValue[]{var9}, PaintConverter.ImagePatternConverter.getInstance());
         }

         return var9;
      } else if ("-fx-background-color".equals(var4)) {
         return this.parsePaintLayers(var2);
      } else if ("-fx-background-image".equals(var4)) {
         return this.parseURILayers(var2);
      } else if ("-fx-background-insets".equals(var4)) {
         return this.parseInsetsLayers(var2);
      } else if ("-fx-opaque-insets".equals(var4)) {
         return this.parseInsetsLayer(var2);
      } else if ("-fx-background-position".equals(var4)) {
         return this.parseBackgroundPositionLayers(var2);
      } else if ("-fx-background-radius".equals(var4)) {
         return this.parseCornerRadius(var2);
      } else if ("-fx-background-repeat".equals(var4)) {
         return this.parseBackgroundRepeatStyleLayers(var2);
      } else if ("-fx-background-size".equals(var4)) {
         return this.parseBackgroundSizeLayers(var2);
      } else if ("-fx-border-color".equals(var4)) {
         return this.parseBorderPaintLayers(var2);
      } else if ("-fx-border-insets".equals(var4)) {
         return this.parseInsetsLayers(var2);
      } else if ("-fx-border-radius".equals(var4)) {
         return this.parseCornerRadius(var2);
      } else if ("-fx-border-style".equals(var4)) {
         return this.parseBorderStyleLayers(var2);
      } else if ("-fx-border-width".equals(var4)) {
         return this.parseMarginsLayers(var2);
      } else if ("-fx-border-image-insets".equals(var4)) {
         return this.parseInsetsLayers(var2);
      } else if ("-fx-border-image-repeat".equals(var4)) {
         return this.parseBorderImageRepeatStyleLayers(var2);
      } else if ("-fx-border-image-slice".equals(var4)) {
         return this.parseBorderImageSliceLayers(var2);
      } else if ("-fx-border-image-source".equals(var4)) {
         return this.parseURILayers(var2);
      } else if ("-fx-border-image-width".equals(var4)) {
         return this.parseBorderImageWidthLayers(var2);
      } else {
         ParsedValueImpl[] var11;
         if ("-fx-padding".equals(var4)) {
            var11 = this.parseSize1to4(var2);
            return new ParsedValueImpl(var11, InsetsConverter.getInstance());
         } else if ("-fx-label-padding".equals(var4)) {
            var11 = this.parseSize1to4(var2);
            return new ParsedValueImpl(var11, InsetsConverter.getInstance());
         } else if (var4.endsWith("font-family")) {
            return this.parseFontFamily(var2);
         } else if (var4.endsWith("font-size")) {
            var9 = this.parseFontSize(var2);
            if (var9 == null) {
               this.error(var2, "Expected '<font-size>'");
            }

            return var9;
         } else if (var4.endsWith("font-style")) {
            var9 = this.parseFontStyle(var2);
            if (var9 == null) {
               this.error(var2, "Expected '<font-style>'");
            }

            return var9;
         } else if (var4.endsWith("font-weight")) {
            var9 = this.parseFontWeight(var2);
            if (var9 == null) {
               this.error(var2, "Expected '<font-style>'");
            }

            return var9;
         } else if (var4.endsWith("font")) {
            return this.parseFont(var2);
         } else {
            int var10;
            if ("-fx-stroke-dash-array".equals(var4)) {
               Term var12 = var2;
               var10 = this.numberOfTerms(var2);
               ParsedValueImpl[] var13 = new ParsedValueImpl[var10];

               for(int var8 = 0; var12 != null; var12 = var12.nextInSeries) {
                  var13[var8++] = this.parseSize(var12);
               }

               return new ParsedValueImpl(var13, SizeConverter.SequenceConverter.getInstance());
            } else if ("-fx-stroke-line-join".equals(var4)) {
               var11 = this.parseStrokeLineJoin(var2);
               if (var11 == null) {
                  this.error(var2, "Expected 'miter', 'bevel' or 'round'");
               }

               return var11[0];
            } else if ("-fx-stroke-line-cap".equals(var4)) {
               var9 = this.parseStrokeLineCap(var2);
               if (var9 == null) {
                  this.error(var2, "Expected 'square', 'butt' or 'round'");
               }

               return var9;
            } else if ("-fx-stroke-type".equals(var4)) {
               var9 = this.parseStrokeType(var2);
               if (var9 == null) {
                  this.error(var2, "Expected 'centered', 'inside' or 'outside'");
               }

               return var9;
            } else if (!"-fx-font-smoothing-type".equals(var4)) {
               return this.parse(var2);
            } else {
               var5 = null;
               boolean var6 = true;
               Token var7 = var2.token;
               if (var2.token == null || (var10 = var2.token.getType()) != 10 && var10 != 11 || (var5 = var2.token.getText()) == null || var5.isEmpty()) {
                  this.error(var2, "Expected STRING or IDENT");
               }

               return new ParsedValueImpl(this.stripQuotes(var5), (StyleConverter)null, false);
            }
         }
      }
   }

   private ParsedValueImpl parse(Term var1) throws ParseException {
      if (var1.token == null) {
         this.error(var1, "Parse error");
      }

      Token var2 = var1.token;
      ParsedValueImpl var3 = null;
      int var4 = var2.getType();
      ParsedValueImpl var5;
      switch (var4) {
         case 10:
         case 11:
            boolean var12 = var4 == 11;
            String var6 = this.stripQuotes(var2.getText());
            String var7 = var6.toLowerCase(Locale.ROOT);
            if ("ladder".equals(var7)) {
               var3 = this.ladder(var1);
            } else if ("linear".equals(var7) && var1.nextInSeries != null) {
               var3 = this.linearGradient(var1);
            } else if ("radial".equals(var7) && var1.nextInSeries != null) {
               var3 = this.radialGradient(var1);
            } else {
               ParsedValueImpl var9;
               Size var13;
               if ("infinity".equals(var7)) {
                  var13 = new Size(Double.MAX_VALUE, SizeUnits.PX);
                  var9 = new ParsedValueImpl(var13, (StyleConverter)null);
                  var3 = new ParsedValueImpl(var9, SizeConverter.getInstance());
               } else if ("indefinite".equals(var7)) {
                  var13 = new Size(Double.POSITIVE_INFINITY, SizeUnits.PX);
                  var9 = new ParsedValueImpl(var13, (StyleConverter)null);
                  var3 = new ParsedValueImpl(var9, DurationConverter.getInstance());
               } else if ("true".equals(var7)) {
                  var3 = new ParsedValueImpl("true", BooleanConverter.getInstance());
               } else if ("false".equals(var7)) {
                  var3 = new ParsedValueImpl("false", BooleanConverter.getInstance());
               } else {
                  boolean var14 = var12 && this.properties.containsKey(var7);
                  if (var14 || (var3 = this.colorValueOfString(var6)) == null) {
                     var3 = new ParsedValueImpl(var14 ? var7 : var6, (StyleConverter)null, var12 || var14);
                  }
               }
            }
            break;
         case 12:
            return this.parseFunction(var1);
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 26:
            if (var1.nextInSeries == null) {
               var5 = new ParsedValueImpl(this.size(var2), (StyleConverter)null);
               var3 = new ParsedValueImpl(var5, SizeConverter.getInstance());
            } else {
               ParsedValueImpl[] var11 = this.parseSizeSeries(var1);
               var3 = new ParsedValueImpl(var11, SizeConverter.SequenceConverter.getInstance());
            }
            break;
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 44:
         default:
            String var15 = "Unknown token type: '" + var4 + "'";
            this.error(var1, var15);
            break;
         case 37:
            String var8 = var2.getText();

            try {
               var3 = new ParsedValueImpl(Color.web(var8), (StyleConverter)null);
            } catch (IllegalArgumentException var10) {
               this.error(var1, var10.getMessage());
            }
            break;
         case 43:
            return this.parseURI(var1);
         case 45:
         case 46:
            var5 = new ParsedValueImpl(this.size(var2), (StyleConverter)null);
            var3 = new ParsedValueImpl(var5, DurationConverter.getInstance());
      }

      return var3;
   }

   private ParsedValueImpl parseSize(Term var1) throws ParseException {
      if (var1.token == null || !this.isSize(var1.token)) {
         this.error(var1, "Expected '<size>'");
      }

      ParsedValueImpl var2 = null;
      if (var1.token.getType() != 11) {
         Size var3 = this.size(var1.token);
         var2 = new ParsedValueImpl(var3, (StyleConverter)null);
      } else {
         String var4 = var1.token.getText();
         var2 = new ParsedValueImpl(var4, (StyleConverter)null, true);
      }

      return var2;
   }

   private ParsedValueImpl parseColor(Term var1) throws ParseException {
      ParsedValueImpl var2 = null;
      if (var1.token == null || var1.token.getType() != 11 && var1.token.getType() != 37 && var1.token.getType() != 12) {
         this.error(var1, "Expected '<color>'");
      } else {
         var2 = this.parse(var1);
      }

      return var2;
   }

   private ParsedValueImpl rgb(Term var1) throws ParseException {
      String var2 = var1.token != null ? var1.token.getText() : null;
      if (var2 == null || !"rgb".regionMatches(true, 0, var2, 0, 3)) {
         this.error(var1, "Expected 'rgb' or 'rgba'");
      }

      Term var3;
      if ((var3 = var1.firstArg) == null) {
         this.error(var1, "Expected '<number>' or '<percentage>'");
      }

      Token var4;
      if ((var4 = var3.token) == null || var4.getType() != 13 && var4.getType() != 22) {
         this.error(var3, "Expected '<number>' or '<percentage>'");
      }

      var1 = var3;
      if ((var3 = var3.nextArg) == null) {
         this.error(var1, "Expected '<number>' or '<percentage>'");
      }

      Token var5;
      if ((var5 = var3.token) == null || var5.getType() != 13 && var5.getType() != 22) {
         this.error(var3, "Expected '<number>' or '<percentage>'");
      }

      var1 = var3;
      if ((var3 = var3.nextArg) == null) {
         this.error(var1, "Expected '<number>' or '<percentage>'");
      }

      Token var6;
      if ((var6 = var3.token) == null || var6.getType() != 13 && var6.getType() != 22) {
         this.error(var3, "Expected '<number>' or '<percentage>'");
      }

      var1 = var3;
      Token var7;
      if ((var3 = var3.nextArg) != null) {
         if ((var7 = var3.token) == null || var7.getType() != 13) {
            this.error(var3, "Expected '<number>'");
         }
      } else {
         var7 = null;
      }

      int var8 = var4.getType();
      if (var8 != var5.getType() || var8 != var6.getType() || var8 != 13 && var8 != 22) {
         this.error(var1, "Argument type mistmatch");
      }

      String var9 = var4.getText();
      String var10 = var5.getText();
      String var11 = var6.getText();
      double var12 = 0.0;
      double var14 = 0.0;
      double var16 = 0.0;
      if (var8 == 13) {
         var12 = this.clamp(0.0, Double.parseDouble(var9) / 255.0, 1.0);
         var14 = this.clamp(0.0, Double.parseDouble(var10) / 255.0, 1.0);
         var16 = this.clamp(0.0, Double.parseDouble(var11) / 255.0, 1.0);
      } else {
         var12 = this.clamp(0.0, Double.parseDouble(var9.substring(0, var9.length() - 1)) / 100.0, 1.0);
         var14 = this.clamp(0.0, Double.parseDouble(var10.substring(0, var10.length() - 1)) / 100.0, 1.0);
         var16 = this.clamp(0.0, Double.parseDouble(var11.substring(0, var11.length() - 1)) / 100.0, 1.0);
      }

      String var18 = var7 != null ? var7.getText() : null;
      double var19 = var18 != null ? this.clamp(0.0, Double.parseDouble(var18), 1.0) : 1.0;
      return new ParsedValueImpl(Color.color(var12, var14, var16, var19), (StyleConverter)null);
   }

   private ParsedValueImpl hsb(Term var1) throws ParseException {
      String var2 = var1.token != null ? var1.token.getText() : null;
      if (var2 == null || !"hsb".regionMatches(true, 0, var2, 0, 3)) {
         this.error(var1, "Expected 'hsb' or 'hsba'");
      }

      Term var3;
      if ((var3 = var1.firstArg) == null) {
         this.error(var1, "Expected '<number>'");
      }

      Token var4;
      if ((var4 = var3.token) == null || var4.getType() != 13) {
         this.error(var3, "Expected '<number>'");
      }

      var1 = var3;
      if ((var3 = var3.nextArg) == null) {
         this.error(var1, "Expected '<percent>'");
      }

      Token var5;
      if ((var5 = var3.token) == null || var5.getType() != 22) {
         this.error(var3, "Expected '<percent>'");
      }

      var1 = var3;
      if ((var3 = var3.nextArg) == null) {
         this.error(var1, "Expected '<percent>'");
      }

      Token var6;
      if ((var6 = var3.token) == null || var6.getType() != 22) {
         this.error(var3, "Expected '<percent>'");
      }

      Token var7;
      if ((var3 = var3.nextArg) != null) {
         if ((var7 = var3.token) == null || var7.getType() != 13) {
            this.error(var3, "Expected '<number>'");
         }
      } else {
         var7 = null;
      }

      Size var8 = this.size(var4);
      Size var9 = this.size(var5);
      Size var10 = this.size(var6);
      double var11 = var8.pixels();
      double var13 = this.clamp(0.0, var9.pixels(), 1.0);
      double var15 = this.clamp(0.0, var10.pixels(), 1.0);
      Size var17 = var7 != null ? this.size(var7) : null;
      double var18 = var17 != null ? this.clamp(0.0, var17.pixels(), 1.0) : 1.0;
      return new ParsedValueImpl(Color.hsb(var11, var13, var15, var18), (StyleConverter)null);
   }

   private ParsedValueImpl derive(Term var1) throws ParseException {
      String var2 = var1.token != null ? var1.token.getText() : null;
      if (var2 == null || !"derive".regionMatches(true, 0, var2, 0, 6)) {
         this.error(var1, "Expected 'derive'");
      }

      Term var3;
      if ((var3 = var1.firstArg) == null) {
         this.error(var1, "Expected '<color>'");
      }

      ParsedValueImpl var4 = this.parseColor(var3);
      Term var5 = var3;
      if ((var3 = var3.nextArg) == null) {
         this.error(var5, "Expected '<percent'");
      }

      ParsedValueImpl var6 = this.parseSize(var3);
      ParsedValueImpl[] var7 = new ParsedValueImpl[]{var4, var6};
      return new ParsedValueImpl(var7, DeriveColorConverter.getInstance());
   }

   private ParsedValueImpl ladder(Term var1) throws ParseException {
      String var2 = var1.token != null ? var1.token.getText() : null;
      if (var2 == null || !"ladder".regionMatches(true, 0, var2, 0, 6)) {
         this.error(var1, "Expected 'ladder'");
      }

      if (LOGGER.isLoggable(Level.WARNING)) {
         LOGGER.warning(this.formatDeprecatedMessage(var1, "ladder"));
      }

      Term var3;
      if ((var3 = var1.nextInSeries) == null) {
         this.error(var1, "Expected '<color>'");
      }

      ParsedValueImpl var4 = this.parse(var3);
      Term var5 = var3;
      if ((var3 = var3.nextInSeries) == null) {
         this.error(var5, "Expected 'stops'");
      }

      if (var3.token == null || var3.token.getType() != 11 || !"stops".equalsIgnoreCase(var3.token.getText())) {
         this.error(var3, "Expected 'stops'");
      }

      var5 = var3;
      if ((var3 = var3.nextInSeries) == null) {
         this.error(var5, "Expected '(<number>, <color>)'");
      }

      int var6 = 0;
      Term var7 = var3;

      do {
         ++var6;
      } while((var7 = var7.nextInSeries) != null && var7.token != null && var7.token.getType() == 34);

      ParsedValueImpl[] var8 = new ParsedValueImpl[var6 + 1];
      var8[0] = var4;
      int var9 = 1;

      do {
         ParsedValueImpl var10 = this.stop(var3);
         if (var10 != null) {
            var8[var9++] = var10;
         }

         var5 = var3;
      } while((var3 = var3.nextInSeries) != null && var3.token.getType() == 34);

      if (var3 != null) {
         var1.nextInSeries = var3;
      } else {
         var1.nextInSeries = null;
         var1.nextLayer = var5.nextLayer;
      }

      return new ParsedValueImpl(var8, LadderConverter.getInstance());
   }

   private ParsedValueImpl parseLadder(Term var1) throws ParseException {
      String var2 = var1.token != null ? var1.token.getText() : null;
      if (var2 == null || !"ladder".regionMatches(true, 0, var2, 0, 6)) {
         this.error(var1, "Expected 'ladder'");
      }

      Term var3;
      if ((var3 = var1.firstArg) == null) {
         this.error(var1, "Expected '<color>'");
      }

      ParsedValueImpl var4 = this.parse(var3);
      Term var5 = var3;
      if ((var3 = var3.nextArg) == null) {
         this.error(var5, "Expected '<color-stop>[, <color-stop>]+'");
      }

      ParsedValueImpl[] var6 = this.parseColorStops(var3);
      ParsedValueImpl[] var7 = new ParsedValueImpl[var6.length + 1];
      var7[0] = var4;
      System.arraycopy(var6, 0, var7, 1, var6.length);
      return new ParsedValueImpl(var7, LadderConverter.getInstance());
   }

   private ParsedValueImpl stop(Term var1) throws ParseException {
      String var2 = var1.token != null ? var1.token.getText() : null;
      if (var2 == null || !"(".equals(var2)) {
         this.error(var1, "Expected '('");
      }

      Term var3 = null;
      if ((var3 = var1.firstArg) == null) {
         this.error(var1, "Expected '<number>'");
      }

      ParsedValueImpl var4 = this.parseSize(var3);
      Term var5 = var3;
      if ((var3 = var3.nextArg) == null) {
         this.error(var5, "Expected '<color>'");
      }

      ParsedValueImpl var6 = this.parseColor(var3);
      ParsedValueImpl[] var7 = new ParsedValueImpl[]{var4, var6};
      return new ParsedValueImpl(var7, StopConverter.getInstance());
   }

   private ParsedValueImpl[] parseColorStops(Term var1) throws ParseException {
      int var2 = 1;
      Term var3 = var1;

      while(var3 != null) {
         if (var3.nextArg != null) {
            ++var2;
            var3 = var3.nextArg;
         } else {
            if (var3.nextInSeries == null) {
               break;
            }

            var3 = var3.nextInSeries;
         }
      }

      if (var2 < 2) {
         this.error(var1, "Expected '<color-stop>'");
      }

      ParsedValueImpl[] var4 = new ParsedValueImpl[var2];
      Size[] var5 = new Size[var2];
      Arrays.fill(var5, (Object)null);
      Term var6 = var1;
      Object var8 = null;

      for(int var9 = 0; var9 < var2; ++var9) {
         var4[var9] = this.parseColor(var6);
         Term var10 = var6.nextInSeries;
         if (var10 != null) {
            if (this.isSize(var10.token)) {
               var5[var9] = this.size(var10.token);
               if (var8 != null && var8 != var5[var9].getUnits()) {
                  this.error(var10, "Parser unable to handle mixed '<percent>' and '<length>'");
               }
            } else {
               this.error(var6, "Expected '<percent>' or '<length>'");
            }

            var6 = var10.nextArg;
         } else {
            var6 = var6.nextArg;
         }
      }

      if (var5[0] == null) {
         var5[0] = new Size(0.0, SizeUnits.PERCENT);
      }

      if (var5[var2 - 1] == null) {
         var5[var2 - 1] = new Size(100.0, SizeUnits.PERCENT);
      }

      Size var19 = null;

      for(int var20 = 1; var20 < var2; ++var20) {
         Size var11 = var5[var20 - 1];
         if (var11 != null) {
            if (var19 == null || var19.getValue() < var11.getValue()) {
               var19 = var11;
            }

            Size var12 = var5[var20];
            if (var12 != null && var12.getValue() < var19.getValue()) {
               var5[var20] = var19;
            }
         }
      }

      Size var21 = null;
      int var22 = -1;

      for(int var23 = 0; var23 < var2; ++var23) {
         Size var13 = var5[var23];
         if (var13 == null) {
            if (var22 == -1) {
               var22 = var23;
            }
         } else if (var22 <= -1) {
            var21 = var13;
         } else {
            int var14 = var23 - var22;
            double var15 = var21.getValue();

            for(double var17 = (var13.getValue() - var15) / (double)(var14 + 1); var22 < var23; var5[var22++] = new Size(var15, var13.getUnits())) {
               var15 += var17;
            }

            var22 = -1;
            var21 = var13;
         }
      }

      ParsedValueImpl[] var24 = new ParsedValueImpl[var2];

      for(int var25 = 0; var25 < var2; ++var25) {
         var24[var25] = new ParsedValueImpl(new ParsedValueImpl[]{new ParsedValueImpl(var5[var25], (StyleConverter)null), var4[var25]}, StopConverter.getInstance());
      }

      return var24;
   }

   private ParsedValueImpl[] point(Term var1) throws ParseException {
      if (var1.token == null || var1.token.getType() != 34) {
         this.error(var1, "Expected '(<number>, <number>)'");
      }

      String var2 = var1.token.getText();
      if (var2 == null || !"(".equalsIgnoreCase(var2)) {
         this.error(var1, "Expected '('");
      }

      Term var3 = null;
      if ((var3 = var1.firstArg) == null) {
         this.error(var1, "Expected '<number>'");
      }

      ParsedValueImpl var4 = this.parseSize(var3);
      Term var5 = var3;
      if ((var3 = var3.nextArg) == null) {
         this.error(var5, "Expected '<number>'");
      }

      ParsedValueImpl var6 = this.parseSize(var3);
      return new ParsedValueImpl[]{var4, var6};
   }

   private ParsedValueImpl parseFunction(Term var1) throws ParseException {
      String var2 = var1.token != null ? var1.token.getText() : null;
      if (var2 == null) {
         this.error(var1, "Expected function name");
      } else {
         if ("rgb".regionMatches(true, 0, var2, 0, 3)) {
            return this.rgb(var1);
         }

         if ("hsb".regionMatches(true, 0, var2, 0, 3)) {
            return this.hsb(var1);
         }

         if ("derive".regionMatches(true, 0, var2, 0, 6)) {
            return this.derive(var1);
         }

         if ("innershadow".regionMatches(true, 0, var2, 0, 11)) {
            return this.innershadow(var1);
         }

         if ("dropshadow".regionMatches(true, 0, var2, 0, 10)) {
            return this.dropshadow(var1);
         }

         if ("linear-gradient".regionMatches(true, 0, var2, 0, 15)) {
            return this.parseLinearGradient(var1);
         }

         if ("radial-gradient".regionMatches(true, 0, var2, 0, 15)) {
            return this.parseRadialGradient(var1);
         }

         if ("image-pattern".regionMatches(true, 0, var2, 0, 13)) {
            return this.parseImagePattern(var1);
         }

         if ("repeating-image-pattern".regionMatches(true, 0, var2, 0, 23)) {
            return this.parseRepeatingImagePattern(var1);
         }

         if ("ladder".regionMatches(true, 0, var2, 0, 6)) {
            return this.parseLadder(var1);
         }

         if ("region".regionMatches(true, 0, var2, 0, 6)) {
            return this.parseRegion(var1);
         }

         this.error(var1, "Unexpected function '" + var2 + "'");
      }

      return null;
   }

   private ParsedValueImpl blurType(Term var1) throws ParseException {
      if (var1 == null) {
         return null;
      } else {
         if (var1.token == null || var1.token.getType() != 11 || var1.token.getText() == null || var1.token.getText().isEmpty()) {
            this.error(var1, "Expected 'gaussian', 'one-pass-box', 'two-pass-box', or 'three-pass-box'");
         }

         String var2 = var1.token.getText().toLowerCase(Locale.ROOT);
         BlurType var3 = BlurType.THREE_PASS_BOX;
         if ("gaussian".equals(var2)) {
            var3 = BlurType.GAUSSIAN;
         } else if ("one-pass-box".equals(var2)) {
            var3 = BlurType.ONE_PASS_BOX;
         } else if ("two-pass-box".equals(var2)) {
            var3 = BlurType.TWO_PASS_BOX;
         } else if ("three-pass-box".equals(var2)) {
            var3 = BlurType.THREE_PASS_BOX;
         } else {
            this.error(var1, "Expected 'gaussian', 'one-pass-box', 'two-pass-box', or 'three-pass-box'");
         }

         return new ParsedValueImpl(var3.name(), new EnumConverter(BlurType.class));
      }
   }

   private ParsedValueImpl innershadow(Term var1) throws ParseException {
      String var2 = var1.token != null ? var1.token.getText() : null;
      if (!"innershadow".regionMatches(true, 0, var2, 0, 11)) {
         this.error(var1, "Expected 'innershadow'");
      }

      Term var3;
      if ((var3 = var1.firstArg) == null) {
         this.error(var1, "Expected '<blur-type>'");
      }

      ParsedValueImpl var4 = this.blurType(var3);
      Term var5 = var3;
      if ((var3 = var3.nextArg) == null) {
         this.error(var5, "Expected '<color>'");
      }

      ParsedValueImpl var6 = this.parseColor(var3);
      var5 = var3;
      if ((var3 = var3.nextArg) == null) {
         this.error(var5, "Expected '<number>'");
      }

      ParsedValueImpl var7 = this.parseSize(var3);
      var5 = var3;
      if ((var3 = var3.nextArg) == null) {
         this.error(var5, "Expected '<number>'");
      }

      ParsedValueImpl var8 = this.parseSize(var3);
      var5 = var3;
      if ((var3 = var3.nextArg) == null) {
         this.error(var5, "Expected '<number>'");
      }

      ParsedValueImpl var9 = this.parseSize(var3);
      var5 = var3;
      if ((var3 = var3.nextArg) == null) {
         this.error(var5, "Expected '<number>'");
      }

      ParsedValueImpl var10 = this.parseSize(var3);
      ParsedValueImpl[] var11 = new ParsedValueImpl[]{var4, var6, var7, var8, var9, var10};
      return new ParsedValueImpl(var11, EffectConverter.InnerShadowConverter.getInstance());
   }

   private ParsedValueImpl dropshadow(Term var1) throws ParseException {
      String var2 = var1.token != null ? var1.token.getText() : null;
      if (!"dropshadow".regionMatches(true, 0, var2, 0, 10)) {
         this.error(var1, "Expected 'dropshadow'");
      }

      Term var3;
      if ((var3 = var1.firstArg) == null) {
         this.error(var1, "Expected '<blur-type>'");
      }

      ParsedValueImpl var4 = this.blurType(var3);
      Term var5 = var3;
      if ((var3 = var3.nextArg) == null) {
         this.error(var5, "Expected '<color>'");
      }

      ParsedValueImpl var6 = this.parseColor(var3);
      var5 = var3;
      if ((var3 = var3.nextArg) == null) {
         this.error(var5, "Expected '<number>'");
      }

      ParsedValueImpl var7 = this.parseSize(var3);
      var5 = var3;
      if ((var3 = var3.nextArg) == null) {
         this.error(var5, "Expected '<number>'");
      }

      ParsedValueImpl var8 = this.parseSize(var3);
      var5 = var3;
      if ((var3 = var3.nextArg) == null) {
         this.error(var5, "Expected '<number>'");
      }

      ParsedValueImpl var9 = this.parseSize(var3);
      var5 = var3;
      if ((var3 = var3.nextArg) == null) {
         this.error(var5, "Expected '<number>'");
      }

      ParsedValueImpl var10 = this.parseSize(var3);
      ParsedValueImpl[] var11 = new ParsedValueImpl[]{var4, var6, var7, var8, var9, var10};
      return new ParsedValueImpl(var11, EffectConverter.DropShadowConverter.getInstance());
   }

   private ParsedValueImpl cycleMethod(Term var1) {
      CycleMethod var2 = null;
      if (var1 != null && var1.token.getType() == 11) {
         String var3 = var1.token.getText().toLowerCase(Locale.ROOT);
         if ("repeat".equals(var3)) {
            var2 = CycleMethod.REPEAT;
         } else if ("reflect".equals(var3)) {
            var2 = CycleMethod.REFLECT;
         } else if ("no-cycle".equals(var3)) {
            var2 = CycleMethod.NO_CYCLE;
         }
      }

      return var2 != null ? new ParsedValueImpl(var2.name(), new EnumConverter(CycleMethod.class)) : null;
   }

   private ParsedValueImpl linearGradient(Term var1) throws ParseException {
      String var2 = var1.token != null ? var1.token.getText() : null;
      if (var2 == null || !"linear".equalsIgnoreCase(var2)) {
         this.error(var1, "Expected 'linear'");
      }

      if (LOGGER.isLoggable(Level.WARNING)) {
         LOGGER.warning(this.formatDeprecatedMessage(var1, "linear gradient"));
      }

      Term var3;
      if ((var3 = var1.nextInSeries) == null) {
         this.error(var1, "Expected '(<number>, <number>)'");
      }

      ParsedValueImpl[] var4 = this.point(var3);
      Term var5 = var3;
      if ((var3 = var3.nextInSeries) == null) {
         this.error(var5, "Expected 'to'");
      }

      if (var3.token == null || var3.token.getType() != 11 || !"to".equalsIgnoreCase(var3.token.getText())) {
         this.error(var1, "Expected 'to'");
      }

      var5 = var3;
      if ((var3 = var3.nextInSeries) == null) {
         this.error(var5, "Expected '(<number>, <number>)'");
      }

      ParsedValueImpl[] var6 = this.point(var3);
      var5 = var3;
      if ((var3 = var3.nextInSeries) == null) {
         this.error(var5, "Expected 'stops'");
      }

      if (var3.token == null || var3.token.getType() != 11 || !"stops".equalsIgnoreCase(var3.token.getText())) {
         this.error(var3, "Expected 'stops'");
      }

      var5 = var3;
      if ((var3 = var3.nextInSeries) == null) {
         this.error(var5, "Expected '(<number>, <number>)'");
      }

      int var7 = 0;
      Term var8 = var3;

      do {
         ++var7;
      } while((var8 = var8.nextInSeries) != null && var8.token != null && var8.token.getType() == 34);

      ParsedValueImpl[] var9 = new ParsedValueImpl[var7];
      int var10 = 0;

      ParsedValueImpl var11;
      do {
         var11 = this.stop(var3);
         if (var11 != null) {
            var9[var10++] = var11;
         }

         var5 = var3;
      } while((var3 = var3.nextInSeries) != null && var3.token.getType() == 34);

      var11 = this.cycleMethod(var3);
      if (var11 == null) {
         var11 = new ParsedValueImpl(CycleMethod.NO_CYCLE.name(), new EnumConverter(CycleMethod.class));
         if (var3 != null) {
            var1.nextInSeries = var3;
         } else {
            var1.nextInSeries = null;
            var1.nextLayer = var5.nextLayer;
         }
      } else {
         var1.nextInSeries = var3.nextInSeries;
         var1.nextLayer = var3.nextLayer;
      }

      ParsedValueImpl[] var12 = new ParsedValueImpl[5 + var9.length];
      int var13 = 0;
      var12[var13++] = var4 != null ? var4[0] : null;
      var12[var13++] = var4 != null ? var4[1] : null;
      var12[var13++] = var6 != null ? var6[0] : null;
      var12[var13++] = var6 != null ? var6[1] : null;
      var12[var13++] = var11;

      for(int var14 = 0; var14 < var9.length; ++var14) {
         var12[var13++] = var9[var14];
      }

      return new ParsedValueImpl(var12, PaintConverter.LinearGradientConverter.getInstance());
   }

   private ParsedValueImpl parseLinearGradient(Term var1) throws ParseException {
      String var2 = var1.token != null ? var1.token.getText() : null;
      if (!"linear-gradient".regionMatches(true, 0, var2, 0, 15)) {
         this.error(var1, "Expected 'linear-gradient'");
      }

      Term var3;
      if ((var3 = var1.firstArg) == null || var3.token == null || var3.token.getText().isEmpty()) {
         this.error(var1, "Expected 'from <point> to <point>' or 'to <side-or-corner>' or '<cycle-method>' or '<color-stop>'");
      }

      Term var4 = var3;
      ParsedValueImpl[] var5 = null;
      ParsedValueImpl[] var6 = null;
      int var10;
      if ("from".equalsIgnoreCase(var3.token.getText())) {
         var4 = var3;
         if ((var3 = var3.nextInSeries) == null) {
            this.error(var4, "Expected '<point>'");
         }

         ParsedValueImpl var7 = this.parseSize(var3);
         var4 = var3;
         if ((var3 = var3.nextInSeries) == null) {
            this.error(var4, "Expected '<point>'");
         }

         ParsedValueImpl var8 = this.parseSize(var3);
         var5 = new ParsedValueImpl[]{var7, var8};
         var4 = var3;
         if ((var3 = var3.nextInSeries) == null) {
            this.error(var4, "Expected 'to'");
         }

         if (var3.token == null || var3.token.getType() != 11 || !"to".equalsIgnoreCase(var3.token.getText())) {
            this.error(var4, "Expected 'to'");
         }

         var4 = var3;
         if ((var3 = var3.nextInSeries) == null) {
            this.error(var4, "Expected '<point>'");
         }

         var7 = this.parseSize(var3);
         var4 = var3;
         if ((var3 = var3.nextInSeries) == null) {
            this.error(var4, "Expected '<point>'");
         }

         var8 = this.parseSize(var3);
         var6 = new ParsedValueImpl[]{var7, var8};
         var4 = var3;
         var3 = var3.nextArg;
      } else if ("to".equalsIgnoreCase(var3.token.getText())) {
         var4 = var3;
         if ((var3 = var3.nextInSeries) == null || var3.token == null || var3.token.getType() != 11 || var3.token.getText().isEmpty()) {
            this.error(var4, "Expected '<side-or-corner>'");
         }

         byte var13 = 0;
         byte var14 = 0;
         byte var9 = 0;
         var10 = 0;
         String var11 = var3.token.getText().toLowerCase(Locale.ROOT);
         if ("top".equals(var11)) {
            var14 = 100;
            var10 = 0;
         } else if ("bottom".equals(var11)) {
            var14 = 0;
            var10 = 100;
         } else if ("right".equals(var11)) {
            var13 = 0;
            var9 = 100;
         } else if ("left".equals(var11)) {
            var13 = 100;
            var9 = 0;
         } else {
            this.error(var3, "Invalid '<side-or-corner>'");
         }

         var4 = var3;
         if (var3.nextInSeries != null) {
            var3 = var3.nextInSeries;
            if (var3.token != null && var3.token.getType() == 11 && !var3.token.getText().isEmpty()) {
               String var12 = var3.token.getText().toLowerCase(Locale.ROOT);
               if ("right".equals(var12) && var13 == 0 && var9 == 0) {
                  var13 = 0;
                  var9 = 100;
               } else if ("left".equals(var12) && var13 == 0 && var9 == 0) {
                  var13 = 100;
                  var9 = 0;
               } else if ("top".equals(var12) && var14 == 0 && var10 == 0) {
                  var14 = 100;
                  var10 = 0;
               } else if ("bottom".equals(var12) && var14 == 0 && var10 == 0) {
                  var14 = 0;
                  var10 = 100;
               } else {
                  this.error(var3, "Invalid '<side-or-corner>'");
               }
            } else {
               this.error(var4, "Expected '<side-or-corner>'");
            }
         }

         var5 = new ParsedValueImpl[]{new ParsedValueImpl(new Size((double)var13, SizeUnits.PERCENT), (StyleConverter)null), new ParsedValueImpl(new Size((double)var14, SizeUnits.PERCENT), (StyleConverter)null)};
         var6 = new ParsedValueImpl[]{new ParsedValueImpl(new Size((double)var9, SizeUnits.PERCENT), (StyleConverter)null), new ParsedValueImpl(new Size((double)var10, SizeUnits.PERCENT), (StyleConverter)null)};
         var4 = var3;
         var3 = var3.nextArg;
      }

      if (var5 == null && var6 == null) {
         var5 = new ParsedValueImpl[]{new ParsedValueImpl(new Size(0.0, SizeUnits.PERCENT), (StyleConverter)null), new ParsedValueImpl(new Size(0.0, SizeUnits.PERCENT), (StyleConverter)null)};
         var6 = new ParsedValueImpl[]{new ParsedValueImpl(new Size(0.0, SizeUnits.PERCENT), (StyleConverter)null), new ParsedValueImpl(new Size(100.0, SizeUnits.PERCENT), (StyleConverter)null)};
      }

      if (var3 == null || var3.token == null || var3.token.getText().isEmpty()) {
         this.error(var4, "Expected '<cycle-method>' or '<color-stop>'");
      }

      CycleMethod var15 = CycleMethod.NO_CYCLE;
      if ("reflect".equalsIgnoreCase(var3.token.getText())) {
         var15 = CycleMethod.REFLECT;
         var4 = var3;
         var3 = var3.nextArg;
      } else if ("repeat".equalsIgnoreCase(var3.token.getText())) {
         var15 = CycleMethod.REFLECT;
         var4 = var3;
         var3 = var3.nextArg;
      }

      if (var3 == null || var3.token == null || var3.token.getText().isEmpty()) {
         this.error(var4, "Expected '<color-stop>'");
      }

      ParsedValueImpl[] var16 = this.parseColorStops(var3);
      ParsedValueImpl[] var18 = new ParsedValueImpl[5 + var16.length];
      var10 = 0;
      var18[var10++] = var5 != null ? var5[0] : null;
      var18[var10++] = var5 != null ? var5[1] : null;
      var18[var10++] = var6 != null ? var6[0] : null;
      var18[var10++] = var6 != null ? var6[1] : null;
      var18[var10++] = new ParsedValueImpl(var15.name(), new EnumConverter(CycleMethod.class));

      for(int var17 = 0; var17 < var16.length; ++var17) {
         var18[var10++] = var16[var17];
      }

      return new ParsedValueImpl(var18, PaintConverter.LinearGradientConverter.getInstance());
   }

   private ParsedValueImpl radialGradient(Term var1) throws ParseException {
      String var2 = var1.token != null ? var1.token.getText() : null;
      if (var2 == null || !"radial".equalsIgnoreCase(var2)) {
         this.error(var1, "Expected 'radial'");
      }

      if (LOGGER.isLoggable(Level.WARNING)) {
         LOGGER.warning(this.formatDeprecatedMessage(var1, "radial gradient"));
      }

      Term var3;
      if ((var3 = var1.nextInSeries) == null) {
         this.error(var1, "Expected 'focus-angle <number>', 'focus-distance <number>', 'center (<number>,<number>)' or '<size>'");
      }

      if (var3.token == null) {
         this.error(var3, "Expected 'focus-angle <number>', 'focus-distance <number>', 'center (<number>,<number>)' or '<size>'");
      }

      ParsedValueImpl var5 = null;
      Term var4;
      if (var3.token.getType() == 11) {
         String var6 = var3.token.getText().toLowerCase(Locale.ROOT);
         if ("focus-angle".equals(var6)) {
            var4 = var3;
            if ((var3 = var3.nextInSeries) == null) {
               this.error(var4, "Expected '<number>'");
            }

            if (var3.token == null) {
               this.error(var4, "Expected '<number>'");
            }

            var5 = this.parseSize(var3);
            var4 = var3;
            if ((var3 = var3.nextInSeries) == null) {
               this.error(var4, "Expected 'focus-distance <number>', 'center (<number>,<number>)' or '<size>'");
            }

            if (var3.token == null) {
               this.error(var3, "Expected 'focus-distance <number>', 'center (<number>,<number>)' or '<size>'");
            }
         }
      }

      ParsedValueImpl var17 = null;
      if (var3.token.getType() == 11) {
         String var7 = var3.token.getText().toLowerCase(Locale.ROOT);
         if ("focus-distance".equals(var7)) {
            var4 = var3;
            if ((var3 = var3.nextInSeries) == null) {
               this.error(var4, "Expected '<number>'");
            }

            if (var3.token == null) {
               this.error(var4, "Expected '<number>'");
            }

            var17 = this.parseSize(var3);
            var4 = var3;
            if ((var3 = var3.nextInSeries) == null) {
               this.error(var4, "Expected  'center (<number>,<number>)' or '<size>'");
            }

            if (var3.token == null) {
               this.error(var3, "Expected  'center (<number>,<number>)' or '<size>'");
            }
         }
      }

      ParsedValueImpl[] var18 = null;
      if (var3.token.getType() == 11) {
         String var8 = var3.token.getText().toLowerCase(Locale.ROOT);
         if ("center".equals(var8)) {
            var4 = var3;
            if ((var3 = var3.nextInSeries) == null) {
               this.error(var4, "Expected '(<number>,<number>)'");
            }

            if (var3.token == null || var3.token.getType() != 34) {
               this.error(var3, "Expected '(<number>,<number>)'");
            }

            var18 = this.point(var3);
            var4 = var3;
            if ((var3 = var3.nextInSeries) == null) {
               this.error(var4, "Expected '<size>'");
            }

            if (var3.token == null) {
               this.error(var3, "Expected '<size>'");
            }
         }
      }

      ParsedValueImpl var19 = this.parseSize(var3);
      var4 = var3;
      if ((var3 = var3.nextInSeries) == null) {
         this.error(var4, "Expected 'stops' keyword");
      }

      if (var3.token == null || var3.token.getType() != 11) {
         this.error(var3, "Expected 'stops' keyword");
      }

      if (!"stops".equalsIgnoreCase(var3.token.getText())) {
         this.error(var3, "Expected 'stops'");
      }

      var4 = var3;
      if ((var3 = var3.nextInSeries) == null) {
         this.error(var4, "Expected '(<number>, <number>)'");
      }

      int var9 = 0;
      Term var10 = var3;

      do {
         ++var9;
      } while((var10 = var10.nextInSeries) != null && var10.token != null && var10.token.getType() == 34);

      ParsedValueImpl[] var11 = new ParsedValueImpl[var9];
      int var12 = 0;

      ParsedValueImpl var13;
      do {
         var13 = this.stop(var3);
         if (var13 != null) {
            var11[var12++] = var13;
         }

         var4 = var3;
      } while((var3 = var3.nextInSeries) != null && var3.token.getType() == 34);

      var13 = this.cycleMethod(var3);
      if (var13 == null) {
         var13 = new ParsedValueImpl(CycleMethod.NO_CYCLE.name(), new EnumConverter(CycleMethod.class));
         if (var3 != null) {
            var1.nextInSeries = var3;
         } else {
            var1.nextInSeries = null;
            var1.nextLayer = var4.nextLayer;
         }
      } else {
         var1.nextInSeries = var3.nextInSeries;
         var1.nextLayer = var3.nextLayer;
      }

      ParsedValueImpl[] var14 = new ParsedValueImpl[6 + var11.length];
      int var15 = 0;
      var14[var15++] = var5;
      var14[var15++] = var17;
      var14[var15++] = var18 != null ? var18[0] : null;
      var14[var15++] = var18 != null ? var18[1] : null;
      var14[var15++] = var19;
      var14[var15++] = var13;

      for(int var16 = 0; var16 < var11.length; ++var16) {
         var14[var15++] = var11[var16];
      }

      return new ParsedValueImpl(var14, PaintConverter.RadialGradientConverter.getInstance());
   }

   private ParsedValueImpl parseRadialGradient(Term var1) throws ParseException {
      String var2 = var1.token != null ? var1.token.getText() : null;
      if (!"radial-gradient".regionMatches(true, 0, var2, 0, 15)) {
         this.error(var1, "Expected 'radial-gradient'");
      }

      Term var3;
      if ((var3 = var1.firstArg) == null || var3.token == null || var3.token.getText().isEmpty()) {
         this.error(var1, "Expected 'focus-angle <angle>' or 'focus-distance <percentage>' or 'center <point>' or 'radius [<length> | <percentage>]'");
      }

      Term var4 = var3;
      ParsedValueImpl var5 = null;
      ParsedValueImpl var6 = null;
      ParsedValueImpl[] var7 = null;
      ParsedValueImpl var8 = null;
      Size var9;
      if ("focus-angle".equalsIgnoreCase(var3.token.getText())) {
         var4 = var3;
         if ((var3 = var3.nextInSeries) == null || !this.isSize(var3.token)) {
            this.error(var4, "Expected '<angle>'");
         }

         var9 = this.size(var3.token);
         switch (var9.getUnits()) {
            default:
               this.error(var3, "Expected [deg | rad | grad | turn ]");
            case DEG:
            case RAD:
            case GRAD:
            case TURN:
            case PX:
               var5 = new ParsedValueImpl(var9, (StyleConverter)null);
               var4 = var3;
               if ((var3 = var3.nextArg) == null) {
                  this.error(var4, "Expected 'focus-distance <percentage>' or 'center <point>' or 'radius [<length> | <percentage>]'");
               }
         }
      }

      if ("focus-distance".equalsIgnoreCase(var3.token.getText())) {
         var4 = var3;
         if ((var3 = var3.nextInSeries) == null || !this.isSize(var3.token)) {
            this.error(var4, "Expected '<percentage>'");
         }

         var9 = this.size(var3.token);
         switch (var9.getUnits()) {
            default:
               this.error(var3, "Expected '%'");
            case PERCENT:
               var6 = new ParsedValueImpl(var9, (StyleConverter)null);
               var4 = var3;
               if ((var3 = var3.nextArg) == null) {
                  this.error(var4, "Expected 'center <center>' or 'radius <length>'");
               }
         }
      }

      if ("center".equalsIgnoreCase(var3.token.getText())) {
         var4 = var3;
         if ((var3 = var3.nextInSeries) == null) {
            this.error(var4, "Expected '<point>'");
         }

         ParsedValueImpl var14 = this.parseSize(var3);
         var4 = var3;
         if ((var3 = var3.nextInSeries) == null) {
            this.error(var4, "Expected '<point>'");
         }

         ParsedValueImpl var10 = this.parseSize(var3);
         var7 = new ParsedValueImpl[]{var14, var10};
         var4 = var3;
         if ((var3 = var3.nextArg) == null) {
            this.error(var4, "Expected 'radius [<length> | <percentage>]'");
         }
      }

      if ("radius".equalsIgnoreCase(var3.token.getText())) {
         var4 = var3;
         if ((var3 = var3.nextInSeries) == null || !this.isSize(var3.token)) {
            this.error(var4, "Expected '[<length> | <percentage>]'");
         }

         var8 = this.parseSize(var3);
         var4 = var3;
         if ((var3 = var3.nextArg) == null) {
            this.error(var4, "Expected 'radius [<length> | <percentage>]'");
         }
      }

      CycleMethod var15 = CycleMethod.NO_CYCLE;
      if ("reflect".equalsIgnoreCase(var3.token.getText())) {
         var15 = CycleMethod.REFLECT;
         var4 = var3;
         var3 = var3.nextArg;
      } else if ("repeat".equalsIgnoreCase(var3.token.getText())) {
         var15 = CycleMethod.REFLECT;
         var4 = var3;
         var3 = var3.nextArg;
      }

      if (var3 == null || var3.token == null || var3.token.getText().isEmpty()) {
         this.error(var4, "Expected '<color-stop>'");
      }

      ParsedValueImpl[] var16 = this.parseColorStops(var3);
      ParsedValueImpl[] var11 = new ParsedValueImpl[6 + var16.length];
      int var12 = 0;
      var11[var12++] = var5;
      var11[var12++] = var6;
      var11[var12++] = var7 != null ? var7[0] : null;
      var11[var12++] = var7 != null ? var7[1] : null;
      var11[var12++] = var8;
      var11[var12++] = new ParsedValueImpl(var15.name(), new EnumConverter(CycleMethod.class));

      for(int var13 = 0; var13 < var16.length; ++var13) {
         var11[var12++] = var16[var13];
      }

      return new ParsedValueImpl(var11, PaintConverter.RadialGradientConverter.getInstance());
   }

   private ParsedValueImpl parseImagePattern(Term var1) throws ParseException {
      String var2 = var1.token != null ? var1.token.getText() : null;
      if (!"image-pattern".regionMatches(true, 0, var2, 0, 13)) {
         this.error(var1, "Expected 'image-pattern'");
      }

      Term var3;
      if ((var3 = var1.firstArg) == null || var3.token == null || var3.token.getText().isEmpty()) {
         this.error(var1, "Expected '<uri-string>'");
      }

      Term var4 = var3;
      String var5 = var3.token.getText();
      ParsedValueImpl[] var6 = new ParsedValueImpl[]{new ParsedValueImpl(var5, StringConverter.getInstance()), null};
      ParsedValueImpl var7 = new ParsedValueImpl(var6, URLConverter.getInstance());
      if (var3.nextArg == null) {
         ParsedValueImpl[] var14 = new ParsedValueImpl[]{var7};
         return new ParsedValueImpl(var14, PaintConverter.ImagePatternConverter.getInstance());
      } else {
         if ((var3 = var3.nextArg) == null) {
            this.error(var4, "Expected '<size>'");
         }

         ParsedValueImpl var9 = this.parseSize(var3);
         var4 = var3;
         if ((var3 = var3.nextArg) == null) {
            this.error(var4, "Expected '<size>'");
         }

         ParsedValueImpl var10 = this.parseSize(var3);
         var4 = var3;
         if ((var3 = var3.nextArg) == null) {
            this.error(var4, "Expected '<size>'");
         }

         ParsedValueImpl var11 = this.parseSize(var3);
         var4 = var3;
         if ((var3 = var3.nextArg) == null) {
            this.error(var4, "Expected '<size>'");
         }

         ParsedValueImpl var12 = this.parseSize(var3);
         ParsedValueImpl[] var13;
         if (var3.nextArg == null) {
            var13 = new ParsedValueImpl[]{var7, var9, var10, var11, var12};
            return new ParsedValueImpl(var13, PaintConverter.ImagePatternConverter.getInstance());
         } else {
            var4 = var3;
            if ((var3 = var3.nextArg) == null) {
               this.error(var4, "Expected '<boolean>'");
            }

            Token var8;
            if ((var8 = var3.token) == null || var8.getText() == null) {
               this.error(var3, "Expected '<boolean>'");
            }

            var13 = new ParsedValueImpl[]{var7, var9, var10, var11, var12, new ParsedValueImpl(Boolean.parseBoolean(var8.getText()), (StyleConverter)null)};
            return new ParsedValueImpl(var13, PaintConverter.ImagePatternConverter.getInstance());
         }
      }
   }

   private ParsedValueImpl parseRepeatingImagePattern(Term var1) throws ParseException {
      String var2 = var1.token != null ? var1.token.getText() : null;
      if (!"repeating-image-pattern".regionMatches(true, 0, var2, 0, 23)) {
         this.error(var1, "Expected 'repeating-image-pattern'");
      }

      Term var3;
      if ((var3 = var1.firstArg) == null || var3.token == null || var3.token.getText().isEmpty()) {
         this.error(var1, "Expected '<uri-string>'");
      }

      String var4 = var3.token.getText();
      ParsedValueImpl[] var5 = new ParsedValueImpl[]{new ParsedValueImpl(var4, StringConverter.getInstance()), null};
      ParsedValueImpl var6 = new ParsedValueImpl(var5, URLConverter.getInstance());
      ParsedValueImpl[] var7 = new ParsedValueImpl[]{var6};
      return new ParsedValueImpl(var7, PaintConverter.RepeatingImagePatternConverter.getInstance());
   }

   private ParsedValueImpl parsePaintLayers(Term var1) throws ParseException {
      int var2 = this.numberOfLayers(var1);
      ParsedValueImpl[] var3 = new ParsedValueImpl[var2];
      Term var4 = var1;
      int var5 = 0;

      do {
         if (var4.token == null || var4.token.getText() == null || var4.token.getText().isEmpty()) {
            this.error(var4, "Expected '<paint>'");
         }

         var3[var5++] = this.parse(var4);
         var4 = this.nextLayer(var4);
      } while(var4 != null);

      return new ParsedValueImpl(var3, PaintConverter.SequenceConverter.getInstance());
   }

   private ParsedValueImpl[] parseSize1to4(Term var1) throws ParseException {
      Term var2 = var1;
      ParsedValueImpl[] var3 = new ParsedValueImpl[4];

      int var4;
      for(var4 = 0; var4 < 4 && var2 != null; var2 = var2.nextInSeries) {
         var3[var4++] = this.parseSize(var2);
      }

      if (var4 < 2) {
         var3[1] = var3[0];
      }

      if (var4 < 3) {
         var3[2] = var3[0];
      }

      if (var4 < 4) {
         var3[3] = var3[1];
      }

      return var3;
   }

   private ParsedValueImpl parseInsetsLayers(Term var1) throws ParseException {
      int var2 = this.numberOfLayers(var1);
      Term var3 = var1;
      int var4 = 0;

      ParsedValueImpl[] var5;
      for(var5 = new ParsedValueImpl[var2]; var3 != null; var3 = this.nextLayer(var3)) {
         ParsedValueImpl[] var6 = this.parseSize1to4(var3);

         for(var5[var4++] = new ParsedValueImpl(var6, InsetsConverter.getInstance()); var3.nextInSeries != null; var3 = var3.nextInSeries) {
         }
      }

      return new ParsedValueImpl(var5, InsetsConverter.SequenceConverter.getInstance());
   }

   private ParsedValueImpl parseInsetsLayer(Term var1) throws ParseException {
      Term var2 = var1;

      ParsedValueImpl var3;
      for(var3 = null; var2 != null; var2 = this.nextLayer(var2)) {
         ParsedValueImpl[] var4 = this.parseSize1to4(var2);

         for(var3 = new ParsedValueImpl(var4, InsetsConverter.getInstance()); var2.nextInSeries != null; var2 = var2.nextInSeries) {
         }
      }

      return var3;
   }

   private ParsedValueImpl parseMarginsLayers(Term var1) throws ParseException {
      int var2 = this.numberOfLayers(var1);
      Term var3 = var1;
      int var4 = 0;

      ParsedValueImpl[] var5;
      for(var5 = new ParsedValueImpl[var2]; var3 != null; var3 = this.nextLayer(var3)) {
         ParsedValueImpl[] var6 = this.parseSize1to4(var3);

         for(var5[var4++] = new ParsedValueImpl(var6, Margins.Converter.getInstance()); var3.nextInSeries != null; var3 = var3.nextInSeries) {
         }
      }

      return new ParsedValueImpl(var5, Margins.SequenceConverter.getInstance());
   }

   private ParsedValueImpl[] parseSizeSeries(Term var1) throws ParseException {
      if (var1.token == null) {
         this.error(var1, "Parse error");
      }

      ArrayList var2 = new ArrayList();

      for(Term var3 = var1; var3 != null; var3 = var3.nextInSeries) {
         Token var4 = var3.token;
         int var5 = var4.getType();
         switch (var5) {
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
               ParsedValueImpl var6 = new ParsedValueImpl(this.size(var4), (StyleConverter)null);
               var2.add(var6);
               break;
            default:
               this.error(var1, "expected series of <size>");
         }
      }

      return (ParsedValueImpl[])var2.toArray(new ParsedValueImpl[var2.size()]);
   }

   private ParsedValueImpl parseCornerRadius(Term var1) throws ParseException {
      int var2 = this.numberOfLayers(var1);
      Term var3 = var1;
      int var4 = 0;

      ParsedValueImpl[] var5;
      Term var14;
      for(var5 = new ParsedValueImpl[var2]; var3 != null; var3 = this.nextLayer(var14)) {
         int var6 = 0;

         Term var7;
         for(var7 = var3; var7 != null; var7 = var7.nextInSeries) {
            if (var7.token.getType() == 32) {
               var7 = var7.nextInSeries;
               break;
            }

            ++var6;
         }

         int var8;
         for(var8 = 0; var7 != null; var7 = var7.nextInSeries) {
            if (var7.token.getType() == 32) {
               this.error(var7, "unexpected SOLIDUS");
               break;
            }

            ++var8;
         }

         if (var6 == 0 || var6 > 4 || var8 > 4) {
            this.error(var1, "expected [<length>|<percentage>]{1,4} [/ [<length>|<percentage>]{1,4}]?");
         }

         int var9 = 0;
         ParsedValueImpl[][] var10 = new ParsedValueImpl[2][4];
         ParsedValueImpl var11 = new ParsedValueImpl(new Size(0.0, SizeUnits.PX), (StyleConverter)null);

         int var12;
         for(var12 = 0; var12 < 4; ++var12) {
            var10[0][var12] = var11;
            var10[1][var12] = var11;
         }

         var12 = 0;
         int var13 = 0;

         for(var14 = var3; var12 <= 4 && var13 <= 4 && var3 != null; var3 = var3.nextInSeries) {
            if (var3.token.getType() == 32) {
               ++var9;
            } else {
               ParsedValueImpl var15 = this.parseSize(var3);
               if (var9 == 0) {
                  var10[var9][var12++] = var15;
               } else {
                  var10[var9][var13++] = var15;
               }
            }

            var14 = var3;
         }

         if (var12 != 0) {
            if (var12 < 2) {
               var10[0][1] = var10[0][0];
            }

            if (var12 < 3) {
               var10[0][2] = var10[0][0];
            }

            if (var12 < 4) {
               var10[0][3] = var10[0][1];
            }
         } else {
            assert false;
         }

         if (var13 != 0) {
            if (var13 < 2) {
               var10[1][1] = var10[1][0];
            }

            if (var13 < 3) {
               var10[1][2] = var10[1][0];
            }

            if (var13 < 4) {
               var10[1][3] = var10[1][1];
            }
         } else {
            var10[1][0] = var10[0][0];
            var10[1][1] = var10[0][1];
            var10[1][2] = var10[0][2];
            var10[1][3] = var10[0][3];
         }

         if (var11.equals(var10[0][0]) || var11.equals(var10[1][0])) {
            var10[1][0] = var10[0][0] = var11;
         }

         if (var11.equals(var10[0][1]) || var11.equals(var10[1][1])) {
            var10[1][1] = var10[0][1] = var11;
         }

         if (var11.equals(var10[0][2]) || var11.equals(var10[1][2])) {
            var10[1][2] = var10[0][2] = var11;
         }

         if (var11.equals(var10[0][3]) || var11.equals(var10[1][3])) {
            var10[1][3] = var10[0][3] = var11;
         }

         var5[var4++] = new ParsedValueImpl(var10, (StyleConverter)null);
      }

      return new ParsedValueImpl(var5, CornerRadiiConverter.getInstance());
   }

   private static boolean isPositionKeyWord(String var0) {
      return "center".equalsIgnoreCase(var0) || "top".equalsIgnoreCase(var0) || "bottom".equalsIgnoreCase(var0) || "left".equalsIgnoreCase(var0) || "right".equalsIgnoreCase(var0);
   }

   private ParsedValueImpl parseBackgroundPosition(Term var1) throws ParseException {
      if (var1.token == null || var1.token.getText() == null || var1.token.getText().isEmpty()) {
         this.error(var1, "Expected '<bg-position>'");
      }

      Term var2 = var1;
      Token var3 = var1.token;
      Term var4 = var1.nextInSeries;
      Token var5 = var4 != null ? var4.token : null;
      Term var6 = var4 != null ? var4.nextInSeries : null;
      Token var7 = var6 != null ? var6.token : null;
      Term var8 = var6 != null ? var6.nextInSeries : null;
      Token var9 = var8 != null ? var8.token : null;
      if (var3 != null && var5 != null && var7 == null && var9 == null) {
         String var18 = var3.getText();
         String var21 = var5.getText();
         if (("top".equals(var18) || "bottom".equals(var18)) && ("left".equals(var21) || "right".equals(var21) || "center".equals(var21))) {
            Token var12 = var5;
            var5 = var3;
            var3 = var12;
            Term var20 = var4;
            var4 = var1;
            var2 = var20;
         }
      } else if (var3 != null && var5 != null && var7 != null) {
         Term[] var10 = null;
         Token[] var11 = null;
         if (var9 != null) {
            if (("top".equals(var3.getText()) || "bottom".equals(var3.getText())) && ("left".equals(var7.getText()) || "right".equals(var7.getText()))) {
               var10 = new Term[]{var6, var8, var1, var4};
               var11 = new Token[]{var7, var9, var3, var5};
            }
         } else if ("top".equals(var3.getText()) || "bottom".equals(var3.getText())) {
            if (!"left".equals(var5.getText()) && !"right".equals(var5.getText())) {
               var10 = new Term[]{var6, var1, var4, null};
               var11 = new Token[]{var7, var3, var5, null};
            } else {
               var10 = new Term[]{var4, var6, var1, null};
               var11 = new Token[]{var5, var7, var3, null};
            }
         }

         if (var10 != null) {
            var2 = var10[0];
            var4 = var10[1];
            var6 = var10[2];
            var8 = var10[3];
            var3 = var11[0];
            var5 = var11[1];
            var7 = var11[2];
            var9 = var11[3];
         }
      }

      ParsedValueImpl var13;
      ParsedValueImpl var22;
      ParsedValueImpl var23;
      ParsedValueImpl var19 = var23 = var22 = var13 = ZERO_PERCENT;
      if (var3 == null && var5 == null && var7 == null && var9 == null) {
         this.error(var1, "No value found for background-position");
      } else {
         String var14;
         if (var3 != null && var5 == null && var7 == null && var9 == null) {
            var14 = var3.getText();
            if ("center".equals(var14)) {
               var13 = FIFTY_PERCENT;
               var23 = ZERO_PERCENT;
               var19 = FIFTY_PERCENT;
               var22 = ZERO_PERCENT;
            } else if ("left".equals(var14)) {
               var13 = ZERO_PERCENT;
               var23 = ZERO_PERCENT;
               var19 = FIFTY_PERCENT;
               var22 = ZERO_PERCENT;
            } else if ("right".equals(var14)) {
               var13 = ONE_HUNDRED_PERCENT;
               var23 = ZERO_PERCENT;
               var19 = FIFTY_PERCENT;
               var22 = ZERO_PERCENT;
            } else if ("top".equals(var14)) {
               var13 = FIFTY_PERCENT;
               var23 = ZERO_PERCENT;
               var19 = ZERO_PERCENT;
               var22 = ZERO_PERCENT;
            } else if ("bottom".equals(var14)) {
               var13 = FIFTY_PERCENT;
               var23 = ZERO_PERCENT;
               var19 = ONE_HUNDRED_PERCENT;
               var22 = ZERO_PERCENT;
            } else {
               var13 = this.parseSize(var2);
               var23 = ZERO_PERCENT;
               var19 = FIFTY_PERCENT;
               var22 = ZERO_PERCENT;
            }
         } else {
            String var15;
            if (var3 != null && var5 != null && var7 == null && var9 == null) {
               var14 = var3.getText().toLowerCase(Locale.ROOT);
               var15 = var5.getText().toLowerCase(Locale.ROOT);
               if (!isPositionKeyWord(var14)) {
                  var13 = this.parseSize(var2);
                  var23 = ZERO_PERCENT;
                  if ("top".equals(var15)) {
                     var19 = ZERO_PERCENT;
                     var22 = ZERO_PERCENT;
                  } else if ("bottom".equals(var15)) {
                     var19 = ONE_HUNDRED_PERCENT;
                     var22 = ZERO_PERCENT;
                  } else if ("center".equals(var15)) {
                     var19 = FIFTY_PERCENT;
                     var22 = ZERO_PERCENT;
                  } else if (!isPositionKeyWord(var15)) {
                     var19 = this.parseSize(var4);
                     var22 = ZERO_PERCENT;
                  } else {
                     this.error(var4, "Expected 'top', 'bottom', 'center' or <size>");
                  }
               } else if (!var14.equals("left") && !var14.equals("right")) {
                  if (var14.equals("center")) {
                     var13 = FIFTY_PERCENT;
                     var23 = ZERO_PERCENT;
                     if (var15.equals("top")) {
                        var19 = ZERO_PERCENT;
                        var22 = ZERO_PERCENT;
                     } else if (var15.equals("bottom")) {
                        var19 = ONE_HUNDRED_PERCENT;
                        var22 = ZERO_PERCENT;
                     } else if (var15.equals("center")) {
                        var19 = FIFTY_PERCENT;
                        var22 = ZERO_PERCENT;
                     } else if (!isPositionKeyWord(var15)) {
                        var19 = this.parseSize(var4);
                        var22 = ZERO_PERCENT;
                     } else {
                        this.error(var4, "Expected 'top', 'bottom', 'center' or <size>");
                     }
                  }
               } else {
                  var13 = var14.equals("right") ? ONE_HUNDRED_PERCENT : ZERO_PERCENT;
                  var23 = ZERO_PERCENT;
                  if (!isPositionKeyWord(var15)) {
                     var19 = this.parseSize(var4);
                     var22 = ZERO_PERCENT;
                  } else if (!var15.equals("top") && !var15.equals("bottom") && !var15.equals("center")) {
                     this.error(var4, "Expected 'top', 'bottom', 'center' or <size>");
                  } else if (var15.equals("top")) {
                     var19 = ZERO_PERCENT;
                     var22 = ZERO_PERCENT;
                  } else if (var15.equals("center")) {
                     var19 = FIFTY_PERCENT;
                     var22 = ZERO_PERCENT;
                  } else {
                     var19 = ONE_HUNDRED_PERCENT;
                     var22 = ZERO_PERCENT;
                  }
               }
            } else {
               String var16;
               if (var3 != null && var5 != null && var7 != null && var9 == null) {
                  var14 = var3.getText().toLowerCase(Locale.ROOT);
                  var15 = var5.getText().toLowerCase(Locale.ROOT);
                  var16 = var7.getText().toLowerCase(Locale.ROOT);
                  if (isPositionKeyWord(var14) && !"center".equals(var14)) {
                     if ("left".equals(var14) || "right".equals(var14)) {
                        if (!isPositionKeyWord(var15)) {
                           if ("left".equals(var14)) {
                              var13 = this.parseSize(var4);
                              var23 = ZERO_PERCENT;
                           } else {
                              var13 = ZERO_PERCENT;
                              var23 = this.parseSize(var4);
                           }

                           if ("top".equals(var16)) {
                              var19 = ZERO_PERCENT;
                              var22 = ZERO_PERCENT;
                           } else if ("bottom".equals(var16)) {
                              var19 = ONE_HUNDRED_PERCENT;
                              var22 = ZERO_PERCENT;
                           } else if ("center".equals(var16)) {
                              var19 = FIFTY_PERCENT;
                              var22 = ZERO_PERCENT;
                           } else {
                              this.error(var6, "Expected 'top', 'bottom' or 'center'");
                           }
                        } else {
                           if ("left".equals(var14)) {
                              var13 = ZERO_PERCENT;
                              var23 = ZERO_PERCENT;
                           } else {
                              var13 = ONE_HUNDRED_PERCENT;
                              var23 = ZERO_PERCENT;
                           }

                           if (!isPositionKeyWord(var16)) {
                              if ("top".equals(var15)) {
                                 var19 = this.parseSize(var6);
                                 var22 = ZERO_PERCENT;
                              } else if ("bottom".equals(var15)) {
                                 var19 = ZERO_PERCENT;
                                 var22 = this.parseSize(var6);
                              } else {
                                 this.error(var4, "Expected 'top' or 'bottom'");
                              }
                           } else {
                              this.error(var6, "Expected <size>");
                           }
                        }
                     }
                  } else {
                     if ("center".equals(var14)) {
                        var13 = FIFTY_PERCENT;
                     } else {
                        var13 = this.parseSize(var2);
                     }

                     var23 = ZERO_PERCENT;
                     if (!isPositionKeyWord(var16)) {
                        if ("top".equals(var15)) {
                           var19 = this.parseSize(var6);
                           var22 = ZERO_PERCENT;
                        } else if ("bottom".equals(var15)) {
                           var19 = ZERO_PERCENT;
                           var22 = this.parseSize(var6);
                        } else {
                           this.error(var4, "Expected 'top' or 'bottom'");
                        }
                     } else {
                        this.error(var6, "Expected <size>");
                     }
                  }
               } else {
                  var14 = var3.getText().toLowerCase(Locale.ROOT);
                  var15 = var5.getText().toLowerCase(Locale.ROOT);
                  var16 = var7.getText().toLowerCase(Locale.ROOT);
                  String var17 = var9.getText().toLowerCase(Locale.ROOT);
                  if ((var14.equals("left") || var14.equals("right")) && (var16.equals("top") || var16.equals("bottom")) && !isPositionKeyWord(var15) && !isPositionKeyWord(var17)) {
                     if (var14.equals("left")) {
                        var13 = this.parseSize(var4);
                        var23 = ZERO_PERCENT;
                     } else {
                        var13 = ZERO_PERCENT;
                        var23 = this.parseSize(var4);
                     }

                     if (var16.equals("top")) {
                        var19 = this.parseSize(var8);
                        var22 = ZERO_PERCENT;
                     } else {
                        var19 = ZERO_PERCENT;
                        var22 = this.parseSize(var8);
                     }
                  } else {
                     this.error(var1, "Expected 'left' or 'right' followed by <size> followed by 'top' or 'bottom' followed by <size>");
                  }
               }
            }
         }
      }

      ParsedValueImpl[] var24 = new ParsedValueImpl[]{var19, var23, var22, var13};
      return new ParsedValueImpl(var24, BackgroundPositionConverter.getInstance());
   }

   private ParsedValueImpl parseBackgroundPositionLayers(Term var1) throws ParseException {
      int var2 = this.numberOfLayers(var1);
      ParsedValueImpl[] var3 = new ParsedValueImpl[var2];
      int var4 = 0;

      for(Term var5 = var1; var5 != null; var5 = this.nextLayer(var5)) {
         var3[var4++] = this.parseBackgroundPosition(var5);
      }

      return new ParsedValueImpl(var3, LayeredBackgroundPositionConverter.getInstance());
   }

   private ParsedValueImpl[] parseRepeatStyle(Term var1) throws ParseException {
      BackgroundRepeat var3;
      BackgroundRepeat var2 = var3 = BackgroundRepeat.NO_REPEAT;
      if (var1.token == null || var1.token.getType() != 11 || var1.token.getText() == null || var1.token.getText().isEmpty()) {
         this.error(var1, "Expected '<repeat-style>'");
      }

      String var5 = var1.token.getText().toLowerCase(Locale.ROOT);
      if ("repeat-x".equals(var5)) {
         var2 = BackgroundRepeat.REPEAT;
         var3 = BackgroundRepeat.NO_REPEAT;
      } else if ("repeat-y".equals(var5)) {
         var2 = BackgroundRepeat.NO_REPEAT;
         var3 = BackgroundRepeat.REPEAT;
      } else if ("repeat".equals(var5)) {
         var2 = BackgroundRepeat.REPEAT;
         var3 = BackgroundRepeat.REPEAT;
      } else if ("space".equals(var5)) {
         var2 = BackgroundRepeat.SPACE;
         var3 = BackgroundRepeat.SPACE;
      } else if ("round".equals(var5)) {
         var2 = BackgroundRepeat.ROUND;
         var3 = BackgroundRepeat.ROUND;
      } else if ("no-repeat".equals(var5)) {
         var2 = BackgroundRepeat.NO_REPEAT;
         var3 = BackgroundRepeat.NO_REPEAT;
      } else if ("stretch".equals(var5)) {
         var2 = BackgroundRepeat.NO_REPEAT;
         var3 = BackgroundRepeat.NO_REPEAT;
      } else {
         this.error(var1, "Expected  '<repeat-style>' " + var5);
      }

      Term var4;
      if ((var4 = var1.nextInSeries) != null && var4.token != null && var4.token.getType() == 11 && var4.token.getText() != null && !var4.token.getText().isEmpty()) {
         var5 = var4.token.getText().toLowerCase(Locale.ROOT);
         if ("repeat-x".equals(var5)) {
            this.error(var4, "Unexpected 'repeat-x'");
         } else if ("repeat-y".equals(var5)) {
            this.error(var4, "Unexpected 'repeat-y'");
         } else if ("repeat".equals(var5)) {
            var3 = BackgroundRepeat.REPEAT;
         } else if ("space".equals(var5)) {
            var3 = BackgroundRepeat.SPACE;
         } else if ("round".equals(var5)) {
            var3 = BackgroundRepeat.ROUND;
         } else if ("no-repeat".equals(var5)) {
            var3 = BackgroundRepeat.NO_REPEAT;
         } else if ("stretch".equals(var5)) {
            var3 = BackgroundRepeat.NO_REPEAT;
         } else {
            this.error(var4, "Expected  '<repeat-style>'");
         }
      }

      return new ParsedValueImpl[]{new ParsedValueImpl(var2.name(), new EnumConverter(BackgroundRepeat.class)), new ParsedValueImpl(var3.name(), new EnumConverter(BackgroundRepeat.class))};
   }

   private ParsedValueImpl parseBorderImageRepeatStyleLayers(Term var1) throws ParseException {
      int var2 = this.numberOfLayers(var1);
      ParsedValueImpl[][] var3 = new ParsedValueImpl[var2][];
      int var4 = 0;

      for(Term var5 = var1; var5 != null; var5 = this.nextLayer(var5)) {
         var3[var4++] = this.parseRepeatStyle(var5);
      }

      return new ParsedValueImpl(var3, RepeatStructConverter.getInstance());
   }

   private ParsedValueImpl parseBackgroundRepeatStyleLayers(Term var1) throws ParseException {
      int var2 = this.numberOfLayers(var1);
      ParsedValueImpl[][] var3 = new ParsedValueImpl[var2][];
      int var4 = 0;

      for(Term var5 = var1; var5 != null; var5 = this.nextLayer(var5)) {
         var3[var4++] = this.parseRepeatStyle(var5);
      }

      return new ParsedValueImpl(var3, RepeatStructConverter.getInstance());
   }

   private ParsedValueImpl parseBackgroundSize(Term var1) throws ParseException {
      ParsedValueImpl var2 = null;
      ParsedValueImpl var3 = null;
      boolean var4 = false;
      boolean var5 = false;
      if (var1.token == null) {
         this.error(var1, "Expected '<bg-size>'");
      }

      String var7;
      if (var1.token.getType() == 11) {
         var7 = var1.token.getText() != null ? var1.token.getText().toLowerCase(Locale.ROOT) : null;
         if (!"auto".equals(var7)) {
            if ("cover".equals(var7)) {
               var4 = true;
            } else if ("contain".equals(var7)) {
               var5 = true;
            } else if ("stretch".equals(var7)) {
               var3 = ONE_HUNDRED_PERCENT;
               var2 = ONE_HUNDRED_PERCENT;
            } else {
               this.error(var1, "Expected 'auto', 'cover', 'contain', or  'stretch'");
            }
         }
      } else if (this.isSize(var1.token)) {
         var3 = this.parseSize(var1);
         var2 = null;
      } else {
         this.error(var1, "Expected '<bg-size>'");
      }

      Term var6;
      if ((var6 = var1.nextInSeries) != null) {
         if (var4 || var5) {
            this.error(var6, "Unexpected '<bg-size>'");
         }

         if (var6.token.getType() == 11) {
            var7 = var6.token.getText() != null ? var6.token.getText().toLowerCase(Locale.ROOT) : null;
            if ("auto".equals(var7)) {
               var2 = null;
            } else if ("cover".equals(var7)) {
               this.error(var6, "Unexpected 'cover'");
            } else if ("contain".equals(var7)) {
               this.error(var6, "Unexpected 'contain'");
            } else if ("stretch".equals(var7)) {
               var2 = ONE_HUNDRED_PERCENT;
            } else {
               this.error(var6, "Expected 'auto' or 'stretch'");
            }
         } else if (this.isSize(var6.token)) {
            var2 = this.parseSize(var6);
         } else {
            this.error(var6, "Expected '<bg-size>'");
         }
      }

      ParsedValueImpl[] var8 = new ParsedValueImpl[]{var3, var2, new ParsedValueImpl(var4 ? "true" : "false", BooleanConverter.getInstance()), new ParsedValueImpl(var5 ? "true" : "false", BooleanConverter.getInstance())};
      return new ParsedValueImpl(var8, BackgroundSizeConverter.getInstance());
   }

   private ParsedValueImpl parseBackgroundSizeLayers(Term var1) throws ParseException {
      int var2 = this.numberOfLayers(var1);
      ParsedValueImpl[] var3 = new ParsedValueImpl[var2];
      int var4 = 0;

      for(Term var5 = var1; var5 != null; var5 = this.nextLayer(var5)) {
         var3[var4++] = this.parseBackgroundSize(var5);
      }

      return new ParsedValueImpl(var3, LayeredBackgroundSizeConverter.getInstance());
   }

   private ParsedValueImpl parseBorderPaint(Term var1) throws ParseException {
      Term var2 = var1;
      ParsedValueImpl[] var3 = new ParsedValueImpl[4];

      int var4;
      for(var4 = 0; var2 != null; var2 = var2.nextInSeries) {
         if (var2.token == null || var3.length <= var4) {
            this.error(var2, "Expected '<paint>'");
         }

         var3[var4++] = this.parse(var2);
      }

      if (var4 < 2) {
         var3[1] = var3[0];
      }

      if (var4 < 3) {
         var3[2] = var3[0];
      }

      if (var4 < 4) {
         var3[3] = var3[1];
      }

      return new ParsedValueImpl(var3, StrokeBorderPaintConverter.getInstance());
   }

   private ParsedValueImpl parseBorderPaintLayers(Term var1) throws ParseException {
      int var2 = this.numberOfLayers(var1);
      ParsedValueImpl[] var3 = new ParsedValueImpl[var2];
      int var4 = 0;

      for(Term var5 = var1; var5 != null; var5 = this.nextLayer(var5)) {
         var3[var4++] = this.parseBorderPaint(var5);
      }

      return new ParsedValueImpl(var3, LayeredBorderPaintConverter.getInstance());
   }

   private ParsedValueImpl parseBorderStyleSeries(Term var1) throws ParseException {
      Term var2 = var1;
      ParsedValueImpl[] var3 = new ParsedValueImpl[4];

      int var4;
      for(var4 = 0; var2 != null; var2 = var2.nextInSeries) {
         var3[var4++] = this.parseBorderStyle(var2);
      }

      if (var4 < 2) {
         var3[1] = var3[0];
      }

      if (var4 < 3) {
         var3[2] = var3[0];
      }

      if (var4 < 4) {
         var3[3] = var3[1];
      }

      return new ParsedValueImpl(var3, BorderStrokeStyleSequenceConverter.getInstance());
   }

   private ParsedValueImpl parseBorderStyleLayers(Term var1) throws ParseException {
      int var2 = this.numberOfLayers(var1);
      ParsedValueImpl[] var3 = new ParsedValueImpl[var2];
      int var4 = 0;

      for(Term var5 = var1; var5 != null; var5 = this.nextLayer(var5)) {
         var3[var4++] = this.parseBorderStyleSeries(var5);
      }

      return new ParsedValueImpl(var3, LayeredBorderStyleConverter.getInstance());
   }

   private String getKeyword(Term var1) {
      return var1 != null && var1.token != null && var1.token.getType() == 11 && var1.token.getText() != null && !var1.token.getText().isEmpty() ? var1.token.getText().toLowerCase(Locale.ROOT) : null;
   }

   private ParsedValueImpl parseBorderStyle(Term var1) throws ParseException {
      ParsedValue var2 = null;
      ParsedValueImpl var3 = null;
      ParsedValueImpl var4 = null;
      ParsedValueImpl var5 = null;
      ParsedValueImpl var6 = null;
      ParsedValueImpl var7 = null;
      var2 = this.dashStyle(var1);
      Term var9 = var1;
      Term var8 = var1.nextInSeries;
      String var10 = this.getKeyword(var8);
      if ("phase".equals(var10)) {
         if ((var8 = var8.nextInSeries) == null || var8.token == null || !this.isSize(var8.token)) {
            this.error(var8, "Expected '<size>'");
         }

         ParsedValueImpl var11 = this.parseSize(var8);
         var3 = new ParsedValueImpl(var11, SizeConverter.getInstance());
         var9 = var8;
         var8 = var8.nextInSeries;
      }

      var4 = this.parseStrokeType(var8);
      if (var4 != null) {
         var9 = var8;
         var8 = var8.nextInSeries;
      }

      var10 = this.getKeyword(var8);
      if ("line-join".equals(var10)) {
         var8 = var8.nextInSeries;
         ParsedValueImpl[] var12 = this.parseStrokeLineJoin(var8);
         if (var12 != null) {
            var5 = var12[0];
            var6 = var12[1];
         } else {
            this.error(var8, "Expected 'miter <size>?', 'bevel' or 'round'");
         }

         var9 = var8;
         var8 = var8.nextInSeries;
         var10 = this.getKeyword(var8);
      }

      if ("line-cap".equals(var10)) {
         var8 = var8.nextInSeries;
         var7 = this.parseStrokeLineCap(var8);
         if (var7 == null) {
            this.error(var8, "Expected 'square', 'butt' or 'round'");
         }

         var9 = var8;
         var8 = var8.nextInSeries;
      }

      if (var8 != null) {
         var1.nextInSeries = var8;
      } else {
         var1.nextInSeries = null;
         var1.nextLayer = var9.nextLayer;
      }

      ParsedValue[] var13 = new ParsedValue[]{var2, var3, var4, var5, var6, var7};
      return new ParsedValueImpl(var13, BorderStyleConverter.getInstance());
   }

   private ParsedValue dashStyle(Term var1) throws ParseException {
      if (var1.token == null) {
         this.error(var1, "Expected '<dash-style>'");
      }

      int var2 = var1.token.getType();
      Object var3 = null;
      if (var2 == 11) {
         var3 = this.borderStyle(var1);
      } else if (var2 == 12) {
         var3 = this.segments(var1);
      } else {
         this.error(var1, "Expected '<dash-style>'");
      }

      return (ParsedValue)var3;
   }

   private ParsedValue borderStyle(Term var1) throws ParseException {
      if (var1.token == null || var1.token.getType() != 11 || var1.token.getText() == null || var1.token.getText().isEmpty()) {
         this.error(var1, "Expected '<border-style>'");
      }

      String var2 = var1.token.getText().toLowerCase(Locale.ROOT);
      if ("none".equals(var2)) {
         return BorderStyleConverter.NONE;
      } else if ("hidden".equals(var2)) {
         return BorderStyleConverter.NONE;
      } else if ("dotted".equals(var2)) {
         return BorderStyleConverter.DOTTED;
      } else if ("dashed".equals(var2)) {
         return BorderStyleConverter.DASHED;
      } else if ("solid".equals(var2)) {
         return BorderStyleConverter.SOLID;
      } else {
         if ("double".equals(var2)) {
            this.error(var1, "Unsupported <border-style> 'double'");
         } else if ("groove".equals(var2)) {
            this.error(var1, "Unsupported <border-style> 'groove'");
         } else if ("ridge".equals(var2)) {
            this.error(var1, "Unsupported <border-style> 'ridge'");
         } else if ("inset".equals(var2)) {
            this.error(var1, "Unsupported <border-style> 'inset'");
         } else if ("outset".equals(var2)) {
            this.error(var1, "Unsupported <border-style> 'outset'");
         } else {
            this.error(var1, "Unsupported <border-style> '" + var2 + "'");
         }

         return BorderStyleConverter.SOLID;
      }
   }

   private ParsedValueImpl segments(Term var1) throws ParseException {
      String var2 = var1.token != null ? var1.token.getText() : null;
      if (!"segments".regionMatches(true, 0, var2, 0, 8)) {
         this.error(var1, "Expected 'segments'");
      }

      Term var3 = var1.firstArg;
      if (var3 == null) {
         this.error((Term)null, "Expected '<size>'");
      }

      int var4 = this.numberOfArgs(var1);
      ParsedValueImpl[] var5 = new ParsedValueImpl[var4];

      for(int var6 = 0; var3 != null; var3 = var3.nextArg) {
         var5[var6++] = this.parseSize(var3);
      }

      return new ParsedValueImpl(var5, SizeConverter.SequenceConverter.getInstance());
   }

   private ParsedValueImpl parseStrokeType(Term var1) throws ParseException {
      String var2 = this.getKeyword(var1);
      return !"centered".equals(var2) && !"inside".equals(var2) && !"outside".equals(var2) ? null : new ParsedValueImpl(var2, new EnumConverter(StrokeType.class));
   }

   private ParsedValueImpl[] parseStrokeLineJoin(Term var1) throws ParseException {
      String var2 = this.getKeyword(var1);
      if (!"miter".equals(var2) && !"bevel".equals(var2) && !"round".equals(var2)) {
         return null;
      } else {
         ParsedValueImpl var3 = new ParsedValueImpl(var2, new EnumConverter(StrokeLineJoin.class));
         ParsedValueImpl var4 = null;
         if ("miter".equals(var2)) {
            Term var5 = var1.nextInSeries;
            if (var5 != null && var5.token != null && this.isSize(var5.token)) {
               var1.nextInSeries = var5.nextInSeries;
               ParsedValueImpl var6 = this.parseSize(var5);
               var4 = new ParsedValueImpl(var6, SizeConverter.getInstance());
            }
         }

         return new ParsedValueImpl[]{var3, var4};
      }
   }

   private ParsedValueImpl parseStrokeLineCap(Term var1) throws ParseException {
      String var2 = this.getKeyword(var1);
      return !"square".equals(var2) && !"butt".equals(var2) && !"round".equals(var2) ? null : new ParsedValueImpl(var2, new EnumConverter(StrokeLineCap.class));
   }

   private ParsedValueImpl parseBorderImageSlice(Term var1) throws ParseException {
      Term var2 = var1;
      if (var1.token == null || !this.isSize(var1.token)) {
         this.error(var1, "Expected '<size>'");
      }

      ParsedValueImpl[] var3 = new ParsedValueImpl[4];
      Boolean var4 = Boolean.FALSE;
      int var5 = 0;

      while(var5 < 4 && var2 != null) {
         var3[var5++] = this.parseSize(var2);
         if ((var2 = var2.nextInSeries) != null && var2.token != null && var2.token.getType() == 11 && "fill".equalsIgnoreCase(var2.token.getText())) {
            var4 = Boolean.TRUE;
            break;
         }
      }

      if (var5 < 2) {
         var3[1] = var3[0];
      }

      if (var5 < 3) {
         var3[2] = var3[0];
      }

      if (var5 < 4) {
         var3[3] = var3[1];
      }

      ParsedValueImpl[] var6 = new ParsedValueImpl[]{new ParsedValueImpl(var3, InsetsConverter.getInstance()), new ParsedValueImpl(var4, (StyleConverter)null)};
      return new ParsedValueImpl(var6, BorderImageSliceConverter.getInstance());
   }

   private ParsedValueImpl parseBorderImageSliceLayers(Term var1) throws ParseException {
      int var2 = this.numberOfLayers(var1);
      ParsedValueImpl[] var3 = new ParsedValueImpl[var2];
      int var4 = 0;

      for(Term var5 = var1; var5 != null; var5 = this.nextLayer(var5)) {
         var3[var4++] = this.parseBorderImageSlice(var5);
      }

      return new ParsedValueImpl(var3, SliceSequenceConverter.getInstance());
   }

   private ParsedValueImpl parseBorderImageWidth(Term var1) throws ParseException {
      Term var2 = var1;
      if (var1.token == null || !this.isSize(var1.token)) {
         this.error(var1, "Expected '<size>'");
      }

      ParsedValueImpl[] var3 = new ParsedValueImpl[4];
      int var4 = 0;

      while(var4 < 4 && var2 != null) {
         var3[var4++] = this.parseSize(var2);
         if ((var2 = var2.nextInSeries) != null && var2.token != null && var2.token.getType() == 11) {
         }
      }

      if (var4 < 2) {
         var3[1] = var3[0];
      }

      if (var4 < 3) {
         var3[2] = var3[0];
      }

      if (var4 < 4) {
         var3[3] = var3[1];
      }

      return new ParsedValueImpl(var3, BorderImageWidthConverter.getInstance());
   }

   private ParsedValueImpl parseBorderImageWidthLayers(Term var1) throws ParseException {
      int var2 = this.numberOfLayers(var1);
      ParsedValueImpl[] var3 = new ParsedValueImpl[var2];
      int var4 = 0;

      for(Term var5 = var1; var5 != null; var5 = this.nextLayer(var5)) {
         var3[var4++] = this.parseBorderImageWidth(var5);
      }

      return new ParsedValueImpl(var3, BorderImageWidthsSequenceConverter.getInstance());
   }

   private ParsedValueImpl parseRegion(Term var1) throws ParseException {
      String var2 = var1.token != null ? var1.token.getText() : null;
      if (!"region".regionMatches(true, 0, var2, 0, 6)) {
         this.error(var1, "Expected 'region'");
      }

      Term var3 = var1.firstArg;
      if (var3 == null) {
         this.error(var1, "Expected 'region(\"<styleclass-or-id-string>\")'");
      }

      if (var3.token == null || var3.token.getType() != 10 || var3.token.getText() == null || var3.token.getText().isEmpty()) {
         this.error(var1, "Expected 'region(\"<styleclass-or-id-string>\")'");
      }

      String var4 = "SPECIAL-REGION-URL:" + Utils.stripQuotes(var3.token.getText());
      return new ParsedValueImpl(var4, StringConverter.getInstance());
   }

   private ParsedValueImpl parseURI(Term var1) throws ParseException {
      if (var1 == null) {
         this.error(var1, "Expected 'url(\"<uri-string>\")'");
      }

      if (var1.token == null || var1.token.getType() != 43 || var1.token.getText() == null || var1.token.getText().isEmpty()) {
         this.error(var1, "Expected 'url(\"<uri-string>\")'");
      }

      String var2 = var1.token.getText();
      ParsedValueImpl[] var3 = new ParsedValueImpl[]{new ParsedValueImpl(var2, StringConverter.getInstance()), null};
      return new ParsedValueImpl(var3, URLConverter.getInstance());
   }

   private ParsedValueImpl parseURILayers(Term var1) throws ParseException {
      int var2 = this.numberOfLayers(var1);
      Term var3 = var1;
      int var4 = 0;

      ParsedValueImpl[] var5;
      for(var5 = new ParsedValueImpl[var2]; var3 != null; var3 = this.nextLayer(var3)) {
         var5[var4++] = this.parseURI(var3);
      }

      return new ParsedValueImpl(var5, URLConverter.SequenceConverter.getInstance());
   }

   private ParsedValueImpl parseFontSize(Term var1) throws ParseException {
      if (var1 == null) {
         return null;
      } else {
         Token var2 = var1.token;
         if (var2 == null || !this.isSize(var2)) {
            this.error(var1, "Expected '<font-size>'");
         }

         Size var3 = null;
         if (var2.getType() == 11) {
            String var4 = var2.getText().toLowerCase(Locale.ROOT);
            double var5 = -1.0;
            if ("inherit".equals(var4)) {
               var5 = 100.0;
            } else if ("xx-small".equals(var4)) {
               var5 = 60.0;
            } else if ("x-small".equals(var4)) {
               var5 = 75.0;
            } else if ("small".equals(var4)) {
               var5 = 80.0;
            } else if ("medium".equals(var4)) {
               var5 = 100.0;
            } else if ("large".equals(var4)) {
               var5 = 120.0;
            } else if ("x-large".equals(var4)) {
               var5 = 150.0;
            } else if ("xx-large".equals(var4)) {
               var5 = 200.0;
            } else if ("smaller".equals(var4)) {
               var5 = 80.0;
            } else if ("larger".equals(var4)) {
               var5 = 120.0;
            }

            if (var5 > -1.0) {
               var3 = new Size(var5, SizeUnits.PERCENT);
            }
         }

         if (var3 == null) {
            var3 = this.size(var2);
         }

         ParsedValueImpl var7 = new ParsedValueImpl(var3, (StyleConverter)null);
         return new ParsedValueImpl(var7, FontConverter.FontSizeConverter.getInstance());
      }
   }

   private ParsedValueImpl parseFontStyle(Term var1) throws ParseException {
      if (var1 == null) {
         return null;
      } else {
         Token var2 = var1.token;
         if (var2 == null || var2.getType() != 11 || var2.getText() == null || var2.getText().isEmpty()) {
            this.error(var1, "Expected '<font-style>'");
         }

         String var3 = var2.getText().toLowerCase(Locale.ROOT);
         String var4 = FontPosture.REGULAR.name();
         if ("normal".equals(var3)) {
            var4 = FontPosture.REGULAR.name();
         } else if ("italic".equals(var3)) {
            var4 = FontPosture.ITALIC.name();
         } else if ("oblique".equals(var3)) {
            var4 = FontPosture.ITALIC.name();
         } else {
            if (!"inherit".equals(var3)) {
               return null;
            }

            var4 = "inherit";
         }

         return new ParsedValueImpl(var4, FontConverter.FontStyleConverter.getInstance());
      }
   }

   private ParsedValueImpl parseFontWeight(Term var1) throws ParseException {
      if (var1 == null) {
         return null;
      } else {
         Token var2 = var1.token;
         if (var2 == null || var2.getText() == null || var2.getText().isEmpty()) {
            this.error(var1, "Expected '<font-weight>'");
         }

         String var3 = var2.getText().toLowerCase(Locale.ROOT);
         String var4 = FontWeight.NORMAL.name();
         if ("inherit".equals(var3)) {
            var4 = FontWeight.NORMAL.name();
         } else if ("normal".equals(var3)) {
            var4 = FontWeight.NORMAL.name();
         } else if ("bold".equals(var3)) {
            var4 = FontWeight.BOLD.name();
         } else if ("bolder".equals(var3)) {
            var4 = FontWeight.BOLD.name();
         } else if ("lighter".equals(var3)) {
            var4 = FontWeight.LIGHT.name();
         } else if ("100".equals(var3)) {
            var4 = FontWeight.findByWeight(100).name();
         } else if ("200".equals(var3)) {
            var4 = FontWeight.findByWeight(200).name();
         } else if ("300".equals(var3)) {
            var4 = FontWeight.findByWeight(300).name();
         } else if ("400".equals(var3)) {
            var4 = FontWeight.findByWeight(400).name();
         } else if ("500".equals(var3)) {
            var4 = FontWeight.findByWeight(500).name();
         } else if ("600".equals(var3)) {
            var4 = FontWeight.findByWeight(600).name();
         } else if ("700".equals(var3)) {
            var4 = FontWeight.findByWeight(700).name();
         } else if ("800".equals(var3)) {
            var4 = FontWeight.findByWeight(800).name();
         } else if ("900".equals(var3)) {
            var4 = FontWeight.findByWeight(900).name();
         } else {
            this.error(var1, "Expected '<font-weight>'");
         }

         return new ParsedValueImpl(var4, FontConverter.FontWeightConverter.getInstance());
      }
   }

   private ParsedValueImpl parseFontFamily(Term var1) throws ParseException {
      if (var1 == null) {
         return null;
      } else {
         Token var2 = var1.token;
         String var3 = null;
         if (var2 == null || var2.getType() != 11 && var2.getType() != 10 || (var3 = var2.getText()) == null || var3.isEmpty()) {
            this.error(var1, "Expected '<font-family>'");
         }

         String var4 = this.stripQuotes(var3.toLowerCase(Locale.ROOT));
         if ("inherit".equals(var4)) {
            return new ParsedValueImpl("inherit", StringConverter.getInstance());
         } else {
            return !"serif".equals(var4) && !"sans-serif".equals(var4) && !"cursive".equals(var4) && !"fantasy".equals(var4) && !"monospace".equals(var4) ? new ParsedValueImpl(var2.getText(), StringConverter.getInstance()) : new ParsedValueImpl(var4, StringConverter.getInstance());
         }
      }
   }

   private ParsedValueImpl parseFont(Term var1) throws ParseException {
      Term var2 = var1.nextInSeries;

      Term var3;
      for(var1.nextInSeries = null; var2 != null; var2 = var3) {
         var3 = var2.nextInSeries;
         var2.nextInSeries = var1;
         var1 = var2;
      }

      Token var13 = var1.token;
      int var4 = var13.getType();
      if (var4 != 11 && var4 != 10) {
         this.error(var1, "Expected '<font-family>'");
      }

      ParsedValueImpl var5 = this.parseFontFamily(var1);
      Term var6;
      if ((var6 = var1.nextInSeries) == null) {
         this.error(var1, "Expected '<size>'");
      }

      if (var6.token == null || !this.isSize(var6.token)) {
         this.error(var6, "Expected '<size>'");
      }

      Term var7;
      if ((var7 = var6.nextInSeries) != null && var7.token != null && var7.token.getType() == 32) {
         var1 = var7;
         if ((var6 = var7.nextInSeries) == null) {
            this.error(var7, "Expected '<size>'");
         }

         if (var6.token == null || !this.isSize(var6.token)) {
            this.error(var6, "Expected '<size>'");
         }

         var13 = var6.token;
      }

      ParsedValueImpl var8 = this.parseFontSize(var6);
      if (var8 == null) {
         this.error(var1, "Expected '<size>'");
      }

      ParsedValueImpl var9 = null;
      ParsedValueImpl var10 = null;
      String var11 = null;

      while(true) {
         while(true) {
            do {
               if ((var6 = var6.nextInSeries) == null) {
                  ParsedValueImpl[] var12 = new ParsedValueImpl[]{var5, var8, var10, var9};
                  return new ParsedValueImpl(var12, FontConverter.getInstance());
               }

               if (var6.token == null || var6.token.getType() != 11 || var6.token.getText() == null || var6.token.getText().isEmpty()) {
                  this.error(var6, "Expected '<font-weight>', '<font-style>' or '<font-variant>'");
               }
            } while(var9 == null && (var9 = this.parseFontStyle(var6)) != null);

            if (var11 == null && "small-caps".equalsIgnoreCase(var6.token.getText())) {
               var11 = var6.token.getText();
            } else if (var10 == null && (var10 = this.parseFontWeight(var6)) != null) {
            }
         }
      }
   }

   private Token nextToken(CSSLexer var1) {
      Token var2 = null;

      do {
         do {
            var2 = var1.nextToken();
         } while(var2 != null && var2.getType() == 40);
      } while(var2.getType() == 41);

      if (LOGGER.isLoggable(Level.FINEST)) {
         LOGGER.finest(var2.toString());
      }

      return var2;
   }

   private void parse(Stylesheet var1, CSSLexer var2) {
      this.currentToken = this.nextToken(var2);

      String var6;
      CssError var7;
      int var12;
      int var14;
      while(this.currentToken != null && this.currentToken.getType() == 47) {
         this.currentToken = this.nextToken(var2);
         if (this.currentToken != null && this.currentToken.getType() == 11) {
            String var9 = this.currentToken.getText().toLowerCase(Locale.ROOT);
            if ("font-face".equals(var9)) {
               FontFace var13 = this.fontFace(var2);
               if (var13 != null) {
                  var1.getFontFaces().add(var13);
               }

               this.currentToken = this.nextToken(var2);
            } else if ("import".equals(var9)) {
               if (imports == null) {
                  imports = new Stack();
               }

               if (!imports.contains(this.sourceOfStylesheet)) {
                  imports.push(this.sourceOfStylesheet);
                  Stylesheet var11 = this.handleImport(var2);
                  if (var11 != null) {
                     var1.importStylesheet(var11);
                  }

                  imports.pop();
                  if (imports.isEmpty()) {
                     imports = null;
                  }
               } else {
                  var12 = this.currentToken.getLine();
                  var14 = this.currentToken.getOffset();
                  var6 = MessageFormat.format("Recursive @import at {2} [{0,number,#},{1,number,#}]", var12, var14, imports.peek());
                  var7 = this.createError(var6);
                  if (LOGGER.isLoggable(Level.WARNING)) {
                     LOGGER.warning(var7.toString());
                  }

                  this.reportError(var7);
               }

               while(true) {
                  do {
                     this.currentToken = var2.nextToken();
                  } while(this.currentToken != null && this.currentToken.getType() == 30);

                  if (this.currentToken.getType() != 40 && this.currentToken.getType() != 41) {
                     break;
                  }
               }
            }
         } else {
            ParseException var3 = new ParseException("Expected IDENT", this.currentToken, this);
            String var4 = var3.toString();
            CssError var5 = this.createError(var4);
            if (LOGGER.isLoggable(Level.WARNING)) {
               LOGGER.warning(var5.toString());
            }

            this.reportError(var5);

            while(true) {
               do {
                  this.currentToken = var2.nextToken();
               } while(this.currentToken != null && this.currentToken.getType() == 30);

               if (this.currentToken.getType() != 40 && this.currentToken.getType() != 41) {
                  break;
               }
            }
         }
      }

      while(true) {
         if (this.currentToken != null && this.currentToken.getType() != -1) {
            List var10 = this.selectors(var2);
            if (var10 == null) {
               return;
            }

            if (this.currentToken != null && this.currentToken.getType() == 28) {
               this.currentToken = this.nextToken(var2);
               List var15 = this.declarations(var2);
               if (var15 == null) {
                  return;
               }

               if (this.currentToken != null && this.currentToken.getType() != 29) {
                  var14 = this.currentToken.getLine();
                  int var16 = this.currentToken.getOffset();
                  String var17 = MessageFormat.format("Expected RBRACE at [{0,number,#},{1,number,#}]", var14, var16);
                  CssError var8 = this.createError(var17);
                  if (LOGGER.isLoggable(Level.WARNING)) {
                     LOGGER.warning(var8.toString());
                  }

                  this.reportError(var8);
                  this.currentToken = null;
                  return;
               }

               var1.getRules().add(new Rule(var10, var15));
               this.currentToken = this.nextToken(var2);
               continue;
            }

            var12 = this.currentToken != null ? this.currentToken.getLine() : -1;
            var14 = this.currentToken != null ? this.currentToken.getOffset() : -1;
            var6 = MessageFormat.format("Expected LBRACE at [{0,number,#},{1,number,#}]", var12, var14);
            var7 = this.createError(var6);
            if (LOGGER.isLoggable(Level.WARNING)) {
               LOGGER.warning(var7.toString());
            }

            this.reportError(var7);
            this.currentToken = null;
            return;
         }

         this.currentToken = null;
         return;
      }
   }

   private FontFace fontFace(CSSLexer var1) {
      HashMap var2 = new HashMap();
      ArrayList var3 = new ArrayList();

      do {
         this.currentToken = this.nextToken(var1);
         if (this.currentToken.getType() == 11) {
            String var4 = this.currentToken.getText();
            this.currentToken = this.nextToken(var1);
            this.currentToken = this.nextToken(var1);
            StringBuilder var5;
            if ("src".equalsIgnoreCase(var4)) {
               for(; this.currentToken != null && this.currentToken.getType() != 30 && this.currentToken.getType() != 29 && this.currentToken.getType() != -1; this.currentToken = this.nextToken(var1)) {
                  if (this.currentToken.getType() == 11) {
                     var3.add(new FontFace.FontFaceSrc(FontFace.FontFaceSrcType.REFERENCE, this.currentToken.getText()));
                  } else {
                     String var19;
                     if (this.currentToken.getType() == 43) {
                        ParsedValueImpl[] var17 = new ParsedValueImpl[]{new ParsedValueImpl(this.currentToken.getText(), StringConverter.getInstance()), new ParsedValueImpl(this.sourceOfStylesheet, (StyleConverter)null)};
                        ParsedValueImpl var18 = new ParsedValueImpl(var17, URLConverter.getInstance());
                        var19 = (String)var18.convert((Font)null);
                        URL var21 = null;

                        int var10;
                        try {
                           URI var9 = new URI(var19);
                           var21 = var9.toURL();
                        } catch (MalformedURLException | URISyntaxException var15) {
                           var10 = this.currentToken.getLine();
                           int var11 = this.currentToken.getOffset();
                           String var12 = MessageFormat.format("Could not resolve @font-face url [{2}] at [{0,number,#},{1,number,#}]", var10, var11, var19);
                           CssError var13 = this.createError(var12);
                           if (LOGGER.isLoggable(Level.WARNING)) {
                              LOGGER.warning(var13.toString());
                           }

                           this.reportError(var13);

                           while(this.currentToken != null) {
                              int var14 = this.currentToken.getType();
                              if (var14 == 29 || var14 == -1) {
                                 return null;
                              }

                              this.currentToken = this.nextToken(var1);
                           }
                        }

                        String var22 = null;

                        while(true) {
                           this.currentToken = this.nextToken(var1);
                           var10 = this.currentToken != null ? this.currentToken.getType() : -1;
                           if (var10 == 12) {
                              if ("format(".equalsIgnoreCase(this.currentToken.getText())) {
                                 continue;
                              }
                              break;
                           } else if (var10 != 11 && var10 != 10) {
                              if (var10 == 35) {
                                 continue;
                              }
                              break;
                           } else {
                              var22 = Utils.stripQuotes(this.currentToken.getText());
                           }
                        }

                        var3.add(new FontFace.FontFaceSrc(FontFace.FontFaceSrcType.URL, var21.toExternalForm(), var22));
                     } else {
                        int var6;
                        int var16;
                        CssError var20;
                        if (this.currentToken.getType() != 12) {
                           if (this.currentToken.getType() != 36) {
                              var16 = this.currentToken.getLine();
                              var6 = this.currentToken.getOffset();
                              var19 = MessageFormat.format("Unexpected TOKEN [" + this.currentToken.getText() + "] at [{0,number,#},{1,number,#}]", var16, var6);
                              var20 = this.createError(var19);
                              if (LOGGER.isLoggable(Level.WARNING)) {
                                 LOGGER.warning(var20.toString());
                              }

                              this.reportError(var20);
                           }
                        } else if (!"local(".equalsIgnoreCase(this.currentToken.getText())) {
                           var16 = this.currentToken.getLine();
                           var6 = this.currentToken.getOffset();
                           var19 = MessageFormat.format("Unknown @font-face src type [" + this.currentToken.getText() + ")] at [{0,number,#},{1,number,#}]", var16, var6);
                           var20 = this.createError(var19);
                           if (LOGGER.isLoggable(Level.WARNING)) {
                              LOGGER.warning(var20.toString());
                           }

                           this.reportError(var20);
                        } else {
                           this.currentToken = this.nextToken(var1);

                           for(var5 = new StringBuilder(); this.currentToken != null && this.currentToken.getType() != 35 && this.currentToken.getType() != -1; this.currentToken = this.nextToken(var1)) {
                              var5.append(this.currentToken.getText());
                           }

                           var6 = 0;
                           int var7 = var5.length();
                           if (var5.charAt(var6) == '\'' || var5.charAt(var6) == '"') {
                              ++var6;
                           }

                           if (var5.charAt(var7 - 1) == '\'' || var5.charAt(var7 - 1) == '"') {
                              --var7;
                           }

                           String var8 = var5.substring(var6, var7);
                           var3.add(new FontFace.FontFaceSrc(FontFace.FontFaceSrcType.LOCAL, var8));
                        }
                     }
                  }
               }
            } else {
               for(var5 = new StringBuilder(); this.currentToken != null && this.currentToken.getType() != 30 && this.currentToken.getType() != -1; this.currentToken = this.nextToken(var1)) {
                  var5.append(this.currentToken.getText());
               }

               var2.put(var4, var5.toString());
            }
         }
      } while(this.currentToken != null && this.currentToken.getType() != 29 && this.currentToken.getType() != -1);

      return new FontFace(var2, var3);
   }

   private Stylesheet handleImport(CSSLexer var1) {
      this.currentToken = this.nextToken(var1);
      if (this.currentToken != null && this.currentToken.getType() != -1) {
         int var2 = this.currentToken.getType();
         String var3 = null;
         if (var2 == 10 || var2 == 43) {
            var3 = this.currentToken.getText();
         }

         Stylesheet var4 = null;
         String var5 = this.sourceOfStylesheet;
         if (var3 != null) {
            ParsedValueImpl[] var6 = new ParsedValueImpl[]{new ParsedValueImpl(var3, StringConverter.getInstance()), new ParsedValueImpl(this.sourceOfStylesheet, (StyleConverter)null)};
            ParsedValueImpl var7 = new ParsedValueImpl(var6, URLConverter.getInstance());
            String var8 = (String)var7.convert((Font)null);
            var4 = StyleManager.loadStylesheet(var8);
            this.sourceOfStylesheet = var5;
         }

         if (var4 == null) {
            String var9 = MessageFormat.format("Could not import {0}", var3);
            CssError var10 = this.createError(var9);
            if (LOGGER.isLoggable(Level.WARNING)) {
               LOGGER.warning(var10.toString());
            }

            this.reportError(var10);
         }

         return var4;
      } else {
         return null;
      }
   }

   private List selectors(CSSLexer var1) {
      ArrayList var2 = new ArrayList();

      while(true) {
         Selector var3 = this.selector(var1);
         if (var3 != null) {
            var2.add(var3);
            if (this.currentToken == null || this.currentToken.getType() != 36) {
               return var2;
            }

            this.currentToken = this.nextToken(var1);
         } else {
            while(this.currentToken != null && this.currentToken.getType() != 29 && this.currentToken.getType() != -1) {
               this.currentToken = this.nextToken(var1);
            }

            this.currentToken = this.nextToken(var1);
            if (this.currentToken == null || this.currentToken.getType() == -1) {
               this.currentToken = null;
               return null;
            }
         }
      }
   }

   private Selector selector(CSSLexer var1) {
      ArrayList var2 = null;
      ArrayList var3 = null;
      SimpleSelector var4 = this.simpleSelector(var1);
      if (var4 == null) {
         return null;
      } else {
         while(true) {
            Combinator var5 = this.combinator(var1);
            if (var5 == null) {
               if (this.currentToken != null && this.currentToken.getType() == 41) {
                  this.currentToken = this.nextToken(var1);
               }

               if (var3 == null) {
                  return var4;
               }

               return new CompoundSelector(var3, var2);
            }

            if (var2 == null) {
               var2 = new ArrayList();
            }

            var2.add(var5);
            SimpleSelector var6 = this.simpleSelector(var1);
            if (var6 == null) {
               return null;
            }

            if (var3 == null) {
               var3 = new ArrayList();
               var3.add(var4);
            }

            var3.add(var6);
         }
      }
   }

   private SimpleSelector simpleSelector(CSSLexer var1) {
      String var2 = "*";
      String var3 = "";
      ArrayList var4 = null;
      ArrayList var5 = null;

      while(true) {
         int var6 = this.currentToken != null ? this.currentToken.getType() : 0;
         switch (var6) {
            case -1:
            case 27:
            case 28:
            case 36:
            case 40:
            case 41:
               return new SimpleSelector(var2, var4, var5, var3);
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 29:
            case 30:
            case 32:
            case 34:
            case 35:
            case 39:
            default:
               return null;
            case 11:
            case 33:
               var2 = this.currentToken.getText();
               break;
            case 31:
               this.currentToken = this.nextToken(var1);
               if (this.currentToken != null && var5 == null) {
                  var5 = new ArrayList();
               }

               if (this.currentToken.getType() == 11) {
                  var5.add(this.currentToken.getText());
               } else if (this.currentToken.getType() == 12) {
                  String var7 = this.functionalPseudo(var1);
                  var5.add(var7);
               } else {
                  this.currentToken = Token.INVALID_TOKEN;
               }

               if (this.currentToken.getType() == 0) {
                  return null;
               }
               break;
            case 37:
               var3 = this.currentToken.getText().substring(1);
               break;
            case 38:
               this.currentToken = this.nextToken(var1);
               if (this.currentToken == null || this.currentToken.getType() != 11) {
                  this.currentToken = Token.INVALID_TOKEN;
                  return null;
               }

               if (var4 == null) {
                  var4 = new ArrayList();
               }

               var4.add(this.currentToken.getText());
         }

         this.currentToken = var1.nextToken();
         if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest(this.currentToken.toString());
         }
      }
   }

   private String functionalPseudo(CSSLexer var1) {
      StringBuilder var2 = new StringBuilder(this.currentToken.getText());

      while(true) {
         this.currentToken = this.nextToken(var1);
         switch (this.currentToken.getType()) {
            case 10:
            case 11:
               var2.append(this.currentToken.getText());
               break;
            case 35:
               var2.append(')');
               return var2.toString();
            default:
               this.currentToken = Token.INVALID_TOKEN;
               return null;
         }
      }
   }

   private Combinator combinator(CSSLexer var1) {
      Combinator var2 = null;

      while(true) {
         int var3 = this.currentToken != null ? this.currentToken.getType() : 0;
         switch (var3) {
            case 11:
            case 31:
            case 33:
            case 37:
            case 38:
               return var2;
            case 27:
               var2 = Combinator.CHILD;
               break;
            case 40:
               if (var2 == null && " ".equals(this.currentToken.getText())) {
                  var2 = Combinator.DESCENDANT;
               }
               break;
            default:
               return null;
         }

         this.currentToken = var1.nextToken();
         if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest(this.currentToken.toString());
         }
      }
   }

   private List declarations(CSSLexer var1) {
      ArrayList var2 = new ArrayList();

      do {
         Declaration var3 = this.declaration(var1);
         if (var3 != null) {
            var2.add(var3);
         } else {
            while(this.currentToken != null && this.currentToken.getType() != 30 && this.currentToken.getType() != 29 && this.currentToken.getType() != -1) {
               this.currentToken = this.nextToken(var1);
            }

            if (this.currentToken != null && this.currentToken.getType() != 30) {
               return var2;
            }
         }

         while(this.currentToken != null && this.currentToken.getType() == 30) {
            this.currentToken = this.nextToken(var1);
         }
      } while(this.currentToken != null && this.currentToken.getType() == 11);

      return var2;
   }

   private Declaration declaration(CSSLexer var1) {
      if (this.currentToken != null) {
         this.currentToken.getType();
      } else {
         boolean var10000 = false;
      }

      if (this.currentToken != null && this.currentToken.getType() == 11) {
         String var3 = this.currentToken.getText();
         this.currentToken = this.nextToken(var1);
         if (this.currentToken != null && this.currentToken.getType() == 31) {
            this.currentToken = this.nextToken(var1);
            Term var13 = this.expr(var1);
            ParsedValueImpl var14 = null;

            try {
               var14 = var13 != null ? this.valueFor(var3, var13, var1) : null;
            } catch (ParseException var12) {
               Token var16 = var12.tok;
               int var8 = var16 != null ? var16.getLine() : -1;
               int var9 = var16 != null ? var16.getOffset() : -1;
               String var10 = MessageFormat.format("{2} while parsing ''{3}'' at [{0,number,#},{1,number,#}]", var8, var9, var12.getMessage(), var3);
               CssError var11 = this.createError(var10);
               if (LOGGER.isLoggable(Level.WARNING)) {
                  LOGGER.warning(var11.toString());
               }

               this.reportError(var11);
               return null;
            }

            boolean var15 = this.currentToken.getType() == 39;
            if (var15) {
               this.currentToken = this.nextToken(var1);
            }

            Declaration var17 = var14 != null ? new Declaration(var3.toLowerCase(Locale.ROOT), var14, var15) : null;
            return var17;
         } else {
            int var4 = this.currentToken.getLine();
            int var5 = this.currentToken.getOffset();
            String var6 = MessageFormat.format("Expected COLON at [{0,number,#},{1,number,#}]", var4, var5);
            CssError var7 = this.createError(var6);
            if (LOGGER.isLoggable(Level.WARNING)) {
               LOGGER.warning(var7.toString());
            }

            this.reportError(var7);
            return null;
         }
      } else {
         return null;
      }
   }

   private Term expr(CSSLexer var1) {
      Term var2 = this.term(var1);
      Term var3 = var2;

      while(true) {
         int var4 = var3 != null && this.currentToken != null ? this.currentToken.getType() : 0;
         if (var4 == 0) {
            this.skipExpr(var1);
            return null;
         }

         if (var4 == 30 || var4 == 39 || var4 == 29 || var4 == -1) {
            return var2;
         }

         if (var4 == 36) {
            this.currentToken = this.nextToken(var1);
            var3 = var3.nextLayer = this.term(var1);
         } else {
            var3 = var3.nextInSeries = this.term(var1);
         }
      }
   }

   private void skipExpr(CSSLexer var1) {
      int var2;
      do {
         this.currentToken = this.nextToken(var1);
         var2 = this.currentToken != null ? this.currentToken.getType() : 0;
      } while(var2 != 30 && var2 != 29 && var2 != -1);

   }

   private Term term(CSSLexer var1) {
      int var2 = this.currentToken != null ? this.currentToken.getType() : 0;
      Term var3;
      int var5;
      switch (var2) {
         case 10:
         case 11:
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 26:
         case 32:
         case 37:
         case 43:
         case 45:
         case 46:
            var3 = new Term(this.currentToken);
            this.currentToken = this.nextToken(var1);
            return var3;
         case 12:
         case 34:
            var3 = new Term(this.currentToken);
            this.currentToken = this.nextToken(var1);
            Term var4 = this.term(var1);
            var3.firstArg = var4;

            while(true) {
               var5 = this.currentToken != null ? this.currentToken.getType() : 0;
               if (var5 == 35) {
                  this.currentToken = this.nextToken(var1);
                  return var3;
               }

               if (var5 == 36) {
                  this.currentToken = this.nextToken(var1);
                  var4 = var4.nextArg = this.term(var1);
               } else {
                  var4 = var4.nextInSeries = this.term(var1);
               }
            }
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 33:
         case 35:
         case 36:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 44:
         default:
            var5 = this.currentToken != null ? this.currentToken.getLine() : -1;
            int var6 = this.currentToken != null ? this.currentToken.getOffset() : -1;
            String var7 = this.currentToken != null ? this.currentToken.getText() : "";
            String var8 = MessageFormat.format("Unexpected token {0}{1}{0} at [{2,number,#},{3,number,#}]", "'", var7, var5, var6);
            CssError var9 = this.createError(var8);
            if (LOGGER.isLoggable(Level.WARNING)) {
               LOGGER.warning(var9.toString());
            }

            this.reportError(var9);
            return null;
      }
   }

   static {
      ZERO_PERCENT = new ParsedValueImpl(new Size(0.0, SizeUnits.PERCENT), (StyleConverter)null);
      FIFTY_PERCENT = new ParsedValueImpl(new Size(50.0, SizeUnits.PERCENT), (StyleConverter)null);
      ONE_HUNDRED_PERCENT = new ParsedValueImpl(new Size(100.0, SizeUnits.PERCENT), (StyleConverter)null);
   }

   static class Term {
      final Token token;
      Term nextInSeries;
      Term nextLayer;
      Term firstArg;
      Term nextArg;

      Term(Token var1) {
         this.token = var1;
         this.nextLayer = null;
         this.nextInSeries = null;
         this.firstArg = null;
         this.nextArg = null;
      }

      Term() {
         this((Token)null);
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         if (this.token != null) {
            var1.append(String.valueOf(this.token.getText()));
         }

         if (this.nextInSeries != null) {
            var1.append("<nextInSeries>");
            var1.append(this.nextInSeries.toString());
            var1.append("</nextInSeries>\n");
         }

         if (this.nextLayer != null) {
            var1.append("<nextLayer>");
            var1.append(this.nextLayer.toString());
            var1.append("</nextLayer>\n");
         }

         if (this.firstArg != null) {
            var1.append("<args>");
            var1.append(this.firstArg.toString());
            if (this.nextArg != null) {
               var1.append(this.nextArg.toString());
            }

            var1.append("</args>");
         }

         return var1.toString();
      }
   }

   private static final class ParseException extends Exception {
      private final Token tok;
      private final String source;

      ParseException(String var1) {
         this(var1, (Token)null, (CSSParser)null);
      }

      ParseException(String var1, Token var2, CSSParser var3) {
         super(var1);
         this.tok = var2;
         if (var3.sourceOfStylesheet != null) {
            this.source = var3.sourceOfStylesheet;
         } else if (var3.sourceOfInlineStyle != null) {
            this.source = var3.sourceOfInlineStyle.toString();
         } else if (var3.stylesheetAsText != null) {
            this.source = var3.stylesheetAsText;
         } else {
            this.source = "?";
         }

      }

      public String toString() {
         StringBuilder var1 = new StringBuilder(super.getMessage());
         var1.append(this.source);
         if (this.tok != null) {
            var1.append(": ").append(this.tok.toString());
         }

         return var1.toString();
      }
   }
}
