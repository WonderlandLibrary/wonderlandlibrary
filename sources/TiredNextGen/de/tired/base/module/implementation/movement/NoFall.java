package de.tired.base.module.implementation.movement;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.guis.newclickgui.setting.ModeSetting;
import de.tired.base.guis.newclickgui.setting.NumberSetting;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.PacketEvent;
import de.tired.base.event.events.UpdateEvent;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;

@ModuleAnnotation(name = "NoFall", category = ModuleCategory.MOVEMENT, clickG = "prevents fallDamage")
public class NoFall extends Module {

    public ModeSetting mode = new ModeSetting("NofallMode", this, new String[]{"Verus", "Matrix", "AAC4", "Vanilla", "Custom", "Vulcan"});
    public NumberSetting fallDistance = new NumberSetting("fallDistance", this, 3.5, 0.5, 10, .5, () -> mode.getValue().equalsIgnoreCase("Matrix"));

    @EventTarget
    public void onPacket(PacketEvent e) {
        if (!this.state) return;
        if (mode.getValue().equalsIgnoreCase("Vulcan")) {
            if (e.getPacket() instanceof C03PacketPlayer) {
                C03PacketPlayer event = (C03PacketPlayer) e.getPacket();
                if (getPlayer().fallDistance >= 3) {
                    getPlayer().fallDistance = 0;
                    getPlayer().motionY = -10;
                    getPlayer().motionX = 0;
                    getPlayer().motionZ = 0;
                    event.onGround = true;

                }
            }
        }
        if (mode.getValue().equalsIgnoreCase("Matrix")) {
            if (e.getPacket() instanceof C03PacketPlayer) {
                C03PacketPlayer o = (C03PacketPlayer) e.getPacket();
                if (MC.thePlayer.fallDistance > fallDistance.getValue()) {
                    o.onGround = true;
                }

            }

        }

        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            if (mode.getValue().equalsIgnoreCase("AAC4")) {
                MC.timer.updateTimer();
                MC.thePlayer.motionY -= 0.01F;
                e.setCancelled(true);
            }
        }
    }

    public int groundDistance() {
        int blocks = (int) MC.thePlayer.posY;

        while (MC.thePlayer.fallDistance > 0 && MC.theWorld.getBlock(MC.thePlayer.posX, MC.thePlayer.posY - blocks, MC.thePlayer.posZ).getMaterial() == Material.air) {
            blocks--;
        }
        return blocks;
    }

    int fallTicks = 0;

    @EventTarget
    public void onUpdate(UpdateEvent e) {
        if (!this.state) return;

        switch (mode.getValue()) {
            case "Verus": {
                if (MC.thePlayer.fallDistance > 3.5) {
                    MC.timer.timerSpeed = .4F;
                    sendPacketUnlogged(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0, 0.5f, 0));
                    sendPacketUnlogged(new C08PacketPlayerBlockPlacement(new BlockPos(getX(), getY() - 1.5, getZ()), 1, new ItemStack(Blocks.stone.getItem(getWorld(), new BlockPos(-1, -1, -1))), 0, 0.94f, 0));
                    sendPacketUnlogged(new C03PacketPlayer(true));
                    MC.thePlayer.fallDistance = 0;
                } else {
                    MC.timer.timerSpeed = 1F;
                }
            }


            case "AAC4": {

                if (MC.thePlayer.fallDistance > 1.2 && !MC.thePlayer.onGround) {
                    if (MC.thePlayer.ticksExisted % 9 == 0) {
                        MC.thePlayer.fallDistance = 1;
                        sendPacket(new C03PacketPlayer(true));
                        MC.thePlayer.motionY = 0;
                        fallTicks++;
                    } else {
                        MC.timer.timerSpeed = .2F;
                        MC.timer.updateTimer();
                    }
                } else {
                    MC.timer.timerSpeed = 1F;
                }
            }
            break;

            case "Matrix": {
                if (MC.thePlayer.fallDistance > fallDistance.getValue()) {
                    MC.thePlayer.moveForward = 0;
                    MC.thePlayer.movementInput.moveStrafe = 0;
                    MC.thePlayer.fallDistance = 0;
                    MC.timer.timerSpeed = .4F;
                } else {
                    MC.timer.timerSpeed = 1F;
                }
            }
            break;
            case "Vanilla": {
                if (MC.thePlayer.fallDistance >= 1) {
                    sendPacketUnlogged(new C03PacketPlayer(true));
                    MC.thePlayer.fallDistance = 0;
                }
            }

            break;
        }
    }

    public boolean shouldExecuteNoFall() {
        return MC.thePlayer.fallDistance > 2F;
    }

    @Override
    public void onState() {
        MC.timer.timerSpeed = 1F;
    }

    @Override
    public void onUndo() {
        MC.timer.timerSpeed = 1F;
    }
}
