package com.peated.valhack.lambda.messages;

public record ActionGroupFunctionResponseEvent(
        String messageVersion,
        ActionGroupResponse response
) {
}
