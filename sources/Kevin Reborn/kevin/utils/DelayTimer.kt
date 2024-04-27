package kevin.utils

import kevin.module.IntegerValue
import kevin.utils.InventoryUtils.CLICK_TIMER
import kevin.utils.TimeUtils.randomDelay

open class DelayTimer(
    private val minDelayValue: IntegerValue, private val maxDelayValue: IntegerValue = minDelayValue,
    private val baseTimer: MSTimer = CLICK_TIMER
) {
    private var delay = 0L

    open fun hasTimePassed() = baseTimer.hasTimePassed(delay)

    fun resetDelay() {
        delay = randomDelay(minDelayValue.get(), maxDelayValue.get())
    }

    fun resetTimer() = baseTimer.reset()

    fun reset() {
        resetTimer()
        resetDelay()
    }
}

open class TickDelayTimer(
    private val minDelayValue: IntegerValue, private val maxDelayValue: IntegerValue = minDelayValue,
    private val baseTimer: TickTimer = TickTimer()
) {
    private var ticks = 0

    open fun hasTimePassed() = baseTimer.hasTimePassed(ticks)

    fun resetTicks() {
        ticks = randomDelay(minDelayValue.get(), maxDelayValue.get()).toInt()
    }

    fun resetTimer() = baseTimer.reset()

    fun update() = baseTimer.update()

    fun reset() {
        resetTimer()
        resetTicks()
    }
}