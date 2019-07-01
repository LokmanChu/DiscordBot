package com.github.aqml15.discordbot;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.ServerTextChannelBuilder;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageDecoration;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

public class ReportBot {
	private static final String reportsPath = "data/reportsList.ser";
	static MembersList list;
	
	public static void main(String[] args) {
		// Discord Bot Setup
		DiscordApi api = new DiscordApiBuilder().setToken("NTkwOTk4MjUxMzIzNTIzMTA5.XQ6QbA.TJwthwrr5vYmMAXNQPiflS4NKM8").login().join();
		System.out.println(api.createBotInvite());
        System.out.println("Logged in!");
        Server server = api.getServers().iterator().next();
        
        // Reports List Setup
        list = null;
        Boolean success = true;
        
        try {
        	FileInputStream fileIn = new FileInputStream(reportsPath);
        	ObjectInputStream in = new ObjectInputStream(fileIn);
        	list = (MembersList) in.readObject();
        	in.close();
        	fileIn.close();
        	System.out.println("Members List found!");
        } catch (ClassNotFoundException c) {
        	System.out.println("Members List not found");
        	success = false;
        	//c.printStackTrace();
        } catch (IOException i) {
        	System.out.println("Members List not found");
        	success = false;
        	//i.printStackTrace();
        }
        
        if (!success) {
        	list = new MembersList();
        	list.add(server.getOwner().getName());
        }
        
        // Creates Mod role if none exists
        Permissions modPermissions = new PermissionsBuilder().setAllAllowed().build();
        Permissions noPermissions = new PermissionsBuilder().setAllDenied().build();
        Role mod = null;
        
        if (server.getRolesByName("Mod").size() == 0) {
        	mod = server.createRoleBuilder().setName("Mod").setPermissions(modPermissions).setColor(Color.CYAN).create().join();
            mod.addUser(server.getOwner(), "cuz he is cool");
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
        System.out.println(server.getChannels());
        
        // Add Listener for '!report' command
        // Add Listener for '!viewMembers' command
        // Add Listener for '!saveList' command
        api.addMessageCreateListener(event -> {
        	
        	// Upon receiving '!report' command, send DM for further instructions
            if (event.getMessageContent().equalsIgnoreCase("!report")) {
            	User author = event.getMessageAuthor().asUser().get();
            	try {
            		event.getChannel().sendMessage("Report Found!");
					author.openPrivateChannel().get().sendMessage(
							"To Report a User for breaking the rules, please reply to this DM with the following format:\n"
							+ "[!!! <userid to be reported>: <mm/dd/yyyy> <'Message breaking the rule'>]\n"
							+ "[Example: !!! JohnDoe: 06/19/2019 'What the ****!']"
							);
				}
            	catch(Exception e) {
            		System.out.println("Report Failed!");
            	}
            }
            
            // Upon receiving '!viewMembers' command, bot sends list of members
            if (event.getMessageContent().equalsIgnoreCase("!viewMembers")) {
            	event.getChannel().sendMessage("Members List Requested...");
            	event.getChannel().sendMessage(list.print());
            }
            
         // Upon receiving '!saveList' command, saves list of members
            if (event.getMessageContent().equalsIgnoreCase("!saveList")) {
            	event.getChannel().sendMessage("Members List Saving...");
            	try {
            		FileOutputStream fileOut = new FileOutputStream(reportsPath);
            		ObjectOutputStream out = new ObjectOutputStream(fileOut);
            		out.writeObject(list);
            		out.close();
            		fileOut.close();
            		System.out.println("Members List was saved into " + reportsPath);
            	} catch (IOException i) {
            		System.out.println("Save failed...");
            		i.printStackTrace();
            	}
            }
            
            // Check if receiving message is DM, if so paste message into a private channel for review later
            if (event.isPrivateMessage()) {
            	User author = event.getMessageAuthor().asUser().get();
            	SimpleDateFormat formatter1=new SimpleDateFormat("MM/dd/yyyy");  
            	String message = event.getMessageContent();	
            	String stringDate = message.split("\\s+")[2];
            	String offender = message.split("\\s+")[1];
            	String reportMessage = "";
            	String[] collection = message.split("\\s+");
            	for (int i = 3; i < collection.length; i++) {
            		reportMessage = reportMessage + " " + collection[i];
            	}
                
            	offender = offender.substring(0, offender.length()-1);
            	try {
            		//TODO: Check if message matches report format, if so paste to review channel
            		// Checks if the offender is a valid USER
            		Date date = null;
    				try {
    					date = formatter1.parse(stringDate);
    					date.setTime(System.currentTimeMillis());
    				} catch (ParseException e1) {
    					// TODO Auto-generated catch block
    					System.out.println("Date Incorrect!");
    				}
            		
            		if (message.startsWith("!!!", 0)) {
            			System.out.println("NAME: " + offender);
            			System.out.println(date);
            			if (server.getMembersByName(offender).size() == 0) {
            				System.out.println("Error! This USER does not exist!");
            			}
            			else {
            				new MessageBuilder()
            			    .append("Report!", MessageDecoration.BOLD, MessageDecoration.UNDERLINE)
            			    .setEmbed(new EmbedBuilder()
            			            .setTitle(date.toString())
            			            .setDescription("Report!\n" + "Reporter: " + author + "\nOffender: " + offender + "\nMessage: " + reportMessage)
            			            .setColor(Color.ORANGE))
            			    .send(server.getTextChannelsByName("reports").get(0));
            			}
            		}
            		else {
            			System.out.println("Incorrect Formatting, Please Try Again!");
            		}
            		System.out.println(author.getName() + " -> PM CONTENT -> " + message);
				} 
            	catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Report Confirmation Failed!");
				} 
            }
        });
        
        // Reports Reactions
        api.addReactionAddListener(event -> {
        	if (event.getChannel() == server.getChannelsByName("reports").get(0)) {
        		if (event.getEmoji().equalsEmoji("👍")) {
        			//TODO: Strike Offender
        			server.getTextChannelsByName("reports").get(0).sendMessage("Striked Boi!");
        			System.out.println("*****");
        		}
        		else if (event.getEmoji().equalsEmoji("👎")) {
        			//TODO: Disregard Report
        			server.getTextChannelsByName("reports").get(0).sendMessage("Disregarded Boi!");
        			System.out.println("*****");
        		}
        	}
        });
        
        api.addServerMemberJoinListener(listener -> {
        	list.add(listener.getUser().getName());
        	
        	try {
        		FileOutputStream fileOut = new FileOutputStream(reportsPath);
        		ObjectOutputStream out = new ObjectOutputStream(fileOut);
        		out.writeObject(list);
        		out.close();
        		fileOut.close();
        		System.out.println("Members List was saved into " + reportsPath);
        	} catch (IOException i) {
        		System.out.println("Save failed...");
        		i.printStackTrace();
        	}
        });
        
        api.addServerMemberLeaveListener(listener -> {
        	list.remove(listener.getUser().getName());
        });
        
	}
}