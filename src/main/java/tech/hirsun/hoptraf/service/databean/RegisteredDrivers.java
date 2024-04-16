package tech.hirsun.hoptraf.service.databean;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class RegisteredDrivers {

    private Set<String> driversSet = new HashSet<>();

    public void addDriver(String driver) {
        driversSet.add(driver);
    }

    public Set AllDrivers() {
        return driversSet;
    }

    public void removeAllDrivers() {
        driversSet.clear();
    }


}
