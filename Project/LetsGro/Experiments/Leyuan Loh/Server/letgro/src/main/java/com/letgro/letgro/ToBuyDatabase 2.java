package com.letgro.letgro;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ToBuyDatabase extends JpaRepository<ToBuy,Integer> {
}
