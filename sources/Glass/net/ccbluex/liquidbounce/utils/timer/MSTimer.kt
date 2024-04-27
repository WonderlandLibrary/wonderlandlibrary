/*
 * GlassClient PVP Client
 * A free open-source mixin-based PVP client based on liquidbounce with all cheats removed.
 * https://github.com/GlassClient/GlassClient
 */
package net.ccbluex.liquidbounce.utils.timer

class MSTimer {
    var time = -1L

    fun hasTimePassed(MS: Long): Boolean {
        return System.currentTimeMillis() >= time + MS
    }

    fun hasTimeLeft(MS: Long): Long {
        return MS + time - System.currentTimeMillis()
    }

    fun timePassed(): Long {
        return System.currentTimeMillis() - time
    }

    fun reset() {
        time = System.currentTimeMillis()
    }
}