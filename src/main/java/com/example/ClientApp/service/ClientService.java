package com.example.ClientApp.service;

import com.example.ClientApp.dto.CepResponse;
import com.example.ClientApp.dto.ClientRequest;
import com.example.ClientApp.dto.ClientResponse;
import com.example.ClientApp.entities.ClientEntity;
import com.example.ClientApp.feign.CepFeignClient;
import com.example.ClientApp.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CepFeignClient cepFeignClient;


    public ClientEntity addClient(ClientRequest clientRequest) {
        ClientEntity clientEntity = modelMapper.map(clientRequest, ClientEntity.class);

        CepResponse cepResponse = cepFeignClient.getByCep(clientEntity.getCep());

        clientEntity.setLogradouro(cepResponse.getLogradouro());
        clientEntity.setLocalidade(cepResponse.getLocalidade());
        clientEntity.setBairro(cepResponse.getBairro());
        clientEntity.setUf(cepResponse.getUf());

        return clientRepository.save(clientEntity);

    }

    public List<ClientResponse> getAllClients() {
        List<ClientEntity> clients = clientRepository.findAll();
        List<ClientResponse> clienResponseList = new ArrayList<>();

        for (ClientEntity client : clients) {
            ClientResponse clientDTO = modelMapper.map(client, ClientResponse.class);
            clienResponseList.add(clientDTO);
        }

        return clienResponseList;
    }


    public ClientResponse getClientById(Long id) {
        ClientEntity clientEntity = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id));
        return modelMapper.map(clientEntity, ClientResponse.class);
    }

    public void deleteByID(Long id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
        }
    }

    public ClientRequest updateByID(Long id, ClientRequest clientRequest) {
        ClientEntity existingClientEntity = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id));
        modelMapper.map(clientRequest, existingClientEntity);
        existingClientEntity.setId(id);
        ClientEntity clientEntity = clientRepository.save(existingClientEntity);
        return modelMapper.map(clientEntity, ClientRequest.class);

    }

    public CepResponse getByCep(String cep) {
        return cepFeignClient.getByCep(cep);
    }

    public Page<ClientResponse> listClientPage(Pageable pageable) {
        Page<ClientEntity> clientEntityPage = clientRepository.findAll(pageable);
        List<ClientResponse> clientResponses = new ArrayList<>();

        for (ClientEntity clientEntity : clientEntityPage.getContent()) {
            clientResponses.add(mapClient(clientEntity));

        }
        return new PageImpl<>(clientResponses, pageable, clientEntityPage.getTotalElements());

    }

    private ClientResponse mapClient(ClientEntity clientEntity) {
        return modelMapper.map(clientEntity, ClientResponse.class);
    }

    public Page<ClientResponse> findPeopleByFilters(String nome, Integer idade, String cep, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<ClientEntity> clientEntityPage = clientRepository.findByFilters(nome, idade, cep, pageable);

        List<ClientResponse> clientResponses = new ArrayList<>();
        List<ClientEntity> clientEntities = clientEntityPage.getContent();

        for (ClientEntity clientEntity : clientEntities) {
            ClientResponse clientResponse = modelMapper.map(clientEntity, ClientResponse.class);
            clientResponses.add(clientResponse);
        }

        return new PageImpl<>(clientResponses, clientEntityPage.getPageable(), clientEntityPage.getTotalElements());

    }
}