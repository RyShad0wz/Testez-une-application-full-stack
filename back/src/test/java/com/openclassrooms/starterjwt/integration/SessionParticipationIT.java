package com.openclassrooms.starterjwt.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class SessionParticipationIT {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    private long sessionId;
    private long userId = 1L; // on suppose que l'user #1 existe

    @BeforeEach
    void createSession() throws Exception {
        Map<String,Object> payload = new HashMap<>();
        payload.put("name", "Participe Test");
        payload.put("description", "Desc");
        payload.put("date", "2025-12-01T10:00:00");
        payload.put("teacher_id", 1L);

        String json = objectMapper.writeValueAsString(payload);
        MvcResult res = mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode tree = objectMapper.readTree(res.getResponse().getContentAsString());
        sessionId = tree.get("id").asLong();
    }

    @Test
    void participateAndUnparticipate() throws Exception {
        // 1) Participer
        mockMvc.perform(post("/api/session/{sid}/participate/{uid}", sessionId, userId))
                .andExpect(status().isOk());

        // 2) Vérifier que l’ID apparaît dans le JSON
        mockMvc.perform(get("/api/session/{sid}", sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[0]").value(userId));

        // 3) Se désinscrire
        mockMvc.perform(delete("/api/session/{sid}/participate/{uid}", sessionId, userId))
                .andExpect(status().isOk());

        // 4) Vérifier que le tableau est à nouveau vide
        mockMvc.perform(get("/api/session/{sid}", sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users").isEmpty());
    }
}
