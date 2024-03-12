// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.intent.cloud.data;

public class Votes
{
    private int upvotes;
    private int downvotes;
    
    public int getTotalVotes() {
        return this.upvotes - this.downvotes;
    }
    
    public int getUpvotes() {
        return this.upvotes;
    }
    
    public int getDownvotes() {
        return this.downvotes;
    }
    
    public void setUpvotes(final int upvotes) {
        this.upvotes = upvotes;
    }
    
    public void setDownvotes(final int downvotes) {
        this.downvotes = downvotes;
    }
    
    public Votes(final int upvotes, final int downvotes) {
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }
}
