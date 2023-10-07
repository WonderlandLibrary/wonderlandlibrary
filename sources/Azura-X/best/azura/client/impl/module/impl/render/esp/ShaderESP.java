package best.azura.client.impl.module.impl.render.esp;

import best.azura.client.impl.events.EventRender2D;
import best.azura.client.impl.events.EventRender3D;
import best.azura.eventbus.core.Event;
import best.azura.client.impl.module.impl.render.ESP;
import best.azura.client.api.shader.ShaderProgram;
import best.azura.client.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import org.lwjgl.opengl.GL11;

public class ShaderESP {

    private Framebuffer framebuffer;
    private final Minecraft mc = Minecraft.getMinecraft();

    private final ShaderProgram outlineShader = new ShaderProgram("outline.fsh");
    private final ShaderProgram chamsShader = new ShaderProgram("chams.fsh");
    private final ShaderProgram filledShader = new ShaderProgram("chams1.fsh");

    public ShaderESP() {
        super();
        framebuffer = new Framebuffer(1, 1, false);
    }

    public void onEvent(final Event event) {
        if (framebuffer == null) return;
        if (event instanceof EventRender3D) {
            GlStateManager.pushMatrix();
            this.framebuffer.framebufferClear();
            this.framebuffer.bindFramebuffer(false);
            GlStateManager.disableLighting();
            for (final Entity entity : mc.theWorld.loadedEntityList) {
                if (!((ESP.shaderTargets.isSelected("Players") && entity instanceof EntityPlayer) ||
                        (ESP.shaderTargets.isSelected("Monsters") && entity instanceof EntityCreature) ||
                        (ESP.shaderTargets.isSelected("Animals") && entity instanceof EntityAnimal) ||
                        (ESP.shaderTargets.isSelected("Items") && entity instanceof EntityItem))) continue;
                if (entity == mc.thePlayer && mc.gameSettings.showDebugInfo == 0) continue;
                final Frustum frustum = new Frustum();
                frustum.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                if (!frustum.isBoundingBoxInFrustum(entity.getEntityBoundingBox())) continue;
                GlStateManager.resetColor();
                mc.getRenderManager().renderEntitySimpleNoShadow(entity, mc.timer.renderPartialTicks);
            }
            if (ESP.shaderTargets.isSelected("Chests")) {
                TileEntityRendererDispatcher.instance.skipDistanceChecks = true;
                for (final TileEntity tileEntity : mc.theWorld.loadedTileEntityList) {
                    if (!(tileEntity instanceof TileEntityChest)) continue;
                    GlStateManager.resetColor();
                    TileEntityRendererDispatcher.instance.renderTileEntity(tileEntity, mc.timer.renderPartialTicks, -1);
                }
                TileEntityRendererDispatcher.instance.skipDistanceChecks = false;
            }
            this.framebuffer.bindFramebuffer(false);
            GlStateManager.popMatrix();
            GlStateManager.disableLighting();
            mc.getFramebuffer().bindFramebuffer(false);
        }
        if (event instanceof EventRender2D) {
            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            framebuffer = RenderUtil.INSTANCE.createFramebuffer(framebuffer);
            if (ESP.shaderCombo.isSelected("Outline")) {
                outlineShader.init();
                GL11.glShadeModel(GL11.GL_SMOOTH);
                outlineShader.setUniformi("texture", 0);
                outlineShader.setUniformf("color", ESP.shaderOutlineColor.getObject().getColor().getRed() / 255.0f,
                        ESP.shaderOutlineColor.getObject().getColor().getGreen() / 255.0f,
                        ESP.shaderOutlineColor.getObject().getColor().getBlue() / 255.0f);
                outlineShader.setUniformf("alpha", ESP.shaderOutlineAlpha.getObject());
                outlineShader.setUniformf("radius", 5);
                outlineShader.setUniformf("lineWidth", ESP.shaderOutlineWidth.getObject());
                outlineShader.setUniformf("texelSize", 1f / mc.displayWidth, 1f / mc.displayHeight);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.framebufferTexture);
                outlineShader.renderCanvas();
                outlineShader.uninit();
            }
            if (ESP.shaderCombo.isSelected("Fill")) {
                filledShader.init();
                filledShader.setUniformi("texture", 0);
                filledShader.setUniformf("color", ESP.shaderFilledColor.getObject().getColor().getRed() / 255.0f,
                        ESP.shaderFilledColor.getObject().getColor().getGreen() / 255.0f,
                        ESP.shaderFilledColor.getObject().getColor().getBlue() / 255.0f);
                filledShader.setUniformf("alpha", ESP.shaderFilledAlpha.getObject());
                filledShader.setUniformf("texelSize", 1f / mc.displayWidth, 1f / mc.displayHeight);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.framebufferTexture);
                filledShader.renderCanvas();
                filledShader.uninit();
            }
            if (ESP.shaderCombo.isSelected("Chams")) {
                chamsShader.init();
                chamsShader.setUniformi("texture", 0);
                chamsShader.setUniformf("alpha", ESP.shaderChamsAlpha.getObject());
                chamsShader.setUniformf("texelSize", 1f / mc.displayWidth, 1f / mc.displayHeight);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.framebufferTexture);
                chamsShader.renderCanvas();
                chamsShader.uninit();
            }
            mc.getFramebuffer().bindFramebuffer(true);
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
        }
    }

}