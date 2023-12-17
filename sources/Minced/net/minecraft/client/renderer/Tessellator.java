// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import net.optifine.SmartAnimations;

public class Tessellator
{
    private final BufferBuilder buffer;
    private final WorldVertexBufferUploader vboUploader;
    private static final Tessellator INSTANCE;
    
    public static Tessellator getInstance() {
        return Tessellator.INSTANCE;
    }
    
    public Tessellator(final int bufferSize) {
        this.vboUploader = new WorldVertexBufferUploader();
        this.buffer = new BufferBuilder(bufferSize);
    }
    
    public void draw() {
        if (this.buffer.animatedSprites != null) {
            SmartAnimations.spritesRendered(this.buffer.animatedSprites);
        }
        this.buffer.finishDrawing();
        this.vboUploader.draw(this.buffer);
    }
    
    public BufferBuilder getBuffer() {
        return this.buffer;
    }
    
    static {
        INSTANCE = new Tessellator(2097152);
    }
}
