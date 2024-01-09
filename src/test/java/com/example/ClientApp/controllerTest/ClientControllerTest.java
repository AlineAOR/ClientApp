package com.example.ClientApp.controllerTest;

import com.example.ClientApp.controller.ClientController;
import com.example.ClientApp.dto.ClientResponse;
import com.example.ClientApp.service.ClientService;
import com.example.ClientApp.service.ScoreService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {
    @InjectMocks
    private ClientController clientController;

    @Mock
    private ClientService clientService;

    @Mock
    private ScoreService scoreService;

    @Test
    void getAll_ValidClients_ReturnsOk() {
        // Arrange
        List<ClientResponse> clients = new ArrayList<>();
        clients.add(mock(ClientResponse.class));
        clients.add(mock(ClientResponse.class));
        when(clientService.getAllClients()).thenReturn(clients);
        when(scoreService.getDescriptionScore(anyInt())).thenReturn(null);


        ResponseEntity<List<ClientResponse>> response = clientController.getAll();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(null, response.getBody().get(0).getDescriptscore());
        assertEquals(null, response.getBody().get(1).getDescriptscore());
    }


    @Test
    void deleteById_ValidId_ReturnsOk() {
        // Arrange
        Long clientId = 1L;


        ResponseEntity<String> response = clientController.deleteById(clientId);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cliente removido com sucesso.", response.getBody());
        verify(clientService, times(1)).deleteByID(clientId);
    }

    @Test
    void deleteById_NonExistentId_ReturnsNotFound() {

        Long clientId = 1L;
        doThrow(EntityNotFoundException.class).when(clientService).deleteByID(clientId);


        ResponseEntity<String> response = clientController.deleteById(clientId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteById_ExceptionDuringDeletion_ReturnsInternalServerError() {

        Long clientId = 1L;
        doThrow(new RuntimeException("Mensagem de erro")).when(clientService).deleteByID(clientId);


        ResponseEntity<String> response = clientController.deleteById(clientId);


        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Erro ao remover o cliente.", response.getBody());
    }

}

