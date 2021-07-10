package de.polocloud.api.network.client;

import de.polocloud.api.network.IStartable;
import de.polocloud.api.network.ITerminatable;
import de.polocloud.api.network.protocol.IProtocol;
import de.polocloud.api.network.protocol.packet.IPacketSender;

public interface INettyClient extends IStartable, ITerminatable, IPacketSender {

    IProtocol getProtocol();
}
