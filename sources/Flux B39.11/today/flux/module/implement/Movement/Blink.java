package today.flux.module.implement.Movement;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import today.flux.event.PacketSendEvent;
import today.flux.module.Category;
import today.flux.module.Module;

import java.util.ArrayList;

public class Blink extends Module {

	public Blink() {
		super("Blink", Category.Movement, false);
	}

	private EntityOtherPlayerMP player;
	private ArrayList<Packet> packets = new ArrayList<>();

	public void onEnable() {
		super.onEnable();

		(player = new EntityOtherPlayerMP(this.mc.theWorld, this.mc.thePlayer.getGameProfile())).clonePlayer(this.mc.thePlayer, true);
		player.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
		player.rotationYawHead = this.mc.thePlayer.rotationYaw;
		player.rotationPitch = this.mc.thePlayer.rotationPitch;
		player.setSneaking(this.mc.thePlayer.isSneaking());

		this.mc.theWorld.addEntityToWorld(-1337, player);
	}

	@EventTarget
	public void onPacketSend(PacketSendEvent event) {
		event.setCancelled(true);
		packets.add(event.getPacket());
	}

	public void onDisable() {
        super.onDisable();

        if (this.mc.theWorld == null)
            return;

        if (player != null) this.mc.theWorld.removeEntity(player);

        for (Packet packet : packets) {
            this.mc.getNetHandler().getNetworkManager().sendPacket(packet);
        }
    }

}
