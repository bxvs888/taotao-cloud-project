package com.taotao.cloud.payment.biz.daxpay.single.service.controller.allocation;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import com.taotao.cloud.payment.biz.daxpay.service.bo.allocation.AllocGroupResultBo;
import com.taotao.cloud.payment.biz.daxpay.service.param.allocation.group.AllocGroupBindParam;
import com.taotao.cloud.payment.biz.daxpay.service.param.allocation.group.AllocGroupParam;
import com.taotao.cloud.payment.biz.daxpay.service.param.allocation.group.AllocGroupQuery;
import com.taotao.cloud.payment.biz.daxpay.service.param.allocation.group.AllocGroupUnbindParam;
import com.taotao.cloud.payment.biz.daxpay.service.bo.allocation.AllocGroupReceiverResultBo;
import com.taotao.cloud.payment.biz.daxpay.service.service.allocation.AllocGroupService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 分账组
 * @author xxm
 * @since 2024/4/2
 */
@Validated
@Tag(name = "分账组")
@RestController
@RequestMapping("/allocation/group")
@RequestGroup(groupCode = "AllocGroup", groupName = "分账组", moduleCode = "Alloc", moduleName = "分账管理" )
@RequiredArgsConstructor
public class AllocGroupController {
    private final AllocGroupService allocGroupService;

    @RequestPath("分页")
    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<AllocGroupResultBo>> page(PageParam pageParam, AllocGroupQuery query){
        return Res.ok(allocGroupService.page(pageParam,query));
    }

    @RequestPath("查询详情")
    @Operation(summary = "查询详情")
    @GetMapping("/findById")
    public Result<AllocGroupResultBo> findById(Long id){
        return Res.ok(allocGroupService.findById(id));
    }

    @RequestPath("编码是否存在")
    @Operation(summary = "编码是否存在")
    @GetMapping("/existsByGroupNo")
    public Result<Boolean> existsByGroupNo(String groupNo, String appId){
        return Res.ok(allocGroupService.existsByGroupNo(groupNo, appId));
    }

    @RequestPath("查询分账接收方信息")
    @Operation(summary = "查询分账接收方信息")
    @GetMapping("/findReceiversByGroups")
    public Result<List<AllocGroupReceiverResultBo>> findReceiversByGroups(Long groupId){
        return Res.ok(allocGroupService.findReceiversByGroups(groupId));
    }

    @RequestPath("添加")
    @Operation(summary = "创建")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody AllocGroupParam param){
        allocGroupService.create(param);
        return Res.ok();
    }

    @RequestPath("修改")
    @Operation(summary = "修改")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody AllocGroupParam param){
        allocGroupService.update(param);
        return Res.ok();
    }

    @RequestPath("删除")
    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<Void> delete(Long id){
        allocGroupService.delete(id);
        return Res.ok();
    }

    @RequestPath("批量绑定接收者")
    @Operation(summary = "批量绑定接收者")
    @PostMapping("/bindReceivers")
    public Result<Void> bindReceivers(@RequestBody AllocGroupBindParam param){
        ValidationUtil.validateParam(param);
        allocGroupService.bindReceivers(param);
        return Res.ok();
    }

    @RequestPath("批量取消绑定接收者")
    @Operation(summary = "批量取消绑定接收者")
    @PostMapping("/unbindReceivers")
    public Result<Void> unbindReceivers(@RequestBody AllocGroupUnbindParam param){
        allocGroupService.unbindReceivers(param);
        return Res.ok();
    }

    @RequestPath("取消绑定接收者")
    @Operation(summary = "取消绑定接收者")
    @PostMapping("/unbindReceiver")
    public Result<Void> unbindReceiver(Long receiverId){
        allocGroupService.unbindReceiver(receiverId);
        return Res.ok();
    }

    @RequestPath("修改分账比例")
    @Operation(summary = "修改分账比例")
    @PostMapping("/updateRate")
    public Result<Void> updateRate(Long receiverId, BigDecimal rate){
        allocGroupService.updateRate(receiverId,rate);
        return Res.ok();
    }

    @RequestPath("设置默认分账组")
    @Operation(summary = "设置默认分账组")
    @PostMapping("/setDefault")
    public Result<Void> setDefault(Long id){
        allocGroupService.setUpDefault(id);
        return Res.ok();
    }

    @RequestPath("清除默认分账组")
    @Operation(summary = "清除默认分账组")
    @PostMapping("/clearDefault")
    public Result<Void> clearDefault(Long id){
        allocGroupService.clearDefault(id);
        return Res.ok();
    }
}
