package com.github.aqml15.discordbot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class Main {

    public static void main(String[] args) {
        //Insert bot's token here
        String token = "NTkwOTk4MjUxMzIzNTIzMTA5.XQ6QbA.TJwthwrr5vYmMAXNQPiflS4NKM8";

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join(); //login
        System.out.println(api.createBotInvite());
        System.out.println("Logged in!");

        api.addListener(new ReportCommand());
        api.addListener(new MembersListCommands());
        //System.out.println(api.getServers().iterator().next().getMembersByName("aqml15"));
        
        @SuppressWarnings("unused")
		Setup su = new Setup(api);
        
        MembersList list = new MembersList();
        if (list.lastSave()) list.add(api.getOwner().toString());
        
    }
}
