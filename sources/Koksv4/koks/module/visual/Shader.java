package koks.module.visual;

import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.event.OutlineEvent;
import koks.event.RenderEntityOutlineFramebufferEvent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.optifine.reflect.Reflector;

import java.util.List;
import java.util.function.Predicate;

@Module.Info(name = "Shader", description = "You can change the Shader", category = Module.Category.VISUAL)
public class Shader extends Module {

    @Value(name = "Mode", modes = {"Vanilla", "Outline"})
    String mode = "Vanilla";

    @Value(name = "Vanilla-SrcFactor", displayName = "Src Factor", minimum = 0, maximum = 770)
    int vanillaSrcFactor = 1;

    @Value(name = "Vanilla-DstFactor", displayName = "Dst Factor", minimum = 0, maximum = 771)
    int vanillaDstFactor = 1;

    @Value(name = "Vanilla-SrcFactorAlpha", displayName = "Src Factor Alpha", minimum = 0, maximum = 1)
    int vanillaSrcFactorAlpha = 0;

    @Value(name = "Vanilla-DstFactorAlpha", displayName = "Dst Factor Alpha", minimum = 0, maximum = 1)
    int vanillaDstFactorAlpha = 0;

    @Value(name = "Team Color")
    boolean teamColor = false;

    @Value(name = "Color", colorPicker = true)
    int color = 0xFFFF;

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        if (name.contains("-")) {
            final String[] split = name.split("-");
            return split[0].equalsIgnoreCase(mode);
        }
        return true;
    }

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof final OutlineEvent outlineEvent) {
            outlineEvent.setAllowOutline(true);
        }
        switch (mode) {
            case "Vanilla":
                if (event instanceof final RenderEntityOutlineFramebufferEvent framebufferEvent) {
                    framebufferEvent.setSrcFactor(vanillaSrcFactor);
                    framebufferEvent.setDstFactor(vanillaDstFactor);
                    framebufferEvent.setDstFactorAlpha(vanillaDstFactorAlpha);
                    framebufferEvent.setSrcFactorAlpha(vanillaSrcFactorAlpha);
                }
                break;
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    public void drawChestShader(List list, boolean flag, RenderManager renderManager, float partialTicks, Entity renderViewEntity, ICamera camera, int i) {
        switch (mode) {
            case "Vanilla":
                for (int k = 0; k < getWorld().loadedTileEntityList.size(); k++) {
                    final TileEntity chest = getWorld().loadedTileEntityList.get(k);
                    if (!flag || Reflector.callBoolean(chest, Reflector.ForgeEntity_shouldRenderInPass, i)) {
                        if (chest instanceof TileEntityChest) {
                            GlStateManager.disableTexture2D();
                            TileEntityRendererDispatcher.instance.renderTileEntity(chest, partialTicks, 1);
                            GlStateManager.enableTexture2D();
                        }
                    }
                }
                break;
        }
    }

    public void drawShader(Predicate<Entity> predicate, List list, boolean flag, RenderManager renderManager, float partialTicks, Entity renderViewEntity, ICamera camera, int i) {
        final double d0 = renderViewEntity.prevPosX + (renderViewEntity.posX - renderViewEntity.prevPosX) * (double) partialTicks;
        final double d1 = renderViewEntity.prevPosY + (renderViewEntity.posY - renderViewEntity.prevPosY) * (double) partialTicks;
        final double d2 = renderViewEntity.prevPosZ + (renderViewEntity.posZ - renderViewEntity.prevPosZ) * (double) partialTicks;
        for (Object o : list) {
            final Entity entity3 = (Entity) o;
            if (!flag || Reflector.callBoolean(entity3, Reflector.ForgeEntity_shouldRenderInPass, i)) {
                final boolean flag2 = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase) this.mc.getRenderViewEntity()).isPlayerSleeping();
                final boolean flag3 = entity3.isInRangeToRender3d(d0, d1, d2) && (entity3.ignoreFrustumCheck || camera.isBoundingBoxInFrustum(entity3.getEntityBoundingBox()) || entity3.riddenByEntity == this.mc.thePlayer);
                if (((entity3 != this.mc.getRenderViewEntity() || this.mc.gameSettings.thirdPersonView != 0) && flag3) && predicate.test(entity3)) {
                    switch (mode) {
                        case "Vanilla":
                            renderManager.renderEntitySimple(entity3, partialTicks);
                            break;
                        case "Outline":
                           /* GL11.glEnable(GL11.GL_STENCIL_TEST);
                            GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
                            GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
                            GL11.glStencilMask(0xFF);
                            espShader.use();
                            renderManager.renderEntitySimple(entity3, partialTicks);
                            espShader.unUse();
                            GL11.glDisable(GL11.GL_STENCIL_TEST);*/
                            break;
                    }
                }
            }
        }
    }
}
