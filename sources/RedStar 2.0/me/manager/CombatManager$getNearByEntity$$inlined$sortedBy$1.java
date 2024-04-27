// ERROR: Unable to apply inner class name fixup
package me.manager;

import java.util.Comparator;
import kotlin.Metadata;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3, d1={"\u0000\n\u0000\n\b\n\b\n\b\n\b\n\b\n\b\n\b\u00000\"\b\u00002\n *HH2\n *HHH\n¢\b¨\b"}, d2={"<anonymous>", "", "T", "a", "kotlin.jvm.PlatformType", "b", "compare", "(Ljava/lang/Object;Ljava/lang/Object;)I", "kotlin/comparisons/ComparisonsKt__ComparisonsKt$compareBy$2"})
public static final class CombatManager$getNearByEntity$.inlined.sortedBy.1<T>
implements Comparator<T> {
    @Override
    public final int compare(T a, T b) {
        boolean bl = false;
        IEntity it = (IEntity)a;
        boolean bl2 = false;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        Comparable comparable = Float.valueOf(it.getDistanceToEntity(iEntityPlayerSP));
        it = (IEntity)b;
        Comparable comparable2 = comparable;
        bl2 = false;
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        Float f = Float.valueOf(it.getDistanceToEntity(iEntityPlayerSP2));
        return ComparisonsKt.compareValues(comparable2, (Comparable)f);
    }
}
