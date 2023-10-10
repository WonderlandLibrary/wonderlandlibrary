// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.protocol.remapper;

import java.util.Iterator;
import com.viaversion.viaversion.exception.InformativeException;
import com.viaversion.viaversion.exception.CancelException;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.function.Function;
import com.viaversion.viaversion.api.type.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class PacketRemapper
{
    private final List<PacketHandler> valueRemappers;
    
    protected PacketRemapper() {
        this.valueRemappers = new ArrayList<PacketHandler>();
        this.registerMap();
    }
    
    public void map(final Type type) {
        this.handler(wrapper -> wrapper.write(type, (T)wrapper.read((Type<T>)type)));
    }
    
    public void map(final Type oldType, final Type newType) {
        this.handler(wrapper -> wrapper.write(newType, (Object)wrapper.read((Type<T>)oldType)));
    }
    
    public <T1, T2> void map(final Type<T1> oldType, final Type<T2> newType, final Function<T1, T2> transformer) {
        this.map(oldType, (ValueTransformer<T1, Object>)new ValueTransformer<T1, T2>(newType) {
            @Override
            public T2 transform(final PacketWrapper wrapper, final T1 inputValue) throws Exception {
                return transformer.apply(inputValue);
            }
        });
    }
    
    public <T1, T2> void map(final ValueTransformer<T1, T2> transformer) {
        if (transformer.getInputType() == null) {
            throw new IllegalArgumentException("Use map(Type<T1>, ValueTransformer<T1, T2>) for value transformers without specified input type!");
        }
        this.map(transformer.getInputType(), transformer);
    }
    
    public <T1, T2> void map(final Type<T1> oldType, final ValueTransformer<T1, T2> transformer) {
        this.map(new TypeRemapper<T1>(oldType), transformer);
    }
    
    public <T> void map(final ValueReader<T> inputReader, final ValueWriter<T> outputWriter) {
        this.handler(wrapper -> outputWriter.write(wrapper, inputReader.read(wrapper)));
    }
    
    public void handler(final PacketHandler handler) {
        this.valueRemappers.add(handler);
    }
    
    public <T> void create(final Type<T> type, final T value) {
        this.handler(wrapper -> wrapper.write(type, value));
    }
    
    public void read(final Type type) {
        this.handler(wrapper -> wrapper.read((Type<Object>)type));
    }
    
    public abstract void registerMap();
    
    public void remap(final PacketWrapper packetWrapper) throws Exception {
        try {
            for (final PacketHandler handler : this.valueRemappers) {
                handler.handle(packetWrapper);
            }
        }
        catch (CancelException e) {
            throw e;
        }
        catch (InformativeException e2) {
            e2.addSource(this.getClass());
            throw e2;
        }
        catch (Exception e3) {
            final InformativeException ex = new InformativeException(e3);
            ex.addSource(this.getClass());
            throw ex;
        }
    }
}
