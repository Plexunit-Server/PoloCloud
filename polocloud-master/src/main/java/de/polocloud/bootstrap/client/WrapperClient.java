package de.polocloud.bootstrap.client;

import de.polocloud.api.event.EventRegistry;
import de.polocloud.api.event.gameserver.CloudGameServerStatusChangeEvent;
import de.polocloud.api.gameserver.IGameServer;
import de.polocloud.api.network.protocol.packet.IPacketSender;
import de.polocloud.api.network.protocol.packet.Packet;
import de.polocloud.api.network.protocol.packet.master.MasterRequestServerStartPacket;
import de.polocloud.api.template.TemplateType;
import de.polocloud.logger.log.Logger;
import de.polocloud.logger.log.types.ConsoleColors;
import de.polocloud.logger.log.types.LoggerType;
import io.netty.channel.ChannelHandlerContext;

public class WrapperClient implements IPacketSender {

    private String name;
    private ChannelHandlerContext chx;

    public WrapperClient(String name, ChannelHandlerContext ctx) {
        this.chx = ctx;
        this.name = name;
    }

    public void startServer(IGameServer gameServer) {
        Logger.log(LoggerType.INFO, "Trying to start server " + ConsoleColors.LIGHT_BLUE.getAnsiCode() + gameServer.getName() + ConsoleColors.GRAY.getAnsiCode() + " on " + getName() + ".");

        sendPacket(new MasterRequestServerStartPacket(
            gameServer.getTemplate().getName(),
            gameServer.getTemplate().getVersion(), gameServer.getSnowflake(),
            gameServer.getTemplate().getTemplateType() == TemplateType.PROXY, gameServer.getTemplate().getMaxMemory(),
            gameServer.getTemplate().getMaxPlayers(), gameServer.getName(), gameServer.getMotd(), gameServer.getTemplate().isStatic()));
        EventRegistry.fireEvent(new CloudGameServerStatusChangeEvent(gameServer, CloudGameServerStatusChangeEvent.Status.STARTING));

    }

    public String getName() {
        return name;
    }

    @Override
    public void sendPacket(Packet object) {
        this.chx.writeAndFlush(object);
    }

    public ChannelHandlerContext getConnection() {
        return this.chx;
    }

}
