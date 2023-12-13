package koks.api.manager.proxy;

import lombok.Getter;
import lombok.Setter;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */
public class ProxyManager {

    @Getter @Setter
    String currentProxy;

    @Getter @Setter
    int currentProxyPort;

    @Getter @Setter
    boolean useProxy;

}
