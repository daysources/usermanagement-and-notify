package com.daysources.thirdchallenge.util;

import com.daysources.thirdchallenge.dto.AddressDto;
import com.daysources.thirdchallenge.entities.Address;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor
public class AddressMapper {

    public static Address toAddress(AddressDto addressDto){
        return new ModelMapper().map(addressDto, Address.class);
    }

    public static AddressDto toDto(Address address){
        return new ModelMapper().map(address, AddressDto.class);
    }
}
