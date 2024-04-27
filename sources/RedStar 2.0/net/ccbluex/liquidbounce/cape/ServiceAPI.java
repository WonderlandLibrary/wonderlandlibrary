package net.ccbluex.liquidbounce.cape;

import java.util.Arrays;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import net.ccbluex.liquidbounce.cape.CapeService;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\b\n\n\u0000\u000020B\r0¢J020HR0X¢\n\u0000¨\b"}, d2={"Lnet/ccbluex/liquidbounce/cape/ServiceAPI;", "Lnet/ccbluex/liquidbounce/cape/CapeService;", "baseURL", "", "(Ljava/lang/String;)V", "getCape", "uuid", "Ljava/util/UUID;", "Pride"})
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
