package me.offeex.bloomware.api.structure

import me.offeex.bloomware.api.helper.ProtectionMark
import net.minecraft.util.math.Vec3d
import java.awt.Transparency
import java.util.function.Consumer
import kotlin.math.floor
import kotlin.random.Random

class ColorMutable {
    var argb: Int
        private set
    private val listeners = mutableListOf<Runnable>()

    @JvmOverloads
    constructor(r: Int, g: Int, b: Int, a: Int = 255) {
        argb = a and 0xFF shl 24 or (r and 0xFF shl 16) or (g and 0xFF shl 8) or (b and 0xFF)
    }

    constructor(rgba: Long) {
        argb = rgba.toInt()
    }

    constructor(rgb: Int) {
        this.argb = rgb
    }

    constructor(hex: String) {
        val i = Integer.decode(hex)
        argb = 0xFF shl 24 or (i shr 16 and 0xFF shl 16) or (i shr 8 and 0xFF shl 8) or (i and 0xFF)
    }

    val red: Int
        get() = argb shr 16 and 0xFF
    val green: Int
        get() = argb shr 8 and 0xFF
    val blue: Int
        get() = argb and 0xFF
    val alpha: Int
        get() = argb shr 24 and 0xff
    val vec3d: Vec3d
        get() = Vec3d(red / 255.0, green / 255.0, blue / 255.0)



    fun setColor(value: Int) {
        argb = value
        listeners.forEach(Consumer { obj: Runnable -> obj.run() })
    }

    fun setColor(r: Int, g: Int, b: Int, a: Int) {
        argb = a and 0xFF shl 24 or (r and 0xFF shl 16) or (g and 0xFF shl 8) or (b and 0xFF)
        listeners.forEach(Consumer { obj: Runnable -> obj.run() })
    }

    fun setColorSilent(value: Int) {
        argb = value
    }

    fun setAlphaSilent(alpha: Float) {
        argb = argb and 0x00FFFFFF or (255f.coerceAtMost(4f.coerceAtLeast(alpha * 255)).toInt() and 0xFF shl 24)
    }

    fun setAlpha(alpha: Float): Int {
        setAlphaSilent(alpha)
        listeners.forEach(Consumer { obj: Runnable -> obj.run() })
        return argb
    }



    fun withAlphaToRGBA(transparency: Int) = transparency and 0xFF shl 24 or (red shl 16) or (green shl 8) or blue

    fun modifyBrightnessToRGBA(value: Int): Int {
        return alpha shl 24 or
                (0.coerceAtLeast(red - value) shl 16) or
                (0.coerceAtLeast(green - value) shl 8) or
                0.coerceAtLeast(blue - value)
    }

    fun modifyToRGBA(r: Int, g: Int, b: Int, a: Int): Int {
        return (if (a == -1) alpha else a) and 0xFF shl 24 or
                ((if (r == -1) red else r) and 0xFF shl 16) or
                ((if (g == -1) green else g) and 0xFF shl 8) or
                ((if (b == -1) blue else b) and 0xFF)
    }

    override fun equals(other: Any?): Boolean {
        return other is ColorMutable && other.argb == argb
    }

    override fun toString(): String {
        return javaClass.name + "[r=" + red + ",g=" + green + ",b=" + blue + "]"
    }

    // IDE-generated
    override fun hashCode(): Int {
        var result = argb
        result = 31 * result + listeners.hashCode()
        return result
    }



    fun toHexString(): String {
        return Integer.toHexString(argb)
    }

    val transparency: Int
        get() {
            return when (alpha) {
                0xff -> Transparency.OPAQUE
                0 -> Transparency.BITMASK
                else -> Transparency.TRANSLUCENT
            }
        }

    fun onUpdate(listener: Runnable) = listeners.add(listener)

    companion object {
        val WHITE get() = ColorMutable(255, 255, 255)
        val BLACK get() = ColorMutable(0, 0, 0)
        val EMPTY get() = ColorMutable(0, 0, 0, 0)
        val YELLOW get() = ColorMutable(255, 230, 0, 255)
        val ORANGE get() = ColorMutable(255, 165, 0, 255)
        val BROWN get() = ColorMutable(165, 42, 42, 255)
        val GRAY get() = ColorMutable(128, 128, 128, 255)
        val GREEN get() = ColorMutable(0, 255, 0, 255)
        val RED get() = ColorMutable(255, 0, 0, 255)
        val PURPLE get() = ColorMutable(255, 0, 255, 255)
        val AQUA get() = ColorMutable(0, 255, 255, 255)
        val BLUE get() = ColorMutable(0, 0, 255, 255)
        val DARK_GRAY get() = ColorMutable(26, 26, 26, 255)

        fun getColor(nm: String, v: ColorMutable): ColorMutable {
            val intval = Integer.getInteger(nm) ?: return v
            return ColorMutable(intval shr 16 and 0xFF, intval shr 8 and 0xFF, intval and 0xFF)
        }
        fun getColor(nm: String, v: Int): ColorMutable {
            val i = Integer.getInteger(nm) ?: v
            return ColorMutable(i shr 16 and 0xFF, i shr 8 and 0xFF, i and 0xFF)
        }

        fun HSBtoColor(hsb: FloatArray) = ColorMutable(HSBtoRGB(hsb))
        fun HSBtoColor(h: Float, s: Float, b: Float) = ColorMutable(HSBtoRGB(h, s, b))

        fun HSBtoRGB(hsb: FloatArray) = HSBtoRGB(hsb[0], hsb[1], hsb[2])
        fun HSBtoRGB(hue: Float, saturation: Float, brightness: Float): Int {
            var r = 0
            var g = 0
            var b = 0
            if (saturation == 0f) {
                b = (brightness * 255.0f + 0.5f).toInt()
                g = b
                r = g
            } else {
                val h = (hue - floor(hue.toDouble()).toFloat()) * 6.0f
                val f = h - floor(h.toDouble()).toFloat()
                val p = brightness * (1.0f - saturation)
                val q = brightness * (1.0f - saturation * f)
                val t = brightness * (1.0f - saturation * (1.0f - f))
                when (h.toInt()) {
                    0 -> {
                        r = (brightness * 255.0f + 0.5f).toInt()
                        g = (t * 255.0f + 0.5f).toInt()
                        b = (p * 255.0f + 0.5f).toInt()
                    }
                    1 -> {
                        r = (q * 255.0f + 0.5f).toInt()
                        g = (brightness * 255.0f + 0.5f).toInt()
                        b = (p * 255.0f + 0.5f).toInt()
                    }
                    2 -> {
                        r = (p * 255.0f + 0.5f).toInt()
                        g = (brightness * 255.0f + 0.5f).toInt()
                        b = (t * 255.0f + 0.5f).toInt()
                    }
                    3 -> {
                        r = (p * 255.0f + 0.5f).toInt()
                        g = (q * 255.0f + 0.5f).toInt()
                        b = (brightness * 255.0f + 0.5f).toInt()
                    }
                    4 -> {
                        r = (t * 255.0f + 0.5f).toInt()
                        g = (p * 255.0f + 0.5f).toInt()
                        b = (brightness * 255.0f + 0.5f).toInt()
                    }
                    5 -> {
                        r = (brightness * 255.0f + 0.5f).toInt()
                        g = (p * 255.0f + 0.5f).toInt()
                        b = (q * 255.0f + 0.5f).toInt()
                    }
                }
            }
            return -0x1000000 or (r shl 16) or (g shl 8) or b
        }

        fun RGBtoHSB(color: ColorMutable, hsbvals: FloatArray) = RGBtoHSB(color.red, color.green, color.blue, hsbvals)
        fun RGBtoHSB(r: Int, g: Int, b: Int, hsbvalz: FloatArray?): FloatArray {
            var hsbvals = hsbvalz
            var hue: Float
            val saturation: Float
            val brightness: Float

            if (hsbvals == null) hsbvals = FloatArray(3)

            var cmax = r.coerceAtLeast(g)
            if (b > cmax) cmax = b
            var cmin = r.coerceAtMost(g)
            if (b < cmin) cmin = b

            brightness = cmax.toFloat() / 255.0f
            saturation = if (cmax != 0) (cmax - cmin).toFloat() / cmax.toFloat() else 0f

            if (saturation == 0f) hue = 0f else {
                val red = (cmax - r).toFloat() / (cmax - cmin).toFloat()
                val green = (cmax - g).toFloat() / (cmax - cmin).toFloat()
                val blue = (cmax - b).toFloat() / (cmax - cmin).toFloat()

                hue = if (r == cmax) blue - green else if (g == cmax) 2.0f + red - blue else 4.0f + green - red
                hue /= 6.0f

                if (hue < 0) hue += 1.0f
            }

            hsbvals[0] = hue
            hsbvals[1] = saturation
            hsbvals[2] = brightness

            return hsbvals
        }

        @ProtectionMark
        fun protection() {}

        fun random() = ColorMutable(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))

        fun getTransparency(color: Int): Int = color and 0x00ffffff or (1 shl 24)
    }
}