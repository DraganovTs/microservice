package com.microservices.buisiness;

import com.microservices.dataaccess.entity.UserPermission;

import java.util.List;
import java.util.Optional;

public interface QueryUserService {
    Optional<List<UserPermission>> findAllPermissionsByUsername(String username);

}
