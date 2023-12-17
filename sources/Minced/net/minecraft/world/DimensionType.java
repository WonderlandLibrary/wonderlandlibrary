// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum DimensionType
{
    OVERWORLD(0, "overworld", "", (Class<? extends WorldProvider>)WorldProviderSurface.class), 
    NETHER(-1, "the_nether", "_nether", (Class<? extends WorldProvider>)WorldProviderHell.class), 
    THE_END(1, "the_end", "_end", (Class<? extends WorldProvider>)WorldProviderEnd.class);
    
    private final int id;
    private final String name;
    private final String suffix;
    private final Class<? extends WorldProvider> clazz;
    
    private DimensionType(final int idIn, final String nameIn, final String suffixIn, final Class<? extends WorldProvider> clazzIn) {
        this.id = idIn;
        this.name = nameIn;
        this.suffix = suffixIn;
        this.clazz = clazzIn;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getSuffix() {
        return this.suffix;
    }
    
    public WorldProvider createDimension() {
        try {
            final Constructor<? extends WorldProvider> constructor = this.clazz.getConstructor((Class<?>[])new Class[0]);
            return (WorldProvider)constructor.newInstance(new Object[0]);
        }
        catch (NoSuchMethodException nosuchmethodexception) {
            throw new Error("Could not create new dimension", nosuchmethodexception);
        }
        catch (InvocationTargetException invocationtargetexception) {
            throw new Error("Could not create new dimension", invocationtargetexception);
        }
        catch (InstantiationException instantiationexception) {
            throw new Error("Could not create new dimension", instantiationexception);
        }
        catch (IllegalAccessException illegalaccessexception) {
            throw new Error("Could not create new dimension", illegalaccessexception);
        }
    }
    
    public static DimensionType getById(final int id) {
        for (final DimensionType dimensiontype : values()) {
            if (dimensiontype.getId() == id) {
                return dimensiontype;
            }
        }
        throw new IllegalArgumentException("Invalid dimension id " + id);
    }
    
    public static DimensionType byName(final String nameIn) {
        for (final DimensionType dimensiontype : values()) {
            if (dimensiontype.getName().equals(nameIn)) {
                return dimensiontype;
            }
        }
        throw new IllegalArgumentException("Invalid dimension " + nameIn);
    }
}
