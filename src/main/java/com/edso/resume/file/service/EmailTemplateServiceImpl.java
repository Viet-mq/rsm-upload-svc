package com.edso.resume.file.service;

import com.edso.resume.file.config.EmailTemplateConfig;
import com.edso.resume.file.domain.db.MongoDbOnlineSyncActions;
import com.edso.resume.file.domain.entities.Email;
import com.edso.resume.file.domain.request.CreateEmailTemplateRequest;
import com.edso.resume.file.domain.request.DeleteEmailTemplateRequest;
import com.edso.resume.file.domain.request.UpdateEmailTemplateRequest;
import com.edso.resume.lib.common.AppUtils;
import com.edso.resume.lib.common.CollectionNameDefs;
import com.edso.resume.lib.common.DbKeyConfig;
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
            c.add(Filters.regex(EmailTemplateConfig.NAME_SEARCH, Pattern.compile(name.toLowerCase())));
        }
        Bson cond = buildCondition(c);
        long total = db.countAll(CollectionNameDefs.COLL_EMAIL_TEMPLATE, cond);
        PagingInfo pagingInfo = PagingInfo.parse(page, size);
        FindIterable<Document> lst = db.findAll2(CollectionNameDefs.COLL_EMAIL_TEMPLATE, cond, null, pagingInfo.getStart(), pagingInfo.getLimit());
        List<Email> rows = new ArrayList<>();
        if (lst != null) {
            for (Document doc : lst) {
                Email email = Email.builder()
                        .id(AppUtils.parseString(doc.get(EmailTemplateConfig.ID)))
                        .name(AppUtils.parseString(doc.get(EmailTemplateConfig.NAME)))
                        .subject(AppUtils.parseString(doc.get(EmailTemplateConfig.SUBJECT)))
                        .content(AppUtils.parseString(doc.get(EmailTemplateConfig.CONTENT)))
                        .attachment(AppUtils.parseString(doc.get(EmailTemplateConfig.ATTACHMENT)))
                        .type(AppUtils.parseString(doc.get(EmailTemplateConfig.TYPE)))
                        .create_at(AppUtils.parseLong(doc.get(DbKeyConfig.CREATE_AT)))
                        .create_by(AppUtils.parseString(doc.get(DbKeyConfig.CREATE_BY)))
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
        Bson c = Filters.eq(EmailTemplateConfig.NAME_SEARCH, name.toLowerCase());
        long count = db.countAll(CollectionNameDefs.COLL_EMAIL_TEMPLATE, c);

        if (count > 0) {
            response.setFailed("Tên Template đã tồn tại");
            return response;
        }

        Document emailTemplate = new Document();
        emailTemplate.append(EmailTemplateConfig.ID, UUID.randomUUID().toString());
        emailTemplate.append(EmailTemplateConfig.NAME, name);
        emailTemplate.append(EmailTemplateConfig.SUBJECT, request.getSubject());
        emailTemplate.append(EmailTemplateConfig.ATTACHMENT, request.getAttachment());
        emailTemplate.append(EmailTemplateConfig.CONTENT, request.getContent());
        emailTemplate.append(EmailTemplateConfig.TYPE, request.getType());
        emailTemplate.append(EmailTemplateConfig.NAME_SEARCH, name.toLowerCase());
        emailTemplate.append(EmailTemplateConfig.CREATE_AT, System.currentTimeMillis());
        emailTemplate.append(EmailTemplateConfig.UPDATE_AT, System.currentTimeMillis());
        emailTemplate.append(EmailTemplateConfig.CREATE_BY, request.getInfo().getUsername());
        emailTemplate.append(EmailTemplateConfig.UPDATE_BY, request.getInfo().getUsername());

        db.insertOne(CollectionNameDefs.COLL_EMAIL_TEMPLATE, emailTemplate);

        return new BaseResponse(0, "OK");
    }

    @Override
    public BaseResponse updateEmailTemplate(UpdateEmailTemplateRequest request) {
        BaseResponse response = new BaseResponse();
        String id = request.getId();
        Bson cond = Filters.eq(EmailTemplateConfig.ID, id);
        Document idDocument = db.findOne(CollectionNameDefs.COLL_EMAIL_TEMPLATE, cond);

        if (idDocument == null) {
            response.setFailed("Id này không tồn tại");
            return response;
        }

        String name = request.getName();
        Document obj = db.findOne(CollectionNameDefs.COLL_EMAIL_TEMPLATE, Filters.eq(EmailTemplateConfig.NAME_SEARCH, name.toLowerCase()));
        if (obj != null) {
            String objId = AppUtils.parseString(obj.get(EmailTemplateConfig.ID));
            if (!objId.equals(id)) {
                response.setFailed("Tên này đã tồn tại");
                return response;
            }
        }

        Bson updates = Updates.combine(
                Updates.set(EmailTemplateConfig.NAME, name),
                Updates.set(EmailTemplateConfig.SUBJECT, request.getSubject()),
                Updates.set(EmailTemplateConfig.ATTACHMENT, request.getAttachment()),
                Updates.set(EmailTemplateConfig.CONTENT, request.getContent()),
                Updates.set(EmailTemplateConfig.TYPE, request.getType()),
                Updates.set(EmailTemplateConfig.NAME_SEARCH, name.toLowerCase()),
                Updates.set(EmailTemplateConfig.UPDATE_AT, System.currentTimeMillis()),
                Updates.set(EmailTemplateConfig.UPDATE_BY, request.getInfo().getUsername())
        );
        db.update(CollectionNameDefs.COLL_EMAIL_TEMPLATE, cond, updates, true);

        response.setSuccess();
        return response;
    }

    @Override
    public BaseResponse deleteEmailTemplate(DeleteEmailTemplateRequest request) {
        BaseResponse response = new BaseResponse();
        String id = request.getId();
        Bson cond = Filters.eq(EmailTemplateConfig.ID, id);
        Document idDocument = db.findOne(CollectionNameDefs.COLL_EMAIL_TEMPLATE, cond);

        if (idDocument == null) {
            response.setFailed("Id này không tồn tại");
            return response;
        }
        db.delete(CollectionNameDefs.COLL_EMAIL_TEMPLATE, cond);
        return new BaseResponse(0, "OK");
    }
}
