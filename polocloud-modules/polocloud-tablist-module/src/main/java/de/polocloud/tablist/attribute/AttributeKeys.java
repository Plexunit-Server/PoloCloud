package de.polocloud.tablist.attribute;

public enum AttributeKeys {

    SERVICE_NAME,
    PROXY_NAME,
    ONLINE_COUNT,
    MAX_PLAYERS;

    private String display;

    AttributeKeys() {
        this.display = new StringBuilder("%").append(this).append("%").toString();
    }

    public String getDisplay() {
        return display;
    }
}
