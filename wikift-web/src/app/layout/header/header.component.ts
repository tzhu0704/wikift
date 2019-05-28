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
import {Component, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {ModalDirective} from 'ngx-bootstrap/modal';
import {Subscription} from 'rxjs/Subscription';
import {FormGroup} from '@angular/forms';
import {ToastyService} from 'ng2-toasty';

import {CookieUtils} from '../../shared/utils/cookie.util';

import {SharedService} from '../../shared/services/shared.service';
import {UserService} from '../../../services/user.service';

import {UserParamModel} from '../../shared/model/param/user.param.model';
import {CommonConfig} from '../../../config/common.config';
import {RemindService} from '../../../services/remind.service';
import {CodeConfig} from '../../../config/code.config';
import {ResultUtils} from '../../shared/utils/result.util';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html'
})
export class HeaderComponent implements OnInit {

  token: String;
  public userInfo;
  maThemeModel = 'green';
  // 用户未读消息, 提示框
  public unreadReminds;
  public unreadRemindsBusy: Subscription;
  // 用户已读消息, 提示框
  public readReminds;
  public readRemindsBusy: Subscription;
  // 文章属性框
  @ViewChild('remindDetial')
  public remindDetial: ModalDirective;
  // 提示消息详情
  public remind;
  // 显示邮箱警告
  public showEmailAlert = true;
  // 用户邮箱
  public userEmail;
  @ViewChild('settingsEmail')
  public settingsEmail: ModalDirective;
  public form: FormGroup;
  // 修改密码
  @ViewChild('changerPassword')
  public changerPassword: ModalDirective;
  public changerPasswordUserInfo;

  setTheme() {
    this.sharedService.setTheme(this.maThemeModel);
  }

  constructor(private sharedService: SharedService,
              private router: Router,
              private userService: UserService,
              private remindService: RemindService,
              private toastyService: ToastyService) {
    sharedService.maThemeSubject.subscribe((value) => {
      this.maThemeModel = value;
    });
  }

  ngOnInit() {
    this.token = CookieUtils.get();
    this.initUserInfo();
    this.changerPasswordUserInfo = new UserParamModel();
  }

  initUserInfo() {
    if (CookieUtils.getUserName() !== '') {
      const param = new UserParamModel();
      param.username = CookieUtils.getUserName();
      this.userService.info(param).subscribe(
        data => {
          this.userInfo = data.data;
          if (this.userInfo.email) {
            this.showEmailAlert = false;
          }
          // 将用户信息存放到cookie中
          CookieUtils.setBy(CommonConfig.AUTH_USER_INFO, JSON.stringify(this.userInfo));
          // 加载用户通知信息
          this.initUnreadRemindByUser();
        }
      );
    }
  }

  initUnreadRemindByUser() {
    this.unreadRemindsBusy = this.remindService.listByUsers(this.userInfo.id, 'unread').subscribe(
      result => {
        this.unreadReminds = result.data;
      }
    );
  }

  initReadRemindByUser() {
    this.readRemindsBusy = this.remindService.listByUsers(this.userInfo.id, 'read').subscribe(
      result => {
        this.readReminds = result.data;
      }
    );
  }

  logout() {
    CookieUtils.clear();
    this.router.navigate(['/user/login']);
  }

  showRemindDetail(value) {
    this.remindDetial.show();
    this.remind = value;
    this.remindService.readRemind(this.remind.id).subscribe(
      result => {
        this.initUnreadRemindByUser();
      }
    );
  }

  unReadRemind() {
    this.unreadReminds = new Array();
    this.initUnreadRemindByUser();
  }

  readRemind() {
    this.readReminds = new Array();
    this.initReadRemindByUser();
  }

  showSettingsEmial() {
    this.changerPassword.hide();
    this.settingsEmail.show();
  }

  settingEmail() {
    this.userService.updateEmail(this.userInfo).subscribe(
      result => {
        if (result.data && result.code === CodeConfig.SUCCESS) {
          this.toastyService.success('您的邮箱已经设置成功!!!');
          this.settingsEmail.hide();
          this.showEmailAlert = false;
        } else {
          this.toastyService.error(ResultUtils.getError(result));
        }
      }
    );
  }

  showChangerPassword() {
    this.changerPassword.show();
  }

  changerPasswordRevice() {
    this.changerPasswordUserInfo.id = this.userInfo.id;
    this.userService.updatePassword(this.changerPasswordUserInfo).subscribe(
      result => {
        if (result.data && result.code === CodeConfig.SUCCESS) {
          this.toastyService.success('您的密码已经修改成功, 下次登录请使用新密码!!!');
          this.changerPassword.hide();
        } else {
          if (result.code > CodeConfig.ERROR_5000) {
            this.toastyService.error(result.msg);
          } else {
            this.toastyService.error(ResultUtils.getError(result));
          }
        }
      }
    );
  }

}
