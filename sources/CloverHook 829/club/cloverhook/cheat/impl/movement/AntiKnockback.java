package club.cloverhook.cheat.impl.movement;

import club.cloverhook.Cloverhook;
import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.cheat.impl.combat.aura.Aura;
import club.cloverhook.cheat.impl.movement.Speed;
import club.cloverhook.cheat.impl.player.Regen;
import club.cloverhook.event.Stage;
import club.cloverhook.event.minecraft.PlayerUpdateEvent;
import club.cloverhook.event.minecraft.ProcessPacketEvent;
import club.cloverhook.utils.property.impl.DoubleProperty;
import club.cloverhook.utils.property.impl.StringsProperty;
import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.potion.Potion;
import net.minecraft.util.ChatComponentText;

public class AntiKnockback extends Cheat {

	private StringsProperty modeProperty = new StringsProperty("Mode", "How this cheat will function.", null,
			false, true, new String[] {"Packet", "Motion", "OldAGC"}, new Boolean[] {true, false, false});
	DoubleProperty prop_motion = new DoubleProperty("Horizontal Multiplier","The amount motion is modified for original knockback ( motionxz *=)", () -> modeProperty.getValue().get("Motion"), 1.10, 0.5, 3.5, 0.1, null);
	DoubleProperty prop_motiony = new DoubleProperty("Vertical Adder","The amount motion is modified from original knockback (motiony *=)", () -> modeProperty.getValue().get("Motion"), 0.10, 0.21, 1.0, 0.1, null);

	private double[] positions = new double[] {0.0, 0.0, 0.0};
	private double[] motions = new double[] {0.0, 0.0, 0.0};
	private double cosmicVel;
	private Aura cheat_killAura;
	private Speed cheat_Speed;
	private Regen cheat_Regen;
	public AntiKnockback() {
		super("Velocity", "Prevents you from taking knockback.", CheatCategory.PLAYER);
		registerProperties(modeProperty, prop_motion, prop_motiony);
	}

	@Collect
	public void onProcessPacket(ProcessPacketEvent processPacketEvent) {
		if (modeProperty.getSelectedStrings().get(0).equalsIgnoreCase("packet")) {
			if ((processPacketEvent.getPacket() instanceof S12PacketEntityVelocity) && (((S12PacketEntityVelocity) processPacketEvent.getPacket()).getEntityID() == getPlayer().getEntityId())) {
				S12PacketEntityVelocity packet = (S12PacketEntityVelocity) processPacketEvent.getPacket();
				packet.motionX = 0;
				packet.motionY = 0;
				packet.motionZ = 0;
				processPacketEvent.setCancelled(true);
			}
			if (processPacketEvent.getPacket() instanceof S27PacketExplosion) {
				S27PacketExplosion packetExplosion = (S27PacketExplosion) processPacketEvent.getPacket();
				packetExplosion.field_149152_f = 0;
				packetExplosion.field_149153_g = 0;
				packetExplosion.field_149159_h = 0;
				processPacketEvent.setCancelled(true);
			}
		}
	}

	@Collect
	public void onPlayerUpdate(PlayerUpdateEvent playerUpdateEvent) {
		if (playerUpdateEvent.isPre()) {
			if (mc.getCurrentServerData() != null && (mc.getCurrentServerData().serverIP.toLowerCase().contains("ghostly"))) {
				if (mc.thePlayer.isPotionActive(Potion.moveSlowdown) || mc.thePlayer.isPotionActive(Potion.blindness)) {
					mc.thePlayer.removePotionEffectClient(Potion.moveSlowdown.id);
					mc.thePlayer.removePotionEffectClient(Potion.blindness.id);
					mc.thePlayer.removePotionEffect(Potion.moveSlowdown.id);
					mc.thePlayer.removePotionEffect(Potion.blindness.id);
					mc.thePlayer.addChatComponentMessage(new ChatComponentText("Bypassing this shit freeze plugin lul - take that L ghostly"));

				}
			}

			if (modeProperty.getSelectedStrings().get(0).equalsIgnoreCase("packet")) {
				if (mc.thePlayer.hurtTime> 0) {
					if (mc.getCurrentServerData() != null) {
						if (mc.getCurrentServerData().serverIP.toLowerCase().contains("cosmicpvp")
								|| mc.getCurrentServerData().serverIP.toLowerCase().contains("viper")) {
							if (cosmicVel > 1.0E-8D) {
								cosmicVel = 1.0E-8D;
							}
							playerUpdateEvent.setPosY(playerUpdateEvent.getPosY() + cosmicVel);
							cosmicVel += 2.15E-12D;
						}
					}
				} else {
					cosmicVel = 0;
				}
			}

			if (modeProperty.getSelectedStrings().get(0).equalsIgnoreCase("OldAGC")) {
				if (getPlayer().hurtTime != 0) {
					mc.thePlayer.motionY -= 10000;
					mc.thePlayer.motionX *= .65;
					mc.thePlayer.motionZ *= .65;
				}
			}
			if (modeProperty.getSelectedStrings().get(0).equalsIgnoreCase("motion")) {
		        if (cheat_killAura == null) {
		            cheat_killAura = (Aura) Cloverhook.instance.cheatManager.getCheatRegistry().get("Aura");
		        }
		        if (cheat_Speed == null) {
		            cheat_Speed = (Speed) Cloverhook.instance.cheatManager.getCheatRegistry().get("Speed");
		        }
		        if (cheat_Regen == null) {
		            cheat_Regen = (Regen) Cloverhook.instance.cheatManager.getCheatRegistry().get("Regen");
		        }
				if (getPlayer().hurtTime != 0) {
					cheat_killAura.waitDelay = 4;
					cheat_Regen.waittime = 2;
				}
				if (getPlayer().hurtTime == 0) {
					positions[0] = getPlayer().posX;
					positions[1] = getPlayer().posY + prop_motiony.getValue();
					positions[2] = getPlayer().posZ;

					motions[0] = getPlayer().motionX * prop_motion.getValue();
					motions[1] = getPlayer().motionY + getPlayer().fallDistance < 0.7 ? prop_motiony.getValue() : prop_motiony.getValue() / 2;
					motions[2] = getPlayer().motionZ * prop_motion.getValue();
				}else if (getPlayer().hurtTime == 9 && (playerUpdateEvent.getPosY() - mc.thePlayer.getEntityBoundingBox().minY <= 0.0001)) {
					getPlayer().posX = getPlayer().lastTickPosX = positions[0];
					getPlayer().posY = getPlayer().lastTickPosY = positions[1];
					getPlayer().posZ = getPlayer().lastTickPosZ = positions[2];
					getPlayer().motionX = motions[0];
					getPlayer().motionY = motions[1];
					getPlayer().motionZ = motions[2];
					mc.thePlayer.hurtTime = 0;
				}
				if (!mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.hurtTime < 9 && mc.thePlayer.hurtTime >= 1 && !(Cloverhook.instance.cheatManager.isCheatEnabled("Fly") || Cloverhook.instance.cheatManager.isCheatEnabled("Speed"))) {

				}
			}
		}
	}
}