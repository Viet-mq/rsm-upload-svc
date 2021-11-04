package com.edso.resume.file.domain.request;

import com.edso.resume.lib.entities.HeaderInfo;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@Builder
public class UploadCVRequest extends BaseAuthRequest {
    private HeaderInfo headerInfo;
    private String profileId;
    private MultipartFile file;
}
