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
        list.write();
    }

    public void remove(User user) {
        list.remove(user.getId());
    }

    public void strike(User user, String reason) {
        StrikedMember offender;
        if (list.contains(user.getId()) && !list.getMember(user.getId()).isStriked()) {
            System.out.println("found in list");
            offender = new StrikedMember(user.getName(), user.getId(), reason);
            list.remove(user.getId());
            list.add(offender);
        }
        else {
            offender = (StrikedMember)list.getMember(user.getId());
            offender.reasons.add(reason);
        }
        if(offender.reasons.size() == 3) user.addRole(api.getRolesByName("Chat Restrict").iterator().next());
    }

    public void unMute(User user) {
        user.removeRole(api.getRolesByName("Chat Restrict").iterator().next());
    }

    public Member get(User user) {
        return list.getMember(user.getId());
    }

    public void write() {
        list.write();
    }

    public void read() {
        list.read();
    }

    /**
     * Prints list of members
     * @param channel
     */
    public void viewMembers(Channel channel) {
        channel.asTextChannel().get().sendMessage("Viewing Members...");
        channel.asTextChannel().get().sendMessage(list.print());
    }

    /**
     * Prints statistics of members
     * @param channel
     */
    public void viewStats(Channel channel) {
        channel.asTextChannel().get().sendMessage("Viewing Stats...");
        channel.asTextChannel().get().sendMessage(list.printStats());
    }

    /**
     * Adds member to list on join
     */
    public void memberJoin(User user) {
        Member member = new Member(user.getName(), user.getId());
        member.setCreationDate(user.getCreationTimestamp().getEpochSecond());
        list.add(member);
        list.write();
    }

    /**
     * Removes member from list on leave
     */
    public void memberLeave(User user) {
        list.remove(user.getId());
        list.write();
    }

    public void countUp(User user) {
        list.getMember(user.getId()).increase();
    }

}
