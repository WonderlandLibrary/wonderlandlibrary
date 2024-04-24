package me.offeex.bloomware.api.rust

import org.lwjgl.opengl.GL

@Suppress("unused")
object NativeFontRasterizer {
    /**
     * @param char Glyph char
     * @param u Texture u (x) coordinate
     * @param v Texture v (y) coordinate
     *
     * @param width Full glyph width on texture
     * @param height Full glyph height on texture
     * @param xmin Whole pixel offset of the left-most edge of the bitmap. This may be negative to reflect the glyph is positioned to the left of the origin.
     * @param ymin Whole pixel offset of the bottom-most edge of the bitmap. This may be negative to reflect the glyph is positioned below the baseline.
     * @param advanceWidth Advance width of the glyph in subpixels. Used in horizontal fonts.
     * @param advanceHeight Advance height of the glyph in subpixels. Used in vertical fonts.
     *
     * Bounds: The bounding box that contains the glyph's outline at the offsets specified by the font. This is always a smaller box than the bitmap bounds.
     * @param boundsXmin Subpixel offset of the left-most edge of the glyph's outline.
     * @param boundsYmin Subpixel offset of the bottom-most edge of the glyph's outline.
     * @param boundsWidth The width of the outline in subpixels.
     * @param boundsHeight The height of the outline in subpixels.
     */
    data class GlyphData(
        val char: Char,

        // Texture coordinates
        val u: Int,
        val v: Int,
        val width: Int,
        val height: Int,

        // Pixel-precise glyph bounds
        val xmin: Int,
        val ymin: Int,

        // Subpixel-precise glyph bounds
        val advanceWidth: Float,
        val advanceHeight: Float,

        val boundsXmin: Float,
        val boundsYmin: Float,
        val boundsWidth: Float,
        val boundsHeight: Float
    )

    /**
     * @param ascent The highest point that any glyph in the font extends to above the baseline. Typically positive.
     * @param descent The lowest point that any glyph in the font extends to below the baseline. Typically negative.
     * @param lineGap The gap to leave between the descent of one line and the ascent of the next. This is of course only a guideline given by the font's designers.
     * @param newLineSize A precalculated value for the height or width of the line depending on if the font is laid out horizontally or vertically. It's calculated by: ascent - descent + line_gap.
     */
    data class LineMetrics(
        val ascent: Float,
        val descent: Float,
        val lineGap: Float,
        val newLineSize: Float
    )

    @JvmInline
    value class Font(val id: Int)

    fun loadFont(font: ByteArray, name: String): Font {
        val id = nativeLoadFont(font, name)
        if (id == -1) throw RuntimeException("Failed to load font")
        return Font(id)
    }

    fun getGlyphCount(font: Font): Int {
        return nativeGetGlyphCount(font.id).also {
            if (it == -1) throw RuntimeException("Failed to get glyph count")
        }
    }

    fun getName(font: Font): String {
        return nativeGetName(font.id)
    }

    fun searchFont(name: String): Font {
        val id = nativeSearchFont(name)
        if (id == -1) throw RuntimeException("Failed to find font")
        return Font(id)
    }

    fun deleteFont(font: Font) {
        val id = nativeDeleteFont(font.id)
        if (id == -1) throw RuntimeException("Failed to delete font")
    }

    fun makeAtlas(font: Font, size: Float, segment: Int, textureId: Int): List<GlyphData> {
        glyphBuffer.clear()
        val result = nativeMakeAtlas(font.id, size, segment, this, textureId)
        if (!result) throw RuntimeException("Failed to make atlas")
        val list = glyphBuffer.toList()
        glyphBuffer.clear()
        return list
    }

    private fun getLineMetrics(font: Font, size: Float, horizontal: Boolean): LineMetrics {
        lineMetrics = null
        val result = nativeGetLineMetrics(font.id, size, horizontal, this)
        if (!result) throw RuntimeException("Failed to get line metrics")
        val metrics = lineMetrics!!
        lineMetrics = null
        return metrics
    }

    fun getHorizontalLineMetrics(font: Font, size: Float): LineMetrics {
        return getLineMetrics(font, size, true)
    }

    fun getVerticalLineMetrics(font: Font, size: Float): LineMetrics {
        return getLineMetrics(font, size, false)
    }

    @JvmStatic
    external fun nativeSetup(callback: NativeFontRasterizer)

    @JvmStatic
    private external fun nativeLoadFont(font: ByteArray, name: String): Int

    @JvmStatic
    private external fun nativeGetGlyphCount(font: Int): Int

    @JvmStatic
    private external fun nativeGetName(font: Int): String

    @JvmStatic
    private external fun nativeSearchFont(name: String): Int

    @JvmStatic
    private external fun nativeDeleteFont(font: Int): Int

    @JvmStatic
    private external fun nativeMakeAtlas(font: Int, size: Float, segment: Int, callback: NativeFontRasterizer, textureId: Int): Boolean

    @JvmStatic
    private external fun nativeGetLineMetrics(font: Int, size: Float, horizontal: Boolean, callback: NativeFontRasterizer): Boolean

    private fun getGlFunction(name: String): Long {
        return GL.getFunctionProvider()!!.getFunctionAddress(name)
    }

    private val glyphBuffer = mutableListOf<GlyphData>()

    /**
     * @param char - Glyph char
     * @param u - Texture u (x) coordinate
     * @param v - Texture v (y) coordinate
     *
     * @param width - Full glyph width on texture
     * @param height - Full glyph height on texture
     * @param xmin - Whole pixel offset of the left-most edge of the bitmap. This may be negative to reflect the glyph is positioned to the left of the origin.
     * @param ymin - Whole pixel offset of the bottom-most edge of the bitmap. This may be negative to reflect the glyph is positioned below the baseline.
     * @param advanceWidth - Advance width of the glyph in subpixels. Used in horizontal fonts.
     * @param advanceHeight - Advance height of the glyph in subpixels. Used in vertical fonts.
     *
     * Bounds: The bounding box that contains the glyph's outline at the offsets specified by the font. This is always a smaller box than the bitmap bounds.
     * @param boundsXmin - Subpixel offset of the left-most edge of the glyph's outline.
     * @param boundsYmin - Subpixel offset of the bottom-most edge of the glyph's outline.
     * @param boundsWidth - The width of the outline in subpixels.
     * @param boundsHeight - The height of the outline in subpixels.
     */
    private fun pushCharData(
        char: Char,
        u: Int,
        v: Int,
        width: Int,
        height: Int,
        xmin: Int,
        ymin: Int,
        advanceWidth: Float,
        advanceHeight: Float,
        boundsXmin: Float,
        boundsYmin: Float,
        boundsWidth: Float,
        boundsHeight: Float
    ) {
        glyphBuffer.add(
            GlyphData(
                char,
                u,
                v,
                width,
                height,
                xmin,
                ymin,
                advanceWidth,
                advanceHeight,
                boundsXmin,
                boundsYmin,
                boundsWidth,
                boundsHeight
            )
        )
    }

    private var lineMetrics: LineMetrics? = null

    private fun pushLineMetrics(
        ascent: Float,
        descent: Float,
        lineGap: Float,
        newLineSize: Float,
    ) {
        lineMetrics = LineMetrics(
            ascent,
            descent,
            lineGap,
            newLineSize
        )
    }
}