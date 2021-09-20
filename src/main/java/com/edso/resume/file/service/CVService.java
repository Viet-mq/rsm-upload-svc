package com.edso.resume.file.service;

import com.edso.resume.file.domain.entities.CV;

import java.util.List;

public interface CVService {
    void saveCv(CV cv);
    List<CV> findAllByName(String name);
}
