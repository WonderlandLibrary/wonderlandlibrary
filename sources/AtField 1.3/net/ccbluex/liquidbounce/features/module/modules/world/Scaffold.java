/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.item.ItemStack
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import java.awt.Color;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemBlock;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketHeldItemChange;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.BlurEvent;
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
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.world.Tower;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.block.PlaceInfo;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="Scaffold", description="LiquidBounce+", category=ModuleCategory.WORLD, keyBind=23)
public class Scaffold
extends Module {
    private final BoolValue autoJumpValue;
    private final IntegerValue minDelayValue;
    private final ListValue zitterModeValue;
    private PlaceInfo targetPlace;
    private long lastMS = 0L;
    public final ListValue modeValue = new ListValue("Mode", new String[]{"Normal", "Rewinside", "Expand"}, "Normal");
    private boolean shouldGoDown = false;
    private final FloatValue maxTurnSpeedValue;
    private final BoolValue counterDisplayValue;
    private final BoolValue zitterValue;
    private final BoolValue safeWalkValue;
    private float progress = 0.0f;
    private final BoolValue slowValue;
    private final ListValue eagleValue;
    private final BoolValue picker;
    private final BoolValue searchValue;
    private final IntegerValue expandLengthValue;
    private final BoolValue placeableDelay;
    private int slot;
    private final FloatValue slowSpeed;
    private final IntegerValue searchAccuracyValue;
    private final IntegerValue maxDelayValue;
    private final FloatValue timerValue;
    private final BoolValue downValue;
    private final BoolValue rotationStrafeValue;
    private final FloatValue staticPitchValue;
    private final IntegerValue keepLengthValue;
    public final BoolValue sprintValue = new BoolValue("Sprint", true);
    private boolean eagleSneaking;
    private boolean zitterDirection;
    private final BoolValue smartSpeedValue;
    private final FloatValue staticYawOffsetValue;
    private float spinYaw = 0.0f;
    private final BoolValue sameYValue;
    private final BoolValue silentRotation;
    private final FloatValue xzRangeValue;
    private final ListValue placeModeValue;
    private final BoolValue keepRotationValue;
    private final FloatValue zitterSpeed;
    private final BoolValue markValue;
    private final MSTimer delayTimer;
    private boolean facesBlock = false;
    public static final ListValue blockCounter = new ListValue("blockCounter", new String[]{"MC", "Normal", "Sigma"}, "Normal");
    private final ListValue rotationModeValue;
    private final FloatValue edgeDistanceValue;
    private final FloatValue minTurnSpeedValue;
    private Rotation lockRotation;
    private int placedBlocksWithoutEagle = 0;
    private final MSTimer zitterTimer;
    private final BoolValue swingValue;
    private final ListValue autoBlockValue;
    private final BoolValue tower;
    private int launchY;
    private final IntegerValue blocksToEagleValue;
    private final FloatValue speedModifierValue;
    private final BoolValue airSafeValue;
    private long delay;
    private Rotation limitedRotation;
    private final FloatValue zitterStrength;
    private final FloatValue yRangeValue;

    public int getBiggestBlockSlotInv() {
        int n = -1;
        int n2 = 0;
        if (this.getBlocksAmount() == 0) {
            return -1;
        }
        for (int i = 9; i < 36; ++i) {
            if (!mc.getThePlayer().getInventoryContainer().getSlot(i).getHasStack()) continue;
            IItem iItem = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack().getItem();
            IItemStack iItemStack = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack();
            if (!(iItem instanceof IItemBlock) || iItemStack.getStackSize() <= n2) continue;
            n2 = iItemStack.getStackSize();
            n = i;
        }
        return n;
    }

    protected void swap(int n, int n2) {
        mc.getPlayerController().windowClick(mc.getThePlayer().getInventoryContainer().getWindowId(), n, n2, 2, mc.getThePlayer());
    }

    private void setRotation(Rotation rotation) {
        this.setRotation(rotation, 0);
    }

    private void place() {
        if (this.targetPlace == null) {
            if (((Boolean)this.placeableDelay.get()).booleanValue()) {
                this.delayTimer.reset();
            }
            return;
        }
        if (!this.delayTimer.hasTimePassed(this.delay) || ((Boolean)this.sameYValue.get()).booleanValue() && this.launchY - 1 != (int)this.targetPlace.getVec3().getYCoord()) {
            return;
        }
        int n = -1;
        IItemStack iItemStack = mc.getThePlayer().getHeldItem();
        if (iItemStack == null || !classProvider.isItemBlock(iItemStack.getItem()) || classProvider.isBlockBush(iItemStack.getItem().asItemBlock().getBlock()) || mc.getThePlayer().getHeldItem().getStackSize() <= 0) {
            if (((String)this.autoBlockValue.get()).equalsIgnoreCase("Off")) {
                return;
            }
            n = InventoryUtils.findAutoBlockBlock();
            if (n == -1) {
                return;
            }
            if (((String)this.autoBlockValue.get()).equalsIgnoreCase("Spoof")) {
                if (n - 36 != this.slot) {
                    mc.getNetHandler().addToSendQueue(classProvider.createCPacketHeldItemChange(n - 36));
                }
            } else if (((String)this.autoBlockValue.get()).equalsIgnoreCase("Switch")) {
                mc.getNetHandler().addToSendQueue(classProvider.createCPacketHeldItemChange(n - 36));
            } else {
                mc.getThePlayer().getInventory().setCurrentItem(n - 36);
                mc.getPlayerController().updateController();
            }
            iItemStack = mc.getThePlayer().getInventoryContainer().getSlot(n).getStack();
        }
        if (mc.getPlayerController().onPlayerRightClick(mc.getThePlayer(), mc.getTheWorld(), iItemStack, this.targetPlace.getBlockPos(), this.targetPlace.getEnumFacing(), this.targetPlace.getVec3())) {
            this.delayTimer.reset();
            this.delay = TimeUtils.randomDelay((Integer)this.minDelayValue.get(), (Integer)this.maxDelayValue.get());
            if (mc.getThePlayer().getOnGround()) {
                float f = ((Float)this.speedModifierValue.get()).floatValue();
                mc.getThePlayer().setMotionX(mc.getThePlayer().getMotionX() * (double)f);
                mc.getThePlayer().setMotionZ(mc.getThePlayer().getMotionZ() * (double)f);
            }
            if (((Boolean)this.swingValue.get()).booleanValue()) {
                mc.getThePlayer().swingItem();
            } else {
                mc.getNetHandler().addToSendQueue(classProvider.createCPacketAnimation());
            }
        }
        this.targetPlace = null;
    }

    @Override
    public void onDisable() {
        if (mc.getThePlayer() == null) {
            return;
        }
        if (!mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindSneak())) {
            mc.getGameSettings().getKeyBindSneak().setPressed(false);
            if (this.eagleSneaking) {
                mc.getNetHandler().addToSendQueue(classProvider.createCPacketEntityAction(mc.getThePlayer(), ICPacketEntityAction.WAction.STOP_SNEAKING));
            }
        }
        if (!mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindRight())) {
            mc.getGameSettings().getKeyBindRight().setPressed(false);
        }
        if (!mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindLeft())) {
            mc.getGameSettings().getKeyBindLeft().setPressed(false);
        }
        this.lockRotation = null;
        this.limitedRotation = null;
        this.facesBlock = false;
        mc.getTimer().setTimerSpeed(1.0f);
        this.shouldGoDown = false;
        if (this.slot != mc.getThePlayer().getInventory().getCurrentItem()) {
            mc.getNetHandler().addToSendQueue(classProvider.createCPacketHeldItemChange(mc.getThePlayer().getInventory().getCurrentItem()));
        }
    }

    @Override
    public void onEnable() {
        if (mc.getThePlayer() == null) {
            return;
        }
        this.launchY = (int)mc.getThePlayer().getPosY();
        this.lastMS = System.currentTimeMillis();
    }

    static IntegerValue access$000(Scaffold scaffold) {
        return scaffold.minDelayValue;
    }

    @EventTarget
    public void onRender3D(Render3DEvent render3DEvent) {
        if (!((Boolean)this.markValue.get()).booleanValue()) {
            return;
        }
        for (int i = 0; i < (((String)this.modeValue.get()).equalsIgnoreCase("Expand") ? (Integer)this.expandLengthValue.get() + 1 : 2); ++i) {
            WBlockPos wBlockPos = new WBlockPos(mc.getThePlayer().getPosX() + (double)(mc.getThePlayer().getHorizontalFacing().equals(classProvider.getEnumFacing(EnumFacingType.WEST)) ? -i : (mc.getThePlayer().getHorizontalFacing().equals(classProvider.getEnumFacing(EnumFacingType.EAST)) ? i : 0)), mc.getThePlayer().getPosY() - (mc.getThePlayer().getPosY() == (double)((int)mc.getThePlayer().getPosY()) + 0.5 ? 0.0 : 1.0) - (this.shouldGoDown ? 1.0 : 0.0), mc.getThePlayer().getPosZ() + (double)(mc.getThePlayer().getHorizontalFacing().equals(classProvider.getEnumFacing(EnumFacingType.NORTH)) ? -i : (mc.getThePlayer().getHorizontalFacing().equals(classProvider.getEnumFacing(EnumFacingType.SOUTH)) ? i : 0)));
            PlaceInfo placeInfo = PlaceInfo.get(wBlockPos);
            if (!BlockUtils.isReplaceable(wBlockPos) || placeInfo == null) continue;
            RenderUtils.drawBlockBox(wBlockPos, new Color(68, 117, 255, 100), false);
            break;
        }
    }

    @EventTarget
    public void onMove(MoveEvent moveEvent) {
        if (!((Boolean)this.safeWalkValue.get()).booleanValue() || this.shouldGoDown) {
            return;
        }
        if (((Boolean)this.airSafeValue.get()).booleanValue() || mc.getThePlayer().getOnGround()) {
            moveEvent.setSafeWalk(true);
        }
    }

    public int getBiggestBlockSlotHotbar() {
        int n = -1;
        int n2 = 0;
        if (this.getBlocksAmount() == 0) {
            return -1;
        }
        for (int i = 36; i < 45; ++i) {
            if (!mc.getThePlayer().getInventoryContainer().getSlot(i).getHasStack()) continue;
            IItem iItem = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack().getItem();
            IItemStack iItemStack = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack();
            if (!(iItem instanceof IItemBlock) || iItemStack.getStackSize() <= n2) continue;
            n2 = iItemStack.getStackSize();
            n = i;
        }
        return n;
    }

    @EventTarget
    public void onPacket(PacketEvent packetEvent) {
        if (mc.getThePlayer() == null) {
            return;
        }
        IPacket iPacket = packetEvent.getPacket();
        if (classProvider.isCPacketHeldItemChange(iPacket)) {
            ICPacketHeldItemChange iCPacketHeldItemChange = iPacket.asCPacketHeldItemChange();
            this.slot = iCPacketHeldItemChange.getSlotId();
        }
    }

    @EventTarget
    public void shader(BlurEvent blurEvent) {
        GlStateManager.func_179117_G();
        ScaledResolution scaledResolution = new ScaledResolution(mc2);
        int n = scaledResolution.func_78328_b() - 90;
        if (this.getBlocksAmount() == 0) {
            return;
        }
        if (blockCounter.get() == "Normal") {
            this.progress = (float)(System.currentTimeMillis() - this.lastMS) / 600.0f;
        }
        if (this.progress >= 1.0f) {
            this.progress = 1.0f;
        }
        double d = EaseUtils.INSTANCE.easeOutBack(this.progress);
        GL11.glPushMatrix();
        GL11.glTranslated((double)((double)((float)scaledResolution.func_78326_a() / 2.0f - 15.0f + 15.0f) * (1.0 - d)), (double)(((double)n + 17.5) * (1.0 - d)), (double)0.0);
        GL11.glScaled((double)d, (double)d, (double)d);
        RenderUtils.drawRoundedRect2((float)scaledResolution.func_78326_a() / 2.0f - 15.0f, n, 30.0f, 35.0f, 0.0f, new Color(0, 0, 0, 255).getRGB());
        GL11.glPopMatrix();
        GlStateManager.func_179117_G();
    }

    /*
     * Exception decompiling
     */
    private void findBlock(boolean var1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl221 : ILOAD - null : trying to set 4 previously set to 2
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

    public Scaffold() {
        this.maxDelayValue = new IntegerValue(this, "MaxDelay", 0, 0, 1000){
            final Scaffold this$0;
            {
                this.this$0 = scaffold;
                super(string, n, n2, n3);
            }

            protected void onChanged(Integer n, Integer n2) {
                int n3 = (Integer)Scaffold.access$000(this.this$0).get();
                if (n3 > n2) {
                    this.set((Object)n3);
                }
            }

            @Override
            protected void onChanged(Object object, Object object2) {
                this.onChanged((Integer)object, (Integer)object2);
            }
        };
        this.placeableDelay = new BoolValue("PlaceableDelay", false);
        this.minDelayValue = new IntegerValue(this, "MinDelay", 0, 0, 1000){
            final Scaffold this$0;

            @Override
            protected void onChanged(Object object, Object object2) {
                this.onChanged((Integer)object, (Integer)object2);
            }
            {
                this.this$0 = scaffold;
                super(string, n, n2, n3);
            }

            protected void onChanged(Integer n, Integer n2) {
                int n3 = (Integer)Scaffold.access$100(this.this$0).get();
                if (n3 < n2) {
                    this.set((Object)n3);
                }
            }
        };
        this.tower = new BoolValue("Tower", true);
        this.autoBlockValue = new ListValue("AutoBlock", new String[]{"Off", "Spoof", "Switch"}, "Spoof");
        this.swingValue = new BoolValue("Swing", true);
        this.searchValue = new BoolValue("Search", true);
        this.downValue = new BoolValue("Down", true);
        this.picker = new BoolValue("Picker", false);
        this.placeModeValue = new ListValue("PlaceTiming", new String[]{"Pre", "Post"}, "Post");
        this.eagleValue = new ListValue("Eagle", new String[]{"Normal", "EdgeDistance", "Silent", "Off"}, "Off");
        this.blocksToEagleValue = new IntegerValue("BlocksToEagle", 0, 0, 10);
        this.edgeDistanceValue = new FloatValue("EagleEdgeDistance", 0.2f, 0.0f, 0.5f);
        this.expandLengthValue = new IntegerValue("ExpandLength", 5, 1, 6);
        this.rotationStrafeValue = new BoolValue("RotationStrafe", false);
        this.rotationModeValue = new ListValue("RotationMode", new String[]{"Normal", "Static", "StaticPitch", "StaticYaw", "Off"}, "Normal");
        this.silentRotation = new BoolValue("SilentRotation", true);
        this.keepRotationValue = new BoolValue("KeepRotation", false);
        this.keepLengthValue = new IntegerValue("KeepRotationLength", 0, 0, 20);
        this.staticPitchValue = new FloatValue("StaticPitchOffset", 86.0f, 70.0f, 90.0f);
        this.staticYawOffsetValue = new FloatValue("StaticYawOffset", 0.0f, 0.0f, 90.0f);
        this.xzRangeValue = new FloatValue("xzRange", 0.8f, 0.1f, 1.0f);
        this.yRangeValue = new FloatValue("yRange", 0.8f, 0.1f, 1.0f);
        this.searchAccuracyValue = new IntegerValue(this, "SearchAccuracy", 8, 1, 24){
            final Scaffold this$0;

            protected void onChanged(Integer n, Integer n2) {
                if (this.getMaximum() < n2) {
                    this.set((Object)this.getMaximum());
                } else if (this.getMinimum() > n2) {
                    this.set((Object)this.getMinimum());
                }
            }

            @Override
            protected void onChanged(Object object, Object object2) {
                this.onChanged((Integer)object, (Integer)object2);
            }
            {
                this.this$0 = scaffold;
                super(string, n, n2, n3);
            }
        };
        this.smartSpeedValue = new BoolValue("SmartSpeed", false);
        this.autoJumpValue = new BoolValue("AutoJump", false);
        this.zitterValue = new BoolValue("Zitter", false);
        this.zitterModeValue = new ListValue("ZitterMode", new String[]{"Teleport", "Smooth"}, "Teleport");
        this.zitterSpeed = new FloatValue("ZitterSpeed", 0.13f, 0.1f, 0.3f);
        this.maxTurnSpeedValue = new FloatValue(this, "MaxTurnSpeed", 180.0f, 1.0f, 180.0f){
            final Scaffold this$0;
            {
                this.this$0 = scaffold;
                super(string, f, f2, f3);
            }

            protected void onChanged(Float f, Float f2) {
                float f3 = ((Float)Scaffold.access$200(this.this$0).get()).floatValue();
                if (f3 > f2.floatValue()) {
                    this.set((Object)Float.valueOf(f3));
                }
                if (this.getMaximum() < f2.floatValue()) {
                    this.set((Object)Float.valueOf(this.getMaximum()));
                } else if (this.getMinimum() > f2.floatValue()) {
                    this.set((Object)Float.valueOf(this.getMinimum()));
                }
            }

            @Override
            protected void onChanged(Object object, Object object2) {
                this.onChanged((Float)object, (Float)object2);
            }
        };
        this.zitterStrength = new FloatValue("ZitterStrength", 0.072f, 0.05f, 0.2f);
        this.minTurnSpeedValue = new FloatValue(this, "MinTurnSpeed", 180.0f, 1.0f, 180.0f){
            final Scaffold this$0;
            {
                this.this$0 = scaffold;
                super(string, f, f2, f3);
            }

            protected void onChanged(Float f, Float f2) {
                float f3 = ((Float)Scaffold.access$300(this.this$0).get()).floatValue();
                if (f3 < f2.floatValue()) {
                    this.set((Object)Float.valueOf(f3));
                }
                if (this.getMaximum() < f2.floatValue()) {
                    this.set((Object)Float.valueOf(this.getMaximum()));
                } else if (this.getMinimum() > f2.floatValue()) {
                    this.set((Object)Float.valueOf(this.getMinimum()));
                }
            }

            @Override
            protected void onChanged(Object object, Object object2) {
                this.onChanged((Float)object, (Float)object2);
            }
        };
        this.timerValue = new FloatValue("Timer", 1.0f, 0.1f, 10.0f);
        this.speedModifierValue = new FloatValue("SpeedModifier", 1.0f, 0.0f, 2.0f);
        this.slowValue = new BoolValue(this, "Slow", false){
            final Scaffold this$0;

            @Override
            protected void onChanged(Object object, Object object2) {
                this.onChanged((Boolean)object, (Boolean)object2);
            }
            {
                this.this$0 = scaffold;
                super(string, bl);
            }

            protected void onChanged(Boolean bl, Boolean bl2) {
                if (bl2.booleanValue()) {
                    this.this$0.sprintValue.set(false);
                }
            }
        };
        this.slowSpeed = new FloatValue("SlowSpeed", 0.6f, 0.2f, 0.8f);
        this.sameYValue = new BoolValue("SameY", false);
        this.safeWalkValue = new BoolValue("SafeWalk", true);
        this.airSafeValue = new BoolValue("AirSafe", false);
        this.counterDisplayValue = new BoolValue("Counter", true);
        this.markValue = new BoolValue("Mark", false);
        this.delayTimer = new MSTimer();
        this.zitterTimer = new MSTimer();
    }

    private void setRotation(Rotation rotation, int n) {
        if (((Boolean)this.silentRotation.get()).booleanValue()) {
            RotationUtils.setTargetRotation(rotation, n);
        } else {
            mc.getThePlayer().setRotationYaw(rotation.getYaw());
            mc.getThePlayer().setRotationPitch(rotation.getPitch());
        }
    }

    static IntegerValue access$100(Scaffold scaffold) {
        return scaffold.maxDelayValue;
    }

    private void update() {
        boolean bl;
        boolean bl2 = bl = mc.getThePlayer().getHeldItem() != null && classProvider.isItemBlock(mc.getThePlayer().getHeldItem().getItem());
        if (!((String)this.autoBlockValue.get()).equalsIgnoreCase("Off") ? InventoryUtils.findAutoBlockBlock() == -1 && !bl : !bl) {
            return;
        }
        this.findBlock(((String)this.modeValue.get()).equalsIgnoreCase("expand"));
    }

    @EventTarget
    public void onMotion(MotionEvent motionEvent) {
        EventState eventState = motionEvent.getEventState();
        if (!((String)this.rotationModeValue.get()).equalsIgnoreCase("Off") && ((Boolean)this.keepRotationValue.get()).booleanValue() && this.lockRotation != null) {
            this.setRotation(this.lockRotation);
        }
        if ((this.facesBlock || ((String)this.rotationModeValue.get()).equalsIgnoreCase("Off")) && ((String)this.placeModeValue.get()).equalsIgnoreCase(eventState.getStateName())) {
            this.place();
        }
        if (eventState == EventState.PRE) {
            this.update();
        }
        if (this.targetPlace == null && ((Boolean)this.placeableDelay.get()).booleanValue()) {
            this.delayTimer.reset();
        }
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    private double calcStepSize(double d) {
        double d2 = ((Integer)this.searchAccuracyValue.get()).intValue();
        d2 += d2 % 2.0;
        return Math.max(d / d2, 0.01);
    }

    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        this.getBestBlocks();
        mc.getTimer().setTimerSpeed(((Float)this.timerValue.get()).floatValue());
        if (((Boolean)this.tower.get()).booleanValue() && Keyboard.isKeyDown((int)57) && !LiquidBounce.moduleManager.getModule(Speed.class).getState()) {
            Scaffold.mc2.field_71439_g.field_70122_E = false;
            LiquidBounce.moduleManager.getModule(Tower.class).setState(true);
        } else {
            LiquidBounce.moduleManager.getModule(Tower.class).setState(false);
        }
        boolean bl = this.shouldGoDown = (Boolean)this.downValue.get() != false && (Boolean)this.sameYValue.get() == false && mc.getGameSettings().getKeyBindSneak().isKeyDown() && this.getBlocksAmount() > 1;
        if (this.shouldGoDown) {
            mc.getGameSettings().getKeyBindSneak().setPressed(false);
        }
        if (((Boolean)this.slowValue.get()).booleanValue()) {
            mc.getThePlayer().setMotionX(mc.getThePlayer().getMotionX() * (double)((Float)this.slowSpeed.get()).floatValue());
            mc.getThePlayer().setMotionZ(mc.getThePlayer().getMotionZ() * (double)((Float)this.slowSpeed.get()).floatValue());
        }
        if (((Boolean)this.sprintValue.get()).booleanValue()) {
            if (!mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindSprint())) {
                mc.getGameSettings().getKeyBindSprint().setPressed(false);
            }
            if (mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindSprint())) {
                mc.getGameSettings().getKeyBindSprint().setPressed(true);
            }
            if (mc.getGameSettings().getKeyBindSprint().isKeyDown()) {
                mc.getThePlayer().setSprinting(true);
            }
            if (!mc.getGameSettings().getKeyBindSprint().isKeyDown()) {
                mc.getThePlayer().setSprinting(false);
            }
        }
        if (mc.getThePlayer().getOnGround()) {
            double d;
            String string = (String)this.modeValue.get();
            if (string.equalsIgnoreCase("Rewinside")) {
                MovementUtils.strafe(0.2f);
                mc.getThePlayer().setMotionY(0.0);
            }
            if (((Boolean)this.zitterValue.get()).booleanValue() && ((String)this.zitterModeValue.get()).equalsIgnoreCase("smooth")) {
                if (!mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindRight())) {
                    mc.getGameSettings().getKeyBindRight().setPressed(false);
                }
                if (!mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindLeft())) {
                    mc.getGameSettings().getKeyBindLeft().setPressed(false);
                }
                if (this.zitterTimer.hasTimePassed(100L)) {
                    this.zitterDirection = !this.zitterDirection;
                    this.zitterTimer.reset();
                }
                if (this.zitterDirection) {
                    mc.getGameSettings().getKeyBindRight().setPressed(true);
                    mc.getGameSettings().getKeyBindLeft().setPressed(false);
                } else {
                    mc.getGameSettings().getKeyBindRight().setPressed(false);
                    mc.getGameSettings().getKeyBindLeft().setPressed(true);
                }
            }
            if (!((String)this.eagleValue.get()).equalsIgnoreCase("Off") && !this.shouldGoDown) {
                boolean bl2;
                d = 0.5;
                if (((String)this.eagleValue.get()).equalsIgnoreCase("EdgeDistance") && !this.shouldGoDown) {
                    block6: for (bl2 = false; bl2 < 4 != 0; bl2 += 1) {
                        switch (bl2) {
                            case 0: {
                                double d2;
                                WBlockPos wBlockPos = new WBlockPos(mc.getThePlayer().getPosX() - 1.0, mc.getThePlayer().getPosY() - (mc.getThePlayer().getPosY() == (double)((int)mc.getThePlayer().getPosY()) + 0.5 ? 0.0 : 1.0), mc.getThePlayer().getPosZ());
                                PlaceInfo placeInfo = PlaceInfo.get(wBlockPos);
                                if (BlockUtils.isReplaceable(wBlockPos) && placeInfo != null) {
                                    d2 = mc.getThePlayer().getPosX() - (double)wBlockPos.getX();
                                    if ((d2 -= 0.5) < 0.0) {
                                        d2 *= -1.0;
                                    }
                                    if ((d2 -= 0.5) < d) {
                                        d = d2;
                                    }
                                }
                            }
                            case 1: {
                                double d2;
                                WBlockPos wBlockPos = new WBlockPos(mc.getThePlayer().getPosX() + 1.0, mc.getThePlayer().getPosY() - (mc.getThePlayer().getPosY() == (double)((int)mc.getThePlayer().getPosY()) + 0.5 ? 0.0 : 1.0), mc.getThePlayer().getPosZ());
                                PlaceInfo placeInfo = PlaceInfo.get(wBlockPos);
                                if (BlockUtils.isReplaceable(wBlockPos) && placeInfo != null) {
                                    d2 = mc.getThePlayer().getPosX() - (double)wBlockPos.getX();
                                    if ((d2 -= 0.5) < 0.0) {
                                        d2 *= -1.0;
                                    }
                                    if ((d2 -= 0.5) < d) {
                                        d = d2;
                                    }
                                }
                            }
                            case 2: {
                                double d2;
                                WBlockPos wBlockPos = new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() - (mc.getThePlayer().getPosY() == (double)((int)mc.getThePlayer().getPosY()) + 0.5 ? 0.0 : 1.0), mc.getThePlayer().getPosZ() - 1.0);
                                PlaceInfo placeInfo = PlaceInfo.get(wBlockPos);
                                if (BlockUtils.isReplaceable(wBlockPos) && placeInfo != null) {
                                    d2 = mc.getThePlayer().getPosZ() - (double)wBlockPos.getZ();
                                    if ((d2 -= 0.5) < 0.0) {
                                        d2 *= -1.0;
                                    }
                                    if ((d2 -= 0.5) < d) {
                                        d = d2;
                                    }
                                }
                            }
                            case 3: {
                                WBlockPos wBlockPos = new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() - (mc.getThePlayer().getPosY() == (double)((int)mc.getThePlayer().getPosY()) + 0.5 ? 0.0 : 1.0), mc.getThePlayer().getPosZ() + 1.0);
                                PlaceInfo placeInfo = PlaceInfo.get(wBlockPos);
                                if (!BlockUtils.isReplaceable(wBlockPos) || placeInfo == null) continue block6;
                                double d2 = mc.getThePlayer().getPosZ() - (double)wBlockPos.getZ();
                                if ((d2 -= 0.5) < 0.0) {
                                    d2 *= -1.0;
                                }
                                if (!((d2 -= 0.5) < d)) continue block6;
                                d = d2;
                            }
                        }
                    }
                }
                if (this.placedBlocksWithoutEagle >= (Integer)this.blocksToEagleValue.get()) {
                    boolean bl3 = bl2 = mc.getTheWorld().getBlockState(new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() - 1.0, mc.getThePlayer().getPosZ())).getBlock().equals(classProvider.getBlockEnum(BlockType.AIR)) || d < (double)((Float)this.edgeDistanceValue.get()).floatValue() && ((String)this.eagleValue.get()).equalsIgnoreCase("EdgeDistance");
                    if (((String)this.eagleValue.get()).equalsIgnoreCase("Silent") && !this.shouldGoDown) {
                        if (this.eagleSneaking != bl2) {
                            mc.getNetHandler().addToSendQueue(classProvider.createCPacketEntityAction(mc.getThePlayer(), bl2 ? ICPacketEntityAction.WAction.START_SNEAKING : ICPacketEntityAction.WAction.STOP_SNEAKING));
                        }
                        this.eagleSneaking = bl2;
                    } else {
                        mc.getGameSettings().getKeyBindSneak().setPressed(bl2);
                    }
                    this.placedBlocksWithoutEagle = 0;
                } else {
                    ++this.placedBlocksWithoutEagle;
                }
            }
            if (((Boolean)this.zitterValue.get()).booleanValue() && ((String)this.zitterModeValue.get()).equalsIgnoreCase("teleport")) {
                MovementUtils.strafe(((Float)this.zitterSpeed.get()).floatValue());
                d = Math.toRadians((double)mc.getThePlayer().getRotationYaw() + (this.zitterDirection ? 90.0 : -90.0));
                mc.getThePlayer().setMotionX(mc.getThePlayer().getMotionX() - Math.sin(d) * (double)((Float)this.zitterStrength.get()).floatValue());
                mc.getThePlayer().setMotionZ(mc.getThePlayer().getMotionZ() + Math.cos(d) * (double)((Float)this.zitterStrength.get()).floatValue());
                boolean bl4 = this.zitterDirection = !this.zitterDirection;
            }
            if (this.shouldGoDown) {
                this.launchY = (int)mc.getThePlayer().getPosY() - 1;
            } else if (!((Boolean)this.sameYValue.get()).booleanValue()) {
                if (!((Boolean)this.autoJumpValue.get()).booleanValue() && (!((Boolean)this.smartSpeedValue.get()).booleanValue() || !LiquidBounce.moduleManager.getModule(Speed.class).getState()) || GameSettings.func_100015_a((KeyBinding)Scaffold.mc2.field_71474_y.field_74314_A) || mc.getThePlayer().getPosY() < (double)this.launchY) {
                    this.launchY = (int)mc.getThePlayer().getPosY();
                }
                if (((Boolean)this.autoJumpValue.get()).booleanValue() && !LiquidBounce.moduleManager.getModule(Speed.class).getState() && MovementUtils.isMoving() && mc.getThePlayer().getOnGround()) {
                    mc.getThePlayer().jump();
                }
            }
        }
    }

    private int getBlocksAmount() {
        int n = 0;
        for (int i = 36; i < 45; ++i) {
            IItemStack iItemStack = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack();
            if (iItemStack == null || !classProvider.isItemBlock(iItemStack.getItem())) continue;
            IBlock iBlock = iItemStack.getItem().asItemBlock().getBlock();
            IItemStack iItemStack2 = mc.getThePlayer().getHeldItem();
            if ((iItemStack2 == null || !iItemStack2.equals(iItemStack)) && (InventoryUtils.BLOCK_BLACKLIST.contains(iBlock) || classProvider.isBlockBush(iBlock))) continue;
            n += iItemStack.getStackSize();
        }
        return n;
    }

    static FloatValue access$300(Scaffold scaffold) {
        return scaffold.maxTurnSpeedValue;
    }

    @EventTarget
    public void onRender2D(Render2DEvent render2DEvent) {
        int n;
        if (this.getBlocksAmount() == 0) {
            return;
        }
        this.progress = (float)(System.currentTimeMillis() - this.lastMS) / 600.0f;
        if (this.progress >= 1.0f) {
            this.progress = 1.0f;
        }
        ScaledResolution scaledResolution = new ScaledResolution(mc2);
        ItemStack itemStack = Scaffold.mc2.field_71439_g.field_71071_by.func_70301_a(this.slot);
        Color color = this.getBlocksAmount() <= 63 ? Color.RED : Color.GREEN;
        String string = this.getBlocksAmount() + " blocks";
        int n2 = Fonts.font35.getStringWidth(string);
        if (blockCounter.get() == "MC") {
            n = scaledResolution.func_78328_b() / 2;
            mc.getFontRendererObj().drawStringWithShadow(String.valueOf(this.getBlocksAmount()), (int)((float)scaledResolution.func_78326_a() / 2.0f + 1.0f), n + 9, color.getRGB());
            if (itemStack != null) {
                GlStateManager.func_179094_E();
                GlStateManager.func_179091_B();
                GlStateManager.func_179147_l();
                GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
                RenderHelper.func_74520_c();
                mc2.func_175599_af().func_180450_b(itemStack, (int)((float)scaledResolution.func_78326_a() / 2.0f - 17.0f), n + 4);
                GlStateManager.func_179101_C();
                GlStateManager.func_179084_k();
                RenderHelper.func_74518_a();
                GlStateManager.func_179121_F();
            } else {
                Fonts.font35.drawCenteredString("?", (float)scaledResolution.func_78326_a() / 2.0f + 0.5f, n + 6, -1);
            }
        }
        if (blockCounter.get() == "Normal") {
            GlStateManager.func_179117_G();
            n = scaledResolution.func_78328_b() - 90;
            double d = EaseUtils.INSTANCE.easeOutBack(this.progress);
            GL11.glPushMatrix();
            GL11.glTranslated((double)((double)((float)scaledResolution.func_78326_a() / 2.0f - 15.0f + 15.0f) * (1.0 - d)), (double)(((double)n + 17.5) * (1.0 - d)), (double)0.0);
            GL11.glScaled((double)d, (double)d, (double)d);
            RenderUtils.drawRoundedRect2((float)scaledResolution.func_78326_a() / 2.0f - 15.0f, n, 30.0f, 35.0f, 0.0f, new Color(0, 0, 0, 50).getRGB());
            if (itemStack != null) {
                Fonts.font35.drawCenteredString(String.valueOf(this.getBlocksAmount()), (float)scaledResolution.func_78326_a() / 2.0f, n + 40 - 18, -1);
                GlStateManager.func_179094_E();
                GlStateManager.func_179091_B();
                GlStateManager.func_179147_l();
                GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
                RenderHelper.func_74520_c();
                mc2.func_175599_af().func_180450_b(itemStack, (int)((float)scaledResolution.func_78326_a() / 2.0f - 8.0f), n + 40 - 33);
                GlStateManager.func_179101_C();
                GlStateManager.func_179084_k();
                RenderHelper.func_74518_a();
                GlStateManager.func_179121_F();
            } else {
                Fonts.font35.drawCenteredString("?", (float)scaledResolution.func_78326_a() / 2.0f + 0.5f, n + 6, -1);
            }
            GlStateManager.func_179117_G();
            GL11.glPopMatrix();
        }
    }

    @EventTarget
    private void onStrafe(StrafeEvent strafeEvent) {
        if (!((Boolean)this.rotationStrafeValue.get()).booleanValue()) {
            return;
        }
        RotationUtils.serverRotation.applyStrafeToPlayer(strafeEvent);
        strafeEvent.cancelEvent();
    }

    /*
     * Exception decompiling
     */
    public void getBestBlocks() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl64 : IF_ICMPGE - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
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

    static FloatValue access$200(Scaffold scaffold) {
        return scaffold.minTurnSpeedValue;
    }
}

