package com.ohare.client.module.modules.visuals;

import com.ohare.client.module.Module;

/**
 * made by oHare for Client
 *
 * @since 5/29/2019
 **/
public class AntiStrike extends Module {

    public AntiStrike() {
        super("AntiStrike", Category.VISUALS, -1);
        setDescription("Replace blacklisted words");
        setRenderlabel("Anti Strike");
    }

    @Override
    public boolean hasSubscribers() {
        return false;
    }
}
