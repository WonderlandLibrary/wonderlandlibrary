/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.chunk;

import java.util.BitSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.util.EnumFacing;

public class SetVisibility {
    private static final int COUNT_FACES = EnumFacing.values().length;
    private final BitSet bitSet = new BitSet(COUNT_FACES * COUNT_FACES);

    public void setManyVisible(Set<EnumFacing> p_178620_1_) {
        Iterator<EnumFacing> iterator = p_178620_1_.iterator();
        block0: while (iterator.hasNext()) {
            EnumFacing enumfacing = iterator.next();
            Iterator<EnumFacing> iterator2 = p_178620_1_.iterator();
            while (true) {
                if (!iterator2.hasNext()) continue block0;
                EnumFacing enumfacing1 = iterator2.next();
                this.setVisible(enumfacing, enumfacing1, true);
            }
            break;
        }
        return;
    }

    public void setVisible(EnumFacing facing, EnumFacing facing2, boolean p_178619_3_) {
        this.bitSet.set(facing.ordinal() + facing2.ordinal() * COUNT_FACES, p_178619_3_);
        this.bitSet.set(facing2.ordinal() + facing.ordinal() * COUNT_FACES, p_178619_3_);
    }

    public void setAllVisible(boolean visible) {
        this.bitSet.set(0, this.bitSet.size(), visible);
    }

    public boolean isVisible(EnumFacing facing, EnumFacing facing2) {
        return this.bitSet.get(facing.ordinal() + facing2.ordinal() * COUNT_FACES);
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append(' ');
        for (EnumFacing enumfacing : EnumFacing.values()) {
            stringbuilder.append(' ').append(enumfacing.toString().toUpperCase().charAt(0));
        }
        stringbuilder.append('\n');
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing enumfacing2 = enumFacingArray[n2];
            stringbuilder.append(enumfacing2.toString().toUpperCase().charAt(0));
            for (EnumFacing enumfacing1 : EnumFacing.values()) {
                if (enumfacing2 == enumfacing1) {
                    stringbuilder.append("  ");
                    continue;
                }
                boolean flag = this.isVisible(enumfacing2, enumfacing1);
                stringbuilder.append(' ').append(flag ? (char)'Y' : 'n');
            }
            stringbuilder.append('\n');
            ++n2;
        }
        return stringbuilder.toString();
    }
}

