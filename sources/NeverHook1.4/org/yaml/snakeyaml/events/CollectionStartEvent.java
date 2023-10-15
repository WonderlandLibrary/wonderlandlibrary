/*    */ package org.yaml.snakeyaml.events;
/*    */ 
/*    */ import org.yaml.snakeyaml.DumperOptions;
/*    */ import org.yaml.snakeyaml.error.Mark;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class CollectionStartEvent
/*    */   extends NodeEvent
/*    */ {
/*    */   private final String tag;
/*    */   private final boolean implicit;
/*    */   private final DumperOptions.FlowStyle flowStyle;
/*    */   
/*    */   public CollectionStartEvent(String anchor, String tag, boolean implicit, Mark startMark, Mark endMark, DumperOptions.FlowStyle flowStyle) {
/* 34 */     super(anchor, startMark, endMark);
/* 35 */     this.tag = tag;
/* 36 */     this.implicit = implicit;
/* 37 */     if (flowStyle == null) throw new NullPointerException("Flow style must be provided."); 
/* 38 */     this.flowStyle = flowStyle;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public CollectionStartEvent(String anchor, String tag, boolean implicit, Mark startMark, Mark endMark, Boolean flowStyle) {
/* 49 */     this(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.fromBoolean(flowStyle));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTag() {
/* 59 */     return this.tag;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean getImplicit() {
/* 69 */     return this.implicit;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DumperOptions.FlowStyle getFlowStyle() {
/* 79 */     return this.flowStyle;
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getArguments() {
/* 84 */     return super.getArguments() + ", tag=" + this.tag + ", implicit=" + this.implicit;
/*    */   }
/*    */   
/*    */   public boolean isFlow() {
/* 88 */     return (DumperOptions.FlowStyle.FLOW == this.flowStyle);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\events\CollectionStartEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */