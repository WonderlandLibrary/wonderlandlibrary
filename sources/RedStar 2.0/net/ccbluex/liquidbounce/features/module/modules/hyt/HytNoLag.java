package net.ccbluex.liquidbounce.features.module.modules.hyt;

import kotlin.Metadata;
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
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="HytNoLag", description="修复版", category=ModuleCategory.HYT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000*\n\n\n\b\n\b\n\b\n\n\u0000\n\n\b\n\n\b\b\u000020B¢J\b\r0HJ\b0HR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0\t8VX¢\b\nR\f0X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/hyt/HytNoLag;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "a", "", "b", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "tag", "", "getTag", "()Ljava/lang/String;", "ticks", "onEnable", "", "onUpdate", "Pride"})
public final class HytNoLag
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"AntiCheat", "AAC5"}, "AAC5");
    private int ticks;
    private int a;
    private int b;

    @Override
    public void onEnable() {
        this.ticks = 0;
    }

    @EventTarget
    public final void onUpdate() {
        String string = (String)this.modeValue.get();
        int n = 0;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
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
    @NotNull
    public String getTag() {
        return ((String)this.modeValue.get()).toString();
    }
}
