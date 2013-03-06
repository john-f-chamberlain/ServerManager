/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koolsource.KSSM.PlayerList;

import com.koolsource.KSSM.Includes.LogWriter;
import com.koolsource.KSSM.Includes.WebReader;
import com.koolsource.KSSM.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Player List Listener - 
 * Marks players online and offline in the database as they join and leave the 
 * server; Adds new players to the database upon first join.
 * 
 * @author Johnathan Chamberlain
 * @since 0.1
 */
public class PlayerListListener implements Listener {
    private final Main plugin;

    /**
     * Constructor Method
     * 
     * @author Johnathan Chamberlain
     * @since 0.1
     * @param aThis
     */
    public PlayerListListener(Main aThis) {
        this.plugin = aThis;
    }
    
    /**
     * Marks a player as <em>online</em> in the database.
     * Adds a player to the database on first join.
     * 
     * @author Johnathan Chamberlain
     * @since 0.1
     * @param event PlayerJoinEvent event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event){
        LogWriter.debug("Player Joined: " + event.getPlayer().getName());
        Player aPlayer = event.getPlayer();
        WebReader web = this.plugin.getWeb("PlayerList", "PlayerJoin");
        web.setParam("PlayerName", aPlayer.getName());
        web.setParam("IPAddress", aPlayer.getAddress().getHostString());
        web.makeRequest();
    }
    
    /**
     * Marks a player as <em>offline</em> in the database.
     * 
     * @author Johnathan Chamberlain
     * @since 0.1
     * @updated 0.2
     * @param event PlayerQuitEvent event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event){
        LogWriter.debug("Player Quit: " + event.getPlayer().getName());
        Player aPlayer = event.getPlayer();
        WebReader web = this.plugin.getWeb("PlayerList", "PlayerQuit");
        web.setParam("PlayerName", aPlayer.getName());
        web.makeRequest();
    }
    
}
