package net.arikia.dev.drpc.callbacks;

import com.sun.jna.*;

public interface ErroredCallback extends Callback
{
    void apply(final int p0, final String p1);
}
