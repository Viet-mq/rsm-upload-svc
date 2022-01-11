package com.edso.resume.file.domain.repo;

import com.edso.resume.file.domain.entities.Profile;
import com.edso.resume.file.domain.request.DeleteCVRequest;
import com.edso.resume.file.domain.request.UpdateCVRequest;

import java.io.IOException;
import java.util.List;

public interface CvRepo {
    List<Profile> findAll();
    List<Profile> multiMatchQuery(String key, int size);
    Profile searchById(String id);
    String delete(DeleteCVRequest deleteCVRequest);
    String update(UpdateCVRequest updateCVRequest) throws IOException;
    void updateStatus(String id, String statusId, String statusName) throws IOException;
    void updateImages(String id, String url) throws IOException;
    void deleteImages(String id) throws IOException;
    void save(Profile profile);
    void saveContent(Profile profile);
}
