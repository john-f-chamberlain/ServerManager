/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koolsource.KSSM.BanList;

import com.koolsource.KSSM.Includes.GeneralFunctions;
import com.koolsource.KSSM.Includes.WebReader;
import com.koolsource.KSSM.Main;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


/**
 *
 * @author Johnathan Chamberlain
 */
public class Functions {

    private Main plugin;
    private final Permission perms;

    Functions(Main aThis) {
        this.plugin = aThis;
        this.perms = this.plugin.getPerm();
    }

    /**
     *
     * @param anAdmin
     * @param arguments
     * @return
     */
    public boolean warnPlayer(CommandSender anAdmin, String[] arguments) {
        if(!this.perms.has(anAdmin, "KSSM.Banlist.warn")){Main.noPermission((Player) anAdmin);return true;}
        if(arguments.length < 2){
            anAdmin.sendMessage(Main.ChatLogo + "Format: /warn <player> <reason>.");
            return true;
        }
        String adminName = anAdmin.getName();
        String victimName = arguments[0];
        String reason = GeneralFunctions.combineSplit(1, arguments, " ");

        Player victim = this.plugin.findPlayer(victimName);

        if (victim == null) {
            anAdmin.sendMessage(Main.ChatLogo + ChatColor.RED + "Unable to find a player with that string.");
            return true;
        }

        if (GeneralFunctions.isNull(reason)) {
            anAdmin.sendMessage(Main.ChatLogo + ChatColor.RED + "You cannot warn without reason.");
            return true;
        }

        WebReader web = this.plugin.getWeb("Banlist", "Warn");
        web.setParam("AdminName", adminName);
        web.setParam("VictimName", victimName);
        web.setParam("Reason", reason);
        web.makeRequest();

        String response = web.getResponse();


        if (response == null) {
            anAdmin.sendMessage(Main.ChatLogo + ChatColor.RED + "[ERROR] BL001: Failed to give warning point. " + ChatColor.YELLOW + "Please contact John.");
        }
        
        if (this.perms.has(victim, "KSSM.BanList.exempt") && !this.perms.has(anAdmin, "KSSM.BanList.override")){
            anAdmin.sendMessage(Main.ChatLogo + ChatColor.RED + "Unable to warn " + victim.getDisplayName() + " they are exempt.");
        }

        if (response.equals("KICKED")) {
            victim.kickPlayer(Main.autoKickReason.replaceFirst("%s", "kicked").replaceFirst("%s", "warned"));
        }else if(response.equals("TEMP_BANNED")){
            victim.kickPlayer(Main.autoKickReason.replaceFirst("%s", "temporarily banned").replaceFirst("%s", "kicked"));
        }else if(response.equals("PERM_BANNED")){
            victim.kickPlayer(Main.autoKickReason.replaceFirst("%s", "permenantly banned").replaceFirst("%s", "temporarily banned"));
        }

        victim.sendMessage("[" + ChatColor.BOLD + ChatColor.RED + "WARNING" + ChatColor.RESET + "]"
                + "[" + ChatColor.BOLD + ChatColor.DARK_RED + "WARNING" + ChatColor.RESET + "]"
                + "[" + ChatColor.BOLD + ChatColor.RED + "WARNING" + ChatColor.RESET + "]");
        victim.sendMessage(Main.ChatLogo + ChatColor.RED + "You have been given a warning point! Reason:");
        victim.sendMessage(Main.ChatLogo + ChatColor.RED + reason);
        victim.sendMessage(Main.ChatLogo + ChatColor.GREEN + "This warning point will expire in 72 hours.");
        victim.sendMessage(Main.ChatLogo + ChatColor.RED + "Visit " + ChatColor.DARK_GREEN + "http://ksrc.co/warns " + ChatColor.RED + " for more info about warnings.");

        return true;

    }

    /**
     *
     * @param anAdmin
     * @param arguments
     * @return
     */
    public boolean kickPlayer(CommandSender anAdmin, String[] arguments) {
        if(!this.perms.has(anAdmin, "KSSM.Banlist.kick")){Main.noPermission((Player) anAdmin);return true;}
        if(arguments.length < 2){
            anAdmin.sendMessage(Main.ChatLogo + "Format: /kick <player> <reason>.");
            return true;
        }
        String adminName = anAdmin.getName();
        String victimName = arguments[0];
        String reason = GeneralFunctions.combineSplit(1, arguments, " ");

        Player victim = this.plugin.findPlayer(victimName);

        if (victim == null) {
            anAdmin.sendMessage(Main.ChatLogo + ChatColor.RED + "Unable to find a player with that string.");
            return true;
        }
        
        if (this.perms.has(victim, "KSSM.BanList.exempt") && !this.perms.has(anAdmin, "KSSM.BanList.override")){
            anAdmin.sendMessage(Main.ChatLogo + ChatColor.RED + "Unable to kick " + victim.getDisplayName() + " they are exempt.");
        }

        if (GeneralFunctions.isNull(reason)) {
            anAdmin.sendMessage(Main.ChatLogo + ChatColor.RED + "You cannot kick without reason.");
            return true;
        }

        WebReader web = this.plugin.getWeb("Banlist", "Kick");
        web.setParam("AdminName", adminName);
        web.setParam("VictimName", victimName);
        web.setParam("Reason", reason);
        web.makeRequest();

        String response = web.getResponse();


        if (response == null) {
            anAdmin.sendMessage(Main.ChatLogo + ChatColor.RED + "[ERROR] BL002: Failed to log kick. " + ChatColor.YELLOW + "Please contact John.");
            anAdmin.sendMessage(Main.ChatLogo + ChatColor.GREEN + "Player still removed from server.");
        }

        victim.kickPlayer(ChatColor.RED + "Kicked: " + ChatColor.RESET + reason);
        this.plugin.getServer().broadcastMessage(ChatColor.RED + "Player Kicked: " + victim.getDisplayName());

        return true;
    }

    /**
     *
     * @param anAdmin
     * @param arguments
     * @return
     */
    public boolean banPlayer(CommandSender anAdmin, String[] arguments) {
        if(!this.perms.has(anAdmin, "KSSM.Banlist.ban")){Main.noPermission((Player) anAdmin);return true;}
        if(arguments.length < 3){
            anAdmin.sendMessage(Main.ChatLogo + "Format: /ban <player> <durration><unit> <reason>");
            return true;
        }
        String adminName    = anAdmin.getName();
        String victimName   = arguments[0];
        String duration     = arguments[1];
        String reason       = GeneralFunctions.combineSplit(2, arguments, " ");
        
        String unit         = duration.substring(-1);
        String length       = duration.substring(0,-1);
        
        Player victim = this.plugin.findPlayer(victimName);
        
        // Can't find a player in the server with that name. Moderators do not
        // need the ability to ban offline players and admins can do it through
        // the database so no need for that feature.
        if (victim == null) {
            anAdmin.sendMessage(Main.ChatLogo + ChatColor.RED + "Unable to find a player with that string.");
            return true;
        }
        
        // Used for admins and moderators to prevent abuse.
        if (this.perms.has(victim, "KSSM.BanList.exempt") && !this.perms.has(anAdmin, "KSSM.BanList.override")){
            anAdmin.sendMessage(Main.ChatLogo + ChatColor.RED + "Unable to ban " + victim.getDisplayName() + " they are exempt.");
        }
        
        // Lets check the unit. If it doesn't match one of the units we support
        // then give an error message and have them try again.
        if(!unit.matches("^[emhdwMY]$")){
           anAdmin.sendMessage(Main.ChatLogo + "Format: /ban <player> <durration><unit> <reason>.");
           anAdmin.sendMessage(Main.ChatLogo + "Available Units: "
                   + ChatColor.AQUA + "e" + ChatColor.RESET + "=" + ChatColor.DARK_AQUA + "Ever" + ChatColor.RESET + ", "
                   + ChatColor.AQUA + "m" + ChatColor.RESET + "=" + ChatColor.DARK_AQUA + "Minute" + ChatColor.RESET + ", "
                   + ChatColor.AQUA + "h" + ChatColor.RESET + "=" + ChatColor.DARK_AQUA + "Hour" + ChatColor.RESET + ", "
                   + ChatColor.AQUA + "d" + ChatColor.RESET + "=" + ChatColor.DARK_AQUA + "Day" + ChatColor.RESET + ", "
                   + ChatColor.AQUA + "w" + ChatColor.RESET + "=" + ChatColor.DARK_AQUA + "Week" + ChatColor.RESET + ", "
                   + ChatColor.AQUA + "M" + ChatColor.RESET + "=" + ChatColor.DARK_AQUA + "Month" + ChatColor.RESET + ", "
                   + ChatColor.AQUA + "Y" + ChatColor.RESET + "=" + ChatColor.DARK_AQUA + "Year" + ChatColor.RESET + ", "
                   );
           return true;
        }
        
        // If the durration interval is not an interval then throw an error and
        // have them try again. Apparently they think numbers contain letters.
        if(!GeneralFunctions.isInt(length)){
            anAdmin.sendMessage(Main.ChatLogo + "Format: /ban <player> <durration><unit> <reason>.");
            anAdmin.sendMessage(Main.ChatLogo + "Ex: /ban jchamb2010 1d Swearing.");
            anAdmin.sendMessage(Main.ChatLogo + "Ex: /ban jchamb2010 4e xray hack");
        }
        
        // A Duration of 4e indicates a permenant ban. This ensures that the admin
        // really wants to ban the person forever.
        if(unit.equals("e") && length != "4"){
            anAdmin.sendMessage(Main.ChatLogo + "That duration makes no sense. Only \"4\" may be used with the durration \"Ever\".");
        }
        
        // No more default reasons, provide a reason or don't ban them.
        // We need reasons for tracking and appeal purposes.
        if(GeneralFunctions.isNull(reason)){
            anAdmin.sendMessage(Main.ChatLogo + "You cannot ban without reason.");
            return true;
        }
        
        
        WebReader web = this.plugin.getWeb("BanList", "Ban");
        web.setParam("AdminName", adminName);
        web.setParam("VictimName", victimName);
        web.setParam("Reason" , reason);
        web.setParam("Length", length);
        web.setParam("Unit", unit);
        web.makeRequest();
        
        String response = web.getResponse();
        
        // Uh oh, Something happened with the webserver and it is unable to fulfill
        // our request. Guess we'll just have to kick the person and hope the
        // problem resolves itself soon.
        if(response.startsWith("ERROR")){
            anAdmin.sendMessage(ChatColor.RED + "ERROR: BL003, Unable to ban. Only kicking. Notify John.");
            victim.kickPlayer(reason);
            this.plugin.getServer().broadcastMessage("Player has been kicked: " + victim.getDisplayName());
            return true;
        }
        
        // Setup the private and public broadcast messages based on type of ban.
        String publicMessage;
        String privateMessage;
        if(unit.equals("e") && length.equals("4")){
            publicMessage = "Player has been banned: " + victim.getDisplayName();
            privateMessage = "You have been banned. Reason: " + reason;
        }else{
            publicMessage = "Player has been temporarily banned: " + victim.getDisplayName();
            privateMessage = "You have been temporarily banned. Reason: " + reason;
        }
        
        // The player has already been banned above, so now we just need to kick
        // them from the server to make it take effect. They will be unable to
        // join the server due to the join listener watching for them.
        victim.kickPlayer(privateMessage);
        this.plugin.getServer().broadcastMessage(publicMessage);

        return true;
    }
    
    
    public void checkBan(CommandSender aPlayer, String[] arguments){
        
        
    }
}
