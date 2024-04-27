package me.aquavit.liquidsense.module.modules.misc;

import com.google.gson.JsonObject;
import io.netty.buffer.Unpooled;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.WorldEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

@ModuleInfo(name = "HYTBypass", description = ":/", category = ModuleCategory.MISC)
public class HYTBypass extends Module {

    @EventTarget
    public void onWorld(final WorldEvent e) {
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("base64", "ecbeb575677ab9a37410748a5f429f9f");
                jsonObject.addProperty("cltitle", "\u6211\u7684\u4e16\u754c 1.8.9");
                jsonObject.addProperty("isLiquidbounce", false);
                jsonObject.addProperty("path", "mixins.mcwrapper.json");
                jsonObject.addProperty("player", mc.thePlayer.getName());

                PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
                packetBuffer.writeString(jsonObject.toString());
                C17PacketCustomPayload antiCheat = new C17PacketCustomPayload("AntiCheat", packetBuffer);
                mc.getNetHandler().getNetworkManager().sendPacket(antiCheat);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
    }
}

