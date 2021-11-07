package com.edso.resume.file.service;

import com.edso.resume.file.domain.db.MongoDbOnlineSyncActions;
import com.edso.resume.file.domain.entities.Email;
import com.edso.resume.file.domain.request.CreateEmailTemplateRequest;
import com.edso.resume.file.domain.request.DeleteEmailTemplateRequest;
import com.edso.resume.file.domain.request.UpdateEmailTemplateRequest;
import com.edso.resume.lib.common.AppUtils;
import com.edso.resume.lib.common.CollectionNameDefs;
import com.edso.resume.lib.entities.HeaderInfo;
import com.edso.resume.lib.entities.PagingInfo;
import com.edso.resume.lib.response.BaseResponse;
import com.edso.resume.lib.response.GetArrayResponse;
import com.google.common.base.Strings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class EmailTemplateServiceImpl extends BaseService implements EmailTemplateService {

    private final MongoDbOnlineSyncActions db;

    public EmailTemplateServiceImpl(MongoDbOnlineSyncActions db) {
        this.db = db;
    }

    @Override
    public GetArrayResponse<Email> findAll(HeaderInfo headerInfo, String name, Integer page, Integer size) {
        List<Bson> c = new ArrayList<>();
        if (!Strings.isNullOrEmpty(name)) {
            c.add(Filters.regex("name_search", Pattern.compile(name.toLowerCase())));
        }
        Bson cond = buildCondition(c);
        long total = db.countAll(CollectionNameDefs.COLL_EMAIL_TEMPLATE, cond);
        PagingInfo pagingInfo = PagingInfo.parse(page, size);
        FindIterable<Document> lst = db.findAll2(CollectionNameDefs.COLL_EMAIL_TEMPLATE, cond, null, pagingInfo.getStart(), pagingInfo.getLimit());
        List<Email> rows = new ArrayList<>();
        if (lst != null) {
            for (Document doc : lst) {
                Email email = Email.builder()
                        .id(AppUtils.parseString(doc.get("id")))
                        .name(AppUtils.parseString(doc.get("name")))
                        .subject(AppUtils.parseString(doc.get("subject")))
                        .content(AppUtils.parseString(doc.get("content")))
                        .attachment(AppUtils.parseString(doc.get("attachment")))
                        .build();
                rows.add(email);
            }
        }
        GetArrayResponse<Email> resp = new GetArrayResponse<>();
        resp.setSuccess();
        resp.setTotal(total);
        resp.setRows(rows);
        return resp;
    }

    @Override
    public BaseResponse createEmailTemplate(CreateEmailTemplateRequest request) {
        BaseResponse response = new BaseResponse();

        String name = request.getName();
        Bson c = Filters.eq("name_search", name.toLowerCase());
        long count = db.countAll(CollectionNameDefs.COLL_EMAIL_TEMPLATE, c);

        if (count > 0) {
            response.setFailed("Tên Template đã tồn tại");
            return response;
        }

        Document emailTemplate = new Document();
        emailTemplate.append("id", UUID.randomUUID().toString());
        emailTemplate.append("name", name);
        emailTemplate.append("subject", request.getSubject());
        emailTemplate.append("attachment", request.getAttachment());
        emailTemplate.append("content", request.getContent());
        emailTemplate.append("name_search", name.toLowerCase());
        emailTemplate.append("create_at", System.currentTimeMillis());
        emailTemplate.append("update_at", System.currentTimeMillis());
        emailTemplate.append("create_by", request.getInfo().getUsername());
        emailTemplate.append("update_by", request.getInfo().getUsername());

        db.insertOne(CollectionNameDefs.COLL_EMAIL_TEMPLATE, emailTemplate);

        return new BaseResponse(0, "OK");
    }

    @Override
    public BaseResponse updateEmailTemplate(UpdateEmailTemplateRequest request) {
        BaseResponse response = new BaseResponse();
        String id = request.getId();
        Bson cond = Filters.eq("id", id);
        Document idDocument = db.findOne(CollectionNameDefs.COLL_EMAIL_TEMPLATE, cond);

        if (idDocument == null) {
            response.setFailed("Id này không tồn tại");
            return response;
        }

        String name = request.getName();
        Document obj = db.findOne(CollectionNameDefs.COLL_EMAIL_TEMPLATE, Filters.eq("name_search", name.toLowerCase()));
        if (obj != null) {
            String objId = AppUtils.parseString(obj.get("id"));
            if (!objId.equals(id)) {
                response.setFailed("Tên này đã tồn tại");
                return response;
            }
        }

        Bson updates = Updates.combine(
                Updates.set("name", name),
                Updates.set("subject", request.getSubject()),
                Updates.set("attachment", request.getAttachment()),
                Updates.set("content", request.getContent()),
                Updates.set("name_search", name.toLowerCase()),
                Updates.set("update_at", System.currentTimeMillis()),
                Updates.set("update_by", request.getInfo().getUsername())
        );
        db.update(CollectionNameDefs.COLL_EMAIL_TEMPLATE, cond, updates, true);

        response.setSuccess();
        return response;
    }

    @Override
    public BaseResponse deleteEmailTemplate(DeleteEmailTemplateRequest request) {
        BaseResponse response = new BaseResponse();
        String id = request.getId();
        Bson cond = Filters.eq("id", id);
        Document idDocument = db.findOne(CollectionNameDefs.COLL_EMAIL_TEMPLATE, cond);

        if (idDocument == null) {
            response.setFailed("Id này không tồn tại");
            return response;
        }
        db.delete(CollectionNameDefs.COLL_EMAIL_TEMPLATE, cond);
        return new BaseResponse(0, "OK");
    }
}
