package me.offeex.bloomware.api.util

import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.api.helper.cNetHandler
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.GameMode

object PlayerUtil {
    fun PlayerEntity.getPing(): Int {
        if (Bloomware.mc.networkHandler == null) return 0
        val playerListEntry = cNetHandler.getPlayerListEntry(uuid) ?: return 0
        return playerListEntry.latency
    }

    fun PlayerEntity.getGamemode(): GameMode? {
        val playerListEntry = cNetHandler.getPlayerListEntry(uuid ?: return null)
        return playerListEntry?.gameMode
    }
}