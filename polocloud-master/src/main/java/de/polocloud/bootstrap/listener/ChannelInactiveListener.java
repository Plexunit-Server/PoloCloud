package de.polocloud.bootstrap.listener;

import com.google.inject.Inject;
import de.polocloud.api.gameserver.IGameServer;
import de.polocloud.api.gameserver.IGameServerManager;
import de.polocloud.api.network.event.ChannelInactiveEvent;
import de.polocloud.api.network.protocol.IPacketHandler;
import de.polocloud.api.network.protocol.packet.IPacket;
import de.polocloud.bootstrap.client.IWrapperClientManager;
import de.polocloud.bootstrap.client.WrapperClient;
import de.polocloud.bootstrap.gameserver.SimpleGameServer;
import io.netty.channel.ChannelHandlerContext;

public class ChannelInactiveListener extends IPacketHandler {

    @Inject
    private IGameServerManager gameServerManager;

    @Inject
    private IWrapperClientManager wrapperClientManager;

    @Override
    public void handlePacket(ChannelHandlerContext ctx, IPacket obj) {

        for (WrapperClient wrapperClient : wrapperClientManager.getWrapperClients()) {
            if(wrapperClient.getConnection().channel().id().asLongText().equalsIgnoreCase(ctx.channel().id().asLongText())){
                System.out.println("Wrapper " + wrapperClient.getName() + " disconnected!");
                wrapperClientManager.removeWrapper(wrapperClient);
                return;
            }
        }

        for (IGameServer o : gameServerManager.getGameServers()) {
            SimpleGameServer  gameServer = (SimpleGameServer) o;
            if(gameServer.getCtx().channel().id().asLongText().equalsIgnoreCase(ctx.channel().id().asLongText())){
                System.out.println("GameServer " + gameServer.getName() + " disconnected!");
                gameServerManager.unregisterGameServer(gameServer);
                return;
            }
        }

        System.out.println("Channel inactive hello from listener");
    }

    @Override
    public Class<? extends IPacket> getPacketClass() {
        return ChannelInactiveEvent.class;
    }
}
