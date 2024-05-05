package net.minecraft.client.renderer.texture;

import java.io.IOException;
import net.minecraft.client.resources.IResourceManager;
import shadersmod.client.MultiTexID;

public abstract interface ITextureObject
{
  public abstract void func_174936_b(boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract void func_174935_a();
  
  public abstract void loadTexture(IResourceManager paramIResourceManager)
    throws IOException;
  
  public abstract int getGlTextureId();
  
  public abstract MultiTexID getMultiTexID();
}
