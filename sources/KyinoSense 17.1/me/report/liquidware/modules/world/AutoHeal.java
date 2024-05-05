/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.network.play.server.S02PacketChat
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.MathHelper
 *  org.apache.commons.lang3.tuple.Pair
 */
package me.report.liquidware.modules.world;

import java.util.Random;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import obfuscator.NativeMethod;
import org.apache.commons.lang3.tuple.Pair;

@ModuleInfo(name="AutoHeal", description="AutoHeal", category=ModuleCategory.WORLD)
public class AutoHeal
extends Module {
    private final FloatValue percent = new FloatValue("HealthPercent", 75.0f, 1.0f, 100.0f);
    private final IntegerValue min = new IntegerValue("MinDelay", 75, 1, 5000);
    private final IntegerValue max = new IntegerValue("MaxDelay", 125, 1, 5000);
    private final FloatValue regenSec = new FloatValue("RegenSec", 4.6f, 0.0f, 10.0f);
    private final BoolValue groundCheck = new BoolValue("GroundCheck", false);
    private final BoolValue voidCheck = new BoolValue("VoidCheck", true);
    private final BoolValue waitRegen = new BoolValue("WaitRegen", true);
    private final BoolValue invCheck = new BoolValue("InvCheck", false);
    private final BoolValue absorpCheck = new BoolValue("AbsorpCheck", true);
    final MSTimer timer = new MSTimer();
    int delay;
    boolean isDisable;

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onEnable() {
        super.onEnable();
        this.timer.reset();
        this.isDisable = false;
        this.delay = MathHelper.func_76136_a((Random)new Random(), (int)((Integer)this.min.get()), (int)((Integer)this.max.get()));
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onPacket(PacketEvent e) {
        if (e.getPacket() instanceof S02PacketChat && ((S02PacketChat)e.getPacket()).func_148915_c().func_150254_d().contains("\u00a7r\u00a77 won the game! \u00a7r\u00a7e\u272a\u00a7r")) {
            ClientUtils.displayChatMessage("\u00a7f[\u00a7cSLHeal\u00a7f] \u00a76Temp Disable Heal");
            this.isDisable = true;
        }
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onUpdate(UpdateEvent event) {
        if (AutoHeal.mc.field_71439_g.field_70173_aa <= 5 && this.isDisable) {
            this.isDisable = false;
            ClientUtils.displayChatMessage("\u00a7f[\u00a7cSLHeal\u00a7f] \u00a76Enable Heal due to World Changed or Player Respawned");
        }
        int absorp = MathHelper.func_76143_f((double)AutoHeal.mc.field_71439_g.func_110139_bj());
        if ((Boolean)this.groundCheck.get() != false && !AutoHeal.mc.field_71439_g.field_70122_E || (Boolean)this.voidCheck.get() != false && !MovementUtils.isBlockUnder() || (Boolean)this.invCheck.get() != false && AutoHeal.mc.field_71462_r instanceof GuiContainer || absorp != 0 && ((Boolean)this.absorpCheck.get()).booleanValue()) {
            return;
        }
        if (((Boolean)this.waitRegen.get()).booleanValue() && AutoHeal.mc.field_71439_g.func_70644_a(Potion.field_76428_l) && (float)AutoHeal.mc.field_71439_g.func_70660_b(Potion.field_76428_l).func_76459_b() > ((Float)this.regenSec.get()).floatValue() * 20.0f) {
            return;
        }
        Pair<Integer, ItemStack> pair = this.getGAppleSlot();
        if (!this.isDisable && pair != null && (AutoHeal.mc.field_71439_g.func_110143_aJ() <= ((Float)this.percent.get()).floatValue() / 100.0f * AutoHeal.mc.field_71439_g.func_110138_aP() || !AutoHeal.mc.field_71439_g.func_70644_a(Potion.field_76444_x) || absorp == 0 && AutoHeal.mc.field_71439_g.func_110143_aJ() == 20.0f && AutoHeal.mc.field_71439_g.func_70644_a(Potion.field_76444_x)) && this.timer.hasTimePassed(this.delay)) {
            ClientUtils.displayChatMessage("\u00a7f[\u00a7cSLHeal\u00a7f] \u00a76Healed");
            int lastSlot = AutoHeal.mc.field_71439_g.field_71071_by.field_70461_c;
            int slot = (Integer)pair.getLeft();
            mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(slot));
            ItemStack stack = (ItemStack)pair.getRight();
            mc.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(stack));
            for (int i = 0; i < 32; ++i) {
                mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer());
            }
            mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(lastSlot));
            AutoHeal.mc.field_71439_g.field_71071_by.field_70461_c = lastSlot;
            AutoHeal.mc.field_71442_b.func_78765_e();
            this.delay = MathHelper.func_76136_a((Random)new Random(), (int)((Integer)this.min.get()), (int)((Integer)this.max.get()));
            this.timer.reset();
        }
    }

    private Pair<Integer, ItemStack> getGAppleSlot() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = AutoHeal.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack == null || stack.func_77973_b() != Items.field_151153_ao) continue;
            return Pair.of((Object)i, (Object)stack);
        }
        return null;
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public String getTag() {
        return AutoHeal.mc.field_71439_g == null || (double)AutoHeal.mc.field_71439_g.func_110143_aJ() == Double.NaN ? null : String.format("%.2f HP", Float.valueOf(((Float)this.percent.get()).floatValue() / 100.0f * AutoHeal.mc.field_71439_g.func_110138_aP()));
    }
}

