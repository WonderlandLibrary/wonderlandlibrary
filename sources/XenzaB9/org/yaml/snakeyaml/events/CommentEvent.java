// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.comments.CommentType;

public final class CommentEvent extends Event
{
    private final CommentType type;
    private final String value;
    
    public CommentEvent(final CommentType type, final String value, final Mark startMark, final Mark endMark) {
        super(startMark, endMark);
        if (type == null) {
            throw new NullPointerException("Event Type must be provided.");
        }
        this.type = type;
        if (value == null) {
            throw new NullPointerException("Value must be provided.");
        }
        this.value = value;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public CommentType getCommentType() {
        return this.type;
    }
    
    @Override
    protected String getArguments() {
        return super.getArguments() + "type=" + this.type + ", value=" + this.value;
    }
    
    @Override
    public ID getEventId() {
        return ID.Comment;
    }
}
