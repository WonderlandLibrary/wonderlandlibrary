package info.sigmaclient.sigma.scripts;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.config.QLExpressRunStrategy;
import com.ql.util.express.config.whitelist.CheckerFactory;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.ConfigManager;
import info.sigmaclient.sigma.event.Event;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static info.sigmaclient.sigma.modules.Module.mc;

public class ScriptModuleManager {
    CodeParser codeParser = new CodeParser();
    public File scriptDir = new File(ConfigManager.normalDir, "/scripts");
    ScriptFunction scriptFunction = new ScriptFunction();
    public void initScript(ScriptModule script) {
        try {
            QLExpressRunStrategy.setCompileWhiteCheckerList(Arrays.asList(
                    CheckerFactory.must(Date.class),
                    CheckerFactory.assignable(List.class),
                    CheckerFactory.assignable(String.class),
                    CheckerFactory.assignable(Double.class),
                    CheckerFactory.assignable(Float.class),
                    CheckerFactory.assignable(Character.class),
                    CheckerFactory.assignable(ScriptFunction.class),
                    CheckerFactory.assignable(Integer.class),
                    CheckerFactory.assignable(System.class)
            ));
            QLExpressRunStrategy.setForbidInvokeSecurityRiskMethods(true);
            QLExpressRunStrategy.setMaxArrLength(100);

            ExpressRunner runner = new ExpressRunner();
            DefaultContext<String, Object> context = new DefaultContext<>();
            runner.addFunctionOfServiceMethod("strafe", scriptFunction, "strafe",
                    new Class[]{Double.class}, null);
            runner.addFunctionOfServiceMethod("jump", scriptFunction, "jump",
                    new Class[]{}, null);
            runner.addFunctionOfServiceMethod("setMotionX", scriptFunction, "setMotionX",
                    new Class[]{Double.class}, null);
            runner.addFunctionOfServiceMethod("setMotionY", scriptFunction, "setMotionY",
                    new Class[]{Double.class}, null);
            runner.addFunctionOfServiceMethod("setMotionZ", scriptFunction, "setMotionZ",
                    new Class[]{Double.class}, null);
            runner.addFunctionOfServiceMethod("setMotion", scriptFunction, "setMotion",
                    new Class[]{Double.class, Double.class, Double.class}, null);

            context.put("player", mc.player);
            String express = script.script;
            runner.loadExpress(express);
            script.runner = runner;
            script.context = context;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void loadAllScript(){
        for(File f : Objects.requireNonNull(scriptDir.listFiles())){
            if(f.isFile() && f.getName().endsWith(".txt")){
                System.out.println("Load " + f.getPath());
                String n = ConfigManager.readConfigData(f.toPath());
                try{
                    codeParser.engine.eval(n);
                    addModule(
                            (String) codeParser.engine.eval("name"),
                            (String)codeParser.engine.eval("describe"),
                            (String) codeParser.engine.eval("cate"),
                            n
                    );
                }catch (Exception e){
                    System.out.println(f.getName() + " | error: " + e.getMessage());
                }
            }
        }
    }
    public void addModule(String name, String desc, String cate, String value) {

        String error = "No error";
        try {
            ScriptModule scriptModule = new ScriptModule(
                    name,
                    Category.valueOf(cate),
                    desc,
                    value
            );
            initScript(scriptModule);
            SigmaNG.getSigmaNG().moduleManager.registerModule(scriptModule);
        }catch (Exception e){
            error = e.getMessage();
        }
        System.out.println("Register module: " + name);
        System.out.println("Error message: " + error);
    }
}
