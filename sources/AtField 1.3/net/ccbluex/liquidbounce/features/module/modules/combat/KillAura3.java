/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.ranges.IntProgression
 *  kotlin.ranges.IntRange
 *  kotlin.ranges.RangesKt
 *  kotlin.text.StringsKt
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.EnumParticleTypes
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.Cylinder
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.awt.Color;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import kotlin.TypeCastException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntProgression;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.IExtractedFunctions;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.enums.WEnumHand;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity;
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.api.minecraft.world.IWorldSettings;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EntityMovementEvent;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.StrafeEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.Criticals;
import net.ccbluex.liquidbounce.features.module.modules.combat.NoFriends;
import net.ccbluex.liquidbounce.features.module.modules.player.Blink;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RaycastUtils;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.extensions.PlayerExtensionKt;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.render.Animation;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

@ModuleInfo(name="KillAura3", description="Skid by LiquidWing", category=ModuleCategory.COMBAT)
public final class KillAura3
extends Module {
    private final BoolValue interactAutoBlockValue;
    private final IntegerValue noInventoryDelayValue;
    private boolean blockingStatus;
    private final ListValue rotationStrafeValue;
    private boolean hitable;
    private final FloatValue maxTurnSpeed;
    private final BoolValue fakeSwingValue;
    private final FloatValue cooldownValue;
    private final BoolValue fakeSharpValue;
    private final IntegerValue minCPS;
    private final IntegerValue hurtTimeValue;
    private final BoolValue aacValue;
    private final BoolValue silentRotationValue;
    private final ListValue autoBlockValue;
    private final FloatValue rangeSprintReducementValue;
    private final ListValue markValue;
    private final FloatValue rangeValue;
    private final MSTimer attackTimer;
    private final IntegerValue circleRed;
    private final IntegerValue circleGreen;
    private long attackDelay;
    private int clicks;
    private final IntegerValue circleBlue;
    private final ListValue hiteffect;
    private final FloatValue minTurnSpeed;
    private final ListValue rotations;
    private IEntityLivingBase currentTarget;
    private final IntegerValue circleAlpha;
    private final BoolValue noInventoryAttackValue;
    private final BoolValue livingRaycastValue;
    private final List prevTargetEntities;
    private final BoolValue predictValue;
    private final BoolValue raycastValue;
    private final BoolValue swingValue;
    private final FloatValue failRateValue;
    private final IntegerValue switchDelayValue;
    private IEntityLivingBase target$1;
    private final IntegerValue maxCPS = new IntegerValue(this, "MaxCPS", 8, 1, 20){
        final KillAura3 this$0;

        static {
        }
        {
            this.this$0 = killAura3;
            super(string, n, n2, n3);
        }

        protected void onChanged(int n, int n2) {
            int n3 = ((Number)KillAura3.access$getMinCPS$p(this.this$0).get()).intValue();
            if (n3 > n2) {
                this.set((Object)n3);
            }
            KillAura3.access$setAttackDelay$p(this.this$0, TimeUtils.randomClickDelay(((Number)KillAura3.access$getMinCPS$p(this.this$0).get()).intValue(), ((Number)this.get()).intValue()));
        }

        public void onChanged(Object object, Object object2) {
            this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
        }
    };
    private final BoolValue keepSprintValue;
    private final BoolValue circletargetValue;
    private final ListValue priorityValue;
    private final IntegerValue blockRate;
    private final IntegerValue limitedMultiTargetsValue;
    private final BoolValue lightingSoundValue;
    private final FloatValue fovValue;
    private final IntegerValue circleAccuracy;
    public static Object target;
    private final FloatValue maxPredictSize;
    private final BoolValue outborderValue;
    private final MSTimer switchTimer;
    private final BoolValue stopSprintValue;
    private final BoolValue circleValue;
    private final BoolValue raycastIgnoredValue;
    private final FloatValue throughWallsRangeValue;
    private final BoolValue randomCenterValue;
    private final ListValue targetModeValue;
    private final FloatValue minPredictSize;
    private long containerOpen;
    public static final Companion Companion;

    public final boolean isBlockingChestAura() {
        return this.getState() && this.target$1 != null;
    }

    private final void startBlocking(IEntity iEntity, boolean bl) {
        Object object;
        if (((Number)this.blockRate.get()).intValue() <= 0 || new Random().nextInt(100) > ((Number)this.blockRate.get()).intValue()) {
            return;
        }
        if (bl) {
            IEntity iEntity2 = MinecraftInstance.mc.getRenderViewEntity();
            object = iEntity2 != null ? iEntity2.getPositionEyes(1.0f) : null;
            double d = iEntity.getCollisionBorderSize();
            IAxisAlignedBB iAxisAlignedBB = iEntity.getEntityBoundingBox().expand(d, d, d);
            Rotation rotation = RotationUtils.targetRotation;
            if (rotation == null) {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                float f = iEntityPlayerSP.getRotationYaw();
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                rotation = new Rotation(f, iEntityPlayerSP2.getRotationPitch());
            }
            Rotation rotation2 = rotation;
            float f = rotation2.component1();
            float f2 = rotation2.component2();
            float f3 = -f * ((float)Math.PI / 180) - (float)Math.PI;
            boolean bl2 = false;
            float f4 = (float)Math.cos(f3);
            float f5 = -f * ((float)Math.PI / 180) - (float)Math.PI;
            boolean bl3 = false;
            f3 = (float)Math.sin(f5);
            float f6 = -f2 * ((float)Math.PI / 180);
            boolean bl4 = false;
            f5 = -((float)Math.cos(f6));
            float f7 = -f2 * ((float)Math.PI / 180);
            boolean bl5 = false;
            f6 = (float)Math.sin(f7);
            double d2 = this.getMaxRange();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            double d3 = PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, iEntity);
            boolean bl6 = false;
            double d4 = Math.min(d2, d3) + 1.0;
            Object object2 = object;
            if (object2 == null) {
                Intrinsics.throwNpe();
            }
            Object object3 = object2;
            d3 = (double)(f3 * f5) * d4;
            double d5 = (double)f6 * d4;
            double d6 = (double)(f4 * f5) * d4;
            boolean bl7 = false;
            WVec3 wVec3 = new WVec3(((WVec3)object3).getXCoord() + d3, ((WVec3)object3).getYCoord() + d5, ((WVec3)object3).getZCoord() + d6);
            IMovingObjectPosition iMovingObjectPosition = iAxisAlignedBB.calculateIntercept((WVec3)object, wVec3);
            if (iMovingObjectPosition == null) {
                return;
            }
            object3 = iMovingObjectPosition;
            WVec3 wVec32 = object3.getHitVec();
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity(iEntity, new WVec3(wVec32.getXCoord() - iEntity.getPosX(), wVec32.getYCoord() - iEntity.getPosY(), wVec32.getZCoord() - iEntity.getPosZ())));
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity(iEntity, ICPacketUseEntity.WAction.INTERACT));
        }
        if (LiquidBounce.INSTANCE.getModuleManager().get(Animation.class).getState()) {
            IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            object = iEntityPlayerSP.getInventory().getCurrentItemInHand();
            WEnumHand wEnumHand = WEnumHand.OFF_HAND;
            IINetHandlerPlayClient iINetHandlerPlayClient2 = iINetHandlerPlayClient;
            boolean bl8 = false;
            IPacket iPacket = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(wEnumHand);
            iINetHandlerPlayClient2.addToSendQueue(iPacket);
        } else {
            IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            object = iEntityPlayerSP.getInventory().getCurrentItemInHand();
            WEnumHand wEnumHand = WEnumHand.MAIN_HAND;
            IINetHandlerPlayClient iINetHandlerPlayClient3 = iINetHandlerPlayClient;
            boolean bl9 = false;
            IPacket iPacket = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(wEnumHand);
            iINetHandlerPlayClient3.addToSendQueue(iPacket);
            IINetHandlerPlayClient iINetHandlerPlayClient4 = MinecraftInstance.mc.getNetHandler();
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            object = iEntityPlayerSP3.getInventory().getCurrentItemInHand();
            wEnumHand = WEnumHand.OFF_HAND;
            iINetHandlerPlayClient3 = iINetHandlerPlayClient4;
            bl9 = false;
            iPacket = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(wEnumHand);
            iINetHandlerPlayClient3.addToSendQueue(iPacket);
        }
        this.blockingStatus = true;
    }

    public static final long access$getAttackDelay$p(KillAura3 killAura3) {
        return killAura3.attackDelay;
    }

    private final float getMaxRange() {
        float f = ((Number)this.rangeValue.get()).floatValue();
        float f2 = ((Number)this.throughWallsRangeValue.get()).floatValue();
        boolean bl = false;
        return Math.max(f, f2);
    }

    public static final IntegerValue access$getMinCPS$p(KillAura3 killAura3) {
        return killAura3.minCPS;
    }

    public final BoolValue getKeepSprintValue() {
        return this.keepSprintValue;
    }

    public static final IntegerValue access$getMaxCPS$p(KillAura3 killAura3) {
        return killAura3.maxCPS;
    }

    public static final void access$setAttackDelay$p(KillAura3 killAura3, long l) {
        killAura3.attackDelay = l;
    }

    public final FloatValue getRangeValue() {
        return this.rangeValue;
    }

    public KillAura3() {
        List list;
        this.minCPS = new IntegerValue(this, "MinCPS", 5, 1, 20){
            final KillAura3 this$0;

            static {
            }

            protected void onChanged(int n, int n2) {
                int n3 = ((Number)KillAura3.access$getMaxCPS$p(this.this$0).get()).intValue();
                if (n3 < n2) {
                    this.set((Object)n3);
                }
                KillAura3.access$setAttackDelay$p(this.this$0, TimeUtils.randomClickDelay(((Number)this.get()).intValue(), ((Number)KillAura3.access$getMaxCPS$p(this.this$0).get()).intValue()));
            }

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
            }
            {
                this.this$0 = killAura3;
                super(string, n, n2, n3);
            }
        };
        this.hurtTimeValue = new IntegerValue("HurtTime", 10, 0, 10);
        this.cooldownValue = new FloatValue("Cooldown", 1.0f, 0.0f, 1.0f);
        this.switchDelayValue = new IntegerValue("SwitchDelay", 700, 0, 2000);
        this.rangeValue = new FloatValue("Range", 3.7f, 1.0f, 8.0f);
        this.throughWallsRangeValue = new FloatValue("ThroughWallsRange", 3.0f, 0.0f, 8.0f);
        this.rangeSprintReducementValue = new FloatValue("RangeSprintReducement", 0.0f, 0.0f, 0.4f);
        this.priorityValue = new ListValue("Priority", new String[]{"Health", "Distance", "Direction", "LivingTime"}, "Distance");
        this.targetModeValue = new ListValue("TargetMode", new String[]{"Single", "Switch", "Multi"}, "Switch");
        this.swingValue = new BoolValue("Swing", true);
        this.keepSprintValue = new BoolValue("KeepSprint", true);
        this.stopSprintValue = new BoolValue("StopSprintOnAir", true);
        this.autoBlockValue = new ListValue("AutoBlock", new String[]{"Off", "Packet", "AfterTick", "Right", "AACBy1.12.2", "Old1.8.9", "Hyt1.12.2"}, "Packet");
        this.interactAutoBlockValue = new BoolValue("InteractAutoBlock", true);
        this.blockRate = new IntegerValue("BlockRate", 100, 1, 100);
        this.raycastValue = new BoolValue("RayCast", true);
        this.raycastIgnoredValue = new BoolValue("RayCastIgnored", false);
        this.livingRaycastValue = new BoolValue("LivingRayCast", true);
        this.aacValue = new BoolValue("AAC", false);
        this.maxTurnSpeed = new FloatValue(this, "MaxTurnSpeed", 180.0f, 0.0f, 180.0f){
            final KillAura3 this$0;

            static {
            }

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).floatValue(), ((Number)object2).floatValue());
            }

            protected void onChanged(float f, float f2) {
                float f3 = ((Number)KillAura3.access$getMinTurnSpeed$p(this.this$0).get()).floatValue();
                if (f3 > f2) {
                    this.set((Object)Float.valueOf(f3));
                }
            }
            {
                this.this$0 = killAura3;
                super(string, f, f2, f3);
            }
        };
        this.minTurnSpeed = new FloatValue(this, "MinTurnSpeed", 180.0f, 0.0f, 180.0f){
            final KillAura3 this$0;

            static {
            }

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).floatValue(), ((Number)object2).floatValue());
            }

            protected void onChanged(float f, float f2) {
                float f3 = ((Number)KillAura3.access$getMaxTurnSpeed$p(this.this$0).get()).floatValue();
                if (f3 < f2) {
                    this.set((Object)Float.valueOf(f3));
                }
            }
            {
                this.this$0 = killAura3;
                super(string, f, f2, f3);
            }
        };
        this.rotations = new ListValue("RotationMode", new String[]{"Vanilla", "Other", "BackTrack", "LiquidSensePlus", "Test", "HytRotation"}, "BackTrack");
        this.silentRotationValue = new BoolValue("SilentRotation", true);
        this.rotationStrafeValue = new ListValue("Strafe", new String[]{"Off", "Strict", "Silent"}, "Off");
        this.randomCenterValue = new BoolValue("RandomCenter", true);
        this.outborderValue = new BoolValue("Outborder", false);
        this.fovValue = new FloatValue("FOV", 180.0f, 0.0f, 180.0f);
        this.predictValue = new BoolValue("Predict", true);
        this.maxPredictSize = new FloatValue(this, "MaxPredictSize", 1.0f, 0.1f, 5.0f){
            final KillAura3 this$0;
            {
                this.this$0 = killAura3;
                super(string, f, f2, f3);
            }

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).floatValue(), ((Number)object2).floatValue());
            }

            protected void onChanged(float f, float f2) {
                float f3 = ((Number)KillAura3.access$getMinPredictSize$p(this.this$0).get()).floatValue();
                if (f3 > f2) {
                    this.set((Object)Float.valueOf(f3));
                }
            }

            static {
            }
        };
        this.minPredictSize = new FloatValue(this, "MinPredictSize", 1.0f, 0.1f, 5.0f){
            final KillAura3 this$0;
            {
                this.this$0 = killAura3;
                super(string, f, f2, f3);
            }

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).floatValue(), ((Number)object2).floatValue());
            }

            protected void onChanged(float f, float f2) {
                float f3 = ((Number)KillAura3.access$getMaxPredictSize$p(this.this$0).get()).floatValue();
                if (f3 < f2) {
                    this.set((Object)Float.valueOf(f3));
                }
            }

            static {
            }
        };
        this.failRateValue = new FloatValue("FailRate", 0.0f, 0.0f, 100.0f);
        this.fakeSwingValue = new BoolValue("FakeSwing", true);
        this.noInventoryAttackValue = new BoolValue("NoInvAttack", false);
        this.noInventoryDelayValue = new IntegerValue("NoInvDelay", 200, 0, 500);
        this.limitedMultiTargetsValue = new IntegerValue("LimitedMultiTargets", 0, 0, 50);
        this.markValue = new ListValue("Mark", new String[]{"Liquid", "Box", "Rise", "FDP", "Block", "Jello", "Plat", "Red", "Sims", "None"}, "Rise");
        this.hiteffect = new ListValue("HitEffect", new String[]{"Lightningbolt", "Criticals", "Blood", "Fire", "Water", "Smoke", "Flame", "Heart", "None"}, "None");
        this.lightingSoundValue = new BoolValue("LightingSound", true);
        this.fakeSharpValue = new BoolValue("FakeSharp", true);
        this.circletargetValue = new BoolValue("CircleTarget", true);
        this.circleValue = new BoolValue("Circle", true);
        this.circleRed = new IntegerValue("CircleRed", 255, 0, 255);
        this.circleGreen = new IntegerValue("CircleGreen", 255, 0, 255);
        this.circleBlue = new IntegerValue("CircleBlue", 255, 0, 255);
        this.circleAlpha = new IntegerValue("CircleAlpha", 255, 0, 255);
        this.circleAccuracy = new IntegerValue("CircleAccuracy", 15, 0, 60);
        KillAura3 killAura3 = this;
        boolean bl = false;
        killAura3.prevTargetEntities = list = (List)new ArrayList();
        this.attackTimer = new MSTimer();
        this.switchTimer = new MSTimer();
        this.containerOpen = -1L;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final boolean getCanBlock() {
        boolean bl = false;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getHeldItem() == null) return false;
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        IItemStack iItemStack = iEntityPlayerSP2.getHeldItem();
        if (iItemStack == null) {
            Intrinsics.throwNpe();
        }
        if (!MinecraftInstance.classProvider.isItemSword(iItemStack.getItem())) return false;
        return true;
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onUpdate(UpdateEvent var1_1) {
        var2_2 = this;
        var3_4 = false;
        v0 = MinecraftInstance.mc.getThePlayer();
        if (v0 == null) {
            Intrinsics.throwNpe();
        }
        if (v0.isSpectator()) ** GOTO lbl-1000
        v1 = MinecraftInstance.mc.getThePlayer();
        if (v1 == null) {
            Intrinsics.throwNpe();
        }
        if (!KillAura3.access$isAlive(var2_2, v1) || LiquidBounce.INSTANCE.getModuleManager().get(Blink.class).getState()) lbl-1000:
        // 2 sources

        {
            v2 = true;
        } else {
            v2 = false;
        }
        if (v2) {
            this.target$1 = null;
            this.currentTarget = null;
            this.hitable = false;
            this.stopBlocking();
            return;
        }
        if (((Boolean)this.noInventoryAttackValue.get()).booleanValue() && (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen()) || System.currentTimeMillis() - this.containerOpen < ((Number)this.noInventoryDelayValue.get()).longValue())) {
            this.target$1 = null;
            this.currentTarget = null;
            this.hitable = false;
            if (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen())) {
                this.containerOpen = System.currentTimeMillis();
            }
            return;
        }
        if (this.target$1 != null && this.currentTarget != null) {
            v3 = MinecraftInstance.mc.getThePlayer();
            if (v3 == null) {
                Intrinsics.throwNpe();
            }
            if (v3.getCooledAttackStrength(0.0f) >= ((Number)this.cooldownValue.get()).floatValue()) {
                while (this.clicks > 0) {
                    this.runAttack();
                    var2_3 = this.clicks;
                    this.clicks = var2_3 + -1;
                }
            }
        }
    }

    public static final BoolValue access$getAacValue$p(KillAura3 killAura3) {
        return killAura3.aacValue;
    }

    /*
     * Exception decompiling
     */
    public final void update() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl83 : RETURN - null : trying to set 0 previously set to 1
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:207)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public static final FloatValue access$getMinPredictSize$p(KillAura3 killAura3) {
        return killAura3.minPredictSize;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onStrafe(StrafeEvent strafeEvent) {
        if (StringsKt.equals((String)((String)this.rotationStrafeValue.get()), (String)"Off", (boolean)true)) {
            return;
        }
        this.update();
        if (this.currentTarget == null || RotationUtils.targetRotation == null) return;
        String string = (String)this.rotationStrafeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        string = string2.toLowerCase();
        switch (string.hashCode()) {
            case -902327211: {
                if (!string.equals("silent")) return;
                break;
            }
            case -891986231: {
                if (!string.equals("strict")) return;
                Rotation rotation = RotationUtils.targetRotation;
                if (rotation == null) {
                    return;
                }
                Rotation rotation2 = rotation;
                float f = rotation2.component1();
                float f2 = strafeEvent.getStrafe();
                float f3 = strafeEvent.getForward();
                float f4 = strafeEvent.getFriction();
                float f5 = f2 * f2 + f3 * f3;
                if (f5 >= 1.0E-4f) {
                    IEntityPlayerSP iEntityPlayerSP;
                    boolean bl2 = false;
                    if ((f5 = (float)Math.sqrt(f5)) < 1.0f) {
                        f5 = 1.0f;
                    }
                    f5 = f4 / f5;
                    f2 *= f5;
                    f3 *= f5;
                    float f6 = (float)((double)f * Math.PI / (double)180.0f);
                    boolean bl3 = false;
                    float f7 = (float)Math.sin(f6);
                    float f8 = (float)((double)f * Math.PI / (double)180.0f);
                    boolean bl4 = false;
                    f6 = (float)Math.cos(f8);
                    IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP2 == null) {
                        Intrinsics.throwNpe();
                    }
                    IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP = iEntityPlayerSP2;
                    iEntityPlayerSP3.setMotionX(iEntityPlayerSP3.getMotionX() + (double)(f2 * f6 - f3 * f7));
                    IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP;
                    iEntityPlayerSP4.setMotionZ(iEntityPlayerSP4.getMotionZ() + (double)(f3 * f6 + f2 * f7));
                }
                strafeEvent.cancelEvent();
                return;
            }
        }
        this.update();
        RotationUtils.targetRotation.applyStrafeToPlayer(strafeEvent);
        strafeEvent.cancelEvent();
        return;
    }

    @Override
    public void onDisable() {
        this.target$1 = null;
        this.currentTarget = null;
        this.hitable = false;
        this.prevTargetEntities.clear();
        this.attackTimer.reset();
        this.clicks = 0;
        this.stopBlocking();
    }

    static {
        Companion = new Companion(null);
    }

    public static final Object access$getTarget$cp() {
        return target;
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onRender3D(Render3DEvent var1_1) {
        var2_2 = this;
        var3_7 = 0;
        v0 = MinecraftInstance.mc.getThePlayer();
        if (v0 == null) {
            Intrinsics.throwNpe();
        }
        if (v0.isSpectator()) ** GOTO lbl-1000
        v1 = MinecraftInstance.mc.getThePlayer();
        if (v1 == null) {
            Intrinsics.throwNpe();
        }
        if (!KillAura3.access$isAlive((KillAura3)var2_2, v1) || LiquidBounce.INSTANCE.getModuleManager().get(Blink.class).getState()) lbl-1000:
        // 2 sources

        {
            v2 = true;
        } else {
            v2 = false;
        }
        if (v2) {
            this.target$1 = null;
            this.currentTarget = null;
            this.hitable = false;
            this.stopBlocking();
            return;
        }
        if (((Boolean)this.circletargetValue.get()).booleanValue() && LiquidBounce.INSTANCE.getCombatManager().getTarget() != null) {
            GL11.glPushMatrix();
            v3 = LiquidBounce.INSTANCE.getCombatManager().getTarget();
            if (v3 == null) {
                Intrinsics.throwNpe();
            }
            v4 = v3.getLastTickPosX();
            v5 = LiquidBounce.INSTANCE.getCombatManager().getTarget();
            if (v5 == null) {
                Intrinsics.throwNpe();
            }
            v6 = v5.getPosX();
            v7 = LiquidBounce.INSTANCE.getCombatManager().getTarget();
            if (v7 == null) {
                Intrinsics.throwNpe();
            }
            v8 = v4 + (v6 - v7.getLastTickPosX()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks() - MinecraftInstance.mc.getRenderManager().getRenderPosX();
            v9 = LiquidBounce.INSTANCE.getCombatManager().getTarget();
            if (v9 == null) {
                Intrinsics.throwNpe();
            }
            v10 = v9.getLastTickPosY();
            v11 = LiquidBounce.INSTANCE.getCombatManager().getTarget();
            if (v11 == null) {
                Intrinsics.throwNpe();
            }
            v12 = v11.getPosY();
            v13 = LiquidBounce.INSTANCE.getCombatManager().getTarget();
            if (v13 == null) {
                Intrinsics.throwNpe();
            }
            v14 = v10 + (v12 - v13.getLastTickPosY()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks() - MinecraftInstance.mc.getRenderManager().getRenderPosY();
            v15 = LiquidBounce.INSTANCE.getCombatManager().getTarget();
            if (v15 == null) {
                Intrinsics.throwNpe();
            }
            v16 = v15.getLastTickPosZ();
            v17 = LiquidBounce.INSTANCE.getCombatManager().getTarget();
            if (v17 == null) {
                Intrinsics.throwNpe();
            }
            v18 = v17.getPosZ();
            v19 = LiquidBounce.INSTANCE.getCombatManager().getTarget();
            if (v19 == null) {
                Intrinsics.throwNpe();
            }
            GL11.glTranslated((double)v8, (double)v14, (double)(v16 + (v18 - v19.getLastTickPosZ()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks() - MinecraftInstance.mc.getRenderManager().getRenderPosZ()));
            GL11.glEnable((int)3042);
            GL11.glEnable((int)2848);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2929);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glLineWidth((float)1.0f);
            GL11.glColor4f((float)((float)((Number)this.circleRed.get()).intValue() / 255.0f), (float)((float)((Number)this.circleGreen.get()).intValue() / 255.0f), (float)((float)((Number)this.circleBlue.get()).intValue() / 255.0f), (float)((float)((Number)this.circleAlpha.get()).intValue() / 255.0f));
            GL11.glRotatef((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glBegin((int)3);
            var5_11 = 0;
            v20 = RangesKt.step((IntProgression)((IntProgression)new IntRange(var5_11, 360)), (int)(61 - ((Number)this.circleAccuracy.get()).intValue()));
            var2_3 = v20.getFirst();
            var3_7 = v20.getLast();
            var4_16 = v20.getStep();
            v21 = var2_3;
            v22 = var3_7;
            if (var4_16 >= 0 ? v21 <= v22 : v21 >= v22) {
                while (true) {
                    var5_12 = (double)var2_3 * 3.141592653589793 / 180.0;
                    var7_23 = 0;
                    v23 = (float)Math.cos(var5_12) * (float)2;
                    var5_12 = (double)var2_3 * 3.141592653589793 / 180.0;
                    var32_19 = v23;
                    var7_23 = 0;
                    var33_22 = Math.sin(var5_12);
                    GL11.glVertex2f((float)var32_19, (float)((float)var33_22 * (float)2));
                    if (var2_3 == var3_7) break;
                    var2_3 += var4_16;
                }
            }
            var2_4 = 6.283185307179586;
            var4_16 = 0;
            v24 = (float)Math.cos(var2_4) * (float)2;
            var2_4 = 6.283185307179586;
            var32_19 = v24;
            var4_16 = 0;
            var33_22 = Math.sin(var2_4);
            GL11.glVertex2f((float)var32_19, (float)((float)var33_22 * (float)2));
            GL11.glEnd();
            GL11.glDisable((int)3042);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glDisable((int)2848);
            GL11.glPopMatrix();
        }
        if (((Boolean)this.circleValue.get()).booleanValue()) {
            GL11.glPushMatrix();
            v25 = MinecraftInstance.mc.getThePlayer();
            if (v25 == null) {
                Intrinsics.throwNpe();
            }
            v26 = v25.getLastTickPosX();
            v27 = MinecraftInstance.mc.getThePlayer();
            if (v27 == null) {
                Intrinsics.throwNpe();
            }
            v28 = v27.getPosX();
            v29 = MinecraftInstance.mc.getThePlayer();
            if (v29 == null) {
                Intrinsics.throwNpe();
            }
            v30 = v26 + (v28 - v29.getLastTickPosX()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks() - MinecraftInstance.mc.getRenderManager().getRenderPosX();
            v31 = MinecraftInstance.mc.getThePlayer();
            if (v31 == null) {
                Intrinsics.throwNpe();
            }
            v32 = v31.getLastTickPosY();
            v33 = MinecraftInstance.mc.getThePlayer();
            if (v33 == null) {
                Intrinsics.throwNpe();
            }
            v34 = v33.getPosY();
            v35 = MinecraftInstance.mc.getThePlayer();
            if (v35 == null) {
                Intrinsics.throwNpe();
            }
            v36 = v32 + (v34 - v35.getLastTickPosY()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks() - MinecraftInstance.mc.getRenderManager().getRenderPosY();
            v37 = MinecraftInstance.mc.getThePlayer();
            if (v37 == null) {
                Intrinsics.throwNpe();
            }
            v38 = v37.getLastTickPosZ();
            v39 = MinecraftInstance.mc.getThePlayer();
            if (v39 == null) {
                Intrinsics.throwNpe();
            }
            v40 = v39.getPosZ();
            v41 = MinecraftInstance.mc.getThePlayer();
            if (v41 == null) {
                Intrinsics.throwNpe();
            }
            GL11.glTranslated((double)v30, (double)v36, (double)(v38 + (v40 - v41.getLastTickPosZ()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks() - MinecraftInstance.mc.getRenderManager().getRenderPosZ()));
            GL11.glEnable((int)3042);
            GL11.glEnable((int)2848);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2929);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glLineWidth((float)1.0f);
            GL11.glColor4f((float)((float)((Number)this.circleRed.get()).intValue() / 255.0f), (float)((float)((Number)this.circleGreen.get()).intValue() / 255.0f), (float)((float)((Number)this.circleBlue.get()).intValue() / 255.0f), (float)((float)((Number)this.circleAlpha.get()).intValue() / 255.0f));
            GL11.glRotatef((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glBegin((int)3);
            var5_11 = 0;
            v42 = RangesKt.step((IntProgression)((IntProgression)new IntRange(var5_11, 360)), (int)(61 - ((Number)this.circleAccuracy.get()).intValue()));
            var2_5 = v42.getFirst();
            var3_7 = v42.getLast();
            var4_16 = v42.getStep();
            v43 = var2_5;
            v44 = var3_7;
            if (var4_16 >= 0 ? v43 <= v44 : v43 >= v44) {
                while (true) {
                    var5_13 = (double)var2_5 * 3.141592653589793 / 180.0;
                    var7_23 = 0;
                    v45 = (float)Math.cos(var5_13) * ((Number)this.rangeValue.get()).floatValue();
                    var5_13 = (double)var2_5 * 3.141592653589793 / 180.0;
                    var32_19 = v45;
                    var7_23 = 0;
                    var33_22 = Math.sin(var5_13);
                    GL11.glVertex2f((float)var32_19, (float)((float)var33_22 * ((Number)this.rangeValue.get()).floatValue()));
                    if (var2_5 == var3_7) break;
                    var2_5 += var4_16;
                }
            }
            var2_6 = 6.283185307179586;
            var4_16 = 0;
            v46 = (float)Math.cos(var2_6) * ((Number)this.rangeValue.get()).floatValue();
            var2_6 = 6.283185307179586;
            var32_19 = v46;
            var4_16 = 0;
            var33_22 = Math.sin(var2_6);
            GL11.glVertex2f((float)var32_19, (float)((float)var33_22 * ((Number)this.rangeValue.get()).floatValue()));
            GL11.glEnd();
            GL11.glDisable((int)3042);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glDisable((int)2848);
            GL11.glPopMatrix();
        }
        if (((Boolean)this.noInventoryAttackValue.get()).booleanValue() && (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen()) || System.currentTimeMillis() - this.containerOpen < ((Number)this.noInventoryDelayValue.get()).longValue())) {
            this.target$1 = null;
            this.currentTarget = null;
            this.hitable = false;
            if (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen())) {
                this.containerOpen = System.currentTimeMillis();
            }
            return;
        }
        if (this.target$1 == null) {
            return;
        }
        var2_2 = (String)this.markValue.get();
        var3_7 = 0;
        v47 = var2_2;
        if (v47 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        if (v47.toLowerCase().equals("box") && !StringsKt.equals((String)((String)this.targetModeValue.get()), (String)"Multi", (boolean)true)) {
            v48 = this.target$1;
            v49 = this.target$1;
            if (v49 == null) {
                Intrinsics.throwNpe();
            }
            RenderUtils.drawPlatform(v48, v49.getHurtTime() > 3 ? new Color(255, 55, 55, 70) : new Color(255, 0, 0, 70));
        }
        v50 = LiquidBounce.INSTANCE.getModuleManager().getModule(HUD.class);
        if (v50 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.HUD");
        }
        var2_2 = (HUD)v50;
        var3_8 = (String)this.markValue.get();
        var4_16 = 0;
        v51 = var3_8;
        if (v51 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        if (v51.toLowerCase().equals("rise") && StringsKt.equals((String)((String)this.targetModeValue.get()), (String)"Switch", (boolean)true)) {
            RenderUtils.drawCircle(this.target$1, 0.67, new Color(159, 24, 242).getRGB(), true);
        }
        if (this.currentTarget != null && this.attackTimer.hasTimePassed(this.attackDelay)) {
            v52 = this.currentTarget;
            if (v52 == null) {
                Intrinsics.throwNpe();
            }
            if (v52.getHurtTime() <= ((Number)this.hurtTimeValue.get()).intValue()) {
                var3_9 = this.clicks;
                this.clicks = var3_9 + 1;
                this.attackTimer.reset();
                this.attackDelay = TimeUtils.randomClickDelay(((Number)this.minCPS.get()).intValue(), ((Number)this.maxCPS.get()).intValue());
            }
        }
        if (((Boolean)this.noInventoryAttackValue.get()).booleanValue() && (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen()) || System.currentTimeMillis() - this.containerOpen < ((Number)this.noInventoryDelayValue.get()).longValue())) {
            this.target$1 = null;
            this.currentTarget = null;
            this.hitable = false;
            if (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen())) {
                this.containerOpen = System.currentTimeMillis();
            }
            return;
        }
        if (this.target$1 == null) {
            return;
        }
        v53 = MinecraftInstance.mc.getTheWorld();
        if (v53 == null) {
            Intrinsics.throwNpe();
        }
        v54 = v53;
        v55 = this.target$1;
        if (v55 == null) {
            Intrinsics.throwNpe();
        }
        v56 = v55.getPosX();
        v57 = this.target$1;
        if (v57 == null) {
            Intrinsics.throwNpe();
        }
        v58 = v57.getPosY();
        v59 = this.target$1;
        if (v59 == null) {
            Intrinsics.throwNpe();
        }
        var3_10 = MinecraftInstance.classProvider.createEntityLightningBolt(v54, v56, v58, v59.getPosZ(), false);
        var4_17 = (String)this.hiteffect.get();
        var5_11 = 0;
        v60 = var4_17;
        if (v60 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        var4_17 = v60.toLowerCase();
        tmp = -1;
        switch (var4_17.hashCode()) {
            case 851318879: {
                if (!var4_17.equals("lightningbolt")) break;
                tmp = 1;
                break;
            }
            case 387153076: {
                if (!var4_17.equals("criticals")) break;
                tmp = 2;
                break;
            }
            case 109562223: {
                if (!var4_17.equals("smoke")) break;
                tmp = 3;
                break;
            }
            case 3143222: {
                if (!var4_17.equals("fire")) break;
                tmp = 4;
                break;
            }
            case 97513267: {
                if (!var4_17.equals("flame")) break;
                tmp = 5;
                break;
            }
            case 112903447: {
                if (!var4_17.equals("water")) break;
                tmp = 6;
                break;
            }
            case 93832698: {
                if (!var4_17.equals("blood")) break;
                tmp = 7;
                break;
            }
            case 99151942: {
                if (!var4_17.equals("heart")) break;
                tmp = 8;
                break;
            }
        }
        switch (tmp) {
            case 1: {
                v61 = MinecraftInstance.mc.getTheWorld();
                if (v61 == null) {
                    Intrinsics.throwNpe();
                }
                v61.addEntityToWorld(-1, var3_10);
                if (!((Boolean)this.lightingSoundValue.get()).booleanValue()) break;
                MinecraftInstance.mc.getSoundHandler().playSound("entity.lightning.impact", 1.0f);
                MinecraftInstance.mc.getSoundHandler().playSound("entity.lightning.thunder", 1.0f);
                break;
            }
            case 5: {
                v62 = MinecraftInstance.mc.getEffectRenderer();
                v63 = this.target$1;
                if (v63 == null) {
                    Intrinsics.throwNpe();
                }
                v62.emitParticleAtEntity(v63, EnumParticleTypes.FLAME);
                break;
            }
            case 3: {
                v64 = MinecraftInstance.mc.getEffectRenderer();
                v65 = this.target$1;
                if (v65 == null) {
                    Intrinsics.throwNpe();
                }
                v64.emitParticleAtEntity(v65, EnumParticleTypes.SMOKE_LARGE);
                break;
            }
            case 8: {
                v66 = MinecraftInstance.mc.getEffectRenderer();
                v67 = this.target$1;
                if (v67 == null) {
                    Intrinsics.throwNpe();
                }
                v66.emitParticleAtEntity(v67, EnumParticleTypes.HEART);
                break;
            }
            case 4: {
                v68 = MinecraftInstance.mc.getEffectRenderer();
                v69 = this.target$1;
                if (v69 == null) {
                    Intrinsics.throwNpe();
                }
                v68.emitParticleAtEntity(v69, EnumParticleTypes.LAVA);
                break;
            }
            case 6: {
                v70 = MinecraftInstance.mc.getEffectRenderer();
                v71 = this.target$1;
                if (v71 == null) {
                    Intrinsics.throwNpe();
                }
                v70.emitParticleAtEntity(v71, EnumParticleTypes.WATER_DROP);
                break;
            }
            case 2: {
                v72 = MinecraftInstance.mc.getEffectRenderer();
                v73 = this.target$1;
                if (v73 == null) {
                    Intrinsics.throwNpe();
                }
                v72.emitParticleAtEntity(v73, EnumParticleTypes.CRIT);
                break;
            }
            case 7: {
                var5_11 = 10;
                var6_27 = 0;
                var7_23 = 0;
                var8_28 = var5_11;
                for (var7_23 = 0; var7_23 < var8_28; ++var7_23) {
                    var9_29 = var7_23;
                    var10_32 = false;
                    v74 = MinecraftInstance.mc.getEffectRenderer();
                    v75 = EnumParticleTypes.BLOCK_CRACK.func_179348_c();
                    v76 = this.target$1;
                    if (v76 == null) {
                        Intrinsics.throwNpe();
                    }
                    v77 = v76.getPosX();
                    v78 = this.target$1;
                    if (v78 == null) {
                        Intrinsics.throwNpe();
                    }
                    v79 = v78.getPosY();
                    v80 = this.target$1;
                    if (v80 == null) {
                        Intrinsics.throwNpe();
                    }
                    v81 = v79 + (double)(v80.getHeight() / (float)2);
                    v82 = this.target$1;
                    if (v82 == null) {
                        Intrinsics.throwNpe();
                    }
                    v83 = v82.getPosZ();
                    v84 = this.target$1;
                    if (v84 == null) {
                        Intrinsics.throwNpe();
                    }
                    v85 = v84.getMotionX() + (double)RandomUtils.INSTANCE.nextFloat(-0.5f, 0.5f);
                    v86 = this.target$1;
                    if (v86 == null) {
                        Intrinsics.throwNpe();
                    }
                    v87 = v86.getMotionY() + (double)RandomUtils.INSTANCE.nextFloat(-0.5f, 0.5f);
                    v88 = this.target$1;
                    if (v88 == null) {
                        Intrinsics.throwNpe();
                    }
                    v74.spawnEffectParticle(v75, v77, v81, v83, v85, v87, v88.getMotionZ() + (double)RandomUtils.INSTANCE.nextFloat(-0.5f, 0.5f), Block.func_176210_f((IBlockState)Blocks.field_150451_bX.func_176223_P()));
                }
                break;
            }
        }
        var4_17 = (String)this.markValue.get();
        var5_11 = 0;
        v89 = var4_17;
        if (v89 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        var4_17 = v89.toLowerCase();
        tmp = -1;
        switch (var4_17.hashCode()) {
            case 112785: {
                if (!var4_17.equals("red")) break;
                tmp = 1;
                break;
            }
            case 101009364: {
                if (!var4_17.equals("jello")) break;
                tmp = 2;
                break;
            }
            case 101234: {
                if (!var4_17.equals("fdp")) break;
                tmp = 3;
                break;
            }
            case -1102567108: {
                if (!var4_17.equals("liquid")) break;
                tmp = 4;
                break;
            }
            case 3530364: {
                if (!var4_17.equals("sims")) break;
                tmp = 5;
                break;
            }
            case 93832333: {
                if (!var4_17.equals("block")) break;
                tmp = 6;
                break;
            }
            case 3443503: {
                if (!var4_17.equals("plat")) break;
                tmp = 7;
                break;
            }
        }
        switch (tmp) {
            case 4: {
                v90 = this.target$1;
                if (v90 == null) {
                    Intrinsics.throwNpe();
                }
                v91 = v90;
                v92 = this.target$1;
                if (v92 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawPlatform(v91, v92.getHurtTime() <= 0 ? new Color(37, 126, 255, 170) : new Color(255, 0, 0, 170));
                break;
            }
            case 7: {
                v93 = this.target$1;
                if (v93 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawPlatform(v93, this.hitable != false ? new Color(37, 126, 255, 70) : new Color(255, 0, 0, 70));
                break;
            }
            case 6: {
                v94 = this.target$1;
                if (v94 == null) {
                    Intrinsics.throwNpe();
                }
                var5_14 = v94.getEntityBoundingBox();
                v95 = this.target$1;
                if (v95 == null) {
                    Intrinsics.throwNpe();
                }
                v95.setEntityBoundingBox(var5_14.expand(0.2, 0.2, 0.2));
                v96 = this.target$1;
                if (v96 == null) {
                    Intrinsics.throwNpe();
                }
                v97 = v96;
                v98 = this.target$1;
                if (v98 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawEntityBox(v97, v98.getHurtTime() <= 0 ? Color.GREEN : Color.RED, true);
                v99 = this.target$1;
                if (v99 == null) {
                    Intrinsics.throwNpe();
                }
                v99.setEntityBoundingBox(var5_14);
                break;
            }
            case 1: {
                v100 = this.target$1;
                if (v100 == null) {
                    Intrinsics.throwNpe();
                }
                v101 = v100;
                v102 = this.target$1;
                if (v102 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawPlatform(v101, v102.getHurtTime() <= 0 ? new Color(255, 255, 255, 255) : new Color(124, 215, 255, 255));
                break;
            }
            case 5: {
                var5_15 = 0.15f;
                var6_27 = 4;
                GL11.glPushMatrix();
                v103 = this.target$1;
                if (v103 == null) {
                    Intrinsics.throwNpe();
                }
                v104 = v103.getLastTickPosX();
                v105 = this.target$1;
                if (v105 == null) {
                    Intrinsics.throwNpe();
                }
                v106 = v105.getPosX();
                v107 = this.target$1;
                if (v107 == null) {
                    Intrinsics.throwNpe();
                }
                v108 = v104 + (v106 - v107.getLastTickPosX()) * (double)var1_1.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosX();
                v109 = this.target$1;
                if (v109 == null) {
                    Intrinsics.throwNpe();
                }
                v110 = v109.getLastTickPosY();
                v111 = this.target$1;
                if (v111 == null) {
                    Intrinsics.throwNpe();
                }
                v112 = v111.getPosY();
                v113 = this.target$1;
                if (v113 == null) {
                    Intrinsics.throwNpe();
                }
                v114 = v110 + (v112 - v113.getLastTickPosY()) * (double)var1_1.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosY();
                v115 = this.target$1;
                if (v115 == null) {
                    Intrinsics.throwNpe();
                }
                v116 = v114 + (double)v115.getHeight() * 1.1;
                v117 = this.target$1;
                if (v117 == null) {
                    Intrinsics.throwNpe();
                }
                v118 = v117.getLastTickPosZ();
                v119 = this.target$1;
                if (v119 == null) {
                    Intrinsics.throwNpe();
                }
                v120 = v119.getPosZ();
                v121 = this.target$1;
                if (v121 == null) {
                    Intrinsics.throwNpe();
                }
                GL11.glTranslated((double)v108, (double)v116, (double)(v118 + (v120 - v121.getLastTickPosZ()) * (double)var1_1.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosZ()));
                v122 = this.target$1;
                if (v122 == null) {
                    Intrinsics.throwNpe();
                }
                GL11.glRotatef((float)(-v122.getWidth()), (float)0.0f, (float)1.0f, (float)0.0f);
                v123 = MinecraftInstance.mc.getThePlayer();
                if (v123 == null) {
                    Intrinsics.throwNpe();
                }
                GL11.glRotatef((float)(((float)v123.getTicksExisted() + MinecraftInstance.mc.getTimer().getRenderPartialTicks()) * (float)5), (float)0.0f, (float)1.0f, (float)0.0f);
                v124 = this.target$1;
                if (v124 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.glColor(v124.getHurtTime() <= 0 ? new Color(80, 255, 80) : new Color(255, 0, 0));
                RenderUtils.enableSmoothLine(1.5f);
                var7_24 = new Cylinder();
                GL11.glRotatef((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                var7_24.draw(0.0f, var5_15, 0.3f, var6_27, 1);
                var7_24.setDrawStyle(100012);
                GL11.glTranslated((double)0.0, (double)0.0, (double)0.3);
                var7_24.draw(var5_15, 0.0f, 0.3f, var6_27, 1);
                GL11.glRotatef((float)90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                GL11.glTranslated((double)0.0, (double)0.0, (double)-0.3);
                var7_24.draw(0.0f, var5_15, 0.3f, var6_27, 1);
                GL11.glTranslated((double)0.0, (double)0.0, (double)0.3);
                var7_24.draw(var5_15, 0.0f, 0.3f, var6_27, 1);
                RenderUtils.disableSmoothLine();
                GL11.glPopMatrix();
                break;
            }
            case 3: {
                var5_11 = (int)(System.currentTimeMillis() % (long)1500);
                var6_27 = var5_11 > 750 ? 1 : 0;
                var7_25 = (double)var5_11 / 750.0;
                var7_25 = var6_27 == 0 ? (double)true - var7_25 : (var7_25 -= (double)true);
                var7_25 = EaseUtils.INSTANCE.easeInOutQuad(var7_25);
                GL11.glPushMatrix();
                GL11.glDisable((int)3553);
                GL11.glEnable((int)2848);
                GL11.glEnable((int)2881);
                GL11.glEnable((int)2832);
                GL11.glEnable((int)3042);
                GL11.glBlendFunc((int)770, (int)771);
                GL11.glHint((int)3154, (int)4354);
                GL11.glHint((int)3155, (int)4354);
                GL11.glHint((int)3153, (int)4354);
                GL11.glDisable((int)2929);
                GL11.glDepthMask((boolean)false);
                v125 = this.target$1;
                if (v125 == null) {
                    Intrinsics.throwNpe();
                }
                var9_30 = v125.getEntityBoundingBox();
                var10_33 = var9_30.getMaxX() - var9_30.getMinX() + 0.3;
                var12_35 = var9_30.getMaxY() - var9_30.getMinY();
                v126 = this.target$1;
                if (v126 == null) {
                    Intrinsics.throwNpe();
                }
                v127 = v126.getLastTickPosX();
                v128 = this.target$1;
                if (v128 == null) {
                    Intrinsics.throwNpe();
                }
                v129 = v128.getPosX();
                v130 = this.target$1;
                if (v130 == null) {
                    Intrinsics.throwNpe();
                }
                var14_36 = v127 + (v129 - v130.getLastTickPosX()) * (double)var1_1.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosX();
                v131 = this.target$1;
                if (v131 == null) {
                    Intrinsics.throwNpe();
                }
                v132 = v131.getLastTickPosY();
                v133 = this.target$1;
                if (v133 == null) {
                    Intrinsics.throwNpe();
                }
                v134 = v133.getPosY();
                v135 = this.target$1;
                if (v135 == null) {
                    Intrinsics.throwNpe();
                }
                var16_37 = v132 + (v134 - v135.getLastTickPosY()) * (double)var1_1.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosY() + var12_35 * var7_25;
                v136 = this.target$1;
                if (v136 == null) {
                    Intrinsics.throwNpe();
                }
                v137 = v136.getLastTickPosZ();
                v138 = this.target$1;
                if (v138 == null) {
                    Intrinsics.throwNpe();
                }
                v139 = v138.getPosZ();
                v140 = this.target$1;
                if (v140 == null) {
                    Intrinsics.throwNpe();
                }
                var18_38 = v137 + (v139 - v140.getLastTickPosZ()) * (double)var1_1.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosZ();
                GL11.glLineWidth((float)((float)(var10_33 * (double)5.0f)));
                GL11.glBegin((int)3);
                var21_40 = 360;
                for (var20_39 = 0; var20_39 <= var21_40; ++var20_39) {
                    v141 = MinecraftInstance.mc.getThePlayer();
                    if (v141 == null) {
                        Intrinsics.throwNpe();
                    }
                    var23_45 = (double)var20_39 / 50.0 * 1.75;
                    var34_50 = (double)v141.getTicksExisted() / 70.0;
                    var25_48 = false;
                    var36_51 = Math.sin(var23_45);
                    var43_54 = Color.HSBtoRGB((float)(var34_50 + var36_51) % 1.0f, 0.7f, 1.0f);
                    var22_43 = new Color(var43_54);
                    GL11.glColor3f((float)((float)var22_43.getRed() / 255.0f), (float)((float)var22_43.getGreen() / 255.0f), (float)((float)var22_43.getBlue() / 255.0f));
                    var23_45 = (double)var20_39 * 6.283185307179586 / 45.0;
                    var34_50 = var10_33;
                    var32_20 = var14_36;
                    var25_48 = false;
                    var36_51 = Math.cos(var23_45);
                    v142 = var32_20 + var34_50 * var36_51;
                    var23_45 = (double)var20_39 * 6.283185307179586 / 45.0;
                    var38_52 = var10_33;
                    var36_51 = var18_38;
                    var34_50 = var16_37;
                    var32_20 = v142;
                    var25_48 = false;
                    var40_53 = Math.sin(var23_45);
                    GL11.glVertex3d((double)var32_20, (double)var34_50, (double)(var36_51 + var38_52 * var40_53));
                }
                GL11.glEnd();
                GL11.glDepthMask((boolean)true);
                GL11.glEnable((int)2929);
                GL11.glDisable((int)2848);
                GL11.glDisable((int)2881);
                GL11.glEnable((int)2832);
                GL11.glEnable((int)3553);
                GL11.glPopMatrix();
                break;
            }
            case 2: {
                var5_11 = (int)(System.currentTimeMillis() % (long)2000);
                var6_27 = var5_11 > 1000 ? 1 : 0;
                var7_26 = (double)var5_11 / 1000.0;
                var7_26 = var6_27 == 0 ? (double)true - var7_26 : (var7_26 -= (double)true);
                var7_26 = EaseUtils.INSTANCE.easeInOutQuad(var7_26);
                var10_32 = false;
                var9_31 = new ArrayList<E>();
                v143 = this.target$1;
                if (v143 == null) {
                    Intrinsics.throwNpe();
                }
                var10_34 = v143.getEntityBoundingBox();
                var11_55 = var10_34.getMaxX() - var10_34.getMinX();
                var13_56 = var10_34.getMaxY() - var10_34.getMinY();
                v144 = this.target$1;
                if (v144 == null) {
                    Intrinsics.throwNpe();
                }
                v145 = v144.getLastTickPosX();
                v146 = this.target$1;
                if (v146 == null) {
                    Intrinsics.throwNpe();
                }
                v147 = v146.getPosX();
                v148 = this.target$1;
                if (v148 == null) {
                    Intrinsics.throwNpe();
                }
                var15_57 = v145 + (v147 - v148.getLastTickPosX()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks();
                v149 = this.target$1;
                if (v149 == null) {
                    Intrinsics.throwNpe();
                }
                v150 = v149.getLastTickPosY();
                v151 = this.target$1;
                if (v151 == null) {
                    Intrinsics.throwNpe();
                }
                v152 = v151.getPosY();
                v153 = this.target$1;
                if (v153 == null) {
                    Intrinsics.throwNpe();
                }
                var17_58 = v150 + (v152 - v153.getLastTickPosY()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks();
                var17_58 = var6_27 != 0 ? (var17_58 -= 0.5) : (var17_58 += 0.5);
                v154 = this.target$1;
                if (v154 == null) {
                    Intrinsics.throwNpe();
                }
                v155 = v154.getLastTickPosZ();
                v156 = this.target$1;
                if (v156 == null) {
                    Intrinsics.throwNpe();
                }
                v157 = v156.getPosZ();
                v158 = this.target$1;
                if (v158 == null) {
                    Intrinsics.throwNpe();
                }
                var19_59 = v155 + (v157 - v158.getLastTickPosZ()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks();
                var24_60 = 0;
                v159 = RangesKt.step((IntProgression)((IntProgression)new IntRange(var24_60, 360)), (int)7);
                var21_41 = v159.getFirst();
                var22_44 = v159.getLast();
                var23_46 = v159.getStep();
                v160 = var21_41;
                v161 = var22_44;
                if (var23_46 >= 0 ? v160 <= v161 : v160 >= v161) {
                    while (true) {
                        var24_61 = (double)var21_41 * 3.141592653589793 / (double)180.0f;
                        var35_63 = var15_57;
                        var32_21 = var9_31;
                        var26_62 = 0;
                        var37_64 = Math.sin(var24_61);
                        v162 = var35_63 - var37_64 * var11_55;
                        var24_61 = (double)var21_41 * 3.141592653589793 / (double)180.0f;
                        var39_65 = var19_59;
                        var37_64 = var17_58 + var13_56 * var7_26;
                        var35_63 = v162;
                        var26_62 = 0;
                        var41_66 = Math.cos(var24_61);
                        var44_67 = var39_65 + var41_66 * var11_55;
                        var46_68 = var37_64;
                        var48_69 = var35_63;
                        var32_21.add(new WVec3(var48_69, var46_68, var44_67));
                        if (var21_41 == var22_44) break;
                        var21_41 += var23_46;
                    }
                }
                var9_31.add(var9_31.get(0));
                MinecraftInstance.mc.getEntityRenderer().disableLightmap();
                GL11.glPushMatrix();
                GL11.glDisable((int)3553);
                GL11.glBlendFunc((int)770, (int)771);
                GL11.glEnable((int)2848);
                GL11.glEnable((int)3042);
                GL11.glDisable((int)2929);
                GL11.glBegin((int)3);
                var21_42 = (var7_26 > 0.5 ? (double)true - var7_26 : var7_26) * (double)2;
                var23_47 = var13_56 / (double)60 * (double)20 * ((double)true - var21_42) * (double)(var6_27 != 0 ? -1 : 1);
                var26_62 = 20;
                for (var25_49 = 0; var25_49 <= var26_62; ++var25_49) {
                    var27_70 = var13_56 / (double)60.0f * (double)var25_49 * var21_42;
                    if (var6_27 != 0) {
                        var27_70 = -var27_70;
                    }
                    var29_71 = (WVec3)var9_31.get(0);
                    GL11.glVertex3d((double)(var29_71.getXCoord() - MinecraftInstance.mc.getRenderManager().getViewerPosX()), (double)(var29_71.getYCoord() - var27_70 - var23_47 - MinecraftInstance.mc.getRenderManager().getViewerPosY()), (double)(var29_71.getZCoord() - MinecraftInstance.mc.getRenderManager().getViewerPosZ()));
                    GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)(0.7f * ((float)var25_49 / 20.0f)));
                    for (WVec3 var30_72 : var9_31) {
                        GL11.glVertex3d((double)(var30_72.getXCoord() - MinecraftInstance.mc.getRenderManager().getViewerPosX()), (double)(var30_72.getYCoord() - var27_70 - var23_47 - MinecraftInstance.mc.getRenderManager().getViewerPosY()), (double)(var30_72.getZCoord() - MinecraftInstance.mc.getRenderManager().getViewerPosZ()));
                    }
                    GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
                }
                GL11.glEnd();
                GL11.glEnable((int)2929);
                GL11.glDisable((int)2848);
                GL11.glDisable((int)3042);
                GL11.glEnable((int)3553);
                GL11.glPopMatrix();
                break;
            }
        }
        if (this.currentTarget != null && this.attackTimer.hasTimePassed(this.attackDelay)) {
            v163 = this.currentTarget;
            if (v163 == null) {
                Intrinsics.throwNpe();
            }
            if (v163.getHurtTime() <= ((Number)this.hurtTimeValue.get()).intValue()) {
                var4_18 = this.clicks;
                this.clicks = var4_18 + 1;
                this.attackTimer.reset();
                this.attackDelay = TimeUtils.randomClickDelay(((Number)this.minCPS.get()).intValue(), ((Number)this.maxCPS.get()).intValue());
            }
        }
    }

    /*
     * Exception decompiling
     */
    private final void updateTarget() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl48 : ALOAD - null : trying to set 1 previously set to 0
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:207)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    @Override
    public String getTag() {
        return (String)this.targetModeValue.get();
    }

    @Override
    public void onEnable() {
        if (MinecraftInstance.mc.getThePlayer() == null) {
            return;
        }
        if (MinecraftInstance.mc.getTheWorld() == null) {
            return;
        }
        this.updateTarget();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onMotion(MotionEvent motionEvent) {
        if (((Boolean)this.stopSprintValue.get()).booleanValue()) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.getOnGround()) {
                this.keepSprintValue.set(true);
            } else {
                this.keepSprintValue.set(false);
            }
        }
        if (motionEvent.getEventState() == EventState.POST) {
            if (this.target$1 == null) return;
            if (this.currentTarget == null) return;
            this.updateHitable();
            if (StringsKt.equals((String)((String)this.autoBlockValue.get()), (String)"AACBy1.12.2", (boolean)true)) {
                MinecraftInstance.mc.getGameSettings().getKeyBindUseItem().setPressed(true);
            }
            if (StringsKt.equals((String)((String)this.autoBlockValue.get()), (String)"Old1.8.9", (boolean)true)) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketTryUseItem(WEnumHand.MAIN_HAND));
            }
            if (StringsKt.equals((String)((String)this.autoBlockValue.get()), (String)"Hyt1.12.2", (boolean)true)) {
                return;
            }
            if (StringsKt.equals((String)((String)this.autoBlockValue.get()), (String)"Right", (boolean)true)) {
                new Robot().mousePress(4096);
            }
            if (!StringsKt.equals((String)((String)this.autoBlockValue.get()), (String)"AfterTick", (boolean)true)) return;
            KillAura3 killAura3 = this;
            boolean bl = false;
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.getHeldItem() == null) return;
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            IItemStack iItemStack = iEntityPlayerSP2.getHeldItem();
            if (iItemStack == null) {
                Intrinsics.throwNpe();
            }
            if (!MinecraftInstance.classProvider.isItemSword(iItemStack.getItem())) return;
            boolean bl2 = true;
            if (!bl2) return;
            IEntityLivingBase iEntityLivingBase = this.currentTarget;
            if (iEntityLivingBase == null) {
                Intrinsics.throwNpe();
            }
            this.startBlocking(iEntityLivingBase, this.hitable);
            return;
        }
        if (!StringsKt.equals((String)((String)this.rotationStrafeValue.get()), (String)"Off", (boolean)true)) return;
        this.update();
    }

    public final IEntityLivingBase getTarget() {
        return this.target$1;
    }

    public static final void access$setTarget$cp(Object object) {
        target = object;
    }

    private final void stopBlocking() {
        if (this.blockingStatus) {
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.Companion.getORIGIN(), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
            this.blockingStatus = false;
        }
    }

    public final IntegerValue getHurtTimeValue() {
        return this.hurtTimeValue;
    }

    public static final boolean access$isAlive(KillAura3 killAura3, IEntityLivingBase iEntityLivingBase) {
        return killAura3.isAlive(iEntityLivingBase);
    }

    public final boolean getBlockingStatus() {
        return this.blockingStatus;
    }

    public static final BoolValue access$getLivingRaycastValue$p(KillAura3 killAura3) {
        return killAura3.livingRaycastValue;
    }

    public static final BoolValue access$getRaycastIgnoredValue$p(KillAura3 killAura3) {
        return killAura3.raycastIgnoredValue;
    }

    @EventTarget
    public final void onEntityMove(EntityMovementEvent entityMovementEvent) {
        IEntity iEntity = entityMovementEvent.getMovedEntity();
        if (this.target$1 == null || iEntity.equals(this.currentTarget) ^ true) {
            return;
        }
        this.updateHitable();
    }

    public static final FloatValue access$getMinTurnSpeed$p(KillAura3 killAura3) {
        return killAura3.minTurnSpeed;
    }

    public final void setTarget(@Nullable IEntityLivingBase iEntityLivingBase) {
        this.target$1 = iEntityLivingBase;
    }

    /*
     * Exception decompiling
     */
    private final void runAttack() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl177 : ALOAD_0 - null : trying to set 1 previously set to 0
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:207)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public static final IExtractedFunctions access$getFunctions$p$s1046033730() {
        return MinecraftInstance.functions;
    }

    public final void setBlockingStatus(boolean bl) {
        this.blockingStatus = bl;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final boolean getCancelRun() {
        boolean bl = false;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.isSpectator()) return true;
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        if (!KillAura3.access$isAlive(this, iEntityPlayerSP2)) return true;
        if (!LiquidBounce.INSTANCE.getModuleManager().get(Blink.class).getState()) return false;
        return true;
    }

    public static final FloatValue access$getMaxPredictSize$p(KillAura3 killAura3) {
        return killAura3.maxPredictSize;
    }

    public static final boolean access$isEnemy(KillAura3 killAura3, IEntity iEntity) {
        return killAura3.isEnemy(iEntity);
    }

    public final ListValue getAutoBlockValue() {
        return this.autoBlockValue;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final void attackEntity(IEntityLivingBase iEntityLivingBase) {
        IEntityPlayerSP iEntityPlayerSP;
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        if ((iEntityPlayerSP = iEntityPlayerSP2).isBlocking() || this.blockingStatus) {
            this.stopBlocking();
        }
        LiquidBounce.INSTANCE.getEventManager().callEvent(new AttackEvent(iEntityLivingBase));
        if (((Boolean)this.swingValue.get()).booleanValue()) {
            // empty if block
        }
        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity((IEntity)iEntityLivingBase, ICPacketUseEntity.WAction.ATTACK));
        if (((Boolean)this.swingValue.get()).booleanValue()) {
            iEntityPlayerSP.swingItem();
        }
        if (((Boolean)this.keepSprintValue.get()).booleanValue()) {
            if (!(!(iEntityPlayerSP.getFallDistance() > 0.0f) || iEntityPlayerSP.getOnGround() || iEntityPlayerSP.isOnLadder() || iEntityPlayerSP.isInWater() || iEntityPlayerSP.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.BLINDNESS)) || iEntityPlayerSP.isRiding())) {
                iEntityPlayerSP.onCriticalHit(iEntityLivingBase);
            }
            if (KillAura3.access$getFunctions$p$s1046033730().getModifierForCreature(iEntityPlayerSP.getHeldItem(), iEntityLivingBase.getCreatureAttribute()) > 0.0f) {
                iEntityPlayerSP.onEnchantmentCritical(iEntityLivingBase);
            }
        } else if (MinecraftInstance.mc.getPlayerController().getCurrentGameType() != IWorldSettings.WGameType.SPECTATOR) {
            iEntityPlayerSP.attackTargetEntityWithCurrentItem(iEntityLivingBase);
        }
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(Criticals.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.Criticals");
        }
        Criticals criticals = (Criticals)module;
        int n = 2;
        for (int i = 0; i <= n; ++i) {
            if (iEntityPlayerSP.getFallDistance() > 0.0f && !iEntityPlayerSP.getOnGround() && !iEntityPlayerSP.isOnLadder() && !iEntityPlayerSP.isInWater() && !iEntityPlayerSP.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.BLINDNESS)) && iEntityPlayerSP.getRidingEntity() == null || criticals.getState() && criticals.getMsTimer().hasTimePassed(((Number)criticals.getDelayValue().get()).intValue()) && !iEntityPlayerSP.isInWater() && !iEntityPlayerSP.isInLava() && !iEntityPlayerSP.isInWeb()) {
                IEntityLivingBase iEntityLivingBase2 = this.target$1;
                if (iEntityLivingBase2 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP.onCriticalHit(iEntityLivingBase2);
            }
            IExtractedFunctions iExtractedFunctions = KillAura3.access$getFunctions$p$s1046033730();
            IItemStack iItemStack = iEntityPlayerSP.getHeldItem();
            IEntityLivingBase iEntityLivingBase3 = this.target$1;
            if (iEntityLivingBase3 == null) {
                Intrinsics.throwNpe();
            }
            if (!(iExtractedFunctions.getModifierForCreature(iItemStack, iEntityLivingBase3.getCreatureAttribute()) > 0.0f) && !((Boolean)this.fakeSharpValue.get()).booleanValue()) continue;
            IEntityLivingBase iEntityLivingBase4 = this.target$1;
            if (iEntityLivingBase4 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP.onEnchantmentCritical(iEntityLivingBase4);
        }
        if (!StringsKt.equals((String)((String)this.autoBlockValue.get()), (String)"Packet", (boolean)true)) return;
        if (!iEntityPlayerSP.isBlocking()) {
            KillAura3 killAura3 = this;
            n = 0;
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP3.getHeldItem() == null) return;
            IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP4 == null) {
                Intrinsics.throwNpe();
            }
            IItemStack iItemStack = iEntityPlayerSP4.getHeldItem();
            if (iItemStack == null) {
                Intrinsics.throwNpe();
            }
            if (!MinecraftInstance.classProvider.isItemSword(iItemStack.getItem())) return;
            boolean bl = true;
            if (!bl) return;
        }
        iEntityPlayerSP.resetCooldown();
    }

    private final float getRange(IEntity iEntity) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        float f = PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, iEntity) >= ((Number)this.throughWallsRangeValue.get()).doubleValue() ? ((Number)this.rangeValue.get()).floatValue() : ((Number)this.throughWallsRangeValue.get()).floatValue();
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        return f - (iEntityPlayerSP2.getSprinting() ? ((Number)this.rangeSprintReducementValue.get()).floatValue() : 0.0f);
    }

    private final void updateHitable() {
        if (((Number)this.maxTurnSpeed.get()).floatValue() <= 0.0f) {
            this.hitable = true;
            return;
        }
        double d = this.getMaxRange();
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntity iEntity = iEntityPlayerSP;
        IEntityLivingBase iEntityLivingBase = this.target$1;
        if (iEntityLivingBase == null) {
            Intrinsics.throwNpe();
        }
        double d2 = PlayerExtensionKt.getDistanceToEntityBox(iEntity, iEntityLivingBase);
        boolean bl = false;
        double d3 = Math.min(d, d2) + 1.0;
        if (((Boolean)this.raycastValue.get()).booleanValue()) {
            IEntity iEntity2 = RaycastUtils.raycastEntity(d3, new RaycastUtils.EntityFilter(this){
                final KillAura3 this$0;

                static {
                }
                {
                    this.this$0 = killAura3;
                }

                /*
                 * Enabled force condition propagation
                 * Lifted jumps to return sites
                 */
                public boolean canRaycast(@Nullable IEntity iEntity) {
                    if (((Boolean)KillAura3.access$getLivingRaycastValue$p(this.this$0).get()).booleanValue()) {
                        if (!MinecraftInstance.classProvider.isEntityLivingBase(iEntity)) return false;
                        if (MinecraftInstance.classProvider.isEntityArmorStand(iEntity)) return false;
                    }
                    if (KillAura3.access$isEnemy(this.this$0, iEntity)) return true;
                    if ((Boolean)KillAura3.access$getRaycastIgnoredValue$p(this.this$0).get() != false) return true;
                    if ((Boolean)KillAura3.access$getAacValue$p(this.this$0).get() == false) return false;
                    IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                    if (iWorldClient == null) {
                        Intrinsics.throwNpe();
                    }
                    IEntity iEntity2 = iEntity;
                    if (iEntity2 == null) {
                        Intrinsics.throwNpe();
                    }
                    Collection collection = iWorldClient.getEntitiesWithinAABBExcludingEntity(iEntity, iEntity2.getEntityBoundingBox());
                    boolean bl = false;
                    if (collection.isEmpty()) return false;
                    return true;
                }
            });
            if (((Boolean)this.raycastValue.get()).booleanValue() && iEntity2 != null && MinecraftInstance.classProvider.isEntityLivingBase(iEntity2) && (LiquidBounce.INSTANCE.getModuleManager().get(NoFriends.class).getState() || !MinecraftInstance.classProvider.isEntityPlayer(iEntity2) || !PlayerExtensionKt.isClientFriend(iEntity2.asEntityPlayer()))) {
                this.currentTarget = iEntity2.asEntityLivingBase();
            }
            this.hitable = ((Number)this.maxTurnSpeed.get()).floatValue() > 0.0f ? this.currentTarget.equals(iEntity2) : true;
        } else {
            this.hitable = RotationUtils.isFaced(this.currentTarget, d3);
        }
    }

    public static final FloatValue access$getMaxTurnSpeed$p(KillAura3 killAura3) {
        return killAura3.maxTurnSpeed;
    }

    public static final class Companion {
        public final Object getTarget() {
            return KillAura3.access$getTarget$cp();
        }

        public final void setTarget(Object object) {
            KillAura3.access$setTarget$cp(object);
        }

        private Companion() {
        }

        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}

