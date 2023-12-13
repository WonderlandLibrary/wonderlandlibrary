// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.utils;

import java.util.ArrayList;

public class NameProtectList
{
    private final ArrayList<String> names;
    
    public NameProtectList() {
        (this.names = new ArrayList<String>()).add("G\u00fcnter");
        this.names.add("GommeSchwitzer123");
        this.names.add("RiceWorker");
        this.names.add("iixYouseF_");
        this.names.add("JohannesAli");
        this.names.add("napimcialp");
        this.names.add("PONGkAN");
        this.names.add("Asaru95");
        this.names.add("BambiSlave");
        this.names.add("ytsiM");
        this.names.add("Dystinz");
        this.names.add("Folorixx");
        this.names.add("mika101x");
        this.names.add("YouTubexMemzy");
        this.names.add("ItsukiC");
        this.names.add("I_Refuse");
        this.names.add("Keidxn");
        this.names.add("lkoxaj");
        this.names.add("condompopper_");
        this.names.add("AysiRix");
        this.names.add("iRxge");
        this.names.add("AsianToggler");
        this.names.add("Jolly_Pink_Rock");
        this.names.add("Combadxitado");
        this.names.add("Nerty_24kgold");
        this.names.add("GommeHD");
        this.names.add("Hamsterfan956");
        this.names.add("BlocksMCYT");
        this.names.add("ibad875");
        this.names.add("Rewinside");
        this.names.add("SureGangGang");
        this.names.add("Cocogoat__");
        this.names.add("Macius997");
        this.names.add("supergames900");
        this.names.add("FramptIRL");
        this.names.add("GrosPif");
        this.names.add("pulloverJunky");
        this.names.add("childchild");
        this.names.add("dongjingting");
        this.names.add("Kyeh000");
        this.names.add("cyberking20");
        this.names.add("ashirne");
        this.names.add("Xyctek");
        this.names.add("PotatoBeam");
        this.names.add("Kejszi__");
        this.names.add("20Nesquikk");
        this.names.add("AlienBoy_007");
        this.names.add("duelst");
        this.names.add("XxSebastian26xX");
        this.names.add("zyja");
        this.names.add("Saocity");
        this.names.add("yt_kakashijunior");
        this.names.add("SupaPVPSwagster");
        this.names.add("Lenianka");
        this.names.add("xIbrahim_");
        this.names.add("trollable");
        this.names.add("Sotakopteri1");
        this.names.add("Halloween66");
        this.names.add("jacodb");
        this.names.add("Aterm");
        this.names.add("BillyBobster007");
        this.names.add("The_SoulDark");
        this.names.add("mysticvz");
        this.names.add("Flooperoosa");
        this.names.add("knilchChamp");
        this.names.add("zubbzz");
        this.names.add("AnimeNoob");
        this.names.add("Lobsy_Dienne");
        this.names.add("ArrowSayer");
        this.names.add("PanHamburger123_");
        this.names.add("Shhheeeeeeeeeshh");
        this.names.add("hiroubrain");
        this.names.add("LoLDavid");
    }
    
    public String getRandomName() {
        final int i = RandomUtil.nextInt(0, this.names.size() - 1);
        return this.names.get(i);
    }
}
