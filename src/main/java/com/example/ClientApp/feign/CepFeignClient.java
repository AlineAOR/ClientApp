package com.example.ClientApp.feign;


import com.example.ClientApp.dto.CepResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@org.springframework.cloud.openfeign.FeignClient(name = "viacep", url = "https://viacep.com.br/ws")
public interface CepFeignClient {

    @GetMapping("/{cep}/json")
    CepResponse getByCep(@PathVariable String cep);

    @PostMapping("/save")
    void saveAddress(@RequestBody CepResponse CepResponse);
}


