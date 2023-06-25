package tudelft.ewi.cse2000.ruisdael.monitoring.component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import tudelft.ewi.cse2000.ruisdael.monitoring.configurations.ApplicationConfig;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Alert;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Device;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Status;
import tudelft.ewi.cse2000.ruisdael.monitoring.repositories.AlertRepository;
import tudelft.ewi.cse2000.ruisdael.monitoring.service.ElasticsearchService;



@Component
@EnableScheduling
public class AlertComponent {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private AlertRepository alertRepository;

    enum Flag {
        GREEN,
        YELLOW,
        RED
    }


    /*
     * Keeps the last flag for each metric for each device.
     */
    private HashMap<String, HashMap<String, Flag>> statusFlags = new HashMap<String, HashMap<String, Flag>>();
    

    /**
     * Constantly get new flags for each device and compare against 
     * the previous flags kept in statusFlags computed with getFlagsOf(device).
     */
    @Scheduled(fixedRate = 1000) //Every 1s
    public void runAlertDetection() {
        //Get the devices from elastic
        List<Device> newDevices = elasticsearchService.getAllDevices();
        for (Device d : newDevices) {
            // If this device hasn't been recorded before make a new entry
            if (!statusFlags.containsKey(d.getName())) {
                statusFlags.put(d.getName(), getFlagsOf(d));
            } else {
                //Otherwise fetch its previous flag values
                //And compare with the new ones
                //If there are transitions save to the db 
                HashMap<String, Flag> oldFlags = statusFlags.get(d.getName());
                HashMap<String, Flag> newFlags = getFlagsOf(d);
                for (String metric: oldFlags.keySet()) {
                    Flag oldFlag = oldFlags.get(metric);
                    Flag newFlag = newFlags.get(metric);
                    if (oldFlag != newFlag) {
                        alertRepository.save(new Alert(0, d.getName(), metric, oldFlag.toString(), newFlag.toString(), LocalDateTime.now()));
                    }
                }
                statusFlags.put(d.getName(), newFlags);
            }
        }
    }

    /*
     * Converts device metric values to flags.
     * @param d  - device
     */
    HashMap<String, Flag> getFlagsOf(Device d) {
        

        Flag upStatusFlag = null;
        if (d.getStatus() == Status.ONLINE) {
            upStatusFlag = Flag.GREEN;
        }
        if (d.getStatus() == Status.WARNING) {
            upStatusFlag = Flag.YELLOW;
        }
        if (d.getStatus() == Status.OFFLINE) {
            upStatusFlag = Flag.RED;
        }
        
        final double cpuVal = d.getCpuUsage();
        final double ramVal = d.getRam().getAvailablePercentage();
        final double storageVal = d.getStorage().getUsedPercStorage();


        Flag cpuFlag = Flag.GREEN;
        Flag ramFlag = Flag.GREEN;
        Flag storageFlag = Flag.GREEN;

        if (cpuVal > ApplicationConfig.CPU_CRITICAL_THRESHOLD) { 
            cpuFlag = Flag.RED;
        } else if (cpuVal > ApplicationConfig.CPU_WARNING_THRESHOLD) {
            cpuFlag = Flag.YELLOW;
        }

        if (ramVal > ApplicationConfig.RAM_CRITICAL_THRESHOLD) {
            ramFlag = Flag.RED;
        } else if (ramVal > ApplicationConfig.RAM_WARNING_THRESHOLD) {
            ramFlag = Flag.YELLOW;
        }

        if (storageVal > ApplicationConfig.STORAGE_CRITICAL_THRESHOLD) {
            storageFlag = Flag.RED;
        } else if (storageVal > ApplicationConfig.STORAGE_WARNING_THRESHOLD) {
            storageFlag = Flag.YELLOW;
        }

        HashMap<String, Flag> metricFlags = new HashMap<String, Flag>();

        metricFlags.put("UP-STATUS", upStatusFlag);
        metricFlags.put("CPU", cpuFlag);
        metricFlags.put("RAM", ramFlag);
        metricFlags.put("STORAGE", storageFlag);

        return metricFlags;
    }

}
