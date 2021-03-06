package de.polocloud.plugin.listener;

import de.polocloud.api.network.protocol.packet.gameserver.GameServerPlayerDisconnectPacket;
import de.polocloud.api.network.protocol.packet.gameserver.GameServerPlayerRequestJoinPacket;
import de.polocloud.api.network.protocol.packet.gameserver.GameServerPlayerUpdatePacket;
import de.polocloud.plugin.CloudPlugin;
import de.polocloud.plugin.protocol.NetworkClient;
import de.polocloud.plugin.protocol.connections.NetworkLoginCache;
import de.polocloud.plugin.protocol.players.MaxPlayerProperty;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class CollectiveProxyEvents implements Listener {

    private Plugin plugin;
    private NetworkClient networkClient;
    private NetworkLoginCache networkLoginCache;

    public CollectiveProxyEvents(Plugin plugin, NetworkClient networkClient, NetworkLoginCache networkLoginCache) {
        this.plugin = plugin;
        this.networkClient = networkClient;
        this.networkLoginCache = networkLoginCache;

        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void handle(ProxyPingEvent event) {
        event.getResponse().getPlayers().setMax(CloudPlugin.getInstance().getMaxPlayerProperty().getMaxPlayers());
    }

    @EventHandler
    public void handle(PostLoginEvent event){
        ProxiedPlayer player = event.getPlayer();

        if(CloudPlugin.getInstance().getState() == null){
            event.getPlayer().disconnect("");
            return;
        }

        if(CloudPlugin.getInstance().getState().isMaintenance()  && !player.hasPermission("*") && !player.hasPermission("cloud.maintenance") ){
            event.getPlayer().disconnect(TextComponent.fromLegacyText(CloudPlugin.getInstance().getState().getKickMessage()));
        }

        MaxPlayerProperty maxPlayerProperty = CloudPlugin.getInstance().getMaxPlayerProperty();

        if(ProxyServer.getInstance().getPlayers().size()-1 >= maxPlayerProperty.getMaxPlayers() && !player.hasPermission("*") && !player.hasPermission("cloud.fulljoin")) {
            event.getPlayer().disconnect(CloudPlugin.getInstance().getMaxPlayerProperty().getMessage());
            return;
        }
    }

    @EventHandler
    public void handle(LoginEvent event) {

        if(CloudPlugin.getInstance().getState() == null){
            event.setCancelled(true);
            return;
        }

        UUID requestId = UUID.randomUUID();
        networkLoginCache.getLoginEvents().put(requestId, event);
        event.registerIntent(this.plugin);
        networkClient.sendPacket(new GameServerPlayerRequestJoinPacket(requestId));
    }

    @EventHandler
    public void handle(ServerConnectEvent event) {
        if (networkLoginCache.getLoginServers().containsKey(event.getPlayer().getUniqueId())) {
            String targetServer = networkLoginCache.getLoginServers().remove(event.getPlayer().getUniqueId());
            event.setTarget(ProxyServer.getInstance().getServerInfo(targetServer));
        }
    }

    @EventHandler
    public void handlePluginMessage(PluginMessageEvent event) {
        if (event.getTag().equals("MC|BSign") || event.getTag().equals("MC|BEdit")) event.setCancelled(true);
    }

    @EventHandler
    public void handle(ServerConnectedEvent event) {
        networkClient.sendPacket(new GameServerPlayerUpdatePacket(event.getPlayer().getUniqueId(), event.getPlayer().getName(), event.getServer().getInfo().getName()));
    }

    @EventHandler
    public void handle(PlayerDisconnectEvent event) {
        networkClient.sendPacket(new GameServerPlayerDisconnectPacket(event.getPlayer().getUniqueId(), event.getPlayer().getName()));
    }

}
