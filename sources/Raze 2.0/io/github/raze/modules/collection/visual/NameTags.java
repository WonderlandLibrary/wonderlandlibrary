package io.github.raze.modules.collection.visual;

import io.github.raze.Raze;
import io.github.raze.events.collection.visual.EventRender3D;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.ArraySetting;
import io.github.raze.settings.collection.NumberSetting;
import io.github.raze.utilities.collection.math.MathUtil;
import io.github.raze.utilities.collection.visual.ColorUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class NameTags extends AbstractModule {

    private final ArraySetting colorMode;
    private final NumberSetting scale, rColor, gColor, bColor;

    public NameTags() {
        super("NameTags", "Changes nametags.", ModuleCategory.VISUAL);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                colorMode = new ArraySetting(this, "Color", "Custom", "Custom", "Rainbow", "Astolfo"),
                scale = new NumberSetting(this, "Scale", 0.1, 2, 0.12),

                rColor = new NumberSetting(this, "R", 0, 255, 255)
                        .setHidden(() -> !colorMode.compare("Custom")),

                gColor = new NumberSetting(this, "G", 0, 255, 255)
                        .setHidden(() -> !colorMode.compare("Custom")),

                bColor = new NumberSetting(this, "B", 0, 255, 255)
                        .setHidden(() -> !colorMode.compare("Custom"))
        );
    }

    @Listen
    public void onRender(EventRender3D eventRender3D) {
        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player.isEntityAlive() && player.getName() != null && player != mc.thePlayer) {
                if (player.getName().isEmpty() || player.getName().equals(" "))
                    return;

                double renderPosX = MathUtil.interpolate(player.posX, player.lastTickPosX) - mc.getRenderManager().renderPosX;
                double renderPosY = MathUtil.interpolate(player.posY, player.lastTickPosY) - mc.getRenderManager().renderPosY;
                double renderPosZ = MathUtil.interpolate(player.posZ, player.lastTickPosZ) - mc.getRenderManager().renderPosZ;

                double height = player.height;
                double scaleValue = scale.get().doubleValue() / 4;

                float r, g, b;
                if(colorMode.compare("Custom")) {
                    r = (float) (rColor.get().floatValue() / 255.0);
                    g = (float) (gColor.get().floatValue() / 255.0);
                    b = (float) (bColor.get().floatValue() / 255.0);
                } else if(colorMode.compare("Rainbow")) {
                    Color rainbowColor = ColorUtil.getRainbow(5.0f, 1.0f, 1.0f, 2000);
                    r = rainbowColor.getRed() / 255.0f;
                    g = rainbowColor.getGreen() / 255.0f;
                    b = rainbowColor.getBlue() / 255.0f;
                } else {
                    int yOffset = 1000;
                    int yTotal = 2000;
                    int astolfoRGB = ColorUtil.AstolfoRGB(yOffset, yTotal);
                    r = ((astolfoRGB >> 16) & 0xFF) / 255.0f;
                    g = ((astolfoRGB >> 8) & 0xFF) / 255.0f;
                    b = (astolfoRGB & 0xFF) / 255.0f;
                }

                GlStateManager.pushMatrix();
                GlStateManager.translate(renderPosX, renderPosY + height + 0.5, renderPosZ);
                GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
                GlStateManager.disableLighting();
                GlStateManager.depthMask(false);
                GlStateManager.disableDepth();

                GlStateManager.scale(-scaleValue, -scaleValue, scaleValue);

                int xPos = -mc.fontRenderer.getStringWidth(player.getName() + EnumChatFormatting.GREEN + " ❤" + Math.round(player.getHealth() / 2.0f * 10) / 10) / 2;
                int yPos = (-mc.fontRenderer.FONT_HEIGHT / 2) - 2;

                mc.fontRenderer.drawStringWithShadow(
                        player.getName() + EnumChatFormatting.GREEN + " ❤" + Math.round(player.getHealth() / 2.0f * 10) / 10,
                        xPos,
                        yPos,
                        new Color(r,g,b).getRGB()
                );

                GlStateManager.enableDepth();
                GlStateManager.depthMask(true);
                GlStateManager.enableTexture2D();
                GlStateManager.popMatrix();
            }
        }
    }

}
