package net.optifine.shaders;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.resources.IResourceManager;
import net.optifine.shaders.MultiTexID;
import net.optifine.shaders.ShadersTex;

public class DefaultTexture
extends AbstractTexture {
    public DefaultTexture() {
        this.loadTexture(null);
    }

    public void loadTexture(IResourceManager resourcemanager) {
        int[] aint = ShadersTex.createAIntImage((int)1, (int)-1);
        ShadersTex.setupTexture((MultiTexID)this.getMultiTexID(), (int[])aint, (int)1, (int)1, (boolean)false, (boolean)false);
    }
}
