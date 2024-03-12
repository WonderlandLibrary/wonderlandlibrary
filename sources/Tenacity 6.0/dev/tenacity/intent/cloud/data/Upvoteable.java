// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.intent.cloud.data;

public class Upvoteable
{
    private boolean upvoted;
    private boolean downvoted;
    
    public void upvote() {
        if (this.upvoted) {
            this.upvoted = false;
        }
        else {
            this.upvoted = true;
            this.downvoted = false;
        }
    }
    
    public void downvote() {
        if (this.downvoted) {
            this.downvoted = false;
        }
        else {
            this.downvoted = true;
            this.upvoted = false;
        }
    }
    
    public void unvote() {
        this.upvoted = false;
        this.downvoted = false;
    }
    
    public void forceSet(final boolean upvoted) {
        if (upvoted) {
            this.upvoted = true;
            this.downvoted = false;
        }
        else {
            this.upvoted = false;
            this.downvoted = true;
        }
    }
    
    public boolean isUpvoted() {
        return this.upvoted;
    }
    
    public boolean isDownvoted() {
        return this.downvoted;
    }
}
