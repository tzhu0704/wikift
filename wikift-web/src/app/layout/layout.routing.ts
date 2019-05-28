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
import {RouterModule, Routes} from '@angular/router';
import {LayoutComponent} from './layout.component';

import {AuthGuard} from '../../app/auth/auth.guard';

const LAYOUT_ROUTES: Routes = [
  {
    path: '', component: LayoutComponent, children: [
      {path: '', redirectTo: 'home', pathMatch: 'full'},
      {path: 'home', loadChildren: '../pages/home/home.module#HomeModule'},
      {path: 'article/info/:id', loadChildren: '../pages/article/info/info.article.module#InfoArticleModule'},
      {path: 'article/search', loadChildren: '../pages/search/search.module#SearchModule'},
    ]
  },
  {
    path: '', component: LayoutComponent, canActivate: [AuthGuard], children: [
      {
        path: ':space/article/create',
        loadChildren: '../pages/article/create/create.article.module#CreateArticleModule'
      },
      {path: 'article/:id/editor', loadChildren: '../pages/article/editor.article.module#EditorArticleModule'},
      {
        path: 'article/:id/history/:articleHistoryVersion/contrast',
        loadChildren: '../pages/article/history/contrast/article.history.constrast.module#ArticleHistoryConstrastModule'
      },
      {path: 'account/:username/info', loadChildren: '../pages/user/info/user.info.module#UserInfoModule'},
      {
        path: 'account/:username/users/following',
        loadChildren: '../pages/user/followed/user.followed.module#UserFollowedModule'
      },
      {path: 'space', loadChildren: '../pages/space/space.module#SpaceModule'},
      {path: 'space/create', loadChildren: '../pages/space/create/space.create.module#SpaceCreateModule'},
      {path: 'space/:code/display', loadChildren: '../pages/space/info/space.info.module#SpaceInfoModule'},
    ]
  },
  // {
  //   path: '', component: SpaceLayoutComponent, canActivate: [AuthGuard], children: [
  //     {path: 'space', loadChildren: '../pages/space/space.module#SpaceModule'},
  //     {path: 'space/:code/display', loadChildren: '../pages/space/info/space.info.module#SpaceInfoModule'},
  //   ]
  // },
  {
    path: 'user', children: [
      {path: 'login', loadChildren: '../pages/user/login/user.login.module#UserLoginModule'},
      {path: 'register', loadChildren: '../pages/user/register/user.register.module#UserRegisterModule'},
    ]
  }
];

export const LayoutRouting = RouterModule.forChild(LAYOUT_ROUTES);
