/*     */ package org.yaml.snakeyaml.serializer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.emitter.Emitable;
/*     */ import org.yaml.snakeyaml.events.AliasEvent;
/*     */ import org.yaml.snakeyaml.events.DocumentEndEvent;
/*     */ import org.yaml.snakeyaml.events.DocumentStartEvent;
/*     */ import org.yaml.snakeyaml.events.Event;
/*     */ import org.yaml.snakeyaml.events.ImplicitTuple;
/*     */ import org.yaml.snakeyaml.events.MappingEndEvent;
/*     */ import org.yaml.snakeyaml.events.MappingStartEvent;
/*     */ import org.yaml.snakeyaml.events.ScalarEvent;
/*     */ import org.yaml.snakeyaml.events.SequenceEndEvent;
/*     */ import org.yaml.snakeyaml.events.SequenceStartEvent;
/*     */ import org.yaml.snakeyaml.events.StreamEndEvent;
/*     */ import org.yaml.snakeyaml.events.StreamStartEvent;
/*     */ import org.yaml.snakeyaml.nodes.AnchorNode;
/*     */ import org.yaml.snakeyaml.nodes.CollectionNode;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
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
/*     */ public final class Serializer
/*     */ {
/*     */   private final Emitable emitter;
/*     */   private final Resolver resolver;
/*     */   private boolean explicitStart;
/*     */   private boolean explicitEnd;
/*     */   private DumperOptions.Version useVersion;
/*     */   private Map<String, String> useTags;
/*     */   private Set<Node> serializedNodes;
/*     */   private Map<Node, String> anchors;
/*     */   private AnchorGenerator anchorGenerator;
/*     */   private Boolean closed;
/*     */   private Tag explicitRoot;
/*     */   
/*     */   public Serializer(Emitable emitter, Resolver resolver, DumperOptions opts, Tag rootTag) {
/*  64 */     this.emitter = emitter;
/*  65 */     this.resolver = resolver;
/*  66 */     this.explicitStart = opts.isExplicitStart();
/*  67 */     this.explicitEnd = opts.isExplicitEnd();
/*  68 */     if (opts.getVersion() != null) {
/*  69 */       this.useVersion = opts.getVersion();
/*     */     }
/*  71 */     this.useTags = opts.getTags();
/*  72 */     this.serializedNodes = new HashSet<>();
/*  73 */     this.anchors = new HashMap<>();
/*  74 */     this.anchorGenerator = opts.getAnchorGenerator();
/*  75 */     this.closed = null;
/*  76 */     this.explicitRoot = rootTag;
/*     */   }
/*     */   
/*     */   public void open() throws IOException {
/*  80 */     if (this.closed == null)
/*  81 */     { this.emitter.emit((Event)new StreamStartEvent(null, null));
/*  82 */       this.closed = Boolean.FALSE; }
/*  83 */     else { if (Boolean.TRUE.equals(this.closed)) {
/*  84 */         throw new SerializerException("serializer is closed");
/*     */       }
/*  86 */       throw new SerializerException("serializer is already opened"); }
/*     */   
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/*  91 */     if (this.closed == null)
/*  92 */       throw new SerializerException("serializer is not opened"); 
/*  93 */     if (!Boolean.TRUE.equals(this.closed)) {
/*  94 */       this.emitter.emit((Event)new StreamEndEvent(null, null));
/*  95 */       this.closed = Boolean.TRUE;
/*     */       
/*  97 */       this.serializedNodes.clear();
/*  98 */       this.anchors.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void serialize(Node node) throws IOException {
/* 103 */     if (this.closed == null)
/* 104 */       throw new SerializerException("serializer is not opened"); 
/* 105 */     if (this.closed.booleanValue()) {
/* 106 */       throw new SerializerException("serializer is closed");
/*     */     }
/* 108 */     this.emitter.emit((Event)new DocumentStartEvent(null, null, this.explicitStart, this.useVersion, this.useTags));
/*     */     
/* 110 */     anchorNode(node);
/* 111 */     if (this.explicitRoot != null) {
/* 112 */       node.setTag(this.explicitRoot);
/*     */     }
/* 114 */     serializeNode(node, null);
/* 115 */     this.emitter.emit((Event)new DocumentEndEvent(null, null, this.explicitEnd));
/* 116 */     this.serializedNodes.clear();
/* 117 */     this.anchors.clear();
/*     */   }
/*     */   
/*     */   private void anchorNode(Node node) {
/* 121 */     if (node.getNodeId() == NodeId.anchor) {
/* 122 */       node = ((AnchorNode)node).getRealNode();
/*     */     }
/* 124 */     if (this.anchors.containsKey(node)) {
/* 125 */       String anchor = this.anchors.get(node);
/* 126 */       if (null == anchor) {
/* 127 */         anchor = this.anchorGenerator.nextAnchor(node);
/* 128 */         this.anchors.put(node, anchor);
/*     */       } 
/*     */     } else {
/* 131 */       SequenceNode seqNode; List<Node> list; MappingNode mnode; List<NodeTuple> map; this.anchors.put(node, (node.getAnchor() != null) ? this.anchorGenerator.nextAnchor(node) : null);
/* 132 */       switch (node.getNodeId()) {
/*     */         case sequence:
/* 134 */           seqNode = (SequenceNode)node;
/* 135 */           list = seqNode.getValue();
/* 136 */           for (Node item : list) {
/* 137 */             anchorNode(item);
/*     */           }
/*     */           break;
/*     */         case mapping:
/* 141 */           mnode = (MappingNode)node;
/* 142 */           map = mnode.getValue();
/* 143 */           for (NodeTuple object : map) {
/* 144 */             Node key = object.getKeyNode();
/* 145 */             Node value = object.getValueNode();
/* 146 */             anchorNode(key);
/* 147 */             anchorNode(value);
/*     */           } 
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void serializeNode(Node node, Node parent) throws IOException {
/* 156 */     if (node.getNodeId() == NodeId.anchor) {
/* 157 */       node = ((AnchorNode)node).getRealNode();
/*     */     }
/* 159 */     String tAlias = this.anchors.get(node);
/* 160 */     if (this.serializedNodes.contains(node)) {
/* 161 */       this.emitter.emit((Event)new AliasEvent(tAlias, null, null));
/*     */     } else {
/* 163 */       ScalarNode scalarNode; Tag detectedTag, defaultTag; ImplicitTuple tuple; ScalarEvent event; SequenceNode seqNode; boolean implicitS; List<Node> list; this.serializedNodes.add(node);
/* 164 */       switch (node.getNodeId()) {
/*     */         case scalar:
/* 166 */           scalarNode = (ScalarNode)node;
/* 167 */           detectedTag = this.resolver.resolve(NodeId.scalar, scalarNode.getValue(), true);
/* 168 */           defaultTag = this.resolver.resolve(NodeId.scalar, scalarNode.getValue(), false);
/* 169 */           tuple = new ImplicitTuple(node.getTag().equals(detectedTag), node.getTag().equals(defaultTag));
/*     */           
/* 171 */           event = new ScalarEvent(tAlias, node.getTag().getValue(), tuple, scalarNode.getValue(), null, null, scalarNode.getScalarStyle());
/*     */           
/* 173 */           this.emitter.emit((Event)event);
/*     */           return;
/*     */         case sequence:
/* 176 */           seqNode = (SequenceNode)node;
/* 177 */           implicitS = node.getTag().equals(this.resolver.resolve(NodeId.sequence, null, true));
/*     */           
/* 179 */           this.emitter.emit((Event)new SequenceStartEvent(tAlias, node.getTag().getValue(), implicitS, null, null, seqNode.getFlowStyle()));
/*     */           
/* 181 */           list = seqNode.getValue();
/* 182 */           for (Node item : list) {
/* 183 */             serializeNode(item, node);
/*     */           }
/* 185 */           this.emitter.emit((Event)new SequenceEndEvent(null, null));
/*     */           return;
/*     */       } 
/* 188 */       Tag implicitTag = this.resolver.resolve(NodeId.mapping, null, true);
/* 189 */       boolean implicitM = node.getTag().equals(implicitTag);
/* 190 */       this.emitter.emit((Event)new MappingStartEvent(tAlias, node.getTag().getValue(), implicitM, null, null, ((CollectionNode)node).getFlowStyle()));
/*     */       
/* 192 */       MappingNode mnode = (MappingNode)node;
/* 193 */       List<NodeTuple> map = mnode.getValue();
/* 194 */       for (NodeTuple row : map) {
/* 195 */         Node key = row.getKeyNode();
/* 196 */         Node value = row.getValueNode();
/* 197 */         serializeNode(key, (Node)mnode);
/* 198 */         serializeNode(value, (Node)mnode);
/*     */       } 
/* 200 */       this.emitter.emit((Event)new MappingEndEvent(null, null));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\serializer\Serializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */