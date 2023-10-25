/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.player;

import de.Hero.settings.Setting;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlow
extends Module {
    Timer timer;
    Timer timer2 = new Timer();

    @Override
    public void onDisable() {
        super.onDisable();
        NoSlow.mc.timer.timerSpeed = 1.0f;
    }

    public NoSlow() {
        super("NoSlow", 0, Category.PLAYER, "Move while blocking.");
        this.timer = new Timer();
    }

    @Override
    public void setup() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("NCP");
        arrayList.add("Vanilla");
        arrayList.add("Hypixel");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("NoSlow Mode", (Module)this, "NCP", arrayList));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("ReleaseUseItemHypixel", this, false));
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("NoSlow Mode").getValString();
        this.setDisplayName("No Slow \ufffdf" + string);
        boolean bl = Exodus.INSTANCE.settingsManager.getSettingByModule("ReleaseUseItemHypixel", this).getValBoolean();
        if (this.timer.hasTimeElapsed(200L, true)) {
            if (Minecraft.thePlayer.getHeldItem() != null) {
                if (Minecraft.thePlayer.getHeldItem().getItem() != null) {
                    if (Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemSword && string.equalsIgnoreCase("Hypixel")) {
                        if (Minecraft.thePlayer.ticksExisted % 3 == 0 && bl) {
                            Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        }
                    }
                }
            }
        }
        if (string.equalsIgnoreCase("NCP") && this.timer.hasTimeElapsed(100L, true)) {
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
        }
    }
}

