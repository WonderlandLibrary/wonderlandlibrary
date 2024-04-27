package net.ccbluex.liquidbounce.features.module.modules.hyt;

import java.util.Timer;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IPlayerControllerMP;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.combat.Velocity;
import net.ccbluex.liquidbounce.injection.backend.EntityPlayerSPImpl;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.minecraft.client.entity.EntityPlayerSP;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="AutoLeos", description="DpSea", category=ModuleCategory.HYT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000R\n\n\n\b\n\n\u0000\n\n\b\n\n\u0000\n\n\b\n\n\b\n\n\b\b\n\n\u0000\n\b\n\b\n\n\u0000\n\n\u0000\b\u000020B¢J02020HJ 02!0\"HJ#02!0$HR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0\tX¢\n\u0000R\n08VX¢\b\f\rR0¢\b\n\u0000\bR0X¢\n\u0000\b\"\bR0X¢\n\u0000\b\"\b¨%"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/hyt/AutoLeos;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "healths", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "keepArmor", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "message", "messages", "Lnet/ccbluex/liquidbounce/value/TextValue;", "tag", "", "getTag", "()Ljava/lang/String;", "timer", "Ljava/util/Timer;", "getTimer", "()Ljava/util/Timer;", "wating", "", "getWating", "()Z", "setWating", "(Z)V", "wating2", "getWating2", "setWating2", "move", "", "item", "", "isArmorSlot", "onUpdate", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "Pride"})
public final class AutoLeos
extends Module {
    private final FloatValue healths = new FloatValue("Health", 5.0f, 1.0f, 20.0f);
    private final BoolValue keepArmor = new BoolValue("KeepArmor", false);
    private final BoolValue message = new BoolValue("Message", false);
    private final TextValue messages = new TextValue("Messages", "[DeepSea]bye~");
    private boolean wating;
    private boolean wating2;
    @NotNull
    private final Timer timer;

    public final boolean getWating() {
        return this.wating;
    }

    public final void setWating(boolean bl) {
        this.wating = bl;
    }

    public final boolean getWating2() {
        return this.wating2;
    }

    public final void setWating2(boolean bl) {
        this.wating2 = bl;
    }

    @NotNull
    public final Timer getTimer() {
        return this.timer;
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getHealth() <= ((Number)this.healths.get()).floatValue()) {
            if (((Boolean)this.keepArmor.get()).booleanValue()) {
                int n = 0;
                int n2 = 3;
                while (n <= n2) {
                    void i;
                    int armorSlot = 3 - i;
                    this.move(8 - armorSlot, true);
                    ++i;
                }
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                IEntityPlayerSP $this$unwrap$iv = iEntityPlayerSP2;
                boolean $i$f$unwrap = false;
                if (((EntityPlayerSP)((EntityPlayerSPImpl)$this$unwrap$iv).getWrapped()).func_70658_aO() < 4 && this.wating2) {
                    IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP3 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP3.sendChatMessage("/hub");
                    this.wating2 = false;
                }
            } else if (this.wating2) {
                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP4.sendChatMessage("/hub");
                this.wating2 = false;
            }
            if (((Boolean)this.message.get()).booleanValue() && this.wating) {
                IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP5 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP5.sendChatMessage((String)this.messages.get());
                this.wating = false;
            }
            LiquidBounce.INSTANCE.getModuleManager().get(KillAura.class).setState(false);
            LiquidBounce.INSTANCE.getModuleManager().get(Velocity.class).setState(false);
        }
    }

    private final void move(int item, boolean isArmorSlot) {
        if (item != -1) {
            boolean openInventory;
            boolean bl = openInventory = !MinecraftInstance.classProvider.isGuiInventory(MinecraftInstance.mc.getCurrentScreen());
            if (openInventory) {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP, ICPacketEntityAction.WAction.OPEN_INVENTORY);
            }
            IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            int n = iEntityPlayerSP.getInventoryContainer().getWindowId();
            int n2 = isArmorSlot ? item : (item < 9 ? item + 36 : item);
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            iPlayerControllerMP.windowClick(n, n2, 0, 1, iEntityPlayerSP2);
            if (openInventory) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketCloseWindow());
            }
        }
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        this.wating = true;
        this.wating2 = true;
    }

    @Override
    @Nullable
    public String getTag() {
        return "Health " + ((Number)this.healths.get()).floatValue();
    }

    public AutoLeos() {
        this.setState(true);
        this.wating = true;
        this.wating2 = true;
        this.timer = new Timer();
    }
}
