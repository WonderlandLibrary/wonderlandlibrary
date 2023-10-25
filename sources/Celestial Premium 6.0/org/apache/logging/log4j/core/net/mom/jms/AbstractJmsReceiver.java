/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.net.mom.jms;

import org.apache.logging.log4j.core.net.server.JmsServer;

public abstract class AbstractJmsReceiver {
    protected abstract void usage();

    protected void doMain(String ... args) throws Exception {
        if (args.length != 4) {
            this.usage();
            System.exit(1);
        }
        JmsServer server = new JmsServer(args[0], args[1], args[2], args[3]);
        server.run();
    }
}

