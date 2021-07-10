package de.polocloud.bootstrap.client;

import java.util.Collection;
import java.util.List;

public interface IWrapperClientManager {

    List<WrapperClient> getWrapperClients();

    void registerWrapperClient(WrapperClient wrapperClient);

    void removeWrapper(WrapperClient wrapperClient);
}
