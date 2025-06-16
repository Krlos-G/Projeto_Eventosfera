package com.apievento.app.Event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // Busca com múltiplos filtros
    @Query("SELECT e FROM Event e WHERE " +
            "(:state IS NULL OR e.state = :state) AND " +
            "(:city IS NULL OR e.city = :city) AND " +
            "(:region IS NULL OR e.region = :region) AND " +
            "(:date IS NULL OR e.eventDateInitial = :date) AND " +
            "e.deletedAt IS NULL")
    List<Event> findWithFilters(
            @Param("state") String state,
            @Param("city") String city,
            @Param("region") String region,
            @Param("date") Date date);

    // Busca por intervalo de datas
    @Query("SELECT e FROM Event e WHERE " +
            "e.eventDateInitial BETWEEN :startDate AND :endDate " +
            "AND e.deletedAt IS NULL")
    List<Event> findByDateRange(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    @Query("SELECT e FROM Event e WHERE e.deletedAt IS NULL")
    List<Event> findAllByDeletedAtIsNull();

    @Modifying
    @Query("UPDATE Event e SET e.deletedAt = CURRENT_TIMESTAMP WHERE e.organizer.id = :organizerId")
    void softDeleteByOrganizerId(@Param("organizerId") int organizerId);

    // Para buscar incluindo deletados (quando necessário)
    @Query("SELECT e FROM Event e WHERE e.id = :id")
    Optional<Event> findByIdIncludingDeleted(@Param("id") int id);
}
