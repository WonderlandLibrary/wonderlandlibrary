// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.examination;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

public abstract class ExaminableProperty
{
    private ExaminableProperty() {
    }
    
    @NotNull
    public abstract String name();
    
    @NotNull
    public abstract <R> R examine(@NotNull final Examiner<? extends R> examiner);
    
    @Override
    public String toString() {
        return "ExaminableProperty{" + this.name() + "}";
    }
    
    @NotNull
    public static ExaminableProperty of(@NotNull final String name, @Nullable final Object value) {
        return new ExaminableProperty() {
            @NotNull
            @Override
            public String name() {
                return name;
            }
            
            @NotNull
            @Override
            public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    @NotNull
    public static ExaminableProperty of(@NotNull final String name, @Nullable final String value) {
        return new ExaminableProperty() {
            @NotNull
            @Override
            public String name() {
                return name;
            }
            
            @NotNull
            @Override
            public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    @NotNull
    public static ExaminableProperty of(@NotNull final String name, final boolean value) {
        return new ExaminableProperty() {
            @NotNull
            @Override
            public String name() {
                return name;
            }
            
            @NotNull
            @Override
            public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    @NotNull
    public static ExaminableProperty of(@NotNull final String name, final boolean[] value) {
        return new ExaminableProperty() {
            @NotNull
            @Override
            public String name() {
                return name;
            }
            
            @NotNull
            @Override
            public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    @NotNull
    public static ExaminableProperty of(@NotNull final String name, final byte value) {
        return new ExaminableProperty() {
            @NotNull
            @Override
            public String name() {
                return name;
            }
            
            @NotNull
            @Override
            public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    @NotNull
    public static ExaminableProperty of(@NotNull final String name, final byte[] value) {
        return new ExaminableProperty() {
            @NotNull
            @Override
            public String name() {
                return name;
            }
            
            @NotNull
            @Override
            public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    @NotNull
    public static ExaminableProperty of(@NotNull final String name, final char value) {
        return new ExaminableProperty() {
            @NotNull
            @Override
            public String name() {
                return name;
            }
            
            @NotNull
            @Override
            public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    @NotNull
    public static ExaminableProperty of(@NotNull final String name, final char[] value) {
        return new ExaminableProperty() {
            @NotNull
            @Override
            public String name() {
                return name;
            }
            
            @NotNull
            @Override
            public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    @NotNull
    public static ExaminableProperty of(@NotNull final String name, final double value) {
        return new ExaminableProperty() {
            @NotNull
            @Override
            public String name() {
                return name;
            }
            
            @NotNull
            @Override
            public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    @NotNull
    public static ExaminableProperty of(@NotNull final String name, final double[] value) {
        return new ExaminableProperty() {
            @NotNull
            @Override
            public String name() {
                return name;
            }
            
            @NotNull
            @Override
            public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    @NotNull
    public static ExaminableProperty of(@NotNull final String name, final float value) {
        return new ExaminableProperty() {
            @NotNull
            @Override
            public String name() {
                return name;
            }
            
            @NotNull
            @Override
            public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    @NotNull
    public static ExaminableProperty of(@NotNull final String name, final float[] value) {
        return new ExaminableProperty() {
            @NotNull
            @Override
            public String name() {
                return name;
            }
            
            @NotNull
            @Override
            public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    @NotNull
    public static ExaminableProperty of(@NotNull final String name, final int value) {
        return new ExaminableProperty() {
            @NotNull
            @Override
            public String name() {
                return name;
            }
            
            @NotNull
            @Override
            public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    @NotNull
    public static ExaminableProperty of(@NotNull final String name, final int[] value) {
        return new ExaminableProperty() {
            @NotNull
            @Override
            public String name() {
                return name;
            }
            
            @NotNull
            @Override
            public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    @NotNull
    public static ExaminableProperty of(@NotNull final String name, final long value) {
        return new ExaminableProperty() {
            @NotNull
            @Override
            public String name() {
                return name;
            }
            
            @NotNull
            @Override
            public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    @NotNull
    public static ExaminableProperty of(@NotNull final String name, final long[] value) {
        return new ExaminableProperty() {
            @NotNull
            @Override
            public String name() {
                return name;
            }
            
            @NotNull
            @Override
            public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    @NotNull
    public static ExaminableProperty of(@NotNull final String name, final short value) {
        return new ExaminableProperty() {
            @NotNull
            @Override
            public String name() {
                return name;
            }
            
            @NotNull
            @Override
            public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    @NotNull
    public static ExaminableProperty of(@NotNull final String name, final short[] value) {
        return new ExaminableProperty() {
            @NotNull
            @Override
            public String name() {
                return name;
            }
            
            @NotNull
            @Override
            public <R> R examine(@NotNull final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
}
