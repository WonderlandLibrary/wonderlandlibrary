package me.offeex.bloomware.event.events

import me.offeex.bloomware.event.Event
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.Entity as MCEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

abstract class EventAttack : Event() {
    class Entity(val player: PlayerEntity, val entity: MCEntity) : EventAttack()
    class Block(val pos: BlockPos, val direction: Direction) : EventAttack()
}