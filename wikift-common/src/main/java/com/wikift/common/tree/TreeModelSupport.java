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
package com.wikift.common.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * <p> TreeModelSupport </p>
 * <p> Description : TreeModelSupport </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-05-28 17:02 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TreeModelSupport {

    private Long id; // 数据唯一标志
    private String name; // 数据显示名称
    private String url; // 数据访问地址
    private Integer sorted; // 数据排序
    private Boolean newd; // 数据新旧标志
    private String icon; // 数据图标
    private Boolean checked = false; // 是否选中
    List<TreeModelSupport> children; // 子数据
    private Long item;
//    private TreeModelItemSupport item;

}
