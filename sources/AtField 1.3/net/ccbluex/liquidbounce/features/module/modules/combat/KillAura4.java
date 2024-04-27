/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.ranges.IntProgression
 *  kotlin.ranges.IntRange
 *  kotlin.ranges.RangesKt
 *  kotlin.text.StringsKt
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.jvm.JvmStatic;
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
import net.ccbluex.liquidbounce.features.module.modules.render.Animations;
import net.ccbluex.liquidbounce.features.module.modules.render.FreeCam;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.PacketUtils;
import net.ccbluex.liquidbounce.utils.RaycastUtils;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.extensions.PlayerExtensionKt;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="KillAura4", description="Skid by Autumn", category=ModuleCategory.COMBAT)
public final class KillAura4
extends Module {
    private final IntegerValue hurtTimeValue;
    private final List prevTargetEntities;
    private final FloatValue minTurnSpeed;
    private final BoolValue keepSprintValue;
    private final ListValue priorityValue;
    private final MSTimer attackTimer;
    private final FloatValue rangeValue;
    private final BoolValue silentRotationValue;
    private boolean blockingStatus;
    private final BoolValue fakeSharpValue;
    private final FloatValue maxTurnSpeed;
    private final IntegerValue circleRed;
    private final ListValue blockModeValue;
    private final FloatValue failRateValue;
    private final BoolValue swingValue;
    private final FloatValue throughWallsRangeValue;
    private final BoolValue circleValue;
    private final BoolValue predictValue;
    private final FloatValue maxPredictSize;
    private final ListValue targetModeValue;
    private final BoolValue stopValue;
    private final FloatValue minPredictSize;
    private long containerOpen;
    private final IntegerValue circleAlpha;
    public static final Companion Companion = new Companion(null);
    private long attackDelay;
    private final IntegerValue minCPS;
    private final IntegerValue maxCPS = new IntegerValue(this, "MaxCPS", 8, 1, 20){
        final KillAura4 this$0;
        {
            this.this$0 = killAura4;
            super(string, n, n2, n3);
        }

        public void onChanged(Object object, Object object2) {
            this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
        }

        static {
        }

        protected void onChanged(int n, int n2) {
            int n3 = ((Number)this.this$0.getMinCPS().get()).intValue();
            if (n3 > n2) {
                this.set((Object)n3);
            }
            KillAura4.access$setAttackDelay$p(this.this$0, TimeUtils.randomClickDelay(((Number)this.this$0.getMinCPS().get()).intValue(), ((Number)this.get()).intValue()));
        }
    };
    private final BoolValue raycastIgnoredValue;
    private IEntityLivingBase target;
    private static int killCounts;
    private final IntegerValue circleBlue;
    private final IntegerValue limitedMultiTargetsValue;
    private final BoolValue outborderValue;
    private final FloatValue fovValue;
    private final BoolValue aacValue;
    private final ListValue markValue;
    private final FloatValue cooldownValue;
    private IEntityLivingBase syncEntity;
    private final BoolValue raycastValue;
    private int clicks;
    private final BoolValue randomCenterValue;
    private boolean hitable;
    private final BoolValue afterAttackValue;
    private IEntityLivingBase currentTarget;
    private final BoolValue fakeSwingValue;
    private final ListValue rotationStrafeValue;
    private final FloatValue rangeSprintReducementValue;
    private final BoolValue interactAutoBlockValue;
    private final IntegerValue circleGreen;
    private final BoolValue livingRaycastValue;
    private final IntegerValue noInventoryDelayValue;
    private final BoolValue noInventoryAttackValue;
    private final ListValue rotations;
    private final IntegerValue circleAccuracy;

    public static final boolean access$isAlive(KillAura4 killAura4, IEntityLivingBase iEntityLivingBase) {
        return killAura4.isAlive(iEntityLivingBase);
    }

    public static final BoolValue access$getRaycastIgnoredValue$p(KillAura4 killAura4) {
        return killAura4.raycastIgnoredValue;
    }

    /*
     * Unable to fully structure code
     */
    private final void attackEntity(IEntityLivingBase var1_1) {
        block25: {
            block26: {
                v0 = MinecraftInstance.mc.getThePlayer();
                if (v0 == null) {
                    Intrinsics.throwNpe();
                }
                if ((var2_2 = v0).isBlocking() || this.blockingStatus) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.Companion.getORIGIN(), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
                    if (((Boolean)this.afterAttackValue.get()).booleanValue()) {
                        this.blockingStatus = false;
                    }
                }
                LiquidBounce.INSTANCE.getEventManager().callEvent(new AttackEvent(var1_1));
                if (((Boolean)this.swingValue.get()).booleanValue()) {
                    // empty if block
                }
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity((IEntity)var1_1, ICPacketUseEntity.WAction.ATTACK));
                if (((Boolean)this.swingValue.get()).booleanValue()) {
                    var2_2.swingItem();
                }
                if (((Boolean)this.keepSprintValue.get()).booleanValue()) {
                    if (!(!(var2_2.getFallDistance() > 0.0f) || var2_2.getOnGround() || var2_2.isOnLadder() || var2_2.isInWater() || var2_2.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.BLINDNESS)) || var2_2.isRiding())) {
                        var2_2.onCriticalHit(var1_1);
                    }
                    if (KillAura4.access$getFunctions$p$s1046033730().getModifierForCreature(var2_2.getHeldItem(), var1_1.getCreatureAttribute()) > 0.0f) {
                        var2_2.onEnchantmentCritical(var1_1);
                    }
                } else if (MinecraftInstance.mc.getPlayerController().getCurrentGameType() != IWorldSettings.WGameType.SPECTATOR) {
                    var2_2.attackTargetEntityWithCurrentItem(var1_1);
                }
                v1 = LiquidBounce.INSTANCE.getModuleManager().get(Criticals.class);
                if (v1 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.Criticals");
                }
                var3_3 = (Criticals)v1;
                var5_6 = 2;
                for (var4_4 = 0; var4_4 <= var5_6; ++var4_4) {
                    if (var2_2.getFallDistance() > 0.0f && var2_2.getOnGround() == false && var2_2.isOnLadder() == false && var2_2.isInWater() == false && var2_2.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.BLINDNESS)) == false && var2_2.getRidingEntity() == null || var3_3.getState() && var3_3.getMsTimer().hasTimePassed(((Number)var3_3.getDelayValue().get()).intValue()) && !var2_2.isInWater() && !var2_2.isInLava() && !var2_2.isInWeb()) {
                        v2 = this.target;
                        if (v2 == null) {
                            Intrinsics.throwNpe();
                        }
                        var2_2.onCriticalHit(v2);
                    }
                    v3 = KillAura4.access$getFunctions$p$s1046033730();
                    v4 = var2_2.getHeldItem();
                    v5 = this.target;
                    if (v5 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!(v3.getModifierForCreature(v4, v5.getCreatureAttribute()) > 0.0f) && !((Boolean)this.fakeSharpValue.get()).booleanValue()) continue;
                    v6 = this.target;
                    if (v6 == null) {
                        Intrinsics.throwNpe();
                    }
                    var2_2.onEnchantmentCritical(v6);
                }
                if (StringsKt.equals((String)((String)this.blockModeValue.get()), (String)"None", (boolean)true)) break block25;
                if (var2_2.isBlocking()) break block26;
                var4_5 = this;
                var5_6 = 0;
                v7 = MinecraftInstance.mc.getThePlayer();
                if (v7 == null) {
                    Intrinsics.throwNpe();
                }
                if (v7.getHeldItem() == null) ** GOTO lbl-1000
                v8 = MinecraftInstance.mc.getThePlayer();
                if (v8 == null) {
                    Intrinsics.throwNpe();
                }
                v9 = v8.getHeldItem();
                if (v9 == null) {
                    Intrinsics.throwNpe();
                }
                if (MinecraftInstance.classProvider.isItemSword(v9.getItem())) {
                    v10 = true;
                } else lbl-1000:
                // 2 sources

                {
                    v10 = false;
                }
                if (!v10) break block25;
            }
            this.startBlocking(var1_1, (Boolean)this.interactAutoBlockValue.get());
        }
        var2_2.resetCooldown();
        var2_2.resetCooldown();
    }

    public final BoolValue getKeepSprintValue() {
        return this.keepSprintValue;
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
        IEntityLivingBase iEntityLivingBase = this.target;
        if (iEntityLivingBase == null) {
            Intrinsics.throwNpe();
        }
        double d2 = PlayerExtensionKt.getDistanceToEntityBox(iEntity, iEntityLivingBase);
        boolean bl = false;
        double d3 = Math.min(d, d2) + 1.0;
        if (((Boolean)this.raycastValue.get()).booleanValue()) {
            IEntity iEntity2 = RaycastUtils.raycastEntity(d3, new RaycastUtils.EntityFilter(this){
                final KillAura4 this$0;
                {
                    this.this$0 = killAura4;
                }

                /*
                 * Enabled force condition propagation
                 * Lifted jumps to return sites
                 */
                public boolean canRaycast(@Nullable IEntity iEntity) {
                    if (((Boolean)KillAura4.access$getLivingRaycastValue$p(this.this$0).get()).booleanValue()) {
                        if (!MinecraftInstance.classProvider.isEntityLivingBase(iEntity)) return false;
                        if (MinecraftInstance.classProvider.isEntityArmorStand(iEntity)) return false;
                    }
                    if (KillAura4.access$isEnemy(this.this$0, iEntity)) return true;
                    if ((Boolean)KillAura4.access$getRaycastIgnoredValue$p(this.this$0).get() != false) return true;
                    if ((Boolean)KillAura4.access$getAacValue$p(this.this$0).get() == false) return false;
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

                static {
                }
            });
            if (((Boolean)this.raycastValue.get()).booleanValue() && iEntity2 != null && MinecraftInstance.classProvider.isEntityLivingBase(iEntity2) && (LiquidBounce.INSTANCE.getModuleManager().get(NoFriends.class).getState() || !EntityUtils.isFriend((IEntity)iEntity2))) {
                this.currentTarget = iEntity2.asEntityLivingBase();
            }
            this.hitable = ((Number)this.maxTurnSpeed.get()).floatValue() > 0.0f ? this.currentTarget.equals(iEntity2) : true;
        } else {
            this.hitable = RotationUtils.isFaced(this.currentTarget, d3);
        }
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
        if (!KillAura4.access$isAlive(this, iEntityPlayerSP2)) return true;
        if (LiquidBounce.INSTANCE.getModuleManager().get(Blink.class).getState()) return true;
        if (!LiquidBounce.INSTANCE.getModuleManager().get(FreeCam.class).getState()) return false;
        return true;
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

    public static final FloatValue access$getMaxTurnSpeed$p(KillAura4 killAura4) {
        return killAura4.maxTurnSpeed;
    }

    public static final long access$getAttackDelay$p(KillAura4 killAura4) {
        return killAura4.attackDelay;
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

    public static final BoolValue access$getLivingRaycastValue$p(KillAura4 killAura4) {
        return killAura4.livingRaycastValue;
    }

    public final FloatValue getRangeValue() {
        return this.rangeValue;
    }

    public final IEntityLivingBase getSyncEntity() {
        return this.syncEntity;
    }

    public static final void access$setAttackDelay$p(KillAura4 killAura4, long l) {
        killAura4.attackDelay = l;
    }

    public static final FloatValue access$getMinPredictSize$p(KillAura4 killAura4) {
        return killAura4.minPredictSize;
    }

    public final void setTarget(@Nullable IEntityLivingBase iEntityLivingBase) {
        this.target = iEntityLivingBase;
    }

    public final void setSyncEntity(@Nullable IEntityLivingBase iEntityLivingBase) {
        this.syncEntity = iEntityLivingBase;
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onRender3D(Render3DEvent var1_1) {
        if (((Boolean)this.circleValue.get()).booleanValue()) {
            GL11.glPushMatrix();
            v0 = MinecraftInstance.mc.getThePlayer();
            if (v0 == null) {
                Intrinsics.throwNpe();
            }
            v1 = v0.getLastTickPosX();
            v2 = MinecraftInstance.mc.getThePlayer();
            if (v2 == null) {
                Intrinsics.throwNpe();
            }
            v3 = v2.getPosX();
            v4 = MinecraftInstance.mc.getThePlayer();
            if (v4 == null) {
                Intrinsics.throwNpe();
            }
            v5 = v1 + (v3 - v4.getLastTickPosX()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks() - MinecraftInstance.mc.getRenderManager().getRenderPosX();
            v6 = MinecraftInstance.mc.getThePlayer();
            if (v6 == null) {
                Intrinsics.throwNpe();
            }
            v7 = v6.getLastTickPosY();
            v8 = MinecraftInstance.mc.getThePlayer();
            if (v8 == null) {
                Intrinsics.throwNpe();
            }
            v9 = v8.getPosY();
            v10 = MinecraftInstance.mc.getThePlayer();
            if (v10 == null) {
                Intrinsics.throwNpe();
            }
            v11 = v7 + (v9 - v10.getLastTickPosY()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks() - MinecraftInstance.mc.getRenderManager().getRenderPosY();
            v12 = MinecraftInstance.mc.getThePlayer();
            if (v12 == null) {
                Intrinsics.throwNpe();
            }
            v13 = v12.getLastTickPosZ();
            v14 = MinecraftInstance.mc.getThePlayer();
            if (v14 == null) {
                Intrinsics.throwNpe();
            }
            v15 = v14.getPosZ();
            v16 = MinecraftInstance.mc.getThePlayer();
            if (v16 == null) {
                Intrinsics.throwNpe();
            }
            GL11.glTranslated((double)v5, (double)v11, (double)(v13 + (v15 - v16.getLastTickPosZ()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks() - MinecraftInstance.mc.getRenderManager().getRenderPosZ()));
            GL11.glEnable((int)3042);
            GL11.glEnable((int)2848);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2929);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glLineWidth((float)1.0f);
            GL11.glColor4f((float)((float)((Number)this.circleRed.get()).intValue() / 255.0f), (float)((float)((Number)this.circleGreen.get()).intValue() / 255.0f), (float)((float)((Number)this.circleBlue.get()).intValue() / 255.0f), (float)((float)((Number)this.circleAlpha.get()).intValue() / 255.0f));
            GL11.glRotatef((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glBegin((int)3);
            var5_2 = 0;
            v17 = RangesKt.step((IntProgression)((IntProgression)new IntRange(var5_2, 360)), (int)(61 - ((Number)this.circleAccuracy.get()).intValue()));
            var2_5 = v17.getFirst();
            var3_9 = v17.getLast();
            var4_12 = v17.getStep();
            v18 = var2_5;
            v19 = var3_9;
            if (var4_12 >= 0 ? v18 <= v19 : v18 >= v19) {
                while (true) {
                    var5_3 = (double)var2_5 * 3.141592653589793 / 180.0;
                    var7_13 = false;
                    v20 = (float)Math.cos(var5_3) * ((Number)this.rangeValue.get()).floatValue();
                    var5_3 = (double)var2_5 * 3.141592653589793 / 180.0;
                    var24_15 = v20;
                    var7_13 = false;
                    var25_17 = Math.sin(var5_3);
                    GL11.glVertex2f((float)var24_15, (float)((float)var25_17 * ((Number)this.rangeValue.get()).floatValue()));
                    if (var2_5 == var3_9) break;
                    var2_5 += var4_12;
                }
            }
            var2_6 = 6.283185307179586;
            var4_12 = 0;
            v21 = (float)Math.cos(var2_6) * ((Number)this.rangeValue.get()).floatValue();
            var2_6 = 6.283185307179586;
            var24_15 = v21;
            var4_12 = 0;
            var25_17 = Math.sin(var2_6);
            GL11.glVertex2f((float)var24_15, (float)((float)var25_17 * ((Number)this.rangeValue.get()).floatValue()));
            GL11.glEnd();
            GL11.glDisable((int)3042);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glDisable((int)2848);
            GL11.glPopMatrix();
        }
        var2_7 = this;
        var3_9 = 0;
        v22 = MinecraftInstance.mc.getThePlayer();
        if (v22 == null) {
            Intrinsics.throwNpe();
        }
        if (v22.isSpectator()) ** GOTO lbl-1000
        v23 = MinecraftInstance.mc.getThePlayer();
        if (v23 == null) {
            Intrinsics.throwNpe();
        }
        if (!KillAura4.access$isAlive((KillAura4)var2_7, v23) || LiquidBounce.INSTANCE.getModuleManager().get(Blink.class).getState() || LiquidBounce.INSTANCE.getModuleManager().get(FreeCam.class).getState()) lbl-1000:
        // 2 sources

        {
            v24 = true;
        } else {
            v24 = false;
        }
        if (v24) {
            this.target = null;
            this.currentTarget = null;
            this.hitable = false;
            this.stopBlocking();
            return;
        }
        if (((Boolean)this.noInventoryAttackValue.get()).booleanValue() && (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen()) || System.currentTimeMillis() - this.containerOpen < ((Number)this.noInventoryDelayValue.get()).longValue())) {
            this.target = null;
            this.currentTarget = null;
            this.hitable = false;
            if (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen())) {
                this.containerOpen = System.currentTimeMillis();
            }
            return;
        }
        if (this.target == null) {
            return;
        }
        var2_7 = (String)this.markValue.get();
        var3_9 = 0;
        v25 = var2_7;
        if (v25 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        var2_7 = v25.toLowerCase();
        tmp = -1;
        switch (var2_7.hashCode()) {
            case 108960: {
                if (!var2_7.equals("new")) break;
                tmp = 1;
                break;
            }
            case 101234: {
                if (!var2_7.equals("fdp")) break;
                tmp = 2;
                break;
            }
            case 93832333: {
                if (!var2_7.equals("block")) break;
                tmp = 3;
                break;
            }
            case -1406316010: {
                if (!var2_7.equals("autumn")) break;
                tmp = 4;
                break;
            }
        }
        switch (tmp) {
            case 3: {
                v26 = this.target;
                if (v26 == null) {
                    Intrinsics.throwNpe();
                }
                var3_10 = v26.getEntityBoundingBox();
                v27 = this.target;
                if (v27 == null) {
                    Intrinsics.throwNpe();
                }
                v27.setEntityBoundingBox(var3_10.expand(0.2, 0.2, 0.2));
                v28 = this.target;
                if (v28 == null) {
                    Intrinsics.throwNpe();
                }
                v29 = v28;
                v30 = this.target;
                if (v30 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawEntityBox(v29, v30.getHurtTime() <= 0 ? Color.BLUE : Color.RED, true);
                v31 = this.target;
                if (v31 == null) {
                    Intrinsics.throwNpe();
                }
                v31.setEntityBoundingBox(var3_10);
                break;
            }
            case 4: {
                v32 = this.target;
                if (v32 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawmark(v32, 0.65, true);
                break;
            }
            case 1: {
                RenderUtils.drawCircleESP(this.target, 0.67, Color.RED.getRGB(), true);
                break;
            }
            case 2: {
                var3_9 = (int)(System.currentTimeMillis() % (long)1500);
                var4_12 = var3_9 > 750 ? 1 : 0;
                var5_4 = (double)var3_9 / 750.0;
                var5_4 = var4_12 == 0 ? (double)true - var5_4 : (var5_4 -= (double)true);
                var5_4 = EaseUtils.INSTANCE.easeInOutQuad(var5_4);
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
                v33 = this.target;
                if (v33 == null) {
                    Intrinsics.throwNpe();
                }
                var7_14 = v33.getEntityBoundingBox();
                var8_18 = var7_14.getMaxX() - var7_14.getMinX() + 0.1;
                var10_19 = var7_14.getMaxY() - var7_14.getMinY();
                v34 = this.target;
                if (v34 == null) {
                    Intrinsics.throwNpe();
                }
                v35 = v34.getLastTickPosX();
                v36 = this.target;
                if (v36 == null) {
                    Intrinsics.throwNpe();
                }
                v37 = v36.getPosX();
                v38 = this.target;
                if (v38 == null) {
                    Intrinsics.throwNpe();
                }
                var12_20 = v35 + (v37 - v38.getLastTickPosX()) * (double)var1_1.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosX();
                v39 = this.target;
                if (v39 == null) {
                    Intrinsics.throwNpe();
                }
                v40 = v39.getLastTickPosY();
                v41 = this.target;
                if (v41 == null) {
                    Intrinsics.throwNpe();
                }
                v42 = v41.getPosY();
                v43 = this.target;
                if (v43 == null) {
                    Intrinsics.throwNpe();
                }
                var14_21 = v40 + (v42 - v43.getLastTickPosY()) * (double)var1_1.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosY() + var10_19 * var5_4;
                v44 = this.target;
                if (v44 == null) {
                    Intrinsics.throwNpe();
                }
                v45 = v44.getLastTickPosZ();
                v46 = this.target;
                if (v46 == null) {
                    Intrinsics.throwNpe();
                }
                v47 = v46.getPosZ();
                v48 = this.target;
                if (v48 == null) {
                    Intrinsics.throwNpe();
                }
                var16_22 = v45 + (v47 - v48.getLastTickPosZ()) * (double)var1_1.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosZ();
                GL11.glLineWidth((float)((float)(var8_18 * (double)5.0f)));
                GL11.glBegin((int)3);
                var19_24 = 360;
                for (var18_23 = 0; var18_23 <= var19_24; ++var18_23) {
                    v49 = MinecraftInstance.mc.getThePlayer();
                    if (v49 == null) {
                        Intrinsics.throwNpe();
                    }
                    var21_28 = (double)var18_23 / 50.0 * 1.75;
                    var26_30 = (double)v49.getTicksExisted() / 70.0;
                    var23_29 = false;
                    var28_31 = Math.sin(var21_28);
                    var34_26 = Color.HSBtoRGB((float)(var26_30 + var28_31) % 1.0f, 0.7f, 1.0f);
                    var20_27 = new Color(var34_26);
                    GL11.glColor3f((float)((float)var20_27.getRed() / 255.0f), (float)((float)var20_27.getGreen() / 255.0f), (float)((float)var20_27.getBlue() / 255.0f));
                    var21_28 = (double)var18_23 * 6.283185307179586 / 45.0;
                    var26_30 = var8_18;
                    var24_16 = var12_20;
                    var23_29 = false;
                    var28_31 = Math.cos(var21_28);
                    v50 = var24_16 + var26_30 * var28_31;
                    var21_28 = (double)var18_23 * 6.283185307179586 / 45.0;
                    var30_32 = var8_18;
                    var28_31 = var16_22;
                    var26_30 = var14_21;
                    var24_16 = v50;
                    var23_29 = false;
                    var32_25 = Math.sin(var21_28);
                    GL11.glVertex3d((double)var24_16, (double)var26_30, (double)(var28_31 + var30_32 * var32_25));
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
        }
        var2_7 = (String)this.markValue.get();
        var3_11 = false;
        v51 = var2_7;
        if (v51 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        if (v51.toLowerCase().equals("old") && !StringsKt.equals((String)((String)this.targetModeValue.get()), (String)"Multi", (boolean)true)) {
            RenderUtils.drawPlatform(this.target, this.hitable != false ? new Color(37, 126, 255, 70) : new Color(255, 0, 0, 70));
        }
        if (this.currentTarget != null && this.attackTimer.hasTimePassed(this.attackDelay)) {
            v52 = this.currentTarget;
            if (v52 == null) {
                Intrinsics.throwNpe();
            }
            if (v52.getHurtTime() <= ((Number)this.hurtTimeValue.get()).intValue()) {
                var2_8 = this.clicks;
                this.clicks = var2_8 + 1;
                this.attackTimer.reset();
                this.attackDelay = TimeUtils.randomClickDelay(((Number)this.minCPS.get()).intValue(), ((Number)this.maxCPS.get()).intValue());
            }
        }
    }

    public final IntegerValue getHurtTimeValue() {
        return this.hurtTimeValue;
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

    public static final FloatValue access$getMinTurnSpeed$p(KillAura4 killAura4) {
        return killAura4.minTurnSpeed;
    }

    @EventTarget
    public final void onMotion(MotionEvent motionEvent) {
        if (StringsKt.equals((String)((String)this.rotationStrafeValue.get()), (String)"Off", (boolean)true)) {
            this.update();
        }
    }

    public final ListValue getRotations() {
        return this.rotations;
    }

    public static final void setKillCounts(int n) {
        Companion companion = Companion;
        killCounts = n;
    }

    public static final BoolValue access$getAacValue$p(KillAura4 killAura4) {
        return killAura4.aacValue;
    }

    public static final int getKillCounts() {
        Companion companion = Companion;
        return killCounts;
    }

    @Override
    public void onDisable() {
        this.target = null;
        this.currentTarget = null;
        this.hitable = false;
        this.prevTargetEntities.clear();
        this.attackTimer.reset();
        this.clicks = 0;
        this.stopBlocking();
    }

    public static final boolean access$isEnemy(KillAura4 killAura4, IEntity iEntity) {
        return killAura4.isEnemy(iEntity);
    }

    public KillAura4() {
        List list;
        this.minCPS = new IntegerValue(this, "MinCPS", 5, 1, 20){
            final KillAura4 this$0;

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
            }

            protected void onChanged(int n, int n2) {
                int n3 = ((Number)this.this$0.getMaxCPS().get()).intValue();
                if (n3 < n2) {
                    this.set((Object)n3);
                }
                KillAura4.access$setAttackDelay$p(this.this$0, TimeUtils.randomClickDelay(((Number)this.get()).intValue(), ((Number)this.this$0.getMaxCPS().get()).intValue()));
            }

            static {
            }
            {
                this.this$0 = killAura4;
                super(string, n, n2, n3);
            }
        };
        this.hurtTimeValue = new IntegerValue("HurtTime", 10, 0, 10);
        this.cooldownValue = new FloatValue("Cooldown", 1.0f, 0.0f, 1.0f);
        this.rangeValue = new FloatValue("Range", 3.7f, 1.0f, 8.0f);
        this.throughWallsRangeValue = new FloatValue("ThroughWallsRange", 3.0f, 0.0f, 8.0f);
        this.rangeSprintReducementValue = new FloatValue("RangeSprintReducement", 0.0f, 0.0f, 0.4f);
        this.priorityValue = new ListValue("Priority", new String[]{"Health", "Distance", "Direction", "LivingTime"}, "Distance");
        this.targetModeValue = new ListValue("TargetMode", new String[]{"Single", "Switch", "Multi"}, "Switch");
        this.swingValue = new BoolValue("Swing", true);
        this.keepSprintValue = new BoolValue("KeepSprint", true);
        this.stopValue = new BoolValue("StopSprintinair", true);
        this.afterAttackValue = new BoolValue("after-attack", false);
        this.blockModeValue = new ListValue("AutoBlockMode", new String[]{"B73", "Normal", "Old", "New", "None"}, "LiquidBounce");
        this.interactAutoBlockValue = new BoolValue("InteractAutoBlock", false);
        this.raycastValue = new BoolValue("RayCast", true);
        this.raycastIgnoredValue = new BoolValue("RayCastIgnored", false);
        this.livingRaycastValue = new BoolValue("LivingRayCast", true);
        this.aacValue = new BoolValue("AAC", false);
        this.rotations = new ListValue("RotationMode", new String[]{"LiquidBounce", "Autumn", "HytRotation"}, "Autumn");
        this.silentRotationValue = new BoolValue("SilentRotation", true);
        this.rotationStrafeValue = new ListValue("Strafe", new String[]{"Off", "Strict", "Silent"}, "Off");
        this.randomCenterValue = new BoolValue("RandomCenter", true);
        this.outborderValue = new BoolValue("Outborder", false);
        this.maxTurnSpeed = new FloatValue(this, "MaxTurnSpeed", 180.0f, 0.0f, 180.0f){
            final KillAura4 this$0;
            {
                this.this$0 = killAura4;
                super(string, f, f2, f3);
            }

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).floatValue(), ((Number)object2).floatValue());
            }

            protected void onChanged(float f, float f2) {
                float f3 = ((Number)KillAura4.access$getMinTurnSpeed$p(this.this$0).get()).floatValue();
                if (f3 > f2) {
                    this.set((Object)Float.valueOf(f3));
                }
            }

            static {
            }
        };
        this.minTurnSpeed = new FloatValue(this, "MinTurnSpeed", 180.0f, 0.0f, 180.0f){
            final KillAura4 this$0;

            static {
            }
            {
                this.this$0 = killAura4;
                super(string, f, f2, f3);
            }

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).floatValue(), ((Number)object2).floatValue());
            }

            protected void onChanged(float f, float f2) {
                float f3 = ((Number)KillAura4.access$getMaxTurnSpeed$p(this.this$0).get()).floatValue();
                if (f3 < f2) {
                    this.set((Object)Float.valueOf(f3));
                }
            }
        };
        this.fovValue = new FloatValue("FOV", 180.0f, 0.0f, 180.0f);
        this.predictValue = new BoolValue("Predict", true);
        this.maxPredictSize = new FloatValue(this, "MaxPredictSize", 1.0f, 0.1f, 5.0f){
            final KillAura4 this$0;
            {
                this.this$0 = killAura4;
                super(string, f, f2, f3);
            }

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).floatValue(), ((Number)object2).floatValue());
            }

            protected void onChanged(float f, float f2) {
                float f3 = ((Number)KillAura4.access$getMinPredictSize$p(this.this$0).get()).floatValue();
                if (f3 > f2) {
                    this.set((Object)Float.valueOf(f3));
                }
            }

            static {
            }
        };
        this.minPredictSize = new FloatValue(this, "MinPredictSize", 1.0f, 0.1f, 5.0f){
            final KillAura4 this$0;

            static {
            }

            protected void onChanged(float f, float f2) {
                float f3 = ((Number)KillAura4.access$getMaxPredictSize$p(this.this$0).get()).floatValue();
                if (f3 < f2) {
                    this.set((Object)Float.valueOf(f3));
                }
            }
            {
                this.this$0 = killAura4;
                super(string, f, f2, f3);
            }

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).floatValue(), ((Number)object2).floatValue());
            }
        };
        this.failRateValue = new FloatValue("FailRate", 0.0f, 0.0f, 100.0f);
        this.fakeSwingValue = new BoolValue("FakeSwing", true);
        this.noInventoryAttackValue = new BoolValue("NoInvAttack", false);
        this.noInventoryDelayValue = new IntegerValue("NoInvDelay", 200, 0, 500);
        this.limitedMultiTargetsValue = new IntegerValue("LimitedMultiTargets", 0, 0, 50);
        this.markValue = new ListValue("Mark", new String[]{"Old", "FDP", "Block", "New", "Autumn", "None"}, "Old");
        this.fakeSharpValue = new BoolValue("FakeSharp", true);
        this.circleValue = new BoolValue("Circle", true);
        this.circleRed = new IntegerValue("CircleRed", 255, 0, 255);
        this.circleGreen = new IntegerValue("CircleGreen", 255, 0, 255);
        this.circleBlue = new IntegerValue("CircleBlue", 255, 0, 255);
        this.circleAlpha = new IntegerValue("CircleAlpha", 255, 0, 255);
        this.circleAccuracy = new IntegerValue("CircleAccuracy", 15, 0, 60);
        KillAura4 killAura4 = this;
        boolean bl = false;
        killAura4.prevTargetEntities = list = (List)new ArrayList();
        this.attackTimer = new MSTimer();
        this.containerOpen = -1L;
    }

    private final void stopBlocking() {
        if (this.blockingStatus) {
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.Companion.getORIGIN(), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
            this.blockingStatus = false;
        }
    }

    public final void setBlockingStatus(boolean bl) {
        this.blockingStatus = bl;
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onUpdate(UpdateEvent var1_1) {
        if (((Boolean)this.stopValue.get()).booleanValue()) {
            v0 = MinecraftInstance.mc.getThePlayer();
            if (v0 == null) {
                Intrinsics.throwNpe();
            }
            this.keepSprintValue.setValue(v0.getOnGround());
        }
        if (((String)this.blockModeValue.get()).equals("New")) {
            v1 = MinecraftInstance.mc.getThePlayer();
            if (v1 == null) {
                Intrinsics.throwNpe();
            }
            if (v1.getHeldItem() != null) {
                v2 = MinecraftInstance.mc.getThePlayer();
                if (v2 == null) {
                    Intrinsics.throwNpe();
                }
                v3 = v2.getHeldItem();
                if (v3 == null) {
                    Intrinsics.throwNpe();
                }
                if (v3.getItem() instanceof ItemSword) {
                    if (MinecraftInstance.mc2.field_71439_g.field_110158_av == -1) {
                        PacketUtils.INSTANCE.sendPacketNoEvent((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
                    } else if ((double)MinecraftInstance.mc2.field_71439_g.field_110158_av < 0.5 && MinecraftInstance.mc2.field_71439_g.field_110158_av != -1) {
                        PacketUtils.INSTANCE.sendPacketNoEvent((Packet)new CPacketPlayerTryUseItemOnBlock(new BlockPos(-1, -1, -1), EnumFacing.WEST, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                        v4 = MinecraftInstance.mc.getThePlayer();
                        if (v4 == null) {
                            Intrinsics.throwNpe();
                        }
                        v5 = v4.getSendQueue();
                        v6 = MinecraftInstance.mc.getThePlayer();
                        if (v6 == null) {
                            Intrinsics.throwNpe();
                        }
                        var2_2 = v6.getInventory().getCurrentItemInHand();
                        var3_4 = WEnumHand.MAIN_HAND;
                        var5_6 = v5;
                        var4_7 = false;
                        var6_8 = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(var3_4);
                        var5_6.addToSendQueue(var6_8);
                        v7 = MinecraftInstance.mc.getThePlayer();
                        if (v7 == null) {
                            Intrinsics.throwNpe();
                        }
                        v8 = v7.getSendQueue();
                        v9 = MinecraftInstance.mc.getThePlayer();
                        if (v9 == null) {
                            Intrinsics.throwNpe();
                        }
                        var2_2 = v9.getInventory().getCurrentItemInHand();
                        var3_4 = WEnumHand.OFF_HAND;
                        var5_6 = v8;
                        var4_7 = false;
                        var6_8 = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(var3_4);
                        var5_6.addToSendQueue(var6_8);
                    }
                }
            }
        }
        if (this.syncEntity != null) {
            v10 = this.syncEntity;
            if (v10 == null) {
                Intrinsics.throwNpe();
            }
            if (v10.isDead()) {
                ++KillAura4.killCounts;
                this.syncEntity = null;
            }
        }
        var2_2 = this;
        var3_5 = false;
        v11 = MinecraftInstance.mc.getThePlayer();
        if (v11 == null) {
            Intrinsics.throwNpe();
        }
        if (v11.isSpectator()) ** GOTO lbl-1000
        v12 = MinecraftInstance.mc.getThePlayer();
        if (v12 == null) {
            Intrinsics.throwNpe();
        }
        if (!KillAura4.access$isAlive((KillAura4)var2_2, v12) || LiquidBounce.INSTANCE.getModuleManager().get(Blink.class).getState() || LiquidBounce.INSTANCE.getModuleManager().get(FreeCam.class).getState()) lbl-1000:
        // 2 sources

        {
            v13 = true;
        } else {
            v13 = false;
        }
        if (v13) {
            this.target = null;
            this.currentTarget = null;
            this.hitable = false;
            this.stopBlocking();
            return;
        }
        if (((Boolean)this.noInventoryAttackValue.get()).booleanValue() && (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen()) || System.currentTimeMillis() - this.containerOpen < ((Number)this.noInventoryDelayValue.get()).longValue())) {
            this.target = null;
            this.currentTarget = null;
            this.hitable = false;
            if (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen())) {
                this.containerOpen = System.currentTimeMillis();
            }
            return;
        }
        if (this.target != null && this.currentTarget != null) {
            v14 = MinecraftInstance.mc.getThePlayer();
            if (v14 == null) {
                Intrinsics.throwNpe();
            }
            if (v14.getCooledAttackStrength(0.0f) >= ((Number)this.cooldownValue.get()).floatValue()) {
                while (this.clicks > 0) {
                    this.runAttack();
                    var2_3 = this.clicks;
                    this.clicks = var2_3 + -1;
                }
            }
        }
    }

    public final IntegerValue getMaxCPS() {
        return this.maxCPS;
    }

    public static final FloatValue access$getMaxPredictSize$p(KillAura4 killAura4) {
        return killAura4.maxPredictSize;
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

    private final float getMaxRange() {
        float f = ((Number)this.rangeValue.get()).floatValue();
        float f2 = ((Number)this.throughWallsRangeValue.get()).floatValue();
        boolean bl = false;
        return Math.max(f, f2);
    }

    @Override
    public String getTag() {
        return (String)this.targetModeValue.get();
    }

    public static final void access$setKillCounts$cp(int n) {
        killCounts = n;
    }

    public static final int access$getKillCounts$cp() {
        return killCounts;
    }

    public final IEntityLivingBase getTarget() {
        return this.target;
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

    public static final IExtractedFunctions access$getFunctions$p$s1046033730() {
        return MinecraftInstance.functions;
    }

    public final FloatValue getFovValue() {
        return this.fovValue;
    }

    public final boolean getBlockingStatus() {
        return this.blockingStatus;
    }

    @EventTarget
    public final void onEntityMove(EntityMovementEvent entityMovementEvent) {
        IEntity iEntity = entityMovementEvent.getMovedEntity();
        if (this.target == null || iEntity.equals(this.currentTarget) ^ true) {
            return;
        }
        this.updateHitable();
    }

    /*
     * Exception decompiling
     */
    public final void update() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl89 : RETURN - null : trying to set 0 previously set to 1
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

    private final void startBlocking(IEntity iEntity, boolean bl) {
        if (LiquidBounce.INSTANCE.getModuleManager().get(Animations.class).getState()) {
            boolean bl2;
            double d;
            Object object;
            boolean bl3;
            float f;
            float f2;
            float f3;
            IAxisAlignedBB iAxisAlignedBB;
            double d2;
            WVec3 wVec3;
            if (this.blockingStatus) {
                return;
            }
            if (bl) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity(iEntity, iEntity.getPositionVector()));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity(iEntity, ICPacketUseEntity.WAction.INTERACT));
            }
            if (StringsKt.equals((String)((String)this.blockModeValue.get()), (String)"B73", (boolean)true)) {
                IEntity iEntity2 = MinecraftInstance.mc.getRenderViewEntity();
                wVec3 = iEntity2 != null ? iEntity2.getPositionEyes(1.0f) : null;
                d2 = iEntity.getCollisionBorderSize();
                iAxisAlignedBB = iEntity.getEntityBoundingBox().expand(d2, d2, d2);
                Rotation rotation = RotationUtils.targetRotation;
                if (rotation == null) {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    float f4 = iEntityPlayerSP.getRotationYaw();
                    IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP2 == null) {
                        Intrinsics.throwNpe();
                    }
                    rotation = new Rotation(f4, iEntityPlayerSP2.getRotationPitch());
                }
                Rotation rotation2 = rotation;
                f3 = rotation2.component1();
                f2 = rotation2.component2();
                f = -f3 * ((float)Math.PI / 180) - (float)Math.PI;
                boolean bl4 = false;
                float f5 = (float)Math.cos(f);
                float f6 = -f3 * ((float)Math.PI / 180) - (float)Math.PI;
                boolean bl5 = false;
                f = (float)Math.sin(f6);
                float f7 = -f2 * ((float)Math.PI / 180);
                boolean bl6 = false;
                f6 = -((float)Math.cos(f7));
                float f8 = -f2 * ((float)Math.PI / 180);
                bl3 = false;
                f7 = (float)Math.sin(f8);
                double d3 = this.getMaxRange();
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                double d4 = PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, iEntity);
                boolean bl7 = false;
                double d5 = Math.min(d3, d4) + 1.0;
                WVec3 wVec32 = wVec3;
                if (wVec32 == null) {
                    Intrinsics.throwNpe();
                }
                object = wVec32;
                d4 = (double)(f * f6) * d5;
                double d6 = (double)f7 * d5;
                d = (double)(f5 * f6) * d5;
                bl2 = false;
                WVec3 wVec33 = new WVec3(((WVec3)object).getXCoord() + d4, ((WVec3)object).getYCoord() + d6, ((WVec3)object).getZCoord() + d);
                IMovingObjectPosition iMovingObjectPosition = iAxisAlignedBB.calculateIntercept(wVec3, wVec33);
                if (iMovingObjectPosition == null) {
                    return;
                }
                object = iMovingObjectPosition;
                WVec3 wVec34 = object.getHitVec();
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity(iEntity, new WVec3(wVec34.getXCoord() - iEntity.getPosX(), wVec34.getYCoord() - iEntity.getPosY(), wVec34.getZCoord() - iEntity.getPosZ())));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity(iEntity, ICPacketUseEntity.WAction.INTERACT));
            }
            if (StringsKt.equals((String)((String)this.blockModeValue.get()), (String)"Normal", (boolean)true)) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketTryUseItem(WEnumHand.MAIN_HAND));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketTryUseItem(WEnumHand.OFF_HAND));
            }
            if (StringsKt.equals((String)((String)this.blockModeValue.get()), (String)"Old", (boolean)true)) {
                IEntity iEntity3 = MinecraftInstance.mc.getRenderViewEntity();
                wVec3 = iEntity3 != null ? iEntity3.getPositionEyes(1.0f) : null;
                d2 = iEntity.getCollisionBorderSize();
                iAxisAlignedBB = iEntity.getEntityBoundingBox().expand(d2, d2, d2);
                Rotation rotation = RotationUtils.targetRotation;
                if (rotation == null) {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    float f9 = iEntityPlayerSP.getRotationYaw();
                    IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP3 == null) {
                        Intrinsics.throwNpe();
                    }
                    rotation = new Rotation(f9, iEntityPlayerSP3.getRotationPitch());
                }
                Rotation rotation3 = rotation;
                f3 = rotation3.component1();
                f2 = rotation3.component2();
                f = -f3 * ((float)Math.PI / 180) - (float)Math.PI;
                boolean bl8 = false;
                float f10 = (float)Math.cos(f);
                float f11 = -f3 * ((float)Math.PI / 180) - (float)Math.PI;
                boolean bl9 = false;
                f = (float)Math.sin(f11);
                float f12 = -f2 * ((float)Math.PI / 180);
                boolean bl10 = false;
                f11 = -((float)Math.cos(f12));
                float f13 = -f2 * ((float)Math.PI / 180);
                bl3 = false;
                f12 = (float)Math.sin(f13);
                double d7 = this.getMaxRange();
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                double d8 = PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, iEntity);
                boolean bl11 = false;
                double d9 = Math.min(d7, d8) + 1.0;
                WVec3 wVec35 = wVec3;
                if (wVec35 == null) {
                    Intrinsics.throwNpe();
                }
                object = wVec35;
                d8 = (double)(f * f11) * d9;
                double d10 = (double)f12 * d9;
                d = (double)(f10 * f11) * d9;
                bl2 = false;
                WVec3 wVec36 = new WVec3(((WVec3)object).getXCoord() + d8, ((WVec3)object).getYCoord() + d10, ((WVec3)object).getZCoord() + d);
                IMovingObjectPosition iMovingObjectPosition = iAxisAlignedBB.calculateIntercept(wVec3, wVec36);
                if (iMovingObjectPosition == null) {
                    return;
                }
                object = iMovingObjectPosition;
                WVec3 wVec37 = object.getHitVec();
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity(iEntity, new WVec3(wVec37.getXCoord() - iEntity.getPosX(), wVec37.getYCoord() - iEntity.getPosY(), wVec37.getZCoord() - iEntity.getPosZ())));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity(iEntity, ICPacketUseEntity.WAction.INTERACT));
                if (LiquidBounce.INSTANCE.getModuleManager().get(Animations.class).getState()) {
                    IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP4 == null) {
                        Intrinsics.throwNpe();
                    }
                    IItemStack iItemStack = iEntityPlayerSP4.getInventory().getCurrentItemInHand();
                    WEnumHand wEnumHand = WEnumHand.OFF_HAND;
                    IINetHandlerPlayClient iINetHandlerPlayClient2 = iINetHandlerPlayClient;
                    boolean bl12 = false;
                    IPacket iPacket = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(wEnumHand);
                    iINetHandlerPlayClient2.addToSendQueue(iPacket);
                } else {
                    IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP5 == null) {
                        Intrinsics.throwNpe();
                    }
                    IItemStack iItemStack = iEntityPlayerSP5.getInventory().getCurrentItemInHand();
                    WEnumHand wEnumHand = WEnumHand.MAIN_HAND;
                    IINetHandlerPlayClient iINetHandlerPlayClient3 = iINetHandlerPlayClient;
                    boolean bl13 = false;
                    IPacket iPacket = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(wEnumHand);
                    iINetHandlerPlayClient3.addToSendQueue(iPacket);
                    IINetHandlerPlayClient iINetHandlerPlayClient4 = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP6 == null) {
                        Intrinsics.throwNpe();
                    }
                    iItemStack = iEntityPlayerSP6.getInventory().getCurrentItemInHand();
                    wEnumHand = WEnumHand.OFF_HAND;
                    iINetHandlerPlayClient3 = iINetHandlerPlayClient4;
                    bl13 = false;
                    iPacket = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(wEnumHand);
                    iINetHandlerPlayClient3.addToSendQueue(iPacket);
                }
            }
        }
        this.blockingStatus = true;
    }

    public final IntegerValue getMinCPS() {
        return this.minCPS;
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

    public static final class Companion {
        public final void setKillCounts(int n) {
            KillAura4.access$setKillCounts$cp(n);
        }

        private Companion() {
        }

        public final int getKillCounts() {
            return KillAura4.access$getKillCounts$cp();
        }

        @JvmStatic
        public static void killCounts$annotations() {
        }

        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}

