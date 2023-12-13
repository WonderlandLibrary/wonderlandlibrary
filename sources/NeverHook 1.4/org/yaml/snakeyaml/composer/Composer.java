/*     */ package org.yaml.snakeyaml.composer;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.yaml.snakeyaml.LoaderOptions;
/*     */ import org.yaml.snakeyaml.error.Mark;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.events.AliasEvent;
/*     */ import org.yaml.snakeyaml.events.Event;
/*     */ import org.yaml.snakeyaml.events.MappingStartEvent;
/*     */ import org.yaml.snakeyaml.events.NodeEvent;
/*     */ import org.yaml.snakeyaml.events.ScalarEvent;
/*     */ import org.yaml.snakeyaml.events.SequenceStartEvent;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ import org.yaml.snakeyaml.parser.Parser;
/*     */ import org.yaml.snakeyaml.resolver.Resolver;
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
/*     */ public class Composer
/*     */ {
/*     */   protected final Parser parser;
/*     */   private final Resolver resolver;
/*     */   private final Map<String, Node> anchors;
/*     */   private final Set<Node> recursiveNodes;
/*  56 */   private int nonScalarAliasesCount = 0;
/*     */   private final LoaderOptions loadingConfig;
/*     */   
/*     */   public Composer(Parser parser, Resolver resolver) {
/*  60 */     this(parser, resolver, new LoaderOptions());
/*     */   }
/*     */   
/*     */   public Composer(Parser parser, Resolver resolver, LoaderOptions loadingConfig) {
/*  64 */     this.parser = parser;
/*  65 */     this.resolver = resolver;
/*  66 */     this.anchors = new HashMap<>();
/*  67 */     this.recursiveNodes = new HashSet<>();
/*  68 */     this.loadingConfig = loadingConfig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkNode() {
/*  78 */     if (this.parser.checkEvent(Event.ID.StreamStart)) {
/*  79 */       this.parser.getEvent();
/*     */     }
/*     */     
/*  82 */     return !this.parser.checkEvent(Event.ID.StreamEnd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getNode() {
/*  93 */     this.parser.getEvent();
/*     */     
/*  95 */     Node node = composeNode(null);
/*     */     
/*  97 */     this.parser.getEvent();
/*     */     
/*  99 */     this.anchors.clear();
/* 100 */     this.recursiveNodes.clear();
/* 101 */     return node;
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
/*     */   public Node getSingleNode() {
/* 115 */     this.parser.getEvent();
/*     */     
/* 117 */     Node document = null;
/* 118 */     if (!this.parser.checkEvent(Event.ID.StreamEnd)) {
/* 119 */       document = getNode();
/*     */     }
/*     */     
/* 122 */     if (!this.parser.checkEvent(Event.ID.StreamEnd)) {
/* 123 */       Event event = this.parser.getEvent();
/* 124 */       Mark contextMark = (document != null) ? document.getStartMark() : null;
/* 125 */       throw new ComposerException("expected a single document in the stream", contextMark, "but found another document", event.getStartMark());
/*     */     } 
/*     */ 
/*     */     
/* 129 */     this.parser.getEvent();
/* 130 */     return document;
/*     */   }
/*     */   private Node composeNode(Node parent) {
/*     */     Node node;
/* 134 */     if (parent != null) this.recursiveNodes.add(parent);
/*     */     
/* 136 */     if (this.parser.checkEvent(Event.ID.Alias)) {
/* 137 */       AliasEvent event = (AliasEvent)this.parser.getEvent();
/* 138 */       String anchor = event.getAnchor();
/* 139 */       if (!this.anchors.containsKey(anchor)) {
/* 140 */         throw new ComposerException(null, null, "found undefined alias " + anchor, event.getStartMark());
/*     */       }
/*     */       
/* 143 */       node = this.anchors.get(anchor);
/* 144 */       if (!(node instanceof ScalarNode)) {
/* 145 */         this.nonScalarAliasesCount++;
/* 146 */         if (this.nonScalarAliasesCount > this.loadingConfig.getMaxAliasesForCollections()) {
/* 147 */           throw new YAMLException("Number of aliases for non-scalar nodes exceeds the specified max=" + this.loadingConfig.getMaxAliasesForCollections());
/*     */         }
/*     */       } 
/* 150 */       if (this.recursiveNodes.remove(node)) {
/* 151 */         node.setTwoStepsConstruction(true);
/*     */       }
/*     */     } else {
/* 154 */       NodeEvent event = (NodeEvent)this.parser.peekEvent();
/* 155 */       String anchor = event.getAnchor();
/*     */       
/* 157 */       if (this.parser.checkEvent(Event.ID.Scalar)) {
/* 158 */         node = composeScalarNode(anchor);
/* 159 */       } else if (this.parser.checkEvent(Event.ID.SequenceStart)) {
/* 160 */         node = composeSequenceNode(anchor);
/*     */       } else {
/* 162 */         node = composeMappingNode(anchor);
/*     */       } 
/*     */     } 
/* 165 */     this.recursiveNodes.remove(parent);
/* 166 */     return node;
/*     */   }
/*     */   protected Node composeScalarNode(String anchor) {
/*     */     Tag nodeTag;
/* 170 */     ScalarEvent ev = (ScalarEvent)this.parser.getEvent();
/* 171 */     String tag = ev.getTag();
/* 172 */     boolean resolved = false;
/*     */     
/* 174 */     if (tag == null || tag.equals("!")) {
/* 175 */       nodeTag = this.resolver.resolve(NodeId.scalar, ev.getValue(), ev.getImplicit().canOmitTagInPlainScalar());
/*     */       
/* 177 */       resolved = true;
/*     */     } else {
/* 179 */       nodeTag = new Tag(tag);
/*     */     } 
/* 181 */     ScalarNode scalarNode = new ScalarNode(nodeTag, resolved, ev.getValue(), ev.getStartMark(), ev.getEndMark(), ev.getScalarStyle());
/*     */     
/* 183 */     if (anchor != null) {
/* 184 */       scalarNode.setAnchor(anchor);
/* 185 */       this.anchors.put(anchor, scalarNode);
/*     */     } 
/* 187 */     return (Node)scalarNode;
/*     */   }
/*     */   protected Node composeSequenceNode(String anchor) {
/*     */     Tag nodeTag;
/* 191 */     SequenceStartEvent startEvent = (SequenceStartEvent)this.parser.getEvent();
/* 192 */     String tag = startEvent.getTag();
/*     */     
/* 194 */     boolean resolved = false;
/* 195 */     if (tag == null || tag.equals("!")) {
/* 196 */       nodeTag = this.resolver.resolve(NodeId.sequence, null, startEvent.getImplicit());
/* 197 */       resolved = true;
/*     */     } else {
/* 199 */       nodeTag = new Tag(tag);
/*     */     } 
/* 201 */     ArrayList<Node> children = new ArrayList<>();
/* 202 */     SequenceNode node = new SequenceNode(nodeTag, resolved, children, startEvent.getStartMark(), null, startEvent.getFlowStyle());
/*     */     
/* 204 */     if (anchor != null) {
/* 205 */       node.setAnchor(anchor);
/* 206 */       this.anchors.put(anchor, node);
/*     */     } 
/* 208 */     while (!this.parser.checkEvent(Event.ID.SequenceEnd)) {
/* 209 */       children.add(composeNode((Node)node));
/*     */     }
/* 211 */     Event endEvent = this.parser.getEvent();
/* 212 */     node.setEndMark(endEvent.getEndMark());
/* 213 */     return (Node)node;
/*     */   }
/*     */   protected Node composeMappingNode(String anchor) {
/*     */     Tag nodeTag;
/* 217 */     MappingStartEvent startEvent = (MappingStartEvent)this.parser.getEvent();
/* 218 */     String tag = startEvent.getTag();
/*     */     
/* 220 */     boolean resolved = false;
/* 221 */     if (tag == null || tag.equals("!")) {
/* 222 */       nodeTag = this.resolver.resolve(NodeId.mapping, null, startEvent.getImplicit());
/* 223 */       resolved = true;
/*     */     } else {
/* 225 */       nodeTag = new Tag(tag);
/*     */     } 
/*     */     
/* 228 */     List<NodeTuple> children = new ArrayList<>();
/* 229 */     MappingNode node = new MappingNode(nodeTag, resolved, children, startEvent.getStartMark(), null, startEvent.getFlowStyle());
/*     */     
/* 231 */     if (anchor != null) {
/* 232 */       node.setAnchor(anchor);
/* 233 */       this.anchors.put(anchor, node);
/*     */     } 
/* 235 */     while (!this.parser.checkEvent(Event.ID.MappingEnd)) {
/* 236 */       composeMappingChildren(children, node);
/*     */     }
/* 238 */     Event endEvent = this.parser.getEvent();
/* 239 */     node.setEndMark(endEvent.getEndMark());
/* 240 */     return (Node)node;
/*     */   }
/*     */   
/*     */   protected void composeMappingChildren(List<NodeTuple> children, MappingNode node) {
/* 244 */     Node itemKey = composeKeyNode(node);
/* 245 */     if (itemKey.getTag().equals(Tag.MERGE)) {
/* 246 */       node.setMerged(true);
/*     */     }
/* 248 */     Node itemValue = composeValueNode(node);
/* 249 */     children.add(new NodeTuple(itemKey, itemValue));
/*     */   }
/*     */   
/*     */   protected Node composeKeyNode(MappingNode node) {
/* 253 */     return composeNode((Node)node);
/*     */   }
/*     */   
/*     */   protected Node composeValueNode(MappingNode node) {
/* 257 */     return composeNode((Node)node);
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\composer\Composer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */