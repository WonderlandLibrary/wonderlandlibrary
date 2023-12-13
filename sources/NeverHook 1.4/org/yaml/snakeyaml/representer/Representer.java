/*     */ package org.yaml.snakeyaml.representer;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.TypeDescription;
/*     */ import org.yaml.snakeyaml.introspector.Property;
/*     */ import org.yaml.snakeyaml.introspector.PropertyUtils;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
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
/*     */ public class Representer
/*     */   extends SafeRepresenter
/*     */ {
/*  46 */   protected Map<Class<? extends Object>, TypeDescription> typeDefinitions = Collections.emptyMap();
/*     */ 
/*     */   
/*     */   public Representer() {
/*  50 */     this.representers.put(null, new RepresentJavaBean());
/*     */   }
/*     */   
/*     */   public Representer(DumperOptions options) {
/*  54 */     super(options);
/*  55 */     this.representers.put(null, new RepresentJavaBean());
/*     */   }
/*     */   
/*     */   public TypeDescription addTypeDescription(TypeDescription td) {
/*  59 */     if (Collections.EMPTY_MAP == this.typeDefinitions) {
/*  60 */       this.typeDefinitions = new HashMap<>();
/*     */     }
/*  62 */     if (td.getTag() != null) {
/*  63 */       addClassTag(td.getType(), td.getTag());
/*     */     }
/*  65 */     td.setPropertyUtils(getPropertyUtils());
/*  66 */     return this.typeDefinitions.put(td.getType(), td);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPropertyUtils(PropertyUtils propertyUtils) {
/*  71 */     super.setPropertyUtils(propertyUtils);
/*  72 */     Collection<TypeDescription> tds = this.typeDefinitions.values();
/*  73 */     for (TypeDescription typeDescription : tds)
/*  74 */       typeDescription.setPropertyUtils(propertyUtils); 
/*     */   }
/*     */   
/*     */   protected class RepresentJavaBean
/*     */     implements Represent {
/*     */     public Node representData(Object data) {
/*  80 */       return (Node)Representer.this.representJavaBean(Representer.this.getProperties((Class)data.getClass()), data);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MappingNode representJavaBean(Set<Property> properties, Object javaBean) {
/*  99 */     List<NodeTuple> value = new ArrayList<>(properties.size());
/*     */     
/* 101 */     Tag customTag = this.classTags.get(javaBean.getClass());
/* 102 */     Tag tag = (customTag != null) ? customTag : new Tag(javaBean.getClass());
/*     */     
/* 104 */     MappingNode node = new MappingNode(tag, value, DumperOptions.FlowStyle.AUTO);
/* 105 */     this.representedObjects.put(javaBean, node);
/* 106 */     DumperOptions.FlowStyle bestStyle = DumperOptions.FlowStyle.FLOW;
/* 107 */     for (Property property : properties) {
/* 108 */       Object memberValue = property.get(javaBean);
/* 109 */       Tag customPropertyTag = (memberValue == null) ? null : this.classTags.get(memberValue.getClass());
/*     */       
/* 111 */       NodeTuple tuple = representJavaBeanProperty(javaBean, property, memberValue, customPropertyTag);
/*     */       
/* 113 */       if (tuple == null) {
/*     */         continue;
/*     */       }
/* 116 */       if (!((ScalarNode)tuple.getKeyNode()).isPlain()) {
/* 117 */         bestStyle = DumperOptions.FlowStyle.BLOCK;
/*     */       }
/* 119 */       Node nodeValue = tuple.getValueNode();
/* 120 */       if (!(nodeValue instanceof ScalarNode) || !((ScalarNode)nodeValue).isPlain()) {
/* 121 */         bestStyle = DumperOptions.FlowStyle.BLOCK;
/*     */       }
/* 123 */       value.add(tuple);
/*     */     } 
/* 125 */     if (this.defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
/* 126 */       node.setFlowStyle(this.defaultFlowStyle);
/*     */     } else {
/* 128 */       node.setFlowStyle(bestStyle);
/*     */     } 
/* 130 */     return node;
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
/*     */   protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag) {
/* 149 */     ScalarNode nodeKey = (ScalarNode)representData(property.getName());
/*     */     
/* 151 */     boolean hasAlias = this.representedObjects.containsKey(propertyValue);
/*     */     
/* 153 */     Node nodeValue = representData(propertyValue);
/*     */     
/* 155 */     if (propertyValue != null && !hasAlias) {
/* 156 */       NodeId nodeId = nodeValue.getNodeId();
/* 157 */       if (customTag == null) {
/* 158 */         if (nodeId == NodeId.scalar) {
/*     */           
/* 160 */           if (property.getType() != Enum.class && 
/* 161 */             propertyValue instanceof Enum) {
/* 162 */             nodeValue.setTag(Tag.STR);
/*     */           }
/*     */         } else {
/*     */           
/* 166 */           if (nodeId == NodeId.mapping && 
/* 167 */             property.getType() == propertyValue.getClass() && 
/* 168 */             !(propertyValue instanceof Map) && 
/* 169 */             !nodeValue.getTag().equals(Tag.SET)) {
/* 170 */             nodeValue.setTag(Tag.MAP);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 175 */           checkGlobalTag(property, nodeValue, propertyValue);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 180 */     return new NodeTuple((Node)nodeKey, nodeValue);
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
/*     */   protected void checkGlobalTag(Property property, Node node, Object object) {
/* 197 */     if (object.getClass().isArray() && object.getClass().getComponentType().isPrimitive()) {
/*     */       return;
/*     */     }
/*     */     
/* 201 */     Class<?>[] arguments = property.getActualTypeArguments();
/* 202 */     if (arguments != null) {
/* 203 */       if (node.getNodeId() == NodeId.sequence) {
/*     */         
/* 205 */         Class<? extends Object> t = (Class)arguments[0];
/* 206 */         SequenceNode snode = (SequenceNode)node;
/* 207 */         Iterable<Object> memberList = Collections.EMPTY_LIST;
/* 208 */         if (object.getClass().isArray()) {
/* 209 */           memberList = Arrays.asList((Object[])object);
/* 210 */         } else if (object instanceof Iterable) {
/*     */           
/* 212 */           memberList = (Iterable<Object>)object;
/*     */         } 
/* 214 */         Iterator<Object> iter = memberList.iterator();
/* 215 */         if (iter.hasNext()) {
/* 216 */           for (Node childNode : snode.getValue()) {
/* 217 */             Object member = iter.next();
/* 218 */             if (member != null && 
/* 219 */               t.equals(member.getClass()) && 
/* 220 */               childNode.getNodeId() == NodeId.mapping) {
/* 221 */               childNode.setTag(Tag.MAP);
/*     */             }
/*     */           }
/*     */         
/*     */         }
/* 226 */       } else if (object instanceof Set) {
/* 227 */         Class<?> t = arguments[0];
/* 228 */         MappingNode mnode = (MappingNode)node;
/* 229 */         Iterator<NodeTuple> iter = mnode.getValue().iterator();
/* 230 */         Set<?> set = (Set)object;
/* 231 */         for (Object member : set) {
/* 232 */           NodeTuple tuple = iter.next();
/* 233 */           Node keyNode = tuple.getKeyNode();
/* 234 */           if (t.equals(member.getClass()) && 
/* 235 */             keyNode.getNodeId() == NodeId.mapping) {
/* 236 */             keyNode.setTag(Tag.MAP);
/*     */           }
/*     */         }
/*     */       
/* 240 */       } else if (object instanceof Map) {
/* 241 */         Class<?> keyType = arguments[0];
/* 242 */         Class<?> valueType = arguments[1];
/* 243 */         MappingNode mnode = (MappingNode)node;
/* 244 */         for (NodeTuple tuple : mnode.getValue()) {
/* 245 */           resetTag((Class)keyType, tuple.getKeyNode());
/* 246 */           resetTag((Class)valueType, tuple.getValueNode());
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void resetTag(Class<? extends Object> type, Node node) {
/* 256 */     Tag tag = node.getTag();
/* 257 */     if (tag.matches(type)) {
/* 258 */       if (Enum.class.isAssignableFrom(type)) {
/* 259 */         node.setTag(Tag.STR);
/*     */       } else {
/* 261 */         node.setTag(Tag.MAP);
/*     */       } 
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
/*     */   protected Set<Property> getProperties(Class<? extends Object> type) {
/* 275 */     if (this.typeDefinitions.containsKey(type)) {
/* 276 */       return ((TypeDescription)this.typeDefinitions.get(type)).getProperties();
/*     */     }
/* 278 */     return getPropertyUtils().getProperties(type);
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\representer\Representer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */