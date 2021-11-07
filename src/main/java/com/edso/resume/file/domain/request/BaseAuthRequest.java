package com.edso.resume.file.domain.request;

import com.edso.resume.lib.entities.HeaderInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class BaseAuthRequest {

    @JsonIgnore
    protected HeaderInfo info;

}
