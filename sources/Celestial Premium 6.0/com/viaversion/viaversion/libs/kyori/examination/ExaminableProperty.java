/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.NonNull
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.viaversion.viaversion.libs.kyori.examination;

import com.viaversion.viaversion.libs.kyori.examination.Examiner;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class ExaminableProperty {
    private ExaminableProperty() {
    }

    public abstract @NonNull String name();

    public abstract <R> @NonNull R examine(@NonNull Examiner<? extends R> var1);

    public String toString() {
        return "ExaminableProperty{" + this.name() + "}";
    }

    public static @NonNull ExaminableProperty of(final @NonNull String name, final @Nullable Object value) {
        return new ExaminableProperty(){

            @Override
            public @NonNull String name() {
                return name;
            }

            @Override
            public <R> @NonNull R examine(@NonNull Examiner<? extends R> examiner) {
                return examiner.examine(value);
            }
        };
    }

    public static @NonNull ExaminableProperty of(final @NonNull String name, final @Nullable String value) {
        return new ExaminableProperty(){

            @Override
            public @NonNull String name() {
                return name;
            }

            @Override
            public <R> @NonNull R examine(@NonNull Examiner<? extends R> examiner) {
                return examiner.examine(value);
            }
        };
    }

    public static @NonNull ExaminableProperty of(final @NonNull String name, final boolean value) {
        return new ExaminableProperty(){

            @Override
            public @NonNull String name() {
                return name;
            }

            @Override
            public <R> @NonNull R examine(@NonNull Examiner<? extends R> examiner) {
                return examiner.examine(value);
            }
        };
    }

    public static @NonNull ExaminableProperty of(final @NonNull String name, final boolean[] value) {
        return new ExaminableProperty(){

            @Override
            public @NonNull String name() {
                return name;
            }

            @Override
            public <R> @NonNull R examine(@NonNull Examiner<? extends R> examiner) {
                return examiner.examine(value);
            }
        };
    }

    public static @NonNull ExaminableProperty of(final @NonNull String name, final byte value) {
        return new ExaminableProperty(){

            @Override
            public @NonNull String name() {
                return name;
            }

            @Override
            public <R> @NonNull R examine(@NonNull Examiner<? extends R> examiner) {
                return examiner.examine(value);
            }
        };
    }

    public static @NonNull ExaminableProperty of(final @NonNull String name, final byte[] value) {
        return new ExaminableProperty(){

            @Override
            public @NonNull String name() {
                return name;
            }

            @Override
            public <R> @NonNull R examine(@NonNull Examiner<? extends R> examiner) {
                return examiner.examine(value);
            }
        };
    }

    public static @NonNull ExaminableProperty of(final @NonNull String name, final char value) {
        return new ExaminableProperty(){

            @Override
            public @NonNull String name() {
                return name;
            }

            @Override
            public <R> @NonNull R examine(@NonNull Examiner<? extends R> examiner) {
                return examiner.examine(value);
            }
        };
    }

    public static @NonNull ExaminableProperty of(final @NonNull String name, final char[] value) {
        return new ExaminableProperty(){

            @Override
            public @NonNull String name() {
                return name;
            }

            @Override
            public <R> @NonNull R examine(@NonNull Examiner<? extends R> examiner) {
                return examiner.examine(value);
            }
        };
    }

    public static @NonNull ExaminableProperty of(final @NonNull String name, final double value) {
        return new ExaminableProperty(){

            @Override
            public @NonNull String name() {
                return name;
            }

            @Override
            public <R> @NonNull R examine(@NonNull Examiner<? extends R> examiner) {
                return examiner.examine(value);
            }
        };
    }

    public static @NonNull ExaminableProperty of(final @NonNull String name, final double[] value) {
        return new ExaminableProperty(){

            @Override
            public @NonNull String name() {
                return name;
            }

            @Override
            public <R> @NonNull R examine(@NonNull Examiner<? extends R> examiner) {
                return examiner.examine(value);
            }
        };
    }

    public static @NonNull ExaminableProperty of(final @NonNull String name, final float value) {
        return new ExaminableProperty(){

            @Override
            public @NonNull String name() {
                return name;
            }

            @Override
            public <R> @NonNull R examine(@NonNull Examiner<? extends R> examiner) {
                return examiner.examine(value);
            }
        };
    }

    public static @NonNull ExaminableProperty of(final @NonNull String name, final float[] value) {
        return new ExaminableProperty(){

            @Override
            public @NonNull String name() {
                return name;
            }

            @Override
            public <R> @NonNull R examine(@NonNull Examiner<? extends R> examiner) {
                return examiner.examine(value);
            }
        };
    }

    public static @NonNull ExaminableProperty of(final @NonNull String name, final int value) {
        return new ExaminableProperty(){

            @Override
            public @NonNull String name() {
                return name;
            }

            @Override
            public <R> @NonNull R examine(@NonNull Examiner<? extends R> examiner) {
                return examiner.examine(value);
            }
        };
    }

    public static @NonNull ExaminableProperty of(final @NonNull String name, final int[] value) {
        return new ExaminableProperty(){

            @Override
            public @NonNull String name() {
                return name;
            }

            @Override
            public <R> @NonNull R examine(@NonNull Examiner<? extends R> examiner) {
                return examiner.examine(value);
            }
        };
    }

    public static @NonNull ExaminableProperty of(final @NonNull String name, final long value) {
        return new ExaminableProperty(){

            @Override
            public @NonNull String name() {
                return name;
            }

            @Override
            public <R> @NonNull R examine(@NonNull Examiner<? extends R> examiner) {
                return examiner.examine(value);
            }
        };
    }

    public static @NonNull ExaminableProperty of(final @NonNull String name, final long[] value) {
        return new ExaminableProperty(){

            @Override
            public @NonNull String name() {
                return name;
            }

            @Override
            public <R> @NonNull R examine(@NonNull Examiner<? extends R> examiner) {
                return examiner.examine(value);
            }
        };
    }

    public static @NonNull ExaminableProperty of(final @NonNull String name, final short value) {
        return new ExaminableProperty(){

            @Override
            public @NonNull String name() {
                return name;
            }

            @Override
            public <R> @NonNull R examine(@NonNull Examiner<? extends R> examiner) {
                return examiner.examine(value);
            }
        };
    }

    public static @NonNull ExaminableProperty of(final @NonNull String name, final short[] value) {
        return new ExaminableProperty(){

            @Override
            public @NonNull String name() {
                return name;
            }

            @Override
            public <R> @NonNull R examine(@NonNull Examiner<? extends R> examiner) {
                return examiner.examine(value);
            }
        };
    }
}

