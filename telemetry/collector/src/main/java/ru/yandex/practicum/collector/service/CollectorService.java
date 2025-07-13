package ru.yandex.practicum.collector.service;

import ru.yandex.practicum.collector.dto.hub.HubEvent;
import ru.yandex.practicum.collector.dto.sensor.SensorEvent;

public interface CollectorService {
    void createEventFromSensor(SensorEvent sensorEvent);

    void createEventFromHub(HubEvent hubEvent);
}
