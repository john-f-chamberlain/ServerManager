package com.koolsource.KSSM;

import com.koolsource.KSSM.BanList.BanListCommand;
import com.koolsource.KSSM.BanList.BanListListener;
import com.koolsource.KSSM.ForumBridge.ForumBridgeCommand;
import com.koolsource.KSSM.ForumBridge.ForumBridgeListener;
import com.koolsource.KSSM.Includes.LogWriter;
import com.koolsource.KSSM.Includes.WebReader;
import com.koolsource.KSSM.IssueReport.IssueReportCommand;
import com.koolsource.KSSM.IssueReport.IssueReportListener;
import com.koolsource.KSSM.PlayerList.PlayerListCommand;
import com.koolsource.KSSM.PlayerList.PlayerListListener;
import com.koolsource.KSSM.PlayerNote.PlayerNoteCommand;
import com.koolsource.KSSM.PlayerNote.PlayerNoteListener;
import com.koolsource.KSSM.Verification.VerificationCommand;
import com.koolsource.KSSM.Verification.VerificationListener;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private Permission perms;
    private Economy econ;
    private Chat chat;
    private static Boolean error = false;
    private static Boolean debug = true;
    private static int serverID = 1;
    public static final String ChatLogo = ChatColor.RED + "[" + ChatColor.YELLOW + "KSFB" + ChatColor.RED + "] " + ChatColor.RESET;
    private static String httpUrl = "http://koolsource.com/KSSM/api.php";
    private static String securityKey = ""
            + "9B6114D977501345C800EE1358D542FB"
            + "3EFDBCBEDE5234C19C9301648AD703E1"
            + "7A432D10B28832EC550D9ACA99064F30"
            + "8CDD487818F436A19C048251E82C1F1C"
            + "7160A8AC727CF7CC48B6741FC093493B"
            + "BCAA81C5FDC1F4E2681B297032F27980"
            + "D80EE3F532CF9A73333DB1460FED0BF7"
            + "FD70AF504408FEE216CF3AF2F55FDD5D"
            + "D0837CCA2E88D96EAF0AF0A064E5B9BC"
            + "6755A6F24D912B371F6111178D508745"
            + "20923FC28EF6A949CA9ACF843A0604CC"
            + "79E5FF0F1EDFEA20653067F07B859F5A"
            + "22E1A4D005AA936472DF68671AC0897E"
            + "38BF049F927370204D811E01CB161808"
            + "0DF417E5B5599D3ECF30229A53A32A34"
            + "615E3A1E5069FF2DFC50B7B611AE3627";

    @Override
    public void onEnable() {

        if (!setupPermissions() || !setupChat() || !setupEconomy()) {
            LogWriter.error("Disabling due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        BanListCommand BanListCommand = new BanListCommand(this);
        ForumBridgeCommand ForumBridgeCommand = new ForumBridgeCommand(this);
        IssueReportCommand IssueReportCommand = new IssueReportCommand(this);
        PlayerNoteCommand PlayerNoteCommand = new PlayerNoteCommand(this);
        VerificationCommand VerificationCommand = new VerificationCommand(this);
        PlayerListCommand PlayerListCommand = new PlayerListCommand(this);

        //# Banlist
        this.getServer().getPluginManager().registerEvents(new BanListListener(this), this);
        this.getCommand("KSBL").setExecutor(BanListCommand);
        this.getCommand("checkban").setExecutor(BanListCommand);
        this.getCommand("banip").setExecutor(BanListCommand);
        this.getCommand("ban").setExecutor(BanListCommand);
        this.getCommand("kick").setExecutor(BanListCommand);
        this.getCommand("tempban").setExecutor(BanListCommand);
        this.getCommand("warn").setExecutor(BanListCommand);
        this.getCommand("unban").setExecutor(BanListCommand);

        //# Forum Bridge
        this.getServer().getPluginManager().registerEvents(new ForumBridgeListener(this), this);
        this.getCommand("KSFB").setExecutor(ForumBridgeCommand);
        this.getCommand("sync").setExecutor(ForumBridgeCommand);

        //# Issue Reports
        this.getServer().getPluginManager().registerEvents(new IssueReportListener(this), this);
        this.getCommand("KSIR").setExecutor(IssueReportCommand);

        //# Player List
        this.getServer().getPluginManager().registerEvents(new PlayerListListener(this), this);
        this.getCommand("KSPL").setExecutor(PlayerListCommand);

        //# Player Notes
        this.getServer().getPluginManager().registerEvents(new PlayerNoteListener(this), this);
        this.getCommand("KSPN").setExecutor(PlayerNoteCommand);

        //# Verification
        this.getServer().getPluginManager().registerEvents(new VerificationListener(this), this);
        this.getCommand("KSVC").setExecutor(VerificationCommand);


        LogWriter.info("KSSM Loaded Successfully");
    }

    public WebReader getWeb() {
        WebReader web = new WebReader();
        web.setURL(httpUrl);
        web.setParam("SecurityKey", securityKey);
        web.setParam("ServerID", this.getServer().getServerId());
        return web;
    }

    private boolean setupPermissions() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) {
            return false;
        }
        perms = rsp.getProvider();
        return perms != null;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupChat() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            return false;
        }
        chat = rsp.getProvider();
        return chat != null;
    }

    public static boolean getDebug() {
        return Main.debug;
    }

    public Permission getPerm() {
        return this.perms;
    }

    public Economy getEco() {
        return this.econ;
    }

    public Chat getChat() {
        return this.chat;
    }

    public static int getID() {
        return Main.serverID;
    }

    public static String getSecurityKey() {
        return Main.securityKey;
    }

    public static String getHttpUrl() {
        return Main.httpUrl;
    }

    public static String combineSplit(int startIndex, String[] string, String seperator) {
        StringBuilder builder = new StringBuilder();

        for (int i = startIndex; i < string.length; i++) {
            builder.append(string[i]);
            builder.append(seperator);
        }

        builder.deleteCharAt(builder.length() - seperator.length());
        return builder.toString();
    }

    public static void noPermission(Player aSender) {
        aSender.sendMessage(ChatColor.RED + "You do not have access to this command.");
    }

    public String[] arrayShift(String[] anArray, int position) {
        String temp = anArray[position];
        String[] array = null;

        for (int i = (position - 1); i >= 0; i--) {
            array[i + 1] = array[i];
        }
        return array;
    }
}
