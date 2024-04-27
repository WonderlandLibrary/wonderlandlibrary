/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.TypeCastException;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketEntityVelocity;
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.event.BlockBBEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.Value;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="Velocity", description="Edit your velocity", category=ModuleCategory.COMBAT)
public final class Velocity
extends Module {
    private boolean canCancelJump;
    private final ListValue modeValue;
    private final Value grimReduceNoMotionY;
    private final Value aacPushYReducerValue;
    private final BoolValue onlyGroundValue;
    private final Value customX;
    private final Value hytpacketaset;
    private final Value customY;
    private final Value newaac4XZReducerValue;
    private final BoolValue noFireValue;
    private final Value customC06FakeLag;
    private final Value reverse2StrengthValue;
    private final FloatValue horizontalValue = new FloatValue("Horizontal", 0.0f, 0.0f, 1.0f);
    private boolean jump;
    private MSTimer velocityTimer;
    private final Value hytpacketbset;
    private final Value reverseStrengthValue;
    private final BoolValue cobwebValue;
    private boolean canCleanJump;
    private final FloatValue verticalValue = new FloatValue("Vertical", 0.0f, 0.0f, 1.0f);
    private IBlock block;
    private boolean reverseHurt;
    private final Value aacPushXZReducerValue;
    private final Value customYStart;
    private int velocityTick;
    private final IntegerValue velocityTickValue;
    private final Value customZ;
    private boolean velocityInput;

    @EventTarget
    public final void onJump(JumpEvent jumpEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null || iEntityPlayerSP.isInWater() || iEntityPlayerSP.isInLava() || iEntityPlayerSP.isInWeb()) {
            return;
        }
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string2.toLowerCase()) {
            case "aacpush": {
                this.jump = true;
                if (iEntityPlayerSP.isCollidedVertically()) break;
                jumpEvent.cancelEvent();
                break;
            }
            case "aac4": {
                if (iEntityPlayerSP.getHurtTime() <= 0) break;
                jumpEvent.cancelEvent();
                break;
            }
            case "aaczero": {
                if (iEntityPlayerSP.getHurtTime() <= 0) break;
                jumpEvent.cancelEvent();
                break;
            }
        }
    }

    public final IBlock getBlock() {
        return this.block;
    }

    @EventTarget
    public final void onBlockBB(BlockBBEvent blockBBEvent) {
        this.block = blockBBEvent.getBlock();
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (iEntityPlayerSP2.isInWater() || iEntityPlayerSP2.isInLava() || iEntityPlayerSP2.isInWeb()) {
            return;
        }
        if (!iEntityPlayerSP2.isDead() && ((Boolean)this.noFireValue.get()).booleanValue()) {
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP3.isBurning() && ((Boolean)this.cobwebValue.get()).booleanValue()) {
                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP4.isInWeb() && ((Boolean)this.onlyGroundValue.get()).booleanValue() && MinecraftInstance.mc2.field_71439_g.field_70160_al) {
                    return;
                }
            }
        }
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string2.toLowerCase()) {
            case "jump": {
                if (iEntityPlayerSP2.getHurtTime() <= 0) break;
                iEntityPlayerSP2.setMotionY(0.42);
                float f = iEntityPlayerSP2.getRotationYaw() * ((float)Math.PI / 180);
                IEntityPlayerSP iEntityPlayerSP5 = iEntityPlayerSP2;
                double d = iEntityPlayerSP5.getMotionX();
                IEntityPlayerSP iEntityPlayerSP6 = iEntityPlayerSP5;
                boolean bl2 = false;
                float f2 = (float)Math.sin(f);
                iEntityPlayerSP6.setMotionX(d - (double)f2 * 0.2);
                IEntityPlayerSP iEntityPlayerSP7 = iEntityPlayerSP2;
                d = iEntityPlayerSP7.getMotionZ();
                iEntityPlayerSP6 = iEntityPlayerSP7;
                bl2 = false;
                f2 = (float)Math.cos(f);
                iEntityPlayerSP6.setMotionZ(d + (double)f2 * 0.2);
                break;
            }
            case "grimreduce": {
                if (iEntityPlayerSP2.getHurtTime() <= 0) break;
                IEntityPlayerSP iEntityPlayerSP8 = iEntityPlayerSP2;
                iEntityPlayerSP8.setMotionX(iEntityPlayerSP8.getMotionX() + -1.0E-7);
                if (!((Boolean)this.grimReduceNoMotionY.get()).booleanValue()) {
                    IEntityPlayerSP iEntityPlayerSP9 = iEntityPlayerSP2;
                    iEntityPlayerSP9.setMotionY(iEntityPlayerSP9.getMotionY() + -1.0E-7);
                }
                IEntityPlayerSP iEntityPlayerSP10 = iEntityPlayerSP2;
                iEntityPlayerSP10.setMotionZ(iEntityPlayerSP10.getMotionZ() + -1.0E-7);
                break;
            }
            case "glitch": {
                iEntityPlayerSP2.setNoClip(this.velocityInput);
                if (iEntityPlayerSP2.getHurtTime() == 7) {
                    iEntityPlayerSP2.setMotionY(0.4);
                }
                this.velocityInput = false;
                break;
            }
            case "aac5reduce": {
                if (iEntityPlayerSP2.getHurtTime() > 1 && this.velocityInput) {
                    IEntityPlayerSP iEntityPlayerSP11 = iEntityPlayerSP2;
                    iEntityPlayerSP11.setMotionX(iEntityPlayerSP11.getMotionX() * 0.81);
                    IEntityPlayerSP iEntityPlayerSP12 = iEntityPlayerSP2;
                    iEntityPlayerSP12.setMotionZ(iEntityPlayerSP12.getMotionZ() * 0.81);
                }
                if (!this.velocityInput || iEntityPlayerSP2.getHurtTime() >= 5 && !iEntityPlayerSP2.getOnGround() || !this.velocityTimer.hasTimePassed(120L)) break;
                this.velocityInput = false;
                break;
            }
            case "hyttick": {
                if (this.velocityTick > ((Number)this.velocityTickValue.get()).intValue()) {
                    if (iEntityPlayerSP2.getMotionY() > 0.0) {
                        iEntityPlayerSP2.setMotionY(0.0);
                    }
                    iEntityPlayerSP2.setMotionX(0.0);
                    iEntityPlayerSP2.setMotionZ(0.0);
                    iEntityPlayerSP2.setJumpMovementFactor(-1.0E-5f);
                    this.velocityInput = false;
                }
                if (this.velocityTick <= 1) break;
                this.velocityInput = false;
                break;
            }
            case "reverse": {
                if (!this.velocityInput) {
                    return;
                }
                if (!iEntityPlayerSP2.getOnGround()) {
                    MovementUtils.strafe(MovementUtils.INSTANCE.getSpeed() * ((Number)this.reverseStrengthValue.get()).floatValue());
                    break;
                }
                if (!this.velocityTimer.hasTimePassed(80L)) break;
                this.velocityInput = false;
                break;
            }
            case "aac4": {
                if (!iEntityPlayerSP2.getOnGround()) {
                    if (!this.velocityInput) break;
                    iEntityPlayerSP2.setSpeedInAir(0.02f);
                    IEntityPlayerSP iEntityPlayerSP13 = iEntityPlayerSP2;
                    iEntityPlayerSP13.setMotionX(iEntityPlayerSP13.getMotionX() * 0.6);
                    IEntityPlayerSP iEntityPlayerSP14 = iEntityPlayerSP2;
                    iEntityPlayerSP14.setMotionZ(iEntityPlayerSP14.getMotionZ() * 0.6);
                    break;
                }
                if (!this.velocityTimer.hasTimePassed(80L)) break;
                this.velocityInput = false;
                iEntityPlayerSP2.setSpeedInAir(0.02f);
                break;
            }
            case "newaac4": {
                if (iEntityPlayerSP2.getHurtTime() <= 0 || iEntityPlayerSP2.getOnGround()) break;
                float f = ((Number)this.newaac4XZReducerValue.get()).floatValue();
                IEntityPlayerSP iEntityPlayerSP15 = iEntityPlayerSP2;
                iEntityPlayerSP15.setMotionX(iEntityPlayerSP15.getMotionX() * (double)f);
                IEntityPlayerSP iEntityPlayerSP16 = iEntityPlayerSP2;
                iEntityPlayerSP16.setMotionZ(iEntityPlayerSP16.getMotionZ() * (double)f);
                break;
            }
            case "smoothreverse": {
                if (!this.velocityInput) {
                    iEntityPlayerSP2.setSpeedInAir(0.02f);
                    return;
                }
                if (iEntityPlayerSP2.getHurtTime() > 0) {
                    this.reverseHurt = true;
                }
                if (!iEntityPlayerSP2.getOnGround()) {
                    if (!this.reverseHurt) break;
                    iEntityPlayerSP2.setSpeedInAir(((Number)this.reverse2StrengthValue.get()).floatValue());
                    break;
                }
                if (!this.velocityTimer.hasTimePassed(80L)) break;
                this.velocityInput = false;
                this.reverseHurt = false;
                break;
            }
            case "aac": {
                if (!this.velocityInput || !this.velocityTimer.hasTimePassed(80L)) break;
                IEntityPlayerSP iEntityPlayerSP17 = iEntityPlayerSP2;
                iEntityPlayerSP17.setMotionX(iEntityPlayerSP17.getMotionX() * ((Number)this.horizontalValue.get()).doubleValue());
                IEntityPlayerSP iEntityPlayerSP18 = iEntityPlayerSP2;
                iEntityPlayerSP18.setMotionZ(iEntityPlayerSP18.getMotionZ() * ((Number)this.horizontalValue.get()).doubleValue());
                IEntityPlayerSP iEntityPlayerSP19 = iEntityPlayerSP2;
                iEntityPlayerSP19.setMotionY(iEntityPlayerSP19.getMotionY() * ((Number)this.verticalValue.get()).doubleValue());
                this.velocityInput = false;
                break;
            }
            case "hytmotion": {
                if (iEntityPlayerSP2.getHurtTime() <= 0 || iEntityPlayerSP2.isDead() || iEntityPlayerSP2.getHurtTime() > 5) break;
                IEntityPlayerSP iEntityPlayerSP20 = iEntityPlayerSP2;
                iEntityPlayerSP20.setMotionX(iEntityPlayerSP20.getMotionX() * 0.4);
                IEntityPlayerSP iEntityPlayerSP21 = iEntityPlayerSP2;
                iEntityPlayerSP21.setMotionZ(iEntityPlayerSP21.getMotionZ() * 0.4);
                IEntityPlayerSP iEntityPlayerSP22 = iEntityPlayerSP2;
                iEntityPlayerSP22.setMotionY(iEntityPlayerSP22.getMotionY() * (double)0.381145f);
                IEntityPlayerSP iEntityPlayerSP23 = iEntityPlayerSP2;
                iEntityPlayerSP23.setMotionY(iEntityPlayerSP23.getMotionY() / (double)1.781145f);
                break;
            }
            case "newhytmotion": {
                if (iEntityPlayerSP2.getHurtTime() <= 0 || iEntityPlayerSP2.isDead() || iEntityPlayerSP2.getOnGround()) break;
                if (!iEntityPlayerSP2.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) {
                    IEntityPlayerSP iEntityPlayerSP24 = iEntityPlayerSP2;
                    iEntityPlayerSP24.setMotionX(iEntityPlayerSP24.getMotionX() * 0.47188);
                    IEntityPlayerSP iEntityPlayerSP25 = iEntityPlayerSP2;
                    iEntityPlayerSP25.setMotionZ(iEntityPlayerSP25.getMotionZ() * 0.47188);
                    if (iEntityPlayerSP2.getMotionY() != 0.42 && !(iEntityPlayerSP2.getMotionY() > 0.42)) break;
                    IEntityPlayerSP iEntityPlayerSP26 = iEntityPlayerSP2;
                    iEntityPlayerSP26.setMotionY(iEntityPlayerSP26.getMotionY() * 0.4);
                    break;
                }
                IEntityPlayerSP iEntityPlayerSP27 = iEntityPlayerSP2;
                iEntityPlayerSP27.setMotionX(iEntityPlayerSP27.getMotionX() * 0.65025);
                IEntityPlayerSP iEntityPlayerSP28 = iEntityPlayerSP2;
                iEntityPlayerSP28.setMotionZ(iEntityPlayerSP28.getMotionZ() * 0.65025);
                if (iEntityPlayerSP2.getMotionY() != 0.42 && !(iEntityPlayerSP2.getMotionY() > 0.42)) break;
                IEntityPlayerSP iEntityPlayerSP29 = iEntityPlayerSP2;
                iEntityPlayerSP29.setMotionY(iEntityPlayerSP29.getMotionY() * 0.4);
                break;
            }
            case "aacpush": {
                if (this.jump) {
                    if (iEntityPlayerSP2.getOnGround()) {
                        this.jump = false;
                    }
                } else {
                    if (iEntityPlayerSP2.getHurtTime() > 0 && iEntityPlayerSP2.getMotionX() != 0.0 && iEntityPlayerSP2.getMotionZ() != 0.0) {
                        iEntityPlayerSP2.setOnGround(true);
                    }
                    if (iEntityPlayerSP2.getHurtResistantTime() > 0 && ((Boolean)this.aacPushYReducerValue.get()).booleanValue()) {
                        Module module = LiquidBounce.INSTANCE.getModuleManager().get(Speed.class);
                        if (module == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!module.getState()) {
                            IEntityPlayerSP iEntityPlayerSP30 = iEntityPlayerSP2;
                            iEntityPlayerSP30.setMotionY(iEntityPlayerSP30.getMotionY() - 0.014999993);
                        }
                    }
                }
                if (iEntityPlayerSP2.getHurtResistantTime() < 19) break;
                float f = ((Number)this.aacPushXZReducerValue.get()).floatValue();
                IEntityPlayerSP iEntityPlayerSP31 = iEntityPlayerSP2;
                iEntityPlayerSP31.setMotionX(iEntityPlayerSP31.getMotionX() / (double)f);
                IEntityPlayerSP iEntityPlayerSP32 = iEntityPlayerSP2;
                iEntityPlayerSP32.setMotionZ(iEntityPlayerSP32.getMotionZ() / (double)f);
                break;
            }
            case "custom": {
                if (iEntityPlayerSP2.getHurtTime() <= 0 || iEntityPlayerSP2.isDead()) break;
                IEntityPlayerSP iEntityPlayerSP33 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP33 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP33.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) break;
                IEntityPlayerSP iEntityPlayerSP34 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP34 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP34.isInWater()) break;
                IEntityPlayerSP iEntityPlayerSP35 = iEntityPlayerSP2;
                iEntityPlayerSP35.setMotionX(iEntityPlayerSP35.getMotionX() * ((Number)this.customX.get()).doubleValue());
                IEntityPlayerSP iEntityPlayerSP36 = iEntityPlayerSP2;
                iEntityPlayerSP36.setMotionZ(iEntityPlayerSP36.getMotionZ() * ((Number)this.customZ.get()).doubleValue());
                if (((Boolean)this.customYStart.get()).booleanValue()) {
                    IEntityPlayerSP iEntityPlayerSP37 = iEntityPlayerSP2;
                    iEntityPlayerSP37.setMotionY(iEntityPlayerSP37.getMotionY() / ((Number)this.customY.get()).doubleValue());
                }
                if (!((Boolean)this.customC06FakeLag.get()).booleanValue()) break;
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosLook(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getPosY(), iEntityPlayerSP2.getPosZ(), iEntityPlayerSP2.getRotationYaw(), iEntityPlayerSP2.getRotationPitch(), iEntityPlayerSP2.getOnGround()));
                break;
            }
            case "aaczero": {
                if (iEntityPlayerSP2.getHurtTime() > 0) {
                    if (!this.velocityInput || iEntityPlayerSP2.getOnGround() || iEntityPlayerSP2.getFallDistance() > 2.0f) {
                        return;
                    }
                    IEntityPlayerSP iEntityPlayerSP38 = iEntityPlayerSP2;
                    iEntityPlayerSP38.setMotionY(iEntityPlayerSP38.getMotionY() - 1.0);
                    iEntityPlayerSP2.setAirBorne(true);
                    iEntityPlayerSP2.setOnGround(true);
                    break;
                }
                this.velocityInput = false;
                break;
            }
        }
    }

    public Velocity() {
        this.modeValue = new ListValue("Mode", new String[]{"GrimReduce", "Custom", "AAC4", "Simple", "AAC", "AACPush", "AACZero", "Reverse", "SmoothReverse", "Jump", "AAC5Reduce", "HytPacketA", "Glitch", "HytTick", "Vanilla", "HytTest", "HytNewTest", "HytPacket", "NewAAC4", "HytMotion", "NewHytMotion", "HytPacketB", "HytPacketFix", "S27"}, "Vanilla");
        this.newaac4XZReducerValue = new FloatValue("NewAAC4XZReducer", 0.45f, 0.0f, 1.0f).displayable(new Function0(this){
            final Velocity this$0;

            public Object invoke() {
                return this.invoke();
            }

            static {
            }

            public final boolean invoke() {
                String string = (String)Velocity.access$getModeValue$p(this.this$0).get();
                boolean bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                return string2.toLowerCase().equals("aac4");
            }
            {
                this.this$0 = velocity;
                super(0);
            }
        });
        this.velocityTickValue = new IntegerValue("VelocityTick", 1, 0, 10);
        this.reverseStrengthValue = new FloatValue("ReverseStrength", 1.0f, 0.1f, 1.0f).displayable(new Function0(this){
            final Velocity this$0;

            static {
            }
            {
                this.this$0 = velocity;
                super(0);
            }

            public Object invoke() {
                return this.invoke();
            }

            public final boolean invoke() {
                String string = (String)Velocity.access$getModeValue$p(this.this$0).get();
                boolean bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                return string2.toLowerCase().equals("reverse");
            }
        });
        this.reverse2StrengthValue = new FloatValue("SmoothReverseStrength", 0.05f, 0.02f, 0.1f).displayable(new Function0(this){
            final Velocity this$0;

            public Object invoke() {
                return this.invoke();
            }

            public final boolean invoke() {
                String string = (String)Velocity.access$getModeValue$p(this.this$0).get();
                boolean bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                return string2.toLowerCase().equals("smoothreverse");
            }
            {
                this.this$0 = velocity;
                super(0);
            }

            static {
            }
        });
        this.hytpacketaset = new FloatValue("HytPacketASet", 0.35f, 0.1f, 1.0f).displayable(new Function0(this){
            final Velocity this$0;
            {
                this.this$0 = velocity;
                super(0);
            }

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            public final boolean invoke() {
                String string = (String)Velocity.access$getModeValue$p(this.this$0).get();
                boolean bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                if (string2.toLowerCase().equals("hytpacketa")) return true;
                string = (String)Velocity.access$getModeValue$p(this.this$0).get();
                bl = false;
                String string3 = string;
                if (string3 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                if (!string3.toLowerCase().equals("hytpacketfix")) return false;
                return true;
            }

            static {
            }

            public Object invoke() {
                return this.invoke();
            }
        });
        this.hytpacketbset = new FloatValue("HytPacketBSet", 0.5f, 1.0f, 1.0f).displayable(new Function0(this){
            final Velocity this$0;

            public Object invoke() {
                return this.invoke();
            }

            static {
            }
            {
                this.this$0 = velocity;
                super(0);
            }

            public final boolean invoke() {
                String string = (String)Velocity.access$getModeValue$p(this.this$0).get();
                boolean bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                return string2.toLowerCase().equals("hytpacketa");
            }
        });
        this.aacPushXZReducerValue = new FloatValue("AACPushXZReducer", 2.0f, 1.0f, 3.0f).displayable(new Function0(this){
            final Velocity this$0;

            public final boolean invoke() {
                String string = (String)Velocity.access$getModeValue$p(this.this$0).get();
                boolean bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                return string2.toLowerCase().equals("aac4push");
            }

            static {
            }
            {
                this.this$0 = velocity;
                super(0);
            }

            public Object invoke() {
                return this.invoke();
            }
        });
        this.aacPushYReducerValue = new BoolValue("AACPushYReducer", true).displayable(new Function0(this){
            final Velocity this$0;

            public final boolean invoke() {
                String string = (String)Velocity.access$getModeValue$p(this.this$0).get();
                boolean bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                return string2.toLowerCase().equals("aac4push");
            }
            {
                this.this$0 = velocity;
                super(0);
            }

            public Object invoke() {
                return this.invoke();
            }

            static {
            }
        });
        this.noFireValue = new BoolValue("noFire", false);
        this.cobwebValue = new BoolValue("NoCobweb", true);
        this.onlyGroundValue = new BoolValue("OnlyGround", true);
        this.grimReduceNoMotionY = new BoolValue("GrimNoMotionY", true).displayable(new Function0(this){
            final Velocity this$0;

            static {
            }

            public final boolean invoke() {
                String string = (String)Velocity.access$getModeValue$p(this.this$0).get();
                boolean bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                return string2.toLowerCase().equals("grimreduce");
            }

            public Object invoke() {
                return this.invoke();
            }
            {
                this.this$0 = velocity;
                super(0);
            }
        });
        this.customX = new FloatValue("CustomX", 0.0f, 0.0f, 1.0f).displayable(new Function0(this){
            final Velocity this$0;
            {
                this.this$0 = velocity;
                super(0);
            }

            static {
            }

            public Object invoke() {
                return this.invoke();
            }

            public final boolean invoke() {
                String string = (String)Velocity.access$getModeValue$p(this.this$0).get();
                boolean bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                return string2.toLowerCase().equals("custom");
            }
        });
        this.customYStart = new BoolValue("CanCustomY", false).displayable(new Function0(this){
            final Velocity this$0;

            static {
            }

            public Object invoke() {
                return this.invoke();
            }

            public final boolean invoke() {
                String string = (String)Velocity.access$getModeValue$p(this.this$0).get();
                boolean bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                return string2.toLowerCase().equals("custom");
            }
            {
                this.this$0 = velocity;
                super(0);
            }
        });
        this.customY = new FloatValue("CustomY", 1.0f, 1.0f, 2.0f).displayable(new Function0(this){
            final Velocity this$0;

            public Object invoke() {
                return this.invoke();
            }

            static {
            }

            public final boolean invoke() {
                String string = (String)Velocity.access$getModeValue$p(this.this$0).get();
                boolean bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                return string2.toLowerCase().equals("custom");
            }
            {
                this.this$0 = velocity;
                super(0);
            }
        });
        this.customZ = new FloatValue("CustomZ", 0.0f, 0.0f, 1.0f).displayable(new Function0(this){
            final Velocity this$0;

            public Object invoke() {
                return this.invoke();
            }
            {
                this.this$0 = velocity;
                super(0);
            }

            static {
            }

            public final boolean invoke() {
                String string = (String)Velocity.access$getModeValue$p(this.this$0).get();
                boolean bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                return string2.toLowerCase().equals("custom");
            }
        });
        this.customC06FakeLag = new BoolValue("CustomC06FakeLag", false).displayable(new Function0(this){
            final Velocity this$0;

            public final boolean invoke() {
                String string = (String)Velocity.access$getModeValue$p(this.this$0).get();
                boolean bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                return string2.toLowerCase().equals("custom");
            }

            static {
            }
            {
                this.this$0 = velocity;
                super(0);
            }

            public Object invoke() {
                return this.invoke();
            }
        });
        this.velocityTimer = new MSTimer();
    }

    public final void setBlock(@Nullable IBlock iBlock) {
        this.block = iBlock;
    }

    public static final ListValue access$getModeValue$p(Velocity velocity) {
        return velocity.modeValue;
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    @Override
    public void onDisable() {
        block0: {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) break block0;
            iEntityPlayerSP.setSpeedInAir(0.02f);
        }
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        IPacket iPacket = packetEvent.getPacket();
        if (MinecraftInstance.classProvider.isSPacketEntityVelocity(iPacket)) {
            Object object;
            ISPacketEntityVelocity iSPacketEntityVelocity = iPacket.asSPacketEntityVelocity();
            if (((Boolean)this.noFireValue.get()).booleanValue()) {
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP3.isBurning() && ((Boolean)this.cobwebValue.get()).booleanValue()) {
                    IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP4 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP4.isInWeb() && ((Boolean)this.onlyGroundValue.get()).booleanValue() && MinecraftInstance.mc2.field_71439_g.field_70160_al) {
                        return;
                    }
                }
            }
            if ((object = MinecraftInstance.mc.getTheWorld()) == null || (object = object.getEntityByID(iSPacketEntityVelocity.getEntityID())) == null) {
                return;
            }
            if (object.equals(iEntityPlayerSP2) ^ true) {
                return;
            }
            this.velocityTimer.reset();
            String string = (String)this.modeValue.get();
            boolean bl = false;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            switch (string2.toLowerCase()) {
                case "vanilla": {
                    packetEvent.cancelEvent();
                    break;
                }
                case "s27": {
                    if (MinecraftInstance.classProvider.isSPacketExplosion(iPacket)) {
                        packetEvent.cancelEvent();
                    }
                    float f = ((Number)this.horizontalValue.get()).floatValue();
                    float f2 = ((Number)this.verticalValue.get()).floatValue();
                    iSPacketEntityVelocity.setMotionX((int)((float)iSPacketEntityVelocity.getMotionX() * f));
                    iSPacketEntityVelocity.setMotionY((int)((float)iSPacketEntityVelocity.getMotionY() * f2));
                    iSPacketEntityVelocity.setMotionZ((int)((float)iSPacketEntityVelocity.getMotionZ() * f));
                    break;
                }
                case "simple": {
                    float f = ((Number)this.horizontalValue.get()).floatValue();
                    float f3 = ((Number)this.verticalValue.get()).floatValue();
                    if (f == 0.0f && f3 == 0.0f) {
                        packetEvent.cancelEvent();
                    }
                    iSPacketEntityVelocity.setMotionX((int)((float)iSPacketEntityVelocity.getMotionX() * f));
                    iSPacketEntityVelocity.setMotionY((int)((float)iSPacketEntityVelocity.getMotionY() * f3));
                    iSPacketEntityVelocity.setMotionZ((int)((float)iSPacketEntityVelocity.getMotionZ() * f));
                    break;
                }
                case "hytpacketfix": {
                    if (iEntityPlayerSP2.getHurtTime() > 0 && !iEntityPlayerSP2.isDead()) {
                        IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP5 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!iEntityPlayerSP5.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) {
                            IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                            if (iEntityPlayerSP6 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (!iEntityPlayerSP6.isInWater()) {
                                IEntityPlayerSP iEntityPlayerSP7 = iEntityPlayerSP2;
                                iEntityPlayerSP7.setMotionX(iEntityPlayerSP7.getMotionX() * 0.4);
                                IEntityPlayerSP iEntityPlayerSP8 = iEntityPlayerSP2;
                                iEntityPlayerSP8.setMotionZ(iEntityPlayerSP8.getMotionZ() * 0.4);
                                IEntityPlayerSP iEntityPlayerSP9 = iEntityPlayerSP2;
                                iEntityPlayerSP9.setMotionY(iEntityPlayerSP9.getMotionY() / (double)1.45f);
                            }
                        }
                    }
                    if (iEntityPlayerSP2.getHurtTime() < 1) {
                        iSPacketEntityVelocity.setMotionY(0);
                    }
                    if (iEntityPlayerSP2.getHurtTime() >= 5) break;
                    iSPacketEntityVelocity.setMotionX(0);
                    iSPacketEntityVelocity.setMotionZ(0);
                    break;
                }
                case "hyttest": {
                    if (!iEntityPlayerSP2.getOnGround()) break;
                    this.canCancelJump = false;
                    iSPacketEntityVelocity.setMotionX((int)0.985114);
                    iSPacketEntityVelocity.setMotionY((int)0.885113);
                    iSPacketEntityVelocity.setMotionZ((int)0.785112);
                    IEntityPlayerSP iEntityPlayerSP10 = iEntityPlayerSP2;
                    iEntityPlayerSP10.setMotionX(iEntityPlayerSP10.getMotionX() / 1.75);
                    IEntityPlayerSP iEntityPlayerSP11 = iEntityPlayerSP2;
                    iEntityPlayerSP11.setMotionZ(iEntityPlayerSP11.getMotionZ() / 1.75);
                    break;
                }
                case "hytnewtest": {
                    if (!iEntityPlayerSP2.getOnGround()) break;
                    this.velocityInput = true;
                    float f = iEntityPlayerSP2.getRotationYaw() * ((float)Math.PI / 180);
                    iSPacketEntityVelocity.setMotionX((int)((double)iSPacketEntityVelocity.getMotionX() * 0.75));
                    iSPacketEntityVelocity.setMotionZ((int)((double)iSPacketEntityVelocity.getMotionZ() * 0.75));
                    IEntityPlayerSP iEntityPlayerSP12 = iEntityPlayerSP2;
                    double d = iEntityPlayerSP12.getMotionX();
                    IEntityPlayerSP iEntityPlayerSP13 = iEntityPlayerSP12;
                    boolean bl2 = false;
                    float f4 = (float)Math.sin(f);
                    iEntityPlayerSP13.setMotionX(d - (double)f4 * 0.2);
                    IEntityPlayerSP iEntityPlayerSP14 = iEntityPlayerSP2;
                    d = iEntityPlayerSP14.getMotionZ();
                    iEntityPlayerSP13 = iEntityPlayerSP14;
                    bl2 = false;
                    f4 = (float)Math.cos(f);
                    iEntityPlayerSP13.setMotionZ(d + (double)f4 * 0.2);
                    break;
                }
                case "hytpacketa": {
                    iSPacketEntityVelocity.setMotionX((int)((double)((float)iSPacketEntityVelocity.getMotionX() * ((Number)this.hytpacketaset.get()).floatValue()) / 1.5));
                    iSPacketEntityVelocity.setMotionY((int)0.7);
                    iSPacketEntityVelocity.setMotionZ((int)((double)((float)iSPacketEntityVelocity.getMotionZ() * ((Number)this.hytpacketaset.get()).floatValue()) / 1.5));
                    packetEvent.cancelEvent();
                    break;
                }
                case "hytpacketb": {
                    iSPacketEntityVelocity.setMotionX((int)((double)((float)iSPacketEntityVelocity.getMotionX() * ((Number)this.hytpacketbset.get()).floatValue()) / 2.5));
                    iSPacketEntityVelocity.setMotionY((int)((double)((float)iSPacketEntityVelocity.getMotionY() * ((Number)this.hytpacketbset.get()).floatValue()) / 2.5));
                    iSPacketEntityVelocity.setMotionZ((int)((double)((float)iSPacketEntityVelocity.getMotionZ() * ((Number)this.hytpacketbset.get()).floatValue()) / 2.5));
                    break;
                }
                case "aac": 
                case "aaczero": 
                case "reverse": 
                case "aac4": 
                case "smoothreverse": 
                case "aac5reduce": {
                    this.velocityInput = true;
                    break;
                }
                case "hyttick": {
                    this.velocityInput = true;
                    packetEvent.cancelEvent();
                    break;
                }
                case "glitch": {
                    if (!iEntityPlayerSP2.getOnGround()) {
                        return;
                    }
                    this.velocityInput = true;
                    packetEvent.cancelEvent();
                    break;
                }
            }
        }
    }
}

