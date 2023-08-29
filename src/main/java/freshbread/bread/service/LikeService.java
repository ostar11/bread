package freshbread.bread.service;

import freshbread.bread.domain.Like;
import freshbread.bread.repository.LikeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;

    @Transactional
    public void saveLikes(Like like) {
        likeRepository.save(like);
    }

    public List<Like> findLikes() {
        return likeRepository.findAll();
    }

    public Like findOne(Long likesId) {
        return likeRepository.findOne(likesId);
    }

}
