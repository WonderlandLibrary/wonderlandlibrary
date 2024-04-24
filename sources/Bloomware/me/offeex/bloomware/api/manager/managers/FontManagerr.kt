package me.offeex.bloomware.api.manager.managers

import me.offeex.bloomware.api.gui.font.NewFontRenderer
import me.offeex.bloomware.api.manager.Manager
import me.offeex.bloomware.api.rust.NativeFontRasterizer
import me.offeex.bloomware.api.structure.ColorMutable
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.setting.settings.SettingColor
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventScaleFactor
import net.minecraft.client.util.math.MatrixStack

object FontManagerr : Manager() {
    private lateinit var renderer: NewFontRenderer

    private val name = "Raleway"
    private var fontSize = 18f // fallback value
    private var currentScaleFactor = 1f

    private val fontMap: Map<String, NativeFontRasterizer.Font> by lazy {
        val f = javaClass.getResourceAsStream("/assets/bloomware/fonts/$name.ttf")!!.readAllBytes()
        hashMapOf(name to NativeFontRasterizer.loadFont(f, name))
    }

    @Subscribe
    private fun onScaleFactorChange(event: EventScaleFactor) {
        if (currentScaleFactor == event.scaleFactor.toFloat()) return
        currentScaleFactor = event.scaleFactor.toFloat()

        val font = if (::renderer.isInitialized) renderer.font else fontMap[name]!!
        updateFont(font, fontSize * event.scaleFactor.toFloat())
    }

    fun drawString(
        matrices: MatrixStack,
        title: String,
        x: Number,
        y: Number,
        color: SettingColor,
        m: HudModule? = null
    ) {
        drawString(matrices, title, x, y, color.color, m)
    }

    fun drawString(
        matrices: MatrixStack,
        title: String,
        x: Number,
        y: Number,
        color: Int,
        m: HudModule? = null
    ) {
        m?.width = width(title).toInt()
        renderer.draw(matrices, title, x.toFloat(), y.toFloat() - 4, color)
    }

    fun drawString(
        matrices: MatrixStack,
        title: String,
        x: Number,
        y: Number,
        color: ColorMutable,
        m: HudModule? = null
    ) {
        drawString(matrices, title, x.toFloat(), y.toFloat(), color.argb, m)
    }

    fun drawVCenteredString(
        matrix: MatrixStack,
        title: String,
        x: Number,
        y: Number,
        color: ColorMutable
    ) {
        drawString(matrix, title, x, y.toFloat() - height(title) / 2f, color)
    }

    fun width(title: String) = renderer.width(title)
    fun height(title: String) = renderer.height(title)

    fun updateSize(size: Float) {
        fontSize = size
        updateFont(renderer.font, size * currentScaleFactor)
    }

    fun updateFontTTF(name: String, size: Number) {
        updateFont(fontMap[name]!!, size.toFloat() * currentScaleFactor)
    }

    fun updateFont(font: NativeFontRasterizer.Font, size: Float) {
        if (::renderer.isInitialized) this.renderer.close()
        renderer = NewFontRenderer(font, size, currentScaleFactor)
    }
}
