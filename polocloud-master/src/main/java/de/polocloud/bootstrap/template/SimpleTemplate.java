package de.polocloud.bootstrap.template;

import de.polocloud.api.template.GameServerVersion;
import de.polocloud.api.template.ITemplate;
import de.polocloud.api.template.TemplateType;

public class SimpleTemplate implements ITemplate {

    private String name;

    private int maxServerCount;
    private int minServerCount;

    private TemplateType templateType;

    private GameServerVersion version;

    private String[] wrapperNames;

    public SimpleTemplate(String name, int maxServerCount, int minServerCount, TemplateType templateType, GameServerVersion version, String[] wrapperNames){
        this.name = name;
        this.maxServerCount = maxServerCount;
        this.minServerCount = minServerCount;
        this.templateType = templateType;
        this.version = version;
        this.wrapperNames = wrapperNames;
    }

    @Override
    public GameServerVersion getVersion() {
        return version;
    }

    @Override
    public String[] getWrapperNames() {
        return this.wrapperNames;
    }

    @Override
    public int getMinServerCount() {
        return this.minServerCount;
    }

    @Override
    public int getMaxServerCount() {
        return this.maxServerCount;
    }

    @Override
    public TemplateType getTemplateType() {
        return this.templateType;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
