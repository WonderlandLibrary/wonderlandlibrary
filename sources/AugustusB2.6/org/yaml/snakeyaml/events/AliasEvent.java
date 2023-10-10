// 
// Decompiled by Procyon v0.5.36
// 

package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

public final class AliasEvent extends NodeEvent
{
    public AliasEvent(final String anchor, final Mark startMark, final Mark endMark) {
        super(anchor, startMark, endMark);
        if (anchor == null) {
            throw new NullPointerException("anchor is not specified for alias");
        }
    }
    
    @Override
    public ID getEventId() {
        return ID.Alias;
    }
}
