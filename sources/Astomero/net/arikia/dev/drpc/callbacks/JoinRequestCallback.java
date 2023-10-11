package net.arikia.dev.drpc.callbacks;

import com.sun.jna.*;
import net.arikia.dev.drpc.*;

public interface JoinRequestCallback extends Callback
{
    void apply(final DiscordUser p0);
}
