package club.dortware.client.module.impl.player;

import club.dortware.client.event.impl.UpdateEvent;
import club.dortware.client.module.Module;
import club.dortware.client.module.annotations.ModuleData;
import club.dortware.client.module.enums.ModuleCategory;
import club.dortware.client.util.impl.networking.PacketUtil;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;

@ModuleData(name = "Zoot", description = "nigger", category = ModuleCategory.PLAYER)
public class Zoot extends Module {

    @Override
    public void setup() {

    }

    @Subscribe
    public void onMotion(UpdateEvent event) {
        if (event.isPre() && mc.thePlayer.isCollidedVertically) {
            for (int i = 0; i < 50; ++i) {
//                PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 11F, mc.thePlayer.posZ, false));
//                PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, -999.0F, mc.thePlayer.posZ, 0, 0, false));
            }
            Potion[] potions = new Potion[] {Potion.digSlowdown, Potion.blindness, Potion.confusion, Potion.poison, Potion.hunger, Potion.wither};
            for (Potion potion : potions) {
                if (mc.thePlayer.isPotionActive(potion)) {

                }
            }
        }
    }

}
