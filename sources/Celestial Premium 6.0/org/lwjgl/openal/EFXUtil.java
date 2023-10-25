/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.openal;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.EFX10;
import org.lwjgl.openal.OpenALException;

public final class EFXUtil {
    private static final int EFFECT = 1111;
    private static final int FILTER = 2222;

    private EFXUtil() {
    }

    public static boolean isEfxSupported() {
        if (!AL.isCreated()) {
            throw new OpenALException("OpenAL has not been created.");
        }
        return ALC10.alcIsExtensionPresent(AL.getDevice(), "ALC_EXT_EFX");
    }

    public static boolean isEffectSupported(int effectType) {
        switch (effectType) {
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 32768: {
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown or invalid effect type: " + effectType);
            }
        }
        return EFXUtil.testSupportGeneric(1111, effectType);
    }

    public static boolean isFilterSupported(int filterType) {
        switch (filterType) {
            case 0: 
            case 1: 
            case 2: 
            case 3: {
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown or invalid filter type: " + filterType);
            }
        }
        return EFXUtil.testSupportGeneric(2222, filterType);
    }

    private static boolean testSupportGeneric(int objectType, int typeValue) {
        switch (objectType) {
            case 1111: 
            case 2222: {
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid objectType: " + objectType);
            }
        }
        boolean supported = false;
        if (EFXUtil.isEfxSupported()) {
            int genError;
            AL10.alGetError();
            int testObject = 0;
            try {
                switch (objectType) {
                    case 1111: {
                        testObject = EFX10.alGenEffects();
                        break;
                    }
                    case 2222: {
                        testObject = EFX10.alGenFilters();
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException("Invalid objectType: " + objectType);
                    }
                }
                genError = AL10.alGetError();
            }
            catch (OpenALException debugBuildException) {
                genError = debugBuildException.getMessage().contains("AL_OUT_OF_MEMORY") ? 40965 : 40964;
            }
            if (genError == 0) {
                int setError;
                AL10.alGetError();
                try {
                    switch (objectType) {
                        case 1111: {
                            EFX10.alEffecti(testObject, 32769, typeValue);
                            break;
                        }
                        case 2222: {
                            EFX10.alFilteri(testObject, 32769, typeValue);
                            break;
                        }
                        default: {
                            throw new IllegalArgumentException("Invalid objectType: " + objectType);
                        }
                    }
                    setError = AL10.alGetError();
                }
                catch (OpenALException debugBuildException) {
                    setError = 40963;
                }
                if (setError == 0) {
                    supported = true;
                }
                try {
                    switch (objectType) {
                        case 1111: {
                            EFX10.alDeleteEffects(testObject);
                            break;
                        }
                        case 2222: {
                            EFX10.alDeleteFilters(testObject);
                            break;
                        }
                        default: {
                            throw new IllegalArgumentException("Invalid objectType: " + objectType);
                        }
                    }
                }
                catch (OpenALException debugBuildException) {}
            } else if (genError == 40965) {
                throw new OpenALException(genError);
            }
        }
        return supported;
    }
}

