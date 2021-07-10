package de.polocloud.api.network.server;

import com.google.inject.Inject;
import de.polocloud.api.network.NetworkHandler;
import de.polocloud.api.network.protocol.IProtocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import javax.inject.Named;

public class SimpleNettyServer implements INettyServer {

    @Inject
    @Named(value = "setting_server_start_port")
    private int port;

    @Inject
    private IProtocol protocol;

    @Override
    public void start() {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            (serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)).childHandler(new ChannelInitializer<SocketChannel>(){

                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                    socketChannel.pipeline().addLast(new ObjectEncoder());
                    socketChannel.pipeline().addLast(new NetworkHandler(protocol));
                }
            }).option(ChannelOption.SO_BACKLOG, 128);
            ChannelFuture channelFuture = serverBootstrap.bind(this.port).sync();
            channelFuture.channel().closeFuture().sync();
        }
        catch (Exception exc) {
            exc.printStackTrace();
            System.out.println(exc.getLocalizedMessage());
        }


        System.out.println("starting server on port " + this.port);
    }

    @Override
    public boolean terminate() {
        return true;
    }


    @Override
    public IProtocol getProtocol() {
        return protocol;
    }
}
