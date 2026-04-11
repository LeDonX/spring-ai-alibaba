package org.ledon.springai.tools;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LeDon
 * @date 2026-04-11 18:02
 */
@RestController
public class ToolsController {
    ChatClient chatClient;

    public ToolsController(ChatClient.Builder chatClientBuilder,
                           ToolService toolService) {
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                        # 角色
                        你是智能航空客服助手
                        ## 要求
                        严禁随意补全或猜测工具调用参数。参数如缺失或语义不准，请不要补充或随意传递，请直接放弃本次工具调用。
                        """
                )
                // 底层会告诉大模型提供了什么工具以及需要的参数
//                .defaultTools(toolService)
                // 动态tool设置
                .defaultToolCallbacks(toolService.getToolCallList(toolService))
                .build();
    }

    // 获得登录态
    @RequestMapping("/login")
    public String login(@RequestParam(value = "id") Long id) {
        StpUtil.login(id);
        return "登录成功！";
    }

    @SaCheckPermission("user.tool")
    @RequestMapping("/tool")
    public String tool(@RequestParam(value = "message", defaultValue = "讲个笑话") String message) {
        return chatClient
                .prompt()
                .options(OllamaChatOptions.builder()
                        .temperature(0.2).build())
                .user(message)
                .call()
                .content();
    }


    // 注销登录态
    @RequestMapping("/logout")
    public String logout() {
        // 当前会话注销登录
        StpUtil.logout();
        return "注销成功！";
    }
}
