/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.authlib.legacy;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.legacy.LegacyMinecraftSessionService;
import com.mojang.authlib.legacy.LegacyUserAuthentication;
import java.net.Proxy;
import org.apache.commons.lang3.Validate;

public class LegacyAuthenticationService
extends HttpAuthenticationService {
    protected LegacyAuthenticationService(Proxy proxy) {
        super(proxy);
    }

    @Override
    public LegacyUserAuthentication createUserAuthentication(Agent agent) {
        Validate.notNull(agent);
        if (agent != Agent.MINECRAFT) {
            throw new IllegalArgumentException("Legacy authentication cannot handle anything but Minecraft");
        }
        return new LegacyUserAuthentication(this);
    }

    @Override
    public LegacyMinecraftSessionService createMinecraftSessionService() {
        return new LegacyMinecraftSessionService(this);
    }

    @Override
    public GameProfileRepository createProfileRepository() {
        throw new UnsupportedOperationException("Legacy authentication service has no profile repository");
    }
}

