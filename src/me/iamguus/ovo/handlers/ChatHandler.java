package me.iamguus.ovo.handlers;

import me.iamguus.ovo.utils.ConfigUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by Guus on 5-1-2016.
 */
public class ChatHandler {

    private static ChatHandler instance;

    FileConfiguration messages = ConfigUtil.get().getMessages();

    public String sendMessage(String string) {
        String noColor = messages.getString(string);
        String color = ChatColor.translateAlternateColorCodes('&', noColor);
        return color;
    }

    public static ChatHandler get() {
        if (instance == null) {
            instance = new ChatHandler();
        }

        return instance;
    }
}
