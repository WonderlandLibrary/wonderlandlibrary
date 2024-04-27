package me.AquaVit.liquidSense.modules.render;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

@ModuleInfo(name = "Ambience", description = "Ambience", category = ModuleCategory.RENDER)
public class Ambience extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[] {"Daytime","Night","DIY","DayWithNight"}, "Night");
    private final IntegerValue time = new IntegerValue("DIYTime", 13000, 0, 24000);
    private final IntegerValue don = new IntegerValue("DayWithNight", 10, 1, 24000);
    int a;
    MSTimer Timer = new MSTimer();
    @Override
    public void onEnable() {
        a = 0;
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        final Packet<?> packet = event.getPacket();
        if (packet instanceof S03PacketTimeUpdate) {
            event.cancelEvent();
        }
    }
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (this.Timer.hasPassed(1F)) {
            ++this.a;
            this.a = this.a + don.get();
            this.Timer.reSet();
        }
        if (a >= 24000) {
            a = 0;
        }

        if(modeValue.get().equalsIgnoreCase("Daytime")){
            mc.theWorld.setWorldTime((long)1000);
        }
        if(modeValue.get().equalsIgnoreCase("Night")){
            mc.theWorld.setWorldTime((long)16000);
        }
        if(modeValue.get().equalsIgnoreCase("DIY")){
            mc.theWorld.setWorldTime(time.get());
        }
        if(modeValue.get().equalsIgnoreCase("DayWithNight")){
            mc.theWorld.setWorldTime(a);
        }

    }

}
