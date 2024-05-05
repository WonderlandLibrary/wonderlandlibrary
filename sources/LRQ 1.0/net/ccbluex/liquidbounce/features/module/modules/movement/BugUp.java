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
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.renderer.entity.IRenderManager;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
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
    private final ListValue modeValue = new ListValue("Mode", new String[]{"TeleportBack", "FlyFlag", "OnGroundSpoof", "MotionTeleport-Flag"}, "FlyFlag");
    private final IntegerValue maxFallDistance = new IntegerValue("MaxFallDistance", 10, 2, 255);
    private final FloatValue maxDistanceWithoutGround = new FloatValue("MaxDistanceToSetback", 2.5f, 1.0f, 30.0f);
    private final BoolValue indicator = new BoolValue("Indicator", true);
    private WBlockPos detectedLocation;
    private float lastFound;
    private double prevX;
    private double prevY;
    private double prevZ;

    @Override
    public void onDisable() {
        this.prevX = 0.0;
        this.prevY = 0.0;
        this.prevZ = 0.0;
    }

    @EventTarget
    public final void onUpdate(UpdateEvent e) {
        this.detectedLocation = null;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (thePlayer.getOnGround() && !MinecraftInstance.classProvider.isBlockAir(BlockUtils.getBlock(new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() - 1.0, thePlayer.getPosZ())))) {
            this.prevX = thePlayer.getPrevPosX();
            this.prevY = thePlayer.getPrevPosY();
            this.prevZ = thePlayer.getPrevPosZ();
        }
        if (!(thePlayer.getOnGround() || thePlayer.isOnLadder() || thePlayer.isInWater())) {
            boolean bl;
            FallingPlayer fallingPlayer = new FallingPlayer(thePlayer.getPosX(), thePlayer.getPosY(), thePlayer.getPosZ(), thePlayer.getMotionX(), thePlayer.getMotionY(), thePlayer.getMotionZ(), thePlayer.getRotationYaw(), thePlayer.getMoveStrafing(), thePlayer.getMoveForward());
            FallingPlayer.CollisionResult collisionResult = fallingPlayer.findCollision(60);
            WBlockPos wBlockPos = this.detectedLocation = collisionResult != null ? collisionResult.getPos() : null;
            if (this.detectedLocation != null) {
                double d = thePlayer.getPosY();
                WBlockPos wBlockPos2 = this.detectedLocation;
                if (wBlockPos2 == null) {
                    Intrinsics.throwNpe();
                }
                double d2 = d - (double)wBlockPos2.getY();
                bl = false;
                if (Math.abs(d2) + (double)thePlayer.getFallDistance() <= ((Number)this.maxFallDistance.get()).doubleValue()) {
                    this.lastFound = thePlayer.getFallDistance();
                }
            }
            if (thePlayer.getFallDistance() - this.lastFound > ((Number)this.maxDistanceWithoutGround.get()).floatValue()) {
                String mode;
                String string = mode = (String)this.modeValue.get();
                bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                switch (string2.toLowerCase()) {
                    case "teleportback": {
                        thePlayer.setPositionAndUpdate(this.prevX, this.prevY, this.prevZ);
                        thePlayer.setFallDistance(0.0f);
                        thePlayer.setMotionY(0.0);
                        break;
                    }
                    case "flyflag": {
                        IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
                        iEntityPlayerSP2.setMotionY(iEntityPlayerSP2.getMotionY() + 0.1);
                        thePlayer.setFallDistance(0.0f);
                        break;
                    }
                    case "ongroundspoof": {
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(true));
                        break;
                    }
                    case "motionteleport-flag": {
                        thePlayer.setPositionAndUpdate(thePlayer.getPosX(), thePlayer.getPosY() + (double)1.0f, thePlayer.getPosZ());
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY(), thePlayer.getPosZ(), true));
                        thePlayer.setMotionY(0.1);
                        MovementUtils.strafe$default(0.0f, 1, null);
                        thePlayer.setFallDistance(0.0f);
                    }
                }
            }
        }
    }

    @EventTarget
    public final void onRender3D(Render3DEvent event) {
        IEntityPlayerSP thePlayer;
        block9: {
            block8: {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    return;
                }
                thePlayer = iEntityPlayerSP;
                if (this.detectedLocation == null || !((Boolean)this.indicator.get()).booleanValue()) break block8;
                double d = thePlayer.getFallDistance();
                double d2 = thePlayer.getPosY();
                WBlockPos wBlockPos = this.detectedLocation;
                if (wBlockPos == null) {
                    Intrinsics.throwNpe();
                }
                if (!(d + (d2 - (double)(wBlockPos.getY() + 1)) < (double)3)) break block9;
            }
            return;
        }
        WBlockPos wBlockPos = this.detectedLocation;
        if (wBlockPos == null) {
            Intrinsics.throwNpe();
        }
        int x = wBlockPos.getX();
        WBlockPos wBlockPos2 = this.detectedLocation;
        if (wBlockPos2 == null) {
            Intrinsics.throwNpe();
        }
        int y = wBlockPos2.getY();
        WBlockPos wBlockPos3 = this.detectedLocation;
        if (wBlockPos3 == null) {
            Intrinsics.throwNpe();
        }
        int z = wBlockPos3.getZ();
        IRenderManager renderManager = MinecraftInstance.mc.getRenderManager();
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        RenderUtils.glColor(new Color(255, 0, 0, 90));
        RenderUtils.drawFilledBox(MinecraftInstance.classProvider.createAxisAlignedBB((double)x - renderManager.getRenderPosX(), (double)(y + 1) - renderManager.getRenderPosY(), (double)z - renderManager.getRenderPosZ(), (double)x - renderManager.getRenderPosX() + 1.0, (double)y + 1.2 - renderManager.getRenderPosY(), (double)z - renderManager.getRenderPosZ() + 1.0));
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        double d = (double)thePlayer.getFallDistance() + (thePlayer.getPosY() - ((double)y + 0.5));
        boolean bl = false;
        int fallDist = (int)Math.floor(d);
        int n = 0;
        int n2 = fallDist - 3;
        StringBuilder stringBuilder = new StringBuilder().append(fallDist).append("m (~");
        bl = false;
        int n3 = Math.max(n, n2);
        RenderUtils.renderNameTag(stringBuilder.append(n3).append(" damage)").toString(), (double)x + 0.5, (double)y + 1.7, (double)z + 0.5);
        MinecraftInstance.classProvider.getGlStateManager().resetColor();
    }
}

