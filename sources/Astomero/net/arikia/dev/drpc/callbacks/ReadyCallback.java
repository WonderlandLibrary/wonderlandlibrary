package net.arikia.dev.drpc.callbacks;

import com.sun.jna.*;
import net.arikia.dev.drpc.*;

public interface ReadyCallback extends Callback
{
    void apply(final DiscordUser p0);
}
