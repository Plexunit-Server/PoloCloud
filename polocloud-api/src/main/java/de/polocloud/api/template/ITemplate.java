package de.polocloud.api.template;

import de.polocloud.api.common.INamable;

public interface ITemplate extends INamable {

    int getMinServerCount();

    int getMaxServerCount();

    TemplateType getTemplateType();

    GameServerVersion getVersion();

    String[] getWrapperNames();

}
