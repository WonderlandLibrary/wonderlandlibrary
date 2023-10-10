// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins.convert;

import org.apache.logging.log4j.util.EnglishEnums;

public class EnumConverter<E extends Enum<E>> implements TypeConverter<E>
{
    private final Class<E> clazz;
    
    public EnumConverter(final Class<E> clazz) {
        this.clazz = clazz;
    }
    
    @Override
    public E convert(final String s) {
        return EnglishEnums.valueOf(this.clazz, s);
    }
}
