// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.audience;

import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.stream.Collector;

final class Audiences
{
    static final Collector<? super Audience, ?, ForwardingAudience> COLLECTOR;
    
    private Audiences() {
    }
    
    static {
        COLLECTOR = Collectors.collectingAndThen((Collector<? super Audience, ?, ArrayList>)Collectors.toCollection((Supplier<R>)ArrayList::new), audiences -> Audience.audience((Iterable<? extends Audience>)Collections.unmodifiableCollection((Collection<?>)audiences)));
    }
}
