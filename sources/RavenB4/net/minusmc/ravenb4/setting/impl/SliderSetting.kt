package net.minusmc.ravenb4.setting.impl

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minusmc.ravenb4.setting.Setting
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.max
import kotlin.math.min


class SliderSetting(name: String, value: Double, val minimum: Double, val maximum: Double, val intervals: Double): Setting<Double>(name, value) {
    override val settingType = "slider"

    override fun getConfigAsJson(): JsonElement {
        val data = JsonObject()
        data.addProperty("type", settingType)
        data.addProperty("value", round2(value, 2))
        return data
    }

    override fun applyConfigFromJson(data: JsonObject) {
        if(!data.get("type").asString.equals(settingType)) return
        set(data.get("value").asDouble)
    }

    private fun round2(value: Double, precision: Int): Double {
        if (precision < 0) return 0.0
        var bd = BigDecimal(value)
        bd = bd.setScale(precision, RoundingMode.HALF_UP)
        return bd.toDouble()
    }

    override fun get() = round2(value, 2)

    override fun set(newValue: Double) {
        var newValue = clampDouble(newValue, minimum, maximum)
        newValue = Math.round(newValue * (1.0 / intervals)).toDouble() / (1.0 / intervals)
        value = newValue
    }

    private fun clampDouble(value: Double, min2: Double, max2: Double): Double {
        var value = max(min2, value)
        value = min(max2, value)
        return value
    }

}