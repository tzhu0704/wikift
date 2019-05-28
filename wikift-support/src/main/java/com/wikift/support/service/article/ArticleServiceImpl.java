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

import com.wikift.common.enums.OrderEnums;
import com.wikift.model.article.ArticleEntity;
import com.wikift.model.article.ArticleHistoryEntity;
import com.wikift.model.counter.CounterEntity;
import com.wikift.model.space.SpaceEntity;
import com.wikift.support.repository.article.ArticleHistoryRepository;
import com.wikift.support.repository.article.ArticleRepository;
import com.wikift.support.repository.article.ArticleRepositorySenior;
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
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleHistoryRepository articleHistoryRepository;

    @Override
    public ArticleEntity save(ArticleEntity entity) {
        return articleRepository.save(entity);
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
        return articleRepository.save(entity);
    }

    @Override
    public Page<ArticleEntity> findAll(OrderEnums order, Pageable pageable) {
        switch (order) {
            case VIEW:
                return articleRepository.findAllOrderByViewCount(pageable);
            case FABULOU:
                return articleRepository.findAllOrderByFabulouCount(pageable);
            case NATIVE_CREATE_TIME:
            default:
                return articleRepository.findAllOrderByCreateTime(pageable);
        }
    }

    @Override
    public Page<ArticleEntity> getAllArticleBySpace(Long spaceId, Pageable pageable) {
        SpaceEntity space = new SpaceEntity();
        space.setId(spaceId);
        return articleRepository.findAllBySpace(space, pageable);
    }

    @Override
    public Page<ArticleEntity> getMyArticles(Long userId, Pageable pageable) {
        return articleRepository.findAllToUserAndCreateTime(userId, pageable);
    }

    @Override
    public Page<ArticleEntity> getAllByTagAndCreateTime(Long tagId, Pageable pageable) {
        return articleRepository.findAllByTagAndCreateTime(tagId, pageable);
    }

    @Override
    @Transactional
    public ArticleEntity getArticle(Long id) {
        // 设置文章浏览量
        articleRepository.viewArticle(id, 1);
        return articleRepository.findById(id);
    }

    @Override
    public Long delete(Long id) {
        articleRepository.delete(id);
        return id;
    }

    @Override
    public List<ArticleEntity> findTopByUserEntityAndCreateTime(String username) {
        return articleRepository.findTopByUserEntityAndCreateTime(username);
    }

    @Override
    public Integer fabulousArticle(Integer userId, Integer articleId) {
        return articleRepository.fabulousArticle(userId, articleId);
    }

    @Override
    public Integer unFabulousArticle(Integer userId, Integer articleId) {
        return articleRepository.unFabulousArticle(userId, articleId);
    }

    @Override
    public Integer fabulousArticleExists(Integer userId, Integer articleId) {
        return articleRepository.findFabulousArticleExists(userId, articleId);
    }

    @Override
    public Integer fabulousArticleCount(Integer articleId) {
        return articleRepository.findFabulousArticleCount(articleId);
    }

    @Override
    public Integer viewArticle(Integer userId, Integer articleId, Integer viewCount, String viewDevice) {
        // 查询是否当前设备是否存在于数据库中
        Integer deviceViewCount = articleRepository.findViewArticleByDevice(userId, articleId, viewDevice);
        if (!ObjectUtils.isEmpty(deviceViewCount) && deviceViewCount > 0) {
            // 如果该设备的数据存在数据库中, 则将设备原有数据与现有数据增加
            viewCount = deviceViewCount + viewCount;
            return articleRepository.updateViewArticle(userId, articleId, viewCount, viewDevice);
        }
        return articleRepository.viewArticle(userId, articleId, viewCount, viewDevice);
    }

    @Override
    public Integer viewArticleCount(Integer userId, Integer articleId) {
        return articleRepository.findViewArticle(userId, articleId);
    }

    @Override
    public ArticleEntity getArticleInfoById(Long id) {
        return articleRepository.findById(id);
    }

    @Override
    public List<CounterEntity> getArticleViewByCreateTimeAndTop7(Long articleId) {
        List<CounterEntity> counters = new ArrayList<>();
        articleRepository.findArticleViewByCreateTimeAndTop7(articleId).forEach(v -> counters.add(new CounterEntity(v[0], v[1])));
        return counters;
    }

    @Autowired
    private ArticleRepositorySenior articleRepositorySenior;

    @Override
    public Page<ArticleEntity> search(Long tagId, String articleTitle, Long spaceId, Long userId, Pageable pageable) {
        return articleRepositorySenior.search(tagId, articleTitle, spaceId, userId, pageable);
    }

}
