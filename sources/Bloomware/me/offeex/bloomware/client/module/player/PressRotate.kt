package me.offeex.bloomware.client.module.player

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventInput
import net.minecraft.client.gui.screen.ChatScreen
import org.lwjgl.glfw.GLFW

object PressRotate : Module("PressRotate", "Enables you to rotate by pressing arrows.", Category.PLAYER) {
    private val yawStep = setting("YawStep").number(45, 1, 90, 1)
    private val pitchStep = setting("PitchStep").number(45, 1, 45, 1)
    private val inGame = setting("AllowInGame").bool(true)
    private val inGui = setting("AllowGUI").bool(true)
    private val inRotationLock = setting("AllowRotationLock").bool(true)

    @Subscribe
    private fun onKey(event: EventInput.Key) {
        val isArrow = event.key == GLFW.GLFW_KEY_UP || event.key == GLFW.GLFW_KEY_DOWN || event.key == GLFW.GLFW_KEY_RIGHT || event.key == GLFW.GLFW_KEY_LEFT
        val gCheck = inGame.toggled && mc.currentScreen == null
        val rlCheck = inRotationLock.toggled && RotationLock.enabled
        val guiCheck = inGui.toggled && mc.currentScreen != null && mc.currentScreen !is ChatScreen
        if (!gCheck && !rlCheck && !guiCheck || event is EventInput.Key.Release || !isArrow) return

        var nextPitch: Double? = null
        when (event.key) {
            GLFW.GLFW_KEY_UP -> nextPitch = cPlayer.pitch - pitchStep.value
            GLFW.GLFW_KEY_DOWN -> nextPitch = cPlayer.pitch + pitchStep.value
            GLFW.GLFW_KEY_RIGHT -> cPlayer.yaw += yawStep.value.toFloat()
            GLFW.GLFW_KEY_LEFT -> cPlayer.yaw -= yawStep.value.toFloat()
        }
        if (nextPitch != null && nextPitch in -90.0..90.0) cPlayer.pitch = nextPitch.toFloat()
    }
}