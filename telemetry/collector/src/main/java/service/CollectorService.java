package service;

import dto.hub.HubEvent;
import dto.sensor.SensorEvent;

public interface CollectorService {
    void createEventFromSensor(SensorEvent sensorEvent);

    void createEventFromHub(HubEvent hubEvent);
}
