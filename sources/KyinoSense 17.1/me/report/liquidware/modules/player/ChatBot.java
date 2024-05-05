/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.report.liquidware.modules.player;

import java.util.Random;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.minecraft.entity.player.EntityPlayer;
import obfuscator.NativeMethod;

@ModuleInfo(name="ChatBot", description="skidder", category=ModuleCategory.PLAYER)
public class ChatBot
extends Module {
    private final TextValue content = new TextValue("Content", "KyinoClient >>Report.gg<<");
    private final IntegerValue delay = new IntegerValue("Delay", 60, 10, 5000);
    private final MSTimer timer = new MSTimer();

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onUpdate(UpdateEvent event) {
        if (!this.timer.hasTimePassed((Integer)this.delay.get() * 1000)) {
            return;
        }
        EntityPlayer[] players = ChatBot.mc.field_71441_e.field_73010_i.toArray(new EntityPlayer[0]);
        Random random = new Random();
        String playerName = players[random.nextInt(players.length)].func_70005_c_();
        ChatBot.mc.field_71439_g.func_71165_d("/tell " + playerName + " " + (String)this.content.get());
    }
}

