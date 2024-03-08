package wagwagt.community.api.interfaces.controller.repositories.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import wagwagt.community.api.entities.domain.EmailVerification;
import wagwagt.community.api.interfaces.controller.repositories.EmailVerificationRepository;

@Repository
@RequiredArgsConstructor
public class EmailVerificationRepositoryImpl implements EmailVerificationRepository {

    private final EntityManager em;

    public void verification(EmailVerification emailVerification){
        em.persist(emailVerification);
    }
    public EmailVerification findLast(String email){
        TypedQuery<EmailVerification> query = em.createQuery("select v from EmailVerification v " +
                "where v.userEmail = :email order by v.createDate desc", EmailVerification.class);
        query.setParameter("email", email);
        query.setMaxResults(1);
        return query.getSingleResult();
    }

}