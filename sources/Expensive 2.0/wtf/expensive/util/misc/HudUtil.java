package wtf.expensive.util.misc;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;
import wtf.expensive.util.IMinecraft;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.font.styled.StyledFont;

import java.util.List;

import static wtf.expensive.util.render.RenderUtil.Render2D.drawTexture;
import static wtf.expensive.util.render.RenderUtil.Render2D.getHurtPercent;

public class HudUtil implements IMinecraft {

    public static String calculateBPS() {
        return String.format("%.2f", Math.hypot(mc.player.getPosX() - mc.player.prevPosX, mc.player.getPosZ() - mc.player.prevPosZ) * (double) mc.timer.timerSpeed * 20.0D);
    }

    public static void drawItemStack(ItemStack stack, float x, float y, boolean withoutOverlay, boolean scale, float scaleValue) {
        RenderSystem.pushMatrix();
        RenderSystem.translatef(x, y, 0);
        if (scale) GL11.glScaled(scaleValue, scaleValue, scaleValue);
        mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, 0, 0);
        if (withoutOverlay) mc.getItemRenderer().renderItemOverlays(mc.fontRenderer, stack, 0, 0);
        RenderSystem.popMatrix();
    }

    public static int calculatePing() {
        return mc.player.connection.getPlayerInfo(mc.player.getUniqueID()) != null ?
                mc.player.connection.getPlayerInfo(mc.player.getUniqueID()).getResponseTime() : 0;
    }

    public static String serverIP() {
        return mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP != null && !mc.isSingleplayer() ? mc.getCurrentServerData().serverIP : "";
    }

    public static List<Function> getSorted(StyledFont font) {
        List<Function> modules = Managment.FUNCTION_MANAGER.getFunctions();
        modules.sort((o1, o2) -> {
            float width1 = font.getWidth(o1.name) + 4;
            float width2 = font.getWidth(o2.name) + 4;
            return Float.compare(width2, width1);
        });
        return modules;
    }

    public static void drawFace(float x, float y, float width, float height, float radius, AbstractClientPlayerEntity target) {
        ResourceLocation skin = target.getLocationSkin();
        mc.getTextureManager().bindTexture(skin);
        drawTexture(x, y, width, height, radius, 1F);
    }

}
