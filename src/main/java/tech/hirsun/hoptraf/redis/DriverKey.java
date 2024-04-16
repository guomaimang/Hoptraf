package tech.hirsun.hoptraf.redis;

public class DriverKey extends BasePrefix {
    private DriverKey(String prefix) {
        // 0 means never expire
        super(0, prefix);
    }
    public static DriverKey byId = new DriverKey("id");

}