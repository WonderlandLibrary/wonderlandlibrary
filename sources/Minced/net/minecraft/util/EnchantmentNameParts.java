// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.client.gui.FontRenderer;
import java.util.Random;

public class EnchantmentNameParts
{
    private static final EnchantmentNameParts INSTANCE;
    private final Random rand;
    private final String[] namePartsArray;
    
    public EnchantmentNameParts() {
        this.rand = new Random();
        this.namePartsArray = "the elder scrolls klaatu berata niktu xyzzy bless curse light darkness fire air earth water hot dry cold wet ignite snuff embiggen twist shorten stretch fiddle destroy imbue galvanize enchant free limited range of towards inside sphere cube self other ball mental physical grow shrink demon elemental spirit animal creature beast humanoid undead fresh stale phnglui mglwnafh cthulhu rlyeh wgahnagl fhtagnbaguette".split(" ");
    }
    
    public static EnchantmentNameParts getInstance() {
        return EnchantmentNameParts.INSTANCE;
    }
    
    public String generateNewRandomName(final FontRenderer fontRendererIn, final int length) {
        final int i = this.rand.nextInt(2) + 3;
        String s = "";
        for (int j = 0; j < i; ++j) {
            if (j > 0) {
                s += " ";
            }
            s += this.namePartsArray[this.rand.nextInt(this.namePartsArray.length)];
        }
        final List<String> list = fontRendererIn.listFormattedStringToWidth(s, length);
        return StringUtils.join((Iterable)((list.size() >= 2) ? list.subList(0, 2) : list), " ");
    }
    
    public void reseedRandomGenerator(final long seed) {
        this.rand.setSeed(seed);
    }
    
    static {
        INSTANCE = new EnchantmentNameParts();
    }
}
