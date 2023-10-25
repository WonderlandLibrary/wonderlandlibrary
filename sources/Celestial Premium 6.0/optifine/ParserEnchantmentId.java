/*
 * Decompiled with CFR 0.150.
 */
package optifine;

import net.minecraft.enchantment.Enchantment;
import optifine.IParserInt;

public class ParserEnchantmentId
implements IParserInt {
    @Override
    public int parse(String p_parse_1_, int p_parse_2_) {
        Enchantment enchantment = Enchantment.getEnchantmentByLocation(p_parse_1_);
        return enchantment == null ? p_parse_2_ : Enchantment.getEnchantmentID(enchantment);
    }
}

