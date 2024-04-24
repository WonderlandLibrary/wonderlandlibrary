package me.crimp.claudius.mod.modules.render;

import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.mod.setting.Setting;
import me.crimp.claudius.utils.Util;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;

public class CoolRenderOptions extends Module {
    public Setting<Boolean> SetDisplayTitle = register(new Setting<>("SetDisplayTitle", true));
    public Setting<Boolean> NoToastPopUps = register(new Setting<>("NoToastPopUps", true));
    public Setting<Boolean> AcidOverlay = register(new Setting<>("AcidOverlay", true));
    public CoolRenderOptions() {
        super("RenderFixes", "Cool Render Options", Category.Render, false, false);
    }

    public void onUpdate() {
       if (SetDisplayTitle.getValue()) Display.setTitle("Minecraft 1.12.2");
       if (NoToastPopUps.getValue()) mc.toastGui.clear();
    }


    @Override
    public void onEnable() {
        if (AcidOverlay.getValue()) {
            if (this.mc.world != null) {
                Util.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/wobble.json"));
            }
        }
    }

    @Override
    public void onDisable() {
        if (this.mc.world != null) {
            Util.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }
}
