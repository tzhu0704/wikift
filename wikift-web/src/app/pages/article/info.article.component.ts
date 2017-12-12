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
import { Component, OnInit, ViewChild } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { ArticleModel } from '../../../app/shared/model/article/article.model';
import { UserModel } from '../../../app/shared/model/user/user.model';
import { ArticleService } from '../../../services/article.service';
import { CommonResultModel } from '../../shared/model/result/result.model';

@Component({
    selector: 'wikift-article-info',
    templateUrl: 'info.article.component.html'
})

export class InfoArticleComponent implements OnInit {

    // 文章id
    id: number;
    // 文章内容
    public article: ArticleModel;

    constructor(private route: ActivatedRoute,
        private articleService: ArticleService) {
        // 获取页面url传递的id参数
        this.route.params.subscribe((params) => this.id = params.id);
    }

    ngOnInit() {
        const params = new ArticleModel();
        params.id = this.id;
        this.articleService.info(params).subscribe(
            result => { this.article = result.data; }
        );
    }

    initArticleInfo() {

    }

}
