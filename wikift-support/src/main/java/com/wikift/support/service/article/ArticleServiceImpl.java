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
package com.wikift.support.service.article;

import com.wikift.common.tree.TreeModelSupport;
import com.wikift.common.utils.ValidateUtils;
import com.wikift.model.article.ArticleEntity;
import com.wikift.model.article.ArticleHistoryEntity;
import com.wikift.model.counter.CounterEntity;
import com.wikift.model.enums.MessageEnums;
import com.wikift.model.enums.OrderEnums;
import com.wikift.model.result.CommonResult;
import com.wikift.model.space.SpaceEntity;
import com.wikift.support.repository.article.ArticleHistoryRepository;
import com.wikift.support.repository.article.ArticleRepository;
import com.wikift.support.repository.article.ArticleRepositorySenior;
import com.wikift.support.service.space.SpaceService;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(value = "articleService")
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository repository;

    @Autowired
    private ArticleHistoryRepository articleHistoryRepository;

    @Autowired
    private SpaceService spaceService;

    @Override
    public ArticleEntity save(ArticleEntity entity) {
        return repository.save(entity);
    }

    @Override
    public ArticleEntity update(ArticleEntity entity) {
        ArticleHistoryEntity historyEntity = new ArticleHistoryEntity();
        ArticleEntity source = this.getArticle(entity.getId());
        historyEntity.setId(0L);
        historyEntity.setContent(source.getContent());
        historyEntity.setUser(entity.getUser());
        historyEntity.setArticle(source);
        historyEntity.setVersion(String.valueOf(new Date().getTime()));
        // 存储文章的修改历史
        articleHistoryRepository.save(historyEntity);
        return repository.save(entity);
    }

    @Override
    public Page<ArticleEntity> findAll(OrderEnums order, Pageable pageable) {
        switch (order) {
            case VIEW:
                return repository.findAllOrderByViewCount(pageable);
            case FABULOU:
                return repository.findAllOrderByFabulouCount(pageable);
            case NATIVE_CREATE_TIME:
            default:
                return repository.findAllOrderByCreateTime(pageable);
        }
    }

    @Override
    public CommonResult getAllArticleBySpace(String code, Pageable pageable) {
        // 校验传递参数
        CommonResult validate = ValidateUtils.validateEmpty(code, MessageEnums.PARAMS_NOT_NULL);
        if (validate.getCode() > 0) {
            return validate;
        }
        // 校验数据是否存在
        SpaceEntity space = this.spaceService.getSpaceInfoByCode(code);
        validate = ValidateUtils.validateEmpty(space, MessageEnums.SPACE_NOT_FOUND);
        if (validate.getCode() > 0) {
            return validate;
        }
        // 获取所有数据集合, 封装树形数据
        List<ArticleEntity> entities = IterableUtils.toList(this.repository.findAllBySpace(space));
        return CommonResult.success(this.getChildren(-1L, entities));
    }

    @Override
    public Page<ArticleEntity> getMyArticles(Long userId, Pageable pageable) {
        return repository.findAllToUserAndCreateTime(userId, pageable);
    }

    @Override
    public Page<ArticleEntity> getAllByTagAndCreateTime(Long tagId, Pageable pageable) {
        return repository.findAllByTagAndCreateTime(tagId, pageable);
    }

    @Override
    @Transactional
    public ArticleEntity getArticle(Long id) {
        // 设置文章浏览量
        repository.viewArticle(id, 1);
        return repository.findById(id);
    }

    @Override
    public Long delete(Long id) {
        repository.delete(id);
        return id;
    }

    @Override
    public List<ArticleEntity> findTopByUserEntityAndCreateTime(String username) {
        return repository.findTopByUserEntityAndCreateTime(username);
    }

    @Override
    public Integer fabulousArticle(Integer userId, Integer articleId) {
        return repository.fabulousArticle(userId, articleId);
    }

    @Override
    public Integer unFabulousArticle(Integer userId, Integer articleId) {
        return repository.unFabulousArticle(userId, articleId);
    }

    @Override
    public Integer fabulousArticleExists(Integer userId, Integer articleId) {
        return repository.findFabulousArticleExists(userId, articleId);
    }

    @Override
    public Integer fabulousArticleCount(Integer articleId) {
        return repository.findFabulousArticleCount(articleId);
    }

    @Override
    public Integer viewArticle(Integer userId, Integer articleId, Integer viewCount, String viewDevice) {
        // 查询是否当前设备是否存在于数据库中
        Integer deviceViewCount = repository.findViewArticleByDevice(userId, articleId, viewDevice);
        if (!ObjectUtils.isEmpty(deviceViewCount) && deviceViewCount > 0) {
            // 如果该设备的数据存在数据库中, 则将设备原有数据与现有数据增加
            viewCount = deviceViewCount + viewCount;
            return repository.updateViewArticle(userId, articleId, viewCount, viewDevice);
        }
        return repository.viewArticle(userId, articleId, viewCount, viewDevice);
    }

    @Override
    public Integer viewArticleCount(Integer userId, Integer articleId) {
        return repository.findViewArticle(userId, articleId);
    }

    @Override
    public ArticleEntity getArticleInfoById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<CounterEntity> getArticleViewByCreateTimeAndTop7(Long articleId) {
        List<CounterEntity> counters = new ArrayList<>();
        repository.findArticleViewByCreateTimeAndTop7(articleId).forEach(v -> counters.add(new CounterEntity(v[0], v[1])));
        return counters;
    }

    @Autowired
    private ArticleRepositorySenior articleRepositorySenior;

    @Override
    public Page<ArticleEntity> search(Long tagId, String articleTitle, Long spaceId, Long userId, Pageable pageable) {
        return articleRepositorySenior.search(tagId, articleTitle, spaceId, userId, pageable);
    }

    /**
     * 获取子节点数据
     *
     * @param id      当前父节点标志
     * @param entitys 数据集合
     * @return 树形结构数据
     */
    public List<TreeModelSupport> getChildren(Long id, List<ArticleEntity> entitys) {
        // 子数据存储器
        List<TreeModelSupport> childrens = new ArrayList<>();
        for (ArticleEntity entity : entitys) {
            // 遍历所有节点,将所有数据的父id与传过来的根节点的id比较,或者-1.相等说明: 为该根节点的子节点
            if (entity.getParent().equals(id)) {
                TreeModelSupport support = new TreeModelSupport();
                support.setId(entity.getId());
                support.setName(entity.getTitle());
//                TreeModelItemSupport item = TreeModelItemSupport.buildNew();
//                item.setPhrase(entity.getId());
//                support.setItem(item);
                support.setItem(entity.getId());
                childrens.add(support);
            }
        }
        // 递归遍历数据填充树形结构
        for (TreeModelSupport support : childrens) {
            support.setChildren(getChildren(support.getId(), entitys));
        }
        // 如果节点下没有子节点,返回一个空List(递归退出),暂时不做任何操作,直接递归退出
        if (childrens.size() == 0) {
            return null;
//            return new ArrayList<>();
        }
        return childrens;
    }

}
