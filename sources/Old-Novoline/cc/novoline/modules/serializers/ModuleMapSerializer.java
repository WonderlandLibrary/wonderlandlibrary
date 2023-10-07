package cc.novoline.modules.serializers;

import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.ModuleArrayMap;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.holder.ModuleHolder;
import cc.novoline.modules.configurations.holder.StoringModuleHolder;
import cc.novoline.utils.java.Checks;
import com.google.common.reflect.TypeToken;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static ninja.leaping.configurate.ValueType.NULL;

/**
 * @author xDelsy
 */
public final class ModuleMapSerializer implements TypeSerializer<ModuleArrayMap> {

    /* fields */
    @NonNull
    private final Logger logger = LogManager.getLogger();
    @NonNull
    private final ModuleManager moduleManager;

    /* constructors */
    public ModuleMapSerializer(@NonNull ModuleManager moduleManager) {
        Checks.notNull(moduleManager, "module manager");
        this.moduleManager = moduleManager;
    }

    /* methods */
    @Override
    @SuppressWarnings("unchecked")
    public void serialize(@NonNull TypeToken<?> type, @Nullable ModuleArrayMap obj,
                          @NonNull ConfigurationNode node) throws ObjectMappingException {
        if (obj == null) {
            node.setValue(null);
            return;
        }

        for (final Object2ObjectMap.Entry<String, ModuleHolder<?>> entry : obj.object2ObjectEntrySet()) {
            final String moduleName = entry.getKey();
            final ModuleHolder<?> moduleHolder = entry.getValue();

            // todo Выделенное значение в селект боксе не сохраняется
            if (moduleHolder != null) {
                final TypeToken<AbstractModule> typeToken = (TypeToken<AbstractModule>) entry.getValue().getTypeToken();
                final AbstractModule module = entry.getValue().getModule();

                node.getNode(moduleName).setValue(typeToken, module);
            }
        }
    }

    @Override
    @NonNull
    public ModuleArrayMap deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode node) {
        final ModuleArrayMap orig = this.moduleManager.getModuleManager();
        final ModuleArrayMap dest = new ModuleArrayMap();

        ModuleHolder<?> creatingHolder;

        for (final Map.Entry<Object, ? extends ConfigurationNode> entry : node.getChildrenMap().entrySet()) {
            final String moduleName = entry.getKey().toString();
            final ConfigurationNode moduleNode = entry.getValue();

            if (moduleNode.getValueType() != NULL && (creatingHolder = orig.get(moduleName)) != null) {
                AbstractModule module = null;
                Throwable throwable = null;

                try {
                    module = moduleNode.getValue(creatingHolder.getTypeToken());
                } catch (ObjectMappingException e) {
                    throwable = e;
                }

                if (module == null) {
                    final String message = "Cannot initiate " + moduleName + " module. Skipping it...";

                    if (throwable != null) {
                        this.logger.warn(message, throwable);
                    } else {
                        this.logger.warn(message);
                    }

                    continue;
                }

                dest.put(moduleName, StoringModuleHolder.of(moduleName, module));
            }
        }

        return dest;
    }

}
