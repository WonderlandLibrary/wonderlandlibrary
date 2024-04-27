/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import co.uk.hexeption.utils.OutlineUtils;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.world.ChestAura;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.shader.FramebufferShader;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.GlowShader;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.OutlineShader;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.tileentity.*;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

@ModuleInfo(name = "StorageESP", description = "Allows you to see chests, dispensers, etc. through walls.", category = ModuleCategory.RENDER)
public class StorageESP extends Module {



    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glPushMatrix();

        int amount = 0;
        for (final TileEntity tileEntity : mc.theWorld.loadedTileEntityList) {
            if (tileEntity instanceof TileEntityChest || tileEntity instanceof TileEntityEnderChest) {
                render(amount, tileEntity);
                amount++;
            }
        }

        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private void render(final int amount, final TileEntity p) {
        GL11.glPushMatrix();

        final RenderManager renderManager = mc.getRenderManager();

        final double x = (p.getPos().getX() + 0.5) - renderManager.renderPosX;
        final double y = p.getPos().getY() - renderManager.renderPosY;
        final double z = (p.getPos().getZ() + 0.5) - renderManager.renderPosZ;

        GL11.glTranslated(x, y, z);

        GL11.glRotated(-renderManager.playerViewY, 0.0D, 1.0D, 0.0D);
        GL11.glRotated(renderManager.playerViewX, mc.gameSettings.thirdPersonView == 2 ? -1.0D : 1.0D, 0.0D, 0.0D);

        final float scale = 1 / 100f;
        GL11.glScalef(-scale, -scale, scale);



        final float offset = renderManager.playerViewX * 0.5f;

        RenderUtils.lineNoGl(-50, offset, 50, offset, new Color(ClickGUI.colorRedValueModules.get(),ClickGUI.colorGreenValueModules.get(),ClickGUI.colorBlueValueModules.get()));
        RenderUtils.lineNoGl(-50, -95 + offset, -50, offset,  new Color(ClickGUI.colorRedValueModules.get(),ClickGUI.colorGreenValueModules.get(),ClickGUI.colorBlueValueModules.get()));
        RenderUtils.lineNoGl(-50, -95 + offset, 50, -95 + offset,  new Color(ClickGUI.colorRedValueModules.get(),ClickGUI.colorGreenValueModules.get(),ClickGUI.colorBlueValueModules.get()));
        RenderUtils.lineNoGl(50, -95 + offset, 50, offset,  new Color(ClickGUI.colorRedValueModules.get(),ClickGUI.colorGreenValueModules.get(),ClickGUI.colorBlueValueModules.get()));

        GL11.glPopMatrix();
    }
}

