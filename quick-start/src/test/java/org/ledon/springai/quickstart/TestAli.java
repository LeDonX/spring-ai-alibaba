package org.ledon.springai.quickstart;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author LeDon
 * @date 2026-04-08 23:59
 */
@SpringBootTest
public class TestAli {
    @Test
    public void testQwen(@Autowired DashScopeChatModel dashScopeChatModel){
        String call = dashScopeChatModel.call("你好，你是谁？");
        System.out.println(call);
    }
}
