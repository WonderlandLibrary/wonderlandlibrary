// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.protocol.remapper;

import com.viaversion.viaversion.exception.InformativeException;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;

public abstract class ValueTransformer<T1, T2> implements ValueWriter<T1>
{
    private final Type<T1> inputType;
    private final Type<T2> outputType;
    
    protected ValueTransformer(final Type<T1> inputType, final Type<T2> outputType) {
        this.inputType = inputType;
        this.outputType = outputType;
    }
    
    protected ValueTransformer(final Type<T2> outputType) {
        this(null, outputType);
    }
    
    public abstract T2 transform(final PacketWrapper p0, final T1 p1) throws Exception;
    
    @Override
    public void write(final PacketWrapper writer, final T1 inputValue) throws Exception {
        try {
            writer.write(this.outputType, this.transform(writer, inputValue));
        }
        catch (InformativeException e) {
            e.addSource(this.getClass());
            throw e;
        }
    }
    
    public Type<T1> getInputType() {
        return this.inputType;
    }
    
    public Type<T2> getOutputType() {
        return this.outputType;
    }
}
