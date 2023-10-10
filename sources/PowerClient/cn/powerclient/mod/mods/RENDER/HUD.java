/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.RENDER;

import com.darkmagician6.eventapi.EventTarget;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;
import me.AveReborn.Client;
import me.AveReborn.Value;
import me.AveReborn.events.EventRender2D;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import me.AveReborn.mod.ModManager;
import me.AveReborn.ui.ClientNotification;
import me.AveReborn.util.ClientUtil;
import me.AveReborn.util.Colors;
import me.AveReborn.util.RenderUtil;
import me.AveReborn.util.fontRenderer.FontManager;
import me.AveReborn.util.fontRenderer.UnicodeFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class HUD
extends Mod {
    public Value<String> logo_mode = new Value("HUD", "LogoStyle", 0);
    public Value<Boolean> logo = new Value<Boolean>("HUD_Logo", true);
    public static Value<Boolean> hotbar = new Value<Boolean>("HUD_Hotbar", false);
    public static Value<Boolean> FPS = new Value<Boolean>("HUD_FPS", false);
    public Value<Boolean> array = new Value<Boolean>("HUD_ArrayList", true);
    public static Value<Boolean> stuff = new Value<Boolean>("HUD_ArmorStatus", true);
    public Value<Boolean> potion = new Value<Boolean>("HUD_PotionStatus", true);
    public static Value<Boolean> ttfChat = new Value<Boolean>("HUD_TTFChat", false);
    public static Value<Boolean> info = new Value<Boolean>("HUD_info", false);
    private Comparator<Mod> comparator = (m2, m1) -> {
        String mName = m2.getDisplayName() == "" ? m2.getName() : String.format("%s %s", m2.getName(), m2.getDisplayName());
        String m1Name = m1.getDisplayName() == "" ? m1.getName() : String.format("%s %s", m1.getName(), m1.getDisplayName());
        return Integer.compare(Client.instance.fontMgr.tahoma18.getStringWidth(m1Name), Client.instance.fontMgr.tahoma18.getStringWidth(mName));
    };
    public int etb_color = new Color(105, 180, 255).getRGB();
    FontRenderer fr;
    boolean lowhealth;
    public static boolean toggledlyric = false;
    public int x;
	private Category Direction;

    public HUD() {
        super("HUD", Category.RENDER);
        this.fr = this.mc.fontRendererObj;
        this.lowhealth = false;
        this.x = 0;
        this.logo_mode.mode.add("Exhi:A");
        this.logo_mode.mode.add("ETB:A");
        this.logo_mode.mode.add("Meme:A");
        this.logo_mode.mode.add("Test:A");
        this.logo_mode.mode.add("Exhi:N");
        this.logo_mode.mode.add("ETB:N");
        this.logo_mode.mode.add("Meme:N");
        this.logo_mode.mode.add("Test:N");
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        ScaledResolution sr2 = new ScaledResolution(this.mc);
        if (Minecraft.thePlayer.getHealth() < 6.0f && !this.lowhealth) {
            ClientUtil.sendClientMessage("Your Health is Low!", ClientNotification.Type.WARNING);
            this.lowhealth = true;
        }
        if (Minecraft.thePlayer.getHealth() > 6.0f && this.lowhealth) {
            this.lowhealth = false;
        }
        this.renderToggled(sr2);
        this.renderPotionStatus(sr2);
        if (this.logo.getValueState().booleanValue()) {
            this.renderLogo();
        }
        String info = "\u00a7bCoords: \u00a7r" + (int)Minecraft.thePlayer.posX + " " + (int)Minecraft.thePlayer.posY + " " + (int)Minecraft.thePlayer.posZ;
        String fps = "\u00a7bFPS: \u00a7r" + Minecraft.getDebugFPS() + (this.mc.isSingleplayer() ? " \u00a7bPing: \u00a7r0ms" : new StringBuilder(" \u00a7bPing: \u00a7r").append(this.mc.getNetHandler().getPlayerInfo(Minecraft.thePlayer.getUniqueID()).getResponseTime()).append("ms").toString());
        String Dev = "\u00a7bUser: \u00a7rDev" + (this.mc.isSingleplayer() ? " \u00a7bPing: \u00a7r0ms" : new StringBuilder(" \u00a7bPing: \u00a7r").append(this.mc.getNetHandler().getPlayerInfo(Minecraft.thePlayer.getUniqueID()).getResponseTime()).append("ms").toString());
        String Other = "\u00a7bUser: \u00a7rOther" + (this.mc.isSingleplayer() ? " \u00a7bPing: \u00a7r0ms" : new StringBuilder(" \u00a7bPing: \u00a7r").append(this.mc.getNetHandler().getPlayerInfo(Minecraft.thePlayer.getUniqueID()).getResponseTime()).append("ms").toString());
        if (HUD.info.getValueState().booleanValue()) {
            if (!(this.mc.currentScreen instanceof GuiChat)) {
                if (this.mc.fontRendererObj.getStringWidth(info) > this.mc.fontRendererObj.getStringWidth(fps)) {
                    RenderUtil.drawRect(0.0f, sr2.getScaledHeight() - 23, this.fr.getStringWidth(info) + 8, sr2.getScaledHeight(), Integer.MIN_VALUE);
                } else if (this.mc.fontRendererObj.getStringWidth(fps) > this.mc.fontRendererObj.getStringWidth(info)) {
                    RenderUtil.drawRect(0.0f, sr2.getScaledHeight() - 23, this.fr.getStringWidth(fps) + 8, sr2.getScaledHeight(), Integer.MIN_VALUE);
                }
                this.fr.drawString(info, 2, sr2.getScaledHeight() - 10, 16777215);
                this.fr.drawString(Dev, 2, sr2.getScaledHeight() - 20, 16777215);
            }
            GlStateManager.color(1.0f, 1.0f, 1.0f);
        }
    }

    public void renderLogo() {
        SimpleDateFormat dft;
        String dire;
        String rendertime;
        String text;
        Date time;
        String text_b;
        if (this.logo_mode.isCurrentMode("ETB:A")) {
            dire = Direction.values()[MathHelper.floor_double((double)(Minecraft.thePlayer.rotationYaw * 4.0f / 180.0f) + 0.5) & 7].name();
            text = Client.CLIENT_NAME;
            text_b = String.valueOf(text.substring(1)) + " " + Client.CLIENT_VER;
            this.fr.drawStringWithShadow(Client.CLIENT_NAME.toUpperCase(), 2.0f, 1.0f, this.etb_color);
            this.fr.drawStringWithShadow(Client.CLIENT_VER, 5 + this.fr.getStringWidth(Client.CLIENT_NAME.toUpperCase()), 1.0f, 10000536);
            this.fr.drawStringWithShadow("[" + dire + "]", this.fr.getStringWidth(Client.CLIENT_NAME.toUpperCase()) + this.fr.getStringWidth(Client.CLIENT_VER) + 9, 1.0f, this.etb_color);
            if (FPS.getValueState().booleanValue()) {
                this.fr.drawStringWithShadow("FPS:" + Minecraft.getDebugFPS(), 2.0f, 9.0f, this.etb_color);
            }
        }
        if (this.logo_mode.isCurrentMode("Exhi:A")) {
            dft = new SimpleDateFormat("hh:mm a");
            time = Calendar.getInstance().getTime();
            rendertime = dft.format(time);
            Client.fontManager.tahoma20.drawStringWithShadow(Client.CLIENT_NAME.substring(0, 1), 2.0f, -1.0f, RenderUtil.rainbow(50));
            Client.fontManager.tahoma20.drawStringWithShadow(Client.CLIENT_NAME.substring(1), Client.fontManager.tahoma20.getStringWidth(Client.CLIENT_NAME.substring(0, 1)) + 2, -1.0f, Colors.WHITE.c);
            Client.fontManager.tahoma18.drawStringWithShadow("[" + rendertime + "]", Client.fontManager.tahoma18.getStringWidth(Client.CLIENT_NAME) + 8, -1.0f, Colors.WHITE.c);
            if (FPS.getValueState().booleanValue()) {
                Client.fontManager.tahoma20.drawStringWithShadow("FPS:" + Minecraft.getDebugFPS(), 2.0f, 8.0f, RenderUtil.rainbow(50));
            }
        }
        if (this.logo_mode.isCurrentMode("Meme:A")) {
            dft = new SimpleDateFormat("hh:mm a");
            time = Calendar.getInstance().getTime();
            rendertime = dft.format(time);
            Client.fontManager.sansation20.drawStringWithShadow(Client.CLIENT_NAME.substring(0, 1), 2.0f, -1.0f, RenderUtil.rainbow(50));
            Client.fontManager.sansation20.drawStringWithShadow(Client.CLIENT_NAME.substring(1), Client.fontManager.sansation20.getStringWidth(Client.CLIENT_NAME.substring(0, 1)) + 2, -1.0f, Colors.WHITE.c);
            Client.fontManager.sansation18.drawStringWithShadow("[" + rendertime + "]", Client.fontManager.sansation18.getStringWidth(Client.CLIENT_NAME) + 8, -1.0f, Colors.WHITE.c);
            if (FPS.getValueState().booleanValue()) {
                Client.fontManager.sansation20.drawStringWithShadow("FPS:" + Minecraft.getDebugFPS(), 2.0f, 8.0f, RenderUtil.rainbow(50));
            }
        }
        if (this.logo_mode.isCurrentMode("Test:A")) {
            dft = new SimpleDateFormat("hh:mm a");
            time = Calendar.getInstance().getTime();
            rendertime = dft.format(time);
            Client.fontManager.consolasbold20.drawStringWithShadow(Client.CLIENT_NAME.substring(0, 1), 2.0f, -1.0f, RenderUtil.rainbow(50));
            Client.fontManager.consolasbold20.drawStringWithShadow(Client.CLIENT_NAME.substring(1), Client.fontManager.consolasbold20.getStringWidth(Client.CLIENT_NAME.substring(0, 1)) + 2, -1.0f, Colors.WHITE.c);
            Client.fontManager.consolasbold18.drawStringWithShadow("[" + rendertime + "]", Client.fontManager.consolasbold18.getStringWidth(Client.CLIENT_NAME) + 8, -1.0f, Colors.WHITE.c);
            if (FPS.getValueState().booleanValue()) {
                Client.fontManager.consolasbold20.drawStringWithShadow("FPS:" + Minecraft.getDebugFPS(), 2.0f, 8.0f, RenderUtil.rainbow(50));
            }
        }
        if (this.logo_mode.isCurrentMode("Exhi:N")) {
            dft = new SimpleDateFormat("hh:mm a");
            time = Calendar.getInstance().getTime();
            rendertime = dft.format(time);
            Client.fontManager.tahoma20.drawStringWithShadow(Client.CLIENT_NAME2.substring(0, 1), 2.0f, -1.0f, RenderUtil.rainbow(50));
            Client.fontManager.tahoma20.drawStringWithShadow(Client.CLIENT_NAME2.substring(1), Client.fontManager.tahoma20.getStringWidth(Client.CLIENT_NAME2.substring(0, 1)) + 2, -1.0f, Colors.WHITE.c);
            Client.fontManager.tahoma18.drawStringWithShadow("[" + rendertime + "]", Client.fontManager.tahoma18.getStringWidth(Client.CLIENT_NAME2) + 8, -1.0f, Colors.WHITE.c);
            if (FPS.getValueState().booleanValue()) {
                Client.fontManager.tahoma20.drawStringWithShadow("FPS:" + Minecraft.getDebugFPS(), 2.0f, 8.0f, RenderUtil.rainbow(50));
            }
        }
        if (this.logo_mode.isCurrentMode("ETB:N")) {
            dire = Direction.values()[MathHelper.floor_double((double)(Minecraft.thePlayer.rotationYaw * 4.0f / 180.0f) + 0.5) & 7].name();
            text = Client.CLIENT_NAME2;
            text_b = String.valueOf(text.substring(1)) + " " + Client.CLIENT_VER;
            this.fr.drawStringWithShadow(Client.CLIENT_NAME2.toUpperCase(), 2.0f, 1.0f, this.etb_color);
            this.fr.drawStringWithShadow(Client.CLIENT_VER, 5 + this.fr.getStringWidth(Client.CLIENT_NAME2.toUpperCase()), 1.0f, 10000536);
            this.fr.drawStringWithShadow("[" + dire + "]", this.fr.getStringWidth(Client.CLIENT_NAME2.toUpperCase()) + this.fr.getStringWidth(Client.CLIENT_VER) + 9, 1.0f, this.etb_color);
            if (FPS.getValueState().booleanValue()) {
                this.fr.drawStringWithShadow("FPS:" + Minecraft.getDebugFPS(), 2.0f, 9.0f, this.etb_color);
            }
        }
        if (this.logo_mode.isCurrentMode("Meme:N")) {
            dft = new SimpleDateFormat("hh:mm a");
            time = Calendar.getInstance().getTime();
            rendertime = dft.format(time);
            Client.fontManager.sansation20.drawStringWithShadow(Client.CLIENT_NAME2.substring(0, 1), 2.0f, -1.0f, RenderUtil.rainbow(50));
            Client.fontManager.sansation20.drawStringWithShadow(Client.CLIENT_NAME2.substring(1), Client.fontManager.sansation20.getStringWidth(Client.CLIENT_NAME2.substring(0, 1)) + 2, -1.0f, Colors.WHITE.c);
            Client.fontManager.sansation18.drawStringWithShadow("[" + rendertime + "]", Client.fontManager.sansation18.getStringWidth(Client.CLIENT_NAME2) + 8, -1.0f, Colors.WHITE.c);
            if (FPS.getValueState().booleanValue()) {
                Client.fontManager.sansation20.drawStringWithShadow("FPS:" + Minecraft.getDebugFPS(), 2.0f, 8.0f, RenderUtil.rainbow(50));
            }
        }
        if (this.logo_mode.isCurrentMode("Test:N")) {
            dft = new SimpleDateFormat("hh:mm a");
            time = Calendar.getInstance().getTime();
            rendertime = dft.format(time);
            Client.fontManager.consolasbold20.drawStringWithShadow(Client.CLIENT_NAME2.substring(0, 1), 2.0f, -1.0f, RenderUtil.rainbow(50));
            Client.fontManager.consolasbold20.drawStringWithShadow(Client.CLIENT_NAME2.substring(1), Client.fontManager.consolasbold20.getStringWidth(Client.CLIENT_NAME2.substring(0, 1)) + 2, -1.0f, Colors.WHITE.c);
            Client.fontManager.consolasbold18.drawStringWithShadow("[" + rendertime + "]", Client.fontManager.consolasbold18.getStringWidth(Client.CLIENT_NAME2) + 8, -1.0f, Colors.WHITE.c);
            if (FPS.getValueState().booleanValue()) {
                Client.fontManager.consolasbold20.drawStringWithShadow("FPS:" + Minecraft.getDebugFPS(), 2.0f, 8.0f, RenderUtil.rainbow(50));
            }
        }
    }

    private void renderToggled(ScaledResolution sr2) {
        if (this.array.getValueState().booleanValue()) {
            ArrayList<Mod> mods = new ArrayList<Mod>(Client.instance.modMgr.getToggled());
            int[] counter = new int[1];
            mods.sort(this.comparator);
            int yStart = 0;
            int right = sr2.getScaledWidth();
            for (Mod module : mods) {
                RenderUtil.drawRect(sr2.getScaledWidth() - 2, yStart - 1, sr2.getScaledWidth(), yStart + Client.fontManager.tahoma18.FONT_HEIGHT, RenderUtil.rainbow(counter[0] * 200));
                if (module.getDisplayName() != "") {
                    RenderUtil.drawRect(sr2.getScaledWidth() - Client.fontManager.tahoma18.getStringWidth(String.valueOf(module.getDisplayName()) + module.getName()) - 9, yStart, sr2.getScaledWidth() - 2, yStart + Client.fontManager.tahoma18.FONT_HEIGHT, ClientUtil.reAlpha(Colors.BLACK.c, 0.4f));
                } else {
                    RenderUtil.drawRect(sr2.getScaledWidth() - Client.fontManager.tahoma18.getStringWidth(module.getName()) - 6, yStart, sr2.getScaledWidth() - 2, yStart + Client.fontManager.tahoma18.FONT_HEIGHT, ClientUtil.reAlpha(Colors.BLACK.c, 0.4f));
                }
                Client.fontManager.tahoma18.drawStringWithShadow(module.getName(), right - Client.fontManager.tahoma18.getStringWidth(module.getName()) - 4, yStart, RenderUtil.rainbow(counter[0] * 200));
                if (module.getDisplayName() != null) {
                    Client.fontManager.tahoma18.drawStringWithShadow(module.getDisplayName(), right - Client.fontManager.tahoma18.getStringWidth(String.valueOf(module.getDisplayName()) + module.getName()) - 7, yStart, Colors.WHITE.c);
                }
                yStart += Client.fontManager.tahoma18.FONT_HEIGHT;
                int[] arrn = counter;
                arrn[0] = arrn[0] + 1;
            }
        }
    }

    public void renderPotionStatus(ScaledResolution sr2) {
        if (!this.potion.getValueState().booleanValue()) {
            return;
        }
        this.x = 0;
        for (PotionEffect effect : Minecraft.thePlayer.getActivePotionEffects()) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            String PType = I18n.format(potion.getName(), new Object[0]);
            String d2 = "";
            switch (effect.getAmplifier()) {
                case 1: {
                    PType = String.valueOf(PType) + (Object)((Object)EnumChatFormatting.DARK_AQUA) + " II";
                    break;
                }
                case 2: {
                    PType = String.valueOf(PType) + (Object)((Object)EnumChatFormatting.BLUE) + " III";
                    break;
                }
                case 3: {
                    PType = String.valueOf(PType) + (Object)((Object)EnumChatFormatting.DARK_PURPLE) + " IV";
                    break;
                }
            }
            if (effect.getDuration() < 600 && effect.getDuration() > 300) {
                d2 = (Object)((Object)EnumChatFormatting.YELLOW) + Potion.getDurationString(effect);
            } else if (effect.getDuration() < 300) {
                d2 = (Object)((Object)EnumChatFormatting.RED) + Potion.getDurationString(effect);
            } else if (effect.getDuration() > 600) {
                d2 = (Object)((Object)EnumChatFormatting.WHITE) + Potion.getDurationString(effect);
            }
            int x1 = (int)((double)(sr2.getScaledWidth() - 6) * 1.33);
            int y1 = (int)((float)(sr2.getScaledHeight() - 32 - this.mc.fontRendererObj.FONT_HEIGHT + this.x + 5) * 1.33f);
            if (potion.hasStatusIcon()) {
                GlStateManager.pushMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                int var10 = potion.getStatusIconIndex();
                ResourceLocation location = new ResourceLocation("textures/gui/container/inventory.png");
                this.mc.getTextureManager().bindTexture(location);
                GlStateManager.scale(0.75, 0.75, 0.75);
                this.mc.ingameGUI.drawTexturedModalRect(x1 - 9, y1 + 20, var10 % 8 * 18, 198 + var10 / 8 * 18, 18, 18);
                GlStateManager.popMatrix();
            }
            int y2 = sr2.getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT + this.x - 5;
            int m2 = 15;
            this.mc.fontRendererObj.drawStringWithShadow(PType, sr2.getScaledWidth() - m2 - this.mc.fontRendererObj.getStringWidth(PType) - 1, y2 - this.mc.fontRendererObj.FONT_HEIGHT - 1, Colors.GREEN.c);
            this.mc.fontRendererObj.drawStringWithShadow(d2, sr2.getScaledWidth() - m2 - this.mc.fontRendererObj.getStringWidth(d2) - 1, y2, -1);
            this.x -= 17;
        }
    }

    private int getRainbow(int speed, int offset) {
        float hue = (System.currentTimeMillis() + (long)offset) % (long)speed;
        return Color.getHSBColor(hue /= (float)speed, 1.0f, 1.0f).getRGB();
    }



        private void Direction(String s2, int n3, String string2, int n4) {
        }
    }


