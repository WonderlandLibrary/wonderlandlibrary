package today.flux.addon.api;

import today.flux.addon.FluxAPI;
import today.flux.addon.api.command.AddonCommand;
import today.flux.addon.api.module.AddonModule;

import java.util.List;

public abstract class FluxAddon {
    /**
     *
     * @return 插件名称
     */
    public abstract String getAPIName();

    /**
     *
     * @return 插件版本号
     */
    public abstract float getVersion();

    /**
     *
     * @param api FluxAPI对象
     */
    public abstract void initAPI(FluxAPI api);

    /**
     *
     * @return 获取作者信息
     */
    public abstract String getAuthor();
    /**
     *
     * @return 获取Module列表
     */
    public abstract List<AddonModule> getModules();

    /**
     *
     * @return 获取Module列表
     */
    public abstract List<AddonCommand> getCommands();
}
