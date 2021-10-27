package com.edso.resume.file.domain.request;

import com.edso.resume.lib.response.BaseResponse;
import com.google.common.base.Strings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class CreateKeypointRequest extends BaseAuthRequest{
    private String id;
    private String description;

    public BaseResponse validate() {
        if (Strings.isNullOrEmpty(id)) {
            return new BaseResponse(-1, "Vui lòng nhập id");
        }
        if (Strings.isNullOrEmpty(description)) {
            return new BaseResponse(-1, "Vui lòng nhập mô tả");
        }

        return null;
    }
}
