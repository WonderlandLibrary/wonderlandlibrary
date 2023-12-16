// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features;

import java.util.Set;
import java.util.stream.Collector;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;
import com.google.common.collect.Sets;
import java.util.LinkedHashSet;

public class ModuleManager
{
    public static final ModuleManager INSTANCE;
    public LinkedHashSet<SCModule> modules;
    
    static {
        INSTANCE = new ModuleManager();
    }
    
    public ModuleManager() {
        this.modules = (LinkedHashSet<SCModule>)Sets.newLinkedHashSet();
    }
    
    public void init() {
        final Set<Class<? extends SCModule>> modules = new Reflections("moonsense.features.modules", new Scanner[0]).getSubTypesOf(SCModule.class);
        final ReflectiveOperationException ex;
        ReflectiveOperationException e;
        modules.forEach(module -> {
            if (module == SCAbstractRenderModule.class || module == SCDefaultRenderModule.class) {
                return;
            }
            else {
                try {
                    module.newInstance();
                }
                catch (InstantiationException | IllegalAccessException ex2) {
                    e = ex;
                    e.printStackTrace();
                }
                return;
            }
        });
        this.modules.forEach(module -> module.initMod());
        this.modules = this.modules.stream().sorted((o1, o2) -> {
            if (o1.isRender() && !o2.isRender()) {
                return -1;
            }
            else if (!o1.isRender() && o2.isRender()) {
                return 1;
            }
            else {
                return o1.getKey().compareTo(o2.getKey());
            }
        }).collect((Collector<? super Object, ?, LinkedHashSet<SCModule>>)Collectors.toCollection((Supplier<R>)LinkedHashSet::new));
    }
}
