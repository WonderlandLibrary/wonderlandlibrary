package me.offeex.bloomware.client.module.visuals

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.util.RenderUtil
import me.offeex.bloomware.api.util.RenderUtil.initialBox
import me.offeex.bloomware.api.util.RenderUtil.use
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventRender

object BoxHighlight : Module("BoxHighlight", "Highlights block you are looking", Category.RENDER) {
    private val mode = setting("Mode").enum("Fill", "Outline")
    private val color = setting("Color").color(255, 255, 255, 70)
    private val lineWidth = setting("Width").number(4.0, 1.0, 5.0, 0.1)

    @Subscribe
    private fun onWorldRender(event: EventRender.World) {
        if (mc.targetedEntity == null) return

        val box = mc.targetedEntity!!.initialBox()
        val pos = mc.targetedEntity!!.getLerpedPos(event.tickDelta)

        event.matrices.use {
            RenderUtil.translateToCamera(event.matrices, pos)
            if (mode.like("Fill")) RenderUtil.drawFilledBox(event.matrices, box, color.color)
            else RenderUtil.drawOutline(event.matrices, box, color.color, lineWidth.value)
        }
    }

    @Subscribe
    private fun onCrosshairRender(event: EventRender.CrosshairTarget) {
        event.canceled = true
        val shape = event.blockState.getOutlineShape(cWorld, event.blockPos)
        if (shape.isEmpty) return

        val boxes = shape.boundingBoxes

        event.matrices.use {
            RenderUtil.translateToCamera(event.matrices, event.blockPos)
//            if (cPlayer.age % 60 == 0) println(event.blockPos)
            boxes.forEach {
                if (mode.like("Fill")) RenderUtil.drawFilledBox(event.matrices, it, color.color)
                else RenderUtil.drawOutline(event.matrices, it, color.color, lineWidth.value)
            }
        }
    }
}