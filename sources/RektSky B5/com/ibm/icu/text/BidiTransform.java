/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import com.ibm.icu.text.UTF16;

public class BidiTransform {
    private Bidi bidi;
    private String text;
    private int reorderingOptions;
    private int shapingOptions;

    public String transform(CharSequence text, byte inParaLevel, Order inOrder, byte outParaLevel, Order outOrder, Mirroring doMirroring, int shapingOptions) {
        if (text == null || inOrder == null || outOrder == null || doMirroring == null) {
            throw new IllegalArgumentException();
        }
        this.text = text.toString();
        byte[] levels = new byte[]{inParaLevel, outParaLevel};
        this.resolveBaseDirection(levels);
        ReorderingScheme currentScheme = this.findMatchingScheme(levels[0], inOrder, levels[1], outOrder);
        if (currentScheme != null) {
            this.bidi = new Bidi();
            this.reorderingOptions = Mirroring.ON.equals((Object)doMirroring) ? 2 : 0;
            this.shapingOptions = shapingOptions & 0xFFFFFFFB;
            currentScheme.doTransform(this);
        }
        return this.text;
    }

    private void resolveBaseDirection(byte[] levels) {
        byte level;
        levels[0] = Bidi.IsDefaultLevel(levels[0]) ? ((level = Bidi.getBaseDirection(this.text)) != 3 ? level : (levels[0] == 127 ? (byte)1 : 0)) : (byte)(levels[0] & 1);
        levels[1] = Bidi.IsDefaultLevel(levels[1]) ? levels[0] : (byte)(levels[1] & 1);
    }

    private ReorderingScheme findMatchingScheme(byte inLevel, Order inOrder, byte outLevel, Order outOrder) {
        for (ReorderingScheme scheme : ReorderingScheme.values()) {
            if (!scheme.matches(inLevel, inOrder, outLevel, outOrder)) continue;
            return scheme;
        }
        return null;
    }

    private void resolve(byte level, int options) {
        this.bidi.setInverse((options & 5) != 0);
        this.bidi.setReorderingMode(options);
        this.bidi.setPara(this.text, level, null);
    }

    private void reorder() {
        this.text = this.bidi.writeReordered(this.reorderingOptions);
        this.reorderingOptions = 0;
    }

    private void reverse() {
        this.text = Bidi.writeReverse(this.text, 0);
    }

    private void mirror() {
        int ch;
        if ((this.reorderingOptions & 2) == 0) {
            return;
        }
        StringBuffer sb = new StringBuffer(this.text);
        byte[] levels = this.bidi.getLevels();
        int n2 = levels.length;
        for (int i2 = 0; i2 < n2; i2 += UTF16.getCharCount(ch)) {
            ch = UTF16.charAt(sb, i2);
            if ((levels[i2] & 1) == 0) continue;
            UTF16.setCharAt(sb, i2, UCharacter.getMirror(ch));
        }
        this.text = sb.toString();
        this.reorderingOptions &= 0xFFFFFFFD;
    }

    private void shapeArabic(int digitsDir, int lettersDir) {
        if (digitsDir == lettersDir) {
            this.shapeArabic(this.shapingOptions | digitsDir);
        } else {
            this.shapeArabic(this.shapingOptions & 0xFFFFFFE7 | digitsDir);
            this.shapeArabic(this.shapingOptions & 0xFFFFFF1F | lettersDir);
        }
    }

    private void shapeArabic(int options) {
        if (options != 0) {
            ArabicShaping shaper = new ArabicShaping(options);
            try {
                this.text = shaper.shape(this.text);
            }
            catch (ArabicShapingException arabicShapingException) {
                // empty catch block
            }
        }
    }

    private static boolean IsLTR(byte level) {
        return (level & 1) == 0;
    }

    private static boolean IsRTL(byte level) {
        return (level & 1) == 1;
    }

    private static boolean IsLogical(Order order) {
        return Order.LOGICAL.equals((Object)order);
    }

    private static boolean IsVisual(Order order) {
        return Order.VISUAL.equals((Object)order);
    }

    private static enum ReorderingScheme {
        LOG_LTR_TO_VIS_LTR{

            @Override
            boolean matches(byte inLevel, Order inOrder, byte outLevel, Order outOrder) {
                return BidiTransform.IsLTR(inLevel) && BidiTransform.IsLogical(inOrder) && BidiTransform.IsLTR(outLevel) && BidiTransform.IsVisual(outOrder);
            }

            @Override
            void doTransform(BidiTransform transform) {
                transform.shapeArabic(0, 0);
                transform.resolve((byte)0, 0);
                transform.reorder();
            }
        }
        ,
        LOG_RTL_TO_VIS_LTR{

            @Override
            boolean matches(byte inLevel, Order inOrder, byte outLevel, Order outOrder) {
                return BidiTransform.IsRTL(inLevel) && BidiTransform.IsLogical(inOrder) && BidiTransform.IsLTR(outLevel) && BidiTransform.IsVisual(outOrder);
            }

            @Override
            void doTransform(BidiTransform transform) {
                transform.resolve((byte)1, 0);
                transform.reorder();
                transform.shapeArabic(0, 4);
            }
        }
        ,
        LOG_LTR_TO_VIS_RTL{

            @Override
            boolean matches(byte inLevel, Order inOrder, byte outLevel, Order outOrder) {
                return BidiTransform.IsLTR(inLevel) && BidiTransform.IsLogical(inOrder) && BidiTransform.IsRTL(outLevel) && BidiTransform.IsVisual(outOrder);
            }

            @Override
            void doTransform(BidiTransform transform) {
                transform.shapeArabic(0, 0);
                transform.resolve((byte)0, 0);
                transform.reorder();
                transform.reverse();
            }
        }
        ,
        LOG_RTL_TO_VIS_RTL{

            @Override
            boolean matches(byte inLevel, Order inOrder, byte outLevel, Order outOrder) {
                return BidiTransform.IsRTL(inLevel) && BidiTransform.IsLogical(inOrder) && BidiTransform.IsRTL(outLevel) && BidiTransform.IsVisual(outOrder);
            }

            @Override
            void doTransform(BidiTransform transform) {
                transform.resolve((byte)1, 0);
                transform.reorder();
                transform.shapeArabic(0, 4);
                transform.reverse();
            }
        }
        ,
        VIS_LTR_TO_LOG_RTL{

            @Override
            boolean matches(byte inLevel, Order inOrder, byte outLevel, Order outOrder) {
                return BidiTransform.IsLTR(inLevel) && BidiTransform.IsVisual(inOrder) && BidiTransform.IsRTL(outLevel) && BidiTransform.IsLogical(outOrder);
            }

            @Override
            void doTransform(BidiTransform transform) {
                transform.shapeArabic(0, 4);
                transform.resolve((byte)1, 5);
                transform.reorder();
            }
        }
        ,
        VIS_RTL_TO_LOG_RTL{

            @Override
            boolean matches(byte inLevel, Order inOrder, byte outLevel, Order outOrder) {
                return BidiTransform.IsRTL(inLevel) && BidiTransform.IsVisual(inOrder) && BidiTransform.IsRTL(outLevel) && BidiTransform.IsLogical(outOrder);
            }

            @Override
            void doTransform(BidiTransform transform) {
                transform.reverse();
                transform.shapeArabic(0, 4);
                transform.resolve((byte)1, 5);
                transform.reorder();
            }
        }
        ,
        VIS_LTR_TO_LOG_LTR{

            @Override
            boolean matches(byte inLevel, Order inOrder, byte outLevel, Order outOrder) {
                return BidiTransform.IsLTR(inLevel) && BidiTransform.IsVisual(inOrder) && BidiTransform.IsLTR(outLevel) && BidiTransform.IsLogical(outOrder);
            }

            @Override
            void doTransform(BidiTransform transform) {
                transform.resolve((byte)0, 5);
                transform.reorder();
                transform.shapeArabic(0, 0);
            }
        }
        ,
        VIS_RTL_TO_LOG_LTR{

            @Override
            boolean matches(byte inLevel, Order inOrder, byte outLevel, Order outOrder) {
                return BidiTransform.IsRTL(inLevel) && BidiTransform.IsVisual(inOrder) && BidiTransform.IsLTR(outLevel) && BidiTransform.IsLogical(outOrder);
            }

            @Override
            void doTransform(BidiTransform transform) {
                transform.reverse();
                transform.resolve((byte)0, 5);
                transform.reorder();
                transform.shapeArabic(0, 0);
            }
        }
        ,
        LOG_LTR_TO_LOG_RTL{

            @Override
            boolean matches(byte inLevel, Order inOrder, byte outLevel, Order outOrder) {
                return BidiTransform.IsLTR(inLevel) && BidiTransform.IsLogical(inOrder) && BidiTransform.IsRTL(outLevel) && BidiTransform.IsLogical(outOrder);
            }

            @Override
            void doTransform(BidiTransform transform) {
                transform.shapeArabic(0, 0);
                transform.resolve((byte)0, 0);
                transform.mirror();
                transform.resolve((byte)0, 3);
                transform.reorder();
            }
        }
        ,
        LOG_RTL_TO_LOG_LTR{

            @Override
            boolean matches(byte inLevel, Order inOrder, byte outLevel, Order outOrder) {
                return BidiTransform.IsRTL(inLevel) && BidiTransform.IsLogical(inOrder) && BidiTransform.IsLTR(outLevel) && BidiTransform.IsLogical(outOrder);
            }

            @Override
            void doTransform(BidiTransform transform) {
                transform.resolve((byte)1, 0);
                transform.mirror();
                transform.resolve((byte)1, 3);
                transform.reorder();
                transform.shapeArabic(0, 0);
            }
        }
        ,
        VIS_LTR_TO_VIS_RTL{

            @Override
            boolean matches(byte inLevel, Order inOrder, byte outLevel, Order outOrder) {
                return BidiTransform.IsLTR(inLevel) && BidiTransform.IsVisual(inOrder) && BidiTransform.IsRTL(outLevel) && BidiTransform.IsVisual(outOrder);
            }

            @Override
            void doTransform(BidiTransform transform) {
                transform.resolve((byte)0, 0);
                transform.mirror();
                transform.shapeArabic(0, 4);
                transform.reverse();
            }
        }
        ,
        VIS_RTL_TO_VIS_LTR{

            @Override
            boolean matches(byte inLevel, Order inOrder, byte outLevel, Order outOrder) {
                return BidiTransform.IsRTL(inLevel) && BidiTransform.IsVisual(inOrder) && BidiTransform.IsLTR(outLevel) && BidiTransform.IsVisual(outOrder);
            }

            @Override
            void doTransform(BidiTransform transform) {
                transform.reverse();
                transform.resolve((byte)0, 0);
                transform.mirror();
                transform.shapeArabic(0, 4);
            }
        }
        ,
        LOG_LTR_TO_LOG_LTR{

            @Override
            boolean matches(byte inLevel, Order inOrder, byte outLevel, Order outOrder) {
                return BidiTransform.IsLTR(inLevel) && BidiTransform.IsLogical(inOrder) && BidiTransform.IsLTR(outLevel) && BidiTransform.IsLogical(outOrder);
            }

            @Override
            void doTransform(BidiTransform transform) {
                transform.resolve((byte)0, 0);
                transform.mirror();
                transform.shapeArabic(0, 0);
            }
        }
        ,
        LOG_RTL_TO_LOG_RTL{

            @Override
            boolean matches(byte inLevel, Order inOrder, byte outLevel, Order outOrder) {
                return BidiTransform.IsRTL(inLevel) && BidiTransform.IsLogical(inOrder) && BidiTransform.IsRTL(outLevel) && BidiTransform.IsLogical(outOrder);
            }

            @Override
            void doTransform(BidiTransform transform) {
                transform.resolve((byte)1, 0);
                transform.mirror();
                transform.shapeArabic(4, 0);
            }
        }
        ,
        VIS_LTR_TO_VIS_LTR{

            @Override
            boolean matches(byte inLevel, Order inOrder, byte outLevel, Order outOrder) {
                return BidiTransform.IsLTR(inLevel) && BidiTransform.IsVisual(inOrder) && BidiTransform.IsLTR(outLevel) && BidiTransform.IsVisual(outOrder);
            }

            @Override
            void doTransform(BidiTransform transform) {
                transform.resolve((byte)0, 0);
                transform.mirror();
                transform.shapeArabic(0, 4);
            }
        }
        ,
        VIS_RTL_TO_VIS_RTL{

            @Override
            boolean matches(byte inLevel, Order inOrder, byte outLevel, Order outOrder) {
                return BidiTransform.IsRTL(inLevel) && BidiTransform.IsVisual(inOrder) && BidiTransform.IsRTL(outLevel) && BidiTransform.IsVisual(outOrder);
            }

            @Override
            void doTransform(BidiTransform transform) {
                transform.reverse();
                transform.resolve((byte)0, 0);
                transform.mirror();
                transform.shapeArabic(0, 4);
                transform.reverse();
            }
        };


        abstract boolean matches(byte var1, Order var2, byte var3, Order var4);

        abstract void doTransform(BidiTransform var1);
    }

    public static enum Mirroring {
        OFF,
        ON;

    }

    public static enum Order {
        LOGICAL,
        VISUAL;

    }
}

