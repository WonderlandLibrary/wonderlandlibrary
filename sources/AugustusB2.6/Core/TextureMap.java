// 
// Decompiled by Procyon v0.5.36
// 

package Core;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL11;

public class TextureMap
{
    private int txtr_id;
    
    public TextureMap() {
        GL11.glPixelStorei(3317, 4);
        GL13.glActiveTexture(33984);
        this.txtr_id = GL11.glGenTextures();
    }
    
    public void prepare(final int width, final int height) {
        GL11.glBindTexture(3553, this.txtr_id);
        GL11.glTexParameteri(3553, 10242, 33071);
        GL11.glTexParameteri(3553, 10243, 33071);
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glTexEnvf(8960, 8704, 8449.0f);
        final ByteBuffer bb = BufferUtils.createByteBuffer(width * height * 3);
        GL11.glTexImage2D(3553, 0, 6407, width, height, 0, 6407, 5121, bb);
    }
    
    public void activate() {
        GL13.glActiveTexture(33984);
        GL11.glBindTexture(3553, this.getID());
    }
    
    public int getID() {
        return this.txtr_id;
    }
}
