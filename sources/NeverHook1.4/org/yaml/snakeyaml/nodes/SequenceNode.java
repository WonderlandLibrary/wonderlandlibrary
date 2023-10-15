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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SequenceNode
/*    */   extends CollectionNode<Node>
/*    */ {
/*    */   private final List<Node> value;
/*    */   
/*    */   public SequenceNode(Tag tag, boolean resolved, List<Node> value, Mark startMark, Mark endMark, DumperOptions.FlowStyle flowStyle) {
/* 34 */     super(tag, startMark, endMark, flowStyle);
/* 35 */     if (value == null) {
/* 36 */       throw new NullPointerException("value in a Node is required.");
/*    */     }
/* 38 */     this.value = value;
/* 39 */     this.resolved = resolved;
/*    */   }
/*    */   
/*    */   public SequenceNode(Tag tag, List<Node> value, DumperOptions.FlowStyle flowStyle) {
/* 43 */     this(tag, true, value, (Mark)null, (Mark)null, flowStyle);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public SequenceNode(Tag tag, List<Node> value, Boolean style) {
/* 53 */     this(tag, value, DumperOptions.FlowStyle.fromBoolean(style));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public SequenceNode(Tag tag, boolean resolved, List<Node> value, Mark startMark, Mark endMark, Boolean style) {
/* 64 */     this(tag, resolved, value, startMark, endMark, DumperOptions.FlowStyle.fromBoolean(style));
/*    */   }
/*    */ 
/*    */   
/*    */   public NodeId getNodeId() {
/* 69 */     return NodeId.sequence;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<Node> getValue() {
/* 78 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setListType(Class<? extends Object> listType) {
/* 82 */     for (Node node : this.value) {
/* 83 */       node.setType(listType);
/*    */     }
/*    */   }
/*    */   
/*    */   public String toString() {
/* 88 */     return "<" + getClass().getName() + " (tag=" + getTag() + ", value=" + getValue() + ")>";
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\nodes\SequenceNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */