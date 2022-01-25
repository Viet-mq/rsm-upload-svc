package com.edso.resume.file.domain.request;

import com.edso.resume.lib.common.ErrorCodeDefs;
import com.edso.resume.lib.response.BaseResponse;
import com.google.common.base.Strings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class SendCalendarRequest extends BaseAuthRequest {
    private String subject;
    private String content;
    private String calendarId;

    public BaseResponse validate() {
        if (Strings.isNullOrEmpty(subject) || subject.length() > 255) {
            return new BaseResponse(ErrorCodeDefs.ID, "Vui lòng nhập tiêu đề");
        }
        if (Strings.isNullOrEmpty(content)) {
            return new BaseResponse(ErrorCodeDefs.ID, "Vui lòng nhập nội dung");
        }
        if (Strings.isNullOrEmpty(calendarId)) {
            return new BaseResponse(ErrorCodeDefs.ID, "Vui lòng nhập calendar id");
        }
        return null;
    }
}
