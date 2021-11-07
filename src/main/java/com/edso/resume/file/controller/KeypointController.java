package com.edso.resume.file.controller;

import com.edso.resume.file.domain.entities.KeyPoint;
import com.edso.resume.file.domain.request.CreateKeypointRequest;
import com.edso.resume.file.domain.request.DeleteKeyPointRequest;
import com.edso.resume.file.domain.request.UpdateKeypointRequest;
import com.edso.resume.file.service.KeypointService;
import com.edso.resume.lib.entities.HeaderInfo;
import com.edso.resume.lib.response.BaseResponse;
import com.edso.resume.lib.response.GetArrayResponse;
import com.edso.resume.lib.utils.ParseHeaderUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/keypoint")
public class KeypointController extends BaseController {

    private final KeypointService keypointService;

    public KeypointController(KeypointService keypointService) {
        this.keypointService = keypointService;
    }

    @GetMapping("/list")
    public BaseResponse findKeypoint(
            @RequestHeader Map<String, String> headers,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        HeaderInfo headerInfo = ParseHeaderUtil.build(headers);
        logger.info("=>findKeypoint u: {}, name: {}, page: {}, size: {}", headerInfo, name, page, size);
        GetArrayResponse<KeyPoint> resp = keypointService.findAll(headerInfo, name, page, size);
        logger.info("<=findKeypoint u: {}, name: {}, page: {}, size: {}, resp: {}", headerInfo, name, page, size, resp.info());
        return resp;
    }

    @PostMapping("/create")
    public BaseResponse createKeypoint(@RequestHeader Map<String, String> headers, @RequestBody CreateKeypointRequest request) {
        BaseResponse response = new BaseResponse();
        HeaderInfo headerInfo = ParseHeaderUtil.build(headers);
        logger.info("=>createKeypoint u: {}, req: {}", headerInfo, request);
        if (request == null) {
            response.setResult(-1, "Vui lòng điền đầy đủ thông tin");
        } else {
            response = request.validate();
            if (response == null) {
                request.setInfo(headerInfo);
                response = keypointService.createKeypoint(request);
            }
        }
        logger.info("<=createKeypoint u: {}, req: {}, resp: {}", headerInfo, request, response);
        return response;
    }

    @PostMapping("/update")
    public BaseResponse updateKeypoint(@RequestHeader Map<String, String> headers, @RequestBody UpdateKeypointRequest request) {
        BaseResponse response = new BaseResponse();
        HeaderInfo headerInfo = ParseHeaderUtil.build(headers);
        logger.info("=>updateKeypoint u: {}, req: {}", headerInfo, request);
        if (request == null) {
            response.setResult(-1, "Vui lòng điền đầy đủ thông tin");
        } else {
            response = request.validate();
            if (response == null) {
                request.setInfo(headerInfo);
                response = keypointService.updateKeypoint(request);
            }
        }
        logger.info("<=updateKeypoint u: {}, req: {}, resp: {}", headerInfo, request, response);
        return response;
    }

    @PostMapping("/delete")
    public BaseResponse deleteKeypoint(@RequestHeader Map<String, String> headers, @RequestBody DeleteKeyPointRequest request) {
        logger.info("=>deleteKeypoint req: {}", request);
        BaseResponse response = new BaseResponse();
        if (request == null) {
            response.setResult(-1, "Vui lòng nhập đầy đủ thông tin");
        } else {
            response = request.validate();
            if (response == null) {
                HeaderInfo headerInfo = ParseHeaderUtil.build(headers);
                request.setInfo(headerInfo);
                response = keypointService.deleteKeypoint(request);
            }
        }
        logger.info("<=deleteKeypoint req: {}, resp: {}", request, response);
        return response;
    }

}
