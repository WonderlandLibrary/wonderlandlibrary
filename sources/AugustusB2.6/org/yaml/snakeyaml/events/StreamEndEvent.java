// 
// Decompiled by Procyon v0.5.36
// 

package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

public final class StreamEndEvent extends Event
{
    public StreamEndEvent(final Mark startMark, final Mark endMark) {
        super(startMark, endMark);
    }
    
    @Override
    public ID getEventId() {
        return ID.StreamEnd;
    }
}
