/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.math.MathKt
 *  kotlin.text.StringsKt
 *  net.minecraft.block.BlockBush
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.server.SPacketDisconnect
 *  net.minecraft.util.EnumFacing
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import java.awt.Color;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.minecraft.block.material.IMaterial;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IPlayerControllerMP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WMathHelper;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.StrafeEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.render.BlockOverlay;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.block.PlaceInfo;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.BlockBush;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="ScaffoldNew", description="Sk1d by LemonHaikea 2997570499.", category=ModuleCategory.WORLD)
public final class ScaffoldNew
extends Module {
    private final BoolValue airSafeValue;
    private boolean f;
    private final IntegerValue searchAccuracyValue;
    private PlaceInfo targetPlace;
    private final BoolValue omniDirectionalExpand;
    private boolean zitterDirection;
    private final BoolValue Fastplace;
    private final MSTimer delayTimer;
    private final IntegerValue Rotairticks;
    private int placedBlocksWithoutEagle;
    private final BoolValue counterDisplayValue;
    private final ListValue searchMode;
    private final FloatValue zitterStrength;
    private final MSTimer zitterTimer;
    private boolean n;
    private final IntegerValue keepLengthValue;
    private TickTimer lockRotationTimer;
    private final ListValue sprintModeValue;
    private final FloatValue xzRangeValue;
    private final ListValue zitterMode;
    private int slot;
    private final FloatValue timerValue;
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Normal", "Rewinside", "Expand"}, "Normal");
    private final BoolValue rotationsValue;
    private final BoolValue silentRotationValue;
    private final IntegerValue minDelayValue;
    private final BoolValue swingValue;
    private long delay;
    private final ListValue placeConditionValue;
    private final FloatValue zitterSpeed;
    private final BoolValue searchValue;
    private final BoolValue downValue;
    private final IntegerValue falldowndelay;
    private final ListValue autoBlockValue;
    private final IntegerValue blocksToEagleValue;
    private final IntegerValue expandLengthValue;
    private final BoolValue sameYValue;
    private final FloatValue speedModifierValue;
    private final FloatValue slowSpeed;
    private final FloatValue maxTurnSpeedValue;
    private final FloatValue edgeDistanceValue;
    private Rotation lockRotation;
    private final IntegerValue maxDelayValue = new IntegerValue(this, "MaxDelay", 0, 0, 1000){
        final ScaffoldNew this$0;

        static {
        }
        {
            this.this$0 = scaffoldNew;
            super(string, n, n2, n3);
        }

        public void onChanged(Object object, Object object2) {
            this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
        }

        protected void onChanged(int n, int n2) {
            int n3 = ((Number)ScaffoldNew.access$getMinDelayValue$p(this.this$0).get()).intValue();
            if (n3 > n2) {
                this.set((Object)n3);
            }
        }
    };
    private boolean canPlace;
    private FloatValue yRangeValue;
    private final FloatValue minDistValue;
    private final BoolValue FallFastplace;
    private final BoolValue safeWalkValue;
    private final BoolValue keepRotationValue;
    private final ListValue RotConditionValue;
    private final FloatValue minTurnSpeedValue;
    private boolean facesBlock;
    private final ListValue strafeMode;
    private final ListValue placeModeValue;
    private final ListValue eagleValue;
    private boolean canRot;
    private final BoolValue sprintValue;
    private boolean shouldGoDown;
    private final IntegerValue airticks;
    private final BoolValue slowValue;
    private int launchY;
    private final BoolValue placeDelay;
    private final BoolValue markValue;
    private boolean eagleSneaking;
    private int airtime;

    @EventTarget
    public final void onMove(MoveEvent moveEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (!((Boolean)this.safeWalkValue.get()).booleanValue() || this.shouldGoDown) {
            return;
        }
        if (((Boolean)this.airSafeValue.get()).booleanValue() || iEntityPlayerSP2.getOnGround()) {
            moveEvent.setSafeWalk(true);
        }
    }

    @Override
    public void onEnable() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        this.canPlace = false;
        this.canRot = false;
        this.f = false;
        this.airtime = 0;
        this.launchY = MathKt.roundToInt((double)iEntityPlayerSP2.getPosY());
        this.slot = iEntityPlayerSP2.getInventory().getCurrentItem();
        this.facesBlock = false;
    }

    public final BoolValue getSprintValue() {
        return this.sprintValue;
    }

    private final void setRotation(Rotation rotation) {
        if (!this.canRot) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (((Boolean)this.silentRotationValue.get()).booleanValue()) {
            RotationUtils.setTargetRotation(rotation, 0);
        } else {
            iEntityPlayerSP2.setRotationYaw(rotation.getYaw());
            iEntityPlayerSP2.setRotationPitch(rotation.getPitch());
        }
    }

    public static final FloatValue access$getMinTurnSpeedValue$p(ScaffoldNew scaffoldNew) {
        return scaffoldNew.minTurnSpeedValue;
    }

    public static final FloatValue access$getMaxTurnSpeedValue$p(ScaffoldNew scaffoldNew) {
        return scaffoldNew.maxTurnSpeedValue;
    }

    @EventTarget
    public final void onStrafe(StrafeEvent strafeEvent) {
        if (StringsKt.equals((String)((String)this.strafeMode.get()), (String)"Off", (boolean)true)) {
            return;
        }
        if (!this.canRot) {
            return;
        }
        this.update();
        Rotation rotation = this.lockRotation;
        if (rotation == null) {
            return;
        }
        Rotation rotation2 = rotation;
        if (((Boolean)this.rotationsValue.get()).booleanValue() && (((Boolean)this.keepRotationValue.get()).booleanValue() || !this.lockRotationTimer.hasTimePassed(((Number)this.keepLengthValue.get()).intValue()))) {
            if (this.targetPlace == null) {
                rotation2.setYaw(WMathHelper.wrapAngleTo180_float((float)MathKt.roundToInt((float)(rotation2.getYaw() / 45.0f)) * 45.0f));
            }
            this.setRotation(rotation2);
            this.lockRotationTimer.update();
            rotation2.applyStrafeToPlayer(strafeEvent);
            strafeEvent.cancelEvent();
            return;
        }
        Rotation rotation3 = RotationUtils.targetRotation;
        if (rotation3 == null) {
            return;
        }
        Rotation rotation4 = rotation3;
        rotation4.applyStrafeToPlayer(strafeEvent);
        strafeEvent.cancelEvent();
    }

    public final int getBlocksAmount() {
        int n = 0;
        int n2 = 44;
        for (int i = 36; i <= n2; ++i) {
            IItemStack iItemStack;
            IItemStack iItemStack2;
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if ((iItemStack2 = iEntityPlayerSP.getInventoryContainer().getSlot(i).getStack()) == null || !MinecraftInstance.classProvider.isItemBlock(iItemStack2.getItem())) continue;
            IItem iItem = iItemStack2.getItem();
            if (iItem == null) {
                Intrinsics.throwNpe();
            }
            IBlock iBlock = iItem.asItemBlock().getBlock();
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            if (((iItemStack = iEntityPlayerSP2.getHeldItem()) == null || !iItemStack.equals(iItemStack2)) && (InventoryUtils.BLOCK_BLACKLIST.contains(iBlock) || MinecraftInstance.classProvider.isBlockBush(iBlock))) continue;
            n += iItemStack2.getStackSize();
        }
        return n;
    }

    public ScaffoldNew() {
        this.minDelayValue = new IntegerValue(this, "MinDelay", 0, 0, 1000){
            final ScaffoldNew this$0;

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
            }
            {
                this.this$0 = scaffoldNew;
                super(string, n, n2, n3);
            }

            protected void onChanged(int n, int n2) {
                int n3 = ((Number)ScaffoldNew.access$getMaxDelayValue$p(this.this$0).get()).intValue();
                if (n3 < n2) {
                    this.set((Object)n3);
                }
            }

            static {
            }
        };
        this.falldowndelay = new IntegerValue("FallDownDelay", 0, 0, 1000);
        this.placeDelay = new BoolValue("PlaceDelay", true);
        this.autoBlockValue = new ListValue("AutoBlock", new String[]{"Off", "Pick", "Spoof", "Switch"}, "Spoof");
        this.sprintValue = new BoolValue("Sprint", true);
        this.sprintModeValue = new ListValue("SprintMode", new String[]{"off", "ground", "air"}, "air");
        this.swingValue = new BoolValue("Swing", true);
        this.searchValue = new BoolValue("Search", true);
        this.downValue = new BoolValue("Down", true);
        this.placeModeValue = new ListValue("PlaceTiming", new String[]{"Pre", "Post"}, "Post");
        this.placeConditionValue = new ListValue("PlaceCondition", new String[]{"Always", "DelayAir", "FallDown"}, "Always");
        this.RotConditionValue = new ListValue("RotCondition", new String[]{"Always", "DelayAir", "FallDown"}, "Always");
        this.airticks = new IntegerValue("PlaceAirTime", 0, 0, 10);
        this.Rotairticks = new IntegerValue("RotAirTime", 0, 0, 10);
        this.eagleValue = new ListValue("Eagle", new String[]{"Normal", "Silent", "Off"}, "Normal");
        this.blocksToEagleValue = new IntegerValue("BlocksToEagle", 0, 0, 10);
        this.edgeDistanceValue = new FloatValue("EagleEdgeDistance", 0.0f, 0.0f, 0.5f);
        this.omniDirectionalExpand = new BoolValue("OmniDirectionalExpand", false);
        this.expandLengthValue = new IntegerValue("ExpandLength", 1, 1, 6);
        this.strafeMode = new ListValue("Strafe", new String[]{"Off", "AAC"}, "Off");
        this.rotationsValue = new BoolValue("Rotations", true);
        this.silentRotationValue = new BoolValue("SilentRotation", true);
        this.keepRotationValue = new BoolValue("KeepRotation", true);
        this.keepLengthValue = new IntegerValue("KeepRotationLength", 0, 0, 20);
        this.searchMode = new ListValue("XYZSearch", new String[]{"Auto", "AutoCenter", "Manual"}, "AutoCenter");
        this.xzRangeValue = new FloatValue("xzRange", 0.8f, 0.0f, 1.0f);
        this.yRangeValue = new FloatValue("yRange", 0.8f, 0.0f, 1.0f);
        this.minDistValue = new FloatValue("MinDist", 0.0f, 0.0f, 0.2f);
        this.searchAccuracyValue = new IntegerValue("SearchAccuracy", 8, 1, 16){

            protected void onChanged(int n, int n2) {
                if (this.getMaximum() < n2) {
                    this.set((Object)this.getMaximum());
                } else if (this.getMinimum() > n2) {
                    this.set((Object)this.getMinimum());
                }
            }

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
            }

            static {
            }
        };
        this.maxTurnSpeedValue = new FloatValue(this, "MaxTurnSpeed", 180.0f, 1.0f, 180.0f){
            final ScaffoldNew this$0;
            {
                this.this$0 = scaffoldNew;
                super(string, f, f2, f3);
            }

            protected void onChanged(float f, float f2) {
                float f3 = ((Number)ScaffoldNew.access$getMinTurnSpeedValue$p(this.this$0).get()).floatValue();
                if (f3 > f2) {
                    this.set((Object)Float.valueOf(f3));
                }
                if (this.getMaximum() < f2) {
                    this.set((Object)Float.valueOf(this.getMaximum()));
                } else if (this.getMinimum() > f2) {
                    this.set((Object)Float.valueOf(this.getMinimum()));
                }
            }

            static {
            }

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).floatValue(), ((Number)object2).floatValue());
            }
        };
        this.minTurnSpeedValue = new FloatValue(this, "MinTurnSpeed", 180.0f, 1.0f, 180.0f){
            final ScaffoldNew this$0;

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).floatValue(), ((Number)object2).floatValue());
            }
            {
                this.this$0 = scaffoldNew;
                super(string, f, f2, f3);
            }

            static {
            }

            protected void onChanged(float f, float f2) {
                float f3 = ((Number)ScaffoldNew.access$getMaxTurnSpeedValue$p(this.this$0).get()).floatValue();
                if (f3 < f2) {
                    this.set((Object)Float.valueOf(f3));
                }
                if (this.getMaximum() < f2) {
                    this.set((Object)Float.valueOf(this.getMaximum()));
                } else if (this.getMinimum() > f2) {
                    this.set((Object)Float.valueOf(this.getMinimum()));
                }
            }
        };
        this.zitterMode = new ListValue("Zitter", new String[]{"Off", "Teleport", "Smooth"}, "Off");
        this.zitterSpeed = new FloatValue("ZitterSpeed", 0.13f, 0.1f, 0.3f);
        this.zitterStrength = new FloatValue("ZitterStrength", 0.05f, 0.0f, 0.2f);
        this.timerValue = new FloatValue("Timer", 1.0f, 0.1f, 10.0f);
        this.speedModifierValue = new FloatValue("SpeedModifier", 1.0f, 0.0f, 2.0f);
        this.slowValue = new BoolValue("Slow", false);
        this.slowSpeed = new FloatValue("SlowSpeed", 0.6f, 0.2f, 0.8f);
        this.sameYValue = new BoolValue("SameY", false);
        this.safeWalkValue = new BoolValue("SafeWalk", true);
        this.airSafeValue = new BoolValue("AirSafe", false);
        this.FallFastplace = new BoolValue("Fallfastplace", false);
        this.Fastplace = new BoolValue("fastplace", false);
        this.counterDisplayValue = new BoolValue("Counter", true);
        this.markValue = new BoolValue("Mark", false);
        this.lockRotationTimer = new TickTimer();
        this.delayTimer = new MSTimer();
        this.zitterTimer = new MSTimer();
    }

    /*
     * Unable to fully structure code
     */
    public final void update() {
        if (!this.canRot) {
            return;
        }
        v0 = MinecraftInstance.mc.getThePlayer();
        if (v0 == null) {
            return;
        }
        var1_1 = v0;
        if (var1_1.getHeldItem() == null) ** GOTO lbl-1000
        v1 = var1_1.getHeldItem();
        if (v1 == null) {
            Intrinsics.throwNpe();
        }
        if (v1.getItem() instanceof ItemBlock) {
            v2 = true;
        } else lbl-1000:
        // 2 sources

        {
            v2 = var2_2 = false;
        }
        v3 = !StringsKt.equals((String)((String)this.autoBlockValue.get()), (String)"off", (boolean)true) ? InventoryUtils.findAutoBlockBlock() == -1 && !var2_2 : var2_2 == false;
        if (v3) {
            return;
        }
        this.findBlock(StringsKt.equals((String)((String)this.modeValue.get()), (String)"expand", (boolean)true));
    }

    private final double calcStepSize(float f) {
        double d = ((Number)this.searchAccuracyValue.get()).intValue();
        return (double)f / (d += d % (double)2) < 0.01 ? 0.01 : (double)f / d;
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        if (packetEvent.getPacket() instanceof SPacketDisconnect) {
            this.setState(false);
        }
        if (MinecraftInstance.mc.getThePlayer() == null) {
            return;
        }
        IPacket iPacket = packetEvent.getPacket();
        if (iPacket instanceof CPacketHeldItemChange) {
            this.slot = ((CPacketHeldItemChange)iPacket).func_149614_c();
        }
    }

    /*
     * Exception decompiling
     */
    private final void findBlock(boolean var1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl180 : ILOAD - null : trying to set 4 previously set to 2
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

    @EventTarget
    public final void onMotion(MotionEvent motionEvent) {
        EventState eventState = motionEvent.getEventState();
        if (!this.canRot) {
            return;
        }
        if (((Boolean)this.rotationsValue.get()).booleanValue() && (((Boolean)this.keepRotationValue.get()).booleanValue() || !this.lockRotationTimer.hasTimePassed(((Number)this.keepLengthValue.get()).intValue())) && this.lockRotation != null && StringsKt.equals((String)((String)this.strafeMode.get()), (String)"Off", (boolean)true)) {
            Rotation rotation = this.lockRotation;
            if (rotation == null) {
                Intrinsics.throwNpe();
            }
            this.setRotation(rotation);
            if (eventState == EventState.POST) {
                this.lockRotationTimer.update();
            }
        }
        if ((this.facesBlock || !((Boolean)this.rotationsValue.get()).booleanValue()) && StringsKt.equals((String)((String)this.placeModeValue.get()), (String)eventState.getStateName(), (boolean)true)) {
            if (!this.canPlace) {
                return;
            }
            this.place();
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getFallDistance() > 0.0f && (Boolean)this.FallFastplace.get() != false || this.canPlace && ((Boolean)this.Fastplace.get()).booleanValue()) {
            this.place();
        }
        if (eventState == EventState.PRE && StringsKt.equals((String)((String)this.strafeMode.get()), (String)"Off", (boolean)true)) {
            this.update();
        }
        if (this.targetPlace == null && ((Boolean)this.placeDelay.get()).booleanValue()) {
            this.delayTimer.reset();
        }
    }

    public final void place() {
        IItemStack iItemStack;
        IWorldClient iWorldClient;
        IEntityPlayerSP iEntityPlayerSP;
        block43: {
            int n;
            block42: {
                block41: {
                    block40: {
                        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP2 == null) {
                            return;
                        }
                        iEntityPlayerSP = iEntityPlayerSP2;
                        IWorldClient iWorldClient2 = MinecraftInstance.mc.getTheWorld();
                        if (iWorldClient2 == null) {
                            return;
                        }
                        iWorldClient = iWorldClient2;
                        if (!this.canPlace) {
                            return;
                        }
                        if (this.targetPlace == null) {
                            if (((Boolean)this.placeDelay.get()).booleanValue()) {
                                this.delayTimer.reset();
                            }
                            return;
                        }
                        if (!this.delayTimer.hasTimePassed(this.delay)) break block40;
                        if (!((Boolean)this.sameYValue.get()).booleanValue()) break block41;
                        PlaceInfo placeInfo = this.targetPlace;
                        if (placeInfo == null) {
                            Intrinsics.throwNpe();
                        }
                        if (this.launchY - 1 == (int)placeInfo.getVec3().getYCoord()) break block41;
                    }
                    return;
                }
                if ((iItemStack = iEntityPlayerSP.getHeldItem()) == null || !(iItemStack.getItem() instanceof ItemBlock)) break block42;
                IItem iItem = iItemStack.getItem();
                if (iItem == null) {
                    Intrinsics.throwNpe();
                }
                if (iItem == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemBlock");
                }
                if (((ItemBlock)iItem).func_179223_d() instanceof BlockBush) break block42;
                IItemStack iItemStack2 = iEntityPlayerSP.getHeldItem();
                if (iItemStack2 == null) {
                    Intrinsics.throwNpe();
                }
                if (iItemStack2.getStackSize() > 0) break block43;
            }
            if ((n = InventoryUtils.findAutoBlockBlock()) == -1) {
                return;
            }
            String string = (String)this.autoBlockValue.get();
            boolean bl = false;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            switch (string2.toLowerCase()) {
                case "off": {
                    return;
                }
                case "pick": {
                    iEntityPlayerSP.getInventory().setCurrentItem(n - 36);
                    MinecraftInstance.mc.getPlayerController().updateController();
                    break;
                }
                case "spoof": {
                    if (n - 36 == this.slot) break;
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(n - 36));
                    break;
                }
                case "switch": {
                    if (n - 36 == this.slot) break;
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(n - 36));
                    break;
                }
            }
            iItemStack = iEntityPlayerSP.getInventoryContainer().getSlot(n).getStack();
        }
        IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
        PlaceInfo placeInfo = this.targetPlace;
        if (placeInfo == null) {
            Intrinsics.throwNpe();
        }
        WBlockPos wBlockPos = placeInfo.getBlockPos();
        PlaceInfo placeInfo2 = this.targetPlace;
        if (placeInfo2 == null) {
            Intrinsics.throwNpe();
        }
        IEnumFacing iEnumFacing = placeInfo2.getEnumFacing();
        PlaceInfo placeInfo3 = this.targetPlace;
        if (placeInfo3 == null) {
            Intrinsics.throwNpe();
        }
        if (iPlayerControllerMP.onPlayerRightClick(iEntityPlayerSP, iWorldClient, iItemStack, wBlockPos, iEnumFacing, placeInfo3.getVec3())) {
            long l;
            this.delayTimer.reset();
            if (!((Boolean)this.placeDelay.get()).booleanValue()) {
                l = 0L;
            } else {
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                l = this.delay = iEntityPlayerSP3.getFallDistance() > 0.0f ? (long)((Number)this.falldowndelay.get()).intValue() : TimeUtils.randomDelay(((Number)this.minDelayValue.get()).intValue(), ((Number)this.maxDelayValue.get()).intValue());
            }
            if (iEntityPlayerSP.getOnGround()) {
                float f = ((Number)this.speedModifierValue.get()).floatValue();
                iEntityPlayerSP.setMotionX(iEntityPlayerSP.getMotionX() * (double)f);
                iEntityPlayerSP.setMotionZ(iEntityPlayerSP.getMotionZ() * (double)f);
            }
            if (((Boolean)this.swingValue.get()).booleanValue()) {
                iEntityPlayerSP.swingItem();
            } else {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketAnimation());
            }
        }
        if (StringsKt.equals((String)((String)this.autoBlockValue.get()), (String)"Switch", (boolean)true) && this.slot != iEntityPlayerSP.getInventory().getCurrentItem()) {
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(iEntityPlayerSP.getInventory().getCurrentItem()));
        }
        this.targetPlace = null;
    }

    @Override
    public void onDisable() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (!GameSettings.func_100015_a((KeyBinding)MinecraftInstance.mc2.field_71474_y.field_74311_E)) {
            MinecraftInstance.mc.getGameSettings().getKeyBindSneak().setPressed(false);
            if (this.eagleSneaking) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.STOP_SNEAKING));
            }
        }
        if (!GameSettings.func_100015_a((KeyBinding)MinecraftInstance.mc2.field_71474_y.field_74366_z)) {
            MinecraftInstance.mc.getGameSettings().getKeyBindRight().setPressed(false);
        }
        if (!GameSettings.func_100015_a((KeyBinding)MinecraftInstance.mc2.field_71474_y.field_74370_x)) {
            MinecraftInstance.mc.getGameSettings().getKeyBindLeft().setPressed(false);
        }
        this.lockRotation = null;
        this.facesBlock = false;
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
        this.shouldGoDown = false;
        if (this.slot != iEntityPlayerSP2.getInventory().getCurrentItem()) {
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(iEntityPlayerSP2.getInventory().getCurrentItem()));
        }
    }

    public static final IntegerValue access$getMinDelayValue$p(ScaffoldNew scaffoldNew) {
        return scaffoldNew.minDelayValue;
    }

    @EventTarget
    public final void onRender3D(Render3DEvent render3DEvent) {
        int n;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (!((Boolean)this.markValue.get()).booleanValue()) {
            return;
        }
        int n2 = n = StringsKt.equals((String)((String)this.modeValue.get()), (String)"Expand", (boolean)true) ? ((Number)this.expandLengthValue.get()).intValue() + 1 : 2;
        for (int i = 0; i < n; ++i) {
            int n3;
            int n4;
            int n5;
            int n6;
            double d = Math.toRadians(iEntityPlayerSP2.getRotationYaw());
            if (((Boolean)this.omniDirectionalExpand.get()).booleanValue()) {
                n6 = 0;
                n5 = -MathKt.roundToInt((double)Math.sin(d));
            } else {
                n5 = n4 = iEntityPlayerSP2.getHorizontalFacing().getDirectionVec().getX();
            }
            if (((Boolean)this.omniDirectionalExpand.get()).booleanValue()) {
                boolean bl = false;
                n3 = MathKt.roundToInt((double)Math.cos(d));
            } else {
                n3 = iEntityPlayerSP2.getHorizontalFacing().getDirectionVec().getZ();
            }
            n6 = n3;
            WBlockPos wBlockPos = new WBlockPos(iEntityPlayerSP2.getPosX() + (double)(n4 * i), (Boolean)this.sameYValue.get() != false && (double)this.launchY <= iEntityPlayerSP2.getPosY() ? (double)this.launchY - 1.0 : iEntityPlayerSP2.getPosY() - (iEntityPlayerSP2.getPosY() == iEntityPlayerSP2.getPosY() + 0.5 ? 0.0 : 1.0) - (this.shouldGoDown ? 1.0 : 0.0), iEntityPlayerSP2.getPosZ() + (double)(n6 * i));
            PlaceInfo placeInfo = PlaceInfo.Companion.get(wBlockPos);
            boolean bl = false;
            IMaterial iMaterial = BlockUtils.getMaterial(wBlockPos);
            if (!(iMaterial != null ? iMaterial.isReplaceable() : false) || placeInfo == null) continue;
            RenderUtils.drawBlockBox(wBlockPos, new Color(68, 117, 255, 100), false);
            break;
        }
    }

    public static final IntegerValue access$getMaxDelayValue$p(ScaffoldNew scaffoldNew) {
        return scaffoldNew.maxDelayValue;
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    private final void onUpdate(UpdateEvent var1_1) {
        block57: {
            block55: {
                block56: {
                    block54: {
                        block53: {
                            v0 = MinecraftInstance.mc.getThePlayer();
                            if (v0 == null) {
                                return;
                            }
                            var2_2 = v0;
                            if (!var2_2.getOnGround()) {
                                var3_3 = this.airtime;
                                this.airtime = var3_3 + 1;
                            } else {
                                if (StringsKt.equals((String)((String)this.placeConditionValue.get()), (String)"falldown", (boolean)true) || StringsKt.equals((String)((String)this.placeConditionValue.get()), (String)"delayair", (boolean)true)) {
                                    this.delay = 0L;
                                    this.delayTimer.reset();
                                    this.eagleSneaking = false;
                                    this.shouldGoDown = false;
                                    this.canPlace = false;
                                    this.canRot = false;
                                    this.f = false;
                                    this.launchY = MathKt.roundToInt((double)var2_2.getPosY());
                                    this.slot = var2_2.getInventory().getCurrentItem();
                                    this.facesBlock = false;
                                }
                                this.airtime = 0;
                            }
                            this.f = this.airtime > ((Number)this.airticks.get()).intValue();
                            this.n = this.airtime > ((Number)this.Rotairticks.get()).intValue();
                            MinecraftInstance.mc.getTimer().setTimerSpeed(((Number)this.timerValue.get()).floatValue());
                            if (!StringsKt.equals((String)((String)this.placeConditionValue.get()), (String)"falldown", (boolean)true)) break block53;
                            v1 = MinecraftInstance.mc.getThePlayer();
                            if (v1 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (v1.getFallDistance() > (float)false) ** GOTO lbl-1000
                        }
                        if (StringsKt.equals((String)((String)this.placeConditionValue.get()), (String)"always", (boolean)true)) ** GOTO lbl-1000
                        if (StringsKt.equals((String)((String)this.placeConditionValue.get()), (String)"delayair", (boolean)true)) {
                            v2 = MinecraftInstance.mc.getThePlayer();
                            if (v2 == null) {
                                Intrinsics.throwNpe();
                            }
                            ** if (v2.getOnGround() || !this.f) goto lbl-1000
                        }
                        ** GOTO lbl-1000
lbl-1000:
                        // 3 sources

                        {
                            v3 = true;
                            ** GOTO lbl40
                        }
lbl-1000:
                        // 2 sources

                        {
                            v3 = this.canPlace = false;
                        }
lbl40:
                        // 2 sources

                        if (!StringsKt.equals((String)((String)this.RotConditionValue.get()), (String)"falldown", (boolean)true)) break block54;
                        v4 = MinecraftInstance.mc.getThePlayer();
                        if (v4 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (v4.getFallDistance() > (float)false) ** GOTO lbl-1000
                    }
                    if (StringsKt.equals((String)((String)this.RotConditionValue.get()), (String)"always", (boolean)true)) ** GOTO lbl-1000
                    if (StringsKt.equals((String)((String)this.RotConditionValue.get()), (String)"delayair", (boolean)true)) {
                        v5 = MinecraftInstance.mc.getThePlayer();
                        if (v5 == null) {
                            Intrinsics.throwNpe();
                        }
                        ** if (v5.getOnGround() || !this.n) goto lbl-1000
                    }
                    ** GOTO lbl-1000
lbl-1000:
                    // 3 sources

                    {
                        v6 = true;
                        ** GOTO lbl56
                    }
lbl-1000:
                    // 2 sources

                    {
                        v6 = this.canRot = false;
                    }
lbl56:
                    // 2 sources

                    if (StringsKt.equals((String)((String)this.sprintModeValue.get()), (String)"off", (boolean)true)) break block55;
                    if (!StringsKt.equals((String)((String)this.sprintModeValue.get()), (String)"ground", (boolean)true)) break block56;
                    v7 = MinecraftInstance.mc.getThePlayer();
                    if (v7 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!v7.getOnGround()) break block55;
                }
                if (!StringsKt.equals((String)((String)this.sprintModeValue.get()), (String)"air", (boolean)true)) break block57;
                v8 = MinecraftInstance.mc.getThePlayer();
                if (v8 == null) {
                    Intrinsics.throwNpe();
                }
                if (!v8.getOnGround()) break block57;
            }
            v9 = MinecraftInstance.mc.getThePlayer();
            if (v9 == null) {
                Intrinsics.throwNpe();
            }
            v9.setSprinting(false);
        }
        v10 = this.shouldGoDown = (Boolean)this.downValue.get() != false && (Boolean)this.sameYValue.get() == false && GameSettings.func_100015_a((KeyBinding)MinecraftInstance.mc2.field_71474_y.field_74311_E) != false && this.getBlocksAmount() > 1;
        if (this.shouldGoDown) {
            MinecraftInstance.mc.getGameSettings().getKeyBindSneak().setPressed(false);
        }
        if (((Boolean)this.slowValue.get()).booleanValue()) {
            var2_2.setMotionX(var2_2.getMotionX() * ((Number)this.slowSpeed.get()).doubleValue());
            var2_2.setMotionZ(var2_2.getMotionZ() * ((Number)this.slowSpeed.get()).doubleValue());
        }
        if (!StringsKt.equals((String)((String)this.eagleValue.get()), (String)"Off", (boolean)true) && !this.shouldGoDown) {
            var3_4 = 0.5;
            var5_6 = new WBlockPos(var2_2.getPosX(), var2_2.getPosY() - 1.0, var2_2.getPosZ());
            if (((Number)this.edgeDistanceValue.get()).floatValue() > (float)false) {
                for (EnumFacingType var6_10 : EnumFacingType.values()) {
                    if (var6_10 == EnumFacing.UP || var6_10 == EnumFacing.DOWN) continue;
                    var10_12 = MinecraftInstance.classProvider.getEnumFacing(var6_10);
                    var11_13 = WBlockPos.offset$default(var5_6, var10_12, 0, 2, null);
                    var12_14 = false;
                    v11 = BlockUtils.getMaterial(var11_13);
                    if (!(v11 != null ? v11.isReplaceable() : false)) continue;
                    if (var6_10 == EnumFacing.NORTH || var6_10 == EnumFacing.SOUTH) {
                        var14_16 = (double)var11_13.getZ() + 0.5 - var2_2.getPosZ();
                        var16_17 = false;
                        v12 = Math.abs(var14_16);
                    } else {
                        var14_16 = (double)var11_13.getX() + 0.5 - var2_2.getPosX();
                        var16_17 = false;
                        v12 = Math.abs(var14_16);
                    }
                    var12_15 = v12 - 0.5;
                    if (!(var12_15 < var3_4)) continue;
                    var3_4 = var12_15;
                }
            }
            if (this.placedBlocksWithoutEagle >= ((Number)this.blocksToEagleValue.get()).intValue()) {
                var7_9 = 0;
                v13 = BlockUtils.getMaterial(var5_6);
                v14 = var6_11 = (v13 != null ? v13.isReplaceable() : false) != false || ((Number)this.edgeDistanceValue.get()).floatValue() > (float)false && var3_4 < ((Number)this.edgeDistanceValue.get()).doubleValue() ? 1 : 0;
                if (StringsKt.equals((String)((String)this.eagleValue.get()), (String)"Silent", (boolean)true)) {
                    if (this.eagleSneaking != var6_11) {
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(var2_2, var6_11 != 0 ? ICPacketEntityAction.WAction.START_SNEAKING : ICPacketEntityAction.WAction.STOP_SNEAKING));
                    }
                    this.eagleSneaking = var6_11;
                } else {
                    MinecraftInstance.mc.getGameSettings().getKeyBindSneak().setPressed((boolean)var6_11);
                }
                this.placedBlocksWithoutEagle = 0;
            } else {
                var6_11 = this.placedBlocksWithoutEagle;
                this.placedBlocksWithoutEagle = var6_11 + 1;
            }
        }
        if (var2_2.getOnGround()) {
            var3_5 = (String)this.modeValue.get();
            var4_18 = false;
            v15 = var3_5;
            if (v15 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            var3_5 = v15.toLowerCase();
            switch (var3_5.hashCode()) {
                case 1388740000: {
                    if (!var3_5.equals("rewinside")) break;
                    MovementUtils.strafe(0.2f);
                    var2_2.setMotionY(0.0);
                    break;
                }
            }
            var3_5 = (String)this.zitterMode.get();
            var4_18 = false;
            v16 = var3_5;
            if (v16 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            var3_5 = v16.toLowerCase();
            tmp = -1;
            switch (var3_5.hashCode()) {
                case -1360201941: {
                    if (!var3_5.equals("teleport")) break;
                    tmp = 1;
                    break;
                }
                case 109935: {
                    if (!var3_5.equals("off")) break;
                    tmp = 2;
                    break;
                }
                case -898533970: {
                    if (!var3_5.equals("smooth")) break;
                    tmp = 3;
                    break;
                }
            }
            switch (tmp) {
                case 2: {
                    return;
                }
                case 3: {
                    if (!GameSettings.func_100015_a((KeyBinding)MinecraftInstance.mc2.field_71474_y.field_74366_z)) {
                        MinecraftInstance.mc.getGameSettings().getKeyBindRight().setPressed(false);
                    }
                    if (!GameSettings.func_100015_a((KeyBinding)MinecraftInstance.mc2.field_71474_y.field_74370_x)) {
                        MinecraftInstance.mc.getGameSettings().getKeyBindLeft().setPressed(false);
                    }
                    if (this.zitterTimer.hasTimePassed(100L)) {
                        this.zitterDirection = this.zitterDirection == false;
                        this.zitterTimer.reset();
                    }
                    if (this.zitterDirection) {
                        MinecraftInstance.mc.getGameSettings().getKeyBindRight().setPressed(true);
                        MinecraftInstance.mc.getGameSettings().getKeyBindLeft().setPressed(false);
                        break;
                    }
                    MinecraftInstance.mc.getGameSettings().getKeyBindRight().setPressed(false);
                    MinecraftInstance.mc.getGameSettings().getKeyBindLeft().setPressed(true);
                    break;
                }
                case 1: {
                    MovementUtils.strafe(((Number)this.zitterSpeed.get()).floatValue());
                    var4_19 = Math.toRadians((double)var2_2.getRotationYaw() + (this.zitterDirection != false ? 90.0 : -90.0));
                    var18_20 = var2_2.getMotionX();
                    var17_21 = var2_2;
                    var6_11 = 0;
                    var20_22 = Math.sin(var4_19);
                    var17_21.setMotionX(var18_20 - var20_22 * ((Number)this.zitterStrength.get()).doubleValue());
                    var18_20 = var2_2.getMotionZ();
                    var17_21 = var2_2;
                    var6_11 = 0;
                    var20_22 = Math.cos(var4_19);
                    var17_21.setMotionZ(var18_20 + var20_22 * ((Number)this.zitterStrength.get()).doubleValue());
                    this.zitterDirection = this.zitterDirection == false;
                    break;
                }
            }
        }
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    @EventTarget
    public final void onRender2D(Render2DEvent render2DEvent) {
        if (((Boolean)this.counterDisplayValue.get()).booleanValue()) {
            GL11.glPushMatrix();
            Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(BlockOverlay.class);
            if (module == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.BlockOverlay");
            }
            BlockOverlay blockOverlay = (BlockOverlay)module;
            if (blockOverlay.getState() && ((Boolean)blockOverlay.getInfoValue().get()).booleanValue() && blockOverlay.getCurrentBlock() != null) {
                GL11.glTranslatef((float)0.0f, (float)15.0f, (float)0.0f);
            }
            String string = "Blocks: \u00a77" + this.getBlocksAmount();
            ScaledResolution scaledResolution = new ScaledResolution(MinecraftInstance.mc2);
            RenderUtils.drawBorderedRect((float)(scaledResolution.func_78326_a() / 2) - (float)2, (float)(scaledResolution.func_78328_b() / 2) + (float)5, (float)(scaledResolution.func_78326_a() / 2 + Fonts.font40.getStringWidth(string)) + (float)2, (float)(scaledResolution.func_78328_b() / 2) + (float)16, 3.0f, Color.BLACK.getRGB(), Color.BLACK.getRGB());
            GlStateManager.func_179117_G();
            Fonts.font40.drawString(string, (float)scaledResolution.func_78326_a() / (float)2, (float)(scaledResolution.func_78328_b() / 2) + (float)7, Color.WHITE.getRGB());
            GL11.glPopMatrix();
        }
    }
}

