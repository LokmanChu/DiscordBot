package com.github.slivermasterz;

import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.javacord.api.event.server.member.ServerMemberLeaveEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;
import org.javacord.api.listener.server.member.ServerMemberLeaveListener;

public class MembersListCommands extends MembersList implements MessageCreateListener, ServerMemberJoinListener, ServerMemberLeaveListener {

	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		// Upon receiving '!viewMembers' command, sends list of members
        if (event.getMessageContent().equalsIgnoreCase("!viewMembers")) {
        	event.getChannel().sendMessage("Members List Requested...");
        	event.getChannel().sendMessage(print());
        }	
	}

	@Override
	public void onServerMemberJoin(ServerMemberJoinEvent event) {
		Member member = new Member(event.getUser().getName(), event.getUser().getId());
		add(event.getUser().getId(), member);
	}

	@Override
	public void onServerMemberLeave(ServerMemberLeaveEvent event) {
		Member member = new Member(event.getUser().getName(), event.getUser().getId());
		remove(member);		
	}
	
}
