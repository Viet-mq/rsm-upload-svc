package com.edso.resume.file;

import com.edso.resume.file.service.CVService;
import com.edso.resume.file.domain.entities.CV;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class Test implements CommandLineRunner {

    private final CVService cvService;

    public Test(CVService cvService) {
        this.cvService = cvService;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            String id = UUID.randomUUID().toString();
            CV cv = new CV();
            cv.setId(id);
            cv.setName("dautv");
            cv.setProfileId("212131");
            cv.setPathFile("/home/dautv");
            cv.setContent("Senior, ....");
            cvService.saveCv(cv);
            List<CV> cvs = cvService.findAllByName(id);
            System.out.println(cvs);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
