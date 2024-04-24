package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.api.manager.managers.FontManagerr
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.module.client.Colors
import me.offeex.bloomware.client.setting.setting
import net.minecraft.client.util.math.MatrixStack
import java.text.SimpleDateFormat
import java.util.*

object Time : HudModule("Time", "Shows current time", Category.HUD) {
    private val showSeconds = setting("ShowSeconds").bool(true)
    private val patternS = SimpleDateFormat("HH:mm:ss")
    private val patternH = SimpleDateFormat("HH:mm")
    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        FontManagerr.drawString(
            matrices,
            "Time: " + if (showSeconds.toggled) patternS.format(Calendar.getInstance().time)
            else patternH.format(
                Calendar.getInstance().time
            ),
            x,
            y,
            Colors.hud,
            this
        )
    }
}