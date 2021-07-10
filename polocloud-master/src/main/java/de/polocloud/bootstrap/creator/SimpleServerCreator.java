package de.polocloud.bootstrap.creator;

import com.google.inject.Inject;
import de.polocloud.api.gameserver.IGameServer;
import de.polocloud.api.gameserver.IGameServerManager;
import de.polocloud.api.template.ITemplate;
import de.polocloud.bootstrap.client.WrapperClient;

import java.util.List;

public class SimpleServerCreator extends ServerCreator {

    @Inject
    private IGameServerManager gameServerManager;

    @Override
    public boolean check(ITemplate template) {
        List<IGameServer> serversByTemplate = gameServerManager.getGameServersByTemplate(template);
        return serversByTemplate.size() < template.getMinServerCount();
    }

}
