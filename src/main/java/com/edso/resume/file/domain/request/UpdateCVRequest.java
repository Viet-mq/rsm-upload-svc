package com.edso.resume.file.domain.request;

import com.edso.resume.lib.response.BaseResponse;
import lombok.Data;
import org.elasticsearch.common.Strings;

@Data
public class UpdateCVRequest {
    private String id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private Long dateOfBirth;
    private String hometown;
    private String school;
    private String job;
    private String levelJob;
    private String cv;
    private String sourceCV;
    private String hrRef;
    private Long dateOfApply;
    private String cvType;
    private String statusCV;
    private String content;

    public BaseResponse validate(){
        if (Strings.isNullOrEmpty(id)) {
            return new BaseResponse(-1, "Vui lòng nhập trường Id");
        }
        if(Strings.isNullOrEmpty(fullName)) {
            return new BaseResponse(-1, "Vui lòng nhập trường name");
        }
        if(Strings.isNullOrEmpty(phoneNumber)) {
            return new BaseResponse(-1, "Vui lòng nhập trường phoneNumber");
        }
        if(Strings.isNullOrEmpty(email)) {
            return new BaseResponse(-1, "Vui lòng nhập trường email");
        }
        if(Strings.isNullOrEmpty(String.valueOf(dateOfBirth))) {
            return new BaseResponse(-1, "Vui lòng nhập trường ngày sinh");
        }
        if(Strings.isNullOrEmpty(hometown)) {
            return new BaseResponse(-1, "Vui lòng nhập trường quê quán");
        }
        if(Strings.isNullOrEmpty(school)) {
            return new BaseResponse(-1, "Vui lòng nhập trường school");
        }
        if(Strings.isNullOrEmpty(job)) {
            return new BaseResponse(-1, "Vui lòng nhập trường job");
        }
        if(Strings.isNullOrEmpty(levelJob)) {
            return new BaseResponse(-1, "Vui lòng nhập trường levelJob");
        }
        if(Strings.isNullOrEmpty(cv)) {
            return new BaseResponse(-1, "Vui lòng nhập trường CV");
        }
        if(Strings.isNullOrEmpty(sourceCV)) {
            return new BaseResponse(-1, "Vui lòng nhập trường sourceCV");
        }
        if(Strings.isNullOrEmpty(hrRef)) {
            return new BaseResponse(-1, "Vui lòng nhập trường hrRef");
        }
        if(Strings.isNullOrEmpty(String.valueOf(dateOfApply))) {
            return new BaseResponse(-1, "Vui lòng nhập trường dateOfApply");
        }
        if(Strings.isNullOrEmpty(cvType)) {
            return new BaseResponse(-1, "Vui lòng nhập trường cvType");
        }
        if(Strings.isNullOrEmpty(statusCV)) {
            return new BaseResponse(-1, "Vui lòng nhập trường statusCV");
        }

        return null;
    }
}
