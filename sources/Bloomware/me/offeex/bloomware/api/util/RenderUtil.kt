package me.offeex.bloomware.api.util

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.gui.screen.ClickGUI
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.structure.ColorMutable
import me.offeex.bloomware.api.util.EntityUtil.getFullHealth
import me.offeex.bloomware.client.module.client.Gui
import me.offeex.bloomware.mixin.accessor.IInGameHud
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.Framebuffer
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.render.*
import net.minecraft.client.render.VertexFormat.DrawMode
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.item.FilledMapItem
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.*
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

object RenderUtil {
    private val MAP_BACKGROUND = Identifier("textures/map/map_background_checkerboard.png")

    fun drawFilledBox(matrices: MatrixStack, box: Box, c: ColorMutable) {
        val minX = box.minX.toFloat()
        val minY = box.minY.toFloat()
        val minZ = box.minZ.toFloat()
        val maxX = box.maxX.toFloat()
        val maxY = box.maxY.toFloat()
        val maxZ = box.maxZ.toFloat()

        val tessellator = Tessellator.getInstance()
        val bufferBuilder = tessellator.buffer

        RenderSystem.setShader { GameRenderer.getPositionColorProgram() }
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)

        RenderSystem.disableDepthTest()
        setup()
        bufferBuilder.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR)

        bufferBuilder.vertex(matrices.peek().positionMatrix, minX, minY, minZ).color(c.argb).next()
        bufferBuilder.vertex(matrices.peek().positionMatrix, maxX, minY, minZ).color(c.argb).next()
        bufferBuilder.vertex(matrices.peek().positionMatrix, maxX, minY, maxZ).color(c.argb).next()
        bufferBuilder.vertex(matrices.peek().positionMatrix, minX, minY, maxZ).color(c.argb).next()

        bufferBuilder.vertex(matrices.peek().positionMatrix, minX, maxY, minZ).color(c.argb).next()
        bufferBuilder.vertex(matrices.peek().positionMatrix, minX, maxY, maxZ).color(c.argb).next()
        bufferBuilder.vertex(matrices.peek().positionMatrix, maxX, maxY, maxZ).color(c.argb).next()
        bufferBuilder.vertex(matrices.peek().positionMatrix, maxX, maxY, minZ).color(c.argb).next()

        bufferBuilder.vertex(matrices.peek().positionMatrix, minX, minY, minZ).color(c.argb).next()
        bufferBuilder.vertex(matrices.peek().positionMatrix, minX, maxY, minZ).color(c.argb).next()
        bufferBuilder.vertex(matrices.peek().positionMatrix, maxX, maxY, minZ).color(c.argb).next()
        bufferBuilder.vertex(matrices.peek().positionMatrix, maxX, minY, minZ).color(c.argb).next()

        bufferBuilder.vertex(matrices.peek().positionMatrix, maxX, minY, minZ).color(c.argb).next()
        bufferBuilder.vertex(matrices.peek().positionMatrix, maxX, maxY, minZ).color(c.argb).next()
        bufferBuilder.vertex(matrices.peek().positionMatrix, maxX, maxY, maxZ).color(c.argb).next()
        bufferBuilder.vertex(matrices.peek().positionMatrix, maxX, minY, maxZ).color(c.argb).next()

        bufferBuilder.vertex(matrices.peek().positionMatrix, minX, minY, maxZ).color(c.argb).next()
        bufferBuilder.vertex(matrices.peek().positionMatrix, maxX, minY, maxZ).color(c.argb).next()
        bufferBuilder.vertex(matrices.peek().positionMatrix, maxX, maxY, maxZ).color(c.argb).next()
        bufferBuilder.vertex(matrices.peek().positionMatrix, minX, maxY, maxZ).color(c.argb).next()

        bufferBuilder.vertex(matrices.peek().positionMatrix, minX, minY, minZ).color(c.argb).next()
        bufferBuilder.vertex(matrices.peek().positionMatrix, minX, minY, maxZ).color(c.argb).next()
        bufferBuilder.vertex(matrices.peek().positionMatrix, minX, maxY, maxZ).color(c.argb).next()
        bufferBuilder.vertex(matrices.peek().positionMatrix, minX, maxY, minZ).color(c.argb).next()

        tessellator.draw()
        clean()
        RenderSystem.enableDepthTest()
    }

    fun drawOutline(matrices: MatrixStack, box: Box, c: ColorMutable, lineWidth: Double) {
        val tessellator = Tessellator.getInstance()
        val bufferBuilder = tessellator.buffer
        val distance = cPlayer.squaredDistanceTo(box.center)
        val factor3d = 1.0 / (1.0 + distance) + lineWidth

        RenderSystem.lineWidth((if (Gui.widthMode.like("2D")) lineWidth else factor3d).toFloat())
        RenderSystem.setShader { GameRenderer.getRenderTypeLinesProgram() }
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        if (Gui.blendMode.like("Add")) RenderSystem.blendFunc(770, 1)
        else RenderSystem.defaultBlendFunc()

        RenderSystem.disableDepthTest()
        RenderSystem.disableCull()
        setup()
        bufferBuilder.begin(DrawMode.LINES, VertexFormats.LINES)

        WorldRenderer.drawBox(
            matrices,
            bufferBuilder,
            box,
            c.red / 255f,
            c.green / 255f,
            c.blue / 255f,
            c.alpha / 255f
        )

        tessellator.draw()
        clean()
        RenderSystem.enableDepthTest()
        RenderSystem.enableCull()
    }

    fun drawLine(
        matrices: MatrixStack,
        startPos: Vec3d,
        endPos: Vec3d,
        color: ColorMutable,
        lineWidth: Double,
        mode: DrawMode,
    ) {
        val tessellator = Tessellator.getInstance()
        val bufferBuilder = tessellator.buffer
        val distance = endPos.distanceTo(startPos)
        RenderSystem.lineWidth((
            if (Gui.widthMode.like("2D")) lineWidth
            else lineWidth / distance).toFloat()
        )
        RenderSystem.setShader { GameRenderer.getRenderTypeLinesProgram() }
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        if (Gui.blendMode.like("Add")) RenderSystem.blendFunc(770, 1)
        else RenderSystem.defaultBlendFunc()

        RenderSystem.disableDepthTest()
        RenderSystem.disableCull()
        bufferBuilder.begin(mode, VertexFormats.LINES)

        drawTracer3D(matrices, bufferBuilder, startPos, endPos.subtract(camera.pos), color)

        tessellator.draw()
        RenderSystem.enableCull()
        RenderSystem.enableDepthTest()
    }

    private fun drawTracer3D(
        matrices: MatrixStack,
        vertexConsumer: VertexConsumer,
        startPos: Vec3d,
        endPos: Vec3d,
        color: ColorMutable,
    ) {
        val position = matrices.peek().positionMatrix
        val normal = matrices.peek().normalMatrix
        val normals = endPos.normalize().toVector3f()
        vertexConsumer.vertex(
            position,
            startPos.getX().toFloat(),
            startPos.getY().toFloat(),
            startPos.getZ().toFloat()
        ).color(color.argb).normal(normal, normals.x, normals.y, normals.z).next()
        vertexConsumer.vertex(
            position,
            endPos.getX().toFloat(),
            endPos.getY().toFloat(),
            endPos.getZ().toFloat()
        ).color(color.argb).normal(normal, normals.x, normals.y, normals.z).next()
    }

    fun draw4Gradient(
        matrix: Matrix4f,
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        topLeft: Int,
        topRight: Int,
        bottomRight: Int,
        bottomLeft: Int,
    ) {
        val topLeftF = floatArrayOf(
            (topLeft shr 16 and 255) / 255.0f, (topLeft shr 8 and 255) / 255.0f, (topLeft and 255) / 255.0f
        )
        val topRightF = floatArrayOf(
            (topRight shr 16 and 255) / 255.0f, (topRight shr 8 and 255) / 255.0f, (topRight and 255) / 255.0f
        )
        val bottomRightF = floatArrayOf(
            (bottomRight shr 16 and 255) / 255.0f,
            (bottomRight shr 8 and 255) / 255.0f,
            (bottomRight and 255) / 255.0f
        )
        val bottomLeftF = floatArrayOf(
            (bottomLeft shr 16 and 255) / 255.0f,
            (bottomLeft shr 8 and 255) / 255.0f,
            (bottomLeft and 255) / 255.0f
        )

        setup()
        RenderSystem.setShader { GameRenderer.getPositionColorProgram() }
        val tessellator = Tessellator.getInstance()
        val bufferBuilder = tessellator.buffer

        bufferBuilder.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR)
        bufferBuilder.vertex(matrix, x, y + h, 0f).color(bottomLeftF[0], bottomLeftF[1], bottomLeftF[2], 1f)
            .next()
        bufferBuilder.vertex(matrix, x + w, y + h, 0f)
            .color(bottomRightF[0], bottomRightF[1], bottomRightF[2], 1f).next()
        bufferBuilder.vertex(matrix, x + w, y, 0f).color(topRightF[0], topRightF[1], topRightF[2], 1f).next()
        bufferBuilder.vertex(matrix, x, y, 0f).color(topLeftF[0], topLeftF[1], topLeftF[2], 1f).next()

        tessellator.draw()
        clean()
    }

    fun drawHGradient(matrix: Matrix4f, x: Float, y: Float, w: Float, h: Float, vararg colors: Int) {
        if (colors.size < 2) return
        val colorsF = Array(colors.size) { FloatArray(4) }
        for (i in colors.indices) {
            colorsF[i] = floatArrayOf(
                (colors[i] shr 24 and 255) / 255.0f,
                (colors[i] shr 16 and 255) / 255.0f,
                (colors[i] shr 8 and 255) / 255.0f,
                (colors[i] and 255) / 255.0f
            )
        }
        setup()
        RenderSystem.setShader { GameRenderer.getPositionColorProgram() }
        val step = w / colors.size
        for (i in 1 until colors.size) {
            val tessellator = Tessellator.getInstance()
            val bufferBuilder = tessellator.buffer
            bufferBuilder.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR)
            bufferBuilder.vertex(matrix, x + if (i == colors.size - 1) w else step * i, y, 0f)
                .color(colorsF[i][1], colorsF[i][2], colorsF[i][3], colorsF[i][0]).next()
            bufferBuilder.vertex(matrix, x + step * (i - 1), y, 0f)
                .color(colorsF[i - 1][1], colorsF[i - 1][2], colorsF[i - 1][3], colorsF[i - 1][0]).next()
            bufferBuilder.vertex(matrix, x + step * (i - 1), y + h, 0f)
                .color(colorsF[i - 1][1], colorsF[i - 1][2], colorsF[i - 1][3], colorsF[i - 1][0]).next()
            bufferBuilder.vertex(matrix, x + if (i == colors.size - 1) w else step * i, y + h, 0f)
                .color(colorsF[i][1], colorsF[i][2], colorsF[i][3], colorsF[i][0]).next()
            tessellator.draw()
        }
        clean()
    }

    fun drawVGradient(matrix: Matrix4f, x: Float, y: Float, w: Float, h: Float, vararg colors: Int) {
        if (colors.size < 2) return
        val colorsF = Array(colors.size) { FloatArray(4) }
        for (i in colors.indices) {
            colorsF[i] = floatArrayOf(
                (colors[i] shr 24 and 255) / 255.0f,
                (colors[i] shr 16 and 255) / 255.0f,
                (colors[i] shr 8 and 255) / 255.0f,
                (colors[i] and 255) / 255.0f
            )
        }
        setup()
        RenderSystem.setShader { GameRenderer.getPositionColorProgram() }
        val step = h / colors.size
        for (i in 1 until colors.size) {
            val tessellator = Tessellator.getInstance()
            val bufferBuilder = tessellator.buffer
            bufferBuilder.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR)
            bufferBuilder.vertex(matrix, x, y + if (i == colors.size - 1) h else step * i, 0f)
                .color(colorsF[i][1], colorsF[i][2], colorsF[i][3], colorsF[i][0]).next()
            bufferBuilder.vertex(matrix, x + w, y + if (i == colors.size - 1) h else step * i, 0f)
                .color(colorsF[i][1], colorsF[i][2], colorsF[i][3], colorsF[i][0]).next()
            bufferBuilder.vertex(matrix, x + w, y + step * (i - 1), 0f)
                .color(colorsF[i - 1][1], colorsF[i - 1][2], colorsF[i - 1][3], colorsF[i - 1][0]).next()
            bufferBuilder.vertex(matrix, x, y + step * (i - 1), 0f)
                .color(colorsF[i - 1][1], colorsF[i - 1][2], colorsF[i - 1][3], colorsF[i - 1][0]).next()
            tessellator.draw()
        }
        clean()
    }

    private fun drawPolygonPart2D(x: Double, y: Double, radius: Int, part: Int, startColor: Int, endColor: Int) {
        val alpha = (startColor shr 24 and 255).toFloat() / 255.0f
        val red = (startColor shr 16 and 255).toFloat() / 255.0f
        val green = (startColor shr 8 and 255).toFloat() / 255.0f
        val blue = (startColor and 255).toFloat() / 255.0f
        val alpha1 = (endColor shr 24 and 255).toFloat() / 255.0f
        val red1 = (endColor shr 16 and 255).toFloat() / 255.0f
        val green1 = (endColor shr 8 and 255).toFloat() / 255.0f
        val blue1 = (endColor and 255).toFloat() / 255.0f

        setup()
        RenderSystem.setShader { GameRenderer.getPositionColorProgram() }

        val tessellator = Tessellator.getInstance()
        val bufferBuilder = tessellator.buffer

        bufferBuilder.begin(DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR)
        bufferBuilder.vertex(x, y, 0.0).color(red, green, blue, alpha).next()

        val twicePi = Math.PI * 2
        val partToDegrees = part * 90
        val nextPart = partToDegrees + 90

        for (i in partToDegrees..nextPart) {
            val angle = twicePi * i / 360 + Math.toRadians(180.0)
            bufferBuilder.vertex(x + sin(angle) * radius, y + cos(angle) * radius, 0.0)
                .color(red1, green1, blue1, alpha1).next()
        }

        tessellator.draw()
        clean()
    }

    fun drawGlow2D(matrices: MatrixStack, x: Int, y: Int, x1: Int, y1: Int, color: Int) {
        setup()
        RenderSystem.setShader { GameRenderer.getPositionColorProgram() }

        val radius = (y1 - y) / 2
        val halfY = y + radius
        val doubleHalfY = halfY.toDouble()

        ClickGUI.fillGradient(matrices, x, y, x1, halfY, ColorMutable.getTransparency(color), color)
        ClickGUI.fillGradient(matrices, x, halfY, x1, y1, color, ColorMutable.getTransparency(color))
        drawPolygonPart2D(x.toDouble(), doubleHalfY, radius, 0, color, ColorMutable.getTransparency(color))
        drawPolygonPart2D(x.toDouble(), doubleHalfY, radius, 1, color, ColorMutable.getTransparency(color))
        drawPolygonPart2D(x1.toDouble(), doubleHalfY, radius, 2, color, ColorMutable.getTransparency(color))
        drawPolygonPart2D(x1.toDouble(), doubleHalfY, radius, 3, color, ColorMutable.getTransparency(color))

        clean()
    }

    fun drawText3D(matrices: MatrixStack, text: String, offsetX: Float, offsetY: Float, color: ColorMutable) {
        matrices.use {
            matrices.scale(-0.025f, -0.025f, 1f)
            RenderSystem.clear(GL11.GL_DEPTH_BUFFER_BIT, false)
            mc.textRenderer.draw(matrices, text, offsetX, offsetY, color.argb)
        }
    }

    fun drawBackground3D(matrices: MatrixStack, x1: Int, x2: Int, y1: Int, y2: Int, color: ColorMutable) {
        RenderSystem.disableDepthTest()
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        matrices.use {
            matrices.scale(-0.025f, -0.025f, 1f)
            DrawableHelper.fill(matrices, x1, y1, x2, y2, color.argb)
        }
        RenderSystem.enableDepthTest()
    }

    fun drawVignette(threshold: Float, power: Float) {
        val dif = cPlayer.getFullHealth() <= threshold
        val f: Float = abs((if (dif) cPlayer.getFullHealth() / threshold else 1f) - 1f) * power
        RenderSystem.disableDepthTest()
        RenderSystem.depthMask(false)
        RenderSystem.blendFuncSeparate(
            GlStateManager.SrcFactor.ZERO,
            GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR,
            GlStateManager.SrcFactor.ONE,
            GlStateManager.DstFactor.ZERO
        )
        RenderSystem.setShaderColor(0f, f, f, 1.0f)
        RenderSystem.setShader { GameRenderer.getPositionTexProgram() }
        RenderSystem.setShaderTexture(0, (mc.inGameHud as IInGameHud).vignette)
        val tessellator = Tessellator.getInstance()
        val bufferBuilder = tessellator.buffer
        bufferBuilder.begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE)
        bufferBuilder.vertex(0.0, mc.window.scaledHeight.toDouble(), -90.0).texture(0.0f, 1.0f).next()
        bufferBuilder.vertex(
            mc.window.scaledWidth.toDouble(), mc.window.scaledHeight.toDouble(), -90.0
        ).texture(1.0f, 1.0f).next()
        bufferBuilder.vertex(mc.window.scaledWidth.toDouble(), 0.0, -90.0).texture(1.0f, 0.0f).next()
        bufferBuilder.vertex(0.0, 0.0, -90.0).texture(0.0f, 0.0f).next()
        tessellator.draw()
        RenderSystem.depthMask(true)
        RenderSystem.enableDepthTest()
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.defaultBlendFunc()
    }

    fun drawItem2D(matrices: MatrixStack, itemStack: ItemStack, x: Int, y: Int, overlay: Boolean) {
        mc.itemRenderer.renderGuiItemIcon(matrices, itemStack, x, y)
        if (overlay) mc.itemRenderer.renderGuiItemOverlay(matrices, mc.textRenderer, itemStack, x, y)
    }

    fun drawItem3D(matrices: MatrixStack, item: ItemStack) {
        DiffuseLighting.disableGuiDepthLighting()
        mc.itemRenderer.renderItem(
            item,
            ModelTransformationMode.GUI,
            0x00000000,
            OverlayTexture.DEFAULT_UV,
            matrices,
            mc.bufferBuilders.entityVertexConsumers,
            mc.world,
            0
        )
        mc.bufferBuilders.entityVertexConsumers.draw()
        DiffuseLighting.enableGuiDepthLighting()
    }

    fun translateToCamera(matrices: MatrixStack, pos: BlockPos) {
        matrices.translate(pos.x - camera.pos.x, pos.y - camera.pos.y, pos.z - camera.pos.z)
    }

    fun translateToCamera(matrices: MatrixStack, pos: Vec3d) {
        matrices.translate(pos.x - camera.pos.x, pos.y - camera.pos.y, pos.z - camera.pos.z)
    }

    fun rotateToCamera(matrices: MatrixStack) {
        matrices.multiply(camera.rotation)
    }

    fun scale2D(matrices: MatrixStack, scale: Float) {
        matrices.scale(scale, scale, 1f)
    }

    fun Entity.initialBox(): Box = boundingBox.offset(pos.negate())

    private fun setup() {
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
    }

    private fun clean() {
        RenderSystem.disableBlend()
    }

    fun drawMap(matrices: MatrixStack, x: Int, y: Int, stack: ItemStack) {
        val mapState = FilledMapItem.getMapState(stack, mc.world)
        if (mapState != null) {
            RenderSystem.setShaderTexture(0, MAP_BACKGROUND)
            DrawableHelper.drawTexture(matrices, x, y, 0f, 0f, 150, 150, 150, 150)
            matrices.use {
                matrices.translate(x + 11.0, y + 11.0, 1000.0)
                val immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().buffer)
                mc.gameRenderer.mapRenderer.draw(
                    matrices, immediate, FilledMapItem.getMapId(stack)!!, mapState, true, 15728880
                )
                immediate.draw()
            }
        }
    }

    fun Framebuffer.drawWithAlpha(disableBlend: Boolean) {
        val width = mc.window.framebufferWidth
        val height = mc.window.framebufferHeight

        RenderSystem.assertOnRenderThread()
        GlStateManager._disableDepthTest()
        GlStateManager._depthMask(false)
        GlStateManager._viewport(0, 0, width, height)
        if (disableBlend) {
            GlStateManager._disableBlend()
        }
        val minecraftClient = MinecraftClient.getInstance()
        val shaderProgram = minecraftClient.gameRenderer.blitScreenProgram
        shaderProgram.addSampler("DiffuseSampler", this.colorAttachment)
        val matrix4f = Matrix4f().setOrtho(0.0f, width.toFloat(), height.toFloat(), 0.0f, 1000.0f, 3000.0f)
        RenderSystem.setProjectionMatrix(matrix4f)
        if (shaderProgram.modelViewMat != null) {
            shaderProgram.modelViewMat!!.set(Matrix4f().translation(0.0f, 0.0f, -2000.0f))
        }
        if (shaderProgram.projectionMat != null) {
            shaderProgram.projectionMat!!.set(matrix4f)
        }
        shaderProgram.bind()
        val f = width.toFloat()
        val g = height.toFloat()
        val h: Float = this.viewportWidth.toFloat() / this.textureWidth.toFloat()
        val i: Float = this.viewportHeight.toFloat() / this.textureHeight.toFloat()
        val tessellator = RenderSystem.renderThreadTesselator()
        val bufferBuilder = tessellator.buffer
        bufferBuilder.begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR)
        bufferBuilder.vertex(0.0, g.toDouble(), 0.0).texture(0.0f, 0.0f).color(255, 255, 255, 255).next()
        bufferBuilder.vertex(f.toDouble(), g.toDouble(), 0.0).texture(h, 0.0f).color(255, 255, 255, 255).next()
        bufferBuilder.vertex(f.toDouble(), 0.0, 0.0).texture(h, i).color(255, 255, 255, 255).next()
        bufferBuilder.vertex(0.0, 0.0, 0.0).texture(0.0f, i).color(255, 255, 255, 255).next()
        BufferRenderer.draw(bufferBuilder.end())
        shaderProgram.unbind()
        GlStateManager._depthMask(true)
        GlStateManager._colorMask(true, true, true, true)
    }

    inline fun MatrixStack.use(callback: () -> Unit) {
        push()
        callback()
        pop()
    }

    private val camera: Camera
        get() = mc.gameRenderer.camera
}