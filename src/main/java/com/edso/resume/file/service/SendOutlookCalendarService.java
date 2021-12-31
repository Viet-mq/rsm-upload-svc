package com.edso.resume.file.service;

import com.edso.resume.file.domain.request.SendOutlookCalendarRequest;
import com.edso.resume.lib.response.BaseResponse;

public interface SendOutlookCalendarService {
    BaseResponse sendOutlookCalendar(SendOutlookCalendarRequest request);
}
