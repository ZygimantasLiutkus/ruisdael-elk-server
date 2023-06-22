package tudelft.ewi.cse2000.ruisdael.monitoring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.stereotype.Service;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Device;

@Service
public class DeviceService {

    public ObjectMapper encodeDevices(List<Device> devices) {
        return null;
    }
}
