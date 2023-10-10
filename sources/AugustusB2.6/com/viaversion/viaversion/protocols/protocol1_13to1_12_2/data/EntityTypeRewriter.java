// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data;

import com.viaversion.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;

public class EntityTypeRewriter
{
    private static final Int2IntMap ENTITY_TYPES;
    
    private static void registerEntity(final int type1_12, final int type1_13) {
        EntityTypeRewriter.ENTITY_TYPES.put(type1_12, type1_13);
    }
    
    public static int getNewId(final int type1_12) {
        return EntityTypeRewriter.ENTITY_TYPES.getOrDefault(type1_12, type1_12);
    }
    
    static {
        (ENTITY_TYPES = new Int2IntOpenHashMap(83, 0.99f)).defaultReturnValue(-1);
        registerEntity(1, 32);
        registerEntity(2, 22);
        registerEntity(3, 0);
        registerEntity(4, 15);
        registerEntity(5, 84);
        registerEntity(6, 71);
        registerEntity(7, 74);
        registerEntity(8, 35);
        registerEntity(9, 49);
        registerEntity(10, 2);
        registerEntity(11, 67);
        registerEntity(12, 34);
        registerEntity(13, 65);
        registerEntity(14, 75);
        registerEntity(15, 23);
        registerEntity(16, 77);
        registerEntity(17, 76);
        registerEntity(18, 33);
        registerEntity(19, 85);
        registerEntity(20, 55);
        registerEntity(21, 24);
        registerEntity(22, 25);
        registerEntity(23, 30);
        registerEntity(24, 68);
        registerEntity(25, 60);
        registerEntity(26, 13);
        registerEntity(27, 89);
        registerEntity(28, 63);
        registerEntity(29, 88);
        registerEntity(30, 1);
        registerEntity(31, 11);
        registerEntity(32, 46);
        registerEntity(33, 20);
        registerEntity(34, 21);
        registerEntity(35, 78);
        registerEntity(36, 81);
        registerEntity(37, 31);
        registerEntity(40, 41);
        registerEntity(41, 5);
        registerEntity(42, 39);
        registerEntity(43, 40);
        registerEntity(44, 42);
        registerEntity(45, 45);
        registerEntity(46, 43);
        registerEntity(47, 44);
        registerEntity(50, 10);
        registerEntity(51, 62);
        registerEntity(52, 69);
        registerEntity(53, 27);
        registerEntity(54, 87);
        registerEntity(55, 64);
        registerEntity(56, 26);
        registerEntity(57, 53);
        registerEntity(58, 18);
        registerEntity(59, 6);
        registerEntity(60, 61);
        registerEntity(61, 4);
        registerEntity(62, 38);
        registerEntity(63, 17);
        registerEntity(64, 83);
        registerEntity(65, 3);
        registerEntity(66, 82);
        registerEntity(67, 19);
        registerEntity(68, 28);
        registerEntity(69, 59);
        registerEntity(200, 16);
        registerEntity(90, 51);
        registerEntity(91, 58);
        registerEntity(92, 9);
        registerEntity(93, 7);
        registerEntity(94, 70);
        registerEntity(95, 86);
        registerEntity(96, 47);
        registerEntity(97, 66);
        registerEntity(98, 48);
        registerEntity(99, 80);
        registerEntity(100, 29);
        registerEntity(101, 56);
        registerEntity(102, 54);
        registerEntity(103, 36);
        registerEntity(104, 37);
        registerEntity(105, 50);
        registerEntity(120, 79);
    }
}
