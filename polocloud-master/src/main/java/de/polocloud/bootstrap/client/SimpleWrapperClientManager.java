package de.polocloud.bootstrap.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleWrapperClientManager implements IWrapperClientManager {

    private List<WrapperClient> clientList = new ArrayList<>();


    @Override
    public List<WrapperClient> getWrapperClients() {
        return this.clientList;
    }

    @Override
    public void registerWrapperClient(WrapperClient wrapperClient) {
        clientList.add(wrapperClient);
    }

    @Override
    public void removeWrapper(WrapperClient wrapperClient) {
        clientList.remove(wrapperClient);
    }

}
