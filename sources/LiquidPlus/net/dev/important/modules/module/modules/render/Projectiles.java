/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.WorldRenderer
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemEgg
 *  net.minecraft.item.ItemEnderPearl
 *  net.minecraft.item.ItemFishingRod
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.item.ItemSnowball
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.util.Vec3
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.Cylinder
 */
package net.dev.important.modules.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Render3DEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.RotationUtils;
import net.dev.important.utils.render.ColorUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSnowball;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

@Info(name="Projectiles", description="Allows you to see where arrows will land.", category=Category.RENDER, cnName="\u629b\u7269\u7ebf")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J \u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000eJ\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2={"Lnet/dev/important/modules/module/modules/render/Projectiles;", "Lnet/dev/important/modules/module/Module;", "()V", "colorBlueValue", "Lnet/dev/important/value/IntegerValue;", "colorGreenValue", "colorMode", "Lnet/dev/important/value/ListValue;", "colorRedValue", "interpolateHSB", "Ljava/awt/Color;", "startColor", "endColor", "process", "", "onRender3D", "", "event", "Lnet/dev/important/event/Render3DEvent;", "LiquidBounce"})
public final class Projectiles
extends Module {
    @NotNull
    private final ListValue colorMode;
    @NotNull
    private final IntegerValue colorRedValue;
    @NotNull
    private final IntegerValue colorGreenValue;
    @NotNull
    private final IntegerValue colorBlueValue;

    public Projectiles() {
        String[] stringArray = new String[]{"Custom", "BowPower", "Rainbow"};
        this.colorMode = new ListValue("Color", stringArray, "Custom");
        this.colorRedValue = new IntegerValue("R", 0, 0, 255);
        this.colorGreenValue = new IntegerValue("G", 160, 0, 255);
        this.colorBlueValue = new IntegerValue("B", 255, 0, 255);
    }

    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (MinecraftInstance.mc.field_71439_g.func_70694_bm() == null) {
            return;
        }
        Item item = MinecraftInstance.mc.field_71439_g.func_70694_bm().func_77973_b();
        RenderManager renderManager = MinecraftInstance.mc.func_175598_ae();
        boolean isBow = false;
        float motionFactor = 1.5f;
        float motionSlowdown = 0.99f;
        float gravity = 0.0f;
        float size = 0.0f;
        if (item instanceof ItemBow) {
            if (!MinecraftInstance.mc.field_71439_g.func_71039_bw()) {
                return;
            }
            isBow = true;
            gravity = 0.05f;
            size = 0.3f;
            float power = (float)MinecraftInstance.mc.field_71439_g.func_71057_bx() / 20.0f;
            if ((power = (power * power + power * 2.0f) / 3.0f) < 0.1f) {
                return;
            }
            if (power > 1.0f) {
                power = 1.0f;
            }
            motionFactor = power * 3.0f;
        } else if (item instanceof ItemFishingRod) {
            gravity = 0.04f;
            size = 0.25f;
            motionSlowdown = 0.92f;
        } else if (item instanceof ItemPotion && ItemPotion.func_77831_g((int)MinecraftInstance.mc.field_71439_g.func_70694_bm().func_77952_i())) {
            gravity = 0.05f;
            size = 0.25f;
            motionFactor = 0.5f;
        } else {
            if (!(item instanceof ItemSnowball || item instanceof ItemEnderPearl || item instanceof ItemEgg)) {
                return;
            }
            gravity = 0.03f;
            size = 0.25f;
        }
        float yaw = RotationUtils.targetRotation != null ? RotationUtils.targetRotation.getYaw() : MinecraftInstance.mc.field_71439_g.field_70177_z;
        float pitch = RotationUtils.targetRotation != null ? RotationUtils.targetRotation.getPitch() : MinecraftInstance.mc.field_71439_g.field_70125_A;
        float yawRadians = yaw / 180.0f * (float)Math.PI;
        float pitchRadians = pitch / 180.0f * (float)Math.PI;
        double posX = renderManager.field_78725_b - (double)(MathHelper.func_76134_b((float)yawRadians) * 0.16f);
        double posY = renderManager.field_78726_c + (double)MinecraftInstance.mc.field_71439_g.func_70047_e() - (double)0.1f;
        double posZ = renderManager.field_78723_d - (double)(MathHelper.func_76126_a((float)yawRadians) * 0.16f);
        double motionX = (double)(-MathHelper.func_76126_a((float)yawRadians) * MathHelper.func_76134_b((float)pitchRadians)) * (isBow ? 1.0 : 0.4);
        double motionY = (double)(-MathHelper.func_76126_a((float)((pitch + (float)(item instanceof ItemPotion && ItemPotion.func_77831_g((int)MinecraftInstance.mc.field_71439_g.func_70694_bm().func_77952_i()) ? -20 : 0)) / 180.0f * (float)Math.PI))) * (isBow ? 1.0 : 0.4);
        double motionZ = (double)(MathHelper.func_76134_b((float)yawRadians) * MathHelper.func_76134_b((float)pitchRadians)) * (isBow ? 1.0 : 0.4);
        float distance = MathHelper.func_76133_a((double)(motionX * motionX + motionY * motionY + motionZ * motionZ));
        motionX /= (double)distance;
        motionY /= (double)distance;
        motionZ /= (double)distance;
        motionX *= (double)motionFactor;
        motionY *= (double)motionFactor;
        motionZ *= (double)motionFactor;
        MovingObjectPosition landingPosition = null;
        boolean hasLanded = false;
        boolean hitEntity = false;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        GL11.glDepthMask((boolean)false);
        Object object = new int[]{3042, 2848};
        RenderUtils.enableGlCap(object);
        object = new int[]{2929, 3008, 3553};
        RenderUtils.disableGlCap(object);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glHint((int)3154, (int)4354);
        String string = ((String)this.colorMode.get()).toLowerCase();
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
        switch ((Object)string) {
            case "custom": {
                RenderUtils.glColor(new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), 255));
                break;
            }
            case "bowpower": {
                Color color = Color.RED;
                Intrinsics.checkNotNullExpressionValue(color, "RED");
                Color color2 = color;
                color = Color.GREEN;
                Intrinsics.checkNotNullExpressionValue(color, "GREEN");
                RenderUtils.glColor(this.interpolateHSB(color2, color, motionFactor / (float)30 * (float)10));
                break;
            }
            case "rainbow": {
                RenderUtils.glColor(ColorUtils.rainbow());
            }
        }
        GL11.glLineWidth((float)2.0f);
        worldRenderer.func_181668_a(3, DefaultVertexFormats.field_181705_e);
        while (!hasLanded && posY > 0.0) {
            Vec3 posBefore = new Vec3(posX, posY, posZ);
            Vec3 posAfter = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
            landingPosition = MinecraftInstance.mc.field_71441_e.func_147447_a(posBefore, posAfter, false, true, false);
            posBefore = new Vec3(posX, posY, posZ);
            posAfter = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
            if (landingPosition != null) {
                hasLanded = true;
                posAfter = new Vec3(landingPosition.field_72307_f.field_72450_a, landingPosition.field_72307_f.field_72448_b, landingPosition.field_72307_f.field_72449_c);
            }
            AxisAlignedBB arrowBox = new AxisAlignedBB(posX - (double)size, posY - (double)size, posZ - (double)size, posX + (double)size, posY + (double)size, posZ + (double)size).func_72321_a(motionX, motionY, motionZ).func_72314_b(1.0, 1.0, 1.0);
            int chunkMinX = MathHelper.func_76128_c((double)((arrowBox.field_72340_a - 2.0) / 16.0));
            int chunkMaxX = MathHelper.func_76128_c((double)((arrowBox.field_72336_d + 2.0) / 16.0));
            int chunkMinZ = MathHelper.func_76128_c((double)((arrowBox.field_72339_c - 2.0) / 16.0));
            int chunkMaxZ = MathHelper.func_76128_c((double)((arrowBox.field_72334_f + 2.0) / 16.0));
            List collidedEntities = new ArrayList();
            int n = chunkMinX;
            if (n <= chunkMaxX) {
                int x;
                do {
                    int z;
                    x = n++;
                    int n2 = chunkMinZ;
                    if (n2 > chunkMaxZ) continue;
                    do {
                        z = n2++;
                        MinecraftInstance.mc.field_71441_e.func_72964_e(x, z).func_177414_a((Entity)MinecraftInstance.mc.field_71439_g, arrowBox, collidedEntities, null);
                    } while (z != chunkMaxZ);
                } while (x != chunkMaxX);
            }
            for (Entity possibleEntity : collidedEntities) {
                AxisAlignedBB possibleEntityBoundingBox;
                MovingObjectPosition movingObjectPosition;
                if (!possibleEntity.func_70067_L() || possibleEntity == MinecraftInstance.mc.field_71439_g || (movingObjectPosition = (possibleEntityBoundingBox = possibleEntity.func_174813_aQ().func_72314_b((double)size, (double)size, (double)size)).func_72327_a(posBefore, posAfter)) == null) continue;
                MovingObjectPosition possibleEntityLanding = movingObjectPosition;
                hitEntity = true;
                hasLanded = true;
                landingPosition = possibleEntityLanding;
            }
            if (MinecraftInstance.mc.field_71441_e.func_180495_p(new BlockPos(posX += motionX, posY += motionY, posZ += motionZ)).func_177230_c().func_149688_o() == Material.field_151586_h) {
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
            switch (landingPosition.field_178784_b.func_176740_k().ordinal()) {
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
        Cylinder cylinder = new Cylinder();
        cylinder.setDrawStyle(100011);
        cylinder.draw(0.2f, 0.0f, 0.0f, 60, 1);
        GL11.glPopMatrix();
        GL11.glDepthMask((boolean)true);
        RenderUtils.resetCaps();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    @Nullable
    public final Color interpolateHSB(@NotNull Color startColor, @NotNull Color endColor, float process2) {
        Intrinsics.checkNotNullParameter(startColor, "startColor");
        Intrinsics.checkNotNullParameter(endColor, "endColor");
        float[] startHSB = Color.RGBtoHSB(startColor.getRed(), startColor.getGreen(), startColor.getBlue(), null);
        float[] endHSB = Color.RGBtoHSB(endColor.getRed(), endColor.getGreen(), endColor.getBlue(), null);
        float brightness = (startHSB[2] + endHSB[2]) / (float)2;
        float saturation = (startHSB[1] + endHSB[1]) / (float)2;
        float hueMax = startHSB[0] > endHSB[0] ? startHSB[0] : endHSB[0];
        float hueMin = startHSB[0] > endHSB[0] ? endHSB[0] : startHSB[0];
        float hue = (hueMax - hueMin) * process2 + hueMin;
        return Color.getHSBColor(hue, saturation, brightness);
    }
}

