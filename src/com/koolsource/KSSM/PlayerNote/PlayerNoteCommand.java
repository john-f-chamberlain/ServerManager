/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koolsource.KSSM.PlayerNote;

import com.koolsource.KSSM.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
/**
 *
 * @author john
 */
public class PlayerNoteCommand implements CommandExecutor{
    private final Main plugin;
    private final Functions function;

    public PlayerNoteCommand(Main aThis) {
        this.function = new Functions(aThis);
        this.plugin = aThis;
    }

    @Override
    public boolean onCommand(CommandSender aSender, Command aCommand, String aString, String[] anArray) {
        // /note create playername This player was being bad.
        if(aCommand.getName().equalsIgnoreCase("noteadd")){
            return this.function.createNote(aSender, anArray, false);
        }else if(aCommand.getName().equalsIgnoreCase("noteaddoff")){
            return this.function.createNote(aSender, anArray, true);
        }else if(aCommand.getName().equalsIgnoreCase("noterem")){
            return this.function.deleteNote(aSender, anArray);
        }else if(aCommand.getName().equalsIgnoreCase("noteview")){
            return this.function.viewNote(aSender, anArray);
        }else if(aCommand.getName().equalsIgnoreCase("watchplayer")){
            return this.function.addWatch(aSender, anArray);
        }else{
            return false;
        }
    }
    
}
