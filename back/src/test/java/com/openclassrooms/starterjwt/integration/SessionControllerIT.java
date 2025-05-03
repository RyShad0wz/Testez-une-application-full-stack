package com.openclassrooms.starterjwt.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
// On désactive les filtres Spring Security pour bypasser l’authent
@AutoConfigureMockMvc(addFilters = false)
class SessionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createListAndDeleteSession_endToEnd() throws Exception {
        // 1) Construction du payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Intégration Test");
        payload.put("description", "Desc longue");
        payload.put("date", "2025-12-31T10:00:00");
        // attention, le DTO attend snake_case
        payload.put("teacher_id", 1L);

        String jsonSession = objectMapper.writeValueAsString(payload);

        // 2) Création et récupération de l’ID
        MvcResult createResult = mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Intégration Test"))
                .andReturn();

        String body = createResult.getResponse().getContentAsString();
        JsonNode tree = objectMapper.readTree(body);
        long createdId = tree.get("id").asLong();

        // 3) Lecture de la liste
        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id==" + createdId + ")].name")
                        .value("Intégration Test"));

        // 4) Suppression de la session nouvellement créée
        mockMvc.perform(delete("/api/session/" + createdId))
                .andExpect(status().isOk());

        // 5) Vérification qu’elle n’apparaît plus
        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id==" + createdId + ")]").doesNotExist());
    }
}
