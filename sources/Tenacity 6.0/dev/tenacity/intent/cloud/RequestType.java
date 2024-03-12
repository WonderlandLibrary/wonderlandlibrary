// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.intent.cloud;

public enum RequestType
{
    UPLOAD("post"), 
    DELETE("delete"), 
    LIST("public"), 
    RETRIEVE("get"), 
    UPDATE("update");
    
    private final String extension;
    
    public String getExtension() {
        return this.extension;
    }
    
    private RequestType(final String extension) {
        this.extension = extension;
    }
}
