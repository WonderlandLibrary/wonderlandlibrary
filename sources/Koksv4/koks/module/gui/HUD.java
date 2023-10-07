package koks.module.gui;

import koks.Koks;
import koks.api.event.Event;
import koks.api.font.Fonts;
import koks.api.manager.notification.Design;
import koks.api.manager.notification.NotificationManager;
import koks.api.manager.secret.SecretHandler;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;
import koks.api.utils.RenderUtil;
import koks.api.utils.Resolution;
import koks.api.utils.TimeHelper;
import koks.event.Render2DEvent;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "HUD", category = Module.Category.GUI, description = "Its display the HUD")
public class HUD extends Module {

    @Value(name = "Rainbow-Speed", minimum = 100, maximum = 10000)
    int rainbowSpeed = 6000;

    @Value(name = "Rainbow-Offset", minimum = 10, maximum = 1000)
    int rainbowOffset = 100;

    @Value(name = "Rainbow-Saturation", minimum = 0.1, maximum = 1)
    double rainbowSaturation = 0.6;

    @Value(name = "Show Visuals")
    boolean showVisuals = true;

    @Value(name = "Draw Notifications")
    boolean drawNotifications = true;

    @Value(name = "Design", modes = {"Turm"})
    String design = "Turm";

    int currentCat;

    public final TimeHelper catTimeHelper = new TimeHelper();
    public final TimeHelper timeHelper = new TimeHelper();
    double bps = 0;
    Color currentColor = new Color(255, 0, 0);
    int index = 0;

    public HUD() {
        setToggled(true);
    }

    @Override
    @Event.Info(priority = Event.Priority.EXTREME)
    public void onEvent(Event event) {
        if (event instanceof Render2DEvent) {

            /*Object[] argb = getAddressableRainbow(index, currentColor, 5, new Color(255,0,0), new Color(0,255,0), new Color(0, 0, 255));
            currentColor = (Color) argb[0];
            index = (int) argb[1];
            Color color = currentColor;*/

            GL11.glPushMatrix();
            final Resolution resolution = Resolution.getResolution();
            final RenderUtil renderUtil = RenderUtil.getInstance();
            int currentOffset = 0;
            int y = 0;

            //ATERO RENDERING
            /*
            int xP = 80;
            int yP = 80;
            int widthP = (int) ((int) Fonts.officinaSansBook55.getStringWidth("atero") * 0.8);
            int heightP = (int) Fonts.officinaSansBook55.getStringHeight("atero") / 2;
            Fonts.officinaSansBook55.drawString("atero", xP, yP, new Color(0x292929), false);
            renderUtil.drawPicture((int) (xP + 5), (int) (yP + Fonts.officinaSansBook55.getStringHeight("atero") * 0.8), widthP, heightP, new ResourceLocation("atero/hud/arrow.png"));*/

            final ArrayList<Module> toggled = new ArrayList<>();
            ModuleRegistry.getModules().forEach(module -> {
                if (module.isToggled() && (showVisuals || module.getCategory() != Category.VISUAL) && module.getCategory() != Category.GUI)
                    toggled.add(module);
            });

            final double motionX = getPlayer().posX - getPlayer().prevPosX;
            final double motionZ = getPlayer().posZ - getPlayer().prevPosZ;
            double speed = Math.sqrt(motionX * motionX + motionZ * motionZ) * 20 * getTimer().timerSpeed;
            speed = Math.round(speed * 10);
            speed = speed / 10;

            String bps = "Bps: ";

            for (int i = 0; i < bps.length(); i++) {
                final char character = bps.charAt(i);
                Fonts.arial18.drawString(character + "", 11 + Fonts.arial18.getStringWidth(bps.substring(0, i)), 28, getRainbow(100 * (i + 1), 3000, 0.6f, 1), true);
            }

            Fonts.arial18.drawString(speed + "", 11 + Fonts.arial18.getStringWidth(bps), 28, new Color(-1), true);

            if (SecretHandler.catMode) {
                renderUtil.drawPicture(0, 35, 100, 100, new ResourceLocation("koks/textures/cat/" + currentCat + ".png"));

                if (catTimeHelper.hasReached(30)) {
                    currentCat++;
                    catTimeHelper.reset();
                }
                if (currentCat > 28)
                    currentCat = 0;
            }

            toggled.sort(Comparator.comparingDouble(module -> -Fonts.arial18.getStringWidth(module.getName()) * (Koks.getKoks().isApril ? -1 : 1)));

            final float size = 3;
            final float depth = 1F;
            final int posX = (int) (depth + size) + 2;
            final int posY = ((int) (depth + size) + 2) * -1;

            for (int i = 0; i < toggled.size(); i++) {
                final Module module = toggled.get(i);
                if (module.isToggled()) {
                    final String displayName = module.getName();
                    final float height = Fonts.arial18.getStringHeight(displayName);

                    final int l = (int) ((float) (resolution.getWidth() - Fonts.arial18.getStringWidth(displayName) - 1 - posX) - size - depth);
                    final int r = (int) (resolution.getWidth() - posX + size + depth);
                    final double distance = r - l;

                    if (i == 0) {
                        int lIndex = 1;

                        for (double left = l; left < r; left += distance / displayName.length()) {
                            double nextStep = left + distance / displayName.length();
                            nextStep = MathHelper.clamp_double(nextStep + depth, left, r);
                            Gui.drawRect((int) left, (int) (y - depth - posY), (int) (nextStep), (int) (y - posY), getRainbow(rainbowOffset * (lIndex + (toggled.size() - i) + 1), rainbowSpeed, (float) rainbowSaturation, 1).getRGB());
                            lIndex++;
                        }
                    }

                    final float bottom = y + height + depth + 1.5F - posY;
                    if (i + 1 < toggled.size()) {
                        final Module nextModule = toggled.get(i + 1);
                        final float difference = (int) (Fonts.arial18.getStringWidth(displayName) - Fonts.arial18.getStringWidth(nextModule.getName()));
                        Gui.drawRect((int) (((float) (resolution.getWidth() - Fonts.arial18.getStringWidth(displayName) - 1 - posX) - size - depth)), (int) (y + height + 1.5F - posY), (int) ((resolution.getWidth() - Fonts.arial18.getStringWidth(displayName) - 1 - posX) + difference - size), (int) bottom, getRainbow(rainbowOffset * ((toggled.size() - i) + 1), rainbowSpeed, (float) rainbowSaturation, 1).getRGB());
                    } else {
                        int lIndex = 1;
                        for (double left = l; left < r; left += distance / displayName.length()) {
                            Gui.drawRect((int) left, (int) (y + height + 1.5f - posY), (int) (left + distance / displayName.length()), (int) bottom, getRainbow(rainbowOffset * ((lIndex + 1) + (toggled.size() - i) + 1), rainbowSpeed, (float) rainbowSaturation, 1).getRGB());
                            lIndex++;
                        }
                    }

                    Gui.drawRect((int) (resolution.getWidth() - posX + size), (int) (y - posY), (int) (resolution.getWidth() - posX + size + depth), (int) (y + height + 2.5F - posY), getRainbow(rainbowOffset * (displayName.length() + (toggled.size() - i) + 2), rainbowSpeed, (float) rainbowSaturation, 1).getRGB());

                    Gui.drawRect((int) ((resolution.getWidth() - Fonts.arial18.getStringWidth(displayName) - 1 - posX) - size), (int) (y - posY - (i == 0 ? 0 : 1)), (int) (resolution.getWidth() - posX + size), (int) (y + Fonts.arial18.getStringHeight(displayName) + 2 - 1 - posY), Integer.MIN_VALUE);
                    Gui.drawRect((int) ((resolution.getWidth() - Fonts.arial18.getStringWidth(displayName) - 1 - posX) - size - depth), y - posY - 1, (int) ((resolution.getWidth() - Fonts.arial18.getStringWidth(displayName) - 1 - posX) - size), (int) (y + height + 2.5F - posY), getRainbow(rainbowOffset * ((toggled.size() - i) + 1), rainbowSpeed, (float) rainbowSaturation, 1).getRGB());

                    for (int c = 0; c < displayName.length(); c++) {
                        final char character = displayName.charAt(c);
                        Fonts.arial18.drawString(character + "", (float) (resolution.getWidth() - Fonts.arial18.getStringWidth(displayName) - 1 - posX) + Fonts.arial18.getStringWidth(displayName.substring(0, c)), y + 1.5f - posY, getRainbow(rainbowOffset * (c + (toggled.size() - i) + 1), rainbowSpeed, (float) rainbowSaturation, 1), true);
                    }

                    y += Fonts.arial18.getStringHeight(displayName) + 2;
                    currentOffset += 100;
                }
            }
            GL11.glColor3d(1, 1, 1);

            final String version = "v" + Koks.version;

            Fonts.ralewayRegular35.drawString(String.valueOf(Koks.name.charAt(0)), 10, 10, getRainbow(0, 3000, 0.6f, 1), true);
            Fonts.ralewayRegular30.drawString(Koks.name.substring(1) + (Koks.name.endsWith("s") ? "" : "s") + "ense", 10 + Fonts.ralewayRegular35.getStringWidth(String.valueOf(Koks.name.charAt(0))), 12, new Color(-1), true);

            for (int i = 0; i < version.length(); i++) {
                Fonts.arial18.drawString(String.valueOf(version.charAt(i)), 10 + Fonts.ralewayRegular35.getStringWidth(String.valueOf(Koks.name.charAt(0))) + Fonts.ralewayRegular30.getStringWidth(Koks.name.substring(1) + (Koks.name.endsWith("s") ? "" : "s") + "ense") + Fonts.arial18.getStringWidth(version.substring(0, i)) - Fonts.arial18.getStringWidth(version), 8, getRainbow(100 * (i + 1), 3000, 0.6f, 1), true);
            }
            GL11.glPopMatrix();
            if (drawNotifications) {
                NotificationManager.getInstance().drawNotifications(Design.valueOf(design.toUpperCase()));
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
