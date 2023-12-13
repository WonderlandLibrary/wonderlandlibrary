// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.emitter;

import java.io.IOException;

interface EmitterState
{
    void expect() throws IOException;
}
