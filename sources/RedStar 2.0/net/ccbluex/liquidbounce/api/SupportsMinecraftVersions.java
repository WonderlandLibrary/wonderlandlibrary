package net.ccbluex.liquidbounce.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.MinecraftVersion;

@Retention(value=RetentionPolicy.RUNTIME)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\n\b\b\u000020B\f\b00R\b00¢\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/SupportsMinecraftVersions;", "", "value", "", "Lnet/ccbluex/liquidbounce/api/MinecraftVersion;", "()[Lnet/ccbluex/liquidbounce/api/MinecraftVersion;", "Pride"})
public @interface SupportsMinecraftVersions {
    public MinecraftVersion[] value();
}
