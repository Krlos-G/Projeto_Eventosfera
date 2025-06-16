package com.apievento.app.Event_Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface EventImageRepository extends JpaRepository<EventImage, Long> {
    List<EventImage> findByEventId(int id);

    @Modifying
    @Query("UPDATE EventImage ei SET ei.deletedAt = CURRENT_TIMESTAMP WHERE ei.event.id = :eventId")
    void softDeleteByEventId(@Param("eventId") Long eventId);

    @Query("SELECT ei FROM EventImage ei WHERE ei.event.id = :eventId AND ei.deletedAt IS NULL")
    List<EventImage> findByEventId(@Param("eventId") Long eventId);
}
