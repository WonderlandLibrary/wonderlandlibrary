package club.cloverhook.gui.hud;

import club.cloverhook.utils.ColorCreator;
import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;

import club.cloverhook.Cloverhook;
import club.cloverhook.event.minecraft.KeyPressEvent;
import club.cloverhook.event.minecraft.PlayerUpdateEvent;
import club.cloverhook.event.minecraft.RenderOverlayEvent;
import club.cloverhook.gui.hud.element.Element;
import club.cloverhook.gui.hud.element.impl.ToggledList;
import club.cloverhook.utils.Draw;
import club.cloverhook.utils.Mafs;
import club.cloverhook.utils.font.FontRenderer;
import club.cloverhook.utils.font.Fonts;

import java.text.SimpleDateFormat;

/**
 * @author antja03
 */
public class Hud {

    private club.cloverhook.cheat.impl.visual.Hud hud;
    public final ArrayList<Element> elements = new ArrayList<>();

    protected Minecraft mc = Minecraft.getMinecraft();

    private double lastPosX = Double.NaN;
    private double lastPosZ = Double.NaN;
    private ArrayList<Double> distances = new ArrayList<Double>();

    public Hud() {
        this.elements.add(new ToggledList());
        Cloverhook.eventBus.register(this);
    }

    FontRenderer logo = new FontRenderer(Fonts.fontFromTTF(new ResourceLocation("client/ElliotSans-Bold.ttf"), 20, Font.PLAIN), true, true);

    public double getDistTraveled() {
        double total = 0;
        for (double d : distances) {
            total += d;
        }
        return total;
    }

    @Collect
    public void onPlayerUpdate(PlayerUpdateEvent e) {
        if (!Double.isNaN(lastPosX) && !Double.isNaN(lastPosZ)) {
            double differenceX = Math.abs(lastPosX - Minecraft.getMinecraft().thePlayer.posX);
            double differenceZ = Math.abs(lastPosZ - Minecraft.getMinecraft().thePlayer.posZ);
            double distance = Math.sqrt(differenceX * differenceX + differenceZ * differenceZ) * 2;

            distances.add(distance);
            if (distances.size() > 20)
                distances.remove(0);
        }

        lastPosX = Minecraft.getMinecraft().thePlayer.posX;
        lastPosZ = Minecraft.getMinecraft().thePlayer.posZ;
    }

    @Collect
    public void render(RenderOverlayEvent event) {

        if (hud == null) {
            hud = (club.cloverhook.cheat.impl.visual.Hud) Cloverhook.instance.cheatManager.getCheatRegistry().get("Overlay");
        }
        final ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());

        this.elements.forEach(element -> element.drawElement(false));

        if (hud == null) {
            hud = (club.cloverhook.cheat.impl.visual.Hud) Cloverhook.instance.cheatManager.getCheatRegistry().get("Overlay");
        }
        if (!Cloverhook.instance.cheatManager.isCheatEnabled("Overlay"))
            return;
        if (hud.prop_logo.getValue()) {
            if (hud.prop_logomode.getValue().get("Exhi")) {
                String pattern = "HH:mm";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                String date = simpleDateFormat.format(new Date());
                Fonts.verdanaN.drawStringWithShadow("C", 4, 3.7, ColorCreator.createRainbowFromOffset(3200, 1));
                Fonts.verdana2.drawStringWithShadow(EnumChatFormatting.GRAY + "loverhook " + "(" + date + ") ", 11.5, 4.15, 0xffffff);
            } else if (hud.prop_logomode.getValue().get("Image")) {
                Draw.drawImg(new ResourceLocation("client/gui/logo/128x128.png"), -2, -2, 64, 64);
            } else if (hud.prop_logomode.getValue().get("Ayylmao")) {
                Fonts.verdanaN.drawStringWithShadow("A", 4, 4.15, ColorCreator.createRainbowFromOffset(3200, 1));
                Fonts.verdanaN.drawStringWithShadow("y", 10, 4.15, ColorCreator.createRainbowFromOffset(3200, 1));
                Fonts.verdanaN.drawStringWithShadow("y", 16, 4.15, ColorCreator.createRainbowFromOffset(3200, 1));

            }
        }


        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());

        //inerts
        /*Draw.drawRectangle(1, resolution.getScaledHeight() / 2 - 205, Fonts.f16.getStringWidth("Blocks P/s: ") + 16, resolution.getScaledHeight() / 2 - 155, new

                Color(0, 0, 0, 90).

                getRGB());

        //right
        Draw.drawRectangle(Fonts.f16.getStringWidth("Blocks P/s: ") + 16.1, resolution.getScaledHeight() / 2 - 205, Fonts.f16.getStringWidth("Blocks P/s: ") + 16.8, resolution.getScaledHeight() / 2 - 155, new

                Color(0, 0, 0, 129).

                getRGB());
        //left
        Draw.drawRectangle(1, resolution.getScaledHeight() / 2 - 205, 1.8, resolution.getScaledHeight() / 2 - 155, new

                Color(0, 0, 0).

                getRGB());
        //top
        // Draw.drawRectangle(1.0, resolution.getScaledHeight() / 2 - 205.2, Fonts.f16.getStringWidth("Blocks P/s: ") + 16, resolution.getScaledHeight() / 2 - 205.9, new Color(0,0,0, 129).getRGB());

        //bottom
        Draw.drawRectangle(1.0, resolution.getScaledHeight() / 2 - 154, Fonts.f16.getStringWidth("Blocks P/s: ") + 16, resolution.getScaledHeight() / 2 - 155, new

                Color(0, 0, 0, 129).

                getRGB());

        //text
        Fonts.f16.drawStringWithShadow("Timer Speed: " + String.valueOf(Mafs.round(Minecraft.getMinecraft().timer.timerSpeed, 2)), 3.5, resolution.getScaledHeight() / 2 - 202, 0xffffffff);
        Fonts.f16.drawStringWithShadow("Blocks P/s: " + String.valueOf(Mafs.round(

                getDistTraveled(), 2)), 3.5, resolution.getScaledHeight() / 2 - 192, 0xffffffff);
        Fonts.f16.drawStringWithShadow("X: " + Mafs.round(Minecraft.getMinecraft().thePlayer.posX, 1), 3.5, resolution.getScaledHeight() / 2 - 182, 0xffffffff);
        Fonts.f16.drawStringWithShadow("Y: " + Mafs.round(Minecraft.getMinecraft().thePlayer.posY, 1), 3.5, resolution.getScaledHeight() / 2 - 172, 0xffffffff);
        Fonts.f16.drawStringWithShadow("Z: " + Mafs.round(Minecraft.getMinecraft().thePlayer.posZ, 1), 3.5, resolution.getScaledHeight() / 2 - 162, 0xffffffff);
    */
    }

    @Collect
    public void onKeyPressed(KeyPressEvent event) {
        this.elements.forEach(module -> module.onKeyPress(event.getKeyCode()));
    }

}
