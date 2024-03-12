// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.expr;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TestExpressions
{
    public static void main(final String[] args) throws Exception {
        final ExpressionParser expressionparser = new ExpressionParser(null);
    Label_0012_Outer:
        while (true) {
            while (true) {
                try {
                    while (true) {
                        final InputStreamReader inputstreamreader = new InputStreamReader(System.in);
                        final BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
                        final String s = bufferedreader.readLine();
                        if (s.length() <= 0) {
                            break;
                        }
                        final IExpression iexpression = expressionparser.parse(s);
                        if (iexpression instanceof IExpressionFloat) {
                            final IExpressionFloat iexpressionfloat = (IExpressionFloat)iexpression;
                            final float f = iexpressionfloat.eval();
                            System.out.println("" + f);
                        }
                        if (!(iexpression instanceof IExpressionBool)) {
                            continue Label_0012_Outer;
                        }
                        final IExpressionBool iexpressionbool = (IExpressionBool)iexpression;
                        final boolean flag = iexpressionbool.eval();
                        System.out.println("" + flag);
                    }
                    return;
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    continue Label_0012_Outer;
                }
                continue;
            }
        }
    }
}
