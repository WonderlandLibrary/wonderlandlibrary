package me.offeex.bloomware.client.module.pvp

import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.manager.managers.HoleManager
import me.offeex.bloomware.api.manager.managers.RotationManager
import me.offeex.bloomware.api.util.BlockUtil
import me.offeex.bloomware.api.util.MovementUtil
import me.offeex.bloomware.client.module.motion.Step
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventMovement
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.RaycastContext

object Surround : Obsidian("Surround", "Surrounds you with obsidian", Category.PVP) {
    private val autoCenter = setting("AutoCenter").bool(true)
    override val rotation = settingRotations("NCP")
    private val autoDisable = setting("Disable").enum("Off", "Completion", "MoveUp", "MoveY")
    private val disableOnStep = setting("DisableOnStep").bool(true)
    private val extraPlace = setting("ExtraPlace").enum("Safe", "AirPlace", "Off")
    private val fallSpeed = setting("FallSpeed").number(1, 1, 5, 0.1)
    private val fallThreshold = setting("FallThreshold").number(-0.3, -1, 0, 0.001)

    private var falling = false
    private var center = false
    private var stepWasEnabled: Boolean? = null

    override fun onEnable() {
        super.onEnable()
        center = true
    }

    override fun positionsToFill(): List<BlockPos> {
        val allPositions = mutableListOf<BlockPos>()

        val playerPos = cPlayer.pos
        val bottomPos = playerPos.withAxis(Direction.Axis.Y, cWorld.bottomY.toDouble())
        val finalPos = cWorld.raycast(RaycastContext(playerPos, bottomPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, cPlayer)).blockPos.up()

        for (pos in Direction.Type.HORIZONTAL) {
            val offsetPos = finalPos.offset(pos)

            if (!BlockUtil.isPlaceable(offsetPos, true)) continue

            allPositions.add(offsetPos)
            val dir = BlockUtil.findBlockSide(offsetPos)
            if (dir == null && extraPlace.like("Safe")) allPositions.add(offsetPos.down())
        }

        return allPositions.sortedBy { it.y }
    }

    override fun isComplete(): Boolean {
        return HoleManager.has(cPlayer.blockPos)
    }

    override fun handleRotations(pos: BlockPos) {
        if (rotation.like("NCP")) RotationManager.targetPitch = 90f
        super.handleRotations(pos)
    }

    @Subscribe
    private fun onStep(event: EventMovement.Step) {
        if (disableOnStep.toggled) disable()
    }

    override fun onTickExtend() {
        if (!complete) {

            if (wasComplete &&
                (autoDisable.like("MoveUp") && !cPlayer.verticalCollision && cPlayer.velocity.y > 0) ||
                (autoDisable.like("MoveY") && !cPlayer.verticalCollision)) {
                return disable()
            }

            if (cPlayer.fallDistance > 0.0f || cPlayer.velocity.y <= -0.0785) falling = true

            if (!wasComplete && autoCenter.toggled && cPlayer.isOnGround && center) {
                center = false
                cPlayer.velocity = Vec3d.ZERO
                MovementUtil.center()
            }

            if (BlockPos.stream(cPlayer.boundingBox).count() > 2) return RotationManager.reset()

            if (!cPlayer.isOnGround && cPlayer.velocity.y < fallThreshold.value && falling) {
                cPlayer.velocity = cPlayer.velocity.multiply(0.0, fallSpeed.value, 0.0)
            }

            if (!cWorld.getBlockState(cPlayer.blockPos.down()).isAir) {

                if (Step.disableOnSurround.toggled) Step.disable()
                if (Step.pauseOnSurround.toggled) {
                    stepWasEnabled = Step.enabled
                    Step.disable()
                }

                cPlayer.velocity = cPlayer.velocity.multiply(0.0, 1.0, 0.0)

                fillPositions()
            }
        } else {
            falling = false
            if (stepWasEnabled == true) Step.enable()
            if (autoDisable.like("Completion")) disable()
        }
    }
}