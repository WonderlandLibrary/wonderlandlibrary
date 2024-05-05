/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.parser;

import java.util.HashMap;
import java.util.Map;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.parser.Lexer;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.internal.parser.TokenKind;
import jdk.nashorn.internal.parser.TokenStream;
import jdk.nashorn.internal.parser.TokenType;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.JSErrorType;
import jdk.nashorn.internal.runtime.ParserException;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.regexp.RegExpFactory;

public abstract class AbstractParser {
    protected final Source source;
    protected final ErrorManager errors;
    protected TokenStream stream;
    protected int k;
    protected long previousToken;
    protected long token;
    protected TokenType type;
    protected TokenType last;
    protected int start;
    protected int finish;
    protected int line;
    protected int linePosition;
    protected Lexer lexer;
    protected boolean isStrictMode;
    protected final int lineOffset;
    private final Map<String, String> canonicalNames = new HashMap<String, String>();
    private static final String SOURCE_URL_PREFIX = "sourceURL=";

    protected AbstractParser(Source source, ErrorManager errors, boolean strict, int lineOffset) {
        this.source = source;
        this.errors = errors;
        this.k = -1;
        this.token = Token.toDesc(TokenType.EOL, 0, 1);
        this.type = TokenType.EOL;
        this.last = TokenType.EOL;
        this.isStrictMode = strict;
        this.lineOffset = lineOffset;
    }

    protected final long getToken(int i) {
        while (i > this.stream.last()) {
            if (this.stream.isFull()) {
                this.stream.grow();
            }
            this.lexer.lexify();
        }
        return this.stream.get(i);
    }

    protected final TokenType T(int i) {
        return Token.descType(this.getToken(i));
    }

    protected final TokenType next() {
        do {
            this.nextOrEOL();
        } while (this.type == TokenType.EOL || this.type == TokenType.COMMENT);
        return this.type;
    }

    protected final TokenType nextOrEOL() {
        do {
            this.nextToken();
            if (this.type != TokenType.DIRECTIVE_COMMENT) continue;
            this.checkDirectiveComment();
        } while (this.type == TokenType.COMMENT || this.type == TokenType.DIRECTIVE_COMMENT);
        return this.type;
    }

    private void checkDirectiveComment() {
        if (this.source.getExplicitURL() != null) {
            return;
        }
        String comment = (String)this.lexer.getValueOf(this.token, this.isStrictMode);
        int len = comment.length();
        if (len > 4 && comment.substring(4).startsWith(SOURCE_URL_PREFIX)) {
            this.source.setExplicitURL(comment.substring(4 + SOURCE_URL_PREFIX.length()));
        }
    }

    private TokenType nextToken() {
        if (this.type != TokenType.COMMENT) {
            this.last = this.type;
        }
        if (this.type != TokenType.EOF) {
            ++this.k;
            long lastToken = this.token;
            this.previousToken = this.token;
            this.token = this.getToken(this.k);
            this.type = Token.descType(this.token);
            if (this.last != TokenType.EOL) {
                this.finish = this.start + Token.descLength(lastToken);
            }
            if (this.type == TokenType.EOL) {
                this.line = Token.descLength(this.token);
                this.linePosition = Token.descPosition(this.token);
            } else {
                this.start = Token.descPosition(this.token);
            }
        }
        return this.type;
    }

    protected static String message(String msgId, String ... args2) {
        return ECMAErrors.getMessage("parser.error." + msgId, args2);
    }

    protected final ParserException error(String message, long errorToken) {
        return this.error(JSErrorType.SYNTAX_ERROR, message, errorToken);
    }

    protected final ParserException error(JSErrorType errorType, String message, long errorToken) {
        int position = Token.descPosition(errorToken);
        int lineNum = this.source.getLine(position);
        int columnNum = this.source.getColumn(position);
        String formatted = ErrorManager.format(message, this.source, lineNum, columnNum, errorToken);
        return new ParserException(errorType, formatted, this.source, lineNum, columnNum, errorToken);
    }

    protected final ParserException error(String message) {
        return this.error(JSErrorType.SYNTAX_ERROR, message);
    }

    protected final ParserException error(JSErrorType errorType, String message) {
        int position = Token.descPosition(this.token);
        int column = position - this.linePosition;
        String formatted = ErrorManager.format(message, this.source, this.line, column, this.token);
        return new ParserException(errorType, formatted, this.source, this.line, column, this.token);
    }

    protected final void warning(JSErrorType errorType, String message, long errorToken) {
        this.errors.warning(this.error(errorType, message, errorToken));
    }

    protected final String expectMessage(TokenType expected) {
        String msg;
        String tokenString = Token.toString(this.source, this.token);
        if (expected == null) {
            msg = AbstractParser.message("expected.stmt", tokenString);
        } else {
            String expectedName = expected.getNameOrType();
            msg = AbstractParser.message("expected", expectedName, tokenString);
        }
        return msg;
    }

    protected final void expect(TokenType expected) throws ParserException {
        this.expectDontAdvance(expected);
        this.next();
    }

    protected final void expectDontAdvance(TokenType expected) throws ParserException {
        if (this.type != expected) {
            throw this.error(this.expectMessage(expected));
        }
    }

    protected final Object expectValue(TokenType expected) throws ParserException {
        if (this.type != expected) {
            throw this.error(this.expectMessage(expected));
        }
        Object value = this.getValue();
        this.next();
        return value;
    }

    protected final Object getValue() {
        return this.getValue(this.token);
    }

    protected final Object getValue(long valueToken) {
        try {
            return this.lexer.getValueOf(valueToken, this.isStrictMode);
        }
        catch (ParserException e) {
            this.errors.error(e);
            return null;
        }
    }

    protected final boolean isNonStrictModeIdent() {
        return !this.isStrictMode && this.type.getKind() == TokenKind.FUTURESTRICT;
    }

    protected final IdentNode getIdent() {
        long identToken = this.token;
        if (this.isNonStrictModeIdent()) {
            identToken = Token.recast(this.token, TokenType.IDENT);
            String ident = (String)this.getValue(identToken);
            this.next();
            return this.createIdentNode(identToken, this.finish, ident).setIsFutureStrictName();
        }
        String ident = (String)this.expectValue(TokenType.IDENT);
        if (ident == null) {
            return null;
        }
        return this.createIdentNode(identToken, this.finish, ident);
    }

    protected IdentNode createIdentNode(long identToken, int identFinish, String name) {
        String existingName = this.canonicalNames.putIfAbsent(name, name);
        String canonicalName = existingName != null ? existingName : name;
        return new IdentNode(identToken, identFinish, canonicalName);
    }

    protected final boolean isIdentifierName() {
        TokenKind kind = this.type.getKind();
        if (kind == TokenKind.KEYWORD || kind == TokenKind.FUTURE || kind == TokenKind.FUTURESTRICT) {
            return true;
        }
        if (kind == TokenKind.LITERAL) {
            switch (this.type) {
                case FALSE: 
                case NULL: 
                case TRUE: {
                    return true;
                }
            }
            return false;
        }
        long identToken = Token.recast(this.token, TokenType.IDENT);
        String ident = (String)this.getValue(identToken);
        return !ident.isEmpty() && Character.isJavaIdentifierStart(ident.charAt(0));
    }

    protected final IdentNode getIdentifierName() {
        if (this.type == TokenType.IDENT) {
            return this.getIdent();
        }
        if (this.isIdentifierName()) {
            long identToken = Token.recast(this.token, TokenType.IDENT);
            String ident = (String)this.getValue(identToken);
            this.next();
            return this.createIdentNode(identToken, this.finish, ident);
        }
        this.expect(TokenType.IDENT);
        return null;
    }

    protected final LiteralNode<?> getLiteral() throws ParserException {
        long literalToken = this.token;
        Object value = this.getValue();
        this.next();
        LiteralNode<Object> node = null;
        if (value == null) {
            node = LiteralNode.newInstance(literalToken, this.finish);
        } else if (value instanceof Number) {
            node = LiteralNode.newInstance(literalToken, this.finish, (Number)value);
        } else if (value instanceof String) {
            node = LiteralNode.newInstance(literalToken, this.finish, (String)value);
        } else if (value instanceof Lexer.LexerToken) {
            if (value instanceof Lexer.RegexToken) {
                Lexer.RegexToken regex = (Lexer.RegexToken)value;
                try {
                    RegExpFactory.validate(regex.getExpression(), regex.getOptions());
                }
                catch (ParserException e) {
                    throw this.error(e.getMessage());
                }
            }
            node = LiteralNode.newInstance(literalToken, this.finish, (Lexer.LexerToken)value);
        } else assert (false) : "unknown type for LiteralNode: " + value.getClass();
        return node;
    }
}

