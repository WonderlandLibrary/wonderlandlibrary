package org.yaml.snakeyaml.serializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.emitter.Emitable;
import org.yaml.snakeyaml.events.AliasEvent;
import org.yaml.snakeyaml.events.DocumentEndEvent;
import org.yaml.snakeyaml.events.DocumentStartEvent;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.ImplicitTuple;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.events.StreamEndEvent;
import org.yaml.snakeyaml.events.StreamStartEvent;
import org.yaml.snakeyaml.nodes.AnchorNode;
import org.yaml.snakeyaml.nodes.CollectionNode;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.resolver.Resolver;

public final class Serializer {
  private final Emitable emitter;
  
  private final Resolver resolver;
  
  private boolean explicitStart;
  
  private boolean explicitEnd;
  
  private DumperOptions.Version useVersion;
  
  private Map<String, String> useTags;
  
  private Set<Node> serializedNodes;
  
  private Map<Node, String> anchors;
  
  private AnchorGenerator anchorGenerator;
  
  private Boolean closed;
  
  private Tag explicitRoot;
  
  public Serializer(Emitable emitter, Resolver resolver, DumperOptions opts, Tag rootTag) {
    this.emitter = emitter;
    this.resolver = resolver;
    this.explicitStart = opts.isExplicitStart();
    this.explicitEnd = opts.isExplicitEnd();
    if (opts.getVersion() != null)
      this.useVersion = opts.getVersion(); 
    this.useTags = opts.getTags();
    this.serializedNodes = new HashSet<>();
    this.anchors = new HashMap<>();
    this.anchorGenerator = opts.getAnchorGenerator();
    this.closed = null;
    this.explicitRoot = rootTag;
  }
  
  public void open() throws IOException {
    if (this.closed == null) {
      this.emitter.emit((Event)new StreamStartEvent(null, null));
      this.closed = Boolean.FALSE;
    } else {
      if (Boolean.TRUE.equals(this.closed))
        throw new SerializerException("serializer is closed"); 
      throw new SerializerException("serializer is already opened");
    } 
  }
  
  public void close() throws IOException {
    if (this.closed == null)
      throw new SerializerException("serializer is not opened"); 
    if (!Boolean.TRUE.equals(this.closed)) {
      this.emitter.emit((Event)new StreamEndEvent(null, null));
      this.closed = Boolean.TRUE;
      this.serializedNodes.clear();
      this.anchors.clear();
    } 
  }
  
  public void serialize(Node node) throws IOException {
    if (this.closed == null)
      throw new SerializerException("serializer is not opened"); 
    if (this.closed.booleanValue())
      throw new SerializerException("serializer is closed"); 
    this.emitter.emit((Event)new DocumentStartEvent(null, null, this.explicitStart, this.useVersion, this.useTags));
    anchorNode(node);
    if (this.explicitRoot != null)
      node.setTag(this.explicitRoot); 
    serializeNode(node, null);
    this.emitter.emit((Event)new DocumentEndEvent(null, null, this.explicitEnd));
    this.serializedNodes.clear();
    this.anchors.clear();
  }
  
  private void anchorNode(Node node) {
    if (node.getNodeId() == NodeId.anchor)
      node = ((AnchorNode)node).getRealNode(); 
    if (this.anchors.containsKey(node)) {
      String anchor = this.anchors.get(node);
      if (null == anchor) {
        anchor = this.anchorGenerator.nextAnchor(node);
        this.anchors.put(node, anchor);
      } 
    } else {
      SequenceNode seqNode;
      List<Node> list;
      MappingNode mnode;
      List<NodeTuple> map;
      this.anchors.put(node, (node.getAnchor() != null) ? this.anchorGenerator.nextAnchor(node) : null);
      switch (node.getNodeId()) {
        case sequence:
          seqNode = (SequenceNode)node;
          list = seqNode.getValue();
          for (Node item : list)
            anchorNode(item); 
          break;
        case mapping:
          mnode = (MappingNode)node;
          map = mnode.getValue();
          for (NodeTuple object : map) {
            Node key = object.getKeyNode();
            Node value = object.getValueNode();
            anchorNode(key);
            anchorNode(value);
          } 
          break;
      } 
    } 
  }
  
  private void serializeNode(Node node, Node parent) throws IOException {
    if (node.getNodeId() == NodeId.anchor)
      node = ((AnchorNode)node).getRealNode(); 
    String tAlias = this.anchors.get(node);
    if (this.serializedNodes.contains(node)) {
      this.emitter.emit((Event)new AliasEvent(tAlias, null, null));
    } else {
      ScalarNode scalarNode;
      Tag detectedTag, defaultTag;
      ImplicitTuple tuple;
      ScalarEvent event;
      SequenceNode seqNode;
      boolean implicitS;
      List<Node> list;
      this.serializedNodes.add(node);
      switch (node.getNodeId()) {
        case scalar:
          scalarNode = (ScalarNode)node;
          detectedTag = this.resolver.resolve(NodeId.scalar, scalarNode.getValue(), true);
          defaultTag = this.resolver.resolve(NodeId.scalar, scalarNode.getValue(), false);
          tuple = new ImplicitTuple(node.getTag().equals(detectedTag), node.getTag().equals(defaultTag));
          event = new ScalarEvent(tAlias, node.getTag().getValue(), tuple, scalarNode.getValue(), null, null, scalarNode.getScalarStyle());
          this.emitter.emit((Event)event);
          return;
        case sequence:
          seqNode = (SequenceNode)node;
          implicitS = node.getTag().equals(this.resolver.resolve(NodeId.sequence, null, true));
          this.emitter.emit((Event)new SequenceStartEvent(tAlias, node.getTag().getValue(), implicitS, null, null, seqNode.getFlowStyle()));
          list = seqNode.getValue();
          for (Node item : list)
            serializeNode(item, node); 
          this.emitter.emit((Event)new SequenceEndEvent(null, null));
          return;
      } 
      Tag implicitTag = this.resolver.resolve(NodeId.mapping, null, true);
      boolean implicitM = node.getTag().equals(implicitTag);
      this.emitter.emit((Event)new MappingStartEvent(tAlias, node.getTag().getValue(), implicitM, null, null, ((CollectionNode)node).getFlowStyle()));
      MappingNode mnode = (MappingNode)node;
      List<NodeTuple> map = mnode.getValue();
      for (NodeTuple row : map) {
        Node key = row.getKeyNode();
        Node value = row.getValueNode();
        serializeNode(key, (Node)mnode);
        serializeNode(value, (Node)mnode);
      } 
      this.emitter.emit((Event)new MappingEndEvent(null, null));
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\serializer\Serializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */