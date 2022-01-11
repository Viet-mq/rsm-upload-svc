package com.edso.resume.file.domain.repo;

import com.edso.resume.file.domain.entities.OutlookSessionEntity;
import org.springframework.data.repository.CrudRepository;

public interface SessionEntityRepo extends CrudRepository<OutlookSessionEntity, String> {
}
