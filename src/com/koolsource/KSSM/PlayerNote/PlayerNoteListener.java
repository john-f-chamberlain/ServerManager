/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koolsource.KSSM.PlayerNote;

import com.koolsource.KSSM.Includes.GeneralFunctions;
import com.koolsource.KSSM.Includes.WebReader;
import com.koolsource.KSSM.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Player Note Listener - 
 *
 * @author Johnathan Chamberlain
 * @since 0.1
 */
public class PlayerNoteListener implements Listener {
    private final Main plugin;

    /**
     *
     * @param aThis
     */
    public PlayerNoteListener(Main aThis) {
        this.plugin = aThis;
    }

    /**
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event){
        String playerName = event.getPlayer().getName();
        
        // If the player is on the watchlist we'll notify everyone that is
        // online and a moderator that the person has joined.
        
        String watchReason = this.watchReason(playerName);
        if(!watchReason.equals("NONE")){
            for(Player p: this.plugin.getServer().getOnlinePlayers()){
                if(this.plugin.getPerm().has(p, "KSSM.PlayerNote.seewatches")){
                    p.sendMessage(Main.ChatLogo + ChatColor.DARK_PURPLE + "[WATCH]" + ChatColor.RED + playerName + ", who is on the watchlist, joined the server.");
                    p.sendMessage(Main.ChatLogo + ChatColor.DARK_RED + "Reason: " + ChatColor.DARK_PURPLE + watchReason );
                }
            }
            return;
        }
        
        // If a moderator joins the server we'll notify them of any players
        // currently on the server who are on the watch list.
        if(this.plugin.getPerm().has(event.getPlayer(), "KSSM.PlayerNote.seewatches.onjoin")){
            for(Player p: this.plugin.getServer().getOnlinePlayers()){
                /* TODO: Remove unnessisary web-calls
                 * it could end badly with lots of players on the server at once
                 * when a moderator joins and it makes 100 webcalls all at once.
                 * 
                 * Maybe store players in an array someplace once they join the
                 * server while on the watchlist? And then remove them from the
                 * array when they get removed from the watchlist or leave the
                 * server?
                 * 
                 * OR
                 * 
                 * Make one web-call that returns an array of players and reasons
                 * for being added to the watchlist.
                 */
                watchReason = this.watchReason(p.getName());
                if(!watchReason.equals("NONE")){
                    event.getPlayer().sendMessage(Main.ChatLogo + ChatColor.DARK_PURPLE + "[WATCH] Player: " + p.getName() + " Reason: " + watchReason);
                }
            }
        }
    }
    
    String watchReason(String playerName){
        WebReader web = this.plugin.getWeb("PlayerNotes", "WatchReason");
        web.setParam("PlayerName", playerName);
        web.makeRequest();
        
        return web.getResponse();
    }
}
