package net.minusmc.ravenb4.utils

class Timer {
    private var currentTime = System.currentTimeMillis()

    fun hasTimePassed(delay: Long) = System.currentTimeMillis() - currentTime >= delay

}