/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

import java.util.List;
import java.util.Random;
import net.minecraft.client.gui.FontRenderer;
import org.apache.commons.lang3.StringUtils;

public class EnchantmentNameParts {
    private static final EnchantmentNameParts INSTANCE = new EnchantmentNameParts();
    private final Random rand = new Random();
    private final String[] namePartsArray = "the elder scrolls klaatu berata niktu xyzzy bless curse light darkness fire air earth water hot dry cold wet ignite snuff embiggen twist shorten stretch fiddle destroy imbue galvanize enchant free limited range of towards inside sphere cube self other ball mental physical grow shrink demon elemental spirit animal creature beast humanoid undead fresh stale phnglui mglwnafh cthulhu rlyeh wgahnagl fhtagnbaguette".split(" ");

    public static EnchantmentNameParts getInstance() {
        return INSTANCE;
    }

    public String generateNewRandomName(FontRenderer p_148334_1_, int p_148334_2_) {
        int i = this.rand.nextInt(2) + 3;
        String s = "";
        for (int j = 0; j < i; ++j) {
            if (j > 0) {
                s = s + " ";
            }
            s = s + this.namePartsArray[this.rand.nextInt(this.namePartsArray.length)];
        }
        List<String> list = p_148334_1_.listFormattedStringToWidth(s, p_148334_2_);
        return StringUtils.join(list.size() >= 2 ? list.subList(0, 2) : list, " ");
    }

    public void reseedRandomGenerator(long seed) {
        this.rand.setSeed(seed);
    }
}

