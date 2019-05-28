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

import com.wikift.model.enums.MessageEnums;
import com.wikift.model.result.CommonResult;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 验证工具类
 *
 * @author qianmoQ
 */
public class ValidateUtils {

    /**
     * 抽取验证错误的信息 TODO: 后期使用bean封装
     *
     * @param result 错误信息
     * @return 错误集合
     */
    public static Map extractValidate(BindingResult result) {
        Map<String, Object> error = new HashMap<>();
        List<FieldError> allErrors = result.getFieldErrors();
        error.put("count", allErrors.size());
        List<Map<String, Object>> fields = new ArrayList<>();
        allErrors.forEach(v -> {
            Map<String, Object> field = new HashMap<>();
            field.put("field", v.getField());
            field.put("message", v.getDefaultMessage());
            fields.add(field);
        });
        error.put("error", fields);
        return error;
    }

    /**
     * 参数为空错误 TODO: 后期使用bean封装
     *
     * @return 错误枚举
     */
    public static MessageEnums extractNull() {
        return MessageEnums.PARAMS_NOT_NULL;
    }

    /**
     * 校验参数是否为空
     *
     * @param object  校验的参数
     * @param message 回显的数据
     * @return 校验状态
     */
    public static CommonResult validateEmpty(Object object, MessageEnums message) {
        if (ObjectUtils.isEmpty(object)) {
            return CommonResult.error(message);
        }
        return CommonResult.success();
    }

}
