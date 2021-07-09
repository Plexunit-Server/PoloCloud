package de.polocloud.wrapper;

import de.polocloud.api.CloudAPI;
import de.polocloud.api.PoloCloudAPI;
import de.polocloud.api.guice.PoloAPIGuiceModule;
import de.polocloud.api.network.IStartable;
import de.polocloud.api.network.ITerminatable;
import de.polocloud.api.network.client.SimpleNettyClient;
import de.polocloud.api.network.protocol.packet.wrapper.WrapperLoginPacket;
import de.polocloud.wrapper.commands.StopCommand;
import de.polocloud.wrapper.network.handler.MasterLoginResponsePacketHandler;
import de.polocloud.wrapper.network.handler.MasterRequestServerStartListener;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URISyntaxException;
import java.util.UUID;


public class Wrapper implements IStartable, ITerminatable {

    private CloudAPI cloudAPI;

    private SimpleNettyClient nettyClient;

    public Wrapper() {


        this.cloudAPI = new PoloCloudAPI(new PoloAPIGuiceModule());

        CloudAPI.getInstance().getCommandPool().registerCommand(new StopCommand());
    }

    @Override
    public void start() {
        this.nettyClient = this.cloudAPI.getGuice().getInstance(SimpleNettyClient.class);
        System.out.println("connecting");
        new Thread(() -> {
            this.nettyClient.start();

        }).start();
        System.out.println("connected!");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.nettyClient.sendPacket(new WrapperLoginPacket("xXxPoloxXxCloudxXx"));
        //this.nettyClient.registerListener(new SimpleWrapperNetworkListener(this.nettyClient.getProtocol()));

        this.nettyClient.getProtocol().registerPacketHandler(new MasterLoginResponsePacketHandler());
        this.nettyClient.getProtocol().registerPacketHandler(new MasterRequestServerStartListener());

    }

    @Override
    public boolean terminate() {
        return this.nettyClient.terminate();
    }
}
