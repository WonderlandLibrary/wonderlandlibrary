package me.offeex.bloomware.client.module.visuals

import com.mojang.blaze3d.systems.RenderSystem
import me.offeex.bloomware.api.manager.managers.GraphicsManager
import me.offeex.bloomware.api.structure.ColorMutable
import me.offeex.bloomware.api.util.RenderUtil
import me.offeex.bloomware.api.util.RenderUtil.use
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.client.setting.settings.SettingColor
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventRender
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d

object ESPTest : Module("ESP", "Highlights various things through blocks", Category.RENDER) {
    private val color = setting("Color").color(ColorMutable.GREEN)

    override fun onEnable() {
//        mc.worldRenderer.reload()
        GraphicsManager.pipelineESP.reloadProgram()
    }

    @Subscribe
    private fun onWorldRender(event: EventRender.World) {
        draw(box(Scale.FULL), event.matrices, Vec3d(
            20.0,
            -60.0,
            0.0
        ), color)
    }

    private fun draw(box: Box, matrices: MatrixStack, pos: Vec3d, color: SettingColor) {
//        matrices.use {
//            RenderUtil.translateToCamera(matrices, pos)
//            RenderUtil.drawFilledBox(matrices, box, color.color)
//        }
        matrices.use {
//            val pos = Vec3d(pos.x, pos.y + 1, pos.z)
            RenderUtil.translateToCamera(matrices, pos)
            GraphicsManager.pipelineESP.use {
                matrices(RenderSystem.getModelViewMatrix(), RenderSystem.getProjectionMatrix())

                val r = color.color.red / 255f
                val g = color.color.green / 255f
                val b = color.color.blue / 255f
                val a = color.color.alpha / 255f

                val bNW = vertex().pos(matrices.peek().positionMatrix, box.minX, box.minY, box.minZ).color(r, g, b, a).index()
                val bNE = vertex().pos(matrices.peek().positionMatrix, box.maxX, box.minY, box.minZ).color(r, g, b, a).index()
                val bSE = vertex().pos(matrices.peek().positionMatrix, box.maxX, box.minY, box.maxZ).color(r, g, b, a).index()
                val bSW = vertex().pos(matrices.peek().positionMatrix, box.minX, box.minY, box.maxZ).color(r, g, b, a).index()

                val tNW = vertex().pos(matrices.peek().positionMatrix, box.minX, box.maxY, box.minZ).color(r, g, b, a).index()
                val tSW = vertex().pos(matrices.peek().positionMatrix, box.minX, box.maxY, box.maxZ).color(r, g, b, a).index()
                val tSE = vertex().pos(matrices.peek().positionMatrix, box.maxX, box.maxY, box.maxZ).color(r, g, b, a).index()
                val tNE = vertex().pos(matrices.peek().positionMatrix, box.maxX, box.maxY, box.minZ).color(r, g, b, a).index()

                index(bNW)
                index(tNW)
                index(tNE)
                index(bNE)

                index(bNE)
                index(tNE)
                index(tSE)
                index(bSE)

                index(bSE)
                index(tSE)
                index(tSW)
                index(bSW)

                index(bSW)
                index(tSW)
                index(tNW)
                index(bNW)

                draw()
            }
        }
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