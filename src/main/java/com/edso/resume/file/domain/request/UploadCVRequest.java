package com.edso.resume.file.domain.request;

import com.edso.resume.lib.entities.HeaderInfo;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class UploadCVRequest {
    private HeaderInfo headerInfo;
    private String profileId;
    private MultipartFile file;
}
