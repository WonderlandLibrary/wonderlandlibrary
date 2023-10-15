/*     */ package org.yaml.snakeyaml.nodes;
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
/*     */ public class ScalarNode
/*     */   extends Node
/*     */ {
/*     */   private DumperOptions.ScalarStyle style;
/*     */   private String value;
/*     */   
/*     */   public ScalarNode(Tag tag, String value, Mark startMark, Mark endMark, DumperOptions.ScalarStyle style) {
/*  32 */     this(tag, true, value, startMark, endMark, style);
/*     */   }
/*     */ 
/*     */   
/*     */   public ScalarNode(Tag tag, boolean resolved, String value, Mark startMark, Mark endMark, DumperOptions.ScalarStyle style) {
/*  37 */     super(tag, startMark, endMark);
/*  38 */     if (value == null) {
/*  39 */       throw new NullPointerException("value in a Node is required.");
/*     */     }
/*  41 */     this.value = value;
/*  42 */     if (style == null) throw new NullPointerException("Scalar style must be provided."); 
/*  43 */     this.style = style;
/*  44 */     this.resolved = resolved;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ScalarNode(Tag tag, String value, Mark startMark, Mark endMark, Character style) {
/*  54 */     this(tag, value, startMark, endMark, DumperOptions.ScalarStyle.createStyle(style));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ScalarNode(Tag tag, boolean resolved, String value, Mark startMark, Mark endMark, Character style) {
/*  65 */     this(tag, resolved, value, startMark, endMark, DumperOptions.ScalarStyle.createStyle(style));
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
/*     */   @Deprecated
/*     */   public Character getStyle() {
/*  79 */     return this.style.getChar();
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
/*     */   public DumperOptions.ScalarStyle getScalarStyle() {
/*  91 */     return this.style;
/*     */   }
/*     */ 
/*     */   
/*     */   public NodeId getNodeId() {
/*  96 */     return NodeId.scalar;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue() {
/* 105 */     return this.value;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 109 */     return "<" + getClass().getName() + " (tag=" + getTag() + ", value=" + getValue() + ")>";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPlain() {
/* 114 */     return (this.style == DumperOptions.ScalarStyle.PLAIN);
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\nodes\ScalarNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */