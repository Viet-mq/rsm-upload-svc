package com.edso.resume.file.service;

import com.edso.resume.file.domain.request.SendCalendarRequest;
import com.edso.resume.lib.response.BaseResponse;

public interface CalendarService {
    BaseResponse sendCalendar(SendCalendarRequest request);
}
