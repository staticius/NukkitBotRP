package net.minetron;

import me.dilley.MineStat;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends ListenerAdapter {

    private static final String BOT_TOKEN = "token";
    private static final String NUKKIT_SERVER_IP = "serverip";
    private static final int NUKKIT_SERVER_PORT = 19132;

    public static void main(String[] args) throws LoginException {

        JDABuilder.createDefault(BOT_TOKEN)
                .addEventListeners(new Main())
                .setActivity(Activity.playing("Hizmetler Kontrol Ediliyor..."))
                .build();

    }

    @Override
    public void onReady(ReadyEvent event) {
        super.onReady(event);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    int playerCount = getPlayerCount(NUKKIT_SERVER_IP, NUKKIT_SERVER_PORT);
                    event.getJDA().getPresence().setActivity(Activity.playing(playerCount + " aktif oyuncu Sunucuda"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 10000);
    }

    private int getPlayerCount(String host, int port) throws IOException {
        try {
            MineStat mineStat = new MineStat(host, port);
            if (mineStat.isServerUp()) {
                return mineStat.getCurrentPlayers();
            } else {
                throw new IOException("Sunucu Kapalı");
            }
        } catch (IOException e) {
            throw new IOException("Sunucuya Bağlanılamadı.", e);
        }
    }
}