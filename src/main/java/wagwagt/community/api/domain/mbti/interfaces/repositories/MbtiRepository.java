package wagwagt.community.api.domain.mbti.interfaces.repositories;

import wagwagt.community.api.domain.mbti.entities.Mbti;
import wagwagt.community.api.domain.mbti.entities.MbtiResult;
import wagwagt.community.api.common.enums.MbtiType;

import java.util.List;


public interface MbtiRepository {


    void save(Mbti mbti);

    List<MbtiResult> findTopX(int X);

    MbtiResult findByResult(MbtiType type);

}