//package freshbread.bread.repository;
//
//import freshbread.bread.domain.Like;
//import java.util.List;
//import javax.persistence.EntityManager;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//
//@Repository
//@RequiredArgsConstructor
//public class LikeRepository {
//
//    private final EntityManager em;
//
//    public void save(Like like) {
//        if(like.getId() == null) {
//            em.persist(like);
//        } else {
//            em.merge(like);
//        }
//    }
//
//    public Like findOne(Long id) {
//        return em.find(Like.class, id);
//    }
//
//    public List<Like> findAll() {
//        return em.createQuery("select l from Likes l", Like.class)
//                .getResultList();
//    }
//
//}
