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
import com.wikift.model.remind.RemindEntity;
import com.wikift.model.result.CommonResult;
import com.wikift.model.user.UserEntity;
import com.wikift.support.service.remind.RemindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "${wikift.api.path}")
public class RemindController {

    @Autowired
    private RemindService remindService;

    @PreAuthorize("hasAuthority(('ADMIN'))")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    CommonResult<RemindEntity> list() {
        return CommonResult.success(remindService.findAll());
    }

    @PreAuthorize("hasAuthority(('USER'))")
    @RequestMapping(value = "info/{id}", method = RequestMethod.GET)
    CommonResult<RemindEntity> info(@PathVariable(value = "id") Long id) {
        Assert.notNull(id, MessageEnums.PARAMS_NOT_NULL.getValue());
        return CommonResult.success(remindService.getById(id));
    }

    @PreAuthorize("hasAuthority(('USER'))")
    @RequestMapping(value = "read/{id}", method = RequestMethod.PUT)
    CommonResult<RemindEntity> read(@PathVariable(value = "id") Long id) {
        Assert.notNull(id, MessageEnums.PARAMS_NOT_NULL.getValue());
        return CommonResult.success(remindService.read(id));
    }

    @PreAuthorize("hasAuthority(('USER'))")
    @RequestMapping(value = "remind/list/user", method = RequestMethod.GET)
    CommonResult<RemindEntity> getAllRemindByUsers(@RequestParam(value = "userId") Long userId,
                                                   @RequestParam(value = "type", defaultValue = "unread") String type) {
        Assert.notNull(userId, MessageEnums.PARAMS_NOT_NULL.getValue());
        List<UserEntity> e = new ArrayList<>();
        UserEntity entity = new UserEntity();
        entity.setId(userId);
        e.add(entity);
        List<RemindEntity> remindEntities = new ArrayList<>();
        switch (type) {
            case "read":
                remindEntities = remindService.getAllReadRemindByUsers(e);
                break;
            case "unread":
            default:
                remindEntities = remindService.getAllUnreadRemindByUsers(e);
                break;
        }
        return CommonResult.success(remindEntities);
    }

}
