package me.offeex.bloomware.api.gui.font

import com.mojang.blaze3d.systems.RenderSystem
import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.graphics.GLApi
import me.offeex.bloomware.api.graphics.elements.Texture
import me.offeex.bloomware.api.graphics.impl.pipelines.FontRendererPipeline
import me.offeex.bloomware.api.manager.managers.GraphicsManager
import me.offeex.bloomware.api.rust.NativeFontRasterizer
import net.minecraft.client.util.math.MatrixStack
import org.joml.Matrix4f
import java.awt.font.GlyphMetrics
import java.awt.font.TextLayout
import java.io.Closeable
import kotlin.math.abs
import kotlin.math.round

/**
 * Custom FontRenderer
 *
 * Represents instance, that can render any font with any size
 * Font and size are immutablem, so reinitialization is required
 *
 * @constructor Precomputes all glyphs for atlas groups returns [NewFontRenderer]
 */
class NewFontRenderer(
    val font: NativeFontRasterizer.Font,
    val size: Float,
    private val scaleFactor: Float,
    precompute: Array<Int> = arrayOf() // TODO: Fix or remove
) : Closeable {

    var closed = false
        private set
    val lineMetrics = NativeFontRasterizer.getHorizontalLineMetrics(font, size)

    private val atlasMap = hashMapOf<Int, Atlas>()

    init {
        precompute.forEach { computeAtlas(it) }
    }

    /**
     * For each char, we need to know which atlas it belongs to.
     * Chars are evenly distributed between 0 and 127
     *
     * In our case we use optimized bitwise version
     *
     * @return [Atlas] for given char
     */
    private fun requestAtlas(char: Char): Atlas {
        // Optimized version of char.code / 128
        val atlasId = char.code shr 7;
        val atlas = atlasMap[atlasId]
        return atlas ?: computeAtlas(atlasId)
    }


    /**
     * Computes atlas for given id, if needed, and returns it
     *
     * @param atlasId equals to char.code divided by 128
     * @return Computed [Atlas]
     */
    private fun computeAtlas(atlasId: Int): Atlas {
        val glyphs = hashMapOf<Char, NativeFontRasterizer.GlyphData>()
        val texture = GraphicsManager.reg(Texture())

        texture.setWrapS(GLApi.TextureWrap.REPEAT)
        texture.setWrapT(GLApi.TextureWrap.REPEAT)
        texture.setMinFilter(GLApi.TextureFilter.NEAREST)
        texture.setMagFilter(GLApi.TextureFilter.NEAREST)

        NativeFontRasterizer.makeAtlas(font, size, atlasId, texture.texture).forEach {
            glyphs[it.char] = it
        }

        val atlas = Atlas(atlasId, texture, glyphs)
        atlasMap[atlasId] = atlas

        return atlas
    }

    override fun close() {
        assertIsOpen()

        closed = true
        atlasMap.values.forEach { it.texture.close() }
    }

    private fun assertIsOpen() {
        if (closed) {
            throw IllegalStateException("Use of closed font renderer")
        }
    }

    fun draw(
        matrices: MatrixStack,
        text: String,
        x: Float,
        y: Float,
        color: Int,
        isGUI: Boolean = true
    ) {
        assertIsOpen()

        var x0 = x * scaleFactor
        var y0 = (y + lineMetrics.ascent) * scaleFactor
        val session = RenderingSession(::requestAtlas, lineMetrics)

        for (c in text) {
            val glyph = session.push(c, x0, y0)

            if (c == '\n') {
                x0 = x
                y0 += glyph.height
                continue
            }

            x0 += glyph.advanceWidth
        }

        session.draw(matrices.peek().positionMatrix, color, isGUI)

    }

    fun width(text: String) = TextLayout(text).width
    fun height(text: String) = TextLayout(text).height

    /**
     * Represents a record of text layout. Used to measure text
     *
     * @constructor
     *
     * @param text string to measure
     */
    private inner class TextLayout(text: String) {
        val width: Float
        val height: Float

        init {
            var width = 0f
            var height = 0f

            for (c in text) {
                val atlas = requestAtlas(c)
                val glyph = atlas.glyphs[c] ?: continue

                width += glyph.advanceWidth
                height = maxOf(height, glyph.height.toFloat())
            }

            this.width = width * scaleFactor
            this.height = height * scaleFactor
        }
    }

    /**
     * Represents a record of atlas
     * @property texture minecraft-specific identifier of atlas
     */
    private data class Atlas(
        val id: Int,
        val texture: Texture,
        val glyphs: HashMap<Char, NativeFontRasterizer.GlyphData>
    )

    /**
     * Used to build mesh for entire text
     *
     * @property atlasResolver function that resolves atlas for given char
     */
    private class RenderingSession(
        private val atlasResolver: (Char) -> Atlas,
        private val lineMetrics: NativeFontRasterizer.LineMetrics
    ) {
        private val meshes = mutableMapOf<Int, MeshBuilder>()


        /**
         * Resolves mesh builder for given atlas. If it doesn't exist, create new one
         *
         * @return mesh builder for given atlas
         */
        private fun resolveMeshBuilder(atlas: Atlas): MeshBuilder {
            return meshes[atlas.id] ?: MeshBuilder(atlas).also { meshes[atlas.id] = it }
        }

        /**
         * Pushes glyph to corresponding mesh builder
         *
         * @param char char to push
         * @param x x coordinate of char
         * @param y y coordinate of char
         * @return pushed glyph
         */
        fun push(char: Char, x: Float, y: Float): NativeFontRasterizer.GlyphData {
            val atlas = atlasResolver(char)
            val glyph = atlas.glyphs[char]!!
            val mesh = resolveMeshBuilder(atlas)

            val x0 = x + glyph.xmin
            val x1 = x0 + glyph.width
            val y0 = y - glyph.height - glyph.ymin + lineMetrics.ascent
            val y1 = y0 + glyph.height

            val u0 = glyph.u.toFloat()
            val v0 = glyph.v.toFloat()
            val u1 = (glyph.u + glyph.width).toFloat()
            val v1 = (glyph.v + glyph.height).toFloat()

            mesh.allocateNext()
            mesh.push(x0, y1, u0, v1)
            mesh.push(x1, y1, u1, v1)
            mesh.push(x1, y0, u1, v0)
            mesh.push(x0, y0, u0, v0)

            return glyph
        }

        /**
         * Draws all accumulated meshes
         *
         * @param pos matrix stack to draw with
         * @param color color to draw with
         * @param useFlatProjectionMatrix whether to swap projection matrix (used for GUI)
         */
        fun draw(pos: Matrix4f, color: Int, useFlatProjectionMatrix: Boolean) {

            val projMat = if (useFlatProjectionMatrix) {
                Matrix4f().setOrtho(
                    0f,
                    mc.window.framebufferWidth.toFloat(),
                    mc.window.framebufferHeight.toFloat(),
                    0f,
                    1000f,
                    3000f
                )
            }
            else {
                RenderSystem.getProjectionMatrix()
            }

            val r = (color shr 16 and 0xFF) / 255.0f
            val g = (color shr 8 and 0xFF) / 255.0f
            val b = (color and 0xFF) / 255.0f
            val a = (color shr 24 and 0xFF) / 255.0f

            GraphicsManager.pipelineFontRenderer.use {
                matrices(RenderSystem.getModelViewMatrix(), projMat)

                meshes.values.forEach { mesh ->
                    atlas(mesh.atlas.texture)
                    mesh.build(this, pos, r, g, b, a)
                    draw()
                }
            }
        }

        /**
         * Used to build mesh for single atlas
         *
         * @property atlas atlas to build mesh for
         */
        private class MeshBuilder(val atlas: Atlas) {

            // Optimized low level quads storage
            val quads = mutableListOf<FloatArray>()
            private lateinit var currentQuad: FloatArray
            private var currentShift = 0

            /**
             * Allocates new quad (4 vertices)
             */
            fun allocateNext() {
                currentQuad = FloatArray(16)
                currentShift = 0
                quads.add(currentQuad)
            }

            /**
             * Pushes vertex to current quad
             *
             * @param x x coordinate of vertex
             * @param y y coordinate of vertex
             * @param u u coordinate of texture
             * @param v v coordinate of texture
             */
            fun push(x: Float, y: Float, u: Float, v: Float) {
                currentQuad[currentShift + 0] = x
                currentQuad[currentShift + 1] = y
                currentQuad[currentShift + 2] = u
                currentQuad[currentShift + 3] = v
                currentShift += 4;
            }

            /**
             * Uploads mesh to buffer builder
             *
             * @param builder buffer builder to build mesh with
             * @param matrices matrix stack to build mesh with
             * @param red red color component
             * @param green green color component
             * @param blue blue color component
             * @param alpha alpha color component
             */
            fun build(
                builder: FontRendererPipeline,
                matrices: Matrix4f,
                red: Float,
                green: Float,
                blue: Float,
                alpha: Float
            ) {
                quads.forEach { q ->
                    builder.vertex().pos(matrices, q[0], q[1], 0f).tex(q[2], q[3]).color(red, green, blue, alpha)
                    builder.vertex().pos(matrices, q[4], q[5], 0f).tex(q[6], q[7]).color(red, green, blue, alpha)
                    builder.vertex().pos(matrices, q[8], q[9], 0f).tex(q[10], q[11]).color(red, green, blue, alpha)
                    builder.vertex().pos(matrices, q[12], q[13], 0f).tex(q[14], q[15]).color(red, green, blue, alpha)
                }
            }
        }
    }
}