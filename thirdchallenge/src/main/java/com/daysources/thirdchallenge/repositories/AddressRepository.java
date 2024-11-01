package com.daysources.thirdchallenge.repositories;

import com.daysources.thirdchallenge.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
