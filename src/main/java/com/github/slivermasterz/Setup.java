package com.github.aqml15.discordbot;

import java.awt.Color;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerTextChannelBuilder;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;

public class Setup {
	
	public Setup(DiscordApi api) {
		Server server = api.getServers().iterator().next();
		
		// Creates Mod role if none exists
        Permissions modPermissions = new PermissionsBuilder().setAllAllowed().build();
        Permissions noPermissions = new PermissionsBuilder().setAllDenied().build();
        Role mod = null;
        
        if (server.getRolesByName("Mod").size() == 0) {
        	mod = server.createRoleBuilder().setName("Mod").setPermissions(modPermissions).setColor(Color.CYAN).create().join();
            mod.addUser(server.getOwner(), "Owner is Moderator");
        }
        
        // Creates Private Report Channel if none exists
        if (server.getChannelsByName("reports").size() == 0) {
        	ServerTextChannelBuilder reports = server.createTextChannelBuilder();
        	System.out.println(server.getTextChannels());
			reports.setName("reports");
			reports.addPermissionOverwrite(mod, modPermissions);
			reports.addPermissionOverwrite(server.getEveryoneRole(), noPermissions);
			reports.create().join();
        	System.out.println("Report Channel successfully created...");
        }
	}
	
}
