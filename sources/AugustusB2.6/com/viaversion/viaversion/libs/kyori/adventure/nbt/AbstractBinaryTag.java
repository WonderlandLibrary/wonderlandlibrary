// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import com.viaversion.viaversion.libs.kyori.examination.Examiner;
import com.viaversion.viaversion.libs.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;

abstract class AbstractBinaryTag implements BinaryTag
{
    @NotNull
    @Override
    public final String examinableName() {
        return this.type().toString();
    }
    
    @Override
    public final String toString() {
        return this.examine((Examiner<String>)StringExaminer.simpleEscaping());
    }
}
