package com.peated.valhack.lambda;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public record BedrockAgentEvent(
        String agent,
        String actionGroup,
        String function,
        List<BedrockAgentParameter> parameters
) {
    static BedrockAgentEvent fromEventMap(Map<String, String> event) {
        return new BedrockAgentEvent(
                event.get("agent"),
                event.get("actionGroup"),
                event.get("function"),
                Collections.emptyList() //event.get("parameters")
        );
    }
}
