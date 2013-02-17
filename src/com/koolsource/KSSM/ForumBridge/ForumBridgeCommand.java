/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koolsource.KSSM.ForumBridge;

import com.koolsource.KSSM.Includes.LogWriter;
import com.koolsource.KSSM.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author john
 */
public class ForumBridgeCommand  implements CommandExecutor{
    private final Main plugin;
    private final Functions function;

    public ForumBridgeCommand(Main aThis) {
        this.plugin = aThis;
        this.function = new Functions(aThis);
    }

    @Override
    public boolean onCommand(CommandSender aSender, Command aCommand, String aString, String[] anArray) {
        if(aCommand.getName().equals("fsync")){
            return this.function.fsync(aSender, anArray);
        }else if(aCommand.getName().equals("fsyncall")){
            return this.function.fsyncall(aSender, anArray);
        }else{
            LogWriter.error("Shouldn't be here! Error No. FB001");
            return false;
        }
    }
    
}
