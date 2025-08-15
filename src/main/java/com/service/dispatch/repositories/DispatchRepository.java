package com.service.dispatch.repositories;

import com.service.dispatch.entities.DispatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DispatchRepository  extends JpaRepository<DispatchEntity, Long> {
}
