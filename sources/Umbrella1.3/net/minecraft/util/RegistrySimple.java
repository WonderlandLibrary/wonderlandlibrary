/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  org.apache.commons.lang3.Validate
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraft.util.IRegistry;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegistrySimple
implements IRegistry {
    private static final Logger logger = LogManager.getLogger();
    protected final Map registryObjects = this.createUnderlyingMap();
    private static final String __OBFID = "CL_00001210";

    protected Map createUnderlyingMap() {
        return Maps.newHashMap();
    }

    @Override
    public Object getObject(Object p_82594_1_) {
        return this.registryObjects.get(p_82594_1_);
    }

    @Override
    public void putObject(Object p_82595_1_, Object p_82595_2_) {
        Validate.notNull((Object)p_82595_1_);
        Validate.notNull((Object)p_82595_2_);
        if (this.registryObjects.containsKey(p_82595_1_)) {
            logger.debug("Adding duplicate key '" + p_82595_1_ + "' to registry");
        }
        this.registryObjects.put(p_82595_1_, p_82595_2_);
    }

    public Set getKeys() {
        return Collections.unmodifiableSet(this.registryObjects.keySet());
    }

    public boolean containsKey(Object p_148741_1_) {
        return this.registryObjects.containsKey(p_148741_1_);
    }

    public Iterator iterator() {
        return this.registryObjects.values().iterator();
    }
}

