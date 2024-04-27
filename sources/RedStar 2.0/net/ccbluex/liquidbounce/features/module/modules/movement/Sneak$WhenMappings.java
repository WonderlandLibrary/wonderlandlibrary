// ERROR: Unable to apply inner class name fixup
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.event.EventState;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3)
public final class Sneak$WhenMappings {
    public static final int[] $EnumSwitchMapping$0;

    static {
        $EnumSwitchMapping$0 = new int[EventState.values().length];
        Sneak$WhenMappings.$EnumSwitchMapping$0[EventState.PRE.ordinal()] = 1;
        Sneak$WhenMappings.$EnumSwitchMapping$0[EventState.POST.ordinal()] = 2;
    }
}
