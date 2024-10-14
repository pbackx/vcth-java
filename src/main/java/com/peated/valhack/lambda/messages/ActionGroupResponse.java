package com.peated.valhack.lambda.messages;

public record ActionGroupResponse(
        String actionGroup,
        String function,
        FunctionResponse functionResponse
) {
}
