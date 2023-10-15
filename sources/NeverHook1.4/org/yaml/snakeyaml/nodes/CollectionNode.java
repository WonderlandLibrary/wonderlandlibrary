/*    */ package org.yaml.snakeyaml.nodes;
/*    */ 
/*    */ import java.util.List;
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
/*    */ public abstract class CollectionNode<T>
/*    */   extends Node
/*    */ {
/*    */   private DumperOptions.FlowStyle flowStyle;
/*    */   
/*    */   public CollectionNode(Tag tag, Mark startMark, Mark endMark, DumperOptions.FlowStyle flowStyle) {
/* 31 */     super(tag, startMark, endMark);
/* 32 */     setFlowStyle(flowStyle);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public CollectionNode(Tag tag, Mark startMark, Mark endMark, Boolean flowStyle) {
/* 42 */     this(tag, startMark, endMark, DumperOptions.FlowStyle.fromBoolean(flowStyle));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract List<T> getValue();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DumperOptions.FlowStyle getFlowStyle() {
/* 59 */     return this.flowStyle;
/*    */   }
/*    */   
/*    */   public void setFlowStyle(DumperOptions.FlowStyle flowStyle) {
/* 63 */     if (flowStyle == null) throw new NullPointerException("Flow style must be provided."); 
/* 64 */     this.flowStyle = flowStyle;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public void setFlowStyle(Boolean flowStyle) {
/* 74 */     setFlowStyle(DumperOptions.FlowStyle.fromBoolean(flowStyle));
/*    */   }
/*    */   
/*    */   public void setEndMark(Mark endMark) {
/* 78 */     this.endMark = endMark;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\nodes\CollectionNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */