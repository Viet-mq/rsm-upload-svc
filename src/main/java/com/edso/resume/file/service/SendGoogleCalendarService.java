package com.edso.resume.file.service;

import com.edso.resume.file.domain.request.SendCalendarRequest;
import com.edso.resume.lib.response.BaseResponse;
import org.springframework.stereotype.Service;

@Service("SendGoogleCalendarService")
public class SendGoogleCalendarService extends BaseService implements CalendarService {

    @Override
    public BaseResponse sendCalendar(SendCalendarRequest request) {
        return null;
    }

}
