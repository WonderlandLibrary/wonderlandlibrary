/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.CPSCounter
import net.ccbluex.liquidbounce.utils.EntityUtils
import net.ccbluex.liquidbounce.utils.ServerUtils
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.UiUtils
import net.ccbluex.liquidbounce.utils.render.shader.shaders.RainbowFontShader
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.*
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ChatAllowedCharacters
import org.lwjgl.input.Keyboard
import java.awt.Color
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import kotlin.math.sqrt

/**
 * CustomHUD text element
 *
 * Allows to draw custom text
 */
@ElementInfo(name = "Text")
class Text(x: Double = 10.0, y: Double = 10.0, scale: Float = 1F,
           side: Side = Side.default()) : Element(x, y, scale, side) {

    companion object {

        val startTime = System.currentTimeMillis()
        val TIMER_FORMAT = SimpleDateFormat("HH:mm:ss")
        val DATE_FORMAT = SimpleDateFormat("yy/MM/dd")
        val HOUR_FORMAT = SimpleDateFormat("HH:mm:ss")
        val Y_FORMAT = DecimalFormat("0.000000000")
        val DECIMAL_FORMAT = DecimalFormat("0.00")

        /**
         * Create default element
         */
        fun defaultClient(): Text {
            val text = Text(x = 2.0, y = 2.0, scale = 1F)

            text.displayString.set("%clientname%")
            text.shadow.set(true)
            text.fontValue.set(Fonts.minecraftFont)
            text.setColor(Color(200, 50, 50))
            return text
        }

    }


    private val displayString = TextValue("DisplayText", "")
    private val redValue = IntegerValue("Red", 255, 0, 255)
    private val greenValue = IntegerValue("Green", 255, 0, 255)
    private val blueValue = IntegerValue("Blue", 255, 0, 255)
    private val rainbow = BoolValue("Rainbow", false)
    private val rainbowX = FloatValue("Rainbow-X", -1000F, -2000F, 2000F)
    private val rainbowY = FloatValue("Rainbow-Y", -1000F, -2000F, 2000F)
    private val animation = BoolValue("Animation", false)
    private val animationDelay = FloatValue("AnimationDelay", 1F, 0.1F, 5F)
    private val shadow = BoolValue("Shadow", false)
    private val outline = BoolValue("Outline",false)
    private val rectMode =
            object : ListValue("RectMode", arrayOf("Custom", "OneTap", "Skeet","OnlyWhite","NeverLose"), "Skeet"){
                override fun changeElement() {
                    username.isSupported = (get() == "NeverLose").also {
                        ping.isSupported = it;fps.isSupported = it;ip.isSupported = it;time.isSupported = it;
                    }
                    redValue.isSupported = (get() != "NeverLose").also {
                        greenValue.isSupported = it;blueValue.isSupported = it;rainbow.isSupported = it;rainbowX.isSupported = it;rainbowY.isSupported = it;animation.isSupported = it;animationDelay.isSupported = it;outline.isSupported = it;shadow.isSupported = it;
                    }
                }
            }
    private var fontValue = FontValue("Font", Fonts.font40)
    private var username = BoolValue("Username",false)
    private var ping = BoolValue("Ping",false)
    private var fps = BoolValue("FPS",false)
    private var ip = BoolValue("IP",false)
    private var time = BoolValue("Time",false)


    private var editMode = false
    private var editTicks = 0
    private var prevClick = 0L
    private var lastTick = -1;

    private var displaySpeed = 0.0
    private var displayText = display

    private val display: String
        get() {
            val textContent = if (displayString.get().isEmpty() && !editMode)
                "Text Element"
            else
                displayString.get()


            return multiReplace(textContent)
        }

    private fun getReplacement(str: String): String? {
        if (mc.thePlayer != null) {
            when (str) {
                "x" -> return DECIMAL_FORMAT.format(mc.thePlayer.posX)
                "y" -> return Y_FORMAT.format(mc.thePlayer.posY)
                "z" -> return DECIMAL_FORMAT.format(mc.thePlayer.posZ)
                "xdp" -> return mc.thePlayer.posX.toString()
                "ydp" -> return mc.thePlayer.posY.toString()
                "zdp" -> return mc.thePlayer.posZ.toString()
                "velocity" -> return DECIMAL_FORMAT.format(sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ))
                "ping" -> return EntityUtils.getPing(mc.thePlayer).toString()
                "speed" -> return String.format("%.2f", displaySpeed) + "\u00a77b/s\u00a7r"
                "0" -> return "§0"
                "1" -> return "§1"
                "2" -> return "§2"
                "3" -> return "§3"
                "4" -> return "§4"
                "5" -> return "§5"
                "6" -> return "§6"
                "7" -> return "§7"
                "8" -> return "§8"
                "9" -> return "§9"
                "a" -> return "§a"
                "b" -> return "§b"
                "c" -> return "§c"
                "d" -> return "§d"
                "e" -> return "§e"
                "f" -> return "§f"
                "n" -> return "§n"
                "m" -> return "§m"
                "l" -> return "§l"
                "k" -> return "§k"
                "o" -> return "§o"
                "r" -> return "§r"
            }
        }

        return when (str) {
            "username" -> mc.getSession().username
            "clientname" -> LiquidBounce.CLIENT_NAME
            "clientversion" -> "b${LiquidBounce.CLIENT_VERSION}"
            "clientcreator" -> LiquidBounce.CLIENT_CREATOR
            "fps" -> Minecraft.getDebugFPS().toString()
            "date" -> DATE_FORMAT.format(System.currentTimeMillis())
            "time" -> HOUR_FORMAT.format(System.currentTimeMillis())
            "timer" -> TIMER_FORMAT.format(System.currentTimeMillis() - startTime - 8 * 60 * 60 * 1000)
            "serverip" -> ServerUtils.getRemoteIp()
            "cps", "lcps" -> return CPSCounter.getCPS(CPSCounter.MouseButton.LEFT).toString()
            "mcps" -> return CPSCounter.getCPS(CPSCounter.MouseButton.MIDDLE).toString()
            "rcps" -> return CPSCounter.getCPS(CPSCounter.MouseButton.RIGHT).toString()

            else -> null // Null = don't replace
        }
    }

    private fun multiReplace(str: String): String {
        var lastPercent = -1
        val result = StringBuilder()
        for (i in str.indices) {
            if (str[i] == '%') {
                if (lastPercent != -1) {
                    if (lastPercent + 1 != i) {
                        val replacement = getReplacement(str.substring(lastPercent + 1, i))

                        if (replacement != null) {
                            result.append(replacement)
                            lastPercent = -1
                            continue
                        }
                    }
                    result.append(str, lastPercent, i)
                }
                lastPercent = i
            } else if (lastPercent == -1) {
                result.append(str[i])
            }
        }

        if (lastPercent != -1) {
            result.append(str, lastPercent, str.length)
        }

        return result.toString()
    }

    /**
     * Draw element
     */
    override fun drawElement(): Border? {
        if (mc.thePlayer != null && lastTick != mc.thePlayer!!.ticksExisted) {
            lastTick = mc.thePlayer!!.ticksExisted
            val z2 = mc.thePlayer!!.posZ
            val z1 = mc.thePlayer!!.prevPosZ
            val x2 = mc.thePlayer!!.posX
            val x1 = mc.thePlayer!!.prevPosX
            var speed = sqrt((z2 - z1) * (z2 - z1) + (x2 - x1) * (x2 - x1))
            if (speed < 0)
                speed = -speed
            displaySpeed = speed * 20;
        }

        val color = Color(redValue.get(), greenValue.get(), blueValue.get()).rgb
        val colord = Color(redValue.get(), greenValue.get(), blueValue.get()).rgb+Color(0,0,0,50).rgb
        val fontRenderer = fontValue.get()

        when (this.rectMode.get().toLowerCase()) {
            "custom" -> {

            }
            "onetap" -> {
                RenderUtils.drawRect(-4.0f, -8.0f, (fontRenderer.getStringWidth(displayText) + 3).toFloat(), fontRenderer.FONT_HEIGHT.toFloat(), Color(43,43,43).rgb)
                RenderUtils.drawGradientSideways(-3.0, -7.0, (fontRenderer.getStringWidth(displayText) +2.0).toDouble(), -3.0, if (rainbow.get())
                    ColorUtils.rainbow(400000000L).rgb+Color(0,0,0,40).rgb else colord,if (rainbow.get())
                    ColorUtils.rainbow(400000000L).rgb else color)
            }
            "skeet" -> {
                UiUtils.drawRect(-11.0, -9.5, (fontRenderer.getStringWidth(displayText) + 9).toDouble(), fontRenderer.FONT_HEIGHT.toDouble()+6,Color(0,0,0).rgb)
                UiUtils.outlineRect(-10.0, -8.5, (fontRenderer.getStringWidth(displayText) + 8).toDouble(), fontRenderer.FONT_HEIGHT.toDouble()+5,8.0, Color(59,59,59).rgb,Color(59,59,59).rgb)
                UiUtils.outlineRect(-9.0, -7.5, (fontRenderer.getStringWidth(displayText) + 7).toDouble(), fontRenderer.FONT_HEIGHT.toDouble()+4,4.0, Color(59,59,59).rgb,Color(40,40,40).rgb)
                UiUtils.outlineRect(-4.0, -3.0, (fontRenderer.getStringWidth(displayText) + 2).toDouble(), fontRenderer.FONT_HEIGHT.toDouble()+0,1.0, Color(18,18,18).rgb,Color(0,0,0).rgb)
            }
            "onlywhite" ->{
                RenderUtils.drawRect(-2f,-2f,(fontRenderer.getStringWidth(displayText)+1).toFloat(),fontRenderer.FONT_HEIGHT.toFloat(),Color(0,0,0,150).rgb)

            }
            "neverlose" -> {
                RenderUtils.drawBorderedRect(-5.5F, -3.5F, (Fonts.font40.getStringWidth("NL    $displayText").toFloat() + 8.5F), Fonts.font40.FONT_HEIGHT.toFloat() + 0.5F, 3F, Color(16, 25, 32).rgb, Color(16, 25, 32).rgb)
                Fonts.font40.drawString("LS", -0.8F, -0.4F, Color(0, 149, 185).rgb, true)
                Fonts.font40.drawString("LS", -0.5F, -0.3F, Color(190, 252, 255).rgb, true)
                Fonts.font40.drawString("LS", -0.2F, -0.2F, Color(190, 252, 255).rgb, true)
                Fonts.font40.drawString("LS", 0F, 0F, Color(247, 255, 255).rgb, true)
                var index = "LS"
                val list = displayText.split(" ")
                for ((count, text: String) in list.withIndex()) {
                    Fonts.font40.drawString(text, Fonts.font40.getStringWidth(index).toFloat(), 0F, Color(155, 155, 155).rgb, false)
                    if (count + 1 == list.size) break
                    Fonts.font40.drawString("|", Fonts.font40.getStringWidth(index + text).toFloat() + 1.5F, 0F, Color(6, 32, 55).rgb, false)
                    index += "$text " + " "
                }
            }

        }

        if(this.outline.get() && !rectMode.get().equals("neverlose", true)){
            GlStateManager.resetColor()
            fontRenderer.drawString(displayText, (fontRenderer.getStringWidth(displayText) - fontRenderer.getStringWidth(displayText) - 1.0f).toInt(), fontRenderer.FONT_HEIGHT - fontRenderer.FONT_HEIGHT, Color.BLACK.rgb)
            fontRenderer.drawString(displayText, (fontRenderer.getStringWidth(displayText) - fontRenderer.getStringWidth(displayText) + 1.0f).toInt(), fontRenderer.FONT_HEIGHT - fontRenderer.FONT_HEIGHT, Color.BLACK.rgb)
            fontRenderer.drawString(displayText, fontRenderer.getStringWidth(displayText) - fontRenderer.getStringWidth(displayText), (fontRenderer.FONT_HEIGHT - fontRenderer.FONT_HEIGHT + 1.0f).toInt(), Color.BLACK.rgb)
            fontRenderer.drawString(displayText, fontRenderer.getStringWidth(displayText) - fontRenderer.getStringWidth(displayText), (fontRenderer.FONT_HEIGHT - fontRenderer.FONT_HEIGHT - 1.0f).toInt(), Color.BLACK.rgb)
            fontRenderer.drawString(displayText, fontRenderer.getStringWidth(displayText) - fontRenderer.getStringWidth(displayText), fontRenderer.FONT_HEIGHT - fontRenderer.FONT_HEIGHT, 0)
        }

        val rainbow = rainbow.get()
        RainbowFontShader.begin(rainbow, if (rainbowX.get() == 0.0F) 0.0F else 1.0F / rainbowX.get(), if (rainbowY.get() == 0.0F) 0.0F else 1.0F / rainbowY.get(), System.currentTimeMillis() % 10000 / 10000F).use {
            if (!rectMode.get().equals("neverlose", true))
                fontRenderer.drawString(displayText, 0F, 0F, if (rainbow)
                0 else color, shadow.get())

            if (editMode && mc.currentScreen is GuiHudDesigner && editTicks <= 40) {
                if (rectMode.get().equals("neverlose", true)) {
                    fontRenderer.drawString("_", fontRenderer.getStringWidth(displayText) + 5F,
                            0F, if (rainbow) ColorUtils.rainbow(400000000L).rgb else color, shadow.get())
                } else {
                    fontRenderer.drawString("_", fontRenderer.getStringWidth(displayText) + 2F,
                            0F, if (rainbow) ColorUtils.rainbow(400000000L).rgb else color, shadow.get())
                }
            }
        }

        if (editMode && mc.currentScreen !is GuiHudDesigner) {
            editMode = false
            updateElement()
        }

        return Border(
                -2F,
                -2F,
                fontRenderer.getStringWidth(displayText) + 2F,
                fontRenderer.FONT_HEIGHT.toFloat()
        )
    }

    private var count = 0
    private var reverse = false;

    private var timer = MSTimer()



    override fun updateElement() {

        editTicks += 5
        if (editTicks > 80) editTicks = 0

        if (count < 0 || count > display.length) {
            count = 0;
            reverse = false
        }

        if (!editMode && animation.get() && !rectMode.get().equals("neverlose", true) && timer.hasTimePassed((animationDelay.get() * 1000).toLong())) {
            if (reverse) count -= 1 else count += 1
            if (count == display.length || count == 0) reverse = !reverse
            timer.reset()
        }

        val tempDisplayText = if (animation.get() && !rectMode.get().equals("neverlose", true)) display.substring(0, count) else display

        displayText = if (editMode) displayString.get() else tempDisplayText

    }


    override fun handleMouseClick(x: Double, y: Double, mouseButton: Int) {
        if (isInBorder(x, y) && mouseButton == 0) {
            if (System.currentTimeMillis() - prevClick <= 250L)
                editMode = true

            prevClick = System.currentTimeMillis()
        } else {
            editMode = false
        }
    }

    override fun handleKey(c: Char, keyCode: Int) {
        if (editMode && mc.currentScreen is GuiHudDesigner) {
            if (keyCode == Keyboard.KEY_BACK) {
                if (displayString.get().isNotEmpty())
                    displayString.set(displayString.get().substring(0, displayString.get().length - 1))

                updateElement()
                return
            }

            if (ChatAllowedCharacters.isAllowedCharacter(c) || c == '§')
                displayString.set(displayString.get() + c)

            updateElement()
        }
    }

    fun setColor(c: Color): Text {
        redValue.set(c.red)
        greenValue.set(c.green)
        blueValue.set(c.blue)
        return this
    }

}