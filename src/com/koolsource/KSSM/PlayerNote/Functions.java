/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koolsource.KSSM.PlayerNote;

import com.koolsource.KSSM.Includes.GeneralFunctions;
import com.koolsource.KSSM.Includes.WebReader;
import com.koolsource.KSSM.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author john
 */
public class Functions {

    private final Main plugin;

    Functions(Main aThis) {
        this.plugin = aThis;
    }

    boolean createNote(CommandSender aSender, String[] anArray, Boolean offline) {
        if (!this.plugin.getPerm().has(aSender, "kssm.playernote.create")) {
            Main.noPermission((Player) aSender);
            return true;
        }

        if (anArray.length < 2) {
            return false;
        }

        String vicName = anArray[0];
        String note = GeneralFunctions.combineSplit(1, anArray, " ");

        Player vic;
        vic = this.plugin.getServer().getPlayer(vicName);

        // This looks like fun doesn't it?
        // If player cannot be found
        if (vic == null) {
            // If they admin specified to search the offline players
            if (offline == true) {
                vic = (Player) this.plugin.getServer().getOfflinePlayer(vicName);
                // If a player still cannot be found
                if (vic == null) {
                    // Report error
                    aSender.sendMessage(Main.ChatLogo + ChatColor.RED + "Unable to find player with search string \"" + vicName + "\". Even offline.");
                    return true;
                }
            } else {
                // Otherwise: Report error
                aSender.sendMessage(Main.ChatLogo + ChatColor.RED + "Unable to find an online player with the search string \"" + vicName + "\".");
                return true;
            }
        }

        // Ok so we have our victim, we have our admin, and we have our note... Now what?
        // Ohh yes... We add them to the database :)
        // Web server processes all the database crap so this is rather...
        WebReader web = this.plugin.getWeb();
        web.setParam("Section", "PlayerNote");
        web.setParam("Action", "CreateNote");
        web.setParam("Admin", aSender.getName());
        web.setParam("Victim", vic.getName());
        web.setParam("Note", note);
        web.makeRequest();

        String response = web.getResponse();

        if (response.equals("success")) {
            aSender.sendMessage(Main.ChatLogo + ChatColor.GREEN + "Note created successfully for player \"" + vic.getName() + "\".");
        } else {
            aSender.sendMessage(Main.ChatLogo + ChatColor.RED + "Note not created!");
            aSender.sendMessage(ChatColor.RED + "Reason: " + response);
        }

        return true;
    }

    boolean deleteNote(CommandSender aSender, String[] anArray) {
        if (!this.plugin.getPerm().has(aSender, "kssm.playernote.delete")) {
            Main.noPermission((Player) aSender);
            return true;
        }

        if (anArray.length != 1 || !GeneralFunctions.isInt(anArray[0])) {
            return false;
        }



        WebReader web = this.plugin.getWeb();
        web.setParam("Section", "PlayerNote");
        web.setParam("Action", "DeleteNote");
        web.setParam("Admin", aSender.getName());
        web.setParam("NoteID", anArray[0]);
        web.setParam("GlobalDelete", (this.plugin.getPerm().has(aSender, "kssm.playernote.delete.all")) ? "true" : "false");
        web.makeRequest();

        String response = web.getResponse();

        if (response.equals("success")) {
            aSender.sendMessage(Main.ChatLogo + ChatColor.GREEN + "Note #" + anArray[0] + " successfully deleted!");
        } else {
            aSender.sendMessage(Main.ChatLogo + ChatColor.RED + "Note NOT deleted!");
            aSender.sendMessage(ChatColor.RED + "Reason: " + response);
        }


        return true;
    }

    boolean viewNote(CommandSender aSender, String[] anArray) {
        if (!this.plugin.getPerm().has(aSender, "kssm.playernote")) {
            Main.noPermission((Player) aSender);
            return true;
        }

        // TODO: Figure out how to best display the player notes.

        return true;
    }

    boolean addWatch(CommandSender aSender, String[] anArray) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    boolean isWatched(String playerName) {
        WebReader web = this.plugin.getWeb();
        web.setParam("Section", "PlayerNotes");
        web.setParam("Action","LookupWatch");
        web.setParam("PlayerName", playerName);
        web.makeRequest();
        
        return web.getBoolean();
    }
}
