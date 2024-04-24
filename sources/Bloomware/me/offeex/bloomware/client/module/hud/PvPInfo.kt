package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.manager.managers.FontManagerr
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.module.client.Colors
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.Items
import java.util.*

object PvPInfo : HudModule("PvPInfo", "", Category.HUD) {
    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        val obsidian = "Obsidian: " + cPlayer.inventory.count(Items.OBSIDIAN)
        val crystals = "Crystals: " + cPlayer.inventory.count(Items.END_CRYSTAL)
        val exp = "Exp: " + cPlayer.inventory.count(Items.EXPERIENCE_BOTTLE)
        val gapples = "Gapples: " + cPlayer.inventory.count(Items.ENCHANTED_GOLDEN_APPLE)
        val totems = "Totems: " + cPlayer.inventory.count(Items.TOTEM_OF_UNDYING)
        val strings = arrayOf(obsidian, crystals, exp, gapples, totems)
        width = getLongest(*strings)
        height = 60
        for (i in strings.indices) FontManagerr.drawString(
            matrices,
            strings[i],
            x,
            y + 12 * i - 4,
            Colors.hud.color
        )
    }

    private fun getLongest(vararg strings: String): Int {
        val list: MutableList<Int> = ArrayList()
        for (s in strings) list.add(FontManagerr.width(s).toInt())
        return Collections.max(list)
    }
}