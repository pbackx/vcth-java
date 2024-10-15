package com.peated.valhack.val;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.peated.valhack.model.Role;
import org.springframework.stereotype.Component;

@Component
final public class AgentToRoleMapper {
    public record AgentData(String guid, String name, Role role) {}

    public static final List<AgentData> AGENTS = List.of(
        new AgentData("add6443a-41bd-e414-f6ad-e58d267f4e95", "Jett", Role.Duelist),
        new AgentData("a3bfb853-43b2-7238-a4f1-ad90e9e46bcc", "Reyna", Role.Duelist),
        new AgentData("f94c3b30-42be-e959-889c-5aa313dba261", "Raze", Role.Duelist),
        new AgentData("7f94d92c-4234-0a36-9646-3a87eb8b5c89", "Yoru", Role.Duelist),
        new AgentData("eb93336a-449b-9c1b-0a54-a891f7921d69", "Phoenix", Role.Duelist),
        new AgentData("bb2a4828-46eb-8cd1-e765-15848195d751", "Neon", Role.Duelist),
        new AgentData("5f8d3a7f-467b-97f3-062c-13acf203c006", "Breach", Role.Initiator),
        new AgentData("6f2a04ca-43e0-be17-7f36-b3908627744d", "Skye", Role.Initiator),
        new AgentData("320b2a48-4d9b-a075-30f1-1f93a9b638fa", "Sova", Role.Initiator),
        new AgentData("601dbbe7-43ce-be57-2a40-4abd24953621", "Kayo", Role.Initiator),
        new AgentData("1e58de9c-4950-5125-93e9-a0aee9f98746", "Killjoy", Role.Sentinel),
        new AgentData("117ed9e3-49f3-6512-3ccf-0cada7e3823b", "Cypher", Role.Sentinel),
        new AgentData("569fdd95-4d10-43ab-ca70-79becc718b46", "Sage", Role.Sentinel),
        new AgentData("22697a3d-45bf-8dd7-4fec-84a9e28c69d7", "Chamber", Role.Sentinel),
        new AgentData("8e253930-4c05-31dd-1b6c-968525494517", "Omen", Role.Controller),
        new AgentData("9f0d8ba9-4140-b941-57d3-a7ad57c6b417", "Brimstone", Role.Controller),
        new AgentData("41fb69c1-4189-7b37-f117-bcaf1e96f1bf", "Astra", Role.Controller),
        new AgentData("707eab51-4836-f488-046a-cda6bf494859", "Viper", Role.Controller),
        new AgentData("dade69b4-4f5a-8528-247b-219e5a1facd6", "Fade", Role.Initiator),
        new AgentData("95b78ed7-4637-86d9-7e41-71ba8c293152", "Harbor", Role.Controller),
        new AgentData("e370fa57-4757-3604-3648-499e1f642d3f", "Gekko", Role.Initiator),
        new AgentData("cc8b64c8-4b25-4ff9-6e7f-37b4da43d235", "Deadlock", Role.Sentinel),
        new AgentData("0e38b510-41a8-5780-5e8f-568b2a4f2d6c", "Iso", Role.Duelist),
        new AgentData("1dbf2edd-4729-0984-3115-daa5eed44993", "Clove", Role.Controller),
        new AgentData("efba5359-4016-a1e5-7626-b1ae76895940", "Vyse", Role.Sentinel)
    );

    private final Map<String, Integer> agentGuidtoRoleId;

    public AgentToRoleMapper() {
        agentGuidtoRoleId = AGENTS.stream().collect(Collectors.toMap(
            AgentData::guid, 
            agentData -> agentData.role.getId()
        ));
    }

    public Integer getRole(String agentGuid) {
        return agentGuidtoRoleId.get(agentGuid.toLowerCase());
    }
}
