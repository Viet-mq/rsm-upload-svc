package com.edso.resume.file.domain.request;

import com.edso.resume.lib.response.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.elasticsearch.common.Strings;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class UpdateCVRequest extends BaseAuthRequest {
    private String id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private Long dateOfBirth;
    private String hometown;
    private String schoolId;
    private String schoolName;
    private String jobId;
    private String jobName;
    private String levelJobId;
    private String levelJobName;
    private String cv;
    private String sourceCVId;
    private String sourceCVName;
    private String hrRef;
    private Long dateOfApply;
    private String cvType;
    private String statusCVId;
    private String statusCVName;
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
        if(Strings.isNullOrEmpty(schoolId)) {
            return new BaseResponse(-1, "Vui lòng nhập trường school");
        }
        if(Strings.isNullOrEmpty(schoolName)) {
            return new BaseResponse(-1, "Vui lòng nhập trường schoolName");
        }
        if(Strings.isNullOrEmpty(jobId)) {
            return new BaseResponse(-1, "Vui lòng nhập trường jobId");
        }
        if(Strings.isNullOrEmpty(jobName)) {
            return new BaseResponse(-1, "Vui lòng nhập trường jobName");
        }
        if(Strings.isNullOrEmpty(levelJobId)) {
            return new BaseResponse(-1, "Vui lòng nhập trường levelJobId");
        }
        if(Strings.isNullOrEmpty(levelJobName)) {
            return new BaseResponse(-1, "Vui lòng nhập trường levelJobName");
        }
        if(Strings.isNullOrEmpty(cv)) {
            return new BaseResponse(-1, "Vui lòng nhập trường Profile");
        }
        if(Strings.isNullOrEmpty(sourceCVId)) {
            return new BaseResponse(-1, "Vui lòng nhập trường sourceCVId");
        }
        if(Strings.isNullOrEmpty(sourceCVName)) {
            return new BaseResponse(-1, "Vui lòng nhập trường sourceCVName");
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

        return null;
    }
}
