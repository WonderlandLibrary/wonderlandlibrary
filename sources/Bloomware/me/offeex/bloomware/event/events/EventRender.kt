package me.offeex.bloomware.event.events

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.event.Event
import net.minecraft.block.BlockState
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.model.EntityModel as MCEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity as MCEntity
import net.minecraft.entity.LivingEntity as MCLivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.BlockRenderView

abstract class EventRender(
    val matrices: MatrixStack,
    val tickDelta: Float,
) : Event() {

    class Entity(
        val entity: net.minecraft.entity.Entity,
        val x: Double,
        val y: Double,
        val z: Double,
        val yaw: Double,
        tickDelta: Float,
        matrices: MatrixStack,
        val vertexConsumers: VertexConsumerProvider,
        val light: Int,
    ) : EventRender(matrices, tickDelta)

    class BlockEntity(
        val entity: net.minecraft.block.entity.BlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        val vertexConsumers: VertexConsumerProvider,
    ) : EventRender(matrices, tickDelta)

    class Block(
        val state: BlockState,
        val pos: BlockPos,
        val world: BlockRenderView,
        matrices: MatrixStack,
        val vertexConsumer: VertexConsumer,
        val cull: Boolean,
        val random: Random
    ) : EventRender(matrices, mc.tickDelta)

    class HUD(matrix: MatrixStack, delta: Float) : EventRender(matrix, delta)

    class HeldItem(
        val player: AbstractClientPlayerEntity,
        tickDelta: Float,
        val pitch: Float,
        val hand: Hand,
        val swingProgress: Float,
        val item: ItemStack,
        val equipProgress: Float,
        stack: MatrixStack,
        val vertexConsumers: VertexConsumerProvider,
        val light: Int,
    ) : EventRender(stack, tickDelta)

    class World(
        tickdelta: Float,
        val limitTime: Long,
        matrices: MatrixStack,
    ) : EventRender(matrices, tickdelta)

    class Label(
        val entity: net.minecraft.entity.Entity,
        val yaw: Float,
        tickDelta: Float,
        matrices: MatrixStack,
        val vertexConsumers: VertexConsumerProvider,
        val light: Int
    ) : EventRender(matrices, tickDelta)

    class CrosshairTarget(
        matrices: MatrixStack,
        val vertexConsumer: VertexConsumer,
        val focusedEntity: net.minecraft.entity.Entity,
        val cameraX: Double,
        val cameraY: Double,
        val cameraZ: Double,
        val blockPos: BlockPos,
        val blockState: BlockState
    ) : EventRender(matrices, mc.tickDelta)

    class Screen(
        matrices: MatrixStack,
        val mouseX: Int,
        val mouseY: Int,
        delta: Float
    ) : EventRender(matrices, delta)

    abstract class TitleScreen(ms: MatrixStack, delta: Float) : EventRender(ms, delta) {
        class AfterPanorama(
            matrices: MatrixStack,
            val mouseX: Int,
            val mouseY: Int,
            delta: Float
        ) : EventRender(matrices, delta)
    }

    abstract class LivingEntity(val entity: MCLivingEntity) : EventRender(MatrixStack(), mc.tickDelta) {
        class HeadYaw(e: MCLivingEntity, var yaw: Float) : LivingEntity(e)
        class BodyYaw(e: MCLivingEntity, var yaw: Float) : LivingEntity(e)
        class HeadPitch(e: MCLivingEntity, var pitch: Float) : LivingEntity(e)
    }

    class EntityModel(val renderer: LivingEntityRenderer<*, *>, val currentLayer: RenderLayer, val entity: MCLivingEntity, m: MatrixStack, val light: Int, val overlay: Int, t: Float) : EventRender(m, t)
}