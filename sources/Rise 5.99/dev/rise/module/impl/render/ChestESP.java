package dev.rise.module.impl.render;

import dev.rise.event.impl.render.Render3DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.util.render.RenderUtil;
import dev.rise.util.render.theme.ThemeType;
import dev.rise.util.render.theme.ThemeUtil;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import org.lwjgl.opengl.GL11;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.awt.*;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "ChestESP", description = "Shows chests through walls", category = Category.RENDER)
public final class ChestESP extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "2D", "2D");

    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        if (Interface.theme == null)
            return;

        switch (mode.getMode()) {
            case "2D": {
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
                break;
            }
        }
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

        final Color c = ThemeUtil.getThemeColor(amount, ThemeType.GENERAL, 0.5f);

        final float offset = renderManager.playerViewX * 0.5f;

        RenderUtil.lineNoGl(-50, offset, 50, offset, c);
        RenderUtil.lineNoGl(-50, -95 + offset, -50, offset, c);
        RenderUtil.lineNoGl(-50, -95 + offset, 50, -95 + offset, c);
        RenderUtil.lineNoGl(50, -95 + offset, 50, offset, c);

        GL11.glPopMatrix();
    }
}
