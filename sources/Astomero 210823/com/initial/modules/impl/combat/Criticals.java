package com.initial.modules.impl.combat;

import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.settings.*;
import net.minecraft.network.play.client.*;
import com.initial.events.*;
import com.initial.events.impl.*;
import net.minecraft.entity.*;
import com.initial.utils.network.*;
import java.util.concurrent.*;
import com.mojang.realmsclient.gui.*;
import com.initial.*;
import com.initial.utils.movement.*;

public class Criticals extends Module
{
    public ModeSet mode;
    public DoubleSet hurttime;
    int safeTicks;
    
    public Criticals() {
        super("Criticals", 0, Category.COMBAT);
        this.mode = new ModeSet("Mode", "Morgan", new String[] { "Watchdog", "Packet", "Morgan", "Spartan", "NCP", "Verus Disabled", "NoGround", "Watchdog New" });
        this.hurttime = new DoubleSet("HurtTime", 5.0, 1.0, 10.0, 1.0);
        this.safeTicks = 0;
        this.addSettings(this.mode, this.hurttime);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if (this.mode.getMode().equalsIgnoreCase("NoGround")) {
            this.mc.thePlayer.jump();
        }
    }
    
    @EventTarget
    public void onSend(final EventSendPacket e) {
        if (e.getPacket() instanceof C03PacketPlayer && this.mode.getMode().equalsIgnoreCase("NoGround")) {
            final C03PacketPlayer modify = (C03PacketPlayer)e.getPacket();
            modify.onGround = false;
        }
    }
    
    @EventTarget
    public void onPre(final EventMotion e) {
        if (this.mode.getMode().equalsIgnoreCase("watchdog new")) {
            this.setDisplayName("Watchdog New");
        }
        else {
            this.setDisplayName("Criticals §7" + this.mode.getMode() + " " + Math.round(this.hurttime.getValue()));
        }
        ++this.safeTicks;
    }
    
    @EventTarget
    public void onAttack(final EventAttack e) {
        final EntityLivingBase ent = (EntityLivingBase)e.getEntity();
        final double x = this.mc.thePlayer.posX;
        final double y = this.mc.thePlayer.posY;
        final double z = this.mc.thePlayer.posZ;
        if ((ent.hurtTime < this.hurttime.getValue() || ent.hurtTime == 0) && this.shouldCrit()) {
            final String mode = this.mode.getMode();
            switch (mode) {
                case "NCP": {
                    PacketUtil.sendC04(x, y + 0.11, z, false, false);
                    PacketUtil.sendC04(x, y, z, false, false);
                    break;
                }
                case "Spartan": {
                    PacketUtil.sendC04(x, y + 0.2, z, false, false);
                    PacketUtil.sendC04(x, y, z, false, false);
                    break;
                }
                case "Morgan": {
                    PacketUtil.sendC04(x, y + 0.00124 + ThreadLocalRandom.current().nextDouble(1.0E-4, 9.0E-4), z, false, false);
                    PacketUtil.sendC04(x, y + 8.5E-4, z, false, false);
                    break;
                }
                case "Packet": {
                    PacketUtil.sendC04(x, y + 0.00424, z, false, false);
                    PacketUtil.sendC04(x, y + 8.5E-4, z, false, false);
                    PacketUtil.sendC04(x, y, z, false, false);
                    break;
                }
                case "Verus Disabled": {
                    if (ent.hurtTime != 0) {
                        PacketUtil.sendC04(x, y + 0.42, z, false, false);
                        PacketUtil.sendC04(x, y, z, false, false);
                        break;
                    }
                    if (this.safeTicks > 4) {
                        PacketUtil.sendC04(x, y + 0.42, z, false, false);
                        PacketUtil.sendC04(x, y, z, false, false);
                        this.safeTicks = 0;
                        break;
                    }
                    break;
                }
                case "Watchdog": {
                    if (ent.hurtTime != 0) {
                        Astomero.addChatMessage(ChatFormatting.RED + "Criticals: " + ChatFormatting.WHITE + "Non-0 Crit!");
                        PacketUtil.sendC04(x, y + 0.11, z, false, false);
                        PacketUtil.sendC04(x, y + 0.1100013579, z, false, false);
                        PacketUtil.sendC04(x, y + 0.1090013579, z, false, false);
                        break;
                    }
                    if (this.safeTicks > 8) {
                        Astomero.addChatMessage(ChatFormatting.RED + "Criticals: " + ChatFormatting.WHITE + "Safe 0HT Crit!");
                        PacketUtil.sendC04(x, y + 0.11, z, false, false);
                        PacketUtil.sendC04(x, y + 0.1100013579, z, false, false);
                        PacketUtil.sendC04(x, y + 0.1090013579, z, false, false);
                        this.safeTicks = 0;
                        break;
                    }
                    break;
                }
                case "Watchdog New": {
                    Criticals.localPlayer.motionY = 0.03999999910593033;
                    if (Criticals.localPlayer.onGround) {
                        MovementUtils.multMotionBy(0.9800000190734863);
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    public boolean shouldCrit() {
        final boolean isRealGround = this.mc.thePlayer.onGround && MovementUtils.getOnRealGround(this.mc.thePlayer, 1.0E-4) && this.mc.thePlayer.isCollidedVertically;
        return isRealGround && !this.mc.thePlayer.isInWater() && !this.mc.thePlayer.isOnLadder();
    }
}
