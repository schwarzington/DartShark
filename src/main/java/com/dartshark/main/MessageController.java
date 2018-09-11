package com.dartshark.main;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.dartshark.data.ActiveWebSocketPlayer;
import com.dartshark.data.ActiveWebSocketPlayerRepository;
import com.dartshark.data.InstantMessage;
import com.dartshark.data.Player;
import com.dartshark.security.CurrentPlayer;

@Controller
public class MessageController {
	private SimpMessageSendingOperations messagingTemplate;
	private ActiveWebSocketPlayerRepository activePlayerRepository;

	@Autowired
	public MessageController(ActiveWebSocketPlayerRepository activePlayerRepository,
			SimpMessageSendingOperations messagingTemplate) {
		this.activePlayerRepository = activePlayerRepository;
		this.messagingTemplate = messagingTemplate;
	}

	@MessageMapping("/im")
	public void im(InstantMessage im, @CurrentPlayer Player currentUser) {
		im.setFrom(currentUser.getEmail());
		this.messagingTemplate.convertAndSendToUser(im.getTo(), "/queue/messages", im);
		this.messagingTemplate.convertAndSendToUser(im.getFrom(), "/queue/messages", im);
	}

	@SubscribeMapping("/users")
	public Collection<ActiveWebSocketPlayer> subscribeMessages() throws Exception {
		return this.activePlayerRepository.findAllActiveUsers();
	}
	
}
