/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.zip.InflaterInputStream;
import jdk.nashorn.internal.ir.FunctionNode;

final class AstDeserializer {
    AstDeserializer() {
    }

    static FunctionNode deserialize(byte[] serializedAst) {
        try {
            return (FunctionNode)new ObjectInputStream(new InflaterInputStream(new ByteArrayInputStream(serializedAst))).readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            throw new AssertionError("Unexpected exception deserializing function", e);
        }
    }
}

