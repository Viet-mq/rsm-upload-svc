package com.edso.resume.file.domain.rabbitmq.consumer;

import com.edso.resume.file.domain.rabbitmq.event.Ids;
import com.edso.resume.file.domain.rabbitmq.event.SendEmailEvent;
import com.edso.resume.file.service.SendEmailService;
import com.edso.resume.file.service.SendOutlookCalendarService;
import com.edso.resume.lib.common.TypeConfig;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    private static final Logger logger = LoggerFactory.getLogger(EmailConsumer.class);

    private final SendOutlookCalendarService sendOutlookCalendarService;

    private final SendEmailService sendRejectEmailToCandidate;
    private final SendEmailService sendRejectEmailToPresenter;
    private final SendEmailService sendRoundEmailToPresenter;
    private final SendEmailService sendRoundEmailToCandidate;
    private final SendEmailService sendCalendarEmailToPresenter;
    private final SendEmailService sendCalendarEmailToInterviewer;
    private final SendEmailService sendCalenderEmailToCandidate;

    public EmailConsumer(SendOutlookCalendarService sendOutlookCalendarService,
                         @Qualifier("sendRejectEmailToCandidateService") SendEmailService sendRejectEmailToCandidate,
                         @Qualifier("sendRejectEmailToPresenterService") SendEmailService sendRejectEmailToPresenter,
                         @Qualifier("sendRoundEmailToPresenterService") SendEmailService sendRoundEmailToPresenter,
                         @Qualifier("sendRoundEmailToCandidateService") SendEmailService sendRoundEmailToCandidate,
                         @Qualifier("sendCalendarEmailToPresenterService") SendEmailService sendCalendarEmailToPresenter,
                         @Qualifier("sendCalendarEmailToInterviewerService") SendEmailService sendCalendarEmailToInterviewer,
                         @Qualifier("sendCalendarEmailToCandidateService") SendEmailService sendCalenderEmailToCandidate) {
        this.sendOutlookCalendarService = sendOutlookCalendarService;
        this.sendRejectEmailToCandidate = sendRejectEmailToCandidate;
        this.sendRejectEmailToPresenter = sendRejectEmailToPresenter;
        this.sendRoundEmailToPresenter = sendRoundEmailToPresenter;
        this.sendRoundEmailToCandidate = sendRoundEmailToCandidate;
        this.sendCalendarEmailToPresenter = sendCalendarEmailToPresenter;
        this.sendCalendarEmailToInterviewer = sendCalendarEmailToInterviewer;
        this.sendCalenderEmailToCandidate = sendCalenderEmailToCandidate;
    }


    @SneakyThrows
    @RabbitListener(queues = "${spring.rabbitmq.email.queue}")
    public void consumeEmail(SendEmailEvent sendEmailEvent) {
        logger.info("Event from queue " + sendEmailEvent);
        switch (sendEmailEvent.getType()) {
            case TypeConfig.REJECT_CANDIDATE:
                sendRejectEmailToCandidate.sendEmail(sendEmailEvent.getProfileId(),
                        sendEmailEvent.getSubject(),
                        sendEmailEvent.getContent(),
                        sendEmailEvent.getHistoryId(),
                        sendEmailEvent.getFiles());
                break;
            case TypeConfig.REJECT_PRESENTER:
                sendRejectEmailToPresenter.sendEmail(sendEmailEvent.getProfileId(),
                        sendEmailEvent.getSubject(),
                        sendEmailEvent.getContent(),
                        sendEmailEvent.getHistoryId(),
                        sendEmailEvent.getFiles());
                break;
            case TypeConfig.CALENDAR_CANDIDATE:
                sendCalenderEmailToCandidate.sendCalendarEmail(sendEmailEvent.getCalendarId(),
                        sendEmailEvent.getSubject(),
                        sendEmailEvent.getContent(),
                        sendEmailEvent.getHistoryId(),
                        sendEmailEvent.getFiles());
                break;
            case TypeConfig.CALENDAR_INTERVIEWER:
                sendCalendarEmailToInterviewer.sendCalendarEmail(sendEmailEvent.getCalendarId(),
                        sendEmailEvent.getSubject(),
                        sendEmailEvent.getContent(),
                        sendEmailEvent.getHistoryId(),
                        sendEmailEvent.getFiles());
                break;
            case TypeConfig.CALENDAR_PRESENTER:
                sendCalendarEmailToPresenter.sendCalendarEmail(sendEmailEvent.getCalendarId(),
                        sendEmailEvent.getSubject(),
                        sendEmailEvent.getContent(),
                        sendEmailEvent.getHistoryId(),
                        sendEmailEvent.getFiles());
                break;
            case TypeConfig.ROUND_CANDIDATE:
                sendRoundEmailToCandidate.sendEmail(sendEmailEvent.getProfileId(),
                        sendEmailEvent.getSubject(),
                        sendEmailEvent.getContent(),
                        sendEmailEvent.getHistoryId(),
                        sendEmailEvent.getFiles());
                break;
            case TypeConfig.ROUND_PRESENTER:
                sendRoundEmailToPresenter.sendEmail(sendEmailEvent.getProfileId(),
                        sendEmailEvent.getSubject(),
                        sendEmailEvent.getContent(),
                        sendEmailEvent.getHistoryId(),
                        sendEmailEvent.getFiles());
                break;
            case TypeConfig.CALENDARS_CANDIDATE:
                for (Ids ids : sendEmailEvent.getIds()) {
                    sendCalenderEmailToCandidate.sendEmail(ids.getCalendarId(),
                            sendEmailEvent.getSubject(),
                            sendEmailEvent.getContent(),
                            ids.getHistoryId(),
                            sendEmailEvent.getFiles());
                }
        }
    }
}
