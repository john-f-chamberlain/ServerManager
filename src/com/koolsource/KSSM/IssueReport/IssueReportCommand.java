/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koolsource.KSSM.IssueReport;

import com.koolsource.KSSM.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author john
 */
public class IssueReportCommand  implements CommandExecutor{
    private final Main plugin;

    public IssueReportCommand(Main aThis) {
        this.plugin = aThis;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
