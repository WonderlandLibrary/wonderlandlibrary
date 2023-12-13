// 
// Decompiled by Procyon v0.5.36
// 

package de.wazed.wrapper;

import de.wazed.wrapper.licensing.SwitchService;
import de.wazed.wrapper.utils.WebUtil;
import de.wazed.wrapper.generation.Generator;

public class Api
{
    public static void init() {
        new Generator();
        new WebUtil();
        new SwitchService();
    }
}
