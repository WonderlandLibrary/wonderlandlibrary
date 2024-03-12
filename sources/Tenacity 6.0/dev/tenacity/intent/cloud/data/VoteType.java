// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.intent.cloud.data;

public enum VoteType
{
    UP("upvote"), 
    DOWN("downvote"), 
    UNVOTE("unvote");
    
    private final String actionName;
    
    public String getActionName() {
        return this.actionName;
    }
    
    private VoteType(final String actionName) {
        this.actionName = actionName;
    }
}
