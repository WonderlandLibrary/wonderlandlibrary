// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.comments;

import org.yaml.snakeyaml.events.CommentEvent;
import org.yaml.snakeyaml.error.Mark;

public class CommentLine
{
    private Mark startMark;
    private Mark endMark;
    private String value;
    private CommentType commentType;
    
    public CommentLine(final CommentEvent event) {
        this(event.getStartMark(), event.getEndMark(), event.getValue(), event.getCommentType());
    }
    
    public CommentLine(final Mark startMark, final Mark endMark, final String value, final CommentType commentType) {
        this.startMark = startMark;
        this.endMark = endMark;
        this.value = value;
        this.commentType = commentType;
    }
    
    public Mark getEndMark() {
        return this.endMark;
    }
    
    public Mark getStartMark() {
        return this.startMark;
    }
    
    public CommentType getCommentType() {
        return this.commentType;
    }
    
    public String getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
        return "<" + this.getClass().getName() + " (type=" + this.getCommentType() + ", value=" + this.getValue() + ")>";
    }
}
