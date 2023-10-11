package com.initial.modules.impl.combat;

import com.initial.settings.impl.*;
import com.initial.utils.player.*;
import com.initial.modules.*;
import com.initial.settings.*;
import com.initial.events.impl.*;
import net.minecraft.init.*;
import com.initial.utils.network.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import com.initial.events.*;

public class AutoHeal extends Module
{
    public ModeSet mode;
    public DoubleSet healthToHeal;
    public DoubleSet timeToHealAgain;
    TimerHelper timer;
    
    public AutoHeal() {
        super("AutoHeal", 0, Category.COMBAT);
        this.mode = new ModeSet("Mode", "Packet Gapple", new String[] { "Packet Gapple", "Golden Head" });
        this.healthToHeal = new DoubleSet("Health", 15.0, 2.0, 20.0, 1.0);
        this.timeToHealAgain = new DoubleSet("Delay", 500.0, 250.0, 3000.0, 10.0, "ms");
        this.timer = new TimerHelper();
        this.addSettings(this.mode, this.healthToHeal, this.timeToHealAgain);
    }
    
    @EventTarget
    public void onPre(final EventMotion e) {
        this.setDisplayName("Auto Heal §7" + String.valueOf(this.healthToHeal.getValue()));
        if (!this.canHeal()) {
            return;
        }
        if (this.shouldHeal()) {
            final String mode = this.mode.getMode();
            switch (mode) {
                case "Packet Gapple": {
                    for (int a = 0; a < 9; ++a) {
                        if (this.mc.thePlayer.inventory.getStackInSlot(a) != null) {
                            final boolean shouldChange = this.mc.thePlayer.inventory.getStackInSlot(a).getItem() == Items.golden_apple;
                            if (shouldChange) {
                                PacketUtil.sendPacket(new C09PacketHeldItemChange(a));
                                PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getStackInSlot(a)));
                                for (int repeatPacket = 0; repeatPacket < 35; ++repeatPacket) {
                                    PacketUtil.sendPacket(new C03PacketPlayer());
                                }
                                PacketUtil.sendPacket(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
                                break;
                            }
                        }
                    }
                    break;
                }
                case "Golden Head": {
                    for (int a = 0; a < 9; ++a) {
                        if (this.mc.thePlayer.inventory.getStackInSlot(a) != null) {
                            final boolean isSafeToSpoof = EnumChatFormatting.getTextWithoutFormattingCodes(this.mc.thePlayer.inventory.getStackInSlot(a).getDisplayName()).equalsIgnoreCase("golden head");
                            if (isSafeToSpoof) {
                                PacketUtil.sendPacket(new C09PacketHeldItemChange(a));
                                PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getStackInSlot(a)));
                                PacketUtil.sendPacket(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
                            }
                        }
                    }
                    break;
                }
            }
            this.timer.reset();
        }
    }
    
    private boolean shouldHeal() {
        return this.mc.thePlayer.getHealth() < this.healthToHeal.getValue() && this.timer.timeElapsed((long)this.timeToHealAgain.getValue());
    }
    
    private boolean canHeal() {
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Packet Gapple": {
                for (int a = 0; a < 9; ++a) {
                    if (this.mc.thePlayer.inventory.getStackInSlot(a) != null) {
                        final boolean isSafeToSpoof = this.mc.thePlayer.inventory.getStackInSlot(a).getItem() == Items.golden_apple;
                        if (isSafeToSpoof) {
                            return true;
                        }
                    }
                }
                break;
            }
            case "Golden Head": {
                for (int a = 0; a < 9; ++a) {
                    if (this.mc.thePlayer.inventory.getStackInSlot(a) != null) {
                        final boolean isSafeToSpoof = EnumChatFormatting.getTextWithoutFormattingCodes(this.mc.thePlayer.inventory.getStackInSlot(a).getDisplayName()).equalsIgnoreCase("golden head");
                        if (isSafeToSpoof) {
                            return true;
                        }
                    }
                }
                break;
            }
        }
        return false;
    }
}
