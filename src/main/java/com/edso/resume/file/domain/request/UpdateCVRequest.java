package com.edso.resume.file.domain.request;

import com.edso.resume.lib.response.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.elasticsearch.common.Strings;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class UpdateCVRequest extends BaseAuthRequest {
    private String id;
    private String fullName;
    private String gender;
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
    private String talentPoolId;
    private String talentPoolName;
    private String content;
    private String schoolLevel;
    private String evaluation;
    private Long lastApply;
    private String departmentId;
    private String departmentName;
    private String urlCV;
    private String image;
    private String fileName;
    private String mailRef;
    private String mailRef2;
    private List<String> skill;
    private String levelSchool;
    private String recruitmentId;
    private String recruitmentName;
    private String avatarColor;
    private Boolean isNew;
    private Long time;
    private String linkedin;
    private String facebook;
    private String skype;
    private String github;
    private String otherTech;
    private String web;
    private String picId;
    private String picName;
    private String status;
    private String companyId;
    private String companyName;
    private String username;
    private List<String> followers;
    private List<String> tags;
    private String picMail;

    public BaseResponse validate() {
        if (Strings.isNullOrEmpty(id)) {
            return new BaseResponse(-1, "Vui lòng nhập trường Id");
        }
        if (Strings.isNullOrEmpty(fullName)) {
            return new BaseResponse(-1, "Vui lòng nhập trường name");
        }
        if (Strings.isNullOrEmpty(gender)) {
            return new BaseResponse(-1, "Vui lòng nhập trường gender");
        }
        if (Strings.isNullOrEmpty(phoneNumber)) {
            return new BaseResponse(-1, "Vui lòng nhập trường phoneNumber");
        }
        if (Strings.isNullOrEmpty(email)) {
            return new BaseResponse(-1, "Vui lòng nhập trường email");
        }
        if (Strings.isNullOrEmpty(String.valueOf(dateOfBirth))) {
            return new BaseResponse(-1, "Vui lòng nhập trường ngày sinh");
        }
        if (Strings.isNullOrEmpty(hometown)) {
            return new BaseResponse(-1, "Vui lòng nhập trường quê quán");
        }
        if (Strings.isNullOrEmpty(schoolId)) {
            return new BaseResponse(-1, "Vui lòng nhập trường school");
        }
        if (Strings.isNullOrEmpty(schoolName)) {
            return new BaseResponse(-1, "Vui lòng nhập trường schoolName");
        }
        if (Strings.isNullOrEmpty(jobId)) {
            return new BaseResponse(-1, "Vui lòng nhập trường jobId");
        }
        if (Strings.isNullOrEmpty(jobName)) {
            return new BaseResponse(-1, "Vui lòng nhập trường jobName");
        }
        if (Strings.isNullOrEmpty(levelJobId)) {
            return new BaseResponse(-1, "Vui lòng nhập trường levelJobId");
        }
        if (Strings.isNullOrEmpty(levelJobName)) {
            return new BaseResponse(-1, "Vui lòng nhập trường levelJobName");
        }
        if (Strings.isNullOrEmpty(cv)) {
            return new BaseResponse(-1, "Vui lòng nhập trường Profile");
        }
        if (Strings.isNullOrEmpty(sourceCVId)) {
            return new BaseResponse(-1, "Vui lòng nhập trường sourceCVId");
        }
        if (Strings.isNullOrEmpty(sourceCVName)) {
            return new BaseResponse(-1, "Vui lòng nhập trường sourceCVName");
        }
        if (Strings.isNullOrEmpty(hrRef)) {
            return new BaseResponse(-1, "Vui lòng nhập trường hrRef");
        }
        if (Strings.isNullOrEmpty(String.valueOf(dateOfApply))) {
            return new BaseResponse(-1, "Vui lòng nhập trường dateOfApply");
        }
        if (Strings.isNullOrEmpty(cvType)) {
            return new BaseResponse(-1, "Vui lòng nhập trường cvType");
        }
        if (Strings.isNullOrEmpty(talentPoolId)) {
            return new BaseResponse(-1, "Vui lòng nhập trường talentPoolId");
        }
        if (Strings.isNullOrEmpty(talentPoolName)) {
            return new BaseResponse(-1, "Vui lòng nhập trường talentPoolName");
        }

        return null;
    }
}
