package tudelft.ewi.cse2000.ruisdael.monitoring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Device;

import java.util.List;

@Service
public class DeviceService {

    public ObjectMapper encodeDevices(List<Device> devices) {
        return null;
    }
}
