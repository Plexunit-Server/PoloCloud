package de.polocloud.bootstrap.network.handler;

import com.google.inject.Inject;
import de.polocloud.api.gameserver.IGameServer;
import de.polocloud.api.gameserver.IGameServerManager;
import de.polocloud.api.network.protocol.IPacketHandler;
import de.polocloud.api.network.protocol.packet.Packet;
import de.polocloud.api.network.protocol.packet.api.gameserver.APIRequestGameServerPacket;
import de.polocloud.api.network.protocol.packet.api.gameserver.APIResponseGameServerPacket;
import de.polocloud.api.template.ITemplateService;
import de.polocloud.api.template.TemplateType;
import io.netty.channel.ChannelHandlerContext;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class APIRequestGameServerHandler extends IPacketHandler<Packet> {

    @Inject
    private IGameServerManager gameServerManager;
    @Inject
    private ITemplateService templateService;

    @Override
    public void handlePacket(ChannelHandlerContext ctx, Packet obj) {
        APIRequestGameServerPacket packet = (APIRequestGameServerPacket) obj;

        UUID requestId = packet.getRequestId();
        String value = packet.getValue();
        APIRequestGameServerPacket.Action action = packet.getAction();
        try {
            final IGameServer requestServer = gameServerManager.getGameServerByConnection(ctx).get();


            if (action == APIRequestGameServerPacket.Action.NAME) {

                gameServerManager.getGameServerByName(value).thenAccept(gameServer -> {
                    requestServer.sendPacket(new APIResponseGameServerPacket(requestId, Collections.singletonList(gameServer), APIResponseGameServerPacket.Type.SINGLE));
                });
            } else if (action == APIRequestGameServerPacket.Action.ALL) {
                gameServerManager.getGameServers().thenAccept(gameServerList -> {
                    requestServer.sendPacket(new APIResponseGameServerPacket(requestId, gameServerList, APIResponseGameServerPacket.Type.LIST));

                });
            } else if (action == APIRequestGameServerPacket.Action.LIST_BY_TYPE) {
                gameServerManager.getGameServersByType(TemplateType.valueOf(value)).thenAccept(gameServerList -> {
                    requestServer.sendPacket(new APIResponseGameServerPacket(requestId, gameServerList, APIResponseGameServerPacket.Type.LIST));

                });
            }else if(action == APIRequestGameServerPacket.Action.SNOWFLAKE){
                gameServerManager.getGameSererBySnowflake(Long.parseLong(value)).thenAccept(gameServer -> {
                    requestServer.sendPacket(new APIResponseGameServerPacket(requestId, Collections.singletonList(gameServer), APIResponseGameServerPacket.Type.SINGLE));
                });
            }else if(action == APIRequestGameServerPacket.Action.LIST_BY_TEMPLATE){
                gameServerManager.getGameServersByTemplate(templateService.getTemplateByName(value).get()).thenAccept(gameServer -> {
                    requestServer.sendPacket(new APIResponseGameServerPacket(requestId, (gameServer), APIResponseGameServerPacket.Type.LIST));
                });
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Class<? extends Packet> getPacketClass() {
        return APIRequestGameServerPacket.class;
    }
}
