package com.taotao.cloud.shortlink.biz.dcloud.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
 * @author 刘森飚
 * @since 2023-02-09
 */


@Data
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class UseTrafficRequest {

    /**
     * 账号
     */
    private Long accountNo;

    /**
     * 业务id, 短链码
     */
    private String bizId;
}
