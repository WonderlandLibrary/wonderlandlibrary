package dev.rise.ui.ingame;

import dev.rise.Rise;
import dev.rise.font.CustomFont;
import dev.rise.font.fontrenderer.TTFFontRenderer;
import dev.rise.module.Module;
import dev.rise.module.enums.Category;
import dev.rise.module.impl.player.Stealer;
import dev.rise.module.impl.render.Interface;
import dev.rise.setting.Setting;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.math.MathUtil;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.player.PlayerUtil;
import dev.rise.util.render.ColorUtil;
import dev.rise.util.render.KeystrokeUtil;
import dev.rise.util.render.RenderUtil;
import dev.rise.util.render.theme.ThemeType;
import dev.rise.util.render.theme.ThemeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


/**
 * This class is used to render in-game gui.
 * <p>
 * This class will extend of the default in-game gui
 * yet will have more features as we add our own to the already existing gui.
 *
 * @author Tecnio
 * @since 02/12/2021
 */

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION, Strategy.NAME_REMAPPING, Strategy.REFERENCE_OBFUSCATION, Strategy.DEBUG_STRIPPING})
public final class IngameGUI extends GuiIngame {

    public static final KeystrokeUtil forward = new KeystrokeUtil();
    public static final KeystrokeUtil backward = new KeystrokeUtil();
    public static final KeystrokeUtil left = new KeystrokeUtil();
    public static final KeystrokeUtil right = new KeystrokeUtil();
    public static final KeystrokeUtil space = new KeystrokeUtil();
    private final static TTFFontRenderer comfortaa = CustomFont.FONT_MANAGER.getFont("Comfortaa 18");
    private final static TTFFontRenderer comfortaaBig = CustomFont.FONT_MANAGER.getFont("Comfortaa 32");
    private final static TTFFontRenderer skeet = CustomFont.FONT_MANAGER.getFont("SkeetBold 12");
    private final static TTFFontRenderer skeetBig = CustomFont.FONT_MANAGER.getFont("Skeet 18");
    private final static TTFFontRenderer oneTap = CustomFont.FONT_MANAGER.getFont("Skeet 16");
    private final static TTFFontRenderer museo = CustomFont.FONT_MANAGER.getFont("Museo 20");
    private final static TTFFontRenderer eaves = CustomFont.FONT_MANAGER.getFont("Eaves 18");
    public static float ticks, ticksSinceClickgui;
    public static float positionOfLastModule;
    public static String key, lastKey;
    static List<Object> modules;
    private final Minecraft mc = Minecraft.getMinecraft();
    private final TimeUtil timer2 = new TimeUtil();
    private final Vec3 lastPos = new Vec3(0, 0, 0);
    private final double bpsSpeed = 0;
    private final TimeUtil timer = new TimeUtil();


    public IngameGUI(final Minecraft mcIn) {
        super(mcIn);
    }

    public static void onFadeOutline() {
        final ModeSetting setting = ((ModeSetting) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getSetting("Interface", "Theme")));
        final String mode = setting.getMode();

        if (modules == null || Minecraft.getMinecraft().gameSettings.showDebugInfo)
            return;

        for (final Object m : modules) {
            final Category c = m instanceof Module ? ((Module) m).getModuleInfo().category() : Category.SCRIPTS;

            final Setting render = Rise.INSTANCE.getModuleManager().getSetting("Interface", "Show Render Modules on List");

            if (render == null) return;
            if (!(render instanceof BooleanSetting)) return;

            final BooleanSetting showRender = ((BooleanSetting) render);

            final int offsetY = 2;
            final int offsetX = 1;

            if (c != Category.RENDER || showRender.isEnabled()) {
                assert m instanceof Module;

                if (mode.equals("Rise Blend") || mode.equals("Rise Christmas") || mode.equals("Rice")) {
                    final double stringWidth = comfortaa.getWidth(((Module) m).getModuleInfo().name());
                    RenderUtil.rect(((Module) m).renderX - offsetX, ((Module) m).renderY - offsetY + 1 + 0.1, stringWidth + offsetX * 1.5 + 0.5, comfortaa.getHeight() + offsetY + 0.3, new Color(25, 25, 25, 245));
                }

                if (mode.equals("Never Lose")) {
                    final double stringWidth = comfortaa.getWidth(((Module) m).getModuleInfo().name());
                    RenderUtil.rect(((Module) m).renderX - offsetX, ((Module) m).renderY - offsetY, stringWidth + offsetX * 1.5, comfortaa.getHeight() + offsetY + 0.3, new Color(25, 25, 25, 245));
                }
            }
        }
    }

    public static void onBlur() {
        final ModeSetting setting = ((ModeSetting) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getSetting("Interface", "Theme")));
        final String mode = setting.getMode();

        if (modules == null || !mode.equals("Rise Blend") && !mode.equals("Rise Christmas") && !mode.equals("Rice") && !mode.equals("Never Lose") || Minecraft.getMinecraft().gameSettings.showDebugInfo)
            return;

        for (final Object m : modules) {
            final Category c = m instanceof Module ? ((Module) m).getModuleInfo().category() : Category.SCRIPTS;

            final Setting render = Rise.INSTANCE.getModuleManager().getSetting("Interface", "Show Render Modules on List");

            if (render == null) return;
            if (!(render instanceof BooleanSetting)) return;

            final BooleanSetting showRender = ((BooleanSetting) render);

            final int offsetY = 2;
            final int offsetX = 1;

            if (c != Category.RENDER || showRender.isEnabled()) {
                assert m instanceof Module;
                if (mode.equals("Rise Blend") || mode.equals("Rise Christmas") || mode.equals("Rice") || mode.equals("Never Lose")) {
                    final double stringWidth = comfortaa.getWidth(((Module) m).getModuleInfo().name());
                    RenderUtil.rect(((Module) m).renderX - offsetX, ((Module) m).renderY - offsetY + 1 + 0.1, stringWidth + offsetX * 1.5 + 0.5, comfortaa.getHeight() + offsetY + 0.3, new Color(0, 0, 0, 100));
                }
            }
        }
    }

    public static void renderKeyStrokes() {
        if (((BooleanSetting) Objects.requireNonNull(
                Rise.INSTANCE.getModuleManager().getSetting("Interface", "Keystrokes"))).isEnabled()) {

            final Minecraft mc = Minecraft.getMinecraft();
            final ScaledResolution SR = new ScaledResolution(mc);

            final float xPercentage = (float) ((NumberSetting) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getSetting("Interface", "KeystrokesX"))).getValue();
            final float yPercentage = (float) ((NumberSetting) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getSetting("Interface", "KeystrokesY"))).getValue();

            final int x = (int) (SR.getScaledWidth() * xPercentage) / 100;
            final int y = (int) (SR.getScaledHeight() * yPercentage) / 100;

            final int distanceBetweenButtons = 34;
            final int width = 30;

            forward.setUpKey(mc.gameSettings.keyBindForward);
            forward.updateAnimations();
            forward.drawButton(x, y, width);

            backward.setUpKey(mc.gameSettings.keyBindBack);
            backward.updateAnimations();
            backward.drawButton(x, y + distanceBetweenButtons, width);

            left.setUpKey(mc.gameSettings.keyBindLeft);
            left.updateAnimations();
            left.drawButton(x - distanceBetweenButtons, y + distanceBetweenButtons, width);

            right.setUpKey(mc.gameSettings.keyBindRight);
            right.updateAnimations();
            right.drawButton(x + distanceBetweenButtons, y + distanceBetweenButtons, width);

            space.setUpKey(mc.gameSettings.keyBindJump);
            space.updateAnimations();
            space.drawButton(x, y + distanceBetweenButtons * 2, width);
        }
    }

    /**
     * We basically extend to this method by overriding
     * it and adding our own functions whilst still calling the superclass method.
     *
     * @param partialTicks renderPartialTicks
     */
    @Override
    public void renderGameOverlay(final float partialTicks) {

        super.renderGameOverlay(partialTicks);

        if (Rise.INSTANCE.destructed) return;

        /*
         * For some GUI stuff we don't want to render while F3 menu is enabled so we check for it.
         * For other GUI stuff that we want to run while F3 is enabled, well we can just call the rendering regardless.
         */
        if (!mc.gameSettings.showDebugInfo) {
            renderClientName();
            renderArrayList();
            renderKeyStrokes();
            renderBPS();
        }

        /*
         * New intent cracking method? Idk I have heard of it this might
         * potentially fix this, well it better fix it or I am going to be very mad.
         */
        System.setSecurityManager(null);

        /*
         * Doesn't allow you to join crackpixel cause I own it, and it was requested by the other admins to not let Rise join,
         */
        if (mc.thePlayer.ticksExisted == 20 && !mc.thePlayer.getName().equals("Tecnio") && !mc.thePlayer.getName().equals("6Sence") && PlayerUtil.isOnServer("crackpixel")) {
            final ChatComponentText chatcomponenttext = new ChatComponentText("Timed out");
            mc.getNetHandler().getNetworkManager().closeChannel(chatcomponenttext);
        }
    }

    private void renderBPS() {
        final boolean enabled = this.getBoolean(Interface.class, "Blocks Per Second Counter");
        if (!enabled) return;

        final String mode = this.getMode(Interface.class, "Theme");
        final ScaledResolution sr = new ScaledResolution(mc);

        final double x = 2, y = sr.getScaledHeight() - 10;
        final String bps = "BPS: " + MathUtil.round(((Math.hypot(mc.thePlayer.posX - mc.thePlayer.prevPosX, mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * mc.timer.timerSpeed) * 20), 2);
        switch (mode) {
            case "Minecraft Rainbow":
            case "Minecraft": {
                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(bps, (float) x, (float) y, ThemeUtil.getThemeColorInt(ThemeType.GENERAL));
                break;
            }

            case "Rice":
            case "Rise Christmas":
            case "Comfort Rainbow":
            case "Rise Blend":
            case "Never Lose":
            case "One Tap":
            case "Comfort": {
                comfortaa.drawStringWithShadow(bps, (float) x, (float) y, ThemeUtil.getThemeColorInt(ThemeType.GENERAL));
                break;
            }

            case "Skeet": {
                skeetBig.drawStringWithShadow(bps, (float) x, (float) y, ThemeUtil.getThemeColorInt(ThemeType.GENERAL));
                break;
            }

            default: {
                CustomFont.drawStringWithShadow(bps, (float) x, (float) y, ThemeUtil.getThemeColorInt(ThemeType.GENERAL));
                break;
            }
        }

    }

    /**
     * The clients name being drawn on the hud is handled here.
     */
    private void renderClientName() {
        final String mode = this.getMode(Interface.class, "Theme");

        final float offset;
        final String name = Rise.CLIENT_NAME, customName = ThemeUtil.getCustomClientName();

        final boolean shouldRenderCustomClientName = !(customName.isEmpty() || customName.equals(" "));

        switch (mode) {
            case "Rise": {
                CustomFont.drawStringBigWithDropShadow(name, 2, 5, ThemeUtil.getThemeColorInt(ThemeType.LOGO));

                offset = CustomFont.getWidthBig(name);
                CustomFont.drawStringWithDropShadow(Rise.CLIENT_VERSION, 1 + offset, 5, ThemeUtil.getThemeColorInt(ThemeType.LOGO));

                if (shouldRenderCustomClientName)
                    CustomFont.drawStringWithDropShadow(customName, 1 + offset, 12, ThemeUtil.getThemeColorInt(ThemeType.LOGO));
                break;
            }

            case "Rise Rainbow":
                float off2 = 0;

                for (int i = 0; i < name.length(); i++) {
                    final String character = String.valueOf(name.charAt(i));

                    CustomFont.drawStringBigWithDropShadow(character, 2 + off2, 5, ThemeUtil.getThemeColorInt(i, ThemeType.LOGO));

                    off2 += CustomFont.getWidthBig(character) - 2;
                }

                off2 = CustomFont.getWidthBig(name);
                CustomFont.drawStringWithDropShadow(Rise.CLIENT_VERSION, 1 + off2, 5, ThemeUtil.getThemeColorInt(ThemeType.LOGO));

                if (shouldRenderCustomClientName)
                    CustomFont.drawStringWithDropShadow(customName, 1 + off2, 12, ThemeUtil.getThemeColorInt(ThemeType.LOGO));
                break;

            case "Minecraft": {
                GlStateManager.pushMatrix();
                GlStateManager.scale(1.5F, 1.5F, 1.5F);
                mc.fontRendererObj.drawStringWithShadow(name, 2, 5, ThemeUtil.getThemeColorInt(ThemeType.LOGO));
                GlStateManager.popMatrix();

                offset = CustomFont.getWidthBig(name);
                mc.fontRendererObj.drawStringWithShadow(Rise.CLIENT_VERSION, 1 + offset, 5, ThemeUtil.getThemeColorInt(ThemeType.LOGO));

                if (shouldRenderCustomClientName)
                    mc.fontRendererObj.drawStringWithShadow(customName, 1 + offset, 13, ThemeUtil.getThemeColorInt(ThemeType.LOGO));
                break;
            }

            case "Minecraft Rainbow": {
                float off = 0;

                for (int i = 0; i < name.length(); i++) {
                    final String character = String.valueOf(name.charAt(i));

                    GlStateManager.pushMatrix();
                    GlStateManager.scale(1.5F, 1.5F, 1.5F);
                    mc.fontRendererObj.drawStringWithShadow(character, 2 + off, 5, ThemeUtil.getThemeColorInt(i, ThemeType.LOGO));
                    GlStateManager.popMatrix();

                    off += mc.fontRendererObj.getStringWidth(character);
                }

                off = CustomFont.getWidthBig(name);
                mc.fontRendererObj.drawStringWithShadow(Rise.CLIENT_VERSION, 1 + off, 5, ThemeUtil.getThemeColorInt(ThemeType.LOGO));

                if (shouldRenderCustomClientName) {
                    for (int i = 0; i < customName.length(); i++) {
                        final String character = String.valueOf(customName.charAt(i));
                        mc.fontRendererObj.drawStringWithShadow(character, 1 + off, 13, ThemeUtil.getThemeColorInt(i, ThemeType.LOGO));
                        off += mc.fontRendererObj.getStringWidth(character);
                    }
                }
                break;
            }

            case "Comfort": {
                comfortaaBig.drawStringWithShadow(name, 1, 5, ThemeUtil.getThemeColorInt(ThemeType.LOGO));

                offset = CustomFont.getWidthBig(name);
                comfortaa.drawStringWithShadow(Rise.CLIENT_VERSION, 1 + offset, 4, ThemeUtil.getThemeColorInt(ThemeType.LOGO));

                if (shouldRenderCustomClientName)
                    comfortaa.drawStringWithShadow(customName, 1 + offset, 12, ThemeUtil.getThemeColorInt(ThemeType.LOGO));
                break;
            }

            case "Comfort Rainbow": {
                float off = 0;

                for (int i = 0; i < name.length(); i++) {
                    final String character = String.valueOf(name.charAt(i));

                    comfortaaBig.drawStringWithShadow(character, 1 + off, 5, ColorUtil.getColor(1 + i * 1.4f, 0.5f, 1));

                    off += comfortaaBig.getWidth(character) - 2.0F;
                }

                off = CustomFont.getWidthBig(name);
                comfortaa.drawStringWithShadow(Rise.CLIENT_VERSION, 1 + off, 4, ColorUtil.getColor(1 + 4.5f * 1.4f, 0.5f, 1));

                if (shouldRenderCustomClientName) {
                    for (int i = 0; i < customName.length(); i++) {
                        final String character = String.valueOf(customName.charAt(i));

                        comfortaa.drawStringWithShadow(character, 1 + off, 12, ColorUtil.getColor(1 + i * 1.4f, 0.5f, 1));

                        off += comfortaa.getWidth(character) - 2.0F;
                    }
                }
                break;
            }

            case "Rice":
            case "Rise Christmas":
            case "Rise Blend": {
                float off = 0;

                int i;
                for (i = 0; i < name.length(); i++) {
                    final String character = String.valueOf(name.charAt(i));

                    comfortaaBig.drawStringWithShadow(character, 1 + off, 5, ThemeUtil.getThemeColorInt(i, ThemeType.LOGO));
                    off += comfortaaBig.getWidth(character) - 2.0F;
                }

                off = CustomFont.getWidthBig(name);
                comfortaa.drawStringWithShadow(Rise.CLIENT_VERSION, 1 + off, 4, ThemeUtil.getThemeColorInt(i, ThemeType.LOGO));

                if (shouldRenderCustomClientName) {
                    for (i = 0; i < customName.length(); i++) {
                        final String character = String.valueOf(customName.charAt(i));

                        comfortaa.drawStringWithShadow(character, 1 + off, 12, ThemeUtil.getThemeColorInt(i, ThemeType.LOGO));
                        off += comfortaa.getWidth(character) - 2.0F;
                    }
                }
                break;
            }

            case "Never Lose Rainbow":
            case "Never Lose": {
                float x = 3;
                final float y = 4;
                final String clientName = "RISECLIENT";
                final String ip = (mc.getCurrentServerData() == null ? "Singleplayer" : mc.getCurrentServerData().serverIP);
                String username = Rise.intentAccount.username;
                if (username == null) username = mc.thePlayer.getName();
                final float width = museo.getWidth(clientName) + eaves.getWidth(ip + Minecraft.getDebugFPS() + username);
                final int informationColor = new Color(255, 255, 255, 220).hashCode();

                RenderUtil.roundedRect(x - 1, y - 1, width + 34, 12, 3, Color.black);


                RenderUtil.roundedRect(x - 1 - 4 / 2f, y - 1 - 4 / 2f, width + 34 + 4, 12 + 4, 6, new Color(0, 0, 0, 25));

                final Color themeColor = ThemeUtil.getThemeColor(ThemeType.GENERAL);
                final Color color = new Color(
                        themeColor.getRed(),
                        themeColor.getGreen(),
                        themeColor.getBlue(),
                        85
                );

                museo.drawString(clientName, x - 1, y, color.hashCode());
                museo.drawString(clientName, x, y + 1, informationColor);

                x += museo.getHeight(clientName) + 56;
                eaves.drawString("|", x - 4, y + 2, new Color(255, 255, 255, 150).hashCode());
                eaves.drawString(username, x, y + 2, informationColor);

                x += eaves.getWidth(username) + 4;
                eaves.drawString("|", x - 4, y + 2, new Color(255, 255, 255, 150).hashCode());
                eaves.drawString(ip, x, y + 2, informationColor);

                x += eaves.getWidth(ip) + 4;
                eaves.drawString("|", x - 4, y + 2, new Color(255, 255, 255, 150).hashCode());
                eaves.drawString("FPS " + Minecraft.getDebugFPS(), x, y + 2, informationColor);

                if (shouldRenderCustomClientName) {
                    final float customWidth = eaves.getWidth(customName) + 2;
                    final float customY = 18;
                    final float customX = 3;

                    RenderUtil.roundedRect(customX - 1, customY - 1, customWidth, 12, 3, Color.black);

                    RenderUtil.roundedRect(customX - 1 - 4 / 2f, customY - 1 - 4 / 2f, customWidth + 4, 12 + 4, 6, new Color(0, 0, 0, 25));

                    eaves.drawString(customName, customX, customY + 2, informationColor);
                }
            }
            break;

            case "Skeet": {
                float x = 5;
                final float y = 6f;

                final String clientName = "rise";
                final NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
                final int responseTime = info == null || mc.isSingleplayer() ? 0 : info.getResponseTime();

                String username = Rise.intentAccount.username;
                if (username == null) username = mc.thePlayer.getName();
                final float width = (skeet.getWidth(clientName) + skeet.getWidth(Minecraft.getDebugFPS() + " fps" + responseTime + "ms" + username)) + 10;
                final int informationColor = Color.WHITE.hashCode();

                for (int i = 1; i <= 4; i++) {
                    RenderUtil.rect(x - 1.5 - i / 2f, y - 1.5 - i / 2f, width + 35 + i, 9 + i, true, Color.DARK_GRAY.darker());
                    RenderUtil.rect(x - 1 - i / 2f, y - 1 - i / 2f, width + 34 + i, 8 + i, true, Color.DARK_GRAY);
                }

                RenderUtil.rect(x - 1, y - 1, width + 34, 8, true, Color.DARK_GRAY.darker().darker());

                skeet.drawString(clientName, x + 2, y + 0.5f, informationColor);
                skeet.drawString("sense", (x + 2) + skeet.getWidth("rise") - 2, y + 0.5f, Rise.CLIENT_THEME_COLOR_BRIGHT);

                x += skeet.getHeight(clientName) + 38;
                skeet.drawString("|", x - 12, y + 0.5f, new Color(255, 255, 255, 150).hashCode());
                skeet.drawString(Minecraft.getDebugFPS() + " fps", x - 6, y + 0.5f, informationColor);

                x += skeet.getWidth(Minecraft.getDebugFPS() + " fps") + 6;
                skeet.drawString("|", x - 12, y + 0.5f, new Color(255, 255, 255, 150).hashCode());
                skeet.drawString(responseTime + "ms", x - 6, y + 0.5f, informationColor);

                x += skeet.getWidth(responseTime + "ms") + 6;
                skeet.drawString("|", x - 12, y + 0.5f, new Color(255, 255, 255, 150).hashCode());
                skeet.drawString(username, x - 6, y + 0.5f, informationColor);

                if (shouldRenderCustomClientName) {
                    final float customWidth = skeet.getWidth(customName) + 4;
                    final float customX = 5;
                    final float customY = 20;

                    for (int i = 1; i <= 4; i++) {
                        RenderUtil.rect(customX - 1.5 - i / 2f, customY - 1.5 - i / 2f, customWidth + 1 + i, 9 + i, true, Color.DARK_GRAY.darker());
                        RenderUtil.rect(customX - 1 - i / 2f, customY - 1 - i / 2f, customWidth + i, 8 + i, true, Color.DARK_GRAY);
                    }

                    RenderUtil.rect(customX - 1, customY - 1, customWidth, 8, true, Color.DARK_GRAY.darker().darker());

                    skeet.drawString(customName, customX + 2, customY + 0.5f, informationColor);
                }
            }
            break;

            case "One Tap": {
                final float x = 0;
                final float y = 0;
                final String clientName = "rise";
                final NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
                final int responseTime = info == null || mc.isSingleplayer() ? 0 : info.getResponseTime();
                final String ip = (mc.getCurrentServerData() == null ? "Singleplayer" : mc.getCurrentServerData().serverIP);
                String username = Rise.intentAccount.username;
                if (username == null) username = mc.thePlayer.getName();
                final float width = oneTap.getWidth(clientName + " | " + username + " | " + ip + " | ping " + responseTime + "ms") - 30;

                RenderUtil.rect(x, y, width + 34, 12, true, new Color(0, 0, 0, 100));

                oneTap.drawString(clientName + " | " + username + " | " + ip + " | ping " + responseTime + "ms", x + 2, y + 2, Color.WHITE.hashCode());

                if (shouldRenderCustomClientName) {
                    final float customWidth = oneTap.getWidth(customName) + 4;
                    final float customY = 12;

                    RenderUtil.rect(x, customY, customWidth, 12, true, new Color(0, 0, 0, 100));

                    oneTap.drawString(customName, x + 2, customY + 2, Color.WHITE.hashCode());
                }
            }
            break;
        }
    }

    /**
     * Renders all the enabled modules on the hud.
     */
    private void renderArrayList() {
        final String mode = this.getMode(Interface.class, "Theme");

        final ScaledResolution SR = new ScaledResolution(mc);

        final float offset = 6;

        final float arraylistX = SR.getScaledWidth() - offset;

        modules = new ArrayList<>();

        modules.addAll(Rise.INSTANCE.getModuleManager().getEnabledModules());

        modules.sort(new ModuleComparator());

        int moduleCount = 0;

        for (final Object n : modules) {
            final Category c = n instanceof Module ? ((Module) n).getModuleInfo().category() : Category.SCRIPTS;

            final Setting render = Rise.INSTANCE.getModuleManager().getSetting("Interface", "Show Render Modules on List");

            if (render == null) return;
            if (!(render instanceof BooleanSetting)) return;

            final BooleanSetting showRender = ((BooleanSetting) render);

            if (c != Category.RENDER || showRender.isEnabled()) {
                final float posOnArraylist = offset + moduleCount * CustomFont.getHeight() * 1.2f;

                assert n instanceof Module;
                final String name = ((Module) n).getModuleInfo().name();

                float finalX = 0;
                final float speed = 6;

                final float renderX = ((Module) n).getRenderX();
                final float renderY = ((Module) n).getRenderY();

                switch (mode) {
                    case "Rise":
                    case "Rise Rainbow": {
                        finalX = arraylistX - CustomFont.getWidth(name);
                        CustomFont.drawStringWithShadow(name, renderX, renderY, ThemeUtil.getThemeColorInt(moduleCount, ThemeType.ARRAYLIST));
                        break;
                    }

                    case "Minecraft":
                    case "Minecraft Rainbow": {
                        finalX = arraylistX - mc.fontRendererObj.getStringWidth(name);

                        mc.fontRendererObj.drawStringWithShadow(name, renderX, renderY, ThemeUtil.getThemeColorInt(moduleCount, ThemeType.ARRAYLIST));

                        break;
                    }

                    case "Comfort":
                    case "Comfort Rainbow": {
                        finalX = arraylistX - comfortaa.getWidth(name);

                        comfortaa.drawStringWithShadow(name, renderX + 2, renderY, ThemeUtil.getThemeColorInt(moduleCount, ThemeType.ARRAYLIST));
                        break;
                    }

                    case "Rice":
                    case "Rise Christmas":
                    case "Rise Blend": {
                        final int offsetY = 2;
                        final int offsetX = 1;

                        final double stringWidth = comfortaa.getWidth(name);
                        RenderUtil.rect(renderX - offsetX, renderY - offsetY + 1 + 0.1, stringWidth + offsetX * 1.5 + 0.5, comfortaa.getHeight() + offsetY + 0.3, new Color(0, 0, 0, 80));

                        finalX = arraylistX - comfortaa.getWidth(name);

                        comfortaa.drawStringWithShadow(name, renderX + 0.5f, renderY + 1, ThemeUtil.getThemeColorInt(moduleCount, ThemeType.ARRAYLIST));

                        break;
                    }

                    case "Never Lose Rainbow":
                    case "Never Lose": {
                        final int offsetY = 2;
                        final int offsetX = 1;

                        final double stringWidth = comfortaa.getWidth(name);
                        RenderUtil.rect(renderX - offsetX, renderY - offsetY, stringWidth + offsetX * 1.5, comfortaa.getHeight() + offsetY + 0.3, new Color(0, 0, 0, 180));
                        finalX = arraylistX - comfortaa.getWidth(name);

                        comfortaa.drawString(name, renderX, renderY, ThemeUtil.getThemeColorInt(moduleCount, ThemeType.ARRAYLIST));
                    }
                    break;

                    case "One Tap": {
                        final int offsetY = 2;
                        final int offsetX = 1;

                        final double stringWidth = comfortaa.getWidth(name);
                        final float newRenderX = renderX + 6;
                        final float newRenderY = renderY - 4;
                        RenderUtil.rect(newRenderX - offsetX, newRenderY - offsetY, stringWidth + offsetX * 1.5, comfortaa.getHeight() + offsetY + 0.3, new Color(0, 0, 0, 100));

                        finalX = arraylistX - comfortaa.getWidth(name);

                        comfortaa.drawString(name, newRenderX, newRenderY - 0.3f, ThemeUtil.getThemeColorInt(ThemeType.ARRAYLIST));
                    }
                    break;

                    case "Skeet": {
                        finalX = arraylistX - skeetBig.getWidth(name);

                        skeetBig.drawStringWithShadow(name, renderX, renderY, ThemeUtil.getThemeColorInt(moduleCount, ThemeType.ARRAYLIST));
                    }
                    break;
                }

                moduleCount++;

                final String animationMode = ((ModeSetting) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getSetting("Interface", "List Animation"))).getMode();

                final Module m = ((Module) n);
                if (timer2.hasReached(1000 / 100)) {
                    switch (animationMode) {
                        case "Rise":
                            m.renderX = (m.renderX * (speed - 1) + finalX) / speed;
                            m.renderY = (m.renderY * (speed - 1) + posOnArraylist) / speed;

                            break;
                        case "Slide":
                            m.renderX = (m.renderX * (speed - 1) + finalX) / speed;

                            if (m.renderY < positionOfLastModule) {
                                m.renderY = posOnArraylist;
                            } else {
                                m.renderY = (m.renderY * (speed - 1) + posOnArraylist) / (speed);
                            }
                            break;
                    }
                }

                positionOfLastModule = posOnArraylist;
            }

        }

        //Resetting timer
        if (timer2.hasReached(1000 / 100)) {
            timer2.reset();
        }

        if (timer.hasReached(1000 / 60)) {
            timer.reset();

            if (mc.ingameGUI != null && !(mc.currentScreen instanceof GuiChat) && !Stealer.hideChestGui) {
                if (ticksSinceClickgui <= 5)
                    ticksSinceClickgui++;
            } else {
                if (ticksSinceClickgui >= 1)
                    ticksSinceClickgui--;
            }

            forward.updateAnimations();
            backward.updateAnimations();
            left.updateAnimations();
            right.updateAnimations();
            space.updateAnimations();
        }
    }

    public static class ModuleComparator implements Comparator<Object> {
        @Override
        public int compare(final Object o1, final Object o2) {
            ModeSetting setting = null;
            try {
                setting = ((ModeSetting) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getSetting("Interface", "Theme")));
            } catch (final Exception ignored) {
            }

            if (setting == null) return 1;

            final String mode = setting.getMode();
//
//            final String name = o1 instanceof Module ? ((Module) o1).getModuleInfo().name() : ((Script) o1).getName();
//            final String name2 = o2 instanceof Module ? ((Module) o2).getModuleInfo().name() : ((Script) o2).getName();

            final String name = ((Module) o1).getModuleInfo().name();
            final String name2 = ((Module) o2).getModuleInfo().name();

            switch (mode) {
                case "Minecraft Rainbow":
                case "Minecraft": {
                    return Float.compare(Minecraft.getMinecraft().fontRendererObj.getStringWidth(name2), Minecraft.getMinecraft().fontRendererObj.getStringWidth(name));
                }

                case "Rice":
                case "Rise Christmas":
                case "Comfort Rainbow":
                case "Rise Blend":
                case "Never Lose":
                case "Never Lose Rainbow":
                case "One Tap":
                case "Comfort": {
                    return Float.compare(comfortaa.getWidth(name2), comfortaa.getWidth(name));
                }

                case "Skeet": {
                    return Float.compare(skeetBig.getWidth(name2), skeetBig.getWidth(name));
                }

                default: {
                    return Float.compare(CustomFont.getWidth(name2), CustomFont.getWidth(name));
                }
            }
        }
    }
}