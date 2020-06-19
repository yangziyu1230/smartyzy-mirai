package cn.smartyzy.mirai.yellopicture;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dreamlu.mica.http.HttpRequest;
import net.dreamlu.mica.http.LogLevel;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.utils.MiraiLogger;
import okhttp3.MultipartBody;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class YelloPictureListener implements Consumer<GroupMessageEvent> {

    public static MiraiLogger log = null;

    public static final AtomicBoolean isRunnung = new AtomicBoolean(false);

    public static final AtomicReference<Long> runningQQ = new AtomicReference<>(0L);

    public static final AtomicReference<Long> runningGroup = new AtomicReference<>(0L);

    public static final AtomicReference<String> pic64 = new AtomicReference<>("");

    public static int step = 0;

    public static final String[] CMD = {
            "涩图", "r18"
    };


    @Override
    public void accept(GroupMessageEvent groupMessageEvent) {
        Group group = groupMessageEvent.getGroup();
        String message = groupMessageEvent.getMessage().toString();

        long groupId = group.getId();

        long qq = groupMessageEvent.getSender().getId();
        log = groupMessageEvent.getBot().getLogger();

        stepCmd(group, groupId, qq, message, groupMessageEvent);

    }

    private void stepCmd(Group group, long groupId, long qq, String message, GroupMessageEvent groupMessageEvent) {
        boolean isCMD = false;
        for (String s : CMD) {
            if (message.contains(s)) {
                isCMD = true;
                if (s.equals("r18")) {
                    step = 1;
                } else {
                    step = 0;
                }
                break;
            }
        }
        if (!isCMD) {
            return;
        }

        isRunnung.set(true);
        runningQQ.set(qq);
        runningGroup.set(groupId);

        log.info("获取涩图");
        stepGetPic(group, groupId, qq, message, groupMessageEvent);
    }

    private void stepGetPic(Group group, long groupId, long qq, String message, GroupMessageEvent groupMessageEvent) {
        log.info("开始获取涩图Http Request请求");

        String response = HttpRequest.get("https://api.lolicon.app/setu/")
                .setHeader("Content-Type", "multipart/form-data;")
                .log(LogLevel.BASIC)
                .query("r18", (step == 1 ? "1" : "0"))
                .query("apikey", "013312155eec4d7a7278c2")
                .execute()
                .asString();

        log.info(response);
        log.info("HttpRequest End");
        sendMsg(group, groupId, qq, message, response, groupMessageEvent);
    }

    private void sendMsg(Group group, long groupId, long qq, String message, String response, GroupMessageEvent groupMessageEvent) {
        JSONObject json = JSONObject.parseObject(response);
        JSONArray data = json.getJSONArray("data");
        String picUrl = data.getJSONObject(0).getString("url");

        URL url = null;

        try {
            url = new URL(picUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Image image = groupMessageEvent.getGroup().uploadImage(url);
        final String imageId = image.getImageId();
        final Image fromId = MessageUtils.newImage(imageId);

        group.sendMessage(image);

    }

}
