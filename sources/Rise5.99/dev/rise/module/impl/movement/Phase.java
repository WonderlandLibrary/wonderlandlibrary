/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.movement;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.BlockCollideEvent;
import dev.rise.event.impl.other.MoveButtonEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.misc.TPAura;
import dev.rise.util.player.MoveUtil;
import dev.rise.util.player.PacketUtil;
import dev.rise.util.player.PlayerUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ModuleInfo(name = "Phase", description = "Lets you walk through blocks", category = Category.MOVEMENT)
public final class Phase extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "Forward", "Vertical", "Vanilla", "Sand", "Clip", "Hypixel");
    private final NumberSetting flySpeed = new NumberSetting("Fly Speed", this, 1, 0.1, 9.5, 0.1);
    private final ModeSetting hypixelMode = new ModeSetting("Hypixel Mode", this, "Auto", "Auto", "Teleport");
    private final ModeSetting teleportMode = new ModeSetting("Teleport Mode", this, "Player", "Player", "Middle");
    private final ModeSetting direction = new ModeSetting("Direction", this, "Down", "Down", "Up");
    private final BooleanSetting sneakOnly = new BooleanSetting("Sneak Only", this, false);

    private int onGroundTicks;
    private boolean canClip;

    @Override
    public void onUpdateAlwaysInGui() {
        hypixelMode.hidden = !mode.is("Hypixel");

        direction.hidden = !((mode.is("Hypixel") && !hypixelMode.is("Teleport")) || mode.is("Clip"));

        teleportMode.hidden = !(mode.is("Hypixel") && hypixelMode.is("Teleport"));

        sneakOnly.hidden = !(mode.is("Normal") || mode.is("Forward") || mode.is("Vertical"));

        flySpeed.hidden = !(mode.is("Vanilla") || mode.is("Sand"));
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        final double rotation = Math.toRadians(mc.thePlayer.rotationYaw);
        final double x;
        final double z;

        if (mc.thePlayer.onGround)
            onGroundTicks++;
        else
            onGroundTicks = 0;

        mc.thePlayer.noClip = !mode.is("Hypixel");

        switch (mode.getMode()) {
            case "Normal":
                x = Math.sin(rotation) * 0.005;
                z = Math.cos(rotation) * 0.005;

                if (!sneakOnly.isEnabled() || mc.gameSettings.keyBindSneak.isKeyDown()) {
                    if (mc.thePlayer.isCollidedHorizontally) {
                        PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX - x, mc.thePlayer.posY, mc.thePlayer.posZ + z, true));
                        mc.thePlayer.setPosition(mc.thePlayer.posX - x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
                    }

                    if (PlayerUtil.isInsideBlock()) {
                        PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + 1.5 * Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F)), mc.thePlayer.posY, mc.thePlayer.posZ + 1.5 * Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F)), true));
                        MoveUtil.strafe(MoveUtil.getBaseMoveSpeed() / 2);
                    }
                }
                break;

            case "Forward":
                x = Math.sin(rotation) * 0.5;
                z = Math.cos(rotation) * 0.5;

                if (!sneakOnly.isEnabled() || mc.gameSettings.keyBindSneak.isKeyDown()) {
                    if (mc.thePlayer.isCollidedHorizontally)
                        mc.thePlayer.setPosition(mc.thePlayer.posX - x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
                }
                break;

            case "Vertical":
                x = Math.sin(rotation) * 0.5;
                z = Math.cos(rotation) * 0.5;

                if (!sneakOnly.isEnabled() || mc.gameSettings.keyBindSneak.isKeyDown()) {
                    if (mc.thePlayer.isCollidedHorizontally) {
                        PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX - x, mc.thePlayer.posY, mc.thePlayer.posZ + z, true));
                        PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 11, mc.thePlayer.posZ, false));
                        mc.thePlayer.setPosition(mc.thePlayer.posX - x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
                    }

                    if (PlayerUtil.isInsideBlock()) {
                        MoveUtil.strafe(MoveUtil.getBaseMoveSpeed() / 2);
                    }
                }
                break;

            case "Sand":
            case "Vanilla":
                if (PlayerUtil.isInsideBlock()) {
                    mc.thePlayer.motionY = mc.gameSettings.keyBindJump.isKeyDown() ? flySpeed.getValue() : mc.gameSettings.keyBindSneak.isKeyDown() ? -flySpeed.getValue() : 0;

                    if (MoveUtil.isMoving())
                        MoveUtil.strafe(flySpeed.getValue());
                    else
                        MoveUtil.stop();
                }
                break;

            case "Hypixel":
                if (mc.thePlayer.ticksExisted == 1)
                    onGroundTicks = 0;

                if (canClip && mc.thePlayer.onGround && onGroundTicks > 10 && !(PlayerUtil.getBlockRelativeToPlayer(0, -0.9, 0) instanceof BlockAir)) {
                    switch (hypixelMode.getMode()) {
                        case "Auto":
                            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + (direction.is("Up") ? (4 + Math.random() / 500) : -(4 + Math.random() / 500)), mc.thePlayer.posZ);
                            break;

                        case "Teleport":
                            switch (teleportMode.getMode()) {
                                case "Player":
                                    final List<EntityPlayer> entities = mc.theWorld.playerEntities
                                            // Stream our entity list.
                                            .stream()

                                            // Only get the entities we can teleport to.
                                            .filter(entity -> {
                                                if (entity.isInvisible()) return false;

                                                if (entity.getName().toLowerCase().contains("[npc]")) return false;

                                                for (final String name : Rise.INSTANCE.getFriends()) {
                                                    if (name.equalsIgnoreCase(entity.getName()))
                                                        return false;
                                                }

                                                return mc.thePlayer != entity;
                                            })

                                            // Sort out potential targets with the algorithm.
                                            .sorted(Comparator.comparingDouble(entity -> mc.thePlayer.getDistanceSqToEntity(entity)))

                                            // Sort out all the specified targets.
                                            .sorted(Comparator.comparing(entity -> !Rise.INSTANCE.getTargets().contains(entity.getName())))

                                            // Get the possible targets and put them in a list.
                                            .collect(Collectors.toList());

                                    // Teleports to the best entity from the list
                                    if (!entities.isEmpty()) {
                                        final EntityPlayer entity = entities.get(0);

                                        TPAura.tpToLocation(
                                                999, 9.5, 9.0,
                                                new ArrayList<>(), new ArrayList<>(),
                                                entity.getPosition().down(5)
                                        );

                                        mc.thePlayer.setPosition(entity.posX, entity.posY - (5 + Math.random() / 500), entity.posZ);
                                    } else {
                                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - (4 + Math.random() / 500), mc.thePlayer.posZ);
                                    }

                                    break;

                                case "Middle":
                                    TPAura.tpToLocation(
                                            999, 9.5, 9.0,
                                            new ArrayList<>(), new ArrayList<>(),
                                            new BlockPos(0, 256, 0)
                                    );
                                    break;
                            }
                            break;
                    }
                    canClip = false;
                }
                break;
        }
    }

    @Override
    public void onBlockCollide(final BlockCollideEvent event) {
        if (PlayerUtil.isInsideBlock() && (mode.is("Vanilla") || mode.is("Sand")))
            event.setCollisionBoundingBox(null);
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (mode.is("Hypixel") && event.getPacket() instanceof S02PacketChat) {
            final S02PacketChat packetChat = (S02PacketChat) event.getPacket();
            final String message = packetChat.getChatComponent().getUnformattedText();

            if (message.startsWith(mc.thePlayer.getName() + " has joined (") || message.equals("Cages open in: 15 seconds!") || message.equals("Cages open in: 10 seconds!")) {
                canClip = true;
            }
        }
    }

    @Override
    public void onMoveButton(final MoveButtonEvent event) {
        if (mode.is("Vanilla") || mode.is("Sand") || ((sneakOnly.isEnabled() && !sneakOnly.isHidden()) && !mode.is("Hypixel")))
            event.setSneak(false);
    }

    @Override
    protected void onEnable() {
        switch (mode.getMode()) {
            case "Clip":
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + (direction.is("Up") ? 3 : -3), mc.thePlayer.posZ);
                this.toggleModule();
                break;

            case "Vanilla":
                PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + Double.POSITIVE_INFINITY, mc.thePlayer.posY + Double.POSITIVE_INFINITY, mc.thePlayer.posZ + Double.POSITIVE_INFINITY, true));
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2, mc.thePlayer.posZ);
                break;
        }
    }

    @Override
    protected void onDisable() {
        mc.thePlayer.noClip = false;
        onGroundTicks = 0;
    }
}