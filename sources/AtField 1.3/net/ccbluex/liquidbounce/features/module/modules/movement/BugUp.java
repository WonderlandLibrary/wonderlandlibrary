/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import java.awt.Color;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.renderer.entity.IRenderManager;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3i;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.misc.FallingPlayer;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="BugUp", description="Automatically setbacks you after falling a certain distance.", category=ModuleCategory.MOVEMENT)
public final class BugUp
extends Module {
    private double prevX;
    private final FloatValue maxDistanceWithoutGround;
    private final ListValue modeValue = new ListValue("Mode", new String[]{"TeleportBack", "FlyFlag", "OnGroundSpoof", "MotionTeleport-Flag"}, "FlyFlag");
    private float lastFound;
    private final IntegerValue maxFallDistance = new IntegerValue("MaxFallDistance", 10, 2, 255);
    private WBlockPos detectedLocation;
    private double prevZ;
    private double prevY;
    private final BoolValue indicator;

    public static final IMinecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    @Override
    public void onDisable() {
        this.prevX = 0.0;
        this.prevY = 0.0;
        this.prevZ = 0.0;
    }

    @EventTarget
    public final void onRender3D(Render3DEvent render3DEvent) {
        IEntityPlayerSP iEntityPlayerSP;
        Object object;
        block9: {
            block8: {
                object = BugUp.access$getMc$p$s1046033730().getThePlayer();
                if (object == null) {
                    return;
                }
                iEntityPlayerSP = object;
                if (this.detectedLocation == null || !((Boolean)this.indicator.get()).booleanValue()) break block8;
                double d = iEntityPlayerSP.getFallDistance();
                double d2 = iEntityPlayerSP.getPosY();
                WBlockPos wBlockPos = this.detectedLocation;
                if (wBlockPos == null) {
                    Intrinsics.throwNpe();
                }
                if (!(d + (d2 - (double)(wBlockPos.getY() + 1)) < 3.0)) break block9;
            }
            return;
        }
        object = this.detectedLocation;
        if (object == null) {
            Intrinsics.throwNpe();
        }
        int n = ((WVec3i)object).getX();
        WBlockPos wBlockPos = this.detectedLocation;
        if (wBlockPos == null) {
            Intrinsics.throwNpe();
        }
        int n2 = wBlockPos.getY();
        WBlockPos wBlockPos2 = this.detectedLocation;
        if (wBlockPos2 == null) {
            Intrinsics.throwNpe();
        }
        int n3 = wBlockPos2.getZ();
        IRenderManager iRenderManager = BugUp.access$getMc$p$s1046033730().getRenderManager();
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        RenderUtils.glColor(new Color(255, 0, 0, 90));
        RenderUtils.drawFilledBox(MinecraftInstance.classProvider.createAxisAlignedBB((double)n - iRenderManager.getRenderPosX(), (double)(n2 + 1) - iRenderManager.getRenderPosY(), (double)n3 - iRenderManager.getRenderPosZ(), (double)n - iRenderManager.getRenderPosX() + 1.0, (double)n2 + 1.2 - iRenderManager.getRenderPosY(), (double)n3 - iRenderManager.getRenderPosZ() + 1.0));
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        double d = (double)iEntityPlayerSP.getFallDistance() + (iEntityPlayerSP.getPosY() - ((double)n2 + 0.5));
        boolean bl = false;
        int n4 = (int)Math.floor(d);
        int n5 = 0;
        int n6 = n4 - 3;
        StringBuilder stringBuilder = new StringBuilder().append(n4).append("m (~");
        bl = false;
        int n7 = Math.max(n5, n6);
        RenderUtils.renderNameTag(stringBuilder.append(n7).append(" damage)").toString(), (double)n + 0.5, (double)n2 + 1.7, (double)n3 + 0.5);
        MinecraftInstance.classProvider.getGlStateManager().resetColor();
    }

    public BugUp() {
        this.maxDistanceWithoutGround = new FloatValue("MaxDistanceToSetback", 2.5f, 1.0f, 30.0f);
        this.indicator = new BoolValue("Indicator", true);
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        this.detectedLocation = null;
        IEntityPlayerSP iEntityPlayerSP = BugUp.access$getMc$p$s1046033730().getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (iEntityPlayerSP2.getOnGround() && !MinecraftInstance.classProvider.isBlockAir(BlockUtils.getBlock(new WBlockPos(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getPosY() - 1.0, iEntityPlayerSP2.getPosZ())))) {
            this.prevX = iEntityPlayerSP2.getPrevPosX();
            this.prevY = iEntityPlayerSP2.getPrevPosY();
            this.prevZ = iEntityPlayerSP2.getPrevPosZ();
        }
        if (!(iEntityPlayerSP2.getOnGround() || iEntityPlayerSP2.isOnLadder() || iEntityPlayerSP2.isInWater())) {
            boolean bl;
            Object object;
            FallingPlayer fallingPlayer = new FallingPlayer(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getPosY(), iEntityPlayerSP2.getPosZ(), iEntityPlayerSP2.getMotionX(), iEntityPlayerSP2.getMotionY(), iEntityPlayerSP2.getMotionZ(), iEntityPlayerSP2.getRotationYaw(), iEntityPlayerSP2.getMoveStrafing(), iEntityPlayerSP2.getMoveForward());
            FallingPlayer.CollisionResult collisionResult = fallingPlayer.findCollision(60);
            this.detectedLocation = collisionResult != null ? collisionResult.getPos() : null;
            WBlockPos wBlockPos = this.detectedLocation;
            if (this.detectedLocation != null) {
                double d = iEntityPlayerSP2.getPosY();
                object = this.detectedLocation;
                if (object == null) {
                    Intrinsics.throwNpe();
                }
                double d2 = d - (double)((WVec3i)object).getY();
                bl = false;
                if (Math.abs(d2) + (double)iEntityPlayerSP2.getFallDistance() <= ((Number)this.maxFallDistance.get()).doubleValue()) {
                    this.lastFound = iEntityPlayerSP2.getFallDistance();
                }
            }
            if (iEntityPlayerSP2.getFallDistance() - this.lastFound > ((Number)this.maxDistanceWithoutGround.get()).floatValue()) {
                String string;
                String string2;
                String string3 = string2 = (String)this.modeValue.get();
                bl = false;
                object = string3;
                if (object == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                switch (string = ((String)object).toLowerCase()) {
                    case "teleportback": {
                        iEntityPlayerSP2.setPositionAndUpdate(this.prevX, this.prevY, this.prevZ);
                        iEntityPlayerSP2.setFallDistance(0.0f);
                        iEntityPlayerSP2.setMotionY(0.0);
                        break;
                    }
                    case "flyflag": {
                        IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
                        iEntityPlayerSP3.setMotionY(iEntityPlayerSP3.getMotionY() + 0.1);
                        iEntityPlayerSP2.setFallDistance(0.0f);
                        break;
                    }
                    case "ongroundspoof": {
                        BugUp.access$getMc$p$s1046033730().getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(true));
                        break;
                    }
                    case "motionteleport-flag": {
                        iEntityPlayerSP2.setPositionAndUpdate(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getPosY() + 1.0, iEntityPlayerSP2.getPosZ());
                        BugUp.access$getMc$p$s1046033730().getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getPosY(), iEntityPlayerSP2.getPosZ(), true));
                        iEntityPlayerSP2.setMotionY(0.1);
                        MovementUtils.INSTANCE.strafe(0.0f, 1, null);
                        iEntityPlayerSP2.setFallDistance(0.0f);
                    }
                }
            }
        }
    }
}

