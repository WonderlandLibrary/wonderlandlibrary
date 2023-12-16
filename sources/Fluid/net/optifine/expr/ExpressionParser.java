// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.expr;

import java.util.ArrayList;
import net.minecraft.src.Config;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Deque;
import java.io.IOException;
import java.util.Collection;
import java.util.ArrayDeque;
import java.util.Arrays;

public class ExpressionParser
{
    private IExpressionResolver expressionResolver;
    
    public ExpressionParser(final IExpressionResolver expressionResolver) {
        this.expressionResolver = expressionResolver;
    }
    
    public IExpressionFloat parseFloat(final String str) throws ParseException {
        final IExpression iexpression = this.parse(str);
        if (!(iexpression instanceof IExpressionFloat)) {
            throw new ParseException("Not a float expression: " + iexpression.getExpressionType());
        }
        return (IExpressionFloat)iexpression;
    }
    
    public IExpressionBool parseBool(final String str) throws ParseException {
        final IExpression iexpression = this.parse(str);
        if (!(iexpression instanceof IExpressionBool)) {
            throw new ParseException("Not a boolean expression: " + iexpression.getExpressionType());
        }
        return (IExpressionBool)iexpression;
    }
    
    public IExpression parse(final String str) throws ParseException {
        try {
            final Token[] atoken = TokenParser.parse(str);
            if (atoken == null) {
                return null;
            }
            final Deque<Token> deque = new ArrayDeque<Token>(Arrays.asList(atoken));
            return this.parseInfix(deque);
        }
        catch (final IOException ioexception) {
            throw new ParseException(ioexception.getMessage(), ioexception);
        }
    }
    
    private IExpression parseInfix(final Deque<Token> deque) throws ParseException {
        if (deque.isEmpty()) {
            return null;
        }
        final List<IExpression> list = new LinkedList<IExpression>();
        final List<Token> list2 = new LinkedList<Token>();
        final IExpression iexpression = this.parseExpression(deque);
        checkNull(iexpression, "Missing expression");
        list.add(iexpression);
        while (true) {
            final Token token = deque.poll();
            if (token == null) {
                return this.makeInfix(list, list2);
            }
            if (token.getType() != TokenType.OPERATOR) {
                throw new ParseException("Invalid operator: " + token);
            }
            final IExpression iexpression2 = this.parseExpression(deque);
            checkNull(iexpression2, "Missing expression");
            list2.add(token);
            list.add(iexpression2);
        }
    }
    
    private IExpression makeInfix(final List<IExpression> listExpr, final List<Token> listOper) throws ParseException {
        final List<FunctionType> list = new LinkedList<FunctionType>();
        for (final Token token : listOper) {
            final FunctionType functiontype = FunctionType.parse(token.getText());
            checkNull(functiontype, "Invalid operator: " + token);
            list.add(functiontype);
        }
        return this.makeInfixFunc(listExpr, list);
    }
    
    private IExpression makeInfixFunc(final List<IExpression> listExpr, final List<FunctionType> listFunc) throws ParseException {
        if (listExpr.size() != listFunc.size() + 1) {
            throw new ParseException("Invalid infix expression, expressions: " + listExpr.size() + ", operators: " + listFunc.size());
        }
        if (listExpr.size() == 1) {
            return listExpr.get(0);
        }
        int i = Integer.MAX_VALUE;
        int j = Integer.MIN_VALUE;
        for (final FunctionType functiontype : listFunc) {
            i = Math.min(functiontype.getPrecedence(), i);
            j = Math.max(functiontype.getPrecedence(), j);
        }
        if (j < i || j - i > 10) {
            throw new ParseException("Invalid infix precedence, min: " + i + ", max: " + j);
        }
        for (int k = j; k >= i; --k) {
            this.mergeOperators(listExpr, listFunc, k);
        }
        if (listExpr.size() == 1 && listFunc.size() == 0) {
            return listExpr.get(0);
        }
        throw new ParseException("Error merging operators, expressions: " + listExpr.size() + ", operators: " + listFunc.size());
    }
    
    private void mergeOperators(final List<IExpression> listExpr, final List<FunctionType> listFuncs, final int precedence) throws ParseException {
        for (int i = 0; i < listFuncs.size(); ++i) {
            final FunctionType functiontype = listFuncs.get(i);
            if (functiontype.getPrecedence() == precedence) {
                listFuncs.remove(i);
                final IExpression iexpression = listExpr.remove(i);
                final IExpression iexpression2 = listExpr.remove(i);
                final IExpression iexpression3 = makeFunction(functiontype, new IExpression[] { iexpression, iexpression2 });
                listExpr.add(i, iexpression3);
                --i;
            }
        }
    }
    
    private IExpression parseExpression(final Deque<Token> deque) throws ParseException {
        final Token token = deque.poll();
        checkNull(token, "Missing expression");
        switch (token.getType()) {
            case NUMBER: {
                return makeConstantFloat(token);
            }
            case IDENTIFIER: {
                final FunctionType functiontype = this.getFunctionType(token, deque);
                if (functiontype != null) {
                    return this.makeFunction(functiontype, deque);
                }
                return this.makeVariable(token);
            }
            case BRACKET_OPEN: {
                return this.makeBracketed(token, deque);
            }
            case OPERATOR: {
                final FunctionType functiontype2 = FunctionType.parse(token.getText());
                checkNull(functiontype2, "Invalid operator: " + token);
                if (functiontype2 == FunctionType.PLUS) {
                    return this.parseExpression(deque);
                }
                if (functiontype2 == FunctionType.MINUS) {
                    final IExpression iexpression1 = this.parseExpression(deque);
                    return makeFunction(FunctionType.NEG, new IExpression[] { iexpression1 });
                }
                if (functiontype2 == FunctionType.NOT) {
                    final IExpression iexpression2 = this.parseExpression(deque);
                    return makeFunction(FunctionType.NOT, new IExpression[] { iexpression2 });
                }
                break;
            }
        }
        throw new ParseException("Invalid expression: " + token);
    }
    
    private static IExpression makeConstantFloat(final Token token) throws ParseException {
        final float f = Config.parseFloat(token.getText(), Float.NaN);
        if (f == Float.NaN) {
            throw new ParseException("Invalid float value: " + token);
        }
        return new ConstantFloat(f);
    }
    
    private FunctionType getFunctionType(final Token token, final Deque<Token> deque) throws ParseException {
        final Token token2 = deque.peek();
        if (token2 != null && token2.getType() == TokenType.BRACKET_OPEN) {
            final FunctionType enumfunctiontype1 = FunctionType.parse(token2.getText());
            checkNull(enumfunctiontype1, "Unknown function: " + token2);
            return enumfunctiontype1;
        }
        final FunctionType functiontype = FunctionType.parse(token.getText());
        if (functiontype == null) {
            return null;
        }
        if (functiontype.getParameterCount(new IExpression[0]) > 0) {
            throw new ParseException("Missing arguments: " + functiontype);
        }
        return functiontype;
    }
    
    private IExpression makeFunction(final FunctionType type, final Deque<Token> deque) throws ParseException {
        if (type.getParameterCount(new IExpression[0]) == 0) {
            final Token token = deque.peek();
            if (token == null || token.getType() != TokenType.BRACKET_OPEN) {
                return makeFunction(type, new IExpression[0]);
            }
        }
        final Token token2 = deque.poll();
        final Deque<Token> deque2 = getGroup(deque, TokenType.BRACKET_CLOSE, true);
        final IExpression[] aiexpression = this.parseExpressions(deque2);
        return makeFunction(type, aiexpression);
    }
    
    private IExpression[] parseExpressions(final Deque<Token> deque) throws ParseException {
        final List<IExpression> list = new ArrayList<IExpression>();
        while (true) {
            final Deque<Token> deque2 = getGroup(deque, TokenType.COMMA, false);
            final IExpression iexpression = this.parseInfix(deque2);
            if (iexpression == null) {
                break;
            }
            list.add(iexpression);
        }
        final IExpression[] aiexpression = list.toArray(new IExpression[list.size()]);
        return aiexpression;
    }
    
    private static IExpression makeFunction(final FunctionType type, final IExpression[] args) throws ParseException {
        final ExpressionType[] aexpressiontype = type.getParameterTypes(args);
        if (args.length != aexpressiontype.length) {
            throw new ParseException("Invalid number of arguments, function: \"" + type.getName() + "\", count arguments: " + args.length + ", should be: " + aexpressiontype.length);
        }
        for (int i = 0; i < args.length; ++i) {
            final IExpression iexpression = args[i];
            final ExpressionType expressiontype = iexpression.getExpressionType();
            final ExpressionType expressiontype2 = aexpressiontype[i];
            if (expressiontype != expressiontype2) {
                throw new ParseException("Invalid argument type, function: \"" + type.getName() + "\", index: " + i + ", type: " + expressiontype + ", should be: " + expressiontype2);
            }
        }
        if (type.getExpressionType() == ExpressionType.FLOAT) {
            return new FunctionFloat(type, args);
        }
        if (type.getExpressionType() == ExpressionType.BOOL) {
            return new FunctionBool(type, args);
        }
        if (type.getExpressionType() == ExpressionType.FLOAT_ARRAY) {
            return new FunctionFloatArray(type, args);
        }
        throw new ParseException("Unknown function type: " + type.getExpressionType() + ", function: " + type.getName());
    }
    
    private IExpression makeVariable(final Token token) throws ParseException {
        if (this.expressionResolver == null) {
            throw new ParseException("Model variable not found: " + token);
        }
        final IExpression iexpression = this.expressionResolver.getExpression(token.getText());
        if (iexpression == null) {
            throw new ParseException("Model variable not found: " + token);
        }
        return iexpression;
    }
    
    private IExpression makeBracketed(final Token token, final Deque<Token> deque) throws ParseException {
        final Deque<Token> deque2 = getGroup(deque, TokenType.BRACKET_CLOSE, true);
        return this.parseInfix(deque2);
    }
    
    private static Deque<Token> getGroup(final Deque<Token> deque, final TokenType tokenTypeEnd, final boolean tokenEndRequired) throws ParseException {
        final Deque<Token> deque2 = new ArrayDeque<Token>();
        int i = 0;
        final Iterator iterator = deque.iterator();
        while (iterator.hasNext()) {
            final Token token = iterator.next();
            iterator.remove();
            if (i == 0 && token.getType() == tokenTypeEnd) {
                return deque2;
            }
            deque2.add(token);
            if (token.getType() == TokenType.BRACKET_OPEN) {
                ++i;
            }
            if (token.getType() != TokenType.BRACKET_CLOSE) {
                continue;
            }
            --i;
        }
        if (tokenEndRequired) {
            throw new ParseException("Missing end token: " + tokenTypeEnd);
        }
        return deque2;
    }
    
    private static void checkNull(final Object obj, final String message) throws ParseException {
        if (obj == null) {
            throw new ParseException(message);
        }
    }
}
