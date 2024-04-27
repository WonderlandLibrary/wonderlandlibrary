/*
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package kevin.module

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import kevin.main.KevinClient
import java.util.*
import java.util.function.Supplier
import kotlin.reflect.KProperty

abstract class Value<T>(val name: String, protected var value: T, protected val isSupported: (() -> Boolean)? = null) {

    fun set(newValue: T) {
        if (newValue == value)
            return

        val oldValue = get()

        try {
            onChange(oldValue, newValue)
            changeValue(newValue)
            onChanged(oldValue, newValue)
            KevinClient.fileManager.saveConfig(KevinClient.fileManager.modulesConfig)
        } catch (e: Exception) {
            println("[ValueSystem ($name)]: ${e.javaClass.name} (${e.message}) [$oldValue >> $newValue]")
        }
    }

    open fun get() = value

    open fun changeValue(value: T) {
        this.value = value
    }

    open fun isSupported(): Boolean = isSupported?.invoke() ?: true

    abstract fun toJson(): JsonElement?
    abstract fun fromJson(element: JsonElement)

    protected open fun onChange(oldValue: T, newValue: T) {}
    protected open fun onChanged(oldValue: T, newValue: T) {}

    // Support for delegating values using the `by` keyword.
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        set(value)
    }
}

/**
 * Bool value represents a value with a boolean
 */
open class BooleanValue @JvmOverloads constructor(name: String, value: Boolean, isSupported: (() -> Boolean)? = null) : Value<Boolean>(name, value, isSupported) {

    override fun toJson() = JsonPrimitive(value)

    override fun fromJson(element: JsonElement) {
        if (element.isJsonPrimitive)
            value = element.asBoolean || element.asString.equals("true", ignoreCase = true)
    }

    override fun get(): Boolean = isSupported() && super.get()
}

/**
 * Integer value represents a value with a integer
 */
open class IntegerValue(name: String, value: Int, val minimum: Int = 0, val maximum: Int = Integer.MAX_VALUE, isSupported: (() -> Boolean)? = null)
    : Value<Int>(name, value, isSupported) {

    fun set(newValue: Number) {
        set(newValue.toInt())
    }

    override fun toJson() = JsonPrimitive(value)

    override fun fromJson(element: JsonElement) {
        if (element.isJsonPrimitive)
            value = element.asInt
    }

    constructor(name: String, value: Int, range: IntRange, isSupported: (() -> Boolean)? = null) : this(name, value, range.first, range.last, isSupported)
    constructor(name: String, value: Int, minimum: Int = 0, maximum: Int = Integer.MAX_VALUE) : this(name, value, minimum, maximum, null) // provide to java
}

/**
 * Float value represents a value with a float
 */
open class FloatValue(name: String, value: Float, val minimum: Float = 0F, val maximum: Float = Float.MAX_VALUE, isSupported: (() -> Boolean)? = null)
    : Value<Float>(name, value, isSupported) {

    fun set(newValue: Number) {
        set(newValue.toFloat())
    }

    override fun toJson() = JsonPrimitive(value)

    override fun fromJson(element: JsonElement) {
        if (element.isJsonPrimitive)
            value = element.asFloat
    }

    constructor(name: String, value: Float, range: ClosedFloatingPointRange<Float>) : this(name, value, range.start, range.endInclusive)
    constructor(name: String, value: Float, minimum: Float = 0F, maximum: Float = Float.MAX_VALUE) : this(name, value, minimum, maximum, null)
}

/**
 * Text value represents a value with a string
 */
open class TextValue @JvmOverloads constructor(name: String, value: String, isSupported: (() -> Boolean)? = null) : Value<String>(name, value, isSupported) {

    override fun toJson() = JsonPrimitive(value)

    override fun fromJson(element: JsonElement) {
        if (element.isJsonPrimitive)
            value = element.asString
    }
}

/**
 * Block value represents a value with a block
 */
class BlockValue @JvmOverloads constructor(name: String, value: Int, isSupported: (() -> Boolean)? = null) : IntegerValue(name, value, 1, 197, isSupported)

/**
 * List value represents a selectable list of values
 */
open class ListValue @JvmOverloads constructor(name: String, val values: Array<String>, value: String, isSupported: (() -> Boolean)? = null) : Value<String>(name, value, isSupported) {

    @JvmField
    var openList = false

    init {
        this.value = value
    }

    infix fun equal(other: String): Boolean{
        return this.get().equals(other,true)
    }

    infix fun notEqual(other: String): Boolean {
        return !this.get().equals(other, true)
    }

    operator fun contains(string: String?): Boolean {
        return Arrays.stream(values).anyMatch { s: String -> s.equals(string, ignoreCase = true) }
    }

    override fun changeValue(value: String) {
        for (element in values) {
            if (element.equals(value, ignoreCase = true)) {
                this.value = element
                break
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other is String) {
            return this equal other
        }
        if (other is ListValue) {
            return this.openList == other.openList && this.name == other.name && this.values.contentEquals(other.values) && this.value == other.value
        }
        return false
    }

    override fun toJson() = JsonPrimitive(value)

    override fun fromJson(element: JsonElement) {
        if (element.isJsonPrimitive) changeValue(element.asString)
    }

    override fun hashCode(): Int {
        var result = values.contentHashCode()
        result = 31 * result + openList.hashCode()
        return result
    }
}