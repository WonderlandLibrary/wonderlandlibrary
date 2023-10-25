package org.yaml.snakeyaml.composer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.events.AliasEvent;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.NodeEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.parser.Parser;
import org.yaml.snakeyaml.resolver.Resolver;

public class Composer {
  protected final Parser parser;
  
  private final Resolver resolver;
  
  private final Map<String, Node> anchors;
  
  private final Set<Node> recursiveNodes;
  
  private int nonScalarAliasesCount = 0;
  
  private final LoaderOptions loadingConfig;
  
  public Composer(Parser parser, Resolver resolver) {
    this(parser, resolver, new LoaderOptions());
  }
  
  public Composer(Parser parser, Resolver resolver, LoaderOptions loadingConfig) {
    this.parser = parser;
    this.resolver = resolver;
    this.anchors = new HashMap<>();
    this.recursiveNodes = new HashSet<>();
    this.loadingConfig = loadingConfig;
  }
  
  public boolean checkNode() {
    if (this.parser.checkEvent(Event.ID.StreamStart))
      this.parser.getEvent(); 
    return !this.parser.checkEvent(Event.ID.StreamEnd);
  }
  
  public Node getNode() {
    this.parser.getEvent();
    Node node = composeNode(null);
    this.parser.getEvent();
    this.anchors.clear();
    this.recursiveNodes.clear();
    return node;
  }
  
  public Node getSingleNode() {
    this.parser.getEvent();
    Node document = null;
    if (!this.parser.checkEvent(Event.ID.StreamEnd))
      document = getNode(); 
    if (!this.parser.checkEvent(Event.ID.StreamEnd)) {
      Event event = this.parser.getEvent();
      Mark contextMark = (document != null) ? document.getStartMark() : null;
      throw new ComposerException("expected a single document in the stream", contextMark, "but found another document", event.getStartMark());
    } 
    this.parser.getEvent();
    return document;
  }
  
  private Node composeNode(Node parent) {
    Node node;
    if (parent != null)
      this.recursiveNodes.add(parent); 
    if (this.parser.checkEvent(Event.ID.Alias)) {
      AliasEvent event = (AliasEvent)this.parser.getEvent();
      String anchor = event.getAnchor();
      if (!this.anchors.containsKey(anchor))
        throw new ComposerException(null, null, "found undefined alias " + anchor, event.getStartMark()); 
      node = this.anchors.get(anchor);
      if (!(node instanceof ScalarNode)) {
        this.nonScalarAliasesCount++;
        if (this.nonScalarAliasesCount > this.loadingConfig.getMaxAliasesForCollections())
          throw new YAMLException("Number of aliases for non-scalar nodes exceeds the specified max=" + this.loadingConfig.getMaxAliasesForCollections()); 
      } 
      if (this.recursiveNodes.remove(node))
        node.setTwoStepsConstruction(true); 
    } else {
      NodeEvent event = (NodeEvent)this.parser.peekEvent();
      String anchor = event.getAnchor();
      if (this.parser.checkEvent(Event.ID.Scalar)) {
        node = composeScalarNode(anchor);
      } else if (this.parser.checkEvent(Event.ID.SequenceStart)) {
        node = composeSequenceNode(anchor);
      } else {
        node = composeMappingNode(anchor);
      } 
    } 
    this.recursiveNodes.remove(parent);
    return node;
  }
  
  protected Node composeScalarNode(String anchor) {
    Tag nodeTag;
    ScalarEvent ev = (ScalarEvent)this.parser.getEvent();
    String tag = ev.getTag();
    boolean resolved = false;
    if (tag == null || tag.equals("!")) {
      nodeTag = this.resolver.resolve(NodeId.scalar, ev.getValue(), ev.getImplicit().canOmitTagInPlainScalar());
      resolved = true;
    } else {
      nodeTag = new Tag(tag);
    } 
    ScalarNode scalarNode = new ScalarNode(nodeTag, resolved, ev.getValue(), ev.getStartMark(), ev.getEndMark(), ev.getScalarStyle());
    if (anchor != null) {
      scalarNode.setAnchor(anchor);
      this.anchors.put(anchor, scalarNode);
    } 
    return (Node)scalarNode;
  }
  
  protected Node composeSequenceNode(String anchor) {
    Tag nodeTag;
    SequenceStartEvent startEvent = (SequenceStartEvent)this.parser.getEvent();
    String tag = startEvent.getTag();
    boolean resolved = false;
    if (tag == null || tag.equals("!")) {
      nodeTag = this.resolver.resolve(NodeId.sequence, null, startEvent.getImplicit());
      resolved = true;
    } else {
      nodeTag = new Tag(tag);
    } 
    ArrayList<Node> children = new ArrayList<>();
    SequenceNode node = new SequenceNode(nodeTag, resolved, children, startEvent.getStartMark(), null, startEvent.getFlowStyle());
    if (anchor != null) {
      node.setAnchor(anchor);
      this.anchors.put(anchor, node);
    } 
    while (!this.parser.checkEvent(Event.ID.SequenceEnd))
      children.add(composeNode((Node)node)); 
    Event endEvent = this.parser.getEvent();
    node.setEndMark(endEvent.getEndMark());
    return (Node)node;
  }
  
  protected Node composeMappingNode(String anchor) {
    Tag nodeTag;
    MappingStartEvent startEvent = (MappingStartEvent)this.parser.getEvent();
    String tag = startEvent.getTag();
    boolean resolved = false;
    if (tag == null || tag.equals("!")) {
      nodeTag = this.resolver.resolve(NodeId.mapping, null, startEvent.getImplicit());
      resolved = true;
    } else {
      nodeTag = new Tag(tag);
    } 
    List<NodeTuple> children = new ArrayList<>();
    MappingNode node = new MappingNode(nodeTag, resolved, children, startEvent.getStartMark(), null, startEvent.getFlowStyle());
    if (anchor != null) {
      node.setAnchor(anchor);
      this.anchors.put(anchor, node);
    } 
    while (!this.parser.checkEvent(Event.ID.MappingEnd))
      composeMappingChildren(children, node); 
    Event endEvent = this.parser.getEvent();
    node.setEndMark(endEvent.getEndMark());
    return (Node)node;
  }
  
  protected void composeMappingChildren(List<NodeTuple> children, MappingNode node) {
    Node itemKey = composeKeyNode(node);
    if (itemKey.getTag().equals(Tag.MERGE))
      node.setMerged(true); 
    Node itemValue = composeValueNode(node);
    children.add(new NodeTuple(itemKey, itemValue));
  }
  
  protected Node composeKeyNode(MappingNode node) {
    return composeNode((Node)node);
  }
  
  protected Node composeValueNode(MappingNode node) {
    return composeNode((Node)node);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\composer\Composer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */