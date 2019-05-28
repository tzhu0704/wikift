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
import com.wikift.model.result.CommonResult;
import com.wikift.model.user.GroupEntity;
import com.wikift.support.service.user.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "new", method = RequestMethod.POST)
    CommonResult<GroupEntity> save(@RequestBody GroupEntity entity) {
        Assert.isNull(entity, MessageEnums.PARAMS_NOT_NULL.getValue());
        return CommonResult.success(groupService.save(entity));
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    CommonResult<GroupEntity> delete(@RequestParam(value = "groupId") Long groupId) {
        Assert.isNull(groupId, MessageEnums.PARAMS_NOT_NULL.getValue());
        GroupEntity entity = new GroupEntity();
        entity.setId(groupId);
        return CommonResult.success(groupService.delete(entity));
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ADMIN')")
    CommonResult<List<GroupEntity>> list() {
        return CommonResult.success(groupService.findAll());
    }

}
