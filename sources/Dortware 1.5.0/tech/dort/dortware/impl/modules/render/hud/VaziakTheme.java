package tech.dort.dortware.impl.modules.render.hud;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.RandomUtils;
import skidmonke.Client;
import tech.dort.dortware.api.font.CustomFontRenderer;
import tech.dort.dortware.api.module.Module;
import tech.dort.dortware.api.module.ModuleData;
import tech.dort.dortware.impl.events.RenderHUDEvent;
import tech.dort.dortware.impl.gui.click.GuiUtils;
import tech.dort.dortware.impl.managers.ModuleManager;
import tech.dort.dortware.impl.modules.render.Hud;
import tech.dort.dortware.impl.utils.render.ColorUtil;

import java.awt.*;
import java.util.Random;

public class VaziakTheme extends Theme {

    public VaziakTheme(Module module) {
        super(module);
    }

    @Override
    public void render(RenderHUDEvent event) {
        if (mc.gameSettings.showDebugInfo)
            return;

        final Hud hud = Client.INSTANCE.getModuleManager().get(Hud.class);
        final Boolean watermark = hud.watermark.getValue();
        final String alternativeNames = hud.alternativeNameMode.getValue().getDisplayName();
        final Boolean background = hud.background.getValue();
        final Boolean lowercase = hud.lowercase.getValue();
        final int alpha = hud.alpha.getCastedValue().intValue();
        int y = 2;
        final CustomFontRenderer font1 = Client.INSTANCE.getFontManager().getFont("Chat").getRenderer();
        final CustomFontRenderer autism = Client.INSTANCE.getFontManager().getFont("autism").getRenderer();
        Client.INSTANCE.getModuleManager().sort(mc.fontRendererObj);

        ScaledResolution scaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

        if (watermark) {
            GlStateManager.translate(4, 4, 0);
            GlStateManager.scale(2, 2, 1);
            GlStateManager.translate(-4, -4, 0);
            font1.drawStringWithShadow("m\247ffw (my face when) i sea وقمق gay demon spider \uD83D\uDE33\uD83D\uDE33\uD83D\uDE21\uD83D\uDE21\uD83D\uDE21\uD83D\uDE21بتلنلني بهيني \uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE2D", 3, mc.thePlayer.ticksExisted % 6 != 0 ? 60 : 70, ColorUtil.astolfoColors(-6000, 0));
            GlStateManager.translate(4, 4, 0);
            GlStateManager.scale(0.5, 0.5, 1);
            GlStateManager.translate(-4, -4, 0);
            font1.drawString(lowercase ? "server no responding (haram)!" : "server no reSponding (HARAM)!", RandomUtils.nextInt(150, 350), RandomUtils.nextInt(150, 500), new Color(RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0.5F, 1F)).getRGB());
            font1.drawString(lowercase ? "server no responding (haram)!" : "server no reSponding (HARAM)!", RandomUtils.nextInt(150, 350), RandomUtils.nextInt(150, 500), new Color(RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0.5F, 1F)).getRGB());
            font1.drawString(lowercase ? "server no responding (haram)!" : "server no reSponding (HARAM)!", RandomUtils.nextInt(150, 350), RandomUtils.nextInt(150, 500), new Color(RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0.5F, 1F)).getRGB());
            font1.drawString(lowercase ? "server no responding (haram)!" : "server no reSponding (HARAM)!", RandomUtils.nextInt(150, 350), RandomUtils.nextInt(150, 500), new Color(RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0.5F, 1F)).getRGB());
            font1.drawString(lowercase ? "server no responding (haram)!" : "server no reSponding (HARAM)!", RandomUtils.nextInt(150, 350), RandomUtils.nextInt(150, 500), new Color(RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0.5F, 1F)).getRGB());
            font1.drawString(lowercase ? "server no responding (haram)!" : "server no reSponding (HARAM)!", RandomUtils.nextInt(150, 350), RandomUtils.nextInt(150, 500), new Color(RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0.5F, 1F)).getRGB());
            font1.drawString(lowercase ? "server no responding (haram)!" : "server no reSponding (HARAM)!", RandomUtils.nextInt(150, 350), RandomUtils.nextInt(150, 500), new Color(RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0.5F, 1F)).getRGB());
            font1.drawString(lowercase ? "server no responding (haram)!" : "server no reSponding (HARAM)!", RandomUtils.nextInt(150, 350), RandomUtils.nextInt(150, 500), new Color(RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0.5F, 1F)).getRGB());
            font1.drawString(lowercase ? "server no responding (haram)!" : "server no reSponding (HARAM)!", RandomUtils.nextInt(150, 350), RandomUtils.nextInt(150, 500), new Color(RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0.5F, 1F)).getRGB());
            font1.drawString(lowercase ? "server no responding (haram)!" : "server no reSponding (HARAM)!", RandomUtils.nextInt(150, 350), RandomUtils.nextInt(150, 500), new Color(RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0.5F, 1F)).getRGB());
            font1.drawString(lowercase ? "server no responding (haram)!" : "server no reSponding (HARAM)!", RandomUtils.nextInt(150, 350), RandomUtils.nextInt(150, 500), new Color(RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0.5F, 1F)).getRGB());
            font1.drawString(lowercase ? "server no responding (haram)!" : "server no reSponding (HARAM)!", RandomUtils.nextInt(150, 350), RandomUtils.nextInt(150, 500), new Color(RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0.5F, 1F)).getRGB());
            font1.drawString(lowercase ? "server no responding (haram)!" : "server no reSponding (HARAM)!", RandomUtils.nextInt(150, 350), RandomUtils.nextInt(150, 500), new Color(RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0.5F, 1F)).getRGB());
            font1.drawString(lowercase ? "server no responding (haram)!" : "server no reSponding (HARAM)!", RandomUtils.nextInt(150, 350), RandomUtils.nextInt(150, 500), new Color(RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0.5F, 1F)).getRGB());
            font1.drawString(lowercase ? "server no responding (haram)!" : "server no reSponding (HARAM)!", RandomUtils.nextInt(150, 350), RandomUtils.nextInt(150, 500), new Color(RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0.5F, 1F)).getRGB());
            font1.drawString(lowercase ? "server no responding (haram)!" : "server no reSponding (HARAM)!", RandomUtils.nextInt(150, 350), RandomUtils.nextInt(150, 500), new Color(RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0.5F, 1F)).getRGB());
            font1.drawString(lowercase ? "server no responding (haram)!" : "server no reSponding (HARAM)!", RandomUtils.nextInt(150, 350), RandomUtils.nextInt(150, 500), new Color(RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0.5F, 1F)).getRGB());
            font1.drawString(lowercase ? "server no responding (haram)!" : "server no reSponding (HARAM)!", RandomUtils.nextInt(150, 350), RandomUtils.nextInt(150, 500), new Color(RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0.5F, 1F)).getRGB());
            font1.drawString(lowercase ? "server no responding (haram)!" : "server no reSponding (HARAM)!", RandomUtils.nextInt(150, 350), RandomUtils.nextInt(150, 500), new Color(RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0.5F, 1F)).getRGB());
            font1.drawString(lowercase ? "server no responding (haram)!" : "server no reSponding (HARAM)!", RandomUtils.nextInt(150, 350), RandomUtils.nextInt(150, 500), new Color(RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0.5F, 1F)).getRGB());
            font1.drawString(lowercase ? "server no responding (haram)!" : "server no reSponding (HARAM)!", RandomUtils.nextInt(150, 350), RandomUtils.nextInt(150, 500), new Color(RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0.5F, 1F)).getRGB());
            font1.drawString(lowercase ? "server no responding (haram)!" : "server no reSponding (HARAM)!", RandomUtils.nextInt(150, 350), RandomUtils.nextInt(150, 500), new Color(RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0.5F, 1F)).getRGB());
            font1.drawString(lowercase ? "server no responding (haram)!" : "server no reSponding (HARAM)!", RandomUtils.nextInt(150, 350), RandomUtils.nextInt(150, 500), new Color(RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0.5F, 1F)).getRGB());
            font1.drawString(lowercase ? "server no responding (haram)!" : "server no reSponding (HARAM)!", RandomUtils.nextInt(150, 350), RandomUtils.nextInt(150, 500), new Color(RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0.5F, 1F)).getRGB());
        }

//        mc.gameSettings.limitFramerate = (int) 111111224343474L;

        Gui.drawRect(0, 0, 165, 10, Color.WHITE.getRGB());
        ResourceLocation camel = new ResourceLocation("dortware/camel.png");
        GuiUtils.drawImage(camel, 256, 256, 128, 128, -1);
        ResourceLocation banks = new ResourceLocation("dortware/banks.png");
        GuiUtils.drawImage(banks, 350, 100, 500, 250, -1);
        GuiUtils.drawImage(new ResourceLocation("dortware/troll.jpg"), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), -1);
        GuiUtils.drawImage(new ResourceLocation("dortware/troll.jpg"), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), -1);
        GuiUtils.drawImage(new ResourceLocation("dortware/troll.jpg"), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), -1);
        GuiUtils.drawImage(new ResourceLocation("dortware/troll.jpg"), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), -1);
        GuiUtils.drawImage(new ResourceLocation("dortware/troll.jpg"), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), -1);
        GuiUtils.drawImage(new ResourceLocation("dortware/troll.jpg"), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), -1);
        GuiUtils.drawImage(new ResourceLocation("dortware/troll.jpg"), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), -1);
        GuiUtils.drawImage(new ResourceLocation("dortware/troll.jpg"), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), -1);
        GuiUtils.drawImage(new ResourceLocation("dortware/troll.jpg"), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), -1);
        Gui.drawRect(455, 410, 565, 780, Color.WHITE.getRGB());
        Gui.drawRect(0, 0, 50000, 50000, new Color(RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 65)).getRGB());
        mc.fontRendererObj.drawString(lowercase ? "\2470\247Lunregistered hypercam 2" : "\2470\247LUnregistered HyperCam 2", 2, 0, Color.BLACK.getRGB());
        mc.fontRendererObj.drawString(lowercase ? "\2470\247Lunregistered hypercam 2" : "\2470\247LUnregistered HyperCam 2", new Random().nextInt(500),  new Random().nextInt(500), Color.BLACK.getRGB());
        mc.fontRendererObj.drawString(lowercase ? "\2470\247Lunregistered hypercam 2" : "\2470\247LUnregistered HyperCam 2", new Random().nextInt(500),  new Random().nextInt(500), Color.BLACK.getRGB());
        mc.fontRendererObj.drawString(lowercase ? "\2470\247Lunregistered hypercam 2" : "\2470\247LUnregistered HyperCam 2", new Random().nextInt(500),  new Random().nextInt(500), Color.BLACK.getRGB());
        mc.fontRendererObj.drawString(lowercase ? "\2470\247Lunregistered hypercam 2" : "\2470\247LUnregistered HyperCam 2", new Random().nextInt(500),  new Random().nextInt(500), Color.BLACK.getRGB());
        mc.fontRendererObj.drawString(lowercase ? "\2470\247Lunregistered hypercam 2" : "\2470\247LUnregistered HyperCam 2", new Random().nextInt(500),  new Random().nextInt(500), Color.BLACK.getRGB());
        mc.fontRendererObj.drawString(lowercase ? "\2470\247Lunregistered hypercam 2" : "\2470\247LUnregistered HyperCam 2", new Random().nextInt(500),  new Random().nextInt(500), Color.BLACK.getRGB());
        mc.fontRendererObj.drawString(lowercase ? "\2470\247Lunregistered hypercam 2" : "\2470\247LUnregistered HyperCam 2", new Random().nextInt(500),  new Random().nextInt(500), Color.BLACK.getRGB());
        mc.fontRendererObj.drawString(lowercase ? "\2470\247Lunregistered hypercam 2" : "\2470\247LUnregistered HyperCam 2", new Random().nextInt(500),  new Random().nextInt(500), Color.BLACK.getRGB());
        mc.fontRendererObj.drawString(lowercase ? "\2470\247Lunregistered hypercam 2" : "\2470\247LUnregistered HyperCam 2", new Random().nextInt(500),  new Random().nextInt(500), Color.BLACK.getRGB());
        mc.fontRendererObj.drawString(lowercase ? "\2470\247Lunregistered hypercam 2" : "\2470\247LUnregistered HyperCam 2", new Random().nextInt(500),  new Random().nextInt(500), Color.BLACK.getRGB());
        mc.fontRendererObj.drawString(lowercase ? "\2470\247Lunregistered hypercam 2" : "\2470\247LUnregistered HyperCam 2", new Random().nextInt(500),  new Random().nextInt(500), Color.BLACK.getRGB());
        mc.fontRendererObj.drawString(lowercase ? "\2470\247Lunregistered hypercam 2" : "\2470\247LUnregistered HyperCam 2", new Random().nextInt(500),  new Random().nextInt(500), Color.BLACK.getRGB());
        mc.fontRendererObj.drawString(lowercase ? "\2470\247Lunregistered hypercam 2" : "\2470\247LUnregistered HyperCam 2", new Random().nextInt(500),  new Random().nextInt(500), Color.BLACK.getRGB());
        mc.fontRendererObj.drawString(lowercase ? "\2470\247Lunregistered hypercam 2" : "\2470\247LUnregistered HyperCam 2", new Random().nextInt(500),  new Random().nextInt(500), Color.BLACK.getRGB());
        mc.fontRendererObj.drawString(lowercase ? "\2470\247Lunregistered hypercam 2" : "\2470\247LUnregistered HyperCam 2", new Random().nextInt(500),  new Random().nextInt(500), Color.BLACK.getRGB());
        mc.fontRendererObj.drawString(lowercase ? "\2470\247Lunregistered hypercam 2" : "\2470\247LUnregistered HyperCam 2", new Random().nextInt(500),  new Random().nextInt(500), Color.BLACK.getRGB());

        final ModuleManager moduleManager = Client.INSTANCE.getModuleManager();
        for (Module module : alternativeNames.equalsIgnoreCase("Memes") ? moduleManager.getModulesSortedAlternativeDumb(font1) : alternativeNames.equalsIgnoreCase("Orialeng") ? moduleManager.getModulesSortedAlternativeDumb(font1) : moduleManager.getObjects()) {
            if (true || !true || true != false && false != true && new Boolean(true) instanceof Boolean && true == false && false == false && new Object() instanceof Object) {
                ModuleData moduleData = module.getModuleData();
                String data = module.getModuleData().getName().equals("Flight") ? " \2477Unblink mode" : "\2477 Unblik 1 paket";
                if (module.getSuffix()
                        != null) {
                    data = lowercase ? module.getSuffix().toLowerCase() : module.getSuffix();
                }

                if (lowercase) {
                    data = data.toLowerCase();
                }

                String name = lowercase ? moduleData.getName().toLowerCase() : moduleData.getName();
                if (alternativeNames.equalsIgnoreCase("Memes") && moduleData.hasOtherName())
                    name = lowercase ? moduleData.getOtherName().toLowerCase() : moduleData.getOtherName();
                else if (alternativeNames.equalsIgnoreCase("Orialeng") && moduleData.hasOtherNameDumb())
                    name = lowercase ? moduleData.getOtherNameDumb().toLowerCase() : moduleData.getOtherNameDumb();

                float xPos = scaledResolution.getScaledWidth() - autism.getWidth((name + data));
                if (background) {
                    Gui.drawRect(xPos - 5, y == 2 ? 0 : y, scaledResolution.getScaledWidth(), y + 11, new Color(0, 0, 0, alpha).getRGB());
                }
                autism.drawStringWithShadow("\247k" + name, xPos - 5, y, ColorUtil.astolfoColors(2, y));
                autism.drawStringWithShadow("\247k" + name, xPos - 74, y, ColorUtil.astolfoColors(2, y));
                autism.drawStringWithShadow("\247k" + name, xPos - 3, y, ColorUtil.astolfoColors(2, y));
                autism.drawStringWithShadow("\247k" + name, xPos - 2, y, ColorUtil.astolfoColors(2, y));
                y += 11;
                autism.drawStringWithShadow(name + data, xPos - 12, y, ColorUtil.astolfoColors(2, y));
                autism.drawStringWithShadow(name + data, xPos - 40, y, ColorUtil.astolfoColors(2, y));
                autism.drawStringWithShadow(name + data, new Random().nextInt(500),  new Random().nextInt(500), ColorUtil.astolfoColors(2, y));
                autism.drawStringWithShadow(name + data, new Random().nextInt(500),  new Random().nextInt(500), ColorUtil.astolfoColors(2, y));
                autism.drawStringWithShadow(name + data, new Random().nextInt(500),  new Random().nextInt(500), ColorUtil.astolfoColors(2, y));
                autism.drawStringWithShadow(name + data, new Random().nextInt(500),  new Random().nextInt(500), ColorUtil.astolfoColors(2, y));
                autism.drawStringWithShadow(name + data, new Random().nextInt(500),  new Random().nextInt(500), ColorUtil.astolfoColors(2, y));
                autism.drawStringWithShadow(name + data, xPos - 6, y, ColorUtil.astolfoColors(2, y));
                y += 11;
            }
        }
    }
}
