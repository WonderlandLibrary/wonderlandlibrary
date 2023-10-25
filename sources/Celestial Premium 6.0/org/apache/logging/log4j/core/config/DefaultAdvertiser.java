/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config;

import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.net.Advertiser;

@Plugin(name="default", category="Core", elementType="advertiser", printObject=false)
public class DefaultAdvertiser
implements Advertiser {
    @Override
    public Object advertise(Map<String, String> properties) {
        return null;
    }

    @Override
    public void unadvertise(Object advertisedObject) {
    }
}

