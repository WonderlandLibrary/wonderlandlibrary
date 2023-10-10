/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 */
package com.viaversion.viaversion.libs.kyori.adventure.text;

import com.viaversion.viaversion.libs.kyori.adventure.text.BlockNBTComponentImpl;
import com.viaversion.viaversion.libs.kyori.adventure.text.NBTComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.NBTComponentBuilder;
import com.viaversion.viaversion.libs.kyori.adventure.text.ScopedComponent;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;
import java.util.regex.Matcher;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface BlockNBTComponent
extends NBTComponent<BlockNBTComponent, Builder>,
ScopedComponent<BlockNBTComponent> {
    @NotNull
    public Pos pos();

    @Contract(pure=true)
    @NotNull
    public BlockNBTComponent pos(@NotNull Pos var1);

    @Contract(pure=true)
    @NotNull
    default public BlockNBTComponent localPos(double left, double up, double forwards) {
        return this.pos(LocalPos.of(left, up, forwards));
    }

    @Contract(pure=true)
    @NotNull
    default public BlockNBTComponent worldPos(@NotNull WorldPos.Coordinate x2, @NotNull WorldPos.Coordinate y2, @NotNull WorldPos.Coordinate z2) {
        return this.pos(WorldPos.of(x2, y2, z2));
    }

    @Contract(pure=true)
    @NotNull
    default public BlockNBTComponent absoluteWorldPos(int x2, int y2, int z2) {
        return this.worldPos(WorldPos.Coordinate.absolute(x2), WorldPos.Coordinate.absolute(y2), WorldPos.Coordinate.absolute(z2));
    }

    @Contract(pure=true)
    @NotNull
    default public BlockNBTComponent relativeWorldPos(int x2, int y2, int z2) {
        return this.worldPos(WorldPos.Coordinate.relative(x2), WorldPos.Coordinate.relative(y2), WorldPos.Coordinate.relative(z2));
    }

    public static interface WorldPos
    extends Pos {
        @NotNull
        public static WorldPos of(@NotNull Coordinate x2, @NotNull Coordinate y2, @NotNull Coordinate z2) {
            return new BlockNBTComponentImpl.WorldPosImpl(x2, y2, z2);
        }

        @NotNull
        public Coordinate x();

        @NotNull
        public Coordinate y();

        @NotNull
        public Coordinate z();

        public static interface Coordinate
        extends Examinable {
            @NotNull
            public static Coordinate absolute(int value) {
                return Coordinate.of(value, Type.ABSOLUTE);
            }

            @NotNull
            public static Coordinate relative(int value) {
                return Coordinate.of(value, Type.RELATIVE);
            }

            @NotNull
            public static Coordinate of(int value, @NotNull Type type) {
                return new BlockNBTComponentImpl.WorldPosImpl.CoordinateImpl(value, type);
            }

            public int value();

            @NotNull
            public Type type();

            public static enum Type {
                ABSOLUTE,
                RELATIVE;

            }
        }
    }

    public static interface LocalPos
    extends Pos {
        @NotNull
        public static LocalPos of(double left, double up, double forwards) {
            return new BlockNBTComponentImpl.LocalPosImpl(left, up, forwards);
        }

        public double left();

        public double up();

        public double forwards();
    }

    public static interface Pos
    extends Examinable {
        @NotNull
        public static Pos fromString(@NotNull String input) throws IllegalArgumentException {
            Matcher localMatch = BlockNBTComponentImpl.Tokens.LOCAL_PATTERN.matcher(input);
            if (localMatch.matches()) {
                return LocalPos.of(Double.parseDouble(localMatch.group(1)), Double.parseDouble(localMatch.group(3)), Double.parseDouble(localMatch.group(5)));
            }
            Matcher worldMatch = BlockNBTComponentImpl.Tokens.WORLD_PATTERN.matcher(input);
            if (worldMatch.matches()) {
                return WorldPos.of(BlockNBTComponentImpl.Tokens.deserializeCoordinate(worldMatch.group(1), worldMatch.group(2)), BlockNBTComponentImpl.Tokens.deserializeCoordinate(worldMatch.group(3), worldMatch.group(4)), BlockNBTComponentImpl.Tokens.deserializeCoordinate(worldMatch.group(5), worldMatch.group(6)));
            }
            throw new IllegalArgumentException("Cannot convert position specification '" + input + "' into a position");
        }

        @NotNull
        public String asString();
    }

    public static interface Builder
    extends NBTComponentBuilder<BlockNBTComponent, Builder> {
        @Contract(value="_ -> this")
        @NotNull
        public Builder pos(@NotNull Pos var1);

        @Contract(value="_, _, _ -> this")
        @NotNull
        default public Builder localPos(double left, double up, double forwards) {
            return this.pos(LocalPos.of(left, up, forwards));
        }

        @Contract(value="_, _, _ -> this")
        @NotNull
        default public Builder worldPos(@NotNull WorldPos.Coordinate x2, @NotNull WorldPos.Coordinate y2, @NotNull WorldPos.Coordinate z2) {
            return this.pos(WorldPos.of(x2, y2, z2));
        }

        @Contract(value="_, _, _ -> this")
        @NotNull
        default public Builder absoluteWorldPos(int x2, int y2, int z2) {
            return this.worldPos(WorldPos.Coordinate.absolute(x2), WorldPos.Coordinate.absolute(y2), WorldPos.Coordinate.absolute(z2));
        }

        @Contract(value="_, _, _ -> this")
        @NotNull
        default public Builder relativeWorldPos(int x2, int y2, int z2) {
            return this.worldPos(WorldPos.Coordinate.relative(x2), WorldPos.Coordinate.relative(y2), WorldPos.Coordinate.relative(z2));
        }
    }
}

