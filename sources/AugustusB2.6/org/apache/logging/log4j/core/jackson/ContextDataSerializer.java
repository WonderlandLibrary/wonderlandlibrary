// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.util.Map;
import com.fasterxml.jackson.core.JsonGenerator;
import org.apache.logging.log4j.util.TriConsumer;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ContextDataSerializer extends StdSerializer<ReadOnlyStringMap>
{
    private static final long serialVersionUID = 1L;
    private static final TriConsumer<String, Object, JsonGenerator> WRITE_STRING_FIELD_INTO;
    
    protected ContextDataSerializer() {
        super((Class)Map.class, false);
    }
    
    public void serialize(final ReadOnlyStringMap contextData, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeStartObject();
        contextData.forEach(ContextDataSerializer.WRITE_STRING_FIELD_INTO, jgen);
        jgen.writeEndObject();
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     5: putstatic       org/apache/logging/log4j/core/jackson/ContextDataSerializer.WRITE_STRING_FIELD_INTO:Lorg/apache/logging/log4j/util/TriConsumer;
        //     8: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Could not infer any expression.
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:374)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}
