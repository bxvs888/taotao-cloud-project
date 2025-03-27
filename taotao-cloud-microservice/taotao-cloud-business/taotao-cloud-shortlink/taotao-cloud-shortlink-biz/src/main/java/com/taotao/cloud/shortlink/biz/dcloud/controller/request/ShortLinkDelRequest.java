package com.taotao.cloud.shortlink.biz.dcloud.controller.request;

import lombok.Data;
import lombok.experimental.*;

/**
 * @Description
 * @Author:刘森飚
 **/

@Data
public class ShortLinkDelRequest {


    /**
     * 组
     */
    private Long groupId;

    /**
     * 映射id
     */
    private Long mappingId;


    /**
     * 短链码
     */
    private String code;

}
