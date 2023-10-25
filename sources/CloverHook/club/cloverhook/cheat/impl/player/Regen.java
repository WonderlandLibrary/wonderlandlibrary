package club.cloverhook.cheat.impl.player;

import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.event.Stage;
import club.cloverhook.event.minecraft.PlayerUpdateEvent;
import club.cloverhook.utils.property.impl.DoubleProperty;
import club.cloverhook.utils.property.impl.StringsProperty;
import me.hippo.systems.lwjeb.annotation.Collect;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

public class Regen extends Cheat {
	public int waittime;
	private StringsProperty modeProperty = new StringsProperty("Mode", "How this cheat will function.", null, false, true, new String[] {"AGC", "Viper", "Burst"}, new Boolean[] {true, false, false});
	private DoubleProperty health = new DoubleProperty("Health", "The yaw and pitch your head shakes to", null, 7, 1, 20, 1, null); 
	private DoubleProperty packets = new DoubleProperty("Packets", "The amount of packets used for regen (more = faster)", null, 100, 5, 500, 5, null); 
	public Regen() {
		super("Regen", "Makes you heal faster", CheatCategory.PLAYER);
		registerProperties(modeProperty, packets, health);
	}
	public void onDisable() {
		if (modeProperty.getValue().get("AGC") || modeProperty.getValue().get("Viper")) {
			mc.timer.timerSpeed = 1f;
		}
	}
	
	public void onEnable() {
		if (modeProperty.getValue().get("AGC") || modeProperty.getValue().get("Viper")) {
			mc.timer.timerSpeed = getPlayer().getHealth() <= health.getValue().intValue() ? 0.1F : 1f;
		}
	}
	
	@Collect
	public void onPlayerUpdate(PlayerUpdateEvent playerUpdateEvent) {
		setMode(modeProperty.getSelectedStrings().get(0));
			if (modeProperty.getValue().get("AGC") && getPlayer().getHealth() <= health.getValue().intValue() && getPlayer().onGround) { 
				mc.timer.timerSpeed = 0.19F;
				if (waittime <= 0) {
					for (int i = 0; i <= packets.getValue().intValue(); i++) {
						 getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer(getPlayer().onGround));
					}
				} else if (waittime >= 0){
					if (getPlayer().ticksExisted % 2 == 0) {
						waittime -= 1;
					}
				}
			}
			if (playerUpdateEvent.isPre() && modeProperty.getValue().get("Viper") && getPlayer().getHealth() <= health.getValue().intValue() && getPlayer().onGround) {
				if (getPlayer().ticksExisted % 2 == 0) {
					getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY - 2, getPlayer().posZ, true));
				} else {
					for (int i = 0; i <= packets.getValue().intValue(); i++) {
						
						getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(getPlayer().posX, getPlayer().posY + 1.0E-9D, getPlayer().posZ, getPlayer().rotationYaw, getPlayer().rotationPitch, true));
	
					} 
				}
			}
			if (modeProperty.getValue().get("Burst") && getPlayer().getHealth() <= health.getValue().intValue()) { 
				for (int i = 0; i <= packets.getValue().intValue(); i++) {
					 getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer(getPlayer().onGround));
				}
			}
	}
	
	public double getGroundLevel() {
        for (int i = (int) Math.round(getPlayer().posY); i > 0; --i) {
            AxisAlignedBB box = (AxisAlignedBB) getPlayer().getEntityBoundingBox().addCoord(0, 0, 0);
            box.minY = i - 1;
            box.maxY = i;
            if (isColliding(box) && box.minY <= getPlayer().posY) {
                return i;
            }
        }
        return 0;
    }

    private boolean isColliding(AxisAlignedBB box) {
        return mc.theWorld.checkBlockCollision(box);
    }
}