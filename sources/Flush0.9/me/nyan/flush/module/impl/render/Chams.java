package me.nyan.flush.module.impl.render;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventEntityLight;
import me.nyan.flush.event.impl.EventRenderEntity;
import me.nyan.flush.event.impl.EventRenderEntityModel;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.ColorSetting;
import me.nyan.flush.utils.player.PlayerUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

public class Chams extends Module {
    private final ColorSetting color = new ColorSetting("Color", this, 0xFFFF0000);
    private final BooleanSetting colorEnabled = new BooleanSetting("Use Color", this, false),
            players = new BooleanSetting("Players", this, true),
            creatures = new BooleanSetting("Creatures", this, false),
            villagers = new BooleanSetting("Villagers", this, false),
            invisibles = new BooleanSetting("Invisibles", this, false),
            ignoreTeam = new BooleanSetting("Ignore Team", this, false);

    public Chams() {
        super("Chams", Category.RENDER);
    }

    @SubscribeEvent
    public void onRenderEntity(EventRenderEntity e) {
        if (colorEnabled.getValue() || !(e.getEntity() instanceof EntityLivingBase && isValid((EntityLivingBase) e.getEntity()))) {
            return;
        }

        if (e.isPre()) {
            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(1, -1000000);
        } else {
            GL11.glPolygonOffset(1, 1000000);
            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        }
    }

    @SubscribeEvent
    public void onRenderModel(EventRenderEntityModel e) {
        if (!colorEnabled.getValue() || !isValid(e.getEntity())) {
            return;
        }

        e.cancel();

        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        RenderUtils.glColor(color.getRGB());
        e.getModelBase().render(e.getEntity(), e.getVar1(), e.getVar2(), e.getVar3(), e.getVar4(), e.getVar5(), e.getScaleFactor());
        GlStateManager.enableDepth();
        RenderUtils.glColor(color.getRGB());
        e.getModelBase().render(e.getEntity(), e.getVar1(), e.getVar2(), e.getVar3(), e.getVar4(), e.getVar5(), e.getScaleFactor());
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        RenderUtils.glColor(-1);
    }

    @SubscribeEvent
    public void onEntityLight(EventEntityLight e) {
        if (!(e.getEntity() instanceof EntityLivingBase && isValid((EntityLivingBase) e.getEntity()))) {
            return;
        }

        e.setLight(15);
    }

    public boolean isValid(EntityLivingBase entity) {
        return PlayerUtils.isValid(entity, players.getValue(), creatures.getValue(),
                villagers.getValue(), invisibles.getValue(), ignoreTeam.getValue());
    }
}
