package de.polocloud.bootstrap.gameserver;

import de.polocloud.api.gameserver.IGameServer;
import de.polocloud.api.gameserver.IGameServerManager;
import de.polocloud.api.template.ITemplate;
import de.polocloud.api.template.TemplateType;

import java.util.ArrayList;
import java.util.List;

public class SimpleGameServerManager implements IGameServerManager {

    private List<IGameServer> gameServerList = new ArrayList<>();

    @Override
    public IGameServer getGameServerByName(String name) {

        for (IGameServer iGameServer : gameServerList) {
            if (iGameServer.getName().equalsIgnoreCase(name)) {
                return iGameServer;
            }
        }
        return null;
    }

    @Override
    public IGameServer getGameSererBySnowflake(long snowflake) {

        for (IGameServer iGameServer : gameServerList) {
            if (iGameServer.getSnowflake() == snowflake) {
                return iGameServer;
            }
        }
        return null;
    }

    @Override
    public List<IGameServer> getGameServers() {
        return this.gameServerList;
    }

    @Override
    public List<IGameServer> getGameServersByTemplate(ITemplate template) {

        List<IGameServer> result = new ArrayList<>();

        for (IGameServer iGameServer : gameServerList) {
            if (iGameServer.getTemplate().getName().equalsIgnoreCase(template.getName())) {
                result.add(iGameServer);
            }
        }
        return result;
    }

    @Override
    public List<IGameServer> getGameServersByType(TemplateType type) {

        List<IGameServer> result = new ArrayList<>();

        for (IGameServer iGameServer : gameServerList) {
            if (iGameServer.getTemplate().getTemplateType().equals(type)) {
                result.add(iGameServer);
            }
        }
        return result;
    }

    @Override
    public void registerGameServer(IGameServer gameServer) {
        gameServerList.add(gameServer);
    }

    @Override
    public void unregisterGameServer(IGameServer gameServer) {
        gameServerList.remove(gameServer);
    }
}
