package net.optifine.config;

import net.minecraft.enchantment.Enchantment;
import net.optifine.config.IParserInt;

public class ParserEnchantmentId
implements IParserInt {
    public int parse(String str, int defVal) {
        Enchantment enchantment = Enchantment.getEnchantmentByLocation((String)str);
        return enchantment == null ? defVal : enchantment.effectId;
    }
}
