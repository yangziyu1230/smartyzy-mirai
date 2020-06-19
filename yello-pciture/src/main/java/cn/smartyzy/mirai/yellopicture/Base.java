package cn.smartyzy.mirai.yellopicture;

import net.mamoe.mirai.console.plugins.PluginBase;
import net.mamoe.mirai.message.GroupMessageEvent;

public class Base extends PluginBase {
    @Override
    public void onEnable() {
        getLogger().info("yello-picture Plugin loadeding...");
    }

    @Override
    public void onLoad() {
        getLogger().info("yello-picture Plugin loaded!");
        getEventListener().subscribeAlways(GroupMessageEvent.class, new YelloPictureListener());
    }
}
