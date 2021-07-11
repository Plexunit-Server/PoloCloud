package de.polocloud.api.player;

import de.polocloud.api.common.INamable;
import de.polocloud.api.gameserver.IGameServer;

import java.util.List;
import java.util.UUID;

public interface ICloudPlayer extends INamable {

    UUID getUUID();

    IGameServer getProxyServer();

    IGameServer getMinecraftServer();

    void sendMessage(String message);

    void sendTo(IGameServer gameServer);

    void kick(String message);


}