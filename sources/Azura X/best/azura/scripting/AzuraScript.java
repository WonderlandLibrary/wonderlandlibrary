package best.azura.scripting;

import fr.ducouscous.csl.running.Script;
import fr.ducouscous.csl.running.variable.values.Function;

import java.util.ArrayList;
import java.util.HashMap;

public class AzuraScript extends Script {
    public AzuraScript(final Script from) {
        from.getGlobalTable().getValues().forEach(this::setGlobalValue);
        setGlobalFunction(from.getGlobalFunction());
    }
}
