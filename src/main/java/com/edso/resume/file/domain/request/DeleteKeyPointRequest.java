package com.edso.resume.file.domain.request;

import com.edso.resume.lib.response.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.elasticsearch.common.Strings;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class DeleteKeyPointRequest extends BaseAuthRequest {
    private String id;

    public BaseResponse validate() {
        if (Strings.isNullOrEmpty(id)) {
            return new BaseResponse(-1, "Vui lòng chọn id");
        }
        return null;
    }
}
