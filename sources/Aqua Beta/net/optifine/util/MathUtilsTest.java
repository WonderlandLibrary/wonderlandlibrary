package net.optifine.util;

import net.minecraft.util.MathHelper;
import net.optifine.util.MathUtils;
import net.optifine.util.MathUtilsTest;

/*
 * Exception performing whole class analysis ignored.
 */
public class MathUtilsTest {
    public static void main(String[] args) throws Exception {
        OPER[] amathutilstest$oper = OPER.values();
        for (int i = 0; i < amathutilstest$oper.length; ++i) {
            OPER mathutilstest$oper = amathutilstest$oper[i];
            MathUtilsTest.dbg("******** " + mathutilstest$oper + " ***********");
            MathUtilsTest.test(mathutilstest$oper, false);
        }
    }

    private static void test(OPER oper, boolean fast) {
        double d1;
        double d0;
        MathHelper.fastMath = fast;
        switch (1.$SwitchMap$net$optifine$util$MathUtilsTest$OPER[oper.ordinal()]) {
            case 1: 
            case 2: {
                d0 = -MathHelper.PI;
                d1 = MathHelper.PI;
                break;
            }
            case 3: 
            case 4: {
                d0 = -1.0;
                d1 = 1.0;
                break;
            }
            default: {
                return;
            }
        }
        int i = 10;
        for (int j = 0; j <= i; ++j) {
            float f1;
            float f;
            double d2 = d0 + (double)j * (d1 - d0) / (double)i;
            switch (1.$SwitchMap$net$optifine$util$MathUtilsTest$OPER[oper.ordinal()]) {
                case 1: {
                    f = (float)Math.sin((double)d2);
                    f1 = MathHelper.sin((float)((float)d2));
                    break;
                }
                case 2: {
                    f = (float)Math.cos((double)d2);
                    f1 = MathHelper.cos((float)((float)d2));
                    break;
                }
                case 3: {
                    f = (float)Math.asin((double)d2);
                    f1 = MathUtils.asin((float)((float)d2));
                    break;
                }
                case 4: {
                    f = (float)Math.acos((double)d2);
                    f1 = MathUtils.acos((float)((float)d2));
                    break;
                }
                default: {
                    return;
                }
            }
            MathUtilsTest.dbg(String.format((String)"%.2f, Math: %f, Helper: %f, diff: %f", (Object[])new Object[]{d2, Float.valueOf((float)f), Float.valueOf((float)f1), Float.valueOf((float)Math.abs((float)(f - f1)))}));
        }
    }

    public static void dbg(String str) {
        System.out.println(str);
    }
}
