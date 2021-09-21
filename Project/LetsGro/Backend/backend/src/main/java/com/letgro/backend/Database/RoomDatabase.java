package com.letgro.backend.Database;

import com.letgro.backend.Model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomDatabase extends JpaRepository<Room, Long> {
    Room findOneById(Long id);
}
