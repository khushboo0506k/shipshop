package com.kns.shipshop.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kns.shipshop.persistence.entity.Items;

@Repository
public interface ItemsRepository extends JpaRepository<Items, Integer> {

}
