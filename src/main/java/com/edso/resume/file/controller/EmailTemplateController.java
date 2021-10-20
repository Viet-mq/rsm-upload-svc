package com.edso.resume.file.controller;

import com.edso.resume.file.domain.entities.Email;
import com.edso.resume.file.domain.request.CreateEmailTemplateRequest;
import com.edso.resume.file.domain.request.DeleteEmailTemplateRequest;
import com.edso.resume.file.domain.request.UpdateEmailTemplateRequest;
import com.edso.resume.file.service.EmailTemplateService;
import com.edso.resume.lib.entities.HeaderInfo;
import com.edso.resume.lib.response.BaseResponse;
import com.edso.resume.lib.response.GetArrayResponse;
import com.edso.resume.lib.utils.ParseHeaderUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/template")
public class EmailTemplateController extends BaseController{

    private final EmailTemplateService emailTemplateService;

    public EmailTemplateController(EmailTemplateService emailTemplateService) {
        this.emailTemplateService = emailTemplateService;
    }

    @GetMapping("/list")
    public BaseResponse findEmailTemplate(
            @RequestHeader Map<String, String> headers,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        HeaderInfo headerInfo = ParseHeaderUtil.build(headers);
        logger.info("=>findEmailTemplate u: {}, name: {}, page: {}, size: {}", headerInfo, name, page, size);
        GetArrayResponse<Email> resp = emailTemplateService.findAll(headerInfo, name, page, size);
        logger.info("<=findEmailTemplate u: {}, name: {}, page: {}, size: {}, resp: {}", headerInfo, name, page, size, resp.info());
        return resp;
    }

    @PostMapping("/create")
    public BaseResponse createEmailTemplate(@RequestHeader Map<String, String> headers, @RequestBody CreateEmailTemplateRequest request) {
        BaseResponse response = new BaseResponse();
        HeaderInfo headerInfo = ParseHeaderUtil.build(headers);
        logger.info("=>createEmailTemplate u: {}, req: {}", headerInfo, request);
        if (request == null) {
            response.setResult(-1, "Vui lòng điền đầy đủ thông tin");
        } else {
            response = request.validate();
            if (response == null) {
                request.setInfo(headerInfo);
                response = emailTemplateService.createEmailTemplate(request);
            }
        }
        logger.info("<=createEmailTemplate u: {}, req: {}, resp: {}", headerInfo, request, response);
        return response;
    }

    @PostMapping("/update")
    public BaseResponse updateEmailTemplate(@RequestHeader Map<String, String> headers, @RequestBody UpdateEmailTemplateRequest request) {
        BaseResponse response = new BaseResponse();
        HeaderInfo headerInfo = ParseHeaderUtil.build(headers);
        logger.info("=>updateEmailTemplate u: {}, req: {}", headerInfo, request);
        if (request == null) {
            response.setResult(-1, "Vui lòng điền đầy đủ thông tin");
        } else {
            response = request.validate();
            if (response == null) {
                request.setInfo(headerInfo);
                response = emailTemplateService.updateEmailTemplate(request);
            }
        }
        logger.info("<=updateEmailTemplate u: {}, req: {}, resp: {}", headerInfo, request, response);
        return response;
    }

    @PostMapping("/delete")
    public BaseResponse deleteEmailTemplate(@RequestHeader Map<String, String> headers, @RequestBody DeleteEmailTemplateRequest request) {
        logger.info("=>deleteEmailTemplate req: {}", request);
        BaseResponse response = new BaseResponse();
        if (request == null) {
            response.setResult(-1, "Vui lòng nhập đầy đủ thông tin");
        } else {
            response = request.validate();
            if (response == null) {
                HeaderInfo headerInfo = ParseHeaderUtil.build(headers);
                request.setInfo(headerInfo);
                response = emailTemplateService.deleteEmailTemplate(request);
            }
        }
        logger.info("<=deleteEmailTemplate req: {}, resp: {}", request, response);
        return response;
    }

}
