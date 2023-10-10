/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.rektsky;

import java.util.Arrays;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.Client;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;

public class BMCAdminDetector
extends Module {
    String[] admins = new String[]{"Jinaaan", "_JustMix", "Eissaa", "1Mhmmd", "mohmad_q8", "1Brhom", "Aliiyah", "AssassinTime", "PerfectRod_", "Ahmmd", "xImTaiG_", "xIBerryPlayz", "comsterr", "1Sweet", "1Hussain", "Ev2n", "1M7mdz", "iMehdi_", "EyesO_Diamond", "xMz7", "1Daykel", "Aboz3bl", "qB6o6", "Luvaa", "Boviix", "Muntadher", "BinRashood", "ixBander", "ZANAD", "WalriderTime", "Thenvra", "CutieRana", "MK_F16", "saad6", "iiRaivy", "Onyc_", "iMrOt", "baderr", "leeleeeleeeleee", "M7mmd", "Bastic", "iikimo", "Creegam", "M4rwaan", "iS3od_", "xanaxjuice", "iiM7mdZ_", "1M7mmD", "Yaazzeed", "1LaB", "3Mmr", "ebararh", "Aymann_", "1Narwhql", "Faariss", "i_LrX", "Y2men", "1Cloud_", "ImAhMeeD", "Refolt", "AssassinLove", "MightyM7MD", "KaaReeeM", "Dizibre", "RADVN", "Sadlly", "TheDaddyJames", "DetectiveFahad", "ritclaw", "420syr1a", "_N3", "Zqvies", "needhiim", "FaRidok", "Requieem", "1RealFadi", "FANOR_SY", "1ELY_", "1Pepe_", "xL2d", "zMhnD", "1Tz3bo", "Tibbz_BGamer", "N15_", "Abdulaziz187", "1Mshari", "N29r", "PT7", "27bk", "ImXann", "m7mdxjw", "1_ST", "1Neres", "A4kat1", "JustDrink_", "FastRank", "arbawii", "SalemBayern_", "0hPqnos", "Tibbz_BGamer_", "xIMonster_Rj", "1ForAGer", "iSolom", "smckingweed", "mzh", "123k1k", "0Da3s", "ogloo_", "iRxv", "Fta7", "Banderr", "w7r", "S3rvox", "3AmOdi_", "PotatoSoublaki", "lt1x", "withoutyouu", "vdhvm", "ilybb0", "_Vxpe", "Jrx7", "KingHOYT", "1M0ha", "TheDrag_Xx", "Tostiebramkaas", "502z", "_R3", "Ravnly", "iA11", "vxom", "91l7", "BlackOurs", "yQuack", "iAhmedGG", "Khanful", "Ba6ee5man", "DemonsBra2", "Ba1z", "MistressFalling", "0nlyHazey", "DaBabyFan", "DetectiveFoxTY", "1LoST_", "ImMajesty", "Blood_Artz", "xa7a", "OREOBZ", "i_Cj", "Veshan", "AbuA7md506", "1A7MD_PvP", "AbduIlah", "Hmud", "Demon_001", "ToFy_", "DarkA5_", "FexoraNEP", "1Adam1_", "1Loga_", "1Ab0oDx", "OldAlone", "vinnythebot", "Raceth", "0h_Roby", "1A7mad1", "BigZ3tr", "N_AR_K", "1RE3", "itzZa1D", "RealA7md", "Mark_Gamer_YT_", "megtitimade1", "yosife_7Y", "wzii", "0PvP_", "whyamisoinlove", "BaSiL_123", "alshe5_steve", "AfootDiamond117", "samorayxs", "swazKweng", "5580", "be6sho", "mokgii", "ImM7MAD", "rivez", "wl3d", "1Sweatly", "1Levaai", "Mondoros", "c22l", "Mervy_", "1Sinqx", "Mythiques", "JustKreem", "mliodse", "_Surfers_", "1_aq", "manuelmaster", "uHyp_v2", "SekErkekEvladi", "M8DaM", "IR3DX", "SpecialAdam_", "G3rryx", "SirMedo_", "Sp0tzy_", "s2lm", "xiiRadi", "stepmixer", "Fluege", "uh8e", "Just7MO", "MeeDoo_", "vSL0W_", "xLePerfect", "xXBXLTXx", "D1ZZY0NE", "Lunching", "Violeet", "LRYD", "Tabby_Bhau", "3au", "MY_PRO_ITS_MAX", "Competely", "aXav", "INFAMOUSEEE", "RealWayne", "3rodi", "sh5boo6", "EZKarDIOPalma", "Draggn_", "TheOnlyM7MAD"};

    public BMCAdminDetector() {
        super("BMCAdminDetector", "Detect admins in BlocksMC, and auto /hub", Category.REKTSKY);
    }

    @Subscribe
    public void onTick(WorldTickEvent event) {
        for (Entity entity : this.mc.theWorld.getLoadedEntityList()) {
            if (!(entity instanceof EntityPlayer) || !Arrays.asList(this.admins).contains(entity.getName())) continue;
            Client.addClientChat("Admin Detected!:" + entity.getName());
        }
    }
}

