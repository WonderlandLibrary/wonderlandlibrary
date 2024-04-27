package net.ccbluex.liquidbounce.utils


import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.MathHelper

object PlayerUtils {
    fun getAr(player : EntityLivingBase):Double{
        var arPercentage: Double = (player!!.totalArmorValue / player!!.maxHealth).toDouble()
        arPercentage = MathHelper.clamp_double(arPercentage, 0.0, 1.0)
        return 100 * arPercentage
    }
}
