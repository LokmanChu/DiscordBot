package com.github.aqml15.discordbot;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.javacord.api.event.server.member.ServerMemberLeaveEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;
import org.javacord.api.listener.server.member.ServerMemberLeaveListener;

public class MembersListCommands extends MembersList implements MessageCreateListener, ServerMemberJoinListener, ServerMemberLeaveListener {
	
	private static final long serialVersionUID = -921260427607624568L;

	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		// TODO Auto-generated method stub
		// Upon receiving '!viewMembers' command, sends list of members
        if (event.getMessageContent().equalsIgnoreCase("!viewMembers")) {
        	event.getChannel().sendMessage("Members List Requested...");
        	event.getChannel().sendMessage(print());
        }
        
        // Upon receiving '!saveList' command, saves list of members
        if (event.getMessageContent().equalsIgnoreCase("!saveList")) {
        	event.getChannel().sendMessage("Members List Saving...");
        	try {
        		FileOutputStream fileOut = new FileOutputStream(reportsPath);
        		ObjectOutputStream out = new ObjectOutputStream(fileOut);
        		out.writeObject(MembersList.treeMap);
        		out.close();
        		fileOut.close();
        		System.out.println("Members List was saved into " + reportsPath);
        	} catch (IOException i) {
        		System.out.println("Save failed...");
        		i.printStackTrace();
        	}
        }       		
	}

	@Override
	public void onServerMemberJoin(ServerMemberJoinEvent event) {
		// TODO Auto-generated method stub
		add(event.getUser().getName());
    	
    	try {
    		FileOutputStream fileOut = new FileOutputStream(reportsPath);
    		ObjectOutputStream out = new ObjectOutputStream(fileOut);
    		out.writeObject(MembersList.treeMap);
    		out.close();
    		fileOut.close();
    		System.out.println("Members List was saved into " + reportsPath);
    	} catch (IOException i) {
    		System.out.println("Save failed...");
    		i.printStackTrace();
    	}		
	}

	@Override
	public void onServerMemberLeave(ServerMemberLeaveEvent event) {
		// TODO Auto-generated method stub
		remove(event.getUser().getName());		
	}
	
}
