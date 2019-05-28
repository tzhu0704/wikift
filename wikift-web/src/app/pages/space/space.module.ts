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
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { ModalModule } from 'ngx-bootstrap/modal';
import { BusyModule, BusyConfig } from 'angular2-busy';

import { SpaceComponent } from './space.component';

import { ArticleService } from '../../../services/article.service';
import { UserService } from '../../../services/user.service';
import { SpaceService } from '../../../services/space.service';
import { WikiftGroupModule } from '../../shared/directives/wikift-group/wikift-group.module';
import {SafeImgHtml} from "../../shared/pipe/SafeImgHtml";

export function busyConfigFactory() {
    return new BusyConfig({
        message: '数据加载中, 请稍候...',
        backdrop: true,
    });
}

const HOME_ROUTES: Routes = [
    { path: '', component: SpaceComponent }
];

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        BusyModule,
        TooltipModule.forRoot(),
        TabsModule.forRoot(),
        PaginationModule.forRoot(),
        RouterModule.forChild(HOME_ROUTES),
        WikiftGroupModule
    ],
    exports: [],
    declarations: [
        SpaceComponent,
        SafeImgHtml
    ],
    providers: [
        ArticleService,
        UserService,
        SpaceService,
        {
            provide: BusyConfig,
            useFactory: busyConfigFactory
        }
    ],
})
export class SpaceModule { }
