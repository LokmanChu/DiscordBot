package com.github.aqml15.discordbot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class Main {

    public static void main(String a[]) {
        //Insert bot's token here
        String token = "NTkwOTk4MjUxMzIzNTIzMTA5.XQ6QbA.TJwthwrr5vYmMAXNQPiflS4NKM8";
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
        System.out.println(api.createBotInvite());

        new EventListener(api);
    }
}
