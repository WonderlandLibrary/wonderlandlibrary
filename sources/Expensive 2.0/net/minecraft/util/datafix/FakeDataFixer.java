package net.minecraft.util.datafix;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.TaggedChoice;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.serialization.Dynamic;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.SharedConstants;

public class FakeDataFixer
implements DataFixer {
    private static FakeSchema FAKE_SCHEMA = new FakeSchema();

    public <T> Dynamic<T> update(DSL.TypeReference type, Dynamic<T> input, int version, int newVersion) {
        return input;
    }

    public Schema getSchema(int key) {
        return FAKE_SCHEMA;
    }

    private static class FakeSchema
    extends Schema {
        public FakeSchema() {
            super(SharedConstants.getVersion().getWorldVersion(), null);
        }

        public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
            return ImmutableMap.of();
        }

        public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
            return ImmutableMap.of();
        }

        public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
        }

        protected Map<String, Type<?>> buildTypes() {
            return ImmutableMap.of();
        }

        public Type<?> getType(DSL.TypeReference type) {
            return null;
        }

        public Type<?> getChoiceType(DSL.TypeReference type, String choiceName) {
            return null;
        }

        public Type<?> getTypeRaw(DSL.TypeReference type) {
            return null;
        }

        public TaggedChoice.TaggedChoiceType<?> findChoiceType(DSL.TypeReference type) {
            return null;
        }
    }
}