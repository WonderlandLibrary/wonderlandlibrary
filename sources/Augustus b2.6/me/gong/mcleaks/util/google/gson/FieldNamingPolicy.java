// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.gson;

import java.util.Locale;
import java.lang.reflect.Field;

public enum FieldNamingPolicy implements FieldNamingStrategy
{
    IDENTITY {
        @Override
        public String translateName(final Field f) {
            return f.getName();
        }
    }, 
    UPPER_CAMEL_CASE {
        @Override
        public String translateName(final Field f) {
            return FieldNamingPolicy.upperCaseFirstLetter(f.getName());
        }
    }, 
    UPPER_CAMEL_CASE_WITH_SPACES {
        @Override
        public String translateName(final Field f) {
            return FieldNamingPolicy.upperCaseFirstLetter(FieldNamingPolicy.separateCamelCase(f.getName(), " "));
        }
    }, 
    LOWER_CASE_WITH_UNDERSCORES {
        @Override
        public String translateName(final Field f) {
            return FieldNamingPolicy.separateCamelCase(f.getName(), "_").toLowerCase(Locale.ENGLISH);
        }
    }, 
    LOWER_CASE_WITH_DASHES {
        @Override
        public String translateName(final Field f) {
            return FieldNamingPolicy.separateCamelCase(f.getName(), "-").toLowerCase(Locale.ENGLISH);
        }
    };
    
    static String separateCamelCase(final String name, final String separator) {
        final StringBuilder translation = new StringBuilder();
        for (int i = 0; i < name.length(); ++i) {
            final char character = name.charAt(i);
            if (Character.isUpperCase(character) && translation.length() != 0) {
                translation.append(separator);
            }
            translation.append(character);
        }
        return translation.toString();
    }
    
    static String upperCaseFirstLetter(final String name) {
        final StringBuilder fieldNameBuilder = new StringBuilder();
        int index;
        char firstCharacter;
        for (index = 0, firstCharacter = name.charAt(index); index < name.length() - 1 && !Character.isLetter(firstCharacter); firstCharacter = name.charAt(++index)) {
            fieldNameBuilder.append(firstCharacter);
        }
        if (index == name.length()) {
            return fieldNameBuilder.toString();
        }
        if (!Character.isUpperCase(firstCharacter)) {
            final String modifiedTarget = modifyString(Character.toUpperCase(firstCharacter), name, ++index);
            return fieldNameBuilder.append(modifiedTarget).toString();
        }
        return name;
    }
    
    private static String modifyString(final char firstCharacter, final String srcString, final int indexOfSubstring) {
        return (indexOfSubstring < srcString.length()) ? (firstCharacter + srcString.substring(indexOfSubstring)) : String.valueOf(firstCharacter);
    }
}
