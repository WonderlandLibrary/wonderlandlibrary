// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.title;

import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface TitlePart<T>
{
    public static final TitlePart<Component> TITLE = new TitlePart<Component>() {
        @Override
        public String toString() {
            return "TitlePart.TITLE";
        }
    };
    public static final TitlePart<Component> SUBTITLE = new TitlePart<Component>() {
        @Override
        public String toString() {
            return "TitlePart.SUBTITLE";
        }
    };
    public static final TitlePart<Title.Times> TIMES = new TitlePart<Title.Times>() {
        @Override
        public String toString() {
            return "TitlePart.TIMES";
        }
    };
}
