package me.nyan.flush.module.impl.render;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventRenderChest;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.ColorSetting;
import me.nyan.flush.utils.render.RenderUtils;
import me.nyan.flush.event.Event;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ChestESP extends Module {
    private final ColorSetting color = new ColorSetting("Color", this, 0xFFFF0000);
    public final BooleanSetting useColor = new BooleanSetting("Use Color", this, false),
            chests = new BooleanSetting("Chests", this, true),
            enderChests = new BooleanSetting("Ender Chests", this, true);

    public ChestESP() {
        super("ChestESP", Category.RENDER);
    }

    @SubscribeEvent
    public void onRenderChest(EventRenderChest e) {
        if ((e.getType() == EventRenderChest.ChestType.CHEST && chests.getValue()) || (e.getType() == EventRenderChest.ChestType.ENDERCHEST && enderChests.getValue())) {
            if (useColor.getValue()) {
                switch (e.getState()) {
                    case PRE:
                        GlStateManager.disableTexture2D();
                        GlStateManager.disableDepth();
                        GL11.glDisable(GL11.GL_LIGHTING);
                        RenderUtils.glColor(color.getRGB());
                        break;
                    case POST:
                        GL11.glEnable(GL11.GL_LIGHTING);
                        GlStateManager.enableTexture2D();
                        GlStateManager.enableDepth();
                        RenderUtils.glColor(-1);
                        break;
                }
                return;
            }

            switch (e.getState()) {
                case PRE:
                    GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
                    GL11.glPolygonOffset(1, -1000000);
                    break;
                case POST:
                    GL11.glPolygonOffset(1, 1000000);
                    GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
                    break;
            }
        }
    }
}
