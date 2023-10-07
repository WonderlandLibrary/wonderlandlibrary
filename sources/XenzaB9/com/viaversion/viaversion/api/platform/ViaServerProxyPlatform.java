// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.platform;

import com.google.common.annotations.Beta;

@Beta
public interface ViaServerProxyPlatform<T> extends ViaPlatform<T>
{
    ProtocolDetectorService protocolDetectorService();
}
