package client.module.impl.render;

import client.Client;
import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.Render2DEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.module.impl.combat.KillAura;
import client.util.PlayerUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.text.DecimalFormat;

@ModuleInfo(name = "Target HUD", description = "Displays information about target", category = Category.RENDER)
public class TargetHUD extends Module {
    @EventLink
    public final Listener<Render2DEvent> onRender2D = event -> {
        final KillAura killAura = Client.INSTANCE.getModuleManager().get(KillAura.class);
        if (!killAura.isEnabled() || killAura.getTarget() == null) return;
        final EntityLivingBase target = killAura.getTarget();
        final ScaledResolution scaledResolution = event.getScaledResolution();
        final int width = scaledResolution.getScaledWidth(), height = scaledResolution.getScaledHeight();
        final String colon = EnumChatFormatting.GRAY + ": " + EnumChatFormatting.RESET;
        final String[] strings = {
                "Name" + colon + target.getName(),
                "Health" + colon + new DecimalFormat("#0.00").format(target.getHealth()),
                "Distance" + colon + new DecimalFormat("#0.00").format(PlayerUtil.getDistance(target))
        };
        final int xOffset = 48, yOffset = 8;
        if (target instanceof AbstractClientPlayer) {
     //          GlStateManager.rotate(4,52,25,53);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            mc.getTextureManager().bindTexture(((AbstractClientPlayer) target).getLocationSkin());
            Gui.drawScaledCustomSizeModalRect(width / 2 + xOffset - 32, height / 2 + yOffset, 8, 8, 8, 8, 32, 32, 64, 64);
            if (((AbstractClientPlayer) target).isWearing(EnumPlayerModelParts.HAT)) Gui.drawScaledCustomSizeModalRect(width / 2 + xOffset - 32, height / 2 + yOffset, 40, 8, 8, 8, 32, 32, 64, 64);
            GlStateManager.disableBlend();
        }
        for (int i = 0; i < strings.length; i++) {
            Gui.drawRect(width / 2 + xOffset, height / 2 + yOffset + 11 * i, width / 2 + xOffset + mc.fontRendererObj.getStringWidth(strings[i]) + 4, height / 2 + yOffset + 11 * i + 11, Integer.MIN_VALUE);
            mc.fontRendererObj.drawString(strings[i], width / 2 + xOffset + 2, height / 2 + yOffset + 2 + 11 * i, -1);
        }
    };
    private int getRainbow(final int i) {
        return Color.getHSBColor((System.currentTimeMillis() - 83 * i) % 1000 / 1000F, 0.5F, 1).getRGB();
    }
}