/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.StringCompanionObject
 */
package net.ccbluex.liquidbounce.cape;

import java.util.Arrays;
import java.util.UUID;
import kotlin.jvm.internal.StringCompanionObject;
import net.ccbluex.liquidbounce.cape.CapeService;

public final class ServiceAPI
implements CapeService {
    private final String baseURL;

    @Override
    public String getCape(UUID uuid) {
        StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
        String string = this.baseURL;
        Object[] objectArray = new Object[]{uuid};
        boolean bl = false;
        return String.format(string, Arrays.copyOf(objectArray, objectArray.length));
    }

    public ServiceAPI(String baseURL) {
        this.baseURL = baseURL;
    }
}

