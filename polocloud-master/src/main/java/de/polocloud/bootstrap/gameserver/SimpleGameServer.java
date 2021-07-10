package de.polocloud.bootstrap.gameserver;

import de.polocloud.api.gameserver.GameServerStatus;
import de.polocloud.api.gameserver.IGameServer;
import de.polocloud.api.network.protocol.packet.IPacket;
import de.polocloud.api.template.ITemplate;
import io.netty.channel.ChannelHandlerContext;

public class SimpleGameServer implements IGameServer {

    private String name;
    private GameServerStatus status;
    private ChannelHandlerContext ctx;
    private long snowflake;
    private ITemplate template;
    private long startTime;

    private int port;

    public SimpleGameServer(String name, GameServerStatus status, ChannelHandlerContext ctx, long snowflake, ITemplate template, long startTime) {
        this.name = name;
        this.status = status;
        this.ctx = ctx;
        this.snowflake = snowflake;
        this.template = template;
        this.startTime = startTime;
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public GameServerStatus getStatus() {
        return this.status;
    }

    @Override
    public long getSnowflake() {
        return this.snowflake;
    }

    @Override
    public ITemplate getTemplate() {
        return this.template;
    }

    @Override
    public void setStatus(GameServerStatus status) {
        this.status = status;
    }

    @Override
    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public long getStartTime() {
        return this.startTime;
    }

    @Override
    public void stop() {
        //TODO implement stop
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    @Override
    public void sendPacket(IPacket packet) {
        this.ctx.writeAndFlush(packet);
    }
}
