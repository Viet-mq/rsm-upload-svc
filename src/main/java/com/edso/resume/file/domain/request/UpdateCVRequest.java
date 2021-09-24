package com.edso.resume.file.domain.request;

import com.edso.resume.lib.response.BaseResponse;
import lombok.Data;
import org.elasticsearch.common.Strings;

@Data
public class UpdateCVRequest {
    private String id;
    private String name;
    private String profileId;
    private String pathFile;
    private String content;

    public BaseResponse validate(){
        if (Strings.isNullOrEmpty(id)) {
            return new BaseResponse(-1, "Vui lòng nhập Id");
        }
        if(Strings.isNullOrEmpty(name)) {
            return new BaseResponse(-1, "Vui lòng nhập tên CV");
        }
        if(Strings.isNullOrEmpty(profileId)) {
            return new BaseResponse(-1, "Vui lòng nhập profile Id");
        }
        if(Strings.isNullOrEmpty(pathFile)) {
            return new BaseResponse(-1, "Vui lòng nhập đường dẫn đến file");
        }
        if(Strings.isNullOrEmpty(content)) {
            return new BaseResponse(-1, "Vui lòng nhập nội dung file");
        }

        return null;
    }
}
