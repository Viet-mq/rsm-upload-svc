package com.edso.resume.file.service;

import com.edso.resume.file.domain.entities.CV;
import com.edso.resume.file.domain.repo.CvRepo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CVServiceImpl extends BaseService implements CVService {

    private final CvRepo cvRepo;

    public CVServiceImpl(CvRepo cvRepo) {
        this.cvRepo = cvRepo;
    }

    @Override
    public void saveCv(CV cv) {
        cvRepo.save(cv);
    }

    @Override
    public List<CV> findAllByName(String name) {
        List<CV> all = cvRepo.findAll();
        logger.info("a: {}", all);
        return null;
    }

}
