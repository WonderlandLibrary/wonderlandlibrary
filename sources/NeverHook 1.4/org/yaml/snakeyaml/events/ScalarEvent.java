/*     */ package org.yaml.snakeyaml.events;
/*     */ 
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.error.Mark;
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
/*     */ public final class ScalarEvent
/*     */   extends NodeEvent
/*     */ {
/*     */   private final String tag;
/*     */   private final DumperOptions.ScalarStyle style;
/*     */   private final String value;
/*     */   private final ImplicitTuple implicit;
/*     */   
/*     */   public ScalarEvent(String anchor, String tag, ImplicitTuple implicit, String value, Mark startMark, Mark endMark, DumperOptions.ScalarStyle style) {
/*  37 */     super(anchor, startMark, endMark);
/*  38 */     this.tag = tag;
/*  39 */     this.implicit = implicit;
/*  40 */     if (value == null) throw new NullPointerException("Value must be provided."); 
/*  41 */     this.value = value;
/*  42 */     if (style == null) throw new NullPointerException("Style must be provided."); 
/*  43 */     this.style = style;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ScalarEvent(String anchor, String tag, ImplicitTuple implicit, String value, Mark startMark, Mark endMark, Character style) {
/*  54 */     this(anchor, tag, implicit, value, startMark, endMark, DumperOptions.ScalarStyle.createStyle(style));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTag() {
/*  64 */     return this.tag;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DumperOptions.ScalarStyle getScalarStyle() {
/*  87 */     return this.style;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Character getStyle() {
/*  96 */     return this.style.getChar();
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
/*     */   public String getValue() {
/* 108 */     return this.value;
/*     */   }
/*     */   
/*     */   public ImplicitTuple getImplicit() {
/* 112 */     return this.implicit;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getArguments() {
/* 117 */     return super.getArguments() + ", tag=" + this.tag + ", " + this.implicit + ", value=" + this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public Event.ID getEventId() {
/* 122 */     return Event.ID.Scalar;
/*     */   }
/*     */   
/*     */   public boolean isPlain() {
/* 126 */     return (this.style == DumperOptions.ScalarStyle.PLAIN);
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\events\ScalarEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */