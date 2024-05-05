package net.arikia.dev.drpc.callbacks;

import com.sun.jna.*;

public interface DisconnectedCallback extends Callback
{
    void apply(final int p0, final String p1);
}
