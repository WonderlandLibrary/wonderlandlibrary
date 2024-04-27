/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.hyt;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="HytNoLagBack", description="\u4fee\u590d\u7248", category=ModuleCategory.HYT)
public final class HytNoLagBack
extends Module {
    private int ticks;
    private int a;
    private int b;
    private final ListValue modeValue = new ListValue("Mode", new String[]{"AntiCheat", "AAC5"}, "AAC5");

    @EventTarget
    public final void onUpdate() {
        String string = (String)this.modeValue.get();
        int n = 0;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string2.toLowerCase()) {
            case "anticheat": {
                if (this.ticks > 1000) {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP.isOnLadder() && MinecraftInstance.mc.getGameSettings().getKeyBindJump().getPressed()) {
                        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP2 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP2.setMotionY(0.11);
                    }
                }
                if (this.ticks > 2000) {
                    this.ticks = 0;
                    break;
                }
                n = this.ticks;
                this.ticks = n + 1;
                break;
            }
            case "aac5": {
                int n2;
                Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(KillAura.class);
                if (module == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
                }
                KillAura killAura = (KillAura)module;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP.getOnGround()) {
                    if (this.b == 0) {
                        killAura.getKeepSprintValue().set(true);
                        n2 = this.b;
                        this.b = n2 + 1;
                    }
                } else {
                    this.b = 0;
                    if (this.a == 0) {
                        killAura.getKeepSprintValue().set(false);
                        n2 = this.a;
                        this.a = n2 + 1;
                    }
                }
                if (this.ticks > 250) {
                    IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP3 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP3.isOnLadder() && MinecraftInstance.mc.getGameSettings().getKeyBindJump().getPressed()) {
                        IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP4 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP4.setMotionY(0.11);
                    }
                }
                if (this.ticks > 500) {
                    this.ticks = 0;
                    break;
                }
                n2 = this.ticks;
                this.ticks = n2 + 1;
                break;
            }
        }
    }

    @Override
    public void onEnable() {
        this.ticks = 0;
    }

    @Override
    public String getTag() {
        return ((String)this.modeValue.get()).toString();
    }
}

