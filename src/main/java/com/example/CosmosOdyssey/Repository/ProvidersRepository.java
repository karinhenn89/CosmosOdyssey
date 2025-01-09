package com.example.CosmosOdyssey.Repository;

import com.example.CosmosOdyssey.Model.Providers;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProvidersRepository extends CrudRepository<Providers, String> {
    List<Providers> findByLegIdIn(List<String> legIds);
}
