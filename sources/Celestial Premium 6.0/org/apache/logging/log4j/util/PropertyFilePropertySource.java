/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.util.LowLevelLogUtil;
import org.apache.logging.log4j.util.PropertiesPropertySource;

public class PropertyFilePropertySource
extends PropertiesPropertySource {
    public PropertyFilePropertySource(String fileName) {
        super(PropertyFilePropertySource.loadPropertiesFile(fileName));
    }

    private static Properties loadPropertiesFile(String fileName) {
        Properties props = new Properties();
        for (URL url : LoaderUtil.findResources(fileName)) {
            try {
                InputStream in = url.openStream();
                Throwable throwable = null;
                try {
                    props.load(in);
                }
                catch (Throwable throwable2) {
                    throwable = throwable2;
                    throw throwable2;
                }
                finally {
                    if (in == null) continue;
                    if (throwable != null) {
                        try {
                            in.close();
                        }
                        catch (Throwable throwable3) {
                            throwable.addSuppressed(throwable3);
                        }
                        continue;
                    }
                    in.close();
                }
            }
            catch (IOException e) {
                LowLevelLogUtil.logException("Unable to read " + url, e);
            }
        }
        return props;
    }

    @Override
    public int getPriority() {
        return 0;
    }
}

