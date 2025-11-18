package cn.gov.forestry.common.service;

import cn.gov.forestry.common.domain.dto.auth.AuthUserRealmInfoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface UserInfoService {
    void saveUserRealmInfo(AuthUserRealmInfoDTO authUserRealmInfoDTO) throws JsonProcessingException;

    AuthUserRealmInfoDTO getUserRealmInfo(String token) throws JsonProcessingException;
}
