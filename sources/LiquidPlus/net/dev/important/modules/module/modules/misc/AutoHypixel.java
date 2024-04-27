/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.network.play.server.S02PacketChat
 *  net.minecraft.util.MathHelper
 */
package net.dev.important.modules.module.modules.misc;

import java.awt.Color;
import java.text.DecimalFormat;
import net.dev.important.event.EventTarget;
import net.dev.important.event.MotionEvent;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.Render2DEvent;
import net.dev.important.gui.font.Fonts;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.AnimationUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.utils.render.Stencil;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.value.BoolValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.dev.important.value.TextValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.MathHelper;

@Info(name="AutoHypixel", spacedName="Auto Hypixel", description="Automatically send you into random games on Hypixel after you die or win.", category=Category.MISC, cnName="\u81ea\u52a8\u6d77\u50cf\u7d20")
public class AutoHypixel
extends Module {
    private final IntegerValue delayValue = new IntegerValue("Delay", 0, 0, 5000, "ms");
    private final BoolValue autoGGValue = new BoolValue("Auto-GG", true);
    private final TextValue ggMessageValue = new TextValue("GG-Message", "gOoD GaMe", () -> (Boolean)this.autoGGValue.get());
    private final BoolValue checkValue = new BoolValue("CheckGameMode", true);
    private final BoolValue antiSnipeValue = new BoolValue("AntiSnipe", true);
    private final BoolValue renderValue = new BoolValue("Render", true);
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Solo", "Teams", "Ranked", "Mega"}, "Solo");
    private final ListValue soloTeamsValue = new ListValue("Solo/Teams-Mode", new String[]{"Normal", "Insane"}, "Insane", () -> ((String)this.modeValue.get()).equalsIgnoreCase("solo") || ((String)this.modeValue.get()).equalsIgnoreCase("teams"));
    private final ListValue megaValue = new ListValue("Mega-Mode", new String[]{"Normal", "Doubles"}, "Normal", () -> ((String)this.modeValue.get()).equalsIgnoreCase("mega"));
    private final MSTimer timer = new MSTimer();
    public static String gameMode = "NONE";
    public boolean shouldChangeGame;
    public boolean useOtherWord = false;
    private final DecimalFormat dFormat = new DecimalFormat("0.0");
    private float posY = -20.0f;
    private final String[] strings = new String[]{"1st Killer -", "1st Place -", "died! Want to play again? Click here!", "won! Want to play again? Click here!", "- Damage Dealt -", "1st -", "Winning Team -", "Winners:", "Winner:", "Winning Team:", " win the game!", "1st Place:", "Last team standing!", "Winner #1 (", "Top Survivors", "Winners -"};

    @Override
    public void onEnable() {
        this.shouldChangeGame = false;
        this.timer.reset();
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        if (((Boolean)this.checkValue.get()).booleanValue() && !gameMode.toLowerCase().contains("skywars")) {
            return;
        }
        ScaledResolution sc = new ScaledResolution(mc);
        float middleX = (float)sc.func_78326_a() / 2.0f;
        String detail = "Next game in " + this.dFormat.format((float)this.timer.hasTimeLeft(((Integer)this.delayValue.get()).intValue()) / 1000.0f) + "s...";
        float middleWidth = (float)Fonts.font40.func_78256_a(detail) / 2.0f;
        float strength = MathHelper.func_76131_a((float)((float)this.timer.hasTimeLeft(((Integer)this.delayValue.get()).intValue()) / (float)((Integer)this.delayValue.get()).intValue()), (float)0.0f, (float)1.0f);
        float wid = strength * (5.0f + middleWidth) * 2.0f;
        this.posY = AnimationUtils.animate(this.shouldChangeGame ? 10.0f : -20.0f, this.posY, 0.0125f * (float)RenderUtils.deltaTime);
        if (!((Boolean)this.renderValue.get()).booleanValue() || this.posY < -15.0f) {
            return;
        }
        Stencil.write(true);
        RenderUtils.drawRoundedRect(middleX - 5.0f - middleWidth, this.posY, middleX + 5.0f + middleWidth, this.posY + 15.0f, 3.0f, -1610612736);
        Stencil.erase(true);
        RenderUtils.drawRect(middleX - 5.0f - middleWidth, this.posY, middleX - 5.0f - middleWidth + wid, this.posY + 15.0f, new Color(0.4f, 0.8f, 0.4f, 0.35f).getRGB());
        Stencil.dispose();
        GlStateManager.func_179117_G();
        Fonts.fontSFUI40.drawString(detail, middleX - middleWidth - 1.0f, this.posY + 4.0f, -1);
    }

    @EventTarget
    public void onMotion(MotionEvent event) {
        if ((!((Boolean)this.checkValue.get()).booleanValue() || gameMode.toLowerCase().contains("skywars")) && this.shouldChangeGame && this.timer.hasTimePassed(((Integer)this.delayValue.get()).intValue())) {
            AutoHypixel.mc.field_71439_g.func_71165_d("/play " + ((String)this.modeValue.get()).toLowerCase() + (((String)this.modeValue.get()).equalsIgnoreCase("ranked") ? "_normal" : (((String)this.modeValue.get()).equalsIgnoreCase("mega") ? "_" + ((String)this.megaValue.get()).toLowerCase() : "_" + ((String)this.soloTeamsValue.get()).toLowerCase())));
            this.shouldChangeGame = false;
        }
        if (!this.shouldChangeGame) {
            this.timer.reset();
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        S02PacketChat chat;
        if (event.getPacket() instanceof S02PacketChat && (chat = (S02PacketChat)event.getPacket()).func_148915_c() != null) {
            if (((Boolean)this.antiSnipeValue.get()).booleanValue() && chat.func_148915_c().func_150260_c().contains("Sending you to")) {
                event.cancelEvent();
                return;
            }
            for (String s : this.strings) {
                if (!chat.func_148915_c().func_150260_c().contains(s)) continue;
                if (((Boolean)this.autoGGValue.get()).booleanValue() && chat.func_148915_c().func_150260_c().contains(this.strings[3])) {
                    AutoHypixel.mc.field_71439_g.func_71165_d((String)this.ggMessageValue.get());
                }
                this.shouldChangeGame = true;
                break;
            }
        }
    }
}

