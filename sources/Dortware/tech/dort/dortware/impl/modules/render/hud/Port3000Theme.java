package tech.dort.dortware.impl.modules.render.hud;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.item.ItemStack;
import skidmonke.Client;
import skidmonke.Minecraft;
import tech.dort.dortware.api.font.CustomFontRenderer;
import tech.dort.dortware.api.module.Module;
import tech.dort.dortware.api.module.ModuleData;
import tech.dort.dortware.impl.events.RenderHUDEvent;
import tech.dort.dortware.impl.managers.ModuleManager;
import tech.dort.dortware.impl.modules.render.Hud;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Arrays;

public class Port3000Theme extends Theme {
    public Port3000Theme(Module module) {
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
        final Boolean fps = hud.fpsCounter.getValue();
        final Boolean bps = hud.bpsCounter.getValue();
        final Boolean ping = hud.ping.getValue();
        final Boolean lowercase = hud.lowercase.getValue();
        int y = 6;
        final CustomFontRenderer font = Client.INSTANCE.getFontManager().getFont("Skidma").getRenderer();
        final CustomFontRenderer fontSmall = Client.INSTANCE.getFontManager().getFont("SkidmaSmall").getRenderer();
        final CustomFontRenderer font1 = Client.INSTANCE.getFontManager().getFont("SkidmaArray").getRenderer();
        final CustomFontRenderer font2 = Client.INSTANCE.getFontManager().getFont("SmallJello").getRenderer();
        Client.INSTANCE.getModuleManager().sortNoSuffix(font1);

        if (watermark) {
            font.drawString(lowercase ? "dortware" : "Dortware", 8, 12, new Color(255, 255, 255, 150).getRGB());
            fontSmall.drawString(lowercase ? "port 3000" : "Port 3000", 10, 34, new Color(255, 255, 255, 150).getRGB());
        }

        NetworkPlayerInfo info = mc.getNetHandler().func_175102_a(mc.thePlayer.getUniqueID());
        double xz = (Math.hypot(mc.thePlayer.posX - mc.thePlayer.prevPosX, mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * mc.timer.timerSpeed) * 20;
        DecimalFormat bpsFormat = new DecimalFormat("#.##");
        try {
            font2.drawStringWithShadow(bps ? lowercase ? "bps: " + bpsFormat.format(xz) : "BPS: " + bpsFormat.format(xz) : "", 2, !fps && !ping ? new ScaledResolution(mc).getScaledHeight() - (mc.currentScreen instanceof GuiChat ? 24 : 10) : new ScaledResolution(mc).getScaledHeight() - (mc.currentScreen instanceof GuiChat ? 34 : 22), -1);
            font2.drawStringWithShadow(ping ? lowercase ? "ping: " + info.getResponseTime() : "Ping: " + info.getResponseTime() : "", fps ? mc.fontRendererObj.getStringWidth("FPS: " + Minecraft.debugFPS) - 2 : 2, new ScaledResolution(mc).getScaledHeight() - (mc.currentScreen instanceof GuiChat ? 24 : 10), -1);
            font2.drawStringWithShadow(fps ? lowercase ? "fps: " + Minecraft.debugFPS : "FPS: " + Minecraft.debugFPS : "", 2, new ScaledResolution(mc).getScaledHeight() - (mc.currentScreen instanceof GuiChat ? 24 : 10), -1);
        } catch (Exception ignored) {
        }

        ScaledResolution scaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        final ModuleManager moduleManager = Client.INSTANCE.getModuleManager();
        for (Module module : alternativeNames.equalsIgnoreCase("Memes") ? moduleManager.getModulesSortedAlternative(font1) : alternativeNames.equalsIgnoreCase("Orialeng") ? moduleManager.getModulesSortedAlternativeDumb(font1) : moduleManager.getObjects()) {
            if (!module.isToggled())
                continue;
            ModuleData moduleData = module.getModuleData();

            String name = moduleData.getName();
            if (alternativeNames.equalsIgnoreCase("Memes") && moduleData.hasOtherName())
                name = moduleData.getOtherName();
            else if (alternativeNames.equalsIgnoreCase("Orialeng") && moduleData.hasOtherNameDumb())
                name = moduleData.getOtherNameDumb();

            float xPos = scaledResolution.getScaledWidth() - font1.getWidth((name));
            font1.drawString(name, xPos - 10, y + 10, new Color(255, 255, 255, 200).getRGB());
//                ResourceLocation shadow = new ResourceLocation("dortware/shadow.png");
//                GuiUtils.drawImage(shadow, xPos - font1.getWidth(name) / 1.25F, y + 4, font1.getWidth(name) * 2, 25, new Color(0, 0, 0, 50).getRGB());
            y += 15;
        }

        int playerY = hud.tabGui.getValue() ? 235 : 135;
        int playerX = (scaledResolution.getScaledWidth() / 2) + 5;
        if (playerModel) {
            hud.drawEntityOnScreen(35, playerY, mc.thePlayer);
        }

        if (armorHud) {
            for (ItemStack itemStack : Lists.reverse(Arrays.asList(mc.thePlayer.inventory.armorInventory))) {
                if (itemStack != null) {
                    font2.drawStringWithShadow(String.valueOf(itemStack.getMaxDamage() - itemStack.getItemDamage()), playerX, scaledResolution.getScaledHeight() - 70, new Color(255, 255, 255, 200).getRGB());
                    mc.getRenderItem().func_175042_a(itemStack, playerX, scaledResolution.getScaledHeight() - 60);
                }
                playerX += 22;
            }
        }
    }
}
