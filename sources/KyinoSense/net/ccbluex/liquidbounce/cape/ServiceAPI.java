/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.cape;

import java.util.Arrays;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import net.ccbluex.liquidbounce.cape.CapeService;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0007H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2={"Lnet/ccbluex/liquidbounce/cape/ServiceAPI;", "Lnet/ccbluex/liquidbounce/cape/CapeService;", "baseURL", "", "(Ljava/lang/String;)V", "getCape", "uuid", "Ljava/util/UUID;", "KyinoClient"})
public final class ServiceAPI
implements CapeService {
    private final String baseURL;

    @Override
    @NotNull
    public String getCape(@NotNull UUID uuid) {
        Intrinsics.checkParameterIsNotNull(uuid, "uuid");
        StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
        String string = this.baseURL;
        Object[] objectArray = new Object[]{uuid};
        boolean bl = false;
        String string2 = String.format(string, Arrays.copyOf(objectArray, objectArray.length));
        Intrinsics.checkExpressionValueIsNotNull(string2, "java.lang.String.format(format, *args)");
        return string2;
    }

    public ServiceAPI(@NotNull String baseURL) {
        Intrinsics.checkParameterIsNotNull(baseURL, "baseURL");
        this.baseURL = baseURL;
    }
}

