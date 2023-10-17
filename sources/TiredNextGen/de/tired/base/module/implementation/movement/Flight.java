package de.tired.base.module.implementation.movement;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.guis.newclickgui.setting.ModeSetting;
import de.tired.util.hook.PlayerHook;
import de.tired.util.math.TimerUtil;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.EventBlockBB;
import de.tired.base.event.events.EventPreMotion;
import de.tired.base.event.events.PacketEvent;
import de.tired.base.event.events.UpdateEvent;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;

@ModuleAnnotation(name = "Flight", category = ModuleCategory.MOVEMENT, clickG = "Fly Around the world")
public class Flight extends Module {
    public ModeSetting flyMode = new ModeSetting("FlyMode", this, new String[]{"VerusSpoof", "VerusPacket", "Motion"});

    public int startY;

    @EventTarget
    public void onMove(UpdateEvent e) {
        if (MC.thePlayer == null) return;
    }

    @EventTarget
    public void onBlock(EventBlockBB eventBlockBB) {

    }

    @EventTarget
    public void onPacket(PacketEvent e) {


    }

    @Override
    public void onState() {
        startY = (int) MC.thePlayer.posY;
    }

    @EventTarget
    public void onPre(EventPreMotion eventPreMotion) {
        switch (flyMode.getValue()) {
            case "Motion": {
                PlayerHook.increaseSpeedWithStrafe(2.7);
                MC.thePlayer.motionY = 0;
                if (MC.gameSettings.keyBindSneak.pressed) {
                    MC.thePlayer.motionY += 1.5;
                } else if (MC.gameSettings.keyBindSprint.pressed) {
                    MC.thePlayer.motionY -= .5;
                }
                break;
            }
            case "VerusSpoof": {

                if (!MC.gameSettings.keyBindSprint.isKeyDown()) {
                    if (MC.gameSettings.keyBindSneak.isKeyDown()) {
                        if (MC.thePlayer.ticksExisted % 21 == 0)
                            MC.thePlayer.motionY = 0.32F;
                    } else {
                        if (MC.thePlayer.onGround) {
                            MC.thePlayer.jump();
                        }
                        if (MC.thePlayer.fallDistance > 1)
                            MC.thePlayer.motionY = -((MC.thePlayer.posY) - Math.floor(MC.thePlayer.posY));

                        MC.thePlayer.motionX *= .97F;
                        MC.thePlayer.motionZ *= .97F;

                        if (MC.thePlayer.motionY == 0 && MC.thePlayer.fallDistance == 0) {
                            MC.thePlayer.jump();
                            MC.thePlayer.onGround = true;
                            MC.thePlayer.fallDistance = 0;
                            eventPreMotion.onGround = true;
                        }
                    }
                }
                break;
            }
            case "VerusPacket": {

                sendPacketUnlogged(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0, 0.5f, 0));
                sendPacketUnlogged(new C08PacketPlayerBlockPlacement(new BlockPos(getX(), getY() - 1.5, getZ()), 1, new ItemStack(Blocks.stone.getItem(getWorld(), new BlockPos(-1, -1, -1))), 0, 0.94f, 0));
                MC.thePlayer.motionY = 0;
                PlayerHook.increaseSpeedWithStrafe(PlayerHook.getSpeed() * 1.02);
                if (MC.gameSettings.keyBindSneak.pressed) {
                    MC.thePlayer.motionY += 1.5;
                } else if (MC.gameSettings.keyBindSprint.pressed) {
                    MC.thePlayer.motionY -= .5;
                }
                break;
            }
        }

    }

    @Override
    public void onUndo() {
        PlayerHook.stop();
        MC.thePlayer.isCollided = false;
        MC.thePlayer.hurtTime = 0;
        MC.thePlayer.onGround = false;
        MC.timer.timerSpeed = 1;

        MC.thePlayer.capabilities.isFlying = false;
    }
}
