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
package kevin.utils

import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos.MutableBlockPos
import net.minecraft.util.MathHelper
import net.minecraft.util.Vec3
import java.util.*

fun Vec3.multiply(value: Double): Vec3 {
    return Vec3(this.xCoord * value, this.yCoord * value, this.zCoord * value)
}

fun AxisAlignedBB.getLookingTargetRange(thePlayer: EntityPlayerSP, rotation: Rotation? = null, range: Double=6.0): Double {
    val eyes = thePlayer.eyesLoc
    val movingObj = this.calculateIntercept(eyes, (rotation ?: RotationUtils.bestServerRotation()).toDirection().multiply(range).add(eyes)) ?: return Double.MAX_VALUE
    return movingObj.hitVec.distanceTo(eyes)
}

fun AxisAlignedBB.expands(v: Double, modifyYDown: Boolean=true, modifyYUp: Boolean=true): AxisAlignedBB {
    return AxisAlignedBB(this.minX - v, this.minY - (if (modifyYDown) v else 0.0), this.minZ - v, this.maxX + v, this.maxY + (if (modifyYUp) v else 0.0), this.maxZ + v)
}

fun AxisAlignedBB.getBlockStatesIncluded(): List<IBlockState> {
    val tmpArr = LinkedList<IBlockState>()
    val minX = MathHelper.floor_double(this.minX)
    val minY = MathHelper.floor_double(this.minY)
    val minZ = MathHelper.floor_double(this.minZ)
    val maxX = MathHelper.floor_double(this.maxX)
    val maxY = MathHelper.floor_double(this.maxY)
    val maxZ = MathHelper.floor_double(this.maxZ)
    val mc = Minecraft.getMinecraft()
    val mbp = MutableBlockPos(minX, minY, minZ)

    for (x in minX .. maxX) {
        for (y in minY .. maxY) {
            for (z in maxZ .. maxX) {
                mbp.set(x, y, z)
                if (mc.theWorld.isAirBlock(mbp)) continue
                tmpArr.add(mc.theWorld.getBlockState(mbp))
            }
        }
    }

    return tmpArr
}


internal fun Number.toRadians(): Double = Math.toRadians(toDouble())

/**
 * Provides:
 * ```
 * val (x, y, z) = vec
 */
operator fun Vec3.component1() = xCoord
operator fun Vec3.component2() = yCoord
operator fun Vec3.component3() = zCoord

/**
 * Provides:
 * `vec + othervec`, `vec - othervec`, `vec * number`, `vec / number`
 * */
operator fun Vec3.plus(vec: Vec3): Vec3 = add(vec)
operator fun Vec3.minus(vec: Vec3): Vec3 = subtract(vec)
operator fun Vec3.times(number: Double) = Vec3(xCoord * number, yCoord * number, zCoord * number)
operator fun Vec3.times(number: Float) = Vec3(xCoord * number, yCoord * number, zCoord * number)
operator fun Vec3.div(number: Double) = times(1 / number)


/**
 * Provides: (step is 0.1 by default)
 * ```
 *      for (x in 0.1..0.9 step 0.05) {}
 *      for (y in 0.1..0.9) {}
 */
class RangeIterator(private val range: ClosedFloatingPointRange<Double>, private val step: Double = 0.1): Iterator<Double> {
    private var value = range.start

    override fun hasNext() = value < range.endInclusive

    override fun next(): Double {
        val returned = value
        value = (value + step).coerceAtMost(range.endInclusive)
        return returned
    }
}
operator fun ClosedFloatingPointRange<Double>.iterator() = RangeIterator(this)
infix fun ClosedFloatingPointRange<Double>.step(step: Double) = RangeIterator(this, step)

class FloatIterator(private val range: ClosedFloatingPointRange<Float>, private val step: Float = 0.1f): Iterator<Float> {
    private var value = range.start

    override fun hasNext() = value < range.endInclusive

    override fun next(): Float {
        val returned = value
        value = (value + step).coerceAtMost(range.endInclusive)
        return returned
    }
}
operator fun ClosedFloatingPointRange<Float>.iterator() = FloatIterator(this)
infix fun ClosedFloatingPointRange<Float>.step(step: Float) = FloatIterator(this, step)


/**
 * Conditionally shuffles an `Iterable`
 * @param shuffle determines if the returned `Iterable` is shuffled
 */
fun <T> Iterable<T>.shuffled(shuffle: Boolean) = toMutableList().apply { if (shuffle) shuffle() }