/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.player;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventMotion;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Options;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;

public class FastUse
extends Module {
    private String MODE = "MODE";

    public FastUse(ModuleData data) {
        super(data);
        this.settings.put(this.MODE, new Setting<Options>(this.MODE, new Options("Use Mode", "Packet", new String[]{"Packet", "Timer"}), "Bypass method."));
    }

    @Override
    public void onEnable() {
        super.onEnable();
        FastUse.mc.timer.timerSpeed = 1.0f;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        FastUse.mc.timer.timerSpeed = 1.0f;
    }

    @RegisterEvent(events={EventMotion.class})
    public void onEvent(Event event) {
        EventMotion em;
        if (event instanceof EventMotion && (em = (EventMotion)event).isPre()) {
            String str = ((Options)((Setting)this.settings.get(this.MODE)).getValue()).getSelected();
            this.setSuffix(str);
            switch (str) {
                case "Timer": {
                    if (FastUse.mc.thePlayer.getItemInUseDuration() > 16 && !(FastUse.mc.thePlayer.getItemInUse().getItem() instanceof ItemSword) && !(FastUse.mc.thePlayer.getItemInUse().getItem() instanceof ItemBow)) {
                        FastUse.mc.timer.timerSpeed = 1.3f;
                        break;
                    }
                    if (FastUse.mc.timer.timerSpeed != 1.3f) break;
                    FastUse.mc.timer.timerSpeed = 1.0f;
                    break;
                }
                case "Packet": {
                    if (FastUse.mc.thePlayer.getItemInUseDuration() != 12 || FastUse.mc.thePlayer.getItemInUse().getItem() instanceof ItemSword || FastUse.mc.thePlayer.getItemInUse().getItem() instanceof ItemBow) break;
                    for (int i = 0; i < 30; ++i) {
                        FastUse.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                    }
                    mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    FastUse.mc.thePlayer.stopUsingItem();
                }
            }
        }
    }
}

