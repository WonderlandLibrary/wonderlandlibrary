/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package cc.paimon.modules.hyt;

import cc.paimon.modules.combat.OldVelocity;
import java.util.Timer;
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
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.TextValue;

@ModuleInfo(name="AutoLobby", category=ModuleCategory.HYT, description="/hub")
public final class AutoLeos
extends Module {
    private final TextValue messages;
    private final Timer timer;
    private final BoolValue keepArmor = new BoolValue("KeepArmor", false);
    private boolean wating;
    private final BoolValue message = new BoolValue("Message", false);
    private boolean wating2;

    @Override
    public String getTag() {
        return "HytPlt";
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getHealth() <= 6.0f) {
            if (((Boolean)this.keepArmor.get()).booleanValue()) {
                int n = 3;
                for (int i = 0; i <= n; ++i) {
                    int n2 = 3 - i;
                    this.move(8 - n2, true);
                }
            } else if (this.wating2) {
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP2.sendChatMessage("/hub 875");
                this.wating2 = false;
            }
            if (((Boolean)this.message.get()).booleanValue() && this.wating) {
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP3.sendChatMessage((String)this.messages.get());
                this.wating = false;
            }
            LiquidBounce.INSTANCE.getModuleManager().get(KillAura.class).setState(false);
            LiquidBounce.INSTANCE.getModuleManager().get(OldVelocity.class).setState(false);
        }
    }

    public final boolean getWating2() {
        return this.wating2;
    }

    private final void move(int n, boolean bl) {
        if (n != -1) {
            boolean bl2;
            boolean bl3 = bl2 = !MinecraftInstance.classProvider.isGuiInventory(MinecraftInstance.mc.getCurrentScreen());
            if (bl2) {
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
            int n2 = iEntityPlayerSP.getInventoryContainer().getWindowId();
            int n3 = bl ? n : (n < 9 ? n + 36 : n);
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            iPlayerControllerMP.windowClick(n2, n3, 0, 1, iEntityPlayerSP2);
            if (bl2) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketCloseWindow());
            }
        }
    }

    public final void setWating(boolean bl) {
        this.wating = bl;
    }

    @EventTarget
    public final void onWorld(WorldEvent worldEvent) {
        this.wating = true;
        this.wating2 = true;
    }

    public final boolean getWating() {
        return this.wating;
    }

    public final void setWating2(boolean bl) {
        this.wating2 = bl;
    }

    public final Timer getTimer() {
        return this.timer;
    }

    public AutoLeos() {
        this.messages = new TextValue("Messages", "Now Skyrim Back.");
        this.setState(true);
        this.wating = true;
        this.wating2 = true;
        this.timer = new Timer();
    }
}

