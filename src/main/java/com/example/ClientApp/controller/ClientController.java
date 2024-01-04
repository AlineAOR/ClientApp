package com.example.ClientApp.controller;

import com.example.ClientApp.dto.CepResponse;
import com.example.ClientApp.dto.ClientRequest;
import com.example.ClientApp.dto.ClientResponse;
import com.example.ClientApp.service.ClientService;
import com.example.ClientApp.service.ScoreService;
import io.swagger.annotations.Api;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app")
@Api(tags = "Exemplo de API", description = "Exemplo de API para demonstração")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ScoreService scoreService;

    @GetMapping("/client-list")
    public ResponseEntity<List<ClientResponse>> getAll() {
        try {
            List<ClientResponse> clients = clientService.getAllClients();

            for (ClientResponse client : clients) {
                int pontuacao = client.getScore();
                String descricaoScore = scoreService.getDescriptionScore(pontuacao);
                client.setDescriptscore(descricaoScore);
            }

            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<ClientResponse> getByID(@PathVariable("id") Long id) {
        try {
            ClientResponse clientResponse = clientService.getClientById(id);

            int pontuacao = clientResponse.getScore();

            String descriptionScore = scoreService.getDescriptionScore(pontuacao);

            clientResponse.setDescriptscore(descriptionScore);

            return ResponseEntity.ok(clientResponse);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{cep}")
    public ResponseEntity<CepResponse> getAddressByCep(@PathVariable String cep) {
        try {
            CepResponse cepResponse = clientService.getByCep(cep);
            return ResponseEntity.ok(cepResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/list-page")
    public ResponseEntity<Page<ClientResponse>> listClientPage(Pageable pageable) {
        try {
            Page<ClientResponse> clientPage = clientService.listClientPage(pageable);

            clientPage.getContent().forEach(client -> {
                int pontuacao = client.getScore();
                String descricaoScore = scoreService.getDescriptionScore(pontuacao);
                client.setDescriptscore(descricaoScore);
            });

            return ResponseEntity.ok(clientPage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ClientResponse>> getPeople(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer idade,
            @RequestParam(required = false) String cep,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<ClientResponse> resultPage = clientService.findPeopleByFilters(nome, idade, cep, page, size);

            resultPage.getContent().forEach(client -> {
                int pontuacao = client.getScore();
                String descricaoScore = scoreService.getDescriptionScore(pontuacao);
                client.setDescriptscore(descricaoScore);
            });

            return ResponseEntity.ok(resultPage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/client-add")
    public ResponseEntity<String> saveClientByCep(@RequestBody ClientRequest clientRequest) {
        try {
            clientService.addClient(clientRequest);
            return ResponseEntity.ok("Cliente adicionado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar o cliente.");
        }
    }

    @PutMapping("/update-client/{id}")
    public ResponseEntity<ClientRequest> upDateById(@PathVariable("id") Long id, @RequestBody ClientRequest clientRequest) {
        try {
            return ResponseEntity.ok(clientService.updateByID(id, clientRequest));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete-by-{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        try {
            clientService.deleteByID(id);
            return ResponseEntity.ok("Cliente removido com sucesso.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao remover o cliente.");
        }
    }
}