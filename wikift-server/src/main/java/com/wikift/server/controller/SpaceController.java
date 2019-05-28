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

import com.wikift.common.utils.BeanUtils;
import com.wikift.common.utils.MessageUtils;
import com.wikift.common.utils.PageAndSortUtils;
import com.wikift.model.result.CommonResult;
import com.wikift.model.space.SpaceEntity;
import com.wikift.model.user.UserEntity;
import com.wikift.server.param.SpaceParam;
import com.wikift.support.service.article.ArticleService;
import com.wikift.support.service.space.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "${wikift.api.path}")
public class SpaceController {

    @Autowired
    private SpaceService spaceService;
    @Autowired
    private ArticleService articleService;

    @PreAuthorize("hasAuthority(('USER'))")
    @RequestMapping(value = "space/list", method = RequestMethod.GET)
    CommonResult<SpaceEntity> getAllSpaces(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Assert.notNull(page, MessageUtils.getParamNotNull("page"));
        Assert.notNull(page, MessageUtils.getParamNotNull("size"));
        return CommonResult.success(spaceService.getAllPublicSpace(PageAndSortUtils.getPage(page, size)));
    }

    @PreAuthorize("hasAuthority(('USER'))")
    @RequestMapping(value = "space/list/public/user/{userId}", method = RequestMethod.GET)
    CommonResult<SpaceEntity> getAllSpaceByPrivatedFalseAndUser(@PathVariable Long userId,
                                                                @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Assert.notNull(userId, MessageUtils.getParamNotNull("userId"));
        UserEntity entity = new UserEntity();
        entity.setId(userId);
        return CommonResult.success(spaceService.getAllSpaceByPrivatedFalseOrUser(entity, PageAndSortUtils.getPage(page, size)));
    }

    @PreAuthorize("hasAuthority(('USER'))")
    @RequestMapping(value = "space/create", method = RequestMethod.POST)
    CommonResult<SpaceEntity> createSpace(@RequestBody @Validated SpaceParam param) {
        Assert.notNull(param, MessageUtils.getParamNotNull("param"));
        SpaceEntity space = new SpaceEntity();
        BeanUtils.copy(param, space);
        return CommonResult.success(spaceService.createSpace(space));
    }

    @PreAuthorize("hasAuthority(('USER'))")
    @RequestMapping(value = "space/article", method = RequestMethod.GET)
    public CommonResult<SpaceEntity> getAllArticleBySpace(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                   @RequestParam(value = "spaceCode") String spaceCode) {
        return articleService.getAllArticleBySpace(spaceCode, PageAndSortUtils.getPage(page, size));
    }

    @PreAuthorize("hasAuthority(('USER'))")
    @RequestMapping(value = "space/article/{spaceCode}", method = RequestMethod.GET)
    CommonResult<SpaceEntity> getArticleCountBySpace(@PathVariable(value = "spaceCode") String spaceCode) {
        Assert.notNull(spaceCode, MessageUtils.getParamNotNull("spaceCode"));
        SpaceEntity space = spaceService.getSpaceInfoByCode(spaceCode);
        if (!ObjectUtils.isEmpty(space)) {
            return CommonResult.success(spaceService.getArticleCountById(space.getId()));
        } else {
            return CommonResult.success(space);
        }
    }

    @PreAuthorize("hasAuthority(('USER'))")
    @RequestMapping(value = "space/list/user/{userId}", method = RequestMethod.GET)
    CommonResult<SpaceEntity> getAllSpaceByUser(@PathVariable Long userId,
                                                @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Assert.notNull(userId, MessageUtils.getParamNotNull("userId"));
        UserEntity entity = new UserEntity();
        entity.setId(userId);
        return CommonResult.success(spaceService.getAllSpaceByUser(entity, PageAndSortUtils.getPage(page, size)));
    }

    @PreAuthorize("hasAuthority(('USER'))")
    @RequestMapping(value = "space/list/user/public/{userId}", method = RequestMethod.GET)
    CommonResult<SpaceEntity> getAllPublicSpaceByUser(@PathVariable Long userId,
                                                      @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Assert.notNull(userId, MessageUtils.getParamNotNull("userId"));
        UserEntity entity = new UserEntity();
        entity.setId(userId);
        return CommonResult.success(spaceService.getAllPublicSpaceByUser(entity, PageAndSortUtils.getPage(page, size)));
    }

    @PreAuthorize("hasAuthority(('USER'))")
    @RequestMapping(value = "space/list/user/private/{userId}", method = RequestMethod.GET)
    CommonResult<SpaceEntity> getAllPrivateSpaceByUser(@PathVariable Long userId,
                                                       @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Assert.notNull(userId, MessageUtils.getParamNotNull("userId"));
        UserEntity entity = new UserEntity();
        entity.setId(userId);
        return CommonResult.success(spaceService.getAllPrivateSpaceByUser(entity, PageAndSortUtils.getPage(page, size)));
    }

    @PreAuthorize("hasAuthority(('USER'))")
    @RequestMapping(value = "space/info/{spaceId}", method = RequestMethod.GET)
    CommonResult<SpaceEntity> getSpaceInfoById(@PathVariable Long spaceId) {
        Assert.notNull(spaceId, MessageUtils.getParamNotNull("spaceId"));
        return CommonResult.success(spaceService.getSpaceInfoById(spaceId));
    }

    @PreAuthorize("hasAuthority(('USER'))")
    @RequestMapping(value = "space/info/code/{code}", method = RequestMethod.GET)
    CommonResult<SpaceEntity> getSpaceInfoByCode(@PathVariable String code) {
        Assert.notNull(code, MessageUtils.getParamNotNull("code"));
        return CommonResult.success(spaceService.getSpaceInfoByCode(code));
    }

    @RequestMapping(value = "public/space/list", method = RequestMethod.GET)
    CommonResult<SpaceEntity> list(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                           @RequestParam(value = "size", defaultValue = "1000") Integer size) {
        Assert.notNull(page, MessageUtils.getParamNotNull("page"));
        Assert.notNull(page, MessageUtils.getParamNotNull("size"));
        return CommonResult.success(spaceService.getAllPublicSpace(PageAndSortUtils.getPage(page, size)));
    }

}
