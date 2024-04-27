/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.settings.IKeyBinding;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="AntiAFK", description="Prevents you from getting kicked for being AFK.", category=ModuleCategory.PLAYER)
public final class AntiAFK
extends Module {
    private final ListValue modeValue;
    private final BoolValue rotateValue;
    private final BoolValue swingValue;
    private long randomTimerDelay = 500L;
    private final MSTimer swingDelayTimer = new MSTimer();
    private final MSTimer delayTimer = new MSTimer();
    private final IntegerValue swingDelayValue;
    private final BoolValue jumpValue;
    private boolean shouldMove;
    private final IntegerValue rotationDelayValue;
    private final BoolValue moveValue;
    private final FloatValue rotationAngleValue;

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        block5 : switch (string2.toLowerCase()) {
            case "old": {
                MinecraftInstance.mc.getGameSettings().getKeyBindForward().setPressed(true);
                if (!this.delayTimer.hasTimePassed(500L)) break;
                IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
                iEntityPlayerSP3.setRotationYaw(iEntityPlayerSP3.getRotationYaw() + 180.0f);
                this.delayTimer.reset();
                break;
            }
            case "random": {
                IKeyBinding iKeyBinding = this.getRandomMoveKeyBind();
                if (iKeyBinding == null) {
                    Intrinsics.throwNpe();
                }
                iKeyBinding.setPressed(this.shouldMove);
                if (!this.delayTimer.hasTimePassed(this.randomTimerDelay)) {
                    return;
                }
                this.shouldMove = false;
                this.randomTimerDelay = 500L;
                switch (RandomUtils.nextInt(0, 6)) {
                    case 0: {
                        if (iEntityPlayerSP2.getOnGround()) {
                            iEntityPlayerSP2.jump();
                        }
                        this.delayTimer.reset();
                        break block5;
                    }
                    case 1: {
                        if (!iEntityPlayerSP2.isSwingInProgress()) {
                            iEntityPlayerSP2.swingItem();
                        }
                        this.delayTimer.reset();
                        break block5;
                    }
                    case 2: {
                        this.randomTimerDelay = RandomUtils.nextInt(0, 1000);
                        this.shouldMove = true;
                        this.delayTimer.reset();
                        break block5;
                    }
                    case 3: {
                        iEntityPlayerSP2.getInventory().setCurrentItem(RandomUtils.nextInt(0, 9));
                        MinecraftInstance.mc.getPlayerController().updateController();
                        this.delayTimer.reset();
                        break block5;
                    }
                    case 4: {
                        IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP2;
                        iEntityPlayerSP4.setRotationYaw(iEntityPlayerSP4.getRotationYaw() + RandomUtils.INSTANCE.nextFloat(-180.0f, 180.0f));
                        this.delayTimer.reset();
                        break block5;
                    }
                    case 5: {
                        if (iEntityPlayerSP2.getRotationPitch() <= (float)-90 || iEntityPlayerSP2.getRotationPitch() >= (float)90) {
                            iEntityPlayerSP2.setRotationPitch(0.0f);
                        }
                        IEntityPlayerSP iEntityPlayerSP5 = iEntityPlayerSP2;
                        iEntityPlayerSP5.setRotationPitch(iEntityPlayerSP5.getRotationPitch() + RandomUtils.INSTANCE.nextFloat(-10.0f, 10.0f));
                        this.delayTimer.reset();
                        break block5;
                    }
                }
                break;
            }
            case "custom": {
                if (((Boolean)this.moveValue.get()).booleanValue()) {
                    MinecraftInstance.mc.getGameSettings().getKeyBindForward().setPressed(true);
                }
                if (((Boolean)this.jumpValue.get()).booleanValue() && iEntityPlayerSP2.getOnGround()) {
                    iEntityPlayerSP2.jump();
                }
                if (((Boolean)this.rotateValue.get()).booleanValue() && this.delayTimer.hasTimePassed(((Number)this.rotationDelayValue.get()).intValue())) {
                    IEntityPlayerSP iEntityPlayerSP6 = iEntityPlayerSP2;
                    iEntityPlayerSP6.setRotationYaw(iEntityPlayerSP6.getRotationYaw() + ((Number)this.rotationAngleValue.get()).floatValue());
                    if (iEntityPlayerSP2.getRotationPitch() <= (float)-90 || iEntityPlayerSP2.getRotationPitch() >= (float)90) {
                        iEntityPlayerSP2.setRotationPitch(0.0f);
                    }
                    IEntityPlayerSP iEntityPlayerSP7 = iEntityPlayerSP2;
                    iEntityPlayerSP7.setRotationPitch(iEntityPlayerSP7.getRotationPitch() + (RandomUtils.INSTANCE.nextFloat(0.0f, 1.0f) * (float)2 - 1.0f));
                    this.delayTimer.reset();
                }
                if (!((Boolean)this.swingValue.get()).booleanValue() || iEntityPlayerSP2.isSwingInProgress() || !this.swingDelayTimer.hasTimePassed(((Number)this.swingDelayValue.get()).intValue())) break;
                iEntityPlayerSP2.swingItem();
                this.swingDelayTimer.reset();
                break;
            }
        }
    }

    public AntiAFK() {
        this.modeValue = new ListValue("Mode", new String[]{"Old", "Random", "Custom"}, "Random");
        this.swingDelayValue = new IntegerValue("SwingDelay", 100, 0, 1000);
        this.rotationDelayValue = new IntegerValue("RotationDelay", 100, 0, 1000);
        this.rotationAngleValue = new FloatValue("RotationAngle", 1.0f, -180.0f, 180.0f);
        this.jumpValue = new BoolValue("Jump", true);
        this.moveValue = new BoolValue("Move", true);
        this.rotateValue = new BoolValue("Rotate", true);
        this.swingValue = new BoolValue("Swing", true);
    }

    @Override
    public void onDisable() {
        if (!MinecraftInstance.mc.getGameSettings().isKeyDown(MinecraftInstance.mc.getGameSettings().getKeyBindForward())) {
            MinecraftInstance.mc.getGameSettings().getKeyBindForward().setPressed(false);
        }
    }

    private final IKeyBinding getRandomMoveKeyBind() {
        switch (RandomUtils.nextInt(0, 4)) {
            case 0: {
                return MinecraftInstance.mc.getGameSettings().getKeyBindRight();
            }
            case 1: {
                return MinecraftInstance.mc.getGameSettings().getKeyBindLeft();
            }
            case 2: {
                return MinecraftInstance.mc.getGameSettings().getKeyBindBack();
            }
            case 3: {
                return MinecraftInstance.mc.getGameSettings().getKeyBindForward();
            }
        }
        return null;
    }
}

