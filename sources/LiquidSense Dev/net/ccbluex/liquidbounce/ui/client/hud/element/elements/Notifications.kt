package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.FontValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import org.lwjgl.opengl.GL11
import java.awt.Color

/**
 * CustomHUD Notification element
 */
@ElementInfo(name = "Notifications", single = true)
class Notifications(
        x: Double = 0.0, y: Double = 30.0, scale: Float = 1F,
        side: Side = Side(Side.Horizontal.RIGHT, Side.Vertical.DOWN)
) : Element(x, y, scale, side) {

    /**
     * Example notification for CustomHUD designer
     */
    private val exampleNotification = Notification("Example Notification" , "")

    private val colorModeValue: ListValue = object : ListValue("Text-Color", arrayOf("Custom", "Rainbow"), "Custom") {
        override fun changeElement() {
            colorRedValue.isSupported =
                    (get() == "Custom").also { colorGreenValue.isSupported = it; colorBlueValue.isSupported = true }
        }
    }

    private val colorRedValue = IntegerValue("Text-R", 0, 0, 255)
    private val colorGreenValue = IntegerValue("Text-G", 111, 0, 255)
    private val colorBlueValue = IntegerValue("Text-B", 255, 0, 255)


    private val rectColorModeValue: ListValue =
            object : ListValue("Rect-Color", arrayOf("Custom", "Rainbow"), "Rainbow") {
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

    private val backgroundColorModeValue: ListValue =
            object : ListValue("Background-Color", arrayOf("Custom", "Rainbow"), "Custom") {
                override fun changeElement() {
                    backgroundColorRedValue.isSupported = get().equals("Custom").also {
                        backgroundColorGreenValue.isSupported = it
                        backgroundColorBlueValue.isSupported = it
                        backgroundColorAlphaValue.isSupported = it
                    }
                }
            }

    private val backgroundColorRedValue = IntegerValue("Background-R", 0, 0, 255)
    private val backgroundColorGreenValue = IntegerValue("Background-G", 0, 0, 255)
    private val backgroundColorBlueValue = IntegerValue("Background-B", 0, 0, 255)
    private val backgroundColorAlphaValue = IntegerValue("Background-Alpha", 0, 0, 255)
   // val description = ListValue("Description",arrayOf("None","LiquidSense","Descript"),"LiquidSense")

    private val fontValue = FontValue("Font", Fonts.font40)
    /**
     * Draw element
     */
/*
    fun getModeValue(): String {
        when{
            Notifications().description.get().equals("none", ignoreCase = true) -> return ""
            Notifications().description.get().equals("liquidSense", ignoreCase = true) -> return " LiquidSense"
            Notifications().description.get().equals("descript", ignoreCase = true) -> return " "+ Module().description
            else -> return ""
        }

    }

 */

    override fun drawElement(): Border? {
        val colorMode = colorModeValue.get()
        val rectColorMode = rectColorModeValue.get()
        val backgroundColorMode = backgroundColorModeValue.get()
        val customColor = Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get(), 1).rgb
        val rectCustomColor = Color(
                rectColorRedValue.get(), rectColorGreenValue.get(), rectColorBlueValue.get(),
                rectColorBlueAlpha.get()
        ).rgb

        val backgroundCustomColor = Color(
                backgroundColorRedValue.get(), backgroundColorGreenValue.get(),
                backgroundColorBlueValue.get(), backgroundColorAlphaValue.get()
        ).rgb
        if (LiquidBounce.hud.notifications.size > 0) {
            for (i in LiquidBounce.hud.notifications) {
                i.backgroundColor = when {
                    backgroundColorMode.equals("Rainbow", ignoreCase = true) -> ColorUtils.rainbow().rgb
                    else -> backgroundCustomColor
                }

                i.colorMode = when {
                    colorMode.equals("Rainbow", ignoreCase = true) -> ColorUtils.rainbow().rgb
                    else -> customColor
                }

                i.rectColorMode = when {
                    rectColorMode.equals("Rainbow", ignoreCase = true) -> ColorUtils.rainbow().rgb
                    else -> rectCustomColor
                }
                i.font = fontValue

            }
            LiquidBounce.hud.notifications[0].drawNotification()
        }


        if (LiquidBounce.hud.notifications.size > 20) {
            for (i in 0..LiquidBounce.hud.notifications.size) {
                LiquidBounce.hud.removeNotification(LiquidBounce.hud.notifications[0])
            }
        }

        if (mc.currentScreen is GuiHudDesigner) {
            if (!LiquidBounce.hud.notifications.contains(exampleNotification))
                LiquidBounce.hud.addNotification(exampleNotification)

            exampleNotification.fadeState = Notification.FadeState.STAY
            exampleNotification.x = exampleNotification.textLength + 8F

            return Border(-95F, -20F, 0F, 0F)
        }

        return null
    }

}

class Notification(private val message: String, private val message2: String) {
    var x = 0F
    var y = 0F
    var textLength = 0
    private var stay = 0F
    var fadeState = FadeState.IN
    var backgroundColor = 0
    var colorMode = 0
    var rectColorMode = 0
    var ElementX = 0.0
    var ElementY = 0.0
    lateinit var font : FontValue


    /**
     * Fade state for animation
     */
    enum class FadeState { IN, STAY, OUT, END }


    /**
     * Draw notification
     */
    fun drawNotification() {
        textLength = font.get().getStringWidth(message) +  font.get().getStringWidth(message2)

        // Animation
        val delta = RenderUtils.deltaTime
        val width = textLength + 8F

        // Draw notification
        RenderUtils.drawRect(-x + 8 + textLength, 0f, -x, -20F, backgroundColor)
        RenderUtils.drawRect(-x, 0f, -x - 5, -20F, rectColorMode)
        font.get().drawString(message + message2, (-x + 4).toInt(), (-18F).toInt(), colorMode)
        if (LiquidBounce.hud.notifications.size > 1) {
            font.get().drawString("${LiquidBounce.hud.notifications.size - 1} More", (-x + 4).toInt(), (-8F).toInt(), Int.MAX_VALUE)
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)

        when (fadeState) {
            FadeState.IN -> {
                if (x < width) {
                    x += 1.0f * delta
                }
                if (x >= width) {
                    fadeState = FadeState.STAY
                    x = width
                }
                stay = 500f
            }

            FadeState.STAY -> if (stay > 0)
                stay -= 2f
            else if (stay == 0f)
                fadeState = FadeState.OUT

            FadeState.OUT -> if (x > 0) {
                x -= 1.0f * delta
            } else
                fadeState = FadeState.END

            FadeState.END -> LiquidBounce.hud.removeNotification(this)
        }
    }
}