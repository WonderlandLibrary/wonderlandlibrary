/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin._Assertions;
import kotlin.collections.AbstractIterator;
import kotlin.io.AccessDeniedException;
import kotlin.io.FileWalkDirection;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010(\n\u0002\b\u0006\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0003\u001a\u001b\u001cB\u0019\b\u0010\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006B\u0089\u0001\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\u0014\u0010\u0007\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t\u0018\u00010\b\u0012\u0014\u0010\n\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u000b\u0018\u00010\b\u00128\u0010\f\u001a4\u0012\u0013\u0012\u00110\u0002\u00a2\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0010\u0012\u0013\u0012\u00110\u0011\u00a2\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0012\u0012\u0004\u0012\u00020\u000b\u0018\u00010\r\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0014\u00a2\u0006\u0002\u0010\u0015J\u000f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00020\u0017H\u0096\u0002J\u000e\u0010\u0013\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u0014J\u001a\u0010\u0007\u001a\u00020\u00002\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t0\bJ \u0010\f\u001a\u00020\u00002\u0018\u0010\u0019\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u000b0\rJ\u001a\u0010\n\u001a\u00020\u00002\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u000b0\bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0007\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t\u0018\u00010\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R@\u0010\f\u001a4\u0012\u0013\u0012\u00110\u0002\u00a2\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0010\u0012\u0013\u0012\u00110\u0011\u00a2\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0012\u0012\u0004\u0012\u00020\u000b\u0018\u00010\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\n\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u000b\u0018\u00010\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0002X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2={"Lkotlin/io/FileTreeWalk;", "Lkotlin/sequences/Sequence;", "Ljava/io/File;", "start", "direction", "Lkotlin/io/FileWalkDirection;", "(Ljava/io/File;Lkotlin/io/FileWalkDirection;)V", "onEnter", "Lkotlin/Function1;", "", "onLeave", "", "onFail", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "f", "Ljava/io/IOException;", "e", "maxDepth", "", "(Ljava/io/File;Lkotlin/io/FileWalkDirection;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;I)V", "iterator", "", "depth", "function", "DirectoryState", "FileTreeWalkIterator", "WalkState", "kotlin-stdlib"})
public final class FileTreeWalk
implements Sequence<File> {
    @NotNull
    private final File start;
    @NotNull
    private final FileWalkDirection direction;
    @Nullable
    private final Function1<File, Boolean> onEnter;
    @Nullable
    private final Function1<File, Unit> onLeave;
    @Nullable
    private final Function2<File, IOException, Unit> onFail;
    private final int maxDepth;

    private FileTreeWalk(File start, FileWalkDirection direction, Function1<? super File, Boolean> onEnter, Function1<? super File, Unit> onLeave, Function2<? super File, ? super IOException, Unit> onFail, int maxDepth) {
        this.start = start;
        this.direction = direction;
        this.onEnter = onEnter;
        this.onLeave = onLeave;
        this.onFail = onFail;
        this.maxDepth = maxDepth;
    }

    /* synthetic */ FileTreeWalk(File file, FileWalkDirection fileWalkDirection, Function1 function1, Function1 function12, Function2 function2, int n, int n2, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n2 & 2) != 0) {
            fileWalkDirection = FileWalkDirection.TOP_DOWN;
        }
        if ((n2 & 0x20) != 0) {
            n = Integer.MAX_VALUE;
        }
        this(file, fileWalkDirection, function1, function12, function2, n);
    }

    public FileTreeWalk(@NotNull File start, @NotNull FileWalkDirection direction) {
        Intrinsics.checkNotNullParameter(start, "start");
        Intrinsics.checkNotNullParameter((Object)direction, "direction");
        this(start, direction, null, null, null, 0, 32, null);
    }

    public /* synthetic */ FileTreeWalk(File file, FileWalkDirection fileWalkDirection, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 2) != 0) {
            fileWalkDirection = FileWalkDirection.TOP_DOWN;
        }
        this(file, fileWalkDirection);
    }

    @Override
    @NotNull
    public Iterator<File> iterator() {
        return new FileTreeWalkIterator();
    }

    @NotNull
    public final FileTreeWalk onEnter(@NotNull Function1<? super File, Boolean> function) {
        Intrinsics.checkNotNullParameter(function, "function");
        return new FileTreeWalk(this.start, this.direction, function, this.onLeave, this.onFail, this.maxDepth);
    }

    @NotNull
    public final FileTreeWalk onLeave(@NotNull Function1<? super File, Unit> function) {
        Intrinsics.checkNotNullParameter(function, "function");
        return new FileTreeWalk(this.start, this.direction, this.onEnter, function, this.onFail, this.maxDepth);
    }

    @NotNull
    public final FileTreeWalk onFail(@NotNull Function2<? super File, ? super IOException, Unit> function) {
        Intrinsics.checkNotNullParameter(function, "function");
        return new FileTreeWalk(this.start, this.direction, this.onEnter, this.onLeave, function, this.maxDepth);
    }

    @NotNull
    public final FileTreeWalk maxDepth(int depth) {
        if (depth <= 0) {
            throw new IllegalArgumentException("depth must be positive, but was " + depth + '.');
        }
        return new FileTreeWalk(this.start, this.direction, this.onEnter, this.onLeave, this.onFail, depth);
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\"\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\n\u0010\u0007\u001a\u0004\u0018\u00010\u0003H&R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\b"}, d2={"Lkotlin/io/FileTreeWalk$WalkState;", "", "root", "Ljava/io/File;", "(Ljava/io/File;)V", "getRoot", "()Ljava/io/File;", "step", "kotlin-stdlib"})
    private static abstract class WalkState {
        @NotNull
        private final File root;

        public WalkState(@NotNull File root) {
            Intrinsics.checkNotNullParameter(root, "root");
            this.root = root;
        }

        @NotNull
        public final File getRoot() {
            return this.root;
        }

        @Nullable
        public abstract File step();
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\"\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004\u00a8\u0006\u0005"}, d2={"Lkotlin/io/FileTreeWalk$DirectoryState;", "Lkotlin/io/FileTreeWalk$WalkState;", "rootDir", "Ljava/io/File;", "(Ljava/io/File;)V", "kotlin-stdlib"})
    private static abstract class DirectoryState
    extends WalkState {
        public DirectoryState(@NotNull File rootDir) {
            Intrinsics.checkNotNullParameter(rootDir, "rootDir");
            super(rootDir);
            if (_Assertions.ENABLED) {
                boolean bl = rootDir.isDirectory();
                if (_Assertions.ENABLED && !bl) {
                    boolean bl2 = false;
                    String string = "rootDir must be verified to be directory beforehand.";
                    throw new AssertionError((Object)string);
                }
            }
        }
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0082\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0003\r\u000e\u000fB\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0007\u001a\u00020\bH\u0014J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0002H\u0002J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0002H\u0082\u0010R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2={"Lkotlin/io/FileTreeWalk$FileTreeWalkIterator;", "Lkotlin/collections/AbstractIterator;", "Ljava/io/File;", "(Lkotlin/io/FileTreeWalk;)V", "state", "Ljava/util/ArrayDeque;", "Lkotlin/io/FileTreeWalk$WalkState;", "computeNext", "", "directoryState", "Lkotlin/io/FileTreeWalk$DirectoryState;", "root", "gotoNext", "BottomUpDirectoryState", "SingleFileState", "TopDownDirectoryState", "kotlin-stdlib"})
    private final class FileTreeWalkIterator
    extends AbstractIterator<File> {
        @NotNull
        private final ArrayDeque<WalkState> state;

        public FileTreeWalkIterator() {
            Intrinsics.checkNotNullParameter(FileTreeWalk.this, "this$0");
            this.state = new ArrayDeque();
            if (FileTreeWalk.this.start.isDirectory()) {
                this.state.push(this.directoryState(FileTreeWalk.this.start));
            } else if (FileTreeWalk.this.start.isFile()) {
                this.state.push(new SingleFileState(FileTreeWalk.this.start));
            } else {
                this.done();
            }
        }

        @Override
        protected void computeNext() {
            File nextFile = this.gotoNext();
            if (nextFile != null) {
                this.setNext(nextFile);
            } else {
                this.done();
            }
        }

        private final DirectoryState directoryState(File root) {
            DirectoryState directoryState;
            FileWalkDirection fileWalkDirection = FileTreeWalk.this.direction;
            int n = WhenMappings.$EnumSwitchMapping$0[fileWalkDirection.ordinal()];
            switch (n) {
                case 1: {
                    directoryState = new TopDownDirectoryState(root);
                    break;
                }
                case 2: {
                    directoryState = new BottomUpDirectoryState(root);
                    break;
                }
                default: {
                    throw new NoWhenBranchMatchedException();
                }
            }
            return directoryState;
        }

        private final File gotoNext() {
            FileTreeWalkIterator fileTreeWalkIterator = this;
            while (true) {
                FileTreeWalkIterator fileTreeWalkIterator2 = fileTreeWalkIterator;
                WalkState walkState = fileTreeWalkIterator2.state.peek();
                if (walkState == null) {
                    return null;
                }
                WalkState topState = walkState;
                File file = topState.step();
                if (file == null) {
                    fileTreeWalkIterator2.state.pop();
                    fileTreeWalkIterator = fileTreeWalkIterator2;
                    continue;
                }
                if (Intrinsics.areEqual(file, topState.getRoot()) || !file.isDirectory() || fileTreeWalkIterator2.state.size() >= FileTreeWalk.this.maxDepth) {
                    return file;
                }
                fileTreeWalkIterator2.state.push(fileTreeWalkIterator2.directoryState(file));
                fileTreeWalkIterator = fileTreeWalkIterator2;
            }
        }

        @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0004\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\n\u0010\r\u001a\u0004\u0018\u00010\u0003H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0018\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u000bR\u000e\u0010\f\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2={"Lkotlin/io/FileTreeWalk$FileTreeWalkIterator$BottomUpDirectoryState;", "Lkotlin/io/FileTreeWalk$DirectoryState;", "rootDir", "Ljava/io/File;", "(Lkotlin/io/FileTreeWalk$FileTreeWalkIterator;Ljava/io/File;)V", "failed", "", "fileIndex", "", "fileList", "", "[Ljava/io/File;", "rootVisited", "step", "kotlin-stdlib"})
        private final class BottomUpDirectoryState
        extends DirectoryState {
            private boolean rootVisited;
            @Nullable
            private File[] fileList;
            private int fileIndex;
            private boolean failed;

            public BottomUpDirectoryState(File rootDir) {
                Intrinsics.checkNotNullParameter(FileTreeWalkIterator.this, "this$0");
                Intrinsics.checkNotNullParameter(rootDir, "rootDir");
                super(rootDir);
            }

            @Override
            @Nullable
            public File step() {
                Object object;
                if (!this.failed && this.fileList == null) {
                    object = FileTreeWalk.this.onEnter;
                    if (object == null ? false : (Boolean)object.invoke(this.getRoot()) == false) {
                        return null;
                    }
                    this.fileList = this.getRoot().listFiles();
                    if (this.fileList == null) {
                        object = FileTreeWalk.this.onFail;
                        if (object != null) {
                            object.invoke(this.getRoot(), new AccessDeniedException(this.getRoot(), null, "Cannot list files in a directory", 2, null));
                        }
                        this.failed = true;
                    }
                }
                if (this.fileList != null) {
                    Intrinsics.checkNotNull(this.fileList);
                    if (this.fileIndex < this.fileList.length) {
                        Intrinsics.checkNotNull(this.fileList);
                        object = this;
                        int n = ((BottomUpDirectoryState)object).fileIndex;
                        ((BottomUpDirectoryState)object).fileIndex = n + 1;
                        return this.fileList[n];
                    }
                }
                if (!this.rootVisited) {
                    this.rootVisited = true;
                    return this.getRoot();
                }
                object = FileTreeWalk.this.onLeave;
                if (object != null) {
                    object.invoke(this.getRoot());
                }
                return null;
            }
        }

        @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\n\u0010\f\u001a\u0004\u0018\u00010\u0003H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0018\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2={"Lkotlin/io/FileTreeWalk$FileTreeWalkIterator$TopDownDirectoryState;", "Lkotlin/io/FileTreeWalk$DirectoryState;", "rootDir", "Ljava/io/File;", "(Lkotlin/io/FileTreeWalk$FileTreeWalkIterator;Ljava/io/File;)V", "fileIndex", "", "fileList", "", "[Ljava/io/File;", "rootVisited", "", "step", "kotlin-stdlib"})
        private final class TopDownDirectoryState
        extends DirectoryState {
            private boolean rootVisited;
            @Nullable
            private File[] fileList;
            private int fileIndex;

            public TopDownDirectoryState(File rootDir) {
                Intrinsics.checkNotNullParameter(FileTreeWalkIterator.this, "this$0");
                Intrinsics.checkNotNullParameter(rootDir, "rootDir");
                super(rootDir);
            }

            @Override
            @Nullable
            public File step() {
                block9: {
                    Object object;
                    block10: {
                        block11: {
                            block8: {
                                if (!this.rootVisited) {
                                    Function1 function1 = FileTreeWalk.this.onEnter;
                                    if (function1 == null ? false : (Boolean)function1.invoke(this.getRoot()) == false) {
                                        return null;
                                    }
                                    this.rootVisited = true;
                                    return this.getRoot();
                                }
                                if (this.fileList == null) break block8;
                                Intrinsics.checkNotNull(this.fileList);
                                if (this.fileIndex >= this.fileList.length) break block9;
                            }
                            if (this.fileList != null) break block10;
                            this.fileList = this.getRoot().listFiles();
                            if (this.fileList == null && (object = FileTreeWalk.this.onFail) != null) {
                                object.invoke(this.getRoot(), new AccessDeniedException(this.getRoot(), null, "Cannot list files in a directory", 2, null));
                            }
                            if (this.fileList == null) break block11;
                            Intrinsics.checkNotNull(this.fileList);
                            if (this.fileList.length != 0) break block10;
                        }
                        if ((object = FileTreeWalk.this.onLeave) != null) {
                            object.invoke(this.getRoot());
                        }
                        return null;
                    }
                    Intrinsics.checkNotNull(this.fileList);
                    object = this;
                    int n = ((TopDownDirectoryState)object).fileIndex;
                    ((TopDownDirectoryState)object).fileIndex = n + 1;
                    return this.fileList[n];
                }
                Function1 function1 = FileTreeWalk.this.onLeave;
                if (function1 != null) {
                    function1.invoke(this.getRoot());
                }
                return null;
            }
        }

        @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\n\u0010\u0007\u001a\u0004\u0018\u00010\u0003H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2={"Lkotlin/io/FileTreeWalk$FileTreeWalkIterator$SingleFileState;", "Lkotlin/io/FileTreeWalk$WalkState;", "rootFile", "Ljava/io/File;", "(Lkotlin/io/FileTreeWalk$FileTreeWalkIterator;Ljava/io/File;)V", "visited", "", "step", "kotlin-stdlib"})
        private final class SingleFileState
        extends WalkState {
            private boolean visited;

            public SingleFileState(File rootFile) {
                Intrinsics.checkNotNullParameter(FileTreeWalkIterator.this, "this$0");
                Intrinsics.checkNotNullParameter(rootFile, "rootFile");
                super(rootFile);
                if (_Assertions.ENABLED) {
                    boolean bl = rootFile.isFile();
                    if (_Assertions.ENABLED && !bl) {
                        boolean bl2 = false;
                        String string = "rootFile must be verified to be file beforehand.";
                        throw new AssertionError((Object)string);
                    }
                }
            }

            @Override
            @Nullable
            public File step() {
                if (this.visited) {
                    return null;
                }
                this.visited = true;
                return this.getRoot();
            }
        }

        @Metadata(mv={1, 6, 0}, k=3, xi=48)
        public final class WhenMappings {
            public static final /* synthetic */ int[] $EnumSwitchMapping$0;

            static {
                int[] nArray = new int[FileWalkDirection.values().length];
                nArray[FileWalkDirection.TOP_DOWN.ordinal()] = 1;
                nArray[FileWalkDirection.BOTTOM_UP.ordinal()] = 2;
                $EnumSwitchMapping$0 = nArray;
            }
        }
    }
}

