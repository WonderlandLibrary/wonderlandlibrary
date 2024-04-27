/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.cape;

import java.util.Map;
import java.util.UUID;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.cape.CapeService;

public final class ServiceList
implements CapeService {
    private final Map<String, String> users;

    @Override
    public String getCape(UUID uuid) {
        return this.users.get(StringsKt.replace$default((String)uuid.toString(), (String)"-", (String)"", (boolean)false, (int)4, null));
    }

    public ServiceList(Map<String, String> users) {
        this.users = users;
    }
}

