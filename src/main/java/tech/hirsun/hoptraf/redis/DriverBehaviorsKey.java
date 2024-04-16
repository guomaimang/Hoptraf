package tech.hirsun.hoptraf.redis;

public class DriverBehaviorsKey extends BasePrefix{
    private DriverBehaviorsKey(String prefix) {
        // 0 means never expire
        super(25, prefix);
    }
    public static DriverBehaviorsKey byId = new DriverBehaviorsKey("id");
    public static DriverBehaviorsKey attributeById = new DriverBehaviorsKey("elementById");
}
