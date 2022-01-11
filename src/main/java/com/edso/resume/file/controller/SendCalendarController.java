package com.edso.resume.file.controller;

import com.edso.resume.file.domain.request.SendCalendarRequest;
import com.edso.resume.file.service.CalendarService;
import com.edso.resume.lib.entities.HeaderInfo;
import com.edso.resume.lib.response.BaseResponse;
import com.edso.resume.lib.utils.ParseHeaderUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/calendar")
public class SendCalendarController extends BaseController {

    private final CalendarService calendarService;

    public SendCalendarController(@Qualifier("SendOutlookCalendarService") CalendarService sendOutlookCalendarService) {
        this.calendarService = sendOutlookCalendarService;
    }

    @PostMapping("/send")
    public BaseResponse sendCalendar(@RequestHeader Map<String, String> headers, @RequestBody SendCalendarRequest request) {
        BaseResponse response = new BaseResponse();
        HeaderInfo headerInfo = ParseHeaderUtil.build(headers);
        logger.info("=>sendOutlookCalendar u: {}, req: {}", headerInfo, request);
        if (request == null) {
            response.setResult(-1, "Vui lòng điền đầy đủ thông tin");
        } else {
            response = request.validate();
            if (response == null) {
                request.setInfo(headerInfo);
                response = calendarService.sendCalendar(request);
            }
        }
        logger.info("<=sendOutlookCalendar u: {}, req: {}, resp: {}", headerInfo, request, response);
        return response;
    }

}