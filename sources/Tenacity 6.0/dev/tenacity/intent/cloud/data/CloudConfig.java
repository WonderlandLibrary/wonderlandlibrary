// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.intent.cloud.data;

public class CloudConfig extends CloudData
{
    private final String server;
    
    public CloudConfig(final String name, final String description, final String shareCode, final String author, final String version, final String lastUpdated, final String server, final boolean ownership) {
        super(name, description, shareCode, author, version, lastUpdated, ownership);
        this.server = server;
    }
    
    public String getServer() {
        return this.server;
    }
}
