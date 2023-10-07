/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package org.spongepowered.asm.mixin.injection.points;

import java.util.Collection;
import java.util.ListIterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;

@InjectionPoint.AtCode(value="INVOKE")
public class BeforeInvoke
extends InjectionPoint {
    protected final MemberInfo target;
    protected final MemberInfo permissiveTarget;
    protected final int ordinal;
    protected final String className;
    private boolean log = false;
    private final Logger logger = LogManager.getLogger((String)"mixin");

    public BeforeInvoke(InjectionPointData data) {
        super(data);
        this.target = data.getTarget();
        this.ordinal = data.getOrdinal();
        this.log = data.get("log", false);
        this.className = this.getClassName();
        this.permissiveTarget = data.getContext().getOption(MixinEnvironment.Option.REFMAP_REMAP) ? this.target.transform(null) : null;
    }

    private String getClassName() {
        InjectionPoint.AtCode atCode = this.getClass().getAnnotation(InjectionPoint.AtCode.class);
        return String.format("@At(%s)", atCode != null ? atCode.value() : this.getClass().getSimpleName().toUpperCase());
    }

    public BeforeInvoke setLogging(boolean logging) {
        this.log = logging;
        return this;
    }

    @Override
    public boolean find(String desc, InsnList insns, Collection<AbstractInsnNode> nodes) {
        this.log("{} is searching for an injection point in method with descriptor {}", this.className, desc);
        if (!this.find(desc, insns, nodes, this.target)) {
            return this.find(desc, insns, nodes, this.permissiveTarget);
        }
        return true;
    }

    protected boolean find(String desc, InsnList insns, Collection<AbstractInsnNode> nodes, MemberInfo target) {
        if (target == null) {
            return false;
        }
        int ordinal = 0;
        boolean found = false;
        ListIterator<AbstractInsnNode> iter = insns.iterator();
        while (iter.hasNext()) {
            AbstractInsnNode insn = iter.next();
            if (this.matchesInsn(insn)) {
                MemberInfo nodeInfo = new MemberInfo(insn);
                this.log("{} is considering insn {}", this.className, nodeInfo);
                if (target.matches(nodeInfo.owner, nodeInfo.name, nodeInfo.desc)) {
                    this.log("{} > found a matching insn, checking preconditions...", this.className);
                    if (this.matchesInsn(nodeInfo, ordinal)) {
                        this.log("{} > > > found a matching insn at ordinal {}", this.className, ordinal);
                        found |= this.addInsn(insns, nodes, insn);
                        if (this.ordinal == ordinal) break;
                    }
                    ++ordinal;
                }
            }
            this.inspectInsn(desc, insns, insn);
        }
        return found;
    }

    protected boolean addInsn(InsnList insns, Collection<AbstractInsnNode> nodes, AbstractInsnNode insn) {
        nodes.add(insn);
        return true;
    }

    protected boolean matchesInsn(AbstractInsnNode insn) {
        return insn instanceof MethodInsnNode;
    }

    protected void inspectInsn(String desc, InsnList insns, AbstractInsnNode insn) {
    }

    protected boolean matchesInsn(MemberInfo nodeInfo, int ordinal) {
        this.log("{} > > comparing target ordinal {} with current ordinal {}", this.className, this.ordinal, ordinal);
        return this.ordinal == -1 || this.ordinal == ordinal;
    }

    protected void log(String message, Object ... params) {
        if (this.log) {
            this.logger.info(message, params);
        }
    }
}

