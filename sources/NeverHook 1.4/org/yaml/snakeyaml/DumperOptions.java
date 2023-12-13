/*     */ package org.yaml.snakeyaml;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.serializer.AnchorGenerator;
/*     */ import org.yaml.snakeyaml.serializer.NumberAnchorGenerator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DumperOptions
/*     */ {
/*     */   public enum ScalarStyle
/*     */   {
/*  39 */     DOUBLE_QUOTED((String)Character.valueOf('"')), SINGLE_QUOTED((String)Character.valueOf('\'')), LITERAL((String)Character.valueOf('|')),
/*  40 */     FOLDED((String)Character.valueOf('>')), PLAIN(null);
/*     */     private Character styleChar;
/*     */     
/*     */     ScalarStyle(Character style) {
/*  44 */       this.styleChar = style;
/*     */     }
/*     */     
/*     */     public Character getChar() {
/*  48 */       return this.styleChar;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  53 */       return "Scalar style: '" + this.styleChar + "'";
/*     */     }
/*     */     
/*     */     public static ScalarStyle createStyle(Character style) {
/*  57 */       if (style == null) {
/*  58 */         return PLAIN;
/*     */       }
/*  60 */       switch (style.charValue()) {
/*     */         case '"':
/*  62 */           return DOUBLE_QUOTED;
/*     */         case '\'':
/*  64 */           return SINGLE_QUOTED;
/*     */         case '|':
/*  66 */           return LITERAL;
/*     */         case '>':
/*  68 */           return FOLDED;
/*     */       } 
/*  70 */       throw new YAMLException("Unknown scalar style character: " + style);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum FlowStyle
/*     */   {
/*  85 */     FLOW((String)Boolean.TRUE), BLOCK((String)Boolean.FALSE), AUTO(null);
/*     */     
/*     */     private Boolean styleBoolean;
/*     */     
/*     */     FlowStyle(Boolean flowStyle) {
/*  90 */       this.styleBoolean = flowStyle;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public static FlowStyle fromBoolean(Boolean flowStyle) {
/* 100 */       return (flowStyle == null) ? AUTO : (flowStyle.booleanValue() ? FLOW : BLOCK);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Boolean getStyleBoolean() {
/* 106 */       return this.styleBoolean;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 111 */       return "Flow style: '" + this.styleBoolean + "'";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public enum LineBreak
/*     */   {
/* 119 */     WIN("\r\n"), MAC("\r"), UNIX("\n");
/*     */     
/*     */     private String lineBreak;
/*     */     
/*     */     LineBreak(String lineBreak) {
/* 124 */       this.lineBreak = lineBreak;
/*     */     }
/*     */     
/*     */     public String getString() {
/* 128 */       return this.lineBreak;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 133 */       return "Line break: " + name();
/*     */     }
/*     */     
/*     */     public static LineBreak getPlatformLineBreak() {
/* 137 */       String platformLineBreak = System.getProperty("line.separator");
/* 138 */       for (LineBreak lb : values()) {
/* 139 */         if (lb.lineBreak.equals(platformLineBreak)) {
/* 140 */           return lb;
/*     */         }
/*     */       } 
/* 143 */       return UNIX;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Version
/*     */   {
/* 151 */     V1_0((String)new Integer[] { Integer.valueOf(1), Integer.valueOf(0) }), V1_1((String)new Integer[] { Integer.valueOf(1), Integer.valueOf(1) });
/*     */     
/*     */     private Integer[] version;
/*     */     
/*     */     Version(Integer[] version) {
/* 156 */       this.version = version;
/*     */     }
/*     */     
/* 159 */     public int major() { return this.version[0].intValue(); } public int minor() {
/* 160 */       return this.version[1].intValue();
/*     */     }
/*     */     public String getRepresentation() {
/* 163 */       return this.version[0] + "." + this.version[1];
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 168 */       return "Version: " + getRepresentation();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public enum NonPrintableStyle
/*     */   {
/* 176 */     BINARY,
/*     */ 
/*     */ 
/*     */     
/* 180 */     ESCAPE;
/*     */   }
/*     */   
/* 183 */   private ScalarStyle defaultStyle = ScalarStyle.PLAIN;
/* 184 */   private FlowStyle defaultFlowStyle = FlowStyle.AUTO;
/*     */   private boolean canonical = false;
/*     */   private boolean allowUnicode = true;
/*     */   private boolean allowReadOnlyProperties = false;
/* 188 */   private int indent = 2;
/* 189 */   private int indicatorIndent = 0;
/*     */   private boolean indentWithIndicator = false;
/* 191 */   private int bestWidth = 80;
/*     */   private boolean splitLines = true;
/* 193 */   private LineBreak lineBreak = LineBreak.UNIX;
/*     */   private boolean explicitStart = false;
/*     */   private boolean explicitEnd = false;
/* 196 */   private TimeZone timeZone = null;
/* 197 */   private int maxSimpleKeyLength = 128;
/* 198 */   private NonPrintableStyle nonPrintableStyle = NonPrintableStyle.BINARY;
/*     */   
/* 200 */   private Version version = null;
/* 201 */   private Map<String, String> tags = null;
/* 202 */   private Boolean prettyFlow = Boolean.valueOf(false);
/* 203 */   private AnchorGenerator anchorGenerator = (AnchorGenerator)new NumberAnchorGenerator(0);
/*     */   
/*     */   public boolean isAllowUnicode() {
/* 206 */     return this.allowUnicode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowUnicode(boolean allowUnicode) {
/* 220 */     this.allowUnicode = allowUnicode;
/*     */   }
/*     */   
/*     */   public ScalarStyle getDefaultScalarStyle() {
/* 224 */     return this.defaultStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultScalarStyle(ScalarStyle defaultStyle) {
/* 235 */     if (defaultStyle == null) {
/* 236 */       throw new NullPointerException("Use ScalarStyle enum.");
/*     */     }
/* 238 */     this.defaultStyle = defaultStyle;
/*     */   }
/*     */   
/*     */   public void setIndent(int indent) {
/* 242 */     if (indent < 1) {
/* 243 */       throw new YAMLException("Indent must be at least 1");
/*     */     }
/* 245 */     if (indent > 10) {
/* 246 */       throw new YAMLException("Indent must be at most 10");
/*     */     }
/* 248 */     this.indent = indent;
/*     */   }
/*     */   
/*     */   public int getIndent() {
/* 252 */     return this.indent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndicatorIndent(int indicatorIndent) {
/* 260 */     if (indicatorIndent < 0) {
/* 261 */       throw new YAMLException("Indicator indent must be non-negative.");
/*     */     }
/* 263 */     if (indicatorIndent > 9) {
/* 264 */       throw new YAMLException("Indicator indent must be at most Emitter.MAX_INDENT-1: 9");
/*     */     }
/* 266 */     this.indicatorIndent = indicatorIndent;
/*     */   }
/*     */   
/*     */   public int getIndicatorIndent() {
/* 270 */     return this.indicatorIndent;
/*     */   }
/*     */   
/*     */   public boolean getIndentWithIndicator() {
/* 274 */     return this.indentWithIndicator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndentWithIndicator(boolean indentWithIndicator) {
/* 282 */     this.indentWithIndicator = indentWithIndicator;
/*     */   }
/*     */   
/*     */   public void setVersion(Version version) {
/* 286 */     this.version = version;
/*     */   }
/*     */   
/*     */   public Version getVersion() {
/* 290 */     return this.version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCanonical(boolean canonical) {
/* 300 */     this.canonical = canonical;
/*     */   }
/*     */   
/*     */   public boolean isCanonical() {
/* 304 */     return this.canonical;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrettyFlow(boolean prettyFlow) {
/* 315 */     this.prettyFlow = Boolean.valueOf(prettyFlow);
/*     */   }
/*     */   
/*     */   public boolean isPrettyFlow() {
/* 319 */     return this.prettyFlow.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWidth(int bestWidth) {
/* 331 */     this.bestWidth = bestWidth;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 335 */     return this.bestWidth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSplitLines(boolean splitLines) {
/* 346 */     this.splitLines = splitLines;
/*     */   }
/*     */   
/*     */   public boolean getSplitLines() {
/* 350 */     return this.splitLines;
/*     */   }
/*     */   
/*     */   public LineBreak getLineBreak() {
/* 354 */     return this.lineBreak;
/*     */   }
/*     */   
/*     */   public void setDefaultFlowStyle(FlowStyle defaultFlowStyle) {
/* 358 */     if (defaultFlowStyle == null) {
/* 359 */       throw new NullPointerException("Use FlowStyle enum.");
/*     */     }
/* 361 */     this.defaultFlowStyle = defaultFlowStyle;
/*     */   }
/*     */   
/*     */   public FlowStyle getDefaultFlowStyle() {
/* 365 */     return this.defaultFlowStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLineBreak(LineBreak lineBreak) {
/* 375 */     if (lineBreak == null) {
/* 376 */       throw new NullPointerException("Specify line break.");
/*     */     }
/* 378 */     this.lineBreak = lineBreak;
/*     */   }
/*     */   
/*     */   public boolean isExplicitStart() {
/* 382 */     return this.explicitStart;
/*     */   }
/*     */   
/*     */   public void setExplicitStart(boolean explicitStart) {
/* 386 */     this.explicitStart = explicitStart;
/*     */   }
/*     */   
/*     */   public boolean isExplicitEnd() {
/* 390 */     return this.explicitEnd;
/*     */   }
/*     */   
/*     */   public void setExplicitEnd(boolean explicitEnd) {
/* 394 */     this.explicitEnd = explicitEnd;
/*     */   }
/*     */   
/*     */   public Map<String, String> getTags() {
/* 398 */     return this.tags;
/*     */   }
/*     */   
/*     */   public void setTags(Map<String, String> tags) {
/* 402 */     this.tags = tags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAllowReadOnlyProperties() {
/* 412 */     return this.allowReadOnlyProperties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowReadOnlyProperties(boolean allowReadOnlyProperties) {
/* 424 */     this.allowReadOnlyProperties = allowReadOnlyProperties;
/*     */   }
/*     */   
/*     */   public TimeZone getTimeZone() {
/* 428 */     return this.timeZone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeZone(TimeZone timeZone) {
/* 437 */     this.timeZone = timeZone;
/*     */   }
/*     */ 
/*     */   
/*     */   public AnchorGenerator getAnchorGenerator() {
/* 442 */     return this.anchorGenerator;
/*     */   }
/*     */   
/*     */   public void setAnchorGenerator(AnchorGenerator anchorGenerator) {
/* 446 */     this.anchorGenerator = anchorGenerator;
/*     */   }
/*     */   
/*     */   public int getMaxSimpleKeyLength() {
/* 450 */     return this.maxSimpleKeyLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxSimpleKeyLength(int maxSimpleKeyLength) {
/* 459 */     if (maxSimpleKeyLength > 1024) {
/* 460 */       throw new YAMLException("The simple key must not span more than 1024 stream characters. See https://yaml.org/spec/1.1/#id934537");
/*     */     }
/* 462 */     this.maxSimpleKeyLength = maxSimpleKeyLength;
/*     */   }
/*     */   
/*     */   public NonPrintableStyle getNonPrintableStyle() {
/* 466 */     return this.nonPrintableStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNonPrintableStyle(NonPrintableStyle style) {
/* 475 */     this.nonPrintableStyle = style;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\DumperOptions.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */