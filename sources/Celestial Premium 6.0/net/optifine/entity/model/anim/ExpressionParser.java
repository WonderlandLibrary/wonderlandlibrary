/*
 * Decompiled with CFR 0.150.
 */
package net.optifine.entity.model.anim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.optifine.entity.model.anim.Constant;
import net.optifine.entity.model.anim.EnumFunctionType;
import net.optifine.entity.model.anim.EnumTokenType;
import net.optifine.entity.model.anim.Function;
import net.optifine.entity.model.anim.IExpression;
import net.optifine.entity.model.anim.IModelResolver;
import net.optifine.entity.model.anim.ParseException;
import net.optifine.entity.model.anim.Token;
import net.optifine.entity.model.anim.TokenParser;
import optifine.Config;

public class ExpressionParser {
    private IModelResolver modelResolver;

    public ExpressionParser(IModelResolver modelResolver) {
        this.modelResolver = modelResolver;
    }

    public IExpression parse(String str) throws ParseException {
        try {
            Token[] atoken = TokenParser.parse(str);
            if (atoken == null) {
                return null;
            }
            ArrayDeque<Token> deque = new ArrayDeque<Token>(Arrays.asList(atoken));
            return this.parseInfix(deque);
        }
        catch (IOException ioexception) {
            throw new ParseException(ioexception.getMessage(), ioexception);
        }
    }

    private IExpression parseInfix(Deque<Token> deque) throws ParseException {
        if (deque.isEmpty()) {
            return null;
        }
        LinkedList<IExpression> list = new LinkedList<IExpression>();
        LinkedList<Token> list1 = new LinkedList<Token>();
        IExpression iexpression = this.parseExpression(deque);
        ExpressionParser.checkNull(iexpression, "Missing expression");
        list.add(iexpression);
        Token token;
        while ((token = deque.poll()) != null) {
            if (token.getType() != EnumTokenType.OPERATOR) {
                throw new ParseException("Invalid operator: " + token);
            }
            IExpression iexpression1 = this.parseExpression(deque);
            ExpressionParser.checkNull(iexpression1, "Missing expression");
            list1.add(token);
            list.add(iexpression1);
        }
        return this.makeInfix(list, list1);
    }

    private IExpression makeInfix(List<IExpression> listExpr, List<Token> listOper) throws ParseException {
        LinkedList<EnumFunctionType> list = new LinkedList<EnumFunctionType>();
        for (Token token : listOper) {
            EnumFunctionType enumfunctiontype = EnumFunctionType.parse(token.getText());
            ExpressionParser.checkNull((Object)enumfunctiontype, "Invalid operator: " + token);
            list.add(enumfunctiontype);
        }
        return this.makeInfixFunc(listExpr, list);
    }

    private IExpression makeInfixFunc(List<IExpression> listExpr, List<EnumFunctionType> listFunc) throws ParseException {
        if (listExpr.size() != listFunc.size() + 1) {
            throw new ParseException("Invalid infix expression, expressions: " + listExpr.size() + ", operators: " + listFunc.size());
        }
        if (listExpr.size() == 1) {
            return listExpr.get(0);
        }
        int i = Integer.MAX_VALUE;
        int j = Integer.MIN_VALUE;
        for (EnumFunctionType enumfunctiontype : listFunc) {
            i = Math.min(enumfunctiontype.getPrecedence(), i);
            j = Math.max(enumfunctiontype.getPrecedence(), j);
        }
        if (j >= i && j - i <= 10) {
            for (int k = j; k >= i; --k) {
                this.mergeOperators(listExpr, listFunc, k);
            }
            if (listExpr.size() == 1 && listFunc.size() == 0) {
                return listExpr.get(0);
            }
            throw new ParseException("Error merging operators, expressions: " + listExpr.size() + ", operators: " + listFunc.size());
        }
        throw new ParseException("Invalid infix precedence, min: " + i + ", max: " + j);
    }

    private void mergeOperators(List<IExpression> listExpr, List<EnumFunctionType> listFuncs, int precedence) {
        for (int i = 0; i < listFuncs.size(); ++i) {
            EnumFunctionType enumfunctiontype = listFuncs.get(i);
            if (enumfunctiontype.getPrecedence() != precedence) continue;
            listFuncs.remove(i);
            IExpression iexpression = listExpr.remove(i);
            IExpression iexpression1 = listExpr.remove(i);
            Function iexpression2 = new Function(enumfunctiontype, new IExpression[]{iexpression, iexpression1});
            listExpr.add(i, iexpression2);
            --i;
        }
    }

    private IExpression parseExpression(Deque<Token> deque) throws ParseException {
        Token token = deque.poll();
        ExpressionParser.checkNull(token, "Missing expression");
        switch (token.getType()) {
            case CONSTANT: {
                return ExpressionParser.makeConstant(token);
            }
            case IDENTIFIER: {
                EnumFunctionType enumfunctiontype = this.getFunctionType(token, deque);
                if (enumfunctiontype != null) {
                    return this.makeFunction(enumfunctiontype, deque);
                }
                return this.makeVariable(token);
            }
            case BRACKET_OPEN: {
                return this.makeBracketed(token, deque);
            }
            case OPERATOR: {
                EnumFunctionType enumfunctiontype1 = EnumFunctionType.parse(token.getText());
                ExpressionParser.checkNull((Object)enumfunctiontype1, "Invalid operator: " + token);
                if (enumfunctiontype1 == EnumFunctionType.PLUS) {
                    return this.parseExpression(deque);
                }
                if (enumfunctiontype1 != EnumFunctionType.MINUS) break;
                IExpression iexpression = this.parseExpression(deque);
                return new Function(EnumFunctionType.NEG, new IExpression[]{iexpression});
            }
        }
        throw new ParseException("Invalid expression: " + token);
    }

    private static IExpression makeConstant(Token token) throws ParseException {
        float f = Config.parseFloat(token.getText(), Float.NaN);
        if (f == Float.NaN) {
            throw new ParseException("Invalid float value: " + token);
        }
        return new Constant(f);
    }

    private EnumFunctionType getFunctionType(Token token, Deque<Token> deque) throws ParseException {
        Token token1 = deque.peek();
        if (token1 != null && token1.getType() == EnumTokenType.BRACKET_OPEN) {
            EnumFunctionType enumfunctiontype1 = EnumFunctionType.parse(token1.getText());
            ExpressionParser.checkNull((Object)enumfunctiontype1, "Unknown function: " + token1);
            return enumfunctiontype1;
        }
        EnumFunctionType enumfunctiontype = EnumFunctionType.parse(token1.getText());
        if (enumfunctiontype == null) {
            return null;
        }
        if (enumfunctiontype.getCountArguments() > 0) {
            throw new ParseException("Missing arguments: " + (Object)((Object)enumfunctiontype));
        }
        return enumfunctiontype;
    }

    private IExpression makeFunction(EnumFunctionType type, Deque<Token> deque) throws ParseException {
        if (type.getCountArguments() == 0) {
            return ExpressionParser.makeFunction(type, new IExpression[0]);
        }
        Token token = deque.poll();
        Deque<Token> deque1 = ExpressionParser.getGroup(deque, EnumTokenType.BRACKET_CLOSE, true);
        IExpression[] aiexpression = this.parseExpressions(deque1);
        return ExpressionParser.makeFunction(type, aiexpression);
    }

    private IExpression[] parseExpressions(Deque<Token> deque) throws ParseException {
        ArrayList<IExpression> list = new ArrayList<IExpression>();
        while (true) {
            Deque<Token> deque1;
            IExpression iexpression;
            if ((iexpression = this.parseInfix(deque1 = ExpressionParser.getGroup(deque, EnumTokenType.COMMA, false))) == null) {
                IExpression[] aiexpression = list.toArray(new IExpression[list.size()]);
                return aiexpression;
            }
            list.add(iexpression);
        }
    }

    private static IExpression makeFunction(EnumFunctionType type, IExpression[] exprs) throws ParseException {
        if (type.getCountArguments() != exprs.length) {
            throw new ParseException("Invalid number of arguments: " + exprs.length + ", should be: " + type.getCountArguments() + ", function: " + type.getName());
        }
        return new Function(type, exprs);
    }

    private IExpression makeVariable(Token token) throws ParseException {
        if (this.modelResolver == null) {
            throw new ParseException("Model variable not found: " + token);
        }
        IExpression iexpression = this.modelResolver.getExpression(token.getText());
        if (iexpression == null) {
            throw new ParseException("Model variable not found: " + token);
        }
        return iexpression;
    }

    private IExpression makeBracketed(Token token, Deque<Token> deque) throws ParseException {
        Deque<Token> deque1 = ExpressionParser.getGroup(deque, EnumTokenType.BRACKET_CLOSE, true);
        return this.parseInfix(deque1);
    }

    private static Deque<Token> getGroup(Deque<Token> deque, EnumTokenType tokenTypeEnd, boolean tokenEndRequired) throws ParseException {
        ArrayDeque<Token> deque1 = new ArrayDeque<Token>();
        int i = 0;
        Iterator iterator = deque1.iterator();
        while (iterator.hasNext()) {
            Token token = (Token)iterator.next();
            iterator.remove();
            if (i == 0 && token.getType() == tokenTypeEnd) {
                return deque1;
            }
            deque1.add(token);
            if (token.getType() == EnumTokenType.BRACKET_OPEN) {
                ++i;
            }
            if (token.getType() != EnumTokenType.BRACKET_CLOSE) continue;
            --i;
        }
        if (tokenEndRequired) {
            throw new ParseException("Missing end token: " + (Object)((Object)tokenTypeEnd));
        }
        return deque1;
    }

    private static void checkNull(Object obj, String message) throws ParseException {
        if (obj == null) {
            throw new ParseException(message);
        }
    }

    public static void main(String[] args) throws Exception {
        ExpressionParser expressionparser = new ExpressionParser(null);
        while (true) {
            try {
                while (true) {
                    InputStreamReader inputstreamreader;
                    BufferedReader bufferedreader;
                    String s;
                    if ((s = (bufferedreader = new BufferedReader(inputstreamreader = new InputStreamReader(System.in))).readLine()).length() <= 0) {
                        return;
                    }
                    IExpression iexpression = expressionparser.parse(s);
                    float f = iexpression.eval();
                    Config.dbg("" + s + " = " + f);
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

