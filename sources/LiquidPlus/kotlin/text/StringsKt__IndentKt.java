/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;
import kotlin.text.StringsKt__AppendableKt;
import kotlin.text.StringsKt__IndentKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=5, xi=49, d1={"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u000b\u001a!\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0002\u00a2\u0006\u0002\b\u0004\u001a\u0011\u0010\u0005\u001a\u00020\u0006*\u00020\u0002H\u0002\u00a2\u0006\u0002\b\u0007\u001a\u0014\u0010\b\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0002\u001aJ\u0010\t\u001a\u00020\u0002*\b\u0012\u0004\u0012\u00020\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00062\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00012\u0014\u0010\r\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001H\u0082\b\u00a2\u0006\u0002\b\u000e\u001a\u0014\u0010\u000f\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0010\u001a\u00020\u0002\u001a\u001e\u0010\u0011\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0012\u001a\u00020\u0002\u001a\n\u0010\u0013\u001a\u00020\u0002*\u00020\u0002\u001a\u0014\u0010\u0014\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0012\u001a\u00020\u0002\u00a8\u0006\u0015"}, d2={"getIndentFunction", "Lkotlin/Function1;", "", "indent", "getIndentFunction$StringsKt__IndentKt", "indentWidth", "", "indentWidth$StringsKt__IndentKt", "prependIndent", "reindent", "", "resultSizeEstimate", "indentAddFunction", "indentCutFunction", "reindent$StringsKt__IndentKt", "replaceIndent", "newIndent", "replaceIndentByMargin", "marginPrefix", "trimIndent", "trimMargin", "kotlin-stdlib"}, xs="kotlin/text/StringsKt")
class StringsKt__IndentKt
extends StringsKt__AppendableKt {
    @NotNull
    public static final String trimMargin(@NotNull String $this$trimMargin, @NotNull String marginPrefix) {
        Intrinsics.checkNotNullParameter($this$trimMargin, "<this>");
        Intrinsics.checkNotNullParameter(marginPrefix, "marginPrefix");
        return StringsKt.replaceIndentByMargin($this$trimMargin, "", marginPrefix);
    }

    public static /* synthetic */ String trimMargin$default(String string, String string2, int n, Object object) {
        if ((n & 1) != 0) {
            string2 = "|";
        }
        return StringsKt.trimMargin(string, string2);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final String replaceIndentByMargin(@NotNull String $this$replaceIndentByMargin, @NotNull String newIndent, @NotNull String marginPrefix) {
        void resultSizeEstimate$iv;
        void $this$mapIndexedNotNullTo$iv$iv$iv;
        void $this$reindent$iv;
        List<String> lines;
        boolean bl;
        Intrinsics.checkNotNullParameter($this$replaceIndentByMargin, "<this>");
        Intrinsics.checkNotNullParameter(newIndent, "newIndent");
        Intrinsics.checkNotNullParameter(marginPrefix, "marginPrefix");
        boolean bl2 = bl = !StringsKt.isBlank(marginPrefix);
        if (!bl) {
            boolean $i$a$-require-StringsKt__IndentKt$replaceIndentByMargin$22 = false;
            String $i$a$-require-StringsKt__IndentKt$replaceIndentByMargin$22 = "marginPrefix must be non-blank string.";
            throw new IllegalArgumentException($i$a$-require-StringsKt__IndentKt$replaceIndentByMargin$22.toString());
        }
        List<String> $i$a$-require-StringsKt__IndentKt$replaceIndentByMargin$22 = lines = StringsKt.lines($this$replaceIndentByMargin);
        int n = $this$replaceIndentByMargin.length() + newIndent.length() * lines.size();
        Function1<String, String> indentAddFunction$iv = StringsKt__IndentKt.getIndentFunction$StringsKt__IndentKt(newIndent);
        boolean $i$f$reindent = false;
        int lastIndex$iv = CollectionsKt.getLastIndex($this$reindent$iv);
        Iterable $this$mapIndexedNotNull$iv$iv = (Iterable)$this$reindent$iv;
        boolean $i$f$mapIndexedNotNull = false;
        Iterable iterable = $this$mapIndexedNotNull$iv$iv;
        Collection destination$iv$iv$iv = new ArrayList();
        boolean $i$f$mapIndexedNotNullTo = false;
        void $this$forEachIndexed$iv$iv$iv$iv = $this$mapIndexedNotNullTo$iv$iv$iv;
        boolean $i$f$forEachIndexed = false;
        int index$iv$iv$iv$iv = 0;
        for (Object item$iv$iv$iv$iv : $this$forEachIndexed$iv$iv$iv$iv) {
            String string;
            String string2;
            String string3;
            void value$iv;
            void element$iv$iv$iv;
            int n2 = index$iv$iv$iv$iv;
            index$iv$iv$iv$iv = n2 + 1;
            if (n2 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            Object t = item$iv$iv$iv$iv;
            int index$iv$iv$iv = n2;
            boolean bl3 = false;
            String string4 = (String)element$iv$iv$iv;
            int index$iv = index$iv$iv$iv;
            boolean bl4 = false;
            if ((index$iv == 0 || index$iv == lastIndex$iv) && StringsKt.isBlank((CharSequence)value$iv)) {
                string3 = null;
            } else {
                String string5;
                String string6;
                int n3;
                void line;
                block12: {
                    line = value$iv;
                    boolean bl5 = false;
                    CharSequence $this$indexOfFirst$iv = (CharSequence)line;
                    boolean $i$f$indexOfFirst = false;
                    int n4 = 0;
                    int n5 = $this$indexOfFirst$iv.length();
                    while (n4 < n5) {
                        int index$iv2 = n4++;
                        char it = $this$indexOfFirst$iv.charAt(index$iv2);
                        boolean bl6 = false;
                        if (!(!CharsKt.isWhitespace(it))) continue;
                        n3 = index$iv2;
                        break block12;
                    }
                    n3 = -1;
                }
                int firstNonWhitespaceIndex = n3;
                if (firstNonWhitespaceIndex == -1) {
                    string6 = null;
                } else if (StringsKt.startsWith$default((String)line, marginPrefix, firstNonWhitespaceIndex, false, 4, null)) {
                    void var28_31 = line;
                    int n6 = firstNonWhitespaceIndex + marginPrefix.length();
                    String string7 = var28_31.substring(n6);
                    Intrinsics.checkNotNullExpressionValue(string7, "this as java.lang.String).substring(startIndex)");
                    string6 = string7;
                } else {
                    string6 = null;
                }
                if ((string5 = string6) == null) {
                    string3 = value$iv;
                } else {
                    String string8 = string5;
                    string3 = indentAddFunction$iv.invoke(string8);
                }
            }
            if ((string2 = string3) == null) continue;
            String it$iv$iv$iv = string = string2;
            boolean bl7 = false;
            destination$iv$iv$iv.add(it$iv$iv$iv);
        }
        String string = ((StringBuilder)CollectionsKt.joinTo$default((List)destination$iv$iv$iv, new StringBuilder((int)resultSizeEstimate$iv), "\n", null, null, 0, null, null, 124, null)).toString();
        Intrinsics.checkNotNullExpressionValue(string, "mapIndexedNotNull { inde\u2026\"\\n\")\n        .toString()");
        return string;
    }

    public static /* synthetic */ String replaceIndentByMargin$default(String string, String string2, String string3, int n, Object object) {
        if ((n & 1) != 0) {
            string2 = "";
        }
        if ((n & 2) != 0) {
            string3 = "|";
        }
        return StringsKt.replaceIndentByMargin(string, string2, string3);
    }

    @NotNull
    public static final String trimIndent(@NotNull String $this$trimIndent) {
        Intrinsics.checkNotNullParameter($this$trimIndent, "<this>");
        return StringsKt.replaceIndent($this$trimIndent, "");
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final String replaceIndent(@NotNull String $this$replaceIndent, @NotNull String newIndent) {
        void resultSizeEstimate$iv;
        void $this$mapIndexedNotNullTo$iv$iv$iv;
        void $this$reindent$iv;
        Object item$iv$iv2;
        void $this$mapTo$iv$iv;
        void $this$map$iv22;
        String p0;
        void $this$filterTo$iv$iv;
        Iterable $this$filter$iv;
        Intrinsics.checkNotNullParameter($this$replaceIndent, "<this>");
        Intrinsics.checkNotNullParameter(newIndent, "newIndent");
        List<String> lines = StringsKt.lines($this$replaceIndent);
        Iterable iterable = lines;
        boolean $i$f$filter = false;
        void var7_7 = $this$filter$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        for (Object element$iv$iv : $this$filterTo$iv$iv) {
            p0 = (String)element$iv$iv;
            boolean bl = false;
            if (!(!StringsKt.isBlank(p0))) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        $this$filter$iv = (List)destination$iv$iv;
        boolean $i$f$map = false;
        $this$filterTo$iv$iv = $this$map$iv22;
        destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv22, 10));
        boolean $i$f$mapTo = false;
        for (Object item$iv$iv2 : $this$mapTo$iv$iv) {
            p0 = (String)item$iv$iv2;
            Collection collection = destination$iv$iv;
            boolean bl = false;
            Integer n = StringsKt__IndentKt.indentWidth$StringsKt__IndentKt(p0);
            collection.add(n);
        }
        Object object = (Integer)CollectionsKt.minOrNull((List)destination$iv$iv);
        int minCommonIndent = object == null ? 0 : (Integer)object;
        object = lines;
        int $this$map$iv22 = $this$replaceIndent.length() + newIndent.length() * lines.size();
        Function1<String, String> indentAddFunction$iv = StringsKt__IndentKt.getIndentFunction$StringsKt__IndentKt(newIndent);
        boolean $i$f$reindent = false;
        int lastIndex$iv = CollectionsKt.getLastIndex($this$reindent$iv);
        Iterable $this$mapIndexedNotNull$iv$iv = (Iterable)$this$reindent$iv;
        boolean $i$f$mapIndexedNotNull = false;
        item$iv$iv2 = $this$mapIndexedNotNull$iv$iv;
        Collection destination$iv$iv$iv = new ArrayList();
        boolean $i$f$mapIndexedNotNullTo = false;
        void $this$forEachIndexed$iv$iv$iv$iv = $this$mapIndexedNotNullTo$iv$iv$iv;
        boolean $i$f$forEachIndexed = false;
        int index$iv$iv$iv$iv = 0;
        for (Object item$iv$iv$iv$iv : $this$forEachIndexed$iv$iv$iv$iv) {
            String string;
            String string2;
            String string3;
            void value$iv;
            void element$iv$iv$iv;
            int n = index$iv$iv$iv$iv;
            index$iv$iv$iv$iv = n + 1;
            if (n < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            Object t = item$iv$iv$iv$iv;
            int index$iv$iv$iv = n;
            boolean bl = false;
            String string4 = (String)element$iv$iv$iv;
            int index$iv = index$iv$iv$iv;
            boolean bl2 = false;
            if ((index$iv == 0 || index$iv == lastIndex$iv) && StringsKt.isBlank((CharSequence)value$iv)) {
                string3 = null;
            } else {
                void line = value$iv;
                boolean bl3 = false;
                String string5 = StringsKt.drop((String)line, minCommonIndent);
                if (string5 == null) {
                    string3 = value$iv;
                } else {
                    String string6 = string5;
                    string3 = indentAddFunction$iv.invoke(string6);
                }
            }
            if ((string2 = string3) == null) continue;
            String it$iv$iv$iv = string = string2;
            boolean bl4 = false;
            destination$iv$iv$iv.add(it$iv$iv$iv);
        }
        String string = ((StringBuilder)CollectionsKt.joinTo$default((List)destination$iv$iv$iv, new StringBuilder((int)resultSizeEstimate$iv), "\n", null, null, 0, null, null, 124, null)).toString();
        Intrinsics.checkNotNullExpressionValue(string, "mapIndexedNotNull { inde\u2026\"\\n\")\n        .toString()");
        return string;
    }

    public static /* synthetic */ String replaceIndent$default(String string, String string2, int n, Object object) {
        if ((n & 1) != 0) {
            string2 = "";
        }
        return StringsKt.replaceIndent(string, string2);
    }

    @NotNull
    public static final String prependIndent(@NotNull String $this$prependIndent, @NotNull String indent) {
        Intrinsics.checkNotNullParameter($this$prependIndent, "<this>");
        Intrinsics.checkNotNullParameter(indent, "indent");
        return SequencesKt.joinToString$default(SequencesKt.map(StringsKt.lineSequence($this$prependIndent), (Function1)new Function1<String, String>(indent){
            final /* synthetic */ String $indent;
            {
                this.$indent = $indent;
                super(1);
            }

            @NotNull
            public final String invoke(@NotNull String it) {
                Intrinsics.checkNotNullParameter(it, "it");
                return StringsKt.isBlank(it) ? (it.length() < this.$indent.length() ? this.$indent : it) : Intrinsics.stringPlus(this.$indent, it);
            }
        }), "\n", null, null, 0, null, null, 62, null);
    }

    public static /* synthetic */ String prependIndent$default(String string, String string2, int n, Object object) {
        if ((n & 1) != 0) {
            string2 = "    ";
        }
        return StringsKt.prependIndent(string, string2);
    }

    /*
     * WARNING - void declaration
     */
    private static final int indentWidth$StringsKt__IndentKt(String $this$indentWidth) {
        void var2_3;
        int n;
        int n2;
        block1: {
            CharSequence $this$indexOfFirst$iv = $this$indentWidth;
            boolean $i$f$indexOfFirst = false;
            int n3 = 0;
            int n4 = $this$indexOfFirst$iv.length();
            while (n3 < n4) {
                int index$iv = n3++;
                char it = $this$indexOfFirst$iv.charAt(index$iv);
                boolean bl = false;
                if (!(!CharsKt.isWhitespace(it))) continue;
                n2 = index$iv;
                break block1;
            }
            n2 = -1;
        }
        int it = n = n2;
        boolean bl = false;
        return it == -1 ? $this$indentWidth.length() : var2_3;
    }

    private static final Function1<String, String> getIndentFunction$StringsKt__IndentKt(String indent) {
        return ((CharSequence)indent).length() == 0 ? (Function1)getIndentFunction.1.INSTANCE : (Function1)new Function1<String, String>(indent){
            final /* synthetic */ String $indent;
            {
                this.$indent = $indent;
                super(1);
            }

            @NotNull
            public final String invoke(@NotNull String line) {
                Intrinsics.checkNotNullParameter(line, "line");
                return Intrinsics.stringPlus(this.$indent, line);
            }
        };
    }

    /*
     * WARNING - void declaration
     */
    private static final String reindent$StringsKt__IndentKt(List<String> $this$reindent, int resultSizeEstimate, Function1<? super String, String> indentAddFunction, Function1<? super String, String> indentCutFunction) {
        void $this$mapIndexedNotNullTo$iv$iv;
        boolean $i$f$reindent = false;
        int lastIndex = CollectionsKt.getLastIndex($this$reindent);
        Iterable $this$mapIndexedNotNull$iv = $this$reindent;
        boolean $i$f$mapIndexedNotNull = false;
        Iterable iterable = $this$mapIndexedNotNull$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$mapIndexedNotNullTo = false;
        void $this$forEachIndexed$iv$iv$iv = $this$mapIndexedNotNullTo$iv$iv;
        boolean $i$f$forEachIndexed = false;
        int index$iv$iv$iv = 0;
        for (Object item$iv$iv$iv : $this$forEachIndexed$iv$iv$iv) {
            String string;
            String string2;
            String string3;
            void value;
            void element$iv$iv;
            int n = index$iv$iv$iv;
            index$iv$iv$iv = n + 1;
            if (n < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            Object t = item$iv$iv$iv;
            int index$iv$iv = n;
            boolean bl = false;
            String string4 = (String)element$iv$iv;
            int index = index$iv$iv;
            boolean bl2 = false;
            if ((index == 0 || index == lastIndex) && StringsKt.isBlank((CharSequence)value)) {
                string3 = null;
            } else {
                String string5 = indentCutFunction.invoke((String)value);
                if (string5 == null) {
                    string3 = value;
                } else {
                    String string6 = string5;
                    string3 = indentAddFunction.invoke(string6);
                }
            }
            if ((string2 = string3) == null) continue;
            String it$iv$iv = string = string2;
            boolean bl3 = false;
            destination$iv$iv.add(it$iv$iv);
        }
        String string = ((StringBuilder)CollectionsKt.joinTo$default((List)destination$iv$iv, new StringBuilder(resultSizeEstimate), "\n", null, null, 0, null, null, 124, null)).toString();
        Intrinsics.checkNotNullExpressionValue(string, "mapIndexedNotNull { inde\u2026\"\\n\")\n        .toString()");
        return string;
    }
}

