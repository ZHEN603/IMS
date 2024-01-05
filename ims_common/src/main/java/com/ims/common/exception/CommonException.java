package com.ims.common.exception;

import com.ims.common.entity.ResultCode;
import lombok.Data;

@Data
public class CommonException extends Exception {
    private ResultCode resultCode;
    public CommonException(ResultCode resultCode){
        this.resultCode = resultCode;
    }
}
