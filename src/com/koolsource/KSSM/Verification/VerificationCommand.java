/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koolsource.KSSM.Verification;

import com.koolsource.KSSM.Includes.GeneralFunctions;
import com.koolsource.KSSM.Includes.LogWriter;
import com.koolsource.KSSM.Includes.WebReader;
import com.koolsource.KSSM.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author john
 */
public class VerificationCommand implements CommandExecutor {

    private final Main plugin;

    /**
     *
     * @param aThis
     */
    public VerificationCommand(Main aThis) {
        this.plugin = aThis;
    }

    /**
     *
     * @param aSender
     * @param aCommand
     * @param aString
     * @param anArray
     * @return
     */
    @Override
    public boolean onCommand(CommandSender aSender, Command aCommand, String aString, String[] anArray) {
        if (aCommand.getName().equals("veify")) {
            this.verifyPlayer(aSender, anArray);
        } else {
            //ErrNo: VC002
            LogWriter.error("Shouldn't be here! Error No. VC002");
            return false;
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private boolean verifyPlayer(CommandSender aSender, String[] anArray) {
        if (anArray.length > 1) {
            aSender.sendMessage(Main.ChatLogo + ChatColor.RED + "Inproper number of parameters. Please visit http://ksrc.co/verify for more info!");
        }

        if (!GeneralFunctions.isInt(anArray[1])) {
            aSender.sendMessage(Main.ChatLogo + ChatColor.RED + "Verification codes are 5-10 characters long and only contain digits (0-9)!");
            aSender.sendMessage(ChatColor.RED + "Please visit http://ksrc.co/verify for more info!");
        }

        WebReader web = this.plugin.getWeb("Verification", "Verify");
        web.setParam("PlayerName", aSender.getName());
        web.makeRequest();

        String response = web.getResponse();
        if (response.startsWith("success:")) {
            // The response will contain what forum account was linked to what minecraft account.
            // I'd rather deal with formatting and message concatination on the website side of
            // things in this particular instance.
            String message = response.split(":")[1];

            aSender.sendMessage(Main.ChatLogo + ChatColor.GREEN + "Verification successfull!");
            aSender.sendMessage(Main.ChatLogo + ChatColor.GREEN + message);
        } else if (response.startsWith("error:")) {
            String message = response.split(":")[1];

            aSender.sendMessage(Main.ChatLogo + ChatColor.RED + "Verification failure!");
            aSender.sendMessage(Main.ChatLogo + ChatColor.RED + "Reason: " + message);
        } else {
            aSender.sendMessage(Main.ChatLogo + ChatColor.RED + "System Failure! Error Number: [VC001]");
        }

        return true;
    }
}
