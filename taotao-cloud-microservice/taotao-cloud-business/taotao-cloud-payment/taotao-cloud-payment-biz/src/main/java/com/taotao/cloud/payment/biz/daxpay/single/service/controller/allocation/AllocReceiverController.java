package com.taotao.cloud.payment.biz.daxpay.single.service.controller.allocation;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import com.taotao.cloud.payment.biz.daxpay.core.param.allocation.receiver.AllocReceiverAddParam;
import com.taotao.cloud.payment.biz.daxpay.core.param.allocation.receiver.AllocReceiverRemoveParam;
import com.taotao.cloud.payment.biz.daxpay.service.bo.allocation.AllocReceiverResultBo;
import com.taotao.cloud.payment.biz.daxpay.service.param.allocation.receiver.AllocReceiverQuery;
import com.taotao.cloud.payment.biz.daxpay.service.service.allocation.AllocReceiverService;
import com.taotao.cloud.payment.biz.daxpay.service.service.assist.PaymentAssistService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分账接收方控制器
 * @author xxm
 * @since 2024/3/28
 */
@Validated
@Tag(name = "分账接收方控制器")
@RestController
@RequestGroup(groupCode = "AllocReceiver", groupName = "分账接收方", moduleCode = "Alloc", moduleName = "分账管理" )
@RequestMapping("/allocation/receiver")
@RequiredArgsConstructor
public class AllocReceiverController {

    private final AllocReceiverService receiverService;
    private final PaymentAssistService paymentAssistService;

    @RequestPath("分页")
    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<AllocReceiverResultBo>> page(PageParam pageParam, AllocReceiverQuery query){
        return Res.ok(receiverService.page(pageParam, query));
    }

    @RequestPath("查询详情")
    @Operation(summary = "查询详情")
    @GetMapping("/findById")
    public Result<AllocReceiverResultBo> findById(Long id){
        return Res.ok(receiverService.findById(id));
    }

    @RequestPath("编码是否存在")
    @Operation(summary = "编码是否存在")
    @GetMapping("/existsByReceiverNo")
    public Result<Boolean> existsByReceiverNo(@NotBlank(message = "接收者编号必填") String receiverNo,@NotBlank(message = "商户应用ID必填") String appId){
        return Res.ok(receiverService.existsByReceiverNo(receiverNo, appId));
    }

    @RequestPath("添加")
    @Operation(summary = "添加")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated AllocReceiverAddParam param){
        paymentAssistService.initMchApp(param.getAppId());
        receiverService.addAndSync(param);
        return Res.ok();
    }

    @RequestPath("删除")
    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody @Validated AllocReceiverRemoveParam param){
        paymentAssistService.initMchApp(param.getAppId());
        receiverService.removeAndSync(param);
        return Res.ok();
    }

    @RequestPath("可分账的通道列表")
    @Operation(summary = "可分账的通道列表")
    @GetMapping("/findChannels")
    public Result<List<LabelValue>> findChannels(){
        return Res.ok(receiverService.findChannels());
    }

    @RequestPath("根据通道获取分账接收方类型")
    @Operation(summary = "根据通道获取分账接收方类型")
    @GetMapping("/findReceiverTypeByChannel")
    public Result<List<LabelValue>> findReceiverTypeByChannel(String channel){
        return Res.ok(receiverService.findReceiverTypeByChannel(channel));
    }
}
