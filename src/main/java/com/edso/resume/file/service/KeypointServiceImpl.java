package com.edso.resume.file.service;

import com.edso.resume.file.domain.db.MongoDbOnlineSyncActions;
import com.edso.resume.file.domain.entities.KeyPoint;
import com.edso.resume.file.domain.request.*;
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
public class KeypointServiceImpl extends BaseService implements KeypointService {

    private final MongoDbOnlineSyncActions db;

    public KeypointServiceImpl (MongoDbOnlineSyncActions db) {
        this.db = db;
    }

    @Override
    public GetArrayResponse<KeyPoint> findAll(HeaderInfo headerInfo, String id, Integer page, Integer size) {
        List<Bson> c = new ArrayList<>();
        if (!Strings.isNullOrEmpty(id)) {
            c.add(Filters.regex("id", Pattern.compile(id)));
        }
        Bson cond = buildCondition(c);
        long total = db.countAll(CollectionNameDefs.COLL_KEY_POINT, cond);
        PagingInfo pagingInfo = PagingInfo.parse(page, size);
        FindIterable<Document> lst = db.findAll2(CollectionNameDefs.COLL_KEY_POINT, cond, null, pagingInfo.getStart(), pagingInfo.getLimit());
        List<KeyPoint> rows = new ArrayList<>();
        if (lst != null) {
            for (Document doc : lst) {
                KeyPoint keyPoint = KeyPoint.builder()
                        .id(AppUtils.parseString(doc.get("id")))
                        .description(AppUtils.parseString(doc.get("description")))
                        .build();
                rows.add(keyPoint);
            }
        }
        GetArrayResponse<KeyPoint> resp = new GetArrayResponse<>();
        resp.setSuccess();
        resp.setTotal(total);
        resp.setRows(rows);
        return resp;
    }

    @Override
    public BaseResponse createKeypoint(CreateKeypointRequest request) {
        BaseResponse response = new BaseResponse();

        String id = request.getId();
        Bson c = Filters.eq("id", id);
        long count = db.countAll(CollectionNameDefs.COLL_EMAIL_TEMPLATE, c);

        if (count > 0) {
            response.setFailed("Keypoint đã tồn tại");
            return response;
        }

        Document keypoint = new Document();
        keypoint.append("id", id);
        keypoint.append("description", request.getDescription());

        db.insertOne(CollectionNameDefs.COLL_KEY_POINT, keypoint);

        return new BaseResponse(0, "OK");
    }

    @Override
    public BaseResponse updateKeypoint(UpdateKeypointRequest request) {
        BaseResponse response = new BaseResponse();
        String id = request.getId();
        Bson cond = Filters.eq("id", id );
        Document idDocument = db.findOne(CollectionNameDefs.COLL_KEY_POINT, cond);

        if (idDocument == null) {
            response.setFailed("Id này không tồn tại");
            return response;
        }

        Bson updates = Updates.combine(
                Updates.set("id", id),
                Updates.set("description", request.getDescription())
        );
        db.update(CollectionNameDefs.COLL_KEY_POINT, cond, updates, true);

        response.setSuccess();
        return response;
    }

    @Override
    public BaseResponse deleteKeypoint(DeleteKeyPointRequest request) {
        BaseResponse response = new BaseResponse();
        String id = request.getId();
        Bson cond = Filters.eq("id", id);
        Document idDocument = db.findOne(CollectionNameDefs.COLL_KEY_POINT, cond);

        if (idDocument == null) {
            response.setFailed("Id này không tồn tại");
            return response;
        }
        db.delete(CollectionNameDefs.COLL_KEY_POINT, cond);
        return new BaseResponse(0, "OK");
    }
}
