package koks.event;

import koks.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

/**
 * Copyright 2021, Koks Team
 * Please don't use the code
 */

@Getter @Setter @AllArgsConstructor
public class HardnessEvent extends Event {
    float hardness;
    final Block block;
    final BlockPos blockPos;
}
