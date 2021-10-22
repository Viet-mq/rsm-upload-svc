package com.edso.resume.file.controller;

import com.edso.resume.file.domain.entities.Profile;
import com.edso.resume.file.domain.request.DeleteCVRequest;
import com.edso.resume.file.domain.request.UpdateCVRequest;
import com.edso.resume.file.domain.request.UploadCVRequest;
import com.edso.resume.file.service.CVService;
import com.edso.resume.file.service.UploadCVService;
import com.edso.resume.lib.entities.HeaderInfo;
import com.edso.resume.lib.response.BaseResponse;
import com.edso.resume.lib.response.GetArrayResponse;
import com.edso.resume.lib.utils.ParseHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/upload")
public class UploadController extends BaseController {

    @Autowired
    private final UploadCVService uploadCVService;

    @Autowired
    CVService cvService;

    public UploadController(UploadCVService uploadCVService) {
        this.uploadCVService = uploadCVService;
    }

    @PostMapping("/upload-cv")
    public BaseResponse uploadCV(
            @RequestHeader Map<String, String> headers,
            @RequestParam("profile_id") String profileId,
            @RequestParam("image") MultipartFile multipartFile) {
        HeaderInfo headerInfo = ParseHeaderUtil.build(headers);
        logger.info("=>uploadCV u: {}, profileId: {}", headerInfo, profileId);
        UploadCVRequest req = UploadCVRequest.builder()
                .headerInfo(headerInfo)
                .profileId(profileId)
                .file(multipartFile)
                .build();
        return uploadCVService.uploadCV(req);
    }

    @GetMapping("/view")
    public GetArrayResponse<Profile> viewAll (@RequestHeader Map<String, String> headers,
                                              @RequestParam(required = false) String key) {
        HeaderInfo headerInfo = ParseHeaderUtil.build(headers);
        if(key != null) {
            logger.info("=>viewAllProfile u: {}, key {}", headerInfo, key);
            return cvService.viewByKey(headerInfo, key);
        }
        else {
            logger.info("=>viewAllProfile u: {}", headerInfo);
            return cvService.viewAll(headerInfo);
        }
    }

    @DeleteMapping("/delete")
    public BaseResponse delete (@RequestHeader Map<String, String> headers, @RequestBody DeleteCVRequest deleteCVRequest){
        HeaderInfo headerInfo = ParseHeaderUtil.build(headers);
        BaseResponse response = new BaseResponse();
        if(deleteCVRequest == null){
            response.setResult(-1, "Vui lòng nhập đầy đủ thông tin");
        }
        else {
            response = deleteCVRequest.validate();
            if(response == null){
                response = cvService.delete(deleteCVRequest);
            }
        }
        logger.info("Delete Profile u: {}, req: {}, res: {}", headerInfo, uploadCVService, response);
        return response;
    }

    @PutMapping("/update")
    public BaseResponse update (@RequestHeader Map<String, String> headers, @RequestBody UpdateCVRequest updateCVRequest){
        HeaderInfo headerInfo = ParseHeaderUtil.build(headers);
        BaseResponse response = new BaseResponse();
        if(updateCVRequest == null){
            response.setResult(-1, "Vui lòng điền đầy đủ thông tin");
        }
        else {
            response = updateCVRequest.validate();
            if(response == null){
                response = cvService.update(updateCVRequest);
            }
        }
        logger.info("Update Profile u: {}, req: {}, res: {}", headerInfo, updateCVRequest, response);
        return response;
    }

}
