/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.inventory.GuiInventory
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IPlayerControllerMP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.player.InventoryCleaner;
import net.ccbluex.liquidbounce.features.module.modules.world.ChestStealer;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.ModuleUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;

@ModuleInfo(name="AutoRunaway", description="Automatically makes you /hub whenever your health is low.", category=ModuleCategory.COMBAT)
public final class AutoRunaway
extends Module {
    private boolean lmao;
    private TextValue text;
    private BoolValue keepArmor;
    private BoolValue autoDisable;
    private FloatValue health = new FloatValue("Health", 5.0f, 0.0f, 20.0f);

    public final void setText(TextValue textValue) {
        this.text = textValue;
    }

    public final void setKeepArmor(BoolValue boolValue) {
        this.keepArmor = boolValue;
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        if (MinecraftInstance.mc.getThePlayer() != null) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.getHealth() < ((Number)this.health.get()).floatValue()) {
                if (((Boolean)this.keepArmor.get()).booleanValue()) {
                    int n = 3;
                    for (int i = 0; i <= n; ++i) {
                        int n2 = 3 - i;
                        this.autoArmor(8 - n2, true);
                    }
                }
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP2.getHealth() <= ((Number)this.health.get()).floatValue() && !this.lmao) {
                    AutoRunaway.access$getMinecraft$p$s1046033730().field_71439_g.func_71165_d((String)this.text.get());
                    this.lmao = true;
                }
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP3.getHealth() <= ((Number)this.health.get()).floatValue() && ((Boolean)this.autoDisable.get()).booleanValue()) {
                    ModuleUtils.disableModules(KillAura.class, Speed.class, Scaffold.class, InventoryCleaner.class, ChestStealer.class);
                }
                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP4.getHealth() >= ((Number)this.health.get()).floatValue()) {
                    this.lmao = false;
                }
            }
        }
    }

    public final void setLmao(boolean bl) {
        this.lmao = bl;
    }

    public final TextValue getText() {
        return this.text;
    }

    public static final Minecraft access$getMinecraft$p$s1046033730() {
        return MinecraftInstance.minecraft;
    }

    public final boolean getLmao() {
        return this.lmao;
    }

    public final void setAutoDisable(BoolValue boolValue) {
        this.autoDisable = boolValue;
    }

    public final BoolValue getAutoDisable() {
        return this.autoDisable;
    }

    public final FloatValue getHealth() {
        return this.health;
    }

    public final void setHealth(FloatValue floatValue) {
        this.health = floatValue;
    }

    public final BoolValue getKeepArmor() {
        return this.keepArmor;
    }

    public AutoRunaway() {
        this.text = new TextValue("Text", "/hub");
        this.autoDisable = new BoolValue("AutoDisable", true);
        this.keepArmor = new BoolValue("KeepArmor", true);
    }

    private final void autoArmor(int n, boolean bl) {
        if (n != -1) {
            boolean bl2;
            boolean bl3 = bl2 = !(MinecraftInstance.mc.getCurrentScreen() instanceof GuiInventory);
            if (bl2) {
                IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP, ICPacketEntityAction.WAction.OPEN_INVENTORY));
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
}

