/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import jdk.nashorn.internal.runtime.ParserException;
import jdk.nashorn.internal.runtime.regexp.RegExp;
import jdk.nashorn.internal.runtime.regexp.RegExpFactory;
import jdk.nashorn.internal.runtime.regexp.RegExpMatcher;
import jdk.nashorn.internal.runtime.regexp.RegExpScanner;
import jdk.nashorn.internal.runtime.regexp.joni.Matcher;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import jdk.nashorn.internal.runtime.regexp.joni.Region;
import jdk.nashorn.internal.runtime.regexp.joni.Syntax;
import jdk.nashorn.internal.runtime.regexp.joni.exception.JOniException;

public class JoniRegExp
extends RegExp {
    private Regex regex;

    public JoniRegExp(String pattern, String flags) throws ParserException {
        super(pattern, flags);
        int option = 8;
        if (this.isIgnoreCase()) {
            option |= 1;
        }
        if (this.isMultiline()) {
            option &= 0xFFFFFFF7;
            option |= 0x40;
        }
        try {
            RegExpScanner parsed;
            try {
                parsed = RegExpScanner.scan(pattern);
            }
            catch (PatternSyntaxException e) {
                Pattern.compile(pattern, 0);
                throw e;
            }
            if (parsed != null) {
                char[] javaPattern = parsed.getJavaPattern().toCharArray();
                this.regex = new Regex(javaPattern, 0, javaPattern.length, option, Syntax.JAVASCRIPT);
                this.groupsInNegativeLookahead = parsed.getGroupsInNegativeLookahead();
            }
        }
        catch (PatternSyntaxException | JOniException e2) {
            JoniRegExp.throwParserException("syntax", e2.getMessage());
        }
    }

    @Override
    public RegExpMatcher match(String input) {
        if (this.regex == null) {
            return null;
        }
        return new JoniMatcher(input);
    }

    class JoniMatcher
    implements RegExpMatcher {
        final String input;
        final Matcher joniMatcher;

        JoniMatcher(String input) {
            this.input = input;
            this.joniMatcher = JoniRegExp.this.regex.matcher(input.toCharArray());
        }

        @Override
        public boolean search(int start) {
            return this.joniMatcher.search(start, this.input.length(), 0) > -1;
        }

        @Override
        public String getInput() {
            return this.input;
        }

        @Override
        public int start() {
            return this.joniMatcher.getBegin();
        }

        @Override
        public int start(int group) {
            return group == 0 ? this.start() : this.joniMatcher.getRegion().beg[group];
        }

        @Override
        public int end() {
            return this.joniMatcher.getEnd();
        }

        @Override
        public int end(int group) {
            return group == 0 ? this.end() : this.joniMatcher.getRegion().end[group];
        }

        @Override
        public String group() {
            return this.input.substring(this.joniMatcher.getBegin(), this.joniMatcher.getEnd());
        }

        @Override
        public String group(int group) {
            if (group == 0) {
                return this.group();
            }
            Region region = this.joniMatcher.getRegion();
            return this.input.substring(region.beg[group], region.end[group]);
        }

        @Override
        public int groupCount() {
            Region region = this.joniMatcher.getRegion();
            return region == null ? 0 : region.numRegs - 1;
        }
    }

    public static class Factory
    extends RegExpFactory {
        @Override
        public RegExp compile(String pattern, String flags) throws ParserException {
            return new JoniRegExp(pattern, flags);
        }
    }
}

