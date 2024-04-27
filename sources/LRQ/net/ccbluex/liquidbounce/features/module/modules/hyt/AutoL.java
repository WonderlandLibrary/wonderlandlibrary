/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.hyt;

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
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="HytAutoL", description="AutoL", category=ModuleCategory.HYT)
public final class AutoL
extends Module {
    private final BoolValue L = new BoolValue("L", true);
    private final TextValue AutoLmsg = new TextValue("AutoLmsg", "@[LRQ]L");
    private IEntityLivingBase target;
    private int kill;

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
    public final void onAttack(AttackEvent event) {
        this.target = (IEntityLivingBase)event.getTargetEntity();
    }

    @EventTarget
    public final void onUpdate(UpdateEvent event) {
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
                iEntityPlayerSP.sendChatMessage((String)this.AutoLmsg.get() + "\u6211\u5df2\u7ecf\u51fb\u6740\u4e86" + this.kill + "\u4eba ");
            }
            this.target = null;
        }
    }

    @Override
    public String getTag() {
        return "Kill " + this.kill;
    }
}

