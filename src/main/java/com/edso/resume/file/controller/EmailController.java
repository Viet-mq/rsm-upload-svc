package com.edso.resume.file.controller;

import com.edso.resume.file.service.SendEmailService;
import com.edso.resume.lib.entities.HeaderInfo;
import com.edso.resume.lib.response.BaseResponse;
import com.edso.resume.lib.utils.ParseHeaderUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/email")
public class EmailController extends BaseController {

    private final SendEmailService sendRejectEmailToCandidate;
    private final SendEmailService sendRejectEmailToPresenter;
    private final SendEmailService sendRoundEmailToPresenter;
    private final SendEmailService sendRoundEmailToCandidate;
    private final SendEmailService sendCalendarEmailToPresenter;
    private final SendEmailService sendCalendarEmailToInterviewer;
    private final SendEmailService sendCalenderEmailToCandidate;

    public EmailController(@Qualifier("sendRejectEmailToCandidateService") SendEmailService sendRejectEmailToCandidate,
                           @Qualifier("sendRejectEmailToPresenterService") SendEmailService sendRejectEmailToPresenter,
                           @Qualifier("sendRoundEmailToPresenterService")SendEmailService sendRoundEmailToPresenter,
                           @Qualifier("sendRoundEmailToCandidateService")SendEmailService sendRoundEmailToCandidate,
                           @Qualifier("sendCalendarEmailToPresenterService")SendEmailService sendCalendarEmailToPresenter,
                           @Qualifier("sendCalendarEmailToInterviewerService")SendEmailService sendCalendarEmailToInterviewer,
                           @Qualifier("sendCalendarEmailToCandidateService")SendEmailService sendCalenderEmailToCandidate) {
        this.sendRejectEmailToCandidate = sendRejectEmailToCandidate;
        this.sendRejectEmailToPresenter = sendRejectEmailToPresenter;
        this.sendRoundEmailToPresenter = sendRoundEmailToPresenter;
        this.sendRoundEmailToCandidate = sendRoundEmailToCandidate;
        this.sendCalendarEmailToPresenter = sendCalendarEmailToPresenter;
        this.sendCalendarEmailToInterviewer = sendCalendarEmailToInterviewer;
        this.sendCalenderEmailToCandidate = sendCalenderEmailToCandidate;
    }

    @PostMapping("/send-template")
    public BaseResponse sendEmailTemplate(@RequestHeader Map<String, String> headers,
                                          @RequestParam String profileId,
                                          @RequestParam String content,
                                          @RequestParam String subject,
                                          @RequestParam String historyId,
                                          @RequestParam(value = "file", required = false) List<String> files) {
        HeaderInfo headerInfo = ParseHeaderUtil.build(headers);
        logger.info("=>u: {} Send email-template, profileId: {}, subject: {}", headerInfo, profileId, subject);
        return sendCalenderEmailToCandidate.sendEmail(profileId, subject, content, historyId, files);
    }
}
