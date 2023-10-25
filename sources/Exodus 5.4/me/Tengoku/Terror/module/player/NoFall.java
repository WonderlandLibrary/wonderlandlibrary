/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.player;

import de.Hero.settings.Setting;
import io.netty.util.internal.ThreadLocalRandom;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventMotion;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

public class NoFall
extends Module {
    int fallDistance = 0;

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("NoFall Mode").getValString();
        this.setDisplayName("NoFall \ufffdf" + string);
    }

    public NoFall() {
        super("No Fall", 36, Category.PLAYER, "Prevents fall damage.");
    }

    @Override
    public void setup() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("NoGround");
        arrayList.add("Watchdog");
        arrayList.add("Spoof");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("NoFall Mode", (Module)this, "Watchdog", arrayList));
    }

    @EventTarget
    public void onPre(EventMotion eventMotion) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("NoFall Mode").getValString();
        if (string.equalsIgnoreCase("Watchdog")) {
            if (Minecraft.thePlayer.fallDistance > 3.0f) {
                int n = 0;
                int n2 = (int)Minecraft.thePlayer.posY;
                while (n2 > 0) {
                    if (Minecraft.theWorld.isAirBlock(new BlockPos(Minecraft.thePlayer.posX, (double)n2, Minecraft.thePlayer.posZ))) {
                        ++n;
                    }
                    --n2;
                }
                if ((double)n < Minecraft.thePlayer.posY - 1.0) {
                    eventMotion.setOnGround(true);
                    Minecraft.thePlayer.fallDistance = 0.0f;
                }
            }
        }
        if (string.equalsIgnoreCase("NoGround")) {
            if (Minecraft.thePlayer.fallDistance > 2.0f) {
                eventMotion.setOnGround(true);
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(ThreadLocalRandom.current().nextBoolean()));
                eventMotion.setOnGround(ThreadLocalRandom.current().nextBoolean());
            }
        }
        if (string.equalsIgnoreCase("Spoof")) {
            if (Minecraft.thePlayer.fallDistance > 2.0f) {
                this.fallDistance += 5;
                if (this.fallDistance >= 10) {
                    eventMotion.setOnGround(true);
                }
                if (Minecraft.thePlayer.hurtTime > 0) {
                    Minecraft.thePlayer.fallDistance = 0.0f;
                }
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.fallDistance = 0;
        NoFall.mc.timer.timerSpeed = 1.0f;
    }
}

