package me.offeex.bloomware.client.module.pvp

import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.api.helper.Stopwatch
import me.offeex.bloomware.api.helper.cInteractManager
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.manager.managers.RotationManager
import me.offeex.bloomware.api.util.CPlayerUtil
import me.offeex.bloomware.api.util.EntityUtil.getFullHealth
import me.offeex.bloomware.api.util.InventoryUtil
import me.offeex.bloomware.api.util.InventoryUtil.getDurability
import me.offeex.bloomware.api.util.MathUtil
import me.offeex.bloomware.api.util.WorldUtil
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventPacket
import net.minecraft.block.Blocks
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.ExperienceOrbEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.decoration.EndCrystalEntity
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity
import net.minecraft.item.EndCrystalItem
import net.minecraft.item.Items
import net.minecraft.item.SwordItem
import net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.RaycastContext
import net.minecraft.world.RaycastContext.FluidHandling
import net.minecraft.world.RaycastContext.ShapeType
import java.util.concurrent.Executors
import java.util.concurrent.RejectedExecutionException

object AutoCrystal : Module("AutoCrystal", "Places and breaks crystals", Category.PVP) {

    /* *************** Break settings *************** */

    private val breaking = setting("Break").group(true)
    private val breakDelay = breaking.setting("TickDelay").number(3, 0, 20)
    private val breakRange = breaking.setting("Range").number(4.2, 1, 5.5, 0.1)
    private val breakWallRange = breaking.setting("WallRange").number(3.5, 0, 5, 0.1)
    private val instaBreak = breaking.setting("Instant").bool()

    /* *************** Place settings *************** */

    private val placing = setting("Place").group(true)
    private val placeDelay = placing.setting("TickDelay").number(1, 0, 20)
    private val placeRange = placing.setting("Range").number(4.5, 1, 5.5, 0.1)
    private val placeWallRange = placing.setting("WallRange").number(3.5, 0, 5, 0.1)
    private val instaPlace = placing.setting("Instant").bool()
    private val autoSwitch = placing.setting("AutoSwitch").bool(true)
    private val protocol = placing.setting("Protocol").enum("1.13+", "1.12")

    /* *************** Calculation settings *************** */

    private val calculation = setting("Calculation").group()
    private val calcDelay = calculation.setting("TickDelay").number(1, 0, 20)
    private val calcRange = calculation.setting("Range").number(4.5, 1, 5.5, 0.1)
    private val calcWallRange = calculation.setting("WallRange").number(3.5, 0, 5, 0.1)

    /* *************** Behavior settings *************** */

    private val behavior = setting("Behavior").group()
    private val minDmg = behavior.setting("MinDMG").number(5, 1, 20, 0.1)
    private val maxSelfDmg = behavior.setting("MaxSelfDMG").number(5, 1, 36, 0.1)
    private val targetRange = behavior.setting("Target").number(8, 1, 12, 0.1)
    private val ignoreTerrain = behavior.setting("IgnoreTerrain").bool()
    private val antiWeakness = behavior.setting("AntiWeakness").bool()
    private val safeHp = behavior.setting("SafeHP").number(8, 0, 36, 0.1)
    private val rageHp = behavior.setting("RageHP").number(8, 0, 36, 0.1)

    private val rotate = setting("Rotate").group(false)
    private val breakRotate = rotate.setting("OnBreak").bool()
    private val placeRotate = rotate.setting("OnPlace").bool()

    /* *************** FacePlace settings *************** */

    private val faceplace = setting("FacePlace").group(false)
    private val minFaceplaceDmg = faceplace.setting("MinDMG").number(0.5, 0.5, 5, 0.01)
    private val faceplaceHP = faceplace.setting("Health").number(10, 5, 36, 0.1)
    private val faceplaceArmorHP = faceplace.setting("ArmorHealth").number(30, 0, 100, 1)

    /* *************** AutoPause settings *************** */

    private val autoPause = setting("AutoPause").group()
    private val antiSuicide = autoPause.setting("AntiSuicide").bool(true)
    private val stopHp = autoPause.setting("StopHP").number(6, 0, 36, 0.1)
    private val whileMining = autoPause.setting("WhileMining").bool()
    private val whileConsuming = autoPause.setting("WhileConsuming").bool()
    private val whileMending = autoPause.setting("WhileMending").bool()

    /* *************** Target settings *************** */

    private val targets = setting("Targets").group()
    private val targetPriority = targets.setting("Prioritize").enum("Health", "Distance")
    private val players = targets.setting("Players").bool(true)
    private val friends = targets.setting("Friends").bool()
    private val animals = targets.setting("Animals").bool()
    private val hostiles = targets.setting("Hostiles").bool(true)

    private val multiThread = setting("MultiThread").group(true)
    private val threadCalc = multiThread.setting("Calculation").bool()
    private val threadInstaBreak = multiThread.setting("InstaBreak").bool()
    private val threadInstaPlace = multiThread.setting("InstaPlace").bool()


    /* *************** General *************** */

    private val breakStopwatch = Stopwatch()
    private val placeStopwatch = Stopwatch()
    private val calcStopwatch = Stopwatch()

    private var target: LivingEntity? = null
    private var spot: CrystalSpot? = null

    private var paused = false

    private val executor = Executors.newFixedThreadPool(3)


    init {
        safeHp.description = "Prioritizes least hurtful spot for you, if your health is lower than this value"
        rageHp.description = "Prioritizes to deal most damage to target, if target's health is lower than this value"
    }

    @Subscribe
    private fun onPacketSend(event: EventPacket.Send) {
//        when(val packet = event.packet) {
//            is PlayerMoveC2SPacket.LookAndOnGround -> {
//                println("${packet.getYaw(0f)} ${packet.getPitch(0f)}")
//            }
//        }
    }

    @Subscribe
    private fun onPacketReceive(event: EventPacket.Receive) {
        val packet = event.packet
        if (event.shift) return

        // Pausing
        if (packet is EntitiesDestroyS2CPacket || packet is EntitySpawnS2CPacket) {
            if (paused) return
        }

        when (packet) {

            // InstaBreak
            is EntitySpawnS2CPacket -> {
                if (packet.entityType != EntityType.END_CRYSTAL || !instaBreak.toggled) return

                // Creating a fake crystal entity
                val crystal = EndCrystalEntity(cWorld, packet.x, packet.y, packet.z)

                // Checking if the crystal is valid
                if (!allowBreak || spot!!.crystalPos != crystal.blockPos) {
                    return
                }

                // Copying all the data from the packet to the crystal
                crystal.id = packet.id
                crystal.uuid = packet.uuid
                crystal.velocity = Vec3d(packet.velocityX, packet.velocityY, packet.velocityZ)
                crystal.yaw = packet.yaw
                crystal.pitch = packet.pitch
                crystal.headYaw = packet.headYaw

                // Now that crystal is valid, we can set it
                execute(threadInstaBreak.toggled) { doBreak(crystal) }
            }

            // InstaPlace
            is EntitiesDestroyS2CPacket -> {
                if (!allowPlace || !instaPlace.toggled) return

                val handWithCrystal = InventoryUtil.findHand<EndCrystalItem>() ?: return

                for (id in packet.entityIds.intIterator()) {
                    val entity = cWorld.getEntityById(id) ?: continue

                    if (entity !is EndCrystalEntity || entity.blockPos != spot!!.crystalPos) continue

                    execute(threadInstaPlace.toggled) {
                        cWorld.removeEntity(entity.id, Entity.RemovalReason.KILLED)
                        doPlace(handWithCrystal)
                    }

                    break
                }
            }
        }
    }

    override fun onEnable() = reset()
    override fun onDisable() {
        reset()
        RotationManager.reset()
    }

    override fun onTick() {

        val hp = cPlayer.getFullHealth()
        if (hp <= stopHp.value) return run { paused = true }

        target = WorldUtil.target(
            targetRange.value,
            targetPriority.selected,
            players.toggled,
            friends.toggled,
            animals.toggled,
            hostiles.toggled
        ) ?: return run { paused = true }

        if (calcStopwatch.passedTicks(calcDelay.value)) {
            execute(threadCalc.toggled) { spot = sortSpots(target!!) }
            calcStopwatch.reset()
        }

        val pauseOnMine = CPlayerUtil.isMining && whileMining.toggled
        val pauseOnEat = CPlayerUtil.isConsuming && whileConsuming.toggled
        val pauseOnMending = CPlayerUtil.isMending && whileMending.toggled
        paused = spot == null || target == null || pauseOnMine || pauseOnEat || pauseOnMending

        if (paused) return

        val pos = spot!!.crystalPos
        val crystal = findCrystalNearby(pos)

        if (allowPlace && crystal == null) doPlace()
        if (allowBreak && crystal != null) doBreak(crystal)

        /*val targetPos = closestVertex(pos)
        val targetYaw = CPlayerUtil.getLookYaw(targetPos) // -180
        var currentYaw = MathHelper.wrapDegrees(RotationManager.packetYaw) // 0

        if (targetYaw == currentYaw) {
            if (allowPlace && crystal == null) doPlace()
            if (allowBreak && crystal != null) doBreak(crystal)
        } else {
            val yawDelta = MathHelper.wrapDegrees(targetYaw - currentYaw)
            val yawSign = sign(yawDelta)

            currentYaw += min(yawStep.value.toFloat() * yawSign, yawDelta)

            RotationManager.setRotation(currentYaw)
            RotationManager.sendPacket()
            return
        } TODO: YawStep */
    }

    private fun reset() {
        target = null
        spot = null

        // So that we don't have to wait that time on next enable
        breakStopwatch.reset()
        placeStopwatch.reset()
        calcStopwatch.reset()

        executor.shutdown()
    }

    private fun doBreak(crystal: EndCrystalEntity) {
        breakStopwatch.reset()

        val weakened = cPlayer.hasStatusEffect(StatusEffects.WEAKNESS)
        val strength = cPlayer.getStatusEffect(StatusEffects.STRENGTH)
        val counters = strength != null && strength.amplifier >= 1
        val hasSword = InventoryUtil.findHand<SwordItem>() == Hand.MAIN_HAND
        if (antiWeakness.toggled && weakened && !counters && !hasSword) {
            InventoryUtil.switchToItem<SwordItem>()
            return
        }

        if (breakRotate.toggled) RotationManager.rotateTo(crystal.eyePos)

        cInteractManager.attackEntity(cPlayer, crystal)
        cPlayer.swingHand(Hand.MAIN_HAND)
    }

    private fun doPlace(hand: Hand? = null) {

        val h = autoSwitch(hand) ?: return
        placeStopwatch.reset()

        if (placeRotate.toggled) RotationManager.rotateTo(spot!!.blockVertex)

        val ray = spot!!.rayBlock
        val dir = if (isVisibleThrough(ray)) ray.side.opposite else Direction.UP
        cInteractManager.interactBlock(
            cPlayer,
            h,
            BlockHitResult(spot!!.blockVertex, dir, spot!!.blockPos, false)
        )
        cPlayer.swingHand(h)
    }

    private fun autoSwitch(hand: Hand?): Hand? {
        if (hand != null) return hand

        val item = Items.END_CRYSTAL
        if (autoSwitch.toggled && !cPlayer.offHandStack.isOf(item)) {
            InventoryUtil.switchToItem(item)
        }

        return InventoryUtil.findHand<EndCrystalItem>()
    }

    // Sorts all the spots by their damage
    private fun sortSpots(target: LivingEntity): CrystalSpot? {
        val hp = cPlayer.getFullHealth()
        val targetHP = target.getFullHealth()

        val comp = when {
            hp <= safeHp.value -> safeComparator
            targetHP <= rageHp.value -> rageComparator
            else -> balanceComparator
        }

        return findPossibleSpots(target).sortedWith(comp).firstOrNull()
    }

    private fun findPossibleSpots(target: LivingEntity): MutableList<CrystalSpot> {
        val places = mutableListOf<CrystalSpot>()
        val range = calcRange.value

        // Iterates over all blocks spherically around player, does necessary checks and adds block to places list.
        WorldUtil.forEachBlock(range) { x, y, z ->

            val blockPos = BlockPos(x, y, z)
            val state = cWorld.getBlockState(blockPos)
            if (!state.isOf(Blocks.BEDROCK) && !state.isOf(Blocks.OBSIDIAN)) return@forEachBlock

            // MaxSelfDmg handling
            val crystalPos = blockPos.up()
            val selfDmg = MathUtil.getCrystalDamage(crystalPos, cPlayer)
            if (selfDmg >= maxSelfDmg.value) return@forEachBlock

            // AntiSuicide handling
            if (selfDmg >= cPlayer.getFullHealth() && antiSuicide.toggled) return@forEachBlock

            // Faceplace and MinDMG handling
            val targetDmg = MathUtil.getCrystalDamage(crystalPos, target, ignoreTerrain.toggled)
            if (target is PlayerEntity && faceplace.toggled && targetDmg > minFaceplaceDmg.value) {
                val durability = getMostBrokenArmorItemHP(target)
                val passHealthCheck = target.health <= faceplaceHP.value
                val passArmorCheck = durability != null && durability <= faceplaceArmorHP.value * 0.01
                if (!passHealthCheck && !passArmorCheck) return@forEachBlock
            } else if (targetDmg < minDmg.value) {
                return@forEachBlock
            }

            if (!canPlaceCrystal(crystalPos)) return@forEachBlock

            // Raytrace handling, checks if the crystal is visible through the block
            // BlockVertex is stored, so it can be used later for strict placing
            val blockVertex = closestVertex(blockPos)
            val rayBlock = findObstacle(blockVertex)
            val rayCrystal = findObstacle(closestVertex(crystalPos))
            val visible = isVisibleThrough(rayCrystal) && isVisibleThrough(rayBlock)
            val inWallRange = crystalPos.isWithinDistance(cPlayer.eyePos, calcWallRange.value)
            if (!inWallRange && !visible) return@forEachBlock

            places.add(CrystalSpot(blockPos, crystalPos, selfDmg, targetDmg, rayBlock, rayCrystal, blockVertex))
        }

        return places
    }

    private inline val allowBreak: Boolean
        get() {
            val inRange = spot!!.crystalPos.isWithinDistance(cPlayer.eyePos, breakRange.value)
            val inWallRange = spot!!.crystalPos.isWithinDistance(cPlayer.eyePos, breakWallRange.value)
            return breaking.toggled
                && breakStopwatch.passedTicks(breakDelay.value)
                && inRange
                && (inWallRange || isVisibleThrough(spot!!.rayCrystal))
        }
    private inline val allowPlace: Boolean
        get() {
            val inRange = spot!!.blockPos.isWithinDistance(cPlayer.eyePos, placeRange.value)
            val inWallRange = spot!!.blockPos.isWithinDistance(cPlayer.eyePos, placeWallRange.value)
            return placing.toggled
                && placeStopwatch.passedTicks(placeDelay.value)
                && inRange
                && (inWallRange || isVisibleThrough(spot!!.rayBlock))
        }

    private fun isVisibleThrough(ray: BlockHitResult) = ray.type == HitResult.Type.MISS

    private fun canPlaceCrystal(pos: BlockPos): Boolean = cWorld.run {

        // Is there enough space for crystal to be placed?
        val enoughSpace = isAir(pos) && (isAir(pos.up()) || protocol.like("1.13+"))

        // Is there any entity inside or on the block?
        val spaceEmpty = getBlockState(pos).isAir && getOtherEntities(null, Box(pos)) {
            it !is ExperienceOrbEntity && it !is ExperienceBottleEntity && it !is EndCrystalEntity
        }.isEmpty()

        return enoughSpace && spaceEmpty
    }

    private fun findCrystalNearby(pos: BlockPos): EndCrystalEntity? {
        val crystalBox = Box(
            pos.x - 1.5, pos.y + 0.0, pos.z - 1.5,
            pos.x + 1.5, pos.y + 2.0, pos.z + 1.5,
        )
        return cWorld.getOtherEntities(null, crystalBox) {
            it is EndCrystalEntity
        }.firstOrNull() as EndCrystalEntity?
    }


    private fun findObstacle(pos: Vec3d) =
        cWorld.raycast(RaycastContext(cPlayer.eyePos, pos, ShapeType.VISUAL, FluidHandling.NONE, cPlayer))

    private fun getMostBrokenArmorItemHP(target: PlayerEntity): Double? {
        return target.armorItems
            .asSequence()
            .filter { it.isDamageable }
            .map { it.getDurability() }
            .minOrNull()
    }

    private fun closestVertex(pos: BlockPos): Vec3d {
        val v = vertexOffsets.minBy {
            cPlayer.squaredDistanceTo(it.x + pos.x, it.y + pos.y, it.z + pos.z)
        }

        return Vec3d(v.x + pos.x, v.y + pos.y, v.z + pos.z)
    }

    private val vertexOffsets = arrayOf(
        Vec3d(1.0, 1.0, 1.0),
        Vec3d(0.0, 1.0, 1.0),
        Vec3d(1.0, 0.0, 1.0),
        Vec3d(0.0, 0.0, 1.0),
        Vec3d(1.0, 1.0, 0.0),
        Vec3d(0.0, 1.0, 0.0),
        Vec3d(1.0, 0.0, 0.0),
        Vec3d(0.0, 0.0, 0.0)
    )

    private val balanceComparator = compareByDescending<CrystalSpot> { it.targetDmg / it.selfDmg }
    private val safeComparator = compareBy<CrystalSpot> { it.selfDmg }.thenBy { it.targetDmg }
    private val rageComparator = compareByDescending<CrystalSpot> { it.targetDmg }.thenBy { it.selfDmg }

    private inline fun execute(check: Boolean, crossinline callback: () -> Unit) {
        if (multiThread.toggled && check) try {
            executor.execute { callback() }
        } catch (e: RejectedExecutionException) {
            Bloomware.LOGGER.warn("Failed to MultiThread task, executing in main thread")
            callback()
        }
        else callback()
    }

    private data class CrystalSpot(
        val blockPos: BlockPos,
        val crystalPos: BlockPos,
        val selfDmg: Double,
        val targetDmg: Double,
        val rayBlock: BlockHitResult,
        val rayCrystal: BlockHitResult,
        val blockVertex: Vec3d,
    )
}
