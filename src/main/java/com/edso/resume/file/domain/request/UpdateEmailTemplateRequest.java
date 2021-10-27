package com.edso.resume.file.domain.request;

import com.edso.resume.lib.response.BaseResponse;
import com.google.common.base.Strings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class UpdateEmailTemplateRequest extends BaseAuthRequest{
    private String id;
    private String name;
    private String subject;
    private String attachment;
    private String content;

    public BaseResponse validate(){
        if (Strings.isNullOrEmpty(id)) {
            return new BaseResponse(-1, "Vui lòng nhập id");
        }
        if (Strings.isNullOrEmpty(name)) {
            return new BaseResponse(-1, "Vui lòng nhập tên Email Template");
        }
        if(Strings.isNullOrEmpty(subject)) {
            return new BaseResponse(-1, "Vui lòng nhập tiêu đề mẫu Email");
        }
        if(Strings.isNullOrEmpty(content)) {
            return new BaseResponse(-1, "Vui lòng nhập nội dung Email");
        }

        return null;
    }
}
