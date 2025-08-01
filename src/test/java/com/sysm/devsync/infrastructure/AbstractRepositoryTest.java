package com.sysm.devsync.infrastructure;

import com.sysm.devsync.infrastructure.repositories.*;
import com.sysm.devsync.infrastructure.repositories.NoteJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@PersistenceTest
public class AbstractRepositoryTest {

    @Autowired
    protected CommentJpaRepository commentJpaRepository;

    @Autowired
    protected NoteJpaRepository noteJpaRepository;

    @Autowired
    protected TestEntityManager entityManager;

    @Autowired
    protected AnswerJpaRepository answerJpaRepository;

    @Autowired
    protected QuestionJpaRepository questionJpaRepository;

    @Autowired
    protected ProjectJpaRepository projectJpaRepository;

    @Autowired
    protected WorkspaceJpaRepository workspaceJpaRepository;

    @Autowired
    protected UserJpaRepository userJpaRepository;

    @Autowired
    protected TagJpaRepository tagJpaRepository;

    protected void clearRepositories() {
        commentJpaRepository.deleteAllInBatch();
        noteJpaRepository.deleteAllInBatch();
        answerJpaRepository.deleteAllInBatch();
        questionJpaRepository.deleteAllInBatch();
        projectJpaRepository.deleteAllInBatch();
        workspaceJpaRepository.deleteAllInBatch();
        userJpaRepository.deleteAllInBatch();
        tagJpaRepository.deleteAllInBatch();
    }

    protected void entityPersist(Object entity) {
        entityManager.persist(entity);
        flushAndClear();
        entityManager.detach(entity);
    }

    protected void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }


}
