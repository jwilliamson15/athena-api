package com.athena.repository;

import java.util.List;

import com.athena.models.Consultant;
import com.athena.repository.queries.DynamicQuery;

public interface ConsultantsRepositoryCustom {
    List<Consultant> query(DynamicQuery dynamicQuery);
}
