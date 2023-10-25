package fun.expensive.client.feature.impl.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import fun.rich.client.Rich;
import fun.rich.client.draggable.component.impl.*;
import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.render.EventRender2D;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.feature.impl.misc.NameProtect;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.ListSetting;
import fun.rich.client.utils.math.AnimationHelper;
import fun.rich.client.utils.math.MathematicHelper;
import fun.rich.client.utils.math.TPSUtils;
import fun.rich.client.utils.render.ClientHelper;
import fun.rich.client.utils.render.GLUtils;
import fun.rich.client.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import optifine.CustomColors;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Hud extends Feature {
    public static BooleanSetting waterMark = new BooleanSetting("WaterMark", true, () -> true);
    public static ListSetting waterMarkMode = new ListSetting("WaterMark Mode", "Simple", () -> waterMark.getBoolValue(), "Simple", "OneTap", "Neverlose", "Rich");
    public static BooleanSetting coords = new BooleanSetting("Coordinates", true, () -> true);
    public static BooleanSetting sessionInfo = new BooleanSetting("Session Info", true, () -> true);
    public static BooleanSetting indicators = new BooleanSetting("Indicators", true, () -> true);
    public static BooleanSetting user_info = new BooleanSetting("User Info", true, () -> true);
    public static BooleanSetting armor = new BooleanSetting("Armor Status", true, () -> true);

    public static BooleanSetting potions = new BooleanSetting("Potion Info", true, () -> true);
    private double cooldownBarWidth;
    private double hurttimeBarWidth;
    public float scale = 2;

    public Hud() {
        super("Hud", "Показывает информацию на экране", FeatureCategory.Hud);
        addSettings(waterMark, waterMarkMode, sessionInfo, indicators, potions, coords, user_info, armor);
    }

    @EventTarget
    public void onRender(EventRender2D eventRender2D) {
        if (waterMark.getBoolValue()) {
            if (waterMarkMode.currentMode.equals("Simple")) {
                DraggableWaterMark dwm = (DraggableWaterMark) Rich.instance.draggableHUD.getDraggableComponentByClass(DraggableWaterMark.class);
                dwm.setWidth(180);
                dwm.setHeight(25);
                GLUtils.INSTANCE.rescale(scale);
                Minecraft.getMinecraft().rubik_30.drawStringWithFade("Capybara Premium", dwm.getX() + 3, dwm.getY() + 3);
                Minecraft.getMinecraft().rubik_18.drawStringWithFade("Version " + Rich.instance.version, dwm.getX() + 27, dwm.getY() + 19);

                GLUtils.INSTANCE.rescaleMC();
            } else if (waterMarkMode.currentMode.equals("OneTap")) {
                DraggableWaterMark dwm = (DraggableWaterMark) Rich.instance.draggableHUD.getDraggableComponentByClass(DraggableWaterMark.class);
                dwm.setWidth(200);
                dwm.setHeight(15);
                GLUtils.INSTANCE.rescale(scale);
                String server;
                if (this.mc.isSingleplayer()) {
                    server = "localhost";
                } else {
                    assert (this.mc.getCurrentServerData() != null);
                    server = this.mc.getCurrentServerData().serverIP.toLowerCase();
                }
                String str = "Perfectionist";
                String dateFormat = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                String text = " capybara.wtf | " + str + " | " + server + " | FPS: " + Minecraft.getDebugFPS() + " | " + dateFormat;
                float width = this.mc.tahoma.getStringWidth(text) + 3;
                RenderUtils.drawRect(dwm.getX(), dwm.getY() - 1, (float) dwm.getX() + width + 2.0f, dwm.getY() + 12, new Color(5, 5, 5, 145).getRGB());
                RenderUtils.drawGradientSideways(dwm.getX(), (float) dwm.getY() - 1.1f, (float) dwm.getX() + width + 2.0f, (float) dwm.getY() + 0.1f, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().darker().darker().getRGB());
                mc.tahoma.drawStringWithShadow(text, dwm.getX() + 3, (float) dwm.getY() + 3.5f, new Color(225, 225, 225, 255).getRGB());
                GLUtils.INSTANCE.rescaleMC();
            } else if (waterMarkMode.currentMode.equals("Neverlose")) {
                DraggableWaterMark dwm = (DraggableWaterMark) Rich.instance.draggableHUD.getDraggableComponentByClass(DraggableWaterMark.class);
                dwm.setWidth(200);
                dwm.setHeight(15);
                GLUtils.INSTANCE.rescale(scale);
                SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();
                String server = mc.isSingleplayer() ? "local" : mc.getCurrentServerData() != null ? mc.getCurrentServerData().serverIP.toLowerCase() : "null";
                String text = "" + ChatFormatting.GRAY + " | " + ChatFormatting.RESET + Minecraft.getDebugFPS() + " fps" + ChatFormatting.GRAY + " | " + ChatFormatting.RESET + server + ChatFormatting.GRAY + " | " + ChatFormatting.RESET + formater.format(date);
                String scam = "Capybara Premium | " + "Perfectionist" + " | " + Minecraft.getDebugFPS() + " fps" + " | " + server + " | " + ChatFormatting.RESET + formater.format(date);
                RenderUtils.drawBlurredShadow(dwm.getX() + 5, dwm.getY() + 6, Minecraft.getMinecraft().rubik_14.getStringWidth(scam) + 12, 12, 8, new Color(21, 21, 21, 255));

                RenderUtils.drawSmoothRect(dwm.getX() + 5, dwm.getY() + 6, dwm.getX() + Minecraft.getMinecraft().rubik_14.getStringWidth(scam) + 15, dwm.getY() + 18, new Color(21, 21, 21, 255).getRGB());
                Minecraft.getMinecraft().neverlose900_15.drawString("Capybara Premium", dwm.getX() + 7.5F, dwm.getY() + 10, new Color(22, 97, 141).getRGB());
                Minecraft.getMinecraft().neverlose900_15.drawString("Capybara Premium", dwm.getX() + 8F, dwm.getY() + 10.5F, -1);
                Minecraft.getMinecraft().rubik_14.drawString(ChatFormatting.GRAY + " | " + TextFormatting.RESET + text, dwm.getX() + 7 + Minecraft.getMinecraft().rubik_14.getStringWidth("Capybara Premium | "), dwm.getY() + 10.5f, -1);
                GLUtils.INSTANCE.rescaleMC();
            } else if (waterMarkMode.currentMode.equals("Rich")) {
                DraggableWaterMark dwm = (DraggableWaterMark) Rich.instance.draggableHUD.getDraggableComponentByClass(DraggableWaterMark.class);
                dwm.setWidth(200);
                dwm.setHeight(15);
                GLUtils.INSTANCE.rescale(scale);
                String server = mc.isSingleplayer() ? "local" : mc.getCurrentServerData() != null ? mc.getCurrentServerData().serverIP.toLowerCase() : "null";
                String scam = "capybara.wtf" + TextFormatting.GRAY + " | " + TextFormatting.RESET + "Perfectionist" + TextFormatting.GRAY + " | " + TextFormatting.RESET + server + TextFormatting.GRAY + " | " + TextFormatting.RESET + Minecraft.getDebugFPS() + "fps";

                String text = TextFormatting.GRAY + "  | " + TextFormatting.RESET +  "Perfectionist"+ TextFormatting.GRAY + " | " + TextFormatting.RESET + server + TextFormatting.GRAY + " | " + TextFormatting.RESET + Minecraft.getDebugFPS() + "fps";
                RenderUtils.drawBlurredShadow(dwm.getX() + 5, dwm.getY() + 6, Minecraft.getMinecraft().rubik_15.getStringWidth(scam) + 8, 17.5f, 8, new Color(25, 25, 25, 215));
                Minecraft.getMinecraft().rubik_14.drawStringWithShadow("capybaraclient.fun", dwm.getX() + 11, dwm.getY() + 13f, -1);

                Minecraft.getMinecraft().rubik_15.drawString(text, dwm.getX() + 10 + Minecraft.getMinecraft().rubik_14.getStringWidth("capybaraclient.fun"), dwm.getY() + 13, -1);

                GLUtils.INSTANCE.rescaleMC();
            }
        }
        if (user_info.getBoolValue()) {
            DraggableUserInfo dci = (DraggableUserInfo) Rich.instance.draggableHUD.getDraggableComponentByClass(DraggableUserInfo.class);
            dci.setWidth(168);
            dci.setHeight(18);
            GLUtils.INSTANCE.rescale(scale);
            String buildStr = ChatFormatting.GRAY + "Version - " + ChatFormatting.WHITE + Rich.instance.version + ChatFormatting.WHITE + " |" + ChatFormatting.GRAY + " Username - " + ChatFormatting.WHITE + "Perfectionist";
            String tpsStr = mc.isSingleplayer() ? ChatFormatting.GRAY + "TPS - " + ChatFormatting.WHITE + "local" : ChatFormatting.GRAY + "TPS - " + ChatFormatting.WHITE + MathematicHelper.round(TPSUtils.getTickRate(), 1);
            mc.rubik_18.drawStringWithShadow(buildStr, getX2(), getY2(), -1);
            mc.rubik_18.drawStringWithShadow(tpsStr, getX2() + mc.rubik_18.getStringWidth(buildStr) - 1 - mc.rubik_18.getStringWidth(tpsStr), getY2() - 10, -1);
            GLUtils.INSTANCE.rescaleMC();
        }
        if (coords.getBoolValue()) {
            DraggableCoordsInfo dci = (DraggableCoordsInfo) Rich.instance.draggableHUD.getDraggableComponentByClass(DraggableCoordsInfo.class);
            dci.setWidth(90);
            dci.setHeight(25);
            GLUtils.INSTANCE.rescale(scale);

            String xCoord = "" + Math.round(mc.player.posX);
            String yCoord = "" + Math.round(mc.player.posY);
            String zCoord = "" + Math.round(mc.player.posZ);
            String fps = "" + Math.round(Minecraft.getDebugFPS());
            mc.rubik_18.drawStringWithShadow("BPS:", getX() + 43, getY() - 10, ClientHelper.getClientColor().getRGB());
            mc.rubik_18.drawStringWithShadow("" + calculateBPS(), getX() + 65, getY() - 10, -1);
            mc.rubik_18.drawStringWithShadow("FPS:", getX(), getY() - 10, ClientHelper.getClientColor().getRGB());
            mc.rubik_18.drawStringWithShadow(fps, getX() + 22, getY() - 10, -1);
            mc.rubik_18.drawStringWithShadow("X: ", getX(), getY(), ClientHelper.getClientColor().getRGB());
            mc.rubik_18.drawStringWithShadow(xCoord, getX() + 10, getY(), -1);
            mc.rubik_18.drawStringWithShadow("Y: ", getX() + 30 + mc.rubik_18.getStringWidth(xCoord) - 17, getY(), ClientHelper.getClientColor().getRGB());
            mc.rubik_18.drawStringWithShadow(yCoord, getX() + 40 + mc.rubik_18.getStringWidth(xCoord) - 17, getY(), -1);
            mc.rubik_18.drawStringWithShadow("Z: ", getX() + 66 + mc.rubik_18.getStringWidth(xCoord) - 23 + mc.rubik_18.getStringWidth(yCoord) - 17, getY(), ClientHelper.getClientColor().getRGB());
            mc.rubik_18.drawStringWithShadow(zCoord, getX() + 76 + mc.rubik_18.getStringWidth(xCoord) - 23 + mc.rubik_18.getStringWidth(yCoord) - 17, getY(), -1);
            GLUtils.INSTANCE.rescaleMC();


        }
        if (indicators.getBoolValue()) {
            DraggableIndicators di = (DraggableIndicators) Rich.instance.draggableHUD.getDraggableComponentByClass(DraggableIndicators.class);

            int x = di.getX();
            int y = di.getY();
            int width = di.getWidth();
            int height = di.getHeight();
            di.setWidth(145);
            di.setHeight(40);
            GLUtils.INSTANCE.rescale(scale);

            RenderUtils.drawRect2(x, y, width, height, new Color(20, 20, 20, 190).getRGB());

            RenderUtils.drawRect2(x, y + 1.5, width, 13, new Color(25, 25, 25).getRGB());

            mc.rubik_18.drawBlurredString("Indicators", x + MathematicHelper.getCenter(width, mc.neverlose900_16.getStringWidth("Indicators")), (float) (y + 5.5), 8, new Color(255, 255, 255, 80), -1);
            RenderUtils.drawBlurredShadow(x, y, width, 1.5f, 9, ClientHelper.getClientColor());

            RenderUtils.drawRect2(x, y, width, 1.5, ClientHelper.getClientColor().getRGB());

            mc.rubik_16.drawBlurredString("Cooldown", x + 4, y + 19, 6, new Color(255, 255, 255, 80), -1);
            mc.rubik_16.drawBlurredString("Hurt-time", x + 4, y + 19 + 9, 6, new Color(255, 255, 255, 80), -1);

            double coef = mc.player.getCooledAttackStrength(1) / 1;
            double wid = coef * 90;
            cooldownBarWidth = AnimationHelper.calculateCompensation((float) wid, (float) cooldownBarWidth, (long) ((long) 3 * Rich.deltaTime()), 3 * Rich.deltaTime());


            double coef2 = MathHelper.clamp(mc.player.hurtTime, 0.0, 0.6);
            double wid2 = coef2 * 90;
            hurttimeBarWidth = AnimationHelper.calculateCompensation((float) wid2, (float) hurttimeBarWidth, (long) ((long) 3 * Rich.deltaTime()), 3 * Rich.deltaTime());

            RenderUtils.drawRect2(x + 50, y + 19, 90, 5, Color.DARK_GRAY.getRGB());
            RenderUtils.drawRect2(x + 50, y + 19 + 9, 90, 5, Color.DARK_GRAY.getRGB());

            RenderUtils.drawBlurredShadow(x + 50, y + 19, (float) cooldownBarWidth, 5, 9, ClientHelper.getClientColor());
            RenderUtils.drawGradientRected(x + 50, y + 19, (float) cooldownBarWidth, 5, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().brighter().getRGB());
            RenderUtils.drawBlurredShadow(x + 50, y + 19 + 9, (float) hurttimeBarWidth, 5, 9, ClientHelper.getClientColor());
            RenderUtils.drawGradientRected(x + 50, y + 19 + 9, (float) hurttimeBarWidth, 5, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().brighter().getRGB());
            GLUtils.INSTANCE.rescaleMC();

        }
        if (potions.getBoolValue()) {
            DraggablePotionHUD dph = (DraggablePotionHUD) Rich.instance.draggableHUD.getDraggableComponentByClass(DraggablePotionHUD.class);
            dph.setWidth(100);
            dph.setHeight(150);
            GLUtils.INSTANCE.rescale(scale);
            int xOff = 21;
            int yOff = 14;
            int counter = 16;

            Collection<PotionEffect> collection = mc.player.getActivePotionEffects();

            {
                GlStateManager.color(1F, 1F, 1F, 1F);
                GlStateManager.disableLighting();
                int listOffset = 23;
                if (collection.size() > 5) {
                    listOffset = 132 / (collection.size() - 1);
                }
                List<PotionEffect> potions = new ArrayList<>(mc.player.getActivePotionEffects());
                potions.sort(Comparator.comparingDouble(effect -> mc.fontRendererObj.getStringWidth((Objects.requireNonNull(Potion.getPotionById(CustomColors.getPotionId(effect.getEffectName()))).getName()))));

                for (PotionEffect potion : potions) {
                    Potion effect = Potion.getPotionById(CustomColors.getPotionId(potion.getEffectName()));
                    GlStateManager.color(1F, 1F, 1F, 1F);

                    assert effect != null;
                    if (effect.hasStatusIcon()) {
                        mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
                        int statusIconIndex = effect.getStatusIconIndex();
                        new Gui().drawTexturedModalRect((float) ((dph.getX() + xOff) - 20), (dph.getY() + counter) - yOff - 4, statusIconIndex % 8 * 18, 198 + statusIconIndex / 8 * 18, 18, 18);
                    }

                    String level = I18n.format(effect.getName());
                    if (potion.getAmplifier() == 1) {
                        level = level + " " + I18n.format("enchantment.level.2");
                    } else if (potion.getAmplifier() == 2) {
                        level = level + " " + I18n.format("enchantment.level.3");
                    } else if (potion.getAmplifier() == 3) {
                        level = level + " " + I18n.format("enchantment.level.4");
                    }

                    int getPotionColor = -1;
                    if ((potion.getDuration() < 200)) {
                        getPotionColor = new Color(215, 59, 59).getRGB();
                    } else if (potion.getDuration() < 400) {
                        getPotionColor = new Color(231, 143, 32).getRGB();
                    } else if (potion.getDuration() > 400) {
                        getPotionColor = new Color(172, 171, 171).getRGB();
                    }

                    String durationString = Potion.getDurationString(potion);

                    mc.rubik_18.drawStringWithOutline(level, dph.getX() + xOff, (dph.getY() + counter) - yOff, -1);
                    mc.rubik_18.drawStringWithOutline(durationString, dph.getX() + xOff, (dph.getY() + counter + 10) - yOff, getPotionColor);
                    counter += listOffset;
                }
                GLUtils.INSTANCE.rescaleMC();
            }
        }
        if (sessionInfo.getBoolValue()) {
            DraggableSessionInfo dsi = (DraggableSessionInfo) Rich.instance.draggableHUD.getDraggableComponentByClass(DraggableSessionInfo.class);
            dsi.setWidth(155);
            dsi.setHeight(56);
            GLUtils.INSTANCE.rescale(scale);
            String server = mc.isSingleplayer() ? "local" : mc.getCurrentServerData() != null ? mc.getCurrentServerData().serverIP.toLowerCase() : "null";
            String name = Rich.instance.featureManager.getFeature(NameProtect.class).isEnabled() && NameProtect.myName.getBoolValue() ? "Name: " + "Protected" : "Name: " + mc.player.getName();

            String time;
            if (Minecraft.getMinecraft().isSingleplayer()) {
                time = "SinglePlayer";
            } else {
                long durationInMillis = System.currentTimeMillis() - Rich.playTimeStart;
                long second = (durationInMillis / 1000) % 60;
                long minute = (durationInMillis / (1000 * 60)) % 60;
                long hour = (durationInMillis / (1000 * 60 * 60)) % 24;
                time = String.format("%02dh %02dm %02ds", hour, minute, second);
            }
            RenderUtils.drawBlurredShadow(dsi.getX(), dsi.getY(), 155, 56, 8, new Color(20, 20, 20, 190));
            RenderUtils.drawSmoothRect(dsi.getX() + 2.4f, dsi.getY() + 3.5f, dsi.getX() + 153, dsi.getY() + 4.5f, new Color(ClientHelper.getClientColor().getRGB()).getRGB());
            RenderUtils.drawBlurredShadow(dsi.getX() + 2.4f, dsi.getY() + 1, 153, 4.5f, 8, RenderUtils.injectAlpha(ClientHelper.getClientColor(), 100));

            RenderUtils.drawRect(dsi.getX() + 2.8f, dsi.getY() + 19, dsi.getX() + 152, dsi.getY() + 19.5f, new Color(65, 65, 65).getRGB());

            mc.rubik_18.drawString("Session Information", dsi.getX() + 5, dsi.getY() + 9.3f, -1);
            mc.rubik_17.drawString("Server: " + server, dsi.getX() + 5, dsi.getY() + 25, -1);
            mc.rubik_17.drawString(name, dsi.getX() + 5, dsi.getY() + 34.5f, -1);
            mc.rubik_17.drawString("Play Time: " + time, dsi.getX() + 5, dsi.getY() + 44, -1);
            GLUtils.INSTANCE.rescaleMC();
        }
    }

    private double calculateBPS() {
        double bps = (Math.hypot(mc.player.posX - mc.player.prevPosX, mc.player.posZ - mc.player.prevPosZ) * mc.timer.timerSpeed) * 20;
        return Math.round(bps * 100.0) / 100.0;
    }

    public float getX2() {
        DraggableUserInfo dci = (DraggableUserInfo) Rich.instance.draggableHUD.getDraggableComponentByClass(DraggableUserInfo.class);

        return dci.getX();
    }

    public float getY2() {
        DraggableUserInfo dci = (DraggableUserInfo) Rich.instance.draggableHUD.getDraggableComponentByClass(DraggableUserInfo.class);

        return dci.getY() + 10;
    }

    public float getX() {
        DraggableCoordsInfo dci = (DraggableCoordsInfo) Rich.instance.draggableHUD.getDraggableComponentByClass(DraggableCoordsInfo.class);

        return dci.getX();
    }

    public float getY() {
        DraggableCoordsInfo dci = (DraggableCoordsInfo) Rich.instance.draggableHUD.getDraggableComponentByClass(DraggableCoordsInfo.class);

        return dci.getY() + 15;
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }
}
