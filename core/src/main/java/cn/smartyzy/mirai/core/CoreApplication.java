package cn.smartyzy.mirai.core;

import net.mamoe.mirai.console.plugins.PluginBase;

public class CoreApplication extends PluginBase {

    @Override
    public void onEnable() {
        getLogger().info("smartyzy-mirai Plugin loadeding...");
    }

    @Override
    public void onLoad() {
        getLogger().info("smartyzy-mirai Plugin loaded!");
    }
}
