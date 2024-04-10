package info.sigmaclient.sigma.utils.render.rendermanagers;

import info.sigmaclient.sigma.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import top.fl0wowp4rty.phantomshield.annotations.Native;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.HashMap;

@Native
public class TextureObf {
    public static HashMap<String, DecodeTexture> mappings = new HashMap<>();

    public static void copyFile(byte[] src, File target) {
        try {
            try (OutputStream out = Files.newOutputStream(target.toPath())) {
                out.write(src);
            }
        } catch (IOException e) {
            Minecraft.getInstance().shutdown();
            e.printStackTrace();
        }
    }

    public static void writeData(final File file, final String content, final boolean append) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            try (PrintWriter printWriter = new PrintWriter(file)) {
                printWriter.print(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean ENABLE = false;

    public static boolean isClientPic(String local) {
        return ENABLE && (local.startsWith("sigmang/") || local.startsWith("sigma/")) && local.endsWith(".png");
    }

    public static void init() {
        if (!ENABLE) return;
        mappings.put("sigma/add.png", new DecodeTexture("uCus64PpOcYvh8r9Vfff.bmp", "dnhZPBWbNf"));
        mappings.put("sigma/addalt.png", new DecodeTexture("RJUmizTpFYJNTZ7CtXXF.bmp", "1t6anOpZyV"));
        mappings.put("sigma/addalt2.png", new DecodeTexture("lFyljux9Ws3mhxbnwgOB.bmp", "TEMTxl0waF"));
        mappings.put("sigma/alt.png", new DecodeTexture("izB3SvE6Hw5Nw48Idt6v.bmp", "aMErEa9K2y"));
        mappings.put("sigma/altmanager.png", new DecodeTexture("8JYEi9KFF6jbbX1Ni9sr.bmp", "3oEnF0mqRN"));
        mappings.put("sigma/altrect.png", new DecodeTexture("R9AomjhXKo5AGWvYcsRu.bmp", "Q0Fapci0Xx"));
        mappings.put("sigma/altshadow.png", new DecodeTexture("9H15J69nuo6Q1H3xPZCC.bmp", "Heo843kheI"));
        mappings.put("sigma/arraylistshadow.png", new DecodeTexture("H8bS9030TA1YHDXHeVVf.bmp", "wFMzef4sjU"));
        mappings.put("sigma/background.png", new DecodeTexture("eUvfILYzVCz092Cd2ZoN.bmp", "Fgahtxy9OR"));
        mappings.put("sigma/bigplay.png", new DecodeTexture("bkhK5YXPoZHkCr7nELN7.bmp", "2w4zlUWOdI"));
        mappings.put("sigma/BlankCard.png", new DecodeTexture("KJF7RUG13Xz0UiZjgp2m.bmp", "8B5KN5cevx"));
        mappings.put("sigma/button/dvd.png", new DecodeTexture("xRwnj2joS9N5Oe4jNNmr.bmp", "yCH9mbjGbv"));
        mappings.put("sigma/button/Jello.png", new DecodeTexture("aQFiK3mYsB6bqU59hDr7.bmp", "nWtgLzzqkX"));
        mappings.put("sigma/button/logo.png", new DecodeTexture("7KcXUcwxA3oIu9ufURXv.bmp", "nvUJYUxWpK"));
        mappings.put("sigma/button/zzz.png", new DecodeTexture("ObVVg80ZxCGyDTPXkWyh.bmp", "pjR1ixCypY"));
        mappings.put("sigma/check.png", new DecodeTexture("CGH8T4rNE49OX4Y7CvJS.bmp", "HQf2TQ0J1C"));
        mappings.put("sigma/checked.png", new DecodeTexture("paiW3btYFCFUlr1EjDd9.bmp", "s4FdTXNvHe"));
        mappings.put("sigma/combatcat.png", new DecodeTexture("y40HJZAmyKIYbIQa5mbM.bmp", "LqC6VJKubk"));
        mappings.put("sigma/copyrightjello.png", new DecodeTexture("oV03vVqsDClU9rHaTGF0.bmp", "ixYf3qtTDU"));
        mappings.put("sigma/durationshadow.png", new DecodeTexture("xX4lIQ8HPJVwyCabnA8G.bmp", "IvkZH9IxAn"));
        mappings.put("sigma/exit.png", new DecodeTexture("mkC1eBfelfbDRrRlHbrB.bmp", "n4sEGzZCdN"));
        mappings.put("sigma/failed.png", new DecodeTexture("iZmw5WUdlVhlHReyFEgL.bmp", "HYsdtafEDf"));
        mappings.put("sigma/fastforward.png", new DecodeTexture("kS5rUY351tMdaen9PiNr.bmp", "v5RcMNqxrY"));
        mappings.put("sigma/img.png", new DecodeTexture("sJLrs3OLd3CbLsK5CaWc.bmp", "qJGo44pte5"));
        mappings.put("sigma/itemcat.png", new DecodeTexture("xYFjHDGbVtndfuVD4NzL.bmp", "fbmgjFgcsi"));
        mappings.put("sigma/jelloaltmanager.png", new DecodeTexture("KOr08I8Smc9Xg2C7dygs.bmp", "FzbatYrO6z"));
        mappings.put("sigma/jellologo.png", new DecodeTexture("s5TZpXPIxllUcL7Y5u2d.bmp", "WfxUwMOymt"));
        mappings.put("sigma/jellomusicbackground.png", new DecodeTexture("zkQhnGYBV4RWHv6kvTi2.bmp", "a6vgkq1Zxj"));
        mappings.put("sigma/JelloPanel.png", new DecodeTexture("hooXcjM6xGyp0iPwRApK.bmp", "aOJSVQqh4J"));
        mappings.put("sigma/JelloPanelOverlay.png", new DecodeTexture("l9jSLXkEB7VZzHR07dSP.bmp", "JC2ZQ7PNzQ"));
        mappings.put("sigma/keybind.png", new DecodeTexture("mwBx7wqCPLBtEMQ2KWJg.bmp", "Yb9RyK9Spv"));
        mappings.put("sigma/keystrokes.png", new DecodeTexture("yNtggODO9fVtIxzZ1iuP.bmp", "7ttDhSBi0z"));
        mappings.put("sigma/keystrokes_nb.png", new DecodeTexture("wJmQUmtfmBg0GepheZM5.bmp", "GVIWY0Kp06"));
        mappings.put("sigma/keystrokes_nb2.png", new DecodeTexture("HXAw92TpL0dOXMPDvl5E.bmp", "z5OulxnEZD"));
        mappings.put("sigma/leftslider.png", new DecodeTexture("QJdzR6a9p5XfXiNZBw0b.bmp", "L7Pmuli6Lb"));
        mappings.put("sigma/loader.png", new DecodeTexture("sJyR3lA0IWa9RyBxuCvB.bmp", "ltrsRh0lb7"));
        mappings.put("sigma/logo.png", new DecodeTexture("JVQYzHrCyTcfnYHgWgcV.bmp", "3ZY7CbAEeu"));
        mappings.put("sigma/LogoNew.png", new DecodeTexture("edG0yRE3zJwGheedhddE.bmp", "ZvncPbqv5B"));
        mappings.put("sigma/mainmenubackground.png", new DecodeTexture("VXx7YlnCU9p0HnSCvhoG.bmp", "x3i3MtVGS6"));
        mappings.put("sigma/menu/accounts.png", new DecodeTexture("h55pVpaQn6KL3eT9m887.bmp", "KW7Whaw0c5"));
        mappings.put("sigma/menu/agora.png", new DecodeTexture("TI2cJuhnBDcLmT8wmGsp.bmp", "YqUPyTyRIn"));
        mappings.put("sigma/menu/big.png", new DecodeTexture("CIvCFNguAVknvNc5jx6V.bmp", "EgcRBgPp21"));
        mappings.put("sigma/menu/changelog.png", new DecodeTexture("iZUMlTsvLW0o9B6bXGOp.bmp", "JapwSXqqd9"));
        mappings.put("sigma/menu/changelog2.png", new DecodeTexture("1UEku4WEPcn5pGSqv4aQ.bmp", "mOiPb8EtNY"));
        mappings.put("sigma/menu/exit.png", new DecodeTexture("X1hEyD6yZeLtlMd5a3Hy.bmp", "yUgw5brihh"));
        mappings.put("sigma/menu/language.png", new DecodeTexture("H6qztHdlK6YE5hnugKXe.bmp", "pCnSQl7BN7"));
        mappings.put("sigma/menu/mainmenubackground.png", new DecodeTexture("eCgFvzWEK0IzpC9UlY5z.bmp", "3wQA3cq8lK"));
        mappings.put("sigma/menu/multiplayer.png", new DecodeTexture("py4H7du1cKtil0boWb1P.bmp", "cBUWbZn5Fg"));
        mappings.put("sigma/menu/options.png", new DecodeTexture("OENCELOhd04rsSQoNZw8.bmp", "SAZJYH2Sgc"));
        mappings.put("sigma/menu/singleplayer.png", new DecodeTexture("9a2GTXgsJpGhk9V7C8tG.bmp", "xSBQqlz5Qy"));
        mappings.put("sigma/menu/yt.png", new DecodeTexture("tGd4rcbH0rL0O3aRGFQ1.bmp", "Ck6MGusS2l"));
        mappings.put("sigma/Microsoftlogin.png", new DecodeTexture("BVFEEVqMAfNkpvjQgyhJ.bmp", "ZQzJZouKD2"));
        mappings.put("sigma/MiniMap.png", new DecodeTexture("HRNs9SEYUoc2NTXqgzhE.bmp", "AftT7FT6go"));
        mappings.put("sigma/movementcat.png", new DecodeTexture("s8yR2wUGxV1iNuI78wzL.bmp", "iBeHrg9Rcc"));
        mappings.put("sigma/multiplayer.png", new DecodeTexture("QBbl559bBmnYzWAvd5RU.bmp", "TgezC1QmmH"));
        mappings.put("sigma/music shadow.png", new DecodeTexture("kYHzQ5r3vfnotBgdzaMG.bmp", "E1dOztnEF0"));
        mappings.put("sigma/music.png", new DecodeTexture("a39XpfCmrCJlDAYwJRm2.bmp", "wkuXhZgos2"));
        mappings.put("sigma/musicicon.png", new DecodeTexture("FWElXEdhi2wu1rjVkCp0.bmp", "xr8UPODjgp"));
        mappings.put("sigma/musicplayer/bigplay.png", new DecodeTexture("2y7wkaD35CjELo7z9j9q.bmp", "sYK3apR2Up"));
        mappings.put("sigma/musicplayer/repeat.png", new DecodeTexture("Spj5TmQIEd4mmuT3nHjq.bmp", "SNUUoGqZBx"));
        mappings.put("sigma/musicplayer/repeatdis.png", new DecodeTexture("OHtzvOhgTPYY8P6aeA0Q.bmp", "XE9yd901ei"));
        mappings.put("sigma/musicplayer/wave.png", new DecodeTexture("1vrbEcI4mf3ONuEZwKNU.bmp", "U42N9ZAl70"));
        mappings.put("sigma/musicshadow.png", new DecodeTexture("z4Lr6hSyXoj3OyEH8dOg.bmp", "bktxTQjM1d"));
        mappings.put("sigma/NameTags.png", new DecodeTexture("hxiZ9CgUrwXIDc3B4dxI.bmp", "5t6Rba5ub5"));
        mappings.put("sigma/NewB.png", new DecodeTexture("iQDtRowOkSKsMywsNQdj.bmp", "H3ex4v60hk"));
        mappings.put("sigma/NewLogo.png", new DecodeTexture("Dm0AjlumRcjSR7Ig099C.bmp", "K73nDuHbqh"));
        mappings.put("sigma/notification.png", new DecodeTexture("apI9tUp86V1S24C9Jgb0.bmp", "KBBpRT6uyX"));
        mappings.put("sigma/nt.png", new DecodeTexture("WoePql6vXURjtuE0xZ5m.bmp", "C2ZGnNnj9Y"));
        mappings.put("sigma/onbuttonbg.png", new DecodeTexture("m3kb4l67SmsTnJE2K11E.bmp", "tsz08CPVTG"));
        mappings.put("sigma/onbuttonbgres.png", new DecodeTexture("vZjWoCPpB57hYBZChGxK.bmp", "4XbdBJTVBK"));
        mappings.put("sigma/options.png", new DecodeTexture("aAOLZZ705o390mYWSrxr.bmp", "x8lGsbx2xd"));
        mappings.put("sigma/panelbottom.png", new DecodeTexture("b6lfvAnRgo0yWoS6NhEo.bmp", "awKcW40x0O"));
        mappings.put("sigma/panelbottom2.png", new DecodeTexture("7eJw6KoS0oG28y8GdAM1.bmp", "eSHhnLJU8c"));
        mappings.put("sigma/panelbottomleft.png", new DecodeTexture("ia7KJMWVZFcLXrVR78Jo.bmp", "bf5UurrWO5"));
        mappings.put("sigma/panelbottomleft2.png", new DecodeTexture("8SfMiyNwhDQWkFWjL8jC.bmp", "7jUSdqXIZI"));
        mappings.put("sigma/panelbottomright.png", new DecodeTexture("AytU2SHo22Zz8hePOALD.bmp", "kSbUu3Ss9Z"));
        mappings.put("sigma/panelbottomright2.png", new DecodeTexture("xBFNpOqoimdNH5RwIt0c.bmp", "bCfDmHGJZi"));
        mappings.put("sigma/panelleft.png", new DecodeTexture("U77UMbVOQDWCZudqoLmw.bmp", "JqzSfrPTWH"));
        mappings.put("sigma/panelleft2.png", new DecodeTexture("hWH7NIDFuuEUnFJjG0sr.bmp", "189VFCwMQS"));
        mappings.put("sigma/panelright.png", new DecodeTexture("mBsBpqK9VCMyBdEaJncv.bmp", "dS5XUHfDed"));
        mappings.put("sigma/panelright2.png", new DecodeTexture("gnxBTlHAfczaq3viQHg3.bmp", "dV9EezMFPb"));
        mappings.put("sigma/paneltop.png", new DecodeTexture("kt6AMSBGHm8WBGtp3C1C.bmp", "sCw9ZKCJsm"));
        mappings.put("sigma/paneltop2.png", new DecodeTexture("3YogRVSU83RKtV4UaqLB.bmp", "HojNMryNmu"));
        mappings.put("sigma/paneltopleft.png", new DecodeTexture("4TdZhnLsHw7qT0Qo0nHn.bmp", "S2BfWR1wum"));
        mappings.put("sigma/paneltopleft2.png", new DecodeTexture("czksPWDNxs1oec068CXg.bmp", "cBzVxOxjlc"));
        mappings.put("sigma/paneltopright.png", new DecodeTexture("l4xn4NSBFQiSB8gtjeTG.bmp", "P9lKFy1tC1"));
        mappings.put("sigma/paneltopright2.png", new DecodeTexture("7yMdhCempdgXVjYfcN8P.bmp", "jY5ctUJ2BO"));
        mappings.put("sigma/pause.png", new DecodeTexture("MKW0KHOYc04bsTsEWuN6.bmp", "jbQMJ6U3Tu"));
        mappings.put("sigma/play.png", new DecodeTexture("QSdLigdcKtFnI60dvDw1.bmp", "oXy2pWtSMA"));
        mappings.put("sigma/playercat.png", new DecodeTexture("fo4oQQB4g9K1ROH4Ml07.bmp", "T41F3hxHMF"));
        mappings.put("sigma/playermodelbackgroundblock.png", new DecodeTexture("tseDYFgaFeKII57TIN0N.bmp", "ROomQ1tilL"));
        mappings.put("sigma/playermodelbackgroundblock2.png", new DecodeTexture("JezoEjHOLfezUI1MOkCV.bmp", "2r8EPreN2M"));
        mappings.put("sigma/playermodelshadow.png", new DecodeTexture("u6SUnGSro9G7kfqcRNmv.bmp", "QtZAHfdfFo"));
        mappings.put("sigma/rendercat.png", new DecodeTexture("UlVXiHnVHdT2juaXbkYG.bmp", "VClST0xiPK"));
        mappings.put("sigma/rewind.png", new DecodeTexture("hFrXDCCwHKOM2x1YLAde.bmp", "KXBdHoHhsX"));
        mappings.put("sigma/sb.png", new DecodeTexture("lwxDZrE8jztV4rNX68Kc.bmp", "UNesMofNKo"));
        mappings.put("sigma/scroll ball.png", new DecodeTexture("KcWyarpPb1INhVihtR0p.bmp", "tiXlKbhmLn"));
        mappings.put("sigma/select/background.png", new DecodeTexture("yPy0BQaZezB4hkwWWlOq.bmp", "DnIQiRe8Ke"));
        mappings.put("sigma/select/big.png", new DecodeTexture("9AblN5noPM8MzNmCoXKZ.bmp", "KvYE7v1OZA"));
        mappings.put("sigma/select/classic.png", new DecodeTexture("cmY59kT5pWl8eu4uyKTh.bmp", "O8ajhtQUgO"));
        mappings.put("sigma/select/jello.png", new DecodeTexture("Zv5tJSkGbMauGfJev2fQ.bmp", "CI3ngMRvNC"));
        mappings.put("sigma/select/jellosigma.png", new DecodeTexture("XHCj3NqmE6Fwy9gfu1yH.bmp", "APVkaX9zoN"));
        mappings.put("sigma/select/logo.png", new DecodeTexture("mMgtuVkJClAmybpO5f4h.bmp", "1tKMUo1CU0"));
        mappings.put("sigma/select/noaddons.png", new DecodeTexture("wXqy8ZlGKVHxarwqNho6.bmp", "gltrYAGtNB"));
        mappings.put("sigma/select/sele.png", new DecodeTexture("0qIy64KZfeGfZUHSIVE8.bmp", "K3P8qn6TVl"));
        mappings.put("sigma/select/sigmalogo.png", new DecodeTexture("oV9rUjY2q8L4PvSpROpv.bmp", "R6DMb58Fft"));
        mappings.put("sigma/select/sigmalogo2.png", new DecodeTexture("BeTex2TURk039r8pp4q8.bmp", "PI4vLYqshu"));
        mappings.put("sigma/select/spl.png", new DecodeTexture("60P5BxDkquQVz31DdRAb.bmp", "EGzirsNuuk"));
        mappings.put("sigma/select/spl2.png", new DecodeTexture("NoM2lJsxfgzaJ7z2X107.bmp", "5lBgqnuuOO"));
        mappings.put("sigma/select/splashlogo.png", new DecodeTexture("KF0nblo8EMEnQlaskFDK.bmp", "ljOoXMS5SL"));
        mappings.put("sigma/selectedAltTriangle.png", new DecodeTexture("ELr5K2OlAkzM2mRLDfdk.bmp", "BjGRu0rEJI"));
        mappings.put("sigma/settings.png", new DecodeTexture("Qpd1Eyxw3vwhnJTFT9DG.bmp", "jdfznuqsoo"));
        mappings.put("sigma/shadow.png", new DecodeTexture("e5R91h9BkgCZJLrlyIwS.bmp", "O52QCHPWUO"));
        mappings.put("sigma/shadowgui.png", new DecodeTexture("5I9PkLxnZ4EiCxGxNLir.bmp", "FPxeJIjm9m"));
        mappings.put("sigma/shop.png", new DecodeTexture("zA2f49zaTOJylUaTz2fP.bmp", "xN2HQKyN3Z"));
        mappings.put("sigma/sigmacount.png", new DecodeTexture("8UsCk9l5wouJNN6H0qHS.bmp", "WPhL5Pzu1l"));
        mappings.put("sigma/sigmalogo.png", new DecodeTexture("Ci2OrIgJPJelXOY49kRN.bmp", "jlYSfQ1jW3"));
        mappings.put("sigma/singleplayer.png", new DecodeTexture("VZHPrBjyPrQu4QC74DJK.bmp", "iq3IwKWX5s"));
        mappings.put("sigma/slector.png", new DecodeTexture("z6kKadAHqsztBd9EP3JN.bmp", "1tYCytzSTc"));
        mappings.put("sigma/sliderhead.png", new DecodeTexture("BDHXTnLv8xLqc7nVGDkJ.bmp", "kVp4KUHRDl"));
        mappings.put("sigma/sliderright.png", new DecodeTexture("vmFU1tKKtzaMQUbNEcDl.bmp", "WkcWHuUGRW"));
        mappings.put("sigma/slidershadow.png", new DecodeTexture("QgfEsLSoXXq7c45UdTTM.bmp", "JcnfLAmEPu"));
        mappings.put("sigma/splash.png", new DecodeTexture("Ylh0t51Q5LkvbhEveYlr.bmp", "lCcJaRWn0g"));
        mappings.put("sigma/splashload.png", new DecodeTexture("NM7mVPtZW5uNoMgTL4uI.bmp", "PvDXvX7ruX"));
        mappings.put("sigma/switch.png", new DecodeTexture("uIgGYxSlQuCUmqiblZ7S.bmp", "L35MYM0RrN"));
        mappings.put("sigma/switchbg.png", new DecodeTexture("dbpd5xqsMyG9sHnTZpRZ.bmp", "oHsqbeGYlt"));
        mappings.put("sigma/switchbgres.png", new DecodeTexture("IYl14oXG4P2Kpg6qchg1.bmp", "F7Fp5PKm9J"));
        mappings.put("sigma/switchonly.png", new DecodeTexture("JosgdsuIZBkMoJWYBPe0.bmp", "sY6oZ9zE1D"));
        mappings.put("sigma/switchonlyres.png", new DecodeTexture("FEsv04wi2PXTPgJNBOWC.bmp", "ocq2mHOYPK"));
        mappings.put("sigma/switchres.png", new DecodeTexture("MFb3co2L7lFRclMPkBcw.bmp", "iAavW9CviT"));
        mappings.put("sigma/TabGUI.png", new DecodeTexture("LOoCdE5R5lEN1Ev1dpb1.bmp", "YdjAgmZYCQ"));
        mappings.put("sigma/tabguishadow.png", new DecodeTexture("jIEPeSelnI0MuS85okwN.bmp", "gX0aZ3U4Zb"));
        mappings.put("sigma/thumbnailsquare.png", new DecodeTexture("giqgH3RAy0ZrMmMzDBoO.bmp", "tpdG7WE6ki"));
        mappings.put("sigma/title.png", new DecodeTexture("SjkG0frTASEBX7GFKgMC.bmp", "1N11p8Dt3I"));
        mappings.put("sigma/unchecked.png", new DecodeTexture("qy7UT3RBvOJGhcdWAEyv.bmp", "DR3ueZAEEW"));
        mappings.put("sigma/viewport.png", new DecodeTexture("TZe3hZpXwbcJopbgV8it.bmp", "rj8rXSW67G"));
        mappings.put("sigma/warning.png", new DecodeTexture("G4lVqvyTdcM3YflbIbBr.bmp", "B7x6IcpmBW"));
        mappings.put("sigmang/images/alt/active.png", new DecodeTexture("RuNKxXxTAbbU44AEG9CG.bmp", "Ysw20hrC6L"));
        mappings.put("sigmang/images/alt/cercle.png", new DecodeTexture("YeHA6b89vf0dKCs54LUD.bmp", "DxKTNfMGj9"));
        mappings.put("sigmang/images/alt/errors.png", new DecodeTexture("O2CZHG4xl1nKCHUeX80Q.bmp", "BkOVqNWcmB"));
        mappings.put("sigmang/images/alt/img.png", new DecodeTexture("E0n4yoq9GOUwqaYEDZ7g.bmp", "tXKydLiCX1"));
        mappings.put("sigmang/images/alt/man.png", new DecodeTexture("tCnG6uSEUbBGA58tiJPj.bmp", "nUaHL4jZCY"));
        mappings.put("sigmang/images/alt/select.png", new DecodeTexture("fZ4Y8obsxFdBX1lMhS7s.bmp", "RsbBTGlu7m"));
        mappings.put("sigmang/images/alt/shadow.png", new DecodeTexture("V5mf1PmBqInP4oGm34AX.bmp", "rQtXxGWrLA"));
        mappings.put("sigmang/images/alt/skin.png", new DecodeTexture("V7F7zZcRMDZ4eJgJVHeV.bmp", "94KGR4TFsQ"));
        mappings.put("sigmang/images/arrow.png", new DecodeTexture("VrILgy9pcqkLJjsaZCgX.bmp", "tytsJaW2wl"));
        mappings.put("sigmang/images/back.png", new DecodeTexture("8hcbWBQ9uzdMWfx79MLK.bmp", "XRtLE7onqb"));
        mappings.put("sigmang/images/background.png", new DecodeTexture("izWSNGht9Z90opBXrh1j.bmp", "v84BQexeWo"));
        mappings.put("sigmang/images/blured.png", new DecodeTexture("fRs2EbKvPdgZhWZWeAm1.bmp", "gH00SO2Vdv"));
        mappings.put("sigmang/images/check.png", new DecodeTexture("x6qleztlOXOa0dWMp8na.bmp", "3kpnnF1drc"));
        mappings.put("sigmang/images/clickgui/color.png", new DecodeTexture("L8rTD3iJfpoNw7UhTDZ5.bmp", "7a6Ngd5l0B"));
        mappings.put("sigmang/images/clickgui/color2.png", new DecodeTexture("B1q44yxlF7rfoSHQBXMf.bmp", "yMIT4WDdz4"));
        mappings.put("sigmang/images/clickgui/disable.png", new DecodeTexture("nY7u0FOywJmdeOUomEWT.bmp", "RuJL7kG4he"));
        mappings.put("sigmang/images/clickgui/enable.png", new DecodeTexture("I5FJm8oEk4Db6iiyvVh4.bmp", "2q4hAgDpRP"));
        mappings.put("sigmang/images/deobf.png", new DecodeTexture("ma20liX8mavBb8VSHYif.bmp", "STG66eN5hE"));
        mappings.put("sigmang/images/dollar.png", new DecodeTexture("Uw46g8ZSkSOyoH5gMY2T.bmp", "TjclbI6vBb"));
        mappings.put("sigmang/images/dvd.png", new DecodeTexture("5TOnhmRuLsXUDKqDfAA7.bmp", "NYUNb885dI"));
        mappings.put("sigmang/images/esp/playermodelshadow.png", new DecodeTexture("Y4ldzHwc4p8aY7xH5Kuo.bmp", "ax5pFPOtbz"));
        mappings.put("sigmang/images/esp/shadow.png", new DecodeTexture("RW6pzJayyNr9qNMINKAe.bmp", "P2WKzHhLuq"));
        mappings.put("sigmang/images/foreground.png", new DecodeTexture("wLqmEDaZKnTaCAwfbiTb.bmp", "fS2KqQGAjq"));
        mappings.put("sigmang/images/gps.png", new DecodeTexture("X6jHU4AJ2fEo8U9ZPXfS.bmp", "CfyyZ1itZ9"));
        mappings.put("sigmang/images/heart.png", new DecodeTexture("kuGcQNaljijEF5g6eIxO.bmp", "lHle0BPQoW"));
        mappings.put("sigmang/images/jello/account.png", new DecodeTexture("ftUjQSONjpvjxtd2BrRW.bmp", "EhkL7JCShX"));
        mappings.put("sigmang/images/jello/dvd.png", new DecodeTexture("FgMJ4TmnvtQzjJfK4wzU.bmp", "1pQpZHxBlg"));
        mappings.put("sigmang/images/jello/floating_border.png", new DecodeTexture("nE0IrFzkaqPYfx5PFcWa.bmp", "wuW4HIxYxb"));
        mappings.put("sigmang/images/jello/floating_corner.png", new DecodeTexture("jw1QRKSdH6H9eCbP34Cu.bmp", "MHnwQ31xXq"));
        mappings.put("sigmang/images/jello/loading_indicator.png", new DecodeTexture("X7HKj3VsXwNFsSA1aEhh.bmp", "30s8B3ljDS"));
        mappings.put("sigmang/images/jello/logo_large.png", new DecodeTexture("juN1lHCkO6NR9VuWSNgf.bmp", "B7KoMwYesD"));
        mappings.put("sigmang/images/jello/logo_large@2x.png", new DecodeTexture("EBTmiG05faxZ6AnWauYu.bmp", "v682Asip8G"));
        mappings.put("sigmang/images/jello/options.png", new DecodeTexture("o15vtJ9ulXBYYRPER0Xn.bmp", "pamrmPIIgR"));
        mappings.put("sigmang/images/jello/search.png", new DecodeTexture("PlXuj4T5LoTvpUlzv0fc.bmp", "kJY1oAPVYT"));
        mappings.put("sigmang/images/jello/shadow_bottom.png", new DecodeTexture("R8EA1Kctc8fwCTsYhLAk.bmp", "C0kObQg5YQ"));
        mappings.put("sigmang/images/jello/shadow_corner.png", new DecodeTexture("LzXeuDN03O4aL5QELSQj.bmp", "7iUGCEQe2D"));
        mappings.put("sigmang/images/jello/shadow_corner_2.png", new DecodeTexture("IUeTijxt4YtumXqelQfW.bmp", "itQ1ho1KbR"));
        mappings.put("sigmang/images/jello/shadow_corner_3.png", new DecodeTexture("xImHDnzCw6mF2vGyJ97W.bmp", "xBL24W7WO8"));
        mappings.put("sigmang/images/jello/shadow_corner_4.png", new DecodeTexture("RK2udqFcn1nWnvt8iqxt.bmp", "yFH4uiBzOX"));
        mappings.put("sigmang/images/jello/shadow_left.png", new DecodeTexture("AMRn9naEZHrzmo2zwc69.bmp", "2f3ERktTKA"));
        mappings.put("sigmang/images/jello/shadow_right.png", new DecodeTexture("UrD9V8pUWHqZT94hQogn.bmp", "kbtfGZtsLc"));
        mappings.put("sigmang/images/jello/shadow_top.png", new DecodeTexture("nzQtWYIrf6mS9KQnsgpc.bmp", "dJnVo4HluK"));
        mappings.put("sigmang/images/jellobg.png", new DecodeTexture("kPkggGHbk6yUWtaMmFdW.bmp", "atIqqCbveG"));
        mappings.put("sigmang/images/jelloblur.png", new DecodeTexture("uef6px83TzbxVfQIz4KP.bmp", "xGRGRvvkKB"));
        mappings.put("sigmang/images/logo.png", new DecodeTexture("bhsyx8DpqkDFraDIE2xF.bmp", "kkhl78czXO"));
        mappings.put("sigmang/images/logo2.png", new DecodeTexture("DFi3wh1R0mnygrKnketR.bmp", "n6YFHUdEf3"));
        mappings.put("sigmang/images/mentalfrostbyte/mentalfrostbyte.png", new DecodeTexture("sjokWnitGtNtG6iWdwqL.bmp", "M9qZmUPYtI"));
        mappings.put("sigmang/images/mentalfrostbyte/sigma.png", new DecodeTexture("Ep4d8l7t1sr1GgTfNgnM.bmp", "vBQLzUF5Lg"));
        mappings.put("sigmang/images/mentalfrostbyte/sigma2.png", new DecodeTexture("8r7vMbNVwVupCOM09Kea.bmp", "nWDWwIXxsr"));
        mappings.put("sigmang/images/mentalfrostbyte/tomy.png", new DecodeTexture("XsBrZPdDw9v4WBXemsjC.bmp", "j3EMOHCKPU"));
        mappings.put("sigmang/images/middle.png", new DecodeTexture("wCPykmn1bsyL8RGr7pkS.bmp", "9wKzeyi4a0"));
        mappings.put("sigmang/images/options.png", new DecodeTexture("b0Zl4U4E68qWXi4jvqqt.bmp", "l5GDpPkibi"));
        mappings.put("sigmang/images/particles.png", new DecodeTexture("ETsSITzGA4FkkG5nwM1G.bmp", "bhhF09lEt9"));
        mappings.put("sigmang/images/particles2.png", new DecodeTexture("JiAHvYly5vI10c4e8P4f.bmp", "A84giJ2Fvc"));
        mappings.put("sigmang/images/shadow/loading_indicator.png", new DecodeTexture("mZEZ8V9p7PrSnZtsg0ZL.bmp", "1JVr93c3bY"));
        mappings.put("sigmang/images/shadow/shadow_bottom.png", new DecodeTexture("AJP37ono39OtW3DIeLUL.bmp", "LzptvpHfub"));
        mappings.put("sigmang/images/shadow/shadow_corner.png", new DecodeTexture("4V78ZrXxUhumqowNf7Rw.bmp", "n9s05Jw5GJ"));
        mappings.put("sigmang/images/shadow/shadow_corner_2.png", new DecodeTexture("3NQZhDl34FqR8UXD6h8A.bmp", "PTELQVZyts"));
        mappings.put("sigmang/images/shadow/shadow_corner_3.png", new DecodeTexture("YznRUlkBIK7T43Ja1q5J.bmp", "0CIOssQiHS"));
        mappings.put("sigmang/images/shadow/shadow_corner_4.png", new DecodeTexture("wpPsEvILaJWklLJ8UtOC.bmp", "B0nKNicxkp"));
        mappings.put("sigmang/images/shadow/shadow_left.png", new DecodeTexture("n9iEnIxMoo1PvOAetSg6.bmp", "tv7QFtMLmT"));
        mappings.put("sigmang/images/shadow/shadow_right.png", new DecodeTexture("BSwtkYvVUW0uyK4N4nIu.bmp", "mcVJkW2XPr"));
        mappings.put("sigmang/images/shadow/shadow_top.png", new DecodeTexture("H7fhRNMff40naXxUryjh.bmp", "SPyIwcxAmX"));
        mappings.put("sigmang/images/sigmalogo.png", new DecodeTexture("31RqWeK5LFvLcCg5cUqK.bmp", "5yppaGuKjj"));
        mappings.put("sigmang/images/sigmalogo2.0.png", new DecodeTexture("puBSfOoaLWpMzCGOIy9N.bmp", "0r0vZYvYEM"));
        mappings.put("sigmang/images/sigmalogoOld.png", new DecodeTexture("Aq5xIrCHbBGCT22Pc2vG.bmp", "kgx4f5Z9Jd"));
        mappings.put("sigmang/images/snowflake.png", new DecodeTexture("DIrJB9cJa2YcKhETOkqC.bmp", "pwBkQ1HpFQ"));
        mappings.put("sigmang/images/star.png", new DecodeTexture("CzyoArpPP5aitWabadeo.bmp", "xRaVOfwwQH"));
        mappings.put("sigmang/images/target.png", new DecodeTexture("v75qayBqaoncypU9GBX3.bmp", "tf2z67yQu8"));
        mappings.put("sigmang/images/warning.png", new DecodeTexture("xhYaJXJtcA9H71v0tT0x.bmp", "0wDc5NMVoh"));
        mappings.put("sigmang/images/watermark1.png", new DecodeTexture("979LLHvzi2lIz2vqtgV4.bmp", "nvtgYNkfA5"));
        mappings.put("sigmang/images/yuankong.png", new DecodeTexture("Sw50S2vDINIJDm7gyR2Z.bmp", "6Sdy4ZuvcK"));
    }
}
