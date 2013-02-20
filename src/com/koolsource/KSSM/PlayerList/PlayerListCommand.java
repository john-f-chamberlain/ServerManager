/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koolsource.KSSM.PlayerList;

import com.koolsource.KSSM.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Player List Command Handler - 
 * Handles the commands relevant to the Player List portion of this plug-in
 * Contains all of the methods required by these commands.
 * 
 * @author Johnathan Chamberlain
 * @since 0.1
 */
public class PlayerListCommand implements CommandExecutor {
    private final Main plugin;

    /**
     *
     * @param aThis
     */
    public PlayerListCommand(Main aThis) {
        this.plugin = aThis;
    }


    /**
     *
     * @param cs
     * @param cmnd
     * @param string
     * @param strings
     * @return
     */
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
