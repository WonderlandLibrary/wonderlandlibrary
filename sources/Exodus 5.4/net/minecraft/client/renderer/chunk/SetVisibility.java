/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.chunk;

import java.util.BitSet;
import java.util.Set;
import net.minecraft.util.EnumFacing;

public class SetVisibility {
    private final BitSet bitSet = new BitSet(COUNT_FACES * COUNT_FACES);
    private static final int COUNT_FACES = EnumFacing.values().length;

    public String toString() {
        EnumFacing enumFacing;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(' ');
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (n2 < n) {
            enumFacing = enumFacingArray[n2];
            stringBuilder.append(' ').append(enumFacing.toString().toUpperCase().charAt(0));
            ++n2;
        }
        stringBuilder.append('\n');
        enumFacingArray = EnumFacing.values();
        n = enumFacingArray.length;
        n2 = 0;
        while (n2 < n) {
            enumFacing = enumFacingArray[n2];
            stringBuilder.append(enumFacing.toString().toUpperCase().charAt(0));
            EnumFacing[] enumFacingArray2 = EnumFacing.values();
            int n3 = enumFacingArray2.length;
            int n4 = 0;
            while (n4 < n3) {
                EnumFacing enumFacing2 = enumFacingArray2[n4];
                if (enumFacing == enumFacing2) {
                    stringBuilder.append("  ");
                } else {
                    boolean bl = this.isVisible(enumFacing, enumFacing2);
                    stringBuilder.append(' ').append(bl ? (char)'Y' : 'n');
                }
                ++n4;
            }
            stringBuilder.append('\n');
            ++n2;
        }
        return stringBuilder.toString();
    }

    public void setManyVisible(Set<EnumFacing> set) {
        for (EnumFacing enumFacing : set) {
            for (EnumFacing enumFacing2 : set) {
                this.setVisible(enumFacing, enumFacing2, true);
            }
        }
    }

    public void setVisible(EnumFacing enumFacing, EnumFacing enumFacing2, boolean bl) {
        this.bitSet.set(enumFacing.ordinal() + enumFacing2.ordinal() * COUNT_FACES, bl);
        this.bitSet.set(enumFacing2.ordinal() + enumFacing.ordinal() * COUNT_FACES, bl);
    }

    public void setAllVisible(boolean bl) {
        this.bitSet.set(0, this.bitSet.size(), bl);
    }

    public boolean isVisible(EnumFacing enumFacing, EnumFacing enumFacing2) {
        return this.bitSet.get(enumFacing.ordinal() + enumFacing2.ordinal() * COUNT_FACES);
    }
}

