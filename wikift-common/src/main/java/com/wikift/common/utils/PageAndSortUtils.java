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
package com.wikift.common.utils;

import com.wikift.model.enums.OrderEnums;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 分页排序工具
 */
public class PageAndSortUtils {

    public static Pageable getPage(Integer page, Integer size) {
        return new PageRequest(page, size);
    }

    public static Pageable getPageAndSortAndCreateTimeByDESC(Integer page, Integer size) {
        return new PageRequest(page, size, new Sort(Sort.Direction.DESC, "createTime"));
    }

    public static Pageable getPageAndSortAndCreateTimeByDESCAndNativeQuery(Integer page, Integer size, OrderEnums order) {
        return new PageRequest(page, size, new Sort(Sort.Direction.DESC, order.getValue()));
    }

}
