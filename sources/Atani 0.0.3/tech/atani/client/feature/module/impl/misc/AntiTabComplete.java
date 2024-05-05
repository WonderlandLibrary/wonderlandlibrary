package tech.atani.client.feature.module.impl.misc;

import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;
import tech.atani.client.listener.event.minecraft.network.PacketEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;

@ModuleData(name = "AntiTabComplete", description = "Prevent people from 'tabing' your name", category = Category.MISCELLANEOUS)
public class AntiTabComplete extends Module {

    @Listen
    public void onPacket(PacketEvent event) {
    	if(Methods.mc.thePlayer == null || Methods.mc.theWorld == null)
    		return;
        if (event.getPacket() instanceof C14PacketTabComplete || event.getPacket() instanceof S3APacketTabComplete) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}
}