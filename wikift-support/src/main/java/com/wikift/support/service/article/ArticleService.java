package com.wikift.support.service.article;

import com.wikift.model.enums.OrderEnums;
import com.wikift.model.article.ArticleEntity;
import com.wikift.model.counter.CounterEntity;
import com.wikift.model.result.CommonResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleService {

    /**
     * 保存文章信息
     *
     * @param entity 文章信息
     * @return 保存的文章信息
     */
    ArticleEntity save(ArticleEntity entity);

    /**
     * 更新文章信息
     *
     * @param entity 文章信息
     * @return 文章信息
     */
    ArticleEntity update(ArticleEntity entity);

    /**
     * 查询所有文章
     *
     * @return 文章列表
     */
    Page<ArticleEntity> findAll(OrderEnums order, Pageable pageable);

    /**
     * 根据空间查询当前空间所有文章列表
     *
     * @param code     当前空间编码
     * @param pageable 分页信息
     * @return 当前空间所有文章列表
     */
    CommonResult getAllArticleBySpace(String code, Pageable pageable);

    Page<ArticleEntity> getMyArticles(Long userId, Pageable pageable);

    Page<ArticleEntity> getAllByTagAndCreateTime(Long tagId, Pageable pageable);

    /**
     * 根据文章ID查询文章信息
     *
     * @param id 文章ID
     * @return 文章信息
     */
    ArticleEntity getArticle(Long id);

    /**
     * 删除文章
     *
     * @param id 文章ID
     * @return
     */
    Long delete(Long id);

    List<ArticleEntity> findTopByUserEntityAndCreateTime(String username);

    /**
     * 赞文章
     *
     * @param userId    当前赞用户id
     * @param articleId 当前被赞文章id
     * @return 状态
     */
    Integer fabulousArticle(Integer userId, Integer articleId);

    /**
     * 解除赞文章
     *
     * @param userId    当前解除赞用户id
     * @param articleId 当前解除赞文章id
     * @return 状态
     */
    Integer unFabulousArticle(Integer userId, Integer articleId);

    Integer fabulousArticleExists(Integer userId, Integer articleId);

    Integer fabulousArticleCount(Integer articleId);

    Integer viewArticle(Integer userId, Integer articleId, Integer viewCount, String viewDevice);

    Integer viewArticleCount(Integer userId, Integer articleId);

    ArticleEntity getArticleInfoById(Long id);

    List<CounterEntity> getArticleViewByCreateTimeAndTop7(Long articleId);

    Page<ArticleEntity> search(Long tagId, String articleTitle, Long spaceId, Long userId, Pageable pageable);

}
