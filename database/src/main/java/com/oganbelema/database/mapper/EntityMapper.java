package com.oganbelema.database.mapper;

import java.util.List;

public interface EntityMapper<E, D> {

    D fromEntity(E entity);

    E toEntity(D model);

    List<D> fromEntityList(List<E> entities);

    List<E> toEntityList(List<D> model);
}
