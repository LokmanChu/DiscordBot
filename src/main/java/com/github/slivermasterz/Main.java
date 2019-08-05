package com.github.slivermasterz;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.util.Arrays;

public class Main {

    public static void main(String a[]) {
        //Insert bot's token here
        String token = "NTk3NTI0OTYyMDY4MjY3MDYy.XSUhcQ.tLlv7BbO7Qzpl5spl4EfMAkw_gM";
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
        System.out.println(api.createBotInvite());

        new EventListener(api);
    }
}
