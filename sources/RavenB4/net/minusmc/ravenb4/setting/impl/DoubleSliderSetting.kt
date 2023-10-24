package net.minusmc.ravenb4.setting.impl

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minusmc.ravenb4.setting.Setting
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.max
import kotlin.math.min

class MinMaxDouble(minValue: Double, maxValue: Double)

class DoubleSliderSetting(
    name: String, var minValue: Double, var maxValue: Double, 
    val minimum: Double, val maximum: Double, val intervals: Double
): Setting<MinMaxDouble>(name, MinMaxDouble(minValue, maxValue)) {

    override val settingType = "doubleslider"

    private val minDefaultValue = minValue
    private val maxDefaultValue = maxValue

    override fun resetToDefaults() {
        maxValue = maxDefaultValue
        minValue = minDefaultValue
    }

    override fun getConfigAsJson(): JsonElement {
        val data = JsonObject()
        data.addProperty("type", settingType)
        data.addProperty("valueMax", round2(maxValue, 2))
        data.addProperty("valueMin", round2(minValue, 2))
        return data
    }

    override fun applyConfigFromJson(data: JsonObject) {
        if(!data.get("type").asString.equals(settingType)) return

        setMaxValue(data.get("valueMax").asDouble)
        setMinValue(data.get("valueMin").asDouble)
    }

    fun getMinValue() = round2(minValue, 2)

    fun getMaxValue() = round2(maxValue, 2)

    fun setMinValue(newValue: Double) {
        var newValue = clampDouble(newValue, minimum, maximum)
        newValue = Math.round(newValue * (1.0 / intervals)).toDouble() / (1.0 / intervals)
        this.minValue = newValue
    }

    fun setMaxValue(newValue: Double) {
        var newValue = clampDouble(newValue, minimum, maximum)
        newValue = Math.round(newValue * (1.0 / intervals)).toDouble() / (1.0 / intervals)
        this.maxValue = newValue
    }

    private fun clampDouble(value: Double, min2: Double, max2: Double): Double {
        var value = max(min2, value)
        value = min(max2, value)
        return value
    }

    private fun round2(value: Double, precision: Int): Double {
        if (precision < 0) return 0.0
        var bd = BigDecimal(value)
        bd = bd.setScale(precision, RoundingMode.HALF_UP)
        return bd.toDouble()
    }

}