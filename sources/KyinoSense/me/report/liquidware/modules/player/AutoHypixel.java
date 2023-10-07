/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.play.server.S02PacketChat
 */
package me.report.liquidware.modules.player;

import java.text.DecimalFormat;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.AntiBot;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.AnimationUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import obfuscator.NativeMethod;

@ModuleInfo(name="AutoHypixel", description="AutoHypixel", category=ModuleCategory.MOVEMENT)
public class AutoHypixel
extends Module {
    private final IntegerValue delayValue = new IntegerValue("Delay", 0, 0, 5000);
    private final BoolValue autoGGValue = new BoolValue("AutoGG", true);
    private final BoolValue antiAtlasValue = new BoolValue("AntiAtlas", true);
    private final BoolValue checkValue = new BoolValue("CheckGameMode", true);
    private final BoolValue renderValue = new BoolValue("Render", true);
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Solo", "Teams", "Ranked", "Mega"}, "Solo");
    private final ListValue soloTeamsValue = new ListValue("Solo/Teams Mode", new String[]{"Normal", "Insane"}, "Insane");
    private final ListValue megaValue = new ListValue("MegaMode", new String[]{"Normal", "Doubles"}, "Normal");
    private final MSTimer timer = new MSTimer();
    public static String gameMode = "NONE";
    public boolean shouldChangeGame;
    public boolean useOtherWord = false;
    private final DecimalFormat dFormat = new DecimalFormat("0.0");
    private float posY = -20.0f;
    private final String[] strings = new String[]{"1st Killer - ", "1st Place - ", "You died! Want to play again? Click here!", "You won! Want to play again? Click here!", " - Damage Dealt - ", "1st - ", "Winning Team - ", "Winners: ", "Winner: ", "Winning Team: ", " win the game!", "1st Place: ", "Last team standing!", "Winner #1 (", "Top Survivors", "Winners - "};

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onEnable() {
        this.shouldChangeGame = false;
        this.timer.reset();
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onRender2D(Render2DEvent event) {
        if (((Boolean)this.checkValue.get()).booleanValue() && !gameMode.toLowerCase().contains("skywars")) {
            return;
        }
        ScaledResolution sc = new ScaledResolution(mc);
        float middleX = (float)sc.func_78326_a() / 2.0f;
        String detail = "Sending you to another game in " + this.dFormat.format((float)this.timer.hasTimeLeft(((Integer)this.delayValue.get()).intValue()) / 1000.0f) + "s...";
        float middleWidth = (float)Fonts.font40.func_78256_a(detail) / 2.0f;
        this.posY = AnimationUtils.animate(this.shouldChangeGame ? 10.0f : -20.0f, this.posY, 0.25f);
        if (!((Boolean)this.renderValue.get()).booleanValue() || this.posY < -15.0f) {
            return;
        }
        RenderUtils.drawRoundedRect(middleX - 5.0f - middleWidth, this.posY, middleX + 5.0f + middleWidth, this.posY + 15.0f, 3.0f, -1610612736);
        GlStateManager.func_179117_G();
        Fonts.font35.drawString(detail, middleX - middleWidth - 1.0f, this.posY + 4.0f, -1);
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onMotion(MotionEvent event) {
        if ((!((Boolean)this.checkValue.get()).booleanValue() || gameMode.toLowerCase().contains("skywars")) && this.shouldChangeGame && this.timer.hasTimePassed(((Integer)this.delayValue.get()).intValue())) {
            if (((Boolean)this.antiAtlasValue.get()).booleanValue()) {
                for (EntityPlayer entity : AutoHypixel.mc.field_71441_e.field_73010_i) {
                    if (entity == null && (AutoHypixel.mc.field_71439_g.field_70173_aa % 10 == 0 || entity == AutoHypixel.mc.field_71439_g) || LiquidBounce.moduleManager.getModule(AntiBot.class).getState() && AntiBot.isBot((EntityLivingBase)entity)) continue;
                    AutoHypixel.mc.field_71439_g.func_71165_d("/wdr " + entity.func_70005_c_() + (this.useOtherWord ? " ka,speed,velocity" : " aimbot,safewalk"));
                    this.useOtherWord = !this.useOtherWord;
                }
            }
            AutoHypixel.mc.field_71439_g.func_71165_d("/play " + ((String)this.modeValue.get()).toLowerCase() + (((String)this.modeValue.get()).equalsIgnoreCase("ranked") ? "_normal" : (((String)this.modeValue.get()).equalsIgnoreCase("mega") ? "_" + ((String)this.megaValue.get()).toLowerCase() : "_" + ((String)this.soloTeamsValue.get()).toLowerCase())));
            this.shouldChangeGame = false;
        }
        if (!this.shouldChangeGame) {
            this.timer.reset();
        }
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onPacket(PacketEvent event) {
        S02PacketChat chat;
        if (event.getPacket() instanceof S02PacketChat && (chat = (S02PacketChat)event.getPacket()).func_148915_c() != null) {
            for (String s : this.strings) {
                if (!chat.func_148915_c().func_150260_c().contains(s)) continue;
                if (((Boolean)this.autoGGValue.get()).booleanValue() && chat.func_148915_c().func_150260_c().contains(this.strings[3])) {
                    AutoHypixel.mc.field_71439_g.func_71165_d("GG");
                }
                this.shouldChangeGame = true;
                break;
            }
        }
    }
}

