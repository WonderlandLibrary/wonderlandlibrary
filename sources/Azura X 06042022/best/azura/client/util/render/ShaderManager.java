package best.azura.client.util.render;

import best.azura.client.util.textures.ShaderReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

public class ShaderManager {
    public static final HashMap<String, ShaderUtil> shaderMap = new HashMap<>();
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static boolean initialized;
    static {
        reloadShaders();
    }
    public static void reloadShaders() {
        if (!initialized) {
            initialized = true;
            if (mc.getResourceManager() instanceof IReloadableResourceManager)
                ((IReloadableResourceManager)mc.getResourceManager()).registerReloadListener(new ShaderReloadListener());
        }
        shaderMap.clear();
        try {
            shaderMap.put("Background", new ShaderUtil(mc.getResourceManager().getResource(
                    new ResourceLocation("custom/shaders/vertex.vsh")).getInputStream(),
                    mc.getResourceManager().getResource(new ResourceLocation("custom/shaders/backgroundshader.fsh")).getInputStream()));
            shaderMap.put("Color", new ShaderUtil(mc.getResourceManager().getResource(
                    new ResourceLocation("custom/shaders/vertex.vert")).getInputStream(),
                    mc.getResourceManager().getResource(new ResourceLocation("custom/shaders/color.fsh")).getInputStream()));
            shaderMap.put("Outline", new ShaderUtil(mc.getResourceManager().getResource(
                    new ResourceLocation("custom/shaders/vertex.vert")).getInputStream(),
                    mc.getResourceManager().getResource(new ResourceLocation("custom/shaders/outline.fsh")).getInputStream()));
            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.MONTH) + 1 == 10 && calendar.get(Calendar.DATE) == 31)
                shaderMap.put("Background", new ShaderUtil(mc.getResourceManager().getResource(
                        new ResourceLocation("custom/shaders/vertex.vsh")).getInputStream(),
                        mc.getResourceManager().getResource(new ResourceLocation("custom/shaders/backgroundshader.fsh")).getInputStream()));
            if (calendar.get(Calendar.MONTH) + 1 == 12 && calendar.get(Calendar.DATE) >= 22 && calendar.get(Calendar.DATE) <= 30)
                shaderMap.put("Background", new ShaderUtil(mc.getResourceManager().getResource(
                        new ResourceLocation("custom/shaders/vertex.vsh")).getInputStream(),
                        mc.getResourceManager().getResource(new ResourceLocation("custom/shaders/backgroundshaderchristmas.fsh")).getInputStream()));
            if (calendar.get(Calendar.MONTH) + 1 == 2 && calendar.get(Calendar.DATE) == 14)
                shaderMap.put("Background", new ShaderUtil(mc.getResourceManager().getResource(
                        new ResourceLocation("custom/shaders/vertex.vsh")).getInputStream(),
                        mc.getResourceManager().getResource(new ResourceLocation("custom/shaders/backgroundshadervalentines.fsh")).getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}