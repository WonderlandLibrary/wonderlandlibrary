/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.text;

import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.SinceKotlin;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\f\n\u0002\b-\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0014\u001a\u00020\u00048\u0006X\u0087T\u00a2\u0006\b\n\u0000\u0012\u0004\b\u0015\u0010\u0002R\u0016\u0010\u0016\u001a\u00020\u00048\u0006X\u0087T\u00a2\u0006\b\n\u0000\u0012\u0004\b\u0017\u0010\u0002R\u000e\u0010\u0018\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u0016\u0010)\u001a\u00020\u00048\u0006X\u0087T\u00a2\u0006\b\n\u0000\u0012\u0004\b*\u0010\u0002R\u0016\u0010+\u001a\u00020\u00048\u0006X\u0087T\u00a2\u0006\b\n\u0000\u0012\u0004\b,\u0010\u0002R\u000e\u0010-\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u00061"}, d2={"Lkotlin/text/Typography;", "", "()V", "almostEqual", "", "amp", "bullet", "cent", "copyright", "dagger", "degree", "dollar", "doubleDagger", "doublePrime", "ellipsis", "euro", "greater", "greaterOrEqual", "half", "leftDoubleQuote", "leftGuillemet", "getLeftGuillemet$annotations", "leftGuillemete", "getLeftGuillemete$annotations", "leftSingleQuote", "less", "lessOrEqual", "lowDoubleQuote", "lowSingleQuote", "mdash", "middleDot", "nbsp", "ndash", "notEqual", "paragraph", "plusMinus", "pound", "prime", "quote", "registered", "rightDoubleQuote", "rightGuillemet", "getRightGuillemet$annotations", "rightGuillemete", "getRightGuillemete$annotations", "rightSingleQuote", "section", "times", "tm", "kotlin-stdlib"})
public final class Typography {
    @NotNull
    public static final Typography INSTANCE = new Typography();
    public static final char quote = '\"';
    public static final char dollar = '$';
    public static final char amp = '&';
    public static final char less = '<';
    public static final char greater = '>';
    public static final char nbsp = '\u00a0';
    public static final char times = '\u00d7';
    public static final char cent = '\u00a2';
    public static final char pound = '\u00a3';
    public static final char section = '\u00a7';
    public static final char copyright = '\u00a9';
    public static final char leftGuillemet = '\u00ab';
    public static final char rightGuillemet = '\u00bb';
    public static final char registered = '\u00ae';
    public static final char degree = '\u00b0';
    public static final char plusMinus = '\u00b1';
    public static final char paragraph = '\u00b6';
    public static final char middleDot = '\u00b7';
    public static final char half = '\u00bd';
    public static final char ndash = '\u2013';
    public static final char mdash = '\u2014';
    public static final char leftSingleQuote = '\u2018';
    public static final char rightSingleQuote = '\u2019';
    public static final char lowSingleQuote = '\u201a';
    public static final char leftDoubleQuote = '\u201c';
    public static final char rightDoubleQuote = '\u201d';
    public static final char lowDoubleQuote = '\u201e';
    public static final char dagger = '\u2020';
    public static final char doubleDagger = '\u2021';
    public static final char bullet = '\u2022';
    public static final char ellipsis = '\u2026';
    public static final char prime = '\u2032';
    public static final char doublePrime = '\u2033';
    public static final char euro = '\u20ac';
    public static final char tm = '\u2122';
    public static final char almostEqual = '\u2248';
    public static final char notEqual = '\u2260';
    public static final char lessOrEqual = '\u2264';
    public static final char greaterOrEqual = '\u2265';
    public static final char leftGuillemete = '\u00ab';
    public static final char rightGuillemete = '\u00bb';

    private Typography() {
    }

    @SinceKotlin(version="1.6")
    public static /* synthetic */ void getLeftGuillemet$annotations() {
    }

    @SinceKotlin(version="1.6")
    public static /* synthetic */ void getRightGuillemet$annotations() {
    }

    @Deprecated(message="This constant has a typo in the name. Use leftGuillemet instead.", replaceWith=@ReplaceWith(expression="Typography.leftGuillemet", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.6")
    public static /* synthetic */ void getLeftGuillemete$annotations() {
    }

    @Deprecated(message="This constant has a typo in the name. Use rightGuillemet instead.", replaceWith=@ReplaceWith(expression="Typography.rightGuillemet", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.6")
    public static /* synthetic */ void getRightGuillemete$annotations() {
    }
}

