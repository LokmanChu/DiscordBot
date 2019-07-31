package com.github.aqml15.discordbot;

import java.io.IOException;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class Main {

    public static void main(String[] args) throws IOException {
        // Insert bot's token here
        String token = "NTkwOTk4MjUxMzIzNTIzMTA5.XQ6QbA.TJwthwrr5vYmMAXNQPiflS4NKM8";
        
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join(); //login
        System.out.println(api.createBotInvite());
        System.out.println("Logged in!");
        
        // DiscordBot Listeners
        api.addListener(new ReportCommand());
        api.addListener(new MembersListCommands());
        api.addListener(new Spam());
        api.addListener(new SpamCommands());
        
        // Setup
        @SuppressWarnings("unused")
		Setup su = new Setup(api);
        MembersList list = new MembersList();
        @SuppressWarnings("unused")
		SpamList slist = new SpamList();
        Member admin = new Member(api.getOwner().getNow(null).getName(), api.getOwnerId());
        list.add(admin);
    }
}
