package net.minecraft.src;

import java.util.Random;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;



public class MathUtils {
	public static int getAverage(int[] p_getAverage_0_) {
		if (p_getAverage_0_.length <= 0) {
			return 0;
		} else {
			int i = 0;

			for (int j = 0; j < p_getAverage_0_.length; ++j) {
				int k = p_getAverage_0_[j];
				i += k;
			}

			int l = i / p_getAverage_0_.length;
			return l;
		}
	}
	private static Random rng = new Random();
	

	 public static int customRandInt(int min, int max)
	  {
	    return new Random().nextInt(max - min + 1) + min;
	  }

	 public static boolean isOnSameTeam(EntityLivingBase Memer, EntityLivingBase jew){
			String all = "0123456789abcdef";
			for(int i = 0; i < all.length(); i++){
				char s = all.charAt(i);
				if(Memer.getDisplayName().getUnformattedText().toLowerCase().startsWith("§" + s) && jew.getDisplayName().getUnformattedText().toLowerCase().startsWith("§" + s)){
					return true;
				}
			}
			return false;
		}


	    public static int getRandom(final int cap) {
	        return MathUtils.rng.nextInt(cap);
	    }



	    public static double square(double value) {
	        return value * value;
	    }

}
