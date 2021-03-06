package de.polocloud.plugin.protocol.register;

import de.polocloud.api.gameserver.GameServerStatus;
import de.polocloud.api.gameserver.IGameServer;
import de.polocloud.api.network.protocol.IPacketHandler;
import de.polocloud.api.network.protocol.packet.Packet;
import de.polocloud.api.network.protocol.packet.RedirectPacket;
import de.polocloud.api.network.protocol.packet.api.cloudplayer.APIResponseCloudPlayerPacket;
import de.polocloud.api.network.protocol.packet.api.gameserver.APIResponseGameServerPacket;
import de.polocloud.api.network.protocol.packet.api.template.APIResponseTemplatePacket;
import de.polocloud.api.network.protocol.packet.gameserver.GameServerExecuteCommandPacket;
import de.polocloud.api.network.protocol.packet.gameserver.GameServerMaintenanceUpdatePacket;
import de.polocloud.api.network.protocol.packet.gameserver.GameServerMaxPlayersUpdatePacket;
import de.polocloud.api.network.protocol.packet.gameserver.GameServerShutdownPacket;
import de.polocloud.api.network.response.ResponseHandler;
import de.polocloud.api.player.ICloudPlayer;
import de.polocloud.api.template.ITemplate;
import de.polocloud.plugin.CloudPlugin;
import de.polocloud.plugin.api.CloudExecutor;
import de.polocloud.plugin.function.BootstrapFunction;
import de.polocloud.plugin.protocol.NetworkClient;
import de.polocloud.plugin.protocol.NetworkRegister;
import de.polocloud.plugin.protocol.maintenance.MaintenanceState;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class NetworkPluginRegister extends NetworkRegister {

    private BootstrapFunction bootstrapFunction;

    public NetworkPluginRegister(NetworkClient networkClient, BootstrapFunction bootstrapFunction) {
        super(networkClient);

        this.bootstrapFunction = bootstrapFunction;

        enableCloudExecutor();

        registerGameServerExecutePacket();
        registerMaintenanceStatePacket();
        registerGameServerShutdownPacket();
        registerMaxPlayersUpdatePacket();
        registerAPIHandler();

    }

    private void enableCloudExecutor() {
        new CloudExecutor(getNetworkClient(), getNetworkClient().getClient().getProtocol());
    }

    public void registerMaxPlayersUpdatePacket() {
        getNetworkClient().registerPacketHandler(new IPacketHandler() {
            @Override
            public void handlePacket(ChannelHandlerContext ctx, Packet obj) {
                GameServerMaxPlayersUpdatePacket packet = (GameServerMaxPlayersUpdatePacket) obj;
                CloudPlugin.getInstance().getMaxPlayerProperty().setMaxPlayers(packet.getMaxPlayers());
                CloudPlugin.getInstance().getMaxPlayerProperty().setMessage(packet.getMessage());
            }

            @Override
            public Class<? extends Packet> getPacketClass() {
                return GameServerMaxPlayersUpdatePacket.class;
            }
        });
    }

    public void registerAPIHandler() {

        getNetworkClient().registerPacketHandler(new IPacketHandler<Packet>() {
            @Override
            public void handlePacket(ChannelHandlerContext ctx, Packet obj) {
                APIResponseCloudPlayerPacket packet = (APIResponseCloudPlayerPacket) obj;
                UUID requestId = packet.getRequestId();
                List<ICloudPlayer> incomming = packet.getResponse();
                List<ICloudPlayer> response = packet.getResponse();
                APIResponseCloudPlayerPacket.Type type = packet.getType();

                CompletableFuture<Object> completableFuture = ResponseHandler.getCompletableFuture(requestId, true);
/*
                for (int i = 0; i < incomming.size(); i++) {
                    ICloudPlayer incommingObj = incomming.get(i);
                    response.add(new ICloudPlayer() {
                        @Override
                        public UUID getUUID() {
                            return incommingObj.getUUID();
                        }

                        @Override
                        public IGameServer getProxyServer() {
                            return incommingObj.getProxyServer();
                        }

                        @Override
                        public IGameServer getMinecraftServer() {
                            return incommingObj.getMinecraftServer();
                        }

                        @Override
                        public void sendMessage(String message) {
                            //TODO
                            throw new NotImplementedException();
                        }

                        @Override
                        public void sendTo(IGameServer gameServer) {
                            //TODO
                            throw new NotImplementedException();
                        }

                        @Override
                        public void kick(String message) {
                            //TODO
                            throw new NotImplementedException();
                        }

                        @Override
                        public String getName() {
                            return incommingObj.getName();
                        }
                    });
                }


 */

                if (packet.getType() == APIResponseCloudPlayerPacket.Type.SINGLE) {
                    completableFuture.complete(response.get(0));
                } else if (packet.getType() == APIResponseCloudPlayerPacket.Type.LIST) {
                    completableFuture.complete(response);
                } else if (packet.getType() == APIResponseCloudPlayerPacket.Type.BOOLEAN) {
                    completableFuture.complete(!response.isEmpty());
                }


            }

            @Override
            public Class<? extends Packet> getPacketClass() {
                return APIResponseCloudPlayerPacket.class;
            }
        });

        getNetworkClient().registerPacketHandler(new IPacketHandler<Packet>() {
            @Override
            public void handlePacket(ChannelHandlerContext ctx, Packet obj) {
                APIResponseGameServerPacket packet = (APIResponseGameServerPacket) obj;

                UUID requestId = packet.getRequestId();
                List<IGameServer> tmp = packet.getResponse();
                List<IGameServer> response = new ArrayList<>();
                CompletableFuture<Object> completableFuture = ResponseHandler.getCompletableFuture(requestId, true);

                for (IGameServer gameserver : tmp) {
                    response.add(new IGameServer() {
                        @Override
                        public String getName() {
                            return gameserver.getName();
                        }

                        @Override
                        public GameServerStatus getStatus() {
                            return gameserver.getStatus();
                        }

                        @Override
                        public long getSnowflake() {
                            return gameserver.getSnowflake();
                        }

                        @Override
                        public ITemplate getTemplate() {
                            return gameserver.getTemplate();
                        }

                        @Override
                        public List<ICloudPlayer> getCloudPlayers() {
                            return gameserver.getCloudPlayers();
                        }

                        @Override
                        public void setStatus(GameServerStatus status) {
                            throw new NotImplementedException();
                        }

                        @Override
                        public long getTotalMemory() {
                            return gameserver.getTotalMemory();
                        }

                        @Override
                        public int getOnlinePlayers() {
                            return gameserver.getOnlinePlayers();
                        }

                        @Override
                        public int getPort() {
                            return gameserver.getPort();
                        }

                        @Override
                        public long getPing() {
                            return gameserver.getPing();
                        }

                        @Override
                        public long getStartTime() {
                            return gameserver.getStartTime();
                        }

                        @Override
                        public void stop() {
                            sendPacket(new GameServerShutdownPacket());
                        }

                        @Override
                        public void sendPacket(Packet packet) {
                            ctx.writeAndFlush(new RedirectPacket(getSnowflake(), packet));
                        }

                        @Override
                        public void setMotd(String motd) {
                            throw new NotImplementedException();
                        }

                        @Override
                        public String getMotd() {
                            return gameserver.getMotd();
                        }
                    });
                }

                if (packet.getType() == APIResponseGameServerPacket.Type.SINGLE) {
                    completableFuture.complete(response.get(0));
                } else {
                    completableFuture.complete(response);
                }

            }

            @Override
            public Class<? extends Packet> getPacketClass() {
                return APIResponseGameServerPacket.class;
            }
        });

        getNetworkClient().registerPacketHandler(new IPacketHandler<Packet>() {
            @Override
            public void handlePacket(ChannelHandlerContext ctx, Packet obj) {
                APIResponseTemplatePacket packet = (APIResponseTemplatePacket) obj;
                UUID requestId = packet.getRequestId();
                List<ITemplate> response = packet.getResponse().stream().collect(Collectors.toList());
                CompletableFuture<Object> completableFuture = ResponseHandler.getCompletableFuture(requestId, true);
                if (packet.getType() == APIResponseTemplatePacket.Type.SINGLE) {
                    completableFuture.complete(response.get(0));
                } else {
                    completableFuture.complete(response);
                }
            }
            @Override
            public Class<? extends Packet> getPacketClass() {
                return APIResponseTemplatePacket.class;
            }
        });

    }

    public void registerMaintenanceStatePacket() {
        getNetworkClient().registerPacketHandler(new IPacketHandler<Packet>() {
            @Override
            public void handlePacket(ChannelHandlerContext ctx, Packet obj) {
                GameServerMaintenanceUpdatePacket maintenanceUpdatePacket = (GameServerMaintenanceUpdatePacket) obj;
                CloudPlugin.getInstance().setState(new MaintenanceState(maintenanceUpdatePacket.isState(), maintenanceUpdatePacket.getMessage()));
            }

            @Override
            public Class<? extends Packet> getPacketClass() {
                return GameServerMaintenanceUpdatePacket.class;
            }
        });
    }

    public void registerGameServerExecutePacket() {
        getNetworkClient().registerPacketHandler(new IPacketHandler<Packet>() {
            @Override
            public void handlePacket(ChannelHandlerContext ctx, Packet obj) {
                GameServerExecuteCommandPacket gameServerExecutePacket = (GameServerExecuteCommandPacket) obj;
                bootstrapFunction.executeCommand(gameServerExecutePacket.getCommand());
            }

            @Override
            public Class<? extends Packet> getPacketClass() {
                return GameServerExecuteCommandPacket.class;
            }
        });
    }

    public void registerGameServerShutdownPacket() {
        getNetworkClient().registerPacketHandler(new IPacketHandler<Packet>() {
            @Override
            public void handlePacket(ChannelHandlerContext ctx, Packet obj) {
                bootstrapFunction.shutdown();
            }

            @Override
            public Class<? extends Packet> getPacketClass() {
                return GameServerShutdownPacket.class;
            }
        });
    }


}
