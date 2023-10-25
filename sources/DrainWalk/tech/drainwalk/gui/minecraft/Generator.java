package tech.drainwalk.gui.minecraft;

import tech.drainwalk.utility.math.MathUtility;

import java.util.Random;

public class Generator {
    public static String generateName() {
        String name;
        String prefix = getRandomPartOfName(PREFIX);
        String base = getRandomPartOfName(BASE);
        String suffix = getRandomPartOfName(SUFFIX);

        int baseRandom = MathUtility.randomNumber(0, 7);
        switch (baseRandom) {
            case 0:
                base = base.replaceFirst("i", "1");
                base = base.replaceFirst("I", "1");
                break;
            case 1:
                base = base.replaceFirst("e", "3");
                base = base.replaceFirst("E", "3");
                break;
            case 2:
                base = base.replaceFirst("a", "4");
                base = base.replaceFirst("A", "4");
                break;
            case 3:
                base = base.replaceFirst("t", "7");
                base = base.replaceFirst("T", "7");
                break;
            case 4:
                base = base.replaceFirst("o", "0");
                base = base.replaceFirst("O", "0");
                break;
            case 5:
                base = base.replaceFirst("s", "5");
                base = base.replaceFirst("S", "5");
                break;
            case 6:
                if (!prefix.equals("DATE_OF_BIRTH")) {
                    base = MathUtility.randomNumber(0, 9) + base;
                }
                if (prefix.equals("DATE_OF_BIRTH") && !suffix.equals("DATE_OF_BIRTH")) {
                    base = base + MathUtility.randomNumber(0, 9);
                }
                break;
        }


        if (!suffix.equals("DATE_OF_BIRTH")) {
            prefix = prefix.replaceFirst("DATE_OF_BIRTH", MathUtility.randomNumber(0, 9999) + "");
        }
        if (!prefix.equals("DATE_OF_BIRTH")) {
            suffix = suffix.replaceFirst("DATE_OF_BIRTH", MathUtility.randomNumber(0, 9999) + "");
        }
        int random = MathUtility.randomNumber(0, 2);
        switch (random) {
            case 0:
                prefix = prefix.replaceFirst("RANDOM", randomString(MathUtility.randomNumber(1, 3)) + "_");
                break;
            case 1:
                prefix = prefix.replaceFirst("RANDOM", randomString(MathUtility.randomNumber(1, 2)) + "__");
                break;
            case 2:
                prefix = prefix.replaceFirst("RANDOM", randomString(1) + "___");
                break;
        }
        random = MathUtility.randomNumber(0, 2);
        switch (random) {
            case 0:
                suffix = suffix.replaceFirst("RANDOM", "_" + randomString(MathUtility.randomNumber(1, 3)));
                break;
            case 1:
                suffix = suffix.replaceFirst("RANDOM", "__" + randomString(MathUtility.randomNumber(1, 2)));
                break;
            case 2:
                suffix = suffix.replaceFirst("RANDOM", "___" + randomString(1));
                break;
        }

        suffix = suffix.replaceFirst("DATE_OF_BIRTH", "_");
        prefix = prefix.replaceFirst("DATE_OF_BIRTH", "_");

        int suffixRandom = MathUtility.randomNumber(0, 6);
        switch (suffixRandom) {
            case 0:
                suffix = suffix.replaceFirst("i", "1");
                suffix = suffix.replaceFirst("I", "1");
                break;
            case 1:
                suffix = suffix.replaceFirst("e", "3");
                suffix = suffix.replaceFirst("E", "3");
                break;
            case 2:
                suffix = suffix.replaceFirst("a", "4");
                suffix = suffix.replaceFirst("A", "4");
                break;
            case 3:
                suffix = suffix.replaceFirst("t", "7");
                suffix = suffix.replaceFirst("T", "7");
                break;
            case 4:
                suffix = suffix.replaceFirst("o", "0");
                suffix = suffix.replaceFirst("O", "0");
                break;
            case 5:
                suffix = suffix.replaceFirst("s", "5");
                suffix = suffix.replaceFirst("S", "5");
                break;
        }
        int prefixRandom = MathUtility.randomNumber(0, 6);
        switch (prefixRandom) {
            case 0:
                prefix = prefix.replaceFirst("i", "1");
                prefix = prefix.replaceFirst("I", "1");
                break;
            case 1:
                prefix = prefix.replaceFirst("e", "3");
                prefix = prefix.replaceFirst("E", "3");
                break;
            case 2:
                prefix = prefix.replaceFirst("a", "4");
                prefix = prefix.replaceFirst("A", "4");
                break;
            case 3:
                prefix = prefix.replaceFirst("t", "7");
                prefix = prefix.replaceFirst("T", "7");
                break;
            case 4:
                prefix = prefix.replaceFirst("o", "0");
                prefix = prefix.replaceFirst("O", "0");
                break;
            case 5:
                prefix = prefix.replaceFirst("s", "5");
                prefix = prefix.replaceFirst("S", "5");
                break;
        }

        name = prefix + base;

        if (name.length() <= 12) {
            name = name + suffix;
        }
        if (name.length() <= 13) {
            int randomAddNumbers = MathUtility.randomNumber(0, 1);
            if (randomAddNumbers == 0) {
                name = name + "_" + randomString(MathUtility.randomNumber(1, 3));
            } else {
                name = randomString(MathUtility.randomNumber(1, 3)) + "_" + name;
            }
        }
        if (name.length() >= 16) {
            name = name.substring(0, MathUtility.randomNumber(12, 16));
        }
        return name;
    }

    private static String randomString(int w) {
        StringBuilder builder = new StringBuilder();
        char[] buffer = "qwertyuiopasdfghjklzxcvbnm1234567890".toCharArray();

        for (int i = 0; i < w; ++i) {
            Random rand = new Random();
            String s = new String(new char[]{buffer[rand.nextInt(buffer.length)]});
            builder.append(rand.nextBoolean() ? s : s.toUpperCase());
        }

        return builder.toString();
    }

    private static String getRandomPartOfName(String[] array) {
        int rnd = (new Random()).nextInt(array.length);
        return array[rnd];
    }


    private static String[] BASE = {
            "fipp",
            "bravlik",
            "sergey cheat",
            "6o6eP",
            "Albenix",
            "vitmoed",
            "st1vaha",
            "wine",
            "korper",
            "xalegioner",
            "sanchezj",
            "onej",
            "killah",
            "CLONNEX",
            "Rayzen",
            "deezy",
            "drainwalk",
            "skeetbeta",
            "Ivan",
            "Slava",
            "Aleksey",
            "Samir",
            "Murad",
            "Murat",
            "Oleg",
            "Leha",
            "Zhenya",
            "Zheka",
            "Evgen",
            "Evhen",
            "Evheniy",
            "Evgeniy",
            "Andrey",
            "Egor",
            "Egorka",
            "Rodion",
            "Anton",
            "Antoha",
            "Minyon",
            "Minion",
            "Boris",
            "Borya",
            "Taras",
            "Vitaliy",
            "Vitya",
            "Vlad",
            "Vladik",
            "Vadim",
            "Vadimka",
            "Misha",
            "Mihail",
            "Denis",
            "Dima",
            "Dimon",
            "Dimka",
            "Grisha",
            "Vlas",
            "Matvey",
            "Nikita",
            "Nikitka",
            "Felix",
            "Nefor",
            "Mark",
            "Makar",
            "Philip",
            "Filip",
            "Nikolay",
            "Daniil",
            "Danil",
            "Maksim",
            "Victor",
            "Vova",
            "Vladimir",
            "Timur",
            "Timoha",
            "Stepan",
            "Sergey",
            "Obama",
            "Max",
            "Maks",
            "Maksim",
            "Maxim",
            "Gosha",
            "Grisha",
            "Nazar",
            "Bodya",
            "Bogdan",
            "Stas",
            "Stepan",
            "Artur",
            "Arthur",
            "Anatoli",
            "Anatoliy",
            "Tolik",
            "Kiril",
            "Kirill",
            "BoTiK",
            "Eclipse",
            "Eclips",
            "Kiran",
            "Wendox",
            "Trust",
            "Patrik",
            "P4trik",
            "Patrick",
            "P4trick",
            "NooM",
            "Lakon",
            "Andrew",
            "Argento",
            "Argentoz",
            "Pcholkin",
            "P4olkin",
            "Smertnix",
            "Nurik",
            "Player",
            "Squad",
            "Moon",
            "Mo_on",
            "Mo_o_n",
            "Limonka",
            "Lemonka",
            "Limon",
            "Lemon",
            "Vendeta",
            "Alex",
            "Steve",
            "Sexy",
            "Mistik",
            "Sobaka",
            "Ryzen",
            "Intel",
            "AMD",
            "AMD_",
            "_AMD_",
            "_AMD",
            "Stalin",
            "TwiX",
            "Bandera",
            "Hilter",
            "Gilter",
            "Magamed",
            "Maga",
            "Kadirov",
            "Putin",
            "Zelensky",
            "Nord",
            "Brusko",
            "Posito",
            "Charon",
            "Sweet",
            "Angel",
            "Sanya",
            "Jocker",
            "Joker",
            "Bomj",
            "LeXuS",
            "NeXuS",
            "SeXuS",
            "TeXuS",
            "XeXuS",
            "KeXuS",
            "BeXuS",
            "Artist",
            "MeXuS",
            "Ksenia",
            "Aktobe",
            "Actobe",
            "Kazah",
            "Ethernal",
            "Eternal",
            "Xilarios",
            "Xilka",
            "Iuda",
            "Igor",
            "Bolshoy",
            "Rostik",
            "Matrix",
            "OpenGL",
            "Vulkan",
            "DirectX",
            "Deus2D",
            "Deus3D",
            "Deus4D",
            "Deus5D",
            "Deus6D",
            "Deus7D",
            "Deus8D",
            "Deus9D",
            "Sultan",
            "Clown",
            "Cloun",
            "Klown",
            "Kloun",
            "Astana",
            "Wild",
            "MORGEN",
            "Nigga",
            "Niga",
            "Knigga",
            "Kniga",
            "PuPoK",
            "Spider",
            "Kraken",
            "Kapitan",
            "Matros",
            "Rick",
            "Morty",
            "Bolshoy",
            "Delik",
            "Terpish",
            "CheZee",
            "Beast",
            "MrBeast",
            "Alfedov",
            "Tenon4ik",
            "DeLiFaN",
            "Deus",
            "Hackeri",
            "Hakeri",
            "Hacker",
            "Haker",
    };
    private static String[] PREFIX = {
            "DATE_OF_BIRTH",
            "RANDOM",
            "VIP_",
            "VPN_",
            "Sky",
            "Sky_",
            "We",
            "We_",
            "We__",
            "You",
            "You_",
            "The_",
            "My",
            "Mr",
            "Mr_",
            "Mrs_",
            "Mr__",
            "Me",
            "Me_",
            "My_",
            "Me__",
            "My__",
            "The",
            "The_",
            "zxc_",
            "LoL_",
            "LoX_",
            "X_",
            "Xx_",
            "Xx__",
            "XxX_",
            "_",
            "Kot_",
            "Cat_",
            "__",
            "___",
            "____",
    };
    private static String[] SUFFIX = {
            "DATE_OF_BIRTH",
            "RANDOM",
            "_LOL",
            "ME",
            "_ME",
            "__ME",
            "_zxc",
            "_YT",
            "_VIP",
            "_VPN",
            "_UwU",
            "_MC",
            "__MC",
            "__YT",
            "__TV",
            "_TV",
            "TV",
            "_",
            "_Kot",
            "_Cat",
            "_XxX",
            "_Xx",
            "__Xx",
            "__",
            "___",
            "____",
    };
}
