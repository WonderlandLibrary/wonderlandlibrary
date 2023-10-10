// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.gson;

import java.util.Locale;
import java.lang.reflect.Field;

public enum FieldNamingPolicy implements FieldNamingStrategy
{
    IDENTITY(0) {
        @Override
        public String translateName(final Field f) {
            return f.getName();
        }
    }, 
    UPPER_CAMEL_CASE(1) {
        @Override
        public String translateName(final Field f) {
            return FieldNamingPolicy.upperCaseFirstLetter(f.getName());
        }
    }, 
    UPPER_CAMEL_CASE_WITH_SPACES(2) {
        @Override
        public String translateName(final Field f) {
            return FieldNamingPolicy.upperCaseFirstLetter(FieldNamingPolicy.separateCamelCase(f.getName(), " "));
        }
    }, 
    LOWER_CASE_WITH_UNDERSCORES(3) {
        @Override
        public String translateName(final Field f) {
            return FieldNamingPolicy.separateCamelCase(f.getName(), "_").toLowerCase(Locale.ENGLISH);
        }
    }, 
    LOWER_CASE_WITH_DASHES(4) {
        @Override
        public String translateName(final Field f) {
            return FieldNamingPolicy.separateCamelCase(f.getName(), "-").toLowerCase(Locale.ENGLISH);
        }
    }, 
    LOWER_CASE_WITH_DOTS(5) {
        @Override
        public String translateName(final Field f) {
            return FieldNamingPolicy.separateCamelCase(f.getName(), ".").toLowerCase(Locale.ENGLISH);
        }
    };
    
    static String separateCamelCase(final String name, final String separator) {
        final StringBuilder translation = new StringBuilder();
        for (int i = 0, length = name.length(); i < length; ++i) {
            final char character = name.charAt(i);
            if (Character.isUpperCase(character) && translation.length() != 0) {
                translation.append(separator);
            }
            translation.append(character);
        }
        return translation.toString();
    }
    
    static String upperCaseFirstLetter(final String name) {
        int firstLetterIndex = 0;
        for (int limit = name.length() - 1; !Character.isLetter(name.charAt(firstLetterIndex)) && firstLetterIndex < limit; ++firstLetterIndex) {}
        final char firstLetter = name.charAt(firstLetterIndex);
        if (Character.isUpperCase(firstLetter)) {
            return name;
        }
        final char uppercased = Character.toUpperCase(firstLetter);
        if (firstLetterIndex == 0) {
            return uppercased + name.substring(1);
        }
        return name.substring(0, firstLetterIndex) + uppercased + name.substring(firstLetterIndex + 1);
    }
}
