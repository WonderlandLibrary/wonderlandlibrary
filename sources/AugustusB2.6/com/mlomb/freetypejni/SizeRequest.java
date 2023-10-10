// 
// Decompiled by Procyon v0.5.36
// 

package com.mlomb.freetypejni;

public class SizeRequest
{
    private int type;
    private long width;
    private long height;
    private int horiResolution;
    private int vertResolution;
    
    public SizeRequest(final FreeTypeConstants.FT_Size_Request_Type ft_Size_Request_Type, final long width, final long height, final int horiResolution, final int vertResolution) {
        this.type = ft_Size_Request_Type.ordinal();
        this.width = width;
        this.height = height;
        this.horiResolution = horiResolution;
        this.vertResolution = vertResolution;
    }
    
    public FreeTypeConstants.FT_Size_Request_Type getType() {
        return FreeTypeConstants.FT_Size_Request_Type.values()[this.type];
    }
    
    public long getWidth() {
        return this.width;
    }
    
    public long getHeight() {
        return this.height;
    }
    
    public int getVertResolution() {
        return this.vertResolution;
    }
    
    public int getHoriResolution() {
        return this.horiResolution;
    }
    
    public void setType(final FreeTypeConstants.FT_Size_Request_Type ft_Size_Request_Type) {
        this.type = ft_Size_Request_Type.ordinal();
    }
    
    public void setWidth(final long width) {
        this.width = width;
    }
    
    public void setHeight(final long height) {
        this.height = height;
    }
    
    public void setHoriResolution(final int horiResolution) {
        this.horiResolution = horiResolution;
    }
    
    public void setVertResolution(final int vertResolution) {
        this.vertResolution = vertResolution;
    }
}
