/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.api.scripting;

import java.util.Collection;
import java.util.Set;
import jdk.Exported;

@Exported
public interface JSObject {
    public Object call(Object var1, Object ... var2);

    public Object newObject(Object ... var1);

    public Object eval(String var1);

    public Object getMember(String var1);

    public Object getSlot(int var1);

    public boolean hasMember(String var1);

    public boolean hasSlot(int var1);

    public void removeMember(String var1);

    public void setMember(String var1, Object var2);

    public void setSlot(int var1, Object var2);

    public Set<String> keySet();

    public Collection<Object> values();

    public boolean isInstance(Object var1);

    public boolean isInstanceOf(Object var1);

    public String getClassName();

    public boolean isFunction();

    public boolean isStrictFunction();

    public boolean isArray();

    @Deprecated
    public double toNumber();
}

