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
package com.wikift.server.security;

import com.wikift.model.enums.RepositoryEnums;
import com.wikift.model.article.ArticleEntity;
import com.wikift.model.comment.CommentEntity;
import com.wikift.model.user.UserEntity;
import com.wikift.support.service.article.ArticleService;
import com.wikift.support.service.comment.CommentService;
import com.wikift.support.service.user.UserService;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * 信息拥有者权限 <br/>
 * 描述 : 改权限用于监控当前信息拥有者是否为当前用户 <br/>
 * 作者 : qianmoQ <br/>
 * 版本 : 1.0 <br/>
 * 创建时间 : 2018-01-18 上午9:45 <br/>
 * 联系作者 : <a href="mailTo:shichengoooo@163.com">qianmoQ</a>
 */
@Component("userSelfPermissionEvaluator")
public class UserSelfPermissionEvaluator implements PermissionEvaluator {

    private static final GrantedAuthority ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");

    @Resource
    private ArticleService articleService;

    @Resource
    private CommentService commentService;

    @Resource
    private UserService userService;

    @Override
    public boolean hasPermission(Authentication auth, Object data, Object permission) {
        if (data instanceof Long && permission instanceof String) {
            // 管理员权限
            if (auth.getAuthorities().contains(ADMIN)) {
                return true;
            }
            // 截取权限信息, 格式为 权限|数据库操作器
            String[] permissionDetail = String.valueOf(permission).split("\\|");
            if ("delete".equals(permissionDetail[0]) || "update".equals(permissionDetail[0])) {
                return isSelf(auth, data, permissionDetail);
            }
        }
        throw new UnsupportedOperationException("第一个参数必须是Integer型, 第二个参数必须为权限加类型集合(delete|class)");
    }

    @Override
    public boolean hasPermission(
            Authentication auth, Serializable targetId, String targetType, Object permission) {
        throw new UnsupportedOperationException();
    }

    /**
     * 校验数据归属
     *
     * @param source  元数据标识(id或者其他主键)
     * @param source1 数据库操作类型(即为操作哪个数据库操作器)
     * @return 归属结果
     */
    private boolean isSelf(Authentication auth, Object source, String... source1) {
        Long primary = (Long) source;
        RepositoryEnums repository = RepositoryEnums.get(source1[1].toUpperCase());
        switch (repository) {
            case ARTICLE:
                ArticleEntity article = articleService.getArticle(primary);
                if (!ObjectUtils.isEmpty(article) && isSelfUser(auth, article.getUser())) {
                    return true;
                }
                return false;
            case COMMENT:
                CommentEntity comment = commentService.getCommentById(primary);
                if (!ObjectUtils.isEmpty(comment) && isSelfUser(auth, comment.getUser())) {
                    return true;
                }
                return false;
            case USER:
                UserEntity entity = userService.getUserById(primary);
                if (!ObjectUtils.isEmpty(entity) && isSelfUser(auth, entity)) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    private boolean isSelfUser(Authentication auth, UserEntity user) {
        return auth.getName().equals(user.getUsername());
    }

}
