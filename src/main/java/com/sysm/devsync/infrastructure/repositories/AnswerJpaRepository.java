package com.sysm.devsync.infrastructure.repositories;

import com.sysm.devsync.infrastructure.repositories.entities.AnswerJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerJpaRepository extends JpaRepository<AnswerJpaEntity, String> {

    Page<AnswerJpaEntity> findAll(Specification<AnswerJpaEntity> whereClause, Pageable page);

    Page<AnswerJpaEntity> findAllByQuestion_Id(String questionId, Pageable page);

    List<AnswerJpaEntity> findByAuthorId(String authorId);

    void deleteAllByQuestion_Id(String questionId);

}
