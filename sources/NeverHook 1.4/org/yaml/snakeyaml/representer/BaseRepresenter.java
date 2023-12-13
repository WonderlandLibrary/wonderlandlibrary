/*     */ package org.yaml.snakeyaml.representer;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.introspector.PropertyUtils;
/*     */ import org.yaml.snakeyaml.nodes.AnchorNode;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
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
/*     */ public abstract class BaseRepresenter
/*     */ {
/*  41 */   protected final Map<Class<?>, Represent> representers = new HashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   protected Represent nullRepresenter;
/*     */ 
/*     */ 
/*     */   
/*  49 */   protected final Map<Class<?>, Represent> multiRepresenters = new LinkedHashMap<>();
/*  50 */   protected DumperOptions.ScalarStyle defaultScalarStyle = null;
/*  51 */   protected DumperOptions.FlowStyle defaultFlowStyle = DumperOptions.FlowStyle.AUTO;
/*  52 */   protected final Map<Object, Node> representedObjects = new IdentityHashMap<Object, Node>() {
/*     */       private static final long serialVersionUID = -5576159264232131854L;
/*     */       
/*     */       public Node put(Object key, Node value) {
/*  56 */         return (Node)super.put(key, new AnchorNode(value));
/*     */       }
/*     */     };
/*     */   
/*     */   protected Object objectToRepresent;
/*     */   private PropertyUtils propertyUtils;
/*     */   private boolean explicitPropertyUtils = false;
/*     */   
/*     */   public Node represent(Object data) {
/*  65 */     Node node = representData(data);
/*  66 */     this.representedObjects.clear();
/*  67 */     this.objectToRepresent = null;
/*  68 */     return node;
/*     */   }
/*     */   protected final Node representData(Object data) {
/*     */     Node node;
/*  72 */     this.objectToRepresent = data;
/*     */     
/*  74 */     if (this.representedObjects.containsKey(this.objectToRepresent)) {
/*  75 */       node = this.representedObjects.get(this.objectToRepresent);
/*  76 */       return node;
/*     */     } 
/*     */ 
/*     */     
/*  80 */     if (data == null) {
/*  81 */       node = this.nullRepresenter.representData(null);
/*  82 */       return node;
/*     */     } 
/*     */ 
/*     */     
/*  86 */     Class<?> clazz = data.getClass();
/*  87 */     if (this.representers.containsKey(clazz)) {
/*  88 */       Represent representer = this.representers.get(clazz);
/*  89 */       node = representer.representData(data);
/*     */     } else {
/*     */       
/*  92 */       for (Class<?> repr : this.multiRepresenters.keySet()) {
/*  93 */         if (repr != null && repr.isInstance(data)) {
/*  94 */           Represent representer = this.multiRepresenters.get(repr);
/*  95 */           node = representer.representData(data);
/*  96 */           return node;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 101 */       if (this.multiRepresenters.containsKey(null)) {
/* 102 */         Represent representer = this.multiRepresenters.get(null);
/* 103 */         node = representer.representData(data);
/*     */       } else {
/* 105 */         Represent representer = this.representers.get(null);
/* 106 */         node = representer.representData(data);
/*     */       } 
/*     */     } 
/* 109 */     return node;
/*     */   }
/*     */   
/*     */   protected Node representScalar(Tag tag, String value, DumperOptions.ScalarStyle style) {
/* 113 */     if (style == null) {
/* 114 */       style = this.defaultScalarStyle;
/*     */     }
/* 116 */     return (Node)new ScalarNode(tag, value, null, null, style);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Node representScalar(Tag tag, String value) {
/* 121 */     return representScalar(tag, value, null);
/*     */   }
/*     */   
/*     */   protected Node representSequence(Tag tag, Iterable<?> sequence, DumperOptions.FlowStyle flowStyle) {
/* 125 */     int size = 10;
/* 126 */     if (sequence instanceof List) {
/* 127 */       size = ((List)sequence).size();
/*     */     }
/* 129 */     List<Node> value = new ArrayList<>(size);
/* 130 */     SequenceNode node = new SequenceNode(tag, value, flowStyle);
/* 131 */     this.representedObjects.put(this.objectToRepresent, node);
/* 132 */     DumperOptions.FlowStyle bestStyle = DumperOptions.FlowStyle.FLOW;
/* 133 */     for (Object item : sequence) {
/* 134 */       Node nodeItem = representData(item);
/* 135 */       if (!(nodeItem instanceof ScalarNode) || !((ScalarNode)nodeItem).isPlain()) {
/* 136 */         bestStyle = DumperOptions.FlowStyle.BLOCK;
/*     */       }
/* 138 */       value.add(nodeItem);
/*     */     } 
/* 140 */     if (flowStyle == DumperOptions.FlowStyle.AUTO) {
/* 141 */       if (this.defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
/* 142 */         node.setFlowStyle(this.defaultFlowStyle);
/*     */       } else {
/* 144 */         node.setFlowStyle(bestStyle);
/*     */       } 
/*     */     }
/* 147 */     return (Node)node;
/*     */   }
/*     */   
/*     */   protected Node representMapping(Tag tag, Map<?, ?> mapping, DumperOptions.FlowStyle flowStyle) {
/* 151 */     List<NodeTuple> value = new ArrayList<>(mapping.size());
/* 152 */     MappingNode node = new MappingNode(tag, value, flowStyle);
/* 153 */     this.representedObjects.put(this.objectToRepresent, node);
/* 154 */     DumperOptions.FlowStyle bestStyle = DumperOptions.FlowStyle.FLOW;
/* 155 */     for (Map.Entry<?, ?> entry : mapping.entrySet()) {
/* 156 */       Node nodeKey = representData(entry.getKey());
/* 157 */       Node nodeValue = representData(entry.getValue());
/* 158 */       if (!(nodeKey instanceof ScalarNode) || !((ScalarNode)nodeKey).isPlain()) {
/* 159 */         bestStyle = DumperOptions.FlowStyle.BLOCK;
/*     */       }
/* 161 */       if (!(nodeValue instanceof ScalarNode) || !((ScalarNode)nodeValue).isPlain()) {
/* 162 */         bestStyle = DumperOptions.FlowStyle.BLOCK;
/*     */       }
/* 164 */       value.add(new NodeTuple(nodeKey, nodeValue));
/*     */     } 
/* 166 */     if (flowStyle == DumperOptions.FlowStyle.AUTO) {
/* 167 */       if (this.defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
/* 168 */         node.setFlowStyle(this.defaultFlowStyle);
/*     */       } else {
/* 170 */         node.setFlowStyle(bestStyle);
/*     */       } 
/*     */     }
/* 173 */     return (Node)node;
/*     */   }
/*     */   
/*     */   public void setDefaultScalarStyle(DumperOptions.ScalarStyle defaultStyle) {
/* 177 */     this.defaultScalarStyle = defaultStyle;
/*     */   }
/*     */   
/*     */   public DumperOptions.ScalarStyle getDefaultScalarStyle() {
/* 181 */     if (this.defaultScalarStyle == null) {
/* 182 */       return DumperOptions.ScalarStyle.PLAIN;
/*     */     }
/* 184 */     return this.defaultScalarStyle;
/*     */   }
/*     */   
/*     */   public void setDefaultFlowStyle(DumperOptions.FlowStyle defaultFlowStyle) {
/* 188 */     this.defaultFlowStyle = defaultFlowStyle;
/*     */   }
/*     */   
/*     */   public DumperOptions.FlowStyle getDefaultFlowStyle() {
/* 192 */     return this.defaultFlowStyle;
/*     */   }
/*     */   
/*     */   public void setPropertyUtils(PropertyUtils propertyUtils) {
/* 196 */     this.propertyUtils = propertyUtils;
/* 197 */     this.explicitPropertyUtils = true;
/*     */   }
/*     */   
/*     */   public final PropertyUtils getPropertyUtils() {
/* 201 */     if (this.propertyUtils == null) {
/* 202 */       this.propertyUtils = new PropertyUtils();
/*     */     }
/* 204 */     return this.propertyUtils;
/*     */   }
/*     */   
/*     */   public final boolean isExplicitPropertyUtils() {
/* 208 */     return this.explicitPropertyUtils;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\representer\BaseRepresenter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */