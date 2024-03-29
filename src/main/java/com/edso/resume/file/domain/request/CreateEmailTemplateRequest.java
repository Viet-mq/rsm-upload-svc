package com.edso.resume.file.domain.request;

import com.edso.resume.lib.response.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class CreateEmailTemplateRequest extends BaseAuthRequest {
    private String name;
    private String subject;
    private String attachment;
    private String content;
    private String type;

    public BaseResponse validate() {

        return null;
    }
}
