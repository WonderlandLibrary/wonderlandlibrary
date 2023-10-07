/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBucketMilk
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.ChatComponentText
 *  net.minecraft.util.IChatComponent
 *  net.minecraft.util.MathHelper
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.util.vector.Vector3f
 */
package net.ccbluex.liquidbounce.utils.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.util.vector.Vector3f;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\t\u0018\u0000 \f2\u00020\u0001:\u0001\fB\u0005\u00a2\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\b\u00a8\u0006\r"}, d2={"Lnet/ccbluex/liquidbounce/utils/entity/PlayerUtik;", "", "()V", "collided", "", "getCollided", "()Z", "setCollided", "(Z)V", "shouldslow", "getShouldslow", "setShouldslow", "Companion", "KyinoClient"})
public final class PlayerUtik {
    private boolean shouldslow;
    private boolean collided;
    private static final Minecraft mc;
    public static final Companion Companion;

    public final boolean getShouldslow() {
        return this.shouldslow;
    }

    public final void setShouldslow(boolean bl) {
        this.shouldslow = bl;
    }

    public final boolean getCollided() {
        return this.collided;
    }

    public final void setCollided(boolean bl) {
        this.collided = bl;
    }

    static {
        Companion = new Companion(null);
        Minecraft minecraft = Minecraft.func_71410_x();
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "Minecraft.getMinecraft()");
        mc = minecraft;
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000p\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010&\u001a\u00020\u0010J\u0018\u0010'\u001a\u00020(2\b\u0010)\u001a\u0004\u0018\u00010*2\u0006\u0010+\u001a\u00020\u0010J\u0010\u0010,\u001a\u00020\u00102\b\u0010-\u001a\u0004\u0018\u00010\fJ\u0006\u0010.\u001a\u00020\u0004J\u0010\u0010/\u001a\u0002002\b\u00101\u001a\u0004\u0018\u000102J&\u00103\u001a\u0002002\u0006\u00104\u001a\u0002052\u0006\u00106\u001a\u00020\u00042\u0006\u00107\u001a\u00020\u00042\u0006\u00108\u001a\u00020\u0004J\u000e\u00109\u001a\u0002002\u0006\u00104\u001a\u000205J\u000e\u0010\t\u001a\u00020\b2\u0006\u0010:\u001a\u00020\bJ6\u0010;\u001a\u00020\u00042\u0006\u0010<\u001a\u00020\u00042\u0006\u0010=\u001a\u00020\u00042\u0006\u0010>\u001a\u00020\u00042\u0006\u00106\u001a\u00020\u00042\u0006\u00107\u001a\u00020\u00042\u0006\u00108\u001a\u00020\u0004J\u0010\u0010?\u001a\u0002022\b\u0010@\u001a\u0004\u0018\u00010\fJ\u0016\u0010A\u001a\u00020\u00042\u0006\u0010B\u001a\u00020\u00042\u0006\u0010C\u001a\u00020\u0004J\u0006\u0010D\u001a\u00020\u0010J\u000e\u0010E\u001a\u00020\u00102\u0006\u0010F\u001a\u00020\u0004J\u0006\u0010G\u001a\u00020\u0010J\u000e\u0010H\u001a\u00020(2\u0006\u0010\u001e\u001a\u00020\u0004J\u0010\u0010I\u001a\u00020(2\b\u0010J\u001a\u0004\u0018\u00010\fJ\u000e\u0010K\u001a\u00020(2\u0006\u0010\u001e\u001a\u00020\u0004J6\u0010L\u001a\u0012\u0012\u0004\u0012\u00020N0Mj\b\u0012\u0004\u0012\u00020N`O2\u0006\u0010P\u001a\u00020\u00042\u0006\u0010Q\u001a\u00020\u00042\u0006\u0010R\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0004R\u0011\u0010\u0003\u001a\u00020\u00048F\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\b8F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\f8F\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\u00108F\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0011R\u0011\u0010\u0012\u001a\u00020\u00108F\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0011R\u0011\u0010\u0013\u001a\u00020\u00108F\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u0011R\u0011\u0010\u0014\u001a\u00020\u00108F\u00a2\u0006\u0006\u001a\u0004\b\u0014\u0010\u0011R\u0011\u0010\u0015\u001a\u00020\u00108F\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u0011R\u0011\u0010\u0016\u001a\u00020\u00108F\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\u0011R\u0011\u0010\u0017\u001a\u00020\u00108F\u00a2\u0006\u0006\u001a\u0004\b\u0017\u0010\u0011R\u0011\u0010\u0018\u001a\u00020\u00108F\u00a2\u0006\u0006\u001a\u0004\b\u0018\u0010\u0011R\u0011\u0010\u0019\u001a\u00020\u00108F\u00a2\u0006\u0006\u001a\u0004\b\u0019\u0010\u0011R\u0011\u0010\u001a\u001a\u00020\b8F\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\nR\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u0004\u00a2\u0006\u0002\n\u0000R$\u0010\u001e\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u00048F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u001f\u0010\u0006\"\u0004\b \u0010!R\u0011\u0010\"\u001a\u00020#8F\u00a2\u0006\u0006\u001a\u0004\b$\u0010%\u00a8\u0006S"}, d2={"Lnet/ccbluex/liquidbounce/utils/entity/PlayerUtik$Companion;", "", "()V", "baseMoveSpeed", "", "getBaseMoveSpeed", "()D", "direction", "", "getDirection", "()F", "iP", "", "getIP", "()Ljava/lang/String;", "isBlockUnder", "", "()Z", "isHoldingSword", "isInLiquid", "isInWater", "isMoving", "isMoving2", "isOnHypixel", "isOnLiquid", "isOnWater", "maxFallDist", "getMaxFallDist", "mc", "Lnet/minecraft/client/Minecraft;", "speed", "getSpeed", "setSpeed", "(D)V", "speedEffect", "", "getSpeedEffect", "()I", "MovementInput", "blockHit", "", "en", "Lnet/minecraft/entity/Entity;", "value", "checkServer", "serverIP", "defaultSpeed", "getBlock", "Lnet/minecraft/block/Block;", "pos", "Lnet/minecraft/util/BlockPos;", "getBlockAtPosC", "inPlayer", "Lnet/minecraft/entity/player/EntityPlayer;", "x2", "y2", "z2", "getBlockUnderPlayer", "yaw", "getDistance", "x1", "y1", "z1", "getHypixelBlockpos", "str", "getIncremental", "val", "inc", "isBlockUnders", "isOnGround1", "height", "isUsingFood", "setMotion", "tellPlayer", "string", "toFwd", "vanillaTeleportPositions", "Ljava/util/ArrayList;", "Lorg/lwjgl/util/vector/Vector3f;", "Lkotlin/collections/ArrayList;", "tpX", "tpY", "tpZ", "KyinoClient"})
    public static final class Companion {
        public final float getDirection() {
            float yaw = mc.field_71439_g.field_70177_z;
            if (mc.field_71439_g.field_70701_bs < 0.0f) {
                yaw += 180.0f;
            }
            float forward = 1.0f;
            if (mc.field_71439_g.field_70701_bs < 0.0f) {
                forward = -0.5f;
            } else if (mc.field_71439_g.field_70701_bs > 0.0f) {
                forward = 0.5f;
            }
            if (mc.field_71439_g.field_70702_br > 0.0f) {
                yaw -= 90.0f * forward;
            }
            if (mc.field_71439_g.field_70702_br < 0.0f) {
                yaw += 90.0f * forward;
            }
            float f = (float)Math.PI / 180;
            boolean bl = false;
            boolean bl2 = false;
            float it = f;
            boolean bl3 = false;
            return yaw *= it;
        }

        public final boolean isInWater() {
            IBlockState iBlockState = mc.field_71441_e.func_180495_p(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v));
            Intrinsics.checkExpressionValueIsNotNull(iBlockState, "mc.theWorld\n            \u2026posY, mc.thePlayer.posZ))");
            Block block = iBlockState.func_177230_c();
            Intrinsics.checkExpressionValueIsNotNull(block, "mc.theWorld\n            \u2026                   .block");
            return block.func_149688_o() == Material.field_151586_h;
        }

        /*
         * WARNING - void declaration
         */
        public final boolean isOnWater() {
            double y = mc.field_71439_g.field_70163_u - 0.03;
            int n = MathHelper.func_76128_c((double)mc.field_71439_g.field_70165_t);
            int n2 = MathHelper.func_76143_f((double)mc.field_71439_g.field_70165_t);
            while (n < n2) {
                void x;
                int n3 = MathHelper.func_76128_c((double)mc.field_71439_g.field_70161_v);
                int n4 = MathHelper.func_76143_f((double)mc.field_71439_g.field_70161_v);
                while (n3 < n4) {
                    void z;
                    BlockPos pos = new BlockPos((int)x, MathHelper.func_76128_c((double)y), (int)z);
                    IBlockState iBlockState = mc.field_71441_e.func_180495_p(pos);
                    Intrinsics.checkExpressionValueIsNotNull(iBlockState, "mc.theWorld.getBlockState(pos)");
                    if (iBlockState.func_177230_c() instanceof BlockLiquid) {
                        IBlockState iBlockState2 = mc.field_71441_e.func_180495_p(pos);
                        Intrinsics.checkExpressionValueIsNotNull(iBlockState2, "mc.theWorld.getBlockState(pos)");
                        Block block = iBlockState2.func_177230_c();
                        Intrinsics.checkExpressionValueIsNotNull(block, "mc.theWorld.getBlockState(pos).block");
                        if (block.func_149688_o() == Material.field_151586_h) {
                            return true;
                        }
                    }
                    ++z;
                }
                ++x;
            }
            return false;
        }

        public final void toFwd(double speed) {
            float yaw = mc.field_71439_g.field_70177_z * ((float)Math.PI / 180);
            mc.field_71439_g.field_70159_w -= (double)MathHelper.func_76126_a((float)yaw) * speed;
            mc.field_71439_g.field_70179_y += (double)MathHelper.func_76134_b((float)yaw) * speed;
        }

        public final void tellPlayer(@Nullable String string) {
            mc.field_71439_g.func_145747_a((IChatComponent)new ChatComponentText(string));
        }

        public final double getSpeed() {
            return Math.sqrt(mc.field_71439_g.field_70159_w * mc.field_71439_g.field_70159_w + mc.field_71439_g.field_70179_y * mc.field_71439_g.field_70179_y);
        }

        public final void setSpeed(double speed) {
            mc.field_71439_g.field_70159_w = -Math.sin(Companion.getDirection()) * speed;
            mc.field_71439_g.field_70179_y = Math.cos(Companion.getDirection()) * speed;
        }

        @NotNull
        public final Block getBlockUnderPlayer(@NotNull EntityPlayer inPlayer) {
            Intrinsics.checkParameterIsNotNull(inPlayer, "inPlayer");
            return this.getBlock(new BlockPos(inPlayer.field_70165_t, inPlayer.field_70163_u - 1.0, inPlayer.field_70161_v));
        }

        @NotNull
        public final Block getBlock(@Nullable BlockPos pos) {
            Minecraft.func_71410_x();
            IBlockState iBlockState = mc.field_71441_e.func_180495_p(pos);
            Intrinsics.checkExpressionValueIsNotNull(iBlockState, "mc.theWorld.getBlockState(pos)");
            Block block = iBlockState.func_177230_c();
            Intrinsics.checkExpressionValueIsNotNull(block, "mc.theWorld.getBlockState(pos).block");
            return block;
        }

        @NotNull
        public final Block getBlockAtPosC(@NotNull EntityPlayer inPlayer, double x2, double y2, double z2) {
            Intrinsics.checkParameterIsNotNull(inPlayer, "inPlayer");
            return this.getBlock(new BlockPos(inPlayer.field_70165_t - x2, inPlayer.field_70163_u - y2, inPlayer.field_70161_v - z2));
        }

        @NotNull
        public final ArrayList<Vector3f> vanillaTeleportPositions(double tpX, double tpY, double tpZ, double speed) {
            double d2;
            ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
            Minecraft minecraft = Minecraft.func_71410_x();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "Minecraft.getMinecraft()");
            Minecraft mc2 = minecraft;
            double posX = tpX - mc.field_71439_g.field_70165_t;
            double posY = tpY - (mc.field_71439_g.field_70163_u + (double)mc.field_71439_g.func_70047_e() + 1.1);
            double posZ = tpZ - mc.field_71439_g.field_70161_v;
            float yaw = (float)(Math.atan2(posZ, posX) * 180.0 / Math.PI - 90.0);
            float pitch = (float)(-Math.atan2(posY, Math.sqrt(posX * posX + posZ * posZ)) * 180.0 / Math.PI);
            double tmpX = mc.field_71439_g.field_70165_t;
            double tmpY = mc.field_71439_g.field_70163_u;
            double tmpZ = mc.field_71439_g.field_70161_v;
            double steps = 1.0;
            for (d2 = speed; d2 < this.getDistance(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, tpX, tpY, tpZ); d2 += speed) {
                steps += 1.0;
            }
            for (d2 = speed; d2 < this.getDistance(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, tpX, tpY, tpZ); d2 += speed) {
                tmpX = mc.field_71439_g.field_70165_t - Math.sin(this.getDirection(yaw)) * d2;
                tmpZ = mc.field_71439_g.field_70161_v + Math.cos(this.getDirection(yaw)) * d2;
                double d = steps;
                double d3 = mc.field_71439_g.field_70163_u - tpY;
                float f = (float)tmpX;
                ArrayList<Vector3f> arrayList = positions;
                boolean bl = false;
                boolean bl2 = false;
                double it = d;
                boolean bl3 = false;
                double d4 = tmpY -= it;
                float f2 = (float)tmpZ;
                float f3 = (float)(d3 / d4);
                float f4 = f;
                arrayList.add(new Vector3f(f4, f3, f2));
            }
            positions.add(new Vector3f((float)tpX, (float)tpY, (float)tpZ));
            return positions;
        }

        public final float getDirection(float yaw) {
            float yaw2 = yaw;
            Minecraft.func_71410_x();
            if (mc.field_71439_g.field_70701_bs < 0.0f) {
                yaw2 += 180.0f;
            }
            float forward = 1.0f;
            Minecraft.func_71410_x();
            if (mc.field_71439_g.field_70701_bs < 0.0f) {
                forward = -0.5f;
            } else {
                Minecraft.func_71410_x();
                if (mc.field_71439_g.field_70701_bs > 0.0f) {
                    forward = 0.5f;
                }
            }
            Minecraft.func_71410_x();
            if (mc.field_71439_g.field_70702_br > 0.0f) {
                yaw2 -= 90.0f * forward;
            }
            Minecraft.func_71410_x();
            if (mc.field_71439_g.field_70702_br < 0.0f) {
                yaw2 += 90.0f * forward;
            }
            float f = (float)Math.PI / 180;
            boolean bl = false;
            boolean bl2 = false;
            float it = f;
            boolean bl3 = false;
            return yaw2 *= it;
        }

        public final double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
            double d0 = x1 - x2;
            double d1 = y1 - y2;
            double d2 = z1 - z2;
            return MathHelper.func_76133_a((double)(d0 * d0 + d1 * d1 + d2 * d2));
        }

        public final boolean MovementInput() {
            return mc.field_71474_y.field_74351_w.field_74513_e || mc.field_71474_y.field_74370_x.field_74513_e || mc.field_71474_y.field_74366_z.field_74513_e || mc.field_71474_y.field_74368_y.field_74513_e;
        }

        public final boolean isMoving() {
            if (!mc.field_71439_g.field_70123_F) {
                EntityPlayerSP entityPlayerSP = mc.field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                if (!entityPlayerSP.func_70093_af()) {
                    return mc.field_71439_g.field_71158_b.field_78900_b != 0.0f || mc.field_71439_g.field_71158_b.field_78902_a != 0.0f;
                }
            }
            return false;
        }

        public final double getIncremental(double val, double inc) {
            double one = 1.0 / inc;
            return (double)Math.round(val * one) / one;
        }

        public final boolean isMoving2() {
            return mc.field_71439_g.field_70701_bs != 0.0f || mc.field_71439_g.field_70702_br != 0.0f;
        }

        /*
         * WARNING - void declaration
         */
        public final boolean isInLiquid() {
            if (mc.field_71439_g == null) {
                return false;
            }
            EntityPlayerSP entityPlayerSP = mc.field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            int n = MathHelper.func_76128_c((double)entityPlayerSP.func_174813_aQ().field_72340_a);
            EntityPlayerSP entityPlayerSP2 = mc.field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
            int n2 = MathHelper.func_76128_c((double)entityPlayerSP2.func_174813_aQ().field_72336_d) + 1;
            while (n < n2) {
                void x;
                EntityPlayerSP entityPlayerSP3 = mc.field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP3, "mc.thePlayer");
                int n3 = MathHelper.func_76128_c((double)entityPlayerSP3.func_174813_aQ().field_72339_c);
                EntityPlayerSP entityPlayerSP4 = mc.field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP4, "mc.thePlayer");
                int n4 = MathHelper.func_76128_c((double)entityPlayerSP4.func_174813_aQ().field_72334_f) + 1;
                while (n3 < n4) {
                    void z;
                    EntityPlayerSP entityPlayerSP5 = mc.field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP5, "mc.thePlayer");
                    BlockPos pos = new BlockPos((int)x, (int)entityPlayerSP5.func_174813_aQ().field_72338_b, (int)z);
                    IBlockState iBlockState = mc.field_71441_e.func_180495_p(pos);
                    Intrinsics.checkExpressionValueIsNotNull(iBlockState, "mc.theWorld.getBlockState(pos)");
                    Block block = iBlockState.func_177230_c();
                    if (block != null && !(block instanceof BlockAir)) {
                        return block instanceof BlockLiquid;
                    }
                    ++z;
                }
                ++x;
            }
            return false;
        }

        public final boolean isOnGround1(double height) {
            WorldClient worldClient = mc.field_71441_e;
            EntityPlayerSP entityPlayerSP = mc.field_71439_g;
            if (entityPlayerSP == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.minecraft.entity.Entity");
            }
            Entity entity = (Entity)entityPlayerSP;
            EntityPlayerSP entityPlayerSP2 = mc.field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
            return !worldClient.func_72945_a(entity, entityPlayerSP2.func_174813_aQ().func_72317_d(0.0, -height, 0.0)).isEmpty();
        }

        public final void setMotion(double speed) {
            double forward = mc.field_71439_g.field_71158_b.field_78900_b;
            double strafe = mc.field_71439_g.field_71158_b.field_78902_a;
            float yaw = mc.field_71439_g.field_70177_z;
            if (forward == 0.0 && strafe == 0.0) {
                mc.field_71439_g.field_70159_w = 0.0;
                mc.field_71439_g.field_70179_y = 0.0;
            } else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        yaw += (float)(forward > 0.0 ? -45 : 45);
                    } else if (strafe < 0.0) {
                        yaw += (float)(forward > 0.0 ? 45 : -45);
                    }
                    strafe = 0.0;
                    if (forward > 0.0) {
                        forward = 1.0;
                    } else if (forward < 0.0) {
                        forward = -1.0;
                    }
                }
                mc.field_71439_g.field_70159_w = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
                mc.field_71439_g.field_70179_y = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
            }
        }

        public final double getBaseMoveSpeed() {
            double baseSpeed = 0.2873;
            if (mc.field_71439_g.func_70644_a(Potion.field_76424_c)) {
                PotionEffect potionEffect = mc.field_71439_g.func_70660_b(Potion.field_76424_c);
                Intrinsics.checkExpressionValueIsNotNull(potionEffect, "mc.thePlayer.getActivePo\u2026nEffect(Potion.moveSpeed)");
                int amplifier = potionEffect.func_76458_c();
                baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
            }
            return baseSpeed;
        }

        /*
         * WARNING - void declaration
         */
        @NotNull
        public final BlockPos getHypixelBlockpos(@Nullable String str) {
            int val = 89;
            if (str != null && str.length() > 1) {
                String string = str;
                int n = 0;
                char[] cArray = string.toCharArray();
                Intrinsics.checkExpressionValueIsNotNull(cArray, "(this as java.lang.String).toCharArray()");
                char[] chs = cArray;
                int lenght = chs.length;
                n = 0;
                int n2 = lenght;
                while (n < n2) {
                    void i;
                    val += chs[i] * str.length() * str.length() + str.charAt(0) + str.charAt(1);
                    ++i;
                }
                val /= str.length();
            }
            return new BlockPos(val, -val % 255, val);
        }

        public final void blockHit(@Nullable Entity en, boolean value) {
            Minecraft minecraft = Minecraft.func_71410_x();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "Minecraft.getMinecraft()");
            Minecraft mc = minecraft;
            EntityPlayerSP entityPlayerSP = mc.field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            ItemStack itemStack = entityPlayerSP.func_71045_bC();
            Intrinsics.checkExpressionValueIsNotNull(itemStack, "mc.thePlayer.currentEquippedItem");
            ItemStack stack = itemStack;
            EntityPlayerSP entityPlayerSP2 = mc.field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
            if (entityPlayerSP2.func_71045_bC() != null && en != null && value && stack.func_77973_b() instanceof ItemSword && (double)mc.field_71439_g.field_70733_aJ > 0.2) {
                KeyBinding keyBinding = mc.field_71474_y.field_74313_G;
                Intrinsics.checkExpressionValueIsNotNull(keyBinding, "mc.gameSettings.keyBindUseItem");
                KeyBinding.func_74507_a((int)keyBinding.func_151463_i());
            }
        }

        /*
         * WARNING - void declaration
         */
        public final boolean isOnLiquid() {
            EntityPlayerSP entityPlayerSP = mc.field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            AxisAlignedBB boundingBox = entityPlayerSP.func_174813_aQ();
            if (boundingBox == null) {
                return false;
            }
            boundingBox = boundingBox.func_72331_e(0.01, 0.0, 0.01).func_72317_d(0.0, -0.01, 0.0);
            boolean onLiquid = false;
            int y = (int)boundingBox.field_72338_b;
            int n = MathHelper.func_76128_c((double)boundingBox.field_72340_a);
            int n2 = MathHelper.func_76128_c((double)(boundingBox.field_72336_d + 1.0));
            while (n < n2) {
                void x;
                int n3 = MathHelper.func_76128_c((double)boundingBox.field_72339_c);
                int n4 = MathHelper.func_76128_c((double)(boundingBox.field_72334_f + 1.0));
                while (n3 < n4) {
                    Block block;
                    void z;
                    IBlockState iBlockState = mc.field_71441_e.func_180495_p(new BlockPos((int)x, y, (int)z));
                    Intrinsics.checkExpressionValueIsNotNull(iBlockState, "mc.theWorld.getBlockState((BlockPos(x, y, z)))");
                    Intrinsics.checkExpressionValueIsNotNull(iBlockState.func_177230_c(), "mc.theWorld.getBlockStat\u2026BlockPos(x, y, z))).block");
                    if (block != Blocks.field_150350_a) {
                        if (!(block instanceof BlockLiquid)) {
                            return false;
                        }
                        onLiquid = true;
                    }
                    ++z;
                }
                ++x;
            }
            return onLiquid;
        }

        public final float getMaxFallDist() {
            PotionEffect potioneffect = mc.field_71439_g.func_70660_b(Potion.field_76430_j);
            int f = potioneffect != null ? potioneffect.func_76458_c() + 1 : 0;
            EntityPlayerSP entityPlayerSP = mc.field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            return entityPlayerSP.func_82143_as() + f;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public final boolean isHoldingSword() {
            EntityPlayerSP entityPlayerSP = mc.field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            if (entityPlayerSP.func_71045_bC() == null) return false;
            EntityPlayerSP entityPlayerSP2 = mc.field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
            ItemStack itemStack = entityPlayerSP2.func_71045_bC();
            Intrinsics.checkExpressionValueIsNotNull(itemStack, "mc.thePlayer.currentEquippedItem");
            if (!(itemStack.func_77973_b() instanceof ItemSword)) return false;
            return true;
        }

        public final boolean isBlockUnder() {
            if (mc.field_71439_g.field_70163_u < 0.0) {
                return false;
            }
            for (int off = 0; off < (int)mc.field_71439_g.field_70163_u + 2; off += 2) {
                AxisAlignedBB bb;
                EntityPlayerSP entityPlayerSP = mc.field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP.func_174813_aQ().func_72317_d(0.0, -((double)off), 0.0), "mc.thePlayer.entityBound\u2026.0, -off.toDouble(), 0.0)");
                if (mc.field_71441_e.func_72945_a((Entity)mc.field_71439_g, bb).isEmpty()) continue;
                return true;
            }
            return false;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public final boolean isOnHypixel() {
            if (mc.func_71356_B()) return false;
            String string = mc.func_147104_D().field_78845_b;
            Intrinsics.checkExpressionValueIsNotNull(string, "mc.currentServerData.serverIP");
            if (!StringsKt.contains$default((CharSequence)string, "hypixel", false, 2, null)) return false;
            return true;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public final boolean checkServer(@Nullable String serverIP) {
            if (mc.func_71356_B()) return false;
            String string = mc.func_147104_D().field_78845_b;
            Intrinsics.checkExpressionValueIsNotNull(string, "mc.currentServerData.serverIP");
            CharSequence charSequence = string;
            String string2 = serverIP;
            if (string2 == null) {
                Intrinsics.throwNpe();
            }
            if (!StringsKt.contains$default(charSequence, string2, false, 2, null)) return false;
            return true;
        }

        @NotNull
        public final String getIP() {
            String string;
            if (mc.func_71356_B()) {
                string = "127.0.0.1";
            } else {
                String string2 = mc.func_147104_D().field_78845_b;
                string = string2;
                Intrinsics.checkExpressionValueIsNotNull(string2, "mc.currentServerData.serverIP");
            }
            return string;
        }

        public final double defaultSpeed() {
            double baseSpeed = 0.2873;
            if (mc.field_71439_g.func_70644_a(Potion.field_76424_c)) {
                PotionEffect potionEffect = mc.field_71439_g.func_70660_b(Potion.field_76424_c);
                Intrinsics.checkExpressionValueIsNotNull(potionEffect, "mc.thePlayer.getActivePo\u2026nEffect(Potion.moveSpeed)");
                int amplifier = potionEffect.func_76458_c();
                baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
            }
            return baseSpeed;
        }

        public final int getSpeedEffect() {
            if (mc.field_71439_g.func_70644_a(Potion.field_76424_c)) {
                PotionEffect potionEffect = mc.field_71439_g.func_70660_b(Potion.field_76424_c);
                Intrinsics.checkExpressionValueIsNotNull(potionEffect, "mc.thePlayer.getActivePo\u2026nEffect(Potion.moveSpeed)");
                return potionEffect.func_76458_c() + 1;
            }
            return 0;
        }

        public final boolean isUsingFood() {
            boolean bl;
            EntityPlayerSP entityPlayerSP = mc.field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            ItemStack itemStack = entityPlayerSP.func_71011_bu();
            Intrinsics.checkExpressionValueIsNotNull(itemStack, "mc.thePlayer.itemInUse");
            Item usingItem = itemStack.func_77973_b();
            EntityPlayerSP entityPlayerSP2 = mc.field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
            if (entityPlayerSP2.func_71011_bu() != null) {
                EntityPlayerSP entityPlayerSP3 = mc.field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP3, "mc.thePlayer");
                bl = entityPlayerSP3.func_71039_bw() && (usingItem instanceof ItemFood || usingItem instanceof ItemBucketMilk || usingItem instanceof ItemPotion);
            } else {
                bl = false;
            }
            return bl;
        }

        public final boolean isBlockUnders() {
            if (mc.field_71439_g.field_70163_u < 0.0) {
                return false;
            }
            for (int off = 0; off < (int)mc.field_71439_g.field_70163_u + 2; off += 2) {
                AxisAlignedBB bb;
                Intrinsics.checkExpressionValueIsNotNull(mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, -((double)off), 0.0), "mc.thePlayer.getEntityBo\u2026.0, -off.toDouble(), 0.0)");
                List list = mc.field_71441_e.func_72945_a((Entity)mc.field_71439_g, bb);
                Intrinsics.checkExpressionValueIsNotNull(list, "mc.theWorld.getColliding\u2026                        )");
                Collection collection = list;
                boolean bl = false;
                if (!(!collection.isEmpty())) continue;
                return true;
            }
            return false;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

