/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.utils.render

import net.minecraft.client.Minecraft
import net.minecraft.util.ChatAllowedCharacters
import java.awt.Color
import java.util.*
import java.util.regex.Pattern

object ColorUtils {

    private val COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]")

    @JvmField
    val hexColors = IntArray(16)

    init {
        repeat(16) { i ->
            val baseColor = (i shr 3 and 1) * 85

            val red = (i shr 2 and 1) * 170 + baseColor + if (i == 6) 85 else 0
            val green = (i shr 1 and 1) * 170 + baseColor
            val blue = (i and 1) * 170 + baseColor

            hexColors[i] = red and 255 shl 16 or (green and 255 shl 8) or (blue and 255)
        }
    }

    @JvmStatic
    fun stripColor(input: String?): String? {
        return COLOR_PATTERN.matcher(input ?: return null).replaceAll("")
    }

    @JvmStatic
    fun translateAlternateColorCodes(textToTranslate: String): String {
        val chars = textToTranslate.toCharArray()

        for (i in 0 until chars.size - 1) {
            if (chars[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".contains(chars[i + 1], true)) {
                chars[i] = '§'
                chars[i + 1] = Character.toLowerCase(chars[i + 1])
            }
        }

        return String(chars)
    }

    fun randomMagicText(text: String): String {
        val stringBuilder = StringBuilder()
        val allowedCharacters = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000"

        for (c in text.toCharArray()) {
            if (ChatAllowedCharacters.isAllowedCharacter(c)) {
                val index = Random().nextInt(allowedCharacters.length)
                stringBuilder.append(allowedCharacters.toCharArray()[index])
            }
        }

        return stringBuilder.toString()
    }

    @JvmStatic
    fun rainbow(): Color {
        val currentColor = Color(Color.HSBtoRGB((System.nanoTime() + 400000L) / 10000000000F % 1,0.4F, 0.9F))
        return Color(currentColor.red / 255F * 1F, currentColor.green / 255f * 1F, currentColor.blue / 255F * 1F, currentColor.alpha / 255F)
    }

    // TODO: Use kotlin optional argument feature

    @JvmStatic
    fun rainbow(offset: Long): Color {
        val currentColor = Color(Color.HSBtoRGB((System.nanoTime() + offset) / 10000000000F % 1, 0.4F, 0.9F))
        return Color(currentColor.red / 255F * 1F, currentColor.green / 255F * 1F, currentColor.blue / 255F * 1F,
                currentColor.alpha / 255F)
    }

    @JvmStatic
    fun ALLColor(offset: Long): Color {
        val currentColor = Color(Color.HSBtoRGB((Minecraft.getMinecraft().thePlayer.ticksExisted / 50.0 + Math.sin(50 / 50.0 * 1.6) % 1).toFloat(), 0.4F, 0.9F))

        return Color(currentColor.rgb)
    }

    @JvmStatic
    fun originalrainbow(offset: Long): Color {
        val currentColor = Color(Color.HSBtoRGB((System.nanoTime() + offset) / 10000000000F % 1, 1.0F, 1.0F))
        return Color(currentColor.red / 255F * 1F, currentColor.green / 255F * 1F, currentColor.blue / 255F * 1F,
                currentColor.alpha / 255F)
    }

    @JvmStatic
    fun LiquidSlowly(time: Long, count: Int, qd: Float, sq: Float): Color? {
        val color = Color(Color.HSBtoRGB((time.toFloat() + count * -3000000f) / 2 / 1.0E9f, qd, sq))
        return Color(color.red / 255.0f * 1, color.green / 255.0f * 1, color.blue / 255.0f * 1, color.alpha / 255.0f)
    }

    @JvmStatic
    fun NewRainbow(var2: Int, bright: Float, st: Float): Int {
        var v1 = Math.ceil(System.currentTimeMillis() + (var2 * 109).toDouble()) / 5
        return Color.getHSBColor(if ((360.0.also { v1 %= it } / 360.0) < 0.5) -(v1 / 360.0).toFloat() else (v1 / 360.0).toFloat(), st, bright).rgb
    }

    fun rainbows(time: Long, count: Int, fade: Float, qd: Float, sq: Float): Color? {
        val color = Color(Color.HSBtoRGB((time.toFloat() + (1.0f + count) * 2.0E8f) / 1.0E10f % 1.0f, qd, sq))
        return Color(color.red.toFloat() / 255.0f * fade, color.green.toFloat() / 255.0f * fade, color.blue.toFloat() / 255.0f * fade, color.alpha.toFloat() / 255.0f)
    }

    @JvmStatic
    fun rainbow(alpha: Float) = rainbow(400000L, alpha)

    @JvmStatic
    fun rainbow(alpha: Int) = rainbow(400000L, alpha / 255)

    @JvmStatic
    fun rainbow(offset: Long, alpha: Int) = rainbow(offset, alpha.toFloat() / 255)

    @JvmStatic
    fun rainbow(offset: Long, alpha: Float): Color {
        val currentColor = Color(Color.HSBtoRGB((System.nanoTime() + offset) / 10000000000F % 1, 0.4F, 0.9F))
        return Color(currentColor.red / 255F * 1F, currentColor.green / 255f * 1F, currentColor.blue / 255F * 1F, alpha)
    }

    @JvmStatic
    fun TwoRainbow(offset: Long,alpha: Float): Color {
        var currentColor = Color(Color.HSBtoRGB((System.nanoTime() + offset) / 8.9999999E10F % 1, 0.75F, 0.8F));
        return Color(currentColor.getRed() / 255.0F * 1.0F, currentColor.getGreen() / 255.0F * 1.0F, currentColor.getBlue() / 255.0F * 1.0F, alpha);
    }


}