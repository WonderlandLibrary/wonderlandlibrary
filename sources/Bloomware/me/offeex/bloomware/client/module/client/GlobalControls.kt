package me.offeex.bloomware.client.module.client

import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting

object GlobalControls : Module("GlobalControls", "Exposes modules global controls", Category.CLIENT) {
    val holeRange = setting("HolesInRange").number(16, 4, 128, 1)
}