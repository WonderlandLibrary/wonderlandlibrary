// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MOVEMENT;

import java.util.Arrays;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import ru.tuskevich.event.EventTarget;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventMotion;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "AirJump", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.MOVEMENT)
public class AirJump extends Module
{
    @EventTarget
    public void onMotionEvent(final EventMotion eventMotion) {
        final WorldClient world = AirJump.mc.world;
        final Minecraft mc = AirJump.mc;
        final double posX = Minecraft.player.posX;
        final Minecraft mc2 = AirJump.mc;
        final double y = Minecraft.player.posY - 1.0;
        final Minecraft mc3 = AirJump.mc;
        if (!this.validBlocks(world.getBlockState(new BlockPos(posX, y, Minecraft.player.posZ)).getBlock())) {
            final WorldClient world2 = AirJump.mc.world;
            final Minecraft mc4 = AirJump.mc;
            final double posX2 = Minecraft.player.posX;
            final Minecraft mc5 = AirJump.mc;
            final double y2 = Minecraft.player.posY - 1.0;
            final Minecraft mc6 = AirJump.mc;
            if (!this.validBlocks(world2.getBlockState(new BlockPos(posX2, y2, Minecraft.player.posZ)).getBlock())) {
                final WorldClient world3 = AirJump.mc.world;
                final Minecraft mc7 = AirJump.mc;
                final double x = Minecraft.player.posX - 1.0;
                final Minecraft mc8 = AirJump.mc;
                final double y3 = Minecraft.player.posY - 1.0;
                final Minecraft mc9 = AirJump.mc;
                if (!this.validBlocks(world3.getBlockState(new BlockPos(x, y3, Minecraft.player.posZ - 1.0)).getBlock())) {
                    final WorldClient world4 = AirJump.mc.world;
                    final Minecraft mc10 = AirJump.mc;
                    final double x2 = Minecraft.player.posX + 1.0;
                    final Minecraft mc11 = AirJump.mc;
                    final double y4 = Minecraft.player.posY - 1.0;
                    final Minecraft mc12 = AirJump.mc;
                    if (!this.validBlocks(world4.getBlockState(new BlockPos(x2, y4, Minecraft.player.posZ + 1.0)).getBlock())) {
                        final WorldClient world5 = AirJump.mc.world;
                        final Minecraft mc13 = AirJump.mc;
                        final double x3 = Minecraft.player.posX - 1.0;
                        final Minecraft mc14 = AirJump.mc;
                        final double y5 = Minecraft.player.posY - 1.0;
                        final Minecraft mc15 = AirJump.mc;
                        if (!this.validBlocks(world5.getBlockState(new BlockPos(x3, y5, Minecraft.player.posZ + 1.0)).getBlock())) {
                            final WorldClient world6 = AirJump.mc.world;
                            final Minecraft mc16 = AirJump.mc;
                            final double x4 = Minecraft.player.posX + 1.0;
                            final Minecraft mc17 = AirJump.mc;
                            final double y6 = Minecraft.player.posY - 1.0;
                            final Minecraft mc18 = AirJump.mc;
                            if (!this.validBlocks(world6.getBlockState(new BlockPos(x4, y6, Minecraft.player.posZ - 1.0)).getBlock())) {
                                final WorldClient world7 = AirJump.mc.world;
                                final Minecraft mc19 = AirJump.mc;
                                final double x5 = Minecraft.player.posX + 1.0;
                                final Minecraft mc20 = AirJump.mc;
                                final double y7 = Minecraft.player.posY - 1.0;
                                final Minecraft mc21 = AirJump.mc;
                                if (!this.validBlocks(world7.getBlockState(new BlockPos(x5, y7, Minecraft.player.posZ)).getBlock())) {
                                    final WorldClient world8 = AirJump.mc.world;
                                    final Minecraft mc22 = AirJump.mc;
                                    final double x6 = Minecraft.player.posX - 1.0;
                                    final Minecraft mc23 = AirJump.mc;
                                    final double y8 = Minecraft.player.posY - 1.0;
                                    final Minecraft mc24 = AirJump.mc;
                                    if (!this.validBlocks(world8.getBlockState(new BlockPos(x6, y8, Minecraft.player.posZ)).getBlock())) {
                                        final WorldClient world9 = AirJump.mc.world;
                                        final Minecraft mc25 = AirJump.mc;
                                        final double posX3 = Minecraft.player.posX;
                                        final Minecraft mc26 = AirJump.mc;
                                        final double y9 = Minecraft.player.posY - 1.0;
                                        final Minecraft mc27 = AirJump.mc;
                                        if (!this.validBlocks(world9.getBlockState(new BlockPos(posX3, y9, Minecraft.player.posZ + 1.0)).getBlock())) {
                                            final WorldClient world10 = AirJump.mc.world;
                                            final Minecraft mc28 = AirJump.mc;
                                            final double posX4 = Minecraft.player.posX;
                                            final Minecraft mc29 = AirJump.mc;
                                            final double y10 = Minecraft.player.posY - 1.0;
                                            final Minecraft mc30 = AirJump.mc;
                                            if (!this.validBlocks(world10.getBlockState(new BlockPos(posX4, y10, Minecraft.player.posZ - 1.0)).getBlock())) {
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        final Minecraft mc31 = AirJump.mc;
        if (Minecraft.player.ticksExisted % 2 == 0 && AirJump.mc.gameSettings.keyBindJump.isKeyDown()) {
            final Minecraft mc32 = AirJump.mc;
            Minecraft.player.jumpTicks = 0;
            final Minecraft mc33 = AirJump.mc;
            Minecraft.player.fallDistance = 0.0f;
            eventMotion.setOnGround(true);
            final Minecraft mc34 = AirJump.mc;
            Minecraft.player.onGround = true;
        }
    }
    
    public boolean validBlocks(final Block blocks) {
        return blocks != Blocks.AIR && !Arrays.asList(6, 27, 28, 31, 32, 37, 38, 39, 40, 44, 77, 143, 175).contains(Block.getIdFromBlock(blocks));
    }
}
