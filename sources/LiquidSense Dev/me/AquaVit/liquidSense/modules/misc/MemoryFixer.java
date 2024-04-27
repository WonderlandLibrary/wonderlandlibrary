package me.AquaVit.liquidSense.modules.misc;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;

@ModuleInfo(name = "MemoryFixer", description = "MemoryFixer", category = ModuleCategory.MISC)
public class MemoryFixer extends Module {

    TimeUtils timer = new TimeUtils();
    @EventTarget
    public void onTick(){
        if(timer.hasReached(3000)) {
            System.gc();
            //Runtime.getRuntime().gc();
            timer.reset();
        }

    }
}
