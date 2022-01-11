package com.edso.resume.file.domain.repo;

import com.edso.resume.file.domain.entities.OutlookSessionEntity;
import com.edso.resume.lib.exception.SessionException;
import org.springframework.stereotype.Repository;

@Repository
public class SessionRepository {

    private final SessionEntityRepo sessionEntityRepo;

    public SessionRepository(SessionEntityRepo sessionEntityRepo) {
        this.sessionEntityRepo = sessionEntityRepo;
    }

    public synchronized void addSession(OutlookSessionEntity sessionEntity) {
        sessionEntityRepo.deleteById(sessionEntity.getId());
        sessionEntityRepo.save(sessionEntity);
    }

    public synchronized String getToken(String id) throws SessionException {
        OutlookSessionEntity sessionEntity = sessionEntityRepo.findById(id).orElse(null);
        if (sessionEntity == null) {
            throw new SessionException("Session not existed -> refresh token again");
        }
        return sessionEntity.getToken();
    }

}