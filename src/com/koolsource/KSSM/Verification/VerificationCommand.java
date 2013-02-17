/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koolsource.KSSM.Verification;

import com.koolsource.KSSM.Includes.LogWriter;
import com.koolsource.KSSM.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author john
 */
public class VerificationCommand implements CommandExecutor {
    private final Main plugin;
    private final Functions function;

    public VerificationCommand(Main aThis) {
        this.plugin = aThis;
        this.function = new Functions(aThis);
    }

    @Override
    public boolean onCommand(CommandSender aSender, Command aCommand, String aString, String[] anArray) {
        if(aCommand.getName().equals("veify")){
            this.function.verifyPlayer(aSender, anArray);
        }else{
            //ErrNo: VC002
            LogWriter.error("Shouldn't be here! Error No. VC002");
            return false;
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
