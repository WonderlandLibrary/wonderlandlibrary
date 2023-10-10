// 
// Decompiled by Procyon v0.5.36
// 

package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.error.Mark;

public final class SequenceStartEvent extends CollectionStartEvent
{
    public SequenceStartEvent(final String anchor, final String tag, final boolean implicit, final Mark startMark, final Mark endMark, final DumperOptions.FlowStyle flowStyle) {
        super(anchor, tag, implicit, startMark, endMark, flowStyle);
    }
    
    @Deprecated
    public SequenceStartEvent(final String anchor, final String tag, final boolean implicit, final Mark startMark, final Mark endMark, final Boolean flowStyle) {
        this(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.fromBoolean(flowStyle));
    }
    
    @Override
    public ID getEventId() {
        return ID.SequenceStart;
    }
}
