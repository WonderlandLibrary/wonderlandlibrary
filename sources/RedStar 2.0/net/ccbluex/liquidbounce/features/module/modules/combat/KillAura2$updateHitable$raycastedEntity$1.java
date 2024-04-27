package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.util.Collection;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura2;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RaycastUtils;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000*\u0000\b\n\u000020J02\b0H¨"}, d2={"net/ccbluex/liquidbounce/features/module/modules/combat/KillAura2$updateHitable$raycastedEntity$1", "Lnet/ccbluex/liquidbounce/utils/RaycastUtils$EntityFilter;", "canRaycast", "", "entity", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "Pride"})
public static final class KillAura2$updateHitable$raycastedEntity$1
implements RaycastUtils.EntityFilter {
    final KillAura2 this$0;

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean canRaycast(@Nullable IEntity entity) {
        if (((Boolean)this.this$0.livingRaycastValue.get()).booleanValue()) {
            if (!MinecraftInstance.classProvider.isEntityLivingBase(entity)) return false;
            if (MinecraftInstance.classProvider.isEntityArmorStand(entity)) return false;
        }
        if (this.this$0.isEnemy(entity)) return true;
        if ((Boolean)this.this$0.raycastIgnoredValue.get() != false) return true;
        if ((Boolean)this.this$0.aacValue.get() == false) return false;
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        IEntity iEntity = entity;
        if (iEntity == null) {
            Intrinsics.throwNpe();
        }
        Collection<IEntity> collection = iWorldClient.getEntitiesWithinAABBExcludingEntity(entity, iEntity.getEntityBoundingBox());
        boolean bl = false;
        if (collection.isEmpty()) return false;
        return true;
    }

    KillAura2$updateHitable$raycastedEntity$1(KillAura2 $outer) {
        this.this$0 = $outer;
    }
}
