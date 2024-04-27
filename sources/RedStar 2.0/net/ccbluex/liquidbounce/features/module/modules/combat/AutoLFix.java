package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.TextValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="AutoL", description="AutoL", category=ModuleCategory.COMBAT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000D\n\n\n\b\n\n\u0000\n\n\u0000\n\b\n\b\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\b\b\u000020B¢J020HJ020HJ\b0HR0X¢\n\u0000R0X¢\n\u0000R0\bX¢\n\u0000\b\t\n\"\b\fR\r08VX¢\bR0X¢\n\u0000\b\"\b¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/AutoLFix;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "AutoLmsg", "Lnet/ccbluex/liquidbounce/value/TextValue;", "L", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "kill", "", "getKill", "()I", "setKill", "(I)V", "tag", "", "getTag", "()Ljava/lang/String;", "target", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "getTarget", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "setTarget", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;)V", "onAttack", "", "event", "Lnet/ccbluex/liquidbounce/event/AttackEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "runAttack", "Pride"})
public final class AutoLFix
extends Module {
    private final BoolValue L = new BoolValue("L", true);
    private final TextValue AutoLmsg = new TextValue("AutoLmsg", "@[RedStar]");
    @Nullable
    private IEntityLivingBase target;
    private int kill;

    @Nullable
    public final IEntityLivingBase getTarget() {
        return this.target;
    }

    public final void setTarget(@Nullable IEntityLivingBase iEntityLivingBase) {
        this.target = iEntityLivingBase;
    }

    public final int getKill() {
        return this.kill;
    }

    public final void setKill(int n) {
        this.kill = n;
    }

    private final void runAttack() {
        if (this.target == null) {
            return;
        }
        this.target = null;
    }

    @EventTarget
    public final void onAttack(@NotNull AttackEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        this.target = (IEntityLivingBase)event.getTargetEntity();
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (this.target != null) {
            IEntityLivingBase iEntityLivingBase = this.target;
            if (iEntityLivingBase == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityLivingBase.isDead()) {
                ++this.kill;
                if (((Boolean)this.L.get()).booleanValue()) {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP.sendChatMessage((String)this.AutoLmsg.get() + "我已经击杀了" + this.kill + "人 ");
                }
                this.target = null;
            }
        }
    }

    @Override
    @Nullable
    public String getTag() {
        return "Kill " + this.kill;
    }
}
