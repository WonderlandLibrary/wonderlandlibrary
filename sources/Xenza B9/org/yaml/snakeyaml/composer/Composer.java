// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.composer;

import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.nodes.SequenceNode;
import java.util.ArrayList;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.NodeEvent;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.events.AliasEvent;
import org.yaml.snakeyaml.nodes.NodeTuple;
import java.util.List;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.nodes.Tag;
import java.util.Collections;
import org.yaml.snakeyaml.comments.CommentLine;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.comments.CommentType;
import java.util.HashSet;
import java.util.HashMap;
import org.yaml.snakeyaml.comments.CommentEventsCollector;
import org.yaml.snakeyaml.LoaderOptions;
import java.util.Set;
import org.yaml.snakeyaml.nodes.Node;
import java.util.Map;
import org.yaml.snakeyaml.resolver.Resolver;
import org.yaml.snakeyaml.parser.Parser;

public class Composer
{
    protected final Parser parser;
    private final Resolver resolver;
    private final Map<String, Node> anchors;
    private final Set<Node> recursiveNodes;
    private int nonScalarAliasesCount;
    private final LoaderOptions loadingConfig;
    private final CommentEventsCollector blockCommentsCollector;
    private final CommentEventsCollector inlineCommentsCollector;
    
    public Composer(final Parser parser, final Resolver resolver) {
        this(parser, resolver, new LoaderOptions());
    }
    
    public Composer(final Parser parser, final Resolver resolver, final LoaderOptions loadingConfig) {
        this.nonScalarAliasesCount = 0;
        this.parser = parser;
        this.resolver = resolver;
        this.anchors = new HashMap<String, Node>();
        this.recursiveNodes = new HashSet<Node>();
        this.loadingConfig = loadingConfig;
        this.blockCommentsCollector = new CommentEventsCollector(parser, new CommentType[] { CommentType.BLANK_LINE, CommentType.BLOCK });
        this.inlineCommentsCollector = new CommentEventsCollector(parser, new CommentType[] { CommentType.IN_LINE });
    }
    
    public boolean checkNode() {
        if (this.parser.checkEvent(Event.ID.StreamStart)) {
            this.parser.getEvent();
        }
        return !this.parser.checkEvent(Event.ID.StreamEnd);
    }
    
    public Node getNode() {
        this.blockCommentsCollector.collectEvents();
        if (this.parser.checkEvent(Event.ID.StreamEnd)) {
            final List<CommentLine> commentLines = this.blockCommentsCollector.consume();
            final Mark startMark = commentLines.get(0).getStartMark();
            final List<NodeTuple> children = Collections.emptyList();
            final Node node = new MappingNode(Tag.COMMENT, false, children, startMark, null, DumperOptions.FlowStyle.BLOCK);
            node.setBlockComments(commentLines);
            return node;
        }
        this.parser.getEvent();
        final Node node2 = this.composeNode(null);
        this.blockCommentsCollector.collectEvents();
        if (!this.blockCommentsCollector.isEmpty()) {
            node2.setEndComments(this.blockCommentsCollector.consume());
        }
        this.parser.getEvent();
        this.anchors.clear();
        this.recursiveNodes.clear();
        return node2;
    }
    
    public Node getSingleNode() {
        this.parser.getEvent();
        Node document = null;
        if (!this.parser.checkEvent(Event.ID.StreamEnd)) {
            document = this.getNode();
        }
        if (!this.parser.checkEvent(Event.ID.StreamEnd)) {
            final Event event = this.parser.getEvent();
            final Mark contextMark = (document != null) ? document.getStartMark() : null;
            throw new ComposerException("expected a single document in the stream", contextMark, "but found another document", event.getStartMark());
        }
        this.parser.getEvent();
        return document;
    }
    
    private Node composeNode(final Node parent) {
        this.blockCommentsCollector.collectEvents();
        if (parent != null) {
            this.recursiveNodes.add(parent);
        }
        Node node;
        if (this.parser.checkEvent(Event.ID.Alias)) {
            final AliasEvent event = (AliasEvent)this.parser.getEvent();
            final String anchor = event.getAnchor();
            if (!this.anchors.containsKey(anchor)) {
                throw new ComposerException(null, null, "found undefined alias " + anchor, event.getStartMark());
            }
            node = this.anchors.get(anchor);
            if (!(node instanceof ScalarNode)) {
                ++this.nonScalarAliasesCount;
                if (this.nonScalarAliasesCount > this.loadingConfig.getMaxAliasesForCollections()) {
                    throw new YAMLException("Number of aliases for non-scalar nodes exceeds the specified max=" + this.loadingConfig.getMaxAliasesForCollections());
                }
            }
            if (this.recursiveNodes.remove(node)) {
                node.setTwoStepsConstruction(true);
            }
            node.setBlockComments(this.blockCommentsCollector.consume());
        }
        else {
            final NodeEvent event2 = (NodeEvent)this.parser.peekEvent();
            final String anchor = event2.getAnchor();
            if (this.parser.checkEvent(Event.ID.Scalar)) {
                node = this.composeScalarNode(anchor, this.blockCommentsCollector.consume());
            }
            else if (this.parser.checkEvent(Event.ID.SequenceStart)) {
                node = this.composeSequenceNode(anchor);
            }
            else {
                node = this.composeMappingNode(anchor);
            }
        }
        this.recursiveNodes.remove(parent);
        return node;
    }
    
    protected Node composeScalarNode(final String anchor, final List<CommentLine> blockComments) {
        final ScalarEvent ev = (ScalarEvent)this.parser.getEvent();
        final String tag = ev.getTag();
        boolean resolved = false;
        Tag nodeTag;
        if (tag == null || tag.equals("!")) {
            nodeTag = this.resolver.resolve(NodeId.scalar, ev.getValue(), ev.getImplicit().canOmitTagInPlainScalar());
            resolved = true;
        }
        else {
            nodeTag = new Tag(tag);
        }
        final Node node = new ScalarNode(nodeTag, resolved, ev.getValue(), ev.getStartMark(), ev.getEndMark(), ev.getScalarStyle());
        if (anchor != null) {
            node.setAnchor(anchor);
            this.anchors.put(anchor, node);
        }
        node.setBlockComments(blockComments);
        node.setInLineComments(this.inlineCommentsCollector.collectEvents().consume());
        return node;
    }
    
    protected Node composeSequenceNode(final String anchor) {
        final SequenceStartEvent startEvent = (SequenceStartEvent)this.parser.getEvent();
        final String tag = startEvent.getTag();
        boolean resolved = false;
        Tag nodeTag;
        if (tag == null || tag.equals("!")) {
            nodeTag = this.resolver.resolve(NodeId.sequence, null, startEvent.getImplicit());
            resolved = true;
        }
        else {
            nodeTag = new Tag(tag);
        }
        final ArrayList<Node> children = new ArrayList<Node>();
        final SequenceNode node = new SequenceNode(nodeTag, resolved, children, startEvent.getStartMark(), null, startEvent.getFlowStyle());
        if (startEvent.isFlow()) {
            node.setBlockComments(this.blockCommentsCollector.consume());
        }
        if (anchor != null) {
            node.setAnchor(anchor);
            this.anchors.put(anchor, node);
        }
        while (!this.parser.checkEvent(Event.ID.SequenceEnd)) {
            this.blockCommentsCollector.collectEvents();
            if (this.parser.checkEvent(Event.ID.SequenceEnd)) {
                break;
            }
            children.add(this.composeNode(node));
        }
        if (startEvent.isFlow()) {
            node.setInLineComments(this.inlineCommentsCollector.collectEvents().consume());
        }
        final Event endEvent = this.parser.getEvent();
        node.setEndMark(endEvent.getEndMark());
        this.inlineCommentsCollector.collectEvents();
        if (!this.inlineCommentsCollector.isEmpty()) {
            node.setInLineComments(this.inlineCommentsCollector.consume());
        }
        return node;
    }
    
    protected Node composeMappingNode(final String anchor) {
        final MappingStartEvent startEvent = (MappingStartEvent)this.parser.getEvent();
        final String tag = startEvent.getTag();
        boolean resolved = false;
        Tag nodeTag;
        if (tag == null || tag.equals("!")) {
            nodeTag = this.resolver.resolve(NodeId.mapping, null, startEvent.getImplicit());
            resolved = true;
        }
        else {
            nodeTag = new Tag(tag);
        }
        final List<NodeTuple> children = new ArrayList<NodeTuple>();
        final MappingNode node = new MappingNode(nodeTag, resolved, children, startEvent.getStartMark(), null, startEvent.getFlowStyle());
        if (startEvent.isFlow()) {
            node.setBlockComments(this.blockCommentsCollector.consume());
        }
        if (anchor != null) {
            node.setAnchor(anchor);
            this.anchors.put(anchor, node);
        }
        while (!this.parser.checkEvent(Event.ID.MappingEnd)) {
            this.blockCommentsCollector.collectEvents();
            if (this.parser.checkEvent(Event.ID.MappingEnd)) {
                break;
            }
            this.composeMappingChildren(children, node);
        }
        if (startEvent.isFlow()) {
            node.setInLineComments(this.inlineCommentsCollector.collectEvents().consume());
        }
        final Event endEvent = this.parser.getEvent();
        node.setEndMark(endEvent.getEndMark());
        this.inlineCommentsCollector.collectEvents();
        if (!this.inlineCommentsCollector.isEmpty()) {
            node.setInLineComments(this.inlineCommentsCollector.consume());
        }
        return node;
    }
    
    protected void composeMappingChildren(final List<NodeTuple> children, final MappingNode node) {
        final Node itemKey = this.composeKeyNode(node);
        if (itemKey.getTag().equals(Tag.MERGE)) {
            node.setMerged(true);
        }
        final Node itemValue = this.composeValueNode(node);
        children.add(new NodeTuple(itemKey, itemValue));
    }
    
    protected Node composeKeyNode(final MappingNode node) {
        return this.composeNode(node);
    }
    
    protected Node composeValueNode(final MappingNode node) {
        return this.composeNode(node);
    }
}
