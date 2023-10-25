package org.dreamcore.client.feature.impl.player;

import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventPreMotion;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.feature.impl.movement.Flight;
import org.dreamcore.client.helpers.misc.ChatHelper;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

public class AntiVoid extends Feature {

    private final NumberSetting fallDist;
    private final ListSetting antiVoidMode;
    public float fall = 0;
    public boolean toggleFeature = false;

    public AntiVoid() {
        super("AntiVoid", "Не дает вам упасть в бездну", Type.Player);
        antiVoidMode = new ListSetting("AntiVoid Mode", "Packet", () -> true, "Packet", "Spoof", "High-Motion", "Invalid Position", "Invalid Pitch", "Flag");
        fallDist = new NumberSetting("Fall Distance", 5, 1, 10, 1, () -> true);
        addSettings(antiVoidMode, fallDist);
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        String mode = antiVoidMode.getOptions();
        this.setSuffix(mode);
        if (dreamcore.instance.featureManager.getFeatureByClass(Flight.class).getState()) {
            return;
        }

        if (!mc.player.onGround && !mc.player.isCollidedVertically) {
            if (mc.player.fallDistance > fallDist.getNumberValue()) {
                if (mc.world.getBlockState(new BlockPos(0, -fall, 0)).getBlock() == Blocks.AIR) {
                    if (mode.equalsIgnoreCase("High-Motion")) {
                        mc.player.motionY += 3f;
                    } else if (mode.equals("Packet")) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 12, mc.player.posZ, true));
                    } else if (mode.equals("Spoof")) {
                        mc.player.connection.sendPacket(new CPacketPlayer(true));
                    } else if (mode.equals("Flag")) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + 1, mc.player.posY + 1, mc.player.posZ + 1, true));
                    } else if (mode.equals("Invalid Pitch")) {
                        event.setOnGround(true);
                        event.setPitch(-91);
                        mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, true));
                    } else if (mode.equals("Invalid Position")) {
                        if (mc.player.onGround) {
                            toggleFeature = true;
                        }

                        if (!toggleFeature && NoFall.noFallMode.currentMode.equals("Matrix")) {
                            ChatHelper.addChatMessage("Переключи мод NoFall на другой!");
                        }

                        if (!toggleFeature && !NoFall.noFallMode.currentMode.equals("Matrix")) {
                            mc.player.setPosition(mc.player.posX + 1, mc.player.posY + 1, mc.player.posZ + 1);
                        } else {
                            setState(false);
                        }
                    }
                }
            }
        }
    }
}