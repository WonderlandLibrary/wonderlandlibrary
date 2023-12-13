// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.net;

import org.apache.logging.log4j.core.appender.ManagerFactory;

public interface MailManagerFactory extends ManagerFactory<MailManager, MailManager.FactoryData>
{
    MailManager createManager(final String name, final MailManager.FactoryData data);
}
