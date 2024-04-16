package tech.hirsun.hoptraf.redis;

public interface KeyPrefix {

    public int getExpireSeconds();
    public String getPrefix();

}
