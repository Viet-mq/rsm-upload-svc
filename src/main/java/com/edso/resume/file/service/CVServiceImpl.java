package com.edso.resume.file.service;

import com.edso.resume.file.domain.entities.CV;
import com.edso.resume.file.domain.repo.CvRepo;
import com.edso.resume.file.domain.request.DeleteCVRequest;
import com.edso.resume.file.domain.request.UpdateCVRequest;
import com.edso.resume.lib.entities.HeaderInfo;
import com.edso.resume.lib.response.BaseResponse;
import com.edso.resume.lib.response.GetArrayResponse;
import lombok.SneakyThrows;
import org.elasticsearch.common.Strings;
import org.springframework.stereotype.Service;

import java.util.*;

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
        List<CV> cvList = new ArrayList<>();
        if(Strings.isNullOrEmpty(name)){

        }
        logger.info("a: {}", cvList);
        return null;
    }

    @Override
    public GetArrayResponse<CV> viewAll(HeaderInfo headerInfo) {
        List<CV> cvs = cvRepo.findAll();
        GetArrayResponse<CV> response = new GetArrayResponse<>();
        response.setSuccess(cvs.size(), cvs);
        logger.info("View all: {}" , cvs);
        return response;
    }

    @Override
    public GetArrayResponse<CV> viewById(HeaderInfo headerInfo, UUID id){
        Map<String, Object> cv = cvRepo.findById(id);
        GetArrayResponse<CV> response = new GetArrayResponse<>();
        Set<String> set = cv.keySet();
        for (String key : set) {
            response.setSuccess(1, (List<CV>) cv.get(key));
        }
        return response;
    }

    @SneakyThrows
    @Override
    public BaseResponse update(HeaderInfo header, UpdateCVRequest updateCVRequest){
        BaseResponse response = new BaseResponse();
        String result = cvRepo.update(updateCVRequest);
        response.setSuccess(result);
        return response;
    }

    @Override
    public BaseResponse delete(HeaderInfo headerInfo, DeleteCVRequest deleteCVRequest) {
        BaseResponse response = new BaseResponse();
        String result = cvRepo.delete(deleteCVRequest);
        response.setSuccess(result);
        return response;
    }
}
