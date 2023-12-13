package dev.tenacity.scripting.api.bindings;

import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude(Strategy.NAME_REMAPPING)
public class EnumFacingBinding {
    public final int DOWN = 0,
            UP = 1,
            NORTH = 2,
            SOUTH = 3,
            WEST = 4,
            EAST = 5;
}
