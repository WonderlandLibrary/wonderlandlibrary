package tech.atani.client.feature.module.impl.chat;

import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.value.impl.StringBoxValue;
import tech.atani.client.listener.event.game.KilledPlayerEvent;
import tech.atani.client.listener.radbus.Listen;

import java.security.SecureRandom;

@ModuleData(name = "KillInsult", description = "Insults player after you killed them", category = Category.COMBAT)
public class KillInsult extends Module {

    private final StringBoxValue language = new StringBoxValue("Languge", "Which language to insult in?", this, new String[]{"English", "Czech"});

    private static final String[] EN_MESSAGES = new String[]{"Is this the best you niggers can PvP?",
            "Go drown in your own salt, %s",
            "Kids like you are the inspiration of birth control, %s",
            "Your brain is a fucking fax machine, %s",
            "Wait, you guys cant fly?",
            "Toxic nigger",
            "Go back to roblox where you belong, you degenerate 6 year old kid",
            "You rush because you're bad and you want the satisfaction of one kill before dying to a noob, %s",
            "Hey %s, check out Kellohylly on youtube!",
            "Your ass is jealous of how much shit is coming trough your mouth, %s",
            "Wow, you died to a fucking free client, %s",
            "%s, r u mad?",
            "black lives matter, amiright %s",
            "%s go eat estrogen femtard",
            "%s go eat estrogen tranny",
            "How did %s even hit the launch game button?",
            "report me %s, I'm really scared",
            "why is this fat retard %s begging me to turn off my hacks",
            "sorry %s, this bypass value is exclusive",
            "%s seriously? go back to cubecraft monkey brain",
            "Stop crying %s or ill put you back in your cage",
            "Did ur parents ask you to run away, %s",
            "%s I'd tell you to uninstall, but your aim is so bad you wouldn't hit the button",
            "You do be lookin' kinda bad at the game, %s", "Did someone leave your cage open %s?",
            "%s you were the inspiration for birth control",
            "Is being in the spectator mode fun, %s?",
            "%s you're the type of guy to quickdrop irl",
            "%s got an F on the iq test",
            "I understand why your parents abused you, %s",
            "Do you practice being this bad, %s?",
            "hi my name is %s and my iq is -420!",
            "%s's aim is sponsored by Parkinson's",
            "%s go take a long walk on a short bridge",
            "%s probably plays fortnite lmao",
            "%s, you really like taking L's",
            "%s drown in your own salt",
            "%s, I'm not saying you're worthless, but i'd unplug ur lifesupport to charge my phone",
            "%s, could you please commit not alive?",
            "%s I don't cheat, you just need to click faster",
            "%s I speak english not your gibberish",
            "Your mom do be lookin' kinda black doe, %s",
            "Hey look! It's a fortnite player %s",
            "Need some pvp advice? %s.",
            "%s, do you really like dying this much?",
            "%s probably reported me",
            "%s you're the type to get 3rd place in a 1v1.",
            "%s how does it feel to get stomped on?",
            "%s, the type of guy to use sigma",
            "%s that's a #VictoryRoyale! better luck next time!",
            "lol %s probably speaks dog eater",
            "%s is a fricking monkey (black person)",
            "%s be like: ''I'm black and this a robbery''",
            "%s, even your mom is better than you in this game",
            "%s go back to fortnite you degenerate",
            "%s, were condoms named after you?",
            "%s your iq is that of a steve",
            "%s go commit stop breathing plz",
            "%s, your parents abandoned you, then the orphanage did the same",
            "%s probably bought sigma premium",
            "%s probably got an error on his hello world program lmao",
            "%s how'd you hit the download button with that aim",
            "Someone in 1940 forgot to gas you, %s :)",
            "%s, did your dad go get milk and never return?",
            "%s you died in a block game",
            "%s thinks that his ping is equal to his iq.",
            "%s stop eating dogs",
            "if the body is 70% water then how is %s's body 100% salt?",
            "%s's got dropped him on his head by his parents",
            "%s doesn't have parents L",
            "how are you so bad? im losing brain cells while watching you play",
            "some kids were dropped at birth, but %s got thrown at the wall.",
            "%s black"};

    private static final String[] CZ_MESSAGES = new String[] {
            "Tohle je to nejlepší, co vy negři umíte?",
            "Jdi se utopit ve vlastní soli, %s",
            "Děti jako ty jsou inspirací pro kondomy, %s",
            "Tvůj mozek je zasraná kopírka, %s",
            "Počkat, vy neumíte létat?",
            "Toxickej negr",
            "Vrať se do robloxu kde patříš ty zdegenerovaný šestiletý děcko",
            "Rushuješ, protože jseš špatný a chceš mít uspokojení z jednoho killu před tím než tě zabije noobka, %s",
            "Tvoje prdel ti závidí kolik sraček ti teče z huby, %s",
            "Páni, ty jsi chcípnul ve fightu proti zdarma clientu, %s",
            "%s, jseš naštvanej?",
            "černí lidé jsou lidé, nonenmámpravdu %s",
            "%s jdi žrát estrogen femtarde",
            "%s jdi žrát estrogen transko",
            "Jak dokázal %s vůbec zapnout minecraft?",
            "jj reportni mě %s, mám opravdu strach",
            "proč mě ten tlustý retard %s prosí, abych vypnul cheaty?",
            "Přestaň brečet %s, nebo tě zavřu zpátky do klece",
            "zeptali se tě rodiče aby si utekl, %s?",
            "%s řekl bych ti, abys to odinstaloval, ale máš tak špatný aim že by ses ani netrefil na 'odinstalovat'",
            "Nechal ti někdo otevřenou klec %s?",
            "%s byl inspirací pro antikoncepci",
            "Být ve spectator módu je zábava že, %s?",
            "%s dostal pětku z testu IQ",
            "Chápu, proč tě rodiče mlátili, %s",
            "Trénuješ být takhle špatný, %s?",
            "Ahoj, jmenuji se %s a moje IQ je -420!",
            "Cíl %s je sponzorován Parkinsonem",
            "%s jdi se dlouze projít po krátkém mostě",
            "%s pravděpodobně hraje fortnite lmao",
            "%s, ty opravdu rád bereš Lka",
            "%s se utopí ve vlastní soli",
            "%s necheatuju, jen musíš klikat rychleji",
            "Máš černou mámu, %s",
            "Potřebuješ poradit s pvp? %s",
            "%s, opravdu tak rád umíráš?",
            "%s mě pravděpodobně nahlásil",
            "%s, ty jsi ten typ, který v 1v1 dá třetí místo.",
            "lol, %s pravděpodobně mluví pso-žroutsky",
            "%s je zasraná opice (negr)",
            "%s, i tvoje máma je v téhle hře lepší než ty",
            "%s, byly po tobě pojmenovány kondomy?",
            "%s, tvoji rodiče tě opustili, pak i dětský domov",
            "%s pravděpodobně dostal chybu ve svém hello world programu lmao",
            "Někdo ve čtyrycítkách tě zapomněl zaplynovat, %s :)",
            "%s, šel tvůj táta pro mléko a už se nevrátil?",
            "%s, zemřel jsi v kostičkové hře pro děti",
            "%s si myslí, že jeho ping se rovná jeho iq.",
            "%s přestaň jíst psy",
            "%s nemá rodiče L",
            "některé děti byly upuštěny při porodu, ale %s hodili na zeď.",
            "%s je černej",
    };

    private SecureRandom secureRandom = new SecureRandom();

    @Listen
    public void onKilledPlayer(KilledPlayerEvent killedPlayerEvent) {
        String[] targetArray = null;
        switch (this.language.getValue()) {
            case "English":
                targetArray = EN_MESSAGES;
                break;
            case "Czech":
                targetArray = CZ_MESSAGES;
        }
        if(killedPlayerEvent.isPlayerFound()) {
            int rnd = secureRandom.nextInt(targetArray.length);
            String message = targetArray[rnd];
            message.replace("%s", killedPlayerEvent.getPlayer());
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}
