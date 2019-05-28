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
package com.wikift.support.repository.article;

import com.wikift.model.article.ArticleEntity;
import com.wikift.model.space.SpaceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ArticleRepository extends PagingAndSortingRepository<ArticleEntity, Long> {

    /**
     * 根据文章浏览量查询文章
     *
     * @param pageable 分页信息
     * @return 文章列表
     */
    @Query(value = "SELECT a.a_id, a.parent, a.a_title, a.a_content, a.a_create_time, IFNULL(SUM(uavr.uavr_view_count) , 0) AS view_count, IFNULL(COUNT(DISTINCT uafr.uafr_user_id), 0) AS fabulou_count, uar.uar_user_id, atr.atr_article_type_id, sar.sar_space_id, IFNULL(c.comments_count, 0) AS comments_count " +
            "FROM article AS a " +
            "LEFT OUTER JOIN users_article_relation AS uar ON a.a_id = uar.uar_article_id " +
            "LEFT OUTER JOIN article_type_relation AS atr ON a.a_id = atr.atr_article_id " +
            "LEFT OUTER JOIN users_article_view_relation AS uavr ON a.a_id = uavr.uavr_article_id " +
            "LEFT OUTER JOIN users_article_fabulous_relation AS uafr ON a.a_id = uafr.uafr_article_id " +
            "LEFT OUTER JOIN space_article_relation AS sar ON a.a_id = sar.sar_article_id " +
            "LEFT OUTER JOIN space AS s ON s.s_id = sar.sar_space_id " +
            "LEFT OUTER JOIN (SELECT car.car_article_id, count(car.car_comments_id) AS comments_count FROM comments_article_relation AS car GROUP BY car.car_article_id ) AS c ON a.a_id = c.car_article_id " +
            "WHERE s.s_private = FALSE " +
            "GROUP BY a_id, uar.uar_user_id, atr.atr_article_type_id, sar.sar_space_id " +
//            "ORDER BY ?#{#pageable}",
            "ORDER BY SUM(uavr.uavr_view_count) DESC \n#pageable\n",
            countQuery = "SELECT COUNT(a.a_id) FROM article AS a",
            nativeQuery = true)
    Page<ArticleEntity> findAllOrderByViewCount(Pageable pageable);

    /**
     * 根据文章点赞量查询文章
     *
     * @param pageable 分页信息
     * @return 文章列表
     */
    @Query(value = "SELECT a.a_id, a.parent, a.a_title, a.a_content, a.a_create_time, IFNULL(SUM(uavr.uavr_view_count) , 0) AS view_count, IFNULL(COUNT(DISTINCT uafr.uafr_user_id), 0) AS fabulou_count, uar.uar_user_id, atr.atr_article_type_id, sar.sar_space_id, IFNULL(c.comments_count, 0) AS comments_count " +
            "FROM article AS a " +
            "LEFT OUTER JOIN users_article_relation AS uar ON a.a_id = uar.uar_article_id " +
            "LEFT OUTER JOIN article_type_relation AS atr ON a.a_id = atr.atr_article_id " +
            "LEFT OUTER JOIN users_article_view_relation AS uavr ON a.a_id = uavr.uavr_article_id " +
            "LEFT OUTER JOIN users_article_fabulous_relation AS uafr ON a.a_id = uafr.uafr_article_id " +
            "LEFT OUTER JOIN space_article_relation AS sar ON a.a_id = sar.sar_article_id " +
            "LEFT OUTER JOIN space AS s ON s.s_id = sar.sar_space_id " +
            "LEFT OUTER JOIN (SELECT car.car_article_id, count(car.car_comments_id) AS comments_count FROM comments_article_relation AS car GROUP BY car.car_article_id ) AS c ON a.a_id = c.car_article_id " +
            "WHERE s.s_private = FALSE " +
            "GROUP BY a_id, uar.uar_user_id, atr.atr_article_type_id, sar.sar_space_id " +
            "ORDER BY COUNT(uafr.uafr_user_id) DESC \n#pageable\n",
            countQuery = "SELECT COUNT(a.a_id) FROM article AS a",
            nativeQuery = true)
    Page<ArticleEntity> findAllOrderByFabulouCount(Pageable pageable);

    /**
     * 根据文章创建时间查询文章
     *
     * @param pageable 分页信息
     * @return 文章列表
     */
    @Query(value = "SELECT a.a_id, a.parent, a.a_title, a.a_content, a.a_create_time, IFNULL(SUM(uavr.uavr_view_count) , 0) AS view_count, IFNULL(COUNT(DISTINCT uafr.uafr_user_id), 0) AS fabulou_count, uar.uar_user_id, atr.atr_article_type_id, sar.sar_space_id, IFNULL(c.comments_count, 0) AS comments_count " +
            "FROM article AS a " +
            "LEFT OUTER JOIN users_article_relation AS uar ON a.a_id = uar.uar_article_id " +
            "LEFT OUTER JOIN article_type_relation AS atr ON a.a_id = atr.atr_article_id " +
            "LEFT OUTER JOIN users_article_view_relation AS uavr ON a.a_id = uavr.uavr_article_id " +
            "LEFT OUTER JOIN users_article_fabulous_relation AS uafr ON a.a_id = uafr.uafr_article_id " +
            "LEFT OUTER JOIN space_article_relation AS sar ON a.a_id = sar.sar_article_id " +
            "LEFT OUTER JOIN space AS s ON s.s_id = sar.sar_space_id " +
            "LEFT OUTER JOIN (SELECT car.car_article_id, count(car.car_comments_id) AS comments_count FROM comments_article_relation AS car GROUP BY car.car_article_id ) AS c ON a.a_id = c.car_article_id " +
            "WHERE s.s_private = FALSE " +
            "GROUP BY a_id, uar.uar_user_id, atr.atr_article_type_id, sar.sar_space_id " +
            "ORDER BY a.a_create_time DESC \n#pageable\n",
            countQuery = "SELECT COUNT(a.a_id) FROM article AS a",
            nativeQuery = true)
    Page<ArticleEntity> findAllOrderByCreateTime(Pageable pageable);

    /**
     * 根据文章标签查询文章
     *
     * @param pageable 分页信息
     * @return 文章列表
     */
    @Query(value = "SELECT a.a_id, a.parent, a.a_title, a.a_content, a.a_create_time, IFNULL(SUM(uavr.uavr_view_count) , 0) AS view_count, IFNULL(COUNT(DISTINCT uafr.uafr_user_id), 0) AS fabulou_count, uar.uar_user_id, atr.atr_article_type_id, sar.sar_space_id, IFNULL(c.comments_count, 0) AS comments_count " +
            "FROM article AS a " +
            "LEFT OUTER JOIN users_article_relation AS uar ON a.a_id = uar.uar_article_id " +
            "LEFT OUTER JOIN article_type_relation AS atr ON a.a_id = atr.atr_article_id " +
            "LEFT OUTER JOIN users_article_view_relation AS uavr ON a.a_id = uavr.uavr_article_id " +
            "LEFT OUTER JOIN users_article_fabulous_relation AS uafr ON a.a_id = uafr.uafr_article_id " +
            "LEFT OUTER JOIN space_article_relation AS sar ON a.a_id = sar.sar_article_id " +
            "LEFT OUTER JOIN space AS s ON s.s_id = sar.sar_space_id " +
            "LEFT OUTER JOIN (SELECT car.car_article_id, count(car.car_comments_id) AS comments_count FROM comments_article_relation AS car GROUP BY car.car_article_id ) AS c ON a.a_id = c.car_article_id " +
            "LEFT OUTER JOIN article_tag_relation AS atr1 ON a.a_id = atr1.atr_article_id " +
            "WHERE s.s_private = FALSE " +
            "AND atr1.atr_article_tag_id = ?1 " +
            "GROUP BY a_id, uar.uar_user_id, atr.atr_article_type_id, sar.sar_space_id " +
            "ORDER BY a.a_create_time DESC \n#pageable\n",
            countQuery = "SELECT COUNT(a.a_id) FROM article AS a",
            nativeQuery = true)
    Page<ArticleEntity> findAllByTagAndCreateTime(Long tagId, Pageable pageable);

    @Query(value = "SELECT a.a_id, a.parent, a.a_title, a.a_content, a.a_create_time, IFNULL(SUM(uavr.uavr_view_count) , 0) AS view_count, IFNULL(COUNT(DISTINCT uafr.uafr_user_id), 0) AS fabulou_count, uar.uar_user_id, atr.atr_article_type_id, sar.sar_space_id, IFNULL(c.comments_count, 0) AS comments_count " +
            "FROM article AS a " +
            "LEFT OUTER JOIN users_article_relation AS uar ON a.a_id = uar.uar_article_id " +
            "LEFT OUTER JOIN article_type_relation AS atr ON a.a_id = atr.atr_article_id " +
            "LEFT OUTER JOIN users_article_view_relation AS uavr ON a.a_id = uavr.uavr_article_id " +
            "LEFT OUTER JOIN users_article_fabulous_relation AS uafr ON a.a_id = uafr.uafr_article_id " +
            "LEFT OUTER JOIN space_article_relation AS sar ON a.a_id = sar.sar_article_id " +
            "LEFT OUTER JOIN space AS s ON s.s_id = sar.sar_space_id " +
            "LEFT OUTER JOIN (SELECT car.car_article_id, count(car.car_comments_id) AS comments_count FROM comments_article_relation AS car GROUP BY car.car_article_id ) AS c ON a.a_id = c.car_article_id " +
            "WHERE uar.uar_user_id = :userId " +
            "GROUP BY a_id, uar.uar_user_id, atr.atr_article_type_id, sar.sar_space_id " +
            "ORDER BY a.a_create_time " +
            "DESC \n#pageable\n",
            countQuery = "SELECT COUNT(a.a_id) FROM article AS a",
            nativeQuery = true)
    Page<ArticleEntity> findAllToUserAndCreateTime(@Param(value = "userId") Long userId, Pageable pageable);

    @Query(value = "SELECT a.a_id, a.parent, a.a_title, a.a_content, a.a_create_time, IFNULL(SUM(uavr.uavr_view_count) , 0) AS view_count, IFNULL(COUNT(DISTINCT uafr.uafr_user_id), 0) AS fabulou_count, uar.uar_user_id, atr.atr_article_type_id, sar.sar_space_id, IFNULL(c.comments_count, 0) AS comments_count " +
            "FROM article AS a " +
            "LEFT OUTER JOIN users_article_relation AS uar ON a.a_id = uar.uar_article_id " +
            "LEFT OUTER JOIN article_type_relation AS atr ON a.a_id = atr.atr_article_id " +
            "LEFT OUTER JOIN users_article_view_relation AS uavr ON a.a_id = uavr.uavr_article_id " +
            "LEFT OUTER JOIN users_article_fabulous_relation AS uafr ON a.a_id = uafr.uafr_article_id " +
            "LEFT OUTER JOIN space_article_relation AS sar ON a.a_id = sar.sar_article_id " +
            "LEFT OUTER JOIN space AS s ON s.s_id = sar.sar_space_id " +
            "LEFT OUTER JOIN (SELECT car.car_article_id, count(car.car_comments_id) AS comments_count FROM comments_article_relation AS car GROUP BY car.car_article_id ) AS c ON a.a_id = c.car_article_id " +
            "WHERE a.a_id = ?1 " +
            "GROUP BY a_id, uar.uar_user_id, atr.atr_article_type_id, sar.sar_space_id " +
            "ORDER BY SUM(uavr.uavr_view_count) DESC \n#pageable\n",
            countQuery = "SELECT COUNT(a.a_id) FROM article AS a",
            nativeQuery = true)
    ArticleEntity findById(Long articleId);

    /**
     * 根据空间查询所有该空间下的文章
     *
     * @param spaceId 空间id
     * @return 该空间的文章列表
     */
    @Query(value = "SELECT a.a_id, a.a_title, a.a_content, a.a_create_time, IFNULL(SUM(uavr.uavr_view_count) , 0) AS view_count, IFNULL(COUNT(DISTINCT uafr.uafr_user_id), 0) AS fabulou_count, uar.uar_user_id, atr.atr_article_type_id, sar.sar_space_id, IFNULL(c.comments_count, 0) AS comments_count " +
            "FROM article AS a " +
            "LEFT OUTER JOIN users_article_relation AS uar ON a.a_id = uar.uar_article_id " +
            "LEFT OUTER JOIN article_type_relation AS atr ON a.a_id = atr.atr_article_id " +
            "LEFT OUTER JOIN users_article_view_relation AS uavr ON a.a_id = uavr.uavr_article_id " +
            "LEFT OUTER JOIN users_article_fabulous_relation AS uafr ON a.a_id = uafr.uafr_article_id " +
            "LEFT OUTER JOIN space_article_relation AS sar ON a.a_id = sar.sar_article_id " +
            "LEFT OUTER JOIN space AS s ON s.s_id = sar.sar_space_id " +
            "LEFT OUTER JOIN (SELECT car.car_article_id, count(car.car_comments_id) AS comments_count FROM comments_article_relation AS car GROUP BY car.car_article_id ) AS c ON a.a_id = c.car_article_id " +
            "WHERE sar.sar_space_id = :spaceId " +
            "GROUP BY a_id, uar.uar_user_id, atr.atr_article_type_id, sar.sar_space_id " +
            "ORDER BY SUM(uavr.uavr_view_count) DESC \n#pageable\n",
            countQuery = "SELECT COUNT(a.a_id) FROM article AS a",
            nativeQuery = true)
    Page<ArticleEntity> findAllBySpace(@Param("spaceId") Long spaceId, Pageable pageable);

    /**
     * 根据空间信息查询当前空间下所有的文章列表
     *
     * @param space    空间标志
     * @param pageable 分页信息
     * @return 当前空间下所有的文章列表
     */
    Page<ArticleEntity> findAllBySpace(SpaceEntity space, Pageable pageable);

    /**
     * 根据空间信息查询当前空间下所有的文章列表
     *
     * @param space 空间标志
     * @return 当前空间下所有的文章列表
     */
    Iterable<ArticleEntity> findAllBySpace(SpaceEntity space);

    /**
     * 根据用户和时间查询文章信息
     *
     * @param username 用户id
     * @return 文章列表
     */
    @Query(value = "SELECT * FROM users_article_relation AS uar, article AS a, users AS u, article_type AS at, article_type_relation AS atr, space_article_relation AS sar " +
            "WHERE uar.uar_user_id = u.u_id " +
            "AND atr.atr_article_id = a.a_id " +
            "AND atr.atr_article_type_id = at.at_id " +
            "AND uar.uar_article_id = a.a_id " +
            "AND a.a_id = sar.sar_article_id " +
            "AND DATE_SUB(CURDATE() , INTERVAL 7 DAY) <= date(a.a_create_time) " +
            "AND u.u_username = ?1 LIMIT 5", nativeQuery = true)
    List<ArticleEntity> findTopByUserEntityAndCreateTime(String username);

    /**
     * 赞文章
     *
     * @param userId    当前赞用户id
     * @param articleId 当前被赞文章id
     * @return 状态
     */
    @Modifying
    @Query(value = "INSERT INTO users_article_fabulous_relation(uafr_user_id, uafr_article_id) " +
            "VALUE(?1, ?2)",
            nativeQuery = true)
    Integer fabulousArticle(Integer userId, Integer articleId);

    /**
     * 解除赞文章
     *
     * @param userId    当前解除赞用户id
     * @param articleId 当前解除赞文章id
     * @return 状态
     */
    @Modifying
    @Query(value = "DELETE FROM users_article_fabulous_relation " +
            "WHERE uafr_user_id = ?1 " +
            "AND uafr_article_id = ?2",
            nativeQuery = true)
    Integer unFabulousArticle(Integer userId, Integer articleId);

    /**
     * 检查当前文章是否被当前用户赞
     *
     * @param userId    当前赞用户id
     * @param articleId 当前被赞文章id
     * @return 数据总数
     */
    @Query(value = "SELECT count(1) AS count FROM users_article_fabulous_relation " +
            "WHERE uafr_user_id = ?1 " +
            "AND uafr_article_id = ?2",
            nativeQuery = true)
    Integer findFabulousArticleExists(Integer userId, Integer articleId);

    /**
     * 当前文章被赞用户总数
     *
     * @param articleId 当前被赞文章id
     * @return 数据总数
     */
    @Query(value = "SELECT count(1) AS count FROM users_article_fabulous_relation " +
            "WHERE uafr_article_id = ?1",
            nativeQuery = true)
    Integer findFabulousArticleCount(Integer articleId);

    /**
     * 用户浏览文章信息
     *
     * @param userId     当前浏览文章的用户id
     * @param articleId  当前浏览的文章id
     * @param viewCount  浏览文章的总数
     * @param viewDevice 浏览文章的用户设备(用于区分为做bi准备)
     * @return 数据状态
     */
    @Modifying
    @Query(value = "INSERT INTO users_article_view_relation(uavr_user_id, uavr_article_id, uavr_view_count, uavr_view_device) " +
            "VALUES (?1, ?2, ?3, ?4)",
            nativeQuery = true)
    Integer viewArticle(Integer userId, Integer articleId, Integer viewCount, String viewDevice);

    @Modifying
    @Query(value = "INSERT INTO users_article_view_relation(uavr_article_id, uavr_view_count) " +
            "VALUES (?1, ?2)",
            nativeQuery = true)
    Integer viewArticle(Long articleId, Integer viewCount);

    /**
     * 更新文章的访问数据总数
     *
     * @param userId     当前浏览文章的用户id
     * @param articleId  当前浏览的文章id
     * @param viewCount  浏览文章的总数
     * @param viewDevice 浏览文章的用户设备(用于区分为做bi准备)
     * @return
     */
    @Modifying
    @Query(value = "UPDATE users_article_view_relation " +
            "SET uavr_user_id = ?1, uavr_article_id = ?2, uavr_view_count = ?3, uavr_view_device = ?4, create_time = CURRENT_TIMESTAMP " +
            "WHERE uavr_user_id = ?1 " +
            "AND uavr_article_id = ?2 " +
            "AND uavr_view_device = ?4",
            nativeQuery = true)
    Integer updateViewArticle(Integer userId, Integer articleId, Integer viewCount, String viewDevice);

    /**
     * 根据用户访问的设备进行查询访问数据总数
     *
     * @param userId     当前浏览文章的用户id
     * @param articleId  当前浏览的文章id
     * @param viewDevice 浏览文章的用户设备(同一用户使用多设备访问, 标识为多设备)
     * @return 当前用户, 设备对当前文章的访问量总数
     */
    @Query(value = "SELECT SUM(uavr_view_count) FROM users_article_view_relation " +
            "WHERE uavr_user_id = ?1 " +
            "AND uavr_article_id = ?2 " +
            "AND uavr_view_device = ?3",
            nativeQuery = true)
    Integer findViewArticleByDevice(Integer userId, Integer articleId, String viewDevice);

    /**
     * 根据用户访问的设备进行查询访问数据总数
     *
     * @param userId    当前浏览文章的用户id
     * @param articleId 当前浏览的文章id
     * @return 对当前文章的访问量总数
     */
    @Query(value = "SELECT SUM(uavr_view_count) FROM users_article_view_relation " +
            "WHERE uavr_user_id = ?1 " +
            "AND uavr_article_id = ?2",
            nativeQuery = true)
    Integer findViewArticle(Integer userId, Integer articleId);

    /**
     * 文章浏览趋势
     *
     * @param articleId 文章id
     * @return 7天浏览趋势图
     */
    @Query(value = "SELECT a.formatDate AS dataKey, IFNULL(b.viewCount, 0) AS dataValue " +
            "FROM ( " +
            "SELECT curdate() AS formatDate UNION ALL " +
            "SELECT date_sub( curdate( ), INTERVAL 1 DAY ) AS formatDate UNION ALL " +
            "SELECT date_sub( curdate( ), INTERVAL 2 DAY ) AS formatDate UNION ALL " +
            "SELECT date_sub( curdate( ), INTERVAL 3 DAY ) AS formatDate UNION ALL " +
            "SELECT date_sub( curdate( ), INTERVAL 4 DAY ) AS formatDate UNION ALL " +
            "SELECT date_sub( curdate( ), INTERVAL 5 DAY ) AS formatDate UNION ALL " +
            "SELECT date_sub( curdate( ), INTERVAL 6 DAY ) AS formatDate " +
            ") AS a LEFT JOIN ( " +
            "SELECT a.a_id , DATE_FORMAT( uavr.create_time, '%Y-%m-%d' ) AS formatDate, IFNULL(SUM(uavr.uavr_view_count), 0) AS viewCount " +
            "FROM article AS a " +
            "LEFT OUTER JOIN users_article_view_relation AS uavr ON a.a_id = uavr.uavr_article_id " +
            "WHERE a.a_id = ?1 " +
            "GROUP BY formatDate " +
            ") AS b ON a.formatDate = b.formatDate " +
            "ORDER BY a.formatDate",
            nativeQuery = true)
    List<Object[]> findArticleViewByCreateTimeAndTop7(Long articleId);

    /**
     * 根据用户和时间查询?天数据
     *
     * @param username 用户名称
     * @return 文章列表
     */
    @Query(value = "SELECT * FROM users_article_relation AS uar, article AS a, users AS u, article_type AS at, article_type_relation AS atr, space_article_relation AS sar " +
            "WHERE uar.uar_user_id = u.u_id " +
            "AND atr.atr_article_id = a.a_id " +
            "AND atr.atr_article_type_id = at.at_id " +
            "AND uar.uar_article_id = a.a_id " +
            "AND a.a_id = sar.sar_article_id " +
            "AND u.u_username = :username " +
            "AND date_format(a.a_create_time,'%Y-%m-%d') = :timeline ", nativeQuery = true)
    List<ArticleEntity> findAllByUserAndCreateTimeRanger(@Param(value = "username") String username,
                                                         @Param(value = "timeline") String timeline);

}
