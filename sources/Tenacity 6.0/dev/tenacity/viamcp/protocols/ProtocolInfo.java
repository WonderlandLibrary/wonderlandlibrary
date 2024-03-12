// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.viamcp.protocols;

public class ProtocolInfo
{
    private final String name;
    private final String description;
    private final String releaseDate;
    
    public ProtocolInfo(final String name, final String description, final String releaseDate) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getReleaseDate() {
        return this.releaseDate;
    }
}
