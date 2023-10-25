/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.command;

import java.util.UUID;

public interface IBaritoneChatControl {
    public static final String FORCE_COMMAND_PREFIX = String.format("<<%s>>", UUID.randomUUID().toString());
}

