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
import com.wikift.model.article.ArticleEntity;
import com.wikift.model.result.CommonResult;
import com.wikift.support.service.article.ArticleHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * ArticleHistoryController <br/>
 * 描述 : ArticleHistoryController <br/>
 * 作者 : qianmoQ <br/>
 * 版本 : 1.0 <br/>
 * 创建时间 : 2018-02-02 下午3:21 <br/>
 * 联系作者 : <a href="mailTo:shichengoooo@163.com">qianmoQ</a>
 */
@RestController
@RequestMapping(value = "${wikift.api.path}")
public class ArticleHistoryController {

    @Autowired
    private ArticleHistoryService articleHistoryService;

    @PreAuthorize("hasPermission(#articleId, 'update|article')")
    @RequestMapping(value = "article/history/{articleId}", method = RequestMethod.GET)
    CommonResult getByArticle(@PathVariable(value = "articleId") Long articleId) {
        Assert.notNull(articleId, MessageEnums.PARAMS_NOT_NULL.getValue());
        ArticleEntity entity = new ArticleEntity();
        entity.setId(articleId);
        return CommonResult.success(articleHistoryService.getByArticle(entity));
    }

    @PreAuthorize("hasAuthority(('USER')) && hasPermission(#articleId, 'update|article')")
    @RequestMapping(value = "article/history/{articleId}/{version}", method = RequestMethod.GET)
    CommonResult getArticleHistoryInfomation(@PathVariable(value = "articleId") Long articleId,
                                             @PathVariable(value = "version") String version) {
        Assert.notNull(articleId, MessageEnums.PARAMS_NOT_NULL.getValue());
        Assert.notNull(version, MessageEnums.PARAMS_NOT_NULL.getValue());
        return CommonResult.success(articleHistoryService.getByVersionAndArticleId(version, articleId));
    }

    @PreAuthorize("hasAuthority(('USER')) && hasPermission(#articleId, 'update|article')")
    @RequestMapping(value = "article/history/{articleId}/{version}/restore", method = RequestMethod.POST)
    CommonResult restoreArticleHistoryInfomation(@PathVariable(value = "articleId") Long articleId,
                                             @PathVariable(value = "version") String version) {
        Assert.notNull(articleId, MessageEnums.PARAMS_NOT_NULL.getValue());
        Assert.notNull(version, MessageEnums.PARAMS_NOT_NULL.getValue());
        return CommonResult.success(articleHistoryService.restoreVersion(version, articleId));
    }

}
