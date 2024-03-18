package wagwagt.community.api.interfaces.controller.repositories.implementations;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import wagwagt.community.api.entities.domain.Image;
import wagwagt.community.api.entities.domain.Post;
import wagwagt.community.api.entities.domain.enums.ImageStatus;
import wagwagt.community.api.interfaces.controller.repositories.FileRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FileRepositoryImpl implements FileRepository {

    private final EntityManager em;

    @Override
    public void save(Image image){
        em.persist(image);
    }

    @Override
    public Image findById(Long id) {
        return em.find(Image.class,id);
    }

    @Override
    public List<Image> findUnusedImages() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);

        return em.createQuery("SELECT i FROM Image i WHERE i.imageStatus = :imageStatus AND i.regDate < :oneHourAgo", Image.class)
                .setParameter("imageStatus", ImageStatus.DRAFT)
                .setParameter("oneHourAgo", oneHourAgo)
                .getResultList();
    }

    @Override
    public List<Image> findByPost(Post post){
        return em.createQuery("SELECT i FROM Image I where i.post = :post and i.imageStatus = :imageStatus",Image.class)
                .setParameter("imageStatus", ImageStatus.DRAFT)
                .setParameter("post",post)
                .getResultList();
    }

    @Override
    public void delete(Long id) {
        Image image = findById(id);
        if(image != null){
            em.remove(image);
        }

    }
}
//