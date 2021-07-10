package de.polocloud.api.network.protocol.packet.wrapper;

import de.polocloud.api.network.protocol.packet.IPacket;

public class WrapperLoginPacket implements IPacket {

    private String name;
    private String key;

    public WrapperLoginPacket() {
    }
    public WrapperLoginPacket(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }
}
