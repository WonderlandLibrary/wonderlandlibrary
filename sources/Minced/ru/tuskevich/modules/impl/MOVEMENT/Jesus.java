// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MOVEMENT;

import ru.tuskevich.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.MathHelper;
import ru.tuskevich.util.movement.MoveUtility;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventMotion;
import ru.tuskevich.ui.dropui.setting.imp.ModeSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "Jesus", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd \ufffd\ufffd\ufffd\ufffd", type = Type.MOVEMENT)
public class Jesus extends Module
{
    public ModeSetting mode;
    
    public Jesus() {
        this.mode = new ModeSetting("Mode", "Matrix Solid", new String[] { "Matrix Solid" });
    }
    
    @EventTarget
    public void onMotion(final EventMotion e) {
        if (this.mode.is("Matrix Solid")) {
            final WorldClient world = Jesus.mc.world;
            final Minecraft mc = Jesus.mc;
            final double posX = Minecraft.player.posX;
            final Minecraft mc2 = Jesus.mc;
            final double y = Minecraft.player.posY + 0.008;
            final Minecraft mc3 = Jesus.mc;
            if (world.getBlockState(new BlockPos(posX, y, Minecraft.player.posZ)).getBlock() == Blocks.WATER) {
                final Minecraft mc4 = Jesus.mc;
                if (!Minecraft.player.onGround) {
                    final WorldClient world2 = Jesus.mc.world;
                    final Minecraft mc5 = Jesus.mc;
                    final double posX2 = Minecraft.player.posX;
                    final Minecraft mc6 = Jesus.mc;
                    final double y2 = Minecraft.player.posY + 0.03;
                    final Minecraft mc7 = Jesus.mc;
                    final boolean isUp = world2.getBlockState(new BlockPos(posX2, y2, Minecraft.player.posZ)).getBlock() == Blocks.WATER;
                    final Minecraft mc8 = Jesus.mc;
                    Minecraft.player.jumpMovementFactor = 0.0f;
                    final float yport = (MoveUtility.getPlayerMotion() > 0.1) ? 0.02f : 0.032f;
                    final Minecraft mc9 = Jesus.mc;
                    final EntityPlayerSP player = Minecraft.player;
                    final Minecraft mc10 = Jesus.mc;
                    final double motionX = Minecraft.player.motionX;
                    final Minecraft mc11 = Jesus.mc;
                    final double y3 = (Minecraft.player.fallDistance < 3.5) ? (isUp ? yport : (-yport)) : -0.1;
                    final Minecraft mc12 = Jesus.mc;
                    player.setVelocity(motionX, y3, Minecraft.player.motionZ);
                }
            }
            final Minecraft mc13 = Jesus.mc;
            final double posY = Minecraft.player.posY;
            final Minecraft mc14 = Jesus.mc;
            Label_0502: {
                Label_0323: {
                    if (posY > (int)Minecraft.player.posY + 0.89) {
                        final Minecraft mc15 = Jesus.mc;
                        final double posY2 = Minecraft.player.posY;
                        final Minecraft mc16 = Jesus.mc;
                        if (posY2 <= (int)Minecraft.player.posY + 1) {
                            break Label_0323;
                        }
                    }
                    final Minecraft mc17 = Jesus.mc;
                    if (Minecraft.player.fallDistance <= 3.5) {
                        break Label_0502;
                    }
                }
                final Minecraft mc18 = Jesus.mc;
                final EntityPlayerSP player2 = Minecraft.player;
                final Minecraft mc19 = Jesus.mc;
                player2.posY = (int)Minecraft.player.posY + 1 + 1.0E-45;
                final Minecraft mc20 = Jesus.mc;
                if (!Minecraft.player.isInWater()) {
                    final WorldClient world3 = Jesus.mc.world;
                    final Minecraft mc21 = Jesus.mc;
                    final double posX3 = Minecraft.player.posX;
                    final Minecraft mc22 = Jesus.mc;
                    final double y4 = Minecraft.player.posY - 0.1;
                    final Minecraft mc23 = Jesus.mc;
                    if (world3.getBlockState(new BlockPos(posX3, y4, Minecraft.player.posZ)).getBlock() == Blocks.WATER) {
                        final Minecraft mc24 = Jesus.mc;
                        if (Minecraft.player.collidedHorizontally) {
                            final Minecraft mc25 = Jesus.mc;
                            Minecraft.player.motionY = 0.2;
                            final Minecraft mc26 = Jesus.mc;
                            final EntityPlayerSP player3 = Minecraft.player;
                            player3.motionX *= 0.0;
                            final Minecraft mc27 = Jesus.mc;
                            final EntityPlayerSP player4 = Minecraft.player;
                            player4.motionZ *= 0.0;
                        }
                        MoveUtility.setMotion(MathHelper.clamp((float)(MoveUtility.getPlayerMotion() + 0.20000000298023224), 0.5f, 1.14f));
                    }
                }
            }
            final Minecraft mc28 = Jesus.mc;
            if (!Minecraft.player.isInWater()) {
                final WorldClient world4 = Jesus.mc.world;
                final Minecraft mc29 = Jesus.mc;
                final double posX4 = Minecraft.player.posX;
                final Minecraft mc30 = Jesus.mc;
                final double y5 = Minecraft.player.posY + 0.15;
                final Minecraft mc31 = Jesus.mc;
                if (world4.getBlockState(new BlockPos(posX4, y5, Minecraft.player.posZ)).getBlock() != Blocks.WATER) {
                    return;
                }
            }
            final Minecraft mc32 = Jesus.mc;
            Minecraft.player.motionY = 0.16;
            final WorldClient world5 = Jesus.mc.world;
            final Minecraft mc33 = Jesus.mc;
            final double posX5 = Minecraft.player.posX;
            final Minecraft mc34 = Jesus.mc;
            final double y6 = Minecraft.player.posY + 2.0;
            final Minecraft mc35 = Jesus.mc;
            if (world5.getBlockState(new BlockPos(posX5, y6, Minecraft.player.posZ)).getBlock() == Blocks.AIR) {
                final Minecraft mc36 = Jesus.mc;
                Minecraft.player.motionY = 0.12;
            }
            final WorldClient world6 = Jesus.mc.world;
            final Minecraft mc37 = Jesus.mc;
            final double posX6 = Minecraft.player.posX;
            final Minecraft mc38 = Jesus.mc;
            final double y7 = Minecraft.player.posY + 1.0;
            final Minecraft mc39 = Jesus.mc;
            if (world6.getBlockState(new BlockPos(posX6, y7, Minecraft.player.posZ)).getBlock() == Blocks.AIR) {
                final Minecraft mc40 = Jesus.mc;
                Minecraft.player.motionY = 0.18;
            }
        }
    }
}
