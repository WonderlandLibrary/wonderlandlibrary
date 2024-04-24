package me.offeex.bloomware.event.events

import me.offeex.bloomware.event.Event
import me.offeex.bloomware.event.EventReturnable
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

abstract class EventBreakBlock : EventReturnable() {
    class Cancel : EventBreakBlock()
    class Update(val pos: BlockPos, val direction: Direction) : EventBreakBlock()
    class Finish(val pos: BlockPos) : EventBreakBlock()
}