/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.io.FilePathComponents;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=5, xi=49, d1={"\u0000$\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u001a\u0011\u0010\u000b\u001a\u00020\f*\u00020\bH\u0002\u00a2\u0006\u0002\b\r\u001a\u001c\u0010\u000e\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u000f\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\fH\u0000\u001a\f\u0010\u0011\u001a\u00020\u0012*\u00020\u0002H\u0000\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F\u00a2\u0006\u0006\u001a\u0004\b\u0000\u0010\u0003\"\u0018\u0010\u0004\u001a\u00020\u0002*\u00020\u00028@X\u0080\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006\"\u0018\u0010\u0007\u001a\u00020\b*\u00020\u00028@X\u0080\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0013"}, d2={"isRooted", "", "Ljava/io/File;", "(Ljava/io/File;)Z", "root", "getRoot", "(Ljava/io/File;)Ljava/io/File;", "rootName", "", "getRootName", "(Ljava/io/File;)Ljava/lang/String;", "getRootLength", "", "getRootLength$FilesKt__FilePathComponentsKt", "subPath", "beginIndex", "endIndex", "toComponents", "Lkotlin/io/FilePathComponents;", "kotlin-stdlib"}, xs="kotlin/io/FilesKt")
class FilesKt__FilePathComponentsKt {
    private static final int getRootLength$FilesKt__FilePathComponentsKt(String $this$getRootLength) {
        int first = StringsKt.indexOf$default((CharSequence)$this$getRootLength, File.separatorChar, 0, false, 4, null);
        if (first == 0) {
            if ($this$getRootLength.length() > 1 && $this$getRootLength.charAt(1) == File.separatorChar && (first = StringsKt.indexOf$default((CharSequence)$this$getRootLength, File.separatorChar, 2, false, 4, null)) >= 0) {
                if ((first = StringsKt.indexOf$default((CharSequence)$this$getRootLength, File.separatorChar, first + 1, false, 4, null)) >= 0) {
                    return first + 1;
                }
                return $this$getRootLength.length();
            }
            return 1;
        }
        if (first > 0 && $this$getRootLength.charAt(first - 1) == ':') {
            int n = first;
            first = n + 1;
            return first;
        }
        if (first == -1 && StringsKt.endsWith$default((CharSequence)$this$getRootLength, ':', false, 2, null)) {
            return $this$getRootLength.length();
        }
        return 0;
    }

    @NotNull
    public static final String getRootName(@NotNull File $this$rootName) {
        Intrinsics.checkNotNullParameter($this$rootName, "<this>");
        String string = $this$rootName.getPath();
        Intrinsics.checkNotNullExpressionValue(string, "path");
        int n = 0;
        String string2 = $this$rootName.getPath();
        Intrinsics.checkNotNullExpressionValue(string2, "path");
        int n2 = FilesKt__FilePathComponentsKt.getRootLength$FilesKt__FilePathComponentsKt(string2);
        String string3 = string.substring(n, n2);
        Intrinsics.checkNotNullExpressionValue(string3, "this as java.lang.String\u2026ing(startIndex, endIndex)");
        return string3;
    }

    @NotNull
    public static final File getRoot(@NotNull File $this$root) {
        Intrinsics.checkNotNullParameter($this$root, "<this>");
        return new File(FilesKt.getRootName($this$root));
    }

    public static final boolean isRooted(@NotNull File $this$isRooted) {
        Intrinsics.checkNotNullParameter($this$isRooted, "<this>");
        String string = $this$isRooted.getPath();
        Intrinsics.checkNotNullExpressionValue(string, "path");
        return FilesKt__FilePathComponentsKt.getRootLength$FilesKt__FilePathComponentsKt(string) > 0;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final FilePathComponents toComponents(@NotNull File $this$toComponents) {
        List list;
        Intrinsics.checkNotNullParameter($this$toComponents, "<this>");
        String path = $this$toComponents.getPath();
        Intrinsics.checkNotNullExpressionValue(path, "path");
        int rootLength = FilesKt__FilePathComponentsKt.getRootLength$FilesKt__FilePathComponentsKt(path);
        String string = path;
        int n = 0;
        Object object = string.substring(n, rootLength);
        Intrinsics.checkNotNullExpressionValue(object, "this as java.lang.String\u2026ing(startIndex, endIndex)");
        String rootName = object;
        String string2 = path;
        object = string2.substring(rootLength);
        Intrinsics.checkNotNullExpressionValue(object, "this as java.lang.String).substring(startIndex)");
        String subPath = object;
        if (((CharSequence)subPath).length() == 0) {
            list = CollectionsKt.emptyList();
        } else {
            void $this$mapTo$iv$iv;
            object = new char[1];
            object[0] = File.separatorChar;
            Iterable $this$map$iv = StringsKt.split$default((CharSequence)subPath, (char[])object, false, 0, 6, null);
            boolean $i$f$map = false;
            Iterable iterable = $this$map$iv;
            Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
            boolean $i$f$mapTo = false;
            for (Object item$iv$iv : $this$mapTo$iv$iv) {
                void p0;
                String string3 = (String)item$iv$iv;
                Collection collection = destination$iv$iv;
                boolean bl = false;
                File file = new File((String)p0);
                collection.add(file);
            }
            list = (List)destination$iv$iv;
        }
        List list2 = list;
        return new FilePathComponents(new File(rootName), list2);
    }

    @NotNull
    public static final File subPath(@NotNull File $this$subPath, int beginIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$subPath, "<this>");
        return FilesKt.toComponents($this$subPath).subPath(beginIndex, endIndex);
    }
}

