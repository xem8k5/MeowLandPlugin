package ru.meowland.discord;

import arc.Core;
import arc.Events;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.net.Administration;
import org.yaml.snakeyaml.Yaml;
import ru.meowland.config.Config;

import javax.net.ssl.HttpsURLConnection;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

public class PlayerJoin {
    private String webhookUrl;
    private Map<String, Object> obj;

    public void join(){
        Config conf = new Config();
        Yaml yml = new Yaml();
        obj = yml.load(String.valueOf(Core.settings.getDataDirectory().child("config.yml").readString()));
        webhookUrl = obj.get("webhook_url").toString();
        Events.on(EventType.PlayerConnect.class, event ->{
            Player player = event.player;
            Administration.Config.showConnectMessages.set(false);
            Call.sendMessage("[lime]Игрок [#B](" + player.name + "[#B]) [lime]зашёл");
            String jsonBrut = "";
            jsonBrut += "{\"embeds\": [{"
                    + "\"title\": \""+ player.name +"\","
                    + "\"description\": \"Зашёл\","
                    + "\"color\": 3211008"
                    + "}],"
                    +"\"username\": \""+ player.name +"\","
                    + "\"avatar\": \"https://media.discordapp.net/attachments/981128238766112808/981527456357974056/unknown.png\""
                    + "}";
            try {
                URL url = new URL(webhookUrl);
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.addRequestProperty("Content-Type", "application/json");
                con.addRequestProperty("meow", "nya");
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                OutputStream stream = con.getOutputStream();
                stream.write(jsonBrut.getBytes());
                stream.flush();
                stream.close();
                con.getInputStream().close();
                con.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


}
