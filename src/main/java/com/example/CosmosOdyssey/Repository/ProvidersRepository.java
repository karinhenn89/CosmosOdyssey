package com.example.CosmosOdyssey.Repository;

import com.example.CosmosOdyssey.Model.Providers;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvidersRepository extends CrudRepository<Providers, String> {
    List<Providers> findByLegIdIn(List<String> legIds);
}
