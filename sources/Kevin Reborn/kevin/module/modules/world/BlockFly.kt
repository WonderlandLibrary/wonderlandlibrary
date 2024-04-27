package kevin.module.modules.world

import kevin.utils.PacketUtils.sendPacket
import kevin.event.*
import kevin.main.KevinClient
import kevin.module.*
import kevin.module.modules.render.BlockOverlay
import kevin.utils.*
import kevin.utils.BlockUtils.canBeClicked
import kevin.utils.BlockUtils.getCenterDistance
import kevin.utils.BlockUtils.isReplaceable
import kevin.utils.MovementUtils.isMoving
import kevin.utils.MovementUtils.strafe
import kevin.utils.RandomUtils.nextFloat
import kevin.utils.RenderUtils.drawBlockBox
import kevin.utils.RenderUtils.drawBorderedRect
import kevin.utils.RotationUtils.*
import kevin.utils.TimeUtils.randomClickDelay
import kevin.utils.TimeUtils.randomDelay
import net.minecraft.block.BlockBush
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager.resetColor
import net.minecraft.client.settings.GameSettings
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.network.play.client.C09PacketHeldItemChange
import net.minecraft.network.play.client.C0APacketAnimation
import net.minecraft.network.play.client.C0BPacketEntityAction
import net.minecraft.network.play.client.C0BPacketEntityAction.Action.START_SNEAKING
import net.minecraft.network.play.client.C0BPacketEntityAction.Action.STOP_SNEAKING
import net.minecraft.util.*
import org.lwjgl.opengl.GL11.*
import java.awt.Color
import javax.vecmath.Color3f
import kotlin.math.*

object BlockFly : Module("BlockFly", "Scaffold skidded from lb b85", 0, ModuleCategory.WORLD) {
    private val mode by ListValue(
        "Mode",
        arrayOf("Normal", "Rewinside", "Expand", "Telly", "GodBridge"),
        "Normal"
    )

    // Expand
    private val omniDirectionalExpand by BooleanValue("OmniDirectionalExpand", false)
    private val expandLength by IntegerValue("ExpandLength", 1, 1..6)

    // Placeable delay
    private val placeDelayValue: BooleanValue = BooleanValue("PlaceDelay", true)

    private val maxDelayValue: IntegerValue = object : IntegerValue("MaxDelay", 0, 0..1000) {
        override fun onChanged(oldValue: Int, newValue: Int) = set(newValue.coerceAtLeast(minDelay))
    }
    private val maxDelay by maxDelayValue

    private val minDelayValue = object : IntegerValue("MinDelay", 0, 0..1000) {
        override fun onChanged(oldValue: Int, newValue: Int) = set(newValue.coerceAtMost(maxDelay))
    }
    private val minDelay by minDelayValue

    // Extra clicks
    private val extraClicks by BooleanValue("DoExtraClicks", false)

    private val extraClickMaxCPSValue: IntegerValue = object : IntegerValue("ExtraClickMaxCPS", 7, 0..50) {
        override fun onChanged(oldValue: Int, newValue: Int) = set(newValue.coerceAtLeast(extraClickMinCPS))
    }
    private val extraClickMaxCPS by extraClickMaxCPSValue

    private val extraClickMinCPS by object : IntegerValue("ExtraClickMinCPS", 3, 0..50) {
        override fun onChanged(oldValue: Int, newValue: Int) = set(newValue.coerceAtMost(extraClickMaxCPS))
    }

    private val placementAttempt by ListValue(
        "PlacementAttempt", arrayOf("Fail", "Independent"), "Fail"
    )

    // Autoblock
    private val autoBlock by ListValue("AutoBlock", arrayOf("Off", "Pick", "Spoof", "Switch"), "Spoof")
    private val sortByHighestAmount by BooleanValue("SortByHighestAmount", false)

    // Basic stuff
    val sprint by BooleanValue("Sprint", false)
    private val swing by BooleanValue("Swing", true)
    private val down by BooleanValue("Down", true)

    private val ticksUntilRotation: IntegerValue = IntegerValue("TicksUntilRotation", 3, 1..5)

    private val jumpAutomatically by BooleanValue("JumpAutomatically", true)
    private val maxBlocksToJump: IntegerValue = object : IntegerValue("MaxBlocksToJump", 4, 1..8) {
        override fun onChanged(oldValue: Int, newValue: Int) = set(newValue.coerceAtMost(extraClickMaxCPS))
    }

    private val minBlocksToJump: IntegerValue = object : IntegerValue("MinBlocksToJump", 4, 1..8) {
        override fun onChanged(oldValue: Int, newValue: Int) = set(newValue.coerceAtMost(extraClickMaxCPS))
    }

    private val startHorizontally by BooleanValue("StartHorizontally", true)

    private val maxHorizontalPlacements: IntegerValue = object : IntegerValue("MaxHorizontalPlacements", 1, 1..10) {
        override fun onChanged(oldValue: Int, newValue: Int) = set(newValue.coerceAtLeast(minHorizontalPlacements.get()))
    }

    private val minHorizontalPlacements: IntegerValue = object : IntegerValue("MinHorizontalPlacements", 1, 1..10) {
        override fun onChanged(oldValue: Int, newValue: Int) = set(newValue.coerceAtMost(maxHorizontalPlacements.get()))
    }

    private val maxVerticalPlacements: IntegerValue = object : IntegerValue("MaxVerticalPlacements", 1, 1..10) {
        override fun onChanged(oldValue: Int, newValue: Int) = set(newValue.coerceAtLeast(minVerticalPlacements.get()))
    }

    private val minVerticalPlacements: IntegerValue = object : IntegerValue("MinVerticalPlacements", 1, 1..10) {
        override fun onChanged(oldValue: Int, newValue: Int) = set(newValue.coerceAtMost(maxVerticalPlacements.get()))
    }

    private val maxJumpTicks: IntegerValue = object : IntegerValue("MaxJumpTicks", 0, 0..10) {
        override fun onChanged(oldValue: Int, newValue: Int) = set(newValue.coerceAtLeast(minJumpTicks.get()))
    }

    private val minJumpTicks: IntegerValue = object : IntegerValue("MinJumpTicks", 0, 0..10) {
        override fun onChanged(oldValue: Int, newValue: Int) = set(newValue.coerceAtMost(maxJumpTicks.get()))
    }

    // Eagle
    private val eagleValue: ListValue = ListValue("Eagle", arrayOf("Normal", "Silent", "Off"), "Normal")

    val eagle by eagleValue

//    private val eagleSpeed by FloatValue("EagleSpeed", 0.3f, 0.3f..1.0f)
//    val eagleSprint by BooleanValue("EagleSprint", false)
    private val blocksToEagle by IntegerValue("BlocksToEagle", 0, 0..10)
    private val edgeDistance by FloatValue("EagleEdgeDistance", 0f, 0f..0.5f)

    // Rotation Options
    private val rotationMode by ListValue("Rotations", arrayOf("Off", "Normal", "Stabilized", "GodBridge"), "Normal")
    private val silentRotation by BooleanValue("SilentRotation", true)
    private val keepRotation by BooleanValue("KeepRotation", true)
    private val keepTicks by object : IntegerValue("KeepTicks", 1, 1..20) {
        override fun onChanged(oldValue: Int, newValue: Int) = set(newValue.coerceAtLeast(minimum))
    }

    // Search options
    private val searchMode by ListValue("SearchMode", arrayOf("Area", "Center"), "Area")
    private val minDist by FloatValue("MinDist", 0f, 0f..0.2f)

    // Turn Speed
    private val maxTurnSpeedValue: FloatValue = object : FloatValue("MaxTurnSpeed", 180f, 1f..180f) {
        override fun onChanged(oldValue: Float, newValue: Float) = set(newValue.coerceAtLeast(minTurnSpeed))
    }
    private val maxTurnSpeed by maxTurnSpeedValue
    private val minTurnSpeed by object : FloatValue("MinTurnSpeed", 180f, 1f..180f) {
        override fun onChanged(oldValue: Float, newValue: Float) = set(newValue.coerceIn(minimum, maxTurnSpeed))
    }

    private val angleThresholdUntilReset by FloatValue("AngleThresholdUntilReset", 5f, 0.1f..180f)

    // Zitter
    private val zitterMode by ListValue("Zitter", arrayOf("Off", "Teleport", "Smooth"), "Off")
    private val zitterSpeed by FloatValue("ZitterSpeed", 0.13f, 0.1f..0.3f)
    private val zitterStrength by FloatValue("ZitterStrength", 0.05f, 0f..0.2f)
    private val maxZitterTicksValue: IntegerValue = object : IntegerValue("MaxZitterTicks", 3, 0..6) {
        override fun onChanged(oldValue: Int, newValue: Int) = set(newValue.coerceAtLeast(minZitterTicks))
    }

    private val maxZitterTicks by maxZitterTicksValue

    private val minZitterTicksValue: IntegerValue = object : IntegerValue("MinZitterTicks", 2, 0..6) {
        override fun onChanged(oldValue: Int, newValue: Int) = set(newValue.coerceAtMost(maxZitterTicks))
    }

    private val minZitterTicks by minZitterTicksValue

    // Game
    private val timer by FloatValue("Timer", 1f, 0.1f..10f)
    private val speedModifier by FloatValue("SpeedModifier", 1f, 0f..2f)
    private val slow by BooleanValue("Slow", false)
    private val slowSpeed by FloatValue("SlowSpeed", 0.6f, 0.2f..0.8f)

    // Safety
    private val sameY by BooleanValue("SameY", false)
    private val safeWalk: BooleanValue = BooleanValue("SafeWalk", true)

    private val airSafe by BooleanValue("AirSafe", false)

    // Visuals
    private val counterDisplay by BooleanValue("Counter", true)
    private val mark by BooleanValue("Mark", false)
    private val trackCPS by BooleanValue("TrackCPS", false)
    private val safetyLines by BooleanValue("SafetyLines", false)

    // Target placement
    private var placeRotation: PlaceRotation? = null

    // Launch position
    private var launchY = 0
    private val shouldKeepLaunchPosition
        get() = sameY && mode != "GodBridge"

    // Zitter
    private var zitterDirection = false

    // Delay
    private val delayTimer = object : DelayTimer(minDelayValue, maxDelayValue, MSTimer()) {
        override fun hasTimePassed() = !placeDelayValue.get() || super.hasTimePassed()
    }
    private val zitterTickTimer = TickDelayTimer(minZitterTicksValue, maxZitterTicksValue)

    // Eagle
    private var placedBlocksWithoutEagle = 0
    var eagleSneaking = false
    private val isEagleEnabled
        get() = eagle != "Off" && !shouldGoDown && mode != "GodBridge"

    // Downwards
    private val shouldGoDown
        get() = down && !sameY && GameSettings.isKeyDown(mc.gameSettings.keyBindSneak) && mode !in arrayOf(
            "GodBridge",
            "Telly"
        ) && blocksAmount > 1

    // Current rotation
    private val currRotation
        get() = targetRotation ?: bestServerRotation()

    // Extra clicks
    private var extraClick = ExtraClickInfo(randomClickDelay(extraClickMinCPS, extraClickMaxCPS).toInt(), 0L, 0)

    // GodBridge
    private var blocksPlacedUntilJump = 0
    private val isManualJumpOptionActive
        get() = mode == "GodBridge" && !jumpAutomatically
    private var blocksToJump = randomDelay(minBlocksToJump.get(), maxBlocksToJump.get())
    private val isGodBridgeEnabled
        get() = mode == "GodBridge" || mode == "Normal" && rotationMode == "GodBridge"

    // Telly
    private var offGroundTicks = 0
    private var ticksUntilJump = 0
    private var blocksUntilAxisChange = 0
    private var jumpTicks = randomDelay(minJumpTicks.get(), maxJumpTicks.get())
    private var horizontalPlacements = randomDelay(minHorizontalPlacements.get(), maxHorizontalPlacements.get())
    private var verticalPlacements = randomDelay(minVerticalPlacements.get(), maxVerticalPlacements.get())
    private val shouldPlaceHorizontally
        get() = mode == "Telly" && isMoving && (startHorizontally && blocksUntilAxisChange <= horizontalPlacements || !startHorizontally && blocksUntilAxisChange > verticalPlacements)

    // Enabling module
    override fun onEnable() {
        val player = mc.thePlayer ?: return

        launchY = player.posY.roundToInt()
        blocksUntilAxisChange = 0
    }

    // Events
    @EventTarget
    private fun onUpdate(event: UpdateEvent) {
        val player = mc.thePlayer ?: return

        mc.timer.timerSpeed = timer

        // Telly
        if (mc.thePlayer.onGround) {
            offGroundTicks = 0
            ticksUntilJump++
        } else {
            offGroundTicks++
        }

        if (shouldGoDown) {
            mc.gameSettings.keyBindSneak.pressed = false
        }

        if (slow) {
            player.motionX *= slowSpeed
            player.motionZ *= slowSpeed
        }

        // Eagle
        if (isEagleEnabled) {
            var dif = 0.5
            val blockPos = BlockPos(player).down()

            for (side in EnumFacing.entries) {
                if (side.axis == EnumFacing.Axis.Y) {
                    continue
                }

                val neighbor = blockPos.offset(side)

                if (isReplaceable(neighbor)) {
                    val calcDif = (if (side.axis == EnumFacing.Axis.Z) {
                        abs(neighbor.z + 0.5 - player.posZ)
                    } else {
                        abs(neighbor.x + 0.5 - player.posX)
                    }) - 0.5

                    if (calcDif < dif) {
                        dif = calcDif
                    }
                }
            }

            if (placedBlocksWithoutEagle >= blocksToEagle) {
                val shouldEagle = isReplaceable(blockPos) || dif < edgeDistance
                if (eagle == "Silent") {
                    if (eagleSneaking != shouldEagle) {
                        sendPacket(C0BPacketEntityAction(player, if (shouldEagle) START_SNEAKING else STOP_SNEAKING))
                    }
                    eagleSneaking = shouldEagle
                } else {
                    mc.gameSettings.keyBindSneak.pressed = shouldEagle
                    eagleSneaking = shouldEagle
                }
                placedBlocksWithoutEagle = 0
            } else {
                placedBlocksWithoutEagle++
            }
        }

        if (player.onGround) {
            // Still a thing?
            if (mode == "Rewinside") {
                strafe(0.2F)
                player.motionY = 0.0
            }

            when (zitterMode.lowercase()) {
                "off" -> {
                    return
                }

                "smooth" -> {
                    if (!GameSettings.isKeyDown(mc.gameSettings.keyBindRight)) {
                        mc.gameSettings.keyBindRight.pressed = false
                    }
                    if (!GameSettings.isKeyDown(mc.gameSettings.keyBindLeft)) {
                        mc.gameSettings.keyBindLeft.pressed = false
                    }

                    if (zitterTickTimer.hasTimePassed()) {
                        zitterDirection = !zitterDirection
                        zitterTickTimer.reset()
                    } else {
                        zitterTickTimer.update()
                    }

                    if (zitterDirection) {
                        mc.gameSettings.keyBindRight.pressed = true
                        mc.gameSettings.keyBindLeft.pressed = false
                    } else {
                        mc.gameSettings.keyBindRight.pressed = false
                        mc.gameSettings.keyBindLeft.pressed = true
                    }
                }

                "teleport" -> {
                    strafe(zitterSpeed)
                    val yaw = (player.rotationYaw + if (zitterDirection) 90.0 else -90.0).toRadians()
                    player.motionX -= sin(yaw) * zitterStrength
                    player.motionZ += cos(yaw) * zitterStrength
                    zitterDirection = !zitterDirection
                }
            }
        }
    }

    @EventTarget
    fun onStrafe(event: StrafeEvent) {
        val player = mc.thePlayer

        // Jumping needs to be done here, so it doesn't get detected by movement-sensitive anti-cheats.
        if (mode == "Telly" && player.onGround && isMoving && currRotation == bestServerRotation() && !mc.gameSettings.keyBindJump.isKeyDown && ticksUntilJump >= jumpTicks) {
            player.jump()

            ticksUntilJump = 0
            jumpTicks = randomDelay(minJumpTicks.get(), maxJumpTicks.get())
        }
    }

    @EventTarget
    fun onMotion(event: MotionEvent) {
        val rotation = targetRotation


        if (rotationMode != "Off" && keepRotation) {
            setRotation(rotation, 1)
        }

        if (event.eventState == EventState.POST) {
            update()
        }
    }

    @EventTarget
    fun onTick(event: TickEvent) {
        val target = placeRotation?.placeInfo
        placeRotation?.apply {
            setRotation(rotation, 1)
        }

        if (extraClicks) {
            while (extraClick.clicks > 0) {
                extraClick.clicks--

                doPlaceAttempt()
            }
        }

        if (target == null) {
            if (placeDelayValue.get()) {
                delayTimer.reset()
            }
            return
        }

        val raycastProperly = !(mode == "Expand" && expandLength > 1 || shouldGoDown) && rotationMode != "Off"

        performBlockRaytrace(currRotation, mc.playerController.blockReachDistance).let {
            if (rotationMode == "Off" || it != null && it.blockPos == target.blockPos && (!raycastProperly || it.sideHit == target.enumFacing)) {
                val result = if (raycastProperly && it != null) {
                    PlaceInfo(it.blockPos, it.sideHit, it.hitVec)
                } else {
                    target
                }

                place(result)
            }
        }
    }
//
//    @EventTarget
//    fun onSneakSlowDown(event: SneakSlowDownEvent) {
//        if (!isEagleEnabled || eagle != "Normal") {
//            return
//        }
//
//        event.forward *= eagleSpeed / 0.3f
//        event.strafe *= eagleSpeed / 0.3f
//    }

    fun update() {
        val player = mc.thePlayer ?: return
        val holdingItem = player.heldItem?.item is ItemBlock

        if (!holdingItem && (autoBlock == "Off" || InventoryUtils.findBlockInHotbar() == null)) {
            return
        }

        findBlock(mode == "Expand" && expandLength > 1, searchMode == "Area")
    }

    private fun setRotation(rotation: Rotation, ticks: Int) {
        val player = mc.thePlayer ?: return

        if (silentRotation) {
            if (mode == "Telly" && isMoving) {
                if (offGroundTicks < ticksUntilRotation.get() && ticksUntilJump >= jumpTicks) {
                    return
                }
            }
            rotation.yaw += angleThresholdUntilReset

            setTargetRotation(
                rotation,
                ticks
            )

        } else {
            rotation.toPlayer(player)
        }
    }

    // Search for new target block
    private fun findBlock(expand: Boolean, area: Boolean) {
        val player = mc.thePlayer ?: return

        val blockPosition = if (shouldGoDown) {
            if (player.posY == player.posY.roundToInt() + 0.5) {
                BlockPos(player.posX, player.posY - 0.6, player.posZ)
            } else {
                BlockPos(player.posX, player.posY - 0.6, player.posZ).down()
            }
        } else if (shouldKeepLaunchPosition && launchY <= player.posY) {
            BlockPos(player.posX, launchY - 1.0, player.posZ)
        } else if (player.posY == player.posY.roundToInt() + 0.5) {
            BlockPos(player)
        } else {
            BlockPos(player).down()
        }

        if (!expand && (!isReplaceable(blockPosition) || search(
                blockPosition,
                !shouldGoDown,
                area,
                shouldPlaceHorizontally
            ))
        ) {
            return
        }

        if (expand) {
            val yaw = player.rotationYaw.toRadians()
            val x = if (omniDirectionalExpand) -sin(yaw).roundToInt() else player.horizontalFacing.directionVec.x
            val z = if (omniDirectionalExpand) cos(yaw).roundToInt() else player.horizontalFacing.directionVec.z

            repeat(expandLength) {
                if (search(blockPosition.add(x * it, 0, z * it), false, area))
                    return
            }
            return
        }

        val (f, g) = if (mode == "Telly") 5 to 3 else 1 to 1

        (-f..f).flatMap { x ->
            (0 downTo -g).flatMap { y ->
                (-f..f).map { z ->
                    Vec3i(x, y, z)
                }
            }
        }.sortedBy {
            getCenterDistance(blockPosition.add(it))
        }.forEach {
            if (canBeClicked(blockPosition.add(it)) || search(
                    blockPosition.add(it),
                    !shouldGoDown,
                    area,
                    shouldPlaceHorizontally
                )
            ) {
                return
            }
        }
    }

    private fun place(placeInfo: PlaceInfo) {
        val player = mc.thePlayer ?: return
        val world = mc.theWorld ?: return

        if (!delayTimer.hasTimePassed() || shouldKeepLaunchPosition && launchY - 1 != placeInfo.vec3.yCoord.toInt())
            return

        var itemStack = player.inventoryContainer.getSlot(serverSlot + 36).stack

        //TODO: blacklist more blocks than only bushes
        if (itemStack == null || itemStack.item !is ItemBlock || (itemStack.item as ItemBlock).block is BlockBush || itemStack.stackSize <= 0 || sortByHighestAmount) {
            val blockSlot = if (sortByHighestAmount) {
                InventoryUtils.findLargestBlockStackInHotbar()
            } else {
                InventoryUtils.findBlockInHotbar()
            }
            if (blockSlot == -1) return

            when (autoBlock.lowercase()) {
                "off" -> return

                "pick" -> {
                    player.inventory.currentItem = blockSlot - 36
                    mc.playerController.updateController()
                }

                "spoof", "switch" -> serverSlot = blockSlot - 36
            }
            itemStack = player.inventoryContainer.getSlot(blockSlot).stack
        }

        if (mc.playerController.onPlayerRightClick(
                player, world, itemStack, placeInfo.blockPos, placeInfo.enumFacing, placeInfo.vec3
            )
        ) {
            delayTimer.reset()

            if (player.onGround) {
                player.motionX *= speedModifier
                player.motionZ *= speedModifier
            }

            if (swing) {
                player.swingItem()
            } else {
                sendPacket(C0APacketAnimation())
            }

            if (isManualJumpOptionActive) {
                blocksPlacedUntilJump++
            }

            updatePlacedBlocksForTelly()
        } else {
            if (mc.playerController.sendUseItem(player, world, itemStack)) {
                mc.entityRenderer.itemRenderer.resetEquippedProgress2()
            }
        }

        if (autoBlock == "Switch")
            serverSlot = player.inventory.currentItem

        // Since we violate vanilla slot switch logic if we send the packets now, we arrange them for the next tick
        switchBlockNextTickIfPossible(itemStack)

        if (trackCPS) {
            CPSCounter.registerClick(CPSCounter.MouseButton.RIGHT)
        }

        placeRotation = null
    }

    @EventTarget(ignoreCondition = true)
    private fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (packet is C09PacketHeldItemChange) _slot = packet.slotId
    }

    private fun doPlaceAttempt() {
        val player = mc.thePlayer ?: return
        val world = mc.theWorld ?: return

        val stack = player.inventoryContainer.getSlot(serverSlot + 36).stack ?: return

        if (stack.item !is ItemBlock || InventoryUtils.BLOCK_BLACKLIST.contains((stack.item as ItemBlock).block)) {
            return
        }

        val block = stack.item as ItemBlock

        val raytrace = performBlockRaytrace(currRotation, mc.playerController.blockReachDistance) ?: return

        val canPlaceOnUpperFace = block.canPlaceBlockOnSide(
            world, raytrace.blockPos, EnumFacing.UP, player, stack
        )

        val shouldPlace = if (placementAttempt == "Fail") {
            !block.canPlaceBlockOnSide(world, raytrace.blockPos, raytrace.sideHit, player, stack)
        } else {
            if (shouldKeepLaunchPosition) {
                raytrace.blockPos.y == launchY - 1 && !canPlaceOnUpperFace
            } else if (shouldPlaceHorizontally) {
                !canPlaceOnUpperFace
            } else {
                raytrace.blockPos.y <= player.posY.toInt() - 1 && !(raytrace.blockPos.y == player.posY.toInt() - 1 && canPlaceOnUpperFace && raytrace.sideHit == EnumFacing.UP)
            }
        }

        if (raytrace.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || !shouldPlace) {
            return
        }

        if (mc.playerController.onPlayerRightClick(
                player, world, stack, raytrace.blockPos, raytrace.sideHit, raytrace.hitVec
            )
        ) {
            if (swing) {
                player.swingItem()
            } else {
                sendPacket(C0APacketAnimation())
            }

            if (isManualJumpOptionActive) {
                blocksPlacedUntilJump++
            }

            updatePlacedBlocksForTelly()
        } else {
            if (mc.playerController.sendUseItem(player, world, stack)) {
                mc.entityRenderer.itemRenderer.resetEquippedProgress2()
            }
        }

        // Since we violate vanilla slot switch logic if we send the packets now, we arrange them for the next tick
        switchBlockNextTickIfPossible(stack)

        if (trackCPS) {
            CPSCounter.registerClick(CPSCounter.MouseButton.RIGHT)
        }
    }

    // Disabling module
    override fun onDisable() {
        val player = mc.thePlayer ?: return

        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindSneak)) {
            mc.gameSettings.keyBindSneak.pressed = false
            if (eagleSneaking) {
                sendPacket(C0BPacketEntityAction(player, STOP_SNEAKING))
            }
        }

        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindRight)) {
            mc.gameSettings.keyBindRight.pressed = false
        }
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindLeft)) {
            mc.gameSettings.keyBindLeft.pressed = false
        }

        placeRotation = null
        mc.timer.timerSpeed = 1f

//        serverSlot = player.inventory.currentItem
    }

    // Entity movement event
    @EventTarget
    fun onMove(event: MoveEvent) {
        val player = mc.thePlayer ?: return

        if (!safeWalk.get() || shouldGoDown) {
            return
        }

        if (airSafe || player.onGround) {
            event.isSafeWalk = true
        }
    }

    // Scaffold visuals
    @EventTarget
    fun onRender2D(event: Render2DEvent) {
        if (counterDisplay) {
            glPushMatrix()
            val blockOverlay = KevinClient.moduleManager[BlockOverlay::class.java]

            if (blockOverlay.state/* && blockOverlay.info*/ && blockOverlay.currentBlock != null) glTranslatef(0f, 15f, 0f)

            val info = "Blocks: §7$blocksAmount"
            val (width, height) = ScaledResolution(mc)

            drawBorderedRect(
                width / 2 - 2,
                height / 2 + 5,
                width / 2 + KevinClient.fontManager.font40.getStringWidth(info) + 2,
                height / 2 + 16,
                3F,
                Color.BLACK.rgb,
                Color.BLACK.rgb
            )

            resetColor()

            KevinClient.fontManager.font40.drawString(
                info, width / 2, height / 2 + 7, Color.WHITE.rgb
            )
            glPopMatrix()
        }
    }

    // Visuals
    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        val player = mc.thePlayer ?: return

        val shouldBother = !(shouldGoDown || mode == "Expand" && expandLength > 1) && extraClicks && isMoving

        if (shouldBother) {
            currRotation.let {
                performBlockRaytrace(it, mc.playerController.blockReachDistance)?.let { raytrace ->
                    val timePassed = System.currentTimeMillis() - extraClick.lastClick >= extraClick.delay

                    if (raytrace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && timePassed) {
                        extraClick = ExtraClickInfo(
                            randomClickDelay(extraClickMinCPS, extraClickMaxCPS).toInt(),
                            System.currentTimeMillis(),
                            extraClick.clicks + 1
                        )
                    }
                }
            }
        }

        displaySafetyLinesIfEnabled()

        if (!mark) {
            return
        }

        repeat(if (mode == "Expand") expandLength + 1 else 2) {
            val yaw = player.rotationYaw.toRadians()
            val x = if (omniDirectionalExpand) -sin(yaw).roundToInt() else player.horizontalFacing.directionVec.x
            val z = if (omniDirectionalExpand) cos(yaw).roundToInt() else player.horizontalFacing.directionVec.z
            val blockPos = BlockPos(
                player.posX + x * it,
                if (shouldKeepLaunchPosition && launchY <= player.posY) launchY - 1.0 else player.posY - (if (player.posY == player.posY + 0.5) 0.0 else 1.0) - if (shouldGoDown) 1.0 else 0.0,
                player.posZ + z * it
            )
            val placeInfo = PlaceInfo.get(blockPos)

            if (isReplaceable(blockPos) && placeInfo != null) {
                drawBlockBox(blockPos, Color(68, 117, 255, 100), false)
                return
            }
        }
    }

    /**
     * Search for placeable block
     *
     * @param blockPosition pos
     * @param raycast visible
     * @param area spot
     * @return
     */

    private fun search(
        blockPosition: BlockPos,
        raycast: Boolean,
        area: Boolean,
        horizontalOnly: Boolean = false
    ): Boolean {
        val player = mc.thePlayer ?: return false

        if (!isReplaceable(blockPosition)) {
            return false
        }

        val maxReach = mc.playerController.blockReachDistance

        val eyes = player.getPositionEyes(1f)
        var placeRotation: PlaceRotation? = null

        var currPlaceRotation: PlaceRotation?

        var considerStableRotation: PlaceRotation? = null

        val isLookingDiagonally = run {
            // Round the rotation to the nearest multiple of 45 degrees so that way we check if the player faces diagonally
            val yaw = round(abs(MathHelper.wrapAngleTo180_float(player.rotationYaw)).roundToInt() / 45f) * 45f

            arrayOf(45f, 135f).any { yaw == it } && player.movementInput.moveStrafe == 0f
        }

        for (side in EnumFacing.entries.filter { !horizontalOnly || it.axis != EnumFacing.Axis.Y }) {
            val neighbor = blockPosition.offset(side)

            if (!canBeClicked(neighbor)) {
                continue
            }

            if (isGodBridgeEnabled) {
                // Selection of these values only. Mostly used by GodBridgers.
                val list = arrayOf(-135f, -45f, 45f, 135f)

                // Selection of pitch values that should be OK in non-complex situations.
                val pitchList = 55.0..75.7 + if (isLookingDiagonally) 1.0 else 0.0

                for (yaw in list) {
                    for (pitch in pitchList step 0.1) {
                        val rotation = Rotation(yaw, pitch.toFloat())

                        val raytrace = performBlockRaytrace(rotation, maxReach) ?: continue

                        currPlaceRotation =
                            PlaceRotation(PlaceInfo(raytrace.blockPos, raytrace.sideHit, raytrace.hitVec), rotation)

                        if (raytrace.blockPos == neighbor && raytrace.sideHit == side.opposite) {
                            val isInStablePitchRange = if (isLookingDiagonally) {
                                pitch >= 75.6
                            } else {
                                pitch in 73.5..75.7
                            }

                            // The module should be looking to aim at (nearly) the upper face of the block. Provides stable bridging most of the time.
                            if (isInStablePitchRange) {
                                considerStableRotation = compareDifferences(currPlaceRotation, considerStableRotation)
                            }

                            placeRotation = compareDifferences(currPlaceRotation, placeRotation)
                        }
                    }
                }

                continue
            }

            if (!area) {
                currPlaceRotation =
                    findTargetPlace(blockPosition, neighbor, Vec3(0.5, 0.5, 0.5), side, eyes, maxReach, raycast)
                        ?: continue

                placeRotation = compareDifferences(currPlaceRotation, placeRotation)
            } else {
                for (x in 0.1..0.9) {
                    for (y in 0.1..0.9) {
                        for (z in 0.1..0.9) {
                            currPlaceRotation =
                                findTargetPlace(blockPosition, neighbor, Vec3(x, y, z), side, eyes, maxReach, raycast)
                                    ?: continue

                            placeRotation = compareDifferences(currPlaceRotation, placeRotation)
                        }
                    }
                }
            }
        }

        placeRotation = considerStableRotation ?: placeRotation

        placeRotation ?: return false

        if (rotationMode != "Off") {
            var targetRotation = placeRotation.rotation

            val info = placeRotation.placeInfo

            if (mode == "GodBridge") {
                val shouldJumpForcefully = isManualJumpOptionActive && blocksPlacedUntilJump >= blocksToJump

                performBlockRaytrace(currRotation, maxReach)?.let {
                    if (it.blockPos == info.blockPos && (it.sideHit != info.enumFacing || shouldJumpForcefully) && isMoving && currRotation.yaw.roundToInt() % 45f == 0f) {
                        if (player.onGround && !isLookingDiagonally) {
                            player.jump()
                        }

                        if (shouldJumpForcefully) {
                            blocksPlacedUntilJump = 0
                            blocksToJump = randomDelay(minBlocksToJump.get(), maxBlocksToJump.get())
                        }

                        targetRotation = currRotation
                    }
                }
            }

            val limitedRotation = limitAngleChange(
                currRotation, targetRotation, nextFloat(minTurnSpeed, maxTurnSpeed)
            )

            setRotation(limitedRotation, if (mode == "Telly") 1 else keepTicks)
        }
        this.placeRotation = placeRotation
        return true
    }

    /**
     * For expand scaffold, fixes vector values that should match according to direction vector
     */
    private fun modifyVec(original: Vec3, direction: EnumFacing, pos: Vec3, shouldModify: Boolean): Vec3 {
        if (!shouldModify) {
            return original
        }

        val x = original.xCoord
        val y = original.yCoord
        val z = original.zCoord

        val side = direction.opposite

        return when (side.axis ?: return original) {
            EnumFacing.Axis.Y -> Vec3(x, pos.yCoord + side.directionVec.y.coerceAtLeast(0), z)
            EnumFacing.Axis.X -> Vec3(pos.xCoord + side.directionVec.x.coerceAtLeast(0), y, z)
            EnumFacing.Axis.Z -> Vec3(x, y, pos.zCoord + side.directionVec.z.coerceAtLeast(0))
        }

    }

    private fun findTargetPlace(
        pos: BlockPos, offsetPos: BlockPos, vec3: Vec3, side: EnumFacing, eyes: Vec3, maxReach: Float, raycast: Boolean
    ): PlaceRotation? {
        val world = mc.theWorld ?: return null

        val vec = (Vec3(pos) + vec3).addVector(
            side.directionVec.x * vec3.xCoord, side.directionVec.y * vec3.yCoord, side.directionVec.z * vec3.zCoord
        )

        val distance = eyes.distanceTo(vec)

        if (raycast && (distance > maxReach || world.rayTraceBlocks(eyes, vec, false, true, false) != null)) {
            return null
        }

        val diff = vec - eyes

        if (side.axis != EnumFacing.Axis.Y) {
            val dist = abs(if (side.axis == EnumFacing.Axis.Z) diff.zCoord else diff.xCoord)

            if (dist < minDist && mode != "Telly") {
                return null
            }
        }

        var rotation = toRotation(vec, false)

        rotation = when (rotationMode) {
            "Stabilized" -> Rotation(round(rotation.yaw / 45f) * 45f, rotation.pitch)
            else -> rotation
        }
        rotation.fixedSensitivity()

        // If the current rotation already looks at the target block and side, then return right here
        performBlockRaytrace(currRotation, maxReach)?.let { raytrace ->
            if (raytrace.blockPos == offsetPos && (!raycast || raytrace.sideHit == side.opposite)) {
                return PlaceRotation(
                    PlaceInfo(
                        raytrace.blockPos, side.opposite, modifyVec(raytrace.hitVec, side, Vec3(offsetPos), !raycast)
                    ), currRotation
                )
            }
        }

        val raytrace = performBlockRaytrace(rotation, maxReach) ?: return null

        if (raytrace.blockPos == offsetPos && (!raycast || raytrace.sideHit == side.opposite)) {
            return PlaceRotation(
                PlaceInfo(
                    raytrace.blockPos, side.opposite, modifyVec(raytrace.hitVec, side, Vec3(offsetPos), !raycast)
                ), rotation
            )
        }

        return null
    }

    private fun performBlockRaytrace(rotation: Rotation, maxReach: Float): MovingObjectPosition? {
        val player = mc.thePlayer ?: return null
        val world = mc.theWorld ?: return null

        val eyes = player.eyesLoc
        val rotationVec = rotation.toDirection()

        val reach = eyes + (rotationVec * maxReach)

        return world.rayTraceBlocks(eyes, reach, false, false, true)
    }

    private fun compareDifferences(
        new: PlaceRotation, old: PlaceRotation?, rotation: Rotation = currRotation
    ): PlaceRotation {
        if (old == null || getRotationDifference(new.rotation, rotation) < getRotationDifference(
                old.rotation, rotation
            )
        ) {
            return new
        }

        return old
    }

    private fun switchBlockNextTickIfPossible(stack: ItemStack) {
        val player = mc.thePlayer ?: return

        if (autoBlock !in arrayOf("Off", "Switch") && stack.stackSize <= 0) {
            InventoryUtils.findBlockInHotbar().let {
                if (it == -1) return
                if (autoBlock == "Pick") {
                    player.inventory.currentItem = it - 36
                    mc.playerController.updateController()
                } else {
                    serverSlot = it - 36
                }
            }
        }
    }

    private fun displaySafetyLinesIfEnabled() {
        if (!safetyLines || !isGodBridgeEnabled) {
            return
        }

        val player = mc.thePlayer ?: return

        // If player is not walking diagonally then continue
        if (round(abs(MathHelper.wrapAngleTo180_float(player.rotationYaw)).roundToInt() / 45f) * 45f !in arrayOf(
                135f,
                45f
            ) || player.movementInput.moveForward == 0f || player.movementInput.moveStrafe != 0f
        ) {
            val (posX, posY, posZ) = player.interpolatedPosition()

            glPushMatrix()
            glTranslated(-posX, -posY, -posZ)
            glLineWidth(5.5f)
            glDisable(GL_TEXTURE_2D)

            val (yawX, yawZ) = player.horizontalFacing.directionVec.x * 1.5 to player.horizontalFacing.directionVec.z * 1.5

            // The target rotation will either be the module's placeRotation or a forced rotation (usually that's where the GodBridge mode aims)
            val targetRotation = run {
                val yaw = arrayOf(-135f, -45f, 45f, 135f).minByOrNull {
                    abs(getAngleDifference(it, MathHelper.wrapAngleTo180_float(currRotation.yaw)))
                } ?: return

                placeRotation?.rotation ?: Rotation(yaw, 73f)
            }

            // Calculate color based on rotation difference
            val color = getColorForRotationDifference(getRotationDifference(targetRotation, currRotation).toFloat())

            val main = BlockPos(player).down()

            val pos = if (canBeClicked(main)) {
                main
            } else {
                (-1..1).flatMap { x ->
                    (-1..1).map { z ->
                        val neighbor = main.add(x, 0, z)

                        neighbor to getCenterDistance(neighbor)
                    }
                }.filter { canBeClicked(it.first) }.minByOrNull { it.second }?.first ?: main
            }.up().vec3

            for (offset in 0..1) {
                for (i in -1..1 step 2) {
                    for (x1 in 0.25..0.5 step 0.01) {
                        val opposite = offset == 1

                        val (offsetX, offsetZ) = if (opposite) 0.0 to x1 * i else x1 * i to 0.0
                        val (lineX, lineZ) = if (opposite) yawX to 0.0 else 0.0 to yawZ

                        val (x, y, z) = pos.add(Vec3(offsetX, -0.99, offsetZ))

                        glBegin(GL_LINES)

                        glColor3f(color.x, color.y, color.z)
                        glVertex3d(x - lineX, y + 0.5, z - lineZ)
                        glVertex3d(x + lineX, y + 0.5, z + lineZ)

                        glEnd()
                    }
                }
            }
            glEnable(GL_TEXTURE_2D)
            glPopMatrix()
        }
    }

    private fun getColorForRotationDifference(rotationDifference: Float): Color3f {
        val maxDifferenceForGreen = 10.0f
        val maxDifferenceForYellow = 40.0f

        val interpolationFactor = when {
            rotationDifference <= maxDifferenceForGreen -> 0.0f
            rotationDifference <= maxDifferenceForYellow -> (rotationDifference - maxDifferenceForGreen) / (maxDifferenceForYellow - maxDifferenceForGreen)
            else -> 1.0f
        }

        val green = 1.0f - interpolationFactor
        val blue = 0.0f

        return Color3f(interpolationFactor, green, blue)
    }

    private fun updatePlacedBlocksForTelly() {
        if (blocksUntilAxisChange > horizontalPlacements + verticalPlacements) {
            blocksUntilAxisChange = 0

            horizontalPlacements = randomDelay(minHorizontalPlacements.get(), maxHorizontalPlacements.get())
            verticalPlacements = randomDelay(minVerticalPlacements.get(), maxVerticalPlacements.get())
            return
        }

        blocksUntilAxisChange++
    }

    /**
     * Returns the amount of blocks
     */
    private val blocksAmount: Int
        get() {
            var amount = 0
            for (i in 36..44) {
                val stack = mc.thePlayer.inventoryContainer.getSlot(i).stack ?: continue
                val item = stack.item
                if (item is ItemBlock) {
                    val block = item.block
                    val heldItem = mc.thePlayer.heldItem
                    if (heldItem != null && heldItem == stack || block !in InventoryUtils.BLOCK_BLACKLIST && block !is BlockBush) {
                        amount += stack.stackSize
                    }
                }
            }
            return amount
        }

    override val tag
        get() = mode

    data class ExtraClickInfo(val delay: Int, val lastClick: Long, var clicks: Int)

    private var serverSlot: Int = 0
        set(value) {
            if (value != _slot) {
                _slot = value
                sendPacket(C09PacketHeldItemChange(value))
            }
        }
    private var _slot = 0
}