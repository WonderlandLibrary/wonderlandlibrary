package tech.dort.dortware.impl.modules.render.hud;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import skidmonke.Client;
import skidmonke.Minecraft;
import tech.dort.dortware.api.module.Module;
import tech.dort.dortware.api.module.ModuleData;
import tech.dort.dortware.impl.events.RenderHUDEvent;
import tech.dort.dortware.impl.managers.ModuleManager;
import tech.dort.dortware.impl.modules.render.Hud;

import java.text.DecimalFormat;
import java.util.Arrays;

public class OGMemeClientTheme extends Theme {
    public OGMemeClientTheme(Module module) {
        super(module);
    }

    @Override
    public void render(RenderHUDEvent event) {
        if (mc.gameSettings.showDebugInfo)
            return;
        final Hud hud = Client.INSTANCE.getModuleManager().get(Hud.class);
        final Boolean playerModel = hud.playerModel.getValue();
        final Boolean watermark = hud.watermark.getValue();
        final Boolean armorHud = hud.armorHUD.getValue();
        final String alternativeNames = hud.alternativeNameMode.getValue().getDisplayName();
        final Boolean edition = hud.edition.getValue();
        final Boolean version = hud.version.getValue();
        final Boolean fps = hud.fpsCounter.getValue();
        final Boolean bps = hud.bpsCounter.getValue();
        final Boolean ping = hud.ping.getValue();
        final Boolean lowercase = hud.lowercase.getValue();
        final int spacing = hud.spacing.getValue().intValue();
        int y = 3;
        Client.INSTANCE.getModuleManager().sort(mc.fontRendererObj);

        if (watermark) {
            GlStateManager.pushMatrix();
            String info = "";
            if (version) {
                info += Client.INSTANCE.getClientVersion();
            }
            if (edition) {
                info += " (" + (lowercase ? Client.INSTANCE.getClientEdition().toLowerCase() : Client.INSTANCE.getClientEdition()) + ")";
            }
            mc.fontRendererObj.drawStringWithShadow(String.join(" ", lowercase ? "\247edortware" : "\247eDortware", info), 3, 3, -1);
            GlStateManager.popMatrix();
        }

        NetworkPlayerInfo info = mc.getNetHandler().func_175102_a(mc.thePlayer.getUniqueID());
        double xz = (Math.hypot(mc.thePlayer.posX - mc.thePlayer.prevPosX, mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * mc.timer.timerSpeed) * 20;
        DecimalFormat bpsFormat = new DecimalFormat("#.##");
        try {
            mc.fontRendererObj.drawStringWithShadow(bps ? lowercase ? "bps: " + bpsFormat.format(xz) : "BPS: " + bpsFormat.format(xz) : "", 2, !fps && !ping ? new ScaledResolution(mc).getScaledHeight() - (mc.currentScreen instanceof GuiChat ? 24 : 10) : new ScaledResolution(mc).getScaledHeight() - (mc.currentScreen instanceof GuiChat ? 34 : 22), -1);
            mc.fontRendererObj.drawStringWithShadow(ping ? lowercase ? "ping: " + info.getResponseTime() : "Ping: " + info.getResponseTime() : "", fps ? mc.fontRendererObj.getStringWidth("FPS: " + Minecraft.debugFPS) + 8 : 2, new ScaledResolution(mc).getScaledHeight() - (mc.currentScreen instanceof GuiChat ? 24 : 10), -1);
            mc.fontRendererObj.drawStringWithShadow(fps ? lowercase ? "fps: " + Minecraft.debugFPS : "FPS: " + Minecraft.debugFPS : "", 2, new ScaledResolution(mc).getScaledHeight() - (mc.currentScreen instanceof GuiChat ? 24 : 10), -1);
        } catch (Exception ignored) {
        }

        ScaledResolution scaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        final ModuleManager moduleManager = Client.INSTANCE.getModuleManager();
        for (Module module : alternativeNames.equalsIgnoreCase("Memes") ? moduleManager.getModulesSortedAlternativeNoFont(mc.fontRendererObj) : alternativeNames.equalsIgnoreCase("Orialeng") ? moduleManager.getModulesSortedAlternativeDumbNoFont(mc.fontRendererObj) : moduleManager.getObjects()) {
            if (!module.isToggled())
                continue;
            ModuleData moduleData = module.getModuleData();
            String data = "";
            if (module.getSuffix()
                    != null) {
                data = module.getSuffix();
            }

            String name = moduleData.getName();
            if (alternativeNames.equalsIgnoreCase("Memes") && moduleData.hasOtherName())
                name = moduleData.getOtherName();
            else if (alternativeNames.equalsIgnoreCase("Orialeng") && moduleData.hasOtherNameDumb())
                name = moduleData.getOtherNameDumb();

            float xPos = scaledResolution.getScaledWidth() - mc.fontRendererObj.getStringWidth((name + data)) - 3;
//                Gui.drawRect(xPos - 7, y, scaledResolution.getScaledWidth(), y + spacing, new Color(0, 0, 0, 120).getRGB());
//                Gui.drawRect(scaledResolution.getScaledWidth() - 4, y, scaledResolution.getScaledWidth(), y + spacing, ColorUtil.getRainbow(-6000, y * 8));
            mc.fontRendererObj.drawStringWithShadow(name + data, xPos, y, module.getColor());
            y += spacing;
        }
        int playerY = hud.tabGui.getValue() ? 210 : 90;
        int playerX = (scaledResolution.getScaledWidth() / 2) + 5;
        if (playerModel) {
            hud.drawEntityOnScreen(35, playerY, mc.thePlayer);
        }

        if (armorHud) {
            for (ItemStack itemStack : Lists.reverse(Arrays.asList(mc.thePlayer.inventory.armorInventory))) {
                if (itemStack != null) {
                    mc.fontRendererObj.drawStringWithShadow("\2472" + (itemStack.getMaxDamage() - itemStack.getItemDamage()), playerX - 3, scaledResolution.getScaledHeight() - 60, -1);
                    mc.getRenderItem().func_175042_a(itemStack, playerX, scaledResolution.getScaledHeight() - 52);
                }
                playerX += 22;
            }
        }
    }
}
