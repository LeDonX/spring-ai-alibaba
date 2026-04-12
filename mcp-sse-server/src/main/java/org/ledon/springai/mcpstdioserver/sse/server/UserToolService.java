package org.ledon.springai.mcpstdioserver.sse.server;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class UserToolService {

    Map<String,Double> userScore = Map.of(
            "蝙蝠侠",99.0,
            "超人",2.0,
            "海王",3.0);
    @Tool(description = "获取用户分数")
    public String getScore(String username) {
        if(userScore.containsKey(username)){
            return userScore.get(username).toString();
        }

        return "未检索到当前用户";
    }
}