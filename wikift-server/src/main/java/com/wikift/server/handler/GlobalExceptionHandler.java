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
package com.wikift.server.handler;

import com.wikift.model.enums.MessageEnums;
import com.wikift.common.utils.ValidateUtils;
import com.wikift.model.result.CommonResult;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求全局错误处理
 *
 * @author qianmoQ
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * 参数验证错误异常
     *
     * @param exception 参数验证失败异常
     * @return 错误结果
     * @throws Exception
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Object MethodArgumentNotValidHandler(MethodArgumentNotValidException exception) throws Exception {
        return CommonResult.validateError(ValidateUtils.extractValidate(exception.getBindingResult()));
    }

    /**
     * 参数错误空异常
     *
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public Object HttpMessageNotReadableHandler() throws Exception {
        return CommonResult.error(ValidateUtils.extractNull());
    }

    /**
     * 415错误异常
     *
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    public Object HttpMediaTypeNotSupportedHandler(HttpMediaTypeNotSupportedException exception) throws Exception {
        Map<String, Object> error = new HashMap<>();
        error.put("message", exception.getMessage());
        return CommonResult.error(MessageEnums.UNSUPPORT_MEDIA_TYPE, error);
    }

}
