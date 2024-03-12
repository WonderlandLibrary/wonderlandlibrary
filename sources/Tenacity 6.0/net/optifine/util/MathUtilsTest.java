// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

import net.minecraft.util.MathHelper;

public class MathUtilsTest
{
    public static void main(final String[] args) throws Exception {
        final OPER[] amathutilstest$oper = OPER.values();
        for (int i = 0; i < amathutilstest$oper.length; ++i) {
            final OPER mathutilstest$oper = amathutilstest$oper[i];
            dbg("******** " + mathutilstest$oper + " ***********");
            test(mathutilstest$oper, false);
        }
    }
    
    private static void test(final OPER oper, final boolean fast) {
        MathHelper.fastMath = fast;
        double d0 = 0.0;
        double d2 = 0.0;
        switch (oper) {
            case SIN:
            case COS: {
                d0 = -MathHelper.PI;
                d2 = MathHelper.PI;
                break;
            }
            case ASIN:
            case ACOS: {
                d0 = -1.0;
                d2 = 1.0;
                break;
            }
            default: {
                return;
            }
        }
        for (int i = 10, j = 0; j <= i; ++j) {
            final double d3 = d0 + j * (d2 - d0) / i;
            float f = 0.0f;
            float f2 = 0.0f;
            switch (oper) {
                case SIN: {
                    f = (float)Math.sin(d3);
                    f2 = MathHelper.sin((float)d3);
                    break;
                }
                case COS: {
                    f = (float)Math.cos(d3);
                    f2 = MathHelper.cos((float)d3);
                    break;
                }
                case ASIN: {
                    f = (float)Math.asin(d3);
                    f2 = MathUtils.asin((float)d3);
                    break;
                }
                case ACOS: {
                    f = (float)Math.acos(d3);
                    f2 = MathUtils.acos((float)d3);
                    break;
                }
                default: {
                    return;
                }
            }
            dbg(String.format("%.2f, Math: %f, Helper: %f, diff: %f", d3, f, f2, Math.abs(f - f2)));
        }
    }
    
    public static void dbg(final String str) {
        System.out.println(str);
    }
    
    private enum OPER
    {
        SIN, 
        COS, 
        ASIN, 
        ACOS;
    }
}
