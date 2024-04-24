package me.offeex.bloomware.client.module.tweaks

import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.manager.managers.RotationManager
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventRender
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Hand
import org.joml.Quaternionf

object ViewModel : Module("ViewModel", "Changes your hand renderer", Category.TWEAKS) {

	/* *************** PlayerModel *************** */

	private val playerModel = setting("PlayerModel").group()

	private val rotations = playerModel.setting("Rotations").group(true)
	private val packetYaw = rotations.setting("PacketYaw").bool(true)
	private val packetPitch = rotations.setting("PacketPitch").bool(true)

	/* *************** Hands *************** */

	private val mainHand = setting("MainHand").group(true)
	private val offHand = setting("OffHand").group(true)

	private val scaleMain = mainHand.setting("Scale").group(true)
	private val positionMain = mainHand.setting("Position").group(true)
	private val rotationMain = mainHand.setting("Rotation").group(true)
	private val animateMain = mainHand.setting("Animate").group(false)

	private val scaleOff = offHand.setting("Scale").group(true)
	private val positionOff = offHand.setting("Position").group(true)
	private val rotationOff = offHand.setting("Rotation").group(true)
	private val animateOff = offHand.setting("Animate").group(false)

	private val scaleMainX = scaleMain.setting("ScaleX").number(1.00, 0.10, 5.00, 0.01)
	private val scaleMainY = scaleMain.setting("ScaleY").number(1.00, 0.10, 5.00, 0.01)
	private val scaleMainZ = scaleMain.setting("ScaleZ").number(1.00, 0.10, 5.00, 0.01)
	private val scaleOffX = scaleOff.setting("ScaleZ").number(1.00, 0.10, 5.00, 0.01)
	private val scaleOffY = scaleOff.setting("ScaleY").number(1.00, 0.10, 5.00, 0.01)
	private val scaleOffZ = scaleOff.setting("ScaleZ").number(1.00, 0.10, 5.00, 0.01)

	private val positionMainX = positionMain.setting("PositionX").number(0.00, -3.00, 3.00, 0.01)
	private val positionMainY = positionMain.setting("PositionY").number(0.00, -3.00, 3.00, 0.01)
	private val positionMainZ = positionMain.setting("PositionZ").number(0.00, -3.00, 3.00, 0.01)
	private val positionOffX = positionOff.setting("PositionX").number(0.00, -3.00, 3.00, 0.01)
	private val positionOffY = positionOff.setting("PositionY").number(0.00, -3.00, 3.00, 0.01)
	private val positionOffZ = positionOff.setting("PositionZ").number(0.00, -3.00, 3.00, 0.01)

	private val rotationMainX = rotationMain.setting("RotationX").number(0.0, -180.0, 180.0, 1.0)
	private val rotationMainY = rotationMain.setting("RotationY").number(0.0, -180.0, 180.0, 1.0)
	private val rotationMainZ = rotationMain.setting("RotationZ").number(0.0, -180.0, 180.0, 1.0)
	private val rotationOffX = rotationOff.setting("RotationX").number(0.0, -180.0, 180.0, 1.0)
	private val rotationOffY = rotationOff.setting("RotationY").number(0.0, -180.0, 180.0, 1.0)
	private val rotationOffZ = rotationOff.setting("RotationZ").number(0.0, -180.0, 180.0, 1.0)

	private val animateMainX = animateMain.setting("AnimateX").bool(true)
	private val animateMainY = animateMain.setting("AnimateY").bool()
	private val animateMainZ = animateMain.setting("AnimateZ").bool()
	private val speedAnimateMain = animateMain.setting("Speed").number(2.0, 1.0, 5.0, 1.0)

	private val animateOffX = animateOff.setting("AnimateX").bool(true)
	private val animateOffY = animateOff.setting("AnimateY").bool()
	private val animateOffZ = animateOff.setting("AnimateZ").bool()
	private val speedAnimateOff = animateOff.setting("Speed").number(2.0, 1.0, 5.0, 1.0)

	@Subscribe
	private fun onHeldItemRender(event: EventRender.HeldItem) {
		val matrices = event.matrices

		if (event.hand == Hand.MAIN_HAND) {
			if (animateMain.toggled) {
				if (animateMainX.toggled) rotationMainX.value =
					animateRotation(rotationMainX.value, speedAnimateMain.value)
				if (animateMainY.toggled) rotationMainY.value =
					animateRotation(rotationMainY.value, speedAnimateMain.value)
				if (animateMainZ.toggled) rotationMainZ.value =
					animateRotation(rotationMainZ.value, speedAnimateMain.value)
			}
			if (scaleMain.toggled) matrices.scale(scaleMainX.value, scaleMainY.value, scaleMainZ.value)
			if (positionMain.toggled) matrices.translate(positionMainX.value, positionMainY.value, positionMainZ.value)
			if (rotationMain.toggled) matrices.multiply(Quaternionf(rotationMainX.value, rotationMainY.value, rotationMainZ.value, 1.0))
		} else {
			if (animateMain.toggled) {
				if (animateOffX.toggled) rotationOffX.value =
					animateRotation(rotationOffX.value, speedAnimateOff.value)
				if (animateOffY.toggled) rotationOffY.value =
					animateRotation(rotationOffY.value, speedAnimateOff.value)
				if (animateOffZ.toggled) rotationOffZ.value =
					animateRotation(rotationOffZ.value, speedAnimateOff.value)
			}
			if (scaleOff.toggled) matrices.scale(scaleOffX.value, scaleOffY.value, scaleOffZ.value)
			if (positionOff.toggled) matrices.translate(positionOffX.value, positionOffY.value, positionOffZ.value)
			if (rotationOff.toggled) matrices.multiply(Quaternionf(rotationOffX.value, -rotationOffY.value, -rotationOffZ.value, 1.0))
		}
	}

	@Subscribe
	private fun onLivingEntityRender(event: EventRender.LivingEntity) {
		if (!RotationManager.rotate || !rotations.toggled || event.entity != cPlayer) return
		when (event) {
			is EventRender.LivingEntity.BodyYaw -> if (packetYaw.toggled) event.yaw = RotationManager.packetYaw
			is EventRender.LivingEntity.HeadYaw -> if (packetYaw.toggled) event.yaw = RotationManager.packetYaw
			is EventRender.LivingEntity.HeadPitch -> if (packetPitch.toggled) event.pitch = RotationManager.packetPitch
		}
	}

	private fun animateRotation(value: Double, speed: Double) =
		if (value - speed <= 180 && value - speed > -180) value - speed else 180.0

	private fun MatrixStack.scale(x: Double, y: Double, z: Double) = this.scale(x.toFloat(), y.toFloat(), z.toFloat())
}