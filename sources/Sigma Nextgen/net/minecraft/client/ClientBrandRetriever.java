package net.minecraft.client;

import info.sigmaclient.sigma.modules.misc.FakeForge;

public class ClientBrandRetriever
{
    public static String getClientModName()
    {
        return FakeForge.getC17Send();
    }
}
