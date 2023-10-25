/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.group;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public class ChannelGroupException
extends ChannelException
implements Iterable<Map.Entry<Channel, Throwable>> {
    private final Collection<Map.Entry<Channel, Throwable>> failed;
    private static final long serialVersionUID = -4093064295562629453L;

    public ChannelGroupException(Collection<Map.Entry<Channel, Throwable>> collection) {
        if (collection == null) {
            throw new NullPointerException("causes");
        }
        if (collection.isEmpty()) {
            throw new IllegalArgumentException("causes must be non empty");
        }
        this.failed = Collections.unmodifiableCollection(collection);
    }

    @Override
    public Iterator<Map.Entry<Channel, Throwable>> iterator() {
        return this.failed.iterator();
    }
}

