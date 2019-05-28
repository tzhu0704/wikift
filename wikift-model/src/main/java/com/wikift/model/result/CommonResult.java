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
package com.wikift.model.result;

import com.wikift.model.enums.MessageEnums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult<T> {

    private int code;
    private String message;
    private T data;

    private void setCodeMessage(MessageEnums enums) {
        this.code = enums.getCode();
        this.message = enums.getValue();
    }

    public static <T> CommonResult success(T data) {
        CommonResult result = new CommonResult();
        result.setCodeMessage(MessageEnums.SUCCESS);
        result.setData(data);
        return result;
    }

    public static CommonResult success() {
        return success(null);
    }

    public static CommonResult validateError() {
        return validateError(null);
    }

    public static CommonResult validateError(MessageEnums message) {
        CommonResult result = new CommonResult();
        result.setCodeMessage(message);
        result.setData(null);
        return result;
    }

    public static <T> CommonResult validateError(T data) {
        CommonResult result = new CommonResult();
        result.setCodeMessage(MessageEnums.PARAMS_VALIDATE_ERROR);
        result.setData(data);
        return result;
    }

    public static <T> CommonResult error(MessageEnums message, T data) {
        CommonResult result = new CommonResult();
        result.setCodeMessage(message);
        result.setData(data);
        return result;
    }

    public static <T> CommonResult error(MessageEnums message) {
        return error(message, null);
    }

}
