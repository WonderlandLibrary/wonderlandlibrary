package net.minecraft.client.renderer.texture;

import java.io.IOException;
import net.minecraft.client.resources.IResourceManager;
import shadersmod.client.MultiTexID;

public interface ITextureObject {
   void loadTexture(IResourceManager var1) throws IOException;

   void restoreLastBlurMipmap();

   void setBlurMipmap(boolean var1, boolean var2);

   MultiTexID getMultiTexID();

   int getGlTextureId();
}
