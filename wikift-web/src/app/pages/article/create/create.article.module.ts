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
import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {RouterModule, Routes} from '@angular/router';
import {ModalModule} from 'ngx-bootstrap/modal';
import {ArchwizardModule} from 'ng2-archwizard';
import {AlertModule} from 'ngx-bootstrap/alert';
import {BusyConfig, BusyModule} from 'angular2-busy';
import {SelectModule} from 'angular2-select';
import {ToastyModule} from 'ng2-toasty';
import {PaginationModule} from 'ngx-bootstrap/pagination';
import {TooltipModule} from 'ngx-bootstrap/tooltip';

import {CreateArticleComponent} from './create.article.component';

import {ArticleService} from '../../../../services/article.service';
import {WikiftEditorModule} from '../../../shared/directives/wikift-editor/wikift-editor.module';
import {ArticleTypeService} from '../../../../services/article.type.service';
import {ArticleTagService} from '../../../../services/article.tag.service';
import {SpaceService} from '../../../../services/space.service';

const CREATE_ARTICLE_ROUTES: Routes = [
  {path: '', component: CreateArticleComponent}
];

export function busyConfigFactory() {
  return new BusyConfig({
    message: '数据加载中, 请稍候...',
    minDuration: 1000,
    backdrop: true,
  });
}

@NgModule({
  imports: [
    WikiftEditorModule,
    CommonModule,
    ArchwizardModule,
    FormsModule,
    BusyModule,
    SelectModule,
    ToastyModule.forRoot(),
    ModalModule.forRoot(),
    AlertModule.forRoot(),
    TooltipModule.forRoot(),
    PaginationModule.forRoot(),
    RouterModule.forChild(CREATE_ARTICLE_ROUTES)
  ],
  exports: [],
  declarations: [
    CreateArticleComponent
  ],
  providers: [
    ArticleService,
    ArticleTypeService,
    ArticleTagService,
    SpaceService,
    {
      provide: BusyConfig,
      useFactory: busyConfigFactory
    }
  ],
})
export class CreateArticleModule {
}
