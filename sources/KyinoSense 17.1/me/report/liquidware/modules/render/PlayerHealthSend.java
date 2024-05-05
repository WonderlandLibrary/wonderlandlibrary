/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.render;

import java.util.HashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.report.liquidware.utils.timer.TimerUtil;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="PlayerHealthSend", description="Debug Health", category=ModuleCategory.PLAYER, array=false)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0007R*\u0010\u0003\u001a\u001e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004j\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0006`\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2={"Lme/report/liquidware/modules/render/PlayerHealthSend;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "healthData", "Ljava/util/HashMap;", "", "", "Lkotlin/collections/HashMap;", "timer", "Lme/report/liquidware/utils/timer/TimerUtil;", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class PlayerHealthSend
extends Module {
    private final TimerUtil timer = new TimerUtil();
    private final HashMap<Integer, Float> healthData = new HashMap();

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        for (Entity entity : PlayerHealthSend.access$getMc$p$s1046033730().field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityLivingBase) || !EntityUtils.isSelected(entity, true)) continue;
            EntityPlayerSP entityPlayerSP = PlayerHealthSend.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            Integer n = entityPlayerSP.func_145782_y();
            EntityPlayerSP entityPlayerSP2 = PlayerHealthSend.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
            Float f = this.healthData.getOrDefault(n, Float.valueOf(entityPlayerSP2.func_110138_aP()));
            Intrinsics.checkExpressionValueIsNotNull(f, "healthData.getOrDefault(\u2026d,mc.thePlayer.maxHealth)");
            float lastHealth = ((Number)f).floatValue();
            Map map = this.healthData;
            EntityPlayerSP entityPlayerSP3 = PlayerHealthSend.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP3, "mc.thePlayer");
            Integer n2 = entityPlayerSP3.func_145782_y();
            EntityPlayerSP entityPlayerSP4 = PlayerHealthSend.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP4, "mc.thePlayer");
            map.put(n2, Float.valueOf(entityPlayerSP4.func_110143_aJ()));
            EntityPlayerSP entityPlayerSP5 = PlayerHealthSend.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP5, "mc.thePlayer");
            if (lastHealth == entityPlayerSP5.func_110143_aJ()) continue;
            EntityPlayerSP entityPlayerSP6 = PlayerHealthSend.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP6, "mc.thePlayer");
            if (lastHealth > entityPlayerSP6.func_110143_aJ()) {
                StringBuilder stringBuilder = new StringBuilder().append("\u00a7c\u6263\u9664\u8840\u91cf\u00a7a");
                EntityPlayerSP entityPlayerSP7 = PlayerHealthSend.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP7, "mc.thePlayer");
                StringBuilder stringBuilder2 = stringBuilder.append(lastHealth - entityPlayerSP7.func_110143_aJ()).append("HP").append(" \u00a7f| ").append("\u00a7c\u5f53\u524d\u8840\u91cf\u00a7a");
                EntityPlayerSP entityPlayerSP8 = PlayerHealthSend.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP8, "mc.thePlayer");
                ClientUtils.displayChatMessage(stringBuilder2.append(entityPlayerSP8.func_110143_aJ()).append("HP").toString());
                continue;
            }
            StringBuilder stringBuilder = new StringBuilder().append("\u00a7c\u589e\u52a0\u8840\u91cf\u00a7a");
            EntityPlayerSP entityPlayerSP9 = PlayerHealthSend.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP9, "mc.thePlayer");
            float f2 = lastHealth - entityPlayerSP9.func_110143_aJ();
            StringBuilder stringBuilder3 = stringBuilder;
            boolean bl = false;
            float f3 = Math.abs(f2);
            StringBuilder stringBuilder4 = stringBuilder3.append(f3).append("HP").append(" \u00a7f| ").append("\u00a7c\u5f53\u524d\u8840\u91cf\u00a7a");
            EntityPlayerSP entityPlayerSP10 = PlayerHealthSend.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP10, "mc.thePlayer");
            ClientUtils.displayChatMessage(stringBuilder4.append(entityPlayerSP10.func_110143_aJ()).append("HP").toString());
        }
        if (this.timer.delay(220.0f)) {
            EntityPlayerSP entityPlayerSP = PlayerHealthSend.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            if (entityPlayerSP.func_110143_aJ() < 10.0f) {
                ClientUtils.displayChatMessage("\u00a7c[Waring]\u00a76\u60a8\u5f53\u524d\u8840\u91cf\u5c0f\u4e8e10\u00a7cHP");
            }
        }
        this.timer.reset();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

