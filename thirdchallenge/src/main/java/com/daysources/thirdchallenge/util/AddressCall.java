package com.daysources.thirdchallenge.util;

import com.daysources.thirdchallenge.entities.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ViaCEP", url = "https://viacep.com.br/ws")
public interface AddressCall {

    @GetMapping("/{cep}/json")
    ResponseEntity<Address> getByPostal(@PathVariable("cep") String cep);
}
