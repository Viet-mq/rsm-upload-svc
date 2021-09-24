package com.edso.resume.file.domain.request;

import com.edso.resume.lib.response.BaseResponse;
import lombok.Data;
import org.elasticsearch.common.Strings;

@Data
public class DeleteCVRequest {
    private String id;

    public BaseResponse validate(){
        if(Strings.isNullOrEmpty(id)){
            return new BaseResponse(-1, "Vui lòng chọn id");
        }
        return null;
    }
}
