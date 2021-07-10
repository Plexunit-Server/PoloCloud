package de.polocloud.api.gameserver;

import de.polocloud.api.template.ITemplate;
import de.polocloud.api.template.TemplateType;

import java.util.List;

public interface IGameServerManager {

    IGameServer getGameServerByName(String name);

    IGameServer getGameSererBySnowflake(long snowflake);

    List<IGameServer> getGameServers();

    List<IGameServer> getGameServersByTemplate(ITemplate template);

    List<IGameServer> getGameServersByType(TemplateType type);

    void registerGameServer(IGameServer gameServer);

    void unregisterGameServer(IGameServer gameServer);

}
