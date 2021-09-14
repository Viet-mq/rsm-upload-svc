package com.edso.resume.file.controller;

import com.edso.resume.file.domain.request.UploadCVRequest;
import com.edso.resume.file.service.UploadCVService;
import com.edso.resume.lib.entities.HeaderInfo;
import com.edso.resume.lib.response.BaseResponse;
import com.edso.resume.lib.utils.ParseHeaderUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/upload")
public class UploadController extends BaseController {

    private final UploadCVService uploadCVService;

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

}
