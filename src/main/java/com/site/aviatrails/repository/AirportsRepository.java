package com.site.aviatrails.repository;

import com.site.aviatrails.domain.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportsRepository extends JpaRepository<Airport, Long> {

    @Query("select a.id FROM airports a where a.portName=:portName and a.portCity=:portCity")
    Long findIdByPortNameAndPortCity(String portName, String portCity);

    @Query("select a.id FROM airports a where a.portCity=:portCity")
    List<Long> findIdsByPortCity(String portCity);

    @Query("select a.portName from airports a WHERE a.id=:id")
    String findPortNameById(long id);

}
