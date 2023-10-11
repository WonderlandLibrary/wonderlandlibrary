package com.initial.modules.impl.movement;

import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.settings.*;
import com.initial.events.impl.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import com.initial.utils.network.*;
import net.minecraft.network.*;
import org.apache.commons.lang3.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;

public class NoSlow extends Module
{
    public ModeSet mode;
    
    public NoSlow() {
        super("NoSlow", 0, Category.MOVEMENT);
        this.mode = new ModeSet("Mode", "Vanilla", new String[] { "Vanilla", "NCP" });
        this.addSettings(this.mode);
    }
    
    @Override
    public void onEvent(final EventNigger e) {
        this.setDisplayName("No Slow");
        final String mode = this.mode.getMode();
        switch (mode) {
            case "NCP": {
                if (!this.mc.thePlayer.isBlocking()) {
                    break;
                }
                if (e.isPre()) {
                    PacketUtil.sendPacketSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -6, -1), EnumFacing.DOWN));
                    break;
                }
                this.mc.thePlayer.setItemInUse(this.mc.thePlayer.getCurrentEquippedItem(), RandomUtils.nextInt(1, Integer.MAX_VALUE));
                this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getCurrentEquippedItem());
                break;
            }
        }
    }
}
