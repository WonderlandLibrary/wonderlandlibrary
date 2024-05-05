package net.optifine.shaders;

import java.nio.ByteBuffer;
import net.optifine.shaders.CustomTextureRaw;
import net.optifine.shaders.ICustomTexture;
import net.optifine.texture.InternalFormat;
import net.optifine.texture.PixelFormat;
import net.optifine.texture.PixelType;
import net.optifine.texture.TextureType;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class CustomTextureRaw
implements ICustomTexture {
    private TextureType type;
    private int textureUnit;
    private int textureId;

    public CustomTextureRaw(TextureType type, InternalFormat internalFormat, int width, int height, int depth, PixelFormat pixelFormat, PixelType pixelType, ByteBuffer data, int textureUnit, boolean blur, boolean clamp) {
        this.type = type;
        this.textureUnit = textureUnit;
        this.textureId = GL11.glGenTextures();
        GL11.glBindTexture((int)this.getTarget(), (int)this.textureId);
        int i = clamp ? 33071 : 10497;
        int j = blur ? 9729 : 9728;
        switch (1.$SwitchMap$net$optifine$texture$TextureType[type.ordinal()]) {
            case 1: {
                GL11.glTexImage1D((int)3552, (int)0, (int)internalFormat.getId(), (int)width, (int)0, (int)pixelFormat.getId(), (int)pixelType.getId(), (ByteBuffer)data);
                GL11.glTexParameteri((int)3552, (int)10242, (int)i);
                GL11.glTexParameteri((int)3552, (int)10240, (int)j);
                GL11.glTexParameteri((int)3552, (int)10241, (int)j);
                break;
            }
            case 2: {
                GL11.glTexImage2D((int)3553, (int)0, (int)internalFormat.getId(), (int)width, (int)height, (int)0, (int)pixelFormat.getId(), (int)pixelType.getId(), (ByteBuffer)data);
                GL11.glTexParameteri((int)3553, (int)10242, (int)i);
                GL11.glTexParameteri((int)3553, (int)10243, (int)i);
                GL11.glTexParameteri((int)3553, (int)10240, (int)j);
                GL11.glTexParameteri((int)3553, (int)10241, (int)j);
                break;
            }
            case 3: {
                GL12.glTexImage3D((int)32879, (int)0, (int)internalFormat.getId(), (int)width, (int)height, (int)depth, (int)0, (int)pixelFormat.getId(), (int)pixelType.getId(), (ByteBuffer)data);
                GL11.glTexParameteri((int)32879, (int)10242, (int)i);
                GL11.glTexParameteri((int)32879, (int)10243, (int)i);
                GL11.glTexParameteri((int)32879, (int)32882, (int)i);
                GL11.glTexParameteri((int)32879, (int)10240, (int)j);
                GL11.glTexParameteri((int)32879, (int)10241, (int)j);
                break;
            }
            case 4: {
                GL11.glTexImage2D((int)34037, (int)0, (int)internalFormat.getId(), (int)width, (int)height, (int)0, (int)pixelFormat.getId(), (int)pixelType.getId(), (ByteBuffer)data);
                GL11.glTexParameteri((int)34037, (int)10242, (int)i);
                GL11.glTexParameteri((int)34037, (int)10243, (int)i);
                GL11.glTexParameteri((int)34037, (int)10240, (int)j);
                GL11.glTexParameteri((int)34037, (int)10241, (int)j);
            }
        }
        GL11.glBindTexture((int)this.getTarget(), (int)0);
    }

    public int getTarget() {
        return this.type.getId();
    }

    public int getTextureId() {
        return this.textureId;
    }

    public int getTextureUnit() {
        return this.textureUnit;
    }

    public void deleteTexture() {
        if (this.textureId > 0) {
            GL11.glDeleteTextures((int)this.textureId);
            this.textureId = 0;
        }
    }
}
