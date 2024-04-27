/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.ranges.IntProgression
 *  kotlin.ranges.IntRange
 *  kotlin.ranges.RangesKt
 *  kotlin.text.StringsKt
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.effect.EntityLightningBolt
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.server.SPacketSpawnGlobalEntity
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.World
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
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntProgression;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import me.utils.render.ColorUtils2;
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
import net.ccbluex.liquidbounce.api.minecraft.util.IIChatComponent;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.world.IWorldSettings;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EntityMovementEvent;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.StrafeEvent;
import net.ccbluex.liquidbounce.event.TickEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.color.Gident;
import net.ccbluex.liquidbounce.features.module.modules.color.Rainbow;
import net.ccbluex.liquidbounce.features.module.modules.combat.Criticals;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura2;
import net.ccbluex.liquidbounce.features.module.modules.combat.NoFriends;
import net.ccbluex.liquidbounce.features.module.modules.player.Blink;
import net.ccbluex.liquidbounce.features.module.modules.render.FreeCam;
import net.ccbluex.liquidbounce.features.module.modules.render.OldHitting;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import net.ccbluex.liquidbounce.ui.font.GLUtils;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.BlendUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.PacketUtils;
import net.ccbluex.liquidbounce.utils.RaycastUtils;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.extensions.PlayerExtensionKt;
import net.ccbluex.liquidbounce.utils.render.AnimationUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

@ModuleInfo(name="KillAura2", description="Used with NoC03 FUCKu", category=ModuleCategory.COMBAT)
public final class KillAura2
extends Module {
    private long lastMS;
    private final IntegerValue maxCPS = new IntegerValue(this, "MaxCPS", 8, 1, 20){
        final KillAura2 this$0;

        static {
        }

        public void onChanged(Object object, Object object2) {
            this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
        }
        {
            this.this$0 = killAura2;
            super(string, n, n2, n3);
        }

        protected void onChanged(int n, int n2) {
            int n3 = ((Number)KillAura2.access$getMinCPS$p(this.this$0).get()).intValue();
            if (n3 > n2) {
                this.set((Object)n3);
            }
            KillAura2.access$setAttackDelay$p(this.this$0, TimeUtils.randomClickDelay(((Number)KillAura2.access$getMinCPS$p(this.this$0).get()).intValue(), ((Number)this.get()).intValue()));
        }
    };
    private final ListValue lightingModeValue;
    private final FloatValue cooldownValue;
    private final FloatValue maxPredictSize;
    private final FloatValue maxTurnSpeed;
    private final BoolValue autoBlockFacing;
    private final IntegerValue colorBlueValue;
    private final FloatValue saturationValue;
    private final IntegerValue colorAlphaValue;
    private final BoolValue aacValue;
    private final BoolValue delayedBlockValue;
    private final IntegerValue switchDelayValue;
    private final FloatValue throughWallsRangeValue;
    private final BoolValue hitableValue;
    private final IntegerValue circleAlpha;
    private final BoolValue keepSprintValue;
    private final BoolValue colorTeam;
    private final FloatValue rangeValue;
    private final IntegerValue minCPS = new IntegerValue(this, "MinCPS", 5, 1, 20){
        final KillAura2 this$0;
        {
            this.this$0 = killAura2;
            super(string, n, n2, n3);
        }

        protected void onChanged(int n, int n2) {
            int n3 = ((Number)KillAura2.access$getMaxCPS$p(this.this$0).get()).intValue();
            if (n3 < n2) {
                this.set((Object)n3);
            }
            KillAura2.access$setAttackDelay$p(this.this$0, TimeUtils.randomClickDelay(((Number)this.get()).intValue(), ((Number)KillAura2.access$getMaxCPS$p(this.this$0).get()).intValue()));
        }

        public void onChanged(Object object, Object object2) {
            this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
        }

        static {
        }
    };
    private final BoolValue afterAttackValue;
    private final BoolValue predictValue;
    private double espAnimation;
    private final IntegerValue circleGreen;
    private final FloatValue rangeSprintReducementValue;
    private final List prevTargetEntities;
    private final ListValue targetModeValue;
    private long attackDelay;
    private boolean hitable;
    private final FloatValue brightnessValue;
    private final IntegerValue circleAccuracy;
    private boolean isUp;
    private final BoolValue randomCenterValue;
    private final BoolValue fakeSharpValue;
    private final FloatValue jelloAlphaValue;
    private final BoolValue raycastValue;
    private final MSTimer switchTimer;
    public static final Companion Companion = new Companion(null);
    private final FloatValue jelloGradientHeightValue;
    private IEntityLivingBase syncEntity;
    private final FloatValue BlockRangeValue;
    private EntityLivingBase markEntity;
    private IEntityLivingBase target;
    private final IntegerValue hurtTimeValue = new IntegerValue("HurtTime", 10, 0, 10);
    private double progress;
    private final IntegerValue colorGreenValue;
    private final BoolValue raycastIgnoredValue;
    private final int blockKey;
    private final BoolValue stopSprintAir;
    private final BoolValue lightingValue;
    private IEntityLivingBase entity;
    private double yPos;
    private final IntegerValue colorRedValue;
    private final MSTimer attackTimer;
    private float al;
    private final IntegerValue circleBlue;
    private final FloatValue jelloWidthValue;
    private String displayText;
    private int clicks;
    private final BoolValue livingRaycastValue;
    private final BoolValue silentRotationValue;
    private final ListValue colorModeValue;
    private final IntegerValue noInventoryDelayValue;
    private IEntityLivingBase lastTarget;
    private final FloatValue minPredictSize;
    private IEntityLivingBase currentTarget;
    private final IntegerValue circleRed;
    private final BoolValue outborderValue;
    private final ListValue rotationStrafeValue;
    private double direction;
    private boolean blockingStatus;
    private long containerOpen;
    private final IntegerValue limitedMultiTargetsValue;
    private final ListValue markValue;
    private final FloatValue minTurnSpeed;
    private final ListValue autoBlockValue;
    private final BoolValue lightingSoundValue;
    private final FloatValue failRateValue;
    private final MSTimer markTimer;
    private final FloatValue jelloFadeSpeedValue;
    private final ListValue autoBlockPacketValue;
    private IAxisAlignedBB bb;
    private final BoolValue circleValue;
    private final BoolValue interactAutoBlockValue;
    private final BoolValue noInventoryAttackValue;
    private final ListValue rotations;
    private final ListValue priorityValue;
    private final BoolValue fakeSwingValue;
    private final BoolValue swingValue;
    private final IntegerValue blockRate;
    private static int killCounts;
    private final FloatValue fovValue;
    private long lastDeltaMS;
    private final ListValue vanillamode;

    private final void esp(IEntityLivingBase iEntityLivingBase, float f, float f2) {
        GL11.glPushMatrix();
        GL11.glDisable((int)3553);
        GLUtils.startSmooth();
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glLineWidth((float)1.0f);
        GL11.glBegin((int)3);
        double d = iEntityLivingBase.getLastTickPosX() + (iEntityLivingBase.getPosX() - iEntityLivingBase.getLastTickPosX()) * (double)f - MinecraftInstance.mc.getRenderManager().getViewerPosX();
        double d2 = iEntityLivingBase.getLastTickPosY() + (iEntityLivingBase.getPosY() - iEntityLivingBase.getLastTickPosY()) * (double)f - MinecraftInstance.mc.getRenderManager().getViewerPosY();
        double d3 = iEntityLivingBase.getLastTickPosZ() + (iEntityLivingBase.getPosZ() - iEntityLivingBase.getLastTickPosZ()) * (double)f - MinecraftInstance.mc.getRenderManager().getViewerPosZ();
        int n = 360;
        for (int i = 0; i <= n; ++i) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            double d4 = (double)i / 50.0 * 1.75;
            double d5 = (double)iEntityPlayerSP.getTicksExisted() / 70.0;
            boolean bl = false;
            double d6 = Math.sin(d4);
            int n2 = Color.HSBtoRGB((float)(d5 + d6) % 1.0f, 0.7f, 1.0f);
            Color color = new Color(n2);
            GL11.glColor3f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f));
            d4 = (double)i * (Math.PI * 2) / 45.0;
            d5 = f2;
            double d7 = d;
            bl = false;
            d6 = Math.cos(d4);
            double d8 = d7 + d5 * d6;
            d4 = (double)i * (Math.PI * 2) / 45.0;
            double d9 = f2;
            d6 = d3;
            d5 = d2 + this.espAnimation;
            d7 = d8;
            bl = false;
            double d10 = Math.sin(d4);
            GL11.glVertex3d((double)d7, (double)d5, (double)(d6 + d9 * d10));
        }
        GL11.glEnd();
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2929);
        GLUtils.endSmooth();
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
    }

    public static final FloatValue access$getBrightnessValue$p(KillAura2 killAura2) {
        return killAura2.brightnessValue;
    }

    @Override
    public String getTag() {
        return (String)this.targetModeValue.get();
    }

    public static final int access$getKillCounts$cp() {
        return killCounts;
    }

    public static final BoolValue access$getRaycastIgnoredValue$p(KillAura2 killAura2) {
        return killAura2.raycastIgnoredValue;
    }

    private final void drawESP(IEntityLivingBase iEntityLivingBase, int n, Render3DEvent render3DEvent) {
        double d = iEntityLivingBase.getLastTickPosX() + (iEntityLivingBase.getPosX() - iEntityLivingBase.getLastTickPosX()) * (double)render3DEvent.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getRenderPosX();
        double d2 = iEntityLivingBase.getLastTickPosY() + (iEntityLivingBase.getPosY() - iEntityLivingBase.getLastTickPosY()) * (double)render3DEvent.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getRenderPosY();
        double d3 = iEntityLivingBase.getLastTickPosZ() + (iEntityLivingBase.getPosZ() - iEntityLivingBase.getLastTickPosZ()) * (double)render3DEvent.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getRenderPosZ();
        float f = 0.15f;
        int n2 = 4;
        GL11.glPushMatrix();
        GL11.glTranslated((double)d, (double)(d2 + (double)2), (double)d3);
        GL11.glRotatef((float)(-iEntityLivingBase.getWidth()), (float)0.0f, (float)1.0f, (float)0.0f);
        RenderUtils.glColor1(n);
        RenderUtils.enableSmoothLine(1.5f);
        Cylinder cylinder = new Cylinder();
        GL11.glRotatef((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        cylinder.setDrawStyle(100012);
        RenderUtils.glColor(new Color(80, 255, 80, 200));
        cylinder.draw(0.0f, f, 0.3f, n2, 1);
        cylinder.setDrawStyle(100012);
        GL11.glTranslated((double)0.0, (double)0.0, (double)0.3);
        cylinder.draw(f, 0.0f, 0.3f, n2, 1);
        GL11.glRotatef((float)90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        cylinder.setDrawStyle(100011);
        GL11.glTranslated((double)0.0, (double)0.0, (double)-0.3);
        RenderUtils.glColor1(n);
        cylinder.draw(0.0f, f, 0.3f, n2, 1);
        cylinder.setDrawStyle(100011);
        GL11.glTranslated((double)0.0, (double)0.0, (double)0.3);
        cylinder.draw(f, 0.0f, 0.3f, n2, 1);
        RenderUtils.disableSmoothLine();
        GL11.glPopMatrix();
    }

    @Override
    public void onDisable() {
        this.target = null;
        this.currentTarget = null;
        this.lastTarget = null;
        this.hitable = false;
        this.prevTargetEntities.clear();
        this.attackTimer.reset();
        this.clicks = 0;
        this.stopBlocking();
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

    public final int getBlockKey() {
        return this.blockKey;
    }

    private final void attackEntity(IEntityLivingBase iEntityLivingBase) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (!StringsKt.equals((String)((String)this.autoBlockPacketValue.get()), (String)"Vanilla", (boolean)true)) {
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP3.isBlocking() || this.blockingStatus) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.Companion.getORIGIN(), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
                if (((Boolean)this.afterAttackValue.get()).booleanValue()) {
                    this.blockingStatus = false;
                }
            }
        }
        LiquidBounce.INSTANCE.getEventManager().callEvent(new AttackEvent(iEntityLivingBase));
        if (((Boolean)this.swingValue.get()).booleanValue()) {
            // empty if block
        }
        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity((IEntity)iEntityLivingBase, ICPacketUseEntity.WAction.ATTACK));
        if (((Boolean)this.swingValue.get()).booleanValue()) {
            iEntityPlayerSP2.swingItem();
        }
        if (((Boolean)this.keepSprintValue.get()).booleanValue()) {
            if (!(!(iEntityPlayerSP2.getFallDistance() > 0.0f) || iEntityPlayerSP2.getOnGround() || iEntityPlayerSP2.isOnLadder() || iEntityPlayerSP2.isInWater() || iEntityPlayerSP2.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.BLINDNESS)) || iEntityPlayerSP2.isRiding())) {
                iEntityPlayerSP2.onCriticalHit(iEntityLivingBase);
            }
            if (KillAura2.access$getFunctions$p$s1046033730().getModifierForCreature(iEntityPlayerSP2.getHeldItem(), iEntityLivingBase.getCreatureAttribute()) > 0.0f) {
                iEntityPlayerSP2.onEnchantmentCritical(iEntityLivingBase);
            }
        } else if (MinecraftInstance.mc.getPlayerController().getCurrentGameType() != IWorldSettings.WGameType.SPECTATOR) {
            iEntityPlayerSP2.attackTargetEntityWithCurrentItem(iEntityLivingBase);
        }
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(Criticals.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.Criticals");
        }
        Criticals criticals = (Criticals)module;
        int n = 2;
        for (int i = 0; i <= n; ++i) {
            if (iEntityPlayerSP2.getFallDistance() > 0.0f && !iEntityPlayerSP2.getOnGround() && !iEntityPlayerSP2.isOnLadder() && !iEntityPlayerSP2.isInWater() && !iEntityPlayerSP2.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.BLINDNESS)) && iEntityPlayerSP2.getRidingEntity() == null || criticals.getState() && criticals.getMsTimer().hasTimePassed(((Number)criticals.getDelayValue().get()).intValue()) && !iEntityPlayerSP2.isInWater() && !iEntityPlayerSP2.isInLava() && !iEntityPlayerSP2.isInWeb()) {
                IEntityLivingBase iEntityLivingBase2 = this.target;
                if (iEntityLivingBase2 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP2.onCriticalHit(iEntityLivingBase2);
            }
            IExtractedFunctions iExtractedFunctions = KillAura2.access$getFunctions$p$s1046033730();
            IItemStack iItemStack = iEntityPlayerSP2.getHeldItem();
            IEntityLivingBase iEntityLivingBase3 = this.target;
            if (iEntityLivingBase3 == null) {
                Intrinsics.throwNpe();
            }
            if (!(iExtractedFunctions.getModifierForCreature(iItemStack, iEntityLivingBase3.getCreatureAttribute()) > 0.0f) && !((Boolean)this.fakeSharpValue.get()).booleanValue()) continue;
            IEntityLivingBase iEntityLivingBase4 = this.target;
            if (iEntityLivingBase4 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP2.onEnchantmentCritical(iEntityLivingBase4);
        }
        IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP4 == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP4.isBlocking() || !StringsKt.equals((String)((String)this.autoBlockValue.get()), (String)"off", (boolean)true) && this == null) {
            if (((Boolean)this.delayedBlockValue.get()).booleanValue()) {
                return;
            }
            if (((Number)this.blockRate.get()).intValue() <= 0 || new Random().nextInt(100) > ((Number)this.blockRate.get()).intValue()) {
                return;
            }
            this.startBlocking(iEntityLivingBase, (Boolean)this.interactAutoBlockValue.get());
        }
        iEntityPlayerSP2.resetCooldown();
    }

    public final IEntityLivingBase getSyncEntity() {
        return this.syncEntity;
    }

    public static final FloatValue access$getMinPredictSize$p(KillAura2 killAura2) {
        return killAura2.minPredictSize;
    }

    public KillAura2() {
        List list;
        this.cooldownValue = new FloatValue("Cooldown", 1.0f, 0.0f, 1.0f);
        this.rangeValue = new FloatValue("Range", 3.7f, 1.0f, 8.0f);
        this.throughWallsRangeValue = new FloatValue("ThroughWallsRange", 3.0f, 0.0f, 8.0f);
        this.rangeSprintReducementValue = new FloatValue("RangeSprintReducement", 0.0f, 0.0f, 0.4f);
        this.priorityValue = new ListValue("Priority", new String[]{"Health", "Distance", "Direction", "LivingTime", "HurtResitanTime"}, "Distance");
        this.targetModeValue = new ListValue("TargetMode", new String[]{"Single", "Switch", "Multi"}, "Switch");
        this.swingValue = new BoolValue("Swing", true);
        this.keepSprintValue = new BoolValue("KeepSprint", true);
        this.stopSprintAir = new BoolValue("StopSprintOnAir", true);
        this.autoBlockValue = new ListValue("AutoBlock", new String[]{"HuaYuTing", "AllTime", "Range", "Off"}, "Off");
        this.BlockRangeValue = new FloatValue("BlockRange", 3.0f, 0.0f, 8.0f);
        this.autoBlockPacketValue = new ListValue("AutoBlockPacket", new String[]{"Vanilla", "Fake", "Mouse", "GameSettings", "UseItem"}, "Simple");
        this.vanillamode = new ListValue("VanillaMode", new String[]{"TryUseItem", "UseItem", "CPacketPlayerBlockPlacement"}, "TryUseItem");
        this.interactAutoBlockValue = new BoolValue("InteractAutoBlock", true);
        this.delayedBlockValue = new BoolValue("AutoBlock-AfterTck", false);
        this.afterAttackValue = new BoolValue("AutoBlock-AfterAttack", false);
        this.autoBlockFacing = new BoolValue("AutoBlockFacing", false);
        this.blockRate = new IntegerValue("BlockRate", 100, 1, 100);
        this.raycastValue = new BoolValue("RayCast", true);
        this.raycastIgnoredValue = new BoolValue("RayCastIgnored", false);
        this.livingRaycastValue = new BoolValue("LivingRayCast", true);
        this.aacValue = new BoolValue("AAC", false);
        this.maxTurnSpeed = new FloatValue(this, "MaxTurnSpeed", 180.0f, 0.0f, 180.0f){
            final KillAura2 this$0;
            {
                this.this$0 = killAura2;
                super(string, f, f2, f3);
            }

            static {
            }

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).floatValue(), ((Number)object2).floatValue());
            }

            protected void onChanged(float f, float f2) {
                float f3 = ((Number)KillAura2.access$getMinTurnSpeed$p(this.this$0).get()).floatValue();
                if (f3 > f2) {
                    this.set((Object)Float.valueOf(f3));
                }
            }
        };
        this.minTurnSpeed = new FloatValue(this, "MinTurnSpeed", 180.0f, 0.0f, 180.0f){
            final KillAura2 this$0;
            {
                this.this$0 = killAura2;
                super(string, f, f2, f3);
            }

            static {
            }

            protected void onChanged(float f, float f2) {
                float f3 = ((Number)KillAura2.access$getMaxTurnSpeed$p(this.this$0).get()).floatValue();
                if (f3 < f2) {
                    this.set((Object)Float.valueOf(f3));
                }
            }

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).floatValue(), ((Number)object2).floatValue());
            }
        };
        this.lightingValue = new BoolValue("Lighting", false);
        this.lightingModeValue = new ListValue("Lighting-Mode", new String[]{"Dead", "Attack"}, "Dead");
        this.lightingSoundValue = new BoolValue("Lighting-Sound", true);
        this.randomCenterValue = new BoolValue("RandomCenter", true);
        this.rotations = new ListValue("RotationMode", new String[]{"None", "New", "Liquidbounce", "BackTrack", "Test1", "Test2", "HytRotation"}, "New");
        this.outborderValue = new BoolValue("Outborder", false);
        this.silentRotationValue = new BoolValue("SilentRotation", true);
        this.rotationStrafeValue = new ListValue("Strafe", new String[]{"Off", "Strict", "Silent"}, "Off");
        this.fovValue = new FloatValue("FOV", 180.0f, 0.0f, 180.0f);
        this.hitableValue = new BoolValue("AlwaysHitable", true);
        this.switchDelayValue = new IntegerValue("SwitchDelay", 300, 1, 2000);
        this.predictValue = new BoolValue("Predict", true);
        this.maxPredictSize = new FloatValue(this, "MaxPredictSize", 1.0f, 0.1f, 5.0f){
            final KillAura2 this$0;

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).floatValue(), ((Number)object2).floatValue());
            }

            protected void onChanged(float f, float f2) {
                float f3 = ((Number)KillAura2.access$getMinPredictSize$p(this.this$0).get()).floatValue();
                if (f3 > f2) {
                    this.set((Object)Float.valueOf(f3));
                }
            }
            {
                this.this$0 = killAura2;
                super(string, f, f2, f3);
            }

            static {
            }
        };
        this.minPredictSize = new FloatValue(this, "MinPredictSize", 1.0f, 0.1f, 5.0f){
            final KillAura2 this$0;
            {
                this.this$0 = killAura2;
                super(string, f, f2, f3);
            }

            static {
            }

            protected void onChanged(float f, float f2) {
                float f3 = ((Number)KillAura2.access$getMaxPredictSize$p(this.this$0).get()).floatValue();
                if (f3 < f2) {
                    this.set((Object)Float.valueOf(f3));
                }
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
        this.markValue = new ListValue("Mark", new String[]{"Liquid", "FDP", "Block", "Jello", "Plat", "Red", "Sims", "None"}, "FDP");
        this.colorModeValue = new ListValue("JelloColor", new String[]{"Custom", "Rainbow", "Sky", "LiquidSlowly", "Fade", "Health", "Gident"}, "Custom");
        this.colorRedValue = new IntegerValue("JelloRed", 255, 0, 255);
        this.colorGreenValue = new IntegerValue("JelloGreen", 255, 0, 255);
        this.colorBlueValue = new IntegerValue("JelloBlue", 255, 0, 255);
        this.colorAlphaValue = new IntegerValue("JelloAlpha", 255, 0, 255);
        this.saturationValue = new FloatValue("Saturation", 1.0f, 0.0f, 1.0f);
        this.brightnessValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f);
        this.colorTeam = new BoolValue("JelloTeam", false);
        this.jelloAlphaValue = new FloatValue("JelloEndAlphaPercent", 0.4f, 0.0f, 1.0f);
        this.jelloWidthValue = new FloatValue("JelloCircleWidth", 3.0f, 0.01f, 5.0f);
        this.jelloGradientHeightValue = new FloatValue("JelloGradientHeight", 3.0f, 1.0f, 8.0f);
        this.jelloFadeSpeedValue = new FloatValue("JelloFadeSpeed", 0.1f, 0.01f, 0.5f);
        this.fakeSharpValue = new BoolValue("FakeSharp", true);
        this.circleValue = new BoolValue("Circle", true);
        this.circleRed = new IntegerValue("CircleRed", 255, 0, 255);
        this.circleGreen = new IntegerValue("CircleGreen", 255, 0, 255);
        this.circleBlue = new IntegerValue("CircleBlue", 255, 0, 255);
        this.circleAlpha = new IntegerValue("CircleAlpha", 255, 0, 255);
        this.circleAccuracy = new IntegerValue("CircleAccuracy", 15, 0, 60);
        this.blockKey = MinecraftInstance.mc.getGameSettings().getKeyBindUseItem().getKeyCode();
        this.switchTimer = new MSTimer();
        KillAura2 killAura2 = this;
        boolean bl = false;
        killAura2.prevTargetEntities = list = (List)new ArrayList();
        this.direction = 1.0;
        this.lastMS = System.currentTimeMillis();
        this.attackTimer = new MSTimer();
        this.markTimer = new MSTimer();
        this.containerOpen = -1L;
        this.displayText = "";
        this.isUp = true;
    }

    public static final IntegerValue access$getMinCPS$p(KillAura2 killAura2) {
        return killAura2.minCPS;
    }

    public static final IntegerValue access$getColorRedValue$p(KillAura2 killAura2) {
        return killAura2.colorRedValue;
    }

    @EventTarget
    public final void onMotion(MotionEvent motionEvent) {
        if (((Boolean)this.stopSprintAir.get()).booleanValue()) {
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
            if (this.target == null) {
                return;
            }
            if (this.currentTarget == null) {
                return;
            }
            this.updateHitable();
            if (!StringsKt.equals((String)((String)this.autoBlockValue.get()), (String)"off", (boolean)true) && ((Boolean)this.delayedBlockValue.get()).booleanValue() && this == null) {
                IEntityLivingBase iEntityLivingBase = this.currentTarget;
                if (iEntityLivingBase == null) {
                    Intrinsics.throwNpe();
                }
                this.startBlocking(iEntityLivingBase, (Boolean)this.interactAutoBlockValue.get());
            }
            return;
        }
        if (StringsKt.equals((String)((String)this.rotationStrafeValue.get()), (String)"Off", (boolean)true)) {
            this.update();
        }
    }

    private final void stopBlocking() {
        if (this.blockingStatus) {
            if (StringsKt.equals((String)((String)this.autoBlockPacketValue.get()), (String)"Vanilla", (boolean)true)) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, MovementUtils.isMoving() ? new WBlockPos(-1, -1, -1) : WBlockPos.Companion.getORIGIN(), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
            }
            if (StringsKt.equals((String)((String)this.autoBlockPacketValue.get()), (String)"UseItem", (boolean)true)) {
                KeyBinding.func_74510_a((int)MinecraftInstance.mc.getGameSettings().getKeyBindUseItem().getKeyCode(), (boolean)false);
            }
            if (StringsKt.equals((String)((String)this.autoBlockPacketValue.get()), (String)"GameSettings", (boolean)true)) {
                MinecraftInstance.mc.getGameSettings().getKeyBindUseItem().setPressed(false);
            }
            if (StringsKt.equals((String)((String)this.autoBlockPacketValue.get()), (String)"Mouse", (boolean)true)) {
                new Robot().mouseRelease(4096);
            }
            if (StringsKt.equals((String)((String)this.autoBlockPacketValue.get()), (String)"Vanilla", (boolean)true)) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.Companion.getORIGIN(), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
            }
            this.blockingStatus = false;
        }
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

    public static final BoolValue access$getAacValue$p(KillAura2 killAura2) {
        return killAura2.aacValue;
    }

    public static final void setKillCounts(int n) {
        Companion companion = Companion;
        killCounts = n;
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onRender3D(Render3DEvent var1_1) {
        var2_2 = onRender3D.1.INSTANCE;
        var3_3 = onRender3D.2.INSTANCE;
        var4_4 = onRender3D.3.INSTANCE;
        var5_5 = new Function1(this){
            final KillAura2 this$0;

            public final Color invoke(@Nullable IEntityLivingBase iEntityLivingBase) {
                Color color;
                int[] nArray = new int[]{0};
                if (iEntityLivingBase instanceof EntityLivingBase) {
                    Object object = iEntityLivingBase;
                    if (StringsKt.equals((String)((String)KillAura2.access$getColorModeValue$p(this.this$0).get()), (String)"Health", (boolean)true)) {
                        return BlendUtils.getHealthColor(object.getHealth(), object.getMaxHealth());
                    }
                    if (((Boolean)KillAura2.access$getColorTeam$p(this.this$0).get()).booleanValue()) {
                        IIChatComponent iIChatComponent = object.getDisplayName();
                        if (iIChatComponent == null) {
                            Intrinsics.throwNpe();
                        }
                        String string = iIChatComponent.getFormattedText();
                        int n = 0;
                        String string2 = string;
                        if (string2 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        char[] cArray = string2.toCharArray();
                        int n2 = Integer.MAX_VALUE;
                        int n3 = cArray.length;
                        for (n = 0; n < n3; ++n) {
                            int n4;
                            if (cArray[n] != '\u00a7' || n + 1 >= cArray.length || (n4 = GameFontRenderer.Companion.getColorIndex(cArray[n + 1])) < 0 || n4 > 15) continue;
                            n2 = ColorUtils.hexColors[n4];
                            break;
                        }
                        return new Color(n2);
                    }
                }
                switch ((String)KillAura2.access$getColorModeValue$p(this.this$0).get()) {
                    case "Gident": {
                        color = RenderUtils.getGradientOffset(new Color(((Number)Gident.redValue.get()).intValue(), ((Number)Gident.greenValue.get()).intValue(), ((Number)Gident.blueValue.get()).intValue()), new Color(((Number)Gident.redValue2.get()).intValue(), ((Number)Gident.greenValue2.get()).intValue(), ((Number)Gident.blueValue2.get()).intValue()), Math.abs((double)System.currentTimeMillis() / (double)((Number)Gident.gidentspeed.get()).intValue()));
                        break;
                    }
                    case "Custom": {
                        color = new Color(((Number)KillAura2.access$getColorRedValue$p(this.this$0).get()).intValue(), ((Number)KillAura2.access$getColorGreenValue$p(this.this$0).get()).intValue(), ((Number)KillAura2.access$getColorBlueValue$p(this.this$0).get()).intValue());
                        break;
                    }
                    case "Rainbow": {
                        color = ColorUtils2.hslRainbow$default(nArray[0] * 100 + 1, 0.0f, 0.0f, 100 * ((Number)Rainbow.rainbowSpeed.get()).intValue(), 0, 0.0f, 0.0f, 118, null);
                        break;
                    }
                    case "Sky": {
                        color = RenderUtils.skyRainbow(0, ((Number)KillAura2.access$getSaturationValue$p(this.this$0).get()).floatValue(), ((Number)KillAura2.access$getBrightnessValue$p(this.this$0).get()).floatValue());
                        break;
                    }
                    case "LiquidSlowly": {
                        color = ColorUtils.LiquidSlowly(System.nanoTime(), 0, ((Number)KillAura2.access$getSaturationValue$p(this.this$0).get()).floatValue(), ((Number)KillAura2.access$getBrightnessValue$p(this.this$0).get()).floatValue());
                        break;
                    }
                    default: {
                        color = ColorUtils.fade(new Color(((Number)KillAura2.access$getColorRedValue$p(this.this$0).get()).intValue(), ((Number)KillAura2.access$getColorGreenValue$p(this.this$0).get()).intValue(), ((Number)KillAura2.access$getColorBlueValue$p(this.this$0).get()).intValue()), 0, 100);
                    }
                }
                return color;
            }

            public Object invoke(Object object) {
                return this.invoke((IEntityLivingBase)object);
            }
            {
                this.this$0 = killAura2;
                super(1);
            }

            static {
            }
        };
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
            var9_6 = 0;
            v17 = RangesKt.step((IntProgression)((IntProgression)new IntRange(var9_6, 360)), (int)(61 - ((Number)this.circleAccuracy.get()).intValue()));
            var6_11 = v17.getFirst();
            var7_15 = v17.getLast();
            var8_19 = v17.getStep();
            v18 = var6_11;
            v19 = var7_15;
            if (var8_19 >= 0 ? v18 <= v19 : v18 >= v19) {
                while (true) {
                    var9_7 = (double)var6_11 * 3.141592653589793 / 180.0;
                    var11_23 = false;
                    v20 = (float)Math.cos(var9_7) * ((Number)this.rangeValue.get()).floatValue();
                    var9_7 = (double)var6_11 * 3.141592653589793 / 180.0;
                    var34_20 = v20;
                    var11_23 = false;
                    var35_22 = Math.sin(var9_7);
                    GL11.glVertex2f((float)var34_20, (float)((float)var35_22 * ((Number)this.rangeValue.get()).floatValue()));
                    if (var6_11 == var7_15) break;
                    var6_11 += var8_19;
                }
            }
            var6_12 = 6.283185307179586;
            var8_19 = 0;
            v21 = (float)Math.cos(var6_12) * ((Number)this.rangeValue.get()).floatValue();
            var6_12 = 6.283185307179586;
            var34_20 = v21;
            var8_19 = 0;
            var35_22 = Math.sin(var6_12);
            GL11.glVertex2f((float)var34_20, (float)((float)var35_22 * ((Number)this.rangeValue.get()).floatValue()));
            GL11.glEnd();
            GL11.glDisable((int)3042);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glDisable((int)2848);
            GL11.glPopMatrix();
        }
        var6_13 = this;
        var7_15 = 0;
        v22 = MinecraftInstance.mc.getThePlayer();
        if (v22 == null) {
            Intrinsics.throwNpe();
        }
        if (v22.isSpectator()) ** GOTO lbl-1000
        v23 = MinecraftInstance.mc.getThePlayer();
        if (v23 == null) {
            Intrinsics.throwNpe();
        }
        if (!KillAura2.access$isAlive((KillAura2)var6_13, v23) || LiquidBounce.INSTANCE.getModuleManager().get(Blink.class).getState() || LiquidBounce.INSTANCE.getModuleManager().get(FreeCam.class).getState()) lbl-1000:
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
        var6_13 = (String)this.markValue.get();
        var7_15 = 0;
        v25 = var6_13;
        if (v25 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        var6_13 = v25.toLowerCase();
        tmp = -1;
        switch (var6_13.hashCode()) {
            case 112785: {
                if (!var6_13.equals("red")) break;
                tmp = 1;
                break;
            }
            case 101009364: {
                if (!var6_13.equals("jello")) break;
                tmp = 2;
                break;
            }
            case 101234: {
                if (!var6_13.equals("fdp")) break;
                tmp = 3;
                break;
            }
            case -1102567108: {
                if (!var6_13.equals("liquid")) break;
                tmp = 4;
                break;
            }
            case 3530364: {
                if (!var6_13.equals("sims")) break;
                tmp = 5;
                break;
            }
            case 93832333: {
                if (!var6_13.equals("block")) break;
                tmp = 6;
                break;
            }
            case 3443503: {
                if (!var6_13.equals("plat")) break;
                tmp = 7;
                break;
            }
        }
        switch (tmp) {
            case 4: {
                v26 = this.target;
                if (v26 == null) {
                    Intrinsics.throwNpe();
                }
                v27 = v26;
                v28 = this.target;
                if (v28 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawPlatform(v27, v28.getHurtTime() <= 0 ? new Color(37, 126, 255, 170) : new Color(255, 0, 0, 170));
                break;
            }
            case 7: {
                v29 = this.target;
                if (v29 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawPlatform(v29, this.hitable != false ? new Color(37, 126, 255, 70) : new Color(255, 0, 0, 70));
                break;
            }
            case 6: {
                v30 = this.target;
                if (v30 == null) {
                    Intrinsics.throwNpe();
                }
                var7_16 = v30.getEntityBoundingBox();
                v31 = this.target;
                if (v31 == null) {
                    Intrinsics.throwNpe();
                }
                v31.setEntityBoundingBox(var7_16.expand(0.2, 0.2, 0.2));
                v32 = this.target;
                if (v32 == null) {
                    Intrinsics.throwNpe();
                }
                v33 = v32;
                v34 = this.target;
                if (v34 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawEntityBox(v33, v34.getHurtTime() <= 0 ? Color.GREEN : Color.RED, true);
                v35 = this.target;
                if (v35 == null) {
                    Intrinsics.throwNpe();
                }
                v35.setEntityBoundingBox(var7_16);
                break;
            }
            case 1: {
                v36 = this.target;
                if (v36 == null) {
                    Intrinsics.throwNpe();
                }
                v37 = v36;
                v38 = this.target;
                if (v38 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawPlatform(v37, v38.getHurtTime() <= 0 ? new Color(255, 255, 255, 255) : new Color(124, 215, 255, 255));
                break;
            }
            case 5: {
                var7_17 = 0.15f;
                var8_19 = 4;
                GL11.glPushMatrix();
                v39 = this.target;
                if (v39 == null) {
                    Intrinsics.throwNpe();
                }
                v40 = v39.getLastTickPosX();
                v41 = this.target;
                if (v41 == null) {
                    Intrinsics.throwNpe();
                }
                v42 = v41.getPosX();
                v43 = this.target;
                if (v43 == null) {
                    Intrinsics.throwNpe();
                }
                v44 = v40 + (v42 - v43.getLastTickPosX()) * (double)var1_1.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosX();
                v45 = this.target;
                if (v45 == null) {
                    Intrinsics.throwNpe();
                }
                v46 = v45.getLastTickPosY();
                v47 = this.target;
                if (v47 == null) {
                    Intrinsics.throwNpe();
                }
                v48 = v47.getPosY();
                v49 = this.target;
                if (v49 == null) {
                    Intrinsics.throwNpe();
                }
                v50 = v46 + (v48 - v49.getLastTickPosY()) * (double)var1_1.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosY();
                v51 = this.target;
                if (v51 == null) {
                    Intrinsics.throwNpe();
                }
                v52 = v50 + (double)v51.getHeight() * 1.1;
                v53 = this.target;
                if (v53 == null) {
                    Intrinsics.throwNpe();
                }
                v54 = v53.getLastTickPosZ();
                v55 = this.target;
                if (v55 == null) {
                    Intrinsics.throwNpe();
                }
                v56 = v55.getPosZ();
                v57 = this.target;
                if (v57 == null) {
                    Intrinsics.throwNpe();
                }
                GL11.glTranslated((double)v44, (double)v52, (double)(v54 + (v56 - v57.getLastTickPosZ()) * (double)var1_1.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosZ()));
                v58 = this.target;
                if (v58 == null) {
                    Intrinsics.throwNpe();
                }
                GL11.glRotatef((float)(-v58.getWidth()), (float)0.0f, (float)1.0f, (float)0.0f);
                v59 = MinecraftInstance.mc.getThePlayer();
                if (v59 == null) {
                    Intrinsics.throwNpe();
                }
                GL11.glRotatef((float)(((float)v59.getTicksExisted() + MinecraftInstance.mc.getTimer().getRenderPartialTicks()) * (float)5), (float)0.0f, (float)1.0f, (float)0.0f);
                v60 = this.target;
                if (v60 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.glColor(v60.getHurtTime() <= 0 ? new Color(80, 255, 80) : new Color(255, 0, 0));
                RenderUtils.enableSmoothLine(1.5f);
                var9_8 = new Cylinder();
                GL11.glRotatef((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                var9_8.draw(0.0f, var7_17, 0.3f, var8_19, 1);
                var9_8.setDrawStyle(100012);
                GL11.glTranslated((double)0.0, (double)0.0, (double)0.3);
                var9_8.draw(var7_17, 0.0f, 0.3f, var8_19, 1);
                GL11.glRotatef((float)90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                GL11.glTranslated((double)0.0, (double)0.0, (double)-0.3);
                var9_8.draw(0.0f, var7_17, 0.3f, var8_19, 1);
                GL11.glTranslated((double)0.0, (double)0.0, (double)0.3);
                var9_8.draw(var7_17, 0.0f, 0.3f, var8_19, 1);
                RenderUtils.disableSmoothLine();
                GL11.glPopMatrix();
                break;
            }
            case 3: {
                var7_15 = (int)(System.currentTimeMillis() % (long)1500);
                var8_19 = var7_15 > 750 ? 1 : 0;
                var9_9 = (double)var7_15 / 750.0;
                var9_9 = var8_19 == 0 ? (double)true - var9_9 : (var9_9 -= (double)true);
                var9_9 = EaseUtils.INSTANCE.easeInOutQuad(var9_9);
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
                v61 = this.target;
                if (v61 == null) {
                    Intrinsics.throwNpe();
                }
                var11_24 = v61.getEntityBoundingBox();
                var12_25 = var11_24.getMaxX() - var11_24.getMinX() + 0.3;
                var14_27 = var11_24.getMaxY() - var11_24.getMinY();
                v62 = this.target;
                if (v62 == null) {
                    Intrinsics.throwNpe();
                }
                v63 = v62.getLastTickPosX();
                v64 = this.target;
                if (v64 == null) {
                    Intrinsics.throwNpe();
                }
                v65 = v64.getPosX();
                v66 = this.target;
                if (v66 == null) {
                    Intrinsics.throwNpe();
                }
                var16_29 = v63 + (v65 - v66.getLastTickPosX()) * (double)var1_1.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosX();
                v67 = this.target;
                if (v67 == null) {
                    Intrinsics.throwNpe();
                }
                v68 = v67.getLastTickPosY();
                v69 = this.target;
                if (v69 == null) {
                    Intrinsics.throwNpe();
                }
                v70 = v69.getPosY();
                v71 = this.target;
                if (v71 == null) {
                    Intrinsics.throwNpe();
                }
                var18_31 = v68 + (v70 - v71.getLastTickPosY()) * (double)var1_1.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosY() + var14_27 * var9_9;
                v72 = this.target;
                if (v72 == null) {
                    Intrinsics.throwNpe();
                }
                v73 = v72.getLastTickPosZ();
                v74 = this.target;
                if (v74 == null) {
                    Intrinsics.throwNpe();
                }
                v75 = v74.getPosZ();
                v76 = this.target;
                if (v76 == null) {
                    Intrinsics.throwNpe();
                }
                var20_33 = v73 + (v75 - v76.getLastTickPosZ()) * (double)var1_1.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosZ();
                GL11.glLineWidth((float)((float)(var12_25 * (double)5.0f)));
                GL11.glBegin((int)3);
                var23_37 = 360;
                for (var22_35 = 0; var22_35 <= var23_37; ++var22_35) {
                    v77 = MinecraftInstance.mc.getThePlayer();
                    if (v77 == null) {
                        Intrinsics.throwNpe();
                    }
                    var25_41 = (double)var22_35 / 50.0 * 1.75;
                    var36_45 = (double)v77.getTicksExisted() / 70.0;
                    var27_43 = false;
                    var38_46 = Math.sin(var25_41);
                    var44_49 = Color.HSBtoRGB((float)(var36_45 + var38_46) % 1.0f, 0.7f, 1.0f);
                    var24_39 = new Color(var44_49);
                    GL11.glColor3f((float)((float)var24_39.getRed() / 255.0f), (float)((float)var24_39.getGreen() / 255.0f), (float)((float)var24_39.getBlue() / 255.0f));
                    var25_41 = (double)var22_35 * 6.283185307179586 / 45.0;
                    var36_45 = var12_25;
                    var34_21 = var16_29;
                    var27_43 = false;
                    var38_46 = Math.cos(var25_41);
                    v78 = var34_21 + var36_45 * var38_46;
                    var25_41 = (double)var22_35 * 6.283185307179586 / 45.0;
                    var40_47 = var12_25;
                    var38_46 = var20_33;
                    var36_45 = var18_31;
                    var34_21 = v78;
                    var27_43 = false;
                    var42_48 = Math.sin(var25_41);
                    GL11.glVertex3d((double)var34_21, (double)var36_45, (double)(var38_46 + var40_47 * var42_48));
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
                var7_18 = this.yPos;
                var9_10 = onRender3D.5.INSTANCE;
                if (this.al > 0.0f) {
                    if (System.currentTimeMillis() - this.lastMS >= 1000L) {
                        this.direction = -this.direction;
                        this.lastMS = System.currentTimeMillis();
                    }
                    var10_50 = this.direction > (double)false ? System.currentTimeMillis() - this.lastMS : 1000L - (System.currentTimeMillis() - this.lastMS);
                    this.progress = (double)var10_50 / 1000.0;
                    this.lastDeltaMS = System.currentTimeMillis() - this.lastMS;
                } else {
                    this.lastMS = System.currentTimeMillis() - this.lastDeltaMS;
                }
                if (this.target != null) {
                    v79 = this.entity = this.target;
                    if (v79 == null) {
                        Intrinsics.throwNpe();
                    }
                    this.bb = v79.getEntityBoundingBox();
                }
                if (this.bb == null || this.entity == null) {
                    return;
                }
                v80 = this.bb;
                if (v80 == null) {
                    Intrinsics.throwNpe();
                }
                v81 = v80.getMaxX();
                v82 = this.bb;
                if (v82 == null) {
                    Intrinsics.throwNpe();
                }
                var10_51 = v81 - v82.getMinX();
                v83 = this.bb;
                if (v83 == null) {
                    Intrinsics.throwNpe();
                }
                v84 = v83.getMaxY();
                v85 = this.bb;
                if (v85 == null) {
                    Intrinsics.throwNpe();
                }
                var12_26 = v84 - v85.getMinY();
                v86 = this.entity;
                if (v86 == null) {
                    Intrinsics.throwNpe();
                }
                v87 = v86.getLastTickPosX();
                v88 = this.entity;
                if (v88 == null) {
                    Intrinsics.throwNpe();
                }
                v89 = v88.getPosX();
                v90 = this.entity;
                if (v90 == null) {
                    Intrinsics.throwNpe();
                }
                var14_28 = v87 + (v89 - v90.getLastTickPosX()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks();
                v91 = this.entity;
                if (v91 == null) {
                    Intrinsics.throwNpe();
                }
                v92 = v91.getLastTickPosY();
                v93 = this.entity;
                if (v93 == null) {
                    Intrinsics.throwNpe();
                }
                v94 = v93.getPosY();
                v95 = this.entity;
                if (v95 == null) {
                    Intrinsics.throwNpe();
                }
                var16_30 = v92 + (v94 - v95.getLastTickPosY()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks();
                v96 = this.entity;
                if (v96 == null) {
                    Intrinsics.throwNpe();
                }
                v97 = v96.getLastTickPosZ();
                v98 = this.entity;
                if (v98 == null) {
                    Intrinsics.throwNpe();
                }
                v99 = v98.getPosZ();
                v100 = this.entity;
                if (v100 == null) {
                    Intrinsics.throwNpe();
                }
                var18_32 = v97 + (v99 - v100.getLastTickPosZ()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks();
                this.yPos = var9_10.invoke(this.progress) * var12_26;
                var20_34 = (this.direction > (double)false ? this.yPos - var7_18 : var7_18 - this.yPos) * -this.direction * ((Number)this.jelloGradientHeightValue.get()).doubleValue();
                if (this.al <= (float)false && this.entity != null) {
                    this.entity = null;
                    return;
                }
                v101 = var22_36 = var5_5.invoke(this.entity);
                if (v101 == null) {
                    Intrinsics.throwNpe();
                }
                var23_38 = (float)v101.getRed() / 255.0f;
                var24_40 = (float)var22_36.getGreen() / 255.0f;
                var25_42 = (float)var22_36.getBlue() / 255.0f;
                var4_4.invoke();
                GL11.glTranslated((double)(-MinecraftInstance.mc.getRenderManager().getViewerPosX()), (double)(-MinecraftInstance.mc.getRenderManager().getViewerPosY()), (double)(-MinecraftInstance.mc.getRenderManager().getViewerPosZ()));
                GL11.glBegin((int)8);
                var27_44 = 360;
                for (var26_52 = 0; var26_52 <= var27_44; ++var26_52) {
                    var28_53 = (double)var26_52 * 3.141592653589793 / (double)180;
                    var30_54 = var14_28 - Math.sin(var28_53) * var10_51;
                    var32_55 = var18_32 + Math.cos(var28_53) * var10_51;
                    GL11.glColor4f((float)var23_38, (float)var24_40, (float)var25_42, (float)0.0f);
                    GL11.glVertex3d((double)var30_54, (double)(var16_30 + this.yPos + var20_34), (double)var32_55);
                    GL11.glColor4f((float)var23_38, (float)var24_40, (float)var25_42, (float)(this.al * ((Number)this.jelloAlphaValue.get()).floatValue()));
                    GL11.glVertex3d((double)var30_54, (double)(var16_30 + this.yPos), (double)var32_55);
                }
                GL11.glEnd();
                var3_3.invoke(var14_28, var16_30 + this.yPos, var18_32, ((Number)this.jelloWidthValue.get()).floatValue(), var10_51, var23_38, var24_40, var25_42, this.al);
                var2_2.invoke();
                break;
            }
        }
        if (this.currentTarget != null && this.attackTimer.hasTimePassed(this.attackDelay)) {
            v102 = this.currentTarget;
            if (v102 == null) {
                Intrinsics.throwNpe();
            }
            if (v102.getHurtTime() <= ((Number)this.hurtTimeValue.get()).intValue()) {
                var6_14 = this.clicks;
                this.clicks = var6_14 + 1;
                this.attackTimer.reset();
                this.attackDelay = TimeUtils.randomClickDelay(((Number)this.minCPS.get()).intValue(), ((Number)this.maxCPS.get()).intValue());
            }
        }
    }

    public final IEntityLivingBase getTarget() {
        return this.target;
    }

    private final float getMaxRange() {
        float f = ((Number)this.rangeValue.get()).floatValue();
        float f2 = ((Number)this.throughWallsRangeValue.get()).floatValue();
        boolean bl = false;
        return Math.max(f, f2);
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

    public final void setSyncEntity(@Nullable IEntityLivingBase iEntityLivingBase) {
        this.syncEntity = iEntityLivingBase;
    }

    public static final ListValue access$getColorModeValue$p(KillAura2 killAura2) {
        return killAura2.colorModeValue;
    }

    public static final void access$setAttackDelay$p(KillAura2 killAura2, long l) {
        killAura2.attackDelay = l;
    }

    public final boolean isBlockingChestAura() {
        return this.getState() && this.target != null;
    }

    public static final IntegerValue access$getColorBlueValue$p(KillAura2 killAura2) {
        return killAura2.colorBlueValue;
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

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onUpdate(UpdateEvent var1_1) {
        if (((Boolean)this.lightingValue.get()).booleanValue()) {
            var2_2 = (String)this.lightingModeValue.get();
            var3_4 = false;
            v0 = var2_2;
            if (v0 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            var2_2 = v0.toLowerCase();
            switch (var2_2.hashCode()) {
                case -1407259064: {
                    if (!var2_2.equals("attack")) ** break;
                    break;
                }
                case 3079268: {
                    if (!var2_2.equals("dead")) ** break;
                    if (this.target != null) {
                        if (this.lastTarget == null) {
                            v1 = this.target;
                        } else {
                            v2 = this.lastTarget;
                            if (v2 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (v2.getHealth() <= (float)false) {
                                v3 = MinecraftInstance.mc.getNetHandler2();
                                v4 = (World)MinecraftInstance.mc2.field_71441_e;
                                v5 = this.lastTarget;
                                if (v5 == null) {
                                    Intrinsics.throwNpe();
                                }
                                v6 = v5.getPosX();
                                v7 = this.lastTarget;
                                if (v7 == null) {
                                    Intrinsics.throwNpe();
                                }
                                v8 = v7.getPosY();
                                v9 = this.lastTarget;
                                if (v9 == null) {
                                    Intrinsics.throwNpe();
                                }
                                v3.func_147292_a(new SPacketSpawnGlobalEntity((Entity)new EntityLightningBolt(v4, v6, v8, v9.getPosZ(), true)));
                                if (((Boolean)this.lightingSoundValue.get()).booleanValue()) {
                                    MinecraftInstance.mc.getSoundHandler().playSound("entity.lightning.impact", 0.5f);
                                }
                            }
                            v1 = this.target;
                        }
                        this.lastTarget = v1;
                        ** break;
                    }
                    if (this.lastTarget == null) ** break;
                    v10 = this.lastTarget;
                    if (v10 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!(v10.getHealth() <= (float)false)) ** break;
                    v11 = MinecraftInstance.mc.getNetHandler2();
                    v12 = (World)MinecraftInstance.mc2.field_71441_e;
                    v13 = this.lastTarget;
                    if (v13 == null) {
                        Intrinsics.throwNpe();
                    }
                    v14 = v13.getPosX();
                    v15 = this.lastTarget;
                    if (v15 == null) {
                        Intrinsics.throwNpe();
                    }
                    v16 = v15.getPosY();
                    v17 = this.lastTarget;
                    if (v17 == null) {
                        Intrinsics.throwNpe();
                    }
                    v11.func_147292_a(new SPacketSpawnGlobalEntity((Entity)new EntityLightningBolt(v12, v14, v16, v17.getPosZ(), true)));
                    if (((Boolean)this.lightingSoundValue.get()).booleanValue()) {
                        MinecraftInstance.mc.getSoundHandler().playSound("entity.lightning.impact", 0.5f);
                    }
                    this.lastTarget = this.target;
                    ** break;
                }
            }
            v18 = MinecraftInstance.mc.getNetHandler2();
            v19 = (World)MinecraftInstance.mc2.field_71441_e;
            v20 = this.target;
            if (v20 == null) {
                Intrinsics.throwNpe();
            }
            v21 = v20.getPosX();
            v22 = this.target;
            if (v22 == null) {
                Intrinsics.throwNpe();
            }
            v23 = v22.getPosY();
            v24 = this.target;
            if (v24 == null) {
                Intrinsics.throwNpe();
            }
            v18.func_147292_a(new SPacketSpawnGlobalEntity((Entity)new EntityLightningBolt(v19, v21, v23, v24.getPosZ(), true)));
            if (!((Boolean)this.lightingSoundValue.get()).booleanValue()) ** break;
            MinecraftInstance.mc.getSoundHandler().playSound("entity.lightning.impact", 0.5f);
            ** break;
        }
lbl81:
        // 11 sources

        if (this.syncEntity != null) {
            v25 = this.syncEntity;
            if (v25 == null) {
                Intrinsics.throwNpe();
            }
            if (v25.isDead()) {
                ++KillAura2.killCounts;
                this.syncEntity = null;
            }
        }
        var2_2 = this;
        var3_4 = false;
        v26 = MinecraftInstance.mc.getThePlayer();
        if (v26 == null) {
            Intrinsics.throwNpe();
        }
        if (v26.isSpectator()) ** GOTO lbl-1000
        v27 = MinecraftInstance.mc.getThePlayer();
        if (v27 == null) {
            Intrinsics.throwNpe();
        }
        if (!KillAura2.access$isAlive((KillAura2)var2_2, v27) || LiquidBounce.INSTANCE.getModuleManager().get(Blink.class).getState() || LiquidBounce.INSTANCE.getModuleManager().get(FreeCam.class).getState()) lbl-1000:
        // 2 sources

        {
            v28 = true;
        } else {
            v28 = false;
        }
        if (v28) {
            this.target = null;
            this.currentTarget = null;
            this.hitable = false;
            this.stopBlocking();
            return;
        }
        if (((String)this.autoBlockValue.get()).equals("HuaYuTing")) {
            v29 = MinecraftInstance.mc.getThePlayer();
            if (v29 == null) {
                Intrinsics.throwNpe();
            }
            if (v29.getHeldItem() != null) {
                v30 = MinecraftInstance.mc.getThePlayer();
                if (v30 == null) {
                    Intrinsics.throwNpe();
                }
                v31 = v30.getHeldItem();
                if (v31 == null) {
                    Intrinsics.throwNpe();
                }
                if (v31.getItem() instanceof ItemSword) {
                    if (MinecraftInstance.mc2.field_71439_g.field_110158_av == -1) {
                        PacketUtils.INSTANCE.sendPacketNoEvent((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
                    } else if ((double)MinecraftInstance.mc2.field_71439_g.field_110158_av < 0.5 && MinecraftInstance.mc2.field_71439_g.field_110158_av != -1) {
                        PacketUtils.INSTANCE.sendPacketNoEvent((Packet)new CPacketPlayerTryUseItemOnBlock(new BlockPos(-1, -1, -1), EnumFacing.WEST, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                        v32 = MinecraftInstance.mc.getThePlayer();
                        if (v32 == null) {
                            Intrinsics.throwNpe();
                        }
                        v33 = v32.getSendQueue();
                        v34 = MinecraftInstance.mc.getThePlayer();
                        if (v34 == null) {
                            Intrinsics.throwNpe();
                        }
                        var2_2 = v34.getInventory().getCurrentItemInHand();
                        var3_5 = WEnumHand.MAIN_HAND;
                        var5_6 = v33;
                        var4_7 = false;
                        var6_8 = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(var3_5);
                        var5_6.addToSendQueue(var6_8);
                        v35 = MinecraftInstance.mc.getThePlayer();
                        if (v35 == null) {
                            Intrinsics.throwNpe();
                        }
                        v36 = v35.getSendQueue();
                        v37 = MinecraftInstance.mc.getThePlayer();
                        if (v37 == null) {
                            Intrinsics.throwNpe();
                        }
                        var2_2 = v37.getInventory().getCurrentItemInHand();
                        var3_5 = WEnumHand.OFF_HAND;
                        var5_6 = v36;
                        var4_7 = false;
                        var6_8 = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(var3_5);
                        var5_6.addToSendQueue(var6_8);
                    }
                }
            }
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
            v38 = MinecraftInstance.mc.getThePlayer();
            if (v38 == null) {
                Intrinsics.throwNpe();
            }
            if (v38.getCooledAttackStrength(0.0f) >= ((Number)this.cooldownValue.get()).floatValue()) {
                while (this.clicks > 0) {
                    this.runAttack();
                    var2_3 = this.clicks;
                    this.clicks = var2_3 + -1;
                }
            }
        }
    }

    public static final BoolValue access$getColorTeam$p(KillAura2 killAura2) {
        return killAura2.colorTeam;
    }

    public static final FloatValue access$getMinTurnSpeed$p(KillAura2 killAura2) {
        return killAura2.minTurnSpeed;
    }

    public static final boolean access$isAlive(KillAura2 killAura2, IEntityLivingBase iEntityLivingBase) {
        return killAura2.isAlive(iEntityLivingBase);
    }

    public final void setTarget(@Nullable IEntityLivingBase iEntityLivingBase) {
        this.target = iEntityLivingBase;
    }

    public static final FloatValue access$getMaxTurnSpeed$p(KillAura2 killAura2) {
        return killAura2.maxTurnSpeed;
    }

    public static final BoolValue access$getLivingRaycastValue$p(KillAura2 killAura2) {
        return killAura2.livingRaycastValue;
    }

    private final void startBlocking(IEntity iEntity, boolean bl) {
        if (LiquidBounce.INSTANCE.getModuleManager().get(OldHitting.class).getState()) {
            if (this.autoBlockValue.equals("Range")) {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, iEntity) > ((Number)this.BlockRangeValue.get()).doubleValue()) {
                    return;
                }
            }
            if (this.blockingStatus) {
                return;
            }
            if (bl) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity(iEntity, iEntity.getPositionVector()));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity(iEntity, ICPacketUseEntity.WAction.INTERACT));
            }
            if (StringsKt.equals((String)((String)this.autoBlockPacketValue.get()), (String)"UseItem", (boolean)true)) {
                KeyBinding.func_74510_a((int)MinecraftInstance.mc.getGameSettings().getKeyBindUseItem().getKeyCode(), (boolean)true);
            }
            if (StringsKt.equals((String)((String)this.autoBlockPacketValue.get()), (String)"GameSettings", (boolean)true)) {
                MinecraftInstance.mc.getGameSettings().getKeyBindUseItem().setPressed(true);
            }
            if (StringsKt.equals((String)((String)this.autoBlockPacketValue.get()), (String)"Mouse", (boolean)true)) {
                new Robot().mousePress(4096);
            }
            if (StringsKt.equals((String)((String)this.autoBlockPacketValue.get()), (String)"Vanilla", (boolean)true)) {
                if (StringsKt.equals((String)((String)this.vanillamode.get()), (String)"TryUseItem", (boolean)true)) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketTryUseItem(WEnumHand.MAIN_HAND));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketTryUseItem(WEnumHand.OFF_HAND));
                }
                if (StringsKt.equals((String)((String)this.vanillamode.get()), (String)"UseItem", (boolean)true)) {
                    IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    IItemStack iItemStack = iEntityPlayerSP.getInventory().getCurrentItemInHand();
                    WEnumHand wEnumHand = WEnumHand.MAIN_HAND;
                    IINetHandlerPlayClient iINetHandlerPlayClient2 = iINetHandlerPlayClient;
                    boolean bl2 = false;
                    IPacket iPacket = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(wEnumHand);
                    iINetHandlerPlayClient2.addToSendQueue(iPacket);
                    IINetHandlerPlayClient iINetHandlerPlayClient3 = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP2 == null) {
                        Intrinsics.throwNpe();
                    }
                    iItemStack = iEntityPlayerSP2.getInventory().getCurrentItemInHand();
                    wEnumHand = WEnumHand.OFF_HAND;
                    iINetHandlerPlayClient2 = iINetHandlerPlayClient3;
                    bl2 = false;
                    iPacket = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(wEnumHand);
                    iINetHandlerPlayClient2.addToSendQueue(iPacket);
                }
                if (StringsKt.equals((String)((String)this.vanillamode.get()), (String)"OldC08", (boolean)true)) {
                    IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                    WBlockPos wBlockPos = new WBlockPos(-0.5534147541, -0.5534147541, -0.5534147541);
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerBlockPlacement(wBlockPos, 255, iEntityPlayerSP.getInventory().getCurrentItemInHand(), 0.0f, 0.0f, 0.0f));
                }
            }
            if (StringsKt.equals((String)((String)this.vanillamode.get()), (String)"CPacketPlayerBlockPlacement", (boolean)true)) {
                NetHandlerPlayClient netHandlerPlayClient = MinecraftInstance.mc2.func_147114_u();
                if (netHandlerPlayClient == null) {
                    Intrinsics.throwNpe();
                }
                netHandlerPlayClient.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(new BlockPos(-0.5534147541, -0.5534147541, -0.5534147541), EnumFacing.WEST, EnumHand.OFF_HAND, 0.0f, 0.0f, 0.0f));
                NetHandlerPlayClient netHandlerPlayClient2 = MinecraftInstance.mc2.func_147114_u();
                if (netHandlerPlayClient2 == null) {
                    Intrinsics.throwNpe();
                }
                netHandlerPlayClient2.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(new BlockPos(-0.5534147541, -0.5534147541, -0.5534147541), EnumFacing.WEST, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            }
        }
        this.blockingStatus = true;
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

    public static final boolean access$isEnemy(KillAura2 killAura2, IEntity iEntity) {
        return killAura2.isEnemy(iEntity);
    }

    public static final FloatValue access$getMaxPredictSize$p(KillAura2 killAura2) {
        return killAura2.maxPredictSize;
    }

    private final float getRange(IEntity iEntity) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        float f = PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, iEntity) >= ((Number)this.throughWallsRangeValue.get()).doubleValue() ? ((Number)this.rangeValue.get()).floatValue() : ((Number)this.rangeValue.get()).floatValue();
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        return f - (iEntityPlayerSP2.getSprinting() ? ((Number)this.rangeSprintReducementValue.get()).floatValue() : 0.0f);
    }

    public final void setBlockingStatus(boolean bl) {
        this.blockingStatus = bl;
    }

    public static final long access$getAttackDelay$p(KillAura2 killAura2) {
        return killAura2.attackDelay;
    }

    public static final IntegerValue access$getColorGreenValue$p(KillAura2 killAura2) {
        return killAura2.colorGreenValue;
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
        if (!KillAura2.access$isAlive(this, iEntityPlayerSP2)) return true;
        if (LiquidBounce.INSTANCE.getModuleManager().get(Blink.class).getState()) return true;
        if (!LiquidBounce.INSTANCE.getModuleManager().get(FreeCam.class).getState()) return false;
        return true;
    }

    @EventTarget
    public final void onTick(@Nullable TickEvent tickEvent) {
        if (StringsKt.equals((String)((String)this.markValue.get()), (String)"jello", (boolean)true)) {
            this.al = AnimationUtils.changer(this.al, this.target != null ? ((Number)this.jelloFadeSpeedValue.get()).floatValue() : -((Number)this.jelloFadeSpeedValue.get()).floatValue(), 0.0f, ((Number)this.colorAlphaValue.get()).floatValue() / 255.0f);
        }
    }

    public static final int getKillCounts() {
        Companion companion = Companion;
        return killCounts;
    }

    public final boolean getBlockingStatus() {
        return this.blockingStatus;
    }

    public static final void access$setKillCounts$cp(int n) {
        killCounts = n;
    }

    private final void updateHitable() {
        if (((Boolean)this.hitableValue.get()).booleanValue()) {
            this.hitable = true;
            return;
        }
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
                final KillAura2 this$0;
                {
                    this.this$0 = killAura2;
                }

                static {
                }

                /*
                 * Enabled force condition propagation
                 * Lifted jumps to return sites
                 */
                public boolean canRaycast(@Nullable IEntity iEntity) {
                    if (((Boolean)KillAura2.access$getLivingRaycastValue$p(this.this$0).get()).booleanValue()) {
                        if (!MinecraftInstance.classProvider.isEntityLivingBase(iEntity)) return false;
                        if (MinecraftInstance.classProvider.isEntityArmorStand(iEntity)) return false;
                    }
                    if (KillAura2.access$isEnemy(this.this$0, iEntity)) return true;
                    if ((Boolean)KillAura2.access$getRaycastIgnoredValue$p(this.this$0).get() != false) return true;
                    if ((Boolean)KillAura2.access$getAacValue$p(this.this$0).get() == false) return false;
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

    @EventTarget
    public final void onEntityMove(EntityMovementEvent entityMovementEvent) {
        IEntity iEntity = entityMovementEvent.getMovedEntity();
        if (this.target == null || iEntity.equals(this.currentTarget) ^ true) {
            return;
        }
        this.updateHitable();
    }

    public static final IExtractedFunctions access$getFunctions$p$s1046033730() {
        return MinecraftInstance.functions;
    }

    public static final FloatValue access$getSaturationValue$p(KillAura2 killAura2) {
        return killAura2.saturationValue;
    }

    public static final IntegerValue access$getMaxCPS$p(KillAura2 killAura2) {
        return killAura2.maxCPS;
    }

    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final void setKillCounts(int n) {
            KillAura2.access$setKillCounts$cp(n);
        }

        public final int getKillCounts() {
            return KillAura2.access$getKillCounts$cp();
        }

        private Companion() {
        }

        @JvmStatic
        public static void killCounts$annotations() {
        }
    }
}

