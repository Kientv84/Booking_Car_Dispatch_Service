package com.service.dispatch.repositories;

import com.service.dispatch.entities.DispatchLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DispatchLogRepository extends JpaRepository<DispatchLogEntity, Long > {
}
