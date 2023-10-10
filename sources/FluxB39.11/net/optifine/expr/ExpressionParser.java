package net.optifine.expr;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.src.Config;
import net.optifine.expr.ConstantFloat;
import net.optifine.expr.ExpressionType;
import net.optifine.expr.FunctionBool;
import net.optifine.expr.FunctionFloat;
import net.optifine.expr.FunctionFloatArray;
import net.optifine.expr.FunctionType;
import net.optifine.expr.IExpression;
import net.optifine.expr.IExpressionBool;
import net.optifine.expr.IExpressionFloat;
import net.optifine.expr.IExpressionResolver;
import net.optifine.expr.ParseException;
import net.optifine.expr.Token;
import net.optifine.expr.TokenParser;
import net.optifine.expr.TokenType;

public class ExpressionParser {
    private IExpressionResolver expressionResolver;

    public ExpressionParser(IExpressionResolver expressionResolver) {
        this.expressionResolver = expressionResolver;
    }

    public IExpressionFloat parseFloat(String str) throws ParseException {
        IExpression expr = this.parse(str);
        if (!(expr instanceof IExpressionFloat)) {
            throw new ParseException("Not a float expression: " + (Object)((Object)expr.getExpressionType()));
        }
        return (IExpressionFloat)expr;
    }

    public IExpressionBool parseBool(String str) throws ParseException {
        IExpression expr = this.parse(str);
        if (!(expr instanceof IExpressionBool)) {
            throw new ParseException("Not a boolean expression: " + (Object)((Object)expr.getExpressionType()));
        }
        return (IExpressionBool)expr;
    }

    public IExpression parse(String str) throws ParseException {
        try {
            Token[] tokens = TokenParser.parse(str);
            if (tokens == null) {
                return null;
            }
            ArrayDeque<Token> deque = new ArrayDeque<Token>(Arrays.asList(tokens));
            return this.parseInfix(deque);
        }
        catch (IOException e) {
            throw new ParseException(e.getMessage(), e);
        }
    }

    private IExpression parseInfix(Deque<Token> deque) throws ParseException {
        Token tokenOper;
        if (deque.isEmpty()) {
            return null;
        }
        LinkedList<IExpression> listExpr = new LinkedList<IExpression>();
        LinkedList<Token> listOperTokens = new LinkedList<Token>();
        IExpression expr = this.parseExpression(deque);
        ExpressionParser.checkNull(expr, "Missing expression");
        listExpr.add(expr);
        while ((tokenOper = deque.poll()) != null) {
            if (tokenOper.getType() != TokenType.OPERATOR) {
                throw new ParseException("Invalid operator: " + tokenOper);
            }
            IExpression expr2 = this.parseExpression(deque);
            ExpressionParser.checkNull(expr2, "Missing expression");
            listOperTokens.add(tokenOper);
            listExpr.add(expr2);
        }
        return this.makeInfix(listExpr, listOperTokens);
    }

    private IExpression makeInfix(List<IExpression> listExpr, List<Token> listOper) throws ParseException {
        LinkedList<FunctionType> listFunc = new LinkedList<FunctionType>();
        for (Token token : listOper) {
            FunctionType type = FunctionType.parse(token.getText());
            ExpressionParser.checkNull((Object)type, "Invalid operator: " + token);
            listFunc.add(type);
        }
        return this.makeInfixFunc(listExpr, listFunc);
    }

    private IExpression makeInfixFunc(List<IExpression> listExpr, List<FunctionType> listFunc) throws ParseException {
        if (listExpr.size() != listFunc.size() + 1) {
            throw new ParseException("Invalid infix expression, expressions: " + listExpr.size() + ", operators: " + listFunc.size());
        }
        if (listExpr.size() == 1) {
            return listExpr.get(0);
        }
        int minPrecedence = Integer.MAX_VALUE;
        int maxPrecedence = Integer.MIN_VALUE;
        for (FunctionType type : listFunc) {
            minPrecedence = Math.min(type.getPrecedence(), minPrecedence);
            maxPrecedence = Math.max(type.getPrecedence(), maxPrecedence);
        }
        if (maxPrecedence < minPrecedence || maxPrecedence - minPrecedence > 10) {
            throw new ParseException("Invalid infix precedence, min: " + minPrecedence + ", max: " + maxPrecedence);
        }
        for (int i = maxPrecedence; i >= minPrecedence; --i) {
            this.mergeOperators(listExpr, listFunc, i);
        }
        if (listExpr.size() != 1 || listFunc.size() != 0) {
            throw new ParseException("Error merging operators, expressions: " + listExpr.size() + ", operators: " + listFunc.size());
        }
        return listExpr.get(0);
    }

    private void mergeOperators(List<IExpression> listExpr, List<FunctionType> listFuncs, int precedence) throws ParseException {
        for (int i = 0; i < listFuncs.size(); ++i) {
            FunctionType type = listFuncs.get(i);
            if (type.getPrecedence() != precedence) continue;
            listFuncs.remove(i);
            IExpression expr1 = listExpr.remove(i);
            IExpression expr2 = listExpr.remove(i);
            IExpression exprOper = ExpressionParser.makeFunction(type, new IExpression[]{expr1, expr2});
            listExpr.add(i, exprOper);
            --i;
        }
    }

    private IExpression parseExpression(Deque<Token> deque) throws ParseException {
        Token token = deque.poll();
        ExpressionParser.checkNull(token, "Missing expression");
        switch (token.getType()) {
            case NUMBER: {
                return ExpressionParser.makeConstantFloat(token);
            }
            case IDENTIFIER: {
                FunctionType type = this.getFunctionType(token, deque);
                if (type != null) {
                    return this.makeFunction(type, deque);
                }
                return this.makeVariable(token);
            }
            case BRACKET_OPEN: {
                return this.makeBracketed(token, deque);
            }
            case OPERATOR: {
                FunctionType operType = FunctionType.parse(token.getText());
                ExpressionParser.checkNull((Object)operType, "Invalid operator: " + token);
                if (operType == FunctionType.PLUS) {
                    return this.parseExpression(deque);
                }
                if (operType == FunctionType.MINUS) {
                    IExpression exprNeg = this.parseExpression(deque);
                    return ExpressionParser.makeFunction(FunctionType.NEG, new IExpression[]{exprNeg});
                }
                if (operType != FunctionType.NOT) break;
                IExpression exprNot = this.parseExpression(deque);
                return ExpressionParser.makeFunction(FunctionType.NOT, new IExpression[]{exprNot});
            }
		default:
			break;
        }
        throw new ParseException("Invalid expression: " + token);
    }

    private static IExpression makeConstantFloat(Token token) throws ParseException {
        float val = Config.parseFloat((String)token.getText(), (float)Float.NaN);
        if (val == Float.NaN) {
            throw new ParseException("Invalid float value: " + token);
        }
        return new ConstantFloat(val);
    }

    private FunctionType getFunctionType(Token token, Deque<Token> deque) throws ParseException {
        Token tokenNext = deque.peek();
        if (tokenNext != null && tokenNext.getType() == TokenType.BRACKET_OPEN) {
            FunctionType type = FunctionType.parse(token.getText());
            ExpressionParser.checkNull((Object)type, "Unknown function: " + token);
            return type;
        }
        FunctionType type = FunctionType.parse(token.getText());
        if (type == null) {
            return null;
        }
        if (type.getParameterCount(new IExpression[0]) > 0) {
            throw new ParseException("Missing arguments: " + (Object)((Object)type));
        }
        return type;
    }

    private IExpression makeFunction(FunctionType type, Deque<Token> deque) throws ParseException {
        Token tokenNext;
        if (type.getParameterCount(new IExpression[0]) == 0 && ((tokenNext = deque.peek()) == null || tokenNext.getType() != TokenType.BRACKET_OPEN)) {
            return ExpressionParser.makeFunction(type, new IExpression[0]);
        }
        Token tokenOpen = deque.poll();
        Deque<Token> dequeBracketed = ExpressionParser.getGroup(deque, TokenType.BRACKET_CLOSE, true);
        IExpression[] exprs = this.parseExpressions(dequeBracketed);
        return ExpressionParser.makeFunction(type, exprs);
    }

    private IExpression[] parseExpressions(Deque<Token> deque) throws ParseException {
        Deque<Token> dequeArg;
        IExpression expr;
        ArrayList<IExpression> list = new ArrayList<IExpression>();
        while ((expr = this.parseInfix(dequeArg = ExpressionParser.getGroup(deque, TokenType.COMMA, false))) != null) {
            list.add(expr);
        }
        IExpression[] exprs = list.toArray(new IExpression[list.size()]);
        return exprs;
    }

    private static IExpression makeFunction(FunctionType type, IExpression[] args) throws ParseException {
        ExpressionType[] funcParamTypes = type.getParameterTypes(args);
        if (args.length != funcParamTypes.length) {
            throw new ParseException("Invalid number of arguments, function: \"" + type.getName() + "\", count arguments: " + args.length + ", should be: " + funcParamTypes.length);
        }
        for (int i = 0; i < args.length; ++i) {
            ExpressionType funcParamType;
            IExpression arg = args[i];
            ExpressionType argType = arg.getExpressionType();
            if (argType == (funcParamType = funcParamTypes[i])) continue;
            throw new ParseException("Invalid argument type, function: \"" + type.getName() + "\", index: " + i + ", type: " + (Object)((Object)argType) + ", should be: " + (Object)((Object)funcParamType));
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
        throw new ParseException("Unknown function type: " + (Object)((Object)type.getExpressionType()) + ", function: " + type.getName());
    }

    private IExpression makeVariable(Token token) throws ParseException {
        if (this.expressionResolver == null) {
            throw new ParseException("Model variable not found: " + token);
        }
        IExpression expr = this.expressionResolver.getExpression(token.getText());
        if (expr == null) {
            throw new ParseException("Model variable not found: " + token);
        }
        return expr;
    }

    private IExpression makeBracketed(Token token, Deque<Token> deque) throws ParseException {
        Deque<Token> dequeBracketed = ExpressionParser.getGroup(deque, TokenType.BRACKET_CLOSE, true);
        return this.parseInfix(dequeBracketed);
    }

    private static Deque<Token> getGroup(Deque<Token> deque, TokenType tokenTypeEnd, boolean tokenEndRequired) throws ParseException {
        ArrayDeque<Token> dequeGroup = new ArrayDeque<Token>();
        int level = 0;
        Iterator<Token> it = deque.iterator();
        while (it.hasNext()) {
            Token token = it.next();
            it.remove();
            if (level == 0 && token.getType() == tokenTypeEnd) {
                return dequeGroup;
            }
            dequeGroup.add(token);
            if (token.getType() == TokenType.BRACKET_OPEN) {
                ++level;
            }
            if (token.getType() != TokenType.BRACKET_CLOSE) continue;
            --level;
        }
        if (tokenEndRequired) {
            throw new ParseException("Missing end token: " + (Object)((Object)tokenTypeEnd));
        }
        return dequeGroup;
    }

    private static void checkNull(Object obj, String message) throws ParseException {
        if (obj == null) {
            throw new ParseException(message);
        }
    }

}

