package dto.hub.scenario;

import dto.hub.HubEvent;
import dto.hub.HubEventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString(callSuper = true)
public class ScenarioAddedEvent extends HubEvent {
    @NotBlank(message = "Scenario name can not be blank")
    @Size(min = 3, message = "Scenario name can not be < 3")
    private String name;

    @NotEmpty(message = "Conditions list can not be Empty")
    private List<ScenarioCondition> conditions;

    @NotEmpty(message = "Action list can not be Empty")
    private List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
