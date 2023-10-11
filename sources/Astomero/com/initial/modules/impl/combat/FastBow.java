package com.initial.modules.impl.combat;

import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.settings.*;
import com.initial.events.impl.*;
import net.minecraft.client.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import com.initial.events.*;

public class FastBow extends Module
{
    public ModeSet modes;
    
    public FastBow() {
        super("FastBow", 0, Category.COMBAT);
        this.modes = new ModeSet("Modes", "Vanilla", new String[] { "Vanilla" });
        this.addSettings(this.modes);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate e) {
        this.setDisplayName("Fast Bow");
        final String mode = this.modes.getMode();
        switch (mode) {
            case "Vanilla": {
                if (Minecraft.getMinecraft().thePlayer.getHealth() > 0.0f && (Minecraft.getMinecraft().thePlayer.onGround || Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode) && Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null && this.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow && Minecraft.getMinecraft().gameSettings.keyBindUseItem.pressed) {
                    Minecraft.getMinecraft().playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getCurrentItem());
                    Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem().onItemRightClick(this.mc.thePlayer.inventory.getCurrentItem(), this.mc.theWorld, this.mc.thePlayer);
                    for (int i = 0; i < 20; ++i) {
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
                    }
                    Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
                    this.mc.thePlayer.inventory.getCurrentItem().getItem().onPlayerStoppedUsing(this.mc.thePlayer.inventory.getCurrentItem(), this.mc.theWorld, this.mc.thePlayer, 0);
                    break;
                }
                break;
            }
        }
    }
}
