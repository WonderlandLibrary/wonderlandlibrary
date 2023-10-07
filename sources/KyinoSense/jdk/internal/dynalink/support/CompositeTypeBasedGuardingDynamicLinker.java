/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.support;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.GuardingDynamicLinker;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;

public class CompositeTypeBasedGuardingDynamicLinker
implements TypeBasedGuardingDynamicLinker,
Serializable {
    private static final long serialVersionUID = 1L;
    private final ClassValue<List<TypeBasedGuardingDynamicLinker>> classToLinker;

    public CompositeTypeBasedGuardingDynamicLinker(Iterable<? extends TypeBasedGuardingDynamicLinker> linkers) {
        LinkedList<TypeBasedGuardingDynamicLinker> l = new LinkedList<TypeBasedGuardingDynamicLinker>();
        for (TypeBasedGuardingDynamicLinker typeBasedGuardingDynamicLinker : linkers) {
            l.add(typeBasedGuardingDynamicLinker);
        }
        this.classToLinker = new ClassToLinker(l.toArray(new TypeBasedGuardingDynamicLinker[l.size()]));
    }

    @Override
    public boolean canLinkType(Class<?> type) {
        return !this.classToLinker.get(type).isEmpty();
    }

    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest linkRequest, LinkerServices linkerServices) throws Exception {
        Object obj = linkRequest.getReceiver();
        if (obj == null) {
            return null;
        }
        for (TypeBasedGuardingDynamicLinker linker : this.classToLinker.get(obj.getClass())) {
            GuardedInvocation invocation = linker.getGuardedInvocation(linkRequest, linkerServices);
            if (invocation == null) continue;
            return invocation;
        }
        return null;
    }

    public static List<GuardingDynamicLinker> optimize(Iterable<? extends GuardingDynamicLinker> linkers) {
        LinkedList<GuardingDynamicLinker> llinkers = new LinkedList<GuardingDynamicLinker>();
        LinkedList<TypeBasedGuardingDynamicLinker> tblinkers = new LinkedList<TypeBasedGuardingDynamicLinker>();
        for (GuardingDynamicLinker guardingDynamicLinker : linkers) {
            if (guardingDynamicLinker instanceof TypeBasedGuardingDynamicLinker) {
                tblinkers.add((TypeBasedGuardingDynamicLinker)guardingDynamicLinker);
                continue;
            }
            CompositeTypeBasedGuardingDynamicLinker.addTypeBased(llinkers, tblinkers);
            llinkers.add(guardingDynamicLinker);
        }
        CompositeTypeBasedGuardingDynamicLinker.addTypeBased(llinkers, tblinkers);
        return llinkers;
    }

    private static void addTypeBased(List<GuardingDynamicLinker> llinkers, List<TypeBasedGuardingDynamicLinker> tblinkers) {
        switch (tblinkers.size()) {
            case 0: {
                break;
            }
            case 1: {
                llinkers.addAll(tblinkers);
                tblinkers.clear();
                break;
            }
            default: {
                llinkers.add(new CompositeTypeBasedGuardingDynamicLinker(tblinkers));
                tblinkers.clear();
            }
        }
    }

    private static class ClassToLinker
    extends ClassValue<List<TypeBasedGuardingDynamicLinker>> {
        private static final List<TypeBasedGuardingDynamicLinker> NO_LINKER = Collections.emptyList();
        private final TypeBasedGuardingDynamicLinker[] linkers;
        private final List<TypeBasedGuardingDynamicLinker>[] singletonLinkers;

        ClassToLinker(TypeBasedGuardingDynamicLinker[] linkers) {
            this.linkers = linkers;
            this.singletonLinkers = new List[linkers.length];
            for (int i = 0; i < linkers.length; ++i) {
                this.singletonLinkers[i] = Collections.singletonList(linkers[i]);
            }
        }

        @Override
        protected List<TypeBasedGuardingDynamicLinker> computeValue(Class<?> clazz) {
            List<TypeBasedGuardingDynamicLinker> list = NO_LINKER;
            block4: for (int i = 0; i < this.linkers.length; ++i) {
                TypeBasedGuardingDynamicLinker linker = this.linkers[i];
                if (!linker.canLinkType(clazz)) continue;
                switch (list.size()) {
                    case 0: {
                        list = this.singletonLinkers[i];
                        continue block4;
                    }
                    case 1: {
                        list = new LinkedList<TypeBasedGuardingDynamicLinker>(list);
                    }
                    default: {
                        list.add(linker);
                    }
                }
            }
            return list;
        }
    }
}

