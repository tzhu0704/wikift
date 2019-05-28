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
import {Component, OnInit} from '@angular/core';
import {Subscription} from 'rxjs/Subscription';
import {ActivatedRoute} from '@angular/router';
import {TreeMode} from 'tree-ngx';

import {CommonPageModel} from '../../../shared/model/result/page.model';
import {UserService} from '../../../../services/user.service';
import {SpaceService} from '../../../../services/space.service';
import {CookieUtils} from "../../../shared/utils/cookie.util";
import {ArticleService} from "../../../../services/article.service";
import {ArticleModel} from "../../../shared/model/article/article.model";
import {CommentService} from "../../../../services/comment.service";

@Component({
  selector: 'wikift-space-info',
  templateUrl: 'space.info.component.html'
})

export class SpaceInfoComponent implements OnInit {

  // 分页数据
  page: CommonPageModel;
  // 当前页数
  currentPage: number;
  // 空间列表
  public articles;
  public articleCount;
  // 当前空间编码
  private spaceCode;
  // 当前空间信息
  public space;
  public loadArticleBusy: Subscription;
  // 当前空间下文章树形目录提示
  public loadArticleDirectoryTree: Subscription;
  // 当前登录的用户信息
  public loginUserInfo;
  // 当前空间下文章树形目录
  public treeNodes: any;
  // 树形插件配置
  public treeOptions = {
    mode: TreeMode.SingleSelect,
    checkboxes: false,
    alwaysEmitSelected: true
  }
  public article: any;
  // 评论列表
  public comments;

  constructor(private route: ActivatedRoute,
              private userService: UserService,
              private spaceService: SpaceService,
              private articleService: ArticleService,
              private commentService: CommentService) {
    this.page = new CommonPageModel();
    this.loginUserInfo = CookieUtils.getUser();
  }

  ngOnInit() {
    this.route.params.subscribe((params) => this.spaceCode = params.code);
    this.initSpaceInfo();
    this.initSpaceArticleCount();
    this.initArticleList(this.page);
  }

  initSpaceInfo() {
    this.spaceService.getSpaceInfoByCode(this.spaceCode).subscribe(
      result => {
        this.space = result.data;
      }
    );
  }

  initSpaceArticleCount() {
    this.spaceService.getArticleCountByCode(this.spaceCode).subscribe(
      result => {
        this.articleCount = result.data;
      }
    );
  }

  initArticleList(page: CommonPageModel) {
    this.loadArticleDirectoryTree = this.spaceService.getAllArticleBySpace(page, this.spaceCode).subscribe(
      result => {
        this.treeNodes = result.data;
      }
    );
  }

  /**
   * 显示文章数据详情
   * @param event 当前点击数据的唯一标志
   */
  showArticleDetails(event) {
    let id = event[0];
    const params = new ArticleModel();
    params.id = id;
    this.loadArticleBusy = this.articleService.info(params).subscribe(
      result => {
        this.article = result.data;
        this.initComments();
      }
    );
  }

  initComments() {
    this.commentService.getAllCommentByArticle(this.article.id, new CommonPageModel()).subscribe(
      result => {
        this.comments = result.data.content;
        this.page = CommonPageModel.getPage(result.data);
        this.currentPage = this.page.number;
      }
    );
  }

}
