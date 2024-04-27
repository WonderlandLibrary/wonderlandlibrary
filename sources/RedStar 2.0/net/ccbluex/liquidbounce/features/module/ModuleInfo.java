package net.ccbluex.liquidbounce.features.module;

import java.lang.annotation.RetentionPolicy;
import kotlin.Metadata;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;
import net.ccbluex.liquidbounce.api.MinecraftVersion;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;

@Retention(value=AnnotationRetention.RUNTIME)
@java.lang.annotation.Retention(value=RetentionPolicy.RUNTIME)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00002\n\n\n\u0000\n\n\b\n\n\u0000\n\b\n\u0000\n\n\b\n\n\n\b\b\u000020BF000\b\b0\b\b\b\t0\n\b\b0\n\b\f\b00\rR0\n¢\bR\t0\n¢\b\tR0¢\bR0¢\bR0\b¢\bR0¢\bR\f\b00\r¢\b\f¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/ModuleInfo;", "", "name", "", "description", "category", "Lnet/ccbluex/liquidbounce/features/module/ModuleCategory;", "keyBind", "", "canEnable", "", "array", "supportedVersions", "", "Lnet/ccbluex/liquidbounce/api/MinecraftVersion;", "()Z", "()Lnet/ccbluex/liquidbounce/features/module/ModuleCategory;", "()Ljava/lang/String;", "()I", "()[Lnet/ccbluex/liquidbounce/api/MinecraftVersion;", "Pride"})
public @interface ModuleInfo {
    public String name();

    public String description();

    public ModuleCategory category();

    public int keyBind() default 0;

    public boolean canEnable() default true;

    public boolean array() default true;

    public MinecraftVersion[] supportedVersions() default {MinecraftVersion.MC_1_8, MinecraftVersion.MC_1_12};
}
