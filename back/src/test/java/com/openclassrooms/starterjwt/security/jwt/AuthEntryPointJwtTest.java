package com.openclassrooms.starterjwt.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AuthEntryPointJwtTest {

    @Test
    void commence_ShouldSetProperResponse() throws IOException, ServletException {
        // Arrange
        AuthEntryPointJwt authEntryPointJwt = new AuthEntryPointJwt();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = Mockito.mock(AuthenticationException.class);
        when(authException.getMessage()).thenReturn("Error message");

        // Act
        authEntryPointJwt.commence(request, response, authException);

        // Assert
        assertEquals(401, response.getStatus());
        assertEquals("application/json", response.getContentType());

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> body = mapper.readValue(response.getContentAsString(), Map.class);

        assertEquals(401, body.get("status"));
        assertEquals("Unauthorized", body.get("error"));
        assertEquals("Error message", body.get("message"));
        assertEquals("/test", body.get("path"));
    }
}