package de.polocloud.bootstrap.client;

import de.polocloud.api.gameserver.IGameServer;
import de.polocloud.api.network.protocol.packet.IPacket;
import de.polocloud.api.network.protocol.packet.IPacketSender;
import de.polocloud.api.network.protocol.packet.master.MasterRequestServerStartPacket;
import de.polocloud.api.template.TemplateType;
import io.netty.channel.ChannelHandlerContext;

public class WrapperClient implements IPacketSender {

    private String name;
    private ChannelHandlerContext chx;

    public WrapperClient(String name, ChannelHandlerContext ctx) {
        this.name = name;
        this.chx = ctx;
    }

    public void startServer(IGameServer gameServer) {
        System.out.println("start server " + gameServer.getName() + " on Wrapper " + getName());
        sendPacket(new MasterRequestServerStartPacket(
            gameServer.getTemplate().getName(),
            gameServer.getTemplate().getVersion(), gameServer.getSnowflake(),
            gameServer.getTemplate().getTemplateType() == TemplateType.PROXY));
    }

    public String getName() {
        return name;
    }

    @Override
    public void sendPacket(IPacket object) {
        System.out.println("> " + object.getClass().getSimpleName());
        this.chx.writeAndFlush(object);
    }
}
