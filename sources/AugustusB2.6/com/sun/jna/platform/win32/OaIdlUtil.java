// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import java.lang.reflect.Array;

public abstract class OaIdlUtil
{
    public static Object toPrimitiveArray(final OaIdl.SAFEARRAY sa, final boolean destruct) {
        final Pointer dataPointer = sa.accessData();
        try {
            final int dimensions = sa.getDimensionCount();
            final int[] elements = new int[dimensions];
            final int[] cumElements = new int[dimensions];
            final int varType = sa.getVarType().intValue();
            for (int i = 0; i < dimensions; ++i) {
                elements[i] = sa.getUBound(i) - sa.getLBound(i) + 1;
            }
            for (int i = dimensions - 1; i >= 0; --i) {
                if (i == dimensions - 1) {
                    cumElements[i] = 1;
                }
                else {
                    cumElements[i] = cumElements[i + 1] * elements[i + 1];
                }
            }
            if (dimensions == 0) {
                throw new IllegalArgumentException("Supplied Array has no dimensions.");
            }
            final int elementCount = cumElements[0] * elements[0];
            Object sourceArray = null;
            switch (varType) {
                case 16:
                case 17: {
                    sourceArray = dataPointer.getByteArray(0L, elementCount);
                    break;
                }
                case 2:
                case 11:
                case 18: {
                    sourceArray = dataPointer.getShortArray(0L, elementCount);
                    break;
                }
                case 3:
                case 10:
                case 19:
                case 22:
                case 23: {
                    sourceArray = dataPointer.getIntArray(0L, elementCount);
                    break;
                }
                case 4: {
                    sourceArray = dataPointer.getFloatArray(0L, elementCount);
                    break;
                }
                case 5:
                case 7: {
                    sourceArray = dataPointer.getDoubleArray(0L, elementCount);
                    break;
                }
                case 8: {
                    sourceArray = dataPointer.getPointerArray(0L, elementCount);
                    break;
                }
                case 12: {
                    final Variant.VARIANT variant = new Variant.VARIANT(dataPointer);
                    sourceArray = variant.toArray(elementCount);
                    break;
                }
                default: {
                    throw new IllegalStateException("Type not supported: " + varType);
                }
            }
            final Object targetArray = Array.newInstance(Object.class, elements);
            toPrimitiveArray(sourceArray, targetArray, elements, cumElements, varType, new int[0]);
            return targetArray;
        }
        finally {
            sa.unaccessData();
            if (destruct) {
                sa.destroy();
            }
        }
    }
    
    private static void toPrimitiveArray(final Object dataArray, final Object targetArray, final int[] elements, final int[] cumElements, final int varType, final int[] currentIdx) {
        final int dimIdx = currentIdx.length;
        final int[] subIdx = new int[currentIdx.length + 1];
        System.arraycopy(currentIdx, 0, subIdx, 0, dimIdx);
        for (int i = 0; i < elements[dimIdx]; ++i) {
            subIdx[dimIdx] = i;
            if (dimIdx == elements.length - 1) {
                int offset = 0;
                for (int j = 0; j < dimIdx; ++j) {
                    offset += cumElements[j] * currentIdx[j];
                }
                offset += subIdx[dimIdx];
                final int targetPos = subIdx[dimIdx];
                Label_0804: {
                    switch (varType) {
                        case 11: {
                            Array.set(targetArray, targetPos, Array.getShort(dataArray, offset) != 0);
                            break;
                        }
                        case 16:
                        case 17: {
                            Array.set(targetArray, targetPos, Array.getByte(dataArray, offset));
                            break;
                        }
                        case 2:
                        case 18: {
                            Array.set(targetArray, targetPos, Array.getShort(dataArray, offset));
                            break;
                        }
                        case 3:
                        case 19:
                        case 22:
                        case 23: {
                            Array.set(targetArray, targetPos, Array.getInt(dataArray, offset));
                            break;
                        }
                        case 10: {
                            Array.set(targetArray, targetPos, new WinDef.SCODE((long)Array.getInt(dataArray, offset)));
                            break;
                        }
                        case 4: {
                            Array.set(targetArray, targetPos, Array.getFloat(dataArray, offset));
                            break;
                        }
                        case 5: {
                            Array.set(targetArray, targetPos, Array.getDouble(dataArray, offset));
                            break;
                        }
                        case 7: {
                            Array.set(targetArray, targetPos, new OaIdl.DATE(Array.getDouble(dataArray, offset)).getAsJavaDate());
                            break;
                        }
                        case 8: {
                            Array.set(targetArray, targetPos, new WTypes.BSTR((Pointer)Array.get(dataArray, offset)).getValue());
                            break;
                        }
                        case 12: {
                            final Variant.VARIANT holder = (Variant.VARIANT)Array.get(dataArray, offset);
                            switch (holder.getVarType().intValue()) {
                                case 0:
                                case 1: {
                                    Array.set(targetArray, targetPos, null);
                                    break Label_0804;
                                }
                                case 11: {
                                    Array.set(targetArray, targetPos, holder.booleanValue());
                                    break Label_0804;
                                }
                                case 16:
                                case 17: {
                                    Array.set(targetArray, targetPos, holder.byteValue());
                                    break Label_0804;
                                }
                                case 2:
                                case 18: {
                                    Array.set(targetArray, targetPos, holder.shortValue());
                                    break Label_0804;
                                }
                                case 3:
                                case 19:
                                case 22:
                                case 23: {
                                    Array.set(targetArray, targetPos, holder.intValue());
                                    break Label_0804;
                                }
                                case 10: {
                                    Array.set(targetArray, targetPos, new WinDef.SCODE((long)holder.intValue()));
                                    break Label_0804;
                                }
                                case 4: {
                                    Array.set(targetArray, targetPos, holder.floatValue());
                                    break Label_0804;
                                }
                                case 5: {
                                    Array.set(targetArray, targetPos, holder.doubleValue());
                                    break Label_0804;
                                }
                                case 7: {
                                    Array.set(targetArray, targetPos, holder.dateValue());
                                    break Label_0804;
                                }
                                case 8: {
                                    Array.set(targetArray, targetPos, holder.stringValue());
                                    break Label_0804;
                                }
                                default: {
                                    throw new IllegalStateException("Type not supported: " + holder.getVarType().intValue());
                                }
                            }
                            break;
                        }
                        default: {
                            throw new IllegalStateException("Type not supported: " + varType);
                        }
                    }
                }
            }
            else {
                toPrimitiveArray(dataArray, Array.get(targetArray, i), elements, cumElements, varType, subIdx);
            }
        }
    }
}
