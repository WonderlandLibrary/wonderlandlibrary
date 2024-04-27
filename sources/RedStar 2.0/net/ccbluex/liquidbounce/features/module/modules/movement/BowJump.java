package net.ccbluex.liquidbounce.features.module.modules.movement;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import me.utils.PacketUtils;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.enums.ItemType;
import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.item.ItemBow;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="BowJump", category=ModuleCategory.MOVEMENT, description="new")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000^\n\n\n\b\n\n\u0000\n\n\u0000\n\b\n\b\n\n\b\n\n\b\n\t\n\b\n\n\b\n\n\b\n\n\u0000\n\n\b\n\n\b\b\u000020B¢J\b0HJ\b0HJ02 0!HJ\"02 0#HJ\b$0HJ%02 0&HJ\b'0HR0X¢\n\u0000R0X¢\n\u0000R0\b8BX¢\b\t\nR0\bX¢\n\u0000R\f0\r8BX¢\bR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0\bX¢\n\u0000R0X¢\n\u0000R08BX¢\bR0X¢\n\u0000¨("}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/BowJump;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "AutoDisable", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "boostValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "bowSlot", "", "getBowSlot", "()I", "bowState", "bowStatus", "", "getBowStatus", "()Ljava/lang/String;", "delayBeforeLaunch", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "heightValue", "lastPlayerTick", "", "lastSlot", "renderValue", "statusColor", "Ljava/awt/Color;", "getStatusColor", "()Ljava/awt/Color;", "timerValue", "onDisable", "", "onEnable", "onMove", "event", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onRender2D", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "onWorld", "Pride"})
public final class BowJump
extends Module {
    private final FloatValue boostValue = new FloatValue("Boost", 4.25f, 0.0f, 10.0f);
    private final FloatValue heightValue = new FloatValue("Height", 0.42f, 0.0f, 10.0f);
    private final FloatValue timerValue = new FloatValue("Timer", 1.0f, 0.1f, 10.0f);
    private final IntegerValue delayBeforeLaunch = new IntegerValue("DelayBeforeArrowLaunch", 1, 1, 20);
    private final BoolValue AutoDisable = new BoolValue("AutoDisable", true);
    private final BoolValue renderValue = new BoolValue("RenderStatus", true);
    private int bowState;
    private long lastPlayerTick;
    private int lastSlot = -1;

    @Override
    public void onEnable() {
        if (MinecraftInstance.mc.getThePlayer() == null) {
            return;
        }
        this.bowState = 0;
        this.lastPlayerTick = -1L;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        this.lastSlot = iEntityPlayerSP.getInventory().getCurrentItem();
        MovementUtils.strafe(0.0f);
    }

    @EventTarget
    public final void onMove(@NotNull MoveEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getOnGround() && this.bowState < 3) {
            event.cancelEvent();
        }
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IPacket packet = event.getPacket();
        if (MinecraftInstance.classProvider.isCPacketHeldItemChange(packet)) {
            this.lastSlot = packet.asCPacketHeldItemChange().getSlotId();
            event.cancelEvent();
        }
        if (MinecraftInstance.classProvider.isCPacketPlayer(packet) && this.bowState < 3) {
            packet.asCPacketPlayer().setIsmoving(false);
        }
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
        forceDisable = false;
        switch (this.bowState) {
            case 0: {
                slot = this.getBowSlot();
                if (slot < 0) ** GOTO lbl12
                v0 = MinecraftInstance.mc.getThePlayer();
                if (v0 == null) {
                    Intrinsics.throwNpe();
                }
                if (CollectionsKt.contains((Iterable)v0.getInventory().getMainInventory(), MinecraftInstance.classProvider.createItemStack(MinecraftInstance.classProvider.getItemEnum(ItemType.BOWL)))) ** GOTO lbl17
lbl12:
                // 2 sources

                LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Bow Jump", " No arrows or bow found in your inventory!", NotifyType.WARNING, 3500, 1500));
                forceDisable = true;
                this.bowState = 5;
                break;
lbl17:
                // 1 sources

                if (this.lastPlayerTick != -1L) break;
                v1 = MinecraftInstance.mc.getThePlayer();
                if (v1 == null) {
                    Intrinsics.throwNpe();
                }
                v1.getInventoryContainer().getSlot(slot + 36).getStack();
                if (this.lastSlot != slot) {
                    PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)((Packet)new CPacketHeldItemChange(slot)));
                }
                v2 = MinecraftInstance.mc.getNetHandler();
                v3 = new WBlockPos(-1, -1, -1);
                v4 = MinecraftInstance.mc.getThePlayer();
                if (v4 == null) {
                    Intrinsics.throwNpe();
                }
                v2.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerBlockPlacement(v3, 255, v4.getInventoryContainer().getSlot(slot + 36).getStack(), 0.0f, 0.0f, 0.0f));
                v5 = MinecraftInstance.mc.getThePlayer();
                if (v5 == null) {
                    Intrinsics.throwNpe();
                }
                this.lastPlayerTick = v5.getTicksExisted();
                this.bowState = 1;
                break;
            }
            case 1: {
                reSlot = this.getBowSlot();
                v6 = MinecraftInstance.mc.getThePlayer();
                if (v6 == null) {
                    Intrinsics.throwNpe();
                }
                if ((long)v6.getTicksExisted() - this.lastPlayerTick <= ((Number)this.delayBeforeLaunch.get()).longValue()) break;
                v7 = MinecraftInstance.mc.getNetHandler();
                v8 = MinecraftInstance.mc.getThePlayer();
                if (v8 == null) {
                    Intrinsics.throwNpe();
                }
                v9 = v8.getRotationYaw();
                v10 = MinecraftInstance.mc.getThePlayer();
                if (v10 == null) {
                    Intrinsics.throwNpe();
                }
                v7.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerLook(v9, -90.0f, v10.getOnGround()));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.Companion.getORIGIN(), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
                if (this.lastSlot != reSlot) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(this.lastSlot));
                }
                this.bowState = 2;
                break;
            }
            case 2: {
                v11 = MinecraftInstance.mc.getThePlayer();
                if (v11 == null) {
                    Intrinsics.throwNpe();
                }
                if (v11.getHurtTime() <= 0) break;
                this.bowState = 3;
                break;
            }
            case 3: {
                MovementUtils.strafe(((Number)this.boostValue.get()).floatValue());
                v12 = MinecraftInstance.mc.getThePlayer();
                if (v12 == null) {
                    Intrinsics.throwNpe();
                }
                v12.setMotionY(((Number)this.heightValue.get()).floatValue());
                this.bowState = 4;
                v13 = MinecraftInstance.mc.getThePlayer();
                if (v13 == null) {
                    Intrinsics.throwNpe();
                }
                this.lastPlayerTick = v13.getTicksExisted();
                break;
            }
            case 4: {
                MinecraftInstance.mc.getTimer().setTimerSpeed(((Number)this.timerValue.get()).floatValue());
                v14 = MinecraftInstance.mc.getThePlayer();
                if (v14 == null) {
                    Intrinsics.throwNpe();
                }
                if (!v14.getOnGround()) break;
                v15 = MinecraftInstance.mc.getThePlayer();
                if (v15 == null) {
                    Intrinsics.throwNpe();
                }
                if ((long)v15.getTicksExisted() - this.lastPlayerTick < 1L) break;
                this.bowState = 5;
                break;
            }
        }
        if (this.bowState < 3) {
            MinecraftInstance.mc2.player.movementInput.moveStrafe = 0.0f;
            MinecraftInstance.mc2.player.movementInput.moveStrafe = 0.0f;
        }
        if (this.bowState == 5 && (((Boolean)this.AutoDisable.get()).booleanValue() || forceDisable)) {
            this.setState(false);
        }
    }

    @EventTarget
    public final void onWorld() {
        this.setState(false);
    }

    @Override
    public void onDisable() {
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        iEntityPlayerSP.setSpeedInAir(0.02f);
    }

    /*
     * WARNING - void declaration
     */
    private final int getBowSlot() {
        int n = 36;
        int n2 = 44;
        while (n <= n2) {
            void i;
            IItemStack stack;
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if ((stack = iEntityPlayerSP.getInventoryContainer().getSlot((int)i).getStack()) != null && stack.getItem() instanceof ItemBow) {
                return (int)(i - 36);
            }
            ++i;
        }
        return -1;
    }

    @EventTarget
    public final void onRender2D() {
        if (!((Boolean)this.renderValue.get()).booleanValue()) {
            return;
        }
        IMinecraft iMinecraft = MinecraftInstance.mc;
        Intrinsics.checkExpressionValueIsNotNull(iMinecraft, "mc");
        IScaledResolution scaledRes = MinecraftInstance.classProvider.createScaledResolution(iMinecraft);
        float width = (float)this.bowState / 5.0f * 60.0f;
        Fonts.font25.drawCenteredString(this.getBowStatus(), (float)scaledRes.getScaledWidth() / 2.0f, (float)scaledRes.getScaledHeight() / 2.0f + 14.0f, -1, true);
        RenderUtils.drawRect((float)scaledRes.getScaledWidth() / 2.0f - 31.0f, (float)scaledRes.getScaledHeight() / 2.0f + 25.0f, (float)scaledRes.getScaledWidth() / 2.0f + 31.0f, (float)scaledRes.getScaledHeight() / 2.0f + 29.0f, -1610612736);
        RenderUtils.drawRect((float)scaledRes.getScaledWidth() / 2.0f - 30.0f, (float)scaledRes.getScaledHeight() / 2.0f + 26.0f, (float)scaledRes.getScaledWidth() / 2.0f - 30.0f + width, (float)scaledRes.getScaledHeight() / 2.0f + 28.0f, this.getStatusColor());
    }

    private final String getBowStatus() {
        String string;
        switch (this.bowState) {
            case 0: {
                string = "Idle...";
                break;
            }
            case 1: {
                string = "Preparing...";
                break;
            }
            case 2: {
                string = "Waiting for damage...";
                break;
            }
            case 3: 
            case 4: {
                string = "Boost!";
                break;
            }
            default: {
                string = "Task completed.";
            }
        }
        return string;
    }

    private final Color getStatusColor() {
        Color color;
        switch (this.bowState) {
            case 0: {
                color = new Color(21, 21, 21);
                break;
            }
            case 1: {
                color = new Color(48, 48, 48);
                break;
            }
            case 2: {
                Color color2 = Color.yellow;
                color = color2;
                Intrinsics.checkExpressionValueIsNotNull(color2, "Color.yellow");
                break;
            }
            case 3: 
            case 4: {
                Color color3 = Color.green;
                color = color3;
                Intrinsics.checkExpressionValueIsNotNull(color3, "Color.green");
                break;
            }
            default: {
                color = new Color(0, 111, 255);
            }
        }
        return color;
    }
}
