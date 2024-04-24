package me.offeex.bloomware.client.module.visuals

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.manager.managers.FriendManager.PersonType
import me.offeex.bloomware.api.manager.managers.FriendManager.getType
import me.offeex.bloomware.api.manager.managers.HoleManager
import me.offeex.bloomware.api.structure.ColorMutable
import me.offeex.bloomware.api.structure.Hole.BEDROCK
import me.offeex.bloomware.api.structure.Hole.SAFE
import me.offeex.bloomware.api.util.RenderUtil
import me.offeex.bloomware.api.util.RenderUtil.initialBox
import me.offeex.bloomware.api.util.RenderUtil.use
import me.offeex.bloomware.api.util.WorldUtil
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.client.setting.settings.SettingColor
import me.offeex.bloomware.client.setting.settings.SettingEnum
import me.offeex.bloomware.client.setting.settings.SettingNumber
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventRender
import net.minecraft.block.entity.*
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.decoration.ItemFrameEntity
import net.minecraft.entity.mob.AmbientEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.mob.WaterCreatureEntity
import net.minecraft.entity.passive.IronGolemEntity
import net.minecraft.entity.passive.PassiveEntity
import net.minecraft.entity.passive.SnowGolemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.vehicle.AbstractMinecartEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import kotlin.math.pow

object ESP : Module("ESP", "Highlights various things through blocks", Category.RENDER) {
    /* ESP */
    private val entities = setting("Entities").group(false)

    private val animals = entities.setting("Animals").group(false)
    private val animalsMode = animals.setting("Mode").enum("Fill", "Outline")
    private val animalsC = animals.setting("Color").color(ColorMutable.GREEN)

    private val hostiles = animals.clone("Hostiles")
    private val hostilesMode = animalsMode.clone(parent = hostiles)
    private val hostilesC = animalsC.clone(parent = hostiles).color(ColorMutable.PURPLE)

    private val players = animals.clone("Players")
    private val playersMode = animalsMode.clone(parent = players)
    private val playersC = animalsC.clone(parent = players).color(ColorMutable.BLUE)

    private val friends = animals.clone("Friends")
    private val friendsMode = animalsMode.clone(parent = friends)
    private val friendsC = animalsC.clone(parent = friends).color(ColorMutable.AQUA)

    private val enemies = animals.clone("Enemies")
    private val enemiesMode = animalsMode.clone(parent = enemies)
    private val enemiesC = animalsC.clone(parent = enemies).color(ColorMutable.RED)

    private val items = animals.clone("Items")
    private val itemsMode = animalsMode.clone(parent = items)
    private val itemsC = animalsC.clone(parent = items).color(ColorMutable.WHITE)

    private val itemFrames = animals.clone("ItemFrames")
    private val itemFramesMode = animalsMode.clone(parent = itemFrames)
    private val itemFramesC = animalsC.clone(parent = itemFrames).color(ColorMutable.BROWN)

    private val minecarts = animals.clone("Minecarts")
    private val minecartsMode = animalsMode.clone(parent = minecarts)
    private val minecartsC = animalsC.clone(parent = minecarts).color(ColorMutable.GRAY)

    private val entityRange = entities.setting("Range").number(256, 32, 512, 1)

    /* StorageESP */
    private val storages = entities.clone("Storages")

    private val chests = animals.clone("Chests", parent = storages)
    private val chestsMode = animalsMode.clone(parent = chests)
    private val chestsC = animalsC.clone(parent = chests).color(255, 120, 80)

    private val echests = chests.clone("EnderChest")
    private val echestsMode = chestsMode.clone(parent = echests)
    private val echestsC = chestsC.clone(parent = echests).color(255, 100, 255)

    private val shulkers = chests.clone("Shulker")
    private val shulkersMode = chestsMode.clone(parent = shulkers)
    private val shulkersC = chestsC.clone(parent = shulkers).color(190, 120, 255)

    private val furnaces = chests.clone("Furnace")
    private val furnacesMode = chestsMode.clone(parent = furnaces)
    private val furnacesC = chestsC.clone(parent = furnaces).color(119, 119, 119)

    private val trappedChests = chests.clone("TrappedChest")
    private val trappedChestsMode = chestsMode.clone(parent = trappedChests)
    private val trappedChestsC = chestsC.clone(parent = trappedChests).color(255, 120, 80)

    private val dispencers = chests.clone("Dispencer")
    private val dispencersMode = chestsMode.clone(parent = dispencers)
    private val dispencersC = chestsC.clone(parent = dispencers).color(150, 150, 150)

    private val droppers = chests.clone("Dropper")
    private val droppersMode = chestsMode.clone(parent = droppers)
    private val droppersC = chestsC.clone(parent = droppers).color(180, 180, 180)

    private val hoppers = chests.clone("Hopper")
    private val hopperMode = chestsMode.clone(parent = hoppers)
    private val hoppersC = chestsC.clone(parent = hoppers).color(234, 43, 0)

    private val storageRange = entityRange.clone(parent = storages)

    /* HoleESP */
    val holes = entities.clone("Holes")

    private val obsidian = holes.setting("Obsidian").group(true)
    private val obsidianMode = animalsMode.clone(parent = obsidian)
    private val obsidianHeight = obsidian.setting("Height").number(0.0, 0.0, 1.0, 0.01)
    private val obsidianColor = animalsC.clone(parent = obsidian).color(0, 189, 252)

    private val bedrock = obsidian.clone("Bedrock")
    private val bedrockMode = obsidianMode.clone(parent = bedrock)
    private val bedrockHeight = obsidianHeight.clone(parent = bedrock)
    private val bedrockColor = obsidianColor.clone(parent = bedrock).color(ColorMutable.AQUA)

    private val holeRenderDist = holes.setting("RenderDistance").number(12, 4, 32, 1)


    private val lineWidth = setting("Width").number(4.0, 1.0, 5.0, 0.1)


    private val entityPairs = listOf(
        PlayerEntity::class.java to players,
        Monster::class.java to hostiles,
        PassiveEntity::class.java to animals,
        ItemEntity::class.java to items,
        ItemFrameEntity::class.java to itemFrames,
        AbstractMinecartEntity::class.java to minecarts
    )
    private val storageTriples = listOf(
        Triple(ChestBlockEntity::class.java, chests, Scale.CHEST),
        Triple(EnderChestBlockEntity::class.java, echests, Scale.CHEST),
        Triple(TrappedChestBlockEntity::class.java, trappedChests, Scale.CHEST),
        Triple(ShulkerBoxBlockEntity::class.java, shulkers, Scale.FULL),
        Triple(AbstractFurnaceBlockEntity::class.java, furnaces, Scale.FULL),
        Triple(DispenserBlockEntity::class.java, dispencers, Scale.FULL),
        Triple(DropperBlockEntity::class.java, droppers, Scale.FULL),
        Triple(HopperBlockEntity::class.java, hoppers, Scale.FULL)
    )
    private val holePairs = listOf(BEDROCK to bedrock, SAFE to obsidian)

    private val entitiesVertexConsumers = hashMapOf(
        BufferBuilder(256) to Pair(animalsC, animalsMode),
        BufferBuilder(256) to Pair(hostilesC, hostilesMode),
        BufferBuilder(256) to Pair(playersC, playersMode),
        BufferBuilder(256) to Pair(friendsC, friendsMode),
        BufferBuilder(256) to Pair(enemiesC, enemiesMode),
        BufferBuilder(256) to Pair(itemsC, itemsMode),
        BufferBuilder(256) to Pair(itemFramesC, itemFramesMode),
        BufferBuilder(256) to Pair(minecartsC, minecartsMode)
    )

    override fun onEnable() {
        mc.worldRenderer.reload()
    }

    @Subscribe
    private fun onWorldRender(event: EventRender.World) {

        /* Entities part */
        if (entities.toggled) cWorld.entities.forEach {
            if (cPlayer.distanceTo(it) > entityRange.value || it === cPlayer) return@forEach

            val box = it.initialBox()
            val pos = it.getLerpedPos(event.tickDelta)

            entityPairs.find { ep -> ep.first.isAssignableFrom(it::class.java) && ep.second.toggled }?.let { (c, s) ->

                var modeSetting = s["Mode"] as SettingEnum
                var colorSetting = s["Color"] as SettingColor

                if (c == PlayerEntity::class.java) when (getType(it as PlayerEntity)) {
                    PersonType.FRIEND -> {
                        colorSetting = friendsC
                        modeSetting = friendsMode
                    }

                    PersonType.ENEMY -> {
                        colorSetting = enemiesC
                        modeSetting = enemiesMode
                    }

                    else -> playersC
                }

                if (s.toggled) draw(box, event.matrices, pos, modeSetting, colorSetting)
            }

            if (it.isAnimal() && animals.toggled) draw(box, event.matrices, pos, animalsMode, animalsC)
        }

        /* Storages part */
        if (storages.toggled) WorldUtil.blockEntities.forEach { be ->
            if (cPlayer.squaredDistanceTo(Vec3d.of(be.pos)) > entityRange.value.pow(2)) return@forEach
            storageTriples.find { it.first == be::class.java && it.second.toggled }?.let {
                draw(
                    box(it.third),
                    event.matrices,
                    Vec3d.of(be.pos),
                    it.second["Mode"] as SettingEnum,
                    it.second["Color"] as SettingColor
                )
            }
        }

        /* Holes part */
        if (holes.toggled) HoleManager.cachedHoles.forEach { (key, value) ->
            val vec = Vec3d.of(key)
            if (cPlayer.squaredDistanceTo(vec) > holeRenderDist.value.pow(2)) return@forEach
            holePairs.find { value == it.first && it.second.toggled }?.let {
                val height = (it.second["Height"] as SettingNumber).value
                val box = Box(BlockPos.ORIGIN).withMaxY(height)
                draw(box, event.matrices, vec, it.second["Mode"] as SettingEnum, it.second["Color"] as SettingColor)
            }
        }
    }

    private fun draw(box: Box, matrices: MatrixStack, pos: Vec3d, mode: SettingEnum, color: SettingColor) {
        matrices.use {
            RenderUtil.translateToCamera(matrices, pos)
            if (mode.like("Fill")) RenderUtil.drawFilledBox(matrices, box, color.color)
            else RenderUtil.drawOutline(matrices, box, color.color, lineWidth.value)
        }
    }

    private fun Entity.isAnimal() = when (this) {
        is PassiveEntity, is WaterCreatureEntity, is AmbientEntity, is SnowGolemEntity, is IronGolemEntity -> true
        else -> false
    }

    private fun box(type: Scale): Box {
        val bp = BlockPos.ORIGIN
        return Box(
            bp.x + if (type == Scale.CHEST) 0.06 else 0.0,
            bp.y.toDouble(),
            bp.z + if (type == Scale.CHEST) 0.06 else 0.0,
            bp.x + if (type == Scale.CHEST) 0.94 else 1.0,
            bp.y + if (type == Scale.CHEST) 0.875 else 1.0,
            bp.z + if (type == Scale.CHEST) 0.94 else 1.0
        )
    }

    private enum class Scale { FULL, CHEST }
}