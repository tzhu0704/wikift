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
package com.wikift.model.enums;

/**
 * RepositoryEnums <br/>
 * 描述 : 该类用于校验归属于哪个Repository <br/>
 * 作者 : qianmoQ <br/>
 * 版本 : 1.0 <br/>
 * 创建时间 : 2018-01-18 上午10:26 <br/>
 * 联系作者 : <a href="mailTo:shichengoooo@163.com">qianmoQ</a>
 */
public enum RepositoryEnums {

    ARTICLE,
    SPACE,
    COMMENT,
    USER,
    REMIND;

    /**
     * 枚举是否包含字符串
     *
     * @param type 字符串
     * @return 包含状态
     */
    public static RepositoryEnums get(String type) {
        for (RepositoryEnums repositoryEnums : RepositoryEnums.values()) {
            if (type.equals(repositoryEnums.name())) {
                return repositoryEnums;
            }
        }
        return null;
    }

}
