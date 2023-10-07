package cc.novoline.modules.configurations.holder;

import cc.novoline.modules.AbstractModule;
import cc.novoline.utils.java.Checks;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @author xDelsy
 */
public final class StoringModuleHolder<Module extends AbstractModule> extends ModuleHolder<Module> {

    /* constructors */
    private StoringModuleHolder(@NonNull String name, @NonNull Module module) {
        super(name, module);
    }

    @NonNull
    public static <Module extends AbstractModule> StoringModuleHolder<Module> of(@NonNull String name,
                                                                                 @NonNull Module module) {
        Checks.notBlank(name, "name");
        Checks.notNull(module, "module");

        return new StoringModuleHolder<>(name, module);
    }

    /* methods */
    //region Lombok
    public void setModule(@NonNull Module module) {
        Checks.notNull(module, "module");
        this.module = module;
    }
    //endregion

}
