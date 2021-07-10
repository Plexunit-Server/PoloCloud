package de.polocloud.bootstrap.network.handler;

import com.google.inject.Inject;
import de.polocloud.api.gameserver.IGameServer;
import de.polocloud.api.gameserver.IGameServerManager;
import de.polocloud.api.network.protocol.IPacketHandler;
import de.polocloud.api.network.protocol.packet.IPacket;
import de.polocloud.api.network.protocol.packet.gameserver.GameServerPlayerRequestJoinPacket;
import de.polocloud.api.network.protocol.packet.master.MasterPlayerRequestResponsePacket;
import de.polocloud.api.template.ITemplateService;
import de.polocloud.bootstrap.config.MasterConfig;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.UUID;

public class GameServerPlayerRequestJoinHandler extends IPacketHandler {

    @Inject
    private IGameServerManager gameServerManager;
    @Inject
    private ITemplateService templateService;

    @Inject
    private MasterConfig config;

    @Override
    public void handlePacket(ChannelHandlerContext ctx, IPacket obj) {
        GameServerPlayerRequestJoinPacket packet = (GameServerPlayerRequestJoinPacket) obj;

        UUID uuid = packet.getUuid();

        //TODO filter lobby with lowest player count
        List<IGameServer> gameServersByTemplate = gameServerManager.getGameServersByTemplate(templateService.getTemplateByName(config.getFallbackServer()));

        IGameServer iGameServer = gameServersByTemplate.get(0);

        ctx.writeAndFlush(new MasterPlayerRequestResponsePacket(uuid, iGameServer.getSnowflake()));

        System.out.println("sending player to " + iGameServer.getName() + " / " + iGameServer.getSnowflake());


    }

    @Override
    public Class<? extends IPacket> getPacketClass() {
        return GameServerPlayerRequestJoinPacket.class;
    }
}