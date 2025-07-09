package ru.yandex.practicum.collector;

import ru.yandex.practicum.collector.dto.hub.HubEvent;
import ru.yandex.practicum.collector.dto.sensor.SensorEvent;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.collector.service.CollectorService;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/events")
public class CollectorController {
    private final CollectorService collectorService;

    @PostMapping("/sensors")
    public void createEventFromSensor(@RequestBody @Valid SensorEvent sensorEvent) {
        collectorService.createEventFromSensor(sensorEvent);
    }

    @PostMapping("/hubs")
    public void createEventFromHub(@RequestBody @Valid HubEvent hubEvent) {
        collectorService.createEventFromHub(hubEvent);
    }
}
