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
package com.wikift.model.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wikift.model.space.SpaceEntity;
import com.wikift.model.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "article")
@EntityListeners(value = AuditingEntityListener.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties({"articleEntityList"})
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "a_id")
    private Long id;

    @Column(name = "a_title")
    @NotNull
    private String title;

    @Column(name = "a_content")
    @NotNull
    @Size(min = 10)
    private String content;

    private Integer parent; // 父级节点

    @Column(name = "a_create_time")
    @CreatedDate
    private Date createTime;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "users_article_relation",
            joinColumns = @JoinColumn(name = "uar_article_id"),
            inverseJoinColumns = @JoinColumn(name = "uar_user_id"))
    @NotNull
    private UserEntity user;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "article_type_relation",
            joinColumns = @JoinColumn(name = "atr_article_id"),
            inverseJoinColumns = @JoinColumn(name = "atr_article_type_id"))
    @NotNull
    private ArticleTypeEntity articleType;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "article_tag_relation",
            joinColumns = @JoinColumn(name = "atr_article_id"),
            inverseJoinColumns = @JoinColumn(name = "atr_article_tag_id"))
    @Fetch(FetchMode.SUBSELECT)
    private List<ArticleTagEntity> articleTags;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "space_article_relation",
            joinColumns = @JoinColumn(name = "sar_article_id"),
            inverseJoinColumns = @JoinColumn(name = "sar_space_id"))
    @NotNull
    private SpaceEntity space;

    // 该字段为统计字段不与数据库字段映射
    @Column(name = "view_count", insertable = false)
    private Integer viewCount;

    // 该字段为统计字段不与数据库字段映射
    @Column(name = "fabulou_count", insertable = false)
    private Integer fabulouCount;

    // 该字段为统计字段不与数据库字段映射
    @Column(name = "comments_count", insertable = false)
    private Integer commentsCount;

}
