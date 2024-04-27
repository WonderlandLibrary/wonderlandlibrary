package me.AquaVit.liquidSense.modules.world;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.util.ChatComponentText;

@ModuleInfo(name = "LightningCheck", description = "Lightning x y z", category = ModuleCategory.WORLD)
public class LightningCheck extends Module {

    public static int x;
    public static int y;
    public static int z;

    @EventTarget
    public void onPacket(PacketEvent event) {
        final Packet<?> packet = event.getPacket();
        if(packet instanceof S2CPacketSpawnGlobalEntity) {
            S2CPacketSpawnGlobalEntity sb = (S2CPacketSpawnGlobalEntity)packet;
            if(sb.func_149053_g() == 1) {
                x = (int) ((double) sb.func_149051_d() / 32.0D);
                y = (int) ((double) sb.func_149050_e() / 32.0D);
                z = (int) ((double) sb.func_149049_f() / 32.0D);
                mc.thePlayer.addChatMessage(new ChatComponentText("§8[§9§l" + LiquidBounce.CLIENT_NAME+ "§8] §3"+ "Found Lightning X:" + x + " -Y:" + y + " -Z:" + z));
            }
        }
    }


}