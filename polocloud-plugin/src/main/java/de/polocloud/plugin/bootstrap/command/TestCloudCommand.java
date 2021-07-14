package de.polocloud.plugin.bootstrap.command;

import de.polocloud.plugin.api.CloudExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class TestCloudCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 1) {
            sender.sendMessage("requesting... " + args[0]);
            CloudExecutor.getInstance().getGameServerManager().getGameServerByName(args[0]).thenAccept(gameServer -> {

                sender.sendMessage(gameServer.getName() + " info");
                sender.sendMessage("snowflake " + gameServer.getSnowflake() + "");
                sender.sendMessage("status: " + gameServer.getStatus().toString());
                sender.sendMessage("onlineplayers: " + gameServer.getCloudPlayers().size());

            });

        }

        return true;
    }
}