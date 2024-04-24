package me.offeex.bloomware.api.helper

class Stopwatch {
    private var time = -1L
    fun reset() {
        time = System.currentTimeMillis()
    }

    fun passed(time: Number) = System.currentTimeMillis() - this.time >= time.toLong()
    fun passedTicks(time: Number) = passed(time.toLong() * 50)
}