package com.example.ClientApp.controller;

import com.example.ClientApp.dto.CepResponse;
import com.example.ClientApp.dto.ClientRequest;
import com.example.ClientApp.dto.ClientResponse;
import com.example.ClientApp.service.ClientService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/client-list")
    public List<ClientResponse> getAll() {
        return clientService.getAllClients();

    }

    @GetMapping("/client/{id}")
    public ResponseEntity<ClientResponse> getByID(@PathVariable("id") Long id) {
        try {
            ClientResponse clientResponse = clientService.getClientById(id);
            return ResponseEntity.ok(clientResponse);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{cep}")
    public CepResponse getAddressByCep(@PathVariable String cep) {
        return clientService.getByCep(cep);
    }

    @GetMapping("/list-page")
    public Page<ClientResponse> listCLientPage (Pageable pageable){
        return clientService.listClientPage(pageable);
    }

    @GetMapping("/search")
    public Page<ClientResponse> getPeople(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer idade,
            @RequestParam(required = false) String cep,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return clientService.findPeopleByFilters(nome, idade, cep, page, size);
    }


    @PostMapping("/client-add")
    public void saveClientByCep(@RequestBody ClientRequest clientRequest) {
        clientService.addClient(clientRequest);
    }


    @PutMapping("/update-client/{id}")
    public ClientRequest upDateById(@PathVariable("id") Long id, @RequestBody ClientRequest clientRequest) {
        return clientService.updateByID(id,clientRequest);

    }

    @DeleteMapping("/delete-by-{id}")
    public void deleteById(@PathVariable("id") Long id) {
        clientService.deleteByID(id);
    }

}
