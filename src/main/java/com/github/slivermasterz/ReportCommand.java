package com.github.slivermasterz;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
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

    class Report {
        User offender;
        String reportMessage;
        long messageId;

        Report(User offender,String reportMessage) {
            this.offender = offender;
            this.reportMessage = reportMessage;
            messageId = -1;
        }

        public void setMessageId(long messageId) {
            this.messageId = messageId;
        }
    }

    DiscordApi api;
    ArrayList<User> pendingReporters;
    ArrayList<Report> activeReports;

    public ReportCommand(DiscordApi api) {
        this.api = api;
        pendingReporters = new ArrayList<User>(4);
        activeReports = new ArrayList<Report>(10);
    }


    public void sendReportPM(User author){
        try {
            pendingReporters.add(author);
            author.openPrivateChannel().get().sendMessage(
                    "To Report a User for breaking the rules, please reply to this DM with the following format:\n"
                            + "[<userid to be reported> : <'Message breaking the rule'>]\n"
                            + "[Example: JohnDoe : 'What the ****!']"
            );
        }
        catch(Exception e) {
            System.out.println("Report Failed!");
        }
    }

    public void addPMReport(User author, String message) {
        String offenderName = message.split("\\s+")[0];
        String reportMessage = message.substring(offenderName.length()+3);

        if (api.getCachedUsersByName(offenderName).size() < 1)
            System.out.println("Error! This USER does not exist!");
        else
            addReport(author,api.getCachedUsersByName(offenderName).iterator().next(),reportMessage);
    }

    public void addReport(User reporter, User offender, String reason) {
        // Check if receiving message is DM, if so paste message into a private channel for review later
        Channel channel = api.getChannelsByNameIgnoreCase("reports").iterator().next();
        System.out.println("channel: " + channel + " ... " + channel.toString());
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String fullDate = dateFormat.format(date);

        System.out.println("NAME: " + offender.getName());
        System.out.println(date);
        Report report = new Report(offender, reason);
        Consumer<Long> setReportMessgeId = (aLong) -> report.setMessageId(aLong.longValue());

        new MessageBuilder()
                .append("Report!", MessageDecoration.BOLD, MessageDecoration.UNDERLINE)
                .setEmbed(new EmbedBuilder()
                        .setTitle(fullDate)
                        .setDescription("Report!\n" + "Reporter: " + reporter + "\nOffender: " + offender + "\nMessage: " + reason)
                        .setColor(Color.ORANGE))
                .send((TextChannel) channel)
                .thenApplyAsync((Message msg) -> {
                    msg.addReaction("👍");
                    msg.addReaction("👎");
                    return msg.getId();
                }).thenAcceptAsync(setReportMessgeId);
        activeReports.add(report);

        System.out.println(reporter.getName() + " -> PM CONTENT -> " + reason);
    }


}