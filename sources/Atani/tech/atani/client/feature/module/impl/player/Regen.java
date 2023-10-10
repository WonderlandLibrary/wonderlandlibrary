package tech.atani.client.feature.module.impl.player;

import net.minecraft.network.play.client.C03PacketPlayer;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.value.impl.SliderValue;

@ModuleData(name = "Regen", description = "Regenerates health faster", category = Category.MISCELLANEOUS)
public class Regen extends Module {
    private final SliderValue<Integer> health = new SliderValue<Integer>("Health", "At what health should regen work?", this, 15, 1, 19, 0),
            packets = new SliderValue<Integer>("Packets", "How much packets should be sent?", this, 10, 1, 100, 0);

    @Listen
    public void onUpdate(UpdateEvent updateEvent) {
        if(mc.thePlayer.getHealth() < health.getValue().floatValue() && mc.thePlayer.getHealth() > 0) {
            for(int i = 0; i < packets.getValue(); i++) {
                this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround));
            }
        }

    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}
}