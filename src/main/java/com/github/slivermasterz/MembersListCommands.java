package com.github.slivermasterz;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.user.User;

public class MembersListCommands {

	DiscordApi api;
	MembersList list;

	/**
	 * Init
	 * @param api
	 */
	public MembersListCommands(DiscordApi api) {
		this.api = api;
		list = new MembersList();
	}

	public void add(User user) {
		list.add(new Member(user.getName(), user.getId()));
	}

	public void ban(User user, String reason) {
		StrikedMember offender = new StrikedMember(user.getName(), user.getId(), reason);
		if (list.contains(user.getId())) {
			list.remove(list.getMember(user.getId()));
		}
		list.add(offender);
	}

	/**
	 * Prints list of members
	 * @param channel
	 */
	public void viewMembers(Channel channel) {
		channel.asTextChannel().get().sendMessage(list.print());
		channel.asTextChannel().get().sendMessage("no");
	}

	/**
	 * Prints statistics of members
	 * @param channel
	 */
	public void viewStats(Channel channel) {
		channel.asTextChannel().get().sendMessage(list.printStats());
	}

	/**
	 * Adds member to list on join
	 */
	public void memberJoin(User user) {
		Member member = new Member(user.getName(), user.getId());
		list.add(member);
		list.write();
	}

	/**
	 * Removes member from list on leave
	 */
	public void memberLeave(User user) {
		Member member = new Member(user.getName(), user.getId());
		list.remove(member);
		list.write();
	}

}
