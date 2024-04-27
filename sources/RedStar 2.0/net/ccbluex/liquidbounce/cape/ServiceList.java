package net.ccbluex.liquidbounce.cape;

import java.util.Map;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.cape.CapeService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n$\n\n\b\n\n\u0000\u000020B000¢J020\bHR000X¢\n\u0000¨\t"}, d2={"Lnet/ccbluex/liquidbounce/cape/ServiceList;", "Lnet/ccbluex/liquidbounce/cape/CapeService;", "users", "", "", "(Ljava/util/Map;)V", "getCape", "uuid", "Ljava/util/UUID;", "Pride"})
public final class ServiceList
implements CapeService {
    private final Map<String, String> users;

    @Override
    @Nullable
    public String getCape(@NotNull UUID uuid) {
        Intrinsics.checkParameterIsNotNull(uuid, "uuid");
        String string = uuid.toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "uuid.toString()");
        return this.users.get(StringsKt.replace$default(string, "-", "", false, 4, null));
    }

    public ServiceList(@NotNull Map<String, String> users) {
        Intrinsics.checkParameterIsNotNull(users, "users");
        this.users = users;
    }
}
