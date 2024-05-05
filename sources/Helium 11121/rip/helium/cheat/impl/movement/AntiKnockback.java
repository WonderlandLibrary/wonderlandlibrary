package rip.helium.cheat.impl.movement;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.potion.Potion;
import net.minecraft.util.ChatComponentText;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.cheat.impl.combat.aura.Aura;
import rip.helium.cheat.impl.player.Regen;
import rip.helium.event.minecraft.PlayerUpdateEvent;
import rip.helium.event.minecraft.ProcessPacketEvent;
import rip.helium.utils.property.impl.DoubleProperty;
import rip.helium.utils.property.impl.StringsProperty;

public class AntiKnockback extends Cheat {

    private final StringsProperty modeProperty = new StringsProperty("Mode", "How this cheat will function.", null,
            false, true, new String[]{"Packet", "Motion", "OldAGC"}, new Boolean[]{true, false, false});
    DoubleProperty prop_motion = new DoubleProperty("Horizontal Multiplier", "The amount motion is modified for original knockback ( motionxz *=)", () -> modeProperty.getValue().get("Motion"), 1.10, 0, 30.5, 0.1, null);
    DoubleProperty prop_motiony = new DoubleProperty("Vertical Adder", "The amount motion is modified from original knockback (motiony *=)", () -> modeProperty.getValue().get("Motion"), 0.10, 0.21, 1.0, 0.1, null);

    private final double[] positions = new double[]{0.0, 0.0, 0.0};
    private final double[] motions = new double[]{0.0, 0.0, 0.0};
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
                    mc.thePlayer.addChatComponentMessage(new ChatComponentText("§c§lRemoved shitty effects :)."));

                }
            }

            if (modeProperty.getSelectedStrings().get(0).equalsIgnoreCase("packet")) {
                if (mc.thePlayer.hurtTime > 0) {
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
                    cheat_killAura = (Aura) Helium.instance.cheatManager.getCheatRegistry().get("Aura");
                }
                if (cheat_Speed == null) {
                    cheat_Speed = (Speed) Helium.instance.cheatManager.getCheatRegistry().get("Speed");
                }
                if (cheat_Regen == null) {
                    cheat_Regen = (Regen) Helium.instance.cheatManager.getCheatRegistry().get("Regen");
                }
                if (getPlayer().hurtTime != 0) {
                    cheat_Regen.waittime = 2;
                }
                if (getPlayer().hurtTime == 0) {
                    positions[0] = getPlayer().posX;
                    positions[1] = getPlayer().posY + prop_motiony.getValue();
                    positions[2] = getPlayer().posZ;

                    motions[0] = getPlayer().motionX * prop_motion.getValue();
                    motions[1] = getPlayer().motionY + getPlayer().fallDistance < 0.7 ? prop_motiony.getValue() : prop_motiony.getValue() / 2;
                    motions[2] = getPlayer().motionZ * prop_motion.getValue();
                } else if (getPlayer().hurtTime == 9 && (playerUpdateEvent.getPosY() - mc.thePlayer.getEntityBoundingBox().minY <= 0.0001)) {
                    getPlayer().posX = getPlayer().lastTickPosX = positions[0];
                    getPlayer().posY = getPlayer().lastTickPosY = positions[1];
                    getPlayer().posZ = getPlayer().lastTickPosZ = positions[2];
                    getPlayer().motionX = motions[0];
                    getPlayer().motionY = motions[1];
                    getPlayer().motionZ = motions[2];
                    mc.thePlayer.hurtTime = 0;
                }
                if (!mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.hurtTime < 9 && mc.thePlayer.hurtTime >= 1 && !(Helium.instance.cheatManager.isCheatEnabled("Fly") || Helium.instance.cheatManager.isCheatEnabled("Speed"))) {

                }
            }
        }
    }
}