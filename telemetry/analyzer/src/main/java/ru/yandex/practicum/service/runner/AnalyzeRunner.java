package ru.yandex.practicum.service.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.service.processor.HubEventProcessor;
import ru.yandex.practicum.service.processor.SnapshotProcessor;

@Component
@RequiredArgsConstructor
public class AnalyzeRunner implements CommandLineRunner {

    private final HubEventProcessor hubEventProcessor;
    private final SnapshotProcessor snapshotProcessor;

    @Override
    public void run(String... args) {
        Thread hubThread = new Thread(hubEventProcessor, "HubEventHandlerThread");
        hubThread.start();

        Thread snapshotThread = new Thread(snapshotProcessor, "SnapshotProcessorThread");
        snapshotThread.start();
    }
}
