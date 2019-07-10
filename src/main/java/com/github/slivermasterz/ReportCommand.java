package com.github.slivermasterz;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageDecoration;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerTextChannelBuilder;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;

    public class ReportCommand {

        DiscordApi api;
        ArrayList<User> pendingReporters;

        public ReportCommand(DiscordApi api) {
            this.api = api;
            pendingReporters = new ArrayList<User>(4);
        }

        public void setupReportChannel() {
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

        public void sendReportPM(User author){
            try {
                pendingReporters.add(author);
                author.openPrivateChannel().get().sendMessage(
                        "To Report a User for breaking the rules, please reply to this DM with the following format:\n"
                                + "[!!! <userid to be reported> : <'Message breaking the rule'>]\n"
                                + "[Example: !!! JohnDoe : 'What the ****!']"
                );
            }
            catch(Exception e) {
                System.out.println("Report Failed!");
            }
        }

        public void addPMReport(User author, String message) {
            // Check if receiving message is DM, if so paste message into a private channel for review later
            Channel channel = api.getChannelsByName("reports").iterator().next();
            System.out.println("channel: " + channel + " ... " + channel.toString());
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String fullDate = dateFormat.format(date);
            String offender = message.split("\\s+")[1];
            String reportMessage = "";
            String[] collection = message.split("\\s+");
            for (int i = 3; i < collection.length; i++) {
                reportMessage = reportMessage + " " + collection[i];
            }
            offender = offender.substring(0, offender.length() - 1);

            if (message.startsWith("!!!", 0)) {
                System.out.println("NAME: " + offender);
                System.out.println(date);
                if (api.getCachedUsersByName(offender).size() < 1) {
                    System.out.println("Error! This USER does not exist!");
                } else {
                    new MessageBuilder()
                            .append("Report!", MessageDecoration.BOLD, MessageDecoration.UNDERLINE)
                            .setEmbed(new EmbedBuilder()
                                    .setTitle(fullDate)
                                    .setDescription("Report!\n" + "Reporter: " + author + "\nOffender: " + api.getCachedUsersByName(offender).iterator().next() + "\nMessage: " + reportMessage)
                                    .setColor(Color.ORANGE))
                            .send((TextChannel) channel);
                }
            } else {
                System.out.println("Incorrect Formatting, Please Try Again!");
            }
            System.out.println(author.getName() + " -> PM CONTENT -> " + message);

        }


        //TODO: Strike Offender
        public void strikeOffender() {
            api.getTextChannelsByName("reports").iterator().next().sendMessage("Striked Boi!");
            System.out.println("*****");
        }

        //TODO: Disregard Report
        public void disregardReport() {
            api.getTextChannelsByName("reports").iterator().next().sendMessage("Disregarded Boi!");
            System.out.println("*****");
        }


    }
