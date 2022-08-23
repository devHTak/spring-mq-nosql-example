package com.example.log.repository;

import com.example.log.entity.WasLog;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WasLogRepository extends CrudRepository<WasLog, String> {

    List<WasLog> findByCreatedAtLessThan(LocalDateTime nowDate);

}
