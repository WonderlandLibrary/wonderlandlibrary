/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin;

import kotlin.KotlinVersionCurrentValue;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0007\u0018\u0000 \u00172\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\u0017B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0007J\u0011\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0000H\u0096\u0002J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u000e\u001a\u0004\u0018\u00010\u0011H\u0096\u0002J\b\u0010\u0012\u001a\u00020\u0003H\u0016J\u0016\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003J\u001e\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0003J\b\u0010\u0014\u001a\u00020\u0015H\u0016J \u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0003H\u0002R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\tR\u000e\u0010\f\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2={"Lkotlin/KotlinVersion;", "", "major", "", "minor", "(II)V", "patch", "(III)V", "getMajor", "()I", "getMinor", "getPatch", "version", "compareTo", "other", "equals", "", "", "hashCode", "isAtLeast", "toString", "", "versionOf", "Companion", "kotlin-stdlib"})
@SinceKotlin(version="1.1")
public final class KotlinVersion
implements Comparable<KotlinVersion> {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private final int major;
    private final int minor;
    private final int patch;
    private final int version;
    public static final int MAX_COMPONENT_VALUE = 255;
    @JvmField
    @NotNull
    public static final KotlinVersion CURRENT = KotlinVersionCurrentValue.get();

    public KotlinVersion(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.version = this.versionOf(this.major, this.minor, this.patch);
    }

    public final int getMajor() {
        return this.major;
    }

    public final int getMinor() {
        return this.minor;
    }

    public final int getPatch() {
        return this.patch;
    }

    public KotlinVersion(int major, int minor) {
        this(major, minor, 0);
    }

    /*
     * Unable to fully structure code
     */
    private final int versionOf(int major, int minor, int patch) {
        v0 = 0 <= major ? major < 256 : false;
        if (!v0) ** GOTO lbl-1000
        v1 = 0 <= minor ? minor < 256 : false;
        if (!v1) ** GOTO lbl-1000
        v2 = 0 <= patch ? patch < 256 : false;
        if (v2) {
            v3 = true;
        } else lbl-1000:
        // 3 sources

        {
            v3 = var4_4 = false;
        }
        if (!var4_4) {
            $i$a$-require-KotlinVersion$versionOf$1 = false;
            var5_6 = "Version components are out of range: " + major + '.' + minor + '.' + patch;
            throw new IllegalArgumentException(var5_6.toString());
        }
        return (major << 16) + (minor << 8) + patch;
    }

    @NotNull
    public String toString() {
        return "" + this.major + '.' + this.minor + '.' + this.patch;
    }

    public boolean equals(@Nullable Object other) {
        KotlinVersion kotlinVersion;
        if (this == other) {
            return true;
        }
        KotlinVersion kotlinVersion2 = kotlinVersion = other instanceof KotlinVersion ? (KotlinVersion)other : null;
        if (kotlinVersion == null) {
            return false;
        }
        KotlinVersion otherVersion = kotlinVersion;
        return this.version == otherVersion.version;
    }

    public int hashCode() {
        return this.version;
    }

    @Override
    public int compareTo(@NotNull KotlinVersion other) {
        Intrinsics.checkNotNullParameter(other, "other");
        return this.version - other.version;
    }

    public final boolean isAtLeast(int major, int minor) {
        return this.major > major || this.major == major && this.minor >= minor;
    }

    public final boolean isAtLeast(int major, int minor, int patch) {
        return this.major > major || this.major == major && (this.minor > minor || this.minor == minor && this.patch >= patch);
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2={"Lkotlin/KotlinVersion$Companion;", "", "()V", "CURRENT", "Lkotlin/KotlinVersion;", "MAX_COMPONENT_VALUE", "", "kotlin-stdlib"})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

