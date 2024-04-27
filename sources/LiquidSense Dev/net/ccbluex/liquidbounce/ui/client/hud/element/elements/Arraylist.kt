/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import com.google.gson.JsonElement
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.client.hud.element.Side.Horizontal
import net.ccbluex.liquidbounce.ui.client.hud.element.Side.Vertical
import net.ccbluex.liquidbounce.ui.font.AWTFontRenderer
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.Colors
import net.ccbluex.liquidbounce.utils.misc.StringUtils
import net.ccbluex.liquidbounce.utils.render.AnimationUtils
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.shader.shaders.RainbowFontShader
import net.ccbluex.liquidbounce.utils.render.shader.shaders.RainbowShader
import net.ccbluex.liquidbounce.value.*
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color

/**
 * CustomHUD Arraylist element
 *
 * Shows a list of enabled modules
 */
@ElementInfo(name = "Arraylist", single = true)
class Arraylist(x: Double = 1.0, y: Double = 2.0, scale: Float = 1F,
                side: Side = Side(Horizontal.RIGHT, Vertical.UP)) : Element(x, y, scale, side) {
    private val RianbowspeedValue = IntegerValue("BRainbowSpeed", 90, 1, 90)
    private val RianbowbValue = FloatValue("BRainbow-Saturation", 1f, 0f, 1f)
    private val RianbowsValue = FloatValue("BRainbow-Brightness", 1f, 0f, 1f)
    private val randowRedValue = IntegerValue("OneRainbow-R", 8, 1, 8)
    private val randowGreenValue = IntegerValue("OneRainbow-G", 8, 1, 8)
    private val randowBlueValue = IntegerValue("OneRainbow-B", 8, 1, 8)
    private val randow2RedValue = IntegerValue("MotionRainbow-R", 120, 0, 120)
    private val randow2GreenValue = IntegerValue("MotionRainbow-G", 120, 0, 120)
    private val randow2BlueValue = IntegerValue("MotionRainbow-B", 120, 0, 120)
    private val rainbowX = FloatValue("Rainbow-X", -1000F, -2000F, 2000F)
    private val rainbowY = FloatValue("Rainbow-Y", -1000F, -2000F, 2000F)
    private val colorModeValue =
            object : ListValue("Text-Color", arrayOf("Custom", "Random", "Rainbow","OtherRainbow","ALLRainbow","Bainbow","TwoRainbow","OriginalRainbow"), "OtherRainbow"){
                override fun changeElement() {
                    colorRedValue.isSupported = (get() == "Custom").also {
                        colorGreenValue.isSupported = it;colorBlueValue.isSupported = it;
                    }
                }
            }
    private val colorRedValue = IntegerValue("Text-R", 0, 0, 255)
    private val colorGreenValue = IntegerValue("Text-G", 111, 0, 255)
    private val colorBlueValue = IntegerValue("Text-B", 255, 0, 255)
    private val rectColorModeValue =
            object : ListValue("Rect-Color", arrayOf("Custom", "Random", "Rainbow","OtherRainbow","ALLRainbow","Bainbow","TwoRainbow","OriginalRainbow"), "OtherRainbow"){
                override fun changeElement() {
                    rectColorRedValue.isSupported = (get() == "Custom").also {
                        rectColorGreenValue.isSupported = it;rectColorBlueValue.isSupported = it;rectColorBlueAlpha.isSupported =
                            it
                    }
                }
            }
    private val rectColorRedValue = IntegerValue("Rect-R", 255, 0, 255)
    private val rectColorGreenValue = IntegerValue("Rect-G", 255, 0, 255)
    private val rectColorBlueValue = IntegerValue("Rect-B", 255, 0, 255)
    private val rectColorBlueAlpha = IntegerValue("Rect-Alpha", 255, 0, 255)
    private val saturationValue = FloatValue("Random-Saturation", 0.9f, 0f, 1f)
    private val brightnessValue = FloatValue("Random-Brightness", 1f, 0f, 1f)
    private val TwoRainbow = FloatValue("TwoRainbow", 1f, 0f, 1f)
    private val tags = BoolValue("Tags", true)
    private val namebreak: BoolValue = object : BoolValue("NameBreak", true) {
        override fun onChanged(oldValue: Boolean, newValue: Boolean) {
            if (newValue)
                LiquidBounce.moduleManager.modules.forEach { it.arrayListName = getBreakName(it.name) }
            else
                LiquidBounce.moduleManager.modules.forEach { it.arrayListName = it.name }
        }

        override fun fromJson(element: JsonElement) {
            super.fromJson(element)
            onChanged(value, value)
        }
    }

    fun getBreakName(str: String): String {
        var out = ""
        if (str.isNotEmpty()) out += str[0]
        for (i in 1 until str.length) {
            if (str[i].isUpperCase() && i < str.length - 1 && (str[i + 1].isLowerCase() || str[i - 1].isLowerCase()))
                out += " "
            out += str[i]
        }
        return out
    }
    private val shadow = BoolValue("ShadowText", true)
    private val backgroundColorModeValue =
            object : ListValue("Background-Color", arrayOf("Custom", "Random", "Rainbow","OtherRainbow","ALLRainbow","Bainbow","TwoRainbow","OriginalRainbow"), "Custom"){
                override fun changeElement() {
                    backgroundColorRedValue.isSupported = (get() == "Custom").also {
                        backgroundColorGreenValue.isSupported = it;backgroundColorBlueValue.isSupported = it;backgroundColorAlphaValue.isSupported =
                            it
                    }
                }
            }

    private val backgroundColorRedValue = IntegerValue("Background-R", 0, 0, 255)
    private val backgroundColorGreenValue = IntegerValue("Background-G", 0, 0, 255)
    private val backgroundColorBlueValue = IntegerValue("Background-B", 0, 0, 255)
    private val backgroundColorAlphaValue = IntegerValue("Background-Alpha", 0, 0, 255)
    private val rectValue = ListValue("Rect", arrayOf("None", "Left", "Right" , "RLeft"), "RLeft")
    private val rlefttop = BoolValue("RLeftTop", false)
    private val rleftright = BoolValue("RLeftRight", true)
    private val rleftall = BoolValue("RLeftALL", false)
    private val upperCaseValue = BoolValue("UpperCase", false)
    private val spaceValue = FloatValue("Space", 0F, 0F, 5F)
    private val textHeightValue = FloatValue("TextHeight", 11F, 1F, 20F)
    private val textYValue = FloatValue("TextY", 0F, 0F, 20F)
    private val tagsArrayColor = BoolValue("TagsArrayColor", false)
    private val animationValue = ListValue("Animation", arrayOf("Normal", "slide"), "Normal")
    private val fontValue = FontValue("Font", Fonts.font40)


    private var x2 = 0
    private var y2 = 0F

    private var modules = emptyList<Module>()

    override fun drawElement(): Border? {

        val fontRenderer = fontValue.get()

        val delta = RenderUtils.deltaTime

        for (module in LiquidBounce.moduleManager.modules) {
            if (!module.array || (!module.state && module.slide == 0F)) continue




            var displayString =  ("")  +

                    if (!tags.get())
                        module.arrayListName
                    else if (tagsArrayColor.get())
                        module.colorlessTagName
                    else module.tagName

            if (upperCaseValue.get())
                displayString = displayString?.toUpperCase()

            var width = fontRenderer.getStringWidth( displayString )

            if (module.state) {
                if (module.slide < width) {
                    module.slide +=  delta * 0.5f
                }
            } else if (!module.state) {
                module.slide -=  delta * 0.5f
            }

            module.slide = module.slide.coerceIn(0F, width.toFloat())
        }

        // Draw arraylist
        val colorMode = colorModeValue.get()
        val rectColorMode = rectColorModeValue.get()
        val backgroundColorMode = backgroundColorModeValue.get()
        val customColor = Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get(), 1).rgb
        val rectCustomColor = Color(rectColorRedValue.get(), rectColorGreenValue.get(), rectColorBlueValue.get(),
                rectColorBlueAlpha.get()).rgb
        val space = spaceValue.get()
        val textHeight = textHeightValue.get()
        val textY = textYValue.get()
        val rectMode = rectValue.get()
        val animationMode = animationValue.get()
        val backgroundCustomColor = Color(backgroundColorRedValue.get(), backgroundColorGreenValue.get(),
                backgroundColorBlueValue.get(), backgroundColorAlphaValue.get()).rgb
        val textShadow = shadow.get()
        val textSpacer = textHeight + space
        val saturation = saturationValue.get()
        val brightness = brightnessValue.get()
        val Rsaturation = RianbowbValue.get()
        val Rbrightness = RianbowsValue.get()

        when (side.horizontal) {
            Horizontal.RIGHT, Horizontal.MIDDLE -> {
                modules.forEachIndexed { index, module ->
                    var displayString =  ("")  +
                            if (!tags.get())
                                module.arrayListName
                            else if (tagsArrayColor.get())
                                module.colorlessTagName
                            else module.tagName



                    if (upperCaseValue.get())
                        displayString = displayString?.toUpperCase()
                    val xPos = if(animationMode.equals("normal",true))
                        -fontRenderer.getStringWidth(displayString).toFloat() - 2 else   -module.slide - 2
                    val yPos = (if (side.vertical == Vertical.DOWN) -textSpacer else textSpacer) *
                            if (side.vertical == Vertical.DOWN) index + 1 else index
                    val moduleColor = Color.getHSBColor(module.hue, saturation, brightness).rgb
                    val LiquidSlowly = ColorUtils.LiquidSlowly(System.nanoTime(), index * RianbowspeedValue.get(), Rsaturation, Rbrightness)?.rgb
                    val c: Int = LiquidSlowly!!
                    val col = Color(c)
                    val braibow = Color(col.green/randowRedValue.get()+randow2RedValue.get(), col.green /randowGreenValue.get()+randow2GreenValue.get(), col.green /randowBlueValue.get()+randow2BlueValue.get()).rgb

                    val backgroundRectRainbow = backgroundColorMode.equals("Rainbow", ignoreCase = true)
                    val size = modules.size * 2.0E-2f
                    if (module.state) {
                        if (module.higt < yPos) {
                            module.higt += (size - Math.min(module.higt * 0.002f, size - (module.higt * 0.0001f) )) * delta
                            module.higt = Math.min(yPos, module.higt)
                        } else {
                            module.higt -= (size - Math.min(module.higt * 0.002f, size - (module.higt * 0.0001f) )) * delta
                            module.higt = Math.max(module.higt, yPos)
                        }
                    }

                    RainbowShader.begin(backgroundRectRainbow, if (rainbowX.get() == 0.0F) 0.0F else 1.0F / rainbowX.get(), if (rainbowY.get() == 0.0F) 0.0F else 1.0F / rainbowY.get(), System.currentTimeMillis() % 10000 / 10000F).use {
                        RenderUtils.drawRect(
                                xPos - if (rectMode.equals("right", true) || rectMode.equals("rleft", true)) 5 else 3,
                                module.higt - if (index == 0) 1 else 0,
                                if (rectMode.equals("right", true) || rectMode.equals("rleft", true)) -1.5F else 0F,
                                module.higt + textHeight, when {
                            backgroundRectRainbow -> 0xFF shl 24
                            backgroundColorMode.equals("Random", ignoreCase = true) -> moduleColor
                            backgroundColorMode.equals("OtherRainbow", ignoreCase = true) -> Colors.getRainbow(-2000, (yPos * 8.toFloat()).toInt())
                            backgroundColorMode.equals("ALLRainbow", ignoreCase = true) -> ColorUtils.ALLColor(400000000L * index).rgb
                            backgroundColorMode.equals("Bainbow", ignoreCase = true) -> braibow
                            backgroundColorMode.equals("TwoRainbow", ignoreCase = true) -> ColorUtils.TwoRainbow(400000000L * index,TwoRainbow.get()).rgb
                            backgroundColorMode.equals("OriginalRainbow", ignoreCase = true) -> ColorUtils.originalrainbow(400000000L * index).rgb
                            else -> backgroundCustomColor
                        }
                        )
                    }

                    val rainbow = colorMode.equals("Rainbow", ignoreCase = true)

                    RainbowFontShader.begin(rainbow, if (rainbowX.get() == 0.0F) 0.0F else 1.0F / rainbowX.get(), if (rainbowY.get() == 0.0F) 0.0F else 1.0F / rainbowY.get(), System.currentTimeMillis() % 10000 / 10000F).use {
                        fontRenderer.drawString(displayString, xPos - if (rectMode.equals("right", true) || rectMode.equals("rleft", true)) 3 else 0, module.higt + textY, when {
                            rainbow -> 0
                            colorMode.equals("Random", ignoreCase = true) -> moduleColor
                            colorMode.equals("OtherRainbow", ignoreCase = true) -> Colors.getRainbow(-2000, (yPos * 8.toFloat()).toInt())
                            colorMode.equals("ALLRainbow", ignoreCase = true) -> ColorUtils.ALLColor(400000000L * index).rgb
                            colorMode.equals("Bainbow", ignoreCase = true) -> braibow
                            colorMode.equals("TwoRainbow", ignoreCase = true) -> ColorUtils.TwoRainbow(400000000L * index,TwoRainbow.get()).rgb
                            colorMode.equals("OriginalRainbow", ignoreCase = true) -> ColorUtils.originalrainbow(400000000L * index).rgb
                            else -> customColor
                        }, textShadow)
                    }

                    if (!rectMode.equals("none", true)) {
                        val rectRainbow = rectColorMode.equals("Rainbow", ignoreCase = true)

                        RainbowShader.begin(rectRainbow, if (rainbowX.get() == 0.0F) 0.0F else 1.0F / rainbowX.get(), if (rainbowY.get() == 0.0F) 0.0F else 1.0F / rainbowY.get(), System.currentTimeMillis() % 10000 / 10000F).use {
                            val rectColor = when {
                                rectRainbow -> 0
                                rectColorMode.equals("Random", ignoreCase = true) -> moduleColor
                                rectColorMode.equals("OtherRainbow", ignoreCase = true) -> Colors.getRainbow(-2000, (yPos * 8.toFloat()).toInt())
                                rectColorMode.equals("ALLRainbow", ignoreCase = true) -> ColorUtils.ALLColor(400000000L * index).rgb
                                rectColorMode.equals("Bainbow", ignoreCase = true) -> braibow
                                rectColorMode.equals("TwoRainbow", ignoreCase = true) -> ColorUtils.TwoRainbow(400000000L * index,TwoRainbow.get()).rgb
                                rectColorMode.equals("OriginalRainbow", ignoreCase = true) -> ColorUtils.originalrainbow(400000000L * index).rgb
                                else -> rectCustomColor
                            }

                            when {
                                rectMode.equals("left", true) -> RenderUtils.drawRect(xPos - 3.2f, module.higt - 1, xPos - 2, module.higt + textHeight,
                                        rectColor)
                                rectMode.equals("right", true) ->
                                    RenderUtils.drawRect(-1.5F, module.higt - 1F, 0F,
                                            module.higt + textHeight, rectColor)

                                rectMode.equals("rleft", true) -> {

                                    if (rleftright.get()){
                                        RenderUtils.drawRect(-1.5F, module.higt - 2F, 0F,
                                                module.higt + textHeight - 1, rectColor)
                                    }
                                    RenderUtils.drawRect(xPos - 8, module.higt - 3, xPos - 7, module.higt + textHeight - 2,
                                            rectColor)//左条
                                    if(module!=modules.get(0)){
                                        var displayStrings = if (!tags.get())
                                            modules.get(index - 1).name
                                        else if (tagsArrayColor.get())
                                            modules.get(index - 1).colorlessTagName
                                        else  modules.get(index - 1).tagName

                                        if (upperCaseValue.get())
                                            displayStrings = displayStrings?.toUpperCase()





                                        RenderUtils.drawRect(xPos-8- (fontRenderer.getStringWidth(displayStrings)-fontRenderer.getStringWidth(displayString)), yPos - 3, xPos-8, yPos - 2,
                                                rectColor) //功能左条和下条间隔
                                    }

                                    if(module==modules.get(modules.size-1)){
                                        RenderUtils.drawRect(xPos - 8, module.higt+textHeight - 2, 0.0F, module.higt + textHeight - 1,
                                                rectColor) //下条
                                    }
                                    if (rleftall.get()){
                                        RenderUtils.drawRect(xPos - 8, module.higt+textHeight - 3, 0.0F, module.higt + textHeight-2,
                                                rectColor) //所有
                                    }
                                    if (rlefttop.get()){
                                        if(module==modules.get(modules.size - (modules.size))){
                                            RenderUtils.drawRect(xPos - 7, module.higt - 3, 0.0F, module.higt - 2,
                                                    rectColor)
                                        }
                                    }


                                }
                            }
                        }
                    }
                }
            }

            Horizontal.LEFT -> {
                modules.forEachIndexed { index, module ->
                    var displayString = if (!tags.get())
                        module.arrayListName
                    else if (tagsArrayColor.get())
                        module.colorlessTagName
                    else module.tagName

                    if (upperCaseValue.get())
                        displayString = displayString.toUpperCase()

                    val width = fontRenderer.getStringWidth(displayString)
                    val xPos = -(width - module.slide) + if (rectMode.equals("left", true)) 5 else 2
                    val yPos = (if (side.vertical == Vertical.DOWN) -textSpacer else textSpacer) *
                            if (side.vertical == Vertical.DOWN) index + 1 else index
                    val moduleColor = Color.getHSBColor(module.hue, saturation, brightness).rgb
                    val LiquidSlowly = ColorUtils.LiquidSlowly(System.nanoTime(), index * RianbowspeedValue.get(), Rsaturation, Rbrightness)?.rgb
                    val c: Int = LiquidSlowly!!
                    val col = Color(c)
                    val braibow = Color(col.green/randowRedValue.get()+randow2RedValue.get(), col.green /randowGreenValue.get()+randow2GreenValue.get(), col.green /randowBlueValue.get()+randow2BlueValue.get()).rgb

                    val backgroundRectRainbow = backgroundColorMode.equals("Rainbow", ignoreCase = true)
                    val size = modules.size * 2.0E-2f
                    if (module.state) {
                        if (module.higt < yPos) {
                            module.higt += (size -
                                    Math.min(module.higt * 0.002f
                                            , size - (module.higt * 0.0001f) )) * delta
                            module.higt = Math.min(yPos, module.higt)
                        } else {
                            module.higt -= (size -
                                    Math.min(module.higt * 0.002f
                                            , size - (module.higt * 0.0001f) )) * delta
                            module.higt = Math.max(module.higt, yPos)
                        }
                    }

                    RainbowShader.begin(backgroundRectRainbow, if (rainbowX.get() == 0.0F) 0.0F else 1.0F / rainbowX.get(), if (rainbowY.get() == 0.0F) 0.0F else 1.0F / rainbowY.get(), System.currentTimeMillis() % 10000 / 10000F).use {
                        RenderUtils.drawRect(
                                0F,
                                module.higt,
                                xPos + width + if (rectMode.equals("right", true)) 5 else 2,
                                module.higt + textHeight, when {
                            backgroundRectRainbow -> 0
                            backgroundColorMode.equals("Random", ignoreCase = true) -> moduleColor
                            backgroundColorMode.equals("OtherRainbow", ignoreCase = true) -> Colors.getRainbow(-2000, (yPos * 8.toFloat()).toInt())
                            backgroundColorMode.equals("ALLRainbow", ignoreCase = true) -> ColorUtils.ALLColor(400000000L * index).rgb
                            backgroundColorMode.equals("Bainbow", ignoreCase = true) -> braibow
                            backgroundColorMode.equals("TwoRainbow", ignoreCase = true) -> ColorUtils.TwoRainbow(400000000L * index,TwoRainbow.get()).rgb
                            backgroundColorMode.equals("OriginalRainbow", ignoreCase = true) -> ColorUtils.originalrainbow(400000000L * index).rgb
                            else -> backgroundCustomColor
                        }
                        )
                    }

                    val rainbow = colorMode.equals("Rainbow", ignoreCase = true)

                    RainbowFontShader.begin(rainbow, if (rainbowX.get() == 0.0F) 0.0F else 1.0F / rainbowX.get(), if (rainbowY.get() == 0.0F) 0.0F else 1.0F / rainbowY.get(), System.currentTimeMillis() % 10000 / 10000F).use {
                        fontRenderer.drawString(displayString, xPos, module.higt + textY, when {
                            rainbow -> 0
                            colorMode.equals("Random", ignoreCase = true) -> moduleColor
                            colorMode.equals("OtherRainbow", ignoreCase = true) -> Colors.getRainbow(-2000, (yPos * 8.toFloat()).toInt())
                            colorMode.equals("ALLRainbow", ignoreCase = true) -> ColorUtils.ALLColor(400000000L * index).rgb
                            colorMode.equals("Bainbow", ignoreCase = true) -> braibow
                            colorMode.equals("TwoRainbow", ignoreCase = true) -> ColorUtils.TwoRainbow(400000000L * index,TwoRainbow.get()).rgb
                            colorMode.equals("OriginalRainbow", ignoreCase = true) -> ColorUtils.originalrainbow(400000000L * index).rgb
                            else -> customColor
                        }, textShadow)
                    }

                    val rectColorRainbow = rectColorMode.equals("Rainbow", ignoreCase = true)

                    RainbowShader.begin(rectColorRainbow, if (rainbowX.get() == 0.0F) 0.0F else 1.0F / rainbowX.get(), if (rainbowY.get() == 0.0F) 0.0F else 1.0F / rainbowY.get(), System.currentTimeMillis() % 10000 / 10000F).use {
                        if (!rectMode.equals("none", true)) {
                            val rectColor = when {
                                rectColorRainbow -> 0
                                rectColorMode.equals("Random", ignoreCase = true) -> moduleColor
                                rectColorMode.equals("OtherRainbow", ignoreCase = true) -> Colors.getRainbow(-2000, (yPos * 8.toFloat()).toInt())
                                rectColorMode.equals("ALLRainbow", ignoreCase = true) -> ColorUtils.ALLColor(400000000L * index).rgb
                                rectColorMode.equals("Bainbow", ignoreCase = true) -> braibow
                                rectColorMode.equals("TwoRainbow", ignoreCase = true) -> ColorUtils.TwoRainbow(400000000L * index,TwoRainbow.get()).rgb
                                rectColorMode.equals("OriginalRainbow", ignoreCase = true) -> ColorUtils.originalrainbow(400000000L * index).rgb
                                else -> rectCustomColor
                            }

                            when {
                                rectMode.equals("left", true) -> RenderUtils.drawRect(0F,
                                        module.higt - 1, 3F, module.higt + textHeight, rectColor)
                                rectMode.equals("right", true) ->
                                    RenderUtils.drawRect(xPos + width + 2, module.higt, xPos + width + 2 + 3,
                                            module.higt + textHeight, rectColor)
                            }
                        }
                    }
                }
            }
        }

        // Draw border
        if (mc.currentScreen is GuiHudDesigner) {
            x2 = Int.MIN_VALUE

            if (modules.isEmpty()) {
                return if (side.horizontal == Horizontal.LEFT)
                    Border(0F, -1F, 20F, 20F)
                else
                    Border(0F, -1F, -20F, 20F)
            }

            for (module in modules) {
                when (side.horizontal) {
                    Horizontal.RIGHT, Horizontal.MIDDLE -> {
                        val xPos = -module.slide.toInt() - 2
                        if (x2 == Int.MIN_VALUE || xPos < x2) x2 = xPos
                    }
                    Horizontal.LEFT -> {
                        val xPos = module.slide.toInt() + 14
                        if (x2 == Int.MIN_VALUE || xPos > x2) x2 = xPos
                    }
                }
            }
            y2 = (if (side.vertical == Vertical.DOWN) -textSpacer else textSpacer) * modules.size

            return Border(0F, 0F, x2 - 7F, y2 - if (side.vertical == Vertical.DOWN) 1F else 0F)
        }

        AWTFontRenderer.assumeNonVolatile = false
        GlStateManager.resetColor()
        return null
    }

    override fun updateElement() {
        modules = LiquidBounce.moduleManager.modules
                .filter { it.array && it.slide > 0 }
                .sortedBy { -fontValue.get().getStringWidth(if (upperCaseValue.get()) (if (!tags.get())
                    it.arrayListName else if (tagsArrayColor.get()) it.colorlessTagName else it.tagName).toUpperCase()
                else if (!tags.get()) it.arrayListName else if (tagsArrayColor.get()) it.colorlessTagName else it.tagName) }
    }
}