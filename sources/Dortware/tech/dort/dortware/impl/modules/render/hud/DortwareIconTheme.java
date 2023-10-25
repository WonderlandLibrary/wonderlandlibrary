package tech.dort.dortware.impl.modules.render.hud;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import skidmonke.Client;
import skidmonke.Minecraft;
import tech.dort.dortware.api.font.CustomFontRenderer;
import tech.dort.dortware.api.module.Module;
import tech.dort.dortware.api.module.ModuleData;
import tech.dort.dortware.impl.events.RenderHUDEvent;
import tech.dort.dortware.impl.gui.click.GuiUtils;
import tech.dort.dortware.impl.managers.ModuleManager;
import tech.dort.dortware.impl.modules.render.Hud;
import tech.dort.dortware.impl.utils.render.ColorUtil;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Arrays;

public class DortwareIconTheme extends Theme {
    public DortwareIconTheme(Module module) {
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
        final Boolean rainbow = hud.rainbow.getValue();
        final Boolean background = hud.background.getValue();
        final Boolean fps = hud.fpsCounter.getValue();
        final Boolean bps = hud.bpsCounter.getValue();
        final Boolean ping = hud.ping.getValue();
        final Boolean lowercase = hud.lowercase.getValue();
        final float red = hud.red.getCastedValue().floatValue();
        final float green = hud.green.getCastedValue().floatValue();
        final float blue = hud.blue.getCastedValue().floatValue();
        final int alpha = hud.alpha.getCastedValue().intValue();
        final int spacing = hud.spacing.getValue().intValue();
        int y = 2;
        final CustomFontRenderer font1 = Client.INSTANCE.getFontManager().getFont("Chat").getRenderer();
        final CustomFontRenderer font2 = Client.INSTANCE.getFontManager().getFont("Small1").getRenderer();
        Client.INSTANCE.getModuleManager().sort(font1);

        Color color = new Color(red / 255.0f, green / 255.0f, blue / 255.0f);
        ScaledResolution scaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        ResourceLocation clown = new ResourceLocation("dortware/clown.png");
        if (watermark) {
            GuiUtils.drawImage(clown, 8, 8, 48, 48, -1);
        }

        NetworkPlayerInfo info = mc.getNetHandler().func_175102_a(mc.thePlayer.getUniqueID());
        double xz = (Math.hypot(mc.thePlayer.posX - mc.thePlayer.prevPosX, mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * mc.timer.timerSpeed) * 20;
        DecimalFormat bpsFormat = new DecimalFormat("#.##");
        try {
            font2.drawStringWithShadow(bps ? lowercase ? "bps: " + bpsFormat.format(xz) : "BPS: " + bpsFormat.format(xz) : "", 2, !fps && !ping ? new ScaledResolution(mc).getScaledHeight() - (mc.currentScreen instanceof GuiChat ? 24 : 10) : new ScaledResolution(mc).getScaledHeight() - (mc.currentScreen instanceof GuiChat ? 34 : 22), rainbow ? ColorUtil.getRainbow(-6000, 0) : color.getRGB());
            font2.drawStringWithShadow(ping ? lowercase ? "ping: " + info.getResponseTime() : "Ping: " + info.getResponseTime() : "", fps ? mc.fontRendererObj.getStringWidth("FPS: " + Minecraft.debugFPS) - 2 : 2, new ScaledResolution(mc).getScaledHeight() - (mc.currentScreen instanceof GuiChat ? 24 : 10), rainbow ? ColorUtil.getRainbow(-6000, 0) : color.getRGB());
            font2.drawStringWithShadow(fps ? lowercase ? "fps: " + Minecraft.debugFPS : "FPS: " + Minecraft.debugFPS : "", 2, new ScaledResolution(mc).getScaledHeight() - (mc.currentScreen instanceof GuiChat ? 24 : 10), rainbow ? ColorUtil.getRainbow(-6000, 0) : color.getRGB());
        } catch (Exception ignored) {
        }

        final ModuleManager moduleManager = Client.INSTANCE.getModuleManager();
        for (Module module : alternativeNames.equalsIgnoreCase("Memes") ? moduleManager.getModulesSortedAlternative(font1) : alternativeNames.equalsIgnoreCase("Orialeng") ? moduleManager.getModulesSortedAlternativeDumb(font1) : moduleManager.getObjects()) {
            int yes = rainbow ? ColorUtil.getRainbow(-6000, y * 6) : color.getRGB();
            if (!module.isToggled() || module.getModuleData().getName().equals("HUD"))
                continue;
            ModuleData moduleData = module.getModuleData();
            String data = "";
            if (module.getSuffix() != null) {
                data = module.getSuffix();
            }

            String name = moduleData.getName();
            if (alternativeNames.equalsIgnoreCase("Memes") && moduleData.hasOtherName())
                name = moduleData.getOtherName();
            else if (alternativeNames.equalsIgnoreCase("Orialeng") && moduleData.hasOtherNameDumb())
                name = moduleData.getOtherNameDumb();

            float xPos = scaledResolution.getScaledWidth() - font1.getWidth((name + data));
            if (background) { // background by aidan and auth
                switch (hud.lineMode.getValue()) {
                    case RIGHT:
                        Gui.drawRect(xPos - 8, y == 2 ? 0 : y, scaledResolution.getScaledWidth(), y + spacing, new Color(0, 0, 0, alpha).getRGB());
                        Gui.drawRect(scaledResolution.getScaledWidth() - 2, y == 2 ? 0 : y, scaledResolution.getScaledWidth(), y + spacing, yes);
                        break;
                    case LEFT:
                        Gui.drawRect(xPos - 8, y == 2 ? 0 : y, scaledResolution.getScaledWidth(), y + spacing, new Color(0, 0, 0, alpha).getRGB());
                        Gui.drawRect(scaledResolution.getScaledWidth() - font1.getWidth(name + data) - 8, y == 2 ? 0 : y, scaledResolution.getScaledWidth() - font1.getWidth(name + data) - 6, y + spacing, yes);
                        break;
                    case BOTH:
                        Gui.drawRect(xPos - 8, y == 2 ? 0 : y, scaledResolution.getScaledWidth(), y + spacing, new Color(0, 0, 0, alpha).getRGB());
                        Gui.drawRect(scaledResolution.getScaledWidth() - font1.getWidth(name + data) - 8, y == 2 ? 0 : y, scaledResolution.getScaledWidth() - font1.getWidth(name + data) - 6, y + spacing, yes);
                        Gui.drawRect(scaledResolution.getScaledWidth() - 2, y == 2 ? 0 : y, scaledResolution.getScaledWidth(), y + spacing, yes);
                        break;
                    case NONE:
                        Gui.drawRect(xPos - 8, y == 2 ? 0 : y, scaledResolution.getScaledWidth(), y + spacing, new Color(0, 0, 0, alpha).getRGB());
                        break;
                }
            }
            font1.drawStringWithShadow(name + data, xPos - 4, y, yes);
            y += spacing;
        }

        int playerY = hud.tabGui.getValue() ? 235 : 135;
        int playerX = (scaledResolution.getScaledWidth() / 2) + 5;
        if (playerModel) {
            hud.drawEntityOnScreen(35, playerY, mc.thePlayer);
        }

        if (armorHud) {
            for (ItemStack itemStack : Lists.reverse(Arrays.asList(mc.thePlayer.inventory.armorInventory))) {
                if (itemStack != null) {
                    font2.drawStringWithShadow(String.valueOf(itemStack.getMaxDamage() - itemStack.getItemDamage()), playerX, scaledResolution.getScaledHeight() - 70, rainbow ? ColorUtil.getRainbow(-6000, 0) : color.getRGB());
                    mc.getRenderItem().func_175042_a(itemStack, playerX, scaledResolution.getScaledHeight() - 60);
                }
                playerX += 22;
            }
        }
    }
}
