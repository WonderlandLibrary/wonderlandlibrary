package net.ccbluex.liquidbounce.features.module.modules.render;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.settings.IGameSettings;
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.event.ClientShutdownEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.render.XRay;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="Fullbright", description="Brightens up the world around you.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000,\n\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\b\u000020B¢J\b0\bHJ\b\t0\bHJ\n0\b2\b0\fHJ\r0\b2\b0HR0X¢\n\u0000R0X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/Fullbright;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "prevGamma", "", "onDisable", "", "onEnable", "onShutdown", "event", "Lnet/ccbluex/liquidbounce/event/ClientShutdownEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class Fullbright
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Gamma", "NightVision"}, "Gamma");
    private float prevGamma = -1.0f;

    @Override
    public void onEnable() {
        this.prevGamma = MinecraftInstance.mc.getGameSettings().getGammaSetting();
    }

    @Override
    public void onDisable() {
        block1: {
            if (this.prevGamma == -1.0f) {
                return;
            }
            MinecraftInstance.mc.getGameSettings().setGammaSetting(this.prevGamma);
            this.prevGamma = -1.0f;
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) break block1;
            iEntityPlayerSP.removePotionEffectClient(MinecraftInstance.classProvider.getPotionEnum(PotionType.NIGHT_VISION).getId());
        }
    }

    @EventTarget(ignoreCondition=true)
    public final void onUpdate(@Nullable UpdateEvent event) {
        block12: {
            block11: {
                block10: {
                    if (this.getState()) break block10;
                    Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(XRay.class);
                    if (module == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!module.getState()) break block11;
                }
                String string = (String)this.modeValue.get();
                boolean bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                String string3 = string2.toLowerCase();
                Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
                string = string3;
                switch (string.hashCode()) {
                    case -820818432: {
                        if (string.equals("nightvision")) {
                            break;
                        }
                        break block12;
                    }
                    case 98120615: {
                        if (string.equals("gamma") && MinecraftInstance.mc.getGameSettings().getGammaSetting() <= 100.0f) {
                            IGameSettings iGameSettings = MinecraftInstance.mc.getGameSettings();
                            float f = iGameSettings.getGammaSetting();
                            iGameSettings.setGammaSetting(f + 1.0f);
                        }
                        break block12;
                    }
                }
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP != null) {
                    iEntityPlayerSP.addPotionEffect(MinecraftInstance.classProvider.createPotionEffect(MinecraftInstance.classProvider.getPotionEnum(PotionType.NIGHT_VISION).getId(), 1337, 1));
                }
                break block12;
            }
            if (this.prevGamma != -1.0f) {
                MinecraftInstance.mc.getGameSettings().setGammaSetting(this.prevGamma);
                this.prevGamma = -1.0f;
            }
        }
    }

    @EventTarget(ignoreCondition=true)
    public final void onShutdown(@Nullable ClientShutdownEvent event) {
        this.onDisable();
    }
}
