/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.BlockPos
 */
package me.report.liquidware.modules.render;

import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.hud.HUD;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.SoundFxPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;

@ModuleInfo(name="PlayerPosition", description=":/", category=ModuleCategory.RENDER, array=false)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0006\u001a\u00020\u0007H\u0002J\b\u0010\b\u001a\u00020\u0007H\u0016R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2={"Lme/report/liquidware/modules/render/PlayerPosition;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "collectedEntities", "", "Lnet/minecraft/entity/Entity;", "collectEntities", "", "onEnable", "KyinoClient"})
public final class PlayerPosition
extends Module {
    private final List<Entity> collectedEntities = new ArrayList();

    /*
     * WARNING - void declaration
     */
    @Override
    public void onEnable() {
        this.collectEntities();
        int collectedEntitiesSize = this.collectedEntities.size();
        int n = 0;
        int n2 = collectedEntitiesSize;
        while (n < n2) {
            void i;
            Entity entity = this.collectedEntities.get((int)i);
            new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.VICTORY, -8.0f);
            HUD hUD = LiquidBounce.INSTANCE.getHud();
            StringBuilder stringBuilder = new StringBuilder().append("\u00a7bName : \u00a77").append(entity.func_70005_c_()).append(" \u00a7a, X : \u00a77");
            BlockPos blockPos = entity.func_180425_c();
            Intrinsics.checkExpressionValueIsNotNull(blockPos, "entity.position");
            StringBuilder stringBuilder2 = stringBuilder.append(blockPos.func_177958_n()).append(" \u00a7a, Y : \u00a77");
            BlockPos blockPos2 = entity.func_180425_c();
            Intrinsics.checkExpressionValueIsNotNull(blockPos2, "entity.position");
            StringBuilder stringBuilder3 = stringBuilder2.append(blockPos2.func_177956_o()).append(" \u00a7a, Z : \u00a77");
            BlockPos blockPos3 = entity.func_180425_c();
            Intrinsics.checkExpressionValueIsNotNull(blockPos3, "entity.position");
            hUD.addNotification(new Notification(stringBuilder3.append(blockPos3.func_177952_p()).toString(), Notification.Type.INFO));
            ++i;
        }
        this.collectedEntities.clear();
        this.setState(false);
    }

    /*
     * WARNING - void declaration
     */
    private final void collectEntities() {
        this.collectedEntities.clear();
        List list = PlayerPosition.access$getMc$p$s1046033730().field_71441_e.field_72996_f;
        Intrinsics.checkExpressionValueIsNotNull(list, "mc.theWorld.loadedEntityList");
        List playerEntities = list;
        int playerEntitiesSize = playerEntities.size();
        int n = 0;
        int n2 = playerEntitiesSize;
        while (n < n2) {
            void i;
            Object e = playerEntities.get((int)i);
            if (e == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.minecraft.entity.Entity");
            }
            Entity entity = (Entity)e;
            if (EntityUtils.isSelected(entity, true)) {
                this.collectedEntities.add(entity);
            }
            ++i;
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

