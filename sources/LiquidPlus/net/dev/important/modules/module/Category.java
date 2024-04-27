/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\r\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000f\u00a8\u0006\u0010"}, d2={"Lnet/dev/important/modules/module/Category;", "", "displayName", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getDisplayName", "()Ljava/lang/String;", "COMBAT", "PLAYER", "MOVEMENT", "RENDER", "WORLD", "MISC", "EXPLOIT", "COLOR", "CLIENT", "LiquidBounce"})
public final class Category
extends Enum<Category> {
    @NotNull
    private final String displayName;
    public static final /* enum */ Category COMBAT = new Category("Combat");
    public static final /* enum */ Category PLAYER = new Category("Player");
    public static final /* enum */ Category MOVEMENT = new Category("Movement");
    public static final /* enum */ Category RENDER = new Category("Render");
    public static final /* enum */ Category WORLD = new Category("World");
    public static final /* enum */ Category MISC = new Category("Misc");
    public static final /* enum */ Category EXPLOIT = new Category("Exploit");
    public static final /* enum */ Category COLOR = new Category("Color");
    public static final /* enum */ Category CLIENT = new Category("Client");
    private static final /* synthetic */ Category[] $VALUES;

    private Category(String displayName) {
        this.displayName = displayName;
    }

    @NotNull
    public final String getDisplayName() {
        return this.displayName;
    }

    public static Category[] values() {
        return (Category[])$VALUES.clone();
    }

    public static Category valueOf(String value) {
        return Enum.valueOf(Category.class, value);
    }

    static {
        $VALUES = categoryArray = new Category[]{Category.COMBAT, Category.PLAYER, Category.MOVEMENT, Category.RENDER, Category.WORLD, Category.MISC, Category.EXPLOIT, Category.COLOR, Category.CLIENT};
    }
}

