package com.github.slivermasterz;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class Main {

    public static void main(String[] args) {
        //Insert bot's token here
        String token = "";

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join(); //login

        api.addMessageCreateListener(event -> {
            if (event.getMessage().getContent().equalsIgnoreCase("!ping")) {
                event.getChannel().sendMessage("Pong!");
            }
        });
    }
}
