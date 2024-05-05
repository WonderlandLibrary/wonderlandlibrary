/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.parser;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import jdk.internal.dynalink.support.NameCodec;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.codegen.Namespace;
import jdk.nashorn.internal.ir.AccessNode;
import jdk.nashorn.internal.ir.BaseNode;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.BlockLexicalContext;
import jdk.nashorn.internal.ir.BlockStatement;
import jdk.nashorn.internal.ir.BreakNode;
import jdk.nashorn.internal.ir.BreakableNode;
import jdk.nashorn.internal.ir.CallNode;
import jdk.nashorn.internal.ir.CaseNode;
import jdk.nashorn.internal.ir.CatchNode;
import jdk.nashorn.internal.ir.ContinueNode;
import jdk.nashorn.internal.ir.EmptyNode;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.ExpressionStatement;
import jdk.nashorn.internal.ir.ForNode;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.ir.IndexNode;
import jdk.nashorn.internal.ir.JoinPredecessorExpression;
import jdk.nashorn.internal.ir.LabelNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.LoopNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.ObjectNode;
import jdk.nashorn.internal.ir.PropertyKey;
import jdk.nashorn.internal.ir.PropertyNode;
import jdk.nashorn.internal.ir.ReturnNode;
import jdk.nashorn.internal.ir.RuntimeNode;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.SwitchNode;
import jdk.nashorn.internal.ir.TernaryNode;
import jdk.nashorn.internal.ir.ThrowNode;
import jdk.nashorn.internal.ir.TryNode;
import jdk.nashorn.internal.ir.UnaryNode;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.ir.WhileNode;
import jdk.nashorn.internal.ir.WithNode;
import jdk.nashorn.internal.ir.debug.ASTWriter;
import jdk.nashorn.internal.ir.debug.PrintVisitor;
import jdk.nashorn.internal.parser.AbstractParser;
import jdk.nashorn.internal.parser.Lexer;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.internal.parser.TokenKind;
import jdk.nashorn.internal.parser.TokenStream;
import jdk.nashorn.internal.parser.TokenType;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.JSErrorType;
import jdk.nashorn.internal.runtime.ParserException;
import jdk.nashorn.internal.runtime.RecompilableScriptFunctionData;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.Timing;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.runtime.logging.Logger;

@Logger(name="parser")
public class Parser
extends AbstractParser
implements Loggable {
    private static final String ARGUMENTS_NAME = CompilerConstants.ARGUMENTS_VAR.symbolName();
    private final ScriptEnvironment env;
    private final boolean scripting;
    private List<Statement> functionDeclarations;
    private final BlockLexicalContext lc = new BlockLexicalContext();
    private final Deque<Object> defaultNames = new ArrayDeque<Object>();
    private final Namespace namespace;
    private final DebugLogger log;
    protected final Lexer.LineInfoReceiver lineInfoReceiver;
    private RecompilableScriptFunctionData reparsedFunction;

    public Parser(ScriptEnvironment env, Source source, ErrorManager errors) {
        this(env, source, errors, env._strict, null);
    }

    public Parser(ScriptEnvironment env, Source source, ErrorManager errors, boolean strict, DebugLogger log) {
        this(env, source, errors, strict, 0, log);
    }

    public Parser(ScriptEnvironment env, Source source, ErrorManager errors, boolean strict, int lineOffset, DebugLogger log) {
        super(source, errors, strict, lineOffset);
        this.env = env;
        this.namespace = new Namespace(env.getNamespace());
        this.scripting = env._scripting;
        this.lineInfoReceiver = this.scripting ? new Lexer.LineInfoReceiver(){

            @Override
            public void lineInfo(int receiverLine, int receiverLinePosition) {
                Parser.this.line = receiverLine;
                Parser.this.linePosition = receiverLinePosition;
            }
        } : null;
        this.log = log == null ? DebugLogger.DISABLED_LOGGER : log;
    }

    @Override
    public DebugLogger getLogger() {
        return this.log;
    }

    @Override
    public DebugLogger initLogger(Context context) {
        return context.getLogger(this.getClass());
    }

    public void setFunctionName(String name) {
        this.defaultNames.push(this.createIdentNode(0L, 0, name));
    }

    public void setReparsedFunction(RecompilableScriptFunctionData reparsedFunction) {
        this.reparsedFunction = reparsedFunction;
    }

    public FunctionNode parse() {
        return this.parse(CompilerConstants.PROGRAM.symbolName(), 0, this.source.getLength(), false);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public FunctionNode parse(String scriptName, int startPos, int len, boolean allowPropertyFunction) {
        FunctionNode functionNode;
        block8: {
            String end;
            block6: {
                boolean isTimingEnabled = this.env.isTimingEnabled();
                long t0 = isTimingEnabled ? System.nanoTime() : 0L;
                this.log.info(this, " begin for '", scriptName, "'");
                try {
                    this.stream = new TokenStream();
                    this.lexer = new Lexer(this.source, startPos, len, this.stream, this.scripting && !this.env._no_syntax_extensions, this.reparsedFunction != null);
                    this.lexer.line = this.lexer.pendingLine = this.lineOffset + 1;
                    this.line = this.lineOffset;
                    this.k = -1;
                    this.next();
                    functionNode = this.program(scriptName, allowPropertyFunction);
                    end = this + " end '" + scriptName + "'";
                    if (!isTimingEnabled) break block6;
                    this.env._timing.accumulateTime(this.toString(), System.nanoTime() - t0);
                }
                catch (Exception e) {
                    FunctionNode functionNode2;
                    block9: {
                        String end2;
                        block7: {
                            try {
                                this.handleParseException(e);
                                functionNode2 = null;
                                end2 = this + " end '" + scriptName + "'";
                                if (!isTimingEnabled) break block7;
                                this.env._timing.accumulateTime(this.toString(), System.nanoTime() - t0);
                            }
                            catch (Throwable throwable) {
                                String end3 = this + " end '" + scriptName + "'";
                                if (isTimingEnabled) {
                                    this.env._timing.accumulateTime(this.toString(), System.nanoTime() - t0);
                                    this.log.info(end3, "' in ", Timing.toMillisPrint(System.nanoTime() - t0), " ms");
                                } else {
                                    this.log.info(end3);
                                }
                                throw throwable;
                            }
                            this.log.info(end2, "' in ", Timing.toMillisPrint(System.nanoTime() - t0), " ms");
                            break block9;
                        }
                        this.log.info(end2);
                    }
                    return functionNode2;
                }
                this.log.info(end, "' in ", Timing.toMillisPrint(System.nanoTime() - t0), " ms");
                break block8;
            }
            this.log.info(end);
        }
        return functionNode;
    }

    public List<IdentNode> parseFormalParameterList() {
        try {
            this.stream = new TokenStream();
            this.lexer = new Lexer(this.source, this.stream, this.scripting && !this.env._no_syntax_extensions);
            this.k = -1;
            this.next();
            return this.formalParameterList(TokenType.EOF);
        }
        catch (Exception e) {
            this.handleParseException(e);
            return null;
        }
    }

    public FunctionNode parseFunctionBody() {
        try {
            this.stream = new TokenStream();
            this.lexer = new Lexer(this.source, this.stream, this.scripting && !this.env._no_syntax_extensions);
            int functionLine = this.line;
            this.k = -1;
            this.next();
            long functionToken = Token.toDesc(TokenType.FUNCTION, 0, this.source.getLength());
            FunctionNode function = this.newFunctionNode(functionToken, new IdentNode(functionToken, Token.descPosition(functionToken), CompilerConstants.PROGRAM.symbolName()), new ArrayList<IdentNode>(), FunctionNode.Kind.NORMAL, functionLine);
            this.functionDeclarations = new ArrayList<Statement>();
            this.sourceElements(false);
            this.addFunctionDeclarations(function);
            this.functionDeclarations = null;
            this.expect(TokenType.EOF);
            function.setFinish(this.source.getLength() - 1);
            function = this.restoreFunctionNode(function, this.token);
            function = function.setBody(this.lc, function.getBody().setNeedsScope(this.lc));
            this.printAST(function);
            return function;
        }
        catch (Exception e) {
            this.handleParseException(e);
            return null;
        }
    }

    private void handleParseException(Exception e) {
        String message = e.getMessage();
        if (message == null) {
            message = e.toString();
        }
        if (e instanceof ParserException) {
            this.errors.error((ParserException)e);
        } else {
            this.errors.error(message);
        }
        if (this.env._dump_on_error) {
            e.printStackTrace(this.env.getErr());
        }
    }

    private void recover(Exception e) {
        if (e != null) {
            String message = e.getMessage();
            if (message == null) {
                message = e.toString();
            }
            if (e instanceof ParserException) {
                this.errors.error((ParserException)e);
            } else {
                this.errors.error(message);
            }
            if (this.env._dump_on_error) {
                e.printStackTrace(this.env.getErr());
            }
        }
        block4: while (true) {
            switch (this.type) {
                case EOF: {
                    break block4;
                }
                case EOL: 
                case SEMICOLON: 
                case RBRACE: {
                    this.next();
                    break block4;
                }
                default: {
                    this.nextOrEOL();
                    continue block4;
                }
            }
            break;
        }
    }

    private Block newBlock() {
        return this.lc.push(new Block(this.token, Token.descPosition(this.token), new Statement[0]));
    }

    private FunctionNode newFunctionNode(long startToken, IdentNode ident, List<IdentNode> parameters, FunctionNode.Kind kind, int functionLine) {
        StringBuilder sb = new StringBuilder();
        FunctionNode parentFunction = this.lc.getCurrentFunction();
        if (parentFunction != null && !parentFunction.isProgram()) {
            sb.append(parentFunction.getName()).append(CompilerConstants.NESTED_FUNCTION_SEPARATOR.symbolName());
        }
        assert (ident.getName() != null);
        sb.append(ident.getName());
        String name = this.namespace.uniqueName(sb.toString());
        assert (parentFunction != null || name.equals(CompilerConstants.PROGRAM.symbolName()) || name.startsWith("Recompilation$")) : "name = " + name;
        int flags = 0;
        if (this.isStrictMode) {
            flags |= 4;
        }
        if (parentFunction == null) {
            flags |= 0x2000;
        }
        FunctionNode functionNode = new FunctionNode(this.source, functionLine, this.token, Token.descPosition(this.token), startToken, this.namespace, ident, name, parameters, kind, flags);
        this.lc.push(functionNode);
        this.newBlock();
        return functionNode;
    }

    private Block restoreBlock(Block block) {
        return this.lc.pop(block);
    }

    private FunctionNode restoreFunctionNode(FunctionNode functionNode, long lastToken) {
        Block newBody = this.restoreBlock(this.lc.getFunctionBody(functionNode));
        return this.lc.pop(functionNode).setBody(this.lc, newBody).setLastToken(this.lc, lastToken);
    }

    private Block getBlock(boolean needsBraces) {
        Block newBlock = this.newBlock();
        try {
            if (needsBraces) {
                this.expect(TokenType.LBRACE);
            }
            this.statementList();
        }
        finally {
            newBlock = this.restoreBlock(newBlock);
        }
        int possibleEnd = Token.descPosition(this.token) + Token.descLength(this.token);
        if (needsBraces) {
            this.expect(TokenType.RBRACE);
        }
        newBlock.setFinish(possibleEnd);
        return newBlock;
    }

    private Block getStatement() {
        if (this.type == TokenType.LBRACE) {
            return this.getBlock(true);
        }
        Block newBlock = this.newBlock();
        try {
            this.statement(false, false, true);
        }
        finally {
            newBlock = this.restoreBlock(newBlock);
        }
        return newBlock;
    }

    private void detectSpecialFunction(IdentNode ident) {
        String name = ident.getName();
        if (CompilerConstants.EVAL.symbolName().equals(name)) {
            Parser.markEval(this.lc);
        }
    }

    private void detectSpecialProperty(IdentNode ident) {
        if (Parser.isArguments(ident)) {
            this.lc.setFlag(this.lc.getCurrentFunction(), 8);
        }
    }

    private boolean useBlockScope() {
        return this.env._es6;
    }

    private static boolean isArguments(String name) {
        return ARGUMENTS_NAME.equals(name);
    }

    private static boolean isArguments(IdentNode ident) {
        return Parser.isArguments(ident.getName());
    }

    private static boolean checkIdentLValue(IdentNode ident) {
        return ident.tokenType().getKind() != TokenKind.KEYWORD;
    }

    private Expression verifyAssignment(long op, Expression lhs, Expression rhs) {
        TokenType opType = Token.descType(op);
        switch (opType) {
            case ASSIGN: 
            case ASSIGN_ADD: 
            case ASSIGN_BIT_AND: 
            case ASSIGN_BIT_OR: 
            case ASSIGN_BIT_XOR: 
            case ASSIGN_DIV: 
            case ASSIGN_MOD: 
            case ASSIGN_MUL: 
            case ASSIGN_SAR: 
            case ASSIGN_SHL: 
            case ASSIGN_SHR: 
            case ASSIGN_SUB: {
                if (!(lhs instanceof AccessNode || lhs instanceof IndexNode || lhs instanceof IdentNode)) {
                    return this.referenceError(lhs, rhs, this.env._early_lvalue_error);
                }
                if (!(lhs instanceof IdentNode)) break;
                if (!Parser.checkIdentLValue((IdentNode)lhs)) {
                    return this.referenceError(lhs, rhs, false);
                }
                this.verifyStrictIdent((IdentNode)lhs, "assignment");
                break;
            }
        }
        if (BinaryNode.isLogical(opType)) {
            return new BinaryNode(op, new JoinPredecessorExpression(lhs), new JoinPredecessorExpression(rhs));
        }
        return new BinaryNode(op, lhs, rhs);
    }

    private static UnaryNode incDecExpression(long firstToken, TokenType tokenType, Expression expression, boolean isPostfix) {
        if (isPostfix) {
            return new UnaryNode(Token.recast(firstToken, tokenType == TokenType.DECPREFIX ? TokenType.DECPOSTFIX : TokenType.INCPOSTFIX), expression.getStart(), Token.descPosition(firstToken) + Token.descLength(firstToken), expression);
        }
        return new UnaryNode(firstToken, expression);
    }

    private FunctionNode program(String scriptName, boolean allowPropertyFunction) {
        long functionToken = Token.toDesc(TokenType.FUNCTION, Token.descPosition(Token.withDelimiter(this.token)), this.source.getLength());
        int functionLine = this.line;
        FunctionNode script = this.newFunctionNode(functionToken, new IdentNode(functionToken, Token.descPosition(functionToken), scriptName), new ArrayList<IdentNode>(), FunctionNode.Kind.SCRIPT, functionLine);
        this.functionDeclarations = new ArrayList<Statement>();
        this.sourceElements(allowPropertyFunction);
        this.addFunctionDeclarations(script);
        this.functionDeclarations = null;
        this.expect(TokenType.EOF);
        script.setFinish(this.source.getLength() - 1);
        script = this.restoreFunctionNode(script, this.token);
        script = script.setBody(this.lc, script.getBody().setNeedsScope(this.lc));
        return script;
    }

    private String getDirective(Node stmt) {
        LiteralNode lit;
        long litToken;
        TokenType tt;
        Expression expr;
        if (stmt instanceof ExpressionStatement && (expr = ((ExpressionStatement)stmt).getExpression()) instanceof LiteralNode && ((tt = Token.descType(litToken = (lit = (LiteralNode)expr).getToken())) == TokenType.STRING || tt == TokenType.ESCSTRING)) {
            return this.source.getString(lit.getStart(), Token.descLength(litToken));
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void sourceElements(boolean shouldAllowPropertyFunction) {
        ArrayList<Statement> directiveStmts = null;
        boolean checkDirective = true;
        boolean allowPropertyFunction = shouldAllowPropertyFunction;
        boolean oldStrictMode = this.isStrictMode;
        try {
            while (this.type != TokenType.EOF) {
                if (this.type == TokenType.RBRACE) {
                    break;
                }
                try {
                    this.statement(true, allowPropertyFunction, false);
                    allowPropertyFunction = false;
                    if (checkDirective) {
                        Statement lastStatement = this.lc.getLastStatement();
                        String directive = this.getDirective(lastStatement);
                        boolean bl = checkDirective = directive != null;
                        if (checkDirective) {
                            int flag;
                            if (!oldStrictMode) {
                                if (directiveStmts == null) {
                                    directiveStmts = new ArrayList<Statement>();
                                }
                                directiveStmts.add(lastStatement);
                            }
                            if ("use strict".equals(directive)) {
                                this.isStrictMode = true;
                                FunctionNode function = this.lc.getCurrentFunction();
                                this.lc.setFlag(this.lc.getCurrentFunction(), 4);
                                if (!oldStrictMode && directiveStmts != null) {
                                    for (Node node : directiveStmts) {
                                        this.getValue(node.getToken());
                                    }
                                    this.verifyStrictIdent(function.getIdent(), "function name");
                                    for (IdentNode identNode : function.getParameters()) {
                                        this.verifyStrictIdent(identNode, "function parameter");
                                    }
                                }
                            } else if (Context.DEBUG && (flag = FunctionNode.getDirectiveFlag(directive)) != 0) {
                                FunctionNode function = this.lc.getCurrentFunction();
                                this.lc.setFlag(function, flag);
                            }
                        }
                    }
                }
                catch (Exception e) {
                    this.recover(e);
                }
                this.stream.commit(this.k);
            }
        }
        finally {
            this.isStrictMode = oldStrictMode;
        }
    }

    private void statement() {
        this.statement(false, false, false);
    }

    private void statement(boolean topLevel, boolean allowPropertyFunction, boolean singleStatement) {
        if (this.type == TokenType.FUNCTION) {
            this.functionExpression(true, topLevel);
            return;
        }
        switch (this.type) {
            case LBRACE: {
                this.block();
                break;
            }
            case VAR: {
                this.variableStatement(this.type, true);
                break;
            }
            case SEMICOLON: {
                this.emptyStatement();
                break;
            }
            case IF: {
                this.ifStatement();
                break;
            }
            case FOR: {
                this.forStatement();
                break;
            }
            case WHILE: {
                this.whileStatement();
                break;
            }
            case DO: {
                this.doStatement();
                break;
            }
            case CONTINUE: {
                this.continueStatement();
                break;
            }
            case BREAK: {
                this.breakStatement();
                break;
            }
            case RETURN: {
                this.returnStatement();
                break;
            }
            case YIELD: {
                this.yieldStatement();
                break;
            }
            case WITH: {
                this.withStatement();
                break;
            }
            case SWITCH: {
                this.switchStatement();
                break;
            }
            case THROW: {
                this.throwStatement();
                break;
            }
            case TRY: {
                this.tryStatement();
                break;
            }
            case DEBUGGER: {
                this.debuggerStatement();
                break;
            }
            case EOF: 
            case RPAREN: 
            case RBRACKET: {
                this.expect(TokenType.SEMICOLON);
                break;
            }
            default: {
                if (this.useBlockScope() && (this.type == TokenType.LET || this.type == TokenType.CONST)) {
                    if (singleStatement) {
                        throw this.error(AbstractParser.message("expected.stmt", this.type.getName() + " declaration"), this.token);
                    }
                    this.variableStatement(this.type, true);
                    break;
                }
                if (this.env._const_as_var && this.type == TokenType.CONST) {
                    this.variableStatement(TokenType.VAR, true);
                    break;
                }
                if (this.type == TokenType.IDENT || this.isNonStrictModeIdent()) {
                    if (this.T(this.k + 1) == TokenType.COLON) {
                        this.labelStatement();
                        return;
                    }
                    if (allowPropertyFunction) {
                        String ident = (String)this.getValue();
                        long propertyToken = this.token;
                        int propertyLine = this.line;
                        if ("get".equals(ident)) {
                            this.next();
                            this.addPropertyFunctionStatement(this.propertyGetterFunction(propertyToken, propertyLine));
                            return;
                        }
                        if ("set".equals(ident)) {
                            this.next();
                            this.addPropertyFunctionStatement(this.propertySetterFunction(propertyToken, propertyLine));
                            return;
                        }
                    }
                }
                this.expressionStatement();
            }
        }
    }

    private void addPropertyFunctionStatement(PropertyFunction propertyFunction) {
        FunctionNode fn = propertyFunction.functionNode;
        this.functionDeclarations.add(new ExpressionStatement(fn.getLineNumber(), fn.getToken(), this.finish, fn));
    }

    private void block() {
        this.appendStatement(new BlockStatement(this.line, this.getBlock(true)));
    }

    private void statementList() {
        block3: while (this.type != TokenType.EOF) {
            switch (this.type) {
                case EOF: 
                case RBRACE: 
                case CASE: 
                case DEFAULT: {
                    break block3;
                }
                default: {
                    this.statement();
                    continue block3;
                }
            }
        }
    }

    private void verifyStrictIdent(IdentNode ident, String contextString) {
        if (this.isStrictMode) {
            switch (ident.getName()) {
                case "eval": 
                case "arguments": {
                    throw this.error(AbstractParser.message("strict.name", ident.getName(), contextString), ident.getToken());
                }
            }
            if (ident.isFutureStrictName()) {
                throw this.error(AbstractParser.message("strict.name", ident.getName(), contextString), ident.getToken());
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private List<VarNode> variableStatement(TokenType varType, boolean isStatement) {
        this.next();
        ArrayList<VarNode> vars = new ArrayList<VarNode>();
        int varFlags = 0;
        if (varType == TokenType.LET) {
            varFlags |= 1;
        } else if (varType == TokenType.CONST) {
            varFlags |= 2;
        }
        while (true) {
            int varLine = this.line;
            long varToken = this.token;
            IdentNode name = this.getIdent();
            this.verifyStrictIdent(name, "variable name");
            Expression init = null;
            if (this.type == TokenType.ASSIGN) {
                this.next();
                this.defaultNames.push(name);
                try {
                    init = this.assignmentExpression(!isStatement);
                }
                finally {
                    this.defaultNames.pop();
                }
            } else if (varType == TokenType.CONST) {
                throw this.error(AbstractParser.message("missing.const.assignment", name.getName()));
            }
            VarNode var = new VarNode(varLine, varToken, this.finish, name.setIsDeclaredHere(), init, varFlags);
            vars.add(var);
            this.appendStatement(var);
            if (this.type != TokenType.COMMARIGHT) break;
            this.next();
        }
        if (isStatement) {
            boolean semicolon = this.type == TokenType.SEMICOLON;
            this.endOfLine();
            if (semicolon) {
                this.lc.getCurrentBlock().setFinish(this.finish);
            }
        }
        return vars;
    }

    private void emptyStatement() {
        if (this.env._empty_statements) {
            this.appendStatement(new EmptyNode(this.line, this.token, Token.descPosition(this.token) + Token.descLength(this.token)));
        }
        this.next();
    }

    private void expressionStatement() {
        int expressionLine = this.line;
        long expressionToken = this.token;
        Expression expression = this.expression();
        ExpressionStatement expressionStatement = null;
        if (expression != null) {
            expressionStatement = new ExpressionStatement(expressionLine, expressionToken, this.finish, expression);
            this.appendStatement(expressionStatement);
        } else {
            this.expect(null);
        }
        this.endOfLine();
        if (expressionStatement != null) {
            expressionStatement.setFinish(this.finish);
            this.lc.getCurrentBlock().setFinish(this.finish);
        }
    }

    private void ifStatement() {
        int ifLine = this.line;
        long ifToken = this.token;
        this.next();
        this.expect(TokenType.LPAREN);
        Expression test = this.expression();
        this.expect(TokenType.RPAREN);
        Block pass = this.getStatement();
        Block fail = null;
        if (this.type == TokenType.ELSE) {
            this.next();
            fail = this.getStatement();
        }
        this.appendStatement(new IfNode(ifLine, ifToken, fail != null ? fail.getFinish() : pass.getFinish(), test, pass, fail));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void forStatement() {
        int startLine = this.start;
        Block outer = this.useBlockScope() ? this.newBlock() : null;
        ForNode forNode = new ForNode(this.line, this.token, Token.descPosition(this.token), null, 0);
        this.lc.push(forNode);
        try {
            this.next();
            if (!this.env._no_syntax_extensions && this.type == TokenType.IDENT && "each".equals(this.getValue())) {
                forNode = forNode.setIsForEach(this.lc);
                this.next();
            }
            this.expect(TokenType.LPAREN);
            List<VarNode> vars = null;
            switch (this.type) {
                case VAR: {
                    vars = this.variableStatement(this.type, false);
                    break;
                }
                case SEMICOLON: {
                    break;
                }
                default: {
                    if (this.useBlockScope() && (this.type == TokenType.LET || this.type == TokenType.CONST)) {
                        if (this.type == TokenType.LET) {
                            forNode = forNode.setPerIterationScope(this.lc);
                        }
                        vars = this.variableStatement(this.type, false);
                        break;
                    }
                    if (this.env._const_as_var && this.type == TokenType.CONST) {
                        vars = this.variableStatement(TokenType.VAR, false);
                        break;
                    }
                    Expression expression = this.expression(this.unaryExpression(), TokenType.COMMARIGHT.getPrecedence(), true);
                    forNode = forNode.setInit(this.lc, expression);
                }
            }
            switch (this.type) {
                case SEMICOLON: {
                    if (forNode.isForEach()) {
                        throw this.error(AbstractParser.message("for.each.without.in", new String[0]), this.token);
                    }
                    this.expect(TokenType.SEMICOLON);
                    if (this.type != TokenType.SEMICOLON) {
                        forNode = forNode.setTest(this.lc, this.joinPredecessorExpression());
                    }
                    this.expect(TokenType.SEMICOLON);
                    if (this.type == TokenType.RPAREN) break;
                    forNode = forNode.setModify(this.lc, this.joinPredecessorExpression());
                    break;
                }
                case IN: {
                    forNode = forNode.setIsForIn(this.lc).setTest(this.lc, new JoinPredecessorExpression());
                    if (vars != null) {
                        if (vars.size() != 1) throw this.error(AbstractParser.message("many.vars.in.for.in.loop", new String[0]), vars.get(1).getToken());
                        forNode = forNode.setInit(this.lc, new IdentNode(vars.get(0).getName()));
                    } else {
                        Expression init = forNode.getInit();
                        assert (init != null) : "for..in init expression can not be null here";
                        if (!(init instanceof AccessNode || init instanceof IndexNode || init instanceof IdentNode)) {
                            throw this.error(AbstractParser.message("not.lvalue.for.in.loop", new String[0]), init.getToken());
                        }
                        if (init instanceof IdentNode) {
                            if (!Parser.checkIdentLValue((IdentNode)init)) {
                                throw this.error(AbstractParser.message("not.lvalue.for.in.loop", new String[0]), init.getToken());
                            }
                            this.verifyStrictIdent((IdentNode)init, "for-in iterator");
                        }
                    }
                    this.next();
                    forNode = forNode.setModify(this.lc, this.joinPredecessorExpression());
                    break;
                }
                default: {
                    this.expect(TokenType.SEMICOLON);
                }
            }
            this.expect(TokenType.RPAREN);
            Block body = this.getStatement();
            forNode = forNode.setBody(this.lc, body);
            forNode.setFinish(body.getFinish());
            this.appendStatement(forNode);
        }
        finally {
            this.lc.pop(forNode);
        }
        if (outer == null) return;
        outer.setFinish(forNode.getFinish());
        outer = this.restoreBlock(outer);
        this.appendStatement(new BlockStatement(startLine, outer));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void whileStatement() {
        long whileToken = this.token;
        this.next();
        WhileNode whileNode = new WhileNode(this.line, whileToken, Token.descPosition(whileToken), false);
        this.lc.push(whileNode);
        try {
            this.expect(TokenType.LPAREN);
            int whileLine = this.line;
            JoinPredecessorExpression test = this.joinPredecessorExpression();
            this.expect(TokenType.RPAREN);
            Block body = this.getStatement();
            whileNode = new WhileNode(whileLine, whileToken, this.finish, false).setTest(this.lc, test).setBody(this.lc, body);
            this.appendStatement(whileNode);
        }
        finally {
            this.lc.pop(whileNode);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void doStatement() {
        long doToken = this.token;
        this.next();
        WhileNode doWhileNode = new WhileNode(-1, doToken, Token.descPosition(doToken), true);
        this.lc.push(doWhileNode);
        try {
            Block body = this.getStatement();
            this.expect(TokenType.WHILE);
            this.expect(TokenType.LPAREN);
            int doLine = this.line;
            JoinPredecessorExpression test = this.joinPredecessorExpression();
            this.expect(TokenType.RPAREN);
            if (this.type == TokenType.SEMICOLON) {
                this.endOfLine();
            }
            doWhileNode.setFinish(this.finish);
            doWhileNode = new WhileNode(doLine, doToken, this.finish, true).setBody(this.lc, body).setTest(this.lc, test);
            this.appendStatement(doWhileNode);
        }
        finally {
            this.lc.pop(doWhileNode);
        }
    }

    private void continueStatement() {
        int continueLine = this.line;
        long continueToken = this.token;
        this.nextOrEOL();
        LabelNode labelNode = null;
        switch (this.type) {
            case EOF: 
            case EOL: 
            case SEMICOLON: 
            case RBRACE: {
                break;
            }
            default: {
                IdentNode ident = this.getIdent();
                labelNode = this.lc.findLabel(ident.getName());
                if (labelNode != null) break;
                throw this.error(AbstractParser.message("undefined.label", ident.getName()), ident.getToken());
            }
        }
        String labelName = labelNode == null ? null : labelNode.getLabelName();
        LoopNode targetNode = this.lc.getContinueTo(labelName);
        if (targetNode == null) {
            throw this.error(AbstractParser.message("illegal.continue.stmt", new String[0]), continueToken);
        }
        this.endOfLine();
        this.appendStatement(new ContinueNode(continueLine, continueToken, this.finish, labelName));
    }

    private void breakStatement() {
        int breakLine = this.line;
        long breakToken = this.token;
        this.nextOrEOL();
        LabelNode labelNode = null;
        switch (this.type) {
            case EOF: 
            case EOL: 
            case SEMICOLON: 
            case RBRACE: {
                break;
            }
            default: {
                IdentNode ident = this.getIdent();
                labelNode = this.lc.findLabel(ident.getName());
                if (labelNode != null) break;
                throw this.error(AbstractParser.message("undefined.label", ident.getName()), ident.getToken());
            }
        }
        String labelName = labelNode == null ? null : labelNode.getLabelName();
        BreakableNode targetNode = this.lc.getBreakable(labelName);
        if (targetNode == null) {
            throw this.error(AbstractParser.message("illegal.break.stmt", new String[0]), breakToken);
        }
        this.endOfLine();
        this.appendStatement(new BreakNode(breakLine, breakToken, this.finish, labelName));
    }

    private void returnStatement() {
        if (this.lc.getCurrentFunction().getKind() == FunctionNode.Kind.SCRIPT) {
            throw this.error(AbstractParser.message("invalid.return", new String[0]));
        }
        int returnLine = this.line;
        long returnToken = this.token;
        this.nextOrEOL();
        Expression expression = null;
        switch (this.type) {
            case EOF: 
            case EOL: 
            case SEMICOLON: 
            case RBRACE: {
                break;
            }
            default: {
                expression = this.expression();
            }
        }
        this.endOfLine();
        this.appendStatement(new ReturnNode(returnLine, returnToken, this.finish, expression));
    }

    private void yieldStatement() {
        int yieldLine = this.line;
        long yieldToken = this.token;
        this.nextOrEOL();
        Expression expression = null;
        switch (this.type) {
            case EOF: 
            case EOL: 
            case SEMICOLON: 
            case RBRACE: {
                break;
            }
            default: {
                expression = this.expression();
            }
        }
        this.endOfLine();
        this.appendStatement(new ReturnNode(yieldLine, yieldToken, this.finish, expression));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void withStatement() {
        int withLine = this.line;
        long withToken = this.token;
        this.next();
        if (this.isStrictMode) {
            throw this.error(AbstractParser.message("strict.no.with", new String[0]), withToken);
        }
        WithNode withNode = new WithNode(withLine, withToken, this.finish);
        try {
            this.lc.push(withNode);
            this.expect(TokenType.LPAREN);
            withNode = withNode.setExpression(this.lc, this.expression());
            this.expect(TokenType.RPAREN);
            withNode = withNode.setBody(this.lc, this.getStatement());
        }
        finally {
            this.lc.pop(withNode);
        }
        this.appendStatement(withNode);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void switchStatement() {
        int switchLine = this.line;
        long switchToken = this.token;
        this.next();
        SwitchNode switchNode = new SwitchNode(switchLine, switchToken, Token.descPosition(switchToken), null, new ArrayList<CaseNode>(), null);
        this.lc.push(switchNode);
        try {
            this.expect(TokenType.LPAREN);
            switchNode = switchNode.setExpression(this.lc, this.expression());
            this.expect(TokenType.RPAREN);
            this.expect(TokenType.LBRACE);
            ArrayList<CaseNode> cases = new ArrayList<CaseNode>();
            CaseNode defaultCase = null;
            while (this.type != TokenType.RBRACE) {
                Expression caseExpression = null;
                long caseToken = this.token;
                switch (this.type) {
                    case CASE: {
                        this.next();
                        caseExpression = this.expression();
                        break;
                    }
                    case DEFAULT: {
                        if (defaultCase != null) {
                            throw this.error(AbstractParser.message("duplicate.default.in.switch", new String[0]));
                        }
                        this.next();
                        break;
                    }
                    default: {
                        this.expect(TokenType.CASE);
                    }
                }
                this.expect(TokenType.COLON);
                Block statements = this.getBlock(false);
                CaseNode caseNode = new CaseNode(caseToken, this.finish, caseExpression, statements);
                statements.setFinish(this.finish);
                if (caseExpression == null) {
                    defaultCase = caseNode;
                }
                cases.add(caseNode);
            }
            switchNode = switchNode.setCases((LexicalContext)this.lc, cases, defaultCase);
            this.next();
            switchNode.setFinish(this.finish);
            this.appendStatement(switchNode);
        }
        finally {
            this.lc.pop(switchNode);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void labelStatement() {
        long labelToken = this.token;
        IdentNode ident = this.getIdent();
        this.expect(TokenType.COLON);
        if (this.lc.findLabel(ident.getName()) != null) {
            throw this.error(AbstractParser.message("duplicate.label", ident.getName()), labelToken);
        }
        LabelNode labelNode = new LabelNode(this.line, labelToken, this.finish, ident.getName(), null);
        try {
            this.lc.push(labelNode);
            labelNode = labelNode.setBody(this.lc, this.getStatement());
            labelNode.setFinish(this.finish);
            this.appendStatement(labelNode);
        }
        finally {
            assert (this.lc.peek() instanceof LabelNode);
            this.lc.pop(labelNode);
        }
    }

    private void throwStatement() {
        int throwLine = this.line;
        long throwToken = this.token;
        this.nextOrEOL();
        Expression expression = null;
        switch (this.type) {
            case EOL: 
            case SEMICOLON: 
            case RBRACE: {
                break;
            }
            default: {
                expression = this.expression();
            }
        }
        if (expression == null) {
            throw this.error(AbstractParser.message("expected.operand", this.type.getNameOrType()));
        }
        this.endOfLine();
        this.appendStatement(new ThrowNode(throwLine, throwToken, this.finish, expression, false));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void tryStatement() {
        int tryLine = this.line;
        long tryToken = this.token;
        this.next();
        int startLine = this.line;
        Block outer = this.newBlock();
        try {
            Block tryBody = this.getBlock(true);
            ArrayList<Block> catchBlocks = new ArrayList<Block>();
            while (this.type == TokenType.CATCH) {
                Expression ifExpression;
                int catchLine = this.line;
                long catchToken = this.token;
                this.next();
                this.expect(TokenType.LPAREN);
                IdentNode exception = this.getIdent();
                this.verifyStrictIdent(exception, "catch argument");
                if (!this.env._no_syntax_extensions && this.type == TokenType.IF) {
                    this.next();
                    ifExpression = this.expression();
                } else {
                    ifExpression = null;
                }
                this.expect(TokenType.RPAREN);
                Block catchBlock = this.newBlock();
                try {
                    Block catchBody = this.getBlock(true);
                    CatchNode catchNode = new CatchNode(catchLine, catchToken, this.finish, exception, ifExpression, catchBody, false);
                    this.appendStatement(catchNode);
                }
                finally {
                    catchBlock = this.restoreBlock(catchBlock);
                    catchBlocks.add(catchBlock);
                }
                if (ifExpression != null) continue;
                break;
            }
            Block finallyStatements = null;
            if (this.type == TokenType.FINALLY) {
                this.next();
                finallyStatements = this.getBlock(true);
            }
            if (catchBlocks.isEmpty() && finallyStatements == null) {
                throw this.error(AbstractParser.message("missing.catch.or.finally", new String[0]), tryToken);
            }
            TryNode tryNode = new TryNode(tryLine, tryToken, Token.descPosition(tryToken), tryBody, catchBlocks, finallyStatements);
            assert (this.lc.peek() == outer);
            this.appendStatement(tryNode);
            tryNode.setFinish(this.finish);
            outer.setFinish(this.finish);
        }
        finally {
            outer = this.restoreBlock(outer);
        }
        this.appendStatement(new BlockStatement(startLine, outer));
    }

    private void debuggerStatement() {
        int debuggerLine = this.line;
        long debuggerToken = this.token;
        this.next();
        this.endOfLine();
        this.appendStatement(new ExpressionStatement(debuggerLine, debuggerToken, this.finish, new RuntimeNode(debuggerToken, this.finish, RuntimeNode.Request.DEBUGGER, new ArrayList<Expression>())));
    }

    private Expression primaryExpression() {
        int primaryLine = this.line;
        long primaryToken = this.token;
        switch (this.type) {
            case THIS: {
                String name = this.type.getName();
                this.next();
                this.lc.setFlag(this.lc.getCurrentFunction(), 32768);
                return new IdentNode(primaryToken, this.finish, name);
            }
            case IDENT: {
                IdentNode ident = this.getIdent();
                if (ident == null) break;
                this.detectSpecialProperty(ident);
                return ident;
            }
            case OCTAL: {
                if (this.isStrictMode) {
                    throw this.error(AbstractParser.message("strict.no.octal", new String[0]), this.token);
                }
            }
            case STRING: 
            case ESCSTRING: 
            case DECIMAL: 
            case HEXADECIMAL: 
            case FLOATING: 
            case REGEX: 
            case XML: {
                return this.getLiteral();
            }
            case EXECSTRING: {
                return this.execString(primaryLine, primaryToken);
            }
            case FALSE: {
                this.next();
                return LiteralNode.newInstance(primaryToken, this.finish, false);
            }
            case TRUE: {
                this.next();
                return LiteralNode.newInstance(primaryToken, this.finish, true);
            }
            case NULL: {
                this.next();
                return LiteralNode.newInstance(primaryToken, this.finish);
            }
            case LBRACKET: {
                return this.arrayLiteral();
            }
            case LBRACE: {
                return this.objectLiteral();
            }
            case LPAREN: {
                this.next();
                Expression expression = this.expression();
                this.expect(TokenType.RPAREN);
                return expression;
            }
            default: {
                if (this.lexer.scanLiteral(primaryToken, this.type, this.lineInfoReceiver)) {
                    this.next();
                    return this.getLiteral();
                }
                if (!this.isNonStrictModeIdent()) break;
                return this.getIdent();
            }
        }
        return null;
    }

    CallNode execString(int primaryLine, long primaryToken) {
        IdentNode execIdent = new IdentNode(primaryToken, this.finish, "$EXEC");
        this.next();
        this.expect(TokenType.LBRACE);
        List<Expression> arguments = Collections.singletonList(this.expression());
        this.expect(TokenType.RBRACE);
        return new CallNode(primaryLine, primaryToken, this.finish, execIdent, arguments, false);
    }

    private LiteralNode<Expression[]> arrayLiteral() {
        long arrayToken = this.token;
        this.next();
        ArrayList<Expression> elements = new ArrayList<Expression>();
        boolean elision = true;
        block4: while (true) {
            switch (this.type) {
                case RBRACKET: {
                    this.next();
                    break block4;
                }
                case COMMARIGHT: {
                    this.next();
                    if (elision) {
                        elements.add(null);
                    }
                    elision = true;
                    continue block4;
                }
                default: {
                    if (!elision) {
                        throw this.error(AbstractParser.message("expected.comma", this.type.getNameOrType()));
                    }
                    Expression expression = this.assignmentExpression(false);
                    if (expression != null) {
                        elements.add(expression);
                    } else {
                        this.expect(TokenType.RBRACKET);
                    }
                    elision = false;
                    continue block4;
                }
            }
            break;
        }
        return LiteralNode.newInstance(arrayToken, this.finish, elements);
    }

    private ObjectNode objectLiteral() {
        long objectToken = this.token;
        this.next();
        ArrayList<PropertyNode> elements = new ArrayList<PropertyNode>();
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        boolean commaSeen = true;
        block4: while (true) {
            switch (this.type) {
                case RBRACE: {
                    this.next();
                    break block4;
                }
                case COMMARIGHT: {
                    if (commaSeen) {
                        throw this.error(AbstractParser.message("expected.property.id", this.type.getNameOrType()));
                    }
                    this.next();
                    commaSeen = true;
                    continue block4;
                }
                default: {
                    boolean isAccessor;
                    if (!commaSeen) {
                        throw this.error(AbstractParser.message("expected.comma", this.type.getNameOrType()));
                    }
                    commaSeen = false;
                    PropertyNode property = this.propertyAssignment();
                    String key = property.getKeyName();
                    Integer existing = (Integer)map.get(key);
                    if (existing == null) {
                        map.put(key, elements.size());
                        elements.add(property);
                        continue block4;
                    }
                    PropertyNode existingProperty = (PropertyNode)elements.get(existing);
                    Expression value = property.getValue();
                    FunctionNode getter = property.getGetter();
                    FunctionNode setter = property.getSetter();
                    Expression prevValue = existingProperty.getValue();
                    FunctionNode prevGetter = existingProperty.getGetter();
                    FunctionNode prevSetter = existingProperty.getSetter();
                    if (this.isStrictMode && value != null && prevValue != null) {
                        throw this.error(AbstractParser.message("property.redefinition", key), property.getToken());
                    }
                    boolean isPrevAccessor = prevGetter != null || prevSetter != null;
                    boolean bl = isAccessor = getter != null || setter != null;
                    if (prevValue != null && isAccessor) {
                        throw this.error(AbstractParser.message("property.redefinition", key), property.getToken());
                    }
                    if (isPrevAccessor && value != null) {
                        throw this.error(AbstractParser.message("property.redefinition", key), property.getToken());
                    }
                    if (isAccessor && isPrevAccessor && (getter != null && prevGetter != null || setter != null && prevSetter != null)) {
                        throw this.error(AbstractParser.message("property.redefinition", key), property.getToken());
                    }
                    if (value != null) {
                        elements.add(property);
                        continue block4;
                    }
                    if (getter != null) {
                        elements.set(existing, existingProperty.setGetter(getter));
                        continue block4;
                    }
                    if (setter == null) continue block4;
                    elements.set(existing, existingProperty.setSetter(setter));
                    continue block4;
                }
            }
            break;
        }
        return new ObjectNode(objectToken, this.finish, elements);
    }

    private PropertyKey propertyName() {
        switch (this.type) {
            case IDENT: {
                return this.getIdent().setIsPropertyName();
            }
            case OCTAL: {
                if (this.isStrictMode) {
                    throw this.error(AbstractParser.message("strict.no.octal", new String[0]), this.token);
                }
            }
            case STRING: 
            case ESCSTRING: 
            case DECIMAL: 
            case HEXADECIMAL: 
            case FLOATING: {
                return this.getLiteral();
            }
        }
        return this.getIdentifierName().setIsPropertyName();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private PropertyNode propertyAssignment() {
        PropertyKey propertyName;
        long propertyToken = this.token;
        int functionLine = this.line;
        if (this.type == TokenType.IDENT) {
            String ident = (String)this.expectValue(TokenType.IDENT);
            if (this.type != TokenType.COLON) {
                long getSetToken = propertyToken;
                switch (ident) {
                    case "get": {
                        PropertyFunction getter = this.propertyGetterFunction(getSetToken, functionLine);
                        return new PropertyNode(propertyToken, this.finish, getter.ident, null, getter.functionNode, null);
                    }
                    case "set": {
                        PropertyFunction setter = this.propertySetterFunction(getSetToken, functionLine);
                        return new PropertyNode(propertyToken, this.finish, setter.ident, null, null, setter.functionNode);
                    }
                }
            }
            propertyName = this.createIdentNode(propertyToken, this.finish, ident).setIsPropertyName();
        } else {
            propertyName = this.propertyName();
        }
        this.expect(TokenType.COLON);
        this.defaultNames.push(propertyName);
        try {
            PropertyNode propertyNode = new PropertyNode(propertyToken, this.finish, propertyName, this.assignmentExpression(false), null, null);
            return propertyNode;
        }
        finally {
            this.defaultNames.pop();
        }
    }

    private PropertyFunction propertyGetterFunction(long getSetToken, int functionLine) {
        PropertyKey getIdent = this.propertyName();
        String getterName = getIdent.getPropertyName();
        IdentNode getNameNode = this.createIdentNode(((Node)((Object)getIdent)).getToken(), this.finish, NameCodec.encode("get " + getterName));
        this.expect(TokenType.LPAREN);
        this.expect(TokenType.RPAREN);
        FunctionNode functionNode = this.functionBody(getSetToken, getNameNode, new ArrayList<IdentNode>(), FunctionNode.Kind.GETTER, functionLine);
        return new PropertyFunction(getIdent, functionNode);
    }

    private PropertyFunction propertySetterFunction(long getSetToken, int functionLine) {
        IdentNode argIdent;
        PropertyKey setIdent = this.propertyName();
        String setterName = setIdent.getPropertyName();
        IdentNode setNameNode = this.createIdentNode(((Node)((Object)setIdent)).getToken(), this.finish, NameCodec.encode("set " + setterName));
        this.expect(TokenType.LPAREN);
        if (this.type == TokenType.IDENT || this.isNonStrictModeIdent()) {
            argIdent = this.getIdent();
            this.verifyStrictIdent(argIdent, "setter argument");
        } else {
            argIdent = null;
        }
        this.expect(TokenType.RPAREN);
        ArrayList<IdentNode> parameters = new ArrayList<IdentNode>();
        if (argIdent != null) {
            parameters.add(argIdent);
        }
        FunctionNode functionNode = this.functionBody(getSetToken, setNameNode, parameters, FunctionNode.Kind.SETTER, functionLine);
        return new PropertyFunction(setIdent, functionNode);
    }

    private Expression leftHandSideExpression() {
        List<Expression> arguments;
        int callLine = this.line;
        long callToken = this.token;
        Expression lhs = this.memberExpression();
        if (this.type == TokenType.LPAREN) {
            arguments = Parser.optimizeList(this.argumentList());
            if (lhs instanceof IdentNode) {
                this.detectSpecialFunction((IdentNode)lhs);
            }
            lhs = new CallNode(callLine, callToken, this.finish, lhs, arguments, false);
        }
        block5: while (true) {
            callLine = this.line;
            callToken = this.token;
            switch (this.type) {
                case LPAREN: {
                    arguments = Parser.optimizeList(this.argumentList());
                    lhs = new CallNode(callLine, callToken, this.finish, lhs, arguments, false);
                    continue block5;
                }
                case LBRACKET: {
                    this.next();
                    Expression rhs = this.expression();
                    this.expect(TokenType.RBRACKET);
                    lhs = new IndexNode(callToken, this.finish, lhs, rhs);
                    continue block5;
                }
                case PERIOD: {
                    this.next();
                    IdentNode property = this.getIdentifierName();
                    lhs = new AccessNode(callToken, this.finish, lhs, property.getName());
                    continue block5;
                }
            }
            break;
        }
        return lhs;
    }

    private Expression newExpression() {
        long newToken = this.token;
        this.next();
        int callLine = this.line;
        Expression constructor = this.memberExpression();
        if (constructor == null) {
            return null;
        }
        ArrayList<Expression> arguments = this.type == TokenType.LPAREN ? this.argumentList() : new ArrayList();
        if (!this.env._no_syntax_extensions && this.type == TokenType.LBRACE) {
            arguments.add(this.objectLiteral());
        }
        CallNode callNode = new CallNode(callLine, constructor.getToken(), this.finish, constructor, Parser.optimizeList(arguments), true);
        return new UnaryNode(newToken, callNode);
    }

    private Expression memberExpression() {
        Expression lhs;
        switch (this.type) {
            case NEW: {
                lhs = this.newExpression();
                break;
            }
            case FUNCTION: {
                lhs = this.functionExpression(false, false);
                break;
            }
            default: {
                lhs = this.primaryExpression();
            }
        }
        block8: while (true) {
            long callToken = this.token;
            switch (this.type) {
                case LBRACKET: {
                    this.next();
                    Expression index = this.expression();
                    this.expect(TokenType.RBRACKET);
                    lhs = new IndexNode(callToken, this.finish, lhs, index);
                    continue block8;
                }
                case PERIOD: {
                    if (lhs == null) {
                        throw this.error(AbstractParser.message("expected.operand", this.type.getNameOrType()));
                    }
                    this.next();
                    IdentNode property = this.getIdentifierName();
                    lhs = new AccessNode(callToken, this.finish, lhs, property.getName());
                    continue block8;
                }
            }
            break;
        }
        return lhs;
    }

    private ArrayList<Expression> argumentList() {
        ArrayList<Expression> nodeList = new ArrayList<Expression>();
        this.next();
        boolean first = true;
        while (this.type != TokenType.RPAREN) {
            if (!first) {
                this.expect(TokenType.COMMARIGHT);
            } else {
                first = false;
            }
            nodeList.add(this.assignmentExpression(false));
        }
        this.expect(TokenType.RPAREN);
        return nodeList;
    }

    private static <T> List<T> optimizeList(ArrayList<T> list) {
        switch (list.size()) {
            case 0: {
                return Collections.emptyList();
            }
            case 1: {
                return Collections.singletonList(list.get(0));
            }
        }
        list.trimToSize();
        return list;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private Expression functionExpression(boolean isStatement, boolean topLevel) {
        FunctionNode functionNode;
        long functionToken = this.token;
        int functionLine = this.line;
        this.next();
        IdentNode name = null;
        if (this.type == TokenType.IDENT || this.isNonStrictModeIdent()) {
            name = this.getIdent();
            this.verifyStrictIdent(name, "function name");
        } else if (isStatement && this.env._no_syntax_extensions && this.reparsedFunction == null) {
            this.expect(TokenType.IDENT);
        }
        boolean isAnonymous = false;
        if (name == null) {
            String tmpName = this.getDefaultValidFunctionName(functionLine, isStatement);
            name = new IdentNode(functionToken, Token.descPosition(functionToken), tmpName);
            isAnonymous = true;
        }
        this.expect(TokenType.LPAREN);
        List<IdentNode> parameters = this.formalParameterList();
        this.expect(TokenType.RPAREN);
        this.hideDefaultName();
        try {
            functionNode = this.functionBody(functionToken, name, parameters, FunctionNode.Kind.NORMAL, functionLine);
        }
        finally {
            this.defaultNames.pop();
        }
        if (isStatement) {
            if (topLevel || this.useBlockScope()) {
                functionNode = functionNode.setFlag(this.lc, 2);
            } else {
                if (this.isStrictMode) {
                    throw this.error(JSErrorType.SYNTAX_ERROR, AbstractParser.message("strict.no.func.decl.here", new String[0]), functionToken);
                }
                if (this.env._function_statement == ScriptEnvironment.FunctionStatementBehavior.ERROR) {
                    throw this.error(JSErrorType.SYNTAX_ERROR, AbstractParser.message("no.func.decl.here", new String[0]), functionToken);
                }
                if (this.env._function_statement == ScriptEnvironment.FunctionStatementBehavior.WARNING) {
                    this.warning(JSErrorType.SYNTAX_ERROR, AbstractParser.message("no.func.decl.here.warn", new String[0]), functionToken);
                }
            }
            if (Parser.isArguments(name)) {
                this.lc.setFlag(this.lc.getCurrentFunction(), 256);
            }
        }
        if (isAnonymous) {
            functionNode = functionNode.setFlag(this.lc, 1);
        }
        int arity = parameters.size();
        boolean strict = functionNode.isStrict();
        if (arity > 1) {
            HashSet<String> parametersSet = new HashSet<String>(arity);
            for (int i = arity - 1; i >= 0; --i) {
                IdentNode parameter = parameters.get(i);
                String parameterName = parameter.getName();
                if (Parser.isArguments(parameterName)) {
                    functionNode = functionNode.setFlag(this.lc, 256);
                }
                if (parametersSet.contains(parameterName)) {
                    if (strict) {
                        throw this.error(AbstractParser.message("strict.param.redefinition", parameterName), parameter.getToken());
                    }
                    parameterName = functionNode.uniqueName(parameterName);
                    long parameterToken = parameter.getToken();
                    parameters.set(i, new IdentNode(parameterToken, Token.descPosition(parameterToken), functionNode.uniqueName(parameterName)));
                }
                parametersSet.add(parameterName);
            }
        } else if (arity == 1 && Parser.isArguments(parameters.get(0))) {
            functionNode = functionNode.setFlag(this.lc, 256);
        }
        if (isStatement) {
            if (isAnonymous) {
                this.appendStatement(new ExpressionStatement(functionLine, functionToken, this.finish, functionNode));
                return functionNode;
            }
            int varFlags = topLevel || !this.useBlockScope() ? 0 : 1;
            VarNode varNode = new VarNode(functionLine, functionToken, this.finish, name, functionNode, varFlags);
            if (topLevel) {
                this.functionDeclarations.add(varNode);
            } else if (this.useBlockScope()) {
                this.prependStatement(varNode);
            } else {
                this.appendStatement(varNode);
            }
        }
        return functionNode;
    }

    private String getDefaultValidFunctionName(int functionLine, boolean isStatement) {
        String defaultFunctionName = this.getDefaultFunctionName();
        if (Parser.isValidIdentifier(defaultFunctionName)) {
            if (isStatement) {
                return CompilerConstants.ANON_FUNCTION_PREFIX.symbolName() + defaultFunctionName;
            }
            return defaultFunctionName;
        }
        return CompilerConstants.ANON_FUNCTION_PREFIX.symbolName() + functionLine;
    }

    private static boolean isValidIdentifier(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        if (!Character.isJavaIdentifierStart(name.charAt(0))) {
            return false;
        }
        for (int i = 1; i < name.length(); ++i) {
            if (Character.isJavaIdentifierPart(name.charAt(i))) continue;
            return false;
        }
        return true;
    }

    private String getDefaultFunctionName() {
        if (!this.defaultNames.isEmpty()) {
            Object nameExpr = this.defaultNames.peek();
            if (nameExpr instanceof PropertyKey) {
                this.markDefaultNameUsed();
                return ((PropertyKey)nameExpr).getPropertyName();
            }
            if (nameExpr instanceof AccessNode) {
                this.markDefaultNameUsed();
                return ((AccessNode)nameExpr).getProperty();
            }
        }
        return null;
    }

    private void markDefaultNameUsed() {
        this.defaultNames.pop();
        this.hideDefaultName();
    }

    private void hideDefaultName() {
        this.defaultNames.push("");
    }

    private List<IdentNode> formalParameterList() {
        return this.formalParameterList(TokenType.RPAREN);
    }

    private List<IdentNode> formalParameterList(TokenType endType) {
        ArrayList<IdentNode> parameters = new ArrayList<IdentNode>();
        boolean first = true;
        while (this.type != endType) {
            if (!first) {
                this.expect(TokenType.COMMARIGHT);
            } else {
                first = false;
            }
            IdentNode ident = this.getIdent();
            this.verifyStrictIdent(ident, "function parameter");
            parameters.add(ident);
        }
        parameters.trimToSize();
        return parameters;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private FunctionNode functionBody(long firstToken, IdentNode ident, List<IdentNode> parameters, FunctionNode.Kind kind, int functionLine) {
        RecompilableScriptFunctionData data;
        boolean parseBody;
        FunctionNode functionNode = null;
        long lastToken = 0L;
        ParserState endParserState = null;
        try {
            functionNode = this.newFunctionNode(firstToken, ident, parameters, kind, functionLine);
            assert (functionNode != null);
            int functionId = functionNode.getId();
            boolean bl = parseBody = this.reparsedFunction == null || functionId <= this.reparsedFunction.getFunctionNodeId();
            if (!this.env._no_syntax_extensions && this.type != TokenType.LBRACE) {
                Expression expr = this.assignmentExpression(true);
                lastToken = this.previousToken;
                assert (this.lc.getCurrentBlock() == this.lc.getFunctionBody(functionNode));
                int lastFinish = Token.descPosition(lastToken) + (Token.descType(lastToken) == TokenType.EOL ? 0 : Token.descLength(lastToken));
                if (parseBody) {
                    ReturnNode returnNode = new ReturnNode(functionNode.getLineNumber(), expr.getToken(), lastFinish, expr);
                    this.appendStatement(returnNode);
                }
                functionNode.setFinish(lastFinish);
            } else {
                this.expectDontAdvance(TokenType.LBRACE);
                if (parseBody || !this.skipFunctionBody(functionNode)) {
                    this.next();
                    List<Statement> prevFunctionDecls = this.functionDeclarations;
                    this.functionDeclarations = new ArrayList<Statement>();
                    try {
                        this.sourceElements(false);
                        this.addFunctionDeclarations(functionNode);
                    }
                    finally {
                        this.functionDeclarations = prevFunctionDecls;
                    }
                    lastToken = this.token;
                    if (parseBody) {
                        endParserState = new ParserState(Token.descPosition(this.token), this.line, this.linePosition);
                    }
                }
                this.expect(TokenType.RBRACE);
                functionNode.setFinish(this.finish);
            }
            functionNode = this.restoreFunctionNode(functionNode, lastToken);
        }
        catch (Throwable throwable) {
            functionNode = this.restoreFunctionNode(functionNode, lastToken);
            throw throwable;
        }
        if (parseBody) {
            functionNode = functionNode.setEndParserState(this.lc, endParserState);
        } else if (functionNode.getBody().getStatementCount() > 0) {
            functionNode = functionNode.setBody(null, functionNode.getBody().setStatements(null, Collections.emptyList()));
        }
        if (this.reparsedFunction != null && (data = this.reparsedFunction.getScriptFunctionData(functionNode.getId())) != null && (functionNode = functionNode.setFlags(this.lc, data.getFunctionFlags())).hasNestedEval()) {
            assert (functionNode.hasScopeBlock());
            functionNode = functionNode.setBody(this.lc, functionNode.getBody().setNeedsScope(null));
        }
        this.printAST(functionNode);
        return functionNode;
    }

    private boolean skipFunctionBody(FunctionNode functionNode) {
        if (this.reparsedFunction == null) {
            return false;
        }
        RecompilableScriptFunctionData data = this.reparsedFunction.getScriptFunctionData(functionNode.getId());
        if (data == null) {
            return false;
        }
        ParserState parserState = (ParserState)data.getEndParserState();
        assert (parserState != null);
        this.stream.reset();
        this.lexer = parserState.createLexer(this.source, this.lexer, this.stream, this.scripting && !this.env._no_syntax_extensions);
        this.line = parserState.line;
        this.linePosition = parserState.linePosition;
        this.type = TokenType.SEMICOLON;
        this.k = -1;
        this.next();
        return true;
    }

    private void printAST(FunctionNode functionNode) {
        if (functionNode.getFlag(524288)) {
            this.env.getErr().println(new ASTWriter(functionNode));
        }
        if (functionNode.getFlag(131072)) {
            this.env.getErr().println(new PrintVisitor(functionNode, true, false));
        }
    }

    private void addFunctionDeclarations(FunctionNode functionNode) {
        VarNode lastDecl = null;
        for (int i = this.functionDeclarations.size() - 1; i >= 0; --i) {
            Statement decl = this.functionDeclarations.get(i);
            if (lastDecl == null && decl instanceof VarNode) {
                lastDecl = ((VarNode)decl).setFlag(4);
                decl = lastDecl;
                this.lc.setFlag(functionNode, 1024);
            }
            this.prependStatement(decl);
        }
    }

    private RuntimeNode referenceError(Expression lhs, Expression rhs, boolean earlyError) {
        if (earlyError) {
            throw this.error(JSErrorType.REFERENCE_ERROR, AbstractParser.message("invalid.lvalue", new String[0]), lhs.getToken());
        }
        ArrayList<Expression> args2 = new ArrayList<Expression>();
        args2.add(lhs);
        if (rhs == null) {
            args2.add(LiteralNode.newInstance(lhs.getToken(), lhs.getFinish()));
        } else {
            args2.add(rhs);
        }
        args2.add(LiteralNode.newInstance(lhs.getToken(), lhs.getFinish(), lhs.toString()));
        return new RuntimeNode(lhs.getToken(), lhs.getFinish(), RuntimeNode.Request.REFERENCE_ERROR, args2);
    }

    private Expression unaryExpression() {
        int unaryLine = this.line;
        long unaryToken = this.token;
        switch (this.type) {
            case DELETE: {
                this.next();
                Expression expr = this.unaryExpression();
                if (expr instanceof BaseNode || expr instanceof IdentNode) {
                    return new UnaryNode(unaryToken, expr);
                }
                this.appendStatement(new ExpressionStatement(unaryLine, unaryToken, this.finish, expr));
                return LiteralNode.newInstance(unaryToken, this.finish, true);
            }
            case VOID: 
            case TYPEOF: 
            case ADD: 
            case SUB: 
            case BIT_NOT: 
            case NOT: {
                this.next();
                Expression expr = this.unaryExpression();
                return new UnaryNode(unaryToken, expr);
            }
            case INCPREFIX: 
            case DECPREFIX: {
                TokenType opType = this.type;
                this.next();
                Expression lhs = this.leftHandSideExpression();
                if (lhs == null) {
                    throw this.error(AbstractParser.message("expected.lvalue", this.type.getNameOrType()));
                }
                if (!(lhs instanceof AccessNode || lhs instanceof IndexNode || lhs instanceof IdentNode)) {
                    return this.referenceError(lhs, null, this.env._early_lvalue_error);
                }
                if (lhs instanceof IdentNode) {
                    if (!Parser.checkIdentLValue((IdentNode)lhs)) {
                        return this.referenceError(lhs, null, false);
                    }
                    this.verifyStrictIdent((IdentNode)lhs, "operand for " + opType.getName() + " operator");
                }
                return Parser.incDecExpression(unaryToken, opType, lhs, false);
            }
        }
        Expression expression = this.leftHandSideExpression();
        if (this.last != TokenType.EOL) {
            switch (this.type) {
                case INCPREFIX: 
                case DECPREFIX: {
                    TokenType opType = this.type;
                    Expression lhs = expression;
                    if (lhs == null) {
                        throw this.error(AbstractParser.message("expected.lvalue", this.type.getNameOrType()));
                    }
                    if (!(lhs instanceof AccessNode || lhs instanceof IndexNode || lhs instanceof IdentNode)) {
                        this.next();
                        return this.referenceError(lhs, null, this.env._early_lvalue_error);
                    }
                    if (lhs instanceof IdentNode) {
                        if (!Parser.checkIdentLValue((IdentNode)lhs)) {
                            this.next();
                            return this.referenceError(lhs, null, false);
                        }
                        this.verifyStrictIdent((IdentNode)lhs, "operand for " + opType.getName() + " operator");
                    }
                    expression = Parser.incDecExpression(this.token, this.type, expression, true);
                    this.next();
                    break;
                }
            }
        }
        if (expression == null) {
            throw this.error(AbstractParser.message("expected.operand", this.type.getNameOrType()));
        }
        return expression;
    }

    private Expression expression() {
        return this.expression(this.unaryExpression(), TokenType.COMMARIGHT.getPrecedence(), false);
    }

    private JoinPredecessorExpression joinPredecessorExpression() {
        return new JoinPredecessorExpression(this.expression());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private Expression expression(Expression exprLhs, int minPrecedence, boolean noIn) {
        int precedence = this.type.getPrecedence();
        Expression lhs = exprLhs;
        while (this.type.isOperator(noIn) && precedence >= minPrecedence) {
            long op = this.token;
            if (this.type == TokenType.TERNARY) {
                this.next();
                Expression trueExpr = this.expression(this.unaryExpression(), TokenType.ASSIGN.getPrecedence(), false);
                this.expect(TokenType.COLON);
                Expression falseExpr = this.expression(this.unaryExpression(), TokenType.ASSIGN.getPrecedence(), noIn);
                lhs = new TernaryNode(op, lhs, new JoinPredecessorExpression(trueExpr), new JoinPredecessorExpression(falseExpr));
            } else {
                Expression rhs;
                boolean isAssign;
                this.next();
                boolean bl = isAssign = Token.descType(op) == TokenType.ASSIGN;
                if (isAssign) {
                    this.defaultNames.push(lhs);
                }
                try {
                    rhs = this.unaryExpression();
                    int nextPrecedence = this.type.getPrecedence();
                    while (this.type.isOperator(noIn) && (nextPrecedence > precedence || nextPrecedence == precedence && !this.type.isLeftAssociative())) {
                        rhs = this.expression(rhs, nextPrecedence, noIn);
                        nextPrecedence = this.type.getPrecedence();
                    }
                }
                finally {
                    if (isAssign) {
                        this.defaultNames.pop();
                    }
                }
                lhs = this.verifyAssignment(op, lhs, rhs);
            }
            precedence = this.type.getPrecedence();
        }
        return lhs;
    }

    private Expression assignmentExpression(boolean noIn) {
        return this.expression(this.unaryExpression(), TokenType.ASSIGN.getPrecedence(), noIn);
    }

    private void endOfLine() {
        switch (this.type) {
            case EOL: 
            case SEMICOLON: {
                this.next();
                break;
            }
            case EOF: 
            case RBRACE: 
            case RPAREN: 
            case RBRACKET: {
                break;
            }
            default: {
                if (this.last == TokenType.EOL) break;
                this.expect(TokenType.SEMICOLON);
            }
        }
    }

    public String toString() {
        return "'JavaScript Parsing'";
    }

    private static void markEval(LexicalContext lc) {
        Iterator<FunctionNode> iter = lc.getFunctions();
        boolean flaggedCurrentFn = false;
        while (iter.hasNext()) {
            FunctionNode fn = iter.next();
            if (!flaggedCurrentFn) {
                lc.setFlag(fn, 32);
                flaggedCurrentFn = true;
            } else {
                lc.setFlag(fn, 64);
            }
            lc.setBlockNeedsScope(lc.getFunctionBody(fn));
        }
    }

    private void prependStatement(Statement statement) {
        this.lc.prependStatement(statement);
    }

    private void appendStatement(Statement statement) {
        this.lc.appendStatement(statement);
    }

    private static class ParserState
    implements Serializable {
        private final int position;
        private final int line;
        private final int linePosition;
        private static final long serialVersionUID = -2382565130754093694L;

        ParserState(int position, int line, int linePosition) {
            this.position = position;
            this.line = line;
            this.linePosition = linePosition;
        }

        Lexer createLexer(Source source, Lexer lexer, TokenStream stream, boolean scripting) {
            Lexer newLexer = new Lexer(source, this.position, lexer.limit - this.position, stream, scripting, true);
            newLexer.restoreState(new Lexer.State(this.position, Integer.MAX_VALUE, this.line, -1, this.linePosition, TokenType.SEMICOLON));
            return newLexer;
        }
    }

    private static class PropertyFunction {
        final PropertyKey ident;
        final FunctionNode functionNode;

        PropertyFunction(PropertyKey ident, FunctionNode function) {
            this.ident = ident;
            this.functionNode = function;
        }
    }
}

