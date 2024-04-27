/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.render;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.value.ListValue;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Info(name="Cape", description="LiquidBouncePlus capes.", category=Category.RENDER, cnName="\u62ab\u98ce\u89c6\u89d2")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001:\u0001\u000eB\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bR\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000f"}, d2={"Lnet/dev/important/modules/module/modules/render/Cape;", "Lnet/dev/important/modules/module/Module;", "()V", "styleValue", "Lnet/dev/important/value/ListValue;", "getStyleValue", "()Lnet/dev/important/value/ListValue;", "tag", "", "getTag", "()Ljava/lang/String;", "getCapeLocation", "Lnet/minecraft/util/ResourceLocation;", "value", "CapeStyle", "LiquidBounce"})
public final class Cape
extends Module {
    @NotNull
    private final ListValue styleValue;

    public Cape() {
        String[] stringArray = new String[]{"Dark", "Light", "Special1", "Special2"};
        this.styleValue = new ListValue("Style", stringArray, "Dark");
    }

    @NotNull
    public final ListValue getStyleValue() {
        return this.styleValue;
    }

    @NotNull
    public final ResourceLocation getCapeLocation(@NotNull String value) {
        ResourceLocation resourceLocation;
        Intrinsics.checkNotNullParameter(value, "value");
        try {
            String string = value.toUpperCase();
            Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toUpperCase()");
            resourceLocation = CapeStyle.valueOf(string).getLocation();
        }
        catch (IllegalArgumentException e) {
            resourceLocation = CapeStyle.DARK.getLocation();
        }
        return resourceLocation;
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.styleValue.get();
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\n\u00a8\u0006\u000b"}, d2={"Lnet/dev/important/modules/module/modules/render/Cape$CapeStyle;", "", "location", "Lnet/minecraft/util/ResourceLocation;", "(Ljava/lang/String;ILnet/minecraft/util/ResourceLocation;)V", "getLocation", "()Lnet/minecraft/util/ResourceLocation;", "DARK", "LIGHT", "SPECIAL1", "SPECIAL2", "LiquidBounce"})
    public static final class CapeStyle
    extends Enum<CapeStyle> {
        @NotNull
        private final ResourceLocation location;
        public static final /* enum */ CapeStyle DARK = new CapeStyle(new ResourceLocation("liquidplus/cape/dark.png"));
        public static final /* enum */ CapeStyle LIGHT = new CapeStyle(new ResourceLocation("liquidplus/cape/light.png"));
        public static final /* enum */ CapeStyle SPECIAL1 = new CapeStyle(new ResourceLocation("liquidplus/cape/special1.png"));
        public static final /* enum */ CapeStyle SPECIAL2 = new CapeStyle(new ResourceLocation("liquidplus/cape/special2.png"));
        private static final /* synthetic */ CapeStyle[] $VALUES;

        private CapeStyle(ResourceLocation location) {
            this.location = location;
        }

        @NotNull
        public final ResourceLocation getLocation() {
            return this.location;
        }

        public static CapeStyle[] values() {
            return (CapeStyle[])$VALUES.clone();
        }

        public static CapeStyle valueOf(String value) {
            return Enum.valueOf(CapeStyle.class, value);
        }

        static {
            $VALUES = capeStyleArray = new CapeStyle[]{CapeStyle.DARK, CapeStyle.LIGHT, CapeStyle.SPECIAL1, CapeStyle.SPECIAL2};
        }
    }
}

