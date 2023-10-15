/*     */ package org.yaml.snakeyaml.nodes;
/*     */ 
/*     */ import java.util.List;
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
/*     */ public class MappingNode
/*     */   extends CollectionNode<NodeTuple>
/*     */ {
/*     */   private List<NodeTuple> value;
/*     */   private boolean merged = false;
/*     */   
/*     */   public MappingNode(Tag tag, boolean resolved, List<NodeTuple> value, Mark startMark, Mark endMark, DumperOptions.FlowStyle flowStyle) {
/*  35 */     super(tag, startMark, endMark, flowStyle);
/*  36 */     if (value == null) {
/*  37 */       throw new NullPointerException("value in a Node is required.");
/*     */     }
/*  39 */     this.value = value;
/*  40 */     this.resolved = resolved;
/*     */   }
/*     */   
/*     */   public MappingNode(Tag tag, List<NodeTuple> value, DumperOptions.FlowStyle flowStyle) {
/*  44 */     this(tag, true, value, (Mark)null, (Mark)null, flowStyle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public MappingNode(Tag tag, boolean resolved, List<NodeTuple> value, Mark startMark, Mark endMark, Boolean flowStyle) {
/*  55 */     this(tag, resolved, value, startMark, endMark, DumperOptions.FlowStyle.fromBoolean(flowStyle));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public MappingNode(Tag tag, List<NodeTuple> value, Boolean flowStyle) {
/*  65 */     this(tag, value, DumperOptions.FlowStyle.fromBoolean(flowStyle));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public NodeId getNodeId() {
/*  71 */     return NodeId.mapping;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<NodeTuple> getValue() {
/*  80 */     return this.value;
/*     */   }
/*     */   
/*     */   public void setValue(List<NodeTuple> mergedValue) {
/*  84 */     this.value = mergedValue;
/*     */   }
/*     */   
/*     */   public void setOnlyKeyType(Class<? extends Object> keyType) {
/*  88 */     for (NodeTuple nodes : this.value) {
/*  89 */       nodes.getKeyNode().setType(keyType);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setTypes(Class<? extends Object> keyType, Class<? extends Object> valueType) {
/*  94 */     for (NodeTuple nodes : this.value) {
/*  95 */       nodes.getValueNode().setType(valueType);
/*  96 */       nodes.getKeyNode().setType(keyType);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 103 */     StringBuilder buf = new StringBuilder();
/* 104 */     for (NodeTuple node : getValue()) {
/* 105 */       buf.append("{ key=");
/* 106 */       buf.append(node.getKeyNode());
/* 107 */       buf.append("; value=");
/* 108 */       if (node.getValueNode() instanceof CollectionNode) {
/*     */         
/* 110 */         buf.append(System.identityHashCode(node.getValueNode()));
/*     */       } else {
/* 112 */         buf.append(node.toString());
/*     */       } 
/* 114 */       buf.append(" }");
/*     */     } 
/* 116 */     String values = buf.toString();
/* 117 */     return "<" + getClass().getName() + " (tag=" + getTag() + ", values=" + values + ")>";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMerged(boolean merged) {
/* 125 */     this.merged = merged;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMerged() {
/* 132 */     return this.merged;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\nodes\MappingNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */