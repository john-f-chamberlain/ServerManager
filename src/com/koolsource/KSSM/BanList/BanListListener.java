/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koolsource.KSSM.BanList;

import com.koolsource.KSSM.Includes.LogWriter;
import com.koolsource.KSSM.Includes.WebReader;
import com.koolsource.KSSM.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 *
 * @author john
 */
public class BanListListener implements Listener {
    private final Main plugin;

    /**
     *
     * @param aThis
     */
    public BanListListener(Main aThis) {
        this.plugin = aThis;
    }
    
    /**
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLoginEvent(PlayerLoginEvent event){
        Player pJoin = event.getPlayer();
        
        WebReader web = this.plugin.getWeb("BanList", "CheckBan");
        web.setParam("PlayerName", pJoin.getName());
        web.makeRequest();
        String response = web.getResponse();
        
        if(response.equals("NOT_BANNED")){
            LogWriter.debug("Player " + pJoin.getName() + "NOT banned.");
            event.setResult(PlayerLoginEvent.Result.ALLOWED);
        }else if(response.startsWith("TEMP_BANNED:")){
            String[] responseParts = response.split(":");
            event.setKickMessage("You are temporarily banned. Reason: " + responseParts[1] + "Expires: " + responseParts[2]);
            event.setResult(PlayerLoginEvent.Result.KICK_BANNED);
        }else if(response.startsWith("PERM_BANNED:")){
            String[] responseParts = response.split(":");
            event.setKickMessage("You are permanently banned. Reason: " + responseParts[1] + "Appeal: http://ksrc.co/appeal");
            event.setResult(PlayerLoginEvent.Result.KICK_BANNED);
        }
        
    }
    
    /**
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event){
        event.setLeaveMessage(null);
    }
    
}
