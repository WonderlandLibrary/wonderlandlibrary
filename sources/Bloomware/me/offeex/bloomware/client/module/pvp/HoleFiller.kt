package me.offeex.bloomware.client.module.pvp

import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.manager.managers.FriendManager
import me.offeex.bloomware.api.manager.managers.HoleManager
import me.offeex.bloomware.client.setting.setting
import net.minecraft.util.math.BlockPos

object HoleFiller : Obsidian("HoleFiller", "Automatically fills safe holes", Category.PVP) {
    private val range = setting("Range").number(4, 3, 5, 0.1)
    override val rotation = settingRotations()
    private val autoDisable = setting("AutoDisable").bool(true)

    private val smartMode = setting("SmartMode").group(true)
    private val enemyRange = smartMode.setting("EnemyRange").number(4.5, 2, 4.5, 0.1)

    override fun onTickExtend() {
        if (complete) return disable()
        fillPositions()
    }

    override fun isComplete() = positionsToFill().isEmpty() && autoDisable.toggled

    override fun positionsToFill(): List<BlockPos> {
        val playerHoles = HoleManager.cachedHoles.keys
            .filter { it.isWithinDistance(cPlayer.pos, range.value) }
        var result = playerHoles

        if (smartMode.toggled) {
            val enemies = cWorld.players
                .apply { remove(cPlayer) }
                .filter { FriendManager.getType(it) != FriendManager.PersonType.FRIEND }
            val enemyHoles = playerHoles
                .filter { enemies.any { e -> it.isWithinDistance(e.pos, enemyRange.value) } }
            result = enemyHoles
        }

        return result
        TODO("vector calculation")
    }
}