/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemEgg
 *  net.minecraft.item.ItemEnderPearl
 *  net.minecraft.item.ItemFishingRod
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.item.ItemSnowball
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.Vec3
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.Cylinder
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

@ModuleInfo(name="Projectiles", description="Allows you to see where arrows will land.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/Projectiles;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "dynamicBowPower", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "onRender3D", "", "event", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "KyinoClient"})
public final class Projectiles
extends Module {
    private final BoolValue dynamicBowPower = new BoolValue("DynamicBowPower", true);

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        block29: {
            block30: {
                block28: {
                    Intrinsics.checkParameterIsNotNull(event, "event");
                    v0 = Projectiles.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(v0, "mc.thePlayer");
                    if (v0.func_70694_bm() == null) {
                        return;
                    }
                    v1 = Projectiles.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(v1, "mc.thePlayer");
                    v2 = v1.func_70694_bm();
                    Intrinsics.checkExpressionValueIsNotNull(v2, "mc.thePlayer.heldItem");
                    item = v2.func_77973_b();
                    v3 = Projectiles.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(v3, "mc");
                    renderManager = v3.func_175598_ae();
                    isBow = false;
                    motionFactor = 1.5f;
                    motionSlowdown = 0.99f;
                    gravity = 0.0f;
                    size = 0.0f;
                    if (!(item instanceof ItemBow)) break block28;
                    if (((Boolean)this.dynamicBowPower.get()).booleanValue()) {
                        v4 = Projectiles.access$getMc$p$s1046033730().field_71439_g;
                        Intrinsics.checkExpressionValueIsNotNull(v4, "mc.thePlayer");
                        if (!v4.func_71039_bw()) {
                            return;
                        }
                    }
                    isBow = true;
                    gravity = 0.05f;
                    size = 0.3f;
                    if (((Boolean)this.dynamicBowPower.get()).booleanValue()) {
                        v5 = Projectiles.access$getMc$p$s1046033730().field_71439_g;
                        Intrinsics.checkExpressionValueIsNotNull(v5, "mc.thePlayer");
                        v6 = v5.func_71057_bx();
                    } else {
                        v6 = item.func_77626_a(new ItemStack(item));
                    }
                    power = (float)v6 / 20.0f;
                    power = (power * power + power * 2.0f) / 3.0f;
                    if (power < 0.1f) {
                        return;
                    }
                    if (power > 1.0f) {
                        power = 1.0f;
                    }
                    motionFactor = power * 3.0f;
                    break block29;
                }
                if (!(item instanceof ItemFishingRod)) break block30;
                gravity = 0.04f;
                size = 0.25f;
                motionSlowdown = 0.92f;
                break block29;
            }
            if (!(item instanceof ItemPotion)) ** GOTO lbl-1000
            v7 = Projectiles.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(v7, "mc.thePlayer");
            v8 = v7.func_70694_bm();
            Intrinsics.checkExpressionValueIsNotNull(v8, "mc.thePlayer.heldItem");
            if (ItemPotion.func_77831_g((int)v8.func_77952_i())) {
                gravity = 0.05f;
                size = 0.25f;
                motionFactor = 0.5f;
            } else lbl-1000:
            // 2 sources

            {
                if (!(item instanceof ItemSnowball || item instanceof ItemEnderPearl || item instanceof ItemEgg)) {
                    return;
                }
                gravity = 0.03f;
                size = 0.25f;
            }
        }
        yaw = RotationUtils.targetRotation != null ? RotationUtils.targetRotation.getYaw() : Projectiles.access$getMc$p$s1046033730().field_71439_g.field_70177_z;
        pitch = RotationUtils.targetRotation != null ? RotationUtils.targetRotation.getPitch() : Projectiles.access$getMc$p$s1046033730().field_71439_g.field_70125_A;
        posX = renderManager.field_78725_b - (double)(MathHelper.func_76134_b((float)(yaw / 180.0f * 3.1415927f)) * 0.16f);
        posY = renderManager.field_78726_c + (double)Projectiles.access$getMc$p$s1046033730().field_71439_g.func_70047_e() - 0.10000000149011612;
        posZ = renderManager.field_78723_d - (double)(MathHelper.func_76126_a((float)(yaw / 180.0f * 3.1415927f)) * 0.16f);
        motionX = (double)(-MathHelper.func_76126_a((float)(yaw / 180.0f * 3.1415927f)) * MathHelper.func_76134_b((float)(pitch / 180.0f * 3.1415927f))) * (isBow != false ? 1.0 : 0.4);
        if (!(item instanceof ItemPotion)) ** GOTO lbl-1000
        v9 = Projectiles.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(v9, "mc.thePlayer");
        v10 = v9.func_70694_bm();
        Intrinsics.checkExpressionValueIsNotNull(v10, "mc.thePlayer.heldItem");
        if (ItemPotion.func_77831_g((int)v10.func_77952_i())) {
            v11 = -20;
        } else lbl-1000:
        // 2 sources

        {
            v11 = 0;
        }
        motionY = (double)(-MathHelper.func_76126_a((float)((pitch + (float)v11) / 180.0f * 3.1415927f))) * (isBow != false ? 1.0 : 0.4);
        motionZ = (double)(MathHelper.func_76134_b((float)(yaw / 180.0f * 3.1415927f)) * MathHelper.func_76134_b((float)(pitch / 180.0f * 3.1415927f))) * (isBow != false ? 1.0 : 0.4);
        distance = MathHelper.func_76133_a((double)(motionX * motionX + motionY * motionY + motionZ * motionZ));
        motionX /= (double)distance;
        motionY /= (double)distance;
        motionZ /= (double)distance;
        motionX *= (double)motionFactor;
        motionY *= (double)motionFactor;
        motionZ *= (double)motionFactor;
        landingPosition = null;
        hasLanded = false;
        hitEntity = false;
        v12 = tessellator = Tessellator.func_178181_a();
        Intrinsics.checkExpressionValueIsNotNull(v12, "tessellator");
        worldRenderer = v12.func_178180_c();
        GL11.glDepthMask((boolean)false);
        RenderUtils.enableGlCap(new int[]{3042, 2848});
        RenderUtils.disableGlCap(new int[]{2929, 3008, 3553});
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glHint((int)3154, (int)4354);
        RenderUtils.glColor(new Color(0, 160, 255, 255));
        GL11.glLineWidth((float)2.0f);
        worldRenderer.func_181668_a(3, DefaultVertexFormats.field_181705_e);
        while (!hasLanded && posY > 0.0) {
            posBefore = new Vec3(posX, posY, posZ);
            posAfter = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
            landingPosition = Projectiles.access$getMc$p$s1046033730().field_71441_e.func_147447_a(posBefore, posAfter, false, true, false);
            posBefore = new Vec3(posX, posY, posZ);
            posAfter = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
            if (landingPosition != null) {
                hasLanded = true;
                posAfter = new Vec3(landingPosition.field_72307_f.field_72450_a, landingPosition.field_72307_f.field_72448_b, landingPosition.field_72307_f.field_72449_c);
            }
            arrowBox = new AxisAlignedBB(posX - (double)size, posY - (double)size, posZ - (double)size, posX + (double)size, posY + (double)size, posZ + (double)size).func_72321_a(motionX, motionY, motionZ).func_72314_b(1.0, 1.0, 1.0);
            chunkMinX = MathHelper.func_76128_c((double)((arrowBox.field_72340_a - 2.0) / 16.0));
            chunkMaxX = MathHelper.func_76128_c((double)((arrowBox.field_72336_d + 2.0) / 16.0));
            chunkMinZ = MathHelper.func_76128_c((double)((arrowBox.field_72339_c - 2.0) / 16.0));
            chunkMaxZ = MathHelper.func_76128_c((double)((arrowBox.field_72334_f + 2.0) / 16.0));
            var37_31 = 0;
            collidedEntities = new ArrayList<E>();
            var37_31 = chunkMinX;
            var38_34 = chunkMaxX;
            if (var37_31 <= var38_34) {
                while (true) {
                    if ((var39_35 = chunkMinZ) <= (var40_37 = chunkMaxZ)) {
                        while (true) {
                            Projectiles.access$getMc$p$s1046033730().field_71441_e.func_72964_e((int)x, (int)z).func_177414_a((Entity)Projectiles.access$getMc$p$s1046033730().field_71439_g, arrowBox, collidedEntities, null);
                            if (z == var40_37) break;
                            ++z;
                        }
                    }
                    if (x == var38_34) break;
                    ++x;
                }
            }
            for (Entity possibleEntity : collidedEntities) {
                if (!possibleEntity.func_70067_L() || possibleEntity == Projectiles.access$getMc$p$s1046033730().field_71439_g) continue;
                possibleEntityBoundingBox = possibleEntity.func_174813_aQ().func_72314_b((double)size, (double)size, (double)size);
                if (possibleEntityBoundingBox.func_72327_a(posBefore, posAfter) == null) {
                    continue;
                }
                hitEntity = true;
                hasLanded = true;
                landingPosition = possibleEntityLanding;
            }
            v13 = Projectiles.access$getMc$p$s1046033730().field_71441_e.func_180495_p(new BlockPos(posX += motionX, posY += motionY, posZ += motionZ));
            Intrinsics.checkExpressionValueIsNotNull(v13, "mc.theWorld.getBlockStat\u2026ockPos(posX, posY, posZ))");
            v14 = v13.func_177230_c();
            Intrinsics.checkExpressionValueIsNotNull(v14, "mc.theWorld.getBlockStat\u2026(posX, posY, posZ)).block");
            if (v14.func_149688_o() == Material.field_151586_h) {
                motionX *= 0.6;
                motionY *= 0.6;
                motionZ *= 0.6;
            } else {
                motionX *= (double)motionSlowdown;
                motionY *= (double)motionSlowdown;
                motionZ *= (double)motionSlowdown;
            }
            motionY -= (double)gravity;
            worldRenderer.func_181662_b(posX - renderManager.field_78725_b, posY - renderManager.field_78726_c, posZ - renderManager.field_78723_d).func_181675_d();
        }
        tessellator.func_78381_a();
        GL11.glPushMatrix();
        GL11.glTranslated((double)(posX - renderManager.field_78725_b), (double)(posY - renderManager.field_78726_c), (double)(posZ - renderManager.field_78723_d));
        if (landingPosition != null) {
            v15 = landingPosition.field_178784_b;
            Intrinsics.checkExpressionValueIsNotNull(v15, "landingPosition.sideHit");
            switch (v15.func_176740_k().ordinal()) {
                case 0: {
                    GL11.glRotatef((float)90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                    break;
                }
                case 2: {
                    GL11.glRotatef((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                }
            }
            if (hitEntity) {
                RenderUtils.glColor(new Color(255, 0, 0, 150));
            }
        }
        GL11.glRotatef((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        cylinder = new Cylinder();
        cylinder.setDrawStyle(100011);
        cylinder.draw(0.2f, 0.0f, 0.0f, 60, 1);
        GL11.glPopMatrix();
        GL11.glDepthMask((boolean)true);
        RenderUtils.resetCaps();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

