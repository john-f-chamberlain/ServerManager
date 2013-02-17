/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koolsource.KSSM.BanList;

import com.koolsource.KSSM.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author john
 */
public class BanListCommand implements CommandExecutor {
    private final Main plugin;

    public BanListCommand(Main aThis) {
        this.plugin = aThis;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
