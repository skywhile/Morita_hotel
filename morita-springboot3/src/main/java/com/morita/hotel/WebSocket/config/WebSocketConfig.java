package com.morita.hotel.WebSocket.config;

import com.morita.hotel.DialogueServiceFactory;
import com.morita.hotel.config.RagConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@Import({RagConfig.class, DialogueServiceFactory.class}) // 导入AI配置
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
