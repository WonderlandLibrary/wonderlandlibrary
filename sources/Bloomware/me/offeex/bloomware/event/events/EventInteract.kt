package me.offeex.bloomware.event.events

import me.offeex.bloomware.event.EventReturnable
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.entity.Entity as EntityMC

abstract class EventInteract(val player: PlayerEntity, val hand: Hand) : EventReturnable() {
    open class Entity(p: PlayerEntity, h: Hand, val entity: EntityMC) : EventInteract(p, h)
    class EntityAtLocation(p: PlayerEntity, h: Hand, val hitResult: EntityHitResult, val entity: EntityMC) : EventInteract(p, h)
    class Block(p: PlayerEntity, h: Hand, val hitResult: BlockHitResult) : EventInteract(p, h)
    class Item(p: PlayerEntity, h: Hand) : EventInteract(p, h)
}