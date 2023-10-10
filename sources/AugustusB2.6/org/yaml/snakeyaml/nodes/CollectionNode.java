// 
// Decompiled by Procyon v0.5.36
// 

package org.yaml.snakeyaml.nodes;

import java.util.List;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.DumperOptions;

public abstract class CollectionNode<T> extends Node
{
    private DumperOptions.FlowStyle flowStyle;
    
    public CollectionNode(final Tag tag, final Mark startMark, final Mark endMark, final DumperOptions.FlowStyle flowStyle) {
        super(tag, startMark, endMark);
        this.setFlowStyle(flowStyle);
    }
    
    @Deprecated
    public CollectionNode(final Tag tag, final Mark startMark, final Mark endMark, final Boolean flowStyle) {
        this(tag, startMark, endMark, DumperOptions.FlowStyle.fromBoolean(flowStyle));
    }
    
    public abstract List<T> getValue();
    
    public DumperOptions.FlowStyle getFlowStyle() {
        return this.flowStyle;
    }
    
    public void setFlowStyle(final DumperOptions.FlowStyle flowStyle) {
        if (flowStyle == null) {
            throw new NullPointerException("Flow style must be provided.");
        }
        this.flowStyle = flowStyle;
    }
    
    @Deprecated
    public void setFlowStyle(final Boolean flowStyle) {
        this.setFlowStyle(DumperOptions.FlowStyle.fromBoolean(flowStyle));
    }
    
    public void setEndMark(final Mark endMark) {
        this.endMark = endMark;
    }
}
