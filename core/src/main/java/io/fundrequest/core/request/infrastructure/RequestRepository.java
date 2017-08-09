package io.fundrequest.core.request.infrastructure;

import io.fundrequest.core.infrastructure.repository.JpaRepository;
import io.fundrequest.core.request.domain.Request;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("SELECT r FROM Request r where r.issueInformation.link = ?1")
    Optional<Request> findByIssueLink(String link);

    @Query("SELECT r FROM Request r where ?1 member of r.watchers")
    List<Request> findRequestsForUser(String watcher);
}
