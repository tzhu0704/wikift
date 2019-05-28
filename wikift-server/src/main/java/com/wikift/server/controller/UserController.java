/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wikift.server.controller;

import com.wikift.model.enums.MessageEnums;
import com.wikift.common.utils.BeanUtils;
import com.wikift.common.utils.ShaUtils;
import com.wikift.model.result.CommonResult;
import com.wikift.model.user.UserEntity;
import com.wikift.model.user.UserTypeEntity;
import com.wikift.server.param.UserParam;
import com.wikift.server.param.UserParamForEmail;
import com.wikift.server.param.UserParamForPassword;
import com.wikift.support.service.user.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping(value = "${wikift.api.path}")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "public/user/register", method = RequestMethod.POST)
    CommonResult<UserEntity> register(@RequestBody @Validated UserParam param) {
        Assert.notNull(param, MessageEnums.PARAMS_NOT_NULL.getValue());
        if (!param.getPassword().equals(param.getRepassword())) {
            return CommonResult.validateError(MessageEnums.PARAMS_CONTRAST_VALIDATE_ERROR);
        }
        // 手动设置id为0, 阻止jpa存储数据出现json绑定错误
        UserEntity entity = new UserEntity();
        entity.setId(0l);
        entity.setUsername(param.getUsername());
        entity.setPassword(ShaUtils.hash256(param.getPassword()));
        entity.setAliasName(param.getUsername());
        UserTypeEntity userType = new UserTypeEntity();
        userType.setId(1L);
        entity.setUserType(userType);
        return CommonResult.success(userService.save(entity));
    }

    @RequestMapping(value = "public/user/list", method = RequestMethod.GET)
    CommonResult<UserEntity> list() {
        return CommonResult.success(userService.getAllUsers());
    }

    @PreAuthorize("hasAuthority(('USER'))")
    @RequestMapping(value = "user/info/{username}", method = RequestMethod.GET)
    CommonResult<UserEntity> info(@PathVariable(value = "username") String username) {
        Assert.notNull(username, MessageEnums.PARAMS_NOT_NULL.getValue());
        return CommonResult.success(userService.getInfoByUsername(username));
    }

    @PreAuthorize("hasAuthority(('USER')) && hasPermission(#entity.id, 'update|user')")
    @RequestMapping(value = "user/update", method = RequestMethod.PUT)
    CommonResult<UserEntity> update(@RequestBody UserEntity entity) {
        Assert.notNull(entity, MessageEnums.PARAMS_NOT_NULL.getValue());
        UserEntity targetUserEntity = userService.getUserById(entity.getId());
        entity.setPassword(targetUserEntity.getPassword());
        return CommonResult.success(userService.update(entity));
    }

    @PreAuthorize("hasAuthority(('USER')) && hasPermission(#entity.id, 'update|user')")
    @RequestMapping(value = "user/update/email", method = RequestMethod.PUT)
    CommonResult<UserEntity> updateEmial(@RequestBody @Validated UserParamForEmail entity) {
        Assert.notNull(entity, MessageEnums.PARAMS_NOT_NULL.getValue());
        UserEntity user = new UserEntity();
        BeanUtils.copy(entity, user);
        return CommonResult.success(userService.updateEmail(user));
    }

    @PreAuthorize("hasAuthority(('USER')) && hasPermission(#param.id, 'update|user')")
    @RequestMapping(value = "user/update/password", method = RequestMethod.PUT)
    CommonResult<UserEntity> updatePassword(@RequestBody @Validated UserParamForPassword param) {
        Assert.notNull(param, MessageEnums.PARAMS_NOT_NULL.getValue());
        // 抽取邮箱是否为当前用户设置的邮箱地址
        UserEntity user = userService.getUserById(param.getId());
        if (!param.getEmail().equals(user.getEmail())) {
            return CommonResult.validateError(MessageEnums.USER_EMAIL_NOT_AGREE);
        }
        // 抽取原密码是否正确
        if (!ShaUtils.hash256(param.getPassword()).equals(user.getPassword())) {
            return CommonResult.validateError(MessageEnums.USER_PASSWORD_INPUT_ERROR);
        }
        // 抽取修改后密码是否与原密码一致
        if (param.getRepassword().equals(param.getPassword())) {
            return CommonResult.validateError(MessageEnums.USER_PASSWORD_INPUT_SAME);
        }
        UserEntity entity = new UserEntity();
        entity.setId(param.getId());
        entity.setPassword(ShaUtils.hash256(param.getRepassword()));
        return CommonResult.success(userService.updatePassword(entity));
    }

    @RequestMapping(value = "public/user/top", method = RequestMethod.GET)
    CommonResult<UserEntity> findTopByArticle() {
        return CommonResult.success(userService.findTopByArticle());
    }

    @PreAuthorize("hasAuthority(('USER'))")
    @RequestMapping(value = "user/follow", method = RequestMethod.PUT)
    CommonResult follow(@RequestBody UserEntity entity) {
        Assert.notNull(entity, MessageEnums.PARAMS_NOT_NULL.getValue());
        return CommonResult.success(userService.follow(entity.getId(), entity.getFollows().get(0).getId()));
    }

    @PreAuthorize("hasAuthority(('USER'))")
    @RequestMapping(value = "user/unfollow", method = RequestMethod.PUT)
    CommonResult unfollow(@RequestBody UserEntity entity) {
        Assert.notNull(entity, MessageEnums.PARAMS_NOT_NULL.getValue());
        return CommonResult.success(userService.unFollow(entity.getId(), entity.getFollows().get(0).getId()));
    }

    @PreAuthorize("hasAuthority(('USER'))")
    @RequestMapping(value = "user/follows/{userId}", method = RequestMethod.GET)
    CommonResult<UserEntity> getFollows(@PathVariable(value = "userId") Long userId) {
        Assert.notNull(userId, MessageEnums.PARAMS_NOT_NULL.getValue());
        return CommonResult.success(userService.findAllFollowersByUserId(userId));
    }

    @PreAuthorize("hasAuthority(('USER'))")
    @RequestMapping(value = "user/follows/check", method = RequestMethod.GET)
    CommonResult<UserEntity> checkFollow(@RequestParam(value = "followUserId") Long followUserId,
                                         @RequestParam(value = "coverUserId") Long coverUserId) {
        Assert.notNull(followUserId, MessageEnums.PARAMS_NOT_NULL.getValue());
        Assert.notNull(coverUserId, MessageEnums.PARAMS_NOT_NULL.getValue());
        return CommonResult.success(userService.findUserEntityByFollowsExists(followUserId, coverUserId));
    }

    @PreAuthorize("hasAuthority(('USER'))")
    @RequestMapping(value = "user/follows/count", method = RequestMethod.GET)
    CommonResult<UserEntity> findFollowCount(@RequestParam(value = "followUserId") Long followUserId) {
        Assert.notNull(followUserId, MessageEnums.PARAMS_NOT_NULL.getValue());
        Map<String, Integer> count = new ConcurrentHashMap<>();
        count.put("followCount", userService.findFollowCount(followUserId));
        count.put("coverCount", userService.findFollowCoverCount(followUserId));
        return CommonResult.success(count);
    }

    @PreAuthorize("hasAuthority(('USER'))")
    @RequestMapping(value = "user/following", method = RequestMethod.GET)
    CommonResult userFollowed(@RequestParam(value = "userName") String userName) {
        Assert.notNull(userName, MessageEnums.PARAMS_NOT_NULL.getValue());
        UserEntity user = userService.getInfoByUsername(userName);
        if (ObjectUtils.isEmpty(user)) {
            return CommonResult.error(MessageEnums.DATA_NOT_FOUND_OR_PARAM_ERROR);
        }
        return CommonResult.success(userService.getUserFollowed(user.getId()));
    }

    @RequestMapping(value = "public/user/info/simple/{username}", method = RequestMethod.GET)
    CommonResult impleInfo(@PathVariable(value = "username") String username) {
        Assert.notNull(username, MessageEnums.PARAMS_NOT_NULL.getValue());
        UserEntity userEntity = userService.getInfoByUsername(username);
        if (ObjectUtils.isEmpty(userEntity)) {
            return CommonResult.error(MessageEnums.USER_NOT_FOUND);
        }
        UserSimple userSimple = new UserSimple();
        BeanUtils.copy(userEntity, userSimple);
        userSimple.setType(userEntity.getUserType().getName());
        return CommonResult.success(userSimple);
    }

    @RequestMapping(value = "public/user/contribution/{username}", method = RequestMethod.GET)
    CommonResult getContribution(@PathVariable(value = "username") String username) {
        Assert.notNull(username, MessageEnums.PARAMS_NOT_NULL.getValue());
        return CommonResult.success(userService.getUserContribution(username));
    }

    @Data
    class UserSimple {
        private Boolean active;
        private String lock;
        private String email;
        private String username;
        private String type;
    }

}
