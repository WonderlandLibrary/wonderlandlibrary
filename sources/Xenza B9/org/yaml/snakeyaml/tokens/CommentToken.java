// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.tokens;

import java.util.Objects;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.comments.CommentType;

public final class CommentToken extends Token
{
    private final CommentType type;
    private final String value;
    
    public CommentToken(final CommentType type, final String value, final Mark startMark, final Mark endMark) {
        super(startMark, endMark);
        Objects.requireNonNull(type);
        this.type = type;
        Objects.requireNonNull(value);
        this.value = value;
    }
    
    public CommentType getCommentType() {
        return this.type;
    }
    
    public String getValue() {
        return this.value;
    }
    
    @Override
    public ID getTokenId() {
        return ID.Comment;
    }
}
