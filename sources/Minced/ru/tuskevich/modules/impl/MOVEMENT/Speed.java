// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MOVEMENT;

import ru.tuskevich.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import ru.tuskevich.modules.impl.PLAYER.Timer;
import net.minecraft.block.BlockAir;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockLiquid;
import ru.tuskevich.util.movement.MoveUtility;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventMotion;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.BooleanSetting;
import ru.tuskevich.ui.dropui.setting.imp.ModeSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "Speed", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.MOVEMENT)
public class Speed extends Module
{
    public ModeSetting speedMode;
    public BooleanSetting autoJump;
    
    public Speed() {
        this.speedMode = new ModeSetting("Mode", "Matrix", new String[] { "Matrix", "Matrix Timer", "RipServer", "Sunrise Damage" });
        this.autoJump = new BooleanSetting("Auto Jump", true);
        this.add(this.speedMode, this.autoJump);
    }
    
    @EventTarget
    public void onMotion(final EventMotion eventMotion) {
        if (this.autoJump.get()) {
            final Minecraft mc = Speed.mc;
            if (Minecraft.player.onGround && !Speed.mc.gameSettings.keyBindJump.pressed && !this.speedMode.is("Matrix Timer")) {
                final Minecraft mc2 = Speed.mc;
                Minecraft.player.jump();
            }
        }
        if (this.speedMode.is("Matrix")) {
            final Minecraft mc3 = Speed.mc;
            if (!Minecraft.player.onGround) {
                final WorldClient world = Speed.mc.world;
                final Minecraft mc4 = Speed.mc;
                final EntityPlayerSP player = Minecraft.player;
                final Minecraft mc5 = Speed.mc;
                if (!world.getCollisionBoxes(player, Minecraft.player.getEntityBoundingBox().offset(0.0, -1.0, 0.0).expand(0.0, 0.2, 0.0)).isEmpty()) {
                    final Minecraft mc6 = Speed.mc;
                    if (Minecraft.player.motionY == -0.4448259643949201) {
                        final Minecraft mc7 = Speed.mc;
                        final EntityPlayerSP player2 = Minecraft.player;
                        player2.motionX *= 2.0;
                        final Minecraft mc8 = Speed.mc;
                        final EntityPlayerSP player3 = Minecraft.player;
                        player3.motionZ *= 2.0;
                    }
                }
            }
        }
        Label_1080: {
            if (this.speedMode.is("Sunrise Damage")) {
                final double radians = MoveUtility.getDirection();
                if (Speed.mc.getCurrentServerData() != null && Speed.mc.getCurrentServerData().serverIP != null && Speed.mc.getCurrentServerData().serverIP.contains("sunset")) {
                    if (MoveUtility.isMoving()) {
                        final Minecraft mc9 = Speed.mc;
                        if (Minecraft.player.onGround) {
                            final Minecraft mc10 = Speed.mc;
                            Minecraft.player.addVelocity(-Math.sin(radians) * 9.5 / 24.5, 0.0, Math.cos(radians) * 9.5 / 24.5);
                            MoveUtility.setStrafe(MoveUtility.getSpeed());
                        }
                        else {
                            final Minecraft mc11 = Speed.mc;
                            if (Minecraft.player.isInWater()) {
                                final Minecraft mc12 = Speed.mc;
                                Minecraft.player.addVelocity(-Math.sin(radians) * 9.5 / 24.5, 0.0, Math.cos(radians) * 9.5 / 24.5);
                                MoveUtility.setStrafe(MoveUtility.getSpeed());
                                Label_0526: {
                                    if (Speed.mc.gameSettings.keyBindJump.isKeyDown()) {
                                        final Minecraft mc13 = Speed.mc;
                                        if (!Minecraft.player.isSneaking()) {
                                            final WorldClient world2 = Speed.mc.world;
                                            final Minecraft mc14 = Speed.mc;
                                            final double posX = Minecraft.player.posX;
                                            final Minecraft mc15 = Speed.mc;
                                            final double posY = Minecraft.player.posY;
                                            final Minecraft mc16 = Speed.mc;
                                            if (world2.getBlockState(new BlockPos(posX, posY, Minecraft.player.posZ)).getBlock() instanceof BlockLiquid) {
                                                final WorldClient world3 = Speed.mc.world;
                                                final Minecraft mc17 = Speed.mc;
                                                final double posX2 = Minecraft.player.posX;
                                                final Minecraft mc18 = Speed.mc;
                                                final double y = Minecraft.player.posY + 2.0;
                                                final Minecraft mc19 = Speed.mc;
                                                if (world3.getBlockState(new BlockPos(posX2, y, Minecraft.player.posZ)).getBlock() instanceof BlockAir) {
                                                    break Label_0526;
                                                }
                                            }
                                            final Minecraft mc20 = Speed.mc;
                                            Minecraft.player.motionY = 0.4000000059604645;
                                            break Label_1080;
                                        }
                                    }
                                }
                                if (Speed.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                    final Minecraft mc21 = Speed.mc;
                                    Minecraft.player.motionY = -1.0;
                                }
                            }
                            else {
                                final Minecraft mc22 = Speed.mc;
                                if (!Minecraft.player.onGround) {
                                    final Minecraft mc23 = Speed.mc;
                                    Minecraft.player.addVelocity(-Math.sin(radians) * 1.0 / 24.5, 0.0, Math.cos(radians) * 5.0 / 24.5);
                                    MoveUtility.setStrafe(MoveUtility.getSpeed());
                                }
                                else {
                                    final Minecraft mc24 = Speed.mc;
                                    Minecraft.player.addVelocity(-Math.sin(radians) * 0.005 * MoveUtility.getSpeed(), 0.0, Math.cos(radians) * 0.005 * MoveUtility.getSpeed());
                                    MoveUtility.setStrafe(MoveUtility.getSpeed());
                                }
                            }
                        }
                    }
                }
                else if (MoveUtility.isMoving()) {
                    final Minecraft mc25 = Speed.mc;
                    if (Minecraft.player.onGround) {
                        final Minecraft mc26 = Speed.mc;
                        Minecraft.player.addVelocity(-Math.sin(radians) * 9.5 / 24.5, 0.0, Math.cos(radians) * 9.5 / 24.5);
                        MoveUtility.setStrafe(MoveUtility.getSpeed());
                    }
                    else {
                        final Minecraft mc27 = Speed.mc;
                        if (Minecraft.player.isInWater()) {
                            final Minecraft mc28 = Speed.mc;
                            Minecraft.player.addVelocity(-Math.sin(radians) * 9.5 / 24.5, 0.0, Math.cos(radians) * 9.5 / 24.5);
                            MoveUtility.setStrafe(MoveUtility.getSpeed());
                            Label_0945: {
                                if (Speed.mc.gameSettings.keyBindJump.isKeyDown()) {
                                    final Minecraft mc29 = Speed.mc;
                                    if (!Minecraft.player.isSneaking()) {
                                        final WorldClient world4 = Speed.mc.world;
                                        final Minecraft mc30 = Speed.mc;
                                        final double posX3 = Minecraft.player.posX;
                                        final Minecraft mc31 = Speed.mc;
                                        final double posY2 = Minecraft.player.posY;
                                        final Minecraft mc32 = Speed.mc;
                                        if (world4.getBlockState(new BlockPos(posX3, posY2, Minecraft.player.posZ)).getBlock() instanceof BlockLiquid) {
                                            final WorldClient world5 = Speed.mc.world;
                                            final Minecraft mc33 = Speed.mc;
                                            final double posX4 = Minecraft.player.posX;
                                            final Minecraft mc34 = Speed.mc;
                                            final double y2 = Minecraft.player.posY + 2.0;
                                            final Minecraft mc35 = Speed.mc;
                                            if (world5.getBlockState(new BlockPos(posX4, y2, Minecraft.player.posZ)).getBlock() instanceof BlockAir) {
                                                break Label_0945;
                                            }
                                        }
                                        final Minecraft mc36 = Speed.mc;
                                        Minecraft.player.motionY = 0.4000000059604645;
                                        break Label_1080;
                                    }
                                }
                            }
                            if (Speed.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                final Minecraft mc37 = Speed.mc;
                                Minecraft.player.motionY = -1.0;
                            }
                        }
                        else {
                            final Minecraft mc38 = Speed.mc;
                            if (!Minecraft.player.onGround) {
                                final Minecraft mc39 = Speed.mc;
                                Minecraft.player.addVelocity(-Math.sin(radians) * 0.11 / 24.5, 0.0, Math.cos(radians) * 0.11 / 24.5);
                                MoveUtility.setStrafe(MoveUtility.getSpeed());
                            }
                            else {
                                final Minecraft mc40 = Speed.mc;
                                Minecraft.player.addVelocity(-Math.sin(radians) * 0.005 * MoveUtility.getSpeed(), 0.0, Math.cos(radians) * 0.005 * MoveUtility.getSpeed());
                                MoveUtility.setStrafe(MoveUtility.getSpeed());
                            }
                        }
                    }
                }
            }
        }
        if (this.speedMode.is("RipServer")) {
            final Minecraft mc41 = Speed.mc;
            if (Minecraft.player.ticksExisted % 2 == 0) {
                Speed.mc.timer.timerSpeed = 3.5f;
                final Minecraft mc42 = Speed.mc;
                Minecraft.player.capabilities.isFlying = true;
            }
            else {
                final Minecraft mc43 = Speed.mc;
                Minecraft.player.capabilities.isFlying = false;
                Speed.mc.timer.timerSpeed = 1.0f;
            }
        }
        Label_2051: {
            if (this.speedMode.is("MotionRW")) {
                final double radians = MoveUtility.getDirection();
                if (Speed.mc.getCurrentServerData() != null && Speed.mc.getCurrentServerData().serverIP != null && Speed.mc.getCurrentServerData().serverIP.contains("sunset")) {
                    if (MoveUtility.isMoving()) {
                        final Minecraft mc44 = Speed.mc;
                        if (Minecraft.player.onGround) {
                            final Minecraft mc45 = Speed.mc;
                            Minecraft.player.addVelocity(-Math.sin(radians) * 9.5 / 24.5, 0.0, Math.cos(radians) * 9.5 / 24.5);
                            MoveUtility.setStrafe(MoveUtility.getSpeed());
                        }
                        else {
                            final Minecraft mc46 = Speed.mc;
                            if (Minecraft.player.isInWater()) {
                                final Minecraft mc47 = Speed.mc;
                                Minecraft.player.addVelocity(-Math.sin(radians) * 9.5 / 24.5, 0.0, Math.cos(radians) * 9.5 / 24.5);
                                MoveUtility.setStrafe(MoveUtility.getSpeed());
                                Label_1497: {
                                    if (Speed.mc.gameSettings.keyBindJump.isKeyDown()) {
                                        final Minecraft mc48 = Speed.mc;
                                        if (!Minecraft.player.isSneaking()) {
                                            final WorldClient world6 = Speed.mc.world;
                                            final Minecraft mc49 = Speed.mc;
                                            final double posX5 = Minecraft.player.posX;
                                            final Minecraft mc50 = Speed.mc;
                                            final double posY3 = Minecraft.player.posY;
                                            final Minecraft mc51 = Speed.mc;
                                            if (world6.getBlockState(new BlockPos(posX5, posY3, Minecraft.player.posZ)).getBlock() instanceof BlockLiquid) {
                                                final WorldClient world7 = Speed.mc.world;
                                                final Minecraft mc52 = Speed.mc;
                                                final double posX6 = Minecraft.player.posX;
                                                final Minecraft mc53 = Speed.mc;
                                                final double y3 = Minecraft.player.posY + 2.0;
                                                final Minecraft mc54 = Speed.mc;
                                                if (world7.getBlockState(new BlockPos(posX6, y3, Minecraft.player.posZ)).getBlock() instanceof BlockAir) {
                                                    break Label_1497;
                                                }
                                            }
                                            final Minecraft mc55 = Speed.mc;
                                            Minecraft.player.motionY = 0.4000000059604645;
                                            break Label_2051;
                                        }
                                    }
                                }
                                if (Speed.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                    final Minecraft mc56 = Speed.mc;
                                    Minecraft.player.motionY = -1.0;
                                }
                            }
                            else {
                                final Minecraft mc57 = Speed.mc;
                                if (!Minecraft.player.onGround) {
                                    final Minecraft mc58 = Speed.mc;
                                    Minecraft.player.addVelocity(-Math.sin(radians) * 1.0 / 24.5, 0.0, Math.cos(radians) * 5.0 / 24.5);
                                    MoveUtility.setStrafe(MoveUtility.getSpeed());
                                }
                                else {
                                    final Minecraft mc59 = Speed.mc;
                                    Minecraft.player.addVelocity(-Math.sin(radians) * 0.005 * MoveUtility.getSpeed(), 0.0, Math.cos(radians) * 0.005 * MoveUtility.getSpeed());
                                    MoveUtility.setStrafe(MoveUtility.getSpeed());
                                }
                            }
                        }
                    }
                }
                else if (MoveUtility.isMoving()) {
                    final Minecraft mc60 = Speed.mc;
                    if (Minecraft.player.onGround) {
                        final Minecraft mc61 = Speed.mc;
                        Minecraft.player.addVelocity(-Math.sin(radians) * 9.5 / 24.5, 0.0, Math.cos(radians) * 9.5 / 24.5);
                        MoveUtility.setStrafe(MoveUtility.getSpeed());
                    }
                    else {
                        final Minecraft mc62 = Speed.mc;
                        if (Minecraft.player.isInWater()) {
                            final Minecraft mc63 = Speed.mc;
                            Minecraft.player.addVelocity(-Math.sin(radians) * 9.5 / 24.5, 0.0, Math.cos(radians) * 9.5 / 24.5);
                            MoveUtility.setStrafe(MoveUtility.getSpeed());
                            Label_1916: {
                                if (Speed.mc.gameSettings.keyBindJump.isKeyDown()) {
                                    final Minecraft mc64 = Speed.mc;
                                    if (!Minecraft.player.isSneaking()) {
                                        final WorldClient world8 = Speed.mc.world;
                                        final Minecraft mc65 = Speed.mc;
                                        final double posX7 = Minecraft.player.posX;
                                        final Minecraft mc66 = Speed.mc;
                                        final double posY4 = Minecraft.player.posY;
                                        final Minecraft mc67 = Speed.mc;
                                        if (world8.getBlockState(new BlockPos(posX7, posY4, Minecraft.player.posZ)).getBlock() instanceof BlockLiquid) {
                                            final WorldClient world9 = Speed.mc.world;
                                            final Minecraft mc68 = Speed.mc;
                                            final double posX8 = Minecraft.player.posX;
                                            final Minecraft mc69 = Speed.mc;
                                            final double y4 = Minecraft.player.posY + 2.0;
                                            final Minecraft mc70 = Speed.mc;
                                            if (world9.getBlockState(new BlockPos(posX8, y4, Minecraft.player.posZ)).getBlock() instanceof BlockAir) {
                                                break Label_1916;
                                            }
                                        }
                                        final Minecraft mc71 = Speed.mc;
                                        Minecraft.player.motionY = 0.4000000059604645;
                                        break Label_2051;
                                    }
                                }
                            }
                            if (Speed.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                final Minecraft mc72 = Speed.mc;
                                Minecraft.player.motionY = -1.0;
                            }
                        }
                        else {
                            final Minecraft mc73 = Speed.mc;
                            if (!Minecraft.player.onGround) {
                                final Minecraft mc74 = Speed.mc;
                                Minecraft.player.addVelocity(-Math.sin(radians) * 0.11 / 24.5, 0.0, Math.cos(radians) * 0.11 / 24.5);
                                MoveUtility.setStrafe(MoveUtility.getSpeed());
                            }
                            else {
                                final Minecraft mc75 = Speed.mc;
                                Minecraft.player.addVelocity(-Math.sin(radians) * 0.005 * MoveUtility.getSpeed(), 0.0, Math.cos(radians) * 0.005 * MoveUtility.getSpeed());
                                MoveUtility.setStrafe(MoveUtility.getSpeed());
                            }
                        }
                    }
                }
            }
        }
        if (this.speedMode.is("Matrix Timer")) {
            final Minecraft mc76 = Speed.mc;
            float n;
            if (Minecraft.player.fallDistance <= 0.22f) {
                n = 2.0f;
            }
            else {
                final Minecraft mc77 = Speed.mc;
                n = (float)((Minecraft.player.fallDistance < 1.25f) ? 0.67 : 1.0);
            }
            final float timerValue = n;
            if (Timer.canEnableTimer(timerValue + 0.2f) && MoveUtility.isMoving()) {
                Speed.mc.timer.timerSpeed = timerValue;
                final Minecraft mc78 = Speed.mc;
                Minecraft.player.jumpMovementFactor = 0.026525f;
                if (MoveUtility.isInGround() && this.autoJump.get()) {
                    final Minecraft mc79 = Speed.mc;
                    Minecraft.player.jump();
                }
            }
            else {
                Speed.mc.timer.timerSpeed = 1.0f;
            }
        }
    }
    
    @Override
    public void onDisable() {
        Speed.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
}
