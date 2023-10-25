package net.optifine.expr;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import net.optifine.expr.ParseException;
import net.optifine.expr.Token;
import net.optifine.expr.TokenType;

public class TokenParser {
    public static Token[] parse(String str) throws IOException, ParseException {
        StringReader reader = new StringReader(str);
        PushbackReader pushbackreader = new PushbackReader((Reader)reader);
        ArrayList list = new ArrayList();
        while (true) {
            int i;
            if ((i = pushbackreader.read()) < 0) {
                Token[] atoken = (Token[])list.toArray((Object[])new Token[list.size()]);
                return atoken;
            }
            char c0 = (char)i;
            if (Character.isWhitespace((char)c0)) continue;
            TokenType tokentype = TokenType.getTypeByFirstChar((char)c0);
            if (tokentype == null) {
                throw new ParseException("Invalid character: '" + c0 + "', in: " + str);
            }
            Token token = TokenParser.readToken(c0, tokentype, pushbackreader);
            list.add((Object)token);
        }
    }

    private static Token readToken(char chFirst, TokenType type, PushbackReader pr) throws IOException {
        int i;
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append(chFirst);
        while ((i = pr.read()) >= 0) {
            char c0 = (char)i;
            if (!type.hasCharNext(c0)) {
                pr.unread((int)c0);
                break;
            }
            stringbuffer.append(c0);
        }
        return new Token(type, stringbuffer.toString());
    }
}
