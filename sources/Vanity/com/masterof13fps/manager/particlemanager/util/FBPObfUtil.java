package com.masterof13fps.manager.particlemanager.util;

import java.util.*;

public class FBPObfUtil
{
    private static HashMap<String, String> map;

    public static String translateObfMaterialName(final String fieldName) {
        if (!FBPObfUtil.map.containsKey(fieldName)) {
            return fieldName;
        }
        return FBPObfUtil.map.get(fieldName);
    }

    static {
        FBPObfUtil.map = new HashMap<String, String>();
        final String obfNames = "field_151579_a,field_151577_b,field_151578_c,field_151575_d,field_151576_e,field_151573_f,field_151574_g,field_151586_h,field_151587_i,field_151584_j,field_151585_k,field_151582_l,field_151583_m,field_151580_n,field_151581_o,field_151595_p,field_151594_q,field_151593_r,field_151592_s,field_151591_t,field_151590_u,field_151589_v,field_151588_w,field_151598_x,field_151597_y,field_151596_z,field_151570_A,field_151571_B,field_151572_C,field_151566_D,field_151567_E,field_151568_F,field_151569_G,field_76233_E,field_175972_I";
        final String deobfNames = "air,grass,ground,wood,rock,iron,anvil,water,lava,leaves,plants,vine,sponge,cloth,fire,sand,circuits,carpet,glass,redstoneLight,tnt,coral,ice,packedIce,snow,craftedSnow,cactus,clay,gourd,dragonEgg,portal,cake,web,piston,barrier";
        final String[] obf = obfNames.split(",");
        final String[] deobf = deobfNames.split(",");
        for (int i = 0; i < obf.length; ++i) {
            FBPObfUtil.map.put(obf[i], deobf[i].toLowerCase());
        }
    }
}
