package best.azura.client.util.textures;

import best.azura.client.util.render.ShaderManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public class ShaderReloadListener implements IResourceManagerReloadListener {
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        ShaderManager.reloadShaders();
    }
}