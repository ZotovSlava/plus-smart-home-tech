package ru.yandex.practicum.model.scenario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.Sensor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scenario_conditions")
public class ScenarioCondition {

    @EmbeddedId
    private ScenarioConditionKey id;

    @ManyToOne
    @MapsId("scenarioId")
    @JoinColumn(name = "scenario_id")
    private Scenario scenario;

    @ManyToOne
    @MapsId("sensorId")
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    @ManyToOne
    @MapsId("conditionId")
    @JoinColumn(name = "condition_id")
    private Condition condition;
}
