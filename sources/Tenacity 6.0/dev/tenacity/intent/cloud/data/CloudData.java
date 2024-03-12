// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.intent.cloud.data;

public abstract class CloudData extends Upvoteable
{
    private final String name;
    private final String description;
    private final String shareCode;
    private final String author;
    private final String version;
    private final String lastUpdated;
    private final boolean ownership;
    private boolean pinned;
    private Votes votes;
    
    public int minutesSinceLastUpdate() {
        return (int)((System.currentTimeMillis() / 1000.0f - Long.parseLong(this.lastUpdated)) / 60.0f);
    }
    
    public int daysSinceLastUpdate() {
        return (int)((System.currentTimeMillis() / 1000.0f - Long.parseLong(this.lastUpdated)) / 60.0f / 60.0f / 24.0f);
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getShareCode() {
        return this.shareCode;
    }
    
    public String getAuthor() {
        return this.author;
    }
    
    public String getVersion() {
        return this.version;
    }
    
    public String getLastUpdated() {
        return this.lastUpdated;
    }
    
    public boolean isOwnership() {
        return this.ownership;
    }
    
    public boolean isPinned() {
        return this.pinned;
    }
    
    public Votes getVotes() {
        return this.votes;
    }
    
    public void setPinned(final boolean pinned) {
        this.pinned = pinned;
    }
    
    public void setVotes(final Votes votes) {
        this.votes = votes;
    }
    
    public CloudData(final String name, final String description, final String shareCode, final String author, final String version, final String lastUpdated, final boolean ownership) {
        this.votes = new Votes(0, 0);
        this.name = name;
        this.description = description;
        this.shareCode = shareCode;
        this.author = author;
        this.version = version;
        this.lastUpdated = lastUpdated;
        this.ownership = ownership;
    }
}
