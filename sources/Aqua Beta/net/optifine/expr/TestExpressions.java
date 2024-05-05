package net.optifine.expr;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import net.optifine.expr.ExpressionParser;
import net.optifine.expr.IExpression;
import net.optifine.expr.IExpressionBool;
import net.optifine.expr.IExpressionFloat;
import net.optifine.expr.IExpressionResolver;

public class TestExpressions {
    public static void main(String[] args) throws Exception {
        ExpressionParser expressionparser = new ExpressionParser((IExpressionResolver)null);
        while (true) {
            try {
                while (true) {
                    InputStreamReader inputstreamreader;
                    BufferedReader bufferedreader;
                    String s;
                    if ((s = (bufferedreader = new BufferedReader((Reader)(inputstreamreader = new InputStreamReader(System.in)))).readLine()).length() <= 0) {
                        return;
                    }
                    IExpression iexpression = expressionparser.parse(s);
                    if (iexpression instanceof IExpressionFloat) {
                        IExpressionFloat iexpressionfloat = (IExpressionFloat)iexpression;
                        float f = iexpressionfloat.eval();
                        System.out.println("" + f);
                    }
                    if (!(iexpression instanceof IExpressionBool)) continue;
                    IExpressionBool iexpressionbool = (IExpressionBool)iexpression;
                    boolean flag = iexpressionbool.eval();
                    System.out.println("" + flag);
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                continue;
            }
            break;
        }
    }
}
