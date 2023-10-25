/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import java.util.Random;

public class EnchantmentNameParts {
    private static final EnchantmentNameParts instance = new EnchantmentNameParts();
    private String[] namePartsArray;
    private Random rand = new Random();

    public static EnchantmentNameParts getInstance() {
        return instance;
    }

    public EnchantmentNameParts() {
        this.namePartsArray = "the elder scrolls klaatu berata niktu xyzzy bless curse light darkness fire air earth water hot dry cold wet ignite snuff embiggen twist shorten stretch fiddle destroy imbue galvanize enchant free limited range of towards inside sphere cube self other ball mental physical grow shrink demon elemental spirit animal creature beast humanoid undead fresh stale ".split(" ");
    }

    public void reseedRandomGenerator(long l) {
        this.rand.setSeed(l);
    }

    public String generateNewRandomName() {
        int n = this.rand.nextInt(2) + 3;
        String string = "";
        int n2 = 0;
        while (n2 < n) {
            if (n2 > 0) {
                string = String.valueOf(string) + " ";
            }
            string = String.valueOf(string) + this.namePartsArray[this.rand.nextInt(this.namePartsArray.length)];
            ++n2;
        }
        return string;
    }
}

